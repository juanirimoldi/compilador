package tp_compiladores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class AnalizadorLexico {
	private String columnas_caracteres_validos = " l L d _ i . f + - * / > < = ! { } ( ) , ; % otro \n BL/TAB ";
	private int filas_estados = 14; 
	
	private int matriz_transicion_estados [][];
	private AccionSemantica matriz_acciones_semanticas [][];
	private TablaSimbolos TSym;
	
	
	
	public AnalizadorLexico() {
		//crea la estructura e inicializo la matriz con tamaño fijo
		this.matriz_transicion_estados = new int[this.columnas_caracteres_validos.length()][this.filas_estados];
		//aca podriamos llamar a una funcion que cargue la matriz de transicion de estados
		
		
		//crea la matriz y la inicializa con el mismo tamaño
		this.matriz_acciones_semanticas = new AccionSemantica[this.columnas_caracteres_validos.length()][this.filas_estados];
		//aca podriamos llamar a una funcion que cargue la matriz de AS
	}
	
	
	//public void inicializarMatrizTransicionEstados() {
		//this.matriz_transicion_estados[][] = {... };
	//}
	
	
	
	
	//1. lee programa fuente, liea por linea, caracter por caracter
	//2.por cada caracter leido, matchea matrices
	//3. la ejecucion de la AS eventualmente genera un token y lo devuelve
	//4. en caso de generar un token, la funcion yylex devuelve el id asociado al token generado(en Tsym) 
	
	public void abrir_y_procesarArchivo() {
		File archivo = null;
	    FileReader fr = null;
	    BufferedReader br = null;

	    try {
	         // Abro fichero y creo BufferedReader 
	        archivo = new File ("casos_prueba.txt");
	        fr = new FileReader (archivo);
	        br = new BufferedReader(fr);


	        // Lectura del fichero linea por linea
	        String linea;
	        int nro_linea = 0;
	        
	        while((linea=br.readLine()) != null) {
	        	nro_linea ++;
	        	
	        	int i=0; //inicializo el primer lugar de la linea
	        	int estado_actual = 0; //estado inicial de cada nueva linea es el estado 0
	        	int nro_columna; //el nro de columna depende del tipo de caracter
	        	AccionSemantica AS;//por cada linea arranco con una AS que va tomando distintas formas de acuerdoa su recorrido
	        	
	        	
	        	// leo caracter por caracter
	        	while (i < linea.length()){
	        		char caracter = linea.charAt(i); //es mejor usar directamente linea.charAt(i)
	        		
	        		nro_columna = getColumnaCaracter(caracter); //funcion que busca en matriz de transicion de estados la columna correspondiente al caracter pasado como parametro
	        		
	        		
	        		AS = matriz_acciones_semanticas[estado_actual][nro_columna];
	        		
	        		
	        		//AS.ejecutar();
	        		
	        		
	        		//obtenemos sgte estado para proxima columna
	        		estado_actual = matriz_transicion_estados[estado_actual][nro_columna];
	        		
	        			        		
	        		
	        		//hasta que en algun lado tendremos que corroborar si es un token valido. lo hace la AS?
	        		
	        		//if (esTokenValido)
	        		//  Token t = new Token();
	        		//	Tsym.addToken(token)
	        		//	Tsym[id_token] <- yylex(buffer)

	        		
	        		i++; //avanza de caracter
	        	}
	        	System.out.println("nro linea "+nro_linea+" -> "+linea);
	        }
	    }catch(Exception e){
	         e.printStackTrace();
	    }finally{
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
	
	
	
	
	public int getSgteEstado(int fil, int col) {
		return this.matriz_transicion_estados[fil][col];
	}
	
	
	public int getColumnaCaracter(char c) {
		//aca deberia resolver si el caracter es una letra minuscula, mayuscula, digito, comentario, etc
		//y devolver el numero de columna asociado
		
		int nro_columna;
		
		Character.isLowerCase(c); // -> nro_columna=1
		Character.isDigit(c); //return 2
		Character.isWhitespace(c); //return 0 (vuelvo a estado inicial)
		
		//if c = '+' -> return x;
		//if . -> ...
		
		//recorro la matriz de transicion de estados hasta encontrar la columna correspondiente al tipo
		
		return 0; //retorna el nro de columna asociado al tipo
	}
	
	
	
	public AccionSemantica getAccionSemantica(int fil, int col) {
		return this.matriz_acciones_semanticas[fil][col];
	}
	
	
	public void separarColumnaCaracteres() {
		
	}
}
