# Design: Home Screen (login-seguro)

## Technical Approach

Post-auth landing screen as a Voyager `Screen` receiving `email: String` via constructor. No shared state store, no HomeViewModel — keep it simple. Login success exposes `isLoggedIn` flag through `LoginState`; registration success navigates directly. Logout resets the backstack entirely with `navigator.replace`.

## Architecture Decisions

| Decision | Choice | Alternatives Considered | Rationale |
|----------|--------|------------------------|-----------|
| HomeScreen state | No ViewModel | HomeViewModel with its own state | Pure UI — no business logic, no state to manage. One string param is not worth a ViewModel. |
| Email transport | Constructor param | SavedStateHandle, SharedFlow, repo store | KISS — simplest path. No DI overhead, no serialization, compiler-verified. |
| Login→Home trigger | State flag + LaunchedEffect | Callback from VM, event channel | Follows existing `state.collectAsState()` pattern used in `DevSpaceLoginScreen`. No new mechanism needed. |
| Nav reset on logout | `navigator.replace` | `popToRoot`, `popUpTo`+push | `replace` removes Home from backstack entirely — prevents back-navigation to Home after logout. |
| Home visual style | Existing `DevSpaceTheme` | New theme/colors | Reuses MaterialTheme color scheme already wired. Consistent with login/register screens. |

## Data Flow

```
[LoginScreen]                              [CreateAccountScreen]
     │                                              │
     │ submit(email, pass)                          │ onCreateAccount(name, email, pass)
     ▼                                              │
[LoginViewModel]                                    │
     │                                              │
     ├── loginWithEmailUseCase()                     │
     │   ├── Result.success  → isLoggedIn = true     │
     │   └── Result.failure  → errorMessage = msg   │
     │                                              │
     │  state.isLoggedIn = true                      ▼
     │  └── LaunchedEffect watches state     navigator.replace(HomeScreen(email))
     │      └── navigator.replace(HomeScreen(email))
     │
[HomeScreen] — receives email via constructor
     │
     ├── TopAppBar → "DevSpace"
     ├── Body → "Bienvenido, {email}"
     ├── Logout → navigator.replace(DevSpaceLoginScreen())
     └── Wrapped in DevSpaceTheme (via App.kt root)
```

## Component Tree — HomeScreen

```
Scaffold                            ← Material3 Scaffold
├── TopAppBar                       ← "DevSpace" title, no nav icon
└── Column (center, fill)           ← Welcome + Logout
    ├── Text("Bienvenido, {email}")
    ├── Spacer
    └── Button("Cerrar sesión")    ← Logout action
```

Glassmorphism card not needed for Home — a centered layout within the existing dark theme is sufficient. If desired, a surface container with alpha can be added for visual consistency.

## File Changes

| File | Action | Description |
|------|--------|-------------|
| `presentation/HomeScreen.kt` | Create | Voyager `Screen` with email param, Scaffold + TopAppBar + welcome + logout |
| `presentation/login/LoginState.kt` | Modify | Add `val isLoggedIn: Boolean = false` |
| `presentation/login/LoginViewModel.kt` | Modify | Replace try-catch with `.onSuccess`/`.onFailure` on Result; set `isLoggedIn` on success |
| `presentation/DevSpaceLoginScreen.kt` | Modify | Add `LaunchedEffect(state.isLoggedIn)` block after state collection |
| `presentation/CreateAccountScreen.kt` | Modify | Wire `onCreateAccount` callback to `navigator.replace(HomeScreen(email))` |

## Interfaces / Contracts

### LoginState changes
```kotlin
data class LoginState(
    val email: String = "",
    val pass: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false  // NEW — signals successful login
)
```

### HomeScreen contract
```kotlin
class HomeScreen(private val email: String) : Screen {
    @Composable
    override fun Content() {
        // Uses LocalNavigator.currentOrThrow for logout
        // Scaffold + TopAppBar("DevSpace") + body + logout Button
    }
}
```

### Navigation flow types
```kotlin
// Login → Home: navigator.replace(HomeScreen(state.email))
// Register → Home: navigator.replace(HomeScreen(email))
// Logout: navigator.replace(DevSpaceLoginScreen())
```

## Bug Fix: Result.Failure swallowed

Current `submitData()` uses try-catch, but `loginWithEmailUseCase` returns `Result<Unit>` — a `Result.Failure` is a return value, not an exception, so the catch block never fires for auth failures. Fix:

```kotlin
// Before (broken):
try {
    loginWithEmailUseCase(_state.value.email, _state.value.pass)
    _state.update { it.copy(isLoading = false) }
} catch (e: Exception) { ... }

// After:
val result = loginWithEmailUseCase(_state.value.email, _state.value.pass)
result.onSuccess {
    _state.update { it.copy(isLoading = false, isLoggedIn = true) }
}.onFailure { e ->
    _state.update { it.copy(isLoading = false, errorMessage = e.message ?: "Error desconocido") }
}
```

## Testing Strategy

| Layer | What to Test | Approach |
|-------|-------------|----------|
| Unit | LoginViewModel: success → isLoggedIn=true | Mock use case returns `Result.success(Unit)`, assert state |
| Unit | LoginViewModel: failure → isLoggedIn=false + errorMessage set | Mock returns `Result.failure(Exception("msg"))`, assert state |
| Unit | LoginState defaults | Verify new `isLoggedIn` default is `false` |
| Unit | LoginState copy immutability | Confirm existing fields unchanged by new field |

No Compose UI test dependencies in the project — UI tests are not feasible at this stage. All tests use `kotlin.test` in `commonTest`.

## Migration / Rollout

No migration required. Pure UI + navigation change — no schema, data, or persistence involved.

## Open Questions

- None. All decisions are straightforward with the existing patterns.
