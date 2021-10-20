package juego;

import java.awt.Color;
import java.util.Random;

import entorno.Entorno;

public class Raptor {
	private int x;
	private int y;
	private int velocidad;
	private char orientacion;
	private Rayo rayo;

	public Raptor() {
		x = 840;
		y = 50;
		velocidad = 2;
		orientacion = 'I';
	}

	public Rayo getRayo() {
		return rayo;
	}

	public void setRayo(Rayo ref) {
		rayo=ref;
	}

	public boolean fueraDePantalla() {
		if (x < -50 || x > 850) // habrá que ajustar luego
			return true;
		return false;
	}

	public void mover() {
		if (orientacion == 'D')
			x = x + velocidad;
		else
			x = x - velocidad;
	}
	
	public void procesarMovimiento() {
		
	}

	public void graficar(Entorno e) {
		e.dibujarRectangulo(x, y, 50, 50, 0, Color.green);
	}

	public void generarRayo() {
		Random rnd = new Random();
		int velocidadRandom = 5 + rnd.nextInt(5);
		rayo = new Rayo(x, y, velocidadRandom, orientacion);
	}

}
