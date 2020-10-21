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
	}
	
	
	
	public void ejecutar(char c, int nro_linea) { 
		this.linea_actual = nro_linea;
		//defino los tipos de token simples que puede entregar al sintactico
		if (c == '=') {  super.tipo_token = "IGUAL"; };
		if ((c == ';') || (c == ',')) { super.tipo_token = "PUNT"; };
		if ((c == '+') || (c == '-') || (c == '*') || (c == '/')) { super.tipo_token = "OP"; }
		
		
		//int id_tipo = this.tt.getIdTipo(super.tipo_buffer); 
		//System.out.println("ID TIPO TOKEN -> "+id_tipo);
		this.t = new Token(super.buffer, super.tipo_token, nro_linea);//, id_tipo);
		
	
		super.tipo_token="";
		super.buffer = ""; //despues de entregar token lo limpio
		//super.token_valido=false;
	}
	
	
	public Token getToken() {
		//super.buffer = ""; //y despues de entregar token lo limpio?
		return t;
		}
}
