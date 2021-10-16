package juego;

import java.awt.Color;

import entorno.Entorno;

public class Rayo {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private int velocidad;
	private char direccion;

	public Rayo(int xIn, int yIn, int velocidadIn, char direccionIn) {
		// (xIn,yIn) es la posicion de raptor/vikinga dado que desde ahi se dispara el rayo
		x = xIn;
		y = yIn;
		// ancho y alto determinados segun los pixeles de la imagen
		ancho = 50;
		alto = 30;
		// velocidad del rayo
		velocidad = velocidadIn;
		// direccion de la mirada de raptor/vikinga para determinar la direccion del rayo
		direccion = direccionIn;
	}

	public void mover() {
		if (direccion == 'D')
			x = x + velocidad;
		else
			x = x - velocidad;
	}

	public boolean fueraDePantalla() {
		if (x < 0 || x > 800) // habrá que ajustar luego
			return true;
		return false;
	}

	public boolean procesarMovimiento() { // el metodo devuelve falso si esta fuera de pantalla
		if (fueraDePantalla()) // ya que en ese caso no hay movimiento que procesar
			return false;
		mover();
		return true;
	}

	public void graficar(Entorno e, char tipo) {
		if (tipo == 'R')
			e.dibujarRectangulo(x, y, ancho, alto, 0, Color.YELLOW); // rayo tipo relampago
		else
			e.dibujarRectangulo(x, y, ancho, alto, 0, Color.RED); // rayo tipo laser
	}
}
