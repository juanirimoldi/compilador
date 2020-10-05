//DECLARATIONS

%{
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;  //????

//package analizador_lexico;
import analizador_lexico.*;

%}

%token
		ID
		CTE
		CADENA
		ASIGNACION
		// Palabras reservadas
		IF
		THEN
		ELSE
		END_IF
		BEGIN
		END
		INTEGER
		DOUBLE
		LONG 
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
 | exp '+' exp { $$ = new ParserVal($1.dval + $3.dval); }
 | exp '-' exp { $$ = new ParserVal($1.dval - $3.dval); }
 | exp '*' exp { $$ = new ParserVal($1.dval * $3.dval); }
 | exp '/' exp { $$ = new ParserVal($1.dval / $3.dval); }
 | '-' exp %prec NEG { $$ = new ParserVal(-$2.dval); }
 | exp '^' exp { $$ = new ParserVal(Math.pow($1.dval, $3.dval)); }
 | '(' exp ')' { $$ = $2; }
 ;

%%




// CODE


/*
// MASSA

// CARACTER DE SINCRONIZACION -> ;
// ; -> "SEGURO" QUE CIERRA UNA REGLA


EJEMPLO 

// LINEA 5 -> ASIGNACION ENCONTRADA
// LINEA 9 -> ASIGNACION CORRECTA ENCONTRADA
// LINEA 14 -> ERROR SINTACTICO. ESTRUCTURA DE ASIGNACION MAL ESCRITA


//EJ MASSA
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


//AnalizadorLexico lexico;


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
