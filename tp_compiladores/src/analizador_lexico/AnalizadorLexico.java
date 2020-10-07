package analizador_lexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


public class AnalizadorLexico {
	private String codigo="";
	
	private int nro_linea=1;
	private int total_lineas;
	private int total_caracteres;
	
	private int pos_actual=0;
	private int ultima_pos=0;
	
	private boolean leer_caracter=false;
	private boolean fin=false; //fin de "archivo"
	
	private int filas_estados = 10; //de 0 a 4 estados. en total ?
	private int columnas_caracteres_validos = 14; // hasta ahora  ->  l d _ i . f BL + - * / = ; otro 
	
	private int estado_final = this.filas_estados - 1;

	
	
	private int matriz_transicion_estados [][];
	private AccionSemantica matriz_acciones_semanticas [][];
	
	private TablaTokens TTok; 
	private TablaSimbolos TSym; 

	
	
	public AnalizadorLexico(TablaTokens tt, TablaSimbolos ts) {
		//this.TTok = new TablaTokens();
		//this.TSym = new TablaSimbolos();
		this.TTok = tt;
		this.TSym = ts;
		
		this.matriz_transicion_estados = new int[this.filas_estados][this.columnas_caracteres_validos];
		this.inicializarMatrizTransicionEstados();
		
		this.matriz_acciones_semanticas = new AccionSemantica[this.filas_estados][this.columnas_caracteres_validos];
		this.inicializarMatrizAccionesSemanticas();
	}
	
	
	
	public void inicializarMatrizTransicionEstados() {
		//fila 0
		this.matriz_transicion_estados[0][0] = 1; //a E0 llega una letra l -> voy a E1
		this.matriz_transicion_estados[0][1] = 2; //a E0 llega un digito d -> voy a E2
		this.matriz_transicion_estados[0][2] = 0; //a E0 llega _ -> descarto y reinicio
		this.matriz_transicion_estados[0][3] = 1; //a E0 llega i -> voy a E1
		this.matriz_transicion_estados[0][4] = 4; //a E0 llega . -> voy a E4
		this.matriz_transicion_estados[0][5] = 1; //a E0 llega f -> voy a E1				
		this.matriz_transicion_estados[0][6] = 0; //a E0 llega blanco ' ' -> ciclo en E0
		this.matriz_transicion_estados[0][7] = -1; //a E0 llega + -> voy a EF
		this.matriz_transicion_estados[0][8] = -1; //a E0 llega - -> voy a EF
		this.matriz_transicion_estados[0][9] = -1; //a E0 llega * -> voy a EF
		this.matriz_transicion_estados[0][10] = -1; //a E0 llega / -> voy a EF
		
		this.matriz_transicion_estados[0][11] = -1; //a E0 llega '=' -> voy a Ef 
		this.matriz_transicion_estados[0][12] = -1; //a E0 llega ; -> voy a Ef
		
		this.matriz_transicion_estados[0][13] = 0; //a E0 llega otro caracter -> ciclo en E0
		//this.matriz_transicion_estados[0][5] = 1; //a E0 llega ';' -> fin de linea 
		//...
		
		
		
		
		//fila 1
		this.matriz_transicion_estados[1][0] = 1; //a E1 le llega una letra l -> ciclo en E1
		this.matriz_transicion_estados[1][1] = 1; //a E1 le llega un digito d -> ciclo en E1
		this.matriz_transicion_estados[1][2] = 1; //a E1 le llega un _ -> ciclo en E1
		this.matriz_transicion_estados[1][3] = 1; //a E1 le llega una i -> ciclo en E1
		this.matriz_transicion_estados[1][4] = 0; //a E1 le llega un . -> limpio y reinicio (notifico error! ID.)
		this.matriz_transicion_estados[1][5] = 1; //a E1 le llega una f -> ciclo en E1
		this.matriz_transicion_estados[1][6] = -1; //a E1 le llega blanco ' ' -> voy a Ef
		this.matriz_transicion_estados[1][7] = -1; //a E1 le llega + -> voy a Ef
		this.matriz_transicion_estados[1][8] = -1; //a E1 le llega - -> voy a Ef
		this.matriz_transicion_estados[1][9] = -1; //a E1 le llega * -> voy a Ef
		this.matriz_transicion_estados[1][10] = -1; //a E1 le llega / -> voy a Ef
		
		this.matriz_transicion_estados[1][11] = -1; //a E1 le llega un '=' -> voy a Ef
		this.matriz_transicion_estados[1][12] = -1; //a E1 le llega un ; -> voy a Ef
		
		this.matriz_transicion_estados[1][13] = -1; //a E1 le llega otro caracter -> limpio y reinicio? o voy a EF?
		//...
		
		
		
		//fila 2
		this.matriz_transicion_estados[2][0] = -1; //a E2 le llega una letra l -> voy a Ef
		this.matriz_transicion_estados[2][1] = 2; //a E2 le llega un digito d -> ciclo en E2	
		this.matriz_transicion_estados[2][2] = 3; //a E2 le llega un _ -> voy a E3
		this.matriz_transicion_estados[2][3] = -1; //a E2 le llega una i -> voy a EF
		this.matriz_transicion_estados[2][4] = 5; //a E2 le llega un . -> voy a E5
		this.matriz_transicion_estados[2][5] = 6; //a E2 le llega una f -> voy a E6
		this.matriz_transicion_estados[2][6] = -1; //a E2 le llega blanco ' ' -> voy a Ef
		this.matriz_transicion_estados[2][7] = -1; //a E2 le llega + -> voy a Ef
		this.matriz_transicion_estados[2][8] = -1; //a E2 le llega - -> voy a Ef
		this.matriz_transicion_estados[2][9] = -1; //a E2 le llega * -> voy a Ef
		this.matriz_transicion_estados[2][10] = -1; //a E2 le llega / -> voy a Ef
		
		
		this.matriz_transicion_estados[2][11] = -1; //a E2 le llega '=' -> voy a Ef
		this.matriz_transicion_estados[2][12] = -1; //a E2 le llega ; -> voy a Ef

		this.matriz_transicion_estados[2][13] = -1; //a E2 le llega otro caracter -> voy a Ef
		//...
		
		
		
		//fila 3
		this.matriz_transicion_estados[3][0] = 0; //a E3 llega una letra l -> descarto y voy a E0
		this.matriz_transicion_estados[3][1] = 0; //a E3 llega un digito -> descartes y voy a E0
		this.matriz_transicion_estados[3][2] = 0; //a E3 llega un _ -> descartes y voy a E0
		this.matriz_transicion_estados[3][3] = -1; //a E3 llega una i -> voy a EF
		this.matriz_transicion_estados[3][4] = 0; //a E3 llega un . -> descarto y voy a E0
		this.matriz_transicion_estados[3][5] = 0; //a E3 llega una f -> descarto y voy a EF
		this.matriz_transicion_estados[3][6] = 0; //a E3 llega un blanco ' ' -> descarto y voy a E0
		this.matriz_transicion_estados[3][7] = 0; //a E3 llega un + -> descarto y voy a EF
		this.matriz_transicion_estados[3][8] = 0; //a E3 llega una - -> descarto y voy a EF
		this.matriz_transicion_estados[3][9] = 0; //a E3 llega una * -> descarto y voy a EF
		this.matriz_transicion_estados[3][10] = 0; //a E3 llega una / -> descarto y voy a EF
		
		
		this.matriz_transicion_estados[3][11] = 0; //a E3 llega un = -> descarto y voy a EF
		this.matriz_transicion_estados[3][12] = 0; //a E3 llega un ; -> descarto y voy a EF
		
		this.matriz_transicion_estados[3][13] = 0; //a E3 llega otro caracter -> descarto y voy a EF
	
	
		
		//fila 4
		this.matriz_transicion_estados[4][0] = 0; //a E4 llega una letra l -> descarto y voy a E0
		this.matriz_transicion_estados[4][1] = 5; //a E4 llega un digito -> voy a E5
		this.matriz_transicion_estados[4][2] = 0; //a E4 llega un _ -> descartes y voy a E0
		this.matriz_transicion_estados[4][3] = 0; //a E4 llega una i -> descarto y reinicio
		this.matriz_transicion_estados[4][4] = 0; //a E4 llega un . -> descarto y voy a E0
		this.matriz_transicion_estados[4][5] = 0; //a E4 llega una f -> descarto y reinicio
		this.matriz_transicion_estados[4][6] = 0; //a E4 llega un blanco ' ' -> descarto y voy a E0
		this.matriz_transicion_estados[4][7] = 0; //a E4 llega un + -> descarto y reinicio
		this.matriz_transicion_estados[4][8] = 0; //a E4 llega una - -> descarto y reinicio
		this.matriz_transicion_estados[4][9] = 0; //a E4 llega una * -> descarto y reinicio
		this.matriz_transicion_estados[4][10] = 0; //a E4 llega una / -> descarto y reinicio
				
				
		this.matriz_transicion_estados[4][11] = 0; //a E4 llega un = -> descarto y reinicio
		this.matriz_transicion_estados[4][12] = 0; //a E4 llega un ; -> descarto y reinicio
				
		this.matriz_transicion_estados[4][13] = 0; //a E4 llega otro caracter -> descarto y reinicio


		
		//fila (estado) 5
		this.matriz_transicion_estados[5][0] = 0; //a E5 llega una letra l -> descarto y voy a E0
		this.matriz_transicion_estados[5][1] = 5; //a E5 llega un digito -> voy a E5
		this.matriz_transicion_estados[5][2] = 0; //a E5 llega un _ -> descartes y voy a E0
		this.matriz_transicion_estados[5][3] = 0; //a E5 llega una i -> descarto y reinicio
		this.matriz_transicion_estados[5][4] = 0; //a E5 llega un . -> descarto y reinicio
		this.matriz_transicion_estados[5][5] = 6; //a E5 llega una f -> voy a E6
		this.matriz_transicion_estados[5][6] = -1; //a E5 llega un blanco ' ' -> voy a EF
		this.matriz_transicion_estados[5][7] = -1; //a E5 llega un + -> voy a EF
		this.matriz_transicion_estados[5][8] = -1; //a E5 llega un - -> voy a EF
		this.matriz_transicion_estados[5][9] = -1; //a E5 llega un * -> voy a EF
		this.matriz_transicion_estados[5][10] = -1; //a E5 llega un / -> voy a EF
						
						
		this.matriz_transicion_estados[5][11] = -1; //a E4 llega un = -> voy a EF
		this.matriz_transicion_estados[5][12] = -1; //a E4 llega un ; -> voy a EF
					
		this.matriz_transicion_estados[5][13] = 0; //a E4 llega otro caracter -> descarto y reinicio

		
		
		//fila (estado) 6
		this.matriz_transicion_estados[6][0] = 0; //a E6 llega una letra l -> descarto y voy a E0
		this.matriz_transicion_estados[6][1] = 8; //a E6 llega un digito -> voy a E8
		this.matriz_transicion_estados[6][2] = 0; //a E6 llega un _ -> descartes y voy a E0
		this.matriz_transicion_estados[6][3] = 0; //a E6 llega una i -> descarto y reinicio
		this.matriz_transicion_estados[6][4] = 0; //a E6 llega un . -> descarto y reinicio
		this.matriz_transicion_estados[6][5] = 6; //a E llega una f -> voy a E6
		this.matriz_transicion_estados[6][6] = 0; //a E6 llega un blanco ' ' -> descarto y reinicio
		this.matriz_transicion_estados[6][7] = 7; //a E6 llega un + -> voy a EF
		this.matriz_transicion_estados[6][8] = 7; //a E6 llega un - -> voy a EF
		this.matriz_transicion_estados[6][9] = 0; //a E6 llega un * -> descarto y reinicio
		this.matriz_transicion_estados[6][10] = 0; //a E6 llega un / -> descarto y reinicio
						
						
		this.matriz_transicion_estados[6][11] = 0; //a E6 llega un = -> descarto y reinicio
		this.matriz_transicion_estados[6][12] = 0; //a E6 llega un ; -> descarto y reincioi
					
		this.matriz_transicion_estados[6][13] = 0; //a E6 llega otro caracter -> descarto y reinicio
		
		
		
		//fila (estado) 7
		this.matriz_transicion_estados[7][0] = 0; //a E7 llega una letra l -> descarto y voy a E0
		this.matriz_transicion_estados[7][1] = 8; //a E7 llega un digito -> voy a E8
		this.matriz_transicion_estados[7][2] = 0; //a E7 llega un _ -> descartes y voy a E0
		this.matriz_transicion_estados[7][3] = 0; //a E7 llega una i -> descarto y reinicio
		this.matriz_transicion_estados[7][4] = 0; //a E7 llega un . -> descarto y reinicio
		this.matriz_transicion_estados[7][5] = 0; //a E7 llega una f -> descarto y reinicio
		this.matriz_transicion_estados[7][6] = 0; //a E7 llega un blanco ' ' -> descarto y reinicio
		this.matriz_transicion_estados[7][7] = 0; //a E7 llega un + -> descarto y reinicio
		this.matriz_transicion_estados[7][8] = 0; //a E7 llega un - -> descarto y reinicio
		this.matriz_transicion_estados[7][9] = 0; //a E7 llega un * -> descarto y reinicio
		this.matriz_transicion_estados[7][10] = 0; //a E7 llega un / -> descarto y reinicio
						
						
		this.matriz_transicion_estados[7][11] = 0; //a E7 llega un = -> descarto y reinicio
		this.matriz_transicion_estados[7][12] = 0; //a E7 llega un ; -> descarto y reincio
					
		this.matriz_transicion_estados[7][13] = 0; //a E7 llega otro caracter -> descarto y reinicio
		
		
		
		//fila (estado) 8
		this.matriz_transicion_estados[8][0] = 0; //a E8 llega una letra l -> descarto y voy a E0
		this.matriz_transicion_estados[8][1] = 8; //a E8 llega un digito -> voy a E8
		this.matriz_transicion_estados[8][2] = 0; //a E8 llega un _ -> descartes y voy a E0
		this.matriz_transicion_estados[8][3] = 0; //a E8 llega una i -> descarto y reinicio
		this.matriz_transicion_estados[8][4] = 0; //a E8 llega un . -> descarto y reinicio
		this.matriz_transicion_estados[8][5] = 0; //a E8 llega una f -> descarto y reinicio
		this.matriz_transicion_estados[8][6] = -1; //a E8 llega un blanco ' ' -> voy a EF
		this.matriz_transicion_estados[8][7] = -1; //a E8 llega un + -> voy a EF
		this.matriz_transicion_estados[8][8] = -1; //a E8 llega un - -> voy a EF
		this.matriz_transicion_estados[8][9] = -1; //a E8 llega un * -> voy a EF
		this.matriz_transicion_estados[8][10] = -1; //a E8 llega un / -> voy a EF
								
								
		this.matriz_transicion_estados[8][11] = -1; //a E8 llega un = -> voy a EF
		this.matriz_transicion_estados[8][12] = -1; //a E8 llega un ; -> voy a EF
						
		this.matriz_transicion_estados[8][13] = 0; //a E8 llega otro caracter -> descarto y reinicio
				

		
		//fila (estado final) 
		this.matriz_transicion_estados[9][0] = 0; //en EF reinicio
		this.matriz_transicion_estados[9][1] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[9][2] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[9][3] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[9][4] = 0; //en EF reinicio
		this.matriz_transicion_estados[9][5] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[9][6] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[9][7] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[9][8] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[9][9] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[9][10] = 0; //en EF entrego token y reinicio
										
										
		this.matriz_transicion_estados[9][11] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[9][12] = 0; //en EF entrego token y reinicio
								
		this.matriz_transicion_estados[9][13] = 0; //en EF entrego token y reinicio

		
	}
	
	
	
	public void inicializarMatrizAccionesSemanticas() {
		//Acciones Semanticas
		AccionSemantica AS1 = new InicializarBuffer();
		AccionSemantica AS2 = new AgregarCaracter();
		AccionSemantica AS3 = new LlegaTokenValido(this.TTok, this.TSym); 
		AccionSemantica AS4 = new DescartarBuffer(); 
		AccionSemantica ASF = new EntregarTokenYReiniciar(this.TTok, this.TSym);

		
		//fila 0
		this.matriz_acciones_semanticas[0][0] = AS1; //InicializarBuffer(); //a inicio le llega letra -> inicializo buffer
		this.matriz_acciones_semanticas[0][1] = AS1; //InicializarBuffer(); //a inicio le llega digito -> inicializo buffer
		
		this.matriz_acciones_semanticas[0][2] = AS4; //Descartar(); //a inicio le llega _ -> rene descarte
		this.matriz_acciones_semanticas[0][3] = AS1; //InicializarBuffer(); //a inicio le llega i -> inicializo buffer
		this.matriz_acciones_semanticas[0][4] = AS1; //InicializarBuffer(); //a inicio le llega . -> inicializo buffer
		this.matriz_acciones_semanticas[0][5] = AS1; //InicializarBuffer(); //a inicio le llega f -> inicializo buffer
		this.matriz_acciones_semanticas[0][6] = AS4; //a inicio le llega blanco ' ' -> 
		this.matriz_acciones_semanticas[0][7] = AS3; //EntregarToken(); //a inicio le llega +
		this.matriz_acciones_semanticas[0][8] = AS3; //EntregarToken(); //a inicio le llega - 
		this.matriz_acciones_semanticas[0][9] = AS3; //EntregarToken(); //a inicio le llega * 
		this.matriz_acciones_semanticas[0][10] = AS3; //EntregarToken(); //a inicio le llega / 

		
		this.matriz_acciones_semanticas[0][11] = AS3; //EntregarToken(); //a inicio le llega =
		this.matriz_acciones_semanticas[0][12] = AS3; //EntregarToken(); //a inicio le llega ;
		
		this.matriz_acciones_semanticas[0][13] = AS4; //Descartar(); //a inicio le llega otro caracter
		
		
		
		//fila 1
		this.matriz_acciones_semanticas[1][0] = AS2; //AgregarCaracter(); //a E1 le llega letra -> agrego caracter a buffer
		this.matriz_acciones_semanticas[1][1] = AS2; //AgregarCaracter(); //a E1 le llega digito -> agrego caracter a buffer
		this.matriz_acciones_semanticas[1][2] = AS2; //AgregarCaracter(); //a E1 le llega _ -> agrego caracter a buffer
		this.matriz_acciones_semanticas[1][3] = AS2; //AgregarCaracter(); //a E1 le llega i -> agrego caracter a buffer
		this.matriz_acciones_semanticas[1][4] = AS4; //Descartar(); //a E1 le llega . -> limpio y reinicio
		this.matriz_acciones_semanticas[1][5] = AS2; //AgregarCaracter(); //a E1 le llega f -> agrego caracter a buffer
		
		this.matriz_acciones_semanticas[1][6] = AS3; //LlegaTokenValido(); //a E1 le llega blanco ' ' -> entrego token y descarto blanco
		this.matriz_acciones_semanticas[1][7] = AS3; //LlegaTokenValido(); //a E1 le llega + -> entrego token y descarto blanco
		this.matriz_acciones_semanticas[1][8] = AS3; //LlegaTokenValido(); //a E1 le llega - -> entrego token y descarto blanco
		this.matriz_acciones_semanticas[1][9] = AS3; //LlegaTokenValido(); //a E1 le llega * -> entrego token y descarto blanco
		this.matriz_acciones_semanticas[1][10] = AS3; //LlegaTokenValido(); //a E1 le llega / -> entrego token y descarto blanco
		
		
		this.matriz_acciones_semanticas[1][11] = AS3; //LlegaTokenValido(); //a E1 le llega '=' -> entrego token 
		this.matriz_acciones_semanticas[1][12] = AS3; //LlegaTokenValido(); //a E1 le llega blanco ' ' -> entrego token y descarto blanco

		this.matriz_acciones_semanticas[1][13] = AS3; //LlegaTokenValido(); //a E1 le llega otro caracter -> entrego token
		//...
		
		
		
		//fila 2
		this.matriz_acciones_semanticas[2][0] = AS4; //DescartarBuffer(); //a E2 le llega una letra -> descarto y vuelvo a inicio? o entregoToken?
		this.matriz_acciones_semanticas[2][1] = AS2; //AgregarCaracter(); //a E2 le llega un digito -> 
		
		this.matriz_acciones_semanticas[2][2] = AS2; //AgregarCaracter(); //a E2 le llega _ -> agrego caracter a buffer
		this.matriz_acciones_semanticas[2][3] = AS4; //Descartar(); //a E2 le llega i -> agrego caracter a buffer
		this.matriz_acciones_semanticas[2][4] = AS2; //AgregarCaracter(); //a E2 le llega . -> limpio y reinicio
		this.matriz_acciones_semanticas[2][5] = AS2; //AgregarCaracter(); //a E2 le llega f -> agrego caracter a buffer
		
		
		this.matriz_acciones_semanticas[2][6] = AS3; //EntregarToken(); //a E2 le llega blanco ' ' -> entrego token tipo constante
		this.matriz_acciones_semanticas[2][7] = AS3; //EntregarToken(); //a E2 le llega + -> entrego token tipo constante
		this.matriz_acciones_semanticas[2][8] = AS3; //EntregarToken(); //a E2 le llega - -> entrego token tipo constante
		this.matriz_acciones_semanticas[2][9] = AS3; //EntregarToken(); //a E2 le llega * -> entrego token tipo constante
		this.matriz_acciones_semanticas[2][10] = AS3; //EntregarToken(); //a E2 le llega / -> entrego token tipo constante
		
		
		this.matriz_acciones_semanticas[2][11] = AS3; //EntregarToken(); //a E2 le llega = -> entrego token tipo constante
		this.matriz_acciones_semanticas[2][12] = AS3; //EntregarToken(); //a E2 le llega ; -> entrego token tipo constante
		
		this.matriz_acciones_semanticas[2][13] = AS3; //EntregarToken(); //a E2 le llega otro caracter -> entrego token tipo constante
		//...
		
		
		
		//fila (estado) 3
		this.matriz_acciones_semanticas[3][0] = AS4; //DescartarBuffer(); //a E3 le llega una letra -> descarto y vuelvo a inicio? o entregoToken?
		this.matriz_acciones_semanticas[3][1] = AS4; //Descartar(); //a E3 le llega un digito -> 
				
		this.matriz_acciones_semanticas[3][2] = AS2; //Descartar(); //a E3 le llega _ -> agrego caracter a buffer
		this.matriz_acciones_semanticas[3][3] = AS4; //AgregarCaracter(); //a E3 le llega i -> agrego caracter a buffer
		this.matriz_acciones_semanticas[3][4] = AS2; //Descartar(); //a E3 le llega . -> limpio y reinicio
		this.matriz_acciones_semanticas[3][5] = AS2; //Descartar(); //a E3 le llega f -> agrego caracter a buffer
				
				
		this.matriz_acciones_semanticas[3][6] = AS3; //EntregarToken(); //a E3 le llega blanco ' ' -> entrego token tipo constante
		this.matriz_acciones_semanticas[3][7] = AS3; //EntregarToken(); //a E3 le llega + -> entrego token tipo constante
		this.matriz_acciones_semanticas[3][8] = AS3; //EntregarToken(); //a E3 le llega - -> entrego token tipo constante
		this.matriz_acciones_semanticas[3][9] = AS3; //EntregarToken(); //a E3 le llega * -> entrego token tipo constante
		this.matriz_acciones_semanticas[3][10] = AS3; //EntregarToken(); //a E3 le llega / -> entrego token tipo constante
				
				
		this.matriz_acciones_semanticas[3][11] = AS3; //EntregarToken(); //a E2 le llega = -> entrego token tipo constante
		this.matriz_acciones_semanticas[3][12] = AS3; //EntregarToken(); //a E2 le llega ; -> entrego token tipo constante
			
		this.matriz_acciones_semanticas[3][13] = AS3; //EntregarTokenIndividual(); //a E2 le llega otro caracter -> entrego token tipo constante
		//...
				
		
		
		//fila (estado) 4
		this.matriz_acciones_semanticas[4][0] = AS4; //DescartarBuffer(); //a E4 le llega una letra -> descarto y vuelvo a inicio? o entregoToken?
		this.matriz_acciones_semanticas[4][1] = AS2; //AgregarCaracter(); //a E4 le llega un digito -> 
		this.matriz_acciones_semanticas[4][2] = AS4; //Descartar(); //a E4 le llega _ -> agrego caracter a buffer
		this.matriz_acciones_semanticas[4][3] = AS4; //Descartar(); //a E4 le llega i -> agrego caracter a buffer
		this.matriz_acciones_semanticas[4][4] = AS4; //Descartar(); //a E4 le llega . -> limpio y reinicio
		this.matriz_acciones_semanticas[4][5] = AS4; //Descartar(); //a E4 le llega f -> agrego caracter a buffer
		this.matriz_acciones_semanticas[4][6] = AS4; //Descartar(); //a E4 le llega blanco ' ' -> entrego token tipo constante
		this.matriz_acciones_semanticas[4][7] = AS4; //Descartar(); //a E4 le llega + -> entrego token tipo constante
		this.matriz_acciones_semanticas[4][8] = AS4; //Descartar(); //a E4 le llega - -> entrego token tipo constante
		this.matriz_acciones_semanticas[4][9] = AS4; //Descartar(); //a E4 le llega * -> entrego token tipo constante
		this.matriz_acciones_semanticas[4][10] = AS4; //Descartar(); //a E4 le llega / -> entrego token tipo constante
				
				
		this.matriz_acciones_semanticas[4][11] = AS4; //Descartar(); //a E4 le llega = -> entrego token tipo constante
		this.matriz_acciones_semanticas[4][12] = AS4; //Descartar(); //a E4 le llega ; -> entrego token tipo constante
			
		this.matriz_acciones_semanticas[4][13] = AS4; //Descartar(); //a E4 le llega otro caracter -> entrego token tipo constante
		//...
				
		
		
		
		//fila (estado) 5
		this.matriz_acciones_semanticas[5][0] = AS4; //DescartarBuffer(); //a E5 le llega una letra -> descarto y vuelvo a inicio? o entregoToken?
		this.matriz_acciones_semanticas[5][1] = AS2; //AgregarCaracter(); //a E5 le llega un digito -> 
		this.matriz_acciones_semanticas[5][2] = AS4; //Descartar(); //a E5 le llega _ -> agrego caracter a buffer
		this.matriz_acciones_semanticas[5][3] = AS4; //Descartar(); //a E5 le llega i -> agrego caracter a buffer
		this.matriz_acciones_semanticas[5][4] = AS4; //Descartar(); //a E5 le llega . -> limpio y reinicio
		this.matriz_acciones_semanticas[5][5] = AS2; //AgregarCaracter(); //a E5 le llega f -> agrego caracter a buffer
		this.matriz_acciones_semanticas[5][6] = AS3; //EntregarToken(); //a E5 le llega blanco ' ' -> entrego token tipo constante
		this.matriz_acciones_semanticas[5][7] = AS3; //EntregarToken(); //a E5 le llega + -> entrego token tipo constante
		this.matriz_acciones_semanticas[5][8] = AS3; //EntregarToken(); //a E5 le llega - -> entrego token tipo constante
		this.matriz_acciones_semanticas[5][9] = AS3; //EntregarToken(); //a E5 le llega * -> entrego token tipo constante
		this.matriz_acciones_semanticas[5][10] = AS3; //EntregarToken(); //a E5 le llega / -> entrego token tipo constante
				
				
		this.matriz_acciones_semanticas[5][11] = AS3; //EntregarToken(); //a E5 le llega = -> entrego token tipo constante
		this.matriz_acciones_semanticas[5][12] = AS3; //EntregarToken(); //a E5 le llega ; -> entrego token tipo constante
			
		this.matriz_acciones_semanticas[5][13] = AS4; //Descartar(); //a E4 le llega otro caracter -> entrego token tipo constante
		//...
		
		

		//fila (estado) 6
		this.matriz_acciones_semanticas[6][0] = AS4; //DescartarBuffer(); //a E6 le llega una letra -> descarto y vuelvo a inicio? o entregoToken?
		this.matriz_acciones_semanticas[6][1] = AS2; //AgregarCaracter(); //a E6 le llega un digito -> 
		this.matriz_acciones_semanticas[6][2] = AS4; //Descartar(); //a E6 le llega _ -> agrego caracter a buffer
		this.matriz_acciones_semanticas[6][3] = AS4; //Descartar(); //a E6 le llega i -> agrego caracter a buffer
		this.matriz_acciones_semanticas[6][4] = AS4; //Descartar(); //a E6 le llega . -> limpio y reinicio
		this.matriz_acciones_semanticas[6][5] = AS4; //Descartar(); //a E6 le llega f -> agrego caracter a buffer
		this.matriz_acciones_semanticas[6][6] = AS4; //Descartar(); //a E6 le llega blanco ' ' -> limpio y notifico error
		this.matriz_acciones_semanticas[6][7] = AS2; //AgregarCaracter(); //a E6 le llega + -> agrego caracter +
		this.matriz_acciones_semanticas[6][8] = AS2; //AgregarCaracter(); //a E6 le llega - -> agregp caracter -
		this.matriz_acciones_semanticas[6][9] = AS4; //Descartar(); //a E6 le llega * -> limpio y reinicio
		this.matriz_acciones_semanticas[6][10] = AS4; //Descartar(); //a E6 le llega / -> lmpio y reinicio
				
				
		this.matriz_acciones_semanticas[6][11] = AS4; //Descartar(); //a E6 le llega = -> limpio y reinicio
		this.matriz_acciones_semanticas[6][12] = AS4; //Descartar(); //a E6 le llega ; -> limpio y reinicio
			
		this.matriz_acciones_semanticas[6][13] = AS4; //Descartar(); //a E4 le llega otro caracter -> entrego token tipo constante
		//...
		
		

		//fila (estado) 7
		this.matriz_acciones_semanticas[7][0] = AS4; //DescartarBuffer(); //a E7 le llega una letra -> descarto y vuelvo a inicio? o entregoToken?
		this.matriz_acciones_semanticas[7][1] = AS2; //AgregarCaracter(); //a E7 le llega un digito -> 
		this.matriz_acciones_semanticas[7][2] = AS4; //Descartar(); //a E7 le llega _ -> agrego caracter a buffer
		this.matriz_acciones_semanticas[7][3] = AS4; //Descartar(); //a E7 le llega i -> agrego caracter a buffer
		this.matriz_acciones_semanticas[7][4] = AS4; //Descartar(); //a E7 le llega . -> limpio y reinicio
		this.matriz_acciones_semanticas[7][5] = AS4; //Descartar(); //a E7 le llega f -> agrego caracter a buffer
		this.matriz_acciones_semanticas[7][6] = AS4; //Descartar(); //a E7 le llega blanco ' ' -> limpio y notifico error
		this.matriz_acciones_semanticas[7][7] = AS4; //Descartar(); //a E7 le llega + -> agrego caracter +
		this.matriz_acciones_semanticas[7][8] = AS4; //Descartar(); //a E7 le llega - -> agregp caracter -
		this.matriz_acciones_semanticas[7][9] = AS4; //Descartar(); //a E7 le llega * -> limpio y reinicio
		this.matriz_acciones_semanticas[7][10] = AS4; //Descartar(); //a E7 le llega / -> lmpio y reinicio
				
				
		this.matriz_acciones_semanticas[7][11] = AS4; //Descartar(); //a E6 le llega = -> limpio y reinicio
		this.matriz_acciones_semanticas[7][12] = AS4; //Descartar(); //a E6 le llega ; -> limpio y reinicio
			
		this.matriz_acciones_semanticas[7][13] = AS4; //Descartar(); //a E4 le llega otro caracter -> entrego token tipo constante
		//...

		

		//fila (estado) 8
		this.matriz_acciones_semanticas[8][0] = AS4; //DescartarBuffer(); //a E8 le llega una letra -> descarto y vuelvo a inicio? o entregoToken?
		this.matriz_acciones_semanticas[8][1] = AS2; //AgregarCaracter(); //a E8 le llega un digito -> 
		this.matriz_acciones_semanticas[8][2] = AS4; //Descartar(); //a E8 le llega _ -> agrego caracter a buffer
		this.matriz_acciones_semanticas[8][3] = AS4; //Descartar(); //a E8 le llega i -> agrego caracter a buffer
		this.matriz_acciones_semanticas[8][4] = AS4; //Descartar(); //a E8 le llega . -> limpio y reinicio
		this.matriz_acciones_semanticas[8][5] = AS4; //Descartar(); //a E8 le llega f -> agrego caracter a buffer
		this.matriz_acciones_semanticas[8][6] = AS3; //EntregarToken(); //a E8 le llega blanco ' ' -> limpio y notifico error
		this.matriz_acciones_semanticas[8][7] = AS3; //EntregarToken(); //a E8 le llega + -> agrego caracter +
		this.matriz_acciones_semanticas[8][8] = AS3; //EntregarToken(); //a E8 le llega - -> agregp caracter -
		this.matriz_acciones_semanticas[8][9] = AS3; //EntregarToken(); //a E8 le llega * -> limpio y reinicio
		this.matriz_acciones_semanticas[8][10] = AS3; //EntregarToken(); //a E8 le llega / -> lmpio y reinicio
				
				
		this.matriz_acciones_semanticas[8][11] = AS3; //EntregarToken(); //a E6 le llega = -> limpio y reinicio
		this.matriz_acciones_semanticas[8][12] = AS3; //EntregarToken(); //a E6 le llega ; -> limpio y reinicio
			
		this.matriz_acciones_semanticas[8][13] = AS4; //Descartar(); //a E4 le llega otro caracter -> entrego token tipo constante
		//...
		
		
		
		//fila EstadoFinal
		this.matriz_acciones_semanticas[9][0] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[9][1] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[9][2] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[9][3] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[9][4] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[9][5] = ASF; //
		
		this.matriz_acciones_semanticas[9][6] = ASF; //
		this.matriz_acciones_semanticas[9][7] = ASF;
		this.matriz_acciones_semanticas[9][8] = ASF; 
		this.matriz_acciones_semanticas[9][9] = ASF; 
		this.matriz_acciones_semanticas[9][10] = ASF; 
		
		
		this.matriz_acciones_semanticas[9][11] = ASF; 
		this.matriz_acciones_semanticas[9][12] = ASF; 
		
		this.matriz_acciones_semanticas[9][13] = ASF; 
	}
	
	
	
	
	public void abrirCargarArchivo() {
		///Lee fichero y lo carga en el String privado codigo
		
		File archivo = null; 
		FileReader fr = null;
	    BufferedReader br = null;
	    
        String linea;
        int nro_linea = 0;
        
        try {
        	archivo = new File ("casos_prueba_id_cte.txt");	        
	        fr = new FileReader (archivo);
	        System.out.println("Archivo valido "+fr+ "\n");
	        
	        br = new BufferedReader(fr); 
	   
	        
	        while((linea=br.readLine()) != null) {
				nro_linea ++;
				System.out.println("nro linea "+nro_linea+" -> "+linea+"\n");
				this.codigo += linea + "\n";
			}
	        
	        this.total_lineas = nro_linea;
	        this.total_caracteres = this.codigo.length();
	        System.out.println();
        }catch(Exception e){
	         e.printStackTrace();
	    } 
	}
	
	
	
	public Token leerCodigo() { 
		//lee el codigo y ejecuta ASs hasta encontrar un token
        
        AccionSemantica AS;
        			
		int estado_actual = 0; //estado inicial 
		int nro_columna = -1; //nro de columna depende del tipo de caracter. por defecto arranco con otro

		char caracter = ' ';
		int ascii;
			
		Token titi = null;
		
		
		while (this.leer_caracter & !fin){ //mientras haya caracteres por leer y no sea fin de codigo			
			this.pos_actual = this.ultima_pos; //retomo a partir de la ultima posicion leida	
			//System.out.println("\n Frontera -> " +  this.pos_actual + " ,  limite -> "+ this.ultima_pos +" , TOTAL -> "+this.total_caracteres);
	
			while (estado_actual != -1) {
					
				caracter = this.codigo.charAt(this.pos_actual); //leo caracter
				ascii = (int)caracter;
					
				System.out.println("\n");
				System.out.println("Caracter actual "+caracter+" , ASCII "+(int)caracter);					
										
				nro_columna = getColumnaCaracter(caracter);
				
				System.out.println("Voy a Posicion matriz: " + estado_actual + " , " + nro_columna);				
				
				AS = this.matriz_acciones_semanticas[estado_actual][nro_columna];
					
				
				AS.ejecutar(caracter, this.nro_linea);
					
				
				estado_actual = matriz_transicion_estados[estado_actual][nro_columna];	
				
				this.pos_actual++;
				
				
				//si es salto de linea
				if (ascii == 10) { //codigo ascii de \n es 10
					System.out.println("\n \n \n Salto de linea \n");
					this.nro_linea++;
					
					//condicion de corte
					if (this.nro_linea > this.total_lineas) {  // si me paso corta todo
						System.out.println();	
						System.out.println("FIN de codigo!! nro linea "+this.nro_linea+" > "+this.total_lineas);
						System.out.println();	
						System.out.println("Frontera -> "+this.pos_actual+"  ,   total caracteres -> "+this.total_caracteres);
						System.out.println();	

						this.leer_caracter = false;
						this.fin = true;
						estado_actual = -1;
						this.pos_actual ++;
					}
				}
			}
			
			
			
			//llega el estado final
			
			if (!fin) {	//si no es el fin de archivo (String), ejecuto ASFinal
					
				estado_actual = this.estado_final; 
				
				AS = this.matriz_acciones_semanticas[estado_actual][nro_columna];
				
				// ejecuto ASF -> entregar token, y reiniciar estado y buffer
				AS.ejecutar(caracter, this.nro_linea);
				
				//System.out.println("\n inicio -> " +  this.pos_actual + " ,  actual -> "+ this.ultima_pos +" , TOTAL -> "+this.total_caracteres);

				//devuelvo token, dejo de leer y actualizo posicion
				titi = AS.getToken();
				
				this.leer_caracter = false; 
				
				
				System.out.println("\n inicio -> " +  ultima_pos + " ,  frontera -> "+ pos_actual +" ,  total -> " + this.total_caracteres);
				
				this.ultima_pos = this.pos_actual; 
				
				System.out.println("Return -> Nro Linea: " + this.nro_linea + " , Token: "+ titi.getLexema()+" , tipo "+titi.getTipo());
				}
		}
		return titi;
	}

	
	
	public int getColumnaCaracter(char c) {
	
		int nro_columna=13; //inicializo con otro caracter
		
		
		if (Character.isLowerCase(c)) { nro_columna=0; }; 
		if (Character.isLetter(c)) { nro_columna=0; };
		
		if (Character.isDigit(c)) { nro_columna=1; }; 
		
		if (c == '_') { nro_columna=2; };
		
		if (c == 'i') { nro_columna=3; };
		if (c == '.') { nro_columna=4; };
		if (c == 'f') { nro_columna=5; };
		
		if (Character.isWhitespace(c)) { nro_columna=6; }; 
		
		if (c == '+') { nro_columna=7; };
		if (c == '-') { nro_columna=8; };
		if (c == '*') { nro_columna=9; };
		if (c == '/') { nro_columna=10; };
		
		if (c == '=') { nro_columna=11; };
		if (c == ';') { nro_columna=12; };
		//if (c == '%') { nro_columna=5; };
		//...
		
		return nro_columna; //retorna el nro de columna asociado al tipo del caracter
	}
	
	
	
	public Token getToken() { //invoco a yylex()
		this.leer_caracter = true;
		
		Token t = this.leerCodigo();
		
		if (t != null) {
			//System.out.println("yylex Retorno ->  " + t.getLexema()+" , "+t.getTipo()); 
			return t;
		} else {
			System.out.println("Fin de archivo!");
			this.fin = true;
			//break;
		}
		
		System.out.println("\n \n ---------------------------------------------------\n");
		return null;
	}
	
	

	public boolean quedanTokens() {
		if (this.fin) {
			return false;
		}
		return true;
	}
	
	
	public void mostrarTablaTokens() {
		this.TTok.mostrarTokens();
		System.out.println();
	}
	
	
	public void mostrarTablaSimbolos() {
		//System.out.println("\n Tabla de simbolos \n");
		this.TSym.mostrarListaTsym();
	}
}
