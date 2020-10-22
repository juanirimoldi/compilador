package analizador_lexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import tabla_simbolos.TablaDeSimbolos;


public class AnalizadorLexico {
	private String dir_codigo;
	private String codigo="";
	
	private int nro_linea=1;
	private int total_lineas;
	private int total_caracteres;
	
	private int pos_actual=0;
	private int ultima_pos=0;
	
	private boolean leer_caracter=false;
	private boolean fin=false; //fin de "archivo"
	
	private int filas_estados = 13; //de 0 a 14 estados. en total ?
	private int columnas_caracteres_validos = 24; // hasta ahora  ->  l d _ i . f BL + - * / = ; otro 
	
	private int estado_final = this.filas_estados - 1;

	
	
	private int matriz_transicion_estados [][];
	private AccionSemantica matriz_acciones_semanticas [][];
	
	
	private TablaDeSimbolos tds;
	
	
	
	
	public AnalizadorLexico(String programa){
		this.dir_codigo = programa;
		
		this.tds = tds;
				
		
		this.matriz_transicion_estados = new int[this.filas_estados][this.columnas_caracteres_validos];
		this.inicializarMatrizTransicionEstados();
		
		this.matriz_acciones_semanticas = new AccionSemantica[this.filas_estados][this.columnas_caracteres_validos];
		this.inicializarMatrizAccionesSemanticas();
	}
	
	
	
	public void inicializarMatrizTransicionEstados() {
		//estado inicial
		this.matriz_transicion_estados[0][0] = 1; //en E0 llega una letra l -> voy a E1
		this.matriz_transicion_estados[0][1] = 2; //en E0 llega un digito d -> voy a E2
		this.matriz_transicion_estados[0][2] = 0; //en E0 llega _ -> descarto y reinicio
		this.matriz_transicion_estados[0][3] = 1; //llega i -> voy a E1
		this.matriz_transicion_estados[0][4] = 4; //llega . -> voy a E4
		this.matriz_transicion_estados[0][5] = 1; //llega f -> voy a E1				
		this.matriz_transicion_estados[0][6] = 0; //llega blanco ' ' -> ciclo en E0
		this.matriz_transicion_estados[0][7] = -1; // llega + -> voy a EF
		this.matriz_transicion_estados[0][8] = -1; //llega - -> voy a EF
		this.matriz_transicion_estados[0][9] = -1; //llega * -> voy a EF
		this.matriz_transicion_estados[0][10] = -1; //llega / -> voy a EF
		this.matriz_transicion_estados[0][11] = -1; //llega '=' -> voy a Ef 
		
		
		this.matriz_transicion_estados[0][20] = -1; //llega ; -> voy a Ef
		this.matriz_transicion_estados[0][21] = 10; //llega % -> voy a E10

		this.matriz_transicion_estados[0][23] = 0; //a E0 llega otro caracter -> ciclo en E0
		
		
		
		
		//fila 1 -> identificadores
		
		this.matriz_transicion_estados[1][0] = 1; //en E1 le llega una letra l -> ciclo en E1
		this.matriz_transicion_estados[1][1] = 1; //en E1 le llega un digito d -> ciclo en E1
		this.matriz_transicion_estados[1][2] = 1; //en E1 le llega un _ -> ciclo en E1
		this.matriz_transicion_estados[1][3] = 1; //llega una i -> ciclo en E1
		this.matriz_transicion_estados[1][4] = 0; //llega un . -> limpio y reinicio (notifico error! ID.)
		this.matriz_transicion_estados[1][5] = 1; //llega una f -> ciclo en E1
		this.matriz_transicion_estados[1][6] = -1; //llega blanco ' ' -> voy a Ef
		this.matriz_transicion_estados[1][7] = -1; //llega + -> voy a Ef
		this.matriz_transicion_estados[1][8] = -1; //llega - -> voy a Ef
		this.matriz_transicion_estados[1][9] = -1; //llega * -> voy a Ef
		this.matriz_transicion_estados[1][10] = -1; //llega / -> voy a Ef
		this.matriz_transicion_estados[1][11] = -1; //llega un '=' -> voy a Ef
		
		//aca poner < > ! etc...
		
		this.matriz_transicion_estados[1][20] = -1; //llega un ; -> voy a Ef
		this.matriz_transicion_estados[1][21] = 0; //llega un % -> limpio y reinicio
		
		this.matriz_transicion_estados[1][23] = -1; //llega otro caracter -> limpio y reinicio? o voy a EF?
	
		
		
		
		//fila 2 -> digitos
		
		this.matriz_transicion_estados[2][0] = -1; //en E2 le llega una letra l -> voy a Ef  !!!VER!!! 22f -> que hacer?
		this.matriz_transicion_estados[2][1] = 2; //en E2 le llega un digito d -> ciclo en E2	
		this.matriz_transicion_estados[2][2] = 3; //en E2 le llega un _ -> voy a E3  -> para los 55_
		this.matriz_transicion_estados[2][3] = -1; //llega una i -> voy a EF 
		this.matriz_transicion_estados[2][4] = 5; //llega un . -> voy a E5 ->  !!!VER!!! -> 7. ??
		this.matriz_transicion_estados[2][5] = 6; //llega una f -> voy a E6 -> !!!VER!!! -> 52f ??
		this.matriz_transicion_estados[2][6] = -1; //llega blanco ' ' -> voy a Ef
		this.matriz_transicion_estados[2][7] = -1; //llega + -> voy a Ef
		this.matriz_transicion_estados[2][8] = -1; //llega - -> voy a Ef
		this.matriz_transicion_estados[2][9] = -1; //llega * -> voy a Ef
		this.matriz_transicion_estados[2][10] = -1; //llega / -> voy a Ef
		this.matriz_transicion_estados[2][11] = -1; //llega '=' -> voy a Ef
		
		
		this.matriz_transicion_estados[2][20] = -1; //llega ; -> voy a Ef
		this.matriz_transicion_estados[2][21] = 0; //llega % -> limpio y reinicio
		
		
		this.matriz_transicion_estados[2][23] = -1; //llega otro caracter -> voy a Ef
		
		
		
		
		//fila 3 -> digitos especiales 111_
		
		this.matriz_transicion_estados[3][0] = 0; //en E3 llega una letra l -> descarto y voy a E0
		this.matriz_transicion_estados[3][1] = 0; //en E3 llega un digito -> descartes y voy a E0
		this.matriz_transicion_estados[3][2] = 0; //en E3 llega un _ -> descartes y voy a E0
		this.matriz_transicion_estados[3][3] = -1; //llega una i -> voy a EF
		this.matriz_transicion_estados[3][4] = 0; //llega un . -> descarto y voy a E0
		this.matriz_transicion_estados[3][5] = 0; //llega una f -> descarto y voy a EF
		this.matriz_transicion_estados[3][6] = 0; //llega un blanco ' ' -> descarto y voy a E0
		this.matriz_transicion_estados[3][7] = 0; //llega un + -> descarto y voy a EF
		this.matriz_transicion_estados[3][8] = 0; //llega una - -> descarto y voy a EF
		this.matriz_transicion_estados[3][9] = 0; //llega una * -> descarto y voy a EF
		this.matriz_transicion_estados[3][10] = 0; //llega una / -> descarto y voy a EF
		this.matriz_transicion_estados[3][11] = 0; //llega un = -> descarto y voy a EF
		
		
		this.matriz_transicion_estados[3][20] = 0; //llega un ; -> descarto y reinicio
		this.matriz_transicion_estados[3][21] = 0; //llega un % -> descarto y reinicio
		
		this.matriz_transicion_estados[3][23] = 0; //llega otro caracter -> descarto y voy a EF
	
	
		
		
		//fila 4 -> digitos double que empiezan con .
		
		this.matriz_transicion_estados[4][0] = 0; //en E4 llega una letra l -> descarto y voy a E0
		this.matriz_transicion_estados[4][1] = 5; //en E4 llega un digito -> voy a E5
		this.matriz_transicion_estados[4][2] = 0; //en E4 llega un _ -> descartes y voy a E0
		this.matriz_transicion_estados[4][3] = 0; //llega una i -> descarto y reinicio
		this.matriz_transicion_estados[4][4] = 0; //llega un . -> descarto y voy a E0
		this.matriz_transicion_estados[4][5] = 0; //llega una f -> descarto y reinicio
		this.matriz_transicion_estados[4][6] = 0; //llega un blanco ' ' -> descarto y voy a E0
		this.matriz_transicion_estados[4][7] = 0; //llega un + -> descarto y reinicio
		this.matriz_transicion_estados[4][8] = 0; //llega una - -> descarto y reinicio
		this.matriz_transicion_estados[4][9] = 0; //llega una * -> descarto y reinicio
		this.matriz_transicion_estados[4][10] = 0; //llega una / -> descarto y reinicio				
		this.matriz_transicion_estados[4][11] = 0; //llega un = -> descarto y reinicio

		
		this.matriz_transicion_estados[4][20] = 0; //llega un ; -> descarto y reinicio
		this.matriz_transicion_estados[4][21] = 0; //llega un % -> descarto y reinicio
				
		this.matriz_transicion_estados[4][23] = 0; //llega otro caracter -> descarto y reinicio


		
		
		//fila 5 -> digitos decimales -> .555
		this.matriz_transicion_estados[5][0] = 0; //en E5 llega una letra l -> descarto y voy a E0
		this.matriz_transicion_estados[5][1] = 5; //en E5 llega un digito -> ciclo en E5
		this.matriz_transicion_estados[5][2] = 0; //en E5 llega un _ -> descartes y voy a E0
		this.matriz_transicion_estados[5][3] = 0; //llega una i -> descarto y reinicio
		this.matriz_transicion_estados[5][4] = 0; //llega un . -> descarto y reinicio
		this.matriz_transicion_estados[5][5] = 6; //llega una f -> voy a E6  !!!VER!!! -> .555f ???
		this.matriz_transicion_estados[5][6] = -1; //llega un blanco ' ' -> voy a EF
		this.matriz_transicion_estados[5][7] = -1; //llega un + -> voy a EF
		this.matriz_transicion_estados[5][8] = -1; //llega un - -> voy a EF
		this.matriz_transicion_estados[5][9] = -1; //llega un * -> voy a EF
		this.matriz_transicion_estados[5][10] = -1; //llega un / -> voy a EF				
		this.matriz_transicion_estados[5][11] = -1; //llega un = -> voy a EF

		
		this.matriz_transicion_estados[5][20] = -1; //llega un ; -> voy a EF
		this.matriz_transicion_estados[5][21] = 0; //llega un % -> limpio y reinicio
					
		this.matriz_transicion_estados[5][23] = 0; //llega otro caracter -> descarto y reinicio

		
		
		//fila 6  ->  66f ????   y   .333f   
		this.matriz_transicion_estados[6][0] = 0; //en E6 llega una letra l -> descarto y voy a E0
		this.matriz_transicion_estados[6][1] = 8; //en E6 llega un digito -> voy a E8  !!!VER!!! -> .333f8 ??
		this.matriz_transicion_estados[6][2] = 0; //en E6 llega un _ -> descartes y voy a E0
		this.matriz_transicion_estados[6][3] = 0; //llega una i -> descarto y reinicio
		this.matriz_transicion_estados[6][4] = 0; //llega un . -> descarto y reinicio
		this.matriz_transicion_estados[6][5] = 0; //llega una f -> descarto
		this.matriz_transicion_estados[6][6] = 0; //llega un blanco ' ' -> descarto y reinicio
		this.matriz_transicion_estados[6][7] = 7; //llega un + -> voy a EF  !!!VER!!! -> .333f+ ??
		this.matriz_transicion_estados[6][8] = 7; //llega un - -> voy a EF  !!!VER!!! -> .333f- ??
		this.matriz_transicion_estados[6][9] = 0; //llega un * -> descarto y reinicio
		this.matriz_transicion_estados[6][10] = 0; //llega un / -> descarto y reinicio				
		this.matriz_transicion_estados[6][11] = 0; //llega un = -> descarto y reinicio
		
		
		this.matriz_transicion_estados[6][20] = 0; //llega un ; -> descarto y reincioi
		this.matriz_transicion_estados[6][21] = 0; //llega un % -> descarto y reincioi
					
		this.matriz_transicion_estados[6][23] = 0; //llega otro caracter -> descarto y reinicio
		
		
		
		
		//fila 7 ->  doubles flasheros  ->  .333f+   y   .333f- 
		this.matriz_transicion_estados[7][0] = 0; //en E7 llega una letra l -> descarto y voy a E0
		this.matriz_transicion_estados[7][1] = 8; //en E7 llega un digito -> voy a E8
		this.matriz_transicion_estados[7][2] = 0; //en E7 llega un _ -> descartes y voy a E0
		this.matriz_transicion_estados[7][3] = 0; //llega una i -> descarto y reinicio
		this.matriz_transicion_estados[7][4] = 0; //llega un . -> descarto y reinicio
		this.matriz_transicion_estados[7][5] = 0; //llega una f -> descarto y reinicio
		this.matriz_transicion_estados[7][6] = 0; //llega un blanco ' ' -> descarto y reinicio
		this.matriz_transicion_estados[7][7] = 0; //llega un + -> descarto y reinicio
		this.matriz_transicion_estados[7][8] = 0; //llega un - -> descarto y reinicio
		this.matriz_transicion_estados[7][9] = 0; //llega un * -> descarto y reinicio
		this.matriz_transicion_estados[7][10] = 0; //llega un / -> descarto y reinicio				
		this.matriz_transicion_estados[7][11] = 0; //llega un = -> descarto y reinicio

		
		this.matriz_transicion_estados[7][20] = 0; //llega un ; -> descarto y reincio
		this.matriz_transicion_estados[7][21] = 0; //llega un % -> descarto y reincio
					
		this.matriz_transicion_estados[7][23] = 0; //llega otro caracter -> descarto y reinicio
		
		
		
		//fila 8 -> doubles ultra flasheros  ->  .333f+55   y   .333f-66
		
		this.matriz_transicion_estados[8][0] = 0; //en E8 llega una letra l -> descarto y voy a E0
		this.matriz_transicion_estados[8][1] = 8; //en E8 llega un digito -> voy a E8
		this.matriz_transicion_estados[8][2] = 0; //en E8 llega un _ -> descartes y voy a E0
		this.matriz_transicion_estados[8][3] = 0; //llega una i -> descarto y reinicio
		this.matriz_transicion_estados[8][4] = 0; //llega un . -> descarto y reinicio
		this.matriz_transicion_estados[8][5] = 0; //llega una f -> descarto y reinicio
		this.matriz_transicion_estados[8][6] = -1; //llega un blanco ' ' -> voy a EF
		this.matriz_transicion_estados[8][7] = -1; //llega un + -> voy a EF
		this.matriz_transicion_estados[8][8] = -1; //llega un - -> voy a EF
		this.matriz_transicion_estados[8][9] = -1; //llega un * -> voy a EF
		this.matriz_transicion_estados[8][10] = -1; //llega un / -> voy a EF						
		this.matriz_transicion_estados[8][11] = -1; //llega un = -> voy a EF !!!VER!!! -> .333f55= ??
		
		
		this.matriz_transicion_estados[8][20] = -1; //llega un ; -> voy a EF
		this.matriz_transicion_estados[8][21] = 0; //llega un % -> limpio y reinicio
						
		this.matriz_transicion_estados[8][23] = 0; //llega otro caracter -> descarto y reinicio
				

		
		//fila 9 -> comparadores 
		
		this.matriz_transicion_estados[9][0] = 0; //en E9 llega una letra l -> descarto y voy a E0
		this.matriz_transicion_estados[9][1] = 0; //en E9 llega un digito -> descarto y reinicio
		this.matriz_transicion_estados[9][2] = 0; //en E9 llega un _ -> descartes y voy a E0
		this.matriz_transicion_estados[9][3] = 0; //en E9 llega una i -> descarto y reinicio
		this.matriz_transicion_estados[9][4] = 0; //en E9 llega un . -> descarto y reinicio
		this.matriz_transicion_estados[9][5] = 0; //en E9 llega una f -> descarto y reinicio
		this.matriz_transicion_estados[9][6] = -1; //en E9 llega un blanco ' ' -> voy a EF
		this.matriz_transicion_estados[9][7] = 0; //en E9 llega un + -> limpio y reinicio
		this.matriz_transicion_estados[9][8] = 0; //en E9 llega un - -> limpioy reinicio
		this.matriz_transicion_estados[9][9] = 0; //en E9 llega un * -> limpio y reinicio
		this.matriz_transicion_estados[9][10] = 0; //en E9 llega un / -> limpio y reinicio								
		this.matriz_transicion_estados[9][11] = -1; //a E9 llega un = -> voy a EF

		
		this.matriz_transicion_estados[9][20] = 0; //en E9 llega un ; -> limpio y reinicio
		this.matriz_transicion_estados[9][21] = 0; //en E9 llega un % -> limpio y reinicio
								
		this.matriz_transicion_estados[9][23] = 0; //a E9 llega otro caracter -> descarto y reinicio
						

		
		//fila 10 -> definicion de comentario
		
		this.matriz_transicion_estados[10][0] = 0; //en E10 llega una letra l -> descarto y voy a E0
		this.matriz_transicion_estados[10][1] = 0; //en E10 llega un digito -> descarto y reinicio
		this.matriz_transicion_estados[10][2] = 0; //en E10 llega un _ -> descartes y voy a E0
		this.matriz_transicion_estados[10][3] = 0; //en E10 llega una i -> descarto y reinicio
		this.matriz_transicion_estados[10][4] = 0; //en E10 llega un . -> descarto y reinicio
		this.matriz_transicion_estados[10][5] = 0; //en E10 llega una f -> descarto y reinicio
		this.matriz_transicion_estados[10][6] = 0; //en E10 llega un blanco ' ' -> voy a EF
		this.matriz_transicion_estados[10][7] = 0; //en E10 llega un + -> limpio y reinicio
		this.matriz_transicion_estados[10][8] = 0; //en E10 llega un - -> limpioy reinicio
		this.matriz_transicion_estados[10][9] = 0; //en E10 llega un * -> limpio y reinicio
		this.matriz_transicion_estados[10][10] = 0; //en E10 llega un / -> limpio y reinicio										
		this.matriz_transicion_estados[10][11] = 0; //a E10 llega un = -> voy a EF
		
		
		this.matriz_transicion_estados[10][20] = 0; //en E10 llega un ; -> limpio y reinicio
		this.matriz_transicion_estados[10][21] = 11; //en E10 llega un % -> voy a E11
										
		this.matriz_transicion_estados[10][23] = 0; //a E10 llega otro caracter -> descarto y reinicio
								

		
		//fila 11 -> cuerpo del comentario
		this.matriz_transicion_estados[11][0] = 11; //en E11 llega una letra l -> agrego caracter
		this.matriz_transicion_estados[11][1] = 11; //en E11 llega un digito -> agrego caracter
		this.matriz_transicion_estados[11][2] = 11; //en E11 llega un _ -> agrego caracter
		this.matriz_transicion_estados[11][3] = 11; //en E11 llega una i -> agrego caracter
		this.matriz_transicion_estados[11][4] = 11; //en E11 llega un . -> agrego caracter
		this.matriz_transicion_estados[11][5] = 11; //en E11 llega una f -> agrego caracter
		this.matriz_transicion_estados[11][6] = 11; //en E11 llega un blanco ' ' -> agrego caracter
		this.matriz_transicion_estados[11][7] = 11; //en E11 llega un + -> agrego caracter
		this.matriz_transicion_estados[11][8] = 11; //en E11 llega un - -> agrego caracter
		this.matriz_transicion_estados[11][9] = 11; //en E11 llega un * -> agrego caracter
		this.matriz_transicion_estados[11][10] = 11; //en E11 llega un / -> agrego caracter										
		this.matriz_transicion_estados[11][11] = 11; //en E11 llega un = -> agrego caracter
		
		
		this.matriz_transicion_estados[11][20] = 11; //en E11 llega un ; -> agrego caracter
		this.matriz_transicion_estados[11][21] = 11; //en E11 llega un % -> agrego caracter
										
		this.matriz_transicion_estados[11][23] = 11; //a E11 llega otro caracter -> agrego caracter
		
		
		
		
		//fila 12 -> estado final 
		this.matriz_transicion_estados[12][0] = 0; //en EF reinicio
		this.matriz_transicion_estados[12][1] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[12][2] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[12][3] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[12][4] = 0; //en EF reinicio
		this.matriz_transicion_estados[12][5] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[12][6] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[12][7] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[12][8] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[12][9] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[12][10] = 0; //en EF entrego token y reinicio								
		this.matriz_transicion_estados[12][11] = 0; //en EF entrego token y reinicio

		
		this.matriz_transicion_estados[12][20] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[12][21] = 0; //en EF muestro comentario y reinicio
								
		this.matriz_transicion_estados[12][23] = 0; //en EF entrego token y reinicio

	}
	
	
	
	public void inicializarMatrizAccionesSemanticas() {
		//Acciones Semanticas
		AccionSemantica AS1 = new InicializarBuffer();
		AccionSemantica AS2 = new AgregarCaracter();
		AccionSemantica AS3 = new LlegaTokenValido(); 
		AccionSemantica AS4 = new DescartarBuffer(); 
		AccionSemantica ASF = new EntregarTokenYReiniciar(this.tds);

		
		//fila 0 -> estado inicial
		this.matriz_acciones_semanticas[0][0] = AS1; //InicializarBuffer(); //a E0 le llega letra -> inicializo buffer
		this.matriz_acciones_semanticas[0][1] = AS1; //InicializarBuffer(); //a E0 le llega digito -> inicializo buffer
		this.matriz_acciones_semanticas[0][2] = AS4; //DescartarBuffer(); //a E0 le llega _ -> descarto y !!!ERROR!!!  -> comienza con _
		this.matriz_acciones_semanticas[0][3] = AS1; //InicializarBuffer(); //a E0 le llega i -> inicializo buffer
		this.matriz_acciones_semanticas[0][4] = AS1; //InicializarBuffer(); //a E0 le llega . -> inicializo buffer
		this.matriz_acciones_semanticas[0][5] = AS1; //InicializarBuffer(); //a E0 le llega f -> inicializo buffer
		this.matriz_acciones_semanticas[0][6] = AS4; //a inicio le llega blanco ' ' -> consumo token
		this.matriz_acciones_semanticas[0][7] = AS3; //EntregarToken(); //a E0 le llega +
		this.matriz_acciones_semanticas[0][8] = AS3; //EntregarToken(); //a E0 le llega - 
		this.matriz_acciones_semanticas[0][9] = AS3; //EntregarToken(); //a E0 le llega * 
		this.matriz_acciones_semanticas[0][10] = AS3; //EntregarToken(); //a E0 le llega / 	
		this.matriz_acciones_semanticas[0][11] = AS3; //EntregarToken(); //a E0 le llega =

		
		this.matriz_acciones_semanticas[0][20] = AS3; //EntregarToken(); //a E0 le llega ;
		this.matriz_acciones_semanticas[0][21] = AS1; //InicializarBuffer(); //a E0 le llega %
		
		this.matriz_acciones_semanticas[0][23] = AS4; //Descartar(); //a E0 le llega otro caracter
		
		
		
		//fila 1 -> identificadores
		
		this.matriz_acciones_semanticas[1][0] = AS2; //AgregarCaracter(); //a E1 le llega letra -> agrego caracter a buffer
		this.matriz_acciones_semanticas[1][1] = AS2; //AgregarCaracter(); //a E1 le llega digito -> agrego caracter a buffer
		this.matriz_acciones_semanticas[1][2] = AS2; //AgregarCaracter(); //a E1 le llega _ -> agrego _ a buffer
		this.matriz_acciones_semanticas[1][3] = AS2; //AgregarCaracter(); //a E1 le llega i -> agrego i a buffer
		this.matriz_acciones_semanticas[1][4] = AS4; //DescartarBuffer(); //a E1 le llega . -> limpio y reinicio !!ERROR!!?? -> ll. 
		this.matriz_acciones_semanticas[1][5] = AS2; //AgregarCaracter(); //a E1 le llega f -> agrego caracter a buffer
		this.matriz_acciones_semanticas[1][6] = AS3; //LlegaTokenValido(); //a E1 le llega blanco ' ' -> entrego token y descarto blanco
		this.matriz_acciones_semanticas[1][7] = AS3; //LlegaTokenValido(); //a E1 le llega + -> entrego token y divido
		this.matriz_acciones_semanticas[1][8] = AS3; //LlegaTokenValido(); //a E1 le llega - -> entrego token y divido
		this.matriz_acciones_semanticas[1][9] = AS3; //LlegaTokenValido(); //a E1 le llega * -> entrego token y divido
		this.matriz_acciones_semanticas[1][10] = AS3; //LlegaTokenValido(); //a E1 le llega / -> entrego token y divido
		this.matriz_acciones_semanticas[1][11] = AS3; //LlegaTokenValido(); //a E1 le llega '=' -> entrego token 
		
		
		this.matriz_acciones_semanticas[1][20] = AS3; //LlegaTokenValido(); //a E1 le llega ; -> entrego token y descarto blanco
		this.matriz_acciones_semanticas[1][21] = AS4; //DescartarBuffer(); //a E1 le llega % -> descarto buffer

		this.matriz_acciones_semanticas[1][23] = AS3; //LlegaTokenValido(); //a E1 le llega otro caracter -> entrego token
		
		
		
		
		//fila 2 -> digitos
		this.matriz_acciones_semanticas[2][0] = AS4; //DescartarBuffer(); //en E2 le llega una letra -> !!ERROR!!  descarto y vuelvo a inicio? o entregoToken?
		this.matriz_acciones_semanticas[2][1] = AS2; //AgregarCaracter(); //en E2 le llega un digito -> 
		this.matriz_acciones_semanticas[2][2] = AS2; //AgregarCaracter(); //en E2 le llega _ -> agrego _ a buffer !!VER!!?? CARACTER ESPECIAL o ERROR??
		this.matriz_acciones_semanticas[2][3] = AS4; //DescartarBuffer(); //en E2 le llega i -> descarto. limpio y reinicio
		this.matriz_acciones_semanticas[2][4] = AS4; //DescartarBuffer(); //en E2 le llega . -> limpio y reinicio
		this.matriz_acciones_semanticas[2][5] = AS2; //AgregarCaracter(); //en E2 le llega f -> agrego caracter a buffer  !!VER!! -> 55f ??		
		this.matriz_acciones_semanticas[2][6] = AS3; //EntregarToken(); //en E2 le llega blanco ' ' -> entrego token tipo constante
		this.matriz_acciones_semanticas[2][7] = AS3; //EntregarToken(); //en E2 le llega + -> entrego token tipo constante
		this.matriz_acciones_semanticas[2][8] = AS3; //EntregarToken(); //en E2 le llega - -> entrego token tipo constante
		this.matriz_acciones_semanticas[2][9] = AS3; //EntregarToken(); //en E2 le llega * -> entrego token tipo constante
		this.matriz_acciones_semanticas[2][10] = AS3; //EntregarToken(); //en E2 le llega / -> entrego token tipo constante
		this.matriz_acciones_semanticas[2][11] = AS3; //EntregarToken(); //en E2 le llega = -> entrego token tipo constante

		
		this.matriz_acciones_semanticas[2][20] = AS3; //EntregarToken(); //en E2 le llega ; -> entrego token tipo constante
		this.matriz_acciones_semanticas[2][21] = AS4; //DescartarBuffer(); //en E2 le llega % -> descarto buffer
		
		this.matriz_acciones_semanticas[2][23] = AS3; //EntregarToken(); //en E2 le llega otro caracter -> entrego token tipo constante
		
		
		
		
		//fila 3 -> constante especial _i
		
		this.matriz_acciones_semanticas[3][0] = AS4; //DescartarBuffer(); //en E3 le llega una letra -> descarto y vuelvo a inicio? o entregoToken?
		this.matriz_acciones_semanticas[3][1] = AS4; //DescartarBuffer(); //en E3 le llega un digito -> descarto
		this.matriz_acciones_semanticas[3][2] = AS4; //DescartarBuffer(); //en E3 le llega _ -> descarto buffer
		this.matriz_acciones_semanticas[3][3] = AS3; //TokenValido(); //enE3 le llega i -> agrego caracter y entrego token
		this.matriz_acciones_semanticas[3][4] = AS4; //DescartarBuffer(); //en E3 le llega . -> !!ERROR!! -> 55_.   notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][5] = AS4; //DescartarBuffer(); //en E3 le llega f -> !!ERROR!! -> 55_f   notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][6] = AS4; //DescartarBuffer(); //en E3 le llega blanco ' ' -> !!ERROR!! -> 55_   notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][7] = AS4; //DescartarBuffer(); //en E3 le llega + -> !!ERROR!! ->  55_+  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][8] = AS4; //DescartarBuffer(); //en E3 le llega - -> !!ERROR!! ->  55_-  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][9] = AS4; //DescartarBuffer(); //en E3 le llega * -> !!ERROR!! ->  55_*  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][10] = AS4; //DescartarBuffer(); //en E3 le llega / -> !!ERROR!! ->  55_/  notifico, limpio y reinicio		
		this.matriz_acciones_semanticas[3][11] = AS4; //DescartarBuffer(); //en E3 le llega = -> !!ERROR!! ->  55_=  notifico, limpio y reinicio

		
		this.matriz_acciones_semanticas[3][20] = AS4; //DescartarBuffer(); //en E3 le llega ; -> !!ERROR!! ->  55_;  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][21] = AS4; //DescartarBuffer(); //en E1 le llega % -> !!ERROR!! ->  55_%  notifico, limpio y reinicio
	
		this.matriz_acciones_semanticas[3][23] = AS3; //EntregarTokenIndividual(); //en E3 le llega otro caracter -> entrego token tipo constante
	
		
		//VER EL CASO DEL ESTADO FINAL!! si llega otro caracter... reinicio?
		
		
		
		//fila 4 -> 
		
		this.matriz_acciones_semanticas[4][0] = AS4; //DescartarBuffer(); //en E4 le llega una letra -> descarto y vuelvo a inicio? o entregoToken?
		this.matriz_acciones_semanticas[4][1] = AS2; //AgregarCaracter(); //en E4 le llega un digito -> 
		this.matriz_acciones_semanticas[4][2] = AS4; //DescartarBuffer(); //en E4 le llega _ -> rene
		this.matriz_acciones_semanticas[4][3] = AS4; //DescartarBuffer(); //en E4 le llega i -> descartes
		this.matriz_acciones_semanticas[4][4] = AS4; //DescartarBuffer(); //en E4 le llega . -> limpio y reinicio
		this.matriz_acciones_semanticas[4][5] = AS4; //DescartarBuffer(); //en E4 le llega f -> limpio y reinicio
		this.matriz_acciones_semanticas[4][6] = AS4; //DescartarBuffer(); //en E4 le llega blanco ' ' -> limpio y reinicio
		this.matriz_acciones_semanticas[4][7] = AS4; //DescartarBuffer(); //en E4 le llega + -> limpio y reinicio
		this.matriz_acciones_semanticas[4][8] = AS4; //DescartarBuffer(); //en E4 le llega - -> limpio y reinicio
		this.matriz_acciones_semanticas[4][9] = AS4; //DescartarBuffer(); //en E4 le llega * -> limpio y reinicio
		this.matriz_acciones_semanticas[4][10] = AS4; //DescartaBuffer(); //en E4 le llega / -> limpio y reinicio		
		this.matriz_acciones_semanticas[4][11] = AS4; //DescartarBuffer(); //en E4 le llega = -> limpio y reinicio

		
		this.matriz_acciones_semanticas[4][20] = AS4; //DescartarBuffer(); //en E4 le llega ; -> limpio y reinicio
		this.matriz_acciones_semanticas[4][21] = AS4; //DescartarBuffer(); //en E1 le llega % -> descarto buffer
	
		this.matriz_acciones_semanticas[4][23] = AS4; //DescartarBuffer(); //en E4 le llega otro caracter -> limpio y reinicio
		
				
		
		
		
		//fila 5 -> constantes doubles  .666 
		
		this.matriz_acciones_semanticas[5][0] = AS4; //DescartarBuffer(); //en E5 le llega una letra -> !!ERROR!! .666a  descarto y vuelvo a inicio
		this.matriz_acciones_semanticas[5][1] = AS2; //AgregarCaracter(); //en E5 le llega un digito -> agrego
		this.matriz_acciones_semanticas[5][2] = AS4; //DescartarBuffer(); //en E5 le llega _ -> !!ERROR!! .666_  limpio y reinicio
		this.matriz_acciones_semanticas[5][3] = AS4; //DescartarBuffer(); //en E5 le llega i -> !!ERROR!! .666i  limpio y reinicio
		this.matriz_acciones_semanticas[5][4] = AS4; //DescartarBuffer(); //en E5 le llega . -> !!ERROR!! .666.  limpio y reinicio
		this.matriz_acciones_semanticas[5][5] = AS2; //AgregarCaracter(); //en E5 le llega f -> agrego caracter a buffer
		this.matriz_acciones_semanticas[5][6] = AS3; //EntregarToken(); //en E5 le llega blanco ' ' -> entrego token tipo constante
		this.matriz_acciones_semanticas[5][7] = AS3; //EntregarToken(); //en E5 le llega + -> entrego token tipo constante double
		this.matriz_acciones_semanticas[5][8] = AS3; //EntregarToken(); //en E5 le llega - -> entrego token tipo constante double
		this.matriz_acciones_semanticas[5][9] = AS3; //EntregarToken(); //en E5 le llega * -> entrego token tipo constante double
		this.matriz_acciones_semanticas[5][10] = AS3; //EntregarToken(); //en E5 le llega / -> entrego token tipo constante double
		this.matriz_acciones_semanticas[5][11] = AS3; //EntregarToken(); //en E5 le llega = -> entrego token tipo constante double
		
		
		this.matriz_acciones_semanticas[5][20] = AS3; //EntregarToken(); //a E5 le llega ; -> entrego token tipo constante double
		this.matriz_acciones_semanticas[5][21] = AS4; //DescartarBuffer(); //a E1 le llega % -> descarto buffer
	
		this.matriz_acciones_semanticas[5][23] = AS4; //Descartar(); //a E4 le llega otro caracter -> entrego token tipo constante
		
		
		

		//fila 6 -> 
		
		this.matriz_acciones_semanticas[6][0] = AS4; //DescartarBuffer(); //a E6 le llega una letra -> descarto y vuelvo a inicio? o entregoToken?
		this.matriz_acciones_semanticas[6][1] = AS2; //AgregarCaracter(); //a E6 le llega un digito -> 
		this.matriz_acciones_semanticas[6][2] = AS4; //DescartarBuffer(); //a E6 le llega _ -> agrego caracter a buffer
		this.matriz_acciones_semanticas[6][3] = AS4; //DescartarBuffer(); //a E6 le llega i -> agrego caracter a buffer
		this.matriz_acciones_semanticas[6][4] = AS4; //DescartarBuffer(); //a E6 le llega . -> limpio y reinicio
		this.matriz_acciones_semanticas[6][5] = AS4; //DescartarBuffer(); //a E6 le llega f -> agrego caracter a buffer
		this.matriz_acciones_semanticas[6][6] = AS4; //DescartarBuffer(); //a E6 le llega blanco ' ' -> limpio y notifico error
		this.matriz_acciones_semanticas[6][7] = AS2; //AgregarCaracter(); //a E6 le llega + -> agrego caracter +
		this.matriz_acciones_semanticas[6][8] = AS2; //AgregarCaracter(); //a E6 le llega - -> agregp caracter -
		this.matriz_acciones_semanticas[6][9] = AS4; //DescartarBuffer(); //a E6 le llega * -> limpio y reinicio
		this.matriz_acciones_semanticas[6][10] = AS4; //DescartarBuffer(); //a E6 le llega / -> lmpio y reinicio			
		this.matriz_acciones_semanticas[6][11] = AS4; //DescartarBuffer(); //a E6 le llega = -> limpio y reinicio
		
		
		this.matriz_acciones_semanticas[6][20] = AS4; //DescartarBuffer(); //a E6 le llega ; -> limpio y reinicio
		this.matriz_acciones_semanticas[6][21] = AS4; //DescartarBuffer(); //a E1 le llega % -> descarto buffer
	
		this.matriz_acciones_semanticas[6][23] = AS4; //DescartarBuffer(); //a E6 le llega otro caracter -> entrego token tipo constante
		
		
		

		//fila (estado) 7
		this.matriz_acciones_semanticas[7][0] = AS4; //DescartarBuffer(); //a E7 le llega una letra -> descarto y vuelvo a inicio? o entregoToken?
		this.matriz_acciones_semanticas[7][1] = AS2; //AgregarCaracter(); //a E7 le llega un digito -> agrego caracter
		this.matriz_acciones_semanticas[7][2] = AS4; //DescartarBuffer(); //a E7 le llega _ -> agrego caracter a buffer
		this.matriz_acciones_semanticas[7][3] = AS4; //Descartar(); //a E7 le llega i -> agrego caracter a buffer
		this.matriz_acciones_semanticas[7][4] = AS4; //Descartar(); //a E7 le llega . -> limpio y reinicio
		this.matriz_acciones_semanticas[7][5] = AS4; //Descartar(); //a E7 le llega f -> agrego caracter a buffer
		this.matriz_acciones_semanticas[7][6] = AS4; //Descartar(); //a E7 le llega blanco ' ' -> limpio y notifico error
		this.matriz_acciones_semanticas[7][7] = AS4; //Descartar(); //a E7 le llega + -> agrego caracter +
		this.matriz_acciones_semanticas[7][8] = AS4; //Descartar(); //a E7 le llega - -> agregp caracter -
		this.matriz_acciones_semanticas[7][9] = AS4; //Descartar(); //a E7 le llega * -> limpio y reinicio
		this.matriz_acciones_semanticas[7][10] = AS4; //Descartar(); //a E7 le llega / -> lmpio y reinicio		
		this.matriz_acciones_semanticas[7][11] = AS4; //Descartar(); //a E6 le llega = -> limpio y reinicio
		
		
		this.matriz_acciones_semanticas[7][20] = AS4; //Descartar(); //a E6 le llega ; -> limpio y reinicio
		this.matriz_acciones_semanticas[7][21] = AS4; //DescartarBuffer(); //a E1 le llega % -> descarto buffer
	
		this.matriz_acciones_semanticas[7][23] = AS4; //Descartar(); //a E4 le llega otro caracter -> entrego token tipo constante
		

		

		//fila (estado) 8
		this.matriz_acciones_semanticas[8][0] = AS4; //DescartarBuffer(); //a E8 le llega una letra -> descarto y vuelvo a inicio? o entregoToken?
		this.matriz_acciones_semanticas[8][1] = AS2; //AgregarCaracter(); //a E8 le llega un digito -> 
		this.matriz_acciones_semanticas[8][2] = AS4; //DescartarBuffer(); //a E8 le llega _ -> agrego caracter a buffer
		this.matriz_acciones_semanticas[8][3] = AS4; //DescartarBuffer(); //a E8 le llega i -> agrego caracter a buffer
		this.matriz_acciones_semanticas[8][4] = AS4; //DescartarBuffer(); //a E8 le llega . -> limpio y reinicio
		this.matriz_acciones_semanticas[8][5] = AS4; //DescartarBuffer(); //a E8 le llega f -> agrego caracter a buffer
		this.matriz_acciones_semanticas[8][6] = AS3; //EntregarToken(); //a E8 le llega blanco ' ' -> limpio y notifico error
		this.matriz_acciones_semanticas[8][7] = AS3; //EntregarToken(); //a E8 le llega + -> agrego caracter +
		this.matriz_acciones_semanticas[8][8] = AS3; //EntregarToken(); //a E8 le llega - -> agregp caracter -
		this.matriz_acciones_semanticas[8][9] = AS3; //EntregarToken(); //a E8 le llega * -> limpio y reinicio
		this.matriz_acciones_semanticas[8][10] = AS3; //EntregarToken(); //a E8 le llega / -> lmpio y reinicio		
		this.matriz_acciones_semanticas[8][11] = AS3; //EntregarToken(); //a E6 le llega = -> limpio y reinicio

		
		this.matriz_acciones_semanticas[8][20] = AS3; //EntregarToken(); //a E6 le llega ; -> limpio y reinicio
		this.matriz_acciones_semanticas[8][21] = AS4; //DescartarBuffer(); //a E1 le llega % -> descarto buffer
	
		this.matriz_acciones_semanticas[8][23] = AS4; //DescartarBuffer(); //a E4 le llega otro caracter -> entrego token tipo constante
		
		

		
		//fila (estado) 9
		this.matriz_acciones_semanticas[9][0] = AS4; //DescartarBuffer(); //en E9 le llega una letra -> descarto y vuelvo a inicio? o entregoToken?
		this.matriz_acciones_semanticas[9][1] = AS4; //DescartarBuffer(); //en E9 le llega un digito -> 
		this.matriz_acciones_semanticas[9][2] = AS4; //DescartarBuffer(); //en E9 le llega _ -> agrego caracter a buffer
		this.matriz_acciones_semanticas[9][3] = AS4; //DescartarBuffer(); //en E9 le llega i -> agrego caracter a buffer
		this.matriz_acciones_semanticas[9][4] = AS4; //DescartarBuffer(); //en E9 le llega . -> limpio y reinicio
		this.matriz_acciones_semanticas[9][5] = AS4; //DescartarBuffer(); //en E9 le llega f -> agrego caracter a buffer
		this.matriz_acciones_semanticas[9][6] = AS3; //EntregarToken(); //en E9 le llega blanco ' ' -> entregar token
		this.matriz_acciones_semanticas[9][7] = AS4; //DescartarBuffer(); //en E9 le llega + -> limpio y reinicio
		this.matriz_acciones_semanticas[9][8] = AS4; //DescartarBuffer(); //en E9 le llega - -> limpio y reinicio
		this.matriz_acciones_semanticas[9][9] = AS4; //DescartarBuffer(); //en E9 le llega * -> limpio y reinicio
		this.matriz_acciones_semanticas[9][10] = AS4; //DescartarBuffer(); //en E9 le llega / -> lmpio y reinicio		
		this.matriz_acciones_semanticas[9][11] = AS3; //EntregarToken(); //en E9 le llega = -> entregar token
		
		
		this.matriz_acciones_semanticas[9][20] = AS4; //DescartarBuffer(); //en E9 le llega ; -> limpio y reinicio
		this.matriz_acciones_semanticas[9][21] = AS4; //DescartarBuffer(); //en E9 le llega % -> descarto buffer
	
		this.matriz_acciones_semanticas[9][23] = AS4; //DescartarBuffer(); //a E4 le llega otro caracter -> entrego token tipo constante
		

		
		
		//fila (estado) 10
		this.matriz_acciones_semanticas[10][0] = AS4; //DescartarBuffer(); //a E10 le llega una letra -> descarto y vuelvo a inicio? o entregoToken?
		this.matriz_acciones_semanticas[10][1] = AS4; //DescartarBuffer(); //a E10 le llega un digito -> 
		this.matriz_acciones_semanticas[10][2] = AS4; //DescartarBuffer(); //a E10 le llega _ -> agrego caracter a buffer
		this.matriz_acciones_semanticas[10][3] = AS4; //DescartarBuffer(); //a E10 le llega i -> agrego caracter a buffer
		this.matriz_acciones_semanticas[10][4] = AS4; //DescartarBuffer(); //a E10 le llega . -> limpio y reinicio
		this.matriz_acciones_semanticas[10][5] = AS4; //DescartarBuffer(); //a E10 le llega f -> agrego caracter a buffer
		this.matriz_acciones_semanticas[10][6] = AS3; //DescartarBuffer(); //a E10 le llega blanco ' ' -> entregar token
		this.matriz_acciones_semanticas[10][7] = AS4; //DescartarBuffer(); //a E10 le llega + -> limpio y reinicio
		this.matriz_acciones_semanticas[10][8] = AS4; //DescartarBuffer(); //a E10 le llega - -> limpio y reinicio
		this.matriz_acciones_semanticas[10][9] = AS4; //DescartarBuffer(); //a E10 le llega * -> limpio y reinicio
		this.matriz_acciones_semanticas[10][10] = AS4; //DescartarBuffer(); //a E10 le llega / -> lmpio y reinicio				
		this.matriz_acciones_semanticas[10][11] = AS4; //DescartarBuffer(); //a E10 le llega = -> limpio y reinicio

		
		this.matriz_acciones_semanticas[10][20] = AS4; //DescartarBuffer(); //a E10 le llega ; -> limpio y reinicio
		this.matriz_acciones_semanticas[10][21] = AS2; //AgregarCaracter(); //en E10 le llega % -> agrego caracter
			
		this.matriz_acciones_semanticas[10][23] = AS4; //DescartarBuffer(); //a E4 le llega otro caracter -> entrego token tipo constante
		

		
		
		//fila (estado) 11
		this.matriz_acciones_semanticas[11][0] = AS2; //AgregarCaracter(); //a E11 le llega una letra -> agrego caracter
		this.matriz_acciones_semanticas[11][1] = AS2; //AgregarCaracter(); //a E11 le llega un digito -> agrego caracter
		this.matriz_acciones_semanticas[11][2] = AS2; //AgregarCaracter(); //a E11 le llega _ -> agrego caracter a buffer
		this.matriz_acciones_semanticas[11][3] = AS2; //AgregarCaracter(); //a E11 le llega i -> agrego caracter a buffer
		this.matriz_acciones_semanticas[11][4] = AS2; //AgregarCaracter(); //a E11 le llega . -> agrego caracter
		this.matriz_acciones_semanticas[11][5] = AS2; //AgregarCaracter(); //a E11 le llega f -> agrego caracter a buffer
		this.matriz_acciones_semanticas[11][6] = AS2; //AgregarCaracter(); //a E11 le llega blanco ' ' -> agrego caracter
		this.matriz_acciones_semanticas[11][7] = AS2; //AgregarCaracter(); //a E11 le llega + -> agrego caracter
		this.matriz_acciones_semanticas[11][8] = AS2; //AgregarCaracter(); //a E11 le llega - -> agrego caracter
		this.matriz_acciones_semanticas[11][9] = AS2; //AgregarCaracter(); //a E11 le llega * -> agrego
		this.matriz_acciones_semanticas[11][10] = AS2; //AgregarCaracter(); //a E11 le llega / -> agrego						
		this.matriz_acciones_semanticas[11][11] = AS2; //AgregarCaracter(); //a E11 le llega = -> agrego
		
		
		this.matriz_acciones_semanticas[11][20] = AS2; //AgregarCaracter(); //en E11 le llega ; -> agrego caracter
		this.matriz_acciones_semanticas[11][21] = AS2; //AgregarCaracter(); //en E11 le llega % -> agrego caracter
					
		this.matriz_acciones_semanticas[11][23] = AS2; //AgregarCaracter(); //a E11 le llega otro caracter -> agrego caracter
		

		
		
		
		//fila EstadoFinal
		this.matriz_acciones_semanticas[12][0] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[12][1] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[12][2] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[12][3] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[12][4] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[12][5] = ASF; 	
		this.matriz_acciones_semanticas[12][6] = ASF; 
		this.matriz_acciones_semanticas[12][7] = ASF;
		this.matriz_acciones_semanticas[12][8] = ASF; 
		this.matriz_acciones_semanticas[12][9] = ASF; 
		this.matriz_acciones_semanticas[12][10] = ASF; 
		this.matriz_acciones_semanticas[12][11] = ASF; 
		
		
		this.matriz_acciones_semanticas[12][20] = ASF; 
		this.matriz_acciones_semanticas[12][21] = ASF; //MostrarComentario() y descartar; //a E12 le llega % -> descarto buffer

		this.matriz_acciones_semanticas[12][23] = ASF; 
	}
	
	
	
	
	public void abrirCargarArchivo() {
		///Lee fichero y lo carga en el String privado 'codigo'
		
		File archivo = null; 
		FileReader fr = null;
	    BufferedReader br = null;
	    
        String linea;
        int nro_linea = 0;
        
        try {
        	archivo = new File (this.dir_codigo);	        
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
        			
		int estado_actual = 0; 
		int nro_columna = -1; //nro de columna depende del tipo de caracter. por defecto arranco con otro

		char caracter = ' ';
		int ascii;
			
		Token titi = null;
		
		
		while (this.leer_caracter & !fin){ 			
			this.pos_actual = this.ultima_pos; //retomo a partir de la ultima posicion leida	
			//System.out.println("\n Frontera -> " +  this.pos_actual + " ,  limite -> "+ this.ultima_pos +" , TOTAL -> "+this.total_caracteres);
	
			while (estado_actual != -1) {
					
				caracter = this.codigo.charAt(this.pos_actual); 
				ascii = (int)caracter;
				
				//System.out.println("\n");
				//System.out.println("Caracter actual "+caracter+" , ASCII "+(int)caracter);					
										
				nro_columna = getColumnaCaracter(caracter);
				
				//System.out.println("Voy a Posicion matriz: " + estado_actual + " , " + nro_columna);				
				
				AS = this.matriz_acciones_semanticas[estado_actual][nro_columna];
					
				
				AS.ejecutar(caracter, this.nro_linea);
					
				
				estado_actual = matriz_transicion_estados[estado_actual][nro_columna];	
				
				this.pos_actual++;
				
				
				//si es salto de linea
				if (ascii == 10) { 
					//System.out.println("\n \n \n Salto de linea \n \n \n");
					this.nro_linea++;
					estado_actual = 0;
					
					//condicion de corte
					if (this.nro_linea > this.total_lineas) {  // si me paso corta todo
						System.out.println();	
						//System.out.println("FIN de codigo!! nro linea "+this.nro_linea+" > "+this.total_lineas);
						System.out.println();	
						//System.out.println("Frontera -> "+this.pos_actual+"  ,   total caracteres -> "+this.total_caracteres);
						System.out.println();	

						this.leer_caracter = false;
						this.fin = true;
						estado_actual = -1;
						this.pos_actual ++;
					}
				}
			}
			
			
			//llega el estado final
			if (!fin) {	//si no es el fin de archivo (String), ejecuto ASFinal (entrego Token)
					
				estado_actual = this.estado_final; 
				
				AS = this.matriz_acciones_semanticas[estado_actual][nro_columna];
				
				AS.ejecutar(caracter, this.nro_linea);
				
				//System.out.println("\n inicio -> " +  this.pos_actual + " ,  actual -> "+ this.ultima_pos +" , TOTAL -> "+this.total_caracteres);

				
				//devuelvo token, dejo de leer y actualizo posicion
				titi = AS.getToken();
				
				this.leer_caracter = false; 
				
				//System.out.println("Inicio -> " +  ultima_pos + " ,  frontera -> "+ pos_actual +" ,  total -> " + this.total_caracteres+"\n");
				
				this.ultima_pos = this.pos_actual; 
				
				System.out.println("\n Lexico  ->  Linea: "+ this.nro_linea + " , Token: "+ titi.getLexema()+" ,  tipo: "+titi.getTipo()+" \n");
				}
		}
		return titi;
	}

	
	
	public int getColumnaCaracter(char c) {
	
		int nro_columna=23; //inicializo con otro caracter
		
		
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
		
		
		if (c == ';') { nro_columna=20; };
		if (c == '%') { nro_columna=21; };
		//...
		
		return nro_columna; 
	}
	
	
	
	public Token getToken() { 
		this.leer_caracter = true;
		
		Token t = this.leerCodigo();
		
		if (t != null) {
			//System.out.println("yylex Retorno ->  " + t.getLexema()+" , "+t.getTipo()); 
			return t;
		} else {
			System.out.println("Fin de archivo!");
			this.fin = true;
		}
		
		//System.out.println("\n \n ---------------------------------------------------\n");
		return null;
	}
	
	

	public boolean quedanTokens() {
		if (this.fin) {
			return false;
		}
		return true;
	}
	
	
	public void mostrarTablaDeSimbolos() {
		this.tds.mostrarSimbolos();
		System.out.println();
	}
	

}
