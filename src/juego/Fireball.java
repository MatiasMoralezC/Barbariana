package juego;

import java.awt.Color;

import entorno.Entorno;

public class Fireball {
	private double x;
	private double y;
	private double velocidad;
	private char direccion;

	public Fireball(double xIn, double yIn, double velocidadIn, char direccionIn) {
		x = xIn;
		y = yIn;
		velocidad = velocidadIn;
		direccion = direccionIn;
	}

	public void mover() {
		if (direccion == 'D')
			x = x + velocidad;
		else
			x = x - velocidad;
		
		y = y + 1.2*velocidad;
	}

	public boolean fueraDePantalla() {
		if (x < -50 || x > 850 || y > 650) // habrá que ajustar luego
			return true;
		return false;
	}

	public void graficar(Entorno e) {
		e.dibujarCirculo(x, y, 50, Color.RED);
	}
}
