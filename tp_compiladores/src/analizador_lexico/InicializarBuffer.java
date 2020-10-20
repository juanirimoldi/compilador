package analizador_lexico;

public class InicializarBuffer extends AccionSemantica {
	
	public void ejecutar(char c, int nro_linea) {
		super.buffer = "";
		super.tipo_token = "";
		
		super.buffer += c;
		
		//defino el tipo de los que se inicializan y se les pueden cargar caracteres
		if (Character.isLetter(c)) { super.tipo_token = "ID"; }
		if (Character.isDigit(c)) { super.tipo_token = "CTE"; super.tipo_variable = "INTEGER";}
		//por defecto es integer, hasta demostrar lo contrario (agregando un . por ej)

		if (c == '.') { super.tipo_token = "CTE"; super.tipo_variable = "DOUBLE";}
		if (c == '%') { super.tipo_token = "COMENT"; }
		
		
		//System.out.println("AS1 -> Inicializo buffer, agrego caracter "+ c +" , tipo "+ super.tipo_buffer);
	}
	
	
	public Token getToken() {
		return null;
	}
}
