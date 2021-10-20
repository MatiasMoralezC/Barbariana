package juego;

import java.awt.Color;
import java.util.Random;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;

	private Raptor[] raptors = new Raptor[3];
	private int cont = 100;
	
	private T_Rex rex = new T_Rex();
	private int contFB=0;
	
	private boolean flagRaptors = true;
	private boolean flagRex = false;

	// Variables y métodos propios de cada grupo
	// ...

	Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Sakura Ikebana Delivery - Grupo XX - v1", 800, 600);

		// Inicializar lo que haga falta para el juego
		// ...

		// Inicia el juego!
		this.entorno.iniciar();
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y por lo
	 * tanto es el método más importante de esta clase. Aquí se debe actualizar el
	 * estado interno del juego para simular el paso del tiempo (ver el enunciado
	 * del TP para mayor detalle).
	 */
	public void tick() {
		// Procesamiento de un instante de tiempo
		// ...
		
		if (flagRaptors) {
			
			// - - - Generacion de Raptors - - - //
			
			// se usa un contador para que no se amontonen al ser generados
			if (cont == 100) {
				int pos = 0;
				/* en este while se recorre las posiciones hasta encontrar
				 * un raptor activo o hasta que la posicion este fuera del
				 * rango del arreglo */
				while (pos < raptors.length && raptors[pos] != null) {
					pos++;
				}
				// si la posicion es valida se genera un raptor
				if (pos < raptors.length) {
					raptors[pos] = new Raptor();
				}
				cont = 0;
			}
			cont++;

			// - - - Movimiento de los Raptors - - - //

			for (int i = 0; i < raptors.length; i++) {
				if (raptors[i] != null) { // esta activo el raptor
					if (raptors[i].fueraDePantalla()) { // si esta fuera, eliminarlo
						raptors[i] = null;
						cont = 100;
					} else {	// si esta adentro, graficar y mover
						raptors[i].graficar(entorno);
						raptors[i].mover();
					}
				}
			}

			// - - - Generacion, Movimiento y Eliminacion(fuera de rango) de Rayos en Raptors - - - //

			for (Raptor raptor : raptors) {
				if (raptor != null) {	// si esta activo el raptor
					if (raptor.getRayo() == null) {	// si esta inactivo su rayo laser se lo genera
						raptor.generarRayo();
					} else { // si esta inactivo su rayo laser
						if (raptor.getRayo().fueraDePantalla()) { // si esta fuera de pantalla se elimina el rayo
							raptor.setRayo(null);
						} else { // si esta dentro de pantalla se grafica y se mueve el rayo
							raptor.getRayo().mover();
							raptor.getRayo().graficar(entorno, 'L'); // tipo de rayo L/R (laser o relampago)
						}
					}
				}
			}
			
		} // flagRaptors
		
		if(flagRex) {
			
			// - - - Movimiento de T-Rex - - - //
			
			if(rex.getPosX()>700)
				rex.setOrientacion('I');
			if(rex.getPosX()<200)
				rex.setOrientacion('D');
			
			rex.graficar(entorno);
			rex.mover();
			
			// - - - Generacion, Movimiento y Eliminacion(fuera de rango) de Fireballs en T-Rex - - - //
			
			Fireball[] fireballs = rex.getFireballs();
			
			for (int i=0;i<fireballs.length;i++) {
				if (fireballs[i] == null) {
					if(contFB>50) {
						fireballs[i] = rex.generarFireball();
						contFB=0;
					}
				} else {
					if (fireballs[i].fueraDePantalla()) {
						fireballs[i]=null;
					} else {
						fireballs[i].mover();
						fireballs[i].graficar(entorno);
					}
				}
			}
			contFB++;
			
		} // flagRex

	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}

	public static void intro(Entorno e) {
		e.escribirTexto("P1 GAMES", 400, 300);
	}
}
