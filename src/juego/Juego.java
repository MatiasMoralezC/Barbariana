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
	
	private boolean flagRaptors, flagRex;

	Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Sakura Ikebana Delivery - Grupo 4 - v1", 800, 600);

		// Inicializar lo que haga falta para el juego
		// ...
		
		barb = new Barbarianna(50, 540, 25, 25, 2, 'D');
		contSalto=0;
		
		raptors = new Raptor[3];
		contR = 100;
		
		rex = new T_Rex(700,63,(float)1.5,'I',7);
		contFB=0;
		
		flagRaptors = true;
		flagRex = false;
		

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
		
		graficarPisos(entorno);
		
		procesarEventos();
		
		// Grafico, Movimiento y Eliminacion(fuera de rango) de Rayo Mjolnir
		procesarRayoMjolnir();
		
		actualizarEstadoSalto();
		
		actualizarEstados();
		
		barb.graficar(entorno);
		
		
		if (flagRaptors) {
			
			generacionRaptors();

			// Grafico, Movimiento y Eliminacion(fuera de rango) de Raptors
			procesarRaptors();

			// Grafico, Generacion, Movimiento y Eliminacion(fuera de rango) de Rayos Laser en Raptors
			procesarRayosLaserRaptors();	
		}
		
		if(flagRex) {
			
			actualizarMovimientoTRex();
			
			// Grafico, Generacion, Movimiento y Eliminacion(fuera de rango) de Fireballs en T-Rex
			procesarFireballsTRex();
			
			rex.graficar(entorno);
		}
		
	} // tick()

	public void procesarFireballsTRex() {
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
	}

	public void actualizarMovimientoTRex() {
		if(rex.getPosX()>700)
			rex.setOrientacion('I');
		if(rex.getPosX()<200)
			rex.setOrientacion('D');
		rex.mover();
	}

	public void procesarRayosLaserRaptors() {
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
	}

	public void procesarRaptors() {
		for (int i = 0; i < raptors.length; i++) {
			if (raptors[i] != null) { // esta activo el raptor
				if (raptors[i].fueraDePantalla()) { // si esta fuera, eliminarlo
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
		if (contR == 100) {
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
			contR = 0;
		}
		contR++;
	}

	public void actualizarEstados() {
		barb.siempreDentroDePantalla();
		actualizarEstadoSalto();
	}

	public void actualizarEstadoSalto() {
		if(barb.getSaltando() && contSalto<40) {
			barb.subir();
			contSalto++;
		}
		
		if(contSalto==40)
			barb.setSaltando(false);
		
		if(!barb.getSaltando() && contSalto>0) {
			barb.bajar();
			contSalto--;
		}
	}

	public void procesarRayoMjolnir() {
		if(barb.getRelampago()!=null) {
			RayoMjolnir relampago = barb.getRelampago();
			if(relampago.fueraDePantalla())
				barb.setRelampago(null);
			else {
				relampago.graficar(entorno);
				relampago.mover();
			}
		}
	}

	public void procesarEventos() {
		if (entorno.estaPresionada(entorno.TECLA_DERECHA)) {
			barb.setOrientacion('D');
			barb.mover();
		}
		if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
			barb.setOrientacion('I');
			barb.mover();
		}
		if (entorno.sePresiono(entorno.TECLA_ARRIBA)) 
			barb.setSaltando(true);
		
		if (entorno.sePresiono(entorno.TECLA_ESPACIO)) {
			if (barb.getRelampago()==null && barb.getX() > 25 && barb.getX() < 775)
				barb.generarRelampago();
		}
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
