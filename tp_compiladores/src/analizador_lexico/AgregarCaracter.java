package analizador_lexico;


public class AgregarCaracter extends AccionSemantica {
	
	public void ejecutar(char c, int nro_linea) {
		System.out.println("AS2 -> Agrego caracter "+c);
		super.buffer += c;
		
		if (super.tipo_buffer.equals("COMENT")) {
			if (c == '\n') {
				//System.out.println("Si el tipo es COMMENT y el caracter un salto d linea ");
				System.out.println("\n COMENTARIO \n");
				System.out.println(super.buffer);
				super.buffer = "";
				super.tipo_buffer="";
			}
		}
	}
	
	public Token getToken() {
		return null;
	}
}
