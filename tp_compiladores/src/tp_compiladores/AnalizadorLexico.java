package tp_compiladores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class AnalizadorLexico {
	private String columnas_caracteres_validos = " l L d _ i . f + - * / > < = ! { } ( ) , ; % otro \n BL/TAB ";
	private int cant_estados = 14; 
	
	
	private int [][] matriz_transicion_estados;
	private AccionSemantica matriz_acciones_semanticas[][];
	private TablaSimbolos TSym;
	
	
	public AnalizadorLexico() {
		this.matriz_transicion_estados = new int[this.columnas_caracteres_validos.length()][this.cant_estados];
		//inicializar matriz de acciones semanticas
	}
	
	//public void inicializarMatrizTransicionEstados() {
		//this.matriz_transicion_estados[][];
	//}
	
	
	
	
	//lee programa fuente
	//genera un token y lo devuelve 
	//la funcion yylex devuelve el id asociado al token (en Tsym) 
	
	public void abrir_y_leerArchivo() {
		File archivo = null;
	    FileReader fr = null;
	    BufferedReader br = null;

	    try {
	         // Abro fichero y creo BufferedReader 
	        archivo = new File ("/home/andres/Documentos/Facultad/Diseño_Compiladores/casos_prueba.txt");
	        fr = new FileReader (archivo);
	        br = new BufferedReader(fr);


	        // Lectura del fichero linea por linea
	        String linea;
	        int nro_linea = 0;
	        
	        while((linea=br.readLine()) != null) {
	        	nro_linea ++;
	        	
	        	// leo caracteres
	        	int i=0;
	        	int id_fila = 0; //el estado inicial de cada nueva linea es el estado 0
	        	int id_columna; //depende del tipo de caracter
	        	
	        	while (i < linea.length()){
	        		//System.out.println("Nro linea: " + nro_linea + " caracter " + linea.charAt(i));
	        		char caracter = linea.charAt(i); //es mejor usar directamente linea.charAt(i)
	        		
	        		id_columna = getColumna(caracter); //funcion que busca en matriz de transicion de estados la columna correspondiente al caracter pasado como parametro
	        		
	        		// hasta aca tenemos el estado inicial(fila 0) y el ide_columna del tipo de caracter
	        		
	        		
	        		//obtenemos sgte estado para proxima columna
	        		int sgte_estado = getSgteEstado(id_fila, id_columna);
	        		
	        		
	        		
	        		//AccionSemantica AS = getAccionSemantica(id_fila, id_columna);
	        		//AS.ejecutar();
	        		
	        		//asi va ejecutando en cada paso de acuerdo a la accion semantica
	        		
	        		
	        		//hasta que en algun lado tendremos que corroborar si es un token valido
	        		
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
	
	
	public int getColumna(char c) {
		//obtengo el tipo de caracter
		//recorro la matriz de transicion de estados hasta encontrar la columna correspondiente al tipo
		return 0; //retorna el nro de columna asociado al tipo
	}
	
	
	
	public AccionSemantica getAccionSemantica(int fil, int col) {
		return this.matriz_acciones_semanticas[fil][col];
	}
	
}
