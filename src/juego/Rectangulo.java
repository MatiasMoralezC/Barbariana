package juego;

import java.awt.Color;

import entorno.Entorno;

public class Rectangulo {
	float x;
	float y;
	private float ancho;
	private float alto;

	public Rectangulo(float x, float y, float ancho, float alto) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
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

	public float posArriba() {
		return y - (alto / 2);
	}

	public float posAbajo() {
		return y + (alto / 2);
	}

	public float posIzquierda() {
		return x - (ancho / 2);
	}

	public float posDerecha() {
		return x + (ancho / 2);
	}
	
	public void graficar(Entorno e) {
		e.dibujarRectangulo(x, y, ancho, alto, 0, Color.MAGENTA);
	}

}