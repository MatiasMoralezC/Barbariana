package juego;

import java.awt.Color;
import java.util.Random;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;

	private Barbarianna barb = new Barbarianna(50, 540, 25, 25, 2, 'D');
	private int contSalto=0;
	
	private Raptor[] raptors = new Raptor[3];
	private int cont = 100;
	
	private T_Rex rex = new T_Rex(700,63,(float)1.5,'I',7);
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
		
		// - - - Barbarianna - - - //
		barb.graficar(entorno);
		
		if (entorno.estaPresionada(entorno.TECLA_DERECHA)) {
			barb.setOrientacion('D');
			barb.mover();
		}
		if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
			barb.setOrientacion('I');
			barb.mover();
		}
		if (entorno.sePresiono(entorno.TECLA_ARRIBA)) {
			//barb.saltar();
			barb.setSaltando(true);
		}
		if (entorno.sePresiono(entorno.TECLA_ESPACIO)) {
			if (barb.getX() > 25 && barb.getX() < 775 && barb.getRelampago()==null) {//solo lo lanza si vikinga está en pantalla
				barb.generarRelampago();
			}
			
		}
		if(barb.getRelampago()!=null) {
			RayoMjolnir relampago = barb.getRelampago();
			if(relampago.fueraDePantalla()) {
				barb.setRelampago(null);
			}
			else {
				relampago.graficar(entorno);
				relampago.mover();
			}
		}
		
		if(barb.getSaltando() && contSalto<30) {
			barb.subir();
			contSalto++;
		}
		
		if(contSalto==30) {
			barb.setSaltando(false);
		}
		
		if(!barb.getSaltando() && contSalto>0) {
			barb.bajar();
			contSalto--;
		}
		
		graficarPisos(entorno);
		
		//barb.caida();
		barb.fueraDePantalla();
		
		// - - -
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
					raptors[pos] = new Raptor(840,50,2,'I');
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
					} else { // si esta adentro, graficar y mover
						raptors[i].graficar(entorno);
						raptors[i].procesarMovimiento();
					}
				}
			}

			// - - - Generacion, Movimiento y Eliminacion(fuera de rango) de Rayos en Raptors - - - //

			for (Raptor raptor : raptors) {
				if (raptor != null) {	// si esta activo el raptor
					if (raptor.getRayoLaser() == null) {	// si esta inactivo su rayo laser se lo genera
						raptor.generarRayoLaser();
					} else { // si esta inactivo su rayo laser
						if (raptor.getRayoLaser().fueraDePantalla()) { // si esta fuera de pantalla se elimina el rayo
							raptor.setRayoLaser(null);
						} else { // si esta dentro de pantalla se grafica y se mueve el rayo
							raptor.getRayoLaser().mover();
							raptor.getRayoLaser().graficar(entorno);
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
			
			rex.mover();
			
			// - - - Generacion, Movimiento y Eliminacion(fuera de rango) de Fireballs en T-Rex - - - //
			
			Fireball[] fireballs = rex.getFireballs();
			
			for (int i=0;i<fireballs.length;i++) {
				if (fireballs[i] == null) {
					if(contFB>70) {
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
			
			rex.graficar(entorno);
			
		} // flagRex
		
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}

	public static void intro(Entorno e) {
		e.escribirTexto("P1 GAMES", 400, 300);
	}
	
	public static void graficarPisos(Entorno e) {
		e.dibujarRectangulo(475, 120, 650, 5, 0, Color.MAGENTA); // 4to piso
		e.dibujarRectangulo(325, 230, 650, 5, 0, Color.RED); // 3to piso
		e.dibujarRectangulo(475, 340, 650, 5, 0, Color.orange); // 2to piso
		e.dibujarRectangulo(325, 450, 650, 5, 0, Color.YELLOW); // 1to piso
		e.dibujarRectangulo(400, 560, 800, 5, 0, Color.GREEN); // planta baja
	}
	
}
