package tp_compiladores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorLexico {
	private int filas_estados = 4; //de 0 a 3 estados. en total 14;
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
		//this.matriz_transicion_estados[0][5] = 1; //al estado 0 llega 
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
		
		
		/*
		this.matriz_transicion_estados[3][0] = 0; //del Ef siempre va a E0
		this.matriz_transicion_estados[3][1] = 0; //del Ef siempre va a E0 ?
		this.matriz_transicion_estados[3][2] = 0; //del Ef siempre va a E0 ??
		this.matriz_transicion_estados[3][3] = 0; //del Ef siempre va a E0 ???
		*/
	}
	
	
	public void inicializarMatrizAccionesSemanticas() {
		//fila 0
		AccionSemantica AS1 = new InicializarBuffer();
		AccionSemantica AS2 = new AgregarCaracter();
		AccionSemantica AS3 = new EntregarToken(this.TTok, this.TSym); //TTok);
		AccionSemantica AS4 = new DescartarBuffer();
		
		
		this.matriz_acciones_semanticas[0][0] = AS1; //new InicializarBuffer(); //a inicio le llega letra -> inicializo buffer
		this.matriz_acciones_semanticas[0][1] = AS1; //new InicializarBuffer(); //a inicio le llega digito -> inicializo buffer
		this.matriz_acciones_semanticas[0][2] = AS4; //a inicio le llega blano ' ' -> 
		this.matriz_acciones_semanticas[0][3] = AS3; //new EntregarToken(); //a inicio le llega otro caracter -> ??
		this.matriz_acciones_semanticas[0][4] = AS3; //new EntregarToken(); //a inicio le llega otro caracter -> ??
		
		
		//fila 1
		this.matriz_acciones_semanticas[1][0] = AS2; //new AgregarCaracter(); //a E1 le llega letra -> agrego caracter a buffer
		this.matriz_acciones_semanticas[1][1] = AS2; //new AgregarCaracter(); //a E1 le llega digito -> agrego caracter a buffer
		this.matriz_acciones_semanticas[1][2] = AS3; //new EntregarToken(); //a E1 le llega blanco ' ' -> entrego token
		this.matriz_acciones_semanticas[1][3] = AS3; //new EntregarToken(); //a E1 le llega '=' -> entrego token
		this.matriz_acciones_semanticas[1][4] = AS3; //new EntregarToken(); //a E1 le llega otro caracter -> entrego token
		//...
		
		//fila 2
		this.matriz_acciones_semanticas[2][0] = AS4; //new DescartarBuffer(); //a E2 le llega una letra -> descarto y vuelvo a inicio? o entregoToken?
		this.matriz_acciones_semanticas[2][1] = AS2; //new AgregarCaracter(); //a E2 le llega un digito -> 
		this.matriz_acciones_semanticas[2][2] = AS3; //new EntregarToken(); //a E2 le llega otro caracter -> entrego token tipo constante
		this.matriz_acciones_semanticas[2][3] = AS3; //new EntregarToken(); //a E2 le llega otro caracter -> entrego token tipo constante
		this.matriz_acciones_semanticas[2][4] = AS3; //new EntregarToken(); //a E2 le llega otro caracter -> entrego token tipo constante
		
		
		//fila EstadoFinal
		//this.matriz_acciones_semanticas[3][0] = new InicializarBuffer(); //a Ef le llega una letra -> reinicio Buffer
		//this.matriz_acciones_semanticas[3][1] = new InicializarBuffer(); //a Ef le llega una letra -> reinicio Buffer
	}
	
	
	
	// funcion que abre el archivo casos_prueba.txt
	// corrobora que existe. si existe, lo lee linea por linea, caracter por caracter
	
	public File abrirArchivo() {
		File archivo = null;
	    FileReader fr = null;
	    BufferedReader br = null;

	    try {
	         // Abro fichero y creo FileReader 
	        archivo = new File ("casos_prueba_id_cte.txt");	        
	        fr = new FileReader (archivo);
	        
	        System.out.println("Archivo valido "+fr+ "\n");
	        
	        return archivo;
	        
	    }catch(Exception e){
	         e.printStackTrace();
	    } 
	    return null;
	}
	
	
	
	//2. por cada caracter leido, matchea en las matrices
	//3. se ejecutan las AS hasta que eventualmente genera un token valido y lo devuelve
	//4. en caso de generar un token, la funcion yylex devuelve el id asociado al token generado(en Tsym) ?? 
		
	public void leerArchivo() {
		// Lectura del fichero linea por linea
		File archivo = null; 
		FileReader fr = null;
	    BufferedReader br = null;
	    
	    
        String linea;
        int nro_linea = 0;
        
        try {
        	archivo = this.abrirArchivo();
	        
	        fr = new FileReader (archivo);

	        br = new BufferedReader(fr);
	        
	        AccionSemantica AS;//por cada linea arranco con una AS que va tomando distintas formas de acuerdoa su recorrido
			//AS.setTTok(TTok);
	        
	        while((linea=br.readLine()) != null) {
				nro_linea ++;
				System.out.println("nro linea "+nro_linea+" -> "+linea+"\n");

				int i=0; //primer lugar de la linea
				int estado_actual = 0; //estado inicial de cada nueva linea es el estado 0
				int nro_columna; //el nro de columna depende del tipo de caracter
				
				
				// leo caracter por caracter
				while (i < linea.length()){
					char caracter = linea.charAt(i); 
					System.out.println("\n");
					System.out.println("Caracter: " + caracter);
					
					
					nro_columna = getColumnaCaracter(caracter);
					
					int nro_fila = estado_actual;
					
					if (nro_fila == -1) { //si estoy en Ef -> reestablezco estado inicial
						//entregar token
						nro_fila = 0; //this.filas_estados;
					}
					
					System.out.println("Posicion matriz: "+nro_fila+" , "+nro_columna);
					
					AS = this.matriz_acciones_semanticas[nro_fila][nro_columna];
					
					
					AS.ejecutar(caracter);

					
					//obtenemos el estado transicion -> sgte estado
					estado_actual = matriz_transicion_estados[nro_fila][nro_columna];
						        		
					// yylex(); ...hay que redefinirla? 
					
					i++; 
				}
			System.out.println("Fin de archivo");
			}
        }catch(Exception e){
	         e.printStackTrace();
	    } finally{
	         // En el finally cerramos fichero, para asegurarnos
	         // que se cierra tanto si todo va bien como si salta una excepcion.
	         try{                    
	            if( null != fr ){   
	               fr.close();     
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	         }
	      }
	}
	

	
	public int getColumnaCaracter(char c) {
		// funcion que resuelve el tipo de caracter: que camino seguir en el automata (que columna)
		//devolver el numero de columna asociado
		int ascii = (int) c;
		//System.out.println("ASCII: "+ascii);
		
		int nro_columna=4; //inicializo con otro caracter
		
		
		if (Character.isLowerCase(c)) { nro_columna=0; }; 
		if (Character.isLetter(c)) { nro_columna=0; };
		
		if (Character.isDigit(c)) { nro_columna=1; }; 
		if (Character.isWhitespace(c)) { nro_columna=2; }; 
		if (c == '=') { nro_columna=3; };
		
		//if (c == '%') { nro_columna=5; };
		
		
		return nro_columna; //retorna el nro de columna asociado al tipo del caracter
	}
	
	
}
