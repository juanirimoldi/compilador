package analizador_lexico;

public abstract class AccionSemantica {
	protected TablaTokens ttok;
	protected static String buffer; 
	protected static String tipo_buffer;

	
	public abstract void ejecutar(char c, int nro_linea);
	public abstract Token getToken();
}

