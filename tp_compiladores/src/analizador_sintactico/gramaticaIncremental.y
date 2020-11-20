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
												   //variable.setAmbito(ambito); 
												   
												   String nombre = variable.getLexema()+"@"+ambito;
												   
												   variable.setLexema(nombre);
												   //name mangling! cambiar variable por variable@main
												   
												   System.out.println("\n Le hago name mangling con el ambito -> "+ambito+"\n");
												   
												   //remuevo variable vieja????????
												   //this.tabla.eliminarSimbolo($2);
												   
												   //aca le agrego uso a variable!
												   }
				        ;


		



declaracion_de_procedimiento : declaracion_PROC_ID lista_parametros_PROC cant_invocaciones_PROC cuerpo_PROC {
																											System.out.println("\n\n PROC CORRECTAMENTE DECLARADO \n\n");
																											}



declaracion_PROC_ID : PROC ID {
								System.out.println("\n CREO TERCETO PROC -> ( PROC , $$.obj.getLexema() ID , -- ) \n");
								
								String pos_str = "["+pos_ultimo_terceto+"]";
 								
 								//Token id = (Token)$2.obj;
 								  
 								//$$ = $3; //a pesar de la asignacion, $$ sigue apuntando al primer valor
 								Token id = (Token)$2.obj ;
 								
 								String nombre = id.getLexema()+"@"+ambito;
												   
							 	id.setLexema(nombre);
								//aca hacer name mangling! cambiar variable por variable@main
												     	  	 
 								this.ambito_proc = id.getLexema();
 								  	    
 	 							Terceto ter = new Terceto(this.nro_terceto, "PROC", id.getLexema() , "--" );
 								this.lista.agregarTerceto(ter);
 								   
 								//this.pos_BF = this.nro_terceto; //marco posicion del terceto BF !
 								
 								
 								//aca le completo el atributo uso al token proced  
 								
 								  
 								pos_ultimo_terceto = this.nro_terceto;
 					
 								this.lista.mostrarTercetos();
 					     		  
 					     		this.nro_terceto++;
								}
					;
					

lista_parametros_PROC : '(' lista_de_parametros ')' {System.out.println("\n ACA LE DOY SEMANTICA A LA LISTA DE PARAMETROS \n");
													//aclarar reglas de pasaje de parametros
													//COPIAR POR EJ X E Y -> 
													//para cada parametro se debe registrar: 
													//	tipo 
													//	forma de pasaje -> REF , copia-valor por defecto
													}
													
					  | '(' ')'
					  ;
					  

cant_invocaciones_PROC : NI '=' CTE {
									//ACA CHECKEO QUE CTE SEA < 4
									//para este proc -> asigno la cantidad de invocaciones que puede efectuar -> CTE
					   				System.out.println("ACA CHECKEO QUE CTE SEA < 4 , y la cant de invocaciones al PROC");
					   				//CTE debe ser de tipo entero (1, 2, 3, 4 )
					   				
					   				//checkeo nro y tipo de parametros en la invocacion
					   				//nro de invocaciones efectuadas por cada procedimiento
					   				}
					   ;
			
			
cuerpo_PROC : '{' lista_de_sentencias '}' {
											//ACA CREO LAS VARIABLES DEL CUERPO DE ACUERDO AL AMBITO DONDE SE ENCUENTRA
											System.out.println("\n CREO LAS VARIABLES DE ACUERDO AL AMBITO DEFINIDO DEL PROC \n");
											
											//dentro de la lista de sentencias, si defino una variable debo agregar el ambito
											}	
			;		




//numero maximo de parametros = 3 . puede no haber parametros!!   !!VER!!

lista_de_parametros : parametro_declarado ',' parametro_declarado ',' parametro_declarado
		    		| parametro_declarado ',' parametro_declarado
		    		| parametro_declarado { System.out.println("\n\n 1 solo parametro \n\n"); }
																												
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
								  
								  System.out.println("\n REGLA ASIGNACION!!  Esta correctamente inicializada  "+ id.getLexema() +"  en Tabla de Simbolos ? \n");
								  
								  
								  Token op = (Token)$2.obj;
								
								  $$ = $3; //a pesar de la asignacion, $$ sigue apuntando al primer valor
								  
								  
								  //ACA!! VER CONVERSIONES EXPLICITAS!
								  //primero checkea tipo de ID
								  //despues checkea tipo de $$(expr)
								  
								  //solo se podran efectuar operaciones entre dos operandos de distinto tipo si se convierte el operando de tipo entero al tipo de punto flotante
								  //caso contrario -> ERROR
								  
								  //si lado izq es tipo entero y lado der != tipo -->> ERROR de compatibilidad de tipos
								  
								  
								  
								  if (isToken) { 
								  	//System.out.println("\n EXPRESION $$ TOKEN!!  -> "+ ((Token)$$.obj).getLexema() +"\n");
							
								  	if (tabla.correctamenteDefinido(id)){
								  		System.out.println("\n SI, esta bien definido "+ id.getLexema()+"\n");
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
								  
							      	//System.out.println("\n ASIGNACION de EXPRESION $$ TERCETO!!  -> "+ ((Terceto)$$.obj).getOperando1() + " , " +((Terceto)$$.obj).getOperando2() +"\n");
								  	
								  	String pos_str = "["+pos_ultimo_terceto+"]";
								  	
								  	System.out.println("\n CREO TERCETO ASIGNACION! con terceto ->  ( "+op.getLexema()+" , "+ id.getLexema()+" , "+pos_str+" ) \n");
								  	
								  	Terceto ter = new Terceto(this.nro_terceto, "=", id.getLexema(), pos_str);
 									this.lista.agregarTerceto(ter); 
 								  	
 								  	this.lista.mostrarTercetos();
 								   	
 								   	this.nro_terceto++;
								  	
								  }
								  
								  isToken=true;
						
							   }
		   
		   | ID '=' '('tipo')'expresion {
		   								//conversion explicita!
		   								//si no reconoce el tipo -> error de compatibilidad 
		   								}			
		   
		   ;
		   
			


sentencia_de_control : IF condicion_IF cuerpo_IF END_IF {System.out.println("SENTENCIA DE CONTROL! aca completo tercetos BI");					 
														 
														 this.lista.completarTerceto(pos_BI, this.nro_terceto);
														 }
				     ;						


condicion_IF : '(' condicion ')' {
								  String pos_str = "["+pos_ultimo_terceto+"]";
 								  
 								  System.out.println("\n CREO EL TERCETO SALTO por FALSO ->  ( BF , "+pos_str +" ,   )  \n");				
 								  	  	 
 								  	    
 	 							  Terceto ter = new Terceto(this.nro_terceto, "BF", pos_str, " " );
 								  this.lista.agregarTerceto(ter);
 								   
 								  this.pos_BF = this.nro_terceto; //marco posicion del terceto BF !
 								  
 								  
 								  pos_ultimo_terceto = this.nro_terceto;
 					
 								  this.lista.mostrarTercetos();
 					     		  
 					     		  this.nro_terceto++;
 									   	
								  }
			 ;


cuerpo_IF : bloque_de_sentencias {//System.out.println("\n 2. THEN -> CUERPO DEL IF! \n");
								  System.out.println("\n COMPLETAR TERCETO INCOMPLETO  \n");
								  System.out.println("\n CREAR TERCETO BI (salto incondicional) \n");
								  }
									
		  | bloque_de_sentencias entra_ELSE bloque_de_sentencias {//System.out.println("...ELSE ");
		  														  //System.out.println("\n 3. COMPLETAR TERCETOS BF y BI  \n ");
		  														  }
		  ;


entra_ELSE : ELSE {//System.out.println("\n ENTRA AL ELSE \n");
				   
				   String pos_str = "["+pos_ultimo_terceto+"]";
 					
 					
 					System.out.println("\n CREA TERCETO SALTO INCONDICIONAL  ->  (  BI  ,   ,  -- ) ");
 	 								    
 	 				Terceto ter = new Terceto(this.nro_terceto, "BI", " " , "--");
 					this.lista.agregarTerceto(ter); 
 					
 					this.pos_BI = this.nro_terceto;
 					
 					pos_ultimo_terceto = this.nro_terceto;
 					
 					
 					this.lista.mostrarTercetos();
 									   	
 									   
 					//aca completo BF! ya tengo la posicion, que es pos_BF
 					
 					this.lista.completarTerceto(pos_BF, this.nro_terceto+1);
				   
				   	this.nro_terceto++; //sgte al terceto BI
 					
				   }
		   ;		  






sentencia_de_iteracion : comienzo_LOOP cuerpo_LOOP entra_UNTIL condicion_LOOP  {//System.out.println("\n SENTENCIA DE CONTROL DETECTADA \n");
																				System.out.println("\n TERMINA CORRECTAMENTE REGLA DE ITERACION... \n");
					  															}
					   ;
		             
		 
comienzo_LOOP : LOOP {
					  System.out.println("\n aca marco el comienzo de la sentencia loop! \n");
					  this.pos_comienzo_loop=this.nro_terceto;
					 }
			  ;
			  
				
cuerpo_LOOP : bloque_de_sentencias {									
									System.out.println("3. CUERPO/BLOQUE DEL WHILE ");
								    System.out.println("\n COMPLETAR TERCETO INCOMPLETO  \n");
								    System.out.println("\n CREAR TERCETO BI (salto incondicional) \n");
								    
								    //aca ya termine el cuerpo...
								    
								    //ACA creo el BI incompleto ?

								    String pos_str = "["+pos_ultimo_terceto+"]";
 					
 					
				 					//System.out.println("\n CREA TERCETO SALTO INCONDICIONAL  ->  (  BI  ,   ,  -- ) ");
				 	 								    
				 	 				//Terceto ter = new Terceto(this.nro_terceto, "BI", " " , "--");
				 					//this.lista.agregarTerceto(ter); 
				 					
				 					//this.pos_BI = this.nro_terceto;
				 					
				 					//pos_ultimo_terceto = this.nro_terceto;
				 					
				 					
				 					//this.lista.mostrarTercetos();
				 									   	
				 									   
				 					//aca completo BF! ya tengo la posicion, que es pos_BF
				 					
				 					//this.lista.completarTerceto(pos_BF, this.nro_terceto+1);
								   
								   	//this.nro_terceto++; //sgte al terceto BI
 					
								    //avanzo el terceto...
								    }
									
		    ;


entra_UNTIL : UNTIL { //aca entra en condicion!!
						//aca seria el comienzo de la condicion!
					System.out.println("\n QUE ONDA?? el UNTIL... \n");
					this.pos_comienzo_condicion_loop = this.pos_ultimo_terceto+1;
					System.out.println("\n COMIENZO DE CONDICION... "+this.pos_comienzo_condicion_loop +"\n");						 
					} 
			;


condicion_LOOP : '(' condicion ')' {
									//ejecuto la condicion, igual que condicionIF...
									//aca comienza la condicion?? apilo el nro de terceto
									//o termina la condicion???
									//aca creo terceto incompleto para BF??
									
									//ACAA creo el BF , en blanco?
									
									String pos_str = "["+this.pos_comienzo_loop+"]";
 								    String pos_sgte = "["+(pos_ultimo_terceto+2)+"]";
 								    
 								    System.out.println("\n CREO EL TERCETO SALTO por FALSO ->  ( BF , "+pos_str +" , "+ pos_sgte +" )  \n");				
 								  	  	 
 								  	    
 	 							    Terceto ter = new Terceto(this.nro_terceto, "BF", pos_str, pos_sgte );
 								    this.lista.agregarTerceto(ter);
 								   
 								    this.pos_BF = this.nro_terceto; //marco posicion del terceto BF !
 								  
 								  
 								    pos_ultimo_terceto = this.nro_terceto;
 					
 								    this.lista.mostrarTercetos();
 					     		    
 					     		    this.nro_terceto++;
 					     		    
 					    			}
			   ;

		
		
		
				
condicion : expresion '>' expresion {if (isToken) { 
 									  	 Token op1 = (Token)$1.obj;
 										 Token op2 = (Token)$2.obj;
 	 								     Token op3 = (Token)$3.obj;
 									  	 
 									  	 System.out.println("CREA TERCETO COMPARACION con TOKEN  ->  (  >  , " +((Token)$$.obj).getLexema() + " , " + op3.getLexema()+" )  \n");
 	 								     
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

	 								      
	 								      	String pos_terc1 = "["+(pos_ultimo_terceto-1)+"]";
	 									   	
	 								      	String pos_terc2 = "["+pos_ultimo_terceto+"]";
	 									  	
	 									  	 
	 								      	System.out.println("\n CREA TERCETOS COMPARACION con TERCETO y TERCETO ->  (  >  ,  "+pos_terc1 + "  ,  " + pos_terc2+" ) \n");//op3.getOperando2());
	 	 								    
	 	 								    
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

	 								      	
	 								      	String pos_terc1 = "["+(pos_ultimo_terceto-1)+"]";
	 									   	
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




bloque_de_sentencias : '{' lista_de_sentencias '}' {
												    }
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
								   
								   
								   //ACA HACER CHECKEO DE TIPOS! CONVERSION EXPLICITA
								   //PRIMERO GENERO TERCETO QUE CONVIERTE EL TIPO DE Y -> TERCETO CONVERSION
								   //GUARDO EL TERCETO EN VAR AUX
								   
								   //C/CONVERSION EXPLICITA GENERA TERCETO
								   
								   
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
	  	  			 
	  	  | CADENA //{System.out.println("EXPRESION -> CADENA! ");
	  	  ;



termino : termino '*' factor {
							  Token op1 = (Token)$1.obj;
							  int linea = op1.getNroLinea();
							  Token op2 = (Token)$2.obj;
							  Token op3 = (Token)$3.obj;
							  
							  System.out.println("\n TERCETO MULTIPLICACION  ->  ( "+op2.getLexema()+" , "+op1.getLexema()+" , "+op3.getLexema()+" ) \n");
							  
							  
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
		
		| factor {
				  $$=$1; 	// termino.ptr = factor.ptr
				  
	   		  	  //System.out.println("\n REGLA TERMINO -> Llega FACTOR!  "+ ((Token)$$.obj).getLexema() +"  la apunto con $$?? \n");
				 }
		
		//| CADENA //{System.out.println("termino -> CADENA! ");
		;
		
		
		
factor : CTE {Token factor = (Token)$$.obj; 
	   		  //System.out.println("\n Llega CTE  "+ factor.getLexema() +"  la apunto con $$?? \n");
	   		  $$=$1;
	   		  //System.out.println("\n REGLA FACTOR -> Llega CTE!  "+ ((Token)$$.obj).getLexema() +"  la apunto con $$?? \n");
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
String ambito_proc;

boolean isToken=true;

int nro_terceto = 0;
int pos_ultimo_terceto = 0;

int pos_BF = 0;
int pos_BI = 0;

int pos_comienzo_loop = 0;
int pos_comienzo_condicion_loop=0;


void nameManglin()
{
 
}


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
 	
 	//GeneradorCodigo gc = new GeneradorCodigo(l);
 	//gc.generarCodigo();
}