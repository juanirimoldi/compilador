package tp_compiladores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class AnalizadorLexico {
	private int matriz_transicion_estados[][];
	private AccionSemantica matriz_acciones_semanticas[][];
	
	
	public AnalizadorLexico() {
		//inicializar matriz de transicion de estados
		//inicializar matriz de acciones semanticas
	}
	
	//public void inicializarMatrizTransicionEstados() {
		//this.matriz_transicion_estados[][];
	//}
	
	
	
	
	//lee programa fuente
	//genera un token y lo devuelve 
	//la funcion yylex devuelve el id asociado al token (en Tsym) 
	
	public void abrirArchivo() {
		File archivo = null;
	    FileReader fr = null;
	    BufferedReader br = null;

	    try {
	         // Abro fichero y creo BufferedReader 
	        archivo = new File ("/home/andres/Documentos/Facultad/Diseño_Compiladores/casos_prueba.txt");
	        fr = new FileReader (archivo);
	        br = new BufferedReader(fr);

	         // Lectura del fichero
	        String linea;
	        int nro_linea = 0;
	        
	        while((linea=br.readLine()) != null) {
	        	nro_linea ++;
	        	
	        	//asi leo caracteres
	        	int i=0;
	        	//AS1;
	        	while (i < linea.length()){
	        		System.out.println("Nro linea: " + nro_linea + " caracter " + linea.charAt(i));
	        		i++;
	        		
	        		nro_fila, nro_columna <- getCamino(linea[i])
	        		matriz_transicion_estados[nro_fila][nro_columna]
	        		//id_token <- yylex(buffer)
	        	}
	        	//ejecuta AS salto de linea
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
	
	//primero lee el codigo fuente.
	//si existe -> lo abre y empieza a leer linea por linea, caracter por caracter
	//si no existe -> sacude un error
}
