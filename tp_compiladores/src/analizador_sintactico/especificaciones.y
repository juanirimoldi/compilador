//DECLARATIONS

%{
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;  //????

//package analizador_lexico;
import analizador_lexico.*;

%}

//en lexico
%token
		ID
		CTE
		CADENA
		ASIG
		// Palabras reservadas
		IF
		THEN
		ELSE
		END_IF
		BEGIN
		END
		INTEGER
		DOUBLE
		WHILE
		//CASE 
		DO
		RETURN 
		PRINT
		
		/* Comparadores */
		MAYORIGUAL
		MENORIGUAL
		IGUAL
		DISTINTO
		EOF
//%%


/* YACC Declarations */
%token ID, CTE
%left '-' '+'
%left '*' '/'
%left NEG /* negation--unary minus */
%right '^' /* exponentiation */




//ACTIONS


/* Grammar follows */
%%

input: /* empty string */
 | input line
 ;

line: '\n'
 | exp '\n' { System.out.println(" " + $1.dval + " "); }
 ;

exp: CTE { $$ = $1; }
 | exp '+' exp { $$ = new ParserVal($1.dval + $3.dval); } //{println pra debuguear -> por ej encontro if}
 | exp '-' exp { $$ = new ParserVal($1.dval - $3.dval); } //{linea x -> asignacion, etc +  errores
 | exp '*' exp { $$ = new ParserVal($1.dval * $3.dval); }
 | exp '/' exp { $$ = new ParserVal($1.dval / $3.dval); }
 | '-' exp %prec NEG { $$ = new ParserVal(-$2.dval); }
 | exp '^' exp { $$ = new ParserVal(Math.pow($1.dval, $3.dval)); }
 | '(' exp ')' { $$ = $2; }
 ;



// GRAMATICA
/* Sentencias / reglas  tema 8

Sentencias declarativas:
Sentencias de declaración de datos para los tipos de datos correspondientes a cada grupo según la consigna del Trabajo
Práctico 1, con la siguiente sintaxis:

<tipo> <lista_de_variables>;

Donde <tipo> puede ser (Según tipos correspondientes a cada grupo):

	INTEGER
	UINT
	LONGINT
	ULONGINT
	FLOAT
	DOUBLE

Las variables de la lista se separan con coma ( “,” )

Incluir declaraciones de procedimientos, con la siguiente sintaxis:

PROC ID (<lista_de_parametros>) {
	<cuerpo_del_procedimiento> // conjunto de sentencias declarativas y ejecutables
	// permitir procedimientos anidados
}

Donde:
<lista_de_parametros> será una lista de parámetros, separados por “,”, con la siguiente estructura para cada
parámetro:
o <tipo> ID
o El número máximo de parámetros permitidos es 3, y puede no haber parámetros. Este chequeo debe
efectuarse durante el Análisis Sintáctico
// Incorporar PROC a la lista de palabras reservadas.

TEMA 8
Sentencias declarativas:
Modificar los encabezados de declaraciones de procedimientos con la siguiente estructura:

PROC ID (<lista_de_parametros>) NI=n { ... }
Donde n será una constante de tipo entero (temas 1-2-3-4)
Modificar la estructura de cada parámetro, permitiendo usar la palabra reservada REF, delante de cada
parámetro.
Ejemplos de declaraciones de procedimiento válidas:

PROC f1(INTEGER x, REF FLOAT y, FLOAT z) NI = 2 {...}
PROC f2(REF DOUBLE a, REF DOUBLE b) NI = 4 {...}

// Incorporar NI y REF a la lista de palabras reservadas.



Sentencias ejecutables:

Asignaciones donde el lado izquierdo es un identificador, y el lado derecho una expresión aritmética.
Los operandos de las expresiones aritméticas pueden ser variables, constantes, u otras expresiones aritméticas.
No se deben permitir anidamientos de expresiones con paréntesis.

Cláusula de selección (IF). Cada rama de la selección será un bloque de sentencias. La condición será una comparación
entre expresiones aritméticas, variables o constantes, y debe escribirse entre “(“ “)”. La estructura de la selección será,
entonces:

IF (<condicion>) <bloque_de_sentencias> ELSE <bloque_de_sentencias> END_IF

El bloque para el ELSE puede estar ausente.
Un bloque de sentencias puede estar constituido por una sola sentencia, o un conjunto de sentencias delimitadas por „{„ y
„}‟.
Sentencia de control según tipo especial asignado al grupo. (Temas 12 A 14 del Trabajo práctico 1)
Debe permitirse anidamiento de sentencias de control. Por ejemplo, puede haber una iteración dentro de una rama de una
selección.
Sentencia de salida de mensajes por pantalla. El formato será

OUT(cadena)

Las cadenas de caracteres sólo podrán ser usadas en esta sentencia, y tendrán el formato asignado al grupo en el Trabajo
Práctico 1 (temas 20 y 21).
 Incorporar como sentencia ejecutable, invocaciones a procedimientos con la siguiente estructura:

ID(<parametros>)
Donde <parsmetros> será una lista de identificadores separados por “,”.
Nota: La semántica de la declaración e invocación de procedimientos, se explicará y resolverá en los trabajos prácticos 3 y 4.


Sentencias ejecutables:
Modificar las invocaciones a procedimientos, incorporando notación explícita (indicando los nombres de los
parámetros formales) para los parámetros:
La invocación a una procedimiento será:

ID(<parametros>)
Donde cada parámetro de la lista tendrá la siguiente estructura:
ID:ID

Ejemplos de invocaciones a procedimiento válidas:

f1(z:i, y:j, x:k)
f2(a:m, b:n)

// Incorporar “:” a la lista de tokens aceptados por el Analizador Léxico.
*/

%%




// CODE


/*
// JOSE MASSA

// CARACTER DE SINCRONIZACION -> ;
// ; -> "SEGURO" QUE CIERRA UNA REGLA


EJEMPLO 

// LINEA 5 -> ASIGNACION ENCONTRADA
// LINEA 9 -> ASIGNACION CORRECTA ENCONTRADA
// LINEA 14 -> ERROR SINTACTICO. ESTRUCTURA DE ASIGNACION MAL ESCRITA


//EJ. 
el fuente es:
int x;
x=1;

//LEXICO
la salida del compilador seria
linea 1 token palabra reservada int
liena 1 token identificador x

//SINTACTICO
linea 1 declaración (sintatctico)
linea 2 asignacion

//TODO DE UNA
linea 1 declaración (sintatctico)
linea 2 token identifcador x
linea 2 token =
linea 2 constante 1
linea 2 asignacion

// CONSIDERAR CTE NEGATIVA!


SI -> en linea 2: x = ;
//EL LEXICO DICE QUE EN LINEA 2 HASTA EL =
//EL SINTACTICO DICE LINEA 2 -> ASIGNACION INCORRECTA

//ACA DEFINIR BIEN LAS REGLAS DE ERROR!!!

//PARA CASO DE ERROR
linea 2 token id. x
linea 2 token =
error linea 2 caracter inválidi -> ERROR LEXICO


//CUANDO HAY TOKEN INVALIDO -> SINTACTICO PUDE NUEVO TOKEN A LEXICO
// LE DEVUELVE ; -> RECIBE DOBLE ; -> OTRO ERROR SINTACTICO
// CUALQUIER ERROR (LEXICO O SINTACTIC) AVISA A ETAPAS POSTERIORES QUE NO HAY QUE HGACER NADA

TIP -> HACERLO ANDAR CON EL TOKEN ERROR

*/




//CODE


AnalizadorLexico lexico;


String ins;
StringTokenizer st;
boolean newline;


void yyerror(String s)
{
 System.out.println("par:"+s);
}


/*  YYLEX DE DOCUMENTACION
int yylex()
{
String s;
int tok;
Double d;
 //System.out.print("yylex ");
 if (!st.hasMoreTokens())
 if (!newline)
 {
 newline=true;
 return '\n'; //So we look like classic YACC example
 }
 else
 return 0;
 s = st.nextToken();
 //System.out.println("tok:"+s);
 try
 {
 d = Double.valueOf(s); //this may fail
 yylval = new ParserVal(d.doubleValue()); //SEE BELOW
 tok = NUM;
 }
 catch (Exception e)
 {
 tok = s.charAt(0);//if not float, return char
 }
 return tok;
}
*/


int yylex()   //YYLEX NUESTRO
{
	String s;
	int tok;
	Double d;
	//System.out.print("yylex ");
	//if (!st.hasMoreTokens())
		//if (!newline)
		//{
			//newline=true;
			//return '\n'; //So we look like classic YACC example
		//}
	//else
	 //return 0;
 
 
 //s = st.nextToken(); //ACA!!!
 //s = lexico.yylex(); //aca devuelve Token
//if (s != null) {
if (lexico.quedanTokens()) {
//System.out.println("El lexico No tiene simbolos que procesar!!");
	s = lexico.getToken().getLexema();

	this.lexico.mostrarTablaSimbolos();
//s = lexico.yylex().getTipo(); //aca que tengo que devolver id?
 
	try
	{
		d = Double.valueOf(s);/*this may fail*/
		yylval = new ParserVal(d.doubleValue()); //SEE BELOW
		tok = NUM;  //NUM es tipo token
	}
	catch (Exception e)
	{
		System.out.println("EXCEPCION!!!");
		tok = s.charAt(0);/*if not float, return char*/
	}
	return tok;
	}
return 0;
}



void dotest()
{
BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
 System.out.println("BYACC/J Calculator Demo");
 System.out.println("Note: Since this example uses the StringTokenizer");
 System.out.println("for simplicity, you will need to separate the items");
 System.out.println("with spaces, i.e.: '( 3 + 5 ) * 2'");
 while (true)
 {
 System.out.print("expression:");
 try
 {
 ins = in.readLine();
 }
 catch (Exception e)
 {
 }
 st = new StringTokenizer(ins);
 newline=false;
 yyparse();
 }
}




public static void main(String args[]) {
 	TablaTokens tt = new TablaTokens();
	TablaSimbolos ts = new TablaSimbolos();
	
 	AnalizadorLexico lexico = new AnalizadorLexico(tt, ts);
	lexico.abrirCargarArchivo();
	//lexico.mostrarTablaSimbolos(); //esta vacia!! te
	//lexico.getToken();
	
	Parser par = new Parser(false, lexico);
 	par.dotest();
}
