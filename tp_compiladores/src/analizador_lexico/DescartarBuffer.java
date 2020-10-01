package analizador_lexico;

public class DescartarBuffer extends AccionSemantica {
	
	public void ejecutar(char c, int nro_linea) {
		System.out.println("Descarto Buffer: ");
		super.buffer = "";
	}
	
	public Token getToken() {
		return null;
	}
}
