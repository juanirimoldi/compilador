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
		System.out.println("Existe " + b + " en la Hash?");
		return this.id_tipo.contains(b);
	}
	
	
	public int getIdTipo(String s) {
		//recorro la hash
		int id_tipo = 0;
		
		System.out.println("Vengo a comparar "+s);
		Enumeration enumeration_keys = this.id_tipo.keys();
		Enumeration enumeration = this.id_tipo.elements();
		
		
		while (enumeration_keys.hasMoreElements()) {
			int key = (int)enumeration_keys.nextElement();
			String id_token = (String)enumeration.nextElement();
			
			if (id_token.equals(s)) {
				System.out.println("Existe el tipo en la Tabla de Hash!!");
				id_tipo = key;
			}
		}
		return id_tipo;
	}
	
	
	public void mostrarTokens() {
		System.out.println("Tabla de tokens unicos \n");

		System.out.println("id_token, lexema	");
		Enumeration enumeration_keys = this.id_tipo.keys();
		
		Enumeration enumeration = this.id_tipo.elements();
		while (enumeration.hasMoreElements()) {
			System.out.println(enumeration_keys.nextElement() + " 	, " + enumeration.nextElement());
		}
		System.out.println("\n");
	}
}
