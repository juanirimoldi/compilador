# Compilador


Reconocimiento lexico de los tokens de un lenguaje compuesto de variables, constantes y cadenas, a partir de las reglas de la gramatica arma las estructuras sintacticas con YACC, y genera codigo intermedio (tercetos) y codigo assembler.


El analizador lexico reconoce los tokens soportados por el lenguaje, y aplica los checkeos semanticos relativos a la sintaxis.

El analizador sintactico genera el arbol de parsing para la gramatica definida, creando las estructuras sintacticas propias del lenguaje a representar. Una vez definida correctamente la gramatica, se generan las estructuras sintacticas con el compilador de compiladores YACC 

La tabla de simbolos contiene la informacion necesaria para manipular la informacion para la correcta compilacion y ejecucion del codigo, previamente procesada por el analizador lexico y sintactico.

El generador de codigo procesa la estrucutura sintactica y aplica semantica en las sentencias declarativas y ejecutables. Con esta informacion, genera los tercetos 
con la lista de tercetos, y crea el codigo assembler listo para ser ensamblado

