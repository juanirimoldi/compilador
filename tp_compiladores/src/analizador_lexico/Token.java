package analizador_lexico;

public class Token {
	private String tipo;
	private String lexema;
	private int nro_linea;
	private int id_tipo;

	
	public Token(String l, String t, int linea, int id_tipo) {
		this.tipo = t;
		this.lexema = l;
		this.nro_linea = linea;
		this.id_tipo = id_tipo;
	}
	
	
	public String getTipo() {
		return this.tipo;
	}
	
	public String getLexema() {
		return this.lexema;
	}
	
	public int getNroLinea() {
		return this.nro_linea;
	}
	
	public int getIdTipo() {
		return this.id_tipo;
	}
}
