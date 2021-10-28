package juego;

import entorno.Entorno;
import entorno.Herramientas;

public class Vida {
	private float x;
	private float y;
	private float ancho;
	private float alto;
	private Rectangulo cuerpo;
	
	public Vida(float x, float y, float ancho, float alto) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.cuerpo = new Rectangulo(x, y, ancho, alto);
	}
	
	public Rectangulo getCuerpo() {
		return cuerpo;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getAncho() {
		return ancho;
	}

	public void setAncho(float ancho) {
		this.ancho = ancho;
	}

	public float getAlto() {
		return alto;
	}

	public void setAlto(float alto) {
		this.alto = alto;
	}
	
	public void graficar(Entorno e) {
		e.dibujarImagen(Herramientas.cargarImagen("vidas.png"), x, y, 0, 0.09);
	}
}
