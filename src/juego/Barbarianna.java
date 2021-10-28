package juego;

import java.awt.Color;

import entorno.Entorno;
import entorno.Herramientas;

public class Barbarianna {
	private float x;
	private float y;
	private float ancho;
	private float alto;
	private int velocidad;
	private int vidas;
	private char orientacion;
	private boolean saltando;
	private boolean superSaltando;
	private boolean agachada;
	private boolean escudo;
	private boolean seDaña;
	private Relampago relampago;
	private Rectangulo cuerpo;

	public Barbarianna(float x, float y, float ancho, float alto, int velocidad, int vidas, char orientacion) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidad = velocidad;
		this.vidas = vidas;
		this.orientacion = orientacion;
		this.saltando = false;
		this.setSuperSaltando(false);
		this.agachada = false;
		this.escudo = false;
		this.seDaña = false;
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
	
	public void saltar() {
		y -= 6;
		cuerpo.setY(y);
	}

	public void caer() {
		y += 3;
		cuerpo.setY(y);
	}

	public Relampago getRelampago() {
		return relampago;
	}

	public void setRelampago(Relampago r) {
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
	
	public boolean getDaño() {
		return seDaña;
	}
	
	public void setDaño(boolean d) {
		this.seDaña = d;
	}
	
	public int getVidas() {
		return vidas;
	}
	
	public void setVidas(int vidas) {
		this.vidas = vidas;
	}
	
	public boolean getEscudo() {
		return escudo;
	}
	
	public void setEscudo(boolean escudo) {
		this.escudo = escudo;
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

	public boolean getSuperSaltando() {
		return superSaltando;
	}

	public void setSuperSaltando(boolean superSaltando) {
		this.superSaltando = superSaltando;
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
	
	public boolean estaDebajoDeUnHueco(Rectangulo[] pisos) {
		// se toma un piso con hueco a la derecha y se crea una columna imaginaria
		if(x>pisos[3].posDerecha()+20 && y>pisos[4].getY()) { // rango y ( pos4toPiso - 600)
			return true;
		}
		// se toma un piso con hueco a la izquierda y se crea una columna imaginaria
		if(x<pisos[2].posIzquierda()-20 && y<pisos[1].getY()) { // rango y ( 0 - pos1erPiso )
			return true;
		}
		return false;
	}
	
	public void graficarVidas(Entorno e) {
		int pos=0;
		for(int i=0; i<vidas; i++) {
			e.dibujarImagen(Herramientas.cargarImagen("vidas.png"), 50 + pos, 580, 0, 0.08);
			pos += 40;
		}
	}

	public void graficar(Entorno e) {
		float escala = (float) 0.05;
		if (orientacion == 'D') {
			if(agachada) {
				e.dibujarImagen(Herramientas.cargarImagen("BarbAgDer.png"), x, y, 0, escala);
			}
			else { // si no esta agachada
				if (getDaño()) {
					e.dibujarImagen(Herramientas.cargarImagen("DanioDer.png"), x, y, 0, escala);
				} else {
					if(Math.sin(0.1*x)>0)
						e.dibujarImagen(Herramientas.cargarImagen("BarbC1Der.png"), x, y, 0, escala);
					else
						e.dibujarImagen(Herramientas.cargarImagen("BarbC1Der.png"), x, y, 0, escala);
				}
			}
		}
		else { // orientacion == 'I'
			if(agachada) {
				e.dibujarImagen(Herramientas.cargarImagen("BarbAgIzq.png"), x, y, 0, escala);
			}
			else {
				if (getDaño()) {
					e.dibujarImagen(Herramientas.cargarImagen("DanioIzq.png"), x, y, 0, escala);
				} else {
					if(Math.sin(0.1*x)>0) 
						e.dibujarImagen(Herramientas.cargarImagen("BarbC1Izq.png"), x, y, 0, escala);
					else 
						e.dibujarImagen(Herramientas.cargarImagen("BarbC1Izq.png"), x, y, 0, escala);
				}
			}
		}
	}
	
	public void generarRelampago() {
		float xRelampago;
		if (this.orientacion == 'D') {
			xRelampago = getX() + 30;
		} else {
			xRelampago = getX() - 30;
		}

		relampago = new Relampago(xRelampago, y, 30, 10, 3, this.orientacion);
	}

}
