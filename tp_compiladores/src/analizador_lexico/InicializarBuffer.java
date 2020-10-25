package analizador_lexico;

public class InicializarBuffer extends AccionSemantica {
	
	public void ejecutar(char c, int nro_linea) {
		super.buffer = "";
		super.tipo_token = "";
		
		//int ascii = (int)c;
		//System.out.println("llega caracter -> "+c+"  ,  ascii -> "+ascii);
		//super.buffer += c;
		
		//defino los tokens simples -> el tipo de los que se inicializan y se les pueden cargar caracteres
		if (Character.isLetter(c)) { super.tipo_token = "ID"; 
									 super.buffer += c;
									 super.tipo_variable = "STRING";
									}

		if (Character.isDigit(c)) { super.tipo_token = "CTE"; 
									super.tipo_variable = "INTEGER";
									super.buffer += c;
								 }

		if (c == '.') { super.tipo_token = "CTE"; 
						super.tipo_variable = "FLOAT";
						super.buffer += c;
						}
		
		if (c == '%') { super.tipo_token = "COMENT"; 
						super.buffer += c;
						}
		
		if ((c == '<') | (c == '>') | (c == '!')) { 
			super.tipo_token = "COMP"; 
			super.buffer += c;
		}

		
		//if (super.tipo_token.equals("ID") | super.tipo_token.equals("CTE") | super.tipo_token.equals("CADENA")) {
		//	super.buffer += c;
		//}
		//System.out.println("AS1 -> Inicializo buffer, agrego caracter "+ c +" , tipo "+ super.tipo_buffer);
	}
	
	
	public Token getToken() {
		return null;
	}
}
