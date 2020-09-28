package tp_compiladores;

import java.util.Enumeration;
import java.util.Hashtable;


public class TablaTokens { //tabla de tokens unicos! lo que le entrega al sintactico
	private Hashtable<Integer, String> id_tipo;
	
	public TablaTokens() {
		this.id_tipo = new Hashtable<Integer, String> ();
		this.id_tipo.put(1, "IF");
		this.id_tipo.put(2, "THEN");
		this.mostrarTokens();
	}
	
	
	public void addToken(int i, String s) {
		this.id_tipo.put(i, s);
	}
	
	
	public boolean existe(String b) {
		//System.out.println("Existe " + b + " en la Hash?");
		return this.id_tipo.contains(b);
	}
	
	
	public void mostrarTokens() {
		System.out.println("Tabla de tokens unicos ");
		//for ()
		Enumeration enumeration_keys = this.id_tipo.keys();
		
		Enumeration enumeration = this.id_tipo.elements();
		while (enumeration.hasMoreElements()) {
			System.out.println(enumeration_keys.nextElement() + " , " + enumeration.nextElement());
		}
		System.out.println("\n");
	}
}
