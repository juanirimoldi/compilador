package analizador_lexico;

public class EntregarTokenYReiniciar extends AccionSemantica {
	private TablaTokens tt;
	private TablaSimbolos ts;
	
	private Token t;
	private int sgte_id = 300;
	private int linea_actual;
	
	
	//Accion Semantica Final 
	public EntregarTokenYReiniciar(TablaTokens tt, TablaSimbolos ts) {
		this.tt = tt;
		this.ts = ts;
	}
	
	
	
	public void ejecutar(char c, int nro_linea) { 
		//super.buffer += c;
		this.linea_actual = nro_linea;
		//defino los tipos de token simples que puede entregar al sintactico
		if (c == '=') {  super.tipo_buffer = "IGUAL"; };
		if ((c == ';') || (c == ',')) { super.tipo_buffer = "PUNT"; };
		if ((c == '+') || (c == '-') || (c == '*') || (c == '/')) { super.tipo_buffer = "OP"; }
		
		//System.out.println("TIPO TOKEN -> "+ super.tipo_buffer);
		
		//int id_tipo = this.tt.getIdTipo(super.tipo_buffer); 
		//System.out.println("ID TIPO TOKEN -> "+id_tipo);
		this.t = new Token(super.buffer, super.tipo_buffer, nro_linea);//, id_tipo);
		
		//System.out.println("Token!!!!!!!!!!!! -> "+this.t.getLexema()+" , tipo: "+this.t.getTipo()+" , ref_tipo: "+id_tipo);
		
		
		// busco en TablaTokens si existe el tipo de token
		if (!this.tt.existe(super.tipo_buffer)) { //si no existe el tipo
			// 
			this.t.setIdTipo(sgte_id);
			//System.out.println("ID TIPO TOKEN -> "+super.tipo_buffer);

			this.tt.addToken(super.tipo_buffer); //lo agrego en la hash
			//this.id++;
			//tt.mostrarTokens();
		} else { //si existe -> le asigno el nro de identificador 
			int id_tipo = this.tt.getIdTipo(super.tipo_buffer);
			this.t.setIdTipo(id_tipo);
			//this.t.setIdTipo(id);
		}
		

		
		//if es cte, id o cadena
			//add

		// busco si existe en tabla de simbolos
		if (this.ts.existe(super.buffer, super.tipo_buffer)) { //si existe el lexema
			//System.out.println("El token  " +super.buffer+"  ya existe en la Tsym " );
			//return buffer + punt_TS
			
		} else { //si no existe en la tsym	
			//t.setIdTipo(id_tipo);
			//System.out.println("El token "+ super.buffer+ " NO existe en la TSymb");
			this.ts.addTokenLista(t); //doy de alta en Tsym
			
			//return buffer + punt_TS
		}
		
		
		
		//System.out.println("ASF -> Entregar token  "+super.buffer+" , tipo "+ super.tipo_buffer +"  y reinicio buffer \n");
		
		super.tipo_buffer="";
		super.buffer = ""; //despues de entregar token lo limpio
	}
	
	
	public Token getToken() {
		super.buffer = ""; //y despues de entregar token lo limpio?
		return t;
		}
}
