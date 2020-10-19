package analizador_lexico;

import tabla_simbolos.TablaDeSimbolos;

public abstract class AccionSemantica {
	//protected TablaTokens ttok;
	protected TablaDeSimbolos tds;
	protected static String buffer; 
	protected static String tipo_buffer;

	
	public abstract void ejecutar(char c, int nro_linea);
	public abstract Token getToken();
}

