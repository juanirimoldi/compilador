package analizador_lexico;

import java.io.BufferedReader;
import java.io.FileReader;

import java.io.File;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub    

		//independizo las tablas del analizador lexico
		TablaTokens tt = new TablaTokens();
	    TablaSimbolos ts = new TablaSimbolos();
	    String arch = "casos_prueba.txt";
	    
	    AnalizadorLexico a = new AnalizadorLexico(arch, tt, ts);
		
	    a.abrirCargarArchivo(); //primero intenta abrir archivo. Si lo abre correctamente, lo lee

	    System.out.println("\n COMIENZA A LEER TOKENS \n");
	    
	    
	    while (a.quedanTokens()) {
	    	//a.yylex();
	    	Token yylex = a.getToken();
	    	if (yylex != null) {
	    		System.out.println("\n \n TOKEN -> " + yylex.getLexema() + " , " + yylex.getTipo() + "\n");
	    		System.out.println("\n -------------------------------------");
	    	} else {
	    		System.out.println("NO HAY MAS TOKENS! \n \n");
	    	}
	    }
	   	
	    System.out.println();   
	    a.mostrarTablaTokens();
	    a.mostrarTablaSimbolos();
	    //ts.eliminarSimbolo(";");
	    //a.mostrarTablaSimbolos();
	}

}
