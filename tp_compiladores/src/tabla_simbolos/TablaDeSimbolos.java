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
		System.out.println("AGREGANDO TOKEN EN TDS!!! \n");
		
		int id_tipo = this.tdt.getIdTipo(t.getTipo());
		t.setIdTipo(id_tipo);
		
		if (t.getTipo().equals("ID")){
			//agrego identificador de tipo y lo guardp
			this.TSimbolos.put(t.getLexema(), t);
			System.out.println("APARECE UN ID -> lo agrego "+ t.getTipo() + "\n");
		}
		if (t.getTipo().equals("CTE")){
			//agrego identificador de tipo y lo guardp
			this.TSimbolos.put(t.getLexema(), t);
			System.out.println("APARECE UNA CTE -> la agrego "+ t.getTipo() + "\n");
		}
		if (t.getTipo().equals("CADENA")){
			//agrego identificador de tipo y lo guardo
			this.TSimbolos.put(t.getLexema(), t);
			System.out.println("APARECE UNA CADENA -> la agrego "+ t.getTipo() + "\n");
		}
		
		
		//System.out.println("NO ESISTIS -> "+ t.getTipo() + "\n");
		
		//if existe en tabla de tipos tdt
			//nada
		//sino
			//agrego tipo
		
		//if existe en tabla de simbolos
			//msj que dice que ya existe
		//sino
			//agrego el token en tabla de simbolos
		//if (this.existe(t.getLexema())){
		//	System.out.println("\n\n YA existe lexemaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa! \n\n\n");
		//} else {
		//	this.TSimbolos.put(t.getLexema(), t);
//			this.mostrarSimbolos();
		//}
	}
	
	
	public boolean existe(String b) {
		//this.mostrarTokens();
		//System.out.println("Existe " + b + " en la Hash???? "+this.id_tipo.contains(b));
		return this.TSimbolos.contains(b);
	}
	
	
	public void mostrarSimbolos() {
		System.out.println("Tabla de SImbolos posta \n");

		System.out.println("id_token, lexema	");
		Enumeration enumeration_keys = this.TSimbolos.keys();
		
		Enumeration enumeration = this.TSimbolos.elements();
		while (enumeration.hasMoreElements()) {
			System.out.println(enumeration_keys.nextElement() + " 	, " + enumeration.nextElement());
		}
		System.out.println("\n");
	}

}
