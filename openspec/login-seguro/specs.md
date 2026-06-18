# Home Screen Specification (login-seguro)

## Purpose

Post-auth landing screen that completes the auth loop. After login or registration success the user reaches this dashboard shell with a welcome message, app title, and logout capability.

## Functional Requirements

### R1: Login → Home Navigation

The system **MUST** navigate to `HomeScreen(email)` when login succeeds. The navigation **MUST** use `navigator.replace()` to remove the login screen from the backstack.

#### Scenario: Login success navigates to Home

- GIVEN the user has valid credentials on the login screen
- WHEN `LoginViewModel.submitData` succeeds
- THEN `LoginState.isLoggedIn` is set to `true`
- AND the screen observes the flag via `LaunchedEffect` and calls `navigator.replace(HomeScreen(email))`

#### Scenario: Login failure does NOT navigate

- GIVEN the user enters invalid credentials
- WHEN `LoginViewModel.submitData` returns a `Result.Failure`
- THEN `LoginState.isLoggedIn` remains `false`
- AND an error message is shown on the login screen

### R2: HomeScreen Layout

The `HomeScreen` **MUST** be a Voyager `Screen` receiving `email: String` as a constructor parameter. It **MUST** render a `Scaffold` with a `TopAppBar` titled "DevSpace" and a centered body showing "Bienvenido, {email}".

#### Scenario: HomeScreen renders with email

- GIVEN the user navigated to HomeScreen with email "dev@example.com"
- WHEN the screen renders
- THEN the TopAppBar displays "DevSpace"
- AND the body displays "Bienvenido, dev@example.com"

#### Scenario: HomeScreen matches dark theme

- GIVEN the HomeScreen is rendered
- THEN it uses `DevSpaceTheme` with the existing dark color scheme and glassmorphism styling
- AND layout dimensions scale via `scaleFactor` matching the login screen pattern

### R3: Logout

A logout button on HomeScreen **MUST** call `navigator.replace(DevSpaceLoginScreen())` to reset the navigation state.

#### Scenario: Logout returns to clean login screen

- GIVEN the user is on HomeScreen
- WHEN they tap the logout button
- THEN `navigator.replace(DevSpaceLoginScreen())` is called
- AND the login screen is the only screen on the backstack

### R4: Registration → Home Navigation

The system **MUST** navigate to `HomeScreen(email)` when account creation succeeds, using `navigator.replace()`.

#### Scenario: Registration success navigates to Home

- GIVEN the user completes registration with email "ada@example.com"
- WHEN the "Crear cuenta" action succeeds
- THEN `CreateAccountScreen` calls `navigator.replace(HomeScreen("ada@example.com"))`
- AND the HomeScreen shows "Bienvenido, ada@example.com"

### R5: Back Button on HomeScreen

The HomeScreen **SHOULD** handle system back with no action or close the app, since `replace` already clears the auth screens from the backstack.

#### Scenario: Back press on HomeScreen does not show login

- GIVEN the user is on HomeScreen after login
- WHEN the system back button is pressed
- THEN the login screen does NOT reappear
- AND the app either stays on HomeScreen or closes (platform-dependent)

## Non-Functional Requirements

| ID | Requirement | Constraint |
|----|-------------|------------|
| N1 | Responsive | Layout MUST use the same `scaleFactor` pattern as the login screen (`maxWidth / 480.dp`, clamped `0.5f..1.5f`) |
| N2 | Performance | No ViewModel for HomeScreen. State is local `remember`. Compose runtime manages recomposition. |
| N3 | Styling | MUST use `DevSpaceTheme` and existing Material 3 color scheme. MUST match dark glassmorphism aesthetic. |

## Out of Scope

- Real dashboard content or data widgets
- Google OAuth flow on HomeScreen
- Persistent session / token storage
- Password reset flow
- Profile or settings screens
- Any API calls from HomeScreen

## File Changes

| File | Type | Description |
|------|------|-------------|
| `presentation/HomeScreen.kt` | New | Home screen composable |
| `presentation/login/LoginState.kt` | Modified | Add `isLoggedIn: Boolean` field |
| `presentation/login/LoginViewModel.kt` | Modified | Set `isLoggedIn` on success + fix `Result.isSuccess` check |
| `presentation/DevSpaceLoginScreen.kt` | Modified | `LaunchedEffect` watching `isLoggedIn` for navigation |
| `presentation/CreateAccountScreen.kt` | Modified | Hook `onCreateAccount` to navigate to HomeScreen |
