#ifndef lint
static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";
#endif
#define YYBYACC 1
#line 4 "especificaciones.y"
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;

package analizador_lexico;
import analizador_lexico.*;

#line 14 "y.tab.c"
#define NUM 257
#define NEG 258
#define YYERRCODE 256
short yylhs[] = {                                        -1,
    0,    0,    1,    1,    2,    2,    2,    2,    2,    2,
    2,    2,
};
short yylen[] = {                                         2,
    0,    2,    1,    2,    1,    3,    3,    3,    3,    2,
    3,    3,
};
short yydefred[] = {                                      1,
    0,    5,    0,    3,    0,    2,    0,    0,    0,    0,
    0,    0,    0,    0,    4,   12,    0,    0,    0,    0,
    0,
};
short yydgoto[] = {                                       1,
    6,    7,
};
short yysindex[] = {                                      0,
  -10,    0,  -39,    0,  -39,    0,   -6,  -92,  -34,  -39,
  -39,  -39,  -39,  -39,    0,    0,  -37,  -37,  -92,  -92,
  -92,
};
short yyrindex[] = {                                      0,
    0,    0,    0,    0,    0,    0,    0,    2,    0,    0,
    0,    0,    0,    0,    0,    0,   36,   48,    9,   21,
   28,
};
short yygindex[] = {                                      0,
    0,   11,
};
#define YYTABLESIZE 247
short yytable[] = {                                       4,
    5,   14,    0,   15,   12,    3,   16,   12,   11,   13,
   10,   10,   13,    8,    0,    9,    0,    0,    8,    0,
   17,   18,   19,   20,   21,    0,    0,    0,    0,    5,
    9,    0,    0,    0,    3,   12,   11,   11,   10,    0,
   13,    0,   10,   10,   10,    7,   10,    0,   10,    8,
    8,    8,    0,    8,    0,    8,   14,    6,    0,   14,
    0,    9,    9,    9,    0,    9,    0,    9,   11,   11,
   11,    0,   11,    0,   11,    0,    7,    0,    7,    0,
    7,    0,    0,    0,    0,    0,    0,   14,    6,    0,
    6,    0,    6,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    2,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    2,
};
short yycheck[] = {                                      10,
   40,   94,   -1,   10,   42,   45,   41,   42,   43,   47,
   45,   10,   47,    3,   -1,    5,   -1,   -1,   10,   -1,
   10,   11,   12,   13,   14,   -1,   -1,   -1,   -1,   40,
   10,   -1,   -1,   -1,   45,   42,   43,   10,   45,   -1,
   47,   -1,   41,   42,   43,   10,   45,   -1,   47,   41,
   42,   43,   -1,   45,   -1,   47,   94,   10,   -1,   94,
   -1,   41,   42,   43,   -1,   45,   -1,   47,   41,   42,
   43,   -1,   45,   -1,   47,   -1,   41,   -1,   43,   -1,
   45,   -1,   -1,   -1,   -1,   -1,   -1,   94,   41,   -1,
   43,   -1,   45,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  257,
};
#define YYFINAL 1
#ifndef YYDEBUG
#define YYDEBUG 0
#endif
#define YYMAXTOKEN 258
#if YYDEBUG
char *yyname[] = {
"end-of-file",0,0,0,0,0,0,0,0,0,"'\\n'",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,"'('","')'","'*'","'+'",0,"'-'",0,"'/'",0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,"'^'",0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
"NUM","NEG",
};
char *yyrule[] = {
"$accept : input",
"input :",
"input : input line",
"line : '\\n'",
"line : exp '\\n'",
"exp : NUM",
"exp : exp '+' exp",
"exp : exp '-' exp",
"exp : exp '*' exp",
"exp : exp '/' exp",
"exp : '-' exp",
"exp : exp '^' exp",
"exp : '(' exp ')'",
};
#endif
#ifndef YYSTYPE
typedef int YYSTYPE;
#endif
#define yyclearin (yychar=(-1))
#define yyerrok (yyerrflag=0)
#ifdef YYSTACKSIZE
#ifndef YYMAXDEPTH
#define YYMAXDEPTH YYSTACKSIZE
#endif
#else
#ifdef YYMAXDEPTH
#define YYSTACKSIZE YYMAXDEPTH
#else
#define YYSTACKSIZE 500
#define YYMAXDEPTH 500
#endif
#endif
int yydebug;
int yynerrs;
int yyerrflag;
int yychar;
short *yyssp;
YYSTYPE *yyvsp;
YYSTYPE yyval;
YYSTYPE yylval;
short yyss[YYSTACKSIZE];
YYSTYPE yyvs[YYSTACKSIZE];
#define yystacksize YYSTACKSIZE
#line 48 "especificaciones.y"




// CODE


/*

// CARACTER DE SINCRONIZACION -> ;
// ; -> "SEGURO" QUE CIERRA UNA REGLA


EJEMPLO MASSA

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


AnalizadorLexico lexico;


/* aca defino el codigo que tome un token!*/



private int yylex() {
	Token token=lexico.yylex();

	if (token!=null){
	    yylval = new ParserVal(token);
	    return token.getId();
	}

	return 0;
}




//String ins;
//StringTokenizer st;


void yyerror(String s)
{
 System.out.println("par:"+s);
}


/*  YYLEX DE DOCUMENTACION
boolean newline;
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
//int tok;
//Double d;
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
 s = lexico.yylex(); //aca devuelve Token
 //s = lexico.yylex().getTipo(); //aca que tengo que devolver?
 
 //System.out.println("tok:"+s);
 try
 {
 d = Double.valueOf(s);/*this may fail*/
 yylval = new ParserVal(d.doubleValue()); //SEE BELOW
 tok = NUM;  //NUM es tipo token
 }
 catch (Exception e)
 {
 tok = s.charAt(0);/*if not float, return char*/
 }
 return tok;
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
    	lexico = new AnalizadorLexico();
	lexico.abrirCargarArchivo();
	
	Parser par = new Parser(false);
 	par.dotest();
}
#line 365 "y.tab.c"
#define YYABORT goto yyabort
#define YYACCEPT goto yyaccept
#define YYERROR goto yyerrlab
int
yyparse()
{
    register int yym, yyn, yystate;
#if YYDEBUG
    register char *yys;
    extern char *getenv();

    if (yys = getenv("YYDEBUG"))
    {
        yyn = *yys;
        if (yyn >= '0' && yyn <= '9')
            yydebug = yyn - '0';
    }
#endif

    yynerrs = 0;
    yyerrflag = 0;
    yychar = (-1);

    yyssp = yyss;
    yyvsp = yyvs;
    *yyssp = yystate = 0;

yyloop:
    if (yyn = yydefred[yystate]) goto yyreduce;
    if (yychar < 0)
    {
        if ((yychar = yylex()) < 0) yychar = 0;
#if YYDEBUG
        if (yydebug)
        {
            yys = 0;
            if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
            if (!yys) yys = "illegal-symbol";
            printf("yydebug: state %d, reading %d (%s)\n", yystate,
                    yychar, yys);
        }
#endif
    }
    if ((yyn = yysindex[yystate]) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
    {
#if YYDEBUG
        if (yydebug)
            printf("yydebug: state %d, shifting to state %d (%s)\n",
                    yystate, yytable[yyn],yyrule[yyn]);
#endif
        if (yyssp >= yyss + yystacksize - 1)
        {
            goto yyoverflow;
        }
        *++yyssp = yystate = yytable[yyn];
        *++yyvsp = yylval;
        yychar = (-1);
        if (yyerrflag > 0)  --yyerrflag;
        goto yyloop;
    }
    if ((yyn = yyrindex[yystate]) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
    {
        yyn = yytable[yyn];
        goto yyreduce;
    }
    if (yyerrflag) goto yyinrecovery;
#ifdef lint
    goto yynewerror;
#endif
yynewerror:
    yyerror("syntax error");
#ifdef lint
    goto yyerrlab;
#endif
yyerrlab:
    ++yynerrs;
yyinrecovery:
    if (yyerrflag < 3)
    {
        yyerrflag = 3;
        for (;;)
        {
            if ((yyn = yysindex[*yyssp]) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
#if YYDEBUG
                if (yydebug)
                    printf("yydebug: state %d, error recovery shifting\
 to state %d\n", *yyssp, yytable[yyn]);
#endif
                if (yyssp >= yyss + yystacksize - 1)
                {
                    goto yyoverflow;
                }
                *++yyssp = yystate = yytable[yyn];
                *++yyvsp = yylval;
                goto yyloop;
            }
            else
            {
#if YYDEBUG
                if (yydebug)
                    printf("yydebug: error recovery discarding state %d\n",
                            *yyssp);
#endif
                if (yyssp <= yyss) goto yyabort;
                --yyssp;
                --yyvsp;
            }
        }
    }
    else
    {
        if (yychar == 0) goto yyabort;
#if YYDEBUG
        if (yydebug)
        {
            yys = 0;
            if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
            if (!yys) yys = "illegal-symbol";
            printf("yydebug: state %d, error recovery discards token %d (%s)\n",
                    yystate, yychar, yys);
        }
#endif
        yychar = (-1);
        goto yyloop;
    }
yyreduce:
#if YYDEBUG
    if (yydebug)
        printf("yydebug: state %d, reducing by rule %d (%s)\n",
                yystate, yyn, yyrule[yyn]);
#endif
    yym = yylen[yyn];
    yyval = yyvsp[1-yym];
    switch (yyn)
    {
case 4:
#line 34 "especificaciones.y"
{ System.out.println(" " + yyvsp[-1].dval + " "); }
break;
case 5:
#line 37 "especificaciones.y"
{ yyval = yyvsp[0]; }
break;
case 6:
#line 38 "especificaciones.y"
{ yyval = new ParserVal(yyvsp[-2].dval + yyvsp[0].dval); }
break;
case 7:
#line 39 "especificaciones.y"
{ yyval = new ParserVal(yyvsp[-2].dval - yyvsp[0].dval); }
break;
case 8:
#line 40 "especificaciones.y"
{ yyval = new ParserVal(yyvsp[-2].dval * yyvsp[0].dval); }
break;
case 9:
#line 41 "especificaciones.y"
{ yyval = new ParserVal(yyvsp[-2].dval / yyvsp[0].dval); }
break;
case 10:
#line 42 "especificaciones.y"
{ yyval = new ParserVal(-yyvsp[0].dval); }
break;
case 11:
#line 43 "especificaciones.y"
{ yyval = new ParserVal(Math.pow(yyvsp[-2].dval, yyvsp[0].dval)); }
break;
case 12:
#line 44 "especificaciones.y"
{ yyval = yyvsp[-1]; }
break;
#line 541 "y.tab.c"
    }
    yyssp -= yym;
    yystate = *yyssp;
    yyvsp -= yym;
    yym = yylhs[yyn];
    if (yystate == 0 && yym == 0)
    {
#if YYDEBUG
        if (yydebug)
            printf("yydebug: after reduction, shifting from state 0 to\
 state %d\n", YYFINAL);
#endif
        yystate = YYFINAL;
        *++yyssp = YYFINAL;
        *++yyvsp = yyval;
        if (yychar < 0)
        {
            if ((yychar = yylex()) < 0) yychar = 0;
#if YYDEBUG
            if (yydebug)
            {
                yys = 0;
                if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
                if (!yys) yys = "illegal-symbol";
                printf("yydebug: state %d, reading %d (%s)\n",
                        YYFINAL, yychar, yys);
            }
#endif
        }
        if (yychar == 0) goto yyaccept;
        goto yyloop;
    }
    if ((yyn = yygindex[yym]) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn];
    else
        yystate = yydgoto[yym];
#if YYDEBUG
    if (yydebug)
        printf("yydebug: after reduction, shifting from state %d \
to state %d\n", *yyssp, yystate);
#endif
    if (yyssp >= yyss + yystacksize - 1)
    {
        goto yyoverflow;
    }
    *++yyssp = yystate;
    *++yyvsp = yyval;
    goto yyloop;
yyoverflow:
    yyerror("yacc stack overflow");
yyabort:
    return (1);
yyaccept:
    return (0);
}
