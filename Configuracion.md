# CodeNest - Guía de Configuración

## 1. Configuración de IA (API Key)

La aplicación soporta múltiples proveedores de Inteligencia Artificial.

### Proveedores compatibles

| Proveedor | Modelo por defecto | Endpoint |
|---|---|---|
| **NVIDIA** | llama-3.1-nemotron-70b | `integrate.api.nvidia.com/v1` |
| **DeepSeek** | deepseek-chat | `api.deepseek.com/v1` |
| **Claude** | claude-3-haiku | `api.anthropic.com/v1` |
| **ChatGPT** | gpt-4o-mini | `api.openai.com/v1` |
| **Personalizado** | configurable | configurable |

### Cómo configurar

1. Ve a **Configuración → Sincronización**
2. Selecciona tu proveedor de IA
3. Ingresa tu **API Key**
4. Si usas **Personalizado**, ingresa también el **Endpoint URL** y el **Modelo**
5. Presiona **Guardar Configuración**

La clave se almacena localmente encriptada en el dispositivo (multiplatform-settings).

### Funcionalidades habilitadas

- **Análisis de código**: envía fragmentos de código y recibe explicaciones y sugerencias
- **Chat de programación**: consultas técnicas con contexto de código
- **Transcripción por visión (OCR)**: sube una imagen con código y la IA la transcribe automáticamente

---

## 2. Detección Automática de Lenguaje

Al pegar o escribir código en **Nuevo Snippet**, el sistema detecta automáticamente el lenguaje.

### Lenguajes soportados (45+)

| Categoría | Lenguajes |
|---|---|
| **Tradicionales** | Kotlin, Java, Python, JavaScript, TypeScript, C, C++, C#, Rust, Go, Swift, Ruby, PHP, Dart, Scala, R, Lua, Perl, Haskell, Elixir, Erlang, Clojure, Groovy, Objective-C, Zig, Nim, Julia |
| **Bases de datos SQL** | SQL (MySQL), SQL genérico (detecta `SELECT`, `INSERT`, `CREATE TABLE`, backticks, `AUTO_INCREMENT`, etc.) |
| **Bases de datos NoSQL** | MongoDB (detecta `db.collection.find`, operadores `$match`, `$group`, `$project`, `$lookup`, etc.) |
| **Scripts** | Shell/Bash, PowerShell, Makefile, CMake, Dockerfile |
| **Web** | HTML, CSS, SCSS, LESS, JSON, YAML, XML, Markdown |
| **Bajo nivel** | Assembly (x86/ARM), LLVM IR, WebAssembly (WAT), VHDL, Verilog, Fortran, COBOL, MATLAB |
| **Texto plano** | Detecta automáticamente si el contenido no es código (>3% caracteres especiales = código, <3% = texto) |

### Cómo funciona

- **Por extensión**: si nombras el archivo `main.py`, detecta Python
- **Por shebang**: `#!/bin/bash` → Shell, `#!/usr/bin/python3` → Python  
- **Por contenido (scoring)**: analiza el código con marcadores únicos (100pts), patrones fuertes (30pts), palabras clave (10pts), y anti-patrones (-20pts). Se elige el lenguaje con mayor puntuación >50.

### Reinicio de detección

Si borras todo el código del editor, el lenguaje se resetea a blanco y la extensión vuelve a `archivo.ext`, listo para detectar código nuevo.

---

## 3. OCR y Transcripción de Imágenes

Convierte capturas de pantalla de código en texto editable.

### Flujo

1. En **Nuevo Snippet → Evidencia**, presiona **Subir Imagen**
2. Selecciona una imagen PNG, JPG, GIF, BMP o WebP
3. Escribe qué quieres hacer en el campo de descripción (ej: "Transcribe este código SQL")
4. Presiona **Analizar con IA**

La IA detecta el lenguaje en la imagen, transcribe el código, y lo inserta automáticamente en el editor con la extensión correcta (`archivo.py`, `archivo.sql`, etc.).

### Requisito

Debes tener configurada una **API Key** de IA (sección 1). La transcripción usa el modelo de visión del proveedor (GPT-4o, Claude, DeepSeek).

---

## 4. Guardar y Eliminar Snippets

### Guardar

- Al presionar **Guardar** en el editor, el snippet se persiste en el dispositivo
- Se almacena como JSON vía `multiplatform-settings` (SharedPreferences en Android)
- Los snippets guardados sobreviven al cierre de la app

### Eliminar

- El botón **Eliminar** borra el snippet del storage permanentemente
- Navega automáticamente de vuelta a Mis Recetas

### Buscar

- En **Mis Recetas**, la barra de búsqueda filtra por:
  - Título del snippet
  - Lenguaje detectado
  - Tags/etiquetas
  - Contenido del código
- Los snippets guardados aparecen junto con los 10 snippets demo pre-cargados

---

## 5. Personalización de Interfaz (Adaptive Layout)

### Diseño responsivo

- **Pantalla ancha (≥600dp)**: sidebar de navegación fijo (260dp) + área de contenido
- **Pantalla angosta (<600dp)**: menú hamburguesa con drawer overlay animado

### Panel de Personalización

- **Tema base**: Claro, Oscuro (Deep), Sistema
- **Color de acento**: selector de paleta 16 colores + espectro 2D libre
- **Escala de UI**, **Transparencia acrílica**, **Animaciones**

### Panel de Sincronización

- Configuración de API Key de IA (proveedor, endpoint, modelo)
- Backup automático en la nube (placeholder)

---

## 6. Dependencias nuevas

```
androidx.activity:activity-compose:1.9.3  (Android file picker)
javax.swing (JVM file picker)              (incluido en JDK)
```

---

## 7. Archivos implementados

```
shared/src/
├── commonMain/kotlin/org/example/project/
│   ├── data/network/
│   │   ├── AiApiService.kt          ← Servicio de IA (chat + visión/OCR)
│   │   └── AiConfigRepository.kt    ← Persistencia de API key/proveedor
│   ├── domain/service/
│   │   └── LanguageDetector.kt      ← Detector de 45+ lenguajes con scoring
│   ├── platform/
│   │   └── FilePicker.kt           ← expect declaración file picker
│   ├── presentation/
│   │   ├── tabs/SettingsTab.kt     ← UI adaptativa + secciones
│   │   └── SnippetDetailScreen.kt  ← Editor con auto-detección + OCR
│   ├── domain/repository/
│   │   └── RecipeRepository.kt     ← Interfaz save/delete/getAll
│   └── data/repository/
│       └── MockRecipeRepositoryImpl.kt  ← Persistencia JSON snippets
├── jvmMain/kotlin/org/example/project/platform/
│   └── FilePicker.jvm.kt          ← JFileChooser actual
└── androidMain/kotlin/org/example/project/platform/
    └── FilePicker.android.kt       ← ActivityResultContracts actual
```
