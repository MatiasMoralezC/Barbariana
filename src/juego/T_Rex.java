package juego;

import java.awt.Color;
import java.util.Random;

import entorno.Entorno;

public class T_Rex {
	private double x;
	private double y;
	private double velocidad;
	private char orientacion;
	private Fireball[] fireballs;

	public T_Rex() {
		x = 700;
		y = 100;
		velocidad = 1.5;
		orientacion = 'I';
		fireballs = new Fireball[5];
	}

	public double getPosX() {
		return x;
	}

	public double getPosY() {
		return y;
	}

	public Fireball[] getFireballs() {
		return fireballs;
	}

	public void setOrientacion(char orient) {
		orientacion = orient;
	}

	public void setFireballs(Fireball ref,int pos) {
		fireballs[pos]=ref;
	}

	public boolean fueraDePantalla() {
		if (x < 0 || x > 800) // habrá que ajustar luego
			return true;
		return false;
	}

	public void mover() {
		if (orientacion == 'D')
			x = x + velocidad;
		else
			x = x - velocidad;
	}

	public void graficar(Entorno e) {
		e.dibujarRectangulo(x, y, 80, 100, 0, Color.green);
	}

	public Fireball generarFireball() {
		Random rnd = new Random();
		int velocidadRandom = 1 + rnd.nextInt(3);
		return new Fireball(x, y, velocidadRandom, orientacion);
	}
}
