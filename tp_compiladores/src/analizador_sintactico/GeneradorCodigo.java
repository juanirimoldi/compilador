package analizador_sintactico;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GeneradorCodigo {
	private ListaTercetos tercetos;
	
	private TablaRegistros t_reg;
	
	private int reg_actual;
	
	private ArrayList<String> instrucciones_asmb;
	
	private String var_aux = "@aux";
	private int id_var_aux=1;
	
	
	public GeneradorCodigo(ListaTercetos t) {
		this.tercetos = t;
		this.instrucciones_asmb = new ArrayList<String> ();
		this.t_reg = new TablaRegistros();
	}
	
	
	public void generarCodigo() {
		System.out.println("");
		for (Terceto t : tercetos.getListaTercetos()) {
			this.crearInstrucciones(t);
		}
	}
	
	
	public void crearInstrucciones(Terceto t) {
		String var_aux = "";
		t.mostrar();
		t_reg.mostrarRegistrosLibres();
		//String mov1 = "MOV R1 , "+t.getOperando1();
		//System.out.println(mov1);
		//int reg;
		String instruccion = "";
		
		if (t.getOperador().equals("+")) {
			//int getRegistroLibre();
			reg_actual = t_reg.getRegistroLibre();
			String nombre_reg = t_reg.getNombreReg(reg_actual);
			
			//System.out.println("reg actuial -> "+reg_actual+" , nombre -> "+nombre_reg);
			
			String mov1 = "MOV "+nombre_reg+" , "+t.getOperando1();
			System.out.println(mov1);
			this.instrucciones_asmb.add(mov1);
			
			
			instruccion = "ADD "+nombre_reg+" , "; 	
			instruccion += t.getOperando2();
			
			System.out.println(instruccion);
			this.instrucciones_asmb.add(instruccion);
			
			/*
			String mov2 = "MOV "+this.var_aux+this.id_var_aux+" , R1";
			t.setVarAux(this.var_aux+this.id_var_aux);
			this.id_var_aux++;
			System.out.println(mov2);
			*/
			};
		
		if (t.getOperador().equals("-")) {
			reg_actual = t_reg.getRegistroLibre();
			String nombre_reg = t_reg.getNombreReg(reg_actual);
			
			//System.out.println("reg actuial -> "+reg_actual+" , nombre -> "+nombre_reg);
			
			String mov1 = "MOV "+nombre_reg+" , "+t.getOperando1();
			
			System.out.println(mov1);
			this.instrucciones_asmb.add(mov1);
			
			
			instruccion = "SUB "+nombre_reg+" , "; 	
			instruccion += t.getOperando2();
			
			System.out.println(instruccion);
			this.instrucciones_asmb.add(instruccion);
			/*
			String mov2 = "MOV "+this.var_aux+this.id_var_aux+" , R1";
			t.setVarAux(this.var_aux+this.id_var_aux);
			this.id_var_aux++;
			System.out.println(mov2);

			*/
		};
		
		if (t.getOperador().equals("*")) {
			reg_actual = t_reg.getRegistroLibre();
			String nombre_reg = t_reg.getNombreReg(reg_actual);
			
			//System.out.println("reg actuial -> "+reg_actual+" , nombre -> "+nombre_reg);
			
			String mov1 = "MOV "+nombre_reg+" , "+t.getOperando1();
			System.out.println(mov1);
			this.instrucciones_asmb.add(mov1);
			
			
			instruccion = "MUL "+nombre_reg+" , "; 	
			instruccion += t.getOperando2();
			
			System.out.println(instruccion);
			this.instrucciones_asmb.add(instruccion);
			
			/*
			String mov2 = "MOV "+this.var_aux+this.id_var_aux+" , R1";
			t.setVarAux(this.var_aux+this.id_var_aux);
			this.id_var_aux++;
			System.out.println(mov2);

			 */
		};
		
		if (t.getOperador().equals("/")) {
						
			reg_actual = t_reg.getRegistroLibre();
			String nombre_reg = t_reg.getNombreReg(reg_actual);
			
			//System.out.println("reg actuial -> "+reg_actual+" , nombre -> "+nombre_reg);
			
			String mov1 = "MOV "+nombre_reg+" , "+t.getOperando1();
			System.out.println(mov1);
			this.instrucciones_asmb.add(mov1);
			
			instruccion = "DIV "+nombre_reg+" , "; 	
			instruccion += t.getOperando2();
			
			System.out.println(instruccion);
			this.instrucciones_asmb.add(instruccion);
				
			/*
			String mov2 = "MOV "+this.var_aux+this.id_var_aux+" , R1";
			t.setVarAux(this.var_aux+this.id_var_aux);
			this.id_var_aux++;
			System.out.println(mov2);

			*/
		};
		
		
		
		if (t.getOperador().equals("=")) {
			String mov = "MOV "+t.getOperando1()+" , "+t_reg.getNombreReg(reg_actual);
			System.out.println(mov);	
			this.t_reg.liberarRegistro(reg_actual);
			//aca libero reg actual!!!
		}
		
		
		//System.out.println(instruccion);
		
		
		System.out.println("\n\n");
	}
	
	
	public void mostrarCodigoAssembler() {
		System.out.println();
		for (String cod : instrucciones_asmb) {
			System.out.println(cod);
		}
	}
	
	
	public void crearArchivoAssembler() throws IOException {
		String ruta = "archivoAssembler.txt";
	    File f = new File(ruta);
	    FileWriter fw = new FileWriter(f);
	    BufferedWriter escritura = new BufferedWriter(fw);
	    for (String str : instrucciones_asmb) {
	    //for(int i=0;i<Lista.size();i++){
	        escritura.write(str);
	        escritura.newLine();

	    }
	    escritura.close();

	}
}
