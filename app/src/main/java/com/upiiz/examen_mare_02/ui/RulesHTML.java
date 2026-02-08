package com.upiiz.examen_mare_02.ui;

public class RulesHTML {

    public static final String ALL =
            "<h1>🌱🧟 Plantas vs Zombies Chess – Reglas Oficiales</h1>" +

                    "<h2>📌 Introducción</h2>" +
                    "<p>Bienvenido a <b>Plantas vs Zombies Chess</b>, un juego de estrategia por turnos donde " +
                    "dos bandos se enfrentan en un tablero de <b>8×8</b>. El objetivo es eliminar a todas " +
                    "las piezas enemigas mediante movimientos tácticos y daño progresivo.</p>" +

                    "<p>A diferencia del ajedrez tradicional, aquí no se captura una pieza de inmediato; " +
                    "en cambio, el daño ocurre por <b>tiempo y cercanía</b>. Esto da lugar a una experiencia " +
                    "más estratégica y dinámica.</p>" +

                    "<hr>" +

                    "<h2>🎮 Cómo se juega</h2>" +
                    "<p>Cada jugador toma turnos alternados. En tu turno puedes:</p>" +
                    "<ul>" +
                    "<li>Seleccionar una de tus piezas.</li>" +
                    "<li>Ver las casillas destacadas donde puede moverse.</li>" +
                    "<li>Realizar un movimiento válido.</li>" +
                    "<li>O presionar <b>“Terminar turno”</b> sin mover.</li>" +
                    "</ul>" +

                    "<p>Tras terminar tu turno, se aplica el <b>daño por tiempo</b> a tus piezas.</p>" +

                    "<hr>" +

                    "<h2>⏳ Daño por tiempo</h2>" +
                    "<p>Cada pieza recibe <b>daño acumulado</b> dependiendo de:</p>" +
                    "<ul>" +
                    "<li>La distancia respecto a piezas enemigas.</li>" +
                    "<li>El rango de ataque del enemigo.</li>" +
                    "<li>El tiempo que tardaste en tu turno.</li>" +
                    "</ul>" +

                    "<p>Cuando el HP llega a cero, la pieza muere y desaparece.</p>" +

                    "<h3>🔥 Ejemplo:</h3>" +
                    "<p>Si un <b>Sniper Planta</b> tiene rango 4, cualquier Zombie a 4 casillas o menos " +
                    "recibe daño ese turno.</p>" +

                    "<hr>" +

                    "<h2>♟️ Movimiento y características</h2>" +

                    "<h2>🌱 PLANTAS</h2>" +

                    "<h3>🌿 Runner</h3>" +
                    "<ul>" +
                    "<li>Movimiento: 1 casilla en cualquier dirección.</li>" +
                    "<li>Rango: 1.</li>" +
                    "<li>Vida: baja.</li>" +
                    "</ul>" +

                    "<h3>🏹 Archer</h3>" +
                    "<ul>" +
                    "<li>Movimiento: recto, varias casillas.</li>" +
                    "<li>Rango: 3.</li>" +
                    "<li>Vida: media.</li>" +
                    "</ul>" +

                    "<h3>🎯 Sniper</h3>" +
                    "<ul>" +
                    "<li>Movimiento: recto o diagonal.</li>" +
                    "<li>Rango: 4.</li>" +
                    "<li>Vida: baja.</li>" +
                    "</ul>" +

                    "<h3>🛡 Guardian</h3>" +
                    "<ul>" +
                    "<li>Movimiento: 1 casilla.</li>" +
                    "<li>Rango: 1.</li>" +
                    "<li>Vida: alta.</li>" +
                    "</ul>" +

                    "<hr>" +

                    "<h2>🧟‍♂️ ZOMBIES</h2>" +

                    "<h3>🧟 Walker</h3>" +
                    "<ul>" +
                    "<li>Movimiento: 1 hacia adelante o diagonal.</li>" +
                    "<li>Rango: 1.</li>" +
                    "<li>Vida: media.</li>" +
                    "</ul>" +

                    "<h3>💪 Brute</h3>" +
                    "<ul>" +
                    "<li>Movimiento: 1 en cualquier dirección.</li>" +
                    "<li>Rango: 1.</li>" +
                    "<li>Vida: muy alta.</li>" +
                    "</ul>" +

                    "<h3>🛢 Tank</h3>" +
                    "<ul>" +
                    "<li>Movimiento: recto varias casillas.</li>" +
                    "<li>Rango: 2.</li>" +
                    "<li>Vida: extremadamente alta.</li>" +
                    "</ul>" +

                    "<h3>🔮 Mage</h3>" +
                    "<ul>" +
                    "<li>Movimiento: diagonal.</li>" +
                    "<li>Rango: 2.</li>" +
                    "<li>Vida: media.</li>" +
                    "</ul>" +

                    "<hr>" +

                    "<h2>🔄 Terminar turno</h2>" +
                    "<p>Si presionas <b>Terminar turno</b> sin mover, igual se aplica daño por tiempo.</p>" +

                    "<hr>" +

                    "<h2>🏆 Condiciones de victoria</h2>" +
                    "<ul>" +
                    "<li>Eliminar todas las piezas del rival.</li>" +
                    "<li>Que el rival no pueda mover ninguna pieza.</li>" +
                    "</ul>" +

                    "<p>Si ambas pierden todo a la vez: <b>empate</b>.</p>" +

                    "<hr>" +

                    "<h2>🧠 Ejemplo de turno</h2>" +
                    "<p>1. Turno de Plantas.<br>" +
                    "2. Seleccionas un Archer.<br>" +
                    "3. Mueves a una casilla válida.<br>" +
                    "4. Termina tu turno.<br>" +
                    "5. Zombies reciben daño.</p>" +

                    "<hr>" +

                    "<h2>🌱🧟 ¡Disfruta tu partida!</h2>" +
                    "<p>Cada movimiento importa. Planifica, protege tus piezas y ataca con inteligencia.</p>";
}
