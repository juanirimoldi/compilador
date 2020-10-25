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
public final static short MAYORIGUAL=273;
public final static short MENORIGUAL=274;
public final static short IGUAL=275;
public final static short DISTINTO=276;
public final static short EOF=277;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    3,    3,    5,    6,    6,
   10,    9,    9,    9,   11,   11,    8,    8,    7,    7,
    4,    4,    4,    4,    4,   12,   13,   13,   18,   18,
   18,   18,   18,   18,   19,   14,   15,   16,   20,   20,
   17,   17,   17,   21,   21,   21,   22,   22,   22,
};
final static short yylen[] = {                            2,
    1,    1,    2,    1,    1,    2,    1,    2,   11,   10,
    1,    5,    3,    1,    2,    3,    3,    1,    1,    1,
    1,    1,    2,    2,    2,    4,    8,    6,    3,    3,
    3,    3,    3,    3,    3,    6,    4,    4,    3,    5,
    3,    3,    1,    3,    3,    1,    1,    2,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,   19,   20,    0,    0,    2,
    4,    5,    0,    7,    0,   21,   22,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    3,    6,   18,
    0,   23,   24,   25,    0,    0,   49,   47,    0,    0,
    0,   46,    0,    0,    0,    0,    0,    0,    0,    0,
   38,    0,   48,   26,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   37,   35,    0,   17,   39,    0,    0,    0,   44,   45,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   15,
    0,    0,    0,    0,    0,   28,   16,    0,    0,    0,
   36,   40,    0,    0,    0,    0,   27,    0,    0,   12,
    0,    0,    0,   10,    0,    9,
};
final static short yydgoto[] = {                          8,
  111,   10,   11,   12,   13,   14,   15,   31,   69,  112,
   70,   16,   17,   18,   19,   20,   43,   44,   27,   36,
   41,   42,
};
final static short yysindex[] = {                      -198,
   -7,  -24, -223,    2,  -83,    0,    0,    0, -198,    0,
    0,    0,   -4,    0, -200,    0,    0,   25,   37,   38,
 -174,  -44,  -44,   44, -173, -198, -185,    0,    0,    0,
   43,    0,    0,    0,   30,   34,    0,    0,  -44,    8,
   21,    0,  -23,   48,  -11,   49, -122,   51, -165, -164,
    0, -163,    0,    0,  -44,  -44,  -44,  -44,  -44,  -44,
  -44,  -44,  -44,  -44,  -83, -207, -171, -161,   56,   54,
    0,    0,  -44,    0,    0,   41,   21,   21,    0,    0,
  -18,  -18,  -18,  -18,  -18,  -18, -186, -157,   40,    0,
 -162, -211,   61, -154,  -83,    0,    0, -153,   45,   63,
    0,    0, -152,  -13, -149, -211,    0, -198,  -10,    0,
 -198,   -9, -198,    0,   -8,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  112,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   55,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -41,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   74,
    0,    0,    0,    0,    0,    0,  -36,  -31,    0,    0,
   77,   78,   79,   80,   81,   82,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   83,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    1,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   15,   -3,    0,    0,    0,    0,  -34,    0,    0,   12,
  -54,    0,    0,    0,    0,    0,  -14,   57,  -52,    0,
   24,  -22,
};
final static int YYTABLESIZE=261;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         43,
   39,   43,   72,   43,   41,   28,   41,   40,   41,   42,
   68,   42,   87,   42,    9,   23,   53,   43,   43,   55,
   43,   56,   41,   41,   55,   41,   56,   42,   42,   67,
   42,   88,   21,   24,   79,   80,   64,  100,   63,   26,
   47,   25,  103,   28,   81,   82,   83,   84,   85,   86,
   55,  110,   56,   22,   29,   66,   30,   68,    1,    6,
    7,    2,   57,    6,    7,    3,   54,   58,   32,    4,
    5,   68,    6,    7,   51,   95,   96,   52,   77,   78,
   33,   34,   35,   45,   48,   46,   49,   50,   65,   71,
   73,   74,   75,   76,   89,   90,   91,   92,   94,   97,
   98,  101,  102,   99,  104,  105,  106,   28,  109,  108,
  107,    1,  113,    8,   14,  114,  116,   31,   32,   33,
   34,   29,   30,   13,  115,   11,    0,    0,    0,   93,
    0,    0,    0,    0,    1,    0,    0,    2,    0,    0,
    0,    3,    0,    0,    0,    4,    5,    0,    6,    7,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   37,   38,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   43,   43,   43,   43,    0,   41,   41,   41,   41,
    0,   42,   42,   42,   42,    0,    0,    0,    0,   59,
   60,   61,   62,    0,    0,   66,    0,    0,    0,    6,
    7,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   45,   43,  125,   45,   41,    9,   43,   22,   45,   41,
   45,   43,   65,   45,    0,   40,   39,   59,   60,   43,
   62,   45,   59,   60,   43,   62,   45,   59,   60,   41,
   62,   66,   40,  257,   57,   58,   60,   92,   62,  123,
   26,   40,   95,   47,   59,   60,   61,   62,   63,   64,
   43,  106,   45,   61,   59,  267,  257,   92,  257,  271,
  272,  260,   42,  271,  272,  264,   59,   47,   44,  268,
  269,  106,  271,  272,   41,  262,  263,   44,   55,   56,
   44,   44,  257,   40,  270,  259,   44,   58,   41,   41,
   40,  257,  257,  257,  266,  257,   41,   44,   58,  257,
   61,   41,  257,  266,  258,   61,   44,  111,  258,  123,
  263,    0,  123,   59,   41,  125,  125,   41,   41,   41,
   41,   41,   41,   41,  113,  125,   -1,   -1,   -1,   73,
   -1,   -1,   -1,   -1,  257,   -1,   -1,  260,   -1,   -1,
   -1,  264,   -1,   -1,   -1,  268,  269,   -1,  271,  272,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  273,  274,  275,  276,   -1,  273,  274,  275,  276,
   -1,  273,  274,  275,  276,   -1,   -1,   -1,   -1,  273,
  274,  275,  276,   -1,   -1,  267,   -1,   -1,   -1,  271,
  272,
};
}
final static short YYFINAL=8;
final static short YYMAXTOKEN=277;
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
"MAYORIGUAL","MENORIGUAL","IGUAL","DISTINTO","EOF",
};
final static String yyrule[] = {
"$accept : programa",
"programa : lista_de_sentencias",
"lista_de_sentencias : sentencia",
"lista_de_sentencias : lista_de_sentencias sentencia",
"sentencia : sentencia_declarativa",
"sentencia : sentencia_ejecutable",
"sentencia_declarativa : declaracion_de_variable ';'",
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
"sentencia_ejecutable : sentencia_de_control ','",
"sentencia_ejecutable : sentencia_de_salida ','",
"sentencia_ejecutable : invocacion ','",
"asignacion : ID '=' expresion ';'",
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
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : CTE",
"factor : '-' factor",
"factor : ID",
};

//#line 215 "gramaticaIncremental.y"
	   	


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
//#line 376 "Parser.java"
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
//#line 50 "gramaticaIncremental.y"
{System.out.println("\n LLEGO A RAIZ! -> termino programa \n ");}
break;
case 2:
//#line 54 "gramaticaIncremental.y"
{System.out.println("SENTENCIA SIMPLE! ");}
break;
case 3:
//#line 55 "gramaticaIncremental.y"
{System.out.println("LISTA DE SENTENCIAS RECURSIVA \n ");}
break;
case 4:
//#line 59 "gramaticaIncremental.y"
{System.out.println("SENTENCIA DECLARATIVA ");}
break;
case 5:
//#line 60 "gramaticaIncremental.y"
{System.out.println("SENTENCIA EJECUTABLE ");}
break;
case 6:
//#line 70 "gramaticaIncremental.y"
{System.out.println("DECLARO VARIABLE! ");}
break;
case 7:
//#line 71 "gramaticaIncremental.y"
{System.out.println("DECLARO PROCEDIMIENTO ");}
break;
case 8:
//#line 75 "gramaticaIncremental.y"
{System.out.println("DECLARO VARIABLE con TIPO ");}
break;
case 21:
//#line 120 "gramaticaIncremental.y"
{System.out.println("SENTENCIA EJECUTABLE -> ASIGNACION! ");}
break;
case 22:
//#line 121 "gramaticaIncremental.y"
{System.out.println("CLAUSULA de SELECCION  IF ");}
break;
case 26:
//#line 132 "gramaticaIncremental.y"
{System.out.println("HAGO ASIGNACION -> asigno la EXPRESION! "+((Token) val_peek(3).obj).getLexema()); }
break;
case 35:
//#line 153 "gramaticaIncremental.y"
{System.out.println("\n\nBLOQUE DE MULTIPLES SENTENCIAS!!\n\n");}
break;
case 41:
//#line 182 "gramaticaIncremental.y"
{System.out.println("EXPRESION... "); }
break;
case 42:
//#line 185 "gramaticaIncremental.y"
{System.out.println("EXPRESION... "); }
break;
case 43:
//#line 188 "gramaticaIncremental.y"
{System.out.println("soy terrible TERMINO -> voy a regla EXPRESION "); }
break;
case 46:
//#line 197 "gramaticaIncremental.y"
{System.out.println("soy factor -> voy a regla TERMINO");}
break;
case 47:
//#line 203 "gramaticaIncremental.y"
{System.out.println("llega CTE! -> voy a regla factor \n"); }
break;
case 49:
//#line 208 "gramaticaIncremental.y"
{System.out.println("llega ID! -> voy a regla factor "); }
break;
//#line 597 "Parser.java"
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
