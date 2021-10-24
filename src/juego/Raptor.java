package juego;

import java.awt.Color;
import java.util.Random;

import entorno.Entorno;
import entorno.Herramientas;

public class Raptor {
	private float x;
	private float y;
	private float ancho;
	private float alto;
	private int velocidad;
	private char orientacion;
	private RayoLaser rayoLaser;
	private Rectangulo cuerpo;

	public Raptor(float x, float y, float ancho, float alto, int velocidad, char orientacion) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidad = velocidad;
		this.orientacion = orientacion;
		this.cuerpo = new Rectangulo(x, y, ancho, alto);
	}
	
	public float getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
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

	public RayoLaser getRayoLaser() {
		return rayoLaser;
	}

	public void setRayoLaser(RayoLaser rayoLaser) {
		this.rayoLaser = rayoLaser;
	}

	public Rectangulo getCuerpo() {
		return cuerpo;
	}

	public boolean fueraDePantalla() {
		if (x < -50 || x > 850) // habrá que ajustar luego
			return true;
		return false;
	}
	
	public void caer() {
		y += 3;
		cuerpo.setY(y);
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
		if (x > 770)
			orientacion='I';
		if (x < 30 && y < 450)
			orientacion='D';
		mover();
	}

	public void graficar(Entorno e) {
		if(orientacion=='D') {
			if(Math.sin(0.1*x)>0){
				e.dibujarImagen(Herramientas.cargarImagen("raptorp3.png"), x, y, 0);
				}
			else {
				e.dibujarImagen(Herramientas.cargarImagen("raptorp4.png"), x, y, 0);}
		}
		else {
			if(Math.sin(0.1*x)>0) {
				e.dibujarImagen(Herramientas.cargarImagen("raptorp1.png"), x, y, 0);
			}
			else {
				e.dibujarImagen(Herramientas.cargarImagen("raptorp2.png"), x, y, 0);
				}
		}
	}

	public void generarRayoLaser() {
		Random rnd = new Random();
		int velocidadRandom = 5 + rnd.nextInt(5);
		rayoLaser = new RayoLaser(x, y, 40, 5, velocidadRandom, orientacion);
	}

}
