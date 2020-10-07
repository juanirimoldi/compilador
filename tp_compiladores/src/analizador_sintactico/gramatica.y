%{
	//imports
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

programa: lista_de_sentencias //
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
			
					  
declaracion_de_variable: tipo lista_de_variables
					   ;


declaracion_de_procedimiento: PROC ID '(' lista_de_parametros ')' NI '=' CTE '{' cuerpo_del_procedimiento '}'
							| PROC ID '(' ')' NI '=' CTE '{' cuerpo_del_procedimiento '}'
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


tipo:	INTEGER
	|	FLOAT
	;


sentencia_ejecutable  : asignacion ','
				      | clausula_de_seleccion ','
				 	  | sentencia_de_control ','
				 	  | sentencia_de_salida ','
				      | invocacion ','
					  ;
					
clausula_de_seleccion: IF '(' condicion ')' bloque_de_sentencias ELSE bloque_de_sentencias END_IF					
					 | IF '(' condicion ')' bloque_de_sentencias END_IF
					 ;
						
condicion: expresion '>' expresion
		 | expresion '<' expresion
		 | expresion MAYORIGUAL expresion
		 | expresion MENORIGUAL expresion
		 | expresion IGUAL expresion
		 | expresion DISTINTO expresion
		 ;
		 
		 
bloque_de_sentencias: sentencia
					| '{' lista_de_sentencias '}'
					;
					
					
sentencia_de_salida: OUT '(' CADENA
					;
					
					
invocacion: ID '(' parametro_ejecutable ')'
		  ;
		  
		  
parametro_ejecutable: ID
		 			| parametro_ejecutable ',' ID
					;
		 			
					
sentencia_de_control: LOOP bloque_de_sentencias UNTIL '(' condicion ')'
					;
										
					
asignacion: ID '=' expresion
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
	   | '-' factor 
	   | ID 
	   ;
	   	