package analizador_sintactico;

public class TablaRegistros {
	//registros inicializados como libres
	private boolean EAX = true;
	private boolean EBX = true;
	private boolean ECX = true;
	private boolean EDX = true;
	
	
	public TablaRegistros() {
		
	}
	
	public int getRegistroLibre() {
		//int reg_libre=0;
		
		if (EAX == true) {
			EAX=false;
			//System.out.println("\n ASIGNO registro 1 = EAX \n");
			return 1; //reg_libre=1;
		}
		if (EBX == true) {
			EBX = false;
			//System.out.println("\n ASIGNO registro 2 = EBX \n");
			return 2; 	//reg_libre=2;
		}
		if (this.ECX == true) {
			ECX=false;
			//System.out.println("\n ASIGNO registro 3 = ECX \n");
			return 3; //reg_libre=3;
		}
		if (this.EDX == true) {
			EDX=false;
			//System.out.println("\n ASIGNO registro 4 = EDX \n");
			return 4; //reg_libre=4;
		}
		
		return 0;
	}
	
	
	
	public void liberarRegistro(int i) {
		if (i == 1) {
			this.EAX = true;
			//System.out.println("\n LIBERO registro 1 \n");
		}
		
		if (i == 2) {
			this.EBX = true;
			//System.out.println("\n LIBERO registro 2 \n");
		}
		
		if (i == 3) {
			this.ECX = true;
			//System.out.println("\n LIBERO registro 3 \n");
		}
		
		if (i == 4) {
			this.EDX = true;
			//System.out.println("\n LIBERO registro 4 \n");
		}
	}
	
	
	public String getNombreReg(int i) {
		String out = "";
		
		if (i == 1) {
			out = "EAX";
		}
		
		if (i == 2) {
			out = "EBX";
		}
		
		if (i == 3) {
			out = "ECX";
		}
		
		if (i == 4) {
			out = "EDX";
		}

		return out;
	}
	
	
	public void mostrarRegistrosLibres() {
		String line = "";
		
		if (this.EAX) { line += " EAX "; };
		if (this.EBX) { line += " EBX "; };
		if (this.ECX) { line += " ECX "; };
		if (this.EDX) { line += " EDX "; };
		
		//System.out.println("Registros libres -> "+line);
		System.out.println();
	}
}
