package analizador_lexico;

import tabla_simbolos.TablaDeSimbolos;

public abstract class AccionSemantica {
	protected TablaDeSimbolos tds;
	protected static String buffer; 
	protected static String tipo_token;
	protected static String tipo_variable;
	//protected static boolean token_valido=false;

	
	public abstract void ejecutar(char c, int nro_linea);
	public abstract Token getToken();
}

