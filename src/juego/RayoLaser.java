package juego;

import java.awt.Color;

import entorno.Entorno;
import entorno.Herramientas;

public class RayoLaser {
	private float x;
	private float y;
	private float ancho;
	private float alto;
	private int velocidad;
	private char direccion;
	private Rectangulo cuerpo;

	public RayoLaser(float x, float y, float ancho, float alto, int velocidad, char direccion) {
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
			this.cuerpo.setX(x);
		}
		else {
			this.x -= velocidad;
			this.cuerpo.setX(x);
		}	
	}

	public boolean fueraDePantalla() {
		if (x < -50 || x > 850) // habrá que ajustar luego
			return true;
		return false;
	}

	public void graficar(Entorno e) {
		e.dibujarImagen(Herramientas.cargarImagen("images/laser.png"), x, y-3, 0, 0.3);
	}

}