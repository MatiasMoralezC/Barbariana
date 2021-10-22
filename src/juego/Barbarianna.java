package juego;

import java.awt.Color;

import entorno.Entorno;

public class Barbarianna {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private int velocidad;
	private char orientacion; 
	private RayoMjolnir relampago;
	
	
	public Barbarianna(int x, int y, int ancho, int alto, int velocidad, char orientacion) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidad = velocidad;
		this.orientacion = orientacion;
	}
	
	public void mover() {
		if (orientacion == 'D')
			this.x += velocidad;
		else
			this.x -= velocidad;
	}
	
	public void saltar() {
		this.y -= 60;
	}
	
	public void generarRelampago() {
		int xRayo;
		if (this.orientacion == 'D') {
			xRayo = getX() + 25;
		} else {
			xRayo = getX() - 25;
		}
		
		relampago = new RayoMjolnir(xRayo, this.y, 5, this.orientacion);
	}
	
	public RayoMjolnir getRelampago() {
		return relampago;
	}
	
	public void setRelampago(RayoMjolnir r) {
		relampago = r;
	}
	
	public void fueraDePantalla() {
		if (this.x <= 25) {
			this.x = 25;
		}
		if (this.x >= 775) {
			this.x = 775;
		}
	}
	
	public void caida() {
		if (this.y <= 540) {
			this.y += 2;
		}
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getAncho() {
		return ancho;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	public int getAlto() {
		return alto;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	public char getOrientacion() {
		return orientacion;
	}

	public void setOrientacion(char orientacion) {
		this.orientacion = orientacion;
	}
	
	public void graficar(Entorno e) {
		e.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.PINK);
	}
}
