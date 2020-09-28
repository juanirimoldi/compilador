package tp_compiladores;

public class Token {
	private String tipo;
	private String lexema;
	//si es constante definir tipo
	//private String lexema;
	//private int nro_linea;
	
	public Token(String t, String l) {
		this.tipo = t;
		this.lexema = l;
	}
	
	public String getTipo() {
		return this.tipo;
	}
}
