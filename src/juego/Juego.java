package juego;

import java.awt.Color;

import javax.sound.sampled.Clip;

import entorno.Herramientas;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;

	// Variables y métodos propios de cada grupo

	private Barbarianna barb;
	private Relampago relampago;
	private Raptor[] raptors;
	private RayoLaser[] rayosLaser;
	private T_Rex rex;
	private Fireball[] fireballs;
	private Rectangulo[] pisos;
	private Computadora commodore;
	private Vida[] vidas;
	private BonoPuntaje[] bonos;
	private Mina[] minas;

	// Multimedia
	private java.awt.Image fondoImagen;
	private java.awt.Image pisosImagen;
	private java.awt.Image pisoImagen;

	// Contadores - contR(raptors) - contFB(fireballs)
	private int contSalto, contSuperSalto, contadorVueltasRaptors, cantVueltasRaptors, contDaño;
	private int contFB, contEnemigosEliminados, puntaje, selectorNivel;
	private int[] contsRayosRaptors; 

	// Flags de activación y colisiones
	private boolean flagCastillo, flagBarb, flagRaptors, flagRex, flagGameOver, flagGanaste;
	private boolean hayColisionConPisosBarbarianna, hayColisionConPisosRaptor;
	private boolean flagRoarSound, flagGameOverSound;

	Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Castlevania, Barbarianna Viking Edition - Grupo 4", 800, 600);

		// Inicializar lo que haga falta para el juego

		construirPisos(); // Inicializa y declara los elementos del array de pisos.
		fondoImagen = Herramientas.cargarImagen("images/fondo.png");
		pisoImagen = Herramientas.cargarImagen("images/piso.png");
		pisosImagen = Herramientas.cargarImagen("images/pisos.png");

		// Config. de Barbarianna
		barb = new Barbarianna(50, 540, 25, 30, 2, 3, 'D'); // planta baja (50,540)
		contSalto = 0;
		contSuperSalto = 0;
		contEnemigosEliminados = 0;

		// Config. de Raptors - max raptors en pantalla: 6
		raptors = new Raptor[6];
		rayosLaser = new RayoLaser[6];
		cantVueltasRaptors = 200; // cant vueltas tick se genera un raptor
		contadorVueltasRaptors = 100; // mayor a cero para arranque rápido
		contsRayosRaptors = new int[]{0,0,0,0,0,0};

		// Config. T Rex.
		rex = new T_Rex(700, 63, 120, 100, 2, 3, 'I');
		fireballs = new Fireball[7]; // max fireballs en pantalla: 7
		contFB = 0;
		flagRoarSound = true;

		// Config. Computadora Commodore 128kb
		commodore = new Computadora(720, 90, 40, 40);

		// Config. Nivel
		puntaje = 0;
		selectorNivel = 0;
		flagGameOverSound = true;
		vidas = new Vida[2];
		vidas[0] = new Vida(70, 270, 20, 20);
		vidas[1] = new Vida(730, 160, 20, 20);
		bonos = new BonoPuntaje[2];
		bonos[0] = new BonoPuntaje(730, 380, 20, 20);
		bonos[1] = new BonoPuntaje(70, 50, 20, 20);
		minas = new Mina[5];
		minas[0] = new Mina(750,550,15,10);
		minas[1] = new Mina(400,440,15,10);
		minas[2] = new Mina(50,440,15,10);
		minas[3] = new Mina(750,330,15,10);
		minas[4] = new Mina(50,220,15,10);

		// Flags del juego
		flagBarb = true;
		flagRaptors = true;
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
			entorno.dibujarImagen(fondoImagen, 400, 300, 0);
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

			// Grafico, Movimiento y Eliminacion(fuera de pantalla) de Relampago Mjolnir
			procesarRelampagoMjolnir();

			procesarEventos();

			barb.siempreDentroDePantalla();
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

				rex.graficar(entorno);
				
				if(flagRoarSound) {
					Herramientas.play("sounds/trex-roar.wav");
					flagRoarSound=false;
				}
			}
			
			// Grafico, Generacion, Movimiento y Eliminacion(fuera de rango) de Fireballs en
			// T-Rex
			procesarFireballsTRex();

		}

		if (flagGameOver) {
			entorno.cambiarFont("Old English Text MT", 95, Color.WHITE);
			entorno.escribirTexto("GAME OVER", 80, 320);
			if(flagGameOverSound) {
				Herramientas.play("sounds/game-over.wav");
				flagGameOverSound = false;
			}
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
				Herramientas.play("sounds/win.wav");
			}

			// Colision entre Barbarianna y Vidas
			for (int i = 0; i < vidas.length; i++) {
				if (vidas[i] != null) {
					if (hayColision(barb.getCuerpo(), vidas[i].getCuerpo())) {
						barb.setVidas( barb.getVidas() + 1);
						vidas[i] = null;
						Herramientas.play("sounds/take.wav");
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
						Herramientas.play("sounds/take.wav");
					} else {
						bonos[i].graficar(entorno);
					}
				}
			}
			
			// Colision entre Barbarianna y Minas
			for (int i = 0; i < minas.length; i++) {
				if (minas[i] != null) {
					if (hayColision(barb.getCuerpo(), minas[i].getCuerpo())) {
						barb.setVidas( barb.getVidas() - 1);
						minas[i] = null;
						Herramientas.play("sounds/explosion.wav");
					} else {
						minas[i].graficar(entorno);
					}
				}
			}
			
			// Si las vidas perdidas por colisiones es igual a cero, game over!
			if (barb.getVidas() == 0) { 
				flagBarb = flagRaptors = flagRex = false;
				flagGameOver = true;
			}
			
			// Animacion daño recibido
			actualizarEstadoDaño();

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
				if (raptors[i] != null && relampago != null) {
					if (hayColision(raptors[i].getCuerpo(), relampago.getCuerpo())) { // si hay colision
						Herramientas.play("sounds/raptor.wav"); // sonido raptor rip
						raptors[i] = null; // se elimina el raptor
						relampago = null; // se elimina el relampago
						contEnemigosEliminados++;
						puntaje += 100;
					}
				}
			}

			// Colisiones entre Raptors y Barbarianna
			for (int i = 0; i < raptors.length; i++) {
				if (raptors[i] != null) {
					if (hayColision(raptors[i].getCuerpo(), barb.getCuerpo())) {
						
						raptors[i] = null; // se elimina el raptor
						barb.setVidas(barb.getVidas() - 1); // quitar vida a barb
						
						if (puntaje >= 100)
							puntaje -= 100;
						
						barb.setDaño(true); // para animacion de daño recibido
						Herramientas.play("sounds/damage.wav");
					}
				}
			}

			// Colisiones entre Rayos Laser de Raptors y Barbarianna
			for (int i=0; i<rayosLaser.length; i++) {
				if (rayosLaser[i] != null) { // si hay instancia de su rayo laser
					if (hayColision(rayosLaser[i].getCuerpo(), barb.getCuerpo())) {
						
						rayosLaser[i] = null; // se elimina su rayo laser
						if (!barb.getEscudo()) { // si se levanta el escudo no se pierde vidas, ni puntaje
							barb.setVidas(barb.getVidas() - 1); // quitar vida
							
							if (puntaje >= 100)
								puntaje -= 100;
							
							barb.setDaño(true); // para animacion de daño recibido
							Herramientas.play("sounds/damage.wav");
						}
					}
				}
			}
		}

		if (flagRex) {

			// Colisiones entre Fireballs y Barbarianna
			if (rex != null) {
				for (int i = 0; i < fireballs.length; i++) {
					if (fireballs[i] != null) { // si hay instancia de fireball
						if (hayColision(fireballs[i].getCuerpo(), barb.getCuerpo())) {
							
							fireballs[i] = null; // se elimina
							barb.setVidas(barb.getVidas() - 1); // quitar vida
							
							if (puntaje >= 100)
								puntaje -= 100; // restar puntaje
							
							barb.setDaño(true);
							Herramientas.play("sounds/damage.wav");
						}
					}
				}

				// Colisiones entre TRex y Barbarianna
				if (hayColision(rex.getCuerpo(), barb.getCuerpo())) {
					barb.setVidas(barb.getVidas() - 1); // quitar vida

					if (puntaje >= 100)
						puntaje -= 100; // restar puntaje
				}

				// Colisiones entre TRex y Relampago Mjolnir
				if (relampago != null) { // si hay instancia de relampago 
					if (hayColision(rex.getCuerpo(), relampago.getCuerpo())) {
						relampago = null; // eliminar relampago
						rex.setVidas(rex.getVidas() - 1); // quitar vida
						if (rex.getVidas() == 0) { // si vidas=0, se elimina al TRex
							Herramientas.play("sounds/trex-rip.wav"); // sonido muerte
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
		for (int i = 0; i < fireballs.length; i++) {
			if (fireballs[i] == null) { // si no hay instancia
				if (contFB > 70 && rex != null) { // un contador para que no se amontonen
					fireballs[i] = rex.generarFireball();
					contFB = 0;
				}
			} else { // si hay instancia
				if (fireballs[i].fueraDePantalla()) { // si esta fuera de pantalla se elimina
					fireballs[i] = null;
				} else { // si no esta fuera de pantalla se mueve y se grafica
					fireballs[i].mover();
					fireballs[i].graficar(entorno);
				}
			}
		}
		contFB++;
	}

	public void procesarRayosLaserRaptors() {
		for (int i=0; i<raptors.length; i++) {
			
			if (raptors[i] != null) { // si hay instancia de raptor
				if (rayosLaser[i] == null) { // si no hay instancia de su rayo laser
					if(contsRayosRaptors[i]>100) { // cont para cada raptor para no amontonar rayos
						rayosLaser[i] = raptors[i].generarRayoLaser();
						contsRayosRaptors[i]=0;
					}
				}
			}
			
			if(rayosLaser[i] != null) { // si hay instancia de su rayo laser
				if (rayosLaser[i].fueraDePantalla()) { // si esta fuera de pantalla se elimina
					rayosLaser[i] = null;
				} else { // si esta dentro de pantalla se grafica y se mueve
					rayosLaser[i].mover();
					rayosLaser[i].graficar(entorno);
				}
			}
			contsRayosRaptors[i]++;
		}
		
	}

	public void procesarRaptors() {
		for (int i = 0; i < raptors.length; i++) {
			if (raptors[i] != null) { // si hay instancia de raptor
				if (raptors[i].fueraDePantalla()) { // si esta fuera de pantalla se elimina
					raptors[i] = null;
					contadorVueltasRaptors = 100; // se aumenta el contador para generar otro rapidamente
				} else { // si no esta fuera de pantalla se grafica y se mueve
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
			// en este while se recorre las posiciones hasta encontrar un raptor activo o
			// hasta que la posicion este fuera del rango del arreglo
			while (pos < raptors.length && raptors[pos] != null) {
				pos++;
			}
			// si la posicion es valida se genera un raptor
			if (pos < raptors.length) {
				raptors[pos] = new Raptor(840, 90, 50, 50, 2, 'I');
			}
			contadorVueltasRaptors = 0;
		}
		contadorVueltasRaptors++;
	}

	//Al recibir daño barbariana, muestra la animacion durante 50 tics y luego vuelve a la animacion normal
	public void actualizarEstadoDaño() {
		int cantDaño = 50;
		if (barb.getDaño() && contDaño < cantDaño) {
			contDaño++;
		}
		if (contDaño == cantDaño) {
			barb.setDaño(false);
			contDaño = 0;
		}
	}
	
	// Procesamiento y control de salto de Barbarianna.
	public void actualizarEstadoSalto() {
		int cantSalto = 20;
		if (barb.getSaltando() && contSalto < cantSalto) {
			barb.saltar();
			contSalto++;
		}
		if (contSalto == cantSalto) {
			barb.setSaltando(false);
			contSalto = 0;
		}

		int cantSuperSalto = 40;
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
	 * Verifica si el relampago está dentro de pantalla, de ser asi lo grafica y mueve.
	 * En caso contrario, lo elimina.
	 */
	public void procesarRelampagoMjolnir() {
		if (relampago != null) { // si hay instancia
			if (relampago.fueraDePantalla()) // y el relampago fuera de pantalla lo elimina
				relampago = null;
			else { // sino esta fuera de pantalla
				relampago.graficar(entorno);
				relampago.mover();
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
		actualizarEstadoSalto();

		if (entorno.estaPresionada(entorno.TECLA_ABAJO)) {
			barb.setAgachada(true);
			barb.getCuerpo().setAlto(20); // se achica el hitbox porque esta agachada
		} else {
			barb.setAgachada(false);
			barb.getCuerpo().setAlto(30); // se vuelve al hitbox normal
		}

		if (entorno.estaPresionada(entorno.TECLA_SHIFT))
			barb.setEscudo(true);
		else
			barb.setEscudo(false);

		if (entorno.sePresiono(entorno.TECLA_ESPACIO) && !barb.getEscudo() && !barb.getAgachada()) {
			if (relampago == null && barb.getX() > 25 && barb.getX() < 775) {
				relampago = barb.generarRelampago();
				Herramientas.play("sounds/thunder.wav");
			}
				
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
			entorno.dibujarImagen(Herramientas.cargarImagen("images/rayo1Der.png"), 700, 580, 0.2, 0.09);
		} else {
			entorno.dibujarImagen(Herramientas.cargarImagen("images/rayo2Der.png"), 700, 580, -0.1, 0.09);
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
			if (entorno.sePresiono(entorno.TECLA_DERECHA)) {
				selectorNivel = 1;
				Herramientas.play("sounds/selector.wav");
			}
		}
		
		if (selectorNivel == 1) {
			entorno.cambiarFont("Impact", 37, Color.DARK_GRAY);
			entorno.escribirTexto("T-REX", 140, 400);
			entorno.cambiarFont("Impact", 37, Color.RED);
			entorno.escribirTexto("INFIERNO", 570, 400);
			if (entorno.sePresiono(entorno.TECLA_IZQUIERDA)) {
				selectorNivel = 0;
				Herramientas.play("sounds/selector.wav");
			}
		}
		
		if (entorno.sePresiono(entorno.TECLA_ENTER)) {
			flagGanaste = false;
			flagBarb = flagRex = true;
			
			barb = new Barbarianna(50, 540, 25, 30, 2, 3, 'D');
			rex = new T_Rex(700, 63, 120, 100, 2, 3, 'I');
			
			if(selectorNivel == 1) {
				flagRaptors = true;
				raptors = new Raptor[6];
				cantVueltasRaptors = 100;
			}
			
			vidas[0] = new Vida(70, 270, 20, 20);
			vidas[1] = new Vida(730, 160, 20, 20);
			bonos[0] = new BonoPuntaje(730, 380, 20, 20);
			bonos[1] = new BonoPuntaje(70, 50, 20, 20);
			minas[0] = new Mina(750,550,15,10);
			minas[1] = new Mina(400,440,15,10);
			minas[2] = new Mina(50,440,15,10);
			minas[3] = new Mina(750,330,15,10);
			minas[4] = new Mina(50,220,15,10);
			
			flagRoarSound=true;
			flagGameOverSound=true;
			Herramientas.play("sounds/seleccion.wav");
		}
		
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}

}
