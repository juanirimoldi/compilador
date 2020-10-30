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
	MENOR
	MAYOR
	MENORIGUAL
	MAYORIGUAL
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


sentencia : sentencia_declarativa ';' //{System.out.println("\n SENTENCIA DECLARATIVA CORRECTA \n");
								   	  // System.out.println("\n----------------------------------------\n");
									  //}
		  | sentencia_ejecutable ';' //{System.out.println("\n SENTENCIA EJECUTABLE CORRECTA \n");
		    	   				     // System.out.println("\n----------------------------------------\n");
		  						     //}
	  	  ;





//SENTENCIAS DECLARATIVAS

//aca los ; andan
sentencia_declarativa : declaracion_de_variable {System.out.println("\n VARIABLE DECLARADA CORRECTAMENTE \n ");
								   				 System.out.println("\n----------------------------------------\n");
												 }
				      | declaracion_de_procedimiento {System.out.println("\n PROCEDIMIENTO DECLARADO CORRECTAMENTE \n ");
				      								  System.out.println("\n----------------------------------------\n");
				      								  }
				      ;

		
declaracion_de_variable : tipo lista_de_variables  {//System.out.println("VARIABLE BIEN DECLARADA con TIPO!! ");
												   //Token t = (Token)yyval.obj;	
												   //System.out.println("yyval SAPE! "+t.getLexema());
												   Token tipo = (Token)$1.obj;
												   Token variable = (Token)$2.obj;
												   System.out.println("\n Sintactico  ->  Linea: "+tipo.getNroLinea()+"  ,  VARIABLE BIEN DEFINIDA  "+tipo.getLexema()+" "+variable.getLexema()+"\n");
												   
												   //String tipo = (Token)$1.obj.getLexema();
												   //String lexema = (Token)$2.obj.getLexema(); 
								   				   //System.out.println("\n----------------------------------------\n");
								   
												   }
				        ;


//clase para generar tercetos!

//!!!VER!!!
declaracion_de_procedimiento : PROC ID '(' lista_de_parametros ')' NI '=' CTE '{' cuerpo_del_procedimiento '}'
						     | PROC ID '(' ')' NI '=' CTE '{' cuerpo_del_procedimiento '}'
			     			 ;
			
			
cuerpo_del_procedimiento : lista_de_sentencias
			 			 //| lista_de_sentencias declaracion_de_procedimiento !!VER!! esto no reduce nunca
			 			 ;			


//numero maximo de parametros = 3 . puede no haber parametros!!   !!VER!!

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

		
sentencia_ejecutable : asignacion //{System.out.println("Sintactico  ->  SENTENCIA EJECUTABLE -> ASIGNACION! ");}
					 | clausula_de_seleccion {System.out.println("\n Sintactico  ->  CLAUSULA de SELECCION EJECUTADA CORRECTAMENTE \n");}
		   			 | sentencia_de_control 
		   			 | sentencia_de_salida 
		   			 | invocacion {System.out.println("\n INVOCO TERRIBLE PROCEDURE \n");}
					 ;


//al hacer la asignacion debo checkear si la variable existe en la tabla de simbolos!!
//si existe -> hago la asignacion
//si no existe sacudo un error de que falta inicializar


asignacion : ID '=' expresion  {//System.out.println("Existe el lexema en la Tabla de Simbolos");
								   Token id = (Token)$1.obj;
								   int linea = id.getNroLinea();
								   //System.out.println("\n OJO!! Existe  "+ id.getLexema() +"  en Tabla de Simbolos ??");
								   //System.out.println("EXISTE?? a ver, mostrala ");
								   //tipo de binding?
								   //tabla.mostrarSimbolos();
								   
								   Token op = (Token)$2.obj;
								   Token expr = (Token)$3.obj;
								   //es valida esta impleentacion? o consumo  memoria al crear tokens?
								   
								   
								   System.out.println("\n Sintactico ->  Linea: "+ linea+"  ,  ASIGNACION DETECTADA   "+id.getLexema()+" "+op.getLexema()+" "+expr.getLexema()+"\n");
								   
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
					
						
condicion : expresion '>' expresion {//System.out.println("\n Sintactico  ->  COMPARACION!!\n");
									 Token op1 = (Token)$1.obj;
									 int linea = op1.getNroLinea();
 								     Token op2 = (Token)$2.obj;
 								     Token op3 = (Token)$3.obj;
 								     System.out.println("\n Sintactico  ->  Linea: "+linea+"  ,  COMPARACION  ->  "+op1.getLexema()+" "+op2.getLexema()+" "+op3.getLexema()+"\n");
									}
	  	  | expresion '<' expresion
	  	  | expresion MAYORIGUAL expresion
	  	  | expresion MENORIGUAL expresion
	  	  | expresion IGUAL expresion
	  	  | expresion DISTINTO expresion
	  	  ;


//bloque_de_sentencias : '{' sentencia '}' {System.out.println("\n\nBLOQUE DE 1 SOLA SENTENCIA!!\n\n");}
bloque_de_sentencias : '{' lista_de_sentencias '}' {System.out.println("\n\nBLOQUE DE MULTIPLES SENTENCIAS!!\n\n");}
				     ;
					

sentencia_de_control : LOOP bloque_de_sentencias UNTIL '(' condicion ')'  {System.out.println("\n SENTENCIA DE CONTROL DETECTADA \n");}
		             ;
		             

//checkear tambien IF y PROC!! hacer sentencias de errror!!!
//asignacion con lado izq o der faltante
// = 3 ; , x = ;					
sentencia_de_salida : OUT '(' CADENA ')'
				    ;
					
					
invocacion : ID '(' parametro_ejecutable ')'
	   	   ;
		  

//!!!VER!!! 		  
parametro_ejecutable : ID ':' ID  //para las invocaciones a parametros!!
		   		     | parametro_ejecutable ',' ID':'ID
		     		 ;
		 			
					
					
					
//terceto -> clase con lista de objetios terceto
// a = b -> checkeo semantico! tiene que existir a y tiene que existir b -> chequeos semanticos dentro de las llaves
// si llego a la raiz -> lista bien escrita -> se lo entrego  a generador de coigo para hacer asembler


expresion : expresion '+' termino {//System.out.println("\n EXPRESION SUMA  "); 
								   Token op1 = (Token)$1.obj;
								   int linea = op1.getNroLinea();
								   Token op2 = (Token)$2.obj;
								   Token op3 = (Token)$3.obj;
								   System.out.println("\n Sintactico  ->  Linea: "+linea+"  ,  EXPRESION SUMA  ->  "+op1.getLexema()+" "+op2.getLexema()+" "+op3.getLexema()+"\n"); 
								   //expresion.ptr = crear_terceto(+, expresion.ptr, termino.ptr);
								   }
	  	  
	  	  | expresion '-' termino {System.out.println("EXPRESION RESTA  -  "); }
			      		//		   expresion.ptr = crear_terceto(+, expresion.ptr, termino.ptr); }
	  	  
	  	  | termino  //{System.out.println("soy terrible TERMINO -> voy a regla EXPRESION "); }
	  	  			 // expresion.ptr = termino.ptr	}
	  	  			 
	  	  | CADENA //{System.out.println("EXPRESION -> CADENA! ");
	  	  ;


termino : termino '*' factor //{System.out.println("TERMINO..");}
		
		| termino '/' factor
		
		| factor //{System.out.println("soy factor -> voy a regla TERMINO");}
				 // termino.ptr = factor.ptr}
		
		//| CADENA //{System.out.println("termino -> CADENA! ");
		;
		
		
		
factor : CTE //{System.out.println("llega CTE! -> voy a regla factor \n"); }
			 // factor.ptr = CTE.ptr; }
       
       | '-'CTE {//System.out.println("CTE negativa! \n"); 
       			 Token op1 = (Token)$1.obj;
				 int linea = op1.getNroLinea();
				 Token op2 = (Token)$2.obj;
				 System.out.println("\n Sintactico  ->  Linea: "+linea+"  ,  CTE NEGATIVA!  ->  "+op1.getLexema()+" "+op2.getLexema()+"\n");
				 } 
       			//va a tsym y va haccia constante a negativa. recheckear los rangos!!!
       
       //| '-' factor 
       
       | ID  //{System.out.println("llega ID! -> voy a regla factor "); 
       		 // tabla.mostrarSimbolos();} no muestra nada!!
       		 //factor.ptr = ID.ptr; }
       		  
       //| CADENA //{System.out.println("llega CADENA! -> voy a regla factor "); 
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