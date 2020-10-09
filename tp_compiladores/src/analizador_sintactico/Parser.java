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






//#line 1 "gramatica.y"

import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;  /*????*/

import analizador_lexico.*;
//#line 24 "Parser.java"




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
public final static short ELSE=261;
public final static short END_IF=262;
public final static short PROC=263;
public final static short NI=264;
public final static short REF=265;
public final static short OUT=266;
public final static short MAYORIGUAL=267;
public final static short MENORIGUAL=268;
public final static short IGUAL=269;
public final static short DISTINTO=270;
public final static short EOF=271;
public final static short INTEGER=272;
public final static short FLOAT=273;
public final static short LOOP=274;
public final static short UNTIL=275;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    3,    3,    5,    6,    6,
   10,   10,    9,    9,    9,   11,   11,    8,    8,    7,
    7,    4,    4,    4,    4,    4,   13,   13,   17,   17,
   17,   17,   17,   17,   18,   18,   15,   16,   20,   20,
   14,   12,   19,   19,   19,   21,   21,   21,   22,   22,
   22,
};
final static short yylen[] = {                            2,
    1,    1,    2,    1,    1,    2,    2,    2,   11,   10,
    1,    2,    5,    3,    1,    2,    3,    3,    1,    1,
    1,    2,    2,    2,    2,    2,    8,    6,    3,    3,
    3,    3,    3,    3,    1,    3,    3,    4,    1,    3,
    6,    3,    3,    3,    1,    3,    3,    1,    1,    2,
    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   20,   21,    0,    0,    0,    2,
    4,    5,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   35,    0,    3,    6,
    7,   19,    0,   22,   23,   24,   25,   26,   39,    0,
   51,   49,    0,    0,    0,   48,    0,    0,    0,   37,
    0,    0,    0,   38,    0,   50,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   36,    0,   18,   40,    0,    0,   46,   47,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   16,
    0,    0,    0,    0,   28,   17,    0,    0,    0,   41,
    0,    0,    0,    0,   27,    0,    0,   13,    0,    0,
    0,    0,   10,    0,    9,
};
final static short yydgoto[] = {                          8,
  109,   10,   11,   12,   13,   14,   15,   33,   71,  110,
   72,   16,   17,   18,   19,   20,   47,   28,   48,   40,
   45,   46,
};
final static short yysindex[] = {                      -198,
   -6,    1, -212,   14,    0,    0, -117,    0, -198,    0,
    0,    0,    7,   22, -199,   36,   38,   39,   40,   41,
 -197,  -43,  -43,   46, -172, -198,    0, -187,    0,    0,
    0,    0,   45,    0,    0,    0,    0,    0,    0,  -12,
    0,    0,  -43,   -1,   -7,    0,   49,  -24,  -41,    0,
 -112,   51, -165,    0, -164,    0,  -43,  -43,  -43,  -43,
 -117,  -43,  -43,  -43,  -43,  -43,  -43, -203, -170, -162,
   55,   53,    0,  -43,    0,    0,   -7,   -7,    0,    0,
 -190,   -1,   -1,   -1,   -1,   -1,   -1, -159,   42,    0,
 -163, -209,   58, -117,    0,    0, -158,   43,   61,    0,
 -160,  -17, -151, -209,    0, -198,  -15,    0, -198,  -16,
 -198,   22,    0,  -11,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  111,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   54,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   68,  -40,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   74,    0,    0,    0,    0,  -34,  -29,    0,    0,
    0,   75,   76,   77,   78,   79,   80,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   81,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   -2,    0,
    0,    2,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   17,   16,    0,    0,    0,   15,  -31,    0,    0,   18,
  -65,    0,    0,    0,    0,    0,   52,  -37,  -14,    0,
   21,  -13,
};
final static int YYTABLESIZE=246;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         69,
   45,   43,   45,   45,   45,   26,   43,   44,   43,   43,
   43,   44,   73,   44,   44,   44,    9,   70,   57,   45,
   58,   45,   27,   81,   29,   43,   99,   43,   54,   56,
   44,   55,   44,   21,   59,   67,   88,   66,  108,   60,
   23,   57,   51,   58,   24,   79,   80,   82,   83,   84,
   85,   86,   87,   25,   22,   68,  101,   32,    1,   39,
   70,    2,    5,    6,    3,   30,   29,    4,    5,    6,
   94,   95,   70,    5,    6,    7,   27,   77,   78,   34,
   31,   35,   36,   37,   38,   49,   50,   52,   53,   61,
   74,   75,   76,   89,   90,   91,   92,   96,  100,  102,
   98,  105,   97,  103,  104,  106,  107,  111,  113,   27,
    1,   42,    8,  115,   15,   31,   32,   33,   34,   29,
   30,   14,   11,  112,   29,   93,   12,    0,  114,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,
    0,    0,    2,    0,    1,    3,    0,    2,    4,    0,
    3,    0,    0,    4,    5,    6,    7,    0,    0,    5,
    6,    7,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   41,   42,    0,    0,    0,    0,    0,
    0,    0,    0,   68,    0,    0,   45,   45,   45,   45,
    5,    6,   43,   43,   43,   43,    0,   44,   44,   44,
   44,    0,   62,   63,   64,   65,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   41,   45,   43,   44,   45,  123,   41,   22,   43,   44,
   45,   41,  125,   43,   44,   45,    0,   49,   43,   60,
   45,   62,    7,   61,    9,   60,   92,   62,   41,   43,
   60,   44,   62,   40,   42,   60,   68,   62,  104,   47,
   40,   43,   26,   45,  257,   59,   60,   62,   63,   64,
   65,   66,   67,   40,   61,  265,   94,  257,  257,  257,
   92,  260,  272,  273,  263,   59,   51,  266,  272,  273,
  261,  262,  104,  272,  273,  274,   61,   57,   58,   44,
   59,   44,   44,   44,   44,   40,  259,  275,   44,   41,
   40,  257,  257,  264,  257,   41,   44,  257,   41,  258,
  264,  262,   61,   61,   44,  123,  258,  123,  125,   94,
    0,   44,   59,  125,   41,   41,   41,   41,   41,   41,
   41,   41,  125,  109,  109,   74,  125,   -1,  111,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,
   -1,   -1,  260,   -1,  257,  263,   -1,  260,  266,   -1,
  263,   -1,   -1,  266,  272,  273,  274,   -1,   -1,  272,
  273,  274,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  257,  258,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  265,   -1,   -1,  267,  268,  269,  270,
  272,  273,  267,  268,  269,  270,   -1,  267,  268,  269,
  270,   -1,  267,  268,  269,  270,
};
}
final static short YYFINAL=8;
final static short YYMAXTOKEN=275;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
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
null,null,null,null,null,null,null,"ID","CTE","CADENA","IF","ELSE","END_IF",
"PROC","NI","REF","OUT","MAYORIGUAL","MENORIGUAL","IGUAL","DISTINTO","EOF",
"INTEGER","FLOAT","LOOP","UNTIL",
};
final static String yyrule[] = {
"$accept : programa",
"programa : lista_de_sentencias",
"lista_de_sentencias : sentencia",
"lista_de_sentencias : lista_de_sentencias sentencia",
"sentencia : sentencia_declarativa",
"sentencia : sentencia_ejecutable",
"sentencia_declarativa : declaracion_de_variable ';'",
"sentencia_declarativa : declaracion_de_procedimiento ';'",
"declaracion_de_variable : tipo lista_de_variables",
"declaracion_de_procedimiento : PROC ID '(' lista_de_parametros ')' NI '=' CTE '{' cuerpo_del_procedimiento '}'",
"declaracion_de_procedimiento : PROC ID '(' ')' NI '=' CTE '{' cuerpo_del_procedimiento '}'",
"cuerpo_del_procedimiento : lista_de_sentencias",
"cuerpo_del_procedimiento : lista_de_sentencias declaracion_de_procedimiento",
"lista_de_parametros : parametro_declarado ',' parametro_declarado ',' parametro_declarado",
"lista_de_parametros : parametro_declarado ',' parametro_declarado",
"lista_de_parametros : parametro_declarado",
"parametro_declarado : tipo ID",
"parametro_declarado : REF tipo ID",
"lista_de_variables : lista_de_variables ',' ID",
"lista_de_variables : ID",
"tipo : INTEGER",
"tipo : FLOAT",
"sentencia_ejecutable : asignacion ','",
"sentencia_ejecutable : clausula_de_seleccion ','",
"sentencia_ejecutable : sentencia_de_control ','",
"sentencia_ejecutable : sentencia_de_salida ','",
"sentencia_ejecutable : invocacion ','",
"clausula_de_seleccion : IF '(' condicion ')' bloque_de_sentencias ELSE bloque_de_sentencias END_IF",
"clausula_de_seleccion : IF '(' condicion ')' bloque_de_sentencias END_IF",
"condicion : expresion '>' expresion",
"condicion : expresion '<' expresion",
"condicion : expresion MAYORIGUAL expresion",
"condicion : expresion MENORIGUAL expresion",
"condicion : expresion IGUAL expresion",
"condicion : expresion DISTINTO expresion",
"bloque_de_sentencias : sentencia",
"bloque_de_sentencias : '{' lista_de_sentencias '}'",
"sentencia_de_salida : OUT '(' CADENA",
"invocacion : ID '(' parametro_ejecutable ')'",
"parametro_ejecutable : ID",
"parametro_ejecutable : parametro_ejecutable ',' ID",
"sentencia_de_control : LOOP bloque_de_sentencias UNTIL '(' condicion ')'",
"asignacion : ID '=' expresion",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : CTE",
"factor : '-' factor",
"factor : ID",
};

//#line 154 "gramatica.y"

	   	


//CODE



AnalizadorLexico lexico;
TablaTokens tt;
TablaSimbolos ts;
String prog;

String ins;
StringTokenizer st;
boolean newline;



public Parser(AnalizadorLexico al, TablaTokens tt, TablaSimbolos ts) {
	//this.tt = new TablaTokens();
	//this.ts = new TablaSimbolos();
	//this.lexico = new AnalizadorLexico(prog, tt, ts);
	//this.prog = prog;
	this.lexico = al;
	this.tt = tt;
	this.ts = ts;
}


public int Parsear() {
	return yyparse();
}


void yyerror(String s)
{
 System.out.println("par:"+s);
}



private int yylex() {
	Token token=lexico.getToken();
	System.out.println("\n yylex -> GET TOKEN -> "+token.getLexema()+" , tipo -> "+token.getTipo()+" , id_tipo -> "+token.getIdTipo()+"\n");
	if (token!=null){
	    yylval = new ParserVal(token.getIdTipo()); //var para obtener el token de la tabla
	    
	    return token.getIdTipo(); //acceso a la entrada que devolvumos
	}
	//lexico devuelve i de token! y lexico en yylval lo asocie con la tabla de simbolos
	return 0;
}





public static void main(String args[]) {
	//String path_archivo = args[0];
    //asi ejecuto pasando archivo como parametro
    
	
 	//TablaTokens tt = new TablaTokens();
	//TablaSimbolos ts = new TablaSimbolos();
	String direccion_codigo = "casos_prueba_id_cte.txt";
	
	AnalizadorLexico al = new AnalizadorLexico(direccion_codigo);//, tt, ts);
	
	al.abrirCargarArchivo();
	
	
	System.out.println("--------------------------------------- \n ");
	System.out.println("\n ARRANCA EL SINTACTICO \n");
	al.mostrarTablaTokens();
	al.mostrarTablaSimbolos();

 	//AnalizadorLexico lexico = new AnalizadorLexico(direccion_codigo, tt, ts);
	//AnalizadorLexico lexico = new AnalizadorLexico(programa, tt, ts);
	//lexico.abrirCargarArchivo();
	//lexico.mostrarTablaSimbolos(); 
	//lexico.getToken();
	
	Parser par = new Parser(false, al);
	//Parser par = new Parser(al);//, tt, ts);
 	//par.dotest();
	par.Parsear();
	//par.Parsear();
	par.Parsear();
	par.Parsear();
	par.Parsear();
	//par.yyparse()
}

//#line 406 "Parser.java"
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
        yychar = yylex();  //get next token!!! ACA! 
        System.out.println("yychar = identificador de tipode token en tabla -> "+yychar);
        //int id_token = 
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
        System.out.println("LLEGO A NEXT STATEE???????? -> SAPEEE");
        //hasta aca llego con el token
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }
    System.out.println("Y ACA?? -> YEAH, NIGGA");  //hasta aca llego con el =
    //salto NEXT STATE!
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
public Parser(boolean debugMe, AnalizadorLexico al)
{
  yydebug=debugMe;
  this.lexico = al;
}
//###############################################################



}
//################### END OF CLASS ##############################
