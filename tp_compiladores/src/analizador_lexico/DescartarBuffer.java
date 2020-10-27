package analizador_lexico;

public class DescartarBuffer extends AccionSemantica {
	//esta accion semantica esat directamente relacionada con los errores!!
	
	public void ejecutar(char c, int nro_linea) {
		//System.out.println("ERROR!! -> "+ super.buffer);
		super.buffer = "";
	}
	
	public Token getToken() {
		return null;
	}
}
