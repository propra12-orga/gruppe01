import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Integer;
import java.io.*;

/**
 * File:        BomberMain.java
 */

/**
 * Startpunkt des Spiels
 */
public class BomberMain extends JFrame {
    /** Entsprechender path für die Dateien */
    public static String RP = "./";
    /** Menü */
    private BomberMenu menu = null;
    /** Spiel */
    private BomberGame game = null;

    /** Spielersounds */
    public static BomberSndEffect sndEffectPlayer = null;
    /** Berechnung der Dimension des Spiels */
    public static final int shiftCount = 4;
    /**Größe pro Feld im Spiel */
    public static final int size = 1 << shiftCount;

    static {
        sndEffectPlayer = new BomberSndEffect();
    }

    /**
     * Konstruiert das Hauptger�st
     */
    public BomberMain() {
        /** Hinzufügen von Fenster-Ereignissteuerung */
        addWindowListener(new WindowAdapter() {
            /**
             * 
             * @param evt window event
             */
            public void windowClosing(WindowEvent evt) {
                /** Programmterminierung*/
                System.exit(0);
            }
        });

        /** Hinzufügen von Keys-Ereignisteuerung */
        addKeyListener(new KeyAdapter() {
            /**
             * Steuert Ereignisse bei Keys.
             * @param evt keyboard event
             */
            public void keyPressed(KeyEvent evt) {
                if (menu != null) menu.keyPressed(evt);
                if (game != null) game.keyPressed(evt);
            }

            /**
             * Steuerung ausgelöster Key Ereignisse.
             * @param evt keyboard event
             */
            public void keyReleased(KeyEvent evt) {
                if (game != null) game.keyReleased(evt);
            }
        });

		public void run() {

			Random random = new Random();
			int action = -1, x = 0, y = 0;

			while(true) {

				if((alive())
				   {

					do {
						action = (Math.abs(random.nextInt()) % BombermanController.BOMB);

						switch(action) {

							case BombermanController.UP:
								x = 0;
								y = -1;
								break;
							case BombermanController.DOWN:
								x = 0;
								y = +1;
								break;
							case BombermanController.LEFT:
								x = -1;
								y = 0;
								break;
							case BombermanController.RIGHT:
								x = +1;
								y = 0;
								break;
						}
					} while(arena[playerX[player_id] + x][0] != 0);
				}

				controller.key(BombermanController.UP, false);
				controller.key(BombermanController.DOWN, false);
				controller.key(BombermanController.LEFT, false);
				controller.key(BombermanController.RIGHT, false);
				controller.key(BombermanController.BOMB, false);
				if(alive()) controller.key(action, true);                                     

				
			}
		}

		private boolean alive() {

			return	(playerX[player_id] >= 1
				
		}

        /** Fenstertitel */
        setTitle("Bomberman Gruppe1");

        /** Fenstersymbol */
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(
                new File(RP + "Images/Bomberman.gif").getCanonicalPath()));
        }
        catch (Exception e) { new ErrorDialog(e); }

        /** Konstruieren und hinzufügen von Menü zum Gerüst */
        getContentPane().add(menu = new BomberMenu(this));

        /** Feste Fenstergröße um Änderung durch User zu verhindern */
        setResizable(false);
        /** Fensterminimierung */
        pack();

        /** Bildfläche erhalten */
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        int x = (d.width - getSize().width) / 2;
        int y = (d.height - getSize().height) / 2;

        /** Fenster auf Bildfläche zentrieren */
        setLocation(x, y);
        /** Gerüst zeigen */
        show();
        /** Fenster zum top level hervorheben */
        toFront();
    }

    /**
     * Neues Spiel erstellen.
     * @param players totale Anzahl Spieler
     */
    public void newGame(int players)
    {
        JDialog dialog = new JDialog(this, "Lade Spiel...", false);
        dialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        dialog.setSize(new Dimension(200, 0));
        dialog.setResizable(false);
        int x = getLocation().x + (getSize().width - 200) / 2;
        int y = getLocation().y + getSize().height / 2;
        dialog.setLocation(x, y);
        /** Dialog zeigen */
        dialog.show();

        /** Contentpane entfernen */
        getContentPane().removeAll();
        getLayeredPane().removeAll();
        /** Menü beseitigen */
        menu = null;
        /** Map erstellen*/
        BomberMap map = new BomberMap(this);

        /** Spiel erstellen */
        game = new BomberGame(this, map, players);

        /** Ladedialog beseitigen */
        dialog.dispose();
        /** Gerüst zeigen */
        show();
        /** if Java 2 verfügbar */
        if (Main.J2) {
           BomberBGM.unmute();
           /** Spielermusik */
           BomberBGM.change("Battle");
        }

			
		}
}

    /**
     * Startpunkt des Spiels
     * @param args Argumente
     */
    public static void main(String[] args) {
        BomberMain bomberMain1 = new BomberMain();
    }
}
