package tp_compiladores;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class TablaSimbolos {
	private Hashtable<Integer, Token> Tsym; //257 para adelante
	private int id = 257; //de 0 a 256 esta reservado para ASCII
	
	private List<Token> Tsymb;
	
	//contiene un registro para cada identificador, constante y cadena de caracteres que aparezca en el codigo fuente
	
	public TablaSimbolos() {
		this.Tsym = new Hashtable<Integer, Token> ();
		this.Tsymb = new ArrayList<Token> ();
		this.Tsymb.add(new Token("PR","IF"));
		this.Tsymb.add(new Token("PR","THEN"));
	}
	
	
	public void addToken(Token t) {
		this.Tsym.put(id, t);
		this.id++;
	}
	
	
	public void addTokenLista(Token t) {
		this.Tsymb.add(t);
	}
	
	
	public boolean existe(String id) {
		//System.out.println("Existe " + b + " en la Hash?");
		return this.Tsymb.contains(id);
		//return this.id_tipo.contains(b);
	}
	
	
	public void mostrarListaTsym() {
		for (Token t : Tsymb) {
			System.out.println(t.getTipo()+" , "+t.getLexema());
		}
	}
	
	/*
	public void mostrarTablaSimbolos() {
		System.out.println("Tabla de simbolos");

		Enumeration enumeration_keys = this.Tsym.keys();
		
		Enumeration enumeration = this.Tsym.elements();
		while (enumeration.hasMoreElements()) {
			System.out.println(enumeration_keys.nextElement() + " , " + enumeration.nextElement());
		}
		System.out.println("\n");
	}
	*/
}
