package juego;

import java.awt.Color;

import entorno.Entorno;

public class Rayo {
	private int x;
	private int y;
	private int velocidad;
	private char direccion;

	public Rayo(int xIn, int yIn, int velocidadIn, char direccionIn) {
		// (xIn,yIn) es la posicion del raptor/vikinga dado que desde ahi se dispara el rayo
		x = xIn;
		y = yIn;
		// velocidad del rayo
		velocidad = velocidadIn;
		// direccion de la mirada del raptor/vikinga para determinar la direccion del rayo
		direccion = direccionIn;
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

	public void graficar(Entorno e, char tipo) {
		if (tipo == 'R')
			e.dibujarRectangulo(x, y, 50, 10, 0, Color.YELLOW); // rayo tipo relampago
		else
			e.dibujarRectangulo(x, y, 50, 10, 0, Color.RED); // rayo tipo laser
	}
}