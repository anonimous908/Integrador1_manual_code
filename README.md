# CodeStash

Sistema de Gestión de Conocimiento Técnico (Knowledge Management System).

Bloc de notas técnico, indexado y categorizado, diseñado para optimizar el flujo de trabajo del desarrollador. Permite almacenar, organizar y recuperar fragmentos de código (snippets) en cuestión de segundos.

## Stack Tecnológico

| Tecnología | Versión |
|-----------|---------|
| Kotlin Multiplatform | 2.3.21 |
| Compose Multiplatform | 1.11.0 |
| AGP | 9.2.1 |
| MinSdk / TargetSdk | 24 / 36 |

## Plataformas

- **Android** — app nativa
- **Desktop** (JVM) — Windows, macOS, Linux
- **Web** — WasmJS (navegadores modernos) + JS (navegadores legacy)

## Arquitectura

El proyecto sigue una arquitectura de **Capas Simples (Package by Layer)** en el módulo `shared`:

```
shared/src/commonMain/kotlin/org/example/project/
├── domain/          → Lógica pura, entidades, interfaces de repositorio
├── data/            → Implementaciones concretas (Room, platform APIs)
└── presentation/    → ViewModels, Composables, UI
```

**Regla de dependencia**: `domain/` no conoce `data/` ni `presentation/`.

## Módulos

| Módulo | Descripción |
|--------|-------------|
| `shared` | Código compartido entre todas las plataformas |
| `androidApp` | Aplicación Android |
| `desktopApp` | Aplicación de escritorio (JVM) |
| `webApp` | Aplicación web (JS + WasmJS) |

## Cómo ejecutar

```bash
# Android
./gradlew :androidApp:assembleDebug

# Desktop (hot reload)
./gradlew :desktopApp:hotRun --auto

# Desktop (standard)
./gradlew :desktopApp:run

# Web (Wasm — modern browsers)
./gradlew :webApp:wasmJsBrowserDevelopmentRun

# Web (JS — legacy browsers)
./gradlew :webApp:jsBrowserDevelopmentRun
```

## Tests

```bash
# Todos los tests
./gradlew :shared:allTests

# Por plataforma
./gradlew :shared:testAndroidHostTest   # Android
./gradlew :shared:jvmTest               # Desktop
./gradlew :shared:wasmJsTest            # Web Wasm
./gradlew :shared:jsTest                # Web JS
```