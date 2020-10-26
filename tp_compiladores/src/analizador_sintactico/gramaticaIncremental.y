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


lista_de_sentencias : sentencia //{System.out.println("SENTENCIA SIMPLE! ");}
		   		    | lista_de_sentencias sentencia //{System.out.println("LISTA DE SENTENCIAS RECURSIVA \n ");}
		    		;


sentencia : sentencia_declarativa  //{System.out.println("SENTENCIA DECLARATIVA CORRECTA ");}
		  | sentencia_ejecutable // {System.out.println("SENTENCIA EJECUTABLE ");}
	  	  ;





//SENTENCIAS DECLARATIVAS

//aca los ; andan
sentencia_declarativa : declaracion_de_variable ';' //{System.out.println("VARIABLE DECLARADA! ");}
				      | declaracion_de_procedimiento  {System.out.println("DECLARO PROCEDIMIENTO ");}
				      ;

		
declaracion_de_variable : tipo lista_de_variables {//System.out.println("VARIABLE BIEN DECLARADA con TIPO!! ");
												   //Token t = (Token)yyval.obj;	
												   //System.out.println("yyval SAPE! "+t.getLexema());
												   Token tipo = (Token)$1.obj;
												   Token variable = (Token)$2.obj;
												   System.out.println("\n Sintactico  ->  VARIABLE BIEN DEFINIDA  "+tipo.getLexema()+" "+variable.getLexema()+"\n");
												   
												   //String tipo = (Token)$1.obj.getLexema();
												   //String lexema = (Token)$2.obj.getLexema(); 
								   				   System.out.println("\n----------------------------------------\n");
								   
												   }
				        ;


//!!!VER!!!
declaracion_de_procedimiento : PROC ID '(' lista_de_parametros ')' NI '=' CTE '{' cuerpo_del_procedimiento '}'
						     | PROC ID '(' ')' NI '=' CTE '{' cuerpo_del_procedimiento '}'
			     			 ;
			
			
cuerpo_del_procedimiento : lista_de_sentencias
			 			 //| lista_de_sentencias declaracion_de_procedimiento !!VER!! esto no reduce nunca
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

		
sentencia_ejecutable : asignacion //{System.out.println("SENTENCIA EJECUTABLE -> ASIGNACION! ");}
					 | clausula_de_seleccion {System.out.println("CLAUSULA de SELECCION  IF ");}
		   			 | sentencia_de_control 
		   			 | sentencia_de_salida 
		   			 | invocacion 
					 ;


//al hacer la asignacion debo checkear si la variable existe en la tabla de simbolos!!
//si existe -> hago la asignacion
//si no existe sacudo un error de que falta inicializar

asignacion : ID '=' expresion ';' {//System.out.println("OJO!!! checkear antes que exista el lexema en la Tabla de Simbolos");
								   Token id = (Token)$1.obj;
								   
								   System.out.println("OJO!!! checkear que  "+ id.getLexema() +"  exista en la Tabla de Simbolos");
								   System.out.println("EXISTE?? a ver, mostrala ");
								   tabla.mostrarSimbolos();
								   
								   Token op = (Token)$2.obj;
								   Token expr = (Token)$3.obj;
								   //es valida esta impleentacion? o consumo  memoria al crear tokens?
								   
								   
								   System.out.println("\n Sintactico -> asigno igual. COMO??   "+id.getLexema()+" "+op.getLexema()+" "+expr.getLexema()+"\n");
								   
								   //System.out.println("Tabla -> addToken() ");
								   //tabla.addToken(id);
								   //tabla.mostrarSimbolos();
								   //String expr = (Token)$2.obj.getLexema(); 
								   //System.out.println("\n Sintactico  ->  HAGO ASIGNACION  "+lexema+" = "+expr+"\n");
								   
								   //tabla.mostrarSimbolos();
								   //tabla.mostrarSimbolos();
								   System.out.println("\n ------------------------------------ \n"); 
								   }
								//checkeo semantico dentro de las reglas
								//asignacion.ptr = crear_terceto(=, ID.ptr, EXPRESION.ptr); } //$1, $$ etc.. y genero el terceto
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


//bloque_de_sentencias : '{' sentencia '}' {System.out.println("\n\nBLOQUE DE 1 SOLA SENTENCIA!!\n\n");}
bloque_de_sentencias : '{' lista_de_sentencias '}' {System.out.println("\n\nBLOQUE DE MULTIPLES SENTENCIAS!!\n\n");}
				     ;
					

sentencia_de_control : LOOP bloque_de_sentencias UNTIL '(' condicion ')' {System.out.println("\n\nSENTENCIA DE CONTROL!!\n\n");}
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


expresion : expresion '+' termino {System.out.println("EXPRESION SUMA  + "); 
								   Token op1 = (Token)$1.obj;
								   Token op2 = (Token)$2.obj;
								   System.out.println("SUMO ->  "+op1.getLexema()+" + "+op2.getLexema()); 
								   //expresion.ptr = crear_terceto(+, expresion.ptr, termino.ptr);
								   }
	  	  
	  	  | expresion '-' termino {System.out.println("EXPRESION RESTA  -  "); }
			      		//		   expresion.ptr = crear_terceto(+, expresion.ptr, termino.ptr); }
	  	  
	  	  | termino  //{System.out.println("soy terrible TERMINO -> voy a regla EXPRESION "); }
	  	  			 // expresion.ptr = termino.ptr	}
	  	  ;


termino : termino '*' factor //{System.out.println("TERMINO..");}
		
		| termino '/' factor
		
		| factor //{System.out.println("soy factor -> voy a regla TERMINO");}
				 // termino.ptr = factor.ptr}
		;
		
		
		
factor : CTE //{System.out.println("llega CTE! -> voy a regla factor \n"); }
			 // factor.ptr = CTE.ptr; }
       
       | '-' factor 
       
       | ID  //{System.out.println("llega ID! -> voy a regla factor "); 
       		 // tabla.mostrarSimbolos();} no muestra nada!!
       		 //factor.ptr = ID.ptr; }
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
		
	    yylval = new ParserVal(token); //var para obtener el token de la tabla
	    return token.getIdTipo(); //acceso a la entrada que devolvumos
	}
	//lexico devuelve i de token! y lexico en yylval lo asocie con la tabla de simbolos
	return 0;
}





public static void main(String args[]) {
 	String direccion_codigo = "casos_de_prueba_tps.txt";
	
 	AnalizadorLexico al = new AnalizadorLexico(direccion_codigo);
	al.abrirCargarArchivo();
	TablaDeSimbolos tds = new TablaDeSimbolos();

	
	Parser par = new Parser(false, al, tds);
 	par.yyparse();
 	
 	tds.mostrarSimbolos();
}