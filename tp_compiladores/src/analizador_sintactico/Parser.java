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
    0,    1,    1,    2,    2,    3,    3,    5,    6,    6,
   10,    9,    9,    9,   11,   11,    8,    8,    7,    7,
    4,    4,    4,    4,    4,   12,   13,   13,   18,   18,
   18,   18,   18,   18,   19,   14,   15,   16,   20,   20,
   17,   17,   17,   17,   21,   21,   21,   22,   22,   22,
};
final static short yylen[] = {                            2,
    1,    1,    2,    2,    2,    1,    1,    2,   11,   10,
    1,    5,    3,    1,    2,    3,    3,    1,    1,    1,
    1,    1,    1,    1,    1,    3,    8,    6,    3,    3,
    3,    3,    3,    3,    3,    6,    4,    4,    3,    5,
    3,    3,    1,    1,    3,    3,    1,    1,    2,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,   19,   20,    0,    0,    2,
    0,    0,    6,    7,    0,   21,   22,   23,   24,   25,
    0,    0,    0,    0,    0,    0,    0,    3,    4,    5,
   18,    0,    0,    0,   50,   48,   44,    0,    0,    0,
   47,    0,    0,    0,    0,    0,    0,    0,    0,   38,
    0,   49,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   37,   35,
    0,   17,   39,    0,    0,    0,   45,   46,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   15,    0,    0,
    0,    0,    0,   28,   16,    0,    0,    0,   36,   40,
    0,    0,    0,    0,   27,    0,    0,   12,    0,    0,
    0,   10,    0,    9,
};
final static short yydgoto[] = {                          8,
  109,   10,   11,   12,   13,   14,   15,   32,   67,  110,
   68,   16,   17,   18,   19,   20,   42,   43,   27,   34,
   40,   41,
};
final static short yysindex[] = {                      -200,
  -15,  -10, -240,   -4,  -89,    0,    0,    0, -200,    0,
  -17,    3,    0,    0, -208,    0,    0,    0,    0,    0,
 -198,  -42,  -42,   30, -181, -200, -188,    0,    0,    0,
    0,   37,   25,    6,    0,    0,    0, -174,   20,   -9,
    0,  -23,   44,  -40,   45, -117,   47, -169, -168,    0,
 -167,    0,  -39,  -39,  -39,  -39,  -42,  -42,  -42,  -42,
  -42,  -42,  -89, -205, -175, -165,   52,   50,    0,    0,
  -42,    0,    0,   38,   -9,   -9,    0,    0,   20,   20,
   20,   20,   20,   20, -189, -162,   36,    0, -166, -227,
   57, -158,  -89,    0,    0, -157,   41,   59,    0,    0,
 -159,  -18, -152, -227,    0, -200,  -16,    0, -200,  -13,
 -200,    0,  -12,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  108,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   51,    0,    0,    0,    0,    0,    0,   55,  -41,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   68,    0,    0,
    0,    0,    0,    0,  -36,  -31,    0,    0,   74,   75,
   76,   77,   78,   79,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   80,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   -3,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   32,    2,    0,    0,    0,    0,  -29,    0,    0,   12,
  -63,    0,    0,    0,    0,    0,   -6,   53,  -50,    0,
   23,   24,
};
final static int YYTABLESIZE=255;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         43,
   65,   43,   38,   43,   41,   38,   41,   70,   41,   42,
   28,   42,   85,   42,   66,   39,   24,   43,   43,   53,
   43,   54,   41,   41,   21,   41,   98,   42,   42,   23,
   42,    9,   55,   26,   86,   25,   62,   56,   61,   64,
  108,   29,  101,    6,    7,   22,   50,   28,   31,   51,
   79,   80,   81,   82,   83,   84,    1,   46,   33,    2,
   66,   30,   53,    3,   54,    6,    7,    4,    5,   44,
    6,    7,   93,   94,   66,   75,   76,   45,   77,   78,
   48,   47,   49,   52,   63,   69,   71,   72,   73,   74,
   87,   88,   89,   90,   95,   92,   96,   99,  100,   97,
  102,  103,  104,  105,  106,  107,  111,    1,   14,    8,
   28,  112,  114,   26,   32,   31,   33,   34,   29,   30,
   13,   11,  113,   91,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,
    0,    0,    2,    0,    0,    0,    3,    0,    0,    0,
    4,    5,    0,    6,    7,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   35,   36,   37,   35,   36,    0,
    0,    0,    0,    0,    0,    0,   64,    0,    0,    0,
    6,    7,    0,   43,   43,   43,   43,    0,   41,   41,
   41,   41,    0,   42,   42,   42,   42,    0,    0,    0,
    0,   57,   58,   59,   60,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   41,   43,   45,   45,   41,   45,   43,  125,   45,   41,
    9,   43,   63,   45,   44,   22,  257,   59,   60,   43,
   62,   45,   59,   60,   40,   62,   90,   59,   60,   40,
   62,    0,   42,  123,   64,   40,   60,   47,   62,  267,
  104,   59,   93,  271,  272,   61,   41,   46,  257,   44,
   57,   58,   59,   60,   61,   62,  257,   26,  257,  260,
   90,   59,   43,  264,   45,  271,  272,  268,  269,   40,
  271,  272,  262,  263,  104,   53,   54,  259,   55,   56,
   44,  270,   58,  258,   41,   41,   40,  257,  257,  257,
  266,  257,   41,   44,  257,   58,   61,   41,  257,  266,
  258,   61,   44,  263,  123,  258,  123,    0,   41,   59,
  109,  125,  125,   59,   41,   41,   41,   41,   41,   41,
   41,  125,  111,   71,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,
   -1,   -1,  260,   -1,   -1,   -1,  264,   -1,   -1,   -1,
  268,  269,   -1,  271,  272,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  257,  258,  259,  257,  258,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  267,   -1,   -1,   -1,
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
"declaracion_de_procedimiento : PROC ID '(' lista_de_parametros ')' NI '=' CTE '{' cuerpo_del_procedimiento '}'",
"declaracion_de_procedimiento : PROC ID '(' ')' NI '=' CTE '{' cuerpo_del_procedimiento '}'",
"cuerpo_del_procedimiento : lista_de_sentencias",
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
"sentencia_ejecutable : clausula_de_seleccion",
"sentencia_ejecutable : sentencia_de_control",
"sentencia_ejecutable : sentencia_de_salida",
"sentencia_ejecutable : invocacion",
"asignacion : ID '=' expresion",
"clausula_de_seleccion : IF '(' condicion ')' bloque_de_sentencias ELSE bloque_de_sentencias END_IF",
"clausula_de_seleccion : IF '(' condicion ')' bloque_de_sentencias END_IF",
"condicion : expresion '>' expresion",
"condicion : expresion '<' expresion",
"condicion : expresion MAYORIGUAL expresion",
"condicion : expresion MENORIGUAL expresion",
"condicion : expresion IGUAL expresion",
"condicion : expresion DISTINTO expresion",
"bloque_de_sentencias : '{' lista_de_sentencias '}'",
"sentencia_de_control : LOOP bloque_de_sentencias UNTIL '(' condicion ')'",
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

//#line 297 "gramaticaIncremental.y"
	   	


//CODE



AnalizadorLexico lexico;
TablaDeSimbolos tabla;




void yyerror(String s)
{
 System.out.println("par:"+s);
}



private int yylex() {
	Token token=lexico.getToken();
	//System.out.println("\n Dentro del Sintactico...\n");

	if (token!=null){
		tabla.addToken(token);
		
	    yylval = new ParserVal(token); //var para obtener el token de la tabla
	    return token.getIdTipo(); //acceso a la entrada que devolvumos
	}
	//lexico devuelve i de token! y lexico en yylval lo asocie con la tabla de simbolos
	return 0;
}





public static void main(String args[]) throws IllegalArgumentException, IllegalAccessException {
 	String direccion_codigo = "casos_de_prueba_tps.txt";
	
 	AnalizadorLexico al = new AnalizadorLexico(direccion_codigo);
	al.abrirCargarArchivo();
	TablaDeSimbolos tds = new TablaDeSimbolos();

	
	Parser par = new Parser(false, al, tds);
 	par.yyparse();
 	
 	tds.mostrarSimbolos();
}
//#line 377 "Parser.java"
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
//#line 52 "gramaticaIncremental.y"
{System.out.println("\n LLEGO A RAIZ! -> termino programa \n ");}
break;
case 4:
//#line 61 "gramaticaIncremental.y"
{System.out.println("\n SENTENCIA DECLARATIVA CORRECTA \n");
								   	System.out.println("\n----------------------------------------\n");
									}
break;
case 5:
//#line 64 "gramaticaIncremental.y"
{System.out.println("\n SENTENCIA EJECUTABLE CORRECTA \n");
		    	   				   System.out.println("\n----------------------------------------\n");
		  						   }
break;
case 8:
//#line 85 "gramaticaIncremental.y"
{/*System.out.println("VARIABLE BIEN DECLARADA con TIPO!! ");*/
												   /*Token t = (Token)yyval.obj;	*/
												   /*System.out.println("yyval SAPE! "+t.getLexema());*/
												   Token tipo = (Token)val_peek(1).obj;
												   Token variable = (Token)val_peek(0).obj;
												   System.out.println("\n Sintactico  ->  Linea: "+tipo.getNroLinea()+"  ,  VARIABLE BIEN DEFINIDA  "+tipo.getLexema()+" "+variable.getLexema()+"\n");
												   
												   /*String tipo = (Token)$1.obj.getLexema();*/
												   /*String lexema = (Token)$2.obj.getLexema(); */
								   				   /*System.out.println("\n----------------------------------------\n");*/
								   
												   }
break;
case 22:
//#line 142 "gramaticaIncremental.y"
{System.out.println("\n Sintactico  ->  CLAUSULA de SELECCION EJECUTADA CORRECTAMENTE \n");}
break;
case 25:
//#line 145 "gramaticaIncremental.y"
{System.out.println("\n INVOCO TERRIBLE PROCEDURE \n");}
break;
case 26:
//#line 154 "gramaticaIncremental.y"
{/*System.out.println("Existe el lexema en la Tabla de Simbolos");*/
								   Token id = (Token)val_peek(2).obj;
								   int linea = id.getNroLinea();
								   /*System.out.println("\n OJO!! Existe  "+ id.getLexema() +"  en Tabla de Simbolos ??");*/
								   /*System.out.println("EXISTE?? a ver, mostrala ");*/
								   /*tipo de binding?*/
								   /*tabla.mostrarSimbolos();*/
								   
								   Token op = (Token)val_peek(1).obj;
								   Token expr = (Token)val_peek(0).obj;
								   /*es valida esta impleentacion? o consumo  memoria al crear tokens?*/
								   
								   
								   System.out.println("\n Sintactico ->  Linea: "+ linea+"  ,  ASIGNACION DETECTADA   "+id.getLexema()+" "+op.getLexema()+" "+expr.getLexema()+"\n");
								   
								   /*System.out.println("Tabla -> addToken() ");*/
								   /*tabla.addToken(id);*/
								   /*tabla.mostrarSimbolos();*/
								   /*String expr = (Token)$2.obj.getLexema(); */
								   /*System.out.println("\n Sintactico  ->  HAGO ASIGNACION  "+lexema+" = "+expr+"\n");*/
								   
								   /*tabla.mostrarSimbolos();*/
								   /*tabla.mostrarSimbolos();*/
								   /*System.out.println("\n ------------------------------------ \n"); */
								   }
break;
case 29:
//#line 192 "gramaticaIncremental.y"
{/*System.out.println("\n Sintactico  ->  COMPARACION!!\n");*/
									 Token op1 = (Token)val_peek(2).obj;
									 int linea = op1.getNroLinea();
 								     Token op2 = (Token)val_peek(1).obj;
 								     Token op3 = (Token)val_peek(0).obj;
 								     System.out.println("\n Sintactico  ->  Linea: "+linea+"  ,  COMPARACION  ->  "+op1.getLexema()+" "+op2.getLexema()+" "+op3.getLexema()+"\n");
									}
break;
case 35:
//#line 208 "gramaticaIncremental.y"
{System.out.println("\n\nBLOQUE DE MULTIPLES SENTENCIAS!!\n\n");}
break;
case 36:
//#line 212 "gramaticaIncremental.y"
{System.out.println("\n\nSENTENCIA DE CONTROL!!\n\n");}
break;
case 41:
//#line 243 "gramaticaIncremental.y"
{/*System.out.println("\n EXPRESION SUMA  "); */
								   Token op1 = (Token)val_peek(2).obj;
								   int linea = op1.getNroLinea();
								   Token op2 = (Token)val_peek(1).obj;
								   Token op3 = (Token)val_peek(0).obj;
								   System.out.println("\n Sintactico  ->  Linea: "+linea+"  ,  EXPRESION SUMA  ->  "+op1.getLexema()+" "+op2.getLexema()+" "+op3.getLexema()+"\n"); 
								   /*expresion.ptr = crear_terceto(+, expresion.ptr, termino.ptr);*/
								   }
break;
case 42:
//#line 252 "gramaticaIncremental.y"
{System.out.println("EXPRESION RESTA  -  "); }
break;
case 49:
//#line 277 "gramaticaIncremental.y"
{/*System.out.println("CTE negativa! \n"); */
       			 Token op1 = (Token)val_peek(1).obj;
				 int linea = op1.getNroLinea();
				 Token op2 = (Token)val_peek(0).obj;
				 System.out.println("\n Sintactico  ->  Linea: "+linea+"  ,  CTE NEGATIVA!  ->  "+op1.getLexema()+" "+op2.getLexema()+"\n");
				 }
break;
//#line 635 "Parser.java"
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
public Parser(boolean debugMe, AnalizadorLexico al, TablaDeSimbolos tds)
{
  yydebug=debugMe;
  lexico=al;
  tabla=tds;
}
//###############################################################



}
//################### END OF CLASS ##############################
