package tp_compiladores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorLexico {
	private int filas_estados = 14;
	private String columnas_caracteres_validos = " l L d _ i . f + - * / > < = ! { } ( ) , ; % otro \n BL/TAB ";
	//columnas_caracteres_validos esta definido como un string separado por espacios
	//se puede definir de cualquier otro tipo, o no definir y solo darle la longitud. eso tenemos que verlo... 
		
	private int matriz_transicion_estados [][];
	private AccionSemantica matriz_acciones_semanticas [][];
	
	private TablaSimbolos TSym;

	private ArrayList<String> palabrasReservadas; //si no esta, devolver error!
	
	private String bufferInicial=""; //variable global usada por las AS

	
	
	public AnalizadorLexico() {
		this.palabrasReservadas = new ArrayList<String> ();
		//cargar lista de palabras reservadas
		
		//crea la estructura e inicializa la matriz con tamaño fijo
		this.matriz_transicion_estados = new int[this.columnas_caracteres_validos.length()][this.filas_estados];
		//aca habria que cargar la matriz de transicion de estados
		//podriamos llamar a una funcion que lo haga, o hacerlo aca directamente
		
		
		//crea la matriz de acciones semanticas y la inicializa con el mismo tamaño
		this.matriz_acciones_semanticas = new AccionSemantica[this.columnas_caracteres_validos.length()][this.filas_estados];
		//aca podriamos llamar a una funcion que cargue la matriz de AS
	}
	
	
	
	// funcion que abre y procesa el codigo (el archivo casos_prueba.txt)
	//1. lee programa fuente, liea por linea, caracter por caracter
	//2. por cada caracter leido, matchea en las matrices
	//3. se ejecutan las AS hasta que eventualmente genera un token valido y lo devuelve
	//4. en caso de generar un token, la funcion yylex devuelve el id asociado al token generado(en Tsym) ?? 
	
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
	        		//para que funcione hay que cargar la matriz de AS e implementar cada uno de sus metodos ejecutar

	        		// la AS corrobora si el token es valido o no
	        		// en caso de ser valido, la AS crea el Token y lo guarda en Tsym 
	        		// la AS devuelve el id del token guardado en la Tsym como retorno de la funcion yylex()

	        		
	        		
	        		//obtenemos el estado transicion: el sgte estado
	        		estado_actual = matriz_transicion_estados[estado_actual][nro_columna];
	        			        		
	        			        		
	        		// yylex(); ...hay que redefinirla? 
	        		
	        		
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
		// funcion que resuelve el tipo de caracter: que camino seguir en el automata (que columna)
		// si el caracter es una letra minuscula, mayuscula, digito, comentario, etc
		
		//devolver el numero de columna asociado
		
		int nro_columna=0;
		
		if (Character.isLowerCase(c)) { nro_columna=1; }; 
		if (Character.isDigit(c)) { nro_columna=2; }; 
		Character.isWhitespace(c); //return 0 (vuelvo a estado inicial)
		
		//if c = '+' -> return x;
		//if . -> ...
		
		
		return nro_columna; //retorna el nro de columna asociado al tipo del caracter
	}
	
	
	
	public AccionSemantica getAccionSemantica(int fil, int col) {
		return this.matriz_acciones_semanticas[fil][col];
	}
	
	
}
