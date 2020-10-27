package analizador_lexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
//import java.io.StringReader;
//import java.util.ArrayList;
//import java.util.List;

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
	
	private int filas_estados = 15; //de 0 a 14 estados. total?
	private int columnas_caracteres_validos = 25;  
	
	private int estado_final = this.filas_estados - 1;

	
	
	private int matriz_transicion_estados [][];
	private AccionSemantica matriz_acciones_semanticas [][];
	
	
	private TablaDeSimbolos tds;
	
	
	
	
	public AnalizadorLexico(String programa) throws IllegalArgumentException, IllegalAccessException{
		this.dir_codigo = programa;
		
		//por que tengo qie inicializar en lexico y en sintactico la tabla de simbolos?
		//this.tds = new TablaDeSimbolos();
				
		
		this.matriz_transicion_estados = new int[this.filas_estados][this.columnas_caracteres_validos];
		this.inicializarMatrizTransicionEstados();
		
		this.matriz_acciones_semanticas = new AccionSemantica[this.filas_estados][this.columnas_caracteres_validos];
		this.inicializarMatrizAccionesSemanticas();
	}
	
	
	
	public void inicializarMatrizTransicionEstados() {
		//estado inicial
		this.matriz_transicion_estados[0][0] = 1; //llega a E0 una letra l -> voy a E1
		this.matriz_transicion_estados[0][1] = 2; //llega a E0 un digito d -> voy a E2
		this.matriz_transicion_estados[0][2] = 0; //llega a E0 un _ -> descarto y reinicio
		this.matriz_transicion_estados[0][3] = 1; //llega i -> voy a E1
		this.matriz_transicion_estados[0][4] = 4; //llega . -> voy a E4
		this.matriz_transicion_estados[0][5] = 1; //llega f -> voy a E1				
		this.matriz_transicion_estados[0][6] = 0; //llega blanco ' ' -> ciclo en E0
		this.matriz_transicion_estados[0][7] = -1; //llega +  ->  voy a EF
		this.matriz_transicion_estados[0][8] = -1; //llega -  ->  voy a EF
		this.matriz_transicion_estados[0][9] = -1; //llega *  ->  voy a EF
		this.matriz_transicion_estados[0][10] = -1; //llega /  ->  voy a EF
		this.matriz_transicion_estados[0][11] = -1; //llega '=' -> voy a Ef 
		this.matriz_transicion_estados[0][12] = 9; //llega '<'  ->  voy a Ef 
		this.matriz_transicion_estados[0][13] = 9; //llega '>'  ->  voy a Ef 
		this.matriz_transicion_estados[0][14] = 9; //llega '!'  ->  voy a Ef 
		this.matriz_transicion_estados[0][15] = -1; //llega '('  ->  voy a Ef 
		this.matriz_transicion_estados[0][16] = -1; //llega ')'  ->  voy a Ef 
		this.matriz_transicion_estados[0][17] = -1; //llega '{'  ->  voy a Ef 
		this.matriz_transicion_estados[0][18] = -1; //llega '}'  ->  voy a Ef 
		this.matriz_transicion_estados[0][19] = -1; //llega ','  ->  voy a Ef 
		this.matriz_transicion_estados[0][20] = -1; //llega ; -> voy a Ef
		//this.matriz_transicion_estados[0][20] = -1; //llega : -> voy a Ef
		
		this.matriz_transicion_estados[0][21] = 10; //llega % -> voy a E10
		this.matriz_transicion_estados[0][22] = 12; //llega " -> voy a E12
		this.matriz_transicion_estados[0][23] = 0; //llega \n -> ciclo en E0
		this.matriz_transicion_estados[0][24] = 0; //a E0 llega otro caracter -> ciclo en E0
		//falta : y listo..
		
		
		
		//fila 1 -> identificadores   abc 
		
		this.matriz_transicion_estados[1][0] = 1; //llega a E1 una letra l  -> ciclo en E1
		this.matriz_transicion_estados[1][1] = 1; //llega a E1 un digito d  -> ciclo en E1
		this.matriz_transicion_estados[1][2] = 1; //llega a E1 un _  -> ciclo en E1
		this.matriz_transicion_estados[1][3] = 1; //llega una i -> ciclo en E1
		this.matriz_transicion_estados[1][4] = 0; //llega un . -> limpio y reinicio (notifico error! ID.)
		this.matriz_transicion_estados[1][5] = 1; //llega una f -> ciclo en E1
		this.matriz_transicion_estados[1][6] = -1; //llega blanco ' ' -> voy a Ef
		this.matriz_transicion_estados[1][7] = 0; //llega + -> !!ERROR!! abc+
		this.matriz_transicion_estados[1][8] = 0; //llega - -> !!ERROR!! abc-
		this.matriz_transicion_estados[1][9] = 0; //llega * -> !!ERROR!! abc*
		this.matriz_transicion_estados[1][10] = 0; //llega / -> !!ERROR!! abc/
		this.matriz_transicion_estados[1][11] = 0; //llega = -> !!ERROR!! abc=
		this.matriz_transicion_estados[1][12] = 0; //llega <  -> !!ERROR!! abc<
		this.matriz_transicion_estados[1][13] = 0; //llega >  -> !!ERROR!! abc>
		this.matriz_transicion_estados[1][14] = 0; //llega !  ->  !!ERROR!! abc!
		this.matriz_transicion_estados[1][15] = 0; //llega (  ->  !!ERROR!! abc( 
		this.matriz_transicion_estados[1][16] = 0; //llega )  ->  !!ERROR!! abc)
		this.matriz_transicion_estados[1][17] = 0; //llega {  ->  !!ERROR!! abc{ 
		this.matriz_transicion_estados[1][18] = 0; //llega }  ->  !!ERROR!! abc}
		this.matriz_transicion_estados[1][19] = 0; //llega ,  ->  !!ERROR!! abc,		
		this.matriz_transicion_estados[1][20] = 0; //llega ; -> !!ERROR!! abc;
		//this.matriz_transicion_estados[1][20] = 0; //llega ; -> !!ERROR!! abc:
		this.matriz_transicion_estados[1][21] = 0; //llega % -> !!ERROR!! abc%
		this.matriz_transicion_estados[1][22] = 0; //llega " -> !!ERROR!! abc"
		this.matriz_transicion_estados[1][23] = 0; //llega \n -> !!ERROR!! abc\n -> no cierro sentencia con ;!		
		this.matriz_transicion_estados[1][24] = 0; //llega otro caracter -> limpio y reinicio 
	
		
		
		
		//fila 2 -> digitos
		
		this.matriz_transicion_estados[2][0] = 0; //llega a E2 una letra l -> !!ERROR!!  55a
		this.matriz_transicion_estados[2][1] = 2; //llega a E2 un digito d -> ciclo en E2	
		this.matriz_transicion_estados[2][2] = 3; //llega a E2 un _ -> voy a E3  ->  55_
		this.matriz_transicion_estados[2][3] = 0; //llega una i -> !!ERROR!!  55i
		this.matriz_transicion_estados[2][4] = 5; //llega un . -> voy a E5 ->  !!!VER!!! -> 55. ??
		this.matriz_transicion_estados[2][5] = 0; //llega una f -> !!ERROR!!  55f 
		this.matriz_transicion_estados[2][6] = -1; //llega blanco ' ' -> token valido. voy a Ef
		this.matriz_transicion_estados[2][7] = 0; //llega + ->  !!ERROR!! 55+
		this.matriz_transicion_estados[2][8] = 0; //llega - ->  !!ERROR!! 55-
		this.matriz_transicion_estados[2][9] = 0; //llega * ->  !!ERROR!! 55*
		this.matriz_transicion_estados[2][10] = 0; //llega / ->  !!ERROR!! 55/
		this.matriz_transicion_estados[2][11] = 0; //llega = ->  !!ERROR!! 55= 
		this.matriz_transicion_estados[2][12] = 0; //llega <  ->  !!ERROR!! 55< 
		this.matriz_transicion_estados[2][13] = 0; //llega >  ->   !!ERROR!! 55> 
		this.matriz_transicion_estados[2][14] = 0; //llega !  ->   !!ERROR!! 55!
		this.matriz_transicion_estados[2][15] = 0; //llega (  ->   !!ERROR!! 55(
		this.matriz_transicion_estados[2][16] = 0; //llega )  ->   !!ERROR!! 55) 
		this.matriz_transicion_estados[2][17] = 0; //llega {  ->   !!ERROR!! 55{
		this.matriz_transicion_estados[2][18] = 0; //llega }  ->   !!ERROR!! 55}
		this.matriz_transicion_estados[2][19] = 0; //llega ,  ->   !!ERROR!! 55,
		this.matriz_transicion_estados[2][20] = 0; //llega ; ->   !!ERROR!! 55;
		//this.matriz_transicion_estados[2][20] = 0; //llega : ->   !!ERROR!! 55;
		
		this.matriz_transicion_estados[2][21] = 0; //llega % -> limpio y reinicio
		this.matriz_transicion_estados[2][22] = 0; //llega " -> limpio y reinicio
		this.matriz_transicion_estados[2][23] = 0; //llega \n -> !!ERROR!! CTE\n -> no cierra sentencia
		this.matriz_transicion_estados[2][24] = 0; //llega otro caracter 
		
		
		
		
		//fila 3 -> digitos especiales 111_ , 55_
		
		this.matriz_transicion_estados[3][0] = 0; //llega a E3 una letra l -> !!ERROR!! 55_a
		this.matriz_transicion_estados[3][1] = 0; //llega a E3 un digito -> !!ERROR!! 55_1
		this.matriz_transicion_estados[3][2] = 0; //llega a E3 un _ -> !!ERROR!! 55__-
		this.matriz_transicion_estados[3][3] = -1; //llega una i -> token valido. voy a EF
		this.matriz_transicion_estados[3][4] = 0; //llega un . -> !!ERROR!! 55_.
		this.matriz_transicion_estados[3][5] = 0; //llega una f -> !!ERROR!! 55_f
		this.matriz_transicion_estados[3][6] = 0; //llega un blanco ' ' -> !!ERROR!! 55_ 
		this.matriz_transicion_estados[3][7] = 0; //llega un + -> !!ERROR!! 55_+
		this.matriz_transicion_estados[3][8] = 0; //llega una - -> !!ERROR!! 55_-
		this.matriz_transicion_estados[3][9] = 0; //llega una * -> !!ERROR!! 55_*
		this.matriz_transicion_estados[3][10] = 0; //llega una / -> !!ERROR!! 55_/
		this.matriz_transicion_estados[3][11] = 0; //llega un = -> !!ERROR!! 55_=
		this.matriz_transicion_estados[3][12] = 0; //llega <  ->  !!ERROR!! 55_<
		this.matriz_transicion_estados[3][13] = 0; //llega >  ->  !!ERROR!! 55_>
		this.matriz_transicion_estados[3][14] = 0; //llega !  ->  !!ERROR!! 55_!
		this.matriz_transicion_estados[3][15] = 0; //llega (  ->  !!ERROR!! 55_(
		this.matriz_transicion_estados[3][16] = 0; //llega )  ->  !!ERROR!! 55_)
		this.matriz_transicion_estados[3][17] = 0; //llega {  ->  !!ERROR!! 55_{
		this.matriz_transicion_estados[3][18] = 0; //llega }  ->  !!ERROR!! 55_}
		this.matriz_transicion_estados[3][19] = 0; //llega ,  ->  !!ERROR!! 55_,
		this.matriz_transicion_estados[3][20] = 0; //llega ; -> !!ERROR!! 55_;
		//this.matriz_transicion_estados[3][20] = 0; //llega : -> !!ERROR!! 55_;
		
		this.matriz_transicion_estados[3][21] = 0; //llega % -> !!ERROR!! 55_%
		this.matriz_transicion_estados[3][22] = 0; //llega " -> !!ERROR!! 55_"
		this.matriz_transicion_estados[3][23] = 0; //llega \n -> !!ERROR!! 55_
		this.matriz_transicion_estados[3][24] = 0; //llega otro caracter -> !!ERROR!! 55_otro
		
	
		
		
		//fila 4 -> digitos float que empiezan con .
		
		this.matriz_transicion_estados[4][0] = 0; //llega a E4 una letra l -> !!ERROR!! .a
		this.matriz_transicion_estados[4][1] = 5; //llega a E4 un digito -> voy a E5    ->  .5
		this.matriz_transicion_estados[4][2] = 0; //llega a E4 un _ -> !!ERROR!! ._
		this.matriz_transicion_estados[4][3] = 0; //llega una i -> !!ERROR!! .i
		this.matriz_transicion_estados[4][4] = 0; //llega un . -> !!ERROR!! ..
		this.matriz_transicion_estados[4][5] = 0; //llega una f -> !!ERROR!! .f
		this.matriz_transicion_estados[4][6] = 0; //llega blanco ' ' -> !!ERROR!! . 
		this.matriz_transicion_estados[4][7] = 0; //llega + -> !!ERROR!! .+
		this.matriz_transicion_estados[4][8] = 0; //llega - -> !!ERROR!! .-
		this.matriz_transicion_estados[4][9] = 0; //llega * -> !!ERROR!! .*
		this.matriz_transicion_estados[4][10] = 0; //llega / -> !!ERROR!! ./				
		this.matriz_transicion_estados[4][11] = 0; //llega = -> !!ERROR!! .=
		this.matriz_transicion_estados[4][12] = 0; //llega <  ->  !!ERROR!! .< 
		this.matriz_transicion_estados[4][13] = 0; //llega >  ->  !!ERROR!! .>
		this.matriz_transicion_estados[4][14] = 0; //llega !  ->  !!ERROR!! .!
		this.matriz_transicion_estados[4][15] = 0; //llega (  ->  !!ERROR!! .(
		this.matriz_transicion_estados[4][16] = 0; //llega )  ->  !!ERROR!! .)
		this.matriz_transicion_estados[4][17] = 0; //llega {  ->  !!ERROR!! .{
		this.matriz_transicion_estados[4][18] = 0; //llega }  ->  !!ERROR!! .}
		this.matriz_transicion_estados[4][19] = 0; //llega ,  ->  !!ERROR!! .,
		this.matriz_transicion_estados[4][20] = 0; //llega ; -> !!ERROR!! .;
		this.matriz_transicion_estados[4][21] = 0; //llega % -> !!ERROR!! .%
		this.matriz_transicion_estados[4][22] = 0; //llega " -> !!ERROR!! ."		
		this.matriz_transicion_estados[4][23] = 0; //llega \n -> !!ERROR!! .\n 
		this.matriz_transicion_estados[4][24] = 0; //llega otro caracter -> descarto y reinicio


		
		
		//fila 5 -> digitos decimales -> .555 , .123   o   1.32  ,  55.66
		
		this.matriz_transicion_estados[5][0] = 0; //llega a E5 una letra l  ->  !!ERROR!! .55a
		this.matriz_transicion_estados[5][1] = 5; //llega a E5 un digito  -> ciclo en E5
		this.matriz_transicion_estados[5][2] = 0; //llega a E5 un _  ->  !!ERROR!! .55_
		this.matriz_transicion_estados[5][3] = 0; //llega una i -> !!ERROR!! .55i
		this.matriz_transicion_estados[5][4] = 0; //llega un . ->  !!ERROR!! .55.
		this.matriz_transicion_estados[5][5] = 6; //llega una f ->  .55f ???
		this.matriz_transicion_estados[5][6] = -1; //llega un blanco ' ' -> token valido .55 -> voy a EF
		this.matriz_transicion_estados[5][7] = -1; //llega un + -> !!ERROR!! .55+
		this.matriz_transicion_estados[5][8] = -1; //llega un - -> !!ERROR!! .55-
		this.matriz_transicion_estados[5][9] = -1; //llega un * -> !!ERROR!! .55*
		this.matriz_transicion_estados[5][10] = -1; //llega un / -> !!ERROR!! .55/			
		this.matriz_transicion_estados[5][11] = -1; //llega un = -> !!ERROR!! .55=
		this.matriz_transicion_estados[5][12] = -1; //llega <  ->  !!ERROR!! .55<
		this.matriz_transicion_estados[5][13] = -1; //llega >  ->  !!ERROR!! .55>
		this.matriz_transicion_estados[5][14] = 0; //llega !  ->  !!ERROR!!  .55! 
		this.matriz_transicion_estados[5][15] = 0; //llega (  ->  !!ERROR!! .55(
		this.matriz_transicion_estados[5][16] = 0; //llega )  ->   !!ERROR!! .55)
		this.matriz_transicion_estados[5][17] = 0; //llega {  ->   !!ERROR!! .55{
		this.matriz_transicion_estados[5][18] = 0; //llega }  ->   !!ERROR!! .55}
		this.matriz_transicion_estados[5][19] = 0; //llega ,  ->   !!ERROR!! .55,
		this.matriz_transicion_estados[5][20] = 0; //llega ; -> !!ERROR!! .55;
		//this.matriz_transicion_estados[5][20] = 0; //llega ; -> !!ERROR!! .55:
		
		this.matriz_transicion_estados[5][21] = 0; //llega % -> !!ERROR!! .55%
		this.matriz_transicion_estados[5][22] = 0; //llega " -> !!ERROR!! .55"			
		this.matriz_transicion_estados[5][23] = 0; //llega \n -> !!ERROR!! .55\n
		this.matriz_transicion_estados[5][24] = 0; //llega otro caracter -> !!ERROR!! .55

		
		
		
		
		//fila 6  ->  floats con . ->  .333f    o   2.5f   ->  solo acepta + y - , lo demas es error
		
		this.matriz_transicion_estados[6][0] = 0; //llega a E6 letra l ->  ERROR!! al definir double
		this.matriz_transicion_estados[6][1] = 0; //llega a E6 un digito -> ERROR -> .333f8 ??
		this.matriz_transicion_estados[6][2] = 0; //llega a E6 un _ -> 
		this.matriz_transicion_estados[6][3] = 0; //llega una i -> descarto y reinicio
		this.matriz_transicion_estados[6][4] = 0; //llega un . -> descarto y reinicio
		this.matriz_transicion_estados[6][5] = 0; //llega una f -> descarto
		this.matriz_transicion_estados[6][6] = 0; //llega un blanco ' ' -> descarto y reinicio
		this.matriz_transicion_estados[6][7] = 7; //llega un + -> voy a EF  !!!VER!!! -> .333f+ ??
		this.matriz_transicion_estados[6][8] = 7; //llega un - -> voy a EF  !!!VER!!! -> .333f- ??
		this.matriz_transicion_estados[6][9] = 0; //llega un * -> descarto y reinicio
		this.matriz_transicion_estados[6][10] = 0; //llega un / -> descarto y reinicio				
		this.matriz_transicion_estados[6][11] = 0; //llega un = -> descarto y reinicio
		this.matriz_transicion_estados[6][12] = 0; //llega '<'  ->  voy a Ef 
		this.matriz_transicion_estados[6][13] = 0; //llega '>'  ->  voy a Ef 
		this.matriz_transicion_estados[6][14] = 0; //llega '!'  ->  voy a Ef 
		this.matriz_transicion_estados[6][15] = 0; //llega '('  ->  voy a Ef 
		this.matriz_transicion_estados[6][16] = 0; //llega ')'  ->  voy a Ef 
		this.matriz_transicion_estados[6][17] = 0; //llega '{'  ->  voy a Ef 
		this.matriz_transicion_estados[6][18] = 0; //llega '}'  ->  voy a Ef 
		this.matriz_transicion_estados[6][19] = 0; //llega ','  ->  voy a Ef 
		this.matriz_transicion_estados[6][20] = 0; //llega ; -> descarto y reincioi
		//this.matriz_transicion_estados[6][20] = 0; //llega : -> descarto y reincioi
		this.matriz_transicion_estados[6][21] = 0; //llega % -> descarto y reincioi
		this.matriz_transicion_estados[6][22] = 0; //llega " -> descarto y reincioi			
		this.matriz_transicion_estados[6][23] = 0; //llega \n -> descarto y reinicio
		this.matriz_transicion_estados[6][24] = 0; //llega otro caracter -> descarto y reinicio
		
		
		
		
		//fila 7 ->  doubles flasheros  ->  .333f+   y   .333f-
		//solo acepta digitos, lo demas es error
		
		this.matriz_transicion_estados[7][0] = 0; //llega a E7 una letra l -> descarto y voy a E0
		this.matriz_transicion_estados[7][1] = 8; //llega a E7 un digito -> voy a E8
		this.matriz_transicion_estados[7][2] = 0; //llega a E7 un _ -> descartes y voy a E0
		this.matriz_transicion_estados[7][3] = 0; //llega una i -> descarto y reinicio
		this.matriz_transicion_estados[7][4] = 0; //llega un . -> descarto y reinicio
		this.matriz_transicion_estados[7][5] = 0; //llega una f -> descarto y reinicio
		this.matriz_transicion_estados[7][6] = 0; //llega un blanco ' ' -> descarto y reinicio
		this.matriz_transicion_estados[7][7] = 0; //llega un + -> descarto y reinicio
		this.matriz_transicion_estados[7][8] = 0; //llega un - -> descarto y reinicio
		this.matriz_transicion_estados[7][9] = 0; //llega un * -> descarto y reinicio
		this.matriz_transicion_estados[7][10] = 0; //llega un / -> descarto y reinicio				
		this.matriz_transicion_estados[7][11] = 0; //llega un = -> descarto y reinicio
		this.matriz_transicion_estados[7][12] = 0; //llega '<'  ->  voy a Ef 
		this.matriz_transicion_estados[7][13] = 0; //llega '>'  ->  voy a Ef 
		this.matriz_transicion_estados[7][14] = 0; //llega '!'  ->  voy a Ef 
		this.matriz_transicion_estados[7][15] = 0; //llega '('  ->  voy a Ef 
		this.matriz_transicion_estados[7][16] = 0; //llega ')'  ->  voy a Ef 
		this.matriz_transicion_estados[7][17] = 0; //llega '{'  ->  voy a Ef 
		this.matriz_transicion_estados[7][18] = 0; //llega '}'  ->  voy a Ef 
		this.matriz_transicion_estados[7][19] = 0; //llega ','  ->  voy a Ef 
		this.matriz_transicion_estados[7][20] = 0; //llega ; -> descarto y reincio
		this.matriz_transicion_estados[7][21] = 0; //llega % -> descarto y reincio
		this.matriz_transicion_estados[7][22] = 0; //llega " -> descarto y reincio			
		this.matriz_transicion_estados[7][23] = 0; //llega \n -> descarto y reinicio
		this.matriz_transicion_estados[7][24] = 0; //llega otro caracter -> descarto y reinicio
		
		
		
		//fila 8 -> doubles ultra flasheros  ->  .333f+55   y   .333f-66
		
		this.matriz_transicion_estados[8][0] = 0; //llega a E8 una letra l -> descarto y voy a E0
		this.matriz_transicion_estados[8][1] = 8; //llega a E8 un digito -> voy a E8
		this.matriz_transicion_estados[8][2] = 0; //llega a E8 un _ -> descartes y voy a E0
		this.matriz_transicion_estados[8][3] = 0; //llega una i -> descarto y reinicio
		this.matriz_transicion_estados[8][4] = 0; //llega un . -> descarto y reinicio
		this.matriz_transicion_estados[8][5] = 0; //llega una f -> descarto y reinicio
		this.matriz_transicion_estados[8][6] = -1; //llega un blanco ' ' -> voy a EF
		this.matriz_transicion_estados[8][7] = -1; //llega un + -> voy a EF
		this.matriz_transicion_estados[8][8] = -1; //llega un - -> voy a EF
		this.matriz_transicion_estados[8][9] = -1; //llega un * -> voy a EF
		this.matriz_transicion_estados[8][10] = -1; //llega un / -> voy a EF						
		this.matriz_transicion_estados[8][11] = -1; //llega un = -> voy a EF !!!VER!!! -> .333f55= ??
		this.matriz_transicion_estados[8][12] = -1; //llega '<'  ->  voy a Ef 
		this.matriz_transicion_estados[8][13] = -1; //llega '>'  ->  voy a Ef 
		this.matriz_transicion_estados[8][14] = 0; //llega '!'  ->  voy a Ef 
		this.matriz_transicion_estados[8][15] = 0; //llega '('  ->  voy a Ef 
		this.matriz_transicion_estados[8][16] = 0; //llega ')'  ->  voy a Ef 
		this.matriz_transicion_estados[8][17] = 0; //llega '{'  ->  voy a Ef 
		this.matriz_transicion_estados[8][18] = 0; //llega '}'  ->  voy a Ef 
		this.matriz_transicion_estados[8][19] = 0; //llega ','  ->  voy a Ef 
		this.matriz_transicion_estados[8][20] = 0; //llega un ; -> voy a EF
		this.matriz_transicion_estados[8][21] = 0; //llega un % -> limpio y reinicio
		this.matriz_transicion_estados[8][22] = 0; //llega un % -> limpio y reinicio						
		this.matriz_transicion_estados[8][23] = 0; //llega \n -> descarto y reinicio
		this.matriz_transicion_estados[8][24] = 0; //llega otro caracter -> descarto y reinicio
		

		
		//fila 9 -> comparadores 
		
		this.matriz_transicion_estados[9][0] = 0; //llega a E9 una letra l ->  !!ERROR!! <l
		this.matriz_transicion_estados[9][1] = 0; //llega a E9 un digito -> !!ERROR!!
		this.matriz_transicion_estados[9][2] = 0; //llega a E9 un _ ->  !!ERROR!!
		this.matriz_transicion_estados[9][3] = 0; //llega una i -> !!ERROR!! <i 
		this.matriz_transicion_estados[9][4] = 0; //llega un . -> !!ERROR!! <.
		this.matriz_transicion_estados[9][5] = 0; //llega una f -> !!ERROR!! <f
		this.matriz_transicion_estados[9][6] = -1; //llega un blanco ' ' -> token valido. voy a EF
		this.matriz_transicion_estados[9][7] = 0; //llega un + -> !!ERROR!! <+
		this.matriz_transicion_estados[9][8] = 0; //llega un - -> !!ERROR!! <-
		this.matriz_transicion_estados[9][9] = 0; //llega un * -> 
		this.matriz_transicion_estados[9][10] = 0; //llega un / -> 						
		this.matriz_transicion_estados[9][11] = -1; //llega un = -> posible token doble valido. voy a EF   !!Y chequeo que sea valido!!!  si es <= o >= o != viaja.. 
		this.matriz_transicion_estados[9][12] = -1; //llega <  ->  posible token valido !!VER!!   << ??  , <> va,  <! error!
		this.matriz_transicion_estados[9][13] = -1; //llega >  ->  posible token valido  !!VER!! ERROR!   ><  forrada  ,  >> ?? ,  >! rrada 
		this.matriz_transicion_estados[9][14] = -1; //llega !  ->  posible token valido  !!VER!!  <! , >! , !! forrada 
		this.matriz_transicion_estados[9][15] = 0; //llega (  ->  !!ERROR!! <(
		this.matriz_transicion_estados[9][16] = 0; //llega )  ->  !!ERROR!! <) 
		this.matriz_transicion_estados[9][17] = 0; //llega {  ->  !!ERROR!! <{ 
		this.matriz_transicion_estados[9][18] = 0; //llega }  ->  !!ERROR!! <}
		this.matriz_transicion_estados[9][19] = 0; //llega ,  ->  !!ERROR!! <,
		this.matriz_transicion_estados[9][20] = 0; //llega ; ->  !!ERROR!! <;
		//this.matriz_transicion_estados[9][20] = 0; //llega ; ->  !!ERROR!! <;
		this.matriz_transicion_estados[9][21] = 0; //llega % -> !!ERROR!! <%
		this.matriz_transicion_estados[9][22] = 0; //llega " -> !!ERROR!! <"						
		this.matriz_transicion_estados[9][23] = 0; //llega \n -> !!ERROR!! <\n
		this.matriz_transicion_estados[9][24] = 0; //llega otro caracter -> !!ERROR!! <otro
						

		
		
		//fila 10 -> definicion de comentario   ->   LA MAYORIA DA ERROR!! solo valido %%
		
		this.matriz_transicion_estados[10][0] = 0; //llega a E10 una letra l -> descarto y voy a E0
		this.matriz_transicion_estados[10][1] = 0; //llega a E10 un digito -> descarto y reinicio
		this.matriz_transicion_estados[10][2] = 0; //llega a E10 un _ -> descartes y voy a E0
		this.matriz_transicion_estados[10][3] = 0; //llega una i -> descarto y reinicio
		this.matriz_transicion_estados[10][4] = 0; //llega un . -> descarto y reinicio
		this.matriz_transicion_estados[10][5] = 0; //llega una f -> descarto y reinicio
		this.matriz_transicion_estados[10][6] = 0; //llega un blanco ' ' -> voy a EF
		this.matriz_transicion_estados[10][7] = 0; //llega + -> limpio y reinicio
		this.matriz_transicion_estados[10][8] = 0; //llega - -> limpioy reinicio
		this.matriz_transicion_estados[10][9] = 0; //llega * -> limpio y reinicio
		this.matriz_transicion_estados[10][10] = 0; //llega / -> limpio y reinicio										
		this.matriz_transicion_estados[10][11] = 0; //llega = -> voy a EF
		this.matriz_transicion_estados[10][12] = 0; //llega <  ->  voy a Ef  !!VER!!   << ??  , <> va,  <! error!
		this.matriz_transicion_estados[10][13] = 0; //llega >  ->  voy a Ef  !!VER!! ERROR!   ><  forrada  ,  >> ?? ,  >! rrada 
		this.matriz_transicion_estados[10][14] = 0; //llega !  ->  voy a Ef  !!VER!!  <! , >! , !! forrada 
		this.matriz_transicion_estados[10][15] = 0; //llega (  ->   
		this.matriz_transicion_estados[10][16] = 0; //llega )  ->   
		this.matriz_transicion_estados[10][17] = 0; //llega {  ->   
		this.matriz_transicion_estados[10][18] = 0; //llega }  ->   
		this.matriz_transicion_estados[10][19] = 0; //llega ,  -> 
		this.matriz_transicion_estados[10][20] = 0; //llega ; -> limpio y reinicio
		//this.matriz_transicion_estados[10][20] = 0; //llega : -> limpio y reinicio
		
		this.matriz_transicion_estados[10][21] = 11; //llega un % -> voy a E11
		this.matriz_transicion_estados[10][22] = 0; //llega " -> descarto								
		this.matriz_transicion_estados[10][23] = 0; //llega \n -> descarto y reinicio
		this.matriz_transicion_estados[10][24] = 0; //llega otro caracter -> descarto y reinicio
								

		
		
		//fila 11 -> cuerpo del comentario

		this.matriz_transicion_estados[11][0] = 11; //llega a E11 una letra l -> agrego caracter
		this.matriz_transicion_estados[11][1] = 11; //llega a E11 un digito -> agrego caracter
		this.matriz_transicion_estados[11][2] = 11; //llega a E11 un _ -> agrego caracter
		this.matriz_transicion_estados[11][3] = 11; //llega una i -> agrego caracter
		this.matriz_transicion_estados[11][4] = 11; //llega un . -> agrego caracter
		this.matriz_transicion_estados[11][5] = 11; //llega una f -> agrego caracter
		this.matriz_transicion_estados[11][6] = 11; //llega un blanco ' ' -> agrego caracter
		this.matriz_transicion_estados[11][7] = 11; //llega un + -> agrego caracter
		this.matriz_transicion_estados[11][8] = 11; //llega un - -> agrego caracter
		this.matriz_transicion_estados[11][9] = 11; //llega un * -> agrego caracter
		this.matriz_transicion_estados[11][10] = 11; //llega un / -> agrego caracter										
		this.matriz_transicion_estados[11][11] = 11; //llega un = -> agrego caracter
		this.matriz_transicion_estados[11][12] = 11; //llega <  ->  voy a Ef  !!VER!!   << ??  , <> va,  <! error!
		this.matriz_transicion_estados[11][13] = 11; //llega >  ->  voy a Ef  !!VER!! ERROR!   ><  forrada  ,  >> ?? ,  >! rrada 
		this.matriz_transicion_estados[11][14] = 11; //llega !  ->  voy a Ef  !!VER!!  <! , >! , !! forrada 
		this.matriz_transicion_estados[11][15] = 11; //llega (  ->  voy a Ef 
		this.matriz_transicion_estados[11][16] = 11; //llega )  ->  voy a Ef 
		this.matriz_transicion_estados[11][17] = 11; //llega {  ->  voy a Ef 
		this.matriz_transicion_estados[11][18] = 11; //llega }  ->  voy a Ef 
		this.matriz_transicion_estados[11][19] = 11; //llega ,  ->  voy a Ef 
		this.matriz_transicion_estados[11][20] = 11; //llega ; -> agrego caracter
		//this.matriz_transicion_estados[11][20] = 11; //llega : -> agrego caracter
		
		this.matriz_transicion_estados[11][21] = 11; //llega % -> agrego caracter
		this.matriz_transicion_estados[11][22] = 11; //llega " -> agrego caracter
										
		this.matriz_transicion_estados[11][23] = -1; //llega \n -> cadena valida!  %%asdsaf\n  estado 0 o -1??
		this.matriz_transicion_estados[11][24] = 11; //llega otro caracter -> agrego caracter
		
		
		
		 
		//AHORA FILA 12 es para las CADENAS NACIONALES!!
		//!!!VER!!!! una vez inicializada la cadena con "  -> cadena de carateres que comiencen y terminen con "
		this.matriz_transicion_estados[12][0] = 12; //llega a E12 una letra -> agrego y ciclo en E12
		this.matriz_transicion_estados[12][1] = 12; //llega a E12 un digito -> agrego? y ciclo?
		this.matriz_transicion_estados[12][2] = 12; //llega a E12 un _ -> agrego
		this.matriz_transicion_estados[12][3] = 12; //llega una i -> agrego y ciclo
		this.matriz_transicion_estados[12][4] = 0; //llega un . -> !!ERROR!! ".
		this.matriz_transicion_estados[12][5] = 12; //llega una f -> agrego y ciclo
		this.matriz_transicion_estados[12][6] = 12; //llega un blanco -> agrego y ciclo
		this.matriz_transicion_estados[12][7] = 0; //llega un + -> !!VER!! "+ ??  !!ERROR!! deben ser caracteres
		this.matriz_transicion_estados[12][8] = 13; //llega un - -> !!VER!! "aaa- ?? FIN DE LINEA!!!
		this.matriz_transicion_estados[12][9] = 0; //llega un * -> !!ERROR!! deben ser caracteres
		this.matriz_transicion_estados[12][10] = 0; //llega un / ->	 !!ERROR!! deben ser caracteres							
		this.matriz_transicion_estados[12][11] = 0; //llega un = -> !!ERROR!! deben ser caracteres
		this.matriz_transicion_estados[12][12] = 0; //llega un < -> !!ERROR!! deben ser caracteres
		this.matriz_transicion_estados[12][13] = 0; //llega un > -> !!ERROR!! deben ser caracteres
		this.matriz_transicion_estados[12][14] = 0; //llega un ! -> !!ERROR!! deben ser caracteres
		this.matriz_transicion_estados[12][15] = 0; //llega un ( -> !!ERROR!! deben ser caracteres
		this.matriz_transicion_estados[12][16] = 0; //llega un ) -> !!ERROR!! deben ser caracteres
		this.matriz_transicion_estados[12][17] = 0; //llega un { -> !!ERROR!! deben ser caracteres
		this.matriz_transicion_estados[12][18] = 0; //llega un } -> !!ERROR!! deben ser caracteres
		this.matriz_transicion_estados[12][19] = 0; //llega un , -> !!ERROR!! deben ser caracteres
		this.matriz_transicion_estados[12][20] = 0; //llega un ; -> !!ERROR!! deben ser caracteres
		//this.matriz_transicion_estados[12][20] = 0; //llega un : -> !!ERROR!! deben ser caracteres
		
		this.matriz_transicion_estados[12][21] = 0; //llega un % -> !!ERROR!! deben ser caracteres
		this.matriz_transicion_estados[12][22] = -1; //llega un " -> token valido "asda sdas" -> cierro cadena								
		this.matriz_transicion_estados[12][23] = 0; //llega \n -> !!ERROR!! al inicializar cadena "\n
		this.matriz_transicion_estados[12][23] = 0; //llega otro caracter 

		

		//FILA 13 para la correcta definicion multilinea de CADENAS 
		//!!!VER!!!! una vez inicializada la cadena con " 
		this.matriz_transicion_estados[13][0] = 0; //llega a E13 una letra -> 
		this.matriz_transicion_estados[13][1] = 0; //llega a E13 un digito ->
		this.matriz_transicion_estados[13][2] = 0; //llega a E13 un _ ->
		this.matriz_transicion_estados[13][3] = 0; //llega una i -> 
		this.matriz_transicion_estados[13][4] = 0; //llega un . ->
		this.matriz_transicion_estados[13][5] = 0; //llega una f ->
		this.matriz_transicion_estados[13][6] = 0; //llega un blanco ->
		this.matriz_transicion_estados[13][7] = 0; //llega un + ->
		this.matriz_transicion_estados[13][8] = 0; //llega un - ->
		this.matriz_transicion_estados[13][9] = 0; //llega un * ->
		this.matriz_transicion_estados[13][10] = 0; //llega	un / ->							
		this.matriz_transicion_estados[13][11] = 0; //llega un = ->
		this.matriz_transicion_estados[13][12] = 0; //llega un < ->
		this.matriz_transicion_estados[13][13] = 0; //llega un > -> 
		this.matriz_transicion_estados[13][14] = 0; //llega un ! ->
		this.matriz_transicion_estados[13][15] = 0; //llega un ( ->
		this.matriz_transicion_estados[13][16] = 0; //llega un ) ->
		this.matriz_transicion_estados[13][17] = 0; //llega un { ->
		this.matriz_transicion_estados[13][18] = 0; //llega un } ->
		this.matriz_transicion_estados[13][19] = 0; //llega un , ->
		this.matriz_transicion_estados[13][20] = 0; //llega una ; ->
		this.matriz_transicion_estados[13][21] = 0; //llega un % ->
		this.matriz_transicion_estados[13][22] = 0; //llega un " ->								
		this.matriz_transicion_estados[13][23] = 12; //llega \n -> vuelvo a 12 y defino nueva linea
		this.matriz_transicion_estados[13][23] = 0; //llega otro caracter ->
		
		
		
		//fila 14 -> estado final -1
		
		this.matriz_transicion_estados[14][0] = 0; //en EF reinicio
		this.matriz_transicion_estados[14][1] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[14][2] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[14][3] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[14][4] = 0; //en EF reinicio
		this.matriz_transicion_estados[14][5] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[14][6] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[14][7] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[14][8] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[14][9] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[14][10] = 0; //en EF entrego token y reinicio								
		this.matriz_transicion_estados[14][11] = 0; //en EF entrego token y reinicio
		this.matriz_transicion_estados[14][12] = 0; //llega <  ->  voy a Ef  !!VER!!   << ??  , <> va,  <! error!
		this.matriz_transicion_estados[14][13] = 0; //llega >  ->  voy a Ef  !!VER!! ERROR!   ><  forrada  ,  >> ?? ,  >! rrada 
		this.matriz_transicion_estados[14][14] = 0; //llega !  ->  voy a Ef  !!VER!!  <! , >! , !! forrada 
		this.matriz_transicion_estados[14][15] = 0; //llega (  ->  voy a Ef 
		this.matriz_transicion_estados[14][16] = 0; //llega )  ->  voy a Ef 
		this.matriz_transicion_estados[14][17] = 0; //llega {  ->  voy a Ef 
		this.matriz_transicion_estados[14][18] = 0; //llega }  ->  voy a Ef 
		this.matriz_transicion_estados[14][19] = 0; //llega ,  ->  voy a Ef 
		this.matriz_transicion_estados[14][20] = 0; //
		this.matriz_transicion_estados[14][21] = 0; //
		this.matriz_transicion_estados[14][22] = 0; //								
		this.matriz_transicion_estados[14][23] = 0; //entrego token y reinicio
		this.matriz_transicion_estados[14][24] = 0; //entrego token y reinicio

	}
	
	
	
	public void inicializarMatrizAccionesSemanticas() {
		//Acciones Semanticas
		AccionSemantica AS1 = new InicializarBuffer();
		AccionSemantica AS2 = new AgregarCaracter();
		AccionSemantica AS3 = new LlegaTokenValido(); 
		AccionSemantica AS4 = new DescartarBuffer();
		AccionSemantica ASErr = new ErrorLexico();
		AccionSemantica ASF = new EntregarTokenYReiniciar(this.tds);

		
		//fila 0 -> estado inicial
		this.matriz_acciones_semanticas[0][0] = AS1; //llega a E0 una letra -> InicializarBuffer(); 
		this.matriz_acciones_semanticas[0][1] = AS1; //llega a E0 un digito -> InicializarBuffer(); 
		this.matriz_acciones_semanticas[0][2] = AS4; //llega a E0 un _ -> DescartarBuffer(); !!ERROR!!!  -> comienza con _
		this.matriz_acciones_semanticas[0][3] = AS1; //llega i -> InicializarBuffer();  
		this.matriz_acciones_semanticas[0][4] = AS1; //llega . -> InicializarBuffer(); 
		this.matriz_acciones_semanticas[0][5] = AS1; //llega f -> InicializarBuffer();
		this.matriz_acciones_semanticas[0][6] = AS4; //llega blanco ' ' -> consumo token
		this.matriz_acciones_semanticas[0][7] = AS3; //llega +  ->  EntregarToken(); 
		this.matriz_acciones_semanticas[0][8] = AS3; //llega -  ->  EntregarToken();  
		this.matriz_acciones_semanticas[0][9] = AS3; //llega *  ->  EntregarToken();  
		this.matriz_acciones_semanticas[0][10] = AS3; //llega /  -> EntregarToken();  	
		this.matriz_acciones_semanticas[0][11] = AS3; //llega =  -> EntregarToken(); 
		this.matriz_acciones_semanticas[0][12] = AS3; //llega <  ->  EntregarToken(); 
		this.matriz_acciones_semanticas[0][13] = AS3; //llega >  ->  EntregarToken();  
		this.matriz_acciones_semanticas[0][14] = AS3; //llega !  ->  EntregarToken();  
		this.matriz_acciones_semanticas[0][15] = AS3; //llega (  -> EntregarToken();  	
		this.matriz_acciones_semanticas[0][16] = AS3; //llega )  -> EntregarToken(); 
		this.matriz_acciones_semanticas[0][17] = AS3; //llega {  -> EntregarToken(); 
		this.matriz_acciones_semanticas[0][18] = AS3; //llega }  -> EntregarToken(); 
		this.matriz_acciones_semanticas[0][19] = AS3; //llega ,  -> EntregarToken(); 		
		this.matriz_acciones_semanticas[0][20] = AS3; //llega ;  -> EntregarToken(); 
		//this.matriz_acciones_semanticas[0][20] = AS3; //llega :  -> EntregarToken(); 
		
		this.matriz_acciones_semanticas[0][21] = AS1; //llega %  ->  InicializarToken();
		this.matriz_acciones_semanticas[0][22] = AS1; //llega "  ->  InicializarToken(); INICIALIZA CADENA!! 
		this.matriz_acciones_semanticas[0][23] = AS4; //llega \n -> consumo token
		this.matriz_acciones_semanticas[0][24] = AS4; //llega otro caracter -> Descartar();
		
		
		
		//fila 1 -> identificadores  
		//solucion -> tokens si o si separados por espacio
		
		this.matriz_acciones_semanticas[1][0] = AS2; //llega a E1 una letra -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[1][1] = AS2; //llega a E1 un digito -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[1][2] = AS2; //llega e E1 un _ -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[1][3] = AS2; //llega i -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[1][4] = AS4; //llega . -> DescartarBuffer();  !!ERROR!! -> ll. 
		this.matriz_acciones_semanticas[1][5] = AS2; //llega f -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[1][6] = AS3; //llega blanco ' ' -> LlegaTokenValido(); 
		this.matriz_acciones_semanticas[1][7] = ASErr; //llega + ->  !!ERROR!! todo junto ID+
		this.matriz_acciones_semanticas[1][8] = ASErr; //llega - ->  !!ERROR!! todo junto ID-
		this.matriz_acciones_semanticas[1][9] = ASErr; //llega * ->  !!ERROR!! todo junto ID*
		this.matriz_acciones_semanticas[1][10] = ASErr; //llega / ->  !!ERROR!! todo junto ID/
		this.matriz_acciones_semanticas[1][11] = ASErr; //llega = ->  !!ERROR!! todo junto ID=
		this.matriz_acciones_semanticas[1][12] = ASErr; //llega <  ->  !!ERROR!! todo junto ID<
		this.matriz_acciones_semanticas[1][13] = ASErr; //llega >  ->  !!ERROR!! todo junto ID>  
		this.matriz_acciones_semanticas[1][14] = ASErr; //llega !  ->  !!ERROR!! todo junto ID!  
		this.matriz_acciones_semanticas[1][15] = ASErr; //llega (  ->  !!ERROR!! todo junto ID( 	
		this.matriz_acciones_semanticas[1][16] = ASErr; //llega )  ->  !!ERROR!! todo junto ID)
		this.matriz_acciones_semanticas[1][17] = ASErr; //llega {  ->  !!ERROR!! todo junto ID{
		this.matriz_acciones_semanticas[1][18] = ASErr; //llega }  ->  !!ERROR!! todo junto ID}
		this.matriz_acciones_semanticas[1][19] = ASErr; //llega ,  ->  !!ERROR!! todo junto ID,		
		this.matriz_acciones_semanticas[1][20] = ASErr; //llega ; ->  !!ERROR!! todo junto ID;
		//this.matriz_acciones_semanticas[1][20] = AS4; //llega : ->  !!ERROR!! todo junto ID;
		
		this.matriz_acciones_semanticas[1][21] = ASErr; //llega % -> !!ERROR!! todo junto ID%
		this.matriz_acciones_semanticas[1][22] = ASErr; //llega " -> !!ERROR!! todo junto ID"
		this.matriz_acciones_semanticas[1][23] = AS4; //llega \n -> !!ERROR!! todo junto IDotro
		this.matriz_acciones_semanticas[1][24] = AS4; //llega otro caracter -> !!ERROR!! todo junto IDotro
		
		
		
		
		//fila 2 -> digitos
		
		this.matriz_acciones_semanticas[2][0] = ASErr; //llega a E2 una letra ->  !!ERROR!!  22a
		this.matriz_acciones_semanticas[2][1] = AS2; //llega a E2 un digito -> AgregarCaracter();  
		this.matriz_acciones_semanticas[2][2] = AS2; //llega a E2 un _ -> AgregarCaracter(); CARACTER ESPECIAL
		this.matriz_acciones_semanticas[2][3] = ASErr; //llega i -> DescartarBuffer(); !!ERROR!!  22i
		this.matriz_acciones_semanticas[2][4] = AS2; //llega . -> AgregarCaracter() 22.
		this.matriz_acciones_semanticas[2][5] = ASErr; //llega f -> !!ERROR!! -> 22f 		
		this.matriz_acciones_semanticas[2][6] = AS3; //llega blanco ' ' -> LlegaTokenValido(); entrego token tipo constante
		this.matriz_acciones_semanticas[2][7] = ASErr; //llega + -> !!ERROR!! 
		this.matriz_acciones_semanticas[2][8] = ASErr; //llega - -> !!ERROR!! 55-
		this.matriz_acciones_semanticas[2][9] = ASErr; //llega * -> !!ERROR!! 55*
		this.matriz_acciones_semanticas[2][10] = ASErr; //llega / -> 
		this.matriz_acciones_semanticas[2][11] = ASErr; //llega = -> 
		this.matriz_acciones_semanticas[2][12] = ASErr; //llega <  ->   
		this.matriz_acciones_semanticas[2][13] = ASErr; //llega >  ->   55>
		this.matriz_acciones_semanticas[2][14] = ASErr; //llega !  ->   55!
		this.matriz_acciones_semanticas[2][15] = ASErr; //llega (  ->   55(
		this.matriz_acciones_semanticas[2][16] = ASErr; //llega )  ->  
		this.matriz_acciones_semanticas[2][17] = ASErr; //llega {  ->  
		this.matriz_acciones_semanticas[2][18] = ASErr; //llega }  ->  
		this.matriz_acciones_semanticas[2][19] = ASErr; //llega ,  ->  		
		this.matriz_acciones_semanticas[2][20] = ASErr; //llega ; ->
		//this.matriz_acciones_semanticas[2][20] = ASErr; //llega : ->
		this.matriz_acciones_semanticas[2][21] = ASErr; //llega % -> DescartarBuffer();
		this.matriz_acciones_semanticas[2][22] = ASErr; //llega " -> DescartarBuffer();  
		this.matriz_acciones_semanticas[2][23] = AS4; //llega \n -> 
		this.matriz_acciones_semanticas[2][24] = AS4; //llega otro caracter
		
		
		
		
		//fila 3 -> constante especial _i
		
		this.matriz_acciones_semanticas[3][0] = ASErr; //llega a E3 una letra -> !!ERROR!! -> 55_a
		this.matriz_acciones_semanticas[3][1] = ASErr; //llega a E3 un digito -> DescartarBuffer();  -> descarto
		this.matriz_acciones_semanticas[3][2] = ASErr; //llega a E3 un _ -> DescartarBuffer(); 
		this.matriz_acciones_semanticas[3][3] = AS3; //llega i -> TokenValido(); agrego caracter y entrego token
		this.matriz_acciones_semanticas[3][4] = ASErr; //llega . -> !!ERROR!! -> 55_.   notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][5] = ASErr; //llega f -> !!ERROR!! -> 55_f   notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][6] = ASErr; //llega blanco ' ' -> !!ERROR!! -> 55_   notifico, limpio y reinicio
		
		this.matriz_acciones_semanticas[3][7] = ASErr; //llega + -> !!ERROR!! ->  55_+  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][8] = ASErr; //llega - -> !!ERROR!! ->  55_-  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][9] = ASErr; //llega * -> !!ERROR!! ->  55_*  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][10] = ASErr; //llega / -> !!ERROR!! ->  55_/  notifico, limpio y reinicio		
		this.matriz_acciones_semanticas[3][11] = ASErr; //llega = -> !!ERROR!! ->  55_=  notifico, limpio y reinicio
		
		this.matriz_acciones_semanticas[3][12] = ASErr; //llega <  ->  !!ERROR!! ->  55_<  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][13] = ASErr; //llega >  ->  !!ERROR!! ->  55_>  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][14] = ASErr; //llega !  ->  !!ERROR!! ->  55_!  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][15] = ASErr; //llega (  ->  !!ERROR!! ->  55_(  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][16] = ASErr; //llega )  ->  !!ERROR!! ->  55_)  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][17] = ASErr; //llega {  ->  !!ERROR!! ->  55_{  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][18] = ASErr; //llega }  ->  !!ERROR!! ->  55_}  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][19] = ASErr; //llega ,  ->  !!ERROR!! ->  55_,  notifico, limpio y reinicio		
		this.matriz_acciones_semanticas[3][20] = ASErr; //llega ; ->   !!ERROR!! ->  55_;  notifico, limpio y reinicio

		//this.matriz_acciones_semanticas[3][20] = ASErr; //llega ; -> !!ERROR!! ->  55_;  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][21] = ASErr; //llega % -> !!ERROR!! ->  55_%  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[3][22] = ASErr; //llega " -> !!ERROR!! ->  55_"  notifico, limpio y reinicio		
		this.matriz_acciones_semanticas[3][23] = ASErr; //llega \n ->  
		this.matriz_acciones_semanticas[3][24] = AS4; //llega otro caracter 
		
			
		
		
		
		//fila 4 -> digitos float que empiezan con .
		
		this.matriz_acciones_semanticas[4][0] = ASErr; //llega a E4 una letra -> DescartarBuffer();  ->  ERROR!! .a
		this.matriz_acciones_semanticas[4][1] = AS2; //llega a E4 un digito -> AgregarCaracter();  -> 
		this.matriz_acciones_semanticas[4][2] = ASErr; //llega a E4 un _ -> DescartarBuffer(); !!ERROR!! ._
		this.matriz_acciones_semanticas[4][3] = ASErr; //llega i -> DescartarBuffer();  !!ERROR!! .i
		this.matriz_acciones_semanticas[4][4] = ASErr; //llega . -> DescartarBuffer();  !!ERROR!! ..
		this.matriz_acciones_semanticas[4][5] = ASErr; //llega f -> DescartarBuffer();  !!ERROR!! .f
		this.matriz_acciones_semanticas[4][6] = ASErr; //llega blanco ' ' -> DescartarBuffer(); !!ERROR!! . 
		this.matriz_acciones_semanticas[4][7] = ASErr; //llega +  ->  DescartarBuffer(); !!ERROR!! .+
		this.matriz_acciones_semanticas[4][8] = ASErr; //llega -  ->  DescartarBuffer(); !!ERROR!! .-
		this.matriz_acciones_semanticas[4][9] = ASErr; //llega *  ->  DescartarBuffer(); !!ERROR!! .*
		this.matriz_acciones_semanticas[4][10] = ASErr; //llega /  ->  DescartarBuffer(); !!ERROR!! ./		
		this.matriz_acciones_semanticas[4][11] = ASErr; //llega =  ->  DescartarBuffer(); !!ERROR!! .=
		this.matriz_acciones_semanticas[4][12] = ASErr; //llega <  ->  DescartarBuffer(); !!ERROR!! .<  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[4][13] = ASErr; //llega >  ->  DescartarBuffer(); !!ERROR!! .>  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[4][14] = ASErr; //llega !  ->  DescartarBuffer(); !!ERROR!! .!  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[4][15] = ASErr; //llega (  ->  DescartarBuffer(); !!ERROR!! .(  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[4][16] = ASErr; //llega )  ->  DescartarBuffer(); !!ERROR!! .)  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[4][17] = ASErr; //llega {  ->  DescartarBuffer(); !!ERROR!! .{  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[4][18] = ASErr; //llega }  ->  DescartarBuffer(); !!ERROR!! .}  notifico, limpio y reinicio
		this.matriz_acciones_semanticas[4][19] = ASErr; //llega ,  ->  DescartarBuffer(); !!ERROR!! .,  notifico, limpio y reinicio		
		this.matriz_acciones_semanticas[4][20] = ASErr; //llega ;  ->   DescartarBuffer(); !!ERROR!! .;  notifico, limpio y reinicio	
		//this.matriz_acciones_semanticas[4][20] = ASErr; //llega :  ->   DescartarBuffer(); !!ERROR!! .;  notifico, limpio y reinicio	
		
		this.matriz_acciones_semanticas[4][21] = ASErr; //llega %  ->  DescartarBuffer(); 
		this.matriz_acciones_semanticas[4][21] = ASErr; //llega "  ->  DescartarBuffer(); 
		this.matriz_acciones_semanticas[4][23] = ASErr; //llega \n -> DescartarBuffer(); //en E4 le llega otro caracter -> limpio y reinicio
		this.matriz_acciones_semanticas[4][24] = AS4; //llega otro caracter -> DescartarBuffer(); //en E4 le llega otro caracter -> limpio y reinicio
		
				
		
		
		
		//fila 5 -> constantes doubles  .666 
		
		this.matriz_acciones_semanticas[5][0] = ASErr; //llega a E5 una letra -> !!ERROR!! .666a  descarto y vuelvo a inicio
		this.matriz_acciones_semanticas[5][1] = AS2; //llega a E5 un digito -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[5][2] = ASErr; //llega a E5 un _ -> !!ERROR!! .666_  limpio y reinicio
		this.matriz_acciones_semanticas[5][3] = ASErr; //llega i ->  !!ERROR!! .666i  limpio y reinicio
		this.matriz_acciones_semanticas[5][4] = ASErr; //llega . ->  !!ERROR!! .666.  limpio y reinicio
		this.matriz_acciones_semanticas[5][5] = AS2; //llega f -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[5][6] = AS3; //llega blanco ' ' -> EntregarToken(); -> entrego token tipo constante
		
		this.matriz_acciones_semanticas[5][7] = ASErr; //llega + -> !!ERROR!! .666+
		this.matriz_acciones_semanticas[5][8] = ASErr; //llega - -> !!ERROR!! .666-
		this.matriz_acciones_semanticas[5][9] = ASErr; //llega * -> !!ERROR!! .666*
		this.matriz_acciones_semanticas[5][10] = ASErr; //llega / -> !!ERROR!! .666/
		this.matriz_acciones_semanticas[5][11] = ASErr; //llega = -> !!ERROR!! .666=
		this.matriz_acciones_semanticas[5][12] = ASErr; //llega <  ->  !!ERROR!! .666<
		this.matriz_acciones_semanticas[5][13] = ASErr; //llega >  ->  !!ERROR!! .666>
		this.matriz_acciones_semanticas[5][14] = ASErr; //llega !  ->  !!ERROR!! .666!
		this.matriz_acciones_semanticas[5][15] = ASErr; //llega (  ->  !!ERROR!! .666(
		this.matriz_acciones_semanticas[5][16] = ASErr; //llega )  ->  !!ERROR!! .666)
		this.matriz_acciones_semanticas[5][17] = ASErr; //llega {  ->  !!ERROR!! .666{
		this.matriz_acciones_semanticas[5][18] = ASErr; //llega }  ->  !!ERROR!! .666}
		this.matriz_acciones_semanticas[5][19] = ASErr; //llega ,  ->  !!ERROR!! .666,
		this.matriz_acciones_semanticas[5][20] = ASErr; //llega ;  ->  !!ERROR!! .666;

		
		this.matriz_acciones_semanticas[5][21] = ASErr; //llega % ->  
		this.matriz_acciones_semanticas[5][22] = ASErr; //llega " ->  
		
		this.matriz_acciones_semanticas[5][23] = AS4; //llega \n ->  
		this.matriz_acciones_semanticas[5][24] = AS4; //llega otro caracter 
		
		
		

		
		//fila 6  ->  floats con . ->  .333f    o   2.5f   ->  solo acepta + y - , lo demas es error		
		
		this.matriz_acciones_semanticas[6][0] = ASErr; //llega a E6 una letra -> !!ERROR!! .33fa
		this.matriz_acciones_semanticas[6][1] = ASErr; //llega a E6 un digito -> !!ERROR!!  .66f6  
		this.matriz_acciones_semanticas[6][2] = ASErr; //llega a E6 un _ ->  !!ERROR!! .66f_
		this.matriz_acciones_semanticas[6][3] = ASErr; //llega i -> !!ERROR!! .66fi
		this.matriz_acciones_semanticas[6][4] = ASErr; //llega . -> !!ERROR!! .66f.
		this.matriz_acciones_semanticas[6][5] = ASErr; //llega f -> !!ERROR!! .66ff 
		this.matriz_acciones_semanticas[6][6] = ASErr; //llega blanco ' ' -> !!ERROR!! .66f 
		this.matriz_acciones_semanticas[6][7] = AS2; //llega + -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[6][8] = AS2; //llega - -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[6][9] = ASErr; //llega * -> !!ERROR!! .66f*
		this.matriz_acciones_semanticas[6][10] = ASErr; //llega / -> !!ERROR!! .66f/			
		this.matriz_acciones_semanticas[6][11] = ASErr; //llega = -> !!ERROR!! .66f=
		this.matriz_acciones_semanticas[6][12] = ASErr; //llega <  -> !!ERROR!! .66f<
		this.matriz_acciones_semanticas[6][13] = ASErr; //llega >  -> !!ERROR!! .66f>
		this.matriz_acciones_semanticas[6][14] = ASErr; //llega !  ->  !!ERROR!! .66f!
		this.matriz_acciones_semanticas[6][15] = ASErr; //llega (  ->  !!ERROR!! .66f(
		this.matriz_acciones_semanticas[6][16] = ASErr; //llega )  ->  !!ERROR!! .66f)
		this.matriz_acciones_semanticas[6][17] = ASErr; //llega {  ->  !!ERROR!! .66f{
		this.matriz_acciones_semanticas[6][18] = ASErr; //llega }  ->  !!ERROR!! .66f}
		this.matriz_acciones_semanticas[6][19] = ASErr; //llega ,  ->  !!ERROR!! .66f,
		this.matriz_acciones_semanticas[6][20] = ASErr; //llega ;  ->  !!ERROR!! .66f;
		//this.matriz_acciones_semanticas[6][20] = ASErr; //llega :  ->  !!ERROR!! .66f;

		this.matriz_acciones_semanticas[6][21] = ASErr; //llega % ->  
		this.matriz_acciones_semanticas[6][22] = ASErr; //llega " ->  
		this.matriz_acciones_semanticas[6][23] = ASErr; //llega \n -> 
		this.matriz_acciones_semanticas[6][24] = AS4; //llega otro caracter -> DescartarBuffer(); //a E6 le llega otro caracter -> entrego token tipo constante
		
		
		

		//fila 7 ->  doubles flasheros  ->  .333f+   y   .333f-
		//solo acepta digitos, lo demas es error
		
		this.matriz_acciones_semanticas[7][0] = ASErr; //llega a E7 una letra ->  
		this.matriz_acciones_semanticas[7][1] = AS2; //llega a E7 un digito -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[7][2] = ASErr; //llega a E7 un _ -> !!ERROR!! .333f+_
		this.matriz_acciones_semanticas[7][3] = ASErr; //llega i -> !!ERROR!! .333f+i
		this.matriz_acciones_semanticas[7][4] = ASErr; //llega . ->  
		this.matriz_acciones_semanticas[7][5] = ASErr; //llega f ->  
		this.matriz_acciones_semanticas[7][6] = ASErr; //llega blanco ' ' -> Descartar(); 
		this.matriz_acciones_semanticas[7][7] = ASErr; //llega + -> 
		this.matriz_acciones_semanticas[7][8] = ASErr; //llega - -> 
		this.matriz_acciones_semanticas[7][9] = ASErr; //llega * ->  
		this.matriz_acciones_semanticas[7][10] = ASErr; //llega / ->  		
		this.matriz_acciones_semanticas[7][11] = ASErr; //llega = ->  
		this.matriz_acciones_semanticas[7][12] = ASErr; //llega <  ->  !!ERROR!! .66f<
		this.matriz_acciones_semanticas[7][13] = ASErr; //llega >  ->  !!ERROR!! .66f>
		this.matriz_acciones_semanticas[7][14] = ASErr; //llega !  ->  !!ERROR!! .66f!
		this.matriz_acciones_semanticas[7][15] = ASErr; //llega (  ->  !!ERROR!! .66f(
		this.matriz_acciones_semanticas[7][16] = ASErr; //llega )  ->  !!ERROR!! .66f)
		this.matriz_acciones_semanticas[7][17] = ASErr; //llega {  ->  !!ERROR!! .66f{
		this.matriz_acciones_semanticas[7][18] = ASErr; //llega }  ->  !!ERROR!! .66f}
		this.matriz_acciones_semanticas[7][19] = ASErr; //llega ,  ->  !!ERROR!! .66f,
		this.matriz_acciones_semanticas[7][20] = ASErr; //llega ;  ->  !!ERROR!! .66f;

		
		this.matriz_acciones_semanticas[7][21] = ASErr; //llega % -> 
		this.matriz_acciones_semanticas[7][22] = ASErr; //llega " ->  
		this.matriz_acciones_semanticas[7][23] = ASErr; //llega \n ->
		this.matriz_acciones_semanticas[7][24] = AS4; //llega otro caracter -> Descartar(); //a E4 le llega otro caracter -> entrego token tipo constante
		

		

		//fila 8 -> doubles aun mas flasheros  ->  .333f+33   y   .333f-33

		this.matriz_acciones_semanticas[8][0] = ASErr; //llega a E8 una letra -> !!ERROR!! .333f+33a
		this.matriz_acciones_semanticas[8][1] = AS2; //llega a E8 un digito -> AgregarCaracter();  !!CHECKEAR RANGO!!
		this.matriz_acciones_semanticas[8][2] = ASErr; //llega a E8 un _ -> !!ERROR!! .333f+33_
		this.matriz_acciones_semanticas[8][3] = ASErr; //llega i ->  
		this.matriz_acciones_semanticas[8][4] = ASErr; //llega . ->  
		this.matriz_acciones_semanticas[8][5] = ASErr; //llega f ->  
		this.matriz_acciones_semanticas[8][6] = AS3; //llega blanco ' ' -> LlegaTokenValido(); 
		this.matriz_acciones_semanticas[8][7] = ASErr; //llega + -> !!ERROR!! .333f+33+
		this.matriz_acciones_semanticas[8][8] = ASErr; //llega - -> !!ERROR!! .333f+33-
		this.matriz_acciones_semanticas[8][9] = ASErr; //llega * ->  !!ERROR!! .333f+33*
		this.matriz_acciones_semanticas[8][10] = ASErr; //llega / -> !!ERROR!! .333f+33/		
		this.matriz_acciones_semanticas[8][11] = ASErr; //llega = -> !!ERROR!! .333f+33=

		this.matriz_acciones_semanticas[8][12] = ASErr; //llega <  ->  !!ERROR!! .333f+33<
		this.matriz_acciones_semanticas[8][13] = ASErr; //llega >  ->  !!ERROR!! .333f+33>
		this.matriz_acciones_semanticas[8][14] = ASErr; //llega !  ->  !!ERROR!! .333f+33!
		this.matriz_acciones_semanticas[8][15] = ASErr; //llega (  ->  !!ERROR!! .333f+33(
		this.matriz_acciones_semanticas[8][16] = ASErr; //llega )  ->  !!ERROR!! .333f+33)
		this.matriz_acciones_semanticas[8][17] = ASErr; //llega {  ->  !!ERROR!! .333f+33{
		this.matriz_acciones_semanticas[8][18] = ASErr; //llega }  ->  !!ERROR!! .333f+33}
		this.matriz_acciones_semanticas[8][19] = ASErr; //llega ,  ->  !!ERROR!! .333f+33,
		this.matriz_acciones_semanticas[8][20] = ASErr; //llega ;  ->  !!ERROR!! .333f+33;
		//this.matriz_acciones_semanticas[8][20] = ASErr; //llega :  ->  !!ERROR!! .333f+33:
		
		this.matriz_acciones_semanticas[8][21] = ASErr; //llega % ->  
		this.matriz_acciones_semanticas[8][22] = ASErr; //llega " ->  
	
		this.matriz_acciones_semanticas[8][23] = AS4; //llega \n -> DescartarBuffer(); //a E4 le llega otro caracter -> entrego token tipo constante
		this.matriz_acciones_semanticas[8][24] = AS4; //llega otro caracter -> DescartarBuffer(); //a E4 le llega otro caracter -> entrego token tipo constante
		
		

		
		//fila 9 -> comparadores
		
		this.matriz_acciones_semanticas[9][0] = ASErr; //llega a E9 una letra -> !!ERROR!!  >a 
		this.matriz_acciones_semanticas[9][1] = ASErr; //llega a E9 un digito -> !!ERROR!! <3 
		this.matriz_acciones_semanticas[9][2] = ASErr; //llega a E9 un _ -> !!ERROR!! <_
		this.matriz_acciones_semanticas[9][3] = ASErr; //llega i ->  
		this.matriz_acciones_semanticas[9][4] = ASErr; //llega . -> 
		this.matriz_acciones_semanticas[9][5] = ASErr; //llega f -> 
		this.matriz_acciones_semanticas[9][6] = AS3; //llega blanco ' ' -> LlegaTokenValido(); entregar token
		this.matriz_acciones_semanticas[9][7] = ASErr; //llega + ->  
		this.matriz_acciones_semanticas[9][8] = ASErr; //llega - ->  
		this.matriz_acciones_semanticas[9][9] = ASErr; //llega * ->  
		this.matriz_acciones_semanticas[9][10] = ASErr; //llega / ->  		
		this.matriz_acciones_semanticas[9][11] = AS3; //llega = -> LlegaTokenValido(); entregar token
		
		//VER!!! automata
		this.matriz_acciones_semanticas[9][12] = AS3; //llega <  ->  posible token valido
		this.matriz_acciones_semanticas[9][13] = AS3; //llega >  ->  posible token valido
		this.matriz_acciones_semanticas[9][14] = AS3; //llega !  ->  posible token valido. checkear en AS3
		this.matriz_acciones_semanticas[9][15] = ASErr; //llega (  ->  !!ERROR!! <(
		this.matriz_acciones_semanticas[9][16] = ASErr; //llega )  ->  !!ERROR!!
		this.matriz_acciones_semanticas[9][17] = ASErr; //llega {  ->  !!ERROR!!
		this.matriz_acciones_semanticas[9][18] = ASErr; //llega }  ->  !!ERROR!!
		this.matriz_acciones_semanticas[9][19] = ASErr; //llega ,  ->  !!ERROR!!
		this.matriz_acciones_semanticas[9][20] = ASErr; //llega ;  ->  !!ERROR!!
		//this.matriz_acciones_semanticas[9][20] = ASErr; //llega ;  ->  !!ERROR!!

		
		this.matriz_acciones_semanticas[9][21] = ASErr; //llega % -> 
		this.matriz_acciones_semanticas[9][22] = ASErr; //llega " ->  
	
		this.matriz_acciones_semanticas[9][23] = AS4; //llega \n ->
		this.matriz_acciones_semanticas[9][24] = AS4; //llega otro caracter -> DescartarBuffer(); //a E4 le llega otro caracter -> entrego token tipo constante
		

		
		
		//fila (estado) 10 -> definicion de comentario
		
		this.matriz_acciones_semanticas[10][0] = AS4; //llega a E10 una letra -> DescartarBuffer(); 
		this.matriz_acciones_semanticas[10][1] = AS4; //llega a E10 un digito -> DescartarBuffer();  
		this.matriz_acciones_semanticas[10][2] = AS4; //llega _ -> DescartarBuffer();
		this.matriz_acciones_semanticas[10][3] = AS4; //llega i -> DescartarBuffer(); 
		this.matriz_acciones_semanticas[10][4] = AS4; //llega . -> DescartarBuffer(); 
		this.matriz_acciones_semanticas[10][5] = AS4; //llega f -> DescartarBuffer(); 
		this.matriz_acciones_semanticas[10][6] = AS3; //llega blanco ' ' -> DescartarBuffer();
		this.matriz_acciones_semanticas[10][7] = AS4; //llega + -> DescartarBuffer(); 
		this.matriz_acciones_semanticas[10][8] = AS4; //llega - -> DescartarBuffer();
		this.matriz_acciones_semanticas[10][9] = AS4; //llega * -> DescartarBuffer(); 
		this.matriz_acciones_semanticas[10][10] = AS4; //llega / -> DescartarBuffer(); 				
		this.matriz_acciones_semanticas[10][11] = AS4; //llega = -> DescartarBuffer(); 
		this.matriz_acciones_semanticas[10][12] = AS4; //llega <  -> DescartarBuffer();
		this.matriz_acciones_semanticas[10][13] = AS4; //llega >  -> DescartarBuffer();
		this.matriz_acciones_semanticas[10][14] = AS4; //llega !  -> DescartarBuffer();
		this.matriz_acciones_semanticas[10][15] = AS4; //llega (  -> DescartarBuffer();
		this.matriz_acciones_semanticas[10][16] = AS4; //llega )  -> DescartarBuffer();
		this.matriz_acciones_semanticas[10][17] = AS4; //llega {  -> DescartarBuffer();
		this.matriz_acciones_semanticas[10][18] = AS4; //llega }  -> DescartarBuffer();
		this.matriz_acciones_semanticas[10][19] = AS4; //llega ,  -> DescartarBuffer();
		this.matriz_acciones_semanticas[10][20] = AS4; //llega ;  -> DescartarBuffer();

		this.matriz_acciones_semanticas[10][21] = AS2; //llega % -> AgregarCaracter();
		this.matriz_acciones_semanticas[10][22] = AS4; //llega " -> DescartarBuffer();			
		this.matriz_acciones_semanticas[10][23] = AS4; //llega \n -> DescartarBuffer(); //a E4 le llega otro caracter -> entrego token tipo constante
		this.matriz_acciones_semanticas[10][24] = AS4; //llega otro caracter -> DescartarBuffer(); //a E4 le llega otro caracter -> entrego token tipo constante
		

		
		
		//fila (estado) 11 -> cuerpo del comentario
		this.matriz_acciones_semanticas[11][0] = AS2; //llega a E11 una letra -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[11][1] = AS2; //llega a E11 un digito -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[11][2] = AS2; //llega _ -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[11][3] = AS2; //llega i -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[11][4] = AS2; //llega . -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[11][5] = AS2; //llega f -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[11][6] = AS2; //llega blanco ' ' -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[11][7] = AS2; //llega + -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[11][8] = AS2; //llega - -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[11][9] = AS2; //llega * -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[11][10] = AS2; //llega / -> AgregarCaracter(); 					
		this.matriz_acciones_semanticas[11][11] = AS2; //llega = -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[11][12] = AS2; //llega <  -> DescartarBuffer();
		this.matriz_acciones_semanticas[11][13] = AS2; //llega >  -> DescartarBuffer();
		this.matriz_acciones_semanticas[11][14] = AS2; //llega !  -> DescartarBuffer();
		this.matriz_acciones_semanticas[11][15] = AS2; //llega (  -> DescartarBuffer();
		this.matriz_acciones_semanticas[11][16] = AS2; //llega )  -> DescartarBuffer();
		this.matriz_acciones_semanticas[11][17] = AS2; //llega {  -> DescartarBuffer();
		this.matriz_acciones_semanticas[11][18] = AS2; //llega }  -> DescartarBuffer();
		this.matriz_acciones_semanticas[11][19] = AS2; //llega ,  -> DescartarBuffer();
		this.matriz_acciones_semanticas[11][20] = AS2; //llega ;  -> DescartarBuffer();

		this.matriz_acciones_semanticas[11][21] = AS2; //llega % -> AgregarCaracter(); 
		this.matriz_acciones_semanticas[11][22] = AS2; //llega " -> AgregarCaracter(); 
					
		this.matriz_acciones_semanticas[11][23] = AS2; //llega \n -> !!COMENTARIO VALIDO!! 
		this.matriz_acciones_semanticas[11][24] = AS2; //llega otro caracter -> AgregarCaracter(); //a E11 le llega otro caracter -> agrego caracter
		
		

		//fila 12 -> Inicializacion de cadena 
		//!!!VER!!! cambiar ASF's 
		this.matriz_acciones_semanticas[12][0] = ASF; //llega a E12 una letra ->
		this.matriz_acciones_semanticas[12][1] = ASF; //llega a E12 un digito ->
		this.matriz_acciones_semanticas[12][2] = ASF; //llega a E12 un _ -> 
		this.matriz_acciones_semanticas[12][3] = ASF; //llega i ->
		this.matriz_acciones_semanticas[12][4] = ASF; //llega . ->
		this.matriz_acciones_semanticas[12][5] = ASF; //llega f -> 	
		this.matriz_acciones_semanticas[12][6] = ASF; //llega blanco ->
		this.matriz_acciones_semanticas[12][7] = ASF; //llega + ->
		this.matriz_acciones_semanticas[12][8] = ASF; //llega - -> 
		this.matriz_acciones_semanticas[12][9] = ASF; //llega * -> 
		this.matriz_acciones_semanticas[12][10] = ASF; //llega / ->
		this.matriz_acciones_semanticas[12][11] = ASF; //llega = ->
		this.matriz_acciones_semanticas[12][12] = ASF; //llega < ->
		this.matriz_acciones_semanticas[12][13] = ASF; //llega > ->
		this.matriz_acciones_semanticas[12][14] = ASF; //llega ! ->
		this.matriz_acciones_semanticas[12][15] = ASF; //llega ( ->
		this.matriz_acciones_semanticas[12][16] = ASF; //llega ) ->
		this.matriz_acciones_semanticas[12][17] = ASF; //llega { ->
		this.matriz_acciones_semanticas[12][18] = ASF; //llega } ->
		this.matriz_acciones_semanticas[12][19] = ASF; //llega , ->
		this.matriz_acciones_semanticas[12][20] = ASF; //llega ; ->

		this.matriz_acciones_semanticas[12][21] = ASF; //llega % -> 
		this.matriz_acciones_semanticas[12][22] = ASF; //llega " -> 

		this.matriz_acciones_semanticas[12][23] = ASF; //llega \n ->
		this.matriz_acciones_semanticas[12][24] = ASF; //llega otro caracter -> 

		
		
		//estado 13 -> forrada de transicion de cadenas
		this.matriz_acciones_semanticas[13][0] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[13][1] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[13][2] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[13][3] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[13][4] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[13][5] = ASF; 	
		this.matriz_acciones_semanticas[13][6] = ASF; 
		this.matriz_acciones_semanticas[13][7] = ASF;
		this.matriz_acciones_semanticas[13][8] = ASF; 
		this.matriz_acciones_semanticas[13][9] = ASF; 
		this.matriz_acciones_semanticas[13][10] = ASF; 
		this.matriz_acciones_semanticas[13][11] = ASF; 
		this.matriz_acciones_semanticas[13][12] = ASF; 
		this.matriz_acciones_semanticas[13][13] = ASF; 
		this.matriz_acciones_semanticas[13][14] = ASF; 
		this.matriz_acciones_semanticas[13][15] = ASF; 
		this.matriz_acciones_semanticas[13][16] = ASF; 
		this.matriz_acciones_semanticas[13][17] = ASF; 
		this.matriz_acciones_semanticas[13][18] = ASF; 
		this.matriz_acciones_semanticas[13][19] = ASF;
		this.matriz_acciones_semanticas[13][20] = ASF; 
				
		this.matriz_acciones_semanticas[13][21] = ASF; //MostrarComentario() y descartar; //a E12 le llega % -> descarto buffer
		this.matriz_acciones_semanticas[13][22] = ASF; 

		this.matriz_acciones_semanticas[13][23] = ASF; 

		
		
		//fila EstadoFinal
		this.matriz_acciones_semanticas[14][0] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[14][1] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[14][2] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[14][3] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[14][4] = ASF; //en EF aplico ASF y reinicio
		this.matriz_acciones_semanticas[14][5] = ASF; 	
		this.matriz_acciones_semanticas[14][6] = ASF; 
		this.matriz_acciones_semanticas[14][7] = ASF;
		this.matriz_acciones_semanticas[14][8] = ASF; 
		this.matriz_acciones_semanticas[14][9] = ASF; 
		this.matriz_acciones_semanticas[14][10] = ASF; 
		this.matriz_acciones_semanticas[14][11] = ASF; 
		this.matriz_acciones_semanticas[14][12] = ASF; 
		this.matriz_acciones_semanticas[14][13] = ASF; 
		this.matriz_acciones_semanticas[14][14] = ASF; 
		this.matriz_acciones_semanticas[14][15] = ASF; 
		this.matriz_acciones_semanticas[14][16] = ASF; 
		this.matriz_acciones_semanticas[14][17] = ASF; 
		this.matriz_acciones_semanticas[14][18] = ASF; 
		this.matriz_acciones_semanticas[14][19] = ASF;
		this.matriz_acciones_semanticas[14][20] = ASF; 

		
		this.matriz_acciones_semanticas[14][21] = ASF; //MostrarComentario() y descartar; //a E12 le llega % -> descarto buffer
		this.matriz_acciones_semanticas[14][22] = ASF; 

		this.matriz_acciones_semanticas[14][23] = ASF; 
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
				
				System.out.println("\n Lexico  ->  Linea: "+ titi.getNroLinea() + " , Token: "+ titi.getLexema()+" ,  tipo: "+titi.getTipo()+" \n");
				}
		}
		return titi;
	}

	
	
	public int getColumnaCaracter(char c) {
	
		int nro_columna=23; //inicializo con otro caracter
		
		/*
		if (c == 'i') { 
			nro_columna=3;
		} else if (c == 'f') {
			nro_columna=5;
		} else if (Character.isLowerCase(c) | Character.isLetter(c)) { 
			nro_columna=0; 
		} 
		*/
		
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
		
		if (c == '<') { nro_columna=12; };
		if (c == '>') { nro_columna=13; };
		if (c == '!') { nro_columna=14; };
		
		if (c == '(') { nro_columna=15; };
		if (c == ')') { nro_columna=16; };
		if (c == '{') { nro_columna=17; };
		if (c == '}') { nro_columna=18; };
		
		if (c == ',') { nro_columna=19; };
		
		if (c == ';') { nro_columna=20; };
		if (c == '%') { nro_columna=21; };
		if (c == '"') { nro_columna=22; };
		//VER!! como asociar salto de linea!! Ccon codice ASCII
		//if (c == '%') { nro_columna=21; };
		int ascii = (int)c;
		if (ascii == 10) { nro_columna=23; }
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
	
	
	public void asociarTablaDeSimbolos(TablaDeSimbolos tds) {
		System.out.println("asocio algo?");
		this.tds = tds;
	}
}
