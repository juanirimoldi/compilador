%{
import java.lang.Math;
import java.io.*;

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



programa : lista_de_sentencias {
								//System.out.println("\n LLEGO A RAIZ! -> termino programa \n ");
								}
		 ;


lista_de_sentencias : sentencia 
		   		    | lista_de_sentencias sentencia 
		    		;


sentencia : sentencia_declarativa ';' {//System.out.println("\n SENTENCIA DECLARATIVA CORRECTA \n");
								   	   //System.out.println("\n----------------------------------------\n");
									  }
		  | sentencia_ejecutable ';' {//System.out.println("\n SENTENCIA EJECUTABLE CORRECTA \n");
		    	   				       //System.out.println("\n----------------------------------------\n");
		  						     }
	  	  ;





//SENTENCIAS DECLARATIVAS


sentencia_declarativa : declaracion_de_variable 

				      | declaracion_de_procedimiento
				      ;

		
		
declaracion_de_variable : tipo lista_de_variables  {Token tipo = (Token)$1.obj;
												   Token variable = (Token)$2.obj;
												   
												   //String nombre = variable.getLexema()+"@"+ambito;
													   
												   //variable.setLexema(nombre);
												   
												   //System.out.println("EXISTE VAR ?? "+variable.getLexema());
												   boolean existeVar = tabla.existe(variable.getLexema());
								  				   //tabla.mostrarSimbolos();
								  				   
								  				   //if (existeVar) {
					   								
													   String nombre = variable.getLexema()+"@"+ambito;
													   
													   variable.setLexema(nombre);
													   //name mangling! cambiar variable por variable@ambito
													  
													   
													   variable.setUso("variable");
													   
													 // } else {
													   
													  // System.out.println("ERROR -> variable redeclarada ");
													 //}
												}
				        ;


		


declaracion_de_procedimiento : declaracion_PROC_ID lista_parametros_PROC cant_invocaciones_PROC cuerpo_PROC {																																																					
																											//desapilo ambito temporal de ambito
																																																					
					   																						//Token t = (Token)tabla.getSimbolo(this.proc_declarado);
					   																						//boolean esValido = tabla.correctamenteDefinido(t);
								  																			//if (esValido) {
						   																						
																												
									  																			Terceto ter = new Terceto(this.nro_terceto, "RET", this.proc_declarado, "--");
	 									   																		this.lista.agregarTerceto(ter); 
	 									   	
	 									   																		this.nro_terceto++;
																												
																												System.out.println(this.proc_declarado+" , "+this.cant_invocaciones);
	 									   																		if (this.proc_declarado != "" ) {
	 									   																			this.setCantidadInvocaciones();/*this.proc_declarado, this.cant_invocaciones);*/
	 									   																		}									
																												desapilarAmbito();
						   																						
																												//this.tabla.mostrarSimbolos();
																											
																											//}else{
																											//	System.out.println("ERROR redeclaracion de procedimiento");
																											//}
																										}
	


declaracion_PROC_ID : PROC ID {											  
 								Token id = (Token)$2.obj ;
 								
 								//boolean esValido = tabla.existe(id.getLexema());
								//if (esValido) {
					   																						
	 								//checkeo de procedimiento redeclarado
	 								//if (tabla.existe(id.getLexema())) {
	 									//System.out.println("NO existe en tabla de simbolos "+id.getLexema());
		 								String nombre = id.getLexema()+"@"+ambito;
														   
									 	id.setLexema(nombre);
										//name mangling! cambia variable por variable@main
										id.setUso("procedimiento");
												
										this.ambito = nombre ;
												
														     	  	 
		 								this.proc_declarado = nombre;
		 								
		 								
		 								
		 								//System.out.println("\n CREO TERCETO PROC -> ( PROC , "+id.getLexema()+" , -- ) \n");
										
										
										//en vez de setearle el nombre, agrego una nueva variable, renombrada. 
		 								tabla.addToken(id);
		 								
		 									 								  	    
		 	 							Terceto ter = new Terceto(this.nro_terceto, "PROC", id.getLexema() , "--" );
		 								this.lista.agregarTerceto(ter);
		 								   
		 								 								
		 								pos_ultimo_terceto = this.nro_terceto;
		 					
		 								//this.lista.mostrarTercetos();
		 					     		  
		 					     		this.nro_terceto++;
		 					     		
								//} else {
								
								//	System.out.println("\n ERROR PROCEDIMIENTO REDECLARADO "+ id.getLexema() +"\n");
								
								//}
							}
					;
					
					

lista_parametros_PROC : '(' lista_de_parametros ')' {//System.out.println("\n ACA LE DOY SEMANTICA A LA LISTA DE PARAMETROS \n");
													//aclarar reglas de pasaje de parametros
													
													//para cada parametro se debe registrar: 
													//	tipo 
													//	forma de pasaje -> REF , copia-valor por defecto
													
													//recorrer la lista de parametros y agregar info de cada parametro a Tsym
													}
													
					  | '(' ')'
					  
					  ;
					  
					  

cant_invocaciones_PROC : NI '=' CTE {
									
									//System.out.println("cantidad de invocaciones al PROC con CTE < 4 ");
					   			
					   				Token cte = (Token)$3.obj;
					   				
					   				
					   				this.cant_invocaciones = Integer.parseInt(cte.getLexema()); 
					   				
					   				//this.setCantidadInvocaciones();/*this.proc_declarado, this.cant_invocaciones);*/
																												
					   				}
					   ;
			
			
cuerpo_PROC : '{' lista_de_sentencias '}' {
											//System.out.println("\n CREO LAS VARIABLES DE ACUERDO AL AMBITO DEFINIDO DEL PROCEDIMIENTO \n");
											
											}	
			;		





lista_de_parametros : parametro_declarado ',' parametro_declarado ',' parametro_declarado
		    		| parametro_declarado ',' parametro_declarado
		    		| parametro_declarado { 
		    								//System.out.println("\n\n 1 parametro \n\n"); 
		    							   }
																												
				    ;


parametro_declarado : tipo ID
				    | REF tipo ID { 
				    				//aca agregar semantica de pasaje por referencia a la variable ID
				    			   }
		    		;


lista_de_variables : lista_de_variables ',' ID
		   		   | ID
		  		   ;


tipo : INTEGER
     | FLOAT
     ;






//SENTENCIAS EJECUTABLES

		
sentencia_ejecutable : asignacion {} 
		   			 | sentencia_de_control
		   			 | sentencia_de_iteracion 
		   			 | sentencia_de_salida 
		   			 | invocacion 
					 ;




asignacion : ID '=' expresion  {  Token id = (Token)$1.obj;
								  
								  //int linea = id.getNroLinea();
								  
								  //System.out.println("\n REGLA ASIGNACION \n");  
								  //System.out.println("\n Esta correctamente inicializada  "+ id.getLexema() +"  en Tabla de Simbolos ? \n");
								  
								  
								  Token op = (Token)$2.obj;
								
								  $$ = $3; //desde lado izq apunto a la expresion
								  //$$.obj = $3 rompe todo				
								  
								  boolean esValido = tabla.correctamenteDefinido(id);
								  if (esValido) {
									  if (isToken) { 
									  	
									  		
									  		Terceto ter = new Terceto(this.nro_terceto, op.getLexema(), id.getLexema(), ((Token)$3.obj).getLexema());
	 									   	this.lista.agregarTerceto(ter); 
	 									   	
	 									   	//this.lista.mostrarTercetos();
	 									   	
	 									   	this.nro_terceto++;
									  	
									  	//} else {
									  	
									  		//System.out.println("\n ERROR -> a "+id.getLexema()+"  no le agrego ambito porque no es valido \n");
									  		//break;
									  		//$$.obj=null; //VER COMO ROMPER UNA REGLA!
									  	//}
									  	
									  } else {
									  
								      	String pos_str = "["+pos_ultimo_terceto+"]";
									  	
									  	//System.out.println("\n CREO TERCETO ASIGNACION! con terceto ->  ( "+op.getLexema()+" , "+ id.getLexema()+" , "+pos_str+" ) \n");
									  	
									  	Terceto ter = new Terceto(this.nro_terceto, "=", id.getLexema(), pos_str);
	 									this.lista.agregarTerceto(ter); 
	 								  	
	 								  	//this.lista.mostrarTercetos();
	 								   	
	 								   	this.nro_terceto++;
									  	
									  }
									  
									isToken=true;
								   } else {
								 	System.out.println("ERROR de asignacion ");
								 }
		   						}
		   
		   | ID '=' '('tipo')'expresion {
		   								//conversion explicita!
		   								//si no reconoce el tipo -> error de compatibilidad 
		   								//ACA!! VER CONVERSIONES EXPLICITAS!
										//primero checkea tipo de ID
										//despues checkea tipo de $$(expr)
										  
										//solo se podran efectuar operaciones entre dos operandos de distinto tipo si se convierte el operando de tipo entero al tipo de punto flotante
										//caso contrario -> ERROR
										  
										//si lado izq es tipo entero y lado der != tipo -->> ERROR de compatibilidad de tipos
										  
								  		  Token id = (Token)$1.obj;
										  int linea = id.getNroLinea();
										  
										  System.out.println("\n REGLA ASIGNACION -> CONVERSION EXPLICITA! \n"); 
										  System.out.println("\n Esta correctamente inicializada  "+ id.getLexema() +"  en Tabla de Simbolos ? \n");
										  
										  
										  Token op = (Token)$2.obj;
										
										  $$ = $3; //lado izq apunto a la expresion
										  								  
										  
										  
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
										  	
										  		System.out.println("\n ERROR -> EL ID  "+id.getLexema()+" no esta correctamente definido. cancelo la asignacion  \n");
										  		System.out.println("\n a  "+id.getLexema()+"  no le agrego ambito -> no es valido \n");
										  		//luego elimina las entradas ID sin ambito
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
		   
		   ;
		   
			





sentencia_de_control : IF condicion_IF cuerpo_IF END_IF {//System.out.println("\n SENTENCIA DE CONTROL!  completo tercetos BI \n");					 
														 
														 this.lista.completarTerceto(pos_BI, this.nro_terceto);
														 														 
														 }
				     ;						


condicion_IF : '(' condicion ')' {
								  String pos_str = "["+pos_ultimo_terceto+"]";
 								  
 								  //System.out.println("\n CREO EL TERCETO SALTO por FALSO ->  ( BF , "+pos_str +" ,   )  \n");				
 								  	  	 
 								  	    
 	 							  Terceto ter = new Terceto(this.nro_terceto, "BF", pos_str, " " );
 								  this.lista.agregarTerceto(ter);
 								   
 								  this.pos_BF = this.nro_terceto; //marco posicion del terceto BF !
 								  
 								  
 								  pos_ultimo_terceto = this.nro_terceto;
 					
 								  //this.lista.mostrarTercetos();
 					     		  
 					     		  this.nro_terceto++;
 									   	
								  }
			 ;


cuerpo_IF : bloque_de_sentencias {
								
 								this.lista.completarTerceto(pos_BF, this.nro_terceto); //antes +1
				   
				   				 }
									
		  | bloque_de_sentencias entra_ELSE bloque_de_sentencias {
		  														  }
		  ;


entra_ELSE : ELSE {				   
				   String pos_str = "["+pos_ultimo_terceto+"]";
 					
 				   //System.out.println("\n CREA TERCETO SALTO INCONDICIONAL  ->  (  BI  ,   ,  -- ) ");
 	 								    
 	 			   Terceto ter = new Terceto(this.nro_terceto, "BI", " " , "--");
 				   this.lista.agregarTerceto(ter); 
 					
 				   //guardo posicion actual para salto incondicional
 				   this.pos_BI = this.nro_terceto;
 					
 				   pos_ultimo_terceto = this.nro_terceto;
 					
 					
 				   //this.lista.mostrarTercetos();
 									   	
 									   
 				   //aca completo salto por falso BF! ya tengo la posicion, que es pos_BF
 					
 				   this.lista.completarTerceto(pos_BF, this.nro_terceto+1);
				   
				   this.nro_terceto++; //sgte al terceto BI
 					
				   }
		   ;		  







sentencia_de_iteracion : comienzo_LOOP cuerpo_LOOP entra_UNTIL condicion_LOOP  {
																				//System.out.println("\n TERMINA CORRECTAMENTE REGLA DE ITERACION... \n");
					  															}
					   ;
		             
		 
comienzo_LOOP : LOOP {
					  //System.out.println("\n marco el comienzo de la sentencia loop! \n");
					  this.pos_comienzo_loop=this.nro_terceto;
					 }
			  ;
			  
				
cuerpo_LOOP : bloque_de_sentencias {									
									//System.out.println("\n COMPLETAR TERCETO INCOMPLETO  \n");
								    
								    //System.out.println("\n CREAR TERCETO BI (salto incondicional) \n");
								    
								    
								    String pos_str = "["+pos_ultimo_terceto+"]";
 					
 								 	}			
		    ;


entra_UNTIL : UNTIL { 
					this.pos_comienzo_condicion_loop = this.pos_ultimo_terceto+1;
					//System.out.println("\n COMIENZO DE CONDICION en posicion "+this.pos_comienzo_condicion_loop +"\n");						 
					} 
			;


condicion_LOOP : '(' condicion ')' {
									
									String pos_str = "["+this.pos_comienzo_loop+"]";
 								    String pos_sgte = "["+(pos_ultimo_terceto+2)+"]";
 								    
 								    //System.out.println("\n CREO EL TERCETO SALTO por FALSO ->  ( BF , "+pos_str +" , "+ pos_sgte +" )  \n");				
 								  	  	 
 								  	    
 	 							    Terceto ter = new Terceto(this.nro_terceto, "BF", pos_str, pos_sgte );
 								    this.lista.agregarTerceto(ter);
 								   
 								    this.pos_BF = this.nro_terceto; //marco posicion del terceto BF !
 								  
 								  
 								    pos_ultimo_terceto = this.nro_terceto;
 					
 								    //this.lista.mostrarTercetos();
 					     		    
 					     		    this.nro_terceto++;
 					     		    
 					    			}
			   ;

		
		
		

				
condicion : expresion '>' expresion {if (isToken) { 
 									  	 Token op1 = (Token)$1.obj;
 										 Token op2 = (Token)$2.obj;
 	 								     Token op3 = (Token)$3.obj;
 									  	 
 									  	 //System.out.println("\n CREA TERCETO COMPARACION con TOKEN  ->  (  >  , " +((Token)$$.obj).getLexema() + " , " + op3.getLexema()+" )  \n");
 	 								     
 	 								     Terceto ter = new Terceto(this.nro_terceto, ">", op1.getLexema(), op3.getLexema());
 									   	 this.lista.agregarTerceto(ter); 
 									   	 pos_ultimo_terceto = this.nro_terceto;
 									   	 
 									   	 //this.lista.mostrarTercetos();
 									   	 
 									   	 this.nro_terceto++;
 									   	 
 									  } else {
 									  
 								      	//System.out.println("\n COMPARACION de EXPRESION $$ con TERCETO!!  -> "+ ((Terceto)yyval.obj).getOperando1() +" , "+((Terceto)yyval.obj).getOperando2()+"\n");
 									  	
 									  	//caso en que $3 es un terceto
 									  	String tipo_obj = $3.obj.toString();
 								      	
 								      	if (tipo_obj.substring(18,23).equals("Token")) { //si el tipo del $3 objeto es Token
 								      		Token op3 = (Token)$3.obj;
 								      		String pos_str = "["+pos_ultimo_terceto+"]";
 	 									  	 
 	 								      	//System.out.println("\n CREA TERCETO COMPARACION con TERCETO y Token ->  (  >  ,  "+pos_str + "  ,  " + op3.getLexema());//op3.getOperando2());
 	 	 								    
 	 	 								    Terceto ter = new Terceto(this.nro_terceto, ">", pos_str, op3.getLexema());//op3.getOperando2());
 	 									   	this.lista.agregarTerceto(ter); 
 	 									   
 	 									   	//this.lista.mostrarTercetos();
 	 									   	
 	 									   	pos_ultimo_terceto = this.nro_terceto;
 	 									   	
 	 									   	this.nro_terceto++;
 	 									   	
 								      	} else { //si el argumento $3 es un terceto
 								      		
 								      		Terceto op3 = (Terceto)$3.obj;

	 								      
	 								      	String pos_terc1 = "["+(pos_ultimo_terceto-1)+"]";
	 									   	
	 								      	String pos_terc2 = "["+pos_ultimo_terceto+"]";
	 									  	
	 									  	 
	 								      	//System.out.println("\n CREA TERCETOS COMPARACION con TERCETO y TERCETO ->  (  >  ,  "+pos_terc1 + "  ,  " + pos_terc2+" ) \n");//op3.getOperando2());
	 	 								    
	 	 								    
	 	 								    Terceto ter = new Terceto(this.nro_terceto, ">", pos_terc1, pos_terc2);//op3.getOperando2());
	 									   	this.lista.agregarTerceto(ter); 
	 									   	
	 									   	//this.lista.mostrarTercetos();
	 									   	
	 									   	pos_ultimo_terceto = this.nro_terceto;
	 									   	
	 									   	this.nro_terceto++;
 								      	}  	 
 									  }
 									  
 									  isToken=true;
									}
									
									
	  	  | expresion '<' expresion {
	  	  							if (isToken) { 
 									  	//System.out.println("\n COMPARACION de EXPRESION $$ TOKEN!!  -> "+ ((Token)yyval.obj).getLexema() +"\n");
 									  	
 									  	Token op1 = (Token)$1.obj;
 										Token op2 = (Token)$2.obj;
 	 								    Token op3 = (Token)$3.obj;
 									  	
 									  	//System.out.println("\n CREA TERCETO COMPARACION con TOKENS  ->  (  <  , "+$$.obj + " , " + op3.getLexema()+" ) \n");
 	 								     
 	 								    Terceto ter = new Terceto(this.nro_terceto, "<", op1.getLexema(), op3.getLexema());
 									   	this.lista.agregarTerceto(ter); 
 									   	pos_ultimo_terceto = this.nro_terceto;
 									   	 
 									   	//this.lista.mostrarTercetos();
 									   	 
 									   	this.nro_terceto++;
 									   	 
 									  } else {
 									  
 								      	//System.out.println("\n COMPARACION de EXPRESION $$ con TERCETO!!  -> "+ ((Terceto)yyval.obj).getOperando1() +" , "+((Terceto)yyval.obj).getOperando2()+"\n");
 									  	
 									  	//caso en que $3 es un terceto
 									  	String tipo_obj = $3.obj.toString();
 								      	
 								      	if (tipo_obj.substring(18,23).equals("Token")) { //si el tipo del $3 objeto es Token
 								      		Token op3 = (Token)$3.obj;
 								      		String pos_str = "["+pos_ultimo_terceto+"]";
 	 									  	 
 	 								      	//System.out.println("\n CREA TERCETO COMPARACION con TERCETO y Token ->  (  <  ,  "+pos_str + "  ,  " + op3.getLexema()+" ) \n");
 	 	 								    
 	 	 								    Terceto ter = new Terceto(this.nro_terceto, "<", pos_str, op3.getLexema());//op3.getOperando2());
 	 									   	this.lista.agregarTerceto(ter); 
 	 									   	
 	 									   	//this.lista.mostrarTercetos();
 	 									   	
 	 									   	pos_ultimo_terceto = this.nro_terceto;
 	 									   	
 	 									   	this.nro_terceto++;
 	 									   	
 								      	} else { //si el argumento $3 es un terceto
 								      		
 								      		Terceto op3 = (Terceto)$3.obj;

	 								      	
	 								      	String pos_terc1 = "["+(pos_ultimo_terceto-1)+"]";
	 									   	
	 								      	String pos_terc2 = "["+pos_ultimo_terceto+"]";
	 									  	 
	 									  	 
	 								      	//System.out.println("\n CREA TERCETOS COMPARACION con TERCETO y TERCETO ->  (  <  ,  "+pos_terc1 + "  ,  " + pos_terc2);//op3.getOperando2());
	 	 								    
	 	 								    Terceto ter = new Terceto(this.nro_terceto, "<", pos_terc1, pos_terc2);//op3.getOperando2());
	 									   	this.lista.agregarTerceto(ter); 
	 									   	
	 									   	//this.lista.mostrarTercetos();
	 									   	
	 									   	pos_ultimo_terceto = this.nro_terceto;
	 									   	
	 									   	this.nro_terceto++;
 								      	}  	 
 									  }
 									  
 									  isToken=true;
									}
									
	  	  | expresion MAYORIGUAL expresion
	  	  
	  	  | expresion MENORIGUAL expresion
	  	  
	  	  | expresion IGUAL expresion {
	  	   							if (isToken) { 
 									  	//System.out.println("\n COMPARACION de EXPRESION $$ TOKEN!!  -> "+ ((Token)yyval.obj).getLexema() +"\n");
 									  	
 									  	Token op1 = (Token)$1.obj;
 										Token op2 = (Token)$2.obj;
 	 								    Token op3 = (Token)$3.obj;
 									  	
 									  	System.out.println("\n CREA TERCETO COMPARACION == con TOKENS  ->  (  <  , "+$$.obj + " , " + op3.getLexema()+" ) \n");
 	 								     
 	 								    Terceto ter = new Terceto(this.nro_terceto, "==", op1.getLexema(), op3.getLexema());
 									   	this.lista.agregarTerceto(ter); 
 									   	pos_ultimo_terceto = this.nro_terceto;
 									   	 
 									   	//this.lista.mostrarTercetos();
 									   	 
 									   	this.nro_terceto++;
 									   	 
 									  } else {
 									  
 								      	//System.out.println("\n COMPARACION de EXPRESION $$ con TERCETO!!  -> "+ ((Terceto)yyval.obj).getOperando1() +" , "+((Terceto)yyval.obj).getOperando2()+"\n");
 									  	
 									  	//caso en que $3 es un terceto
 									  	String tipo_obj = $3.obj.toString();
 								      	
 								      	if (tipo_obj.substring(18,23).equals("Token")) { //si el tipo del $3 objeto es Token
 								      		Token op3 = (Token)$3.obj;
 								      		String pos_str = "["+pos_ultimo_terceto+"]";
 	 									  	 
 	 								      	System.out.println("\n CREA TERCETO COMPARACION con TERCETO y Token ->  (  <  ,  "+pos_str + "  ,  " + op3.getLexema());//op3.getOperando2());
 	 	 								    
 	 	 								    Terceto ter = new Terceto(this.nro_terceto, "<", pos_str, op3.getLexema());//op3.getOperando2());
 	 									   	this.lista.agregarTerceto(ter); 
 	 									   	
 	 									   	//this.lista.mostrarTercetos();
 	 									   	
 	 									   	pos_ultimo_terceto = this.nro_terceto;
 	 									   	
 	 									   	this.nro_terceto++;
 	 									   	
 								      	} else { //si el argumento $3 es un terceto
 								      		
 								      		Terceto op3 = (Terceto)$3.obj;

	 								      	
	 								      	String pos_terc1 = "["+(pos_ultimo_terceto-1)+"]";
	 									   	
	 								      	String pos_terc2 = "["+pos_ultimo_terceto+"]";
	 									  	 
	 									  	 
	 								      	System.out.println("\n CREA TERCETOS COMPARACION con TERCETO y TERCETO ->  (  <  ,  "+pos_terc1 + "  ,  " + pos_terc2);//op3.getOperando2());
	 	 								    
	 	 								    Terceto ter = new Terceto(this.nro_terceto, "<", pos_terc1, pos_terc2);//op3.getOperando2());
	 									   	this.lista.agregarTerceto(ter); 
	 									   	
	 									   	//this.lista.mostrarTercetos();
	 									   	
	 									   	pos_ultimo_terceto = this.nro_terceto;
	 									   	
	 									   	this.nro_terceto++;
 								      	}  	 
 									  }
 									  
 									  isToken=true;
									}
						  	  //}
	  	  
	  	  | expresion DISTINTO expresion
	  	  ;




bloque_de_sentencias : '{' lista_de_sentencias '}' {
												    }
				     ;

					

					
sentencia_de_salida : OUT '(' CADENA ')'
				    ;
					
					
invocacion : ID '(' parametro_ejecutable ')' {
	   	   									 Token id = (Token)$1.obj;
	   	   									 //System.out.println("\n\n INVOCACION -> "+ id.getLexema() +"\n\n");
	   	   									 
	   	   									 if (tabla.correctamenteDefinido(id)){
								  		
								  				//System.out.println("\n PROCEDIMIENTO "+ id.getLexema()+" CORRECTAMENTE DEFINIDO  \n\n");
								  				
								  				//System.out.println("\n CREO TERCETO CALL -> ( CALL , "+ id.getLexema()+" )  \n");
								  				
								  				Terceto ter = new Terceto(this.nro_terceto, "CALL",id.getLexema(),"--");
	 									   		this.lista.agregarTerceto(ter); 
	 									   	
	 									   	
	 									   		this.proc_invocado = id.getLexema();
	 									   	
	 									   		this.procedimientoInvocado();
	 									   	
	 									   		
	 									   		pos_ultimo_terceto = this.nro_terceto;
	 									   	
	 									   		this.nro_terceto++;
															  		
								  			 } else {
								  	
								  			 	System.out.println("\n ERROR -> PROCEDIMIENTO "+id.getLexema()+"  no es valido \n");
								  			 	}
	   	   									 }
	   	   									 
	   	   ;
		  

		  
parametro_ejecutable : ID ':' ID  
								{
								Token id1 = (Token)$1.obj; 
								Token id2 = (Token)$3.obj; 
								//if existe real -> hago una copia de su parametro formal
								//else -> ERROR ! no existe parametro real
								//System.out.println("\n PARAMETROS EJECUTABLES ->  real : "+id1.getLexema()+"  ,   formal : "+id2.getLexema()+"\n ");
								
								//FALTA REF Y SU SEMANTICA
								
								}
								
		   		     | parametro_ejecutable ',' ID':'ID
		     		 ;
		 			
					
					

expresion : expresion '+' termino {
								   
								   //FALTA HACER CHECKEO DE TIPOS! CONVERSION EXPLICITA
								   //PRIMERO GENERO TERCETO QUE CONVIERTE EL TIPO DE Y -> TERCETO CONVERSION
								   //GUARDO EL TERCETO EN VAR AUX
								   
								   //C/CONVERSION EXPLICITA GENERA TERCETO
								   
								   
								   
								   
								   String tipo_obj1 = $1.obj.toString();
								  String tipo_obj2 = $3.obj.toString();
								  boolean obj1 = false;
								  boolean obj2 = false;
								  
								  if (tipo_obj1.substring(18,23).equals("Token")){
								  	obj1 = true;	
								  }
								  
								  if (tipo_obj2.substring(18,23).equals("Token")){
								  	obj2 = true;	
								  }
								  
								  
								  if (obj1 && obj2) { //si son los dos Tokens
	
								  	Token op1 = (Token)$1.obj;
								  	//Token op2 = (Token)$2.obj;
									Token op3 = (Token)$3.obj;
											  
									//System.out.println("\n TERCETO SUMA simple ->  ( + , "+op1.getLexema()+" , "+op3.getLexema()+" ) \n");
											  
											  
									Terceto ter = new Terceto(this.nro_terceto, "+" , op1.getLexema(), op3.getLexema());
									this.lista.agregarTerceto(ter); 
									
									pos_ultimo_terceto = this.nro_terceto;
											  
									//this.lista.mostrarTercetos();
											  
									this.nro_terceto++;
									//apunto a terceto!?
									$$.obj = ter ;
									this.isToken=false;
											
								  }
								  
								  
								  if (obj1 & !obj2) { //primero Token y el 2do Terceto
								  	Token op3 = (Token)$1.obj;
	 								String pos_str = "["+pos_ultimo_terceto+"]";
	 	 								  	 
	 	 							//System.out.println("\n TERCETO SUMA entre Token y TERCETO  ->  (  +  ,  "+pos_str + "  ,  " + op3.getLexema()+" ) \n");
	 	 	 								    
	 	 	 						Terceto ter = new Terceto(this.nro_terceto, "+", pos_str, op3.getLexema());
	 	 							this.lista.agregarTerceto(ter); 
	 	 									   
	 	 							//this.lista.mostrarTercetos();
	 	 									   	
	 	 							pos_ultimo_terceto = this.nro_terceto;
	 	 									   	
	 	 							this.nro_terceto++;
	 	 							
	 	 							$$.obj = ter ;
									this.isToken=false;
									
								  }
								  
								  
								  if (!obj1 & obj2) { //primero Terceto y el 2do Token
								  	Token op3 = (Token)$3.obj;
	 								String pos_str = "["+pos_ultimo_terceto+"]";
	 	 								  	 
	 	 							//System.out.println("\n TERCETO SUMA entre TERCETO y Token ->  (  *  ,  "+pos_str + "  ,  " + op3.getLexema());//op3.getOperando2());
	 	 	 								    
	 	 	 						Terceto ter = new Terceto(this.nro_terceto, "+", pos_str, op3.getLexema());//op3.getOperando2());
	 	 							this.lista.agregarTerceto(ter); 
	 	 									   
	 	 							//this.lista.mostrarTercetos();
	 	 									   	
	 	 							pos_ultimo_terceto = this.nro_terceto;
	 	 									   	
	 	 							this.nro_terceto++;
	 	 							
	 	 							$$.obj = ter ;
									this.isToken=false;
											   
								  }
								  
								  
								  if (!obj1 & !obj2) {
									  //OPERACION ENTRE 2 TERCETOS!
	 								      		
	 								  Terceto op3 = (Terceto)$3.obj;
	
		 								      
		 							  String pos_terc1 = "["+(pos_ultimo_terceto-1)+"]";
		 									   	
		 							  String pos_terc2 = "["+pos_ultimo_terceto+"]";
		 									  	
		 									  	 
		 							  //System.out.println("\n CREA TERCETO SUMA con TERCETO y TERCETO ->  (  >  ,  "+pos_terc1 + "  ,  " + pos_terc2+" ) \n");//op3.getOperando2());
		 	 								    
		 	 							    
		 	 						  Terceto ter = new Terceto(this.nro_terceto, "+m", pos_terc1, pos_terc2);//op3.getOperando2());
		 							  this.lista.agregarTerceto(ter); 
		 									   	
		 							  //this.lista.mostrarTercetos();
		 									   	
		 							  pos_ultimo_terceto = this.nro_terceto;
		 									   	
		 						      this.nro_terceto++;
									
									  $$.obj = ter ;
									  this.isToken=false;
																		  
								  }
							  
								 }
	  	  
	  	  
	  	  | expresion '-' termino {//FALTA HACER CHECKEO DE TIPOS! CONVERSION EXPLICITA
								   //PRIMERO GENERO TERCETO QUE CONVIERTE EL TIPO DE Y -> TERCETO CONVERSION
								   //GUARDO EL TERCETO EN VAR AUX
								   
								   //C/CONVERSION EXPLICITA GENERA TERCETO
								   
								   
								   
								   
								   String tipo_obj1 = $1.obj.toString();
								  String tipo_obj2 = $3.obj.toString();
								  boolean obj1 = false;
								  boolean obj2 = false;
								  
								  if (tipo_obj1.substring(18,23).equals("Token")){
								  	obj1 = true;	
								  }
								  
								  if (tipo_obj2.substring(18,23).equals("Token")){
								  	obj2 = true;	
								  }
								  
								  
								  if (obj1 && obj2) { //si son los dos Tokens
	
								  	Token op1 = (Token)$1.obj;
								  	//Token op2 = (Token)$2.obj;
									Token op3 = (Token)$3.obj;
											  
									//System.out.println("\n TERCETO RESTA simple ->  ( + , "+op1.getLexema()+" , "+op3.getLexema()+" ) \n");
											  
											  
									Terceto ter = new Terceto(this.nro_terceto, "-" , op1.getLexema(), op3.getLexema());
									this.lista.agregarTerceto(ter); 
									
									pos_ultimo_terceto = this.nro_terceto;
											  
									//this.lista.mostrarTercetos();
											  
									this.nro_terceto++;
									//apunto a terceto!?
									$$.obj = ter ;
									this.isToken=false;
											
								  }
								  
								  
								  if (obj1 & !obj2) { //primero Token y el 2do Terceto
								  	Token op3 = (Token)$1.obj;
	 								String pos_str = "["+pos_ultimo_terceto+"]";
	 	 								  	 
	 	 							//System.out.println("\n TERCETO RESTA entre Token y TERCETO  ->  (  +  ,  "+pos_str + "  ,  " + op3.getLexema()+" ) \n");
	 	 	 								    
	 	 	 						Terceto ter = new Terceto(this.nro_terceto, "-", pos_str, op3.getLexema());
	 	 							this.lista.agregarTerceto(ter); 
	 	 									   
	 	 							//this.lista.mostrarTercetos();
	 	 									   	
	 	 							pos_ultimo_terceto = this.nro_terceto;
	 	 									   	
	 	 							this.nro_terceto++;
	 	 							
	 	 							$$.obj = ter ;
									this.isToken=false;
									
								  }
								  
								  
								  if (!obj1 & obj2) { //primero Terceto y el 2do Token
								  	Token op3 = (Token)$3.obj;
	 								String pos_str = "["+pos_ultimo_terceto+"]";
	 	 								  	 
	 	 							//System.out.println("\n TERCETO RESTA entre TERCETO y Token ->  (  *  ,  "+pos_str + "  ,  " + op3.getLexema());//op3.getOperando2());
	 	 	 								    
	 	 	 						Terceto ter = new Terceto(this.nro_terceto, "-", pos_str, op3.getLexema());//op3.getOperando2());
	 	 							this.lista.agregarTerceto(ter); 
	 	 									   
	 	 							//this.lista.mostrarTercetos();
	 	 									   	
	 	 							pos_ultimo_terceto = this.nro_terceto;
	 	 									   	
	 	 							this.nro_terceto++;
	 	 							
	 	 							$$.obj = ter ;
									this.isToken=false;
											   
								  }
								  
								  
								  if (!obj1 & !obj2) {
									  //OPERACION ENTRE 2 TERCETOS!
	 								      		
	 								  Terceto op3 = (Terceto)$3.obj;
	
		 								      
		 							  String pos_terc1 = "["+(pos_ultimo_terceto-1)+"]";
		 									   	
		 							  String pos_terc2 = "["+pos_ultimo_terceto+"]";
		 									  	
		 									  	 
		 							  //System.out.println("\n CREA TERCETO RESTA con TERCETO y TERCETO ->  (  >  ,  "+pos_terc1 + "  ,  " + pos_terc2+" ) \n");//op3.getOperando2());
		 	 								    
		 	 							    
		 	 						  Terceto ter = new Terceto(this.nro_terceto, "-", pos_terc1, pos_terc2);//op3.getOperando2());
		 							  this.lista.agregarTerceto(ter); 
		 									   	
		 							  //this.lista.mostrarTercetos();
		 									   	
		 							  pos_ultimo_terceto = this.nro_terceto;
		 									   	
		 						      this.nro_terceto++;
									
									  $$.obj = ter ;
									  this.isToken=false;
																		  
								  }
							}
			      		
	  	  
	  	  | termino  {
	  	  			$$=$1;
	  	  			//aca capaz que tengo que poner que isToken=true
	  	  			 }
	  	  			
	  	  			 
	  	  | CADENA {System.out.println("EXPRESION -> CADENA ");}
	  	  ;



termino : termino '*' factor {
							 
							  String tipo_obj1 = $1.obj.toString();
							  String tipo_obj2 = $3.obj.toString();
							  boolean obj1 = false;
							  boolean obj2 = false;
							  
							  if (tipo_obj1.substring(18,23).equals("Token")){
							  	obj1 = true;	
							  }
							  
							  if (tipo_obj2.substring(18,23).equals("Token")){
							  	obj2 = true;	
							  }
							  
							  
							  if (obj1 && obj2) { //si son los dos Tokens

							  	Token op1 = (Token)$1.obj;
							  	//Token op2 = (Token)$2.obj;
								Token op3 = (Token)$3.obj;
										  
								//System.out.println("\n TERCETO MULTIPLICACION simple ->  (  *  , "+op1.getLexema()+" , "+op3.getLexema()+" ) \n");
										  
										  
								Terceto ter = new Terceto(this.nro_terceto, "*" , op1.getLexema(), op3.getLexema());
								this.lista.agregarTerceto(ter); 
								pos_ultimo_terceto = this.nro_terceto;
										  
								//this.lista.mostrarTercetos();
										  
								this.nro_terceto++;
								//apunto a terceto!?
								$$.obj = ter ;
								this.isToken=false;
										
							  }
							  
							  
							  if (obj1 & !obj2) { //primero Token y el 2do Terceto
							  	Token op3 = (Token)$1.obj;
 								String pos_str = "["+pos_ultimo_terceto+"]";
 	 								  	 
 	 							//System.out.println("\n CREA TERCETO MULTIPLICACION entre Token y TERCETO  ->  (  *  ,  "+pos_str + "  ,  " + op3.getLexema());//op3.getOperando2());
 	 	 								    
 	 	 						Terceto ter = new Terceto(this.nro_terceto, "*", pos_str, op3.getLexema());//op3.getOperando2());
 	 							this.lista.agregarTerceto(ter); 
 	 									   
 	 							//this.lista.mostrarTercetos();
 	 									   	
 	 							pos_ultimo_terceto = this.nro_terceto;
 	 									   	
 	 							this.nro_terceto++;
 	 							
 	 							$$.obj = ter ;
								this.isToken=false;
								
							  }
							  
							  
							  if (!obj1 & obj2) { //primero Terceto y el 2do Token
							  	Token op3 = (Token)$3.obj;
 								String pos_str = "["+pos_ultimo_terceto+"]";
 	 								  	 
 	 							//System.out.println("\n CREA TERCETO MULTIPLICACION entre TERCETO y Token ->  (  *  ,  "+pos_str + "  ,  " + op3.getLexema());//op3.getOperando2());
 	 	 								    
 	 	 						Terceto ter = new Terceto(this.nro_terceto, "*", pos_str, op3.getLexema());
 	 							this.lista.agregarTerceto(ter); 
 	 									   
 	 							//this.lista.mostrarTercetos();
 	 									   	
 	 							pos_ultimo_terceto = this.nro_terceto;
 	 									   	
 	 							this.nro_terceto++;
 	 							
 	 							$$.obj = ter ;
								this.isToken=false;
										   
							  }
							  
							  
							  if (!obj1 & !obj2) {
								  //OPERACION ENTRE 2 TERCETOS!
 								      		
 								  Terceto op3 = (Terceto)$3.obj;

	 								      
	 							  String pos_terc1 = "["+(pos_ultimo_terceto-1)+"]";
	 									   	
	 							  String pos_terc2 = "["+pos_ultimo_terceto+"]";
	 									  	
	 									  	 
	 							  //System.out.println("\n TERCETO MULTIPLICACION con TERCETO y TERCETO ->  (  *  ,  "+pos_terc1 + "  ,  " + pos_terc2+" ) \n");//op3.getOperando2());
	 	 								    
	 	 							    
	 	 						  Terceto ter = new Terceto(this.nro_terceto, "*", pos_terc1, pos_terc2);
	 							  this.lista.agregarTerceto(ter); 
	 									   	
	 							  //this.lista.mostrarTercetos();
	 									   	
	 							  pos_ultimo_terceto = this.nro_terceto;
	 									   	
	 						      this.nro_terceto++;
								
								  $$.obj = ter ;
								  this.isToken=false;
																  
							  }
							  this.isToken=false;
							  
							  }
		
		
		| termino '/' factor {
							  String tipo_obj1 = $1.obj.toString();
							  String tipo_obj2 = $3.obj.toString();
							  boolean obj1 = false;
							  boolean obj2 = false;
							  
							  if (tipo_obj1.substring(18,23).equals("Token")){
							  	obj1 = true;	
							  }
							  
							  if (tipo_obj2.substring(18,23).equals("Token")){
							  	obj2 = true;	
							  }
							  
							  
							  if (obj1 && obj2) { //si son los dos Tokens

							  	Token op1 = (Token)$1.obj;
							  	//Token op2 = (Token)$2.obj;
								Token op3 = (Token)$3.obj;
										  
								//System.out.println("\n TERCETO DIVISION simple ->  (  *  , "+op1.getLexema()+" , "+op3.getLexema()+" ) \n");
										  
										  
								Terceto ter = new Terceto(this.nro_terceto, "/" , op1.getLexema(), op3.getLexema());
								this.lista.agregarTerceto(ter); 
								pos_ultimo_terceto = this.nro_terceto;
										  
								//this.lista.mostrarTercetos();
										  
								this.nro_terceto++;
								//apunto a terceto!?
								$$.obj = ter ;
								this.isToken=false;
										
							  }
							  
							  
							  if (obj1 & !obj2) { //primero Token y el 2do Terceto
							  	Token op3 = (Token)$1.obj;
 								String pos_str = "["+pos_ultimo_terceto+"]";
 	 								  	 
 	 							//System.out.println("\n CREA TERCETO DIVISION entre Token y TERCETO  ->  (  *  ,  "+pos_str + "  ,  " + op3.getLexema());//op3.getOperando2());
 	 	 								    
 	 	 						Terceto ter = new Terceto(this.nro_terceto, "/", pos_str, op3.getLexema());//op3.getOperando2());
 	 							this.lista.agregarTerceto(ter); 
 	 									   
 	 							//this.lista.mostrarTercetos();
 	 									   	
 	 							pos_ultimo_terceto = this.nro_terceto;
 	 									   	
 	 							this.nro_terceto++;
 	 							
 	 							$$.obj = ter ;
								this.isToken=false;
								
							  }
							  
							  
							  if (!obj1 & obj2) { //primero Terceto y el 2do Token
							  	Token op3 = (Token)$3.obj;
 								String pos_str = "["+pos_ultimo_terceto+"]";
 	 								  	 
 	 							//System.out.println("\n TERCETO DIVISION entre TERCETO y Token ->  (  *  ,  "+pos_str + "  ,  " + op3.getLexema());//op3.getOperando2());
 	 	 								    
 	 	 						Terceto ter = new Terceto(this.nro_terceto, "/", pos_str, op3.getLexema());
 	 							this.lista.agregarTerceto(ter); 
 	 									   
 	 							//this.lista.mostrarTercetos();
 	 									   	
 	 							pos_ultimo_terceto = this.nro_terceto;
 	 									   	
 	 							this.nro_terceto++;
 	 							
 	 							$$.obj = ter ;
								this.isToken=false;
										   
							  }
							  
							  
							  if (!obj1 & !obj2) {
								  //OPERACION ENTRE 2 TERCETOS!
 								      		
 								  Terceto op3 = (Terceto)$3.obj;

	 								      
	 							  String pos_terc1 = "["+(pos_ultimo_terceto-1)+"]";
	 									   	
	 							  String pos_terc2 = "["+pos_ultimo_terceto+"]";
	 									  	
	 									  	 
	 							  //System.out.println("\n TERCETO DIVISION con TERCETO y TERCETO ->  (  *  ,  "+pos_terc1 + "  ,  " + pos_terc2+" ) \n");//op3.getOperando2());
	 	 								    
	 	 							    
	 	 						  Terceto ter = new Terceto(this.nro_terceto, "/", pos_terc1, pos_terc2);
	 							  this.lista.agregarTerceto(ter); 
	 									   	
	 							  //this.lista.mostrarTercetos();
	 									   	
	 							  pos_ultimo_terceto = this.nro_terceto;
	 									   	
	 						      this.nro_terceto++;
								
								  $$.obj = ter ;
								  this.isToken=false;
																  
							  }
							  this.isToken=false;
							  	
							  }
							  
		
		| factor {
				  $$=$1; 	// termino.ptr = factor.ptr
				 }
		
		//| CADENA //{System.out.println("termino -> CADENA! ");
		;
		
		
		
factor : CTE {Token factor = (Token)$$.obj; 
	   		  //System.out.println("\n Llega CTE  "+ factor.getLexema() +"  la apunto con $$?? \n");
	   		  $$=$1;
	   		  // isToken=true;
	   		  }
       
       | '-'CTE {//System.out.println("CTE negativa! \n"); 
       			 Token op1 = (Token)$1.obj;
				 //int linea = op1.getNroLinea();
				 Token op2 = (Token)$2.obj;
				 //System.out.println("\n Sintactico  ->  Linea: "+linea+"  ,  CTE NEGATIVA!  ->  "+op1.getLexema()+" "+op2.getLexema()+"\n");
				 } 
       			
       
	    | ID  {
	    		$$=$1;
       		  }
       		  
       //| CADENA //{System.out.println("llega CADENA! -> voy a regla factor "); 
       ;
	   	


%%
	   	


//CODE



AnalizadorLexico lexico;
TablaDeSimbolos tabla;
ListaTercetos lista;

String ambito;
String ambito_temporal;
String proc_declarado;
String proc_invocado;

boolean isToken=true;

int nro_terceto = 0;
int pos_ultimo_terceto = 0;

int pos_BF = 0;
int pos_BI = 0;

int pos_comienzo_loop = 0;
int pos_comienzo_condicion_loop=0;

int cant_invocaciones = 0;



void desapilarAmbito() {
	
	int corte = 0;
	
	for (int i = 0; i < this.ambito.length(); i++) {
		if (ambito.charAt(i) == '@') {
			corte = i;
			break;
		}
	}
	
	this.ambito = this.ambito.substring(corte+1, ambito.length());	
}


public void procedimientoInvocado(){
	//System.out.println("\n\n RESTO CANTIDAD DE INVOCACIONES -> "+this.cant_invocaciones);
	int cant_invoc = this.tabla.getSimbolo(this.proc_invocado).getCantInvocaciones();
	if (cant_invoc > 0 ){
		this.tabla.getSimbolo(this.proc_invocado).setCantInvocaciones(cant_invoc-1);
	} else {
	 	System.out.println("ERROR -> ya invocaste muchas veces...");
	}
	this.proc_invocado = "";
}


public void setCantidadInvocaciones(){

	this.tabla.getSimbolo(this.proc_declarado).setCantInvocaciones(this.cant_invocaciones);
	
	this.proc_declarado = "";
	this.cant_invocaciones = 0;
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
			//System.out.println("\n YA EXISTE TOKEN  "+ token.getLexema() +"  EN TSYM. apunto al simbolo en la tabla \n");
			token = tabla.getSimbolo(token.getLexema());
		}
		tabla.addToken(token);
		
	    yylval = new ParserVal(token); //var para obtener el token de la tabla
	    return token.getIdTipo(); //acceso a la entrada que devolvumos
	}
	return 0;
}





public static void main(String args[]) {
 	//String direccion_codigo = "casos_prueba_tercetos.txt";
	//String direccion_codigo = "casos_prueba_simple.txt";
	String direccion_codigo = "casos_prueba_filminas.txt";
			
			
 	AnalizadorLexico al = new AnalizadorLexico(direccion_codigo);
	al.abrirCargarArchivo();
	
	TablaDeSimbolos tds = new TablaDeSimbolos();
	
	String a = "main";
	
	ListaTercetos l = new ListaTercetos();
	
	
	Parser par = new Parser(false, al, tds, l, a);
 	par.yyparse();
 	
 	
 	tds.removerTokensInvalidos();
 	
 	tds.mostrarSimbolos();
 	
 	l.mostrarTercetos();
 	
 	
 	GeneradorCodigo gc = new GeneradorCodigo(tds, l);
 	
 	gc.generarCodigo();
 	
 	gc.crearArchivoAssembler();
 	
 	gc.mostrarCodigoAssembler();
 	
 
 	gc.generarEstructuraProgramaAsm();
 	
 	gc.crearArchivoAssembler();
}