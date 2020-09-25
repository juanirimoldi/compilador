package tp_compiladores;

import java.util.Hashtable;

public class TablaSimbolos {
	private Hashtable<Integer, Token> Tsym; //257 para adelante
	private int id = 257; //de 0 a 256 esta reservado para ASCII

	
	public TablaSimbolos() {
		this.Tsym = new Hashtable<Integer, Token> ();
		
	}
	
	public void addToken(Token t) {
		this.Tsym.put(id, t);
		this.id++;
	}
	
	
}
