package tp_compiladores;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class TablaSimbolos {
	//private Hashtable<Integer, Token> Tsym; //257 para adelante
	private int id = 257; //de 0 a 256 esta reservado para ASCII
	
	private List<Token> Tsymb;
	//contiene un registro para cada identificador, constante y cadena de caracteres que aparezca en el codigo fuente
	
	
	public TablaSimbolos() {
		//this.Tsym = new Hashtable<Integer, Token> ();
		this.Tsymb = new ArrayList<Token> ();
	}
	
	
	
	public void addTokenLista(Token t) {
		this.Tsymb.add(t);
	}
	
	
	public boolean existe(String id, String tipo) {
		boolean existe = false;
		
		for (Token t : this.Tsymb) {
			if (t.getLexema().equals(id) & t.getTipo().equals(tipo)){
				existe = true;
			}
		}
		return existe;
	}
	
	
	
	public void mostrarListaTsym() {
		System.out.println("lexema	, tipo	, n_linea, id_tipo");
		for (Token t : Tsymb) {
			System.out.println(t.getLexema() + "	, " + t.getTipo() + "	 , " +t.getNroLinea()+" 	,   "+t.getIdTipo());
		}
	}
}
