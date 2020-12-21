package analizador_sintactico;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import analizador_lexico.Token;
import tabla_simbolos.TablaDeSimbolos;

public class GeneradorCodigo {
	private TablaDeSimbolos tds;
	
	private ListaTercetos tercetos;
	
	private TablaRegistros t_reg;
	
	private int reg_actual;
	
	private ArrayList<String> instrucciones_asmb;
	
	//archivo .asm generado, en formato String
	private String archivo_asm = "";

	
	
	
	public GeneradorCodigo(TablaDeSimbolos tds, ListaTercetos t) {
		this.tds=tds;
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
		//String var_aux = "";
		//t.mostrar();
		//t_reg.mostrarRegistrosLibres();
		
		//checkear si son opeaciones entre enteros o con pto flotante!!
		
		
		String instruccion = "";
		
		
		if (t.getOperador().equals("+")) {
			
			int tipo_op = 0;
			
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) != '['){
				//primer operando es un registro y el segundo no
				tipo_op=1;
			}
			
			if ((t.getOperando1().charAt(0)) != '[' && (t.getOperando2().charAt(0)) == '['){
				//primer operando NO es un registro y el segundo si lo es
				tipo_op=2;
			}
			
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) == '['){
				//los dos operandos son registros
				tipo_op=3;
			}
			
			
			switch(tipo_op) {
				case 0:
				{
					//System.out.println("\n SUMO tokens \n");
					reg_actual = t_reg.getRegistroLibre();
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					String mov1 = "MOV "+nombre_reg+" , "+t.getOperando1();
					
					//System.out.println(mov1);
					
					this.instrucciones_asmb.add(mov1);
					
					
					instruccion = "ADD "+nombre_reg+" , "; 	
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					
					this.instrucciones_asmb.add(instruccion);
					
					break;
				}
				
				case 1:
				{
										
					//System.out.println("\n SUMO Registro y Token \n");
					
					
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
										
					instruccion = "ADD "+nombre_reg+" , "; 	
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					this.instrucciones_asmb.add(instruccion);
					
					break;
				}
				
				case 2:
				{
										
					//System.out.println("\n SUMO Token y Terceto \n");

					reg_actual = t_reg.getRegistroLibre();
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					
					instruccion = "ADD "+nombre_reg+" , "; 	
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					this.instrucciones_asmb.add(instruccion);
					
					break;
				}
				
				case 3:
				{
					//aca tengo que liberar los 2 registros!!!
					//System.out.println("\n SUMO Terceto y Terceto \n");

					String nombre_reg1 = t_reg.getNombreReg(reg_actual);
					String nombre_reg2 = "";
					int id_reg2 = 0;
					
					//caso en que usa 2 registros en simultaneo
					if (nombre_reg1.charAt(1)=='B') {
						nombre_reg2 = "EAX";
						id_reg2 = 1;
					}
					
					if (nombre_reg1.charAt(1)=='C') {
						nombre_reg2 = "EBX";
						id_reg2 = 2;
					}
					
										
					reg_actual = t_reg.getRegistroLibre();
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					String mov1 = "MOV "+nombre_reg+" , "+nombre_reg2;//t.getOperando1();
					
					//System.out.println(mov1);
					
					this.instrucciones_asmb.add(mov1);
					
					
					instruccion = "ADD "+nombre_reg+" , "+nombre_reg2; 	
					
					
					//System.out.println(instruccion);
					this.instrucciones_asmb.add(instruccion);
				
					
					this.t_reg.liberarRegistro(reg_actual);
					this.t_reg.liberarRegistro(id_reg2);
				}
			}
			
		};
			
			
		
			
		if (t.getOperador().equals("-")) {
			
			int tipo_op = 0;
			
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) != '['){
				//si elprimer operando es un registro y el segundo no
				tipo_op=1;
			}
			
			if ((t.getOperando1().charAt(0)) != '[' && (t.getOperando2().charAt(0)) == '['){
				//si elprimer operando NO es un registro y el segundo si lo es
				tipo_op=2;
			}
			
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) == '['){
				//si los dos operandos son registros
				tipo_op=3;
			}
			
			
			switch(tipo_op) {
				case 0:
				{
					//System.out.println("\n RESTA tokens \n");
					
					reg_actual = t_reg.getRegistroLibre();
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					String mov1 = "MOV "+nombre_reg+" , "+t.getOperando1();
					
					//System.out.println(mov1);
					
					this.instrucciones_asmb.add(mov1);
					
					
					instruccion = "SUB "+nombre_reg+" , "; 	
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					this.instrucciones_asmb.add(instruccion);
					
					break;
				}
				
				case 1:
				{
					//System.out.println("\n RESTA Registro y Token \n");
					
					
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
										
					instruccion = "SUB "+nombre_reg+" , "; 	
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					this.instrucciones_asmb.add(instruccion);
					
					break;
				}
				
				case 2:
				{
										
					//System.out.println("\n RESTA Token y Terceto \n");

					reg_actual = t_reg.getRegistroLibre();
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					
					instruccion = "ADD "+nombre_reg+" , "; 	
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					
					this.instrucciones_asmb.add(instruccion);
				
				}
				
				case 3:
				{
					//aca tengo que liberar los 2 registros!!!
					//System.out.println("\n RESTA Terceto y Terceto \n");

					String nombre_reg1 = t_reg.getNombreReg(reg_actual);
					String nombre_reg2 = "";
					int id_reg2 = 0;
					
					//caso en que usa 2 registros en simultaneo
					if (nombre_reg1.charAt(1)=='B') {
						nombre_reg2 = "EAX";
						id_reg2 = 1;
					}
					
					if (nombre_reg1.charAt(1)=='C') {
						nombre_reg2 = "EBX";
						id_reg2 = 2;
					}
					
										
					reg_actual = t_reg.getRegistroLibre();
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					
					String mov1 = "MOV "+nombre_reg+" , "+nombre_reg2;//t.getOperando1();
					//System.out.println(mov1);
					this.instrucciones_asmb.add(mov1);
					
					
					instruccion = "SUB "+nombre_reg+" , "+nombre_reg2; 	
					//instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					this.instrucciones_asmb.add(instruccion);
				
					
					this.t_reg.liberarRegistro(reg_actual);
					this.t_reg.liberarRegistro(id_reg2);
				}
			}	
		};
		
		
		
		if (t.getOperador().equals("*")) {
			
			int tipo_op = 0;
			
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) != '['){
				//si elprimer operando es un registro y el segundo no
				tipo_op=1;
			}
			
			if ((t.getOperando1().charAt(0)) != '[' && (t.getOperando2().charAt(0)) == '['){
				//si elprimer operando NO es un registro y el segundo si lo es
				tipo_op=2;
			}
			
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) == '['){
				//si los dos operandos son registros
				tipo_op=3;
			}
			
			
			switch(tipo_op) {
				case 0:
				{
					//System.out.println("\n MULTIPLICO tokens \n");
					reg_actual = t_reg.getRegistroLibre();
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					String mov1 = "MOV "+nombre_reg+" , "+t.getOperando1();
					
					//System.out.println(mov1);
					this.instrucciones_asmb.add(mov1);
					
					
					instruccion = "MUL "+nombre_reg+" , "; 	
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					
					this.instrucciones_asmb.add(instruccion);
					
					break;
				}
				
				case 1:
				{
										
					//System.out.println("\n MULTIPLICO Terceto y Token \n");
					
					
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					
					instruccion = "MUL "+nombre_reg+" , "; 	
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					this.instrucciones_asmb.add(instruccion);
					
					break;
				}
				
				case 2:
				{
										
					//System.out.println("\n MULTIPLICO Token y Terceto \n");

					reg_actual = t_reg.getRegistroLibre();
					
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					String mov1 = "MOV "+nombre_reg+" , "+t.getOperando1();
					
					//System.out.println(mov1);
					
					this.instrucciones_asmb.add(mov1);
					
					
					instruccion = "MUL "+nombre_reg+" , "; 	
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					
					this.instrucciones_asmb.add(instruccion);
				
					break;
				}
				
				case 3:
				{
					//System.out.println("\n MULTIPLICO Terceto y Terceto \n");

					String nombre_reg1 = t_reg.getNombreReg(reg_actual);
					String nombre_reg2 = "";
					int id_reg2 = 0;
					
					//caso en que usa 2 registros en simultaneo
					if (nombre_reg1.charAt(1)=='B') {
						nombre_reg2 = "EAX";
						id_reg2 = 1;
					}
					
					if (nombre_reg1.charAt(1)=='C') {
						nombre_reg2 = "EBX";
						id_reg2 = 2;
					}
					
										
					reg_actual = t_reg.getRegistroLibre();
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					
					String mov1 = "MOV "+nombre_reg+" , "+nombre_reg2;//t.getOperando1();
					//System.out.println(mov1);
					this.instrucciones_asmb.add(mov1);
					
					
					instruccion = "MUL "+nombre_reg+" , "+nombre_reg2; 	
					
					
					//System.out.println(instruccion);
					
					this.instrucciones_asmb.add(instruccion);
				
								
					this.t_reg.liberarRegistro(reg_actual);
					this.t_reg.liberarRegistro(id_reg2);
				}
			}
			
		};
		
		
		
		if (t.getOperador().equals("/")) {
			int tipo_op = 0;
			
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) != '['){
				//si elprimer operando es un registro y el segundo no
				tipo_op=1;
			}
			
			if ((t.getOperando1().charAt(0)) != '[' && (t.getOperando2().charAt(0)) == '['){
				//si elprimer operando NO es un registro y el segundo si lo es
				tipo_op=2;
			}
			
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) == '['){
				//si los dos operandos son registros
				tipo_op=3;
			}
			
			
			switch(tipo_op) {
				case 0:
				{
					//System.out.println("\n DIVIDO tokens \n");
					
					reg_actual = t_reg.getRegistroLibre();
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					String mov1 = "MOV "+nombre_reg+" , "+t.getOperando1();
					
					//System.out.println(mov1);
					
					this.instrucciones_asmb.add(mov1);
					
					
					instruccion = "DIV "+nombre_reg+" , "; 	
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					
					this.instrucciones_asmb.add(instruccion);
					
					break;
				}
				
				case 1:
				{					
					//System.out.println("\n DIVIDO Terceto y Token \n");
					
					
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					
					instruccion = "DIV "+nombre_reg+" , "; 	
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					
					this.instrucciones_asmb.add(instruccion);
					
					break;
				}
				
				case 2:
				{
										
					//System.out.println("\n DIVIDO Token y Terceto \n");

					reg_actual = t_reg.getRegistroLibre();
					
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					String mov1 = "MOV "+nombre_reg+" , "+t.getOperando1();
					
					//System.out.println(mov1);
					
					this.instrucciones_asmb.add(mov1);
					
					
					instruccion = "DIV "+nombre_reg+" , "; 	
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					
					this.instrucciones_asmb.add(instruccion);
				
					break;
				}
				
				case 3:
				{
					//aca tengo que liberar los 2 registros!!!
					//System.out.println("\n DIVIDO Terceto y Terceto \n");

					String nombre_reg1 = t_reg.getNombreReg(reg_actual);
					String nombre_reg2 = "";
					int id_reg2 = 0;
					
					//caso en que usa 2 registros en simultaneo
					if (nombre_reg1.charAt(1)=='B') {
						nombre_reg2 = "EAX";
						id_reg2 = 1;
					}
					
					if (nombre_reg1.charAt(1)=='C') {
						nombre_reg2 = "EBX";
						id_reg2 = 2;
					}
					
										
					reg_actual = t_reg.getRegistroLibre();
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					String mov1 = "MOV "+nombre_reg+" , "+nombre_reg2;//t.getOperando1();
					
					//System.out.println(mov1);
					
					this.instrucciones_asmb.add(mov1);
					
					
					instruccion = "DIV "+nombre_reg+" , "+nombre_reg2; 	
					
					//System.out.println(instruccion);
					
					this.instrucciones_asmb.add(instruccion);
				
								
					this.t_reg.liberarRegistro(reg_actual);
					this.t_reg.liberarRegistro(id_reg2);
				}
			}
	
		};
		
		
		
		//---------- COMPARADORES ---------------------
		
		if (t.getOperador().equals("<")) {
			
			//aca defino el tipo de JUMP
			String jump = "JL ";
			String tag = "etiqueta";
			
			jump += tag;
			
			
			int tipo_op = 0;
			
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) != '['){
				//primer operando es un registro y el segundo no
				tipo_op=1;
			}
			
			if ((t.getOperando1().charAt(0)) != '[' && (t.getOperando2().charAt(0)) == '['){
				//primer operando NO es un registro y el segundo si lo es
				tipo_op=2;
			}
			
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) == '['){
				//los dos operandos son registros
				tipo_op=3;
			}
			
			
			switch(tipo_op) {
				case 0:
				{
					instruccion = "CMP "+t.getOperando1()+" , ";
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					
					this.instrucciones_asmb.add(instruccion);
					this.t_reg.liberarRegistro(reg_actual);
					
					//System.out.println(jump);
					
					this.instrucciones_asmb.add(jump);	
					
					break;
				}
				
				case 1:
				{
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					instruccion = "CMP "+nombre_reg+" , ";
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					this.instrucciones_asmb.add(instruccion);
					this.t_reg.liberarRegistro(reg_actual);

					//System.out.println(jump);
					this.instrucciones_asmb.add(jump);
					
					break;
				}
				
				case 2:
				{
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					instruccion = "CMP "+nombre_reg+" , ";
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					
					this.instrucciones_asmb.add(instruccion);
					this.t_reg.liberarRegistro(reg_actual);

					//System.out.println(jump);
					
					this.instrucciones_asmb.add(jump);	
					
					break;
				}
				
				case 3:
				{
					//aca tengo que liberar los 2 registros!!!
					String nombre_reg1 = t_reg.getNombreReg(reg_actual);
					String nombre_reg2 = "";
					int id_reg2 = 0;
					
					//caso en que usa 2 registros en simultaneo
					if (nombre_reg1.charAt(1)=='B') {
						nombre_reg2 = "EAX";
						id_reg2 = 1;
					}
					
					if (nombre_reg1.charAt(1)=='C') {
						nombre_reg2 = "EBX";
						id_reg2 = 2;
					}
					
					
					instruccion = "CMP "+nombre_reg1+" , ";
					instruccion += nombre_reg2;
					
					//System.out.println(instruccion);
					
					this.instrucciones_asmb.add(instruccion);
					

					//System.out.println(jump);
					
					this.instrucciones_asmb.add(jump);
					
					
					this.t_reg.liberarRegistro(reg_actual);
					this.t_reg.liberarRegistro(id_reg2);
				}
			}
			
		};
		
		
			
		if (t.getOperador().equals(">")) {
			//aca defino el tipo de JUMP
			String jump = "JG ";
			String tag = "etiqueta";
			
			jump += tag;
			
			int tipo_op = 0;
			
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) != '['){
				//primer operando es un registro y el segundo no
				tipo_op=1;
			}
			
			if ((t.getOperando1().charAt(0)) != '[' && (t.getOperando2().charAt(0)) == '['){
				//primer operando NO es un registro y el segundo si lo es
				tipo_op=2;
			}
			
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) == '['){
				//los dos operandos son registros
				tipo_op=3;
			}
			
			
			switch(tipo_op) {
				case 0:
				{
					instruccion = "CMP "+t.getOperando1()+" , ";
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					
					this.instrucciones_asmb.add(instruccion);
					this.t_reg.liberarRegistro(reg_actual);
					
					//System.out.println(jump);
					
					this.instrucciones_asmb.add(jump);	
					
					break;
				}
				
				case 1:
				{
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					instruccion = "CMP "+nombre_reg+" , ";
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					
					this.instrucciones_asmb.add(instruccion);
					this.t_reg.liberarRegistro(reg_actual);
					
					//System.out.println(jump);
					
					this.instrucciones_asmb.add(jump);	
					
					break;
				}
				
				case 2:
				{
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					instruccion = "CMP "+nombre_reg+" , ";
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					
					this.instrucciones_asmb.add(instruccion);
					this.t_reg.liberarRegistro(reg_actual);
					
					//System.out.println(jump);
					this.instrucciones_asmb.add(jump);		
					
					break;
				}
				
				case 3:
				{
					//aca tengo que liberar los 2 registros!!!
					String nombre_reg1 = t_reg.getNombreReg(reg_actual);
					String nombre_reg2 = "";
					int id_reg2 = 0;
					
					//caso en que usa 2 registros en simultaneo
					if (nombre_reg1.charAt(1)=='B') {
						nombre_reg2 = "EAX";
						id_reg2 = 1;
					}
					
					if (nombre_reg1.charAt(1)=='C') {
						nombre_reg2 = "EBX";
						id_reg2 = 2;
					}
					
					
					instruccion = "CMP "+nombre_reg1+" , ";
					instruccion += nombre_reg2;
					
					//System.out.println(instruccion);
					
					this.instrucciones_asmb.add(instruccion);
					
					this.t_reg.liberarRegistro(reg_actual);
					this.t_reg.liberarRegistro(id_reg2);
					
					//System.out.println(jump);
					
					this.instrucciones_asmb.add(jump);	
					
					break;
				}
			}
		};
				
		
		
		//--------------- ASIGNACIONES -----------------
		
		
		if (t.getOperador().equals("=")) {
			
			char tipo_asig = getTipoAsignacion(t); 
			
			if (tipo_asig == 'r') { //asignacion entre var y reg
				String mov = "MOV "+t.getOperando1()+" , "+t_reg.getNombreReg(reg_actual);
				//System.out.println(mov);
				this.instrucciones_asmb.add(mov);

				this.t_reg.liberarRegistro(reg_actual);
			}
			
			if (tipo_asig == 'v') { //asignacion entre 2 variables
				reg_actual = t_reg.getRegistroLibre();
				String nombre_reg = t_reg.getNombreReg(reg_actual);
				String mov1 = "MOV "+nombre_reg+" , "+t.getOperando2();
				
				//System.out.println(mov1);
				this.instrucciones_asmb.add(mov1);
				
				
				String mov2 = "MOV "+t.getOperando1()+" , "+t_reg.getNombreReg(reg_actual);
				//System.out.println(mov2);	
				this.instrucciones_asmb.add(mov2);
				
				this.t_reg.liberarRegistro(reg_actual);
				
			}
		}
		
		
		
		//---------------- SALTOS --------------------
		
		if (t.getOperador().equals("BI")) {
			
			//jump incondicional 
			String salto = "JMP "+t.getOperando1();
			
			//System.out.println(salto);
			
			this.instrucciones_asmb.add(salto);
			
		};
		
			
		if (t.getOperador().equals("BF")) {
			
			//completar etiqueta -> JG
			String last_instr = getUltimaInstruccion();
			
			last_instr = last_instr.substring(0, 3);
			
			if (t.getOperando2().charAt(0) == '[') {
				last_instr += t.getOperando2().substring(1, t.getOperando2().length()-1);
			} else {
				last_instr += " "+t.getOperando2();
			}
			
			//System.out.println(last_instr);
			
			this.instrucciones_asmb.add(last_instr);
				
			};
		
		
		//------------- PROCEDIMIENTOS -------------------
				
		if (t.getOperador().equals("PROC")) {
				
			String proc = t.getOperando1()+" : ";
			//System.out.println(proc);
			this.instrucciones_asmb.add(proc);
				
			};
				
		
		if (t.getOperador().equals("RET")) {
				
			String ret = "RET "+t.getOperando1()+" ";
			//System.out.println(ret);
			this.instrucciones_asmb.add(ret);
					
			};
				
		
		
		//---------------- INVOCACIONES ----------------
		
		if (t.getOperador().equals("CALL")) {
			
			String call = "CALL "+t.getOperando1();
			//System.out.println(call);
			this.instrucciones_asmb.add(call);						
		};
			
		
		//System.out.println("\n");
	}
	
	
	
	public void mostrarCodigoAssembler() {
		System.out.println();
		for (String cod : instrucciones_asmb) {
			System.out.println(cod);
		}
	}
	
	
	public void crearArchivoAssembler() throws IOException {
		//String ruta = "archivoAssembler.txt";
		String ruta = "archivoAssembler.asm";
		
		File f = new File(ruta);
	    FileWriter fw = new FileWriter(f);
	    BufferedWriter escritura = new BufferedWriter(fw);
	    
	    escritura.write(this.archivo_asm);
	   
	    escritura.close();

	}
	
	
	public void generarEstructuraProgramaAsm() {
		//aca ubica en memoria los items de datos y agrega nombre simbolico a direccion de memoria
		String programa_asmb = "";
		
		System.out.println();
		programa_asmb += "\n.MODEL small \n";
		programa_asmb += "\n.STACK 200h  \n";
		programa_asmb += "\n.DATA \n\n";
		programa_asmb += this.getSentenciasDeclarativasString();
		programa_asmb += "\n.CODE \n"; //VER SI VA en minuscula
		//programa_asmb += this.tercetos.getListaTercetosString();
		programa_asmb += "\nSTART: \n\n";
		programa_asmb += this.getInstruccionesAssemblerString();
		programa_asmb += "\nEND START \n";
		
		System.out.println(programa_asmb);
		
		this.archivo_asm = programa_asmb;
	}
	
	
	public String getEstructuraPrograma() {
		
		return this.archivo_asm;
	}
	
	
	
	public char getTipoAsignacion(Terceto t) {
		if (t.getOperando2().charAt(0) == '[')
			return 'r';
		return 'v';
		}

	
	public String getInstruccionesAssemblerString() {
		String aux = "";
		
		for (String instr : instrucciones_asmb) {
			aux += instr+"\n";
		}
		
		return aux;
	}
	
	
	public String getSentenciasDeclarativasString() {
		//recorro tabla de simbolos y creo una instruccion para el ensamblador
		//con DB , DW o DD!! depende del tipo de datos
		
		String out = "";
		
		for (Token t : this.tds.getListaTokens()) {
			if (t.getTipo().equals("CTE")){
				//System.out.println("_"+t.getLexema() + " DW "+t.getLexema());//+ t.getLexema());
				out += "_"+t.getLexema() + " DW "+t.getLexema()+"\n";
				//si el tipo es INTEGER -> DW 
				//si es FLOAT -> DD
			} else {
				//System.out.println(t.getLexema() + " DW ");//+ t.getLexema());
				out += t.getLexema() + " DW  \n";
			}
		}	
		
		return out;
	}
	
	
	public String getUltimaInstruccion() {
		return this.instrucciones_asmb.remove(this.instrucciones_asmb.size()-1);
	}
}
