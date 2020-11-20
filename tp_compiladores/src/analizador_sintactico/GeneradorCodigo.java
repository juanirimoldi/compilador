package analizador_sintactico;

import java.util.ArrayList;

public class GeneradorCodigo {
	private ListaTercetos tercetos;
	
	private ArrayList<String> instrucciones_asmb;
	
	private String var_aux = "@aux";
	private int id_var_aux=1;
	
	
	public GeneradorCodigo(ListaTercetos t) {
		this.tercetos = t;
		this.instrucciones_asmb = new ArrayList<String> ();
	}
	
	public void generarCodigo() {
		System.out.println("");
		for (Terceto t : tercetos.getListaTercetos()) {
			this.generarInstrucciones(t);
		}
	}
	
	
	public void crearInstrucciones(Terceto t) {
		String var_aux = "";
		t.mostrar();
		
		//String mov1 = "MOV R1 , "+t.getOperando1();
		//System.out.println(mov1);
		
		String opera = "";
		
		if (t.getOperador().equals("+")) {
			String mov1 = "MOV R1 , "+t.getOperando1();
			System.out.println(mov1);
			opera = "ADD R1 , "; 	
			opera += t.getOperando2();
			};
		
		if (t.getOperador().equals("-")) {
			opera = "SUB R1 , "; 	
			String mov1 = "MOV R1 , "+t.getOperando1();
			System.out.println(mov1);
			opera += t.getOperando2();
			};
		
		if (t.getOperador().equals("*")) {
			opera = "MUL R1 , ";  
			String mov1 = "MOV R1 , "+t.getOperando1();
			System.out.println(mov1);
			opera += t.getOperando2();};
		
		if (t.getOperador().equals("/")) {
			opera = "DIV R1 , "; 	
			String mov1 = "MOV R1 , "+t.getOperando1();
			System.out.println(mov1);
			opera += t.getOperando2();};
		
		
			//if (t.getOperador().equals("=")) {opera = " "; };
		
		
		
		System.out.println(opera);
		
		
		String mov2 = "MOV "+this.var_aux+this.id_var_aux+" , R1";
		t.setVarAux(this.var_aux+this.id_var_aux);
		this.id_var_aux++;
		System.out.println(mov2);
		//String op = ""
		System.out.println("\n\n");
	}
	
	
	public void generarInstrucciones(Terceto t) {
		
		//t.mostrar();
		//System.out.println("CODIGO GENERADO POR TERCETO ");
		
		this.crearInstrucciones(t);
		/*
		if (t.getOperador().equals("*")) { 
			
		};
		if (t.getOperador().equals("/")) { };
		if (t.getOperador().equals("+")) { };
		if (t.getOperador().equals("-")) { };
		*/
	}

}
