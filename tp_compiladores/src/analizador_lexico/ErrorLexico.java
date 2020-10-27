package analizador_lexico;

public class ErrorLexico extends AccionSemantica {

	
	@Override
	public void ejecutar(char c, int nro_linea) {
		System.out.println("ERROR LEXICO!! "+super.buffer+c+"\n");
		super.buffer="";
	}

	@Override
	public Token getToken() {
		
		return null;
	}

}
