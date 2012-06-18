
public class Spielfigur {
	public int x = 1; //x-Koordinate
	public int y = 1; //y-Koordinate
	
	public Spielfigur(int x,int y){
		this.x = x;
		this.y = y;
	}
	
	public void setSpielerposition(){
		
	}
	
	/**
	 * aktuelle x-Koordinate
	 */
	public int get_x(){
		return x;
	}
	
	/**
	 * x-Koordinate wird aktualisiert
	 */
	public void set_x(int x){
		this.x = x;
	}
	
	/**
	 * aktuelle y-Koordinate
	 */
	public int get_y(){
		return y;
	}
	
	/**
	 * y-Koordinate wird aktualisiert
	 */
	public void set_y(int y){
		this.y = y;
	}

}
