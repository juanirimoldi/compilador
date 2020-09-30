package tp_compiladores;

public abstract class AccionSemantica {
	protected TablaTokens ttok;
	protected static String buffer; // = ""; 
	protected static String tipo_buffer;
	//protected static int nro_linea;
	//protected static String tipo_AS;
	
	//public abstract String getTipo();
	public abstract void ejecutar(char c, int nro_linea);
	public abstract Token getToken();
}

