# Tasks: Home Screen (login-seguro)

## Review Workload Forecast

| Field | Value |
|-------|-------|
| Estimated changed lines | ~140 |
| 400-line budget risk | Low |
| Chained PRs recommended | No |
| Suggested split | Single PR |
| Delivery strategy | ask-on-risk |
| Chain strategy | pending |

Decision needed before apply: Yes
Chained PRs recommended: No
Chain strategy: pending
400-line budget risk: Low

### Suggested Work Units

| Unit | Goal | Likely PR | Notes |
|------|------|-----------|-------|
| 1 | Login flow fix + navigation + HomeScreen + tests | PR 1 | Single PR — under 200 lines |

## Phase 1: Infrastructure & Navigation

- [x] 1.1 Add `isLoggedIn: Boolean = false` to `LoginState` — `presentation/login/LoginState.kt`
- [x] 1.2 Fix `submitData()`: replace try-catch with `.onSuccess`/`.onFailure` on Result; set `isLoggedIn = true` on success — `presentation/login/LoginViewModel.kt`
- [x] 1.3 Add `LaunchedEffect` in `DevSpaceLoginScreen` watching `state.isLoggedIn` → `navigator.replace(HomeScreen(state.email))` — `presentation/DevSpaceLoginScreen.kt`
- [x] 1.4 Wire post-registration navigation to `HomeScreen` (change `LaunchedEffect(state.success)` from `pop()` to `replace(HomeScreen)` — `presentation/CreateAccountScreen.kt`

## Phase 2: HomeScreen

- [x] 2.1 Create `HomeScreen(email: String)` as Voyager `Screen` with `Scaffold` + `TopAppBar("DevSpace")` + centered "Bienvenido, {email}" + "Cerrar sesión" button → `navigator.replace(DevSpaceLoginScreen())` — `presentation/HomeScreen.kt`

## Phase 3: Tests

- [ ] 3.1 Unit tests: LoginViewModel — success → isLoggedIn=true; failure → isLoggedIn=false + errorMessage set — `commonTest/.../login/LoginViewModelTest.kt`
- [ ] 3.2 Unit tests: LoginState defaults (isLoggedIn=false) and copy immutability
- [ ] 3.3 Run `./gradlew :shared:allTests` — verify all tests pass
