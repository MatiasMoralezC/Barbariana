package juego;

import entorno.Entorno;
import entorno.Herramientas;

public class Fireball {
	private float x;
	private float y;
	private int velocidad;
	private char direccion;

	public Fireball(float x, float y, int velocidad, char direccion) {
		this.x = x;
		this.y = y;
		this.velocidad = velocidad;
		this.direccion = direccion;
	}

	public void mover() {
		if (direccion == 'D')
			x = x + velocidad;
		else
			x = x - velocidad;
		
		y = (float) (y + 1.2*velocidad);
	}

	public boolean fueraDePantalla() {
		if (x < -50 || x > 850 || y > 650) // habr� que ajustar luego
			return true;
		return false;
	}

	public void graficar(Entorno e) {
		e.dibujarImagen(Herramientas.cargarImagen("fireball.png"), x, y, Math.sin(0.5*x), 0.07);
	}
}