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
	PROC
	RETURN
	NI
	REF
	OUT
	
	/* Sentencias de Control*/
	LOOP
	UNTIL
	
	/* Tipo de Constantes */
	INTEGER
	FLOAT
		
	/* Comparadores */
	MAYORIGUAL
	MENORIGUAL
	IGUAL
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


sentencia : sentencia_declarativa ';' {System.out.println("SENTENCIA DECLARATIVA ");}
		  | sentencia_ejecutable ';' {System.out.println("SENTENCIA EJECUTABLE ");}
	  	  ;





//SENTENCIAS DECLARATIVAS

//aca los ; andan
sentencia_declarativa : declaracion_de_variable  {System.out.println("DECLARO VARIABLE! ");}
				      | declaracion_de_procedimiento  {System.out.println("DECLARO PROCEDIMIENTO ");}
				      ;

		
declaracion_de_variable : tipo lista_de_variables {System.out.println("DECLARO VARIABLE con TIPO ");}
				        ;


//!!!VER!!!
declaracion_de_procedimiento : PROC ID '(' lista_de_parametros ')' NI '=' CTE '{' cuerpo_del_procedimiento '}'
						     | PROC ID '(' ')' NI '=' CTE '{' cuerpo_del_procedimiento '}'
			     			 ;
			
			
cuerpo_del_procedimiento : lista_de_sentencias
			 			 | lista_de_sentencias declaracion_de_procedimiento
			 			 ;			


//numero maximo de parametros = 3		 		
//puede no haber parametros!!   !!VER!!

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

		
sentencia_ejecutable : asignacion {System.out.println("SENTENCIA EJECUTABLE -> ASIGNACION! ");}
					 | clausula_de_seleccion ',' {System.out.println("CLAUSULA de SELECCION  IF ");}
		   			 | sentencia_de_control ','
		   			 | sentencia_de_salida ','
		   			 | invocacion ','
		   			 | 
					 ;



asignacion : ID '=' expresion ';' {System.out.println("HAGO ASIGNACION! "+$1);
									//checkeo semantico dentro de las reglas
								   asignacion.ptr = crear_terceto(=, ID.ptr, EXPRESION.ptr); } //$1, $$ etc.. y genero el terceto
		   ;
//expr.ptr nro de terceto que tiene la expresion

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
				     | '{' lista_de_sentencias '}' //DEFINIDA ARRIBA!! lista de sentencias declarativas o ejecutables
				     ;
					

sentencia_de_control : LOOP bloque_de_sentencias UNTIL '(' condicion ')'
		             ;

					
sentencia_de_salida : OUT '(' CADENA ')'
				    ;
					
					
invocacion : ID '(' parametro_ejecutable ')'
	   	   ;
		  

//!!!VER!!! 		  
parametro_ejecutable : ID':'ID  //para las invocaciones a parametros!!
		   		     | parametro_ejecutable ',' ID':'ID
		     		 ;
		 			
					
					
					
//terceto -> clase con lista de objetios terceto
// a = b -> checkeo semantico! tiene que existir a y tiene que existir b -> chequeos semanticos dentro de las llaves
// si llego a la raiz -> lista bien escrita -> se lo entrego  a generador de coigo para hacer asembler


expresion : expresion '+' termino {System.out.println("EXPRESION... ");
								   expresion.ptr = crear_terceto(+, expresion.ptr, termino.ptr); }
	  	  
	  	  | expresion '-' termino {System.out.println("EXPRESION... ");
			      				   expresion.ptr = crear_terceto(+, expresion.ptr, termino.ptr); }
	  	  
	  	  | termino  {System.out.println("de EXPRESION a TERMINO... ");
	  	  			  expresion.ptr = termino.ptr	}
	  	  ;


termino : termino '*' factor {System.out.println("TERMINO..");}
		
		| termino '/' factor
		
		| factor {System.out.println("de regla TERMINO a FACTOR..");
				  termino.ptr = factor.ptr}
		;
		
		
		
factor : CTE {System.out.println("CTE!! entra en regla factor \n");
			  factor.ptr = CTE.ptr; }
       
       | '-' factor 
       
       | ID {System.out.println("ID!! entra en regla factor ");
       		 factor.ptr = ID.ptr; }
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
