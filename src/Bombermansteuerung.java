import java.awt.event.KeyEvent;
import java.io.*;

// Die Steuerung für alle Spieler

public abstract class Bombermansteuerung {
    // 
    public static int[][] keys = null;
    // Aufzählung der Spieler
    public static final int P1 = 0;
    public static final int P2 = 1;
    public static final int P3 = 2;
    public static final int P4 = 3;
  // Aufzählung der Stuerungselemente
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int BOMB = 4;

    // Steuerung öffnen
    public static void InitConrols(){
        // erstelle ein Objekt:
    	File file = new File("BomberKeyConfig.dat");
// wenn file nicht exestiert
        if (!file.exists())
        {
           // DefaultFile erstellen
            createDefaultFile();
            // erneut versuchen es zu öffnen
            openFile();
        }
    }

   // öffne konfiguration
    public static boolean openFile() {
        // setup result to be returned 																									?????????????????????????
        boolean result = true;
        // versuche File zu öffnen
        try {
            //  Fileinput um Datei zu lesen																							
            FileInputStream fis = new FileInputStream("Bombermansteuerung.dat");
            // durchläuft array keys
            for(int i = 0; i < keys.length; i++){
            	for(int j = 0; j < keys[i].length; j++){
            		// lesen von gespeicherten tasten 
            		keys[i][j] = fis.read();
            	}
            }
            // file schließen
            fis.close(); 
        }
        //  wenn etwas falsch läuft, setze result auf falsch  
        catch (Exception e) {
            result = false;
        }
        // return result
        return result;
    }
    
     // 	damit Tastenbelegung geändert werden kann
     
    public static void writeFile() {
        // versuche File zu erstellen
        try {
            // erstelle file objekt 
            FileOutputStream fos = new FileOutputStream("Bombermansteuerung.dat");
            // schreibe file
            for(int i = 0; i < keys.length; i++){
            	for(int j = 0; j < keys[i].length; j++){
            		// hier werden die tasten in die datei geschrieben die oben gelesen werden
            		fos.write(keys[i][j]);
            	}
            }
            // schließe file
            fos.close();
        }
        catch (Exception e) { new ErrorDialog(e); }
    }

    // erstelle default file
    public static void createDefaultFile() {
        // 
        if (keys == null) keys = new int[4][5];

        // Steuerng Spieler 1
        keys[P1][UP] = KeyEvent.VK_W;
        keys[P1][DOWN] = KeyEvent.VK_S;
        keys[P1][LEFT] = KeyEvent.VK_A;
        keys[P1][RIGHT] = KeyEvent.VK_D;
        keys[P1][BOMB] = KeyEvent.VK_SPACE;

        // Steuerung Spieler 2
        keys[P2][UP] = KeyEvent.VK_UP;
        keys[P2][DOWN] = KeyEvent.VK_DOWN;
        keys[P2][LEFT] = KeyEvent.VK_LEFT;
        keys[P2][RIGHT] = KeyEvent.VK_RIGHT;
        keys[P2][BOMB] = KeyEvent.VK_NUMPAD0;

        // Steuerung Spieler 3
        keys[P3][UP] = KeyEvent.VK_I;
        keys[P3][DOWN] = KeyEvent.VK_K;
        keys[P3][LEFT] = KeyEvent.VK_J;
        keys[P3][RIGHT] = KeyEvent.VK_L;
        keys[P3][BOMB] = KeyEvent.VK_O;

        // Steuerung Spieler 4
        keys[P4][UP] = KeyEvent.VK_NUMPAD8;
        keys[P4][DOWN] = KeyEvent.VK_NUMPAD5;
        keys[P4][LEFT] = KeyEvent.VK_NUMPAD4;
        keys[P4][RIGHT] = KeyEvent.VK_NUMPAD6;
        keys[P4][BOMB] = KeyEvent.VK_NUMPAD9;

        // schreibe file
        writeFile();
    }
}