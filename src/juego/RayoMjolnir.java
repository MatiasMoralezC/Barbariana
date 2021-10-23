package juego;

import java.awt.Color;

import entorno.Entorno;

public class RayoMjolnir {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private int velocidad;
	private char direccion;
	private Rectangulo cuerpo;

	public RayoMjolnir(int x, int y, int ancho, int alto, int velocidad, char direccion) {
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
		}
		else {
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
		e.dibujarRectangulo(x, y, ancho, alto, 0, Color.YELLOW);
	}

}
