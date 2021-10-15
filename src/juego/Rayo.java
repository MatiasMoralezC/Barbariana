package juego;

public class Rayo {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private char direccion;

	public Rayo(int x2, int y2, char mirada) {
		// (x2,y2) posicion de raptor/vikinga dado que desde esa posicion inicia su
		// movimiento el rayo
		x = x2;
		y = y2;
		// ancho y alto determinados segun los pixeles de la imagen
		ancho = 50;
		alto = 50;
		// direccion de la mirada de raptor/vikinga para determinar la direccion del rayo
		direccion = mirada;
	}

	public void mover() {
		if (direccion == 'D')
			x = x + 2;
		else
			x = x - 2;
	}

	public boolean fueraDePantalla() {
		if (x < 0 || x > 800)
			return true;
		return false;
	}

	public boolean procesarMovimiento() { 	// el metodo devuelve falso si esta fuera de pantalla
		if (fueraDePantalla()) 				// ya que en ese caso no hay movimiento que procesar
			return false;
		mover();
		return true;
	}
}
