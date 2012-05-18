import java.awt.event.*;

public abstract class Steuerung {
	public static int [][] keys=null;

	// festlegung der Spieler (erstmal nur einen ^^)
	public static final int P1=0; {
	
	//festlegung der Tastenanzahl
	final int UP=0;
	final int DOWN=1;
	final int LEFT=2;
	final int RIGHT=3;
	final int BOMB=4;
	
	//festlegung der Tastenbelegung
	keys[P1][UP]=KeyEvent.VK_W;
	keys[P1][DOWN]=KeyEvent.VK_S;
	keys[P1][LEFT]=KeyEvent.VK_A;
	keys[P1][RIGHT]=KeyEvent.VK_D;
	keys[P1][BOMB]=KeyEvent.VK_SPACE;
	}
}
