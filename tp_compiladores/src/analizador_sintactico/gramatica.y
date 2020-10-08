%{
	package analizador_sintactico;
	import analizador_lexico.*;
%}

%token
	ID
	CTE
	CADENA
	/* Palabras reservadas */
	IF
	ELSE
	END_IF
	PROC
	NI
	REF
	OUT
	/* Tipo de Constantes */
		
	/* Comparadores */
	MAYORIGUAL
	MENORIGUAL
	IGUAL
	DISTINTO
	EOF
%%

programa: lista_de_sentencias {System.out.println("TERMINO GRAMATICA");}
		;


lista_de_sentencias: 	sentencia
					|	lista_de_sentencias sentencia
					;


sentencia: sentencia_declarativa
		 | sentencia_ejecutable
		 ;


sentencia_declarativa:	declaracion_de_variable ';'
					  |	declaracion_de_procedimiento ';'
					  ;
			
					  
declaracion_de_variable: tipo lista_de_variables {System.out.println("LINEA "+((Token)$1.obj).getNroLinea())+": DECLARACION DE VARIABLE");}
					   ;


declaracion_de_procedimiento: PROC ID '(' lista_de_parametros ')' NI '=' CTE '{' cuerpo_del_procedimiento '}' {System.out.println("LINEA "+((Token)$1.obj).getNroLinea())+": DECLARACION DE PROCEDIMIENTO CON PARAMETROS");}
							| PROC ID '(' ')' NI '=' CTE '{' cuerpo_del_procedimiento '}' {System.out.println("LINEA "+((Token)$1.obj).getNroLinea())+": DECLARACION DE PROCEDIMIENTOS SIN PARAMETROS");}
							;
			
			
cuerpo_del_procedimiento: lista_de_sentencias
						| lista_de_sentencias declaracion_de_procedimiento
						;			

		 		
lista_de_parametros: parametro_declarado ',' parametro_declarado ',' parametro_declarado
				   | parametro_declarado ',' parametro_declarado
				   | parametro_declarado 
				   ;


parametro_declarado: tipo ID
	 			   | REF tipo ID
				   ;


lista_de_variables: lista_de_variables ',' ID
				  | ID
				  ;


sentencia_ejecutable  : asignacion ';'
				      | clausula_de_seleccion ';'
				 	  | sentencia_de_control ';'
				 	  | sentencia_de_salida ';'
				      | invocacion ';'
					  ;
					  
					
clausula_de_seleccion: IF '(' condicion ')' bloque_de_sentencias ELSE bloque_de_sentencias END_IF {System.out.println("LINEA "+((Token)$1.obj).getNroLinea())+": CLAUSULA DE SELECCION IF ELSE END_IF");}					
					 | IF '(' condicion ')' bloque_de_sentencias END_IF {System.out.println("LINEA "+((Token)$1.obj).getNroLinea())+": CLAUSULA DE SELECCION IF END_IF");}
					 ;
						


comparador : MAYORIGUAL
		   | MENORIGUAL
		   | IGUAL
		   | DISTINTO
		   | '>'
		   | '<'
		   ; 
		
		 
bloque_de_sentencias: sentencia
					| '{' lista_de_sentencias '}'
					;
					
					
sentencia_de_salida: OUT '(' CADENA ')' {System.out.println("LINEA "+((Token)$1.obj).getNroLinea())+": SENTENCIA DE SALIDA");}
					;
					
					
invocacion: ID '(' parametro_ejecutable ')' {System.out.println("LINEA "+((Token)$1.obj).getNroLinea())+": INVOCACION");}
		  ;
		  
		  
parametro_ejecutable: ID
		 			| parametro_ejecutable ',' ID
					;
		 			
					
sentencia_de_control: LOOP bloque_de_sentencias UNTIL '(' condicion ')' {System.out.println("LINEA "+((Token)$1.obj).getNroLinea())+": SENTENCIA DE CONTROL LOOP UNTIL");}
					;
					
					
condicion: FLOAT '(' expresion ')' comparador FLOAT '(' expresion ')'
		 ;
										
					
asignacion: ID '=' FLOAT '(' expresion ')'
 		  ;
					

expresion : expresion '+' termino
		  | expresion '-' termino
		  | termino
		  ;


termino : termino '*' factor
		| termino '/' factor
		| factor
		;
				
		
factor : CTE
	   | '-' CTE		{System.out.println("LINEA "+((Token)$1.obj).getNroLinea())+": CONSTANTE NEGATIVA");
	   					 actualizarTablaNegativo(((Token)$2.obj).getLexema());
	   					} 
	   | ID 
	   ;
	   

tipo:	INTEGER
	|	FLOAT
	;
	
		   
%%


/**/


AnalizadorLexico lexico;
TablaSimbolos ts;
TablaTokens tt;

public Parser(TablaTokens tt, TablaSimbolos ts) {
    lexico = new AnalizadorLexico(tt, ts);
	this.ts = ts;
}

private int yylex() {
	Token token=lexico.getToken();

	if (token!=null){
	    yylval = new ParserVal(token);
	    return token.getIdTipo();
	}
	return 0;
}


public int Parse(){
	return yyparse();
}

public AnalizadorLexico getAnalizadorLexico(){
	return lexico;
}


public void actualizarTablaNegativo(String lexema){
/*	
	if (ts.getSimbolo(lexema).getReferencias() > 1) {
		ts.getSimbolo(lexema).decrementarReferencias();
		Simbolo s = new Simbolo("-"+lexema, getSimbolo(lexema).getidToken(), getSimbolo(lexema).getTipo(), 1); 
		ts.add(s);
	}
	else {
		Simbolo s = new Simbolo("-"+lexema, getSimbolo(lexema).getidToken(), getSimbolo(lexema).getTipo(), 1);
		ts.eliminarSimbolo(lexema);
		ts.add(s);
	}
*/
}
	   	