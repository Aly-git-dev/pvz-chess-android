# PvZ-Chess Android  
**Videojuego de estrategia por turnos — Android (Java)**

PvZ-Chess Android es un videojuego de **estrategia por turnos** inspirado en el ajedrez clásico y en la lógica de *Plants vs Zombies*.  
El objetivo del juego es **eliminar todas las piezas del contrincante**, utilizando posicionamiento estratégico, control del tiempo y gestión de daño.

Este proyecto fue desarrollado como una aplicación **offline funcional (modo demo)** para demostración y pruebas locales, y cuenta además con una **versión online extendida** que integra servicios en la nube.

---

## Gameplay y reglas principales

- Juego por turnos sobre un tablero **8×8**.
- Dos bandos: **Plants** y **Zombies**.
- Cada pieza cuenta con:
  - Puntos de vida (**HP**)
  - Daño por segundo (**DPS**)
  - Rango de ataque
  - Reglas de movimiento específicas
- Durante cada turno:
  - El **tiempo transcurrido** influye en el daño recibido por las piezas dentro del rango enemigo.
- **Condición de victoria**:
  - Un jugador pierde cuando **se queda sin piezas en el tablero**.

El juego prioriza la **estrategia**, el control del espacio y la toma de decisiones bajo presión.

---

## Mecánicas destacadas

- **Motor de juego propio**, desacoplado de la interfaz gráfica.
- Sistema de **daño por tiempo**, aplicado según la proximidad entre piezas enemigas.
- Cálculo de rangos mediante métrica tipo *Chebyshev*.
- Serialización del estado del tablero para guardar y reanudar partidas.
- Validación de movimientos según reglas específicas por tipo de pieza.

---

## Arquitectura

La aplicación está organizada en capas claras:

### 🔹 Lógica del juego
- Motor independiente del tablero y las reglas.
- Clases para piezas, movimientos y validaciones.
- Cálculo de daño, rangos y condición de victoria.

### 🔹 Persistencia local (offline)
- Base de datos local con **Room (SQLite)**.
- Entidades para usuarios y partidas.
- Almacenamiento del estado de la partida (tablero, turno, estado).

### Interfaz de usuario
- Actividades para navegación y flujo de la aplicación.
- **Vista personalizada del tablero** usando `Canvas`, que incluye:
  - Renderizado de piezas
  - Indicadores de selección
  - Barras de vida
  - Sugerencias de movimientos válidos

---

## Estructura del proyecto

```txt
app/
 ├── data/
 │   ├── game/        # Motor del juego, tablero y reglas
 │   ├── local/       # Room (User, Match, DAO)
 │
 ├── ui/
 │   ├── activities/  # Pantallas principales
 │   ├── views/       # BoardView personalizada
 │
 ├── utils/           # Utilidades y helpers
