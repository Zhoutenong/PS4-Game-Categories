"""
从 Metacritic 抓取 PS4 游戏评分，更新 ps4_games.db 的 score 字段。
只处理 score=0 且 name_en 不为空的游戏（约 1639 条）。

用法：
    pip install requests beautifulsoup4
    python fetch_scores.py
"""

import sqlite3
import re
import time
import random
import requests
from bs4 import BeautifulSoup

DB_PATH = "../app/src/main/assets/ps4_games.db"
HEADERS = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36",
    "Accept-Language": "en-US,en;q=0.9",
}
PLATFORMS = ["playstation-4", "playstation-5"]  # 优先 PS4，没有则试 PS5


def name_to_slug(name: str) -> str:
    slug = name.lower()
    slug = re.sub(r"[^a-z0-9\s-]", "", slug)
    slug = re.sub(r"\s+", "-", slug.strip())
    slug = re.sub(r"-+", "-", slug)
    return slug


def fetch_score(name_en: str) -> int | None:
    slug = name_to_slug(name_en)
    for platform in PLATFORMS:
        url = f"https://www.metacritic.com/game/{slug}/"
        try:
            resp = requests.get(url, headers=HEADERS, timeout=10)
            if resp.status_code == 404:
                continue
            if resp.status_code != 200:
                print(f"  HTTP {resp.status_code}: {url}")
                return None
            soup = BeautifulSoup(resp.text, "html.parser")
            # Metascore 在 <span> 或特定 div 里，找包含纯数字(1-3位)的评分节点
            # 页面结构：<div data-testid="score-details-metascore"> 或直接找数字
            score_tag = soup.find("div", attrs={"data-testid": "score-details-metascore"})
            if score_tag:
                text = score_tag.get_text(strip=True)
                m = re.search(r"\b(\d{1,3})\b", text)
                if m:
                    return int(m.group(1))
            # 备用：找页面里第一个独立的 1-3 位数字评分
            for tag in soup.find_all(["span", "div"], class_=re.compile(r"metascore|score", re.I)):
                text = tag.get_text(strip=True)
                if re.fullmatch(r"\d{1,3}", text):
                    return int(text)
        except requests.RequestException as e:
            print(f"  请求失败: {e}")
            return None
    return None


def main():
    conn = sqlite3.connect(DB_PATH)
    cur = conn.cursor()

    cur.execute("SELECT id, name, name_en FROM games WHERE score=0 AND name_en!='' ORDER BY id")
    games = cur.fetchall()
    print(f"待处理: {len(games)} 条\n")

    updated = 0
    failed = []

    for i, (gid, name, name_en) in enumerate(games, 1):
        print(f"[{i}/{len(games)}] {name} / {name_en}", end=" ... ")
        score = fetch_score(name_en)
        if score is not None:
            cur.execute("UPDATE games SET score=? WHERE id=?", (score, gid))
            conn.commit()
            print(f"✓ {score}")
            updated += 1
        else:
            print("✗ 未找到")
            failed.append((gid, name, name_en))

        # 随机延迟 1-3 秒，避免被封
        time.sleep(random.uniform(1.0, 3.0))

    conn.close()

    print(f"\n完成：更新 {updated} 条，失败 {len(failed)} 条")
    if failed:
        with open("failed_games.txt", "w", encoding="utf-8") as f:
            for gid, name, name_en in failed:
                f.write(f"{gid}\t{name}\t{name_en}\n")
        print("失败列表已写入 failed_games.txt")


if __name__ == "__main__":
    main()
