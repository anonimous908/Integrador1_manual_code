# Proposal: Home Screen (login-seguro)

## Intent

Login/registration flow terminates in a void — no destination after success. This change adds a Home/Dashboard screen as post-auth landing, completing the auth loop.

## Scope

### In Scope
- HomeScreen with "Bienvenido, {email}" welcome
- TopAppBar with "DevSpace" title
- Logout button → returns to login
- Login success → HomeScreen navigation
- Registration success → HomeScreen navigation

### Out of Scope
- Real dashboard content/data
- Google OAuth flow
- Password reset
- Persistent session (token storage)

## Capabilities

### New Capabilities
- `home-screen`: Post-auth dashboard with welcome, AppBar, and logout

### Modified Capabilities
- None

## Approach

| Concern | Decision |
|---------|----------|
| HomeScreen | Voyager `Screen` with `email: String` constructor param. `Scaffold` + `TopAppBar` + centered welcome + logout button. No ViewModel needed. |
| Login → Home | Add `isLoggedIn: Boolean` flag to `LoginState`. `LoginViewModel` sets it on success. Screen watches via `LaunchedEffect` → `navigator.replace(HomeScreen(email))`. |
| Registration → Home | `CreateAccountScreen.onCreateAccount` navigates to `HomeScreen(email)` directly (mock). |
| Logout | `navigator.replace(DevSpaceLoginScreen())` — full navigation reset. |
| Fix | `LoginViewModel.submitData` must check `Result.isSuccess` (current try-catch swallows `Result.Failure`). |

## Affected Areas

| Area | Impact | Description |
|------|--------|-------------|
| `presentation/HomeScreen.kt` | New | Home screen composable |
| `presentation/login/LoginState.kt` | Modified | Add `isLoggedIn` flag |
| `presentation/login/LoginViewModel.kt` | Modified | Success signal + fix Result check |
| `presentation/DevSpaceLoginScreen.kt` | Modified | `LaunchedEffect` for navigation |
| `presentation/CreateAccountScreen.kt` | Modified | Hook navigation to Home |

## Risks

| Risk | Likelihood | Mitigation |
|------|------------|------------|
| Result.Failure swallowed by try-catch | High (existing bug) | Fix in this change |
| `replace` removes login from backstack | Low | Intentional — logout resets state |

## Rollback Plan

Revert 5 files. Pure UI + navigation — no schema, data, or config changes.

## Success Criteria

- [ ] Login navigates to HomeScreen showing "Bienvenido, {email}"
- [ ] Registration navigates to HomeScreen
- [ ] Logout returns to clean login
- [ ] HomeScreen matches dark glassmorphism style (uses existing `DevSpaceTheme`)
- [ ] Existing tests pass
- [ ] New unit tests for HomeScreen state and navigation edge cases
