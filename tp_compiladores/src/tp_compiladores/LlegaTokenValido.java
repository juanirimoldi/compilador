package tp_compiladores;

public class LlegaTokenValido extends AccionSemantica {
	private TablaTokens ttok;
	private TablaSimbolos tsym;
	
	//private 
	
	private int id = 20;
	
	
	public LlegaTokenValido(TablaTokens tt, TablaSimbolos ts) {
		this.ttok = tt;
		this.tsym = ts;
	}
	
	
	public void ejecutar(char c, int nro_linea) {
		System.out.println("AS3 -> Voy a Ef con un Token Valido "+ super.buffer);// + super.tipo_buffer + " , " + super.buffer);// + " , " + this.buffer);
		//InicializarBuffer ib =  
		
		super.buffer += c; //devuelvo a entrada caracter leido???
		
		
		
		
		
		/*
		
		if (this.ttok.existe(super.tipo_buffer)) { //si esta en palabras reservadas o tipos
			System.out.println("Ya existe tipo!! ");
			//si es palabra reservada -> devolver palabra reservada
			//else devolver ID + punt TS
			this.tsym.addToken(t); //agregar token a tabla de simbolos
			//devolver palabra reservada
		} else {
			System.out.println("No existe tipo. Agrego a Tokens unicos un nuevo tipo -> " + super.tipo_buffer);
			this.ttok.addToken(id, super.tipo_buffer);//, super.buffer);
			this.id ++;
			System.out.println("Agrego a T simbolos un nuevo registro -> " + t.getLexema() + " , " + t.getTipo());
			this.tsym.addToken(t); //agregar a la tabla de simbolos
			//devolver ID + punt TS
		}
		
		
		//una vez que cargo el token en la tabla, limpio
		//super.buffer = "";
	
	*/
	}
	
	public Token getToken() {
		return null;//super.buffer;
	}
}
