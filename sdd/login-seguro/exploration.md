## Exploration: login-seguro

### Current State

#### Estructura del proyecto
```
shared/src/commonMain/kotlin/org/example/project/
├── domain/                          ← Capa más pura (sin dependencias externas)
│   ├── GreetingUtil.kt              ← sayHello() — template legacy
│   └── repository/
│       └── GreetingRepository.kt    ← interfaz: getGreeting()
│
├── data/                            ← Implementaciones concretas
│   ├── Platform.kt                  ← interfaz + expect fun getPlatform()
│   └── repository/
│       └── GreetingRepositoryImpl.kt ← implementación concreta
│
└── presentation/                    ← UI Compose
    ├── App.kt                       ← entry point: MaterialTheme { DevSpaceLoginScreen() }
    ├── DevSpaceLoginScreen.kt       ← Pantalla de login (~406 líneas)
    └── theme/
        └── DevSpaceLoginColors.kt   ← 10 colores en un object
```

#### Login screen actual (DevSpaceLoginScreen.kt)
- **Layout**: `BoxWithConstraints` con fondo oscuro + glow radial + `Column` scrolleable centrada con glassmorphism
- **Responsive**: factor de escala basado en `maxWidth / 480.dp`, clamp `0.5f .. 1.5f`
- **Campos**:
  - Email: `OutlinedTextField` con teclado email, sin validación
  - Password: `OutlinedTextField` con toggle `PasswordVisible`, sin validación
- **Botones**:
  - "Iniciar sesión con Google" → `onClick = { /* TODO: Google Login */ }`
  - "Iniciar sesión" → `onClick = { /* TODO: Submit Login */ }`
  - "Regístrate" → `onClick = { /* TODO */ }`
  - "Términos y Condiciones" / "Privacidad" → `onClick = { /* TODO */ }`
  - "¿Has olvidado tu contraseña?" → `onClick = { /* TODO */ }`
- **Estado**: 3 variables locales con `remember { mutableStateOf(...) }` — sin ViewModel
- **Brand / Logo**: `Bison_Logo_Studios.png` (56dp * scaleFactor, mínimo 40dp) centrado horizontalmente al final del Column, con texto "Ztrene Studios"

#### Seguridad actual
**No existe absolutamente ninguna validación ni seguridad.** El estado es local, los botones no hacen nada, no hay hash de contraseña, no hay sanitización, no hay conexión a backend. Es un cascarón de UI.

#### Navegación
No existe. No hay Navigation, NavHost, ni `navController`. Solo `App()` → `DevSpaceLoginScreen()`.

#### Tests
No hay tests. Cero archivos en `src/*/test/`.

---

### Affected Areas

| Archivo | Por qué toca |
|---------|-------------|
| `presentation/DevSpaceLoginScreen.kt` | Agregar validación, conectar con ViewModel, mover brand a bottom-end |
| `presentation/theme/DevSpaceLoginColors.kt` | Probablemente no toca, solo colores |
| `presentation/App.kt` | Quizás para inyectar dependencias o wrapper del ViewModel |
| `domain/repository/AuthRepository.kt` **(nuevo)** | Interfaz del contrato de autenticación |
| `data/repository/AuthRepositoryImpl.kt` **(nuevo)** | Implementación concreta de auth |
| `presentation/LoginViewModel.kt` **(nuevo)** | ViewModel con estado y lógica de login |
| `build.gradle.kts` (shared) | Si se necesita `lifecycle-viewmodel-compose` (ya está en version catalog?) |

---

### Approaches

Dos problemas independientes, separo los análisis:

#### A. Seguridad en login

1. **ViewModel + AuthRepository (recomendado)**
   - Crear `AuthRepository` interfaz en domain, `AuthRepositoryImpl` en data
   - Crear `LoginViewModel` con estado (`LoginUiState`), validación de email/password, y llamada al repositorio
   - El ViewModel se alinea con la arquitectura actual (Package by Layer)
   - Pros: Sigue la arquitectura existente, testeable, escalable
   - Cons: Requiere decidir backend (Firebase? Supabase? Mock?)
   - Effort: **Medium** (3-4 archivos nuevos + modificar login screen)

2. **Validación inline en el Composable**
   - Agregar `isEmailValid`, `isPasswordValid` en el mismo screen
   - Pros: Rápido, mínimo movimiento
   - Cons: Viola separación de capas, no testeable, no escala
   - Effort: **Low**

3. **ViewModel sin repository (middle ground)**
   - ViewModel con validación, pero sin capa domain/data aún (mock hardcodeado)
   - Pros: Prepara para el backend sin acoplarse
   - Cons: Domain/data quedan vacíos de auth por ahora
   - Effort: **Low-Medium**

#### B. Mover brand a esquina inferior derecha

La brand (logo + "Ztrene Studios") está actualmente centrada al final del `Column` scrolleable.

**Opción: Layout con Box + Absolute Positioning**
- Salir del Column scrolleable y poner la brand en un `Box` separado con `Modifier.align(Alignment.BottomEnd)`
- Pros: Posición exacta sin depender del scroll
- Cons: Compatibilidad con el Column scrolleable — hay que asegurarse que la brand no se superponga al contenido

Effort: **Low** (cambios localizados en el mismo archivo)

---

### Puntos clave para implementar

1. **Validación de formulario**: email formato, password longitud mínima, mostrar errores visuales
2. **Manejo de estado**: ViewModel con `LoginUiState` (email, password, isLoading, errorMessage)
3. **AuthRepository interfaz** en domain: `suspend fun login(email: String, password: String): Result<User>`
4. **Implementación** en data: mock por ahora, después Firebase/Supabase
5. **Brand a bottom-end**: sacar del flujo del Column scrolleable, poner en Box separado con `BottomEnd`
6. **Navegación futura**: Preparar para Navigation 3 cuando haya segunda pantalla

### Riesgos

- **El Column scrolleable y el brand**: Si sacamos la brand del Column para posicionarla absolutamente, puede superponerse al contenido en viewports chicos. Habrá que probar bien en 320dp.
- **No hay DI**: Sin Hilt/Koin, el ViewModel y el repositorio se van a instanciar manualmente (manual DI o paso por parámetros).
- **lifecycle-viewmodel-compose**: Verificar si ya está en el version catalog o hay que agregarlo. No está en las dependencias actuales.
- **KMP multiplatform**: Si se conecta a Firebase, solo funciona en Android. Si se quiere multiplatform, toca Ktor HTTP + backend propio o Supabase.
- **Testing**: No hay infraestructura de tests todavía — el cambio debería incluir testsunitarios del ViewModel y del repositorio.
- **El proyecto apunta a 4 targets (Android, Desktop, Web)**: Firebase solo corre en Android. Cualquier solución de auth debe considerar multiplatform o aceptar la limitación.

### Ready for Proposal
**Sí** — hay suficiente claridad. Recomiendo:
1. **En seguridad**: Approach #1 (ViewModel + AuthRepository) con implementación mock inicial, que permita migrar a Firebase/Supabase después.
2. **En brand**: Cambio simple de layout con Box + BottomEnd.
3. **Tratar como cambio único** (login-seguro) que incluya ambos.
