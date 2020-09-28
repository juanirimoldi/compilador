package tp_compiladores;

public class EntregarToken extends AccionSemantica {
	private TablaTokens ttok;
	private TablaSimbolos tsym;
	
	private int id = 20;
	
	
	public EntregarToken(TablaTokens tt, TablaSimbolos ts) {
		this.ttok = tt;
		this.tsym = ts;
	}
	
	
	public void ejecutar(char c) {
		
		System.out.println("ACCION -> Entrego Token -> " + super.buffer);// + " , " + this.buffer);
		Token t = new Token(super.tipo, super.buffer);
		
		
		if (this.ttok.existe(super.tipo)) {
			System.out.println("Ya existe tipo!! ");
			//agregar a tabla de simbolos
			//devolver palabra reservada
		} else {
			System.out.println("Agrego a Tokens un nuevo tipo -> " + super.tipo);
			this.ttok.addToken(id, super.tipo);//, super.buffer);
			this.id ++;
			//agregar a la tabla de simbolos
			//devolver ID + punt TS
		}
		
		
		//una vez que cargo el token en la tabla, limpio
		super.buffer = "";
	}
}
