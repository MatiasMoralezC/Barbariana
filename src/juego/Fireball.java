package juego;

import entorno.Entorno;
import entorno.Herramientas;

public class Fireball {
	private float x;
	private float y;
	private int velocidad;
	private char direccion;
	private Rectangulo cuerpo;

	public Fireball(float x, float y, float ancho, float alto, int velocidad, char direccion) {
		this.x = x;
		this.y = y;
		this.velocidad = velocidad;
		this.direccion = direccion;
		this.cuerpo = new Rectangulo(x, y, ancho, alto);
	}
	
	public Rectangulo getCuerpo() {
		return cuerpo;
	}

	public void mover() {
		if (direccion == 'D') {
			x = x + velocidad;
			cuerpo.setX(x+2);
		}
		else {
			x = x - velocidad;
			cuerpo.setX(x+2);
		}
		
		y = (float) (y + 1.2*velocidad);
		cuerpo.setY(y+1);
	}

	public boolean fueraDePantalla() {
		if (x < -50 || x > 850 || y > 650) // habrá que ajustar luego
			return true;
		return false;
	}

	public void graficar(Entorno e) {
		e.dibujarImagen(Herramientas.cargarImagen("fireball.png"), x, y, Math.sin(0.5*x), 0.07);
	}
}
