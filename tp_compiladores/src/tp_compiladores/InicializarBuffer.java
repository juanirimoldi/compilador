package tp_compiladores;

public class InicializarBuffer extends AccionSemantica {
	
	public void ejecutar(char c, String buff) {
		super.buffer = "";
		super.buffer += c;
		
		if (Character.isLetter(c)) { super.tipo_buffer = "ID"; };
		if (Character.isDigit(c)) { super.tipo_buffer = "CTE"; };
		if (c == '=') { super.tipo_buffer = "COMPARADOR"; };
		
		System.out.println("AS1 -> Inicializo buffer, agrego caracter y tipo ");//+ super.buffer + " , tipo "+super.tipo_buffer);
		
	}
	
	
	public String getToken() {
		return "";
	}
}
