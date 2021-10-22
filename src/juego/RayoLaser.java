package juego;

import java.awt.Color;

import entorno.Entorno;

public class RayoLaser {
	private int x;
	private int y;
	private int velocidad;
	private char direccion;

	public RayoLaser(int x, int y, int velocidad, char direccion) {
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
	}

	public boolean fueraDePantalla() {
		if (x < -50 || x > 850) // habrá que ajustar luego
			return true;
		return false;
	}

	public void graficar(Entorno e) {
		e.dibujarRectangulo(x, y, 50, 10, 0, Color.RED);
	}

}