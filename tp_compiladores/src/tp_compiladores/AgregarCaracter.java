package tp_compiladores;

public class AgregarCaracter extends AccionSemantica {
	
	public void ejecutar(char c, int nro_linea) {
		System.out.println("AS2 -> Agrego caracter");
		super.buffer += c;
	}
	
	public Token getToken() {
		return null;
	}
}
