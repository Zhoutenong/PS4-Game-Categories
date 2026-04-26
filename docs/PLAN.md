# PS4 游戏分类 App — 执行计划

## 阶段划分

共 4 个阶段，预计总工时 **8-12 小时**（独立开发者）。

---

## Phase 1：项目初始化与数据层（约 2h）

**目标**：能从代码中读取到数据库数据。

### 任务清单

- [ ] 用 Android Studio 创建新项目（Empty Activity，Kotlin，Compose）
- [ ] 配置 `build.gradle.kts`：添加 Room、Hilt、Navigation Compose 依赖
- [ ] 将 `ps4_games.db` 复制到 `app/src/main/assets/`
- [ ] 编写 `GameEntity.kt`（映射 games 表，tags TypeConverter）
- [ ] 编写 `GameDao.kt`（4 个查询方法，见下）
- [ ] 编写 `AppDatabase.kt`（`createFromAsset`，version=1）
- [ ] 编写 `DatabaseModule.kt`（Hilt 提供 Database 和 Dao）
- [ ] 编写 `GameRepository.kt`（封装 Dao，返回 Flow）
- [ ] 写一个简单的单元测试验证数据能读出来

**GameDao 需要的查询：**
```kotlin
fun getCategories(): Flow<List<CategoryCount>>   // 分类+数量
fun getGamesByCategory(category: String): Flow<List<GameEntity>>
fun searchGames(query: String): Flow<List<GameEntity>>
fun getGameById(id: Int): Flow<GameEntity?>
fun getStats(): Flow<Stats>                       // 总数+分类数
```

**验收**：单元测试通过，能查出游戏数据。

---

## Phase 2：导航骨架与首页（约 2h）

**目标**：App 能跑起来，首页展示分类列表。

### 任务清单

- [ ] 配置 Hilt：`@HiltAndroidApp` Application 类，`MainActivity` 加 `@AndroidEntryPoint`
- [ ] 编写 `AppNavGraph.kt`（定义 4 个路由，占位 Screen）
- [ ] 编写 `HomeViewModel.kt`（加载分类列表 + 统计数据）
- [ ] 编写 `HomeScreen.kt`：
  - 顶部统计栏（总游戏数 / 总分类数）
  - 分类卡片 LazyColumn
  - 右上角搜索图标
- [ ] 配置 Material3 主题（`Theme.kt`，可用默认配色）

**验收**：首页正常显示分类列表，数量正确。

---

## Phase 3：游戏列表页 + 搜索页（约 3h）

**目标**：核心功能全部可用。

### 任务清单

**游戏列表页**
- [ ] 编写 `GameListViewModel.kt`（接收 category 参数，加载游戏列表）
- [ ] 编写 `GameListScreen.kt`：
  - TopAppBar 显示分类名 + 返回按钮
  - 游戏卡片 LazyColumn（中文名、英文名、评分徽章）
  - 右上角排序按钮（id / 评分切换）

**搜索页**
- [ ] 编写 `SearchViewModel.kt`（`MutableStateFlow<String>` + `debounce(300)`）
- [ ] 编写 `SearchScreen.kt`：
  - 顶部搜索框（自动获取焦点）
  - 结果 LazyColumn
  - 空状态视图

**验收**：分类列表可点击进入游戏列表，搜索有结果。

---

## Phase 4：详情页 + 收尾（约 2h）

**目标**：完整体验，可发布。

### 任务清单

- [ ] 编写 `DetailScreen.kt`（通过 gameId 从 ViewModel 加载，展示所有字段，tags 用 Chip）
- [ ] 完善导航：所有页面跳转串通
- [ ] 处理边界情况：空数据、加载状态（`CircularProgressIndicator`）
- [ ] 更新 `AndroidManifest.xml`：去掉 INTERNET 权限，设置 App 名称和图标
- [ ] 真机/模拟器完整流程测试
- [ ] 构建 Release APK（`./gradlew assembleRelease`）

**验收**：完整流程可用，APK 可安装运行。

---

## 依赖配置参考

`app/build.gradle.kts` 关键依赖：

```kotlin
val roomVersion = "2.6.1"
val hiltVersion = "2.51.1"

// Room
implementation("androidx.room:room-runtime:$roomVersion")
implementation("androidx.room:room-ktx:$roomVersion")
ksp("androidx.room:room-compiler:$roomVersion")

// Hilt
implementation("com.google.dagger:hilt-android:$hiltVersion")
ksp("com.google.dagger:hilt-compiler:$hiltVersion")
implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

// Navigation
implementation("androidx.navigation:navigation-compose:2.8.0")

// Compose BOM
implementation(platform("androidx.compose:compose-bom:2024.09.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
```

---

## 风险与注意事项

| 风险 | 应对 |
|---|---|
| Room `createFromAsset` 首次复制 db 较慢 | 在 Splash 或 Application.onCreate 中异步初始化，加 loading 状态 |
| tags 字段为空字符串 | TypeConverter 中判断空串返回 emptyList() |
| 搜索中文 LIKE 性能 | 数据量约 500-1000 条，SQLite LIKE 足够，无需 FTS |
| db 版本升级 | 当前 version=1，数据只读，无需迁移策略 |
