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


lista_de_sentencias : sentencia {this.deshacer=false;}
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
												   
												    boolean existeVar = tabla.existe(variable.getLexema());
								  				    //tabla.mostrarSimbolos();
								  				   
								  				   
												    String nombre = variable.getLexema()+"@"+ambito;
													   
												    variable.setLexema(nombre);
													//name mangling! cambiar variable por variable@ambito
													  
													variable.setTipoVar(tipo.getLexema());
													  
													variable.setUso("variable");
													   
												}
				        ;


		


declaracion_de_procedimiento : declaracion_PROC_ID lista_parametros_PROC cant_invocaciones_PROC cuerpo_PROC {																																																					
																											//desapilo ambito temporal de ambito
																																																					
					   																							
									  																		Terceto ter = new Terceto(this.nro_terceto, "RET", this.proc_declarado, "--");
	 									   																	this.lista.agregarTerceto(ter); 
	 									   	
	 									   																	this.nro_terceto++;
																												
																											System.out.println(this.proc_declarado+" , "+this.cant_invocaciones);
	 									   																	
	 									   																	if (this.proc_declarado != "" ) {
	 									   																		this.setCantidadInvocaciones();/*this.proc_declarado, this.cant_invocaciones);*/
	 									   																	}									
																											
																											desapilarAmbito();
						   																						
																											
																											//this.tabla.mostrarSimbolos();
																											
																									}
	


declaracion_PROC_ID : PROC ID {											  
 								Token id = (Token)$2.obj ;
 								
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
		    								//desapilarAmbito();
		    							   }
																												
				    ;


parametro_declarado : tipo ID {
																
				    			Token tipo = (Token)$1.obj;
				    			Token id = (Token)$2.obj;
				    			
				    			String nombre = "";
				    			
				    			if (tabla.correctamenteDefinido(id) ){
				    				
				    				String nom = "";
				    				for (int i=id.getLexema().length()-1; i > 0 ;i--) {
				    					if (id.getLexema().charAt(i) == '@') {
				    						nom = id.getLexema().substring(0, i);
				    					}
				    				}
				    				
				    				nombre = nom+"@"+ambito;
				    				
				    				Token param = new Token(nombre, id.getTipo(), id.getNroLinea(), tipo.getLexema());
									
									
				    				param.setTipoVar(tipo.getLexema());
									
									param.setUso("parametro");					  			   
								    					
					    			tabla.addToken(param);
					    			
				    				
				    			} else {
				    				
				    				/*
				    				String nom = "";
				    				for (int i=id.getLexema().length()-1; i > 0 ;i--) {
				    					if (id.getLexema().charAt(i) == '@') {
				    						nom = id.getLexema().substring(0, i);
				    					}
				    				}
				    				System.out.println("nom -> "+nom);
				    				nombre=nom;
				    				*/
				    				
				    				nombre = id.getLexema()+"@"+ambito;
				    				
				    				id.setPtr(nombre);
				    				
				    				Token param = new Token(nombre, id.getTipo(), id.getNroLinea(), tipo.getLexema());
									
									param.setTipoVar(tipo.getLexema());

									param.setUso("parametro");					  
														
					    			tabla.addToken(param);
					    			
					    			//$$ = param;
				    				}
				    			
				    			}
								
								
				    | REF tipo ID { 
				    			    
				    			    Token tipo = (Token)$2.obj;
				    			    Token id = (Token)$3.obj;
				    				
				    				
				    				//aca modifico el token de la TSym
				    				
				    				//String nombre = id.getLexema()+"@"+ambito;
									
									$$=$3;
													   
									//puedpo modificar el puntero..
									//id.setPtr();
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
								  
								  Token op = (Token)$2.obj;
								
								  $$ = $3; //desde lado izq apunto a la expresion
								  //$$.obj = $3 rompe todo				
								  
								  boolean esValido = tabla.correctamenteDefinido(id);
								  
								  if (esValido) {
									  if (isToken) { 
									  	if (!deshacer){ //si la operacion es valida
										  		
										  	//System.out.println("Asignacion valida! le agrego a ID el ptr a la direccion en la TSym ");
										  	id.setPtr(((Token)$3.obj).getLexema());
										  		
										  	Terceto ter = new Terceto(this.nro_terceto, op.getLexema(), id.getLexema(), ((Token)$3.obj).getLexema());
		 									this.lista.agregarTerceto(ter); 
		 									   	
		 									//this.lista.mostrarTercetos();
		 									   	
		 									this.nro_terceto++;
		 									
										} else {
										
											//System.out.println("DESHACER ");
									  		deshacer=false;
									  		break;
										}   	
									  	
									} else {
									  
								      	String pos_str = "["+pos_ultimo_terceto+"]";
									  	
									  	//System.out.println("\n CREO TERCETO ASIGNACION con terceto ->  ( "+op.getLexema()+" , "+ id.getLexema()+" , "+pos_str+" ) \n");
									  	
									  	Terceto ter = new Terceto(this.nro_terceto, "=", id.getLexema(), pos_str);
	 									this.lista.agregarTerceto(ter); 
	 								  	
	 								  	//this.lista.mostrarTercetos();
	 								   	
	 								   	this.nro_terceto++;
									  	
									  }
									  
									isToken=true;
									
								 } else {
								 	
								 	System.out.println("Linea "+op.getNroLinea()+" -> ERROR de asignacion ");
								 	System.out.println("Variable "+id.getLexema()+" no esta correctamente definida \n");
									break;
								 	
							}
		   				}
		   
		   
		   | ID '=' '('tipo')' expresion {
		   								
								  		  Token id = (Token)$1.obj;
										  
										  //System.out.println("\n ASIGNACION con CONVERSION EXPLICITA! "+ id.getLexema() +"\n"); 
										  Token op = (Token)$2.obj;
										  
										  Token tipo = (Token)$4.obj;
										  
										  $$ = $6; //desde lado izq apunto a la expresion
								  		  
										  boolean esValido = tabla.correctamenteDefinido(id);
										  
										  
										  if (esValido) {
											  if (isToken) { 
											  	if (!deshacer){ //si la operacion es valida
												  		
												  	//System.out.println("Asignacion EXPLICITA valida! le agrego a ID el ptr a la direccion en la TSym ");
												  	id.setPtr(((Token)$6.obj).getLexema());
												  		
												  	Terceto ter = new Terceto(this.nro_terceto, "=", id.getLexema(), ((Token)$6.obj).getLexema());
				 									this.lista.agregarTerceto(ter); 
				 									   	
				 									//this.lista.mostrarTercetos();
				 									   	
				 									this.nro_terceto++;
												} else {
													System.out.println("Linea "+op.getNroLinea()+" -> ERROR! Tipos incompatibles en conversion EXPLICITA \n");
											  		deshacer=false;
											  		break;
												}   	
											  	
											} else {
											  
										      	String pos_str = "["+pos_ultimo_terceto+"]";
											  	
											  	//System.out.println("\n CREO TERCETO ASIGNACION EXPLICITA con terceto ->  ( "+op.getLexema()+" , "+ id.getLexema()+" , "+pos_str+" ) \n");
											  	
											  	Terceto ter = new Terceto(this.nro_terceto, "=", id.getLexema(), pos_str);
			 									this.lista.agregarTerceto(ter); 
			 								  	
			 								  	//this.lista.mostrarTercetos();
			 								   	
			 								   	this.nro_terceto++;
											  	
											  }
											  
											isToken=true;
											
										 } else {
										 	
										 	System.out.println("Linea "+id.getNroLinea()+" -> ERROR de asignacion explicita");
									}
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
									//System.out.println("\n COMPLETA TERCETO INCOMPLETO  \n");
								    
								    //System.out.println("\n CREA TERCETO BI (salto incondicional) \n");
								    
								    
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
 								    
 								    //System.out.println("\n CREA EL TERCETO SALTO por FALSO ->  ( BF , "+pos_str +" , "+ pos_sgte +" )  \n");				
 								  	  	 
 								  	    
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
								  	
								  			 	System.out.println("\n ERROR -> PROCEDIMIENTO "+id.getLexema()+" no existe \n");
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

															   
								   String tipo_obj1 = $1.obj.toString();
								   String tipo_obj2 = $3.obj.toString();
								   boolean obj1 = false;
								   boolean obj2 = false;
								   
								   Token op = (Token)$2.obj;
								   
								   
								   //checkeo compatibilidad de tipos
								   
								   //si el obj1 es un Token -> true
								   if (tipo_obj1.substring(18,23).equals("Token")){
								     obj1 = true;	
								   }
								   
								   //si objeto2 es un Token -> true
								   if (tipo_obj2.substring(18,23).equals("Token")){
								  	 obj2 = true;	
								   }
								  
								   
								   //si son los dos Token
								   if (obj1 && obj2) { 
									
									 
									 Token op1 = (Token)$1.obj;
								  	 Token op3 = (Token)$3.obj;
										
									 
								  
								  	 if (tabla.correctamenteDefinido(op3)){
										String tipo_op1 = op1.getTipo();
										String tipo_op2 = op3.getTipo();
										
										//si los tokens son 2 variables
										
										
										//operacion entre CTE e ID del mismo tipo
										if (tipo_op1.equals("CTE") && tipo_op2.equals("ID")) {
											
											Token ref_op1 = tabla.getSimbolo(op1.getPtr());
											String tipo_ref_op1 = "";
												
											if (ref_op1 != null) { /*si la tiene, la copio*/
												tipo_ref_op1 = ref_op1.getTipoVar();
											}
													
											
											Token ref_op2 = tabla.getSimbolo(op3.getPtr());
											String tipo_ref_op2 = "";
												
											if (ref_op2 != null) {
												tipo_ref_op2=ref_op2.getTipoVar();
											}
											
											
											if (!op1.getTipoVar().equals(op3.getTipoVar())) {
												System.out.println("Linea "+op.getNroLinea()+" -> ERROR de tipos en la SUMA"); 
												System.out.println("No se puede sumar "+op1.getLexema()+" de tipo "+tipo_ref_op2+" a "+op3.getLexema()+" de tipo "+tipo_ref_op1+" \n");
												this.deshacer=true; 
												break;
												
											} else {
											
												Terceto ter = new Terceto(this.nro_terceto, "+" , op1.getLexema(), op3.getLexema());
												this.lista.agregarTerceto(ter); 
													
												pos_ultimo_terceto = this.nro_terceto;
															  
												/*this.lista.mostrarTercetos();*/
															  
												this.nro_terceto++;
													
												/*apunto a terceto*/
												yyval.obj = ter ;
													
												this.isToken=false; /*ya no soy un token*/
												break;
											}
											
										}	
										
										
										
										//operacion entre ID e ID
										if (tipo_op1.equals("ID") && tipo_op2.equals("ID")) {
											
											//System.out.println("Multiplicacion entre dos variables ID ! "+op1.getLexema()+" * "+op3.getLexema());
											
											//checkeo si el operador 1 tiene alguna referencia	
											Token ref_op1 = tabla.getSimbolo(op1.getPtr());
											String tipo_ref_op1 = "";
												
											if (ref_op1 != null) { //si la tiene, la copio
												tipo_ref_op1 = ref_op1.getTipoVar();
											}
													
												
											//checkeo si el operador 2 tiene alguna referencia	
											Token ref_op2 = tabla.getSimbolo(op3.getPtr());
											String tipo_ref_op2 = "";
												
											if (ref_op2 != null) {
												tipo_ref_op2=ref_op2.getTipoVar();
											}
													
											Token nuevo = tabla.getSimbolo(op1.getPtr());
											
											//if (nuevo.getTipo().equals("ID") && tabla.correctamenteDefinido(nuevo)) {
											if (tabla.correctamenteDefinido(nuevo)) {
											
														
												if (!nuevo.getTipoVar().equals(op3.getTipoVar())) {
													System.out.println("Linea "+op.getNroLinea()+" -> ERROR de tipos en la SUMA"); 
													System.out.println("No se puede sumar "+op1.getLexema()+" de tipo "+tipo_ref_op2+" a "+op3.getLexema()+" de tipo "+tipo_ref_op1+" \n");
													this.deshacer=true; 
													break;
													
												} else {
														
													//Terceto ter = new Terceto(this.nro_terceto, "+" , nuevo.getLexema(), op3.getLexema());
													//Terceto ter = new Terceto(this.nro_terceto, "+" , op1.getLexema(), op3.getLexema());
													
													Terceto ter = null;
													if (nuevo.getTipo().equals("CTE")) {
														ter = new Terceto(this.nro_terceto, "+" , op1.getLexema(), op3.getLexema());
														
													} else {
														ter = new Terceto(this.nro_terceto, "+" , nuevo.getLexema(), op3.getLexema());
													}
													
													this.lista.agregarTerceto(ter); 
															
													pos_ultimo_terceto = this.nro_terceto;
																	  
													/*this.lista.mostrarTercetos();*/
																	  
													this.nro_terceto++;
															
													/*apunto a terceto*/
													yyval.obj = ter ;
															
													this.isToken=false; /*ya no soy un token*/
													break;			
													}
													}			
												}
											} else {
	
												//si no esta bien definido,pero tiene un puntero
												if (tabla.correctamenteDefinido(tabla.getSimbolo(op3.getPtr()))) {
													
													Terceto ter = new Terceto(this.nro_terceto, "+" , op1.getPtr(), op3.getPtr());
													this.lista.agregarTerceto(ter); 
														
													pos_ultimo_terceto = this.nro_terceto;
																  
													/*this.lista.mostrarTercetos();*/
																  
													this.nro_terceto++;
														
													/*apunto a terceto*/
													yyval.obj = ter ;
														
													this.isToken=false; /*ya no soy un token*/
													break;
													}
											}
										//}
								  }
								  
								  //si primero un Token y despues un Terceto
								  if (obj1 && !obj2) { 
								  
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
								  
								  
								  //primero Terceto (guardado en registro) y despues Token
								  if (!obj1 && obj2) { 
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
								  
								  
								  //SUMA entre 2 TERCETOS!
	 							  if (!obj1 && !obj2) {
									      		
	 								  Terceto op3 = (Terceto)$3.obj;
	
		 								      
		 							  String pos_terc1 = "["+(pos_ultimo_terceto-1)+"]";
		 									   	
		 							  String pos_terc2 = "["+pos_ultimo_terceto+"]";
		 									  	
		 									  	 
		 							  //System.out.println("\n CREA TERCETO SUMA con TERCETO y TERCETO ->  (  >  ,  "+pos_terc1 + "  ,  " + pos_terc2+" ) \n");//op3.getOperando2());
		 	 								    
		 	 							    
		 	 						  Terceto ter = new Terceto(this.nro_terceto, "+", pos_terc1, pos_terc2);//op3.getOperando2());
		 							  this.lista.agregarTerceto(ter); 
		 									   	
		 							  //this.lista.mostrarTercetos();
		 									   	
		 							  pos_ultimo_terceto = this.nro_terceto;
		 									   	
		 						      this.nro_terceto++;
									
									  $$.obj = ter ;
									  this.isToken=false;
																		  
								  }
							  
							}
	  	  
	  	  
	  	  
	  	  | expresion '-' termino {
								   String tipo_obj1 = $1.obj.toString();
								   String tipo_obj2 = $3.obj.toString();
								   boolean obj1 = false;
								   boolean obj2 = false;
								   
								   Token op = (Token)$2.obj;
								   
								   
								   //checkeo compatibilidad de tipos
								   
								   //si el obj1 es un Token -> true
								   if (tipo_obj1.substring(18,23).equals("Token")){
								     obj1 = true;	
								   }
								   
								   //si objeto2 es un Token -> true
								   if (tipo_obj2.substring(18,23).equals("Token")){
								  	 obj2 = true;	
								   }
								  
								  
								   if (obj1 && obj2) { //si son los dos Tokens
									
									 Token op1 = (Token)$1.obj;
								  	 Token op3 = (Token)$3.obj;
									
									 String tipo_op1 = op1.getTipo();
									 String tipo_op2 = op3.getTipo();
									
									 //si los tokens son 2 variables
									 if (tipo_op1.equals("ID") && tipo_op2.equals("ID")) {
										
										//System.out.println("Resta entre dos variables ID ! "+op1.getLexema()+" + "+op3.getLexema());
										
										//checkeo si el operador 1 tiene alguna referencia	
										Token ref_op1 = tabla.getSimbolo(op1.getPtr());
										String tipo_ref_op1 = "";
										
										if (ref_op1 != null) { //si la tiene, la copio
											tipo_ref_op1 = ref_op1.getTipoVar();
										}
											
										
										//checkeo si el operador 2 tiene alguna referencia	
										Token ref_op2 = tabla.getSimbolo(op3.getPtr());
										String tipo_ref_op2 = "";
										
										if (ref_op2 != null) {
											tipo_ref_op2=ref_op2.getTipoVar();
										}
											
												
										if (!op1.getTipoVar().equals(op3.getTipoVar())) {
											System.out.println("Linea "+op.getNroLinea()+" -> ERROR de tipos en la SUMA!"); 
											System.out.println("No se puede restar "+op1.getLexema()+" de tipo "+tipo_ref_op2+" a "+op3.getLexema()+" de tipo "+tipo_ref_op1+" \n");
											this.deshacer=true; 
											break;
										}
										
										
									}
									
									
									//si llego hasta aca la operacion entre tokens es valida
											  
									//System.out.println("\n TERCETO RESTA simple ->  ( - , "+op1.getLexema()+" , "+op3.getLexema()+" ) \n");
											  
											  
									Terceto ter = new Terceto(this.nro_terceto, "-" , op1.getLexema(), op3.getLexema());
									this.lista.agregarTerceto(ter); 
									
									pos_ultimo_terceto = this.nro_terceto;
											  
									//this.lista.mostrarTercetos();
											  
									this.nro_terceto++;
									
									//apunto a terceto
									$$.obj = ter ;
									
									this.isToken=false; //ya no soy un token
											
								  	}
								  
								  
								  
								  //si primero un Token y despues un Terceto
								  if (obj1 && !obj2) { 
								  
								  	Token op3 = (Token)$1.obj;
	 								String pos_str = "["+pos_ultimo_terceto+"]";
	 	 								  	 
	 	 							//System.out.println("\n TERCETO RESTA entre Token y TERCETO  ->  (  -  ,  "+pos_str + "  ,  " + op3.getLexema()+" ) \n");
	 	 	 								    
	 	 	 						Terceto ter = new Terceto(this.nro_terceto, "-", pos_str, op3.getLexema());
	 	 							this.lista.agregarTerceto(ter); 
	 	 									   
	 	 							//this.lista.mostrarTercetos();
	 	 									   	
	 	 							pos_ultimo_terceto = this.nro_terceto;
	 	 									   	
	 	 							this.nro_terceto++;
	 	 							
	 	 							$$.obj = ter ;
									this.isToken=false;
									
								  }
								  
								  
								  //primero Terceto (guardado en registro) y despues Token
								  if (!obj1 && obj2) { 
								    Token op3 = (Token)$3.obj;
	 								String pos_str = "["+pos_ultimo_terceto+"]";
	 	 								  	 
	 	 							//System.out.println("\n TERCETO RESTA entre TERCETO y Token ->  (  -  ,  "+pos_str + "  ,  " + op3.getLexema());//op3.getOperando2());
	 	 	 								    
	 	 	 						Terceto ter = new Terceto(this.nro_terceto, "-", pos_str, op3.getLexema());//op3.getOperando2());
	 	 							this.lista.agregarTerceto(ter); 
	 	 									   
	 	 							//this.lista.mostrarTercetos();
	 	 									   	
	 	 							pos_ultimo_terceto = this.nro_terceto;
	 	 									   	
	 	 							this.nro_terceto++;
	 	 							
	 	 							$$.obj = ter ;
									this.isToken=false;
											   
								  }
								  
								  
								  //RESTA entre 2 TERCETOS!
	 							  if (!obj1 && !obj2) {
									      		
	 								  Terceto op3 = (Terceto)$3.obj;
	
		 								      
		 							  String pos_terc1 = "["+(pos_ultimo_terceto-1)+"]";
		 									   	
		 							  String pos_terc2 = "["+pos_ultimo_terceto+"]";
		 									  	
		 									  	 
		 							  //System.out.println("\n CREA TERCETO RESTA con TERCETO y TERCETO ->  (  -  ,  "+pos_terc1 + "  ,  " + pos_terc2+" ) \n");//op3.getOperando2());
		 	 								    
		 	 							    
		 	 						  Terceto ter = new Terceto(this.nro_terceto, "-", pos_terc1, pos_terc2);
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
	  	  			 }
	  	  			
	  	  			 
	  	  | CADENA {System.out.println("EXPRESION CADENA ");}
	  	  ;



termino : termino '*' factor {
							  							  
							    String tipo_obj1 = $1.obj.toString();
							    String tipo_obj2 = $3.obj.toString();
							    boolean obj1 = false;
							    boolean obj2 = false;
								
								Token op = (Token)$2.obj;
								   
								   
								//checkeo compatibilidad de tipos
								   
								//si el obj1 es un Token -> true
								if (tipo_obj1.substring(18,23).equals("Token")){
									obj1 = true;	
								}
								   
								//si objeto2 es un Token -> true
								if (tipo_obj2.substring(18,23).equals("Token")){
								 obj2 = true;	
								}
								  
								  
								if (obj1 && obj2) { //si son los dos Tokens
									
									Token op1 = (Token)$1.obj;
								  	Token op3 = (Token)$3.obj;
									
									if (tabla.correctamenteDefinido(op3)){
										String tipo_op1 = op1.getTipo();
										String tipo_op2 = op3.getTipo();
										
										//si los tokens son 2 variables
										
										
										//operacion entre CTE e ID del mismo tipo
										if (tipo_op1.equals("CTE") && tipo_op2.equals("ID")) {
											
											Token ref_op1 = tabla.getSimbolo(op1.getPtr());
											String tipo_ref_op1 = "";
												
											if (ref_op1 != null) { /*si la tiene, la copio*/
												tipo_ref_op1 = ref_op1.getTipoVar();
											}
													
											
											Token ref_op2 = tabla.getSimbolo(op3.getPtr());
											String tipo_ref_op2 = "";
												
											if (ref_op2 != null) {
												tipo_ref_op2=ref_op2.getTipoVar();
											}
											
											
											if (!op1.getTipoVar().equals(op3.getTipoVar())) {
												System.out.println("Linea "+op.getNroLinea()+" -> ERROR de tipos en la MULTIPLICACION"); 
												System.out.println("No se puede multiplicar "+op1.getLexema()+" de tipo "+tipo_ref_op2+" a "+op3.getLexema()+" de tipo "+tipo_ref_op1+" \n");
												this.deshacer=true; 
												break;
												
											} else {
											
												Terceto ter = new Terceto(this.nro_terceto, "*" , op1.getLexema(), op3.getLexema());
												this.lista.agregarTerceto(ter); 
													
												pos_ultimo_terceto = this.nro_terceto;
															  
												/*this.lista.mostrarTercetos();*/
															  
												this.nro_terceto++;
													
												/*apunto a terceto*/
												yyval.obj = ter ;
													
												this.isToken=false; /*ya no soy un token*/
												break;
											}
											
										}	
										
										
										//operacion entre ID e ID
										if (tipo_op1.equals("ID") && tipo_op2.equals("ID")) {
											
										//System.out.println("Multiplicacion entre dos variables ID ! "+op1.getLexema()+" * "+op3.getLexema());
										
										//checkeo si el operador 1 tiene alguna referencia	
										Token ref_op1 = tabla.getSimbolo(op1.getPtr());
										String tipo_ref_op1 = "";
											
										if (ref_op1 != null) { //si la tiene, la copio
											tipo_ref_op1 = ref_op1.getTipoVar();
										}
												
											
										//checkeo si el operador 2 tiene alguna referencia	
										Token ref_op2 = tabla.getSimbolo(op3.getPtr());
										String tipo_ref_op2 = "";
											
										if (ref_op2 != null) {
											tipo_ref_op2=ref_op2.getTipoVar();
										}
												
													
										if (!op1.getTipoVar().equals(op3.getTipoVar())) {
												System.out.println("Linea "+op.getNroLinea()+" -> ERROR de tipos en la MULTIPLICACION"); 
												System.out.println("No se puede multiplicar "+op1.getLexema()+" de tipo "+tipo_ref_op2+" a "+op3.getLexema()+" de tipo "+tipo_ref_op1+" \n");
												this.deshacer=true; 
												break;
											
											} else {
												
												Terceto ter = new Terceto(this.nro_terceto, "*" , op1.getLexema(), op3.getLexema());
												this.lista.agregarTerceto(ter); 
													
												pos_ultimo_terceto = this.nro_terceto;
															  
												/*this.lista.mostrarTercetos();*/
															  
												this.nro_terceto++;
													
												/*apunto a terceto*/
												yyval.obj = ter ;
													
												this.isToken=false; /*ya no soy un token*/
												break;			
												}
												
											}
										}
									}
								  
								  
								  
								//si primero un Token y despues un Terceto
								if (obj1 && !obj2) { 
								  
									Token op3 = (Token)$1.obj;
	 								String pos_str = "["+pos_ultimo_terceto+"]";
	 	 								  	 
	 	 							//System.out.println("\n TERCETO MULTIPLICACION entre Token y TERCETO  ->  (  *  ,  "+pos_str + "  ,  " + op3.getLexema()+" ) \n");
	 	 	 								    
	 	 	 						Terceto ter = new Terceto(this.nro_terceto, "*", pos_str, op3.getLexema());
	 	 							this.lista.agregarTerceto(ter); 
	 	 									   
	 	 							//this.lista.mostrarTercetos();
	 	 									   	
	 	 							pos_ultimo_terceto = this.nro_terceto;
	 	 									   	
	 	 							this.nro_terceto++;
	 	 							
	 	 							$$.obj = ter ;
									this.isToken=false;
									
								  }
								  
								  
								//primero Terceto (guardado en registro) y despues Token
								if (!obj1 && obj2) { 
								    Token op3 = (Token)$3.obj;
	 								String pos_str = "["+pos_ultimo_terceto+"]";
	 	 								  	 
	 	 							//System.out.println("\n TERCETO MULTIPLICACION entre TERCETO y Token ->  (  *  ,  "+pos_str + "  ,  " + op3.getLexema());//op3.getOperando2());
	 	 	 								    
	 	 	 						Terceto ter = new Terceto(this.nro_terceto, "+", pos_str, op3.getLexema());//op3.getOperando2());
	 	 							this.lista.agregarTerceto(ter); 
	 	 									   
	 	 							//this.lista.mostrarTercetos();
	 	 									   	
	 	 							pos_ultimo_terceto = this.nro_terceto;
	 	 									   	
	 	 							this.nro_terceto++;
	 	 							
	 	 							$$.obj = ter ;
									this.isToken=false;
											   
								  }
								  
								  
								//MULTIPLICACION entre 2 TERCETOS!
	 							if (!obj1 && !obj2) {
									      		
	 								  Terceto op3 = (Terceto)$3.obj;
	
		 								      
		 							  String pos_terc1 = "["+(pos_ultimo_terceto-1)+"]";
		 									   	
		 							  String pos_terc2 = "["+pos_ultimo_terceto+"]";
		 									  	
		 									  	 
		 							  //System.out.println("\n CREA TERCETO MULTIPLICACION con TERCETO y TERCETO ->  (  >  ,  "+pos_terc1 + "  ,  " + pos_terc2+" ) \n");//op3.getOperando2());
		 	 								    
		 	 							    
		 	 						  Terceto ter = new Terceto(this.nro_terceto, "*", pos_terc1, pos_terc2);//op3.getOperando2());
		 							  this.lista.agregarTerceto(ter); 
		 									   	
		 							  //this.lista.mostrarTercetos();
		 									   	
		 							  pos_ultimo_terceto = this.nro_terceto;
		 									   	
		 						      this.nro_terceto++;
									
									  $$.obj = ter ;
									  this.isToken=false;
																		  
								  }
	
							  }
		
		
		| termino '/' factor {
							
	
							  String tipo_obj1 = $1.obj.toString();
							  String tipo_obj2 = $3.obj.toString();
							  boolean obj1 = false;
							  boolean obj2 = false;
							  Token op = (Token)$2.obj;
								   
								   
							  //checkeo compatibilidad de tipos
								   
							  //si el obj1 es un Token -> true
							  if (tipo_obj1.substring(18,23).equals("Token")){
							     obj1 = true;	
							   }
								   
							   //si objeto2 es un Token -> true
							   if (tipo_obj2.substring(18,23).equals("Token")){
							  	 obj2 = true;	
							   }
								  
								  
							   if (obj1 && obj2) { //si son los dos Tokens
									
								 Token op1 = (Token)$1.obj;
							  	 Token op3 = (Token)$3.obj;
									
								 String tipo_op1 = op1.getTipo();
								 String tipo_op2 = op3.getTipo();
									
								 //si los tokens son 2 variables
								 if (tipo_op1.equals("ID") && tipo_op2.equals("ID")) {
										
									//System.out.println("DIVISION entre dos variables ID ! "+op1.getLexema()+" / "+op3.getLexema());
										
									//checkeo si el operador 1 tiene alguna referencia	
									Token ref_op1 = tabla.getSimbolo(op1.getPtr());
									String tipo_ref_op1 = "";
										
									if (ref_op1 != null) { //si la tiene, la copio
										tipo_ref_op1 = ref_op1.getTipoVar();
									}
											
										
									//checkeo si el operador 2 tiene alguna referencia	
									Token ref_op2 = tabla.getSimbolo(op3.getPtr());
									String tipo_ref_op2 = "";
										
									if (ref_op2 != null) {
										tipo_ref_op2=ref_op2.getTipoVar();
									}
											
												
									if (!op1.getTipoVar().equals(op3.getTipoVar())) {
										System.out.println("Linea "+op.getNroLinea()+" -> ERROR de tipos en la SUMA!"); 
										System.out.println("No se puede dividir "+op1.getLexema()+" de tipo "+tipo_ref_op2+" a "+op3.getLexema()+" de tipo "+tipo_ref_op1+" \n");
										this.deshacer=true; 
										break;
									}
										
										
								}
									
									
								//si llego hasta aca la operacion entre tokens es valida
										  
								//System.out.println("\n TERCETO DIVISION simple ->  ( / , "+op1.getLexema()+" , "+op3.getLexema()+" ) \n");
											  
											  
								Terceto ter = new Terceto(this.nro_terceto, "/" , op1.getLexema(), op3.getLexema());
								this.lista.agregarTerceto(ter); 
									
								pos_ultimo_terceto = this.nro_terceto;
											  
								//this.lista.mostrarTercetos();
											  
								this.nro_terceto++;
									
								//apunto a terceto
								$$.obj = ter ;
									
								this.isToken=false; //ya no soy un token
											
							  	}
								  
								  
								  
							  //si primero un Token y despues un Terceto
							  if (obj1 && !obj2) { 
								  
							  	Token op3 = (Token)$1.obj;
	 							String pos_str = "["+pos_ultimo_terceto+"]";
	 	 								  	 
	 	 						//System.out.println("\n TERCETO DIVISION entre Token y TERCETO  ->  (  /  ,  "+pos_str + "  ,  " + op3.getLexema()+" ) \n");
	 	 	 								    
	 	 	 					Terceto ter = new Terceto(this.nro_terceto, "/", pos_str, op3.getLexema());
	 	 						this.lista.agregarTerceto(ter); 
	 	 									   
	 	 						//this.lista.mostrarTercetos();
	 	 									   	
	 	 						pos_ultimo_terceto = this.nro_terceto;
	 	 									   	
	 	 						this.nro_terceto++;
	 	 							
	 	 						$$.obj = ter ;
								this.isToken=false;
									
							  }
								  
								  
							  //primero Terceto (guardado en registro) y despues Token
							  if (!obj1 && obj2) { 
							    Token op3 = (Token)$3.obj;
	 							String pos_str = "["+pos_ultimo_terceto+"]";
	 	 								  	 
	 	 						//System.out.println("\n TERCETO DIVISION entre TERCETO y Token ->  (  /  ,  "+pos_str + "  ,  " + op3.getLexema());//op3.getOperando2());
	 	 	 								    
	 	 	 					Terceto ter = new Terceto(this.nro_terceto, "/", pos_str, op3.getLexema());//op3.getOperando2());
	 	 						this.lista.agregarTerceto(ter); 
	 	 									   
	 	 						//this.lista.mostrarTercetos();
	 	 									   	
	 	 						pos_ultimo_terceto = this.nro_terceto;
	 	 									   	
	 	 						this.nro_terceto++;
	 	 							
	 	 						$$.obj = ter ;
								this.isToken=false;
											   
							  }
								  
								  
							  //DIVISION entre 2 TERCETOS!
	 						  if (!obj1 && !obj2) {
									      		
	 							  Terceto op3 = (Terceto)$3.obj;
	
		 								      
		 						  String pos_terc1 = "["+(pos_ultimo_terceto-1)+"]";
		 									   	
		 						  String pos_terc2 = "["+pos_ultimo_terceto+"]";
		 									  	
		 									  	 
		 						  //System.out.println("\n CREA TERCETO DIVISION con TERCETO y TERCETO ->  (  /  ,  "+pos_terc1 + "  ,  " + pos_terc2+" ) \n");//op3.getOperando2());
		 	 								    
		 	 							    
		 	 					  Terceto ter = new Terceto(this.nro_terceto, "/", pos_terc1, pos_terc2);//op3.getOperando2());
		 						  this.lista.agregarTerceto(ter); 
		 									   	
		 						  //this.lista.mostrarTercetos();
		 									   	
		 						  pos_ultimo_terceto = this.nro_terceto;
		 									   	
		 					      this.nro_terceto++;
									
								  $$.obj = ter ;
								  this.isToken=false;
																		  
							  }
		
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
				 //System.out.println("\n ERROR! Linea: "+linea+"  ,  CTE NEGATIVA!  ->  "+op1.getLexema()+" "+op2.getLexema()+"\n");
				 } 
       			
       
	    | ID  {
	    		$$=$1;
       		  }
       		  
       //| CADENA //{System.out.println("llega CADENA "); 
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
//String lexemaAux;
//String tipoAux;

boolean isToken=true;
boolean deshacer=false;

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
	
	if (!this.ambito.contains("@")) {
		this.ambito = "main" ;
	} else {
		this.ambito = this.ambito.substring(corte+1, ambito.length());	
	}
	
	//this.ambito = this.ambito.substring(corte+1, ambito.length());	
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
	String direccion_codigo = "casos_prueba_simple.txt";
	//String direccion_codigo = "casos_prueba_filminas.txt";
	
	//String direccion_codigo = args[0];
	
	String nombre_file = direccion_codigo.substring(0, direccion_codigo.length()-4);		
			
			
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
 	
 	//gc.crearArchivoAssembler(nombre_file);
 	
 	//gc.mostrarCodigoAssembler();
 	
 
 	gc.generarEstructuraProgramaAsm();
 	
 	gc.crearArchivoAssembler(nombre_file);
}