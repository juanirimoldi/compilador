package analizador_lexico;

public class Token {
	private String tipo;
	private String lexema;
	private int nro_linea;
	private int id_tipo;
	private String tipo_var;

	
	public Token(String l, String t, int linea, String tipo_var) {//, int id_tipo) {
		this.lexema = l;
		this.tipo = t;
		this.nro_linea = linea;
		//this.id_tipo = id_tipo;
		this.tipo_var=tipo_var;
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
	
	public void setIdTipo(int id) {
		this.id_tipo = id;
	}
	
	public void setTipo(String tipo) {
		this.tipo=tipo;
	}
	
	public String getTipoVar() {
		return this.tipo_var;
	}
}
