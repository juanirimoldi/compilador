package analizador_lexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


public class AnalizadorLexico {
	private String codigo="";
	
	private int nro_linea=1;
	private int total_lineas;
	private int total_caracteres;
	
	private int pos_actual=0;
	private int ultima_pos=0;
	
	private boolean leer_caracter=false;
	private boolean fin=false; //fin de "archivo"
	
	private int filas_estados = 4; //de 0 a 4 estados. en total ?
	private int columnas_caracteres_validos = 5; // hasta ahora -> l d = ' ' ; otro 
	
	private int estado_final = this.filas_estados - 1;

	
	private int matriz_transicion_estados [][];
	private AccionSemantica matriz_acciones_semanticas [][];
	
	private TablaTokens TTok; //hash <int, String>
	private TablaSimbolos TSym; //lista dinamica de registros Token

	
	
	public AnalizadorLexico(TablaTokens tt, TablaSimbolos ts) {
		//this.TTok = new TablaTokens();
		//this.TSym = new TablaSimbolos();
		this.TTok = tt;
		this.TSym = ts;
		
		this.matriz_transicion_estados = new int[this.filas_estados][this.columnas_caracteres_validos];
		this.inicializarMatrizTransicionEstados();
		
		this.matriz_acciones_semanticas = new AccionSemantica[this.filas_estados][this.columnas_caracteres_validos];
		this.inicializarMatrizAccionesSemanticas();
	}
	
	
	
	public void inicializarMatrizTransicionEstados() {
		//fila 0
		this.matriz_transicion_estados[0][0] = 1; //al E0 llega una letra l -> voy a E1
		this.matriz_transicion_estados[0][1] = 2; //al estado 0 llega un digito d -> voy a E2
		this.matriz_transicion_estados[0][2] = 0; //al estado 0 llega blanco ' ' -> ciclo en E0
		this.matriz_transicion_estados[0][3] = -1; //al estado 0 llega '=' -> voy a Ef 
		this.matriz_transicion_estados[0][4] = -1; //al estado 0 llega otro caracter -> voy a Ef
		//this.matriz_transicion_estados[0][5] = 1; //al estado 0 llega ';' -> fin de linea 
		//...
		
		
		//fila 1
		this.matriz_transicion_estados[1][0] = 1; //a E1 le llega una letra l -> ciclo en E1
		this.matriz_transicion_estados[1][1] = 1; //a E1 le llega un digito d -> ciclo en E1
		this.matriz_transicion_estados[1][2] = -1; //a E1 le llega blanco ' ' -> voy a Ef
		this.matriz_transicion_estados[1][3] = -1; //a E1 le llega un '=' -> voy a Ef
		this.matriz_transicion_estados[1][4] = -1; //a E1 le llega otro caracter -> voy a Ef
		//...
		
		
		//fila 2
		this.matriz_transicion_estados[2][0] = -1; //a E2 le llega una letra l -> voy a Ef
		this.matriz_transicion_estados[2][1] = 2; //a E2 le llega un digito d -> ciclo en E2
		this.matriz_transicion_estados[2][2] = -1; //a E2 le llega blanco ' ' -> voy a Ef
		this.matriz_transicion_estados[2][3] = -1; //a E2 le llega '=' -> voy a Ef
		this.matriz_transicion_estados[2][4] = -1; //a E2 le llega otro caracter -> voy a Ef
		//...
		
		
		
		this.matriz_transicion_estados[3][0] = 0; //del Ef siempre va a E0
		this.matriz_transicion_estados[3][1] = 0; //del Ef siempre va a E0 ?
		this.matriz_transicion_estados[3][2] = 0; //del Ef siempre va a E0 ??
		this.matriz_transicion_estados[3][3] = 0; //del Ef siempre va a E0 ???
		this.matriz_transicion_estados[3][4] = 0; //del Ef siempre va a E0 ???
	}
	
	
	public void inicializarMatrizAccionesSemanticas() {
		//Acciones Semanticas
		AccionSemantica AS1 = new InicializarBuffer();
		AccionSemantica AS2 = new AgregarCaracter();
		AccionSemantica AS3 = new LlegaTokenValido(this.TTok, this.TSym); 
		AccionSemantica AS4 = new DescartarBuffer();
		AccionSemantica ASF = new EntregarTokenYReiniciar(this.TTok, this.TSym);

		
		//fila 0
		this.matriz_acciones_semanticas[0][0] = AS1; //InicializarBuffer(); //a inicio le llega letra -> inicializo buffer
		this.matriz_acciones_semanticas[0][1] = AS1; //InicializarBuffer(); //a inicio le llega digito -> inicializo buffer
		this.matriz_acciones_semanticas[0][2] = AS4; //a inicio le llega blanco ' ' -> 
		this.matriz_acciones_semanticas[0][3] = AS3; //EntregarToken(); //a inicio le llega otro caracter -> ??
		this.matriz_acciones_semanticas[0][4] = AS3; //EntregarToken(); //a inicio le llega otro caracter -> ??
		
		
		//fila 1
		this.matriz_acciones_semanticas[1][0] = AS2; //AgregarCaracter(); //a E1 le llega letra -> agrego caracter a buffer
		this.matriz_acciones_semanticas[1][1] = AS2; //AgregarCaracter(); //a E1 le llega digito -> agrego caracter a buffer
		this.matriz_acciones_semanticas[1][2] = AS3; //LlegaTokenValido(); //a E1 le llega blanco ' ' -> entrego token y descarto blanco
		this.matriz_acciones_semanticas[1][3] = AS3; //LlegaTokenValido(); //a E1 le llega '=' -> entrego token 
		this.matriz_acciones_semanticas[1][4] = AS3; //LlegaTokenValido(); //a E1 le llega otro caracter -> entrego token
		//...
		
		//fila 2
		this.matriz_acciones_semanticas[2][0] = AS4; //DescartarBuffer(); //a E2 le llega una letra -> descarto y vuelvo a inicio? o entregoToken?
		this.matriz_acciones_semanticas[2][1] = AS2; //AgregarCaracter(); //a E2 le llega un digito -> 
		this.matriz_acciones_semanticas[2][2] = AS3; //EntregarToken(); //a E2 le llega otro caracter -> entrego token tipo constante
		this.matriz_acciones_semanticas[2][3] = AS3; //EntregarToken(); //a E2 le llega otro caracter -> entrego token tipo constante
		this.matriz_acciones_semanticas[2][4] = AS3; //EntregarTokenIndividual(); //a E2 le llega otro caracter -> entrego token tipo constante
		//...
		
		
		//fila EstadoFinal
		this.matriz_acciones_semanticas[3][0] = ASF; //a Ef le llega una letra -> reinicio estado y Buffer
		this.matriz_acciones_semanticas[3][1] = ASF; //a Ef le llega un digimon -> reinicio estado y Buffer
		this.matriz_acciones_semanticas[3][2] = ASF; //a Ef le llega un blanco -> reinicio estado y Buffer
		this.matriz_acciones_semanticas[3][3] = ASF; //a Ef le llega un = -> reinicio estado y Buffer
		this.matriz_acciones_semanticas[3][4] = ASF; //a Ef le llega una letra -> reinicio estado y Buffer
	}
	
	
	
	
	public void abrirCargarArchivo() {
		///Lee fichero y lo carga en el String privado codigo
		
		File archivo = null; 
		FileReader fr = null;
	    BufferedReader br = null;
	    
        String linea;
        int nro_linea = 0;
        
        try {
        	archivo = new File ("casos_prueba_id_cte.txt");	        
	        fr = new FileReader (archivo);
	        System.out.println("Archivo valido "+fr+ "\n");
	        
	        br = new BufferedReader(fr); 
	   
	        
	        while((linea=br.readLine()) != null) {
				nro_linea ++;
				System.out.println("nro linea "+nro_linea+" -> "+linea+"\n");
				this.codigo += linea + "\n";
			}
	        
	        this.total_lineas = nro_linea;
	        this.total_caracteres = this.codigo.length();
	        System.out.println();
        }catch(Exception e){
	         e.printStackTrace();
	    } 
	}
	
	
	
	public Token leerCodigo() { 
		//lee el codigo y ejecuta ASs hasta encontrar un token
        
        AccionSemantica AS;
        			
		int estado_actual = 0; //estado inicial 
		int nro_columna = -1; //nro de columna depende del tipo de caracter. por defecto arranco con otro

		char caracter = ' ';
		int ascii;
			
		Token titi = null;
		
		
		while (this.leer_caracter & !fin){ //mientras haya caracteres por leer y no sea fin de codigo			
			this.pos_actual = this.ultima_pos; //retomo a partir de la ultima posicion leida	
			System.out.println("\n Frontera -> " +  this.pos_actual + " ,  limite -> "+ this.ultima_pos +" , TOTAL -> "+this.total_caracteres);
	
			while (estado_actual != -1) {
					
				caracter = this.codigo.charAt(this.pos_actual); //leo caracter
				ascii = (int)caracter;
					
				System.out.println("\n");
				System.out.println("Caracter actual "+caracter+" , ASCII "+(int)caracter);					
										
				nro_columna = getColumnaCaracter(caracter);
				
				System.out.println("Voy a Posicion matriz: " + estado_actual + " , " + nro_columna);				
				
				AS = this.matriz_acciones_semanticas[estado_actual][nro_columna];
					
				
				AS.ejecutar(caracter, this.nro_linea);
					
				
				estado_actual = matriz_transicion_estados[estado_actual][nro_columna];	
				
				this.pos_actual++;
				
				
				//si es salto de linea
				if (ascii == 10) { //codigo ascii de \n es 10
					System.out.println("\n \n \n Salto de linea \n");
					this.nro_linea++;
					
					//condicion de corte
					if (this.nro_linea > this.total_lineas) {  // si me paso corta todo
						System.out.println();	
						System.out.println("FIN de codigo!! nro linea "+this.nro_linea+" > "+this.total_lineas);
						System.out.println();	
						
						this.leer_caracter = false;
						this.fin = true;
						estado_actual = -1;
					}
				}
			}
			
			
			
			//llega el estado final
			
			if (!fin) {	//si no es el fin de archivo (String), ejecuto ASFinal
					
				estado_actual = this.estado_final; 
				
				AS = this.matriz_acciones_semanticas[estado_actual][nro_columna];
				
				// ejecuto ASF -> entregar token, y reiniciar estado y buffer
				AS.ejecutar(caracter, this.nro_linea);
				
	
				//devuelvo token, dejo de leer y actualizo posicion
				titi = AS.getToken();
				
				this.leer_caracter = false; 
				
				this.ultima_pos = this.pos_actual; 
				
				System.out.println("\n Frontera -> " +  pos_actual + " ,  limite -> "+ ultima_pos +" ,  total -> " + this.total_caracteres);
				System.out.println("Return -> Nro Linea: " + this.nro_linea + " , Token: "+ titi.getLexema()+" , tipo "+titi.getTipo());
				}
		}
		return titi;
	}

	
	
	public int getColumnaCaracter(char c) {
	
		int nro_columna=5; //inicializo con otro caracter
		
		
		if (Character.isLowerCase(c)) { nro_columna=0; }; 
		if (Character.isLetter(c)) { nro_columna=0; };
		
		if (Character.isDigit(c)) { nro_columna=1; }; 
		if (Character.isWhitespace(c)) { nro_columna=2; }; 
		if (c == '=') { nro_columna=3; };
		if (c == ';') { nro_columna=4; };
		//if (c == '%') { nro_columna=5; };
		//...
		
		return nro_columna; //retorna el nro de columna asociado al tipo del caracter
	}
	
	
	
	public Token getToken() { //invoco a yylex()
		this.leer_caracter = true;
		
		Token t = this.leerCodigo();
		
		if (t != null) {
			//System.out.println("yylex Retorno ->  " + t.getLexema()+" , "+t.getTipo()); 
			return t;
		} else {
			System.out.println("Fin de archivo!");
			this.fin = true;
			//break;
		}
		
		System.out.println("\n \n ---------------------------------------------------\n");
		return null;
	}
	
	

	public boolean quedanTokens() {
		if (this.fin) {
			return false;
		}
		return true;
	}
	
	
	public void mostrarTablaTokens() {
		this.TTok.mostrarTokens();
		System.out.println();
	}
	
	
	public void mostrarTablaSimbolos() {
		System.out.println("\n Tabla de simbolos \n");
		this.TSym.mostrarListaTsym();
	}
}
