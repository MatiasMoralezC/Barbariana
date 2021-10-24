package juego;

import java.awt.Color;

import entorno.Entorno;
import entorno.Herramientas;

public class Barbarianna {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private int velocidad;
	private int vidas;
	private char orientacion;
	private boolean saltando;
	private boolean bajando;
	private boolean agachada;
	private RayoMjolnir relampago;
	private Rectangulo cuerpo;

	public Barbarianna(int x, int y, int ancho, int alto, int velocidad, int vidas, char orientacion) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidad = velocidad;
		this.vidas = vidas;
		this.orientacion = orientacion;
		this.saltando = false;
		this.bajando = false;
		this.agachada = false;
		this.cuerpo = new Rectangulo(x, y, ancho, alto);
	}

	public void mover() {
		if (orientacion == 'D') {
			this.x += velocidad;
			cuerpo.setX(x);
		} else {
			this.x -= velocidad;
			cuerpo.setX(x);
		}
	}
	
	public void subir() {
		y -= 3;
		cuerpo.setY(y);
	}

	public void bajar() {
		y += 3;
		cuerpo.setY(y);
	}

	public RayoMjolnir getRelampago() {
		return relampago;
	}

	public void setRelampago(RayoMjolnir r) {
		relampago = r;
	}

	public void siempreDentroDePantalla() {
		if (this.x <= 25) {
			this.x = 25;
		}
		if (this.x >= 775) {
			this.x = 775;
		}
	}
	
	public int getVidas() {
		return vidas;
	}
	
	public void setVidas(int vidas) {
		this.vidas = vidas;
	}
	
	public boolean getAgachada() {
		return agachada;
	}

	public void setAgachada(boolean agachada) {
		this.agachada = agachada;
	}

	public boolean getSaltando() {
		return saltando;
	}

	public void setSaltando(boolean saltando) {
		this.saltando = saltando;
	}
	
	public boolean getBajando() {
		return bajando;
	}

	public void setBajando(boolean bajando) {
		this.bajando = bajando;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getAncho() {
		return ancho;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	public int getAlto() {
		return alto;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	public char getOrientacion() {
		return orientacion;
	}

	public void setOrientacion(char orientacion) {
		this.orientacion = orientacion;
	}

	public Rectangulo getCuerpo() {
		return cuerpo;
	}

	public void graficar(Entorno e) {
		float escala = (float) 0.05;
		if (orientacion == 'D') {
			if(agachada) {
				e.dibujarImagen(Herramientas.cargarImagen("BarbAgDer.png"), x, y, 0, escala);
			}
			else { // si no esta agachada
				if(Math.sin(0.1*x)>0)
					e.dibujarImagen(Herramientas.cargarImagen("BarbC1Der.png"), x, y, 0, escala);
				else
					e.dibujarImagen(Herramientas.cargarImagen("BarbC1Der.png"), x, y, 0, escala);
			}
		}
		else { // orientacion == 'I'
			if(agachada) {
				e.dibujarImagen(Herramientas.cargarImagen("BarbAgIzq.png"), x, y, 0, escala);
			}
			else {
				if(Math.sin(0.1*x)>0) 
					e.dibujarImagen(Herramientas.cargarImagen("BarbC1Izq.png"), x, y, 0, escala);
				else 
					e.dibujarImagen(Herramientas.cargarImagen("BarbC1Izq.png"), x, y, 0, escala);
			}
		}
	}
	
	public void generarRelampago() {
		int xRayo;
		if (this.orientacion == 'D') {
			xRayo = getX() + 30;
		} else {
			xRayo = getX() - 30;
		}

		relampago = new RayoMjolnir(xRayo, y, 40, 5, 5, this.orientacion);
	}
	
	public void graficarVidas(Entorno e) {
		int pos=0;
		for(int i=0; i<vidas; i++) {
			e.dibujarImagen(Herramientas.cargarImagen("vidas.png"), 50 + pos, 580, 0, 0.08);
			pos += 40;
		}
	}
}
