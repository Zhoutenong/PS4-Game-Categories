# PS4 游戏分类 App — 架构文档

## 1. 技术选型

| 层 | 选型 | 理由 |
|---|---|---|
| 语言 | Kotlin | Android 官方首选 |
| UI | Jetpack Compose | 声明式 UI，减少模板代码 |
| 数据库 | SQLite（Room） | 直接读取内置 db，Room 提供类型安全查询 |
| 架构模式 | MVVM + Repository | 官方推荐，职责清晰 |
| 导航 | Navigation Compose | 单 Activity，Compose 原生支持 |
| 依赖注入 | Hilt | 官方 DI，减少手动管理 |
| 异步 | Kotlin Coroutines + Flow | 与 Room/ViewModel 无缝集成 |

## 2. 整体架构

```
┌─────────────────────────────────────┐
│              UI Layer               │
│  HomeScreen / GameListScreen /      │
│  SearchScreen / DetailScreen        │
│         (Jetpack Compose)           │
└──────────────┬──────────────────────┘
               │ StateFlow / collectAsState
┌──────────────▼──────────────────────┐
│           ViewModel Layer           │
│  HomeViewModel / GameListViewModel  │
│  SearchViewModel                    │
└──────────────┬──────────────────────┘
               │ suspend fun / Flow
┌──────────────▼──────────────────────┐
│          Repository Layer           │
│          GameRepository             │
└──────────────┬──────────────────────┘
               │ DAO
┌──────────────▼──────────────────────┐
│            Data Layer               │
│   Room Database  ←  ps4_games.db   │
│   GameDao                           │
└─────────────────────────────────────┘
```

## 3. 目录结构

```
app/
├── src/main/
│   ├── assets/
│   │   └── ps4_games.db          # 内置数据库（只读）
│   ├── java/com/ps4games/
│   │   ├── data/
│   │   │   ├── db/
│   │   │   │   ├── AppDatabase.kt     # Room Database，预填充
│   │   │   │   ├── GameDao.kt         # 查询接口
│   │   │   │   └── GameEntity.kt      # 数据库实体
│   │   │   ├── model/
│   │   │   │   └── Game.kt            # 领域模型
│   │   │   └── repository/
│   │   │       └── GameRepository.kt  # 数据访问封装
│   │   ├── ui/
│   │   │   ├── home/
│   │   │   │   ├── HomeScreen.kt
│   │   │   │   └── HomeViewModel.kt
│   │   │   ├── gamelist/
│   │   │   │   ├── GameListScreen.kt
│   │   │   │   └── GameListViewModel.kt
│   │   │   ├── search/
│   │   │   │   ├── SearchScreen.kt
│   │   │   │   └── SearchViewModel.kt
│   │   │   ├── detail/
│   │   │   │   └── DetailScreen.kt    # 无 ViewModel，参数直接传入
│   │   │   ├── navigation/
│   │   │   │   └── AppNavGraph.kt     # 路由定义
│   │   │   └── theme/
│   │   │       └── Theme.kt           # Material3 主题
│   │   ├── di/
│   │   │   └── DatabaseModule.kt      # Hilt 模块
│   │   └── MainActivity.kt
│   └── res/
└── build.gradle.kts
```

## 4. 关键设计决策

### 4.1 数据库预填充
使用 Room 的 `createFromAsset("ps4_games.db")` 直接加载内置数据库，无需首次启动导入逻辑。数据库以只读方式使用（`fallbackToDestructiveMigration` 不需要，版本固定为 1）。

### 4.2 tags 字段处理
数据库中 tags 存储为逗号分隔字符串。在 `GameEntity` 中用 `@TypeConverter` 转换为 `List<String>`，上层统一使用列表。

### 4.3 搜索防抖
`SearchViewModel` 中用 `StateFlow` + `debounce(300ms)` 处理输入，避免每次击键都查询数据库。

### 4.4 导航路由

| 路由 | 说明 |
|---|---|
| `/` | 首页（分类列表） |
| `/games/{category}` | 分类游戏列表 |
| `/search` | 搜索页 |
| `/detail/{gameId}` | 游戏详情 |

## 5. 数据流示例（搜索）

```
用户输入 "战神"
    → SearchScreen 更新 query StateFlow
    → SearchViewModel debounce 300ms
    → GameRepository.search("战神")
    → GameDao: SELECT ... WHERE name LIKE '%战神%' OR name_en LIKE '%战神%' OR tags LIKE '%战神%'
    → Flow<List<Game>> 发射结果
    → SearchScreen recompose 展示列表
```

## 6. 依赖版本（参考）

```
compileSdk = 35
minSdk = 26
kotlin = "2.0.0"
compose-bom = "2024.09.00"
room = "2.6.1"
hilt = "2.51.1"
navigation-compose = "2.8.0"
```
