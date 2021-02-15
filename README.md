# Compilador


Trabajo practico que consiste en crear un compilador que consuma codigo (tipo algol, estilo Java), haga el procesamiento de la informacion y, en caso de compilar correctamente, genere codigo maquina.


Los componentes principales del compilador son: 

  .  Analisis Lexico, que se encarga de leer los tokens correctos y clasificarlos en variables, constantes y cadenas;  
  
  .  Analisis Sintactico, que a partir de las reglas de la gramaticaIncremental.y  arma las estructuras sintacticas con YACC, 
  
  .  Generacion de Codigo, que genera codigo intermedio (tercetos) y codigo assembler.
  
  .  Tabla De Simbolos, que almacena todos los tokens correctos sintacticamente, con sus atributos como caracteristicas semanticas del token.  





El AnalizadorLexico reconoce los tokens soportados por el lenguaje, y aplica los checkeos semanticos relativos a la sintaxis.

El AnalizadorSintactico genera el arbol de parsing para la gramatica definida, creando las estructuras sintacticas propias del lenguaje a representar. Una vez definida correctamente la gramatica, se generan las estructuras sintacticas con el compilador de compiladores YACC 

La TablaDeSimbolos contiene la informacion necesaria para manipular la informacion para la correcta compilacion y ejecucion del codigo, previamente procesada por el analizador lexico y sintactico.

El GeneradorCodigo procesa la estrucutura sintactica y aplica semantica en las sentencias declarativas y ejecutables. Con esta informacion, genera los tercetos 
con la lista de tercetos, y crea el codigo maquina listo para ser ensamblado.


Como ultimo paso, se procesa el ejecutable contenido en codigo.asm con el programa MASM32
En caso de compilar correctamente, se ejecuta. La ejecucion del archivo correctamente compilado, construye el proyecto y crea un objeto referenciable, y un archivo ejecutable .exe

