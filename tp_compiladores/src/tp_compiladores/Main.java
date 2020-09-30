package tp_compiladores;

import java.io.BufferedReader;
import java.io.FileReader;

import java.io.File;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub    
	    
	    AnalizadorLexico a = new AnalizadorLexico();
		
	    a.abrirCargarArchivo(); //primero hace abrir archivo. Si lo abre correctamente, lo lee
		//a.procesarCodigo();
	    //a.leerCaracterXCaracter();
	    a.yylex();
	    a.yylex();
	    a.yylex();
	    a.yylex();
	    a.yylex();

	    a.yylex();
	    a.yylex();
	    a.yylex();
	    a.yylex();
	    
	    a.mostrarTablaTokens();
	    a.mostrarTablaSimbolos();
	}

}
