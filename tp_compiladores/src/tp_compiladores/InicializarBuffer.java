package tp_compiladores;

public class InicializarBuffer extends AccionSemantica {
	
	public void ejecutar(char c) {
		super.buffer = "";
		super.buffer += c;
		
		if (Character.isLetter(c)) { super.tipo = "ID"; };
		if (Character.isDigit(c)) { super.tipo = "CTE"; };
		
		System.out.println("ACCION -> Inicializo buffer, agrego caracter y tipo -> "+ super.buffer + " , tipo "+super.tipo);// + " , " + this.buffer);
		
	}
}
