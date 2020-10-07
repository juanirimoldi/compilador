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
		
		System.out.println("AS3 -> Voy a Ef con un Token Valido ->> "+ super.buffer + "	, tipo ->"+super.tipo_buffer);
		
		//aca checkeo rango -> hago AS 3.1 , 3.2 
		//if (super.tipo == "INTEGER") -> checkeo tipos de enteros y trunco si es necesario
		//if (super.tipo == "DOUBLE") -> checkeo tipos de enteros y si es necesario trunco

	}
	
	
	public Token getToken() {
		return null;
	}
}
