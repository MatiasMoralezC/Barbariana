package juego;

import java.awt.Color;
import java.util.Random;

import entorno.Entorno;

public class Raptor {
	private int x;
	private int y;
	private int velocidad;
	private char orientacion;
	private RayoLaser rayoLaser;

	public Raptor(int x,int y, int velocidad, char orientacion) {
		this.x = x;
		this.y = y;
		this.velocidad = velocidad;
		this.orientacion = orientacion;
	}

	public RayoLaser getRayoLaser() {
		return rayoLaser;
	}

	public void setRayoLaser(RayoLaser ref) {
		rayoLaser=ref;
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
		mover();
	}

	public void graficar(Entorno e) {
		e.dibujarRectangulo(x, y, 50, 50, 0, Color.green);
	}

	public void generarRayoLaser() {
		Random rnd = new Random();
		int velocidadRandom = 5 + rnd.nextInt(5);
		rayoLaser = new RayoLaser(x, y, velocidadRandom, orientacion);
	}

}
