package analizador_sintactico;

import java.util.ArrayList;

public class GeneradorCodigo {
	private ListaTercetos tercetos;
	
	private ArrayList<String> instrucciones_asmb;
	
	
	public GeneradorCodigo(ListaTercetos t) {
		this.tercetos = t;
		this.instrucciones_asmb = new ArrayList<String> ();
	}
	
	public void generarCodigo() {
		
		for (Terceto t : tercetos.getListaTercetos()) {
			this.generarInstrucciones(t);
		}
	}
	
	
	public void generarInstrucciones(Terceto t) {
		System.out.println("BUENASSSS, SOY TTT -> "+t);
		System.out.println("aca genero codigo assembler -> asigno variables aux y uso registros ");
	}

}
