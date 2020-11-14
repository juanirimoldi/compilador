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

		
declaracion_de_variable : tipo lista_de_variables  {Token tipo = (Token)$1.obj;
												   Token variable = (Token)$2.obj;
												   
												   System.out.println("\n REGLA DECLARATIVA DETECTADA !! -> por defecto agrego  "+ variable.getLexema() +"  a TSym   \n");
												   
												   System.out.println("\n VARIABLE BIEN DEFINIDA   ->   "+tipo.getLexema()+" "+variable.getLexema()+"\n");
												   variable.setAmbito(ambito); 
												   
												   System.out.println("\n Le agrego el ambito -> "+ambito+"\n");
												   
												   //tabla.mostrarSimbolos();
												   
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
		   			 | sentencia_de_control
		   			 | sentencia_de_iteracion 
		   			 | sentencia_de_salida 
		   			 | invocacion {System.out.println("\n   \n");}
					 ;




asignacion : ID '=' expresion  {  Token id = (Token)$1.obj;
								  int linea = id.getNroLinea();
								  //System.out.println("\n Llega ID "+id.getLexema()+" a la asignacion \n");
								  System.out.println("\n REGLA ASIGNACION!!  Esta correctamente inicializada  "+ id.getLexema() +"  en Tabla de Simbolos ?");
								  
								  //tabla.mostrarSimbolos();
								  
								  Token op = (Token)$2.obj;
								
								  $$ = $3; //a pesar de la asignacion, $$ sigue apuntando al primer valor
								  
								  
								  
								  //System.out.println("\n EXPRESION $$ -> "+ ((Token)$$.obj).getLexema() +"\n");
								  //System.out.println("\n EXPRESION $1 -> "+ ((Token)$1.obj).getLexema() +"\n");
								  //System.out.println("\n EXPRESION $3 -> "+ ((Token)$3.obj).getLexema() +"\n");
							
								  if (isToken) { 
								  	System.out.println("\n EXPRESION $$ TOKEN!!  -> "+ ((Token)$$.obj).getLexema() +"\n");
							
								  	if (tabla.correctamenteDefinido(id)){
								  		System.out.println("\n SI, esta bien definido"+ id.getLexema()+"\n");
								  		System.out.println("\n CREO TERCETO ASIGNACION  ->  ( "+op.getLexema()+" , "+id.getLexema()+" , "+((Token)$$.obj).getLexema()+" )  \n\n");
								  		Terceto ter = new Terceto(this.nro_terceto, op.getLexema(), id.getLexema(), ((Token)$3.obj).getLexema());
 									   	this.lista.agregarTerceto(ter); 
 									   	this.lista.mostrarTercetos();
 									   	this.nro_terceto++;
								  		
								  	} else {
								  	
								  		System.out.println("\n EL ID  "+id.getLexema()+" no esta correctamente definido. cancelo la asignacion  \n");
								  		System.out.println("\n a  "+id.getLexema()+"  no le agrego ambito -> no es valido \n");
								  		//luego eliminar las entradas ID sin ambito
								  	}
								  	
								  } else {
								  
							      	System.out.println("\n ASIGNACION de EXPRESION $$ TERCETO!!  -> "+ ((Terceto)$$.obj).getOperando1() + " , " +((Terceto)$$.obj).getOperando2() +"\n");
								  	
								  	String pos_str = "["+pos_ultimo_terceto+"]";
								  	
								  	System.out.println("\n CREO TERCETO ASIGNACION! con terceto ->  ( "+op.getLexema()+" , "+ id.getLexema()+" , "+pos_str+" ) \n");
								  	
								  	Terceto ter = new Terceto(this.nro_terceto, "=", id.getLexema(), pos_str);
 									this.lista.agregarTerceto(ter); 
 								  	this.lista.mostrarTercetos();
 								   	this.nro_terceto++;
								  	
								  }
								  
								  isToken=true;
						
							   }
						
		   ;
		   
			


sentencia_de_control : IF condicion_IF cuerpo_IF END_IF {System.out.println("SENTENCIA DE CONTROL!");
														 System.out.println("aca COMPLETO TERCETOS BI  ");	
														 
														 
														 //aca completo terceto incondicional BI!
														 
														 //Terceto tt = (Terceto)$$.obj; //referencio BI
														 //tt.setOperando1(this.nro_terceto);
														 
														 this.lista.completarTerceto(pos_BI, this.nro_terceto);
														 
														 }
				     ;						


condicion_IF : '(' condicion ')' {
								  String pos_str = "["+pos_ultimo_terceto+"]";
 								  
 								  System.out.println("\n ACA CREO EL TERCETO SALTO por FALSO ->  ( BF , "+pos_str +" ,   )  \n");				
 								  	  	 
 								  	    
 	 							  Terceto ter = new Terceto(this.nro_terceto, "BF", pos_str, " " );
 								  this.lista.agregarTerceto(ter);
 								   
 								  this.pos_BF = this.nro_terceto; //marco posicion del terceto BF !
 								  
 								  //lo referencio??
 								  //$$=ter;
 					
 								  pos_ultimo_terceto = this.nro_terceto;
 					
 								  this.lista.mostrarTercetos();
 					     		  this.nro_terceto++;
 									   	
								  }
			 ;


cuerpo_IF : bloque_de_sentencias {System.out.println("\n 2. THEN -> CUERPO DEL IF! \n");
								  System.out.println("\n COMPLETAR TERCETO INCOMPLETO  \n");
								  System.out.println("\n CREAR TERCETO BI (salto incondicional) \n");
								  //desapilar terceto?
								  //completar el terceto??
								  //crear terceto incompleto  ->  (BI,  ,  -- ) 	
									}
		  | bloque_de_sentencias entra_ELSE bloque_de_sentencias {System.out.println("...ELSE ");
		  														  System.out.println("\n 3. COMPLETAR TERCETOS BF y BI ??!! \n ");
		  														  }
		  ;


entra_ELSE : ELSE {System.out.println("\n ENTRO AL ELSE!!  ->  ACAA!! \n");
				   
				   String pos_str = "["+pos_ultimo_terceto+"]";
 					
 					//VER!!				  	 
 					System.out.println("\n CREA TERCETO SALTO INCONDICIONAL  ->  (  BI  ,   ,  -- ) ");
 	 								    
 	 				Terceto ter = new Terceto(this.nro_terceto, "BI", " " , "--");
 					this.lista.agregarTerceto(ter); 
 					
 					this.pos_BI = this.nro_terceto;
 					
 					//aca completo BF!! ya tengo la posicion, que es pos_BF
 					
 					this.lista.mostrarTercetos();
 									   	
 					pos_ultimo_terceto = this.nro_terceto;
 									   	
 					
 					
 					//completo el BF y referencio??
 					//Terceto tt = (Terceto)$$.obj;
 					//tt.setOperando2(this.nro_terceto);
					
					
					//referencio BI
					//$$=ter;
					
					
					//para modificarlo debo referenciarlo!! con $$ 	algo asi...			
 					//Terceto tt = $$.obj;
 					//tt.setOperando2();
 					this.lista.completarTerceto(pos_BF, this.nro_terceto+1);
				   
				   	this.nro_terceto++; //sgte al terceto BI
 					
				   }
		   ;		  


				
		
				
condicion : expresion '>' expresion {if (isToken) { 
 									  	System.out.println("\n COMPARACION de EXPRESION $$ TOKEN!!  -> "+ ((Token)yyval.obj).getLexema() +"\n");
 									  	Token op1 = (Token)$1.obj;
 										 /*int linea = op1.getNroLinea();*/
 	 								     Token op2 = (Token)$2.obj;
 	 								     Token op3 = (Token)$3.obj;
 									  	 System.out.println("CREA TERCETO COMPARACION con TOKENS  ->  (  >  , " +$$.obj + " , " + op3.getLexema());
 	 								     
 	 								     Terceto ter = new Terceto(this.nro_terceto, ">", op1.getLexema(), op3.getLexema());
 									   	 this.lista.agregarTerceto(ter); 
 									   	 pos_ultimo_terceto = this.nro_terceto;
 									   	 
 									   	 this.lista.mostrarTercetos();
 									   	 this.nro_terceto++;
 									   	 
 									  } else {
 									  
 								      	System.out.println("\n COMPARACION de EXPRESION $$ con TERCETO!!  -> "+ ((Terceto)yyval.obj).getOperando1() +" , "+((Terceto)yyval.obj).getOperando2()+"\n");
 									  	
 									  	//caso en que $3 es un terceto
 									  	String tipo_obj = $3.obj.toString();
 								      	
 								      	if (tipo_obj.substring(18,23).equals("Token")) { //si el tipo del $3 objeto es Token
 								      		Token op3 = (Token)$3.obj;
 								      		String pos_str = "["+pos_ultimo_terceto+"]";
 	 									  	 
 	 								      	System.out.println("\n CREA TERCETO COMPARACION con TERCETO y Token ->  (  >  ,  "+pos_str + "  ,  " + op3.getLexema());//op3.getOperando2());
 	 	 								    
 	 	 								    Terceto ter = new Terceto(this.nro_terceto, ">", pos_str, op3.getLexema());//op3.getOperando2());
 	 									   	this.lista.agregarTerceto(ter); 
 	 									   	this.lista.mostrarTercetos();
 	 									   	
 	 									   	pos_ultimo_terceto = this.nro_terceto;
 	 									   	
 	 									   	this.nro_terceto++;
 	 									   	
 								      	} else { //si el argumento $3 es un terceto
 								      		
 								      		Terceto op3 = (Terceto)$3.obj;

	 								      	pos_ultimo_terceto = this.nro_terceto;
	 								      	
	 								      	
	 								      	String pos_terc1 = "["+pos_ultimo_terceto+"]";
	 								      	
	 								      	
	 									   	
	 									   	//this.nro_terceto++;
	 									   	
	 									   	
	 								      	String pos_terc2 = "["+pos_ultimo_terceto+"]";
	 									  	 
	 								      	System.out.println("\n CREA TERCETOS COMPARACION con TERCETO y TERCETO ->  (  >  ,  "+pos_terc1 + "  ,  " + pos_terc2);//op3.getOperando2());
	 	 								    
	 	 								    Terceto ter = new Terceto(this.nro_terceto, ">", pos_terc1, pos_terc2);//op3.getOperando2());
	 									   	this.lista.agregarTerceto(ter); 
	 									   	this.lista.mostrarTercetos();
	 									   	
	 									   	pos_ultimo_terceto = this.nro_terceto;
	 									   	
	 									   	this.nro_terceto++;
 								      	}  	 
 									  }
 									  
 									  isToken=true;
									}
									
									
	  	  | expresion '<' expresion {
	  	  							if (isToken) { 
 									  	System.out.println("\n COMPARACION de EXPRESION $$ TOKEN!!  -> "+ ((Token)yyval.obj).getLexema() +"\n");
 									  	Token op1 = (Token)$1.obj;
 										 /*int linea = op1.getNroLinea();*/
 	 								     Token op2 = (Token)$2.obj;
 	 								     Token op3 = (Token)$3.obj;
 									  	 System.out.println("CREA TERCETO COMPARACION con TOKENS  ->  (  <  , "+$$.obj + " , " + op3.getLexema());
 	 								     
 	 								     Terceto ter = new Terceto(this.nro_terceto, "<", op1.getLexema(), op3.getLexema());
 									   	 this.lista.agregarTerceto(ter); 
 									   	 pos_ultimo_terceto = this.nro_terceto;
 									   	 
 									   	 this.lista.mostrarTercetos();
 									   	 this.nro_terceto++;
 									   	 
 									  } else {
 									  
 								      	System.out.println("\n COMPARACION de EXPRESION $$ con TERCETO!!  -> "+ ((Terceto)yyval.obj).getOperando1() +" , "+((Terceto)yyval.obj).getOperando2()+"\n");
 									  	
 									  	//caso en que $3 es un terceto
 									  	String tipo_obj = $3.obj.toString();
 								      	
 								      	if (tipo_obj.substring(18,23).equals("Token")) { //si el tipo del $3 objeto es Token
 								      		Token op3 = (Token)$3.obj;
 								      		String pos_str = "["+pos_ultimo_terceto+"]";
 	 									  	 
 	 								      	System.out.println("\n CREA TERCETO COMPARACION con TERCETO y Token ->  (  <  ,  "+pos_str + "  ,  " + op3.getLexema());//op3.getOperando2());
 	 	 								    
 	 	 								    Terceto ter = new Terceto(this.nro_terceto, "<", pos_str, op3.getLexema());//op3.getOperando2());
 	 									   	this.lista.agregarTerceto(ter); 
 	 									   	this.lista.mostrarTercetos();
 	 									   	
 	 									   	pos_ultimo_terceto = this.nro_terceto;
 	 									   	
 	 									   	this.nro_terceto++;
 	 									   	
 								      	} else { //si el argumento $3 es un terceto
 								      		
 								      		Terceto op3 = (Terceto)$3.obj;

	 								      	pos_ultimo_terceto = this.nro_terceto;
	 									   	
	 								      	String pos_terc1 = "["+pos_ultimo_terceto+"]";
	 								      	
	 								      	
	 									   	
	 									   	
	 								      	String pos_terc2 = "["+pos_ultimo_terceto+"]";
	 									  	 
	 								      	System.out.println("\n CREA TERCETOS COMPARACION con TERCETO y TERCETO ->  (  <  ,  "+pos_terc1 + "  ,  " + pos_terc2);//op3.getOperando2());
	 	 								    
	 	 								    Terceto ter = new Terceto(this.nro_terceto, "<", pos_terc1, pos_terc2);//op3.getOperando2());
	 									   	this.lista.agregarTerceto(ter); 
	 									   	this.lista.mostrarTercetos();
	 									   	
	 									   	pos_ultimo_terceto = this.nro_terceto;
	 									   	
	 									   	this.nro_terceto++;
 								      	}  	 
 									  }
 									  
 									  isToken=true;
									}
									
	  	  | expresion MAYORIGUAL expresion
	  	  | expresion MENORIGUAL expresion
	  	  | expresion IGUAL expresion
	  	  | expresion DISTINTO expresion
	  	  ;




bloque_de_sentencias : '{' lista_de_sentencias '}' {//System.out.println("\n NO-BODY !\n");
												    }
				     ;

					

sentencia_de_iteracion : LOOP bloque_de_sentencias UNTIL '(' condicion ')'  {System.out.println("\n SENTENCIA DE CONTROL DETECTADA \n");}
		               ;
		             

					
sentencia_de_salida : OUT '(' CADENA ')'
				    ;
					
					
invocacion : ID '(' parametro_ejecutable ')'
	   	   ;
		  

		  
parametro_ejecutable : ID ':' ID  //para las invocaciones a parametros!!
		   		     | parametro_ejecutable ',' ID':'ID
		     		 ;
		 			
					
					

expresion : expresion '+' termino {Token op1 = (Token)$1.obj;
								   int linea = op1.getNroLinea();
								   Token op2 = (Token)$2.obj;
								   Token op3 = (Token)$3.obj;
								   
								   
								   //$$=$1;
	   		  					   
	   		  					   System.out.println("\n CREA TERCETO SUMA  ->  ("+op2.getLexema()+" , "+op1.getLexema()+" , "+op3.getLexema()+") \n\n");
								   
								   Terceto ter = new Terceto(this.nro_terceto, op2.getLexema(), op1.getLexema(), op3.getLexema());
								   this.lista.agregarTerceto(ter); 
								   pos_ultimo_terceto = this.nro_terceto;
								   this.lista.mostrarTercetos();
								   this.nro_terceto++;
								   //apunto a terceto!?
								   $$.obj = ter ;
								   this.isToken=false;
								   }
	  	  
	  	  
	  	  | expresion '-' termino {Token op1 = (Token)$1.obj;
								   int linea = op1.getNroLinea();
								   Token op2 = (Token)$2.obj;
								   Token op3 = (Token)$3.obj;
								   
								   
								   //$$=$1;
	   		  					   
	   		  					   System.out.println("\n CREA TERCETO RESTA  ->  ("+op2.getLexema()+" , "+op1.getLexema()+" , "+op3.getLexema()+") \n\n");
								   
								   Terceto ter = new Terceto(this.nro_terceto, op2.getLexema(), op1.getLexema(), op3.getLexema());
								   this.lista.agregarTerceto(ter); 
								   pos_ultimo_terceto = this.nro_terceto;
								   this.lista.mostrarTercetos();
								   this.nro_terceto++;
								   //apunto a terceto!?
								   $$.obj = ter ;
								   this.isToken=false; //no apunto al token, apunto a terceto
								   }
			      		
	  	  
	  	  | termino  {//System.out.println("\n\n soy terrible TERMINO \n\n"); }
	  	  			 //Token expr = (Token)$$.obj;
	  	  			 $$=$1;
	  	  			 
	  	  			 //System.out.println("\n\n EXPRESION SIMPLE -> TERMINO -> "+ ((Token)$$.obj).getLexema() +" \n\n");
	  	  			 }
	  	  			 // expresion.ptr = termino.ptr	}
	  	  			 
	  	  | CADENA //{System.out.println("EXPRESION -> CADENA! ");
	  	  ;



termino : termino '*' factor {
							  Token op1 = (Token)$1.obj;
							  int linea = op1.getNroLinea();
							  Token op2 = (Token)$2.obj;
							  Token op3 = (Token)$3.obj;
							  
							  System.out.println("\n TERCETO MULTIPLICACION  ->  ( "+op2.getLexema()+" , "+op1.getLexema()+" , "+op3.getLexema()+" ) \n");
							  
							  //System.out.println("\n CREA TERCETO MULTIPLICACION  ->  ("+op2.getLexema()+" , "+op1.getLexema()+" , "+op3.getLexema()+") \n\n");
							  
							  Terceto ter = new Terceto(this.nro_terceto, "*" , op1.getLexema(), op3.getLexema());
							  this.lista.agregarTerceto(ter); 
							  pos_ultimo_terceto = this.nro_terceto;
							  this.lista.mostrarTercetos();
							  this.nro_terceto++;
								 //apunto a terceto!?
							  $$.obj = ter ;
							  this.isToken=false; 
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

int nro_terceto = 0;
int pos_ultimo_terceto = 0;

int pos_BF = 0;
int pos_BI = 0;



void yyerror(String s)
{
 System.out.println("par:"+s);
}



private int yylex() {
	Token token=lexico.getToken();
	
	if (token!=null){
		//System.out.println("\n Llega token "+token.getLexema()+"\n");
	
		if (tabla.existe(token.getLexema())){
			System.out.println("\n YA EXISTE TOKEN  "+ token.getLexema() +"  EN TSYM! apunto al simbolo en la tabla \n");
			token = tabla.getSimbolo(token.getLexema());
		}
		tabla.addToken(token);
		
	    yylval = new ParserVal(token); //var para obtener el token de la tabla
	    return token.getIdTipo(); //acceso a la entrada que devolvumos
	}
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