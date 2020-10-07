package analizador_lexico;

public class LlegaTokenValido extends AccionSemantica {
	private TablaTokens ttok;
	private TablaSimbolos tsym;
	
	//private 
	
	private int id = 20;
	
	
	public LlegaTokenValido(TablaTokens tt, TablaSimbolos ts) {
		this.ttok = tt;
		this.tsym = ts;
	}
	
	//se detecta comentario -> volver a E0
	
	public void ejecutar(char c, int nro_linea) {
		super.buffer += c; 
		
		System.out.println("AS3 -> Voy a Ef con un Token Valido "+ super.buffer);
		
		
	}
	
	
	public Token getToken() {
		return null;
	}
}
