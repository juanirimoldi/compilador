package analizador_lexico;

public class InicializarBuffer extends AccionSemantica {
	
	public void ejecutar(char c, int nro_linea) {
		super.buffer = "";
		super.tipo_buffer = "";
		
		super.buffer += c;
		
		//defino el tipo de los que se inicializan y se les pueden cargar caracteres
		if (Character.isLetter(c)) { super.tipo_buffer = "ID"; }
		if (Character.isDigit(c)) { super.tipo_buffer = "CTE"; }
		
		if (c == '.') { super.tipo_buffer = "DOUBLE";}
		if (c == '%') { super.tipo_buffer = "COMENT"; }
		
		
		System.out.println("AS1 -> Inicializo buffer, agrego caracter "+ c +" , tipo "+ super.tipo_buffer);
	}
	
	
	public Token getToken() {
		return null;
	}
}
