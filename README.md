# Cordova-Total-Player
La Idea es Crear un Plugin para Cordova con Exoplayer , Google Cast y compatible con la App de VLC.

## Funciones

* **playVlc** = function (url, success, error) = Reproduce con la aplicación de VLC
* **exoPlayer** = function (url, success, error) = Reproduce con la lib de exoplayer 
* **mediaPlayer** = function (url, success, error) = Reproduce con el nativo de Android


## Descargarlo
Actualmente el repositorio esta dividido en app-prueba que contiene un proyecto android con las funciones implementadas, y Total-Player que es lo que contiene realmente el plug-in, probado y en funcionamiento con Android X. 

* Por tanto para descargarlo tienes que descargar el repositorio y con cordova plugin realizar un add sobre la carpeta [Total-Player](https://github.com/domlopluis94/Cordova-Total-Player/tree/master/Total-Player)

		cordova plugin add Cordova-Total-Player/Total-Player

* [Cordova Plugin add](https://cordova.apache.org/docs/en/latest/reference/cordova-cli/index.html#cordova-plugin-command)