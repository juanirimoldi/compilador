package analizador_lexico;

import java.io.BufferedReader;
import java.io.FileReader;

import tabla_simbolos.TablaDeSimbolos;

import java.io.File;


public class Main {

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		// TODO Auto-generated method stub    
	    //String path_archivo = args[0];
	    //asi ejecuto pasando archivo como parametro
	    

	      
	    //TablaDeSimbolos tds = new TablaDeSimbolos();
	    
	    String path_archivo = "casos_prueba.txt";
	    
	    AnalizadorLexico a = new AnalizadorLexico(path_archivo);//, tds);//, tt, ts);
		
	    
	    a.abrirCargarArchivo(); //primero intenta abrir archivo. Si lo abre correctamente, lo lee

	    System.out.println("\n COMIENZA A LEER TOKENS \n\n");
	    
	    
	    while (a.quedanTokens()) {
	    	//a.yylex();
	    	Token yylex = a.getToken();
	    	if (yylex != null) {
	    		//System.out.println("\n \n TOKEN -> " + yylex.getLexema() + " , " + yylex.getTipo() + "\n");
	    		System.out.println("\n ---------------------------------------------");
	    	} else {
	    		//System.out.println("NO HAY MAS TOKENS! \n \n");
	    	}
	    }
	   	
	    System.out.println();   
	    a.mostrarTablaDeSimbolos();
	}

}
