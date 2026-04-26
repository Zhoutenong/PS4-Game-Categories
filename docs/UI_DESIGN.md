# PS4 游戏分类 App — UI 设计规范

## 1. 设计原则

- **深色优先**：App 固定深色主题，契合游戏氛围，减少眼疲劳
- **PlayStation 感**：紫色主色调，呼应 PS 品牌色，与现有 Web 版视觉一致
- **Material 3 规范**：遵循 M3 色彩角色体系，保证对比度和可访问性
- **信息密度适中**：列表行高舒适，不过度压缩，手指点击区域 ≥ 48dp

---

## 2. 色彩系统

基于 Material Theme Builder 以 `#7C6AF7`（PlayStation 紫）为 primary 生成完整 M3 色板。

### 2.1 核心色值（Color.kt）

```kotlin
// Primary — PlayStation 紫
val Purple80       = Color(0xFFCDBBFF)   // dark primary
val Purple40       = Color(0xFF5B4CDB)   // light primary
val PurpleGrey80   = Color(0xFFCBC4DC)
val PurpleGrey40   = Color(0xFF635B72)

// Secondary — 粉紫点缀
val Pink80         = Color(0xFFEFB8C8)
val Pink40         = Color(0xFF7D5260)

// 自定义语义色（评分）
val ScoreGreen     = Color(0xFF4CAF50)   // ≥85
val ScoreOrange    = Color(0xFFFF9800)   // 70-84
val ScoreRed       = Color(0xFFF44336)   // <70
val ScoreNone      = Color(0xFF9E9E9E)   // 无评分
```

### 2.2 深色主题 ColorScheme（Theme.kt）

```kotlin
private val DarkColorScheme = darkColorScheme(
    primary          = Color(0xFFCDBBFF),   // 主色，用于按钮、激活态
    onPrimary        = Color(0xFF2D0082),
    primaryContainer = Color(0xFF4336C4),   // 分类标题栏背景
    onPrimaryContainer = Color(0xFFE6DEFF),

    secondary        = Color(0xFFCBC4DC),
    onSecondary      = Color(0xFF332D41),
    secondaryContainer = Color(0xFF4A4458), // 标签 Chip 背景
    onSecondaryContainer = Color(0xFFE8DEF8),

    tertiary         = Color(0xFFEFB8C8),   // 评分高亮点缀
    onTertiary       = Color(0xFF4A2532),

    background       = Color(0xFF0F0F1A),   // 页面背景
    onBackground     = Color(0xFFE8E8F0),

    surface          = Color(0xFF16162A),   // 卡片/列表项背景
    onSurface        = Color(0xFFE8E8F0),
    surfaceVariant   = Color(0xFF1E1E35),   // 次级卡片背景
    onSurfaceVariant = Color(0xFF8888AA),   // 次要文字

    outline          = Color(0xFF2A2A50),   // 边框、分割线
    outlineVariant   = Color(0xFF3A3A6A),
)
```

### 2.3 色彩使用规则

| 元素 | 颜色 token |
|---|---|
| 页面背景 | `background` (#0F0F1A) |
| 卡片背景 | `surface` (#16162A) |
| 悬停/按下态 | `surfaceVariant` (#1E1E35) |
| 主要文字 | `onSurface` (#E8E8F0) |
| 次要文字（英文名、数量） | `onSurfaceVariant` (#8888AA) |
| 分割线/边框 | `outline` (#2A2A50) |
| 分类标题左侧竖条 | `primary` 渐变到 `tertiary` |
| 激活态分类 | `primaryContainer` 背景 |
| 搜索框边框聚焦 | `primary` |
| 标签 Chip | `secondaryContainer` 背景 + `onSecondaryContainer` 文字 |

---

## 3. 字体排版

```kotlin
val AppTypography = Typography(
    // 页面大标题（详情页游戏名）
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    // 分类名、列表节标题
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 28.sp
    ),
    // 游戏中文名
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 22.sp
    ),
    // 游戏英文名、标签
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        color = onSurfaceVariant
    ),
    // 评分徽章、数量
    labelMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)
```

---

## 4. 形状规范

```kotlin
val AppShapes = Shapes(
    small  = RoundedCornerShape(8.dp),   // 评分徽章、标签 Chip
    medium = RoundedCornerShape(12.dp),  // 游戏列表卡片
    large  = RoundedCornerShape(16.dp),  // 分类卡片
)
```

---

## 5. 页面设计详述

### 5.1 首页 — 分类列表

```
┌──────────────────────────────────────┐  背景 #0F0F1A
│  🎮 PS4 游戏库              [🔍]     │  TopAppBar，surface色，无阴影
│     渐变紫色标题文字                  │
├──────────────────────────────────────┤
│  1,247 款游戏  ·  32 个分类          │  onSurfaceVariant，12sp，px16 py8
├──────────────────────────────────────┤
│                                      │
│  ┌────────────────────────────────┐  │
│  │▌ ACT  动作冒险          156 款 │  │  卡片：surface背景，outline边框
│  │  ████████████████████████  →  │  │  左侧4dp竖条：primary渐变
│  └────────────────────────────────┘  │  圆角16dp，mx16 my4
│                                      │
│  ┌────────────────────────────────┐  │
│  │▌ RPG  角色扮演          134 款 │  │
│  └────────────────────────────────┘  │
│                                      │
│  ┌────────────────────────────────┐  │
│  │▌ SPT  运动竞技           98 款 │  │
│  └────────────────────────────────┘  │
└──────────────────────────────────────┘
```

**分类卡片细节：**
- 高度：64dp
- 左侧紫色竖条：宽4dp，高32dp，渐变 `#7C6AF7 → #E040FB`，圆角2dp
- 分类代码（ACT）：`titleMedium`，`onSurface`
- 分类中文名：`bodySmall`，`onSurfaceVariant`，分类代码右侧
- 游戏数量：`labelMedium`，`onSurfaceVariant`，右对齐
- 右箭头图标：`onSurfaceVariant`，16dp
- 点击涟漪：`primary` 10% 透明度

---

### 5.2 游戏列表页

```
┌──────────────────────────────────────┐
│  ←  ACT · 动作冒险          [分数↕] │  TopAppBar
├──────────────────────────────────────┤
│  ┌────────────────────────────────┐  │
│  │  战神：诸神黄昏                │  │  titleMedium，onSurface
│  │  God of War Ragnarök    [ 94 ] │  │  bodySmall，onSurfaceVariant
│  └────────────────────────────────┘  │  评分徽章：绿色，右对齐
│  ┌────────────────────────────────┐  │
│  │  血源诅咒                      │  │
│  │  Bloodborne             [ 92 ] │  │
│  └────────────────────────────────┘  │
│  ┌────────────────────────────────┐  │
│  │  最终幻想 XVI                  │  │
│  │  Final Fantasy XVI      [ 88 ] │  │
│  └────────────────────────────────┘  │
└──────────────────────────────────────┘
```

**游戏列表项细节：**
- 高度：64dp（含上下 padding 各8dp）
- 左侧：游戏名（中文）+ 英文名两行，flex-1
- 右侧：评分徽章，宽40dp，高24dp，圆角8dp
- 评分色：≥85 绿色，70-84 橙色，<70 红色，0/无 灰色
- 分割线：`outline` 色，高0.5dp
- 点击后背景变为 `surfaceVariant`

**排序按钮（右上角）：**
- IconButton，图标 `sort`
- 点击弹出 DropdownMenu：「默认排序」「按评分降序」「按评分升序」

---

### 5.3 搜索页

```
┌──────────────────────────────────────┐
│  ←  [ 🔍  搜索游戏名、标签...     ] │  搜索框自动聚焦，圆角24dp
├──────────────────────────────────────┤
│  找到 3 个结果                        │  labelMedium，onSurfaceVariant
│                                      │
│  ┌────────────────────────────────┐  │
│  │  **战**神：诸神黄昏            │  │  关键词高亮：primary色加粗
│  │  ACT                    [ 94 ] │  │  分类名：secondaryContainer Chip
│  └────────────────────────────────┘  │
│  ┌────────────────────────────────┐  │
│  │  战神（2018）                  │  │
│  │  ACT                    [ 94 ] │  │
│  └────────────────────────────────┘  │
│                                      │
│  （无结果时）                         │
│       🎮                             │
│    没有找到相关游戏                   │
│    换个关键词试试                     │
└──────────────────────────────────────┘
```

**搜索框细节：**
- 背景：`surfaceVariant`
- 边框：默认 `outline`，聚焦 `primary`，宽2dp
- 圆角：24dp（胶囊形）
- 防抖：300ms

---

### 5.4 游戏详情页

```
┌──────────────────────────────────────┐
│  ←                                   │  TopAppBar，透明背景
├──────────────────────────────────────┤
│                                      │
│   战神：诸神黄昏                      │  headlineMedium，onSurface，px24
│   God of War Ragnarök                │  bodySmall，onSurfaceVariant
│                                      │
│   ┌──────────────────────────────┐   │
│   │                              │   │  surface卡片，圆角16dp，mx16
│   │   评分                       │   │
│   │                              │   │
│   │        ╔══════╗              │   │
│   │        ║  94  ║              │   │  大评分圆圈，直径72dp
│   │        ╚══════╝              │   │  绿色边框+文字，背景透明
│   │                              │   │
│   └──────────────────────────────┘   │
│                                      │
│   ┌──────────────────────────────┐   │
│   │  分类                        │   │  surface卡片
│   │  ┌─────┐                     │   │
│   │  │ ACT │                     │   │  primaryContainer Chip
│   │  └─────┘                     │   │
│   └──────────────────────────────┘   │
│                                      │
│   ┌──────────────────────────────┐   │
│   │  标签                        │   │  surface卡片
│   │  [动作] [冒险] [神话] [单机]  │   │  secondaryContainer Chips
│   └──────────────────────────────┘   │
│                                      │
└──────────────────────────────────────┘
```

---

## 6. 通用组件规范

### 评分徽章 ScoreBadge
```
宽40dp × 高24dp，圆角8dp
≥85：背景 rgba(76,175,80,0.15)，文字 #4CAF50
70-84：背景 rgba(255,152,0,0.15)，文字 #FF9800
<70：背景 rgba(244,67,54,0.15)，文字 #F44336
0/无：背景 rgba(158,158,158,0.15)，文字 #9E9E9E，显示"N/A"
```

### 标签 Chip
```
背景：secondaryContainer
文字：onSecondaryContainer，labelMedium
圆角：small (8dp)
内边距：水平8dp，垂直4dp
```

### 分类 Chip（详情页）
```
背景：primaryContainer
文字：onPrimaryContainer，labelMedium，Bold
圆角：small (8dp)
```

### 空状态
```
居中布局，图标64dp（onSurfaceVariant色）
主文字：titleMedium，onSurface
副文字：bodySmall，onSurfaceVariant
```

---

## 7. 间距规范

| 场景 | 值 |
|---|---|
| 页面水平边距 | 16dp |
| 卡片内边距 | 16dp |
| 列表项垂直间距 | 4dp |
| 节标题与内容间距 | 8dp |
| Chip 间距 | 8dp |
| 最小点击区域 | 48dp × 48dp |

---

## 8. Theme.kt 实现骨架

```kotlin
@Composable
fun PS4GameTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,  // 固定深色，不跟随系统
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
```

> 不使用 dynamic color（Android 12+ 壁纸取色），保持 PlayStation 紫色品牌感不被覆盖。
