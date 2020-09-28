package tp_compiladores;

public abstract class AccionSemantica {
	protected TablaTokens ttok;
	protected static String buffer; // = ""; 
	protected static String tipo;
	
	//public AccionSemantica(TablaTokens tt) {
	//	this.ttok = tt;
	//}
	
	public abstract void ejecutar(char c);
}

