package tabla_simbolos;

import java.util.Enumeration;
import java.util.Hashtable;

import analizador_lexico.Token;


public class TablaDeSimbolos {

	//de entrada guardo tokens. si surge alguna mejora podemos hacer una clase envolvente que reciba u token y lo extienda
	private TablaDeTipos tdt;
	
	private Hashtable<String, Token> TSimbolos;
	
	
	//contendra un registro para cada ID, CTE o CADENA de caracteres
	//con campos para registrar la info relevante de cada simbolo (atributo)
	
	//ej token -> PalabraReservada - IF
	//			  ID , plazo
	//			  ID , tasa
	//			  CTE , 123
	
	//ej. x(lexema), ID, 
	
	public TablaDeSimbolos() throws IllegalArgumentException, IllegalAccessException {
		this.tdt = new TablaDeTipos();
		this.TSimbolos = new Hashtable<String, Token> ();
		
		//this.tdt.cargarTablaConMacros();
	}
	
	
	public void addToken(Token t) {
		
		if (tdt.existe(t.getLexema())){
			//System.out.println("Entro aca???");
			t.setIdTipo(tdt.getIdTipo(t.getLexema()));
			//System.out.println("que onda? "+t.getLexema()+" , "+t.getIdTipo());

		}
		//if t.getLexema() existe en tabla de palabra reservada 
		//    no agrego el token y seteo el id con la palabra reservada
		
		
		//para los tokens simples asigno su identificador ASCII
		if (t.getTipo().equals("PUNT") | t.getTipo().equals("IGUAL") | t.getTipo().equals("OP") | t.getTipo().equals("PARENTESIS") | t.getTipo().equals("LLAVES") | t.getTipo().equals("COMP")) {
			int ascii = (int)t.getLexema().charAt(0);
			t.setIdTipo(ascii);
		}
		
		
		
		if (t.getTipo().equals("ID")){
			int id_tipo = this.tdt.getIdTipo(t.getTipo());
			t.setIdTipo(id_tipo);
			this.TSimbolos.put(t.getLexema(), t);
			//System.out.println(" Tabla -> APARECE UN ID  "+ t.getLexema() + " , " + t.getTipo() + " a la Tabla de Simbolos \n");
		}
		if (t.getTipo().equals("CTE")){
			int id_tipo = this.tdt.getIdTipo(t.getTipo());
			t.setIdTipo(id_tipo);
			//agrego identificador de tipo y lo guardp
			this.TSimbolos.put(t.getLexema(), t);
			//System.out.println(" Tabla de Simbolos -> APARECE UNA CTE "+ t.getLexema() + " , " +t.getTipo() + " \n");
		}
		if (t.getTipo().equals("CADENA")){
			int id_tipo = this.tdt.getIdTipo(t.getTipo());
			t.setIdTipo(id_tipo);
			//agrego identificador de tipo y lo guardo
			this.TSimbolos.put(t.getLexema(), t);
			System.out.println("APARECE UNA CADENA -> la agrego "+ t.getTipo() + " a la Tabla de Simbolos \n");
		}
		
		
		//System.out.println("NO ESISTIS -> "+ t.getTipo() + "\n");
		
	}
	
	
	public boolean existe(String b) {
		//this.mostrarTokens();
		//System.out.println("Existe " + b + " en la Hash???? "+this.id_tipo.contains(b));
		return this.TSimbolos.contains(b);
	}
	
	
	public void mostrarSimbolos() {
		System.out.println("\n\n Tabla de SImbolos \n");

		System.out.println("Lexema	, Token	");
		Enumeration enumeration_keys = this.TSimbolos.keys();
		
		Enumeration enumeration = this.TSimbolos.elements();
		while (enumeration.hasMoreElements()) {
			Token t = (Token)enumeration.nextElement();
			System.out.println(enumeration_keys.nextElement() + " 	, "+t.getTipo()+ " 	, "+t.getTipoVar());
		}
		System.out.println("\n");
	}

}
