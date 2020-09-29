package tp_compiladores;

public class EntregarTokenYReiniciar extends AccionSemantica {
	private String tt = "";
	
	public void ejecutar(char c, String s) {
		this.tt = super.buffer;
		System.out.println("ASF -> Entregar token, reinicio estado y buffer"+this.tt);
		super.buffer = "";
	}
	
	public String getToken() {
		//String token = super.buffer;
		//super.buffer = "";
		return this.tt;
	}
}
