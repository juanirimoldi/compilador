package tp_compiladores;

public abstract class AccionSemantica {
	protected TablaTokens ttok;
	protected static String buffer; // = ""; 
	protected static String tipo_buffer;
	//protected static String tipo_AS;
	
	//public abstract String getTipo();
	public abstract void ejecutar(char c, String buffer);
	public abstract String getToken();
}

