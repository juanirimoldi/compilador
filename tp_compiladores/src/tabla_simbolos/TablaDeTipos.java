package tabla_simbolos;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Hashtable;

public class TablaDeTipos {
	//tabla de tokens unicos. contiene el indice de lo que le entrega al sintactico

	//con private -> no anda
	//sin final -> anda pero con warnings
	
	//#### end semantic value section ####
	public final static short ID=257;
	public final static short CTE=258;
	public final static short CADENA=259;
	public final static short IF=260;
	public final static short ELSE=261;
	public final static short END_IF=262;
	public final static short PROC=263;
	public final static short NI=264;
	public final static short REF=265;
	public final static short OUT=266;
	public final static short MAYORIGUAL=267;
	public final static short MENORIGUAL=268;
	public final static short IGUAL=269;
	public final static short DISTINTO=270;
	public final static short PUNT=271;
	public final static short EOF=272;
	public final static short YYERRCODE=256;
	public final static short INTEGER=273;
	public final static short FLOAT=274;
	public final static short LOOP=275;
	public final static short UNTIL=276;
	
	public int sgte_id = 300;
	
	
	private Hashtable<Integer, String> id_tipo;
	
	
	
	public TablaDeTipos() {
		
		this.id_tipo = new Hashtable<Integer, String> ();
		
		inicializarTokensDelParser();

		//this.mostrarTokens();
	}
	
	
	public void inicializarTokensDelParser() {
		try {
			this.cargarTablaConMacros();
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void cargarTablaConMacros() throws IllegalArgumentException, IllegalAccessException {
		
		Field[] fields = this.getClass().getFields();
		for (Field f : fields) {
			String id = f.getName();
			int ref = f.getInt(id);
			this.id_tipo.put(ref, id);
			//System.out.println("FIELDDD -> "+id+"  "+ref);
		    } 
		}

	
	public void addToken(String s) {
		//int id_tipo_token = this.getIdentificadorTipo(s);
		//System.out.println("AGREGO TOKEN -> ");
		if (existe(s)) {
			//System.out.println("EXISTE EL tipo de TOKEN "+s);
		}
		else {
			//System.out.println("NO existe el tipo de TOKEN, lo agrego "+s +"  "+sgte_id);
		//this.getIdentificadorTipo(s);
			this.id_tipo.put(sgte_id, s);
			sgte_id++;
		}
	}
	
	
	
	
		
	
	public boolean existe(String b) {
		//this.mostrarTokens();
		//System.out.println("Existe " + b + " en la Hash???? "+this.id_tipo.contains(b));
		return this.id_tipo.contains(b);
	}
	
	
	public int getIdTipo(String s) {
		//recorro la hash y busco key del tipo de token, para devolver a yylex
		
		int id_tipo = 0;
		
		Enumeration enumeration_keys = this.id_tipo.keys();
		Enumeration enumeration = this.id_tipo.elements();
		
		
		while (enumeration_keys.hasMoreElements()) {
			int key = (int)enumeration_keys.nextElement();
			String id_token = (String)enumeration.nextElement();
			
			if (id_token.equals(s)) {
				//System.out.println("Existe el tipo en la Tabla de Hash");
				id_tipo = key;
			}
		}
		return id_tipo;
	}
	
	
	
	public void mostrarTokens() {
		System.out.println("Tabla de tokens unicos \n");

		System.out.println("id_token, lexema	");
		Enumeration enumeration_keys = this.id_tipo.keys();
		
		Enumeration enumeration = this.id_tipo.elements();
		while (enumeration.hasMoreElements()) {
			System.out.println(enumeration_keys.nextElement() + " 	, " + enumeration.nextElement());
		}
		System.out.println("\n");
	}
}
