# 💰 TreasureGame_2D: Tile-Based Stealth Game

Un juego 2D de búsqueda del tesoro desarrollado en Java con Processing, donde el jugador debe explorar un laberinto, evitar a los guardias y encontrar el tesoro oculto.

Este proyecto está orientado al aprendizaje de programación orientada a objetos, manejo de sprites animados, colisiones y lógica de juego en tiempo real.

---
## 🎮 Gameplay

- Explora un laberinto tile-based
- Controla al personaje principal con sprites animados
- Evita a los guardias patrullando
- Encuentra el tesoro para ganar la partida
---

## 🕹️ Controles

| Tecla | Acción |
|------|-------|
| W / ↑ | Mover arriba |
| S / ↓ | Mover abajo |
| A / ← | Mover izquierda |
| D / → | Mover derecha |
| R | Reiniciar partida |
---

## Características técnicas

- Programado en **Java + Processing**
- Arquitectura **POO (Programación Orientada a Objetos)**
- Sistema de **sprites animados** con sprite sheets
- Movimiento fluido con corrección de esquinas (corner correction)
- Detección de colisiones con paredes y enemigos
- Lógica de victoria y *game over*

## Arquitectura (POO)

- **GameBoard** → gestiona el mapa y colisiones
- **Character (abstract)** → base común para personajes
- **Spy** → personaje controlado por el jugador (sprites animados)
Incluye:
  - Movimiento mediante teclado
  - Animación por sprite sheet
  - Colisiones con paredes y enemigos

- **Guard**  
  Enemigos con movimiento automático y animación.  
  Incluye:
  - Movimiento autónomo
  - Cambio de dirección al colisionar
  - Animación por sprite sheet

Cada personaje gestiona de forma independiente:
- Movimiento
- Dirección
- Colisiones
- Animación independiente
---
## 🛠️ Requisitos

* **Lenguaje:** Java 8 o superior
* **Framework Gráfico:** Processing Core Library
* **Entorno de Desarrollo recomendado:** Processing IDE o IntelliJ IDEA con Processing Core

---

## 🚀 Cómo ejecutar el proyecto

1. Clona el repositorio:

```bash
git clone https://github.com/nkaryoli/TreasureGame_2D.git
```

2. Abre el proyecto en tu IDE

3. Asegúrate de que la carpeta data/ contiene todas las imágenes

4. Ejecuta la clase:
```bash
TreasureGame2D
```

## 🎯 Objetivos del proyecto

Este proyecto tiene como objetivo practicar y consolidar conceptos fundamentales del desarrollo de videojuegos 2D utilizando Java y Processing:

- Programación orientada a objetos (POO)
- Gestión de sprites y animaciones
- Movimiento y colisiones en mapas 2D
- Lógica de juego basada en estados (victoria / derrota)
- Organización del código y arquitectura clara

---

## ✨ Posibles mejoras futuras

- Añadir sonido y música
- Sistema de niveles
- IA más avanzada para los guardias
- Menú principal
- HUD (vidas, tiempo, puntuación)
- Mejora de animaciones y transiciones.

---

## 👷 Autor
**Karyoli Nieves** 

## 📬 Contact

Feel free to reach out to me for collaboration or opportunities!

*   **Email:** karyoli.ie@gmail.com
*   **LinkedIn:** [Karyoli Nieves](https://www.linkedin.com/in/karyoli-nieves/)
*   **GitHub:** [@nkaryoli](https://github.com/nkaryoli)
*   **Portfolio:** [Karyoli Nieves](https://nkaryoli.github.io/miPortfolio/)

