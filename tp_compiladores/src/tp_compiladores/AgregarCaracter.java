package tp_compiladores;

public class AgregarCaracter extends AccionSemantica {
	
	public void ejecutar(char c) {
		System.out.println("Agrego caracter");
		//s += c;
		super.buffer += c;
	}
	
}
