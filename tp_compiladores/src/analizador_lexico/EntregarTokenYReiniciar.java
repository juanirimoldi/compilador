package analizador_lexico;

import tabla_simbolos.TablaDeSimbolos;

public class EntregarTokenYReiniciar extends AccionSemantica {

	private TablaDeSimbolos tds;
	
	
	private Token t;
	//private int sgte_id = 300;
	private int linea_actual;
	
	
	//Accion Semantica Final 
	public EntregarTokenYReiniciar(TablaDeSimbolos tds){
		this.tds = tds;
		this.t = null;
	}
	
	
	
	public void ejecutar(char c, int nro_linea) { 
		this.linea_actual = nro_linea;
		//defino los tipos de token simples que puede entregar al sintactico
		if (c == '=') {  super.tipo_token = "IGUAL"; };
		if ((c == ';') | (c == ':') | (c == ',')) { super.tipo_token = "PUNT"; };
		if ((c == '+') || (c == '-') || (c == '*') || (c == '/')) { super.tipo_token = "OP"; }
		
		
		//PALABRAS RESERVADAS SAPE
		if (super.buffer.equals("INTEGER") | super.buffer.equals("FLOAT")) {
			super.tipo_token="PALABRA_RESERVADA" ;
		}
		
		if (super.buffer.equals("IF") | super.buffer.equals("ELSE") | super.buffer.equals("END_IF") | super.buffer.equals("PROC") | super.buffer.equals("NI") | super.buffer.equals("REF")) {
			super.tipo_token="PALABRA_RESERVADA" ;
		}
		
		if (super.buffer.equals("LOOP") | super.buffer.equals("UNTIL")) {
			super.tipo_token="PALABRA_RESERVADA" ;
		}
		
		
		this.t = new Token(super.buffer, super.tipo_token, nro_linea, super.tipo_variable);//, id_tipo);
				
	
		super.tipo_token="";
		super.buffer = ""; //despues de entregar token lo limpio
	}
	
	
	public Token getToken() {
		//super.buffer = ""; //y despues de entregar token lo limpio?
		return t;
		}
}
