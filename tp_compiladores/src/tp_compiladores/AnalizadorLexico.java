package tp_compiladores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorLexico {
	private String codigo="";
	private String buffer_temporal="";
	
	private static int nro_linea=1;
	private static int pos_actual=0;
	private static int ultima_pos=0;
	
	private static boolean leer=false;
	
	private int filas_estados = 5; //de 0 a 3 estados. en total 14;
	private int columnas_caracteres_validos = 5; //l, d, =,  , otro 
	
	private int matriz_transicion_estados [][];
	private AccionSemantica matriz_acciones_semanticas [][];
	
	private TablaTokens TTok;
	private TablaSimbolos TSym;

	
	
	public AnalizadorLexico() {
		this.TTok = new TablaTokens();
		this.TSym = new TablaSimbolos();
		
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
		//fila 0
		AccionSemantica AS1 = new InicializarBuffer();
		AccionSemantica AS2 = new AgregarCaracter();
		AccionSemantica AS3 = new LlegaTokenValido(this.TTok, this.TSym); 
		AccionSemantica AS4 = new DescartarBuffer();
		AccionSemantica ASF = new EntregarTokenYReiniciar();
		//AccionSemantica AS5 = new EntregarTokenIndividual(this.TTok, this.TSym); 

		
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
		
		
		//fila EstadoFinal
		this.matriz_acciones_semanticas[3][0] = ASF; //a Ef le llega una letra -> reinicio Buffer
		this.matriz_acciones_semanticas[3][1] = ASF; //a Ef le llega una letra -> reinicio Buffer
		this.matriz_acciones_semanticas[3][2] = ASF; //a Ef le llega una letra -> reinicio Buffer
		this.matriz_acciones_semanticas[3][3] = ASF; //a Ef le llega una letra -> reinicio Buffer
		this.matriz_acciones_semanticas[3][4] = ASF; //a Ef le llega una letra -> reinicio Buffer
	}
	
	
	
	
	public void abrirCargarArchivo() {
		///Lee fichero y lo carga en un String
		
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
        }catch(Exception e){
	         e.printStackTrace();
	    } 
	}
	
	
	
	public void leerCodigo() { //procesa el codigo con AS
        
        AccionSemantica AS;
        			
		int estado_actual = 0; //estado inicial de cada nueva linea es el estado 0
		int nro_columna; //el nro de columna depende del tipo de caracter
		char caracter = ' ';
		
			
		while (this.leer){ //mientras haya caracteres por leer
			String aux = "";
			this.pos_actual = this.ultima_pos;
			
			while (estado_actual != -1) {
				//System.out.println("posicion de string "+pos+" , caracter "+c);
				caracter = this.codigo.charAt(this.pos_actual); //leo caracter
				System.out.println("\n");
				System.out.println("Caracter actual " + caracter);
				
				
				nro_columna = getColumnaCaracter(caracter);
				
				System.out.println("Voy a Posicion matriz: " + estado_actual + " , " + nro_columna);
				
				AS = this.matriz_acciones_semanticas[estado_actual][nro_columna];
				
				AS.ejecutar(caracter,  this.buffer_temporal);
				
				
				estado_actual = matriz_transicion_estados[estado_actual][nro_columna];
				
				aux+=caracter;
				this.pos_actual ++;
			}
			
			//estando en el estado final... (-1)
			nro_columna = getColumnaCaracter(caracter);
			estado_actual = 3; //estado final!
			
			AS = this.matriz_acciones_semanticas[estado_actual][nro_columna];
			//aca estoy en estado final. tengo que entregar token y reiniciar buffer
			//esta accion semantica solo puede ser ASF
			AS.ejecutar(caracter, "");
			//devuelvo token, dejo de leer y espero
			//this.pos++;
			//System.out.println(AS.getToken());
			this.leer = false;
			
			this.ultima_pos = this.pos_actual;
			System.out.println("Retorno token -> " + AS.getToken());
			aux = "";
		}
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
		
		
		return nro_columna; //retorna el nro de columna asociado al tipo del caracter
	}
	
	
	
	
	public void yylex() { //invoco a yylex()
		this.leer = true;
		this.leerCodigo();
		//return this.buffer_temporal;
	}
}
