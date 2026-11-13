# 🚀 DevLauncher: Tema y Launcher para Celular con Estética de Programación

Una aplicación completa de **Launcher Nativo para Android** diseñada con la estética de los entornos de desarrollo modernos (**VS Code / Dracula Pro / One Dark**), creada específicamente para satisfacer todas tus necesidades de personalización:

---

## ✨ Características Principales

### 1. 🖼️ Fondo de Pantalla a tu Gusto
* **Fotos de tu Galería:** Selecciona cualquier imagen o foto desde la galería de tu teléfono.
* **Fondos de Programación Integrados:**
  * **Dracula IDE Code:** Bloque de código Kotlin/Compose con resaltado de sintaxis y canaleta de números de línea.
  * **Matrix Code Rain:** Lluvia binaria y caracteres en verde fosforescente estilo terminal retro.
  * **Cyberpunk Circuit:** Malla tecnológica en azul eléctrico y neón violeta.
  * **Carbon Minimal:** Fibra de carbono oscura optimizada para pantallas AMOLED y ahorro de batería.
* **Control de Atenuación (Dimming Slider):** Ajusta la opacidad oscura sobre cualquier foto (0% a 85%) para garantizar que los iconos y etiquetas de código siempre se lean con máxima claridad.

### 2. ⚡ Leves Animaciones de Programación
* **Tokens de Código Flotantes:** Partículas sutiles de sintaxis (`const`, `val`, `fun()`, `0x80`, `async`, `return`, `{...}`) que ascienden suavemente por la pantalla con opacidad transparente (batería y rendimiento optimizados).
* **Velocidad Regulable:** Ajusta la velocidad de las partículas o apágalas cuando desees desde los ajustes.
* **Cursor Parpadeante de Terminal:** Efecto clásico de consola interactiva (`dev@phone:~$ search_apps █`) en la barra superior.
* **Barra de Estado IDE:** Métricas en tiempo real de RAM libre, hora, estado del sistema y branch Git (`git:main* | UTF-8 | Kotlin 2.2`).

### 3. 🔤 Selector de Fuentes Tipográficas
Cambia la tipografía de todo el sistema del launcher con un solo toque:
* **JetBrains Mono:** La fuente preferida por desarrolladores, nítida y moderna.
* **Fira Code:** Monospace clásico con estilo de ligaduras.
* **Hacker Terminal (VT323):** Estilo retro consola de fósforo verde de los 80s / CRT.
* **Android Monospace:** Fuente monoespaciada nativa del sistema.
* **Modern Sans:** Fuente limpia sin serifa.

### 4. 🎨 Personalización de Iconos App por App (Individual)
Personaliza el icono de **cualquier aplicación instalada de manera individual** simplemente **manteniendo presionada la app**:
* **Catálogo de Iconos Dev Integrado:** Más de 35 iconos técnicos vectoriales:
  * *Lenguajes:* Python, JavaScript, TypeScript, HTML5, CSS3, PHP, Data Science, etc.
  * *Herramientas:* Terminal, Git/Branch, Docker, Base de Datos SQL, Bug Debugger, Servidor, Memoria/CPU, Nube, Escudo/Seguridad, etc.
  * *Utilidades estilizadas:* Teléfono VoIP, Chat WebSocket, Cámara Raw, Música Stream, Navegador HTTP, etc.
* **Insignias Monograma IDE (Badge):** Crea un icono de archivo de código con 1 a 3 letras (`[Py]`, `[WA]`, `[YT]`, `[Sp]`) con color de acento personalizable.
* **Foto de Galería Individual:** Asigna cualquier foto de tu carrete como icono de una app concreta.
* **Paleta de Acentos Dracula:** Púrpura (`#BD93F9`), Cian (`#8BE9FD`), Verde (`#50FA7B`), Rosa (`#FF79C6`), Naranja (`#FFB86C`), Amarillo (`#F1FA8C`).
* **Restablecer:** Vuelve al icono original del sistema en cualquier momento.

### 5. 🔋 Animación de Carga de Programador (Charging Screen)
* **Detección Automática:** Al conectar el cargador (cable o inalámbrico), un receptor en segundo plano (`BroadcastReceiver`) detecta la conexión y abre la pantalla de carga.
* **Medidor Circular Futurista:** Porcentaje de batería en tiempo real con arco de luz palpitante en sincronía con el flujo de energía.
* **Consola de Compilación de Kernel:**
  ```bash
  > kernel::power_daemon --watch
  val source = Source.AC_FAST_CHARGE
  val voltage = 4.18f; // V
  val temperature = 31.4°C; // OPTIMAL
  val health = Condition.HEALTH_GOOD
  > while (battery < 100) { injectEnergyBuffer() }
  ```
* **Barra de Progreso ASCII:** `[################....] 78%`
* **Salida Rápida:** Toca en cualquier parte de la pantalla o desconecta el cargador para volver al inicio.
* **Modo de Prueba:** Puedes pulsar el botón "⚡ Probar Animación de Carga Ahora" en los ajustes o en la barra inferior para verla inmediatamente.

---

## 📲 Cómo Instalar en tu Celular (Samsung / Android)

El archivo ejecutable listo para instalar se encuentra compilado en:
📁 `app/build/outputs/apk/debug/app-debug.apk`

### Opción A: Vía Cable USB (ADB)
Con tu celular Samsung conectado a la PC con depuración USB activada:
```powershell
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### Opción B: Copiando el APK directamente al Celular
1. Copia el archivo `app-debug.apk` a tu teléfono (por cable, enviándolo por Telegram/WhatsApp a ti mismo, o subiéndolo a Google Drive).
2. En tu celular, abre el archivo `app-debug.apk` y selecciona **Instalar**. *(Si te pide permiso para "Instalar aplicaciones de fuentes desconocidas", concédelo).*

### Configurar como Launcher Predeterminado en Samsung (One UI):
1. Abre **Ajustes** en tu Samsung.
2. Ve a **Aplicaciones** > **Elegir aplicaciones predeterminadas** (o tres puntos arriba a la derecha).
3. Selecciona **Aplicación de inicio** (Home app).
4. Elige **DevLauncher**.

---

## 🛠️ Comandos de Terminal Integrados
En la barra de búsqueda superior puedes escribir comandos de consola rápidos:
* `:help` - Muestra la lista de atajos rápidos.
* `:theme` - Abre la pantalla de preferencias y personalización.
* `:charge` - Lanza la animación de carga de programador en pantalla completa.
* `:font` - Acceso directo a la selección de fuentes.
* `:wp` - Acceso directo a la selección de fondos de pantalla.
* `:clear` - Limpia la búsqueda actual.
