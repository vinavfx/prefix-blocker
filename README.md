# Prefix Blocker

Aplicación Android mínima para rechazar **solo llamadas entrantes** cuyos números comiencen con un prefijo de la lista. Las llamadas
salientes nunca se bloquean, aunque coincidan con un prefijo.

## Uso

1. Abre **Prefix Blocker** y pulsa **Enable call blocking** para seleccionarla como filtro de llamadas de Android.
2. Pulsa **Include saved contacts** y concede el permiso si también quieres bloquear números guardados. Sin él, Android excluye los
   contactos del filtrado. La aplicación no consulta ni transmite tu agenda.
3. Añade prefijos con **Add** y elimínalos con **Delete**. La lista se guarda en el teléfono y comienza vacía en instalaciones nuevas.

`600` bloquea los números nacionales que empiezan por `600`, incluso si llegan como `+56 600…`. `80` funciona del mismo modo. Un prefijo con
`+`, como `+56600`, restringe la coincidencia al código internacional indicado. La normalización utiliza libphonenumber y el país de la red
o de la SIM.

Funciona sin conexión, publicidad, cuenta, permiso de Internet ni notificación permanente. No es necesario mantener abierta la pantalla:
Android inicia el servicio al recibir una llamada. No fuerces la detención de la aplicación desde Ajustes.

## Alcance y límites

- Android 10 o posterior, con soporte para el rol de filtrado de llamadas. Solo una aplicación puede ocupar ese rol a la vez.
- Se utiliza `CallScreeningService` para rechazar la llamada antes del timbre y solicitar que no haya notificación de llamada perdida.
- Android puede conservar la llamada en el historial de bloqueadas: las aplicaciones normales no pueden impedirlo mediante este servicio.
- Los números ocultos o no identificados no tienen un prefijo que comparar. No se filtran llamadas de WhatsApp ni de otras aplicaciones.
- Las excepciones del sistema, por ejemplo tras una llamada de emergencia, y el desvío al buzón de voz dependen de Android y del operador.

## Instalar en otros teléfonos por USB

La APK reutilizable queda en `dist/prefix-blocker.apk`. Para instalarla desde este ordenador solo necesitas ADB, que ya está instalado; no
hace falta recompilar ni descargar herramientas de desarrollo.

1. En el teléfono, activa las opciones de desarrollador pulsando siete veces **Número de compilación** en los ajustes de información del
   dispositivo. La ubicación varía según la marca.
2. Activa **Depuración USB**, conecta un cable de datos y acepta **Permitir depuración USB** en el teléfono desbloqueado.
3. Ejecuta:

```bash
cd /home/pancho/Documents/develop/prefix-blocker
./install.sh
```

Si hay varios dispositivos, consulta `adb devices -l` y usa `./install.sh SERIAL` con el identificador del teléfono deseado. El instalador
comprueba la conexión y Android 10 o posterior, instala o actualiza sin borrar la lista y abre la aplicación. Después, activa el filtro y
añade los prefijos siguiendo la sección **Uso**. Las instalaciones nuevas comienzan sin prefijos.

Para pedírmelo después: **«Instala Prefix Blocker del repositorio /home/pancho/Documents/develop/prefix-blocker en el teléfono conectado por
USB y ayúdame a activar el filtro»**.

Para compartirla, basta copiar la APK; su instalación directa requiere permitir aplicaciones de esa fuente. El SDK y `.tools/` no son
necesarios para instalarla. Los artefactos de `dist/` se conservan localmente, pero no están versionados en Git.

## Compilar una versión actualizada

Requisitos: Java 17, Gradle 8.7 y SDK Android con `platforms;android-34` y `build-tools;34.0.0`. Configura `JAVA_HOME` y `ANDROID_HOME`.
`build.sh` también reconoce las herramientas locales de `.tools/`, excluidas de Git.

```bash
./build.sh
sha256sum -c dist/prefix-blocker.apk.sha256
./install.sh
```

La compilación incluye el análisis estático de Android y actualiza la copia de `dist/` y su suma SHA-256. Es una APK de desarrollo firmada,
válida para instalación manual, no una publicación de tienda. Conserva `~/.android/debug.keystore`: las futuras actualizaciones deben usar
la misma clave para mantener la compatibilidad con las instalaciones existentes. No compartas ni publiques esa clave.

## Verificación en el dispositivo

Instalada mediante USB en un Motorola moto e14 con Android 14. Se comprobaron la activación del rol, el permiso para incluir contactos, el
alta, borrado y persistencia de prefijos. También se verificaron coincidencias nacionales e internacionales con comprobaciones locales
temporales. Falta comprobar con una llamada entrante real el rechazo sin sonido ni notificación.

Referencia: [servicio nativo de filtrado de Android](https://developer.android.com/reference/android/telecom/CallScreeningService).
