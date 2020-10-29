package analizador_lexico;

public class LlegaTokenValido extends AccionSemantica {
	private int id_long_max=20;
	private int rango_max_int=(int)Math.pow(2, 15) - 1;
	private int rango_min_int=(int)Math.pow(2, 15)*(-1);
	private float rango_pos_min_fl = 1.17549435f-38 ;
	private float rango_pos_max_fl = 3.40282347f+38;
	private float rango_neg_min_fl = -3.40282347f+38;
	private float rango_neg_max_fl = -1.17549435f-38;

	//si detecta comentario -> volver a E0
	
	public void ejecutar(char c, int nro_linea) {
		//System.out.println("AS3 -> Voy a Ef con un Token Valido -> "+ super.buffer + " , tipo ->"+super.tipo_buffer);
		
		int ascii = (int)c;
		//if (super.buffer == '=') {
		//TOKEN DOBLE!!! dos tokens simples!! como entrego los dos por separado?
		//System.out.println("LLEGA TOKEN VALIDO!! "+super.buffer+" , char "+c);	
		
		if (ascii == 32){
			if (super.tipo_variable.equals("CADENA")){
				super.buffer += c;
			}
			//System.out.println("ESPACIO EN BLANCO -> "+ascii);
			//continue;
		} else if (ascii == 61) {
			//System.out.println("IGUAL -> "+ascii);
			//continue;
			super.tipo_token="IGUAL";
			super.buffer += c;
		} 
	
		
		if ((c == '+') || (c == '-') || (c == '*') || (c == '/')) { 
			super.tipo_token = "OP";
			super.buffer += c;
		} 
		
		
		if ((c == '<') | (c == '>') | (c == '!')) { 
			super.tipo_token = "COMP";
			super.buffer += c;
		} 
		
		
		if ((c == '(') | (c == ')')) { 
			super.tipo_token = "PARENTESIS";
			super.buffer += c;
		}
		
		
		if ((c == '{') | (c == '}')) {
			super.tipo_token = "LLAVES";
			super.buffer += c;
		}
		
		
		if ((c == ';') | (c == ',') | (c == ':')) {
			super.tipo_token = "PUNT";
			super.buffer += c;
		} 
		
		
		
		super.token_valido=true;
		
		
		
		//aca checkeo tipos y rango de un token valido -> hago AS 3.1 , 3.2 y 3.3
		
		
		//AS 3.1
		if (super.tipo_token.equals("ID")) {
			//System.out.println("TOKEN ID VALIDO -> " + super.buffer+" , "+super.buffer.length());
			if (super.buffer.length() > this.id_long_max) {
				System.out.println("\n WARNING!!!! te pasaste.. -> " + super.buffer+" , "+super.buffer.length());
				super.buffer = super.buffer.substring(0, id_long_max);
				System.out.println("TRUNCADO -> " + super.buffer+" , "+super.buffer.length());

			}
		}
		
		if (super.tipo_token.equals("CTE")) {
			if (super.tipo_variable.equals("INTEGER")) {
				int nro = Integer.parseInt(super.buffer);
				if (nro > this.rango_max_int) {
					super.token_valido=false;
					System.out.println("\n TE PASASTE DE RANGO MAXIMO de INTEGERS! "+nro+" > "+this.rango_max_int+"\n");
				}
				if (nro < this.rango_min_int) {
					super.token_valido=false;
					System.out.println("\n TE PASASTE DE RANGO MINIMO de INTEGERS! "+nro+" > "+this.rango_min_int+"\n");
				}
			}
			
			//VER!! QUE ONDA LOS RANGOS DE LOS FLOATS
			/*
			if (super.tipo_variable.equals("FLOAT")) {
				float nro_f = Float.parseFloat(super.buffer);
				super.token_valido = false;
				if ((this.rango_pos_min_fl < nro_f) & (nro_f < this.rango_pos_max_fl)) {
					super.token_valido=true;
					System.out.println("\n ENTRAAA dentro de rango positivo! "+nro_f+" > "+this.rango_max_int+"\n");
				}
				if ((this.rango_neg_min_fl < nro_f) & (nro_f < this.rango_neg_max_fl)) {
					super.token_valido=true;
					System.out.println("\n ENTRAA dentro de rango de negativos! "+nro_f+" > "+this.rango_min_int+"\n");
				}
				System.out.println("\n que onda el float????! "+nro_f+" > "+super.token_valido+"\n");
				
			}
			*/
		}
	
	}
	
	
	
	public Token getToken() {
		return null;
	}
}
