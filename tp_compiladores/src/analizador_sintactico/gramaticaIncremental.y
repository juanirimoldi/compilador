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


sentencia : sentencia_declarativa ';' {//System.out.println("\n SENTENCIA DECLARATIVA CORRECTA \n");
								   	   System.out.println("\n----------------------------------------\n");
									  }
		  | sentencia_ejecutable ';' {//System.out.println("\n SENTENCIA EJECUTABLE CORRECTA \n");
		    	   				       System.out.println("\n----------------------------------------\n");
		  						     }
	  	  ;





//SENTENCIAS DECLARATIVAS


sentencia_declarativa : declaracion_de_variable {//System.out.println("\n VARIABLE DECLARADA CORRECTAMENTE \n ");
								   				 //System.out.println("\n----------------------------------------\n");
												 }
				      | declaracion_de_procedimiento {System.out.println("\n PROCEDIMIENTO DECLARADO CORRECTAMENTE \n ");
				      								  System.out.println("\n----------------------------------------\n");
				      								  }
				      ;

		
declaracion_de_variable : tipo lista_de_variables  {//Token t = (Token)yyval.obj;	
												   //System.out.println("yyval SAPE! "+t.getLexema());												   
												   Token tipo = (Token)$1.obj;
												   Token variable = (Token)$2.obj;
												   //System.out.println("Linea "+tipo.getNroLinea()+"  ,  "+tipo.getLexema()+" "+variable.getLexema());
												   System.out.println("\n POR DEFECTO AGREGO A TSYM  ->  "+ variable.getLexema() +"\n");
												   
												   System.out.println("\n VARIABLE BIEN DEFINIDA   ->   "+tipo.getLexema()+" "+variable.getLexema()+"\n");
												   variable.setAmbito(ambito); 
												   
												   System.out.println("\n Le agrego el ambito -> "+ambito+"\n");
												   //System.out.println("ambito -> "+ambito); 
												   tabla.mostrarSimbolos();
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
					 | clausula_de_seleccion //{System.out.println("\n Sintactico  ->  CLAUSULA de SELECCION EJECUTADA CORRECTAMENTE \n");}
		   			 | sentencia_de_control 
		   			 | sentencia_de_salida 
		   			 | invocacion {System.out.println("\n INVOCO TERRIBLE PROCEDURE \n");}
					 ;


//al hacer la asignacion debo checkear si la variable existe en la tabla de simbolos!!
//si existe -> hago la asignacion
//si no existe sacudo un error de que falta inicializar


asignacion : ID '=' expresion  {  Token id = (Token)$1.obj;
								  int linea = id.getNroLinea();
								  System.out.println("Llega ID "+id.getLexema()+" a la asignacion");
								  System.out.println("\n ASIGNACION!  correctamente inicializada  "+ id.getLexema() +"  en Tabla de Simbolos ??");
								  
								  tabla.mostrarSimbolos();
								  
								  Token op = (Token)$2.obj;
								  Token expr = (Token)$3.obj;
								  
								   
								  if (tabla.correctamenteDefinido(id)){
								  	tabla.modificarValor(id.getLexema(), expr.getLexema());
								  	System.out.println("\n CREO TERCETO ASIGNACION  ->  ( "+op.getLexema()+" , "+id.getLexema()+" , "+expr.getLexema()+" )  \n\n");
								  } else {
								  	System.out.println("\n EL ID  "+id.getLexema()+" no esta correctamente definido. cancelo la asignacion  \n");
								  	//como no se puede alterar la TSym desde las reglas, la agrego sin ambito y luego la elimino?
								  }
								  
								  // System.out.println("\n ------------------------------------ \n"); 
								   }
								//checkeo semantico dentro de las reglas
								//asignacion.ptr = crear_terceto(=, ID.ptr, EXPRESION.ptr); } //$1, $$ etc.. y genero el terceto
		   ;
		   
//expr.ptr nro de terceto que tiene la expresion



clausula_de_seleccion : IF '(' condicion ')'  bloque_de_sentencias ELSE bloque_de_sentencias END_IF	{ System.out.println("\n\n ESTOY DENTRO DEL IF!! \n\n");
																									  Token f = (Token)$$.obj;
																									  Token f2 = (Token)$2.obj;
																									  System.out.println("\n\n tengo algun parametro??"+ f.getLexema() +" algo mas? "+f2.getLexema()+"\n\n");
																									  
																									}				
		      		  | IF '(' condicion ')' bloque_de_sentencias END_IF
		     		  ;
					
						//IF <condicion_if>				
						
						
condicion : expresion '>' expresion {//System.out.println("\n Sintactico  ->  COMPARACION!!\n");
									 //popner aca condicion de salto
									 //ya sea IF o LOOP, la cond va a seguir ejecutandose
									 //codigo que haaga la comparacion 
									 //si es falso, en IF salta a else
									 
									 Token op1 = (Token)$1.obj;
									 int linea = op1.getNroLinea();
 								     Token op2 = (Token)$2.obj;
 								     Token op3 = (Token)$3.obj;
 								     System.out.println("\n 1. TERCETO COMPARACION  ->  ( "+op2.getLexema()+" , "+op1.getLexema()+" , "+op3.getLexema()+" ) \n");
									}
	  	  | expresion '<' expresion {Token op1 = (Token)$1.obj;
									 int linea = op1.getNroLinea();
 								     Token op2 = (Token)$2.obj;
 								     Token op3 = (Token)$3.obj;
 								     System.out.println("\n 1. TERCETO COMPARACION  ->  ( "+op2.getLexema()+" , "+op1.getLexema()+" , "+op3.getLexema()+" ) \n");
									}
	  	  | expresion MAYORIGUAL expresion
	  	  | expresion MENORIGUAL expresion
	  	  | expresion IGUAL expresion
	  	  | expresion DISTINTO expresion
	  	  ;


//bloque_de_sentencias : '{' sentencia '}' {System.out.println("\n\nBLOQUE DE 1 SOLA SENTENCIA!!\n\n");}
bloque_de_sentencias : '{' lista_de_sentencias '}' //{System.out.println("\n\nBLOQUE DE MULTIPLES SENTENCIAS!!\n\n");}
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


expresion : expresion '+' termino {//System.out.println("2. agregar la operacion que se realizo  "); 
								   Token op1 = (Token)$1.obj;
								   int linea = op1.getNroLinea();
								   Token op2 = (Token)$2.obj;
								   Token op3 = (Token)$3.obj;
								   //System.out.println("\n Sintactico  ->  Linea: "+linea+"  ,  EXPRESION SUMA  ->  "+op1.getLexema()+" "+op2.getLexema()+" "+op3.getLexema()+"\n"); 
								   System.out.println("\n CREAR TERCETO SUMA  ->  ("+op2.getLexema()+" , "+op1.getLexema()+" , "+op3.getLexema()+") \n\n"); 
								   //expresion.ptr = crear_terceto(+, expresion.ptr, termino.ptr);
								   }
	  	  
	  	  | expresion '-' termino {//System.out.println("2. agregar la expresion que se realizo  "); 
	  	  						   Token op1 = (Token)$1.obj;
								   int linea = op1.getNroLinea();
								   Token op2 = (Token)$2.obj;
								   Token op3 = (Token)$3.obj;
								   //System.out.println("\n Sintactico  ->  Linea: "+linea+"  ,  EXPRESION SUMA  ->  "+op1.getLexema()+" "+op2.getLexema()+" "+op3.getLexema()+"\n"); 
								   System.out.println("\n CREAR TERCETO RESTA  ->  ("+op2.getLexema()+" , "+op1.getLexema()+" , "+op3.getLexema()+") \n\n"); 
								   }
			      		//		   expresion.ptr = crear_terceto(+, expresion.ptr, termino.ptr); }
	  	  
	  	  | termino  {//System.out.println("\n\n soy terrible TERMINO \n\n"); }
	  	  			 //Token expr = (Token)$$.obj;
	  	  			 //System.out.println("\n\n TERMINO -> "+ expr.getLexema() +" \n\n");
	  	  			 }
	  	  			 // expresion.ptr = termino.ptr	}
	  	  			 
	  	  | CADENA //{System.out.println("EXPRESION -> CADENA! ");
	  	  ;



termino : termino '*' factor {//System.out.println("2. agregar operacion que se realizo -> *");
								//$$ = $1 + $3
							  Token op1 = (Token)$1.obj;
							  int linea = op1.getNroLinea();
							  Token op2 = (Token)$2.obj;
							  Token op3 = (Token)$3.obj;
							  System.out.println("\n TERCETO MULTIPLICACION  ->  ( "+op2.getLexema()+" , "+op1.getLexema()+" , "+op3.getLexema()+" ) \n"); 
							  }
		
		| termino '/' factor {System.out.println("2. agregar operacion que se realizo -> /");}
		
		| factor //{//System.out.println("1. agregar el ID o CTE que interviene en la asignacion u operacion (Ref a Tsym)");
				  //Token factor = (Token)$$.obj;
				  //System.out.println("\n\n FACTOR -> "+factor.getLexema() +" , "+factor.getTipo()+"\n\n");}
				 
				 // termino.ptr = factor.ptr}
		
		//| CADENA //{System.out.println("termino -> CADENA! ");
		;
		
		
		
factor : CTE {Token factor = (Token)$$.obj; 
	   		  System.out.println("\n Llega CTE  "+ factor.getLexema() +"  la apunto con $$?? \n");
       		  }
       
       | '-'CTE {//System.out.println("CTE negativa! \n"); 
       			 Token op1 = (Token)$1.obj;
				 int linea = op1.getNroLinea();
				 Token op2 = (Token)$2.obj;
				 System.out.println("\n Sintactico  ->  Linea: "+linea+"  ,  CTE NEGATIVA!  ->  "+op1.getLexema()+" "+op2.getLexema()+"\n");
				 } 
       			//va a tsym y va haccia constante a negativa. recheckear los rangos!!!
 
       
	    | ID  {Token factor = (Token)$$.obj; 
	   		  System.out.println("llega FACTOR ID!??!  creo puntero -> "+ factor.getLexema() +"\n");
       		  }
       		  
       //| CADENA //{System.out.println("llega CADENA! -> voy a regla factor "); 
       ;
	   	


%%
	   	


//CODE



AnalizadorLexico lexico;
TablaDeSimbolos tabla;
String ambito;



void yyerror(String s)
{
 System.out.println("par:"+s);
}



private int yylex() {
	Token token=lexico.getToken();
	//System.out.println("\n Dentro del Sintactico...\n");

	if (token!=null){
		if (tabla.existe(token.getLexema())){
			System.out.println("\n EXISTE TOKEN  "+ token.getLexema() +"  EN TSYM! apunto al simbolo en la tabla \n");
			token = tabla.getSimbolo(token.getLexema());
		}
		tabla.addToken(token);
		
	    yylval = new ParserVal(token); //var para obtener el token de la tabla
	    return token.getIdTipo(); //acceso a la entrada que devolvumos
	}
	//lexico devuelve i de token! y lexico en yylval lo asocie con la tabla de simbolos
	return 0;
}





public static void main(String args[]) {
 	String direccion_codigo = "casos_prueba_tercetos.txt";
	
 	AnalizadorLexico al = new AnalizadorLexico(direccion_codigo);
	al.abrirCargarArchivo();
	TablaDeSimbolos tds = new TablaDeSimbolos();
	String a = "main";
	
	Parser par = new Parser(false, al, tds, a);
 	par.yyparse();
 	
 	tds.mostrarSimbolos();
}