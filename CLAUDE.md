# VitaForge — Claude Code Rules

## Role
You are a Senior Kotlin Multiplatform engineer. Favour clean, readable code, small
focused modules, and maximum code sharing across Android and iOS.

## Project Overview
KMP app (Android + iOS) with shared Compose Multiplatform UI. One Gradle build;
all dependency versions live in `gradle/libs.versions.toml`.

## Modules
- `:shared` — design system (colours, fonts, alpha, resources), common models, `Screen` routes
- `:data` — repository interfaces + implementations (Firebase via GitLive)
- `:di` — Koin modules, `initialiseKoin()`
- `:feature:auth`, `:feature:home` — one module per feature (screens, ViewModels, components)
- `:navigation` — root `NavGraph`, platform entry points
- `:androidApp` / `iosApp` — thin platform hosts, no business logic

## Modularisation Best Practices
- Dependencies point one way: `androidApp`/`iosApp` → `navigation` → `feature:*` → `data`/`shared`
- Feature modules never depend on other feature modules; cross-feature navigation goes through `:navigation`
- Shared code needed by several features goes in `:shared` (or a dedicated `:core:*` module), never copied
- Each module has a single responsibility and a small public API; mark everything else `internal`
- Keep implementations `internal` in `:data`; expose only interfaces and models
- Only `:di` (and platform entry points) wire implementations to interfaces
- Avoid circular dependencies and "god" modules; split `:shared` before it becomes a dumping ground
- New feature = new `:feature:<name>` module registered in `settings.gradle.kts`
- Package names follow the module: `com.vitaforge.<module>`
- Use type-safe project accessors (`projects.feature.home`) and the version catalog; never hardcode versions or coordinates in module build files
- Keep module build files minimal; extract shared config into convention plugins (`build-logic`) when duplication appears

## KMP Best Practices
- Default to `commonMain`; add platform code only when a platform API is unavoidable
- Prefer interfaces + Koin injection over `expect`/`actual`; use `expect`/`actual` only for small, stateless platform bindings
- Never import `android.*`, `androidx.appcompat`, `platform.UIKit`, or `java.*` in `commonMain`
- Use multiplatform libraries (Ktor, kotlinx.*, Koin, Coil 3, multiplatform-settings, GitLive Firebase) — no Android-only libs in shared code
- Use the JetBrains Compose / AndroidX multiplatform artifacts (`org.jetbrains.*`)
- Resources go through Compose Resources (`Res.*`), not Android `R` or iOS asset catalogs
- Platform source sets (`androidMain`, `iosMain`) contain only glue: engines, initialisers, entry points
- Use the default hierarchy template; share code in intermediate source sets (`appleMain`) instead of duplicating in each iOS target
- Use `kotlinx-datetime`, `kotlinx.serialization`, and `Dispatchers.Default` / injected dispatchers instead of JVM-only APIs (`Dispatchers.IO` is not common)
- Every change to shared code must compile for both Android and iOS
- No secrets in the repo; keep keys in `local.properties` / untracked platform config

## Architecture
- Clean Architecture: data → domain ← presentation (domain has no dependencies)
- MVVM: ViewModels expose `StateFlow<ScreenNameUiState>` (sealed: Loading, Success, Error)
- Business logic lives in UseCases, not ViewModels or the data layer
- Repository interfaces in domain; implementations in `:data`
- Return `Flow` / `suspend` results from the data layer; no callback lambdas, no coroutine launches
- ViewModels use `viewModelScope`; never launch unscoped coroutines

## Compose Multiplatform
- Screens are stateless composables fed by UiState + event lambdas; ViewModel is resolved at the screen entry point only
- Mark UI state classes `@Immutable` / `@Stable`
- Hoist state to the narrowest scope; use `remember` and `derivedStateOf` appropriately
- `LazyColumn`/`LazyGrid` items need stable `key` and `contentType`
- Collect with `collectAsStateWithLifecycle()`
- No heavy mapping/formatting inside composables — do it upstream
- Interactive elements have `contentDescription` (or `null` for decorative); 48dp minimum touch target

## Navigation
- Navigation Compose (multiplatform), single root `NavGraph` in `:navigation`
- Routes are sealed classes/objects or `@Serializable` types in `Screen.kt`, never raw strings
- Each feature exposes a nav-graph extension (`fun NavGraphBuilder.homeGraph(...)`) that `:navigation` composes

## Kotlin Conventions
- PascalCase classes, camelCase functions/variables; functions start with a verb
- Booleans: `isX`, `hasX`, `canX`
- No magic numbers — named constants
- Prefer `val`, immutable data classes, early returns
- Functions under 20 lines, single purpose
- No `lateinit var` unless unavoidable
- Format with ktlint before committing

## Testing
- Arrange-Act-Assert; variables named `inputX`, `mockX`, `actualX`, `expectedX`
- Write tests in `commonTest` with `kotlin-test`; use fakes, not real dependencies
- Unit test every public UseCase and ViewModel
- Run: `./gradlew :shared:testAndroidHostTest` and `./gradlew :shared:iosSimulatorArm64Test`

## Commands
- Build Android: `./gradlew :androidApp:assembleDebug`
- Format/lint: `./gradlew ktlintFormat` / `./gradlew ktlintCheck`
- iOS: open `iosApp/` in Xcode

## Don'ts
- No platform imports in `commonMain` or domain
- No feature → feature dependencies
- No business logic in the data layer, ViewModels, or platform hosts
- No public implementation classes when `internal` works
- No dependencies added outside the version catalog
