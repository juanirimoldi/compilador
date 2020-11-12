package analizador_sintactico;

import java.util.ArrayList;

public class ListaTercetos {
	private ArrayList<Terceto> lista_tercetos;
	
	public ListaTercetos() {
		this.lista_tercetos = new ArrayList<Terceto> ();
	}
	
	
	public void agregarTerceto(Terceto t) {
		this.lista_tercetos.add(t);
	}
	
	
	public void mostrarTercetos() {
		System.out.println("\n Tercetos \n");
		for (Terceto t : lista_tercetos) {
			System.out.println(t.getId()+" , "+t.getOperador()+" , "+t.getOperando1()+" , "+t.getOperando2());
		}
	}
}
