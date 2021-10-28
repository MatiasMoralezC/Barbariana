package juego;

import entorno.Entorno;
import entorno.Herramientas;

public class Relampago {
	private float x;
	private float y;
	private float ancho;
	private float alto;
	private int velocidad;
	private char direccion;
	private Rectangulo cuerpo;

	public Relampago(float x, float y, float ancho, float alto, int velocidad, char direccion) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidad = velocidad;
		this.direccion = direccion;
		this.cuerpo = new Rectangulo(x, y, ancho, alto);
	}

	public Rectangulo getCuerpo() {
		return cuerpo;
	}

	public void mover() {
		if (direccion == 'D') {
			this.x += velocidad;
			cuerpo.setX(x);
		} else {
			this.x -= velocidad;
			cuerpo.setX(x);
		}
	}

	public boolean fueraDePantalla() {
		if (x < -50 || x > 850) // habrá que ajustar luego
			return true;
		return false;
	}

	public void graficar(Entorno e) {
		float escala=(float) 0.08;
		if (direccion == 'D') {
			if (Math.sin(0.1 * x) > 0) {
				e.dibujarImagen(Herramientas.cargarImagen("rayo1Der.png"), x, y, 0, escala);
			} else {
				e.dibujarImagen(Herramientas.cargarImagen("rayo2Der.png"), x, y, 0, escala);
			}
		} else {
			if (Math.sin(0.1 * x) > 0) {
				e.dibujarImagen(Herramientas.cargarImagen("rayo1Izq.png"), x, y, 0, escala);
			} else {
				e.dibujarImagen(Herramientas.cargarImagen("rayo2Izq.png"), x, y, 0, escala);
			}
		}
	}

}
