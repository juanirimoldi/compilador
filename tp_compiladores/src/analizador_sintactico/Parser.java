package analizador_sintactico;

//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramaticaIncremental.y"
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;  /*????*/
/*package analizador_sintactico;*/

import analizador_lexico.*;
//#line 24 "Parser.java"
import tabla_simbolos.TablaDeSimbolos;




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short CTE=258;
public final static short CADENA=259;
public final static short IF=260;
public final static short THEN=261;
public final static short ELSE=262;
public final static short END_IF=263;
public final static short PROC=264;
public final static short RETURN=265;
public final static short NI=266;
public final static short REF=267;
public final static short OUT=268;
public final static short LOOP=269;
public final static short UNTIL=270;
public final static short INTEGER=271;
public final static short FLOAT=272;
public final static short MENOR=273;
public final static short MAYOR=274;
public final static short MENORIGUAL=275;
public final static short MAYORIGUAL=276;
public final static short IGUAL=277;
public final static short DISTINTO=278;
public final static short EOF=279;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    3,    3,    5,    6,    9,
   10,   10,   11,   12,   13,   13,   13,   14,   14,    8,
    8,    7,    7,    4,    4,    4,    4,    4,   15,   15,
   16,   21,   22,   22,   25,   17,   26,   27,   28,   29,
   23,   23,   23,   23,   23,   23,   24,   18,   19,   30,
   30,   20,   20,   20,   20,   31,   31,   31,   32,   32,
   32,
};
final static short yylen[] = {                            2,
    1,    1,    2,    2,    2,    1,    1,    2,    4,    2,
    3,    2,    3,    3,    5,    3,    1,    2,    3,    3,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    6,
    4,    3,    1,    3,    1,    4,    1,    1,    1,    3,
    3,    3,    3,    3,    3,    3,    3,    4,    4,    3,
    5,    3,    3,    1,    1,    3,    3,    1,    1,    2,
    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   37,   22,   23,    0,    0,    2,
    0,    0,    6,    7,    0,    0,   24,   25,   26,   27,
   28,    0,    0,    0,    0,    0,   10,    0,    3,    4,
    5,   21,    0,    0,    0,    0,   38,    0,    0,    0,
   61,   59,   55,    0,    0,    0,    0,   58,    0,    0,
    0,    0,    0,    0,    0,   12,    0,    0,    0,    0,
    0,    0,   39,    0,    0,   49,    0,    0,   60,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   32,
   31,   35,    0,   48,   20,    0,   18,   11,    0,    0,
    0,    9,   47,    0,   36,   50,    0,    0,    0,    0,
   56,   57,    0,    0,    0,    0,    0,    0,   34,   19,
    0,   13,    0,    0,    0,    0,    0,   14,   40,   51,
   15,
};
final static short yydgoto[] = {                          8,
    9,   10,   11,   12,   13,   14,   15,   33,   16,   35,
   61,   92,   58,   59,   17,   18,   19,   20,   21,   49,
   26,   51,   50,   37,   83,   22,   38,   64,   95,   40,
   47,   48,
};
final static short yysindex[] = {                      -215,
   -6,   -5, -214,   12,    0,    0,    0,    0, -215,    0,
    8,   10,    0,    0, -197,   24,    0,    0,    0,    0,
    0,  -53, -186,  -37,  -34,  -53,    0, -185,    0,    0,
    0,    0,   37,  -40, -184, -215,    0, -187,   26,    3,
    0,    0,    0, -213, -173,    5,    4,    0,  -23,   45,
 -175, -172,   48, -166, -213,    0, -165,   52,   50,   34,
  -27, -112,    0,   57, -159,    0, -156,   61,    0,  -39,
  -39,  -39,  -39,  -34,  -34,  -34,  -34,  -34,  -34,    0,
    0,    0,  -53,    0,    0, -154,    0,    0, -231, -153,
 -215,    0,    0,  -34,    0,    0,   46,  -34,    4,    4,
    0,    0,    5,    5,    5,    5,    5,    5,    0,    0,
   62,    0,  -95,   66, -149,    5, -231,    0,    0,    0,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  109,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   51,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   53,  -41,    0,    0,    0,
    0, -152,    0,    0,    0,    0,    0,    0,   72,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -36,  -31,
    0,    0,   73,   74,   75,   76,   77,   79,    0,    0,
   80,    0,    0,    0,    0,    8,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
   -4,    6,    0,    0,    0,    0,  -17,    0,    0,    0,
    0,    0,    0,  -56,    0,    0,    0,    0,    0,    1,
    0,    0,   28,  -10,    0,    0,    0,    0,    0,    0,
   -8,   -7,
};
final static int YYTABLESIZE=255;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         54,
   56,   54,   44,   54,   52,   45,   52,   45,   52,   53,
   45,   53,   93,   53,   29,   52,   57,   54,   54,   70,
   54,   71,   52,   52,   46,   52,   68,   53,   53,  118,
   53,   62,  111,   23,   25,   55,   79,   86,   78,    6,
    7,    1,   27,   66,    2,   72,   67,   70,    3,   71,
   73,   28,    4,    5,   24,    6,    7,    6,    7,   32,
  121,   99,  100,   34,  101,  102,   30,   29,   31,   36,
   39,   57,  109,   53,  103,  104,  105,  106,  107,  108,
   54,   60,   63,   65,   69,   80,  113,   81,   84,   82,
   85,   87,   88,   89,   90,   91,   94,   96,  116,   57,
   97,   98,  110,  115,  112,  117,  119,  120,    1,    8,
   33,   29,   17,   44,   43,   45,   46,   41,   29,   42,
   16,  114,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    1,    0,    0,    2,    0,    0,
    0,    3,    0,    0,    0,    4,    5,    0,    6,    7,
    0,    1,    0,    0,    2,    0,    0,    0,    3,    0,
    0,    0,    4,    5,    0,    6,    7,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   41,   42,   41,
   42,   43,   41,   42,   43,    0,   55,    0,    0,    0,
    6,    7,    0,   54,   54,   54,   54,    0,   52,   52,
   52,   52,    0,   53,   53,   53,   53,    0,    0,    0,
    0,   74,   75,   76,   77,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   41,   43,   40,   45,   41,   45,   43,   45,   45,   41,
   45,   43,  125,   45,    9,   26,   34,   59,   60,   43,
   62,   45,   59,   60,   24,   62,   44,   59,   60,  125,
   62,   36,   89,   40,   40,  267,   60,   55,   62,  271,
  272,  257,  257,   41,  260,   42,   44,   43,  264,   45,
   47,   40,  268,  269,   61,  271,  272,  271,  272,  257,
  117,   70,   71,   40,   72,   73,   59,   62,   59,  123,
  257,   89,   83,  259,   74,   75,   76,   77,   78,   79,
   44,  266,  270,   58,  258,   41,   91,  263,   41,  262,
  257,  257,   41,   44,   61,  123,   40,  257,   98,  117,
  257,   41,  257,   58,  258,   44,   41,  257,    0,   59,
  263,   59,   41,   41,   41,   41,   41,   41,  113,   41,
   41,   94,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  257,   -1,   -1,  260,   -1,   -1,
   -1,  264,   -1,   -1,   -1,  268,  269,   -1,  271,  272,
   -1,  257,   -1,   -1,  260,   -1,   -1,   -1,  264,   -1,
   -1,   -1,  268,  269,   -1,  271,  272,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,  257,
  258,  259,  257,  258,  259,   -1,  267,   -1,   -1,   -1,
  271,  272,   -1,  275,  276,  277,  278,   -1,  275,  276,
  277,  278,   -1,  275,  276,  277,  278,   -1,   -1,   -1,
   -1,  275,  276,  277,  278,
};
}
final static short YYFINAL=8;
final static short YYMAXTOKEN=279;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","CTE","CADENA","IF","THEN","ELSE",
"END_IF","PROC","RETURN","NI","REF","OUT","LOOP","UNTIL","INTEGER","FLOAT",
"MENOR","MAYOR","MENORIGUAL","MAYORIGUAL","IGUAL","DISTINTO","EOF",
};
final static String yyrule[] = {
"$accept : programa",
"programa : lista_de_sentencias",
"lista_de_sentencias : sentencia",
"lista_de_sentencias : lista_de_sentencias sentencia",
"sentencia : sentencia_declarativa ';'",
"sentencia : sentencia_ejecutable ';'",
"sentencia_declarativa : declaracion_de_variable",
"sentencia_declarativa : declaracion_de_procedimiento",
"declaracion_de_variable : tipo lista_de_variables",
"declaracion_de_procedimiento : declaracion_PROC_ID lista_parametros_PROC cant_invocaciones_PROC cuerpo_PROC",
"declaracion_PROC_ID : PROC ID",
"lista_parametros_PROC : '(' lista_de_parametros ')'",
"lista_parametros_PROC : '(' ')'",
"cant_invocaciones_PROC : NI '=' CTE",
"cuerpo_PROC : '{' lista_de_sentencias '}'",
"lista_de_parametros : parametro_declarado ',' parametro_declarado ',' parametro_declarado",
"lista_de_parametros : parametro_declarado ',' parametro_declarado",
"lista_de_parametros : parametro_declarado",
"parametro_declarado : tipo ID",
"parametro_declarado : REF tipo ID",
"lista_de_variables : lista_de_variables ',' ID",
"lista_de_variables : ID",
"tipo : INTEGER",
"tipo : FLOAT",
"sentencia_ejecutable : asignacion",
"sentencia_ejecutable : sentencia_de_control",
"sentencia_ejecutable : sentencia_de_iteracion",
"sentencia_ejecutable : sentencia_de_salida",
"sentencia_ejecutable : invocacion",
"asignacion : ID '=' expresion",
"asignacion : ID '=' '(' tipo ')' expresion",
"sentencia_de_control : IF condicion_IF cuerpo_IF END_IF",
"condicion_IF : '(' condicion ')'",
"cuerpo_IF : bloque_de_sentencias",
"cuerpo_IF : bloque_de_sentencias entra_ELSE bloque_de_sentencias",
"entra_ELSE : ELSE",
"sentencia_de_iteracion : comienzo_LOOP cuerpo_LOOP entra_UNTIL condicion_LOOP",
"comienzo_LOOP : LOOP",
"cuerpo_LOOP : bloque_de_sentencias",
"entra_UNTIL : UNTIL",
"condicion_LOOP : '(' condicion ')'",
"condicion : expresion '>' expresion",
"condicion : expresion '<' expresion",
"condicion : expresion MAYORIGUAL expresion",
"condicion : expresion MENORIGUAL expresion",
"condicion : expresion IGUAL expresion",
"condicion : expresion DISTINTO expresion",
"bloque_de_sentencias : '{' lista_de_sentencias '}'",
"sentencia_de_salida : OUT '(' CADENA ')'",
"invocacion : ID '(' parametro_ejecutable ')'",
"parametro_ejecutable : ID ':' ID",
"parametro_ejecutable : parametro_ejecutable ',' ID ':' ID",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"expresion : CADENA",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : CTE",
"factor : '-' CTE",
"factor : ID",
};

//#line 751 "gramaticaIncremental.y"
	   	


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





public static void main(String args[]) throws IllegalArgumentException, IllegalAccessException {
 	String direccion_codigo = "casos_prueba_tercetos.txt";
	
 	AnalizadorLexico al = new AnalizadorLexico(direccion_codigo);
	al.abrirCargarArchivo();
	TablaDeSimbolos tds = new TablaDeSimbolos();
	String a = "main";
	
	ListaTercetos l = new ListaTercetos();
	
	
	Parser par = new Parser(false, al, tds, l, a);
 	par.yyparse();
 	
 	
 	//aca hago una limpieza de tsym de los ID que no tengan @
 	tds.mostrarSimbolos();
 	
 	l.mostrarTercetos();
 	
 	//GeneradorCodigo gc = new GeneradorCodigo(l);
 	//gc.generarCodigo();
}
//#line 429 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 51 "gramaticaIncremental.y"
{System.out.println("\n LLEGO A RAIZ! -> termino programa \n ");}
break;
case 4:
//#line 60 "gramaticaIncremental.y"
{/*System.out.println("\n SENTENCIA DECLARATIVA CORRECTA \n");*/
								   	   System.out.println("\n----------------------------------------\n");
									  }
break;
case 5:
//#line 63 "gramaticaIncremental.y"
{/*System.out.println("\n SENTENCIA EJECUTABLE CORRECTA \n");*/
		    	   				       System.out.println("\n----------------------------------------\n");
		  						     }
break;
case 6:
//#line 75 "gramaticaIncremental.y"
{/*System.out.println("\n VARIABLE DECLARADA CORRECTAMENTE \n ");*/
								   				 /*System.out.println("\n----------------------------------------\n");*/
												 }
break;
case 7:
//#line 78 "gramaticaIncremental.y"
{System.out.println("\n PROCEDIMIENTO DECLARADO CORRECTAMENTE \n ");
				      								  System.out.println("\n----------------------------------------\n");
				      								  }
break;
case 8:
//#line 84 "gramaticaIncremental.y"
{Token tipo = (Token)val_peek(1).obj;
												   Token variable = (Token)val_peek(0).obj;
												   
												   System.out.println("\n REGLA DECLARATIVA DETECTADA !! -> por defecto agrego  "+ variable.getLexema() +"  a TSym   \n");
												   
												   System.out.println("\n VARIABLE BIEN DEFINIDA   ->   "+tipo.getLexema()+" "+variable.getLexema()+"\n");
												   /*variable.setAmbito(ambito); */
												   
												   String nombre = variable.getLexema()+"@"+ambito;
												   
												   variable.setLexema(nombre);
												   /*name mangling! cambiar variable por variable@main*/
												   
												   System.out.println("\n Le hago name mangling con el ambito -> "+ambito+"\n");
												   
												   /*remuevo variable vieja????????*/
												   /*this.tabla.eliminarSimbolo($2);*/
												   
												   /*aca le agrego uso a variable!*/
												   }
break;
case 9:
//#line 111 "gramaticaIncremental.y"
{
																											System.out.println("\n\n PROC CORRECTAMENTE DECLARADO \n\n");
																											}
break;
case 10:
//#line 117 "gramaticaIncremental.y"
{
								System.out.println("\n CREO TERCETO PROC -> ( PROC , $$.obj.getLexema() ID , -- ) \n");
								
								String pos_str = "["+pos_ultimo_terceto+"]";
 								
 								/*Token id = (Token)$2.obj;*/
 								  
 								/*$$ = $3; //a pesar de la asignacion, $$ sigue apuntando al primer valor*/
 								Token id = (Token)val_peek(0).obj ;
 								
 								String nombre = id.getLexema()+"@"+ambito;
												   
							 	id.setLexema(nombre);
								/*aca hacer name mangling! cambiar variable por variable@main*/
												     	  	 
 								this.ambito_proc = id.getLexema();
 								  	    
 	 							Terceto ter = new Terceto(this.nro_terceto, "PROC", id.getLexema() , "--" );
 								this.lista.agregarTerceto(ter);
 								   
 								/*this.pos_BF = this.nro_terceto; //marco posicion del terceto BF !*/
 								
 								
 								/*aca le completo el atributo uso al token proced  */
 								
 								  
 								pos_ultimo_terceto = this.nro_terceto;
 					
 								this.lista.mostrarTercetos();
 					     		  
 					     		this.nro_terceto++;
								}
break;
case 11:
//#line 152 "gramaticaIncremental.y"
{System.out.println("\n ACA LE DOY SEMANTICA A LA LISTA DE PARAMETROS \n");
													/*aclarar reglas de pasaje de parametros*/
													/*COPIAR POR EJ X E Y -> */
													/*para cada parametro se debe registrar: */
													/*	tipo */
													/*	forma de pasaje -> REF , copia-valor por defecto*/
													}
break;
case 13:
//#line 164 "gramaticaIncremental.y"
{
									/*ACA CHECKEO QUE CTE SEA < 4*/
									/*para este proc -> asigno la cantidad de invocaciones que puede efectuar -> CTE*/
					   				System.out.println("ACA CHECKEO QUE CTE SEA < 4 , y la cant de invocaciones al PROC");
					   				/*CTE debe ser de tipo entero (1, 2, 3, 4 )*/
					   				
					   				/*checkeo nro y tipo de parametros en la invocacion*/
					   				/*nro de invocaciones efectuadas por cada procedimiento*/
					   				}
break;
case 14:
//#line 176 "gramaticaIncremental.y"
{
											/*ACA CREO LAS VARIABLES DEL CUERPO DE ACUERDO AL AMBITO DONDE SE ENCUENTRA*/
											System.out.println("\n CREO LAS VARIABLES DE ACUERDO AL AMBITO DEFINIDO DEL PROC \n");
											
											/*dentro de la lista de sentencias, si defino una variable debo agregar el ambito*/
											}
break;
case 17:
//#line 191 "gramaticaIncremental.y"
{ System.out.println("\n\n 1 solo parametro \n\n"); }
break;
case 28:
//#line 222 "gramaticaIncremental.y"
{System.out.println("\n   \n");}
break;
case 29:
//#line 228 "gramaticaIncremental.y"
{  Token id = (Token)val_peek(2).obj;
								  int linea = id.getNroLinea();
								  
								  System.out.println("\n REGLA ASIGNACION!!  Esta correctamente inicializada  "+ id.getLexema() +"  en Tabla de Simbolos ? \n");
								  
								  
								  Token op = (Token)val_peek(1).obj;
								
								  yyval = val_peek(0); /*a pesar de la asignacion, $$ sigue apuntando al primer valor*/
								  
								  
								  /*ACA!! VER CONVERSIONES EXPLICITAS!*/
								  /*primero checkea tipo de ID*/
								  /*despues checkea tipo de $$(expr)*/
								  
								  /*solo se podran efectuar operaciones entre dos operandos de distinto tipo si se convierte el operando de tipo entero al tipo de punto flotante*/
								  /*caso contrario -> ERROR*/
								  
								  /*si lado izq es tipo entero y lado der != tipo -->> ERROR de compatibilidad de tipos*/
								  
								  
								  
								  if (isToken) { 
								  	/*System.out.println("\n EXPRESION $$ TOKEN!!  -> "+ ((Token)$$.obj).getLexema() +"\n");*/
							
								  	if (tabla.correctamenteDefinido(id)){
								  		System.out.println("\n SI, esta bien definido "+ id.getLexema()+"\n");
								  		System.out.println("\n CREO TERCETO ASIGNACION  ->  ( "+op.getLexema()+" , "+id.getLexema()+" , "+((Token)yyval.obj).getLexema()+" )  \n\n");
								  		Terceto ter = new Terceto(this.nro_terceto, op.getLexema(), id.getLexema(), ((Token)val_peek(0).obj).getLexema());
 									   	this.lista.agregarTerceto(ter); 
 									   	
 									   	this.lista.mostrarTercetos();
 									   	
 									   	this.nro_terceto++;
								  		
								  	} else {
								  	
								  		System.out.println("\n EL ID  "+id.getLexema()+" no esta correctamente definido. cancelo la asignacion  \n");
								  		System.out.println("\n a  "+id.getLexema()+"  no le agrego ambito -> no es valido \n");
								  		/*luego eliminar las entradas ID sin ambito*/
								  	}
								  	
								  } else {
								  
							      	/*System.out.println("\n ASIGNACION de EXPRESION $$ TERCETO!!  -> "+ ((Terceto)$$.obj).getOperando1() + " , " +((Terceto)$$.obj).getOperando2() +"\n");*/
								  	
								  	String pos_str = "["+pos_ultimo_terceto+"]";
								  	
								  	System.out.println("\n CREO TERCETO ASIGNACION! con terceto ->  ( "+op.getLexema()+" , "+ id.getLexema()+" , "+pos_str+" ) \n");
								  	
								  	Terceto ter = new Terceto(this.nro_terceto, "=", id.getLexema(), pos_str);
 									this.lista.agregarTerceto(ter); 
 								  	
 								  	this.lista.mostrarTercetos();
 								   	
 								   	this.nro_terceto++;
								  	
								  }
								  
								  isToken=true;
						
							   }
break;
case 30:
//#line 291 "gramaticaIncremental.y"
{
		   								/*conversion explicita!*/
		   								/*si no reconoce el tipo -> error de compatibilidad */
		   								}
break;
case 31:
//#line 301 "gramaticaIncremental.y"
{System.out.println("SENTENCIA DE CONTROL! aca completo tercetos BI");					 
														 
														 this.lista.completarTerceto(pos_BI, this.nro_terceto);
														 }
break;
case 32:
//#line 308 "gramaticaIncremental.y"
{
								  String pos_str = "["+pos_ultimo_terceto+"]";
 								  
 								  System.out.println("\n CREO EL TERCETO SALTO por FALSO ->  ( BF , "+pos_str +" ,   )  \n");				
 								  	  	 
 								  	    
 	 							  Terceto ter = new Terceto(this.nro_terceto, "BF", pos_str, " " );
 								  this.lista.agregarTerceto(ter);
 								   
 								  this.pos_BF = this.nro_terceto; /*marco posicion del terceto BF !*/
 								  
 								  
 								  pos_ultimo_terceto = this.nro_terceto;
 					
 								  this.lista.mostrarTercetos();
 					     		  
 					     		  this.nro_terceto++;
 									   	
								  }
break;
case 33:
//#line 330 "gramaticaIncremental.y"
{/*System.out.println("\n 2. THEN -> CUERPO DEL IF! \n");*/
								  System.out.println("\n COMPLETAR TERCETO INCOMPLETO  \n");
								  System.out.println("\n CREAR TERCETO BI (salto incondicional) \n");
								  }
break;
case 34:
//#line 335 "gramaticaIncremental.y"
{/*System.out.println("...ELSE ");*/
		  														  /*System.out.println("\n 3. COMPLETAR TERCETOS BF y BI  \n ");*/
		  														  }
break;
case 35:
//#line 341 "gramaticaIncremental.y"
{/*System.out.println("\n ENTRA AL ELSE \n");*/
				   
				   String pos_str = "["+pos_ultimo_terceto+"]";
 					
 					
 					System.out.println("\n CREA TERCETO SALTO INCONDICIONAL  ->  (  BI  ,   ,  -- ) ");
 	 								    
 	 				Terceto ter = new Terceto(this.nro_terceto, "BI", " " , "--");
 					this.lista.agregarTerceto(ter); 
 					
 					this.pos_BI = this.nro_terceto;
 					
 					pos_ultimo_terceto = this.nro_terceto;
 					
 					
 					this.lista.mostrarTercetos();
 									   	
 									   
 					/*aca completo BF! ya tengo la posicion, que es pos_BF*/
 					
 					this.lista.completarTerceto(pos_BF, this.nro_terceto+1);
				   
				   	this.nro_terceto++; /*sgte al terceto BI*/
 					
				   }
break;
case 36:
//#line 373 "gramaticaIncremental.y"
{/*System.out.println("\n SENTENCIA DE CONTROL DETECTADA \n");*/
																				System.out.println("\n TERMINA CORRECTAMENTE REGLA DE ITERACION... \n");
					  															}
break;
case 37:
//#line 379 "gramaticaIncremental.y"
{
					  System.out.println("\n aca marco el comienzo de la sentencia loop! \n");
					  this.pos_comienzo_loop=this.nro_terceto;
					 }
break;
case 38:
//#line 386 "gramaticaIncremental.y"
{									
									System.out.println("3. CUERPO/BLOQUE DEL WHILE ");
								    System.out.println("\n COMPLETAR TERCETO INCOMPLETO  \n");
								    System.out.println("\n CREAR TERCETO BI (salto incondicional) \n");
								    
								    /*aca ya termine el cuerpo...*/
								    
								    /*ACA creo el BI incompleto ?*/

								    String pos_str = "["+pos_ultimo_terceto+"]";
 					
 					
				 					/*System.out.println("\n CREA TERCETO SALTO INCONDICIONAL  ->  (  BI  ,   ,  -- ) ");*/
				 	 								    
				 	 				/*Terceto ter = new Terceto(this.nro_terceto, "BI", " " , "--");*/
				 					/*this.lista.agregarTerceto(ter); */
				 					
				 					/*this.pos_BI = this.nro_terceto;*/
				 					
				 					/*pos_ultimo_terceto = this.nro_terceto;*/
				 					
				 					
				 					/*this.lista.mostrarTercetos();*/
				 									   	
				 									   
				 					/*aca completo BF! ya tengo la posicion, que es pos_BF*/
				 					
				 					/*this.lista.completarTerceto(pos_BF, this.nro_terceto+1);*/
								   
								   	/*this.nro_terceto++; //sgte al terceto BI*/
 					
								    /*avanzo el terceto...*/
								    }
break;
case 39:
//#line 423 "gramaticaIncremental.y"
{ /*aca entra en condicion!!*/
						/*aca seria el comienzo de la condicion!*/
					System.out.println("\n QUE ONDA?? el UNTIL... \n");
					this.pos_comienzo_condicion_loop = this.pos_ultimo_terceto+1;
					System.out.println("\n COMIENZO DE CONDICION... "+this.pos_comienzo_condicion_loop +"\n");						 
					}
break;
case 40:
//#line 432 "gramaticaIncremental.y"
{
									/*ejecuto la condicion, igual que condicionIF...*/
									/*aca comienza la condicion?? apilo el nro de terceto*/
									/*o termina la condicion???*/
									/*aca creo terceto incompleto para BF??*/
									
									/*ACAA creo el BF , en blanco?*/
									
									String pos_str = "["+this.pos_comienzo_loop+"]";
 								    String pos_sgte = "["+(pos_ultimo_terceto+2)+"]";
 								    
 								    System.out.println("\n CREO EL TERCETO SALTO por FALSO ->  ( BF , "+pos_str +" , "+ pos_sgte +" )  \n");				
 								  	  	 
 								  	    
 	 							    Terceto ter = new Terceto(this.nro_terceto, "BF", pos_str, pos_sgte );
 								    this.lista.agregarTerceto(ter);
 								   
 								    this.pos_BF = this.nro_terceto; /*marco posicion del terceto BF !*/
 								  
 								  
 								    pos_ultimo_terceto = this.nro_terceto;
 					
 								    this.lista.mostrarTercetos();
 					     		    
 					     		    this.nro_terceto++;
 					     		    
 					    			}
break;
case 41:
//#line 465 "gramaticaIncremental.y"
{if (isToken) { 
 									  	 Token op1 = (Token)val_peek(2).obj;
 										 Token op2 = (Token)val_peek(1).obj;
 	 								     Token op3 = (Token)val_peek(0).obj;
 									  	 
 									  	 System.out.println("CREA TERCETO COMPARACION con TOKEN  ->  (  >  , " +((Token)yyval.obj).getLexema() + " , " + op3.getLexema()+" )  \n");
 	 								     
 	 								     Terceto ter = new Terceto(this.nro_terceto, ">", op1.getLexema(), op3.getLexema());
 									   	 this.lista.agregarTerceto(ter); 
 									   	 pos_ultimo_terceto = this.nro_terceto;
 									   	 
 									   	 this.lista.mostrarTercetos();
 									   	 
 									   	 this.nro_terceto++;
 									   	 
 									  } else {
 									  
 								      	System.out.println("\n COMPARACION de EXPRESION $$ con TERCETO!!  -> "+ ((Terceto)yyval.obj).getOperando1() +" , "+((Terceto)yyval.obj).getOperando2()+"\n");
 									  	
 									  	/*caso en que $3 es un terceto*/
 									  	String tipo_obj = val_peek(0).obj.toString();
 								      	
 								      	if (tipo_obj.substring(18,23).equals("Token")) { /*si el tipo del $3 objeto es Token*/
 								      		Token op3 = (Token)val_peek(0).obj;
 								      		String pos_str = "["+pos_ultimo_terceto+"]";
 	 									  	 
 	 								      	System.out.println("\n CREA TERCETO COMPARACION con TERCETO y Token ->  (  >  ,  "+pos_str + "  ,  " + op3.getLexema());/*op3.getOperando2());*/
 	 	 								    
 	 	 								    Terceto ter = new Terceto(this.nro_terceto, ">", pos_str, op3.getLexema());/*op3.getOperando2());*/
 	 									   	this.lista.agregarTerceto(ter); 
 	 									   
 	 									   	this.lista.mostrarTercetos();
 	 									   	
 	 									   	pos_ultimo_terceto = this.nro_terceto;
 	 									   	
 	 									   	this.nro_terceto++;
 	 									   	
 								      	} else { /*si el argumento $3 es un terceto*/
 								      		
 								      		Terceto op3 = (Terceto)val_peek(0).obj;

	 								      
	 								      	String pos_terc1 = "["+(pos_ultimo_terceto-1)+"]";
	 									   	
	 								      	String pos_terc2 = "["+pos_ultimo_terceto+"]";
	 									  	
	 									  	 
	 								      	System.out.println("\n CREA TERCETOS COMPARACION con TERCETO y TERCETO ->  (  >  ,  "+pos_terc1 + "  ,  " + pos_terc2+" ) \n");/*op3.getOperando2());*/
	 	 								    
	 	 								    
	 	 								    Terceto ter = new Terceto(this.nro_terceto, ">", pos_terc1, pos_terc2);/*op3.getOperando2());*/
	 									   	this.lista.agregarTerceto(ter); 
	 									   	
	 									   	this.lista.mostrarTercetos();
	 									   	
	 									   	pos_ultimo_terceto = this.nro_terceto;
	 									   	
	 									   	this.nro_terceto++;
 								      	}  	 
 									  }
 									  
 									  isToken=true;
									}
break;
case 42:
//#line 530 "gramaticaIncremental.y"
{
	  	  							if (isToken) { 
 									  	System.out.println("\n COMPARACION de EXPRESION $$ TOKEN!!  -> "+ ((Token)yyval.obj).getLexema() +"\n");
 									  	Token op1 = (Token)val_peek(2).obj;
 										Token op2 = (Token)val_peek(1).obj;
 	 								    Token op3 = (Token)val_peek(0).obj;
 									  	
 									  	System.out.println("CREA TERCETO COMPARACION con TOKENS  ->  (  <  , "+yyval.obj + " , " + op3.getLexema());
 	 								     
 	 								    Terceto ter = new Terceto(this.nro_terceto, "<", op1.getLexema(), op3.getLexema());
 									   	this.lista.agregarTerceto(ter); 
 									   	pos_ultimo_terceto = this.nro_terceto;
 									   	 
 									   	this.lista.mostrarTercetos();
 									   	 
 									   	this.nro_terceto++;
 									   	 
 									  } else {
 									  
 								      	System.out.println("\n COMPARACION de EXPRESION $$ con TERCETO!!  -> "+ ((Terceto)yyval.obj).getOperando1() +" , "+((Terceto)yyval.obj).getOperando2()+"\n");
 									  	
 									  	/*caso en que $3 es un terceto*/
 									  	String tipo_obj = val_peek(0).obj.toString();
 								      	
 								      	if (tipo_obj.substring(18,23).equals("Token")) { /*si el tipo del $3 objeto es Token*/
 								      		Token op3 = (Token)val_peek(0).obj;
 								      		String pos_str = "["+pos_ultimo_terceto+"]";
 	 									  	 
 	 								      	System.out.println("\n CREA TERCETO COMPARACION con TERCETO y Token ->  (  <  ,  "+pos_str + "  ,  " + op3.getLexema());/*op3.getOperando2());*/
 	 	 								    
 	 	 								    Terceto ter = new Terceto(this.nro_terceto, "<", pos_str, op3.getLexema());/*op3.getOperando2());*/
 	 									   	this.lista.agregarTerceto(ter); 
 	 									   	
 	 									   	this.lista.mostrarTercetos();
 	 									   	
 	 									   	pos_ultimo_terceto = this.nro_terceto;
 	 									   	
 	 									   	this.nro_terceto++;
 	 									   	
 								      	} else { /*si el argumento $3 es un terceto*/
 								      		
 								      		Terceto op3 = (Terceto)val_peek(0).obj;

	 								      	
	 								      	String pos_terc1 = "["+(pos_ultimo_terceto-1)+"]";
	 									   	
	 								      	String pos_terc2 = "["+pos_ultimo_terceto+"]";
	 									  	 
	 									  	 
	 								      	System.out.println("\n CREA TERCETOS COMPARACION con TERCETO y TERCETO ->  (  <  ,  "+pos_terc1 + "  ,  " + pos_terc2);/*op3.getOperando2());*/
	 	 								    
	 	 								    Terceto ter = new Terceto(this.nro_terceto, "<", pos_terc1, pos_terc2);/*op3.getOperando2());*/
	 									   	this.lista.agregarTerceto(ter); 
	 									   	
	 									   	this.lista.mostrarTercetos();
	 									   	
	 									   	pos_ultimo_terceto = this.nro_terceto;
	 									   	
	 									   	this.nro_terceto++;
 								      	}  	 
 									  }
 									  
 									  isToken=true;
									}
break;
case 47:
//#line 604 "gramaticaIncremental.y"
{
												    }
break;
case 52:
//#line 627 "gramaticaIncremental.y"
{Token op1 = (Token)val_peek(2).obj;
								   int linea = op1.getNroLinea();
								   Token op2 = (Token)val_peek(1).obj;
								   Token op3 = (Token)val_peek(0).obj;
								   
								   
								   /*ACA HACER CHECKEO DE TIPOS! CONVERSION EXPLICITA*/
								   /*PRIMERO GENERO TERCETO QUE CONVIERTE EL TIPO DE Y -> TERCETO CONVERSION*/
								   /*GUARDO EL TERCETO EN VAR AUX*/
								   
								   /*C/CONVERSION EXPLICITA GENERA TERCETO*/
								   
								   
								   /*$$=$1;*/
	   		  					   
	   		  					   System.out.println("\n CREA TERCETO SUMA  ->  ("+op2.getLexema()+" , "+op1.getLexema()+" , "+op3.getLexema()+") \n\n");
								   
								   Terceto ter = new Terceto(this.nro_terceto, op2.getLexema(), op1.getLexema(), op3.getLexema());
								   this.lista.agregarTerceto(ter); 
								   pos_ultimo_terceto = this.nro_terceto;
								   
								   this.lista.mostrarTercetos();
								   
								   this.nro_terceto++;
								   /*apunto a terceto!?*/
								   yyval.obj = ter ;
								   this.isToken=false;
								   }
break;
case 53:
//#line 657 "gramaticaIncremental.y"
{Token op1 = (Token)val_peek(2).obj;
								   int linea = op1.getNroLinea();
								   Token op2 = (Token)val_peek(1).obj;
								   Token op3 = (Token)val_peek(0).obj;
								   
								   
								   /*$$=$1;*/
	   		  					   
	   		  					   System.out.println("\n CREA TERCETO RESTA  ->  ("+op2.getLexema()+" , "+op1.getLexema()+" , "+op3.getLexema()+") \n\n");
								   
								   Terceto ter = new Terceto(this.nro_terceto, op2.getLexema(), op1.getLexema(), op3.getLexema());
								   this.lista.agregarTerceto(ter); 
								   pos_ultimo_terceto = this.nro_terceto;
								   
								   this.lista.mostrarTercetos();
								   
								   this.nro_terceto++;
								   /*apunto a terceto!?*/
								   yyval.obj = ter ;
								   this.isToken=false; /*no apunto al token, apunto a terceto*/
								   }
break;
case 54:
//#line 680 "gramaticaIncremental.y"
{/*System.out.println("\n\n soy terrible TERMINO \n\n"); }*/
	  	  			 /*Token expr = (Token)$$.obj;*/
	  	  			 yyval=val_peek(0);
	  	  			 
	  	  			 /*System.out.println("\n\n EXPRESION SIMPLE -> TERMINO -> "+ ((Token)$$.obj).getLexema() +" \n\n");*/
	  	  			 }
break;
case 56:
//#line 692 "gramaticaIncremental.y"
{
							  Token op1 = (Token)val_peek(2).obj;
							  int linea = op1.getNroLinea();
							  Token op2 = (Token)val_peek(1).obj;
							  Token op3 = (Token)val_peek(0).obj;
							  
							  System.out.println("\n TERCETO MULTIPLICACION  ->  ( "+op2.getLexema()+" , "+op1.getLexema()+" , "+op3.getLexema()+" ) \n");
							  
							  
							  Terceto ter = new Terceto(this.nro_terceto, "*" , op1.getLexema(), op3.getLexema());
							  this.lista.agregarTerceto(ter); 
							  pos_ultimo_terceto = this.nro_terceto;
							  
							  this.lista.mostrarTercetos();
							  
							  this.nro_terceto++;
							  /*apunto a terceto!?*/
							  yyval.obj = ter ;
							  this.isToken=false; 
							  }
break;
case 57:
//#line 713 "gramaticaIncremental.y"
{System.out.println("2. agregar operacion que se realizo -> /");}
break;
case 58:
//#line 715 "gramaticaIncremental.y"
{
				  yyval=val_peek(0); 	/* termino.ptr = factor.ptr*/
				  
	   		  	  /*System.out.println("\n REGLA TERMINO -> Llega FACTOR!  "+ ((Token)$$.obj).getLexema() +"  la apunto con $$?? \n");*/
				 }
break;
case 59:
//#line 726 "gramaticaIncremental.y"
{Token factor = (Token)yyval.obj; 
	   		  /*System.out.println("\n Llega CTE  "+ factor.getLexema() +"  la apunto con $$?? \n");*/
	   		  yyval=val_peek(0);
	   		  /*System.out.println("\n REGLA FACTOR -> Llega CTE!  "+ ((Token)$$.obj).getLexema() +"  la apunto con $$?? \n");*/
	   		  }
break;
case 60:
//#line 732 "gramaticaIncremental.y"
{/*System.out.println("CTE negativa! \n"); */
       			 Token op1 = (Token)val_peek(1).obj;
				 int linea = op1.getNroLinea();
				 Token op2 = (Token)val_peek(0).obj;
				 /*System.out.println("\n Sintactico  ->  Linea: "+linea+"  ,  CTE NEGATIVA!  ->  "+op1.getLexema()+" "+op2.getLexema()+"\n");*/
				 }
break;
case 61:
//#line 741 "gramaticaIncremental.y"
{/*Token factor = (Token)$$.obj; */
	   		  /*System.out.println("llega FACTOR ID!??!  creo puntero -> "+ factor.getLexema() +"\n");*/
       		  }
break;
//#line 1199 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe, AnalizadorLexico al, TablaDeSimbolos tds, ListaTercetos l, String a)
{
  yydebug=debugMe;
  lexico=al;
  tabla=tds;
  lista=l;
  ambito=a;
}
//###############################################################



}
//################### END OF CLASS ##############################
