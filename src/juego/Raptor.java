package juego;

import java.awt.Color;
import java.util.Random;

import entorno.Entorno;

public class Raptor {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private int velocidad;
	private char orientacion;
	private RayoLaser rayoLaser;
	private Rectangulo cuerpo;

	public Raptor(int x, int y, int ancho, int alto, int velocidad, char orientacion) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidad = velocidad;
		this.orientacion = orientacion;
		this.cuerpo = new Rectangulo(x, y, ancho, alto);
	}

	public RayoLaser getRayoLaser() {
		return rayoLaser;
	}

	public void setRayoLaser(RayoLaser ref) {
		rayoLaser = ref;
	}
	
	public Rectangulo getCuerpo() {
		return cuerpo;
	}

	public boolean fueraDePantalla() {
		if (x < -50 || x > 850) // habrá que ajustar luego
			return true;
		return false;
	}

	public void mover() {
		if (orientacion == 'D') {
			this.x += velocidad;
			this.cuerpo.setX(x);
		}
		else {
			this.x -= velocidad;
			this.cuerpo.setX(x);
		}	
	}

	public void procesarMovimiento() {
		mover();
	}

	public void graficar(Entorno e) {
		e.dibujarRectangulo(x, y, ancho, alto, 0, Color.green);
	}

	public void generarRayoLaser() {
		Random rnd = new Random();
		int velocidadRandom = 5 + rnd.nextInt(5);
		rayoLaser = new RayoLaser(x, y, velocidadRandom, orientacion);
	}

}
