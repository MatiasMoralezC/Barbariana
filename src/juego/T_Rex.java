package juego;

import java.util.Random;

import entorno.Entorno;
import entorno.Herramientas;

public class T_Rex {
	private float x;
	private float y;
	private float ancho;
	private float alto;
	private int velocidad;
	private int vidas;
	private char orientacion;
	private Rectangulo cuerpo;

	public T_Rex(float x, float y, float ancho, float alto, int velocidad, int vidas, char orientacion) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidad = velocidad;
		this.vidas = vidas;
		this.orientacion = orientacion;
		this.cuerpo = new Rectangulo(x, y, ancho, alto);
	}

	public double getPosX() {
		return x;
	}

	public double getPosY() {
		return y;
	}

	public float getAncho() {
		return ancho;
	}

	public void setAncho(float ancho) {
		this.ancho = ancho;
	}

	public float getAlto() {
		return alto;
	}

	public void setAlto(float alto) {
		this.alto = alto;
	}

	public Rectangulo getCuerpo() {
		return cuerpo;
	}

	public int getVidas() {
		return vidas;
	}

	public void setVidas(int vidas) {
		this.vidas = vidas;
	}

	public void setOrientacion(char orient) {
		orientacion = orient;
	}

	public boolean fueraDePantalla() {
		if (x < 0 || x > 800) // habrá que ajustar luego
			return true;
		return false;
	}

	public void mover() {
		if (orientacion == 'D') {
			this.x += velocidad;
			this.cuerpo.setX(x);
		} else {
			this.x -= velocidad;
			this.cuerpo.setX(x);
		}
	}

	public void procesarMovimiento() {
		if (x > 700)
			orientacion = 'I';
		if (x < 200)
			orientacion = 'D';
		mover();
	}

	public void graficar(Entorno e) {
		float escala = (float) 0.3;
		if (orientacion == 'D') {
			if (Math.sin(0.1 * x) > 0)
				e.dibujarImagen(Herramientas.cargarImagen("RexC1Der.png"), x, y, 0.15, escala);
			else
				e.dibujarImagen(Herramientas.cargarImagen("RexC2Der.png"), x, y, 0.15, escala);
		} else {
			if (Math.sin(0.1 * x) > 0)
				e.dibujarImagen(Herramientas.cargarImagen("RexC1Izq.png"), x, y, -0.15, escala);
			else
				e.dibujarImagen(Herramientas.cargarImagen("RexC2Izq.png"), x, y, -0.15, escala);
		}

	}

	public Fireball generarFireball() {
		Random rnd = new Random();
		int velocidadRandom = 1 + rnd.nextInt(3);
		if (orientacion == 'D')
			return new Fireball(x + 60, y - 20, 35, 35, velocidadRandom, orientacion);
		else
			return new Fireball(x - 60, y - 20, 35, 35, velocidadRandom, orientacion);
	}
}
