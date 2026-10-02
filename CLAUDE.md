# VitaForge — Claude Code Rules

## Role
You are a Senior Kotlin Multiplatform engineer. Favour clean, readable code, small
focused modules, and maximum code sharing across Android and iOS. Prefer small, focused
changes; ask before restructuring modules or adding dependencies.

## Project Overview
KMP app (Android + iOS) with shared Compose Multiplatform UI. One Gradle build;
all dependency versions live in `gradle/libs.versions.toml`.

## Modules
- `:shared` — design system (colours, fonts, alpha, resources), domain models, `Screen` routes
- `:data` — repository interfaces (`domain` package) + internal implementations (Firebase via GitLive)
- `:di` — Koin modules, `initialiseKoin()`
- `:feature:auth`, `:feature:home` — one module per feature (screens, ViewModels, use cases, components)
- `:navigation` — root `NavGraph`, platform entry points
- `:androidApp` / `iosApp` — thin platform hosts, no business logic

### Dependency direction
```
androidApp / iosApp → navigation → feature:* → data, shared
                  └→ di → data, feature:*   (wiring only)
```
- Feature modules never depend on other feature modules; cross-feature navigation goes through `:navigation`
- Only `:di` (and platform entry points) wire implementations to interfaces
- Avoid circular dependencies and "god" modules; split `:shared` before it becomes a dumping ground
- Shared code needed by several features goes in `:shared` (or a dedicated `:core:*` module), never copied

### Packages
- Default: `com.vitaforge.<module>` (e.g. `com.vitaforge.home`)
- Exceptions: `:shared` uses `com.tylerdev.vitaforge.shared`; `:androidApp` uses `com.tylerdev.vitaforge`
- New code follows the default; do not rename existing packages unless asked

### Module hygiene
- Each module has a single responsibility and a small public API; mark everything else `internal`
- Keep implementations `internal` in `:data`; expose only interfaces and models
- New feature = new `:feature:<name>` module registered in `settings.gradle.kts`
- Use type-safe project accessors (`projects.feature.home`) and the version catalog; never hardcode versions or coordinates
- Keep module build files minimal. There is no `build-logic` yet; create convention plugins there once config duplication appears

## KMP Best Practices
- Default to `commonMain`; add platform code only when a platform API is unavoidable
- Prefer interfaces + Koin injection over `expect`/`actual`; use `expect`/`actual` only for small, stateless platform bindings
- Never import `android.*`, `androidx.appcompat`, `platform.UIKit`, or `java.*` in `commonMain`
- Use multiplatform libraries (Ktor, kotlinx.*, Koin, Coil 3, multiplatform-settings, GitLive Firebase); no Android-only libs in shared code
- Use the JetBrains Compose / AndroidX multiplatform artifacts (`org.jetbrains.*`)
- Resources go through Compose Resources (`Res.*`), not Android `R` or iOS asset catalogs
- Platform source sets (`androidMain`, `iosMain`) contain only glue: engines, initialisers, entry points
- Use the default hierarchy template; share iOS code in `appleMain`/`iosMain`, not per target
- Use `kotlinx-datetime`, `kotlinx.serialization`, and `Dispatchers.Default` / injected dispatchers (`Dispatchers.IO` is not common)
- Every change to shared code must compile for both Android and iOS
- No secrets in the repo; keep keys in `local.properties` / untracked platform config (`google-services.json`, `GoogleService-Info.plist`)

## Architecture
- Clean Architecture: data → domain ← presentation. There is no `:domain` module: domain models live in `:shared`, repository interfaces in `:data`'s `domain` package, use cases in the feature that owns them
- Domain code (models, interfaces, use cases) must not expose SDK types (e.g. Firebase) in its signatures
- MVVM + unidirectional data flow: state flows down, events flow up
- ViewModels expose `StateFlow<ScreenNameUiState>` (sealed: Loading, Success, Error); one-off effects (navigation, snackbars) are modelled as state or a `Channel`-backed flow, never callbacks held in the ViewModel
- Business logic lives in UseCases, not ViewModels or the data layer
- Implementations are `internal` in `:data`; return `Flow` / `suspend` results with no callback lambdas and no coroutine launches
- ViewModels use `viewModelScope`; never launch unscoped coroutines or use `GlobalScope`
- Inject dispatchers; never hardcode them, so tests can swap them
- Errors: the data layer returns `Result` or a sealed result type; ViewModels map failures to `Error` state with a user-facing message. Never let raw SDK exceptions reach the UI. Always rethrow `CancellationException`
- Expose read-only `StateFlow`/`Flow`; keep `MutableStateFlow` private. Use `stateIn(viewModelScope, WhileSubscribed(5_000), initial)` for derived state flows

## Compose Multiplatform
- Screens are stateless composables fed by UiState + event lambdas; the ViewModel is resolved at the screen entry point only (`koinViewModel()`)
- Mark UI state classes `@Immutable` / `@Stable`; use `kotlinx.collections.immutable` for lists in state
- Hoist state to the narrowest scope; use `remember`, `rememberSaveable` (for UI state that must survive config change/process death) and `derivedStateOf` appropriately
- Composables take a `modifier: Modifier = Modifier` as the first optional parameter and apply it to the root element
- `LazyColumn`/`LazyGrid` items need stable `key` and `contentType`
- Collect with `collectAsStateWithLifecycle()`
- No heavy mapping/formatting inside composables; do it upstream. Read state as late as possible (lambda modifiers, `graphicsLayer`) to limit recomposition
- Use `LaunchedEffect`/`DisposableEffect` with correct keys; no side effects in composition
- Design system: use `:shared` colours, typography, spacing and alpha tokens via the theme; never hardcode colours, text styles, or font sizes
- Strings come from Compose Resources (`stringResource(Res.string.x)`), never hardcoded in UI, to support localisation
- Add `@Preview` for reusable components and screens where the platform supports it
- Accessibility: interactive elements have `contentDescription` (or `null` for decorative), 48dp minimum touch target, sufficient contrast, support font scaling
- Handle edge-to-edge insets (`WindowInsets`, `safeDrawing`) rather than fixed padding for system bars
- Support dark/light theme and different screen sizes; avoid fixed widths and heights

## Navigation
- Navigation Compose (multiplatform), single root `NavGraph` in `:navigation`
- Routes are sealed classes/objects or `@Serializable` types in `Screen.kt`, never raw strings
- Each feature exposes a nav-graph extension (`fun NavGraphBuilder.homeGraph(...)`) that `:navigation` composes
- Features receive navigation as lambdas (`onNavigateToX`), never a `NavController`
- Pass IDs, not objects, as route arguments

## Kotlin Conventions
- PascalCase classes, camelCase functions/variables; functions start with a verb
- Booleans: `isX`, `hasX`, `canX`
- No magic numbers in logic; use named constants. Design values (`dp`, alpha) come from `:shared` tokens, or a private `val` in the file when unique to one composable
- Prefer `val`, immutable data classes, early returns, and `sealed interface` for closed hierarchies
- Functions under 20 lines and single purpose (guideline for composables: split into smaller composables when they grow)
- No `lateinit var` unless unavoidable; no `!!`
- Prefer `Flow`/coroutines over callbacks; structured concurrency only
- Format with ktlint (see Workflow)

## Testing
- Arrange-Act-Assert; variables named `inputX`, `mockX`, `actualX`, `expectedX`
- Write tests in `commonTest` with `kotlin-test`; use fakes, not real dependencies or mocking libraries
- Use `kotlinx-coroutines-test` (`runTest`, `StandardTestDispatcher`) and Turbine for Flow assertions; inject test dispatchers
- Unit test every public UseCase and ViewModel (every state transition and error path)
- Name tests `functionName_condition_expectedResult`

## Workflow
- Build Android: `./gradlew :androidApp:assembleDebug`
- Format/lint: `./gradlew ktlintFormat` / `./gradlew ktlintCheck`; run `ktlintCheck` before committing
- Tests: `./gradlew allTests`, or per module e.g. `./gradlew :shared:testAndroidHostTest`, `./gradlew :shared:iosSimulatorArm64Test`, `./gradlew :feature:home:iosSimulatorArm64Test`
- iOS: open `iosApp/` in Xcode, or verify shared code compiles with `./gradlew :shared:compileKotlinIosSimulatorArm64`
- Commits: short, imperative, one logical change each (e.g. "Implement sign out")

## Don'ts
- No platform imports in `commonMain` or domain
- No feature → feature dependencies
- No business logic in the data layer, ViewModels, or platform hosts
- No public implementation classes when `internal` works
- No dependencies added outside the version catalog
- No hardcoded strings, colours, or dispatchers
- No `GlobalScope`, `runBlocking` in production code, or swallowed exceptions
