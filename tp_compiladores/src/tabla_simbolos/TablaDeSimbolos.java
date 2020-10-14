package tabla_simbolos;

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
	public TablaDeSimbolos() {
		this.TSimbolos = new Hashtable<String, Token> ();
	}
	
	
	public void addToken(Token t) {
		//if existe en tabla de tipos tdt
			//nada
		//sino
			//agrego tipo
		
		//if existe en tabla de simbolos
			//msj que dice que ya existe
		//sino
			//agrego el token en tabla de simbolos
		this.TSimbolos.put(t.getLexema(), t);
	}
	
}
