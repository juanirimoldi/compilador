package tp_compiladores;

public class DescartarBuffer extends AccionSemantica {
	
	public void ejecutar(char c, int nro_linea) {
		System.out.println("Muestro y Descarto Buffer: ");//+ super.buffer);
		super.buffer = "";
	}
	
	public Token getToken() {
		return null;
	}
}
