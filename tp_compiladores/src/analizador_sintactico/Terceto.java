package analizador_sintactico;

public class Terceto {
	private int id;
	private String operador;
	private String operando1;
	private String operando2;

	
	public Terceto(int id, String op, String op1, String op2) {
		this.id=id;
		this.operador=op;
		this.operando1=op1;
		this.operando2=op2;
	}
	
	
	public int getId() {
		return this.id;
	}
	
	
	public String getOperador() {
		return this.operador;
	}
	
	
	public String getOperando1() {
		return this.operando1;
	}
	
	
	public String getOperando2() {
		return this.operando2;
	}
	
	
	public void setOperando1(String val) {
		this.operando1=val;
	}
	
	public void setOperando2(String val) {
		this.operando2=val;
	}
}
