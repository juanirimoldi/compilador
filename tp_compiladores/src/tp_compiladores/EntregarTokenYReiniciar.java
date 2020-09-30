package tp_compiladores;

public class EntregarTokenYReiniciar extends AccionSemantica {
	private TablaTokens tt;
	private TablaSimbolos ts;
	
	private Token t;
	private int id = 40;
	
	
	//Accion Semantica Final 
	public EntregarTokenYReiniciar(TablaTokens tt, TablaSimbolos ts) {
		this.tt = tt;
		this.ts = ts;
	}
	
	
	
	public void ejecutar(char c, int nro_linea) { 
		
		//defino si el token es simple, sin necesidad de inicializar buffer temporal
		if (c == '=') { super.tipo_buffer = "COMP"; };
		if (c == ';') { super.tipo_buffer = "PUNT"; };

		
		
		// busco si existe el tipo de token en tabla de tipos de Token
		if (!this.tt.existe(super.tipo_buffer)) { //si no existe el tipo en la tabla de id de Tokens
			this.tt.addToken(id, super.tipo_buffer); //lo agrego en la hash
			this.id++;
		}
		

		//lo cargo al nuevo token y al toque aplico una fucion que busca su llave
		int id_tipo = this.tt.getIdTipo(super.tipo_buffer); // busco el id de tipo de token
		

		// busco si existe en tabla de simbolos
		if (this.ts.existe(super.buffer, super.tipo_buffer)) { //si existe el lexema
			System.out.print("Ya existe registro en la Tsym!" + super.buffer);
			//return buffer + punt_TS
			
		} else { //si no existe en la tsym	
			this.t = new Token(super.buffer, super.tipo_buffer, nro_linea, id_tipo);
			this.ts.addTokenLista(t); //doy de alta en Tsym
			//return buffer + punt_TS
		}
		
		//aun no lo retorno, sino que modifico la variable privada
		
		
		System.out.println("ASF -> Entregar token  "+t.getLexema()+" , tipo "+ t.getTipo() +"  y reinicio buffer \n");
		
		super.buffer = ""; //despues de entregar token lo limpio
	}
	
	
	public Token getToken() {
		return this.t; 
	}
}
