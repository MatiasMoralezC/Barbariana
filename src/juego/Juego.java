package juego;

import java.awt.Color;
import java.util.Random;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;

	// Variables y métodos propios de cada grupo
	// ...

	private Barbarianna barb;
	private Raptor[] raptors;
	private T_Rex rex;

	private int contSalto, contR, contFB;

	private boolean flagBarb, flagRaptors, flagRex, flagGameOver, flagEscudo;

	Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Sakura Ikebana Delivery - Grupo 4 - v1", 800, 600);

		// Inicializar lo que haga falta para el juego
		// ...

		barb = new Barbarianna(50, 433, 25, 30, 2, 3, 'D'); // planta baja pos y 543
		contSalto = 0;
		
		// convertir los pisos a class Rectangulo

		raptors = new Raptor[1];
		contR = 100;

		rex = new T_Rex(700, 63, (float) 1.5, 'I', 7);
		contFB = 0;

		flagBarb = true;
		flagRaptors = false;
		flagRex = false;
		flagGameOver = false;
		flagEscudo = false;

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

		if (flagBarb) {

			graficarPisos(entorno);

			barb.graficar(entorno);
			
			// Grafico, Movimiento y Eliminacion(fuera de rango) de Rayo Mjolnir
			procesarRayoMjolnir();
			
			procesarEventos();

			barb.graficarVidas(entorno);
		}

		actualizarEstados();

		procesarColisiones();

		if (flagRaptors) {

			generacionRaptors();

			// Grafico, Movimiento y Eliminacion(fuera de rango) de Raptors
			procesarRaptors();

			// Grafico, Generacion, Movimiento y Eliminacion(fuera de rango) de Rayos Laser
			// en Raptors
			procesarRayosLaserRaptors();
		}

		if (flagRex) {

			actualizarMovimientoTRex();

			// Grafico, Generacion, Movimiento y Eliminacion(fuera de rango) de Fireballs en
			// T-Rex
			procesarFireballsTRex();

			rex.graficar(entorno);
		}

		if (flagGameOver) {
			entorno.cambiarFont("Old English Text MT", 95, Color.MAGENTA);
			entorno.escribirTexto("GAME OVER", 80, 320);
		}

	} // tick()

	private void procesarColisiones() {

		// Colisiones entre Raptors y Rayo Mjolnir
		for (int i = 0; i < raptors.length; i++) {
			if (raptors[i] != null && barb.getRelampago() != null) {
				if (hayColision(raptors[i].getCuerpo(), barb.getRelampago().getCuerpo())) {
					raptors[i] = null;
					barb.setRelampago(null);
				}
			}
		}

		// Colisiones entre Raptors y Barbarianna
		for (int i = 0; i < raptors.length; i++) {
			if (raptors[i] != null) {
				if (hayColision(raptors[i].getCuerpo(), barb.getCuerpo())) {
					raptors[i] = null;
					barb.setVidas(barb.getVidas() - 1);
					if (barb.getVidas() == 0) {
						flagBarb = flagRaptors = flagRex = false;
						flagGameOver = true;
					}
				}
			}
		}

		// Colisiones entre Rayos Laser de Raptors y Barbarianna
		for (Raptor raptor : raptors) {
			if (raptor != null) { // si esta activo el raptor
				if (raptor.getRayoLaser() != null) { // si esta activo su rayo laser
					if (hayColision(raptor.getRayoLaser().getCuerpo(), barb.getCuerpo())) {
						raptor.setRayoLaser(null);
						if (!flagEscudo) { // si se levanta el escudo no se pierde vidas
							barb.setVidas(barb.getVidas() - 1);
							if (barb.getVidas() == 0) {
								flagBarb = flagRaptors = flagRex = false;
								flagGameOver = true;
							}
						}
					}
				}
			}
		}

	}

	public static boolean hayColision(Rectangulo r1, Rectangulo r2) {
		return !(r1.posArriba() > r2.posAbajo() || r1.posIzquierda() > r2.posDerecha() || r1.posAbajo() < r2.posArriba()
				|| r1.posDerecha() < r2.posIzquierda());
	}

	public void procesarFireballsTRex() {
		Fireball[] fireballs = rex.getFireballs();

		for (int i = 0; i < fireballs.length; i++) {
			if (fireballs[i] == null) {
				if (contFB > 70) {
					fireballs[i] = rex.generarFireball();
					contFB = 0;
				}
			} else {
				if (fireballs[i].fueraDePantalla()) {
					fireballs[i] = null;
				} else {
					fireballs[i].mover();
					fireballs[i].graficar(entorno);
				}
			}
		}
		contFB++;
	}

	public void actualizarMovimientoTRex() {
		if (rex.getPosX() > 700)
			rex.setOrientacion('I');
		if (rex.getPosX() < 200)
			rex.setOrientacion('D');
		rex.mover();
	}

	public void procesarRayosLaserRaptors() {
		for (Raptor raptor : raptors) {
			if (raptor != null) { // si esta activo el raptor
				if (raptor.getRayoLaser() == null) { // si esta inactivo su rayo laser se lo genera
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
	}

	public void procesarRaptors() {
		for (int i = 0; i < raptors.length; i++) {
			if (raptors[i] != null) { // esta activo el raptor
				if (raptors[i].fueraDePantalla()) { // si esta fuera de pantalla, eliminarlo
					raptors[i] = null;
					contR = 100;
				} else { // si esta adentro, graficar y mover
					raptors[i].graficar(entorno);
					raptors[i].procesarMovimiento();
				}
			}
		}
	}

	public void generacionRaptors() {
		// se usa un contador para que no se amontonen al ser generados
		if (contR > 100) {
			int pos = 0;
			/*
			 * en este while se recorre las posiciones hasta encontrar un raptor activo o
			 * hasta que la posicion este fuera del rango del arreglo
			 */
			while (pos < raptors.length && raptors[pos] != null) {
				pos++;
			}
			// si la posicion es valida se genera un raptor
			if (pos < raptors.length) {
				raptors[pos] = new Raptor(840, 530, 50, 50, 2, 'I');
			}
			contR = 0;
		}
		contR++;
	}

	public void actualizarEstados() {
		barb.siempreDentroDePantalla();
		actualizarEstadoSalto();
	}

	public void actualizarEstadoSalto() {
		// cada vuelta son 3px de subida y bajada regulado por subir() y bajar()
		if (barb.getSaltando() && contSalto < 21) {
			barb.subir();
			contSalto++;
		}

		if (contSalto == 21) {
			barb.setSaltando(false);
			barb.setBajando(true);
		}

		if (barb.getBajando() && contSalto > 0) {
			barb.bajar();
			contSalto--;
		}
		
		if (contSalto == 0)
			barb.setBajando(false);
	}

	public void procesarRayoMjolnir() {
		if (barb.getRelampago() != null) {
			if (barb.getRelampago().fueraDePantalla())
				barb.setRelampago(null);
			else {
				barb.getRelampago().graficar(entorno);
				barb.getRelampago().mover();
			}
		}
	}

	public void procesarEventos() {

		if (entorno.estaPresionada(entorno.TECLA_DERECHA) && !flagEscudo && !barb.getAgachada()) {
			barb.setOrientacion('D');
			barb.mover();
		}

		if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && !flagEscudo && !barb.getAgachada()) {
			barb.setOrientacion('I');
			barb.mover();
		}

		if (entorno.sePresiono(entorno.TECLA_ARRIBA) && !barb.getSaltando() && !barb.getBajando() && !flagEscudo && !barb.getAgachada())
			barb.setSaltando(true);
		
		if (entorno.estaPresionada(entorno.TECLA_ABAJO)) {
			barb.setAgachada(true);
			barb.getCuerpo().setAlto(20);
		}
		else {
			barb.setAgachada(false);
			barb.getCuerpo().setAlto(30);
		}

		if (entorno.sePresiono(entorno.TECLA_ESPACIO) && !flagEscudo) {
			if (barb.getRelampago() == null && barb.getX() > 25 && barb.getX() < 775)
				barb.generarRelampago();
		}

		if (entorno.estaPresionada(entorno.TECLA_SHIFT))
			flagEscudo = true;
		else
			flagEscudo = false;

	}

	public static void graficarPisos(Entorno e) {
		e.dibujarRectangulo(475, 120, 650, 5, 0, Color.MAGENTA); // 4to piso
		e.dibujarRectangulo(325, 230, 650, 5, 0, Color.RED); // 3to piso
		e.dibujarRectangulo(475, 340, 650, 5, 0, Color.orange); // 2to piso
		e.dibujarRectangulo(325, 450, 650, 5, 0, Color.YELLOW); // 1to piso
		e.dibujarRectangulo(400, 560, 800, 5, 0, Color.GREEN); // planta baja
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}

}
