# 🪹 CodeNest

¡Bienvenido a **CodeNest**! 

CodeNest es un sistema multiplataforma enfocado en el inicio de sesión (autenticación). La gran ventaja de este proyecto es que **el código se escribe una sola vez** y funciona perfectamente en **Android, Escritorio (Windows/Mac/Linux) y Web**, gracias a la tecnología de **Kotlin Multiplatform (KMP)** y **Compose Multiplatform**.

---

## ✨ ¿Por qué CodeNest?

- **Escribe una vez, úsalo en todas partes:** Toda la lógica (validaciones, contraseñas) y el diseño visual se comparten entre todas las plataformas.
- **Fácil de mantener:** Al tener un solo código centralizado, si corriges un error o agregas un botón, se actualiza automáticamente en las tres aplicaciones.
- **Arquitectura Limpia:** El código está súper organizado, separando la interfaz visual de las reglas del programa.
- **Listo para probar:** No necesitas configurar bases de datos ni servidores complicados. Todo funciona con datos locales simulados (mocks) para que lo pruebes apenas lo descargues.

---

## 🚀 ¿Cómo pruebo la aplicación?

Para empezar, necesitas tener instalado **Android Studio (versión Koala o superior)** o **IntelliJ IDEA** (con los plugins de Kotlin Multiplatform). Tu versión de Java (JDK) debe ser al menos **17**.

### 1. Descarga el proyecto
Abre tu terminal y ejecuta estos comandos:
```bash
git clone https://github.com/tu-usuario/CodeNest.git
cd CodeNest
```

### 2. Ejecútalo en tu plataforma favorita

Dependiendo de dónde quieras ver funcionar la aplicación, usa uno de estos comandos en la terminal de tu editor:

- **📱 Android (Celular o Emulador):**
  Puedes darle al botón de "Play" ▶️ en Android Studio seleccionando el módulo `androidApp`, o usar la terminal:
  ```bash
  ./gradlew :androidApp:installDebug
  ```

- **🖥️ Escritorio (Aplicación para PC):**
  Para abrir la aplicación como si fuera un programa nativo en tu computadora:
  ```bash
  ./gradlew :desktopApp:run
  ```

- **🌐 Web (En tu navegador):**
  Para lanzar la aplicación web y verla directamente en tu navegador (Chrome, Firefox, etc.):
  ```bash
  ./gradlew :webApp:wasmJsBrowserRun
  ```

---

## 📁 ¿Cómo está organizado el código?

Si exploras las carpetas del proyecto, verás una estructura muy sencilla:

- 📱 `androidApp/` 👉 Todo lo específico para que funcione en Android.
- 🖥️ `desktopApp/` 👉 Todo lo específico para que funcione como programa de PC.
- 🌐 `webApp/` 👉 Todo lo específico para que funcione como página web.
- 📦 `shared/` 👉 **¡El corazón de la app!** Aquí vive casi todo el código del proyecto. Las pantallas visuales, las reglas de autenticación y la lógica se comparten desde aquí hacia el resto de carpetas.

---

## 🧪 Ejecutar Pruebas (Tests)

Si quieres asegurarte de que todas las validaciones (como el formato del email o la contraseña) funcionan correctamente, corre este comando:
```bash
./gradlew :shared:test
```

---

## 🤝 Cómo Contribuir a CodeNest

¡Nos encanta recibir aportes de la comunidad! Si quieres ayudar a mejorar CodeNest, ya sea resolviendo bugs, añadiendo nuevas funciones, o mejorando la documentación, sigue esta guía:

### 🐛 Reportar Bugs y Sugerencias
Antes de escribir código, verifica en los *Issues* si el problema o idea ya ha sido reportado. Si no es así, abre un nuevo *Issue* detallando:
- ¿Qué problema encontraste? (Incluye capturas de pantalla si es visual).
- Pasos exactos para reproducirlo.
- ¿Qué solución propones?

### 💻 Pasos para enviar código (Pull Requests)

1. **Haz un Fork:** Copia el repositorio a tu cuenta personal de GitHub.
2. **Clona tu Fork:** Descárgalo en tu computadora de forma local.
3. **Crea una nueva rama:** No trabajes directamente en la rama principal (`main`). Crea una rama con un nombre descriptivo:
   ```bash
   git checkout -b feature/nueva-pantalla-login
   # o para un bugfix:
   git checkout -b fix/error-validacion-email
   ```
4. **Programa tu magia:** 
   - Asegúrate de seguir la **Arquitectura Limpia**. Si cambias lógica de negocio, hazlo en la carpeta `shared/`.
   - Respeta el formato de código de Kotlin (Kodetyle/Ktlint).
   - Documenta cualquier función pública o compleja usando KDoc.
5. **Escribe y pasa las pruebas:** Todo código nuevo de lógica debe venir acompañado de su respectivo Test Unitario. Ejecuta las pruebas (`./gradlew :shared:test`) para asegurar que nada se ha roto.
6. **Haz Commits claros:** Usa *Conventional Commits*:
   ```bash
   git commit -m "feat: agregar inicio de sesión con Google"
   # o
   git commit -m "fix: corregir crash al dejar el email vacío"
   ```
7. **Sube tus cambios y abre un Pull Request:** Envía tu código a nuestro repositorio y explica detalladamente qué cambiaste y por qué. ¡Nuestro equipo lo revisará lo antes posible!

---

<div align="center">

![Bison Logo](shared/src/commonMain/composeResources/drawable/Bison_Logo_Studios.png) 
![Wild Byte Studio Logo](shared/src/commonMain/composeResources/drawable/wild_byte_studio.png)
![Owl Connect Logo](shared/src/commonMain/composeResources/drawable/owl_connect.png)

<br>
<strong>Ztrene Studios, Wild Byte Studio & Owl Connect</strong>

</div>
