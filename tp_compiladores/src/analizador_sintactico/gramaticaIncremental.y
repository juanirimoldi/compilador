%{
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;  //????
//package analizador_sintactico;

import analizador_lexico.*;
%}


%token
	ID
	CTE
	CADENA
	
	/* Palabras reservadas */
	IF
	THEN
	ELSE
	END_IF
	FUNC
	RETURN
	//NI
	//REF
	OUT
	
	/* Tipo de Constantes */
	INTEGER
	FLOAT
		
	/* Comparadores */
	MAYORIGUAL
	MENORIGUAL
	//IGUAL
	DISTINTO
	//PUNT
	EOF

%start programa


%%



programa : lista_de_sentencias {System.out.println("\n LLEGO A RAIZ! -> termino programa \n ");}
		 ;


lista_de_sentencias : sentencia
		   		    | lista_de_sentencias sentencia {System.out.println("LISTA DE SENTENCIAS RECURSIVA! ");}
		    		;


sentencia : sentencia_declarativa {System.out.println("TIPO DE SENTENCIA DECLARATIVA ");}
		  | sentencia_ejecutable {System.out.println("SENTENCIA -> EJECUTABLE ");}
	  	  ;





//SENTENCIAS DECLARATIVAS


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


//numero maximo de parametros = 3		 		

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





//SENTENCIAS EJECUTABLES

		
sentencia_ejecutable : clausula_de_seleccion ','
		   			 | sentencia_de_control ','
		   			 | sentencia_de_salida ','
		   			 | invocacion ','
		   			 | asignacion {System.out.println("SENTENCIA EJECUTABLE -> ASIGNACION! ");}
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
					

sentencia_de_control : LOOP bloque_de_sentencias UNTIL '(' condicion ')'
		             ;

					
sentencia_de_salida : OUT '(' CADENA ')'
				    ;
					
					
invocacion : ID '(' parametro_ejecutable ')'
	   	   ;
		  
		  
parametro_ejecutable : ID
		   		     | parametro_ejecutable ',' ID
		     		 ;
		 			
					


asignacion : ID '=' expresion ';' {System.out.println("HAGO ASIGNACION! "+$1);} //$1, $$ etc.. y genero el terceto
		;
					
					
//terceto -> clase con lista de objetios terceto
// a = b -> checkeo semantico! tiene que existir a y tiene que existir b -> chequeos semanticos dentro de las llaves
// si llego a la raiz -> lista bien escrita -> se lo entrego  a generador de coigo para hacer asembler


expresion : expresion '+' termino {System.out.println("EXPRESION... ");}
	  	| expresion '-' termino
	  	| termino  {System.out.println("de EXPRESION a TERMINO... ");}
	  	;


termino : termino '*' factor {System.out.println("TERMINO..");}
		| termino '/' factor
		| factor {System.out.println("de regla TERMINO a FACTOR..");}
		;
		
		
factor : CTE {System.out.println("CTE!! entra en regla factor \n");}
       	| '-' factor 
       	| ID {System.out.println("ID!! entra en regla factor ");}
       	;
	   	


%%
	   	


//CODE



AnalizadorLexico lexico;
TablaDeSimbolos tabla;




void yyerror(String s)
{
 System.out.println("par:"+s);
}



private int yylex() {
	Token token=lexico.getToken();
	//System.out.println("\n Dentro del Sintactico...\n");

	if (token!=null){
		tabla.addToken(token);
		
		//if token es id, cte o cadena -> tabla.addToken()
		//else todo lo de abajo -> seteo el idTipo y no lo guardo en Tsymb
		
		//esto checkearlo dentro de la tabla de simbolos!
		if (token.getTipo().equals("PUNT") || token.getTipo().equals("IGUAL") || token.getTipo().equals("OP")) {
			int ascii = (int)token.getLexema().charAt(0);
			token.setIdTipo(ascii);
		}
	    yylval = new ParserVal(token); //var para obtener el token de la tabla
	    return token.getIdTipo(); //acceso a la entrada que devolvumos
	}
	//lexico devuelve i de token! y lexico en yylval lo asocie con la tabla de simbolos
	return 0;
}





public static void main(String args[]) {
 	String direccion_codigo = "casos_prueba_id_cte.txt";
	
 	AnalizadorLexico al = new AnalizadorLexico(direccion_codigo);
	al.abrirCargarArchivo();
	TablaDeSimbolos tds = new TablaDeSimbolos();

	
	Parser par = new Parser(false, al, tds);
 	par.yyparse();
 	
 	tds.mostrarSimbolos();
