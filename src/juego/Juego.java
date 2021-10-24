package juego;

import java.awt.Color;
import java.util.Random;
import entorno.Herramientas;

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
	private Rectangulo[] pisos;
	
	private java.awt.Image fondoImagen;
	private java.awt.Image pisosImagen;
	private java.awt.Image pisoImagen;

	private int contSalto, contSuperSalto, contR, contFB, puntaje;

	private boolean flagBarb, flagRaptors, flagRex, flagGameOver, flagInmune;
	private boolean hayColisionConPisosBarbarianna, hayColisionConPisosRaptor;

	Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Sakura Ikebana Delivery - Grupo 4 - v1", 800, 600);

		// Inicializar lo que haga falta para el juego
		// ...

		construirPisos();
		fondoImagen=Herramientas.cargarImagen("fondo.png");
		pisoImagen=Herramientas.cargarImagen("piso.png");
		pisosImagen=Herramientas.cargarImagen("pisos.png");

		barb = new Barbarianna(50, 540, 25, 30, 2, 3, 'D'); // planta baja (50,540)
		contSalto = 0;
		contSuperSalto = 0;
		puntaje = 0;

		raptors = new Raptor[6];
		contR = 100;

		rex = new T_Rex(700, 63, (float) 1.5, 'I', 7);
		contFB = 0;

		flagBarb = true;
		flagRaptors = false;
		flagRex = true;
		flagGameOver = false;
		flagInmune = false;

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

		// TECLAS: Shift: Escudo. Ctrl: super salto (solo debajo de un hueco)

		if (flagBarb) {

			entorno.dibujarImagen(this.fondoImagen, 400, 300, 0);
			graficarPisos();

			barb.graficar(entorno);
			barb.graficarVidas(entorno);
			
			entorno.cambiarFont("Bauhaus 93", 14, Color.WHITE);
			entorno.escribirTexto("SCORE: "+puntaje, 340, 585);

			// Grafico, Movimiento y Eliminacion(fuera de rango) de Rayo Mjolnir
			procesarRayoMjolnir();

			procesarEventos();

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

			rex.procesarMovimiento();

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

		if (flagBarb) {
			
			// Colision entre Barbarianna y Pisos (simulador de gravedad)
			hayColisionConPisosBarbarianna = false;
			for (Rectangulo piso : pisos) {
				if (hayColision(barb.getCuerpo(), piso)) { // si hay colision
					hayColisionConPisosBarbarianna = true;
					// al querer atravesar el piso, repele hacia arriba
					if (barb.getCuerpo().posAbajo() >= piso.posArriba()) {
						barb.setY(piso.posArriba() - 15);
						barb.getCuerpo().setY(piso.posArriba() - 10);
					}

				}
			}
			if (!hayColisionConPisosBarbarianna) { // si no hay colision con los pisos cae
				barb.caer();
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
								raptor.setY(piso.posArriba() - 25);
							}
						}
					}
					if (!hayColisionConPisosRaptor) {
						raptor.caer();
					}
				}
			}

			// Colisiones entre Raptors y Rayo Mjolnir
			for (int i = 0; i < raptors.length; i++) {
				if (raptors[i] != null && barb.getRelampago() != null) {
					if (hayColision(raptors[i].getCuerpo(), barb.getRelampago().getCuerpo())) {
						raptors[i] = null;
						barb.setRelampago(null);
						puntaje+=100;
					}
				}
			}

			// Colisiones entre Raptors y Barbarianna
			for (int i = 0; i < raptors.length; i++) {
				if (raptors[i] != null) {
					if (hayColision(raptors[i].getCuerpo(), barb.getCuerpo())) {
						raptors[i] = null;
						barb.setVidas(barb.getVidas() - 1);	// quitar vida
						if (barb.getVidas() == 0) { // si las vidas es igual a cero, game over!
							flagBarb = flagRaptors = flagRex = false;
							flagGameOver = true;
						}
						if(puntaje>=100)
							puntaje-=100; // restar puntaje
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
								if(puntaje>=100)
									puntaje-=100; // restar puntaje
							}
						}
					}
				}
			}
		}
		
		if(flagRex) {
			
			// Colisiones entre Fireballs y Barbarianna
			Fireball[] fireballs = rex.getFireballs();
			for(int i=0; i<fireballs.length; i++) {
				if(fireballs[i]!=null) {
					if (hayColision(fireballs[i].getCuerpo(),barb.getCuerpo())) {
						fireballs[i]=null;
						barb.setVidas(barb.getVidas() - 1); // quitar vida
						if (barb.getVidas() == 0) { // si las vidas es igual a cero, game over!
							flagBarb = flagRaptors = flagRex = false;
							flagGameOver = true;
						}
						if(puntaje>=100)
							puntaje-=100; // restar puntaje
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
					fireballs[i].getCuerpo().dibujar(entorno);
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
				raptors[pos] = new Raptor(840, 70, 50, 50, 3, 'I');
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

		if (entorno.estaPresionada(entorno.TECLA_DERECHA) && !flagInmune && !barb.getAgachada()) {
			barb.setOrientacion('D');
			barb.mover();
		}

		if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && !flagInmune && !barb.getAgachada()) {
			barb.setOrientacion('I');
			barb.mover();
		}

		if (entorno.sePresiono(entorno.TECLA_ARRIBA) && !barb.getSaltando() && !barb.getEscudo() && !barb.getAgachada()
				&& hayColisionConPisosBarbarianna)
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

		if (barb.getEscudo())
			flagInmune = true;
		else
			flagInmune = false;

		if (entorno.sePresiono(entorno.TECLA_ESPACIO) && !barb.getEscudo() && !barb.getAgachada()) {
			if (barb.getRelampago() == null && barb.getX() > 25 && barb.getX() < 775)
				barb.generarRelampago();
		}

		if (entorno.estaPresionada(entorno.TECLA_CTRL) && barb.estaDebajoDeUnHueco(pisos) && !barb.getSuperSaltando()
				&& !flagInmune && !barb.getAgachada() && hayColisionConPisosBarbarianna)
			barb.setSuperSaltando(true);

	}

	public void graficarPisos() {																												// baja
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

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}

}
