package juego;

import java.awt.Color;
import entorno.Herramientas;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;

	// Variables y métodos propios de cada grupo

	private Barbarianna barb;
	private Raptor[] raptors;
	private T_Rex rex;
	private Rectangulo[] pisos;
	private Computadora commodore;
	private Vida[] vidas;
	private BonoPuntaje[] bonos;

	// Multimedia
	private java.awt.Image fondoImagen;
	private java.awt.Image pisosImagen;
	private java.awt.Image pisoImagen;

	// Contadores - contR(raptors) - contFB(fireballs)
	private int contSalto, contSuperSalto, contadorVueltasRaptors, cantVueltasRaptors;
	private int contFB, contEnemigosEliminados, puntaje, selectorNivel;

	// Flags de activación y colisiones
	private boolean flagCastillo, flagBarb, flagRaptors, flagRex, flagGameOver, flagGanaste;
	private boolean hayColisionConPisosBarbarianna, hayColisionConPisosRaptor;

	Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Castlevania, Barbarianna Viking Edition - Grupo 4", 800, 600);

		// Inicializar lo que haga falta para el juego

		construirPisos(); // Inicializa y declara los elementos del array de pisos.
		fondoImagen = Herramientas.cargarImagen("fondo.png");
		pisoImagen = Herramientas.cargarImagen("piso.png");
		pisosImagen = Herramientas.cargarImagen("pisos.png");

		// Config. de Barbarianna
		barb = new Barbarianna(50, 540, 25, 30, 2, 3, 'D'); // planta baja (50,540)
		contSalto = 0;
		contSuperSalto = 0;
		contEnemigosEliminados = 0;

		// Config. de Raptors - max raptors en pantalla: 6
		raptors = new Raptor[6];
		cantVueltasRaptors = 250; // cant vueltas tick se genera un raptor
		contadorVueltasRaptors = 100; // mayor a cero para arranque rápido

		// Config. T Rex.
		rex = new T_Rex(700, 63, 120, 100, 2, 10, 7, 'I');
		contFB = 0;

		// Config. Computadora Commodore 128kb
		commodore = new Computadora(720, 90, 40, 40);

		// Config. Nivel
		puntaje = 0;
		selectorNivel = 0;
		vidas = new Vida[2];
		vidas[0] = new Vida(100, 270, 20, 20);
		vidas[1] = new Vida(750, 180, 20, 20);
		bonos = new BonoPuntaje[2];
		bonos[0] = new BonoPuntaje(750, 380, 20, 20);
		bonos[1] = new BonoPuntaje(100, 50, 20, 20);

		// Flags del juego
		flagBarb = true;
		flagRaptors = false;
		flagRex = false;
		flagCastillo = true;
		flagGameOver = false;
		flagGanaste = false;

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
		// TECLAS: Shift: Escudo. Ctrl: super salto (solo debajo de un hueco)

		// Grafico de fondo, pisos y otros
		if (flagCastillo) {
			entorno.dibujarImagen(this.fondoImagen, 400, 300, 0);
			graficarPisos();
			commodore.graficar(entorno);
			entorno.cambiarFont("Impact", 14, Color.GREEN);
			entorno.escribirTexto("SCORE: " + puntaje, 300, 585);
			entorno.escribirTexto("KILLS: " + contEnemigosEliminados, 450, 585);
		}

		// Funcionalidades de Barbarianna
		if (flagBarb) {

			barb.graficar(entorno);
			barb.graficarVidas(entorno);

			// Grafico, Movimiento y Eliminacion(fuera de pantalla) de Rayo Mjolnir
			procesarRayoMjolnir();

			procesarEventos(); // Eventos de teclado de Barbarianna

			barb.siempreDentroDePantalla();
			actualizarEstadoSalto(); // Salto de Barbarianna
		}

		// Procesamiento de colisiones en general.
		procesarColisiones();

		if (flagRaptors) {

			generacionRaptors();

			// Grafico, Movimiento y Eliminacion(fuera de pantalla) de Raptors
			procesarRaptors();

			// Grafico, Generacion, Movimiento y Eliminacion(fuera de pantalla) de Rayos Laser
			// en Raptors
			procesarRayosLaserRaptors();
		}

		if (flagRex) {
			if (rex != null) {
				rex.procesarMovimiento();

				// Grafico, Generacion, Movimiento y Eliminacion(fuera de rango) de Fireballs en
				// T-Rex
				procesarFireballsTRex();

				rex.graficar(entorno);
			}

		}

		if (flagGameOver) {
			entorno.cambiarFont("Old English Text MT", 95, Color.WHITE);
			entorno.escribirTexto("GAME OVER", 80, 320);
		}

		if (flagGanaste) {
			barb.graficar(entorno); // se grafica la ultima posicion de Barbarianna
			entorno.cambiarFont("Old English Text MT", 95, Color.WHITE);
			entorno.escribirTexto("¡ GANASTE !", 80, 210);
			selectorNivel();
		}

	} // tick()

	/*
	 * Utiliza el metodo hayColision para verificar colision entre objectos
	 * Rectangulos y ejecuta las acciones correspondientes.
	 * 
	 */

	private void procesarColisiones() {

		if (flagBarb) {

			// Colision entre Barbarianna y Pisos (simulador de gravedad)
			hayColisionConPisosBarbarianna = false;
			for (Rectangulo piso : pisos) {
				if (hayColision(barb.getCuerpo(), piso)) { // si hay colision
					hayColisionConPisosBarbarianna = true;
					// al querer atravesar el piso, repele hacia arriba
					if (barb.getCuerpo().posAbajo() >= piso.posArriba()) { // fix bug
						barb.setY(piso.posArriba() - 15);
						barb.getCuerpo().setY(piso.posArriba() - 10);
					}

				}
			}
			if (!hayColisionConPisosBarbarianna) { // si no hay colision con los pisos cae
				barb.caer();
			}

			// Colision entre Barbarianna y la Computadora
			if (hayColision(barb.getCuerpo(), commodore.getCuerpo())) {
				puntaje += 5000;
				flagBarb = flagRaptors = flagRex = false;
				flagGanaste = true;
				raptors = null;
				rex = null;
			}

			// Colision entre Barbarianna y Vidas
			for (int i = 0; i < vidas.length; i++) {
				if (vidas[i] != null) {
					if (hayColision(barb.getCuerpo(), vidas[i].getCuerpo())) {
						barb.setVidas(barb.getVidas() + 1);
						vidas[i] = null;
					} else {
						vidas[i].graficar(entorno);
					}
				}
			}
			
			// Colision entre Barbarianna y Bonos
			for (int i = 0; i < bonos.length; i++) {
				if (bonos[i] != null) {
					if (hayColision(barb.getCuerpo(), bonos[i].getCuerpo())) {
						puntaje+=1000;
						bonos[i] = null;
					} else {
						bonos[i].graficar(entorno);
					}
				}
			}

		}

		if (flagRaptors) {

			// Colision entre Raptors y Pisos (simulador de gravedad)
			for (Raptor raptor : raptors) {
				if (raptor != null) {
					hayColisionConPisosRaptor = false;
					for (Rectangulo piso : pisos) {
						if (hayColision(raptor.getCuerpo(), piso)) {
							hayColisionConPisosRaptor = true;
							// al querer atravesar el piso, repele hacia arriba
							if (raptor.getCuerpo().posAbajo() >= piso.posArriba()) {
								raptor.setY(piso.posArriba() - 25); // fix bug
							}
						}
					}
					if (!hayColisionConPisosRaptor) {
						raptor.caer();
					}
				}
			}

			// Colisiones entre Raptors y Relampago del Mjolnir
			for (int i = 0; i < raptors.length; i++) {
				if (raptors[i] != null && barb.getRelampago() != null) {
					if (hayColision(raptors[i].getCuerpo(), barb.getRelampago().getCuerpo())) {
						raptors[i] = null;
						barb.setRelampago(null);
						contEnemigosEliminados++;
						puntaje += 100;
					}
				}
			}

			// Colisiones entre Raptors y Barbarianna
			for (int i = 0; i < raptors.length; i++) {
				if (raptors[i] != null) {
					if (hayColision(raptors[i].getCuerpo(), barb.getCuerpo())) {
						raptors[i] = null;
						barb.setVidas(barb.getVidas() - 1); // quitar vida
						if (barb.getVidas() == 0) { // si las vidas es igual a cero, game over!
							flagBarb = flagRaptors = flagRex = false;
							flagGameOver = true;
						}
						if (puntaje >= 100)
							puntaje -= 100; // restar puntaje
					}
				}
			}

			// Colisiones entre Rayos Laser de Raptors y Barbarianna
			for (Raptor raptor : raptors) {
				if (raptor != null) { // si esta activo el raptor
					if (raptor.getRayoLaser() != null) { // si esta activo su rayo laser
						if (hayColision(raptor.getRayoLaser().getCuerpo(), barb.getCuerpo())) {
							raptor.setRayoLaser(null);
							if (!barb.getEscudo()) { // si se levanta el escudo no se pierde vidas, ni puntaje
								barb.setVidas(barb.getVidas() - 1); // quitar vida
								if (barb.getVidas() == 0) { // si las vidas es igual a cero, game over!
									flagBarb = flagRaptors = flagRex = false;
									flagGameOver = true;
								}
								if (puntaje >= 100)
									puntaje -= 100; // restar puntaje
							}
						}
					}
				}
			}
		}

		if (flagRex) {

			// Colisiones entre Fireballs y Barbarianna
			if (rex != null) {
				Fireball[] fireballs = rex.getFireballs();
				for (int i = 0; i < fireballs.length; i++) {
					if (fireballs[i] != null) {
						if (hayColision(fireballs[i].getCuerpo(), barb.getCuerpo())) {
							fireballs[i] = null;
							barb.setVidas(barb.getVidas() - 1); // quitar vida
							if (barb.getVidas() == 0) { // si las vidas es igual a cero, game over!
								flagBarb = flagRaptors = flagRex = false;
								flagGameOver = true;
							}
							if (puntaje >= 100)
								puntaje -= 100; // restar puntaje
						}
					}
				}

				// Colisiones entre TRex y Barbarianna
				if (hayColision(rex.getCuerpo(), barb.getCuerpo())) {
					barb.setVidas(barb.getVidas() - 1); // quitar vida
					if (barb.getVidas() == 0) { // si las vidas es igual a cero, game over!
						flagBarb = flagRaptors = flagRex = false;
						flagGameOver = true;
					}
					if (puntaje >= 100)
						puntaje -= 100; // restar puntaje
				}

				// Colisiones entre TRex y Rayo Mjolnir
				if (barb.getRelampago() != null) {
					if (hayColision(rex.getCuerpo(), barb.getRelampago().getCuerpo())) {
						barb.setRelampago(null);
						rex.setVidas(rex.getVidas() - 1); // quitar vida
						if (rex.getVidas() == 0) {
							rex = null;
							puntaje += 5000;
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
					contadorVueltasRaptors = 100;
				} else { // si esta adentro, graficar y mover
					raptors[i].graficar(entorno);
					raptors[i].procesarMovimiento();
				}
			}
		}
	}

	public void generacionRaptors() {
		// se usa un contador para que no se amontonen al ser generados
		if (contadorVueltasRaptors > cantVueltasRaptors) {
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
				raptors[pos] = new Raptor(840, 85, 50, 50, 3, 'I');
			}
			contadorVueltasRaptors = 0;
		}
		contadorVueltasRaptors++;
	}

	// Procesamiento y control de salto de Barbarianna.
	public void actualizarEstadoSalto() {
		int cantSalto = 23;
		if (barb.getSaltando() && contSalto < cantSalto) {
			barb.saltar();
			contSalto++;
		}
		if (contSalto == cantSalto) {
			barb.setSaltando(false);
			contSalto = 0;
		}

		int cantSuperSalto = 50;
		if (barb.getSuperSaltando() && contSuperSalto < cantSuperSalto) {
			barb.saltar();
			contSuperSalto++;
		}

		if (!barb.estaDebajoDeUnHueco(pisos))
			contSuperSalto = cantSuperSalto;

		if (contSuperSalto == cantSuperSalto) {
			barb.setSuperSaltando(false);
			contSuperSalto = 0;
		}

	}

	/*
	 * Verifica si el rayo está dentro de pantalla, de ser asi lo grafica y mueve.
	 * En caso contrario, lo elimina.
	 */
	public void procesarRayoMjolnir() {
		if (barb.getRelampago() != null) {
			if (barb.getRelampago().fueraDePantalla())
				barb.setRelampago(null);
			else {
				barb.getRelampago().graficar(entorno);
				barb.getRelampago().mover();
			}
		} else {
			graficarRelampagoListo();
		}
	}

	// Control y procesamiento de eventos de Barbarianna
	public void procesarEventos() {

		if (entorno.estaPresionada(entorno.TECLA_DERECHA) && !barb.getEscudo() && !barb.getAgachada()) {
			barb.setOrientacion('D');
			barb.mover();
		}

		if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && !barb.getEscudo() && !barb.getAgachada()) {
			barb.setOrientacion('I');
			barb.mover();
		}

		if (entorno.sePresiono(entorno.TECLA_ARRIBA) && !barb.getSaltando() && !barb.getSuperSaltando()
				&& !barb.getEscudo() && !barb.getAgachada() && hayColisionConPisosBarbarianna)
			barb.setSaltando(true);

		if (entorno.estaPresionada(entorno.TECLA_ABAJO)) {
			barb.setAgachada(true);
			barb.getCuerpo().setAlto(20);
		} else {
			barb.setAgachada(false);
			barb.getCuerpo().setAlto(30);
		}

		if (entorno.estaPresionada(entorno.TECLA_SHIFT))
			barb.setEscudo(true);
		else
			barb.setEscudo(false);

		if (entorno.sePresiono(entorno.TECLA_ESPACIO) && !barb.getEscudo() && !barb.getAgachada()) {
			if (barb.getRelampago() == null && barb.getX() > 25 && barb.getX() < 775)
				barb.generarRelampago();
		}
		if (entorno.estaPresionada(entorno.TECLA_CTRL) && barb.estaDebajoDeUnHueco(pisos) && !barb.getSuperSaltando()
				&& !barb.getEscudo() && !barb.getAgachada() && !barb.getSaltando() && hayColisionConPisosBarbarianna)
			barb.setSuperSaltando(true);

	}

	public void graficarPisos() { // baja
		entorno.dibujarImagen(pisosImagen, 480, 120, 0); // piso4
		entorno.dibujarImagen(pisosImagen, 320, 230, 0); // piso3
		entorno.dibujarImagen(pisosImagen, 480, 340, 0); // piso2
		entorno.dibujarImagen(pisosImagen, 320, 450, 0); // piso1
		entorno.dibujarImagen(pisoImagen, 400, 588, 0); // planta baja
	}

	public void construirPisos() {
		pisos = new Rectangulo[5];
		pisos[4] = new Rectangulo(475, 120, 650, 5);
		pisos[3] = new Rectangulo(325, 230, 650, 5);
		pisos[2] = new Rectangulo(475, 340, 650, 5);
		pisos[1] = new Rectangulo(325, 450, 650, 5);
		pisos[0] = new Rectangulo(400, 560, 800, 5);
	}

	public void graficarRelampagoListo() {
		if (Math.sin(0.1 * barb.getX()) > 0) {
			entorno.dibujarImagen(Herramientas.cargarImagen("rayo1Der.png"), 700, 580, 0.2, 0.09);
		} else {
			entorno.dibujarImagen(Herramientas.cargarImagen("rayo2Der.png"), 700, 580, -0.1, 0.09);
		}
	}

	public void selectorNivel() {
		entorno.cambiarFont("Impact", 27, Color.GREEN);
		entorno.escribirTexto("Selecciona un nivel", 290, 395);
		entorno.escribirTexto("<-----                         ----->", 285, 420);
		if (selectorNivel == 0) {
			entorno.cambiarFont("Impact", 37, Color.RED);
			entorno.escribirTexto("T-REX", 140, 400);
			entorno.cambiarFont("Impact", 37, Color.DARK_GRAY);
			entorno.escribirTexto("INFIERNO", 570, 400);
			if (entorno.sePresiono(entorno.TECLA_DERECHA))
				selectorNivel = 1;
			else if (entorno.sePresiono(entorno.TECLA_ENTER)) {
				flagGanaste = false;
				flagBarb = flagRex = true;
				barb = new Barbarianna(50, 540, 25, 30, 2, 3, 'D');
				rex = new T_Rex(700, 63, 120, 100, 2, 10, 7, 'I');
			}
		}
		if (selectorNivel == 1) {
			entorno.cambiarFont("Impact", 37, Color.DARK_GRAY);
			entorno.escribirTexto("T-REX", 140, 400);
			entorno.cambiarFont("Impact", 37, Color.RED);
			entorno.escribirTexto("INFIERNO", 570, 400);
			if (entorno.sePresiono(entorno.TECLA_IZQUIERDA))
				selectorNivel = 0;
			else if (entorno.sePresiono(entorno.TECLA_ENTER)) {
				flagGanaste = false;
				flagBarb = flagRaptors = flagRex = true;
				barb = new Barbarianna(50, 540, 25, 30, 2, 3, 'D');
				rex = new T_Rex(700, 63, 120, 100, 2, 10, 7, 'I');
				raptors = new Raptor[6];
				cantVueltasRaptors = 100;
			}
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}

}
