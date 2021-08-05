package generacion_codigo;

import java.util.ArrayList;

public class ListaTercetos {
	private ArrayList<Terceto> lista_tercetos;
	
	public ListaTercetos() {
		this.lista_tercetos = new ArrayList<Terceto> ();
	}
	
	
	public void agregarTerceto(Terceto t) {
		this.lista_tercetos.add(t);
	}
	
	
	public int getPosUltimoTerceto() {
		return 0;
	}
	
	
	public void completarTerceto(int pos, int valor) {
		
		//System.out.println("\n POSICION DEL TERCETO A MODIFICAR -> "+pos);
		
		String val = Integer.toString(valor);
		
		
		if (this.lista_tercetos.get(pos).getOperador().equals("BF")) {
			//System.out.println("\n MODIFICO EL TERCETO BF -> valor "+ val+ "  en pos "+pos+"\n");

			this.lista_tercetos.get(pos).setOperando2(val);
			
		} 
		
		if (this.lista_tercetos.get(pos).getOperador().equals("BI")) {
			this.lista_tercetos.get(pos).setOperando1(val);
		}
		
	
	}
	
	
	public ArrayList<Terceto> getListaTercetos(){
		return this.lista_tercetos;
	}
	
	
	public String getListaTercetosString() {
		String aux = "";
		
		for (Terceto t : lista_tercetos) {
			
			aux += t.getOperador()+" , "+t.getOperando1()+" , "+t.getOperando2()+"\n";
			
		}
		
		return aux;
	}
	
	
	public void mostrarTercetos() {
		System.out.println("\n Tercetos \n");
		for (Terceto t : lista_tercetos) {
			System.out.println(t.getId()+" , "+t.getOperador()+" , "+t.getOperando1()+" , "+t.getOperando2());
		}
		System.out.println();
	}
}
