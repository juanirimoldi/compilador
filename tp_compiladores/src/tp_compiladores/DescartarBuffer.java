package tp_compiladores;

public class DescartarBuffer extends AccionSemantica {
	
	public void ejecutar(char c, String buffer) {
		System.out.println("Muestro y Descarto Buffer: ");//+ super.buffer);
		super.buffer = "";
	}
	
	public String getToken() {
		return null;
	}
}
