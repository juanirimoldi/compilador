package generacion_codigo;

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
	private int reg2_actual=0;
	
	private ArrayList<String> instrucciones_asmb;
	
	//archivo .asm generado, en formato String
	private String archivo_asm = "";
	
	private int cont=0;

	private boolean errorEjecucion=false;
	
	
	
	public GeneradorCodigo(TablaDeSimbolos tds, ListaTercetos t) {
		this.tds=tds;
		this.tercetos = t;
		this.instrucciones_asmb = new ArrayList<String> ();
		this.t_reg = new TablaRegistros();
	}
	
	
	public void generarCodigo() {
		System.out.println("");
		for (Terceto t : tercetos.getListaTercetos()) {
			if (t != null) {
				this.crearInstrucciones(t);
			}
		}
	}
	
	
	public boolean esPtoFlotante(String val) {
		if (val.contains(".")) {
			return true;
		}
		return false;
	}
	
	public boolean esPunteroPuntoFlotante(String var) {
		//checkea si la variable es de tipo FLOAT
		Token aux = tds.getSimbolo(var);
		
		
		String ptr = "";
		if (aux != null) { //si existe el symbolo en la TSym
			if (aux.getPtr() != "") { //si tiene puntero
				ptr = tds.getSimbolo(var).getPtr();
			}
		}
		
		Token apuntado = tds.getSimbolo(ptr);
		
		if (ptr != "") {	
			if (apuntado != null) {
				if (apuntado.getTipoVar().equals("FLOAT")) {
					return true;
				}
			}
		}
	
		return false;
	}
	
	
	public void crearInstrucciones(Terceto t) {
		
		
		String instruccion = "";
		
		
		// SUMA
		
		if (t.getOperador().equals("+")) {
			
			int tipo_op = 0; //por defecto son 2 tokens
			
			//aclaracion!
			//en vez de referenciar al terceto y leer para atras
			//lo quye hay que hacer es crear variables auxiliares que almacenen el valor del terceto
			//y despues limpiar
			
			//si primer operando es un registro y el segundo no
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) != '['){
				tipo_op=1;
			}

			//si primer operando NO es un registro y el segundo si lo es
			if ((t.getOperando1().charAt(0)) != '[' && (t.getOperando2().charAt(0)) == '['){
				tipo_op=2;
			}
			
			//los dos operandos son registros
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) == '['){
				tipo_op=3;
			}
			
			//System.out.println("sumas "+t.getOperando1()+" , "+t.getOperando2());
			
			switch(tipo_op) {
				
				case 0: //suma entre 2 tokens
				{
					if (esPtoFlotante(t.getOperando1()) && esPtoFlotante(t.getOperando2())) {
						
						String fld1 = "FLD _"+t.getOperando1().replace('.', '_');
						this.instrucciones_asmb.add(fld1);
												
						String fld2 = "FLD _"+t.getOperando2().replace('.', '_');
						this.instrucciones_asmb.add(fld2);
						
						String fadd = "FADD ";
						this.instrucciones_asmb.add(fadd);
						
						break;
					}
					
					
					if (!esPunteroPuntoFlotante(t.getOperando1())) { 
											
						reg_actual = t_reg.getRegistroLibre();
						String nombre_reg = t_reg.getNombreReg(reg_actual);
						
						String mov1 = "MOV "+nombre_reg+" , "+t.getOperando1();
						
						
						this.instrucciones_asmb.add(mov1);

						
						instruccion = "ADD "+nombre_reg+" , "; 	
						instruccion += t.getOperando2();
						
						//System.out.println(instruccion);
						
						this.instrucciones_asmb.add(instruccion);

						break;
						
				} else {
					//System.out.println("suma entre tokens puntero a tipo float "+t.getOperando1()+" , "+t.getOperando2());
					
					String fadd = "FADD "; // esto hace ST(1) + ST , ajusta el puntero de pila y pone el resultado en ST
					//es decir, FADD hace la suma b@main + bb@main
					this.instrucciones_asmb.add(fadd);
					break;
					}
					
				}
				
				
				case 1:
				{
					//SUMO Terceto (Registro) y Token
					
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					instruccion = "ADD "+nombre_reg+" , ";
					

					Token tt = tds.getSimbolo(t.getOperando2());
					//System.out.println("tt"+tt);
					
					if (tt.getTipo().equals("CTE") && tt.getTipoVar().equals("INTEGER")){
						instruccion += "_";
					}
					//instruccion = "ADD "+nombre_reg+" , _"; 	
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					this.instrucciones_asmb.add(instruccion);
					
					break;
				}
				
				
				case 2:
				{
										
					// SUMO Token y Terceto ;

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
					// SUMO Registro y Registro 
					//tengo que liberar los 2 registros!!?
					
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
			
			
			int tipo_op = 0; //por defecto son 2 tokens
			
			//si primer operando es un registro y el segundo no
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) != '['){
				tipo_op=1;
			}

			//si primer operando NO es un registro y el segundo si lo es
			if ((t.getOperando1().charAt(0)) != '[' && (t.getOperando2().charAt(0)) == '['){
				tipo_op=2;
			}
			
			//los dos operandos son registros
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) == '['){
				tipo_op=3;
			}
			
			
			switch(tipo_op) {
				
				case 0: //resta entre 2 tokens
				{	
					if (esPtoFlotante(t.getOperando1()) && esPtoFlotante(t.getOperando2())) {
						
						String fld1 = "FLD _"+t.getOperando1().replace('.', '_');
						this.instrucciones_asmb.add(fld1);
												
						String fld2 = "FLD _"+t.getOperando2().replace('.', '_');
						this.instrucciones_asmb.add(fld2);
						
						String fsub = "FSUB ";
						this.instrucciones_asmb.add(fsub);
						
						break;
					}
					
					
					if (!esPunteroPuntoFlotante(t.getOperando1())) { 
											
						reg_actual = t_reg.getRegistroLibre();
						String nombre_reg = t_reg.getNombreReg(reg_actual);
						
						String mov1 = "MOV "+nombre_reg+" , "+t.getOperando1();
						
						
						this.instrucciones_asmb.add(mov1);
						
						
						instruccion = "SUB "+nombre_reg+" , "; 	
						
						Token aux = tds.getSimbolo(t.getOperando2());
						
						//si es una CTE 
						if (aux.getPtr() == ""){
							instruccion += "_";
						}
						
						instruccion += t.getOperando2();
						
						//System.out.println(instruccion);
						
						this.instrucciones_asmb.add(instruccion);
						
						break;
				
				} else {
					
					String fsub = "FSUB "; // esto hace ST(1) + ST , ajusta el puntero de pila y pone el resultado en ST
					
					this.instrucciones_asmb.add(fsub);
					break;
					}
					
				}
				
				
				case 1:
				{
					//RESTA Registro y Token
					
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					instruccion = "SUB "+nombre_reg+" , "; 	
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					this.instrucciones_asmb.add(instruccion);
					
					break;
				}
				
				
				case 2:
				{
										
					// RESTA Token y Terceto ;

					reg_actual = t_reg.getRegistroLibre();
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					
					instruccion = "SUB "+nombre_reg+" , "; 	
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					this.instrucciones_asmb.add(instruccion);
					
					break;
				}
				
				case 3:
				{
					// RESTA Registro y Registro 
					//tengo que liberar los 2 registros!!?
					
					
					if (reg2_actual == 0) {
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
						
						
						//System.out.println(instruccion);
						this.instrucciones_asmb.add(instruccion);
					
						
						this.t_reg.liberarRegistro(reg_actual);
						this.t_reg.liberarRegistro(id_reg2);
				
					} else {
						
						
						String nombre_reg1 = t_reg.getNombreReg(reg_actual);
						String nombre_reg2_mov = "";//t_reg.getNombreReg(reg2_actual);
						
						int id_reg2 = 0;
						
						//caso en que usa 2 registros en simultaneo
						if (nombre_reg1.charAt(1)=='B') {
							nombre_reg2_mov = "EAX";
							id_reg2 = 1;
						}
						
						if (nombre_reg1.charAt(1)=='C') {
							nombre_reg2_mov = "EBX";
							id_reg2 = 2;
						}
						
											
						reg_actual = t_reg.getRegistroLibre();
						String nombre_reg = t_reg.getNombreReg(reg_actual);
						
						String mov1 = "MOV "+nombre_reg+" , "+nombre_reg2_mov;//t.getOperando1();
						
						//System.out.println(mov1);
						
						this.instrucciones_asmb.add(mov1);
						
						
						String nombre_reg2 = t_reg.getNombreReg(reg2_actual);
						
						instruccion = "SUB "+nombre_reg+" , "+nombre_reg2; 	
						
						
						this.instrucciones_asmb.add(instruccion);
					
						
						this.t_reg.liberarRegistro(reg_actual);
						this.t_reg.liberarRegistro(id_reg2);
						
						this.reg2_actual=0;
					}
				}
			}

		};
		
		
		
		if (t.getOperador().equals("*")) {
						
			int tipo_op = 0; //por defecto son 2 tokens
			
			//si primer operando es un registro y el segundo no
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) != '['){
				tipo_op=1;
			}

			//si primer operando NO es un registro y el segundo si lo es
			if ((t.getOperando1().charAt(0)) != '[' && (t.getOperando2().charAt(0)) == '['){
				tipo_op=2;
			}
			
			//los dos operandos son registros
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) == '['){
				tipo_op=3;
			}
			
			
			switch(tipo_op) {
				
				case 0: //multiplicacion entre 2 tokens
				{
					//si son dos tokens float
					if (esPtoFlotante(t.getOperando1()) && esPtoFlotante(t.getOperando2())) {
						
						String fld1 = "FLD _"+t.getOperando1().replace('.', '_');
						this.instrucciones_asmb.add(fld1);
												
						String fld2 = "FLD _"+t.getOperando2().replace('.', '_');
						this.instrucciones_asmb.add(fld2);
						
						String fadd = "FMUL ";
						this.instrucciones_asmb.add(fadd);
						
						break;
					}
					
					//si el op1 no es puntero a float
					if (!esPunteroPuntoFlotante(t.getOperando1())) { 
								
						reg_actual = t_reg.getRegistroLibre();
						String nombre_reg = t_reg.getNombreReg(reg_actual);
						
						String mov1 = "MOV "+nombre_reg+" , "+t.getOperando1();
						
						
						this.instrucciones_asmb.add(mov1);
						
						instruccion = "IMUL "+nombre_reg+" , "; 	
						
						if (tds.getSimbolo(t.getOperando2()).getTipo().equals("CTE")) {
							instruccion += "_"; 	
						}
						
						instruccion += t.getOperando2();
						
						//System.out.println(instruccion);
						
						this.instrucciones_asmb.add(instruccion);
						
						break;
						
				} else {
					//System.out.println("suma entre tokens puntero a tipo float "+t.getOperando1()+" , "+t.getOperando2());
					
					String fadd = "FMUL "; // esto hace ST(1) + ST , ajusta el puntero de pila y pone el resultado en ST
					//es decir, FADD hace la suma b@main + bb@main
					this.instrucciones_asmb.add(fadd);
					break;
					}
					
				}
				
				
				case 1:
				{
					//MULTIPLICA Registro y Token
					
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					instruccion = "MUL "+nombre_reg+" , "; 	
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					this.instrucciones_asmb.add(instruccion);
					
					break;
				}
				
				
				case 2:
				{
										
					// MULTIPLICA Token y Terceto ;

					reg_actual = t_reg.getRegistroLibre();
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					
					instruccion = "MUL "+nombre_reg+" , "; 	
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					this.instrucciones_asmb.add(instruccion);
					
					break;
				}
				
				case 3:
				{
					// MULTIPLICA Registro y Registro 
					//tengo que liberar los 2 registros!!?
					
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
		
		
		
		// DIVISION
		
		if (t.getOperador().equals("/")) {
						
			int tipo_op = 0; //por defecto son 2 tokens
			
			//si primer operando es un registro y el segundo no
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) != '['){
				tipo_op=1;
			}

			//si primer operando NO es un registro y el segundo si lo es
			if ((t.getOperando1().charAt(0)) != '[' && (t.getOperando2().charAt(0)) == '['){
				tipo_op=2;
			}
			
			//los dos operandos son registros
			if ((t.getOperando1().charAt(0)) == '[' && (t.getOperando2().charAt(0)) == '['){
				tipo_op=3;
			}
			
			
			switch(tipo_op) {
				
				case 0: //division entre 2 tokens
				{
					if (esPtoFlotante(t.getOperando1()) && esPtoFlotante(t.getOperando2())) {
						
						String fld1 = "FLD _"+t.getOperando1().replace('.', '_');
						this.instrucciones_asmb.add(fld1);
												
						String fld2 = "FLD _"+t.getOperando2().replace('.', '_');
						this.instrucciones_asmb.add(fld2);
						
						String fdiv = "FDIV ";
						this.instrucciones_asmb.add(fdiv);
						
						break;
					}
					
					
					if (!esPunteroPuntoFlotante(t.getOperando1())) { 
						
						if (t.getOperando2().equals("0")){
							System.out.println("ERROR de EJECUCION!  Dividide "+t.getOperando1()+" por 0 ");
							this.errorEjecucion=true;
							break;
						}
						
						reg_actual = t_reg.getRegistroLibre();
						String nombre_reg = t_reg.getNombreReg(reg_actual);
						
						String mov1 = "MOV "+nombre_reg+" , "+t.getOperando1();
						
						
						this.instrucciones_asmb.add(mov1);
						
						
						instruccion = "DIV ";//+nombre_reg+" , "; 	
						
						
						this.reg2_actual=reg_actual;
						//System.out.println("reg2 "+reg2_actual);
						
						
						if (tds.getSimbolo(t.getOperando2()).getTipo().equals("CTE")) {
							instruccion += "_"; 	
						}
						
						instruccion += t.getOperando2();
						
						
						this.instrucciones_asmb.add(instruccion);
						
						break;
				} else {
					//System.out.println("suma entre tokens puntero a tipo float "+t.getOperando1()+" , "+t.getOperando2());
					
					String fadd = "FDIV "; // esto hace ST(1) + ST , ajusta el puntero de pila y pone el resultado en ST
					//es decir, FADD hace la suma b@main + bb@main
					this.instrucciones_asmb.add(fadd);
					break;
					}
					
				}
				
				
				case 1:
				{
					//DIVIDO Registro y Token
					
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					instruccion = "DIV "+nombre_reg+" , "; 	
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					this.instrucciones_asmb.add(instruccion);
					
					break;
				}
				
				
				case 2:
				{
										
					// DIVIDO Token y Terceto ;

					reg_actual = t_reg.getRegistroLibre();
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					
					instruccion = "DIV "+nombre_reg+" , "; 	
					instruccion += t.getOperando2();
					
					//System.out.println(instruccion);
					this.instrucciones_asmb.add(instruccion);
					
					break;
				}
				
				case 3:
				{
					// DIVISION Registro y Registro 
					//tengo que liberar los 2 registros!!?
					
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
			//String tag = "etiqueta";
			
			//jump += tag;
			
			
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
					
				
					
					reg_actual = t_reg.getRegistroLibre();
					String nombre_reg = t_reg.getNombreReg(reg_actual);
					
					String mov1 = "CMP "+nombre_reg+" , "+nombre_reg2;//t.getOperando1();
					
					//System.out.println(mov1);
					
					this.instrucciones_asmb.add(mov1);
					
					
					this.t_reg.liberarRegistro(reg_actual);
					this.t_reg.liberarRegistro(id_reg2);
				}
			}
			
		};
		
		
			
		if (t.getOperador().equals(">")) {
			//aca defino el tipo de JUMP
			String jump = "JG ";
			//String tag = "etiqueta";
			
			//jump += tag;
			
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
				case 0: //comparacion entre 2 tokens
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
				
				case 1: //comparacion entre un terceto y un token 
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
				
				case 2: //comparacion entre un token y un terceto
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
				
				case 3: //comparacion entre tercetos
				{
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
			
			if (!errorEjecucion) {
				char tipo_asig = getTipoAsignacion(t); 
				
				
				if (tipo_asig == 'r') { //asignacion entre reg (terceto) y variable
					
					if (!this.esPunteroPuntoFlotante(t.getOperando1())) {
											
						String mov = "MOV "+t.getOperando1()+" , "+t_reg.getNombreReg(reg_actual);
						//System.out.println(mov);
						this.instrucciones_asmb.add(mov);
		
						this.t_reg.liberarRegistro(reg_actual);
						
					} else {
											
						String fst = "FST "+t.getOperando1();
						
						this.instrucciones_asmb.add(fst);
							
					}
				}
				
				
				if (tipo_asig == 'v') { //asignacion entre 2 variables
					
					if (this.esPtoFlotante(t.getOperando2())) {
						
						String fld = "FLD _"+t.getOperando2().replace('.', '_');
						this.instrucciones_asmb.add(fld);
						
						String fstp = "FSTP "+t.getOperando1();
						this.instrucciones_asmb.add(fstp);
					
						
					} else {
							
						reg_actual = t_reg.getRegistroLibre();
						String nombre_reg = t_reg.getNombreReg(reg_actual);
						
						String mov1 = "MOV "+nombre_reg+" , ";
						
						System.out.println("a ver.. "+t.getOperando1()+" , "+t.getOperando2());
						Token sym = tds.getSimbolo(t.getOperando2());

						if (sym.getTipo().equals("CTE")) {
						//		System.out.println("symba sabe"+sym.getLexema());
								mov1 += "_";
							}
						
						mov1 += t.getOperando2();
						//String mov1 = "MOV "+nombre_reg+" , _"+t.getOperando2();
							
						//System.out.println(mov1);
						this.instrucciones_asmb.add(mov1);
							
						//porque en las asignaciones, el operando1 siempre es varible
						String mov2 = "MOV "+t.getOperando1()+" , "+t_reg.getNombreReg(reg_actual);
						//System.out.println(mov2);	
						this.instrucciones_asmb.add(mov2);
							
						this.t_reg.liberarRegistro(reg_actual);
			
					}
				}
				} 
				else {
					
					this.errorEjecucion=false;
					//break;
				}
			
			}
		
		
		
		
		//---------------- SALTOS --------------------
		
		
		if (t.getOperador().equals("BI")) {
			
			//salto incondicional 
			String salto = "JMP instr_"+t.getOperando1();
			
			int dir_jg; 
			dir_jg = Integer.parseInt(t.getOperando1());

			String dir_aux = "instr_"+String.valueOf(dir_jg)+": \n";
			dir_aux += instrucciones_asmb.get(dir_jg-1);
			
			instrucciones_asmb.set(dir_jg-1, dir_aux);
			this.instrucciones_asmb.add(salto);
			
		};
		
		
			
		if (t.getOperador().equals("BF")) {
			
			String last_instr = getUltimaInstruccion();
			
			last_instr = last_instr.substring(0, 3);
			
			if (t.getOperando2() != " ") {	
				int dir_jg; 

				if (t.getOperando2().charAt(0) == '[') {
					last_instr += "instr_"+t.getOperando2().substring(1, t.getOperando2().length()-1);
					dir_jg = Integer.parseInt(t.getOperando2().substring(1, t.getOperando2().length()-1));
				} else {
					last_instr += "instr_"+t.getOperando2();
					dir_jg = Integer.parseInt(t.getOperando2());
				}	
			
				int dir = dir_jg-1;
				
				if (dir >= instrucciones_asmb.size()) {
					dir = dir-1;
				}
				
				String dir_aux = "instr_"+String.valueOf(dir_jg)+": \n";
				//if ((dir_jg-1) != null) {
				dir_aux += instrucciones_asmb.get(dir);
				
				instrucciones_asmb.set(dir, dir_aux);
			
				this.instrucciones_asmb.add(last_instr);
				//}
			}
		};
		
		
			
		//------------- PROCEDIMIENTOS -------------------
				
		if (t.getOperador().equals("PROC")) {
				
			String proc = t.getOperando1()+" PROC";
			//System.out.println(proc);
			this.instrucciones_asmb.add(proc);
				
			};
				
		
		if (t.getOperador().equals("RET")) {
				
			String ret = "RET ";
			String endp = t.getOperando1()+" ENDP";
			//System.out.println(ret);
			this.instrucciones_asmb.add(ret);
			this.instrucciones_asmb.add(endp);
					
			};
				
		
		
		//---------------- INVOCACIONES ----------------
		
		if (t.getOperador().equals("CALL")) {
			
			String call = "INVOKE "+t.getOperando1();
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
	
	
	public void crearArchivoAssembler(String n) throws IOException {
		//String ruta = "archivoAssembler.txt";
		String ruta = "archivoAssembler_"+n+".asm";
		
		File f = new File(System.getProperty("user.dir"), ruta);
		//File f = new File(ruta);
		
	    FileWriter fw = new FileWriter(f);
	    BufferedWriter escritura = new BufferedWriter(fw);
	    
	    escritura.write(this.archivo_asm);
	   
	    escritura.close();

	}
	
	
	public void generarEstructuraProgramaAsm() {
		//aca ubica en memoria los items de datos y agrega nombre simbolico a direccion de memoria
		String programa_asmb = "";
		
		System.out.println();
		//generar encabezado
		//programa_asmb += "\n.MODEL small \n";
		//programa_asmb += "\n.STACK 200h  \n";
		programa_asmb += this.generarEncabezado();
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
	
	
	
	public String generarEncabezado() {
		String aux="";
		aux += ".386 \n";
		aux += ".MODEL flat, stdcall \n";
		aux += "option casemap:none \n";
		aux += "include \\masm32\\include\\windows.inc \n";
		aux += "include \\masm32\\include\\kernel32.inc \n";
		aux += "include \\masm32\\include\\user32.inc \n";
		aux += "includelib \\masm32\\lib\\kernel32.lib \n";
		aux += "includelib \\masm32\\lib\\user32.lib \n";

		return aux;
	}
	
	
	public String getEstructuraPrograma() {
		
		return this.archivo_asm;
	}
	
	
	
	public char getTipoAsignacion(Terceto t) {
		//si el operando 2 del Terceto es una referencia a otro Terceto 
		//	retorno tipo 'r' (registro)
		//sino retorno tipo 'v' (variable)
		
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
		
		String out = "";
		
		for (Token t : this.tds.getListaTokens()) {
			if (t != null) {
				if (t.getTipo().equals("CTE")){
					//System.out.println("_"+t.getLexema() + " DW "+t.getLexema());//+ t.getLexema());
					//out += "_"+t.getLexema() + " DW "+t.getLexema()+"\n";
					
					if (t.getTipoVar().equals("INTEGER")) {
						out += "_"+t.getLexema() + " DD "+t.getLexema()+"\n";
					}
					
					if (t.getTipoVar().equals("FLOAT")) {
						out += "_"+t.getLexema().replace('.', '_') + " DQ "+t.getLexema()+ "\n";
						//si es float, cambio el . por _
					}
					
				} else {
					//si el token no es CTE -> es ID o CADENA
					//System.out.println(t.getLexema() + " DW ");//+ t.getLexema());
					if (t.getUso().equals("procedimiento")) {
						//nada. no lo agrego como sentencia declarativa al ID de un proc
						continue;
					} else {
						
						//si es ID o CADENA
						if (esPunteroPuntoFlotante(t.getLexema())) {
							out += t.getLexema() + " DQ ? \n" ;
						} else {
							out += t.getLexema() + " DD ? \n";
						}
					}
				}
			}
		}	
		
		return out;
	}
	
	
	public String getUltimaInstruccion() {
		return this.instrucciones_asmb.remove(this.instrucciones_asmb.size()-1);
	}
}
