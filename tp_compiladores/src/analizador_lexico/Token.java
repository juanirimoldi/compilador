package analizador_lexico;

public class Token {
	private String tipo;
	private String lexema;
	private int nro_linea;
	private int id_tipo;
	private String tipo_var;
	private String ambito;
	private String uso="";
	private int valor;
	private int cantInvocaciones;
	
	
	public Token(String l, String t, int linea, String tipo_var) {//, int id_tipo) {
		this.lexema = l;
		this.tipo = t;
		this.nro_linea = linea;
		//this.id_tipo = id_tipo;
		this.tipo_var=tipo_var;
		this.ambito="";
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
	
	public void setAmbito(String a) {
		this.ambito=a;
	}
	
	public String getAmbito() {
		return this.ambito;
	}
	
	public void setLexema(String l) {
		this.lexema=l;
	}
	
	
	public void setUso(String u) {
		this.uso=u;
	}
	
	public String getUso() {
		return this.uso;
	}
	
	public void setCantInvocaciones(int c) {
		this.cantInvocaciones=c;
	}
	
	public int getCantInvocaciones() {
		return this.cantInvocaciones;
	}
	
	/*
	public void setValor(String val) {
		if (this.tipo.equals("ID")) {
			this.valor=Integer.parseInt(val);
		}
	}
	
	public int getValor() {
		return this.valor;
	}
	*/
}
