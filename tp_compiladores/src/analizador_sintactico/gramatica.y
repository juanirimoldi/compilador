%{
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;  //????

import analizador_lexico.*;
%}


%token
	ID
	CTE
	CADENA
	/* Palabras reservadas */
	IF
	ELSE
	END_IF
	PROC
	NI
	REF
	OUT
	/* Tipo de Constantes */
		
	/* Comparadores */
	MAYORIGUAL
	MENORIGUAL
	IGUAL
	DISTINTO
	EOF


%%


/*

programa : lista_de_sentencias //
	 ;


lista_de_sentencias : sentencia
		    | lista_de_sentencias sentencia
		    ;


sentencia : sentencia_declarativa
          | sentencia_ejecutable
	  ;


sentencia_declarativa : declaracion_de_variable ';'
		      | declaracion_de_procedimiento ';'
		      ;
			
					  
declaracion_de_variable : tipo lista_de_variables
		        ;


declaracion_de_procedimiento : PROC ID '(' lista_de_parametros ')' NI '=' CTE '{' cuerpo_del_procedimiento '}'
			     | PROC ID '(' ')' NI '=' CTE '{' cuerpo_del_procedimiento '}'
			     ;
			
			
cuerpo_del_procedimiento : lista_de_sentencias
			 | lista_de_sentencias declaracion_de_procedimiento
			 ;			

		 		
lista_de_parametros : parametro_declarado ',' parametro_declarado ',' parametro_declarado
		    | parametro_declarado ',' parametro_declarado
		    | parametro_declarado 
		    ;


parametro_declarado : tipo ID
		    | REF tipo ID
		    ;


lista_de_variables : lista_de_variables ',' ID
		   | ID
		   ;


tipo : INTEGER
     | FLOAT
     ;


sentencia_ejecutable : asignacion ','
		     | clausula_de_seleccion ','
		     | sentencia_de_control ','
		     | sentencia_de_salida ','
		     | invocacion ','
					  ;
					
clausula_de_seleccion : IF '(' condicion ')' bloque_de_sentencias ELSE bloque_de_sentencias END_IF					
		      | IF '(' condicion ')' bloque_de_sentencias END_IF
		      ;
						
condicion : expresion '>' expresion
	  | expresion '<' expresion
	  | expresion MAYORIGUAL expresion
	  | expresion MENORIGUAL expresion
	  | expresion IGUAL expresion
	  | expresion DISTINTO expresion
	  ;
		 
		 
bloque_de_sentencias : sentencia
		     | '{' lista_de_sentencias '}'
		     ;
					
					
sentencia_de_salida : OUT '(' CADENA
		    ;
					
					
invocacion : ID '(' parametro_ejecutable ')'
	   ;
		  
		  
parametro_ejecutable : ID
		     | parametro_ejecutable ',' ID
		     ;
		 			
					
sentencia_de_control : LOOP bloque_de_sentencias UNTIL '(' condicion ')'
	             ;
										
					
asignacion : ID '=' expresion
 	   ;
					

expresion : expresion '+' termino
	  | expresion '-' termino
	  | termino
	  ;


termino : termino '*' factor
	| termino '/' factor
	| factor
	;
*/
		
		
factor : CTE {System.out.print("Entra en CTE en  regla factor ");}
       | '-' factor 
       | ID {System.out.print("Entra en ID en regla factor ");}
       ;
	   	


%%
	   	


//CODE



AnalizadorLexico lexico;


String ins;
StringTokenizer st;
boolean newline;




void yyerror(String s)
{
 System.out.println("par:"+s);
}



private int yylex() {
	Token token=lexico.getToken();

	if (token!=null){
	    yylval = new ParserVal(token); //var para obtener el token de la tabla
	    return token.getIdTipo(); //acceso a la entrada que devolvumos
	}
	//lexico devuelve i de token! y lexico en yylval lo asocie con la tabla de simbolos
	return 0;
}



void dotest() //esto esta de mas!
{
//BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
 //System.out.println("BYACC/J Calculator Demo");
 //System.out.println("Note: Since this example uses the StringTokenizer");
 //System.out.println("for simplicity, you will need to separate the items");
 //System.out.println("Separate items with spaces, i.e.: '( 3 + 5 ) * 2'");
 //while (true)
 while (true)
 {
 System.out.println();
 System.out.print("expression:");
 try
 {
  //ins = in.readLine();
  //System.out.println("Leo linea!! "+ins); 

 }
 catch (Exception e)
 {
 }
 st = new StringTokenizer(ins);
 System.out.println("EXPRESION A COMPROBAR -> "+ins);
 //ins es la entrada
 newline=false;
 yyparse();
 }
}




public static void main(String args[]) {
 	TablaTokens tt = new TablaTokens();
	TablaSimbolos ts = new TablaSimbolos();
	
 	AnalizadorLexico lexico = new AnalizadorLexico(tt, ts);
	lexico.abrirCargarArchivo();
	//lexico.mostrarTablaSimbolos(); 
	//lexico.getToken();
	
	Parser par = new Parser(false, lexico);
 	par.dotest();
 	//par.yyparse()
}