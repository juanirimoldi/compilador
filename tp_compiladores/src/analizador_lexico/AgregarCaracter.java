package analizador_lexico;


public class AgregarCaracter extends AccionSemantica {
	
	public void ejecutar(char c, int nro_linea) {
		//System.out.println("AS2 -> Agrego caracter "+c);
		super.buffer += c;
		
		
		//si el primer caracter es un digito y viene un punto -> cambio tipo de variable a FLOAT
		if (Character.isDigit(super.buffer.charAt(0)) & (c == '.')) {
			super.tipo_variable = "FLOAT";
		}
		
		
		if (super.tipo_token.equals("COMENT")) {
			if (c == '\n') {
				//System.out.println("Si el tipo es COMMENT y el caracter un salto d linea ");
				System.out.println("\n COMENTARIO \n");
				System.out.println(super.buffer);
				System.out.println("--------------------------------\n");
				super.buffer = "";
				super.tipo_token="";
			}
		}
	}
	
	
	public Token getToken() {
		return null;
	}
}
