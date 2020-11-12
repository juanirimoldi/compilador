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
												   System.out.println("\n REGLA DECLARATIVA!! -> POR DEFECTO AGREGO  "+ variable.getLexema() +"  A TSYM   \n");
												   
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
					 //| clausula_de_seleccion //{System.out.println("\n Sintactico  ->  CLAUSULA de SELECCION EJECUTADA CORRECTAMENTE \n");}
		   			 //| sentencia
		   			 | sentencia_de_control 
		   			 | sentencia_de_salida 
		   			 | invocacion {System.out.println("\n INVOCO TERRIBLE PROCEDURE \n");}
					 ;




asignacion : ID '=' expresion  {  Token id = (Token)$1.obj;
								  int linea = id.getNroLinea();
								  //System.out.println("\n Llega ID "+id.getLexema()+" a la asignacion \n");
								  System.out.println("\n REGLA ASIGNACION!  Esta correctamente inicializada  "+ id.getLexema() +"  en Tabla de Simbolos ?");
								  
								  tabla.mostrarSimbolos();
								  
								  Token op = (Token)$2.obj;
								  //Token expr = (Token)$3.obj;
								  //Token expr = (Token)$$.obj;
								  $$ = $3; //a pesar de la asignacion, $$ sigue apuntando al primer valor
								  
								  
								  
								  //System.out.println("\n EXPRESION $$ -> "+ ((Token)$$.obj).getLexema() +"\n");
								  //System.out.println("\n EXPRESION $1 -> "+ ((Token)$1.obj).getLexema() +"\n");
								  //System.out.println("\n EXPRESION $3 -> "+ ((Token)$3.obj).getLexema() +"\n");
							
								  if (isToken) { 
								  	System.out.println("\n EXPRESION $$ TOKEN!!  -> "+ ((Token)$$.obj).getLexema() +"\n");
							
								  	if (tabla.correctamenteDefinido(id)){
								  		System.out.println("\n SI, esta bien definido"+ id.getLexema()+"\n");
								  		//tabla.modificarValor(id.getLexema(), expr.getLexema());
								  		System.out.println("\n CREO TERCETO ASIGNACION  ->  ( "+op.getLexema()+" , "+id.getLexema()+" , "+((Token)$$.obj).getLexema()+" )  \n\n");
								  	} else {
								  		System.out.println("\n EL ID  "+id.getLexema()+" no esta correctamente definido. cancelo la asignacion  \n");
								  		//como no se puede alterar la TSym desde las reglas, la agrego sin ambito y luego la elimino?
								  	}
								  } else {
							      	System.out.println("\n ASIGNACION de EXPRESION $$ TERCETO!!  -> "+ ((Terceto)$$.obj).getOperando1() +"\n");
								  	
								  	//EL ERROR ES QUE CONSIDERO COMO TERCETO A UN TOKEN!!!
								  	
								  	System.out.println("\n CREO TERCETO ASIGNACION! con terceto ->  ( "+op.getLexema()+" , "+yyval.obj+" ) \n");
								  }
								  
								  isToken=true;
								  // System.out.println("\n ------------------------------------ \n"); 
								   }
								//checkeo semantico dentro de las reglas
								//asignacion.ptr = crear_terceto(=, ID.ptr, EXPRESION.ptr); } //$1, $$ etc.. y genero el terceto
		   ;
		   
			


sentencia_de_control : IF condicion_IF cuerpo_IF END_IF {System.out.println("SENTENCIA DE CONTROL!");}
				     ;						


condicion_IF : '(' condicion ')' {System.out.println("\n ENTRO EN CONDICION IF!! \n");}
			 ;


cuerpo_IF : bloque_de_sentencias entra_ELSE bloque_de_sentencias {System.out.println("\n CUERPAZO DEL IF! \n");}
		  | bloque_de_sentencias {System.out.println("...THEN -> BLOQUE DE SENTENCIAS SIMPLE");}
		  ;


entra_ELSE : ELSE {System.out.println("\n ENTRO AL ELSE!! dentro del IF \n");}
		   ;		  



//TIPS DE JOSE

//IF CONDICION BLOQUE_SGTE  	
//BLOQUE_SGTE tiene bloque(puede contener un else)			
				
//sentenciaControl  : IF condIf cpoIf	
//condIf: '(' condicion ')' 	
//cpoIf:  bloqueEjecutable entroElse bloqueEjecutable END_IF 
//bloqueEjecutable -> definir salto si entra en then	
//..
//entroElse: -> aca decido hacia donde salta
						
//entroElse : ELSE 
					
						
								
//condicion_if -> asi sabes cuando termina la condicion IF		
				
		
				
condicion : expresion '>' expresion {//System.out.println("\n Sintactico  ->  COMPARACION!!\n");
									 //popner aca condicion de salto
									 //ya sea IF o LOOP, la cond va a seguir ejecutandose
									 //codigo que haaga la comparacion 
									 //si es falso, en IF salta a else
									 
									 //dejo lugar en blanco y guarno el nro de terceto
									 
									 Token op1 = (Token)$1.obj;
									 int linea = op1.getNroLinea();
 								     Token op2 = (Token)$2.obj;
 								     Token op3 = (Token)$3.obj;
 								     System.out.println("\n 1. TERCETO COMPARACION  ->  ( "+op2.getLexema()+" , "+op1.getLexema()+" , "+op3.getLexema()+" ) \n");
									}
									
	  	  | expresion '<' expresion {
	  	  							if (isToken) { 
 									  	System.out.println("\n EXPRESION $$ TOKEN!!  -> "+ ((Token)yyval.obj).getLexema() +"\n");
 									  	Token op1 = (Token)val_peek(2).obj;
 										 /*int linea = op1.getNroLinea();*/
 	 								     Token op2 = (Token)val_peek(1).obj;
 	 								     Token op3 = (Token)val_peek(0).obj;
 									  	 System.out.println("CREA TERCETO COMPARACION con TOKENS  ->  ( "+op2.getLexema()+" , "+yyval.obj + " , " + op3.getLexema());
 	 								     
 									  } else {
 								      	System.out.println("\n COMPARACION de EXPRESION $$ con TERCETO!!  -> "+ ((Terceto)yyval.obj).getOperando1() +" , "+((Terceto)yyval.obj).getOperando2()+"\n");
 									  	Token op3 = (Token)val_peek(0).obj;
 								      	System.out.println("\n CREA TERCETO COMPARACION con TERCETO  ->  (  <  ,  "+yyval.obj + "  ,  " + op3.getLexema());
 	 								    // Terceto ter = new Terceto(this.nro_terceto, op2.getLexema(), op1.getLexema(), op3.getLexema());
 									   	// this.lista.agregarTerceto(ter); 
 									   	// this.lista.mostrarTercetos();
 									   	// this.nro_terceto++;
 									   	 
 									  	//System.out.println("\n CREO TERCETO ASIGNACION! con terceto ->  ( "+op.getLexema()+" , "+yyval.obj+" ) \n");
 									  }
 									  
 									  isToken=true;
	  	  							/*
	  	  							Token op1 = (Token)$1.obj;
									 //int linea = op1.getNroLinea();
 								     Token op2 = (Token)$2.obj;
 								     Token op3 = (Token)$3.obj;
 								     System.out.println("CREA TERCETO COMPARACION ->  ( "+op2.getLexema()+" , "+$$.obj + " , " + op3.getLexema());
 								     Terceto ter = new Terceto(this.nro_terceto, op2.getLexema(), op1.getLexema(), op3.getLexema());
								   	 this.lista.agregarTerceto(ter); 
								   	 this.lista.mostrarTercetos();
								   	 this.nro_terceto++;
								   	 //apunto a terceto!
								     $$.obj = ter ;
								     this.isToken=false;
								     */
									}
	  	  | expresion MAYORIGUAL expresion
	  	  | expresion MENORIGUAL expresion
	  	  | expresion IGUAL expresion
	  	  | expresion DISTINTO expresion
	  	  ;



//bloque_de_sentencias : '{' sentencia '}' {System.out.println("\n\nBLOQUE DE 1 SOLA SENTENCIA!!\n\n");}
bloque_de_sentencias : '{' lista_de_sentencias '}' {//System.out.println("\n NO-BODY !\n");
													}
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
		  

		  
parametro_ejecutable : ID ':' ID  //para las invocaciones a parametros!!
		   		     | parametro_ejecutable ',' ID':'ID
		     		 ;
		 			
					
					
					
//terceto -> clase con lista de objetios terceto
// a = b -> checkeo semantico! tiene que existir a y tiene que existir b -> chequeos semanticos dentro de las llaves
// si llego a la raiz -> lista bien escrita -> se lo entrego  a generador de coigo para hacer asembler


expresion : expresion '+' termino {Token op1 = (Token)$1.obj;
								   int linea = op1.getNroLinea();
								   Token op2 = (Token)$2.obj;
								   Token op3 = (Token)$3.obj;
								   
								   //System.out.println("\n que onda $$ ?? "+(Token)$$.obj+" \n"); 
								   //ACA!!
								   //$$ = (Token)$1.obj + (Token)$3.obj ;
								   
								   $$=$1;
	   		  					   //System.out.println("\n EXPRESION SUMA!  "+ ((Token)$$.obj).getLexema() +"  la apunto con $$?? \n");
	   		  					   
	   		  					   //creo el terceto y apunto a la suma!!
								   
								   //System.out.println("\n que onda $$ procesado ?? "+((Token)$$.obj).getLexema()+" \n");
								   System.out.println("\n CREA TERCETO SUMA  ->  ("+op2.getLexema()+" , "+op1.getLexema()+" , "+op3.getLexema()+") \n\n");
								   Terceto ter = new Terceto(this.nro_terceto, op2.getLexema(), op1.getLexema(), op3.getLexema());
								   this.lista.agregarTerceto(ter); 
								   this.lista.mostrarTercetos();
								   this.nro_terceto++;
								   //apunto a terceto!
								   $$.obj = ter ;
								   this.isToken=false;
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
	  	  			 $$=$1;
	  	  			 
	  	  			 //System.out.println("\n\n EXPRESION SIMPLE -> TERMINO -> "+ ((Token)$$.obj).getLexema() +" \n\n");
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
		
		| factor {//System.out.println("1. agregar el ID o CTE que interviene en la asignacion u operacion (Ref a Tsym)");
				  //Token factor = (Token)$$.obj;
				  //System.out.println("\n\n FACTOR -> "+factor.getLexema() +" , "+factor.getTipo()+"\n\n");}
				  $$=$1;
	   		  	  //System.out.println("\n REGLA TERMINO -> Llega FACTOR!  "+ ((Token)$$.obj).getLexema() +"  la apunto con $$?? \n");
				 // termino.ptr = factor.ptr
				 }
		
		//| CADENA //{System.out.println("termino -> CADENA! ");
		;
		
		
		
factor : CTE {Token factor = (Token)$$.obj; 
	   		  //System.out.println("\n Llega CTE  "+ factor.getLexema() +"  la apunto con $$?? \n");
	   		  
	   		  //(Token)$$.obj = (Token)$1.obj ;
	   		  //VER!!! no puede convertir tipo Token a ParserVal
	   		  $$=$1;
	   		  //System.out.println("\n REGLA FACTOR -> Llega CTE!  "+ ((Token)$$.obj).getLexema() +"  la apunto con $$?? \n");
	   		  //System.out.println("\n que onda cte $$??? "+ $$.getLexema() +" \n");
       		  }
       
       | '-'CTE {//System.out.println("CTE negativa! \n"); 
       			 Token op1 = (Token)$1.obj;
				 int linea = op1.getNroLinea();
				 Token op2 = (Token)$2.obj;
				 //System.out.println("\n Sintactico  ->  Linea: "+linea+"  ,  CTE NEGATIVA!  ->  "+op1.getLexema()+" "+op2.getLexema()+"\n");
				 } 
       			//va a tsym y va haccia constante a negativa. recheckear los rangos!!!
 
       
	    | ID  {//Token factor = (Token)$$.obj; 
	   		  //System.out.println("llega FACTOR ID!??!  creo puntero -> "+ factor.getLexema() +"\n");
       		  }
       		  
       //| CADENA //{System.out.println("llega CADENA! -> voy a regla factor "); 
       ;
	   	


%%
	   	


//CODE



AnalizadorLexico lexico;
TablaDeSimbolos tabla;
ListaTercetos lista;
String ambito;
boolean isToken=true;
int nro_terceto = 1;



void yyerror(String s)
{
 System.out.println("par:"+s);
}



private int yylex() {
	Token token=lexico.getToken();
	//System.out.println("\n Dentro del Sintactico...\n");

	if (token!=null){
		System.out.println("\n Llega token "+token.getLexema()+"\n");
	
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
	
	ListaTercetos l = new ListaTercetos();
	
	
	Parser par = new Parser(false, al, tds, l, a);
 	par.yyparse();
 	
 	
 	tds.mostrarSimbolos();
 	
 	l.mostrarTercetos();
}