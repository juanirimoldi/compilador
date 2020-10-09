package analizador_lexico;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class TablaSimbolos {

	//private int id = 257; //de 0 a 256 esta reservado para ASCII
	
	private List<Token> Tsymb;
	//contiene un registro para cada identificador, constante y cadena de caracteres que aparezca en el codigo fuente
	
	
	public TablaSimbolos() {
		this.Tsymb = new ArrayList<Token> ();
	}
	
	
	
	public void addTokenLista(Token t) {
		this.Tsymb.add(t);
		//this.mostrarListaTsym();
	}
	
	
	public boolean existe(String id, String tipo) {
		boolean existe = false;
		//System.out.println("Existe "+ id + " , tipo "+ tipo + " en la Tsym???");
		for (Token t : this.Tsymb) {
			if (t.getLexema().equals(id) & t.getTipo().equals(tipo)){
				existe = true;
			}
		}
		//System.out.println("Existe "+ id + " , tipo "+ tipo + " en la Tsym???  -> "+existe);
		
		return existe;
	}
	
	
	public void eliminarSimbolo(String s) {
		Token t;
		for (int i = 0; i<Tsymb.size(); i++) {
			t = Tsymb.get(i);
			if (t.getLexema().equals(s)) {
				Tsymb.remove(t);
				System.out.print("REMUEVO ALGO -> "+s + " \n");
			}
		}	
	}
	
	
	public void mostrarListaTsym() {
		System.out.println("\n TABLA DE SIMBOLOS \n");
		System.out.println("lexema	, tipo	, n_linea, id_tipo");
		for (Token t : Tsymb) {
			System.out.println(t.getLexema() + "	, " + t.getTipo() + "	 , " +t.getNroLinea()+" 	,   "+t.getIdTipo());
		}
		System.out.println();
	}
}
