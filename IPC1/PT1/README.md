---

# 🎮 Pac-Man Console Game

## 📜 Descripción

¡Bienvenido a nuestra práctica de desarrollo de un juego en consola basado en Java! 🎉 Hemos recreado una versión simplificada del famoso videojuego PAC-MAN. Disfruta de una experiencia de juego en una interfaz amigable y sencilla.

## 📋 Menú de Inicio

Al iniciar el juego, encontrarás las siguientes opciones en el menú principal:

1. **Iniciar Juego** 🎮
2. **Historial de Partidas** 📜
3. **Salir** 🚪

## 🎮 Iniciar Juego

Cuando elijas iniciar el juego, se te pedirá la información del jugador, incluyendo:

- **Nombre del Jugador**: Para identificar al jugador.
- **Tipo de Tablero**: 
  - `P` para pequeño
  - `G` para grande
- **Cantidad de Premios**: 
  - Debe ser al menos 1 y como máximo el 40% del total de espacios en el tablero. Los premios pueden ser simples o especiales.
- **Cantidad de Paredes**: 
  - Debe ser al menos 1 y como máximo el 20% del total de espacios en el tablero.
- **Cantidad de Trampas**: 
  - Debe ser al menos 1 y como máximo el 20% del total de espacios en el tablero.

## 📏 Reglas del Juego

- El jugador comienza con **3 vidas** 💖.
- El puntaje inicial es **0** 🏆.
- El juego se considera ganado cuando el jugador ha recolectado todos los premios 🎯.
- No hay bordes externos en el tablero, por lo que Pac-Man puede atravesar de un lado a otro del tablero 🌍.
- Pac-Man no puede atravesar las paredes; debe rodearlas 🚧.

## 💥 Interacción con Elementos

- Al pasar sobre un fantasma, el jugador pierde una vida 👻💔. El fantasma se elimina del tablero.
- Al recoger un premio, el jugador suma puntos según el tipo de premio:
  - **Premio Simple**: 10 puntos ✨
  - **Premio Especial**: 15 puntos 🌟
- El juego termina si:
  - El jugador recoge todos los premios 🏅.
  - El jugador pierde todas sus vidas 💀.
  - El jugador pausa y termina la partida ⏸️.

## 🗂️ Historial de Partidas

Se almacenarán los datos de cada partida, incluyendo:

- Nombre del usuario 📝
- Puntos acumulados 📊

## 🕹️ Controles

- **Arriba**: `8` ⬆️
- **Abajo**: `5` ⬇️
- **Derecha**: `6` ➡️
- **Izquierda**: `4` ⬅️

## 🛠️ Instalación

1. Clona el repositorio: `git clone [URL del repositorio]`
2. Navega al directorio del proyecto: `cd nombre-del-proyecto`
3. Compila el proyecto: `javac *.java`
4. Ejecuta el juego: `java NombreDelJuego`

## 🤝 Contribuciones

¡Las contribuciones son bienvenidas! Si tienes ideas para mejorar el juego, no dudes en enviar un pull request.

---

¡Diviértete jugando y desarrollando! 🎉

