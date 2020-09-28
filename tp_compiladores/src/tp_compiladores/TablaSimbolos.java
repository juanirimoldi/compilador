package tp_compiladores;

import java.util.Enumeration;
import java.util.Hashtable;

public class TablaSimbolos {
	private Hashtable<Token, Integer> Tsym; //257 para adelante
	private int id = 257; //de 0 a 256 esta reservado para ASCII
	
	//contiene un registro para cada identificador, constante y cadena de caracteres que aparezca en el codigo fuente
	
	public TablaSimbolos() {
		this.Tsym = new Hashtable<Token, Integer> ();
		
	}
	
	public void addToken(Token t) {
		this.Tsym.put(t, id);
		this.id++;
	}
	
	
	public void mostrarTablaSimbolos() {
		System.out.println("Tabla de simbolos");

		Enumeration enumeration_keys = this.Tsym.keys();
		
		Enumeration enumeration = this.Tsym.elements();
		while (enumeration.hasMoreElements()) {
			System.out.println(enumeration_keys.nextElement() + " , " + enumeration.nextElement());
		}
		System.out.println("\n");
	}
}
