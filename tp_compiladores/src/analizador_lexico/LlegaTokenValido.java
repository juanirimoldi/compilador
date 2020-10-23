package analizador_lexico;

public class LlegaTokenValido extends AccionSemantica {
	private int id_long_max=20;
	
	//si detecta comentario -> volver a E0
	
	public void ejecutar(char c, int nro_linea) {
		//System.out.println("AS3 -> Voy a Ef con un Token Valido -> "+ super.buffer + " , tipo ->"+super.tipo_buffer);
		
		int ascii = (int)c;
		//if (super.buffer == '=') {
		//TOKEN DOBLE!!! dos tokens simples!! como entrego los dos por separado?
		//System.out.println("LLEGA TOKEN VALIDO!! "+super.buffer+" , char "+c);	
		
		if (ascii == 32){
			//System.out.println("ESPACIO EN BLANCO -> "+ascii);
			//continue;
		} else if (ascii == 61) {
			//System.out.println("IGUAL -> "+ascii);
			//continue;
			super.tipo_token="IGUAL";
			super.buffer += c;
		} 
	
		if ((c == ';') || (c == ',')) {
			super.tipo_token = "PUNT";
			super.buffer += c;
		} else if ((c == '+') || (c == '-') || (c == '*') || (c == '/')) { 
			super.tipo_token = "OP";
			super.buffer += c;
		} 
		
		
		//COMPARADORES!
		//if (c == '<') ->
		//if (c == '>')
		
		//super.token_valido=true;
		
		
		
		//aca checkeo tipos y rango de un token valido -> hago AS 3.1 , 3.2 y 3.3
		
		
		//AS 3.1
		if (super.tipo_token.equals("ID")) {
			//System.out.println("TOKEN ID VALIDO -> " + super.buffer+" , "+super.buffer.length());
			if (super.buffer.length() > this.id_long_max) {
				System.out.println("WARNING!!!! TE CEBASTE.. -> " + super.buffer+" , "+super.buffer.length());
				super.buffer = super.buffer.substring(0, id_long_max);
				System.out.println("TRUNCADO -> " + super.buffer+" , "+super.buffer.length());

			}
		}

		//if (super.tipo == "INTEGER") -> checkeo tipos de enteros y trunco si es necesario
		//if (super.tipo == "DOUBLE") -> checkeo tipos de enteros y si es necesario trunco

	}
	
	
	public Token getToken() {
		return null;
	}
}
