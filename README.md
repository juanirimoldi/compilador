# Compilador

partiendo de un lenguaje con variables, constantes y cadenas, se reconocen los tokens, arma las estructuras sintacticas y genera codigo intermedio (tercetos) y codig assembler.


analizador lexico que reconoce los tokens definidos en el diagrama, y aplica los checkeos semanticos relativos a la sintaxis

analizador sintactico genera el arbol de parsing para la gramatica definida, creando las estructuras sintacticas propias del lenguaje a representar

tabla de simbolos contiene la informacion necesaria para manipular la informacion correctamente definida, previamente filtrada por el analizador lexico y sintactico

el generador de codigo procesa la estrucutura sintactico y aplica semantica en las sentencias declarativas y ejecutables. con esta informacion, genera los tercetos 
con la lista de tercetos generada, se crea el codigo assembler listo para ser ensamblado

