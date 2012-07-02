import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Vector;

/**
 * Diese Klasse generiert die Karte und behandelt Dinge wie Bonuslevel und Bomben
 */
public class BomberMap extends JPanel {

	private BomberMain main = null;
    
    private boolean gameOver = false;

    private Color backgroundColor = null;

    public int[][] grid = null;

    public boolean[][] fireGrid = null;
    public BomberBomb[][] bombGrid = null;
    public BomberBonus[][] bonusGrid = null;
    private Vector bombs = null;

    private Vector bonuses = null;

    /**
     * Bomben Infoklasse
     */
    private class Bomb {
        public Bomb(int x, int y) {
            r = (x >> BomberMain.shiftCount);
            c = (y >> BomberMain.shiftCount);
        }
        public int r = 0;
        public int c = 0;
    }

    /**
     * Bonus Infoklasse
     */
    private class Bonus {
        public Bonus(int x, int y) {
            r = (x >> BomberMain.shiftCount);
            c = (y >> BomberMain.shiftCount);
        }
        public int r = 0;
        public int c = 0;
    }

    /** Kartenbilder */
    private static Image[][] mapImages = null;
    /** Bombenbild */
    public static Image[] bombImages = null;
    /** Feuerbild */
    public static Image[][] fireImages = null;
    /** Feuerblock Bild */
    public static Image[][] fireBrickImages = null;
    /** Bonus Bild */
    public static Image[][] bonusImages = null;
    /** Feuerausrichtungen */
    public static final int FIRE_CENTER = 0;
    public static final int FIRE_VERTICAL = 1;
    public static final int FIRE_HORIZONTAL = 2;
    public static final int FIRE_NORTH = 3;
    public static final int FIRE_SOUTH = 4;
    public static final int FIRE_EAST = 5;
    public static final int FIRE_WEST = 6;
    public static final int FIRE_BRICK = 7;

    public static final int BONUS_FIRE = -4;
    public static final int BONUS_BOMB = -3;
    public static final int NOTHING = -1;
    public static final int WALL = 0;
    public static final int BRICK = 1;
    public static final int BOMB = 3;
    /** Levelgenerator */
    private static BomberRandInt levelRand = null;
    /** Bonusgenerator */
    private static BomberRandInt bonusRand = null;
    /** jetziges Level */
    public static int level = 0;
    /** Hinweise */
    private static Object hints = null;

    static {
        if (Main.J2) {
            RenderingHints h = null;
            h = new RenderingHints(null);
            h.put(RenderingHints.KEY_TEXT_ANTIALIASING,
             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            h.put(RenderingHints.KEY_FRACTIONALMETRICS,
             RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            h.put(RenderingHints.KEY_ALPHA_INTERPOLATION,
             RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            h.put(RenderingHints.KEY_ANTIALIASING,
             RenderingHints.VALUE_ANTIALIAS_ON);
            h.put(RenderingHints.KEY_COLOR_RENDERING,
             RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            hints = (RenderingHints)h;
        }

        /** Levelgenerator */
        levelRand = new BomberRandInt(0, 100);
        bonusRand = new BomberRandInt(0, 7);
        mapImages = new Image[3][3];
        bombImages = new Image[2];
        fireImages = new Image[8][8];
        fireBrickImages = new Image[3][8];
        bonusImages = new Image[2][2];

        try {
            String[] strs = new String[3];
            /** laden von Kartentexturen */
            for (int i = 0; i < 2; i++) {
                strs[0] = BomberMain.RP + "Images/BomberWalls/" + (i + 1);
                strs[1] = BomberMain.RP + "Images/BomberBricks/" + (i + 1);
                strs[2] = BomberMain.RP + "Images/BomberFloors/" + (i + 1);
                for (int j = 0; j < 3; j++) {
                    if (i == 0) strs[j] += ".jpg";
                    else strs[j] += ".gif";
                }
                mapImages[i][0] = Toolkit.getDefaultToolkit().getImage(
                new File(strs[0]).getCanonicalPath());
                mapImages[i][1] = Toolkit.getDefaultToolkit().getImage(
                new File(strs[1]).getCanonicalPath());
                if (i == 0) mapImages[i][2] = null;
                else
                    mapImages[i][2] = Toolkit.getDefaultToolkit().getImage(
                    new File(strs[2]).getCanonicalPath());
            }

            String str = null;
            /** laden von Bombentexturen */
            for (int i = 0; i < 2; i++) {
                str = BomberMain.RP + "Images/BomberBombs/" + (i + 1) + ".gif";
                bombImages[i] = Toolkit.getDefaultToolkit().getImage(
                new File(str).getCanonicalPath());
            }

            /** laden der Sprengungstexturen */
            for (int t = 0; t < 7; t++) for (int i = 0; i < 8; i++)
            {
                str = BomberMain.RP + "Images/BomberFires/";
                if (t == FIRE_CENTER) str += "C";
                else if (t == FIRE_VERTICAL) str += "V";
                else if (t == FIRE_NORTH) str += "N";
                else if (t == FIRE_HORIZONTAL) str += "H";
                else if (t == FIRE_EAST) str += "E";
                else if (t == FIRE_WEST) str += "W";
                else if (t == FIRE_SOUTH) str += "S";
                if (t == FIRE_BRICK) fireImages[t][i] = null;
                else {
                    str += (i + 1) + ".gif";
                    fireImages[t][i] = Toolkit.getDefaultToolkit().getImage(
                    new File(str).getCanonicalPath());
                }
            }

            int f = 0;
            
            /** laden der Sprengbloeke */
            for (int i = 0; i < 2; i++) for (f = 0; f < 8; f++)
            {
                str = BomberMain.RP + "Images/BomberFireBricks/" +
                (i + 1) + (f + 1) + ".gif";
                fireBrickImages[i][f] = Toolkit.getDefaultToolkit().getImage(
                new File(str).getCanonicalPath());
            }

            /** laden der Bonusbloeke */
            for (int i = 0; i < 2; i++) for (f = 0; f < 2; f++)
            {
                str = BomberMain.RP + "Images/BomberBonuses/" +
                (i == 0 ? "F" : "B") + (f + 1) + ".gif";
                bonusImages[i][f] = Toolkit.getDefaultToolkit().getImage(
                new File(str).getCanonicalPath());
            }
        }
        catch (Exception e) { new ErrorDialog(e); }
    }

    public BomberMap(BomberMain main) {
        this.main = main;
        level = levelRand.draw() % 2;
        MediaTracker tracker = new MediaTracker(this);
        
        try
        {
            int counter = 0;
            /** laden der Kartentexturen */
            for (int i = 0; i < 2; i++) for (int j = 0; j < 3; j++) {
                if (mapImages[i][j] != null)
                { tracker.addImage(mapImages[i][j], counter++); }
            }
            /** laden der Bombentexturen */
            for (int i = 0; i < 2; i++)
                tracker.addImage(bombImages[i], counter++);
            /** laden der Sprengblock Texturen */
            for (int i = 0; i < 8; i++)
                fireImages[FIRE_BRICK][i] = fireBrickImages[level][i];
            /** laden der Sprengungstexturen */
            for (int i = 0; i < 8; i++) for (int j = 0; j < 8; j++)
                tracker.addImage(fireImages[i][j], counter++);

            /** warte bis alles geladen hat */
            tracker.waitForAll();
        } catch (Exception e) { new ErrorDialog(e); }

        bombs = new Vector();
        bonuses = new Vector();
        /** generiert Feuerbloeke */
        fireGrid = new boolean[17][17];
        /** generiert Bombenbloecke */
        bombGrid = new BomberBomb[17][17];
        /** generiert Bonusbloeke */
        bonusGrid = new BomberBonus[17][17];
        /** generiert Kartenbloeke */
        grid = new int[17][17];
        /** fuellt die Karte mit Mauern mit r von c */
        for (int r = 0; r < 17; r++) for (int c = 0; c < 17; c++) {
            /** bei einer Ecke */
            if (r == 0 || c == 0 || r == 16 || c == 16) grid[r][c] = WALL;
            else if ( (r & 1) == 0 && (c & 1) == 0 ) grid[r][c] = WALL;
            else grid[r][c] = NOTHING;
            fireGrid[r][c] = false;
            bombGrid[r][c] = null;
            bonusGrid[r][c] = null;
        }

        int x, y;
        BomberRandInt ri = new BomberRandInt(1, 15);
        /** generiert bombenbloeke */
        for (int i = 0; i < 192 * 2; i++)
        {
            x = ri.draw();
            y = ri.draw();
            if (grid [x][y] == NOTHING)
               grid [x][y] = BRICK;
        }

        /** ecken werden geloescht fuer spieler */
        grid [ 1][ 1] = grid [ 2][ 1] = grid [ 1][ 2] =
        grid [ 1][15] = grid [ 2][15] = grid [ 1][14] =
        grid [15][ 1] = grid [14][ 1] = grid [15][ 2] =
        grid [15][15] = grid [15][14] = grid [14][15] = NOTHING;

        /** Hintergrundfarbe festlegen */
        backgroundColor = new Color(52, 108, 108);
        /** set panel size */
        setPreferredSize(new Dimension(17 << BomberMain.shiftCount,
        17 << BomberMain.shiftCount));
        /** double buffer on */
        setDoubleBuffered(true);

        setBounds(0, 0, 17 << main.shiftCount, 17 << main.shiftCount);
        setOpaque(false);

        main.getLayeredPane().add(this, 1000);
    }

    /**
     * Game over 
     */
     public void setGameOver() {
        gameOver = true;
        paintImmediately(0, 0,
        17 << BomberMain.shiftCount, 17 << BomberMain.shiftCount);
     }

     /**
      * generiert den Bonus.
      * @param x x-koordinate
      * @param y y-koordinate
      * @param owner owner
      */
    public synchronized void createBonus(int x, int y) {
        int _x = (x >> BomberMain.shiftCount) << BomberMain.shiftCount;
        int _y = (y >> BomberMain.shiftCount) << BomberMain.shiftCount;
        int type = bonusRand.draw();
        /** create bonus : 0 = fire; 1 = bomb */
        if (type == 0 || type == 1) {
           bonusGrid[_x >> BomberMain.shiftCount][_y >> BomberMain.shiftCount] =
           new BomberBonus(this, _x, _y, type);
           bonuses.addElement(new Bonus(_x, _y));
        }
    }

    /**
     * loescht den bonus.
     * @param x x-koordinate
     * @param y y-koordinate
     */
     public synchronized void removeBonus(int x, int y) {
        int i = 0, k = bonuses.size();
        int r = (x >> BomberMain.shiftCount);
        int c = (y >> BomberMain.shiftCount);
        Bonus b = null;
        while (i < k) {
            b = (Bonus)bonuses.elementAt(i);
            if (b.r == r && b.c == c) {
                bonuses.removeElementAt(i);
                bonusGrid[b.r][b.c].kill();
                bonusGrid[b.r][b.c] = null;
                paintImmediately(b.r << BomberMain.shiftCount,
                b.c << BomberMain.shiftCount, BomberMain.size,
                BomberMain.size);
                break;
            }
            i += 1;
            k = bonuses.size();
        }
     }

     /**
      * generiert eine bombe.
      * @param x x-koordinate
      * @param y y-koordinate
      * @param owner owner
      */
    public synchronized void createBomb(int x, int y, int owner) {
        int _x = (x >> BomberMain.shiftCount) << BomberMain.shiftCount;
        int _y = (y >> BomberMain.shiftCount) << BomberMain.shiftCount;
        bombGrid[_x >> BomberMain.shiftCount][_y >> BomberMain.shiftCount] =
        new BomberBomb(this, _x, _y, owner);
        bombs.addElement(new Bomb(_x, _y));
    }

    /**
     * loescht die bombe.
     * @param x x-koordinate
     * @param y y-koordinate
     */
     public synchronized void removeBomb(int x, int y) {
        int i = 0, k = bombs.size();
        int r = (x >> BomberMain.shiftCount);
        int c = (y >> BomberMain.shiftCount);
        Bomb b = null;
        while (i < k) {
            b = (Bomb)bombs.elementAt(i);
            if (b.r == r & b.c == c) {
                bombs.removeElementAt(i);
                break;
            }
            i += 1;
            k = bombs.size();
        }
     }

    /**
     * generiert Feuerblock.
     * @param x x-koordinate
     * @param y y-koordinate
     * @param owner owner
     * @param type fire type
     */
     public void createFire(int x, int y, int owner, int type)
     {
        int _x = (x >> BomberMain.shiftCount) << BomberMain.shiftCount;
        int _y = (y >> BomberMain.shiftCount) << BomberMain.shiftCount;
        boolean createFire = false;
        if (grid[_x >> BomberMain.shiftCount][_y >> BomberMain.shiftCount] ==
        BOMB) {
            if (bombGrid[_x >> BomberMain.shiftCount][_y
            >> BomberMain.shiftCount] != null)
            bombGrid[_x >> BomberMain.shiftCount][_y
            >> BomberMain.shiftCount].shortBomb();
        }
        /** wenn kein Feuer vorhanden */
        else if (!fireGrid[_x >>
        BomberMain.shiftCount][_y >> BomberMain.shiftCount]) {
            createFire = true;
            /** generiere Feuer */
            BomberFire f = new BomberFire(this, _x, _y, type);
        }
        /** Center */
        if (createFire && type == FIRE_CENTER) {
            int shiftCount = BomberMain.shiftCount;
            int size = BomberMain.size;
            int northStop = 0, southStop = 0, westStop = 0, eastStop = 0,
            northBlocks = 0, southBlocks = 0, westBlocks = 0, eastBlocks = 0;
            for (int i = 1; i <= BomberGame.players[owner].fireLength; i++) {
                if (southStop == 0) { if (((_y >> shiftCount) + i) < 17) {
                   if (grid[_x >> shiftCount][(_y >> shiftCount) + i] != WALL) {
                      if (grid[_x >> shiftCount][(_y >> shiftCount) + i]
                      != NOTHING)
                         { southStop = grid[_x >> shiftCount]
                            [(_y >> shiftCount) + i]; }
                      southBlocks += 1;
                    } else southStop = -1; }
                }
                /** Norden */
                if (northStop == 0) { if (((_y >> shiftCount) - 1) >= 0) {
                   if (grid[_x >> shiftCount][(_y >> shiftCount) - i] != WALL) {
                      if (grid[_x >> shiftCount][(_y >> shiftCount) - i]
                      != NOTHING)
                         { northStop = grid[_x >> shiftCount]
                            [(_y >> shiftCount) - i]; }
                      northBlocks += 1;
                      } else northStop = -1; }
                }
                /** Osten */
                if (eastStop == 0) { if (((_x >> shiftCount) + i) < 17) {
                   if (grid[(_x >> shiftCount) + i][_y >> shiftCount] != WALL) {
                      if (grid[(_x >> shiftCount) + i][_y >> shiftCount]
                      != NOTHING)
                         { eastStop = grid[(_x >> shiftCount) + i]
                            [_y >> shiftCount]; }
                      eastBlocks += 1;
                    } else eastStop = -1; }
                }
                /**Westen*/
                if (westStop == 0) { if (((_x >> shiftCount) - i) >= 0) {
                   if (grid[(_x >> shiftCount) - i][_y >> shiftCount] != WALL) {
                      if (grid[(_x >> shiftCount) - i]
                      [_y >> shiftCount] != NOTHING)
                         { westStop = grid[(_x >> shiftCount) - i]
                            [_y >> shiftCount]; }
                      westBlocks += 1;
                    } else westStop = -1; }
                }
            }
            for (int i = 1; i <= northBlocks; i++) {
                if (i == northBlocks) {
                   if (northStop == BRICK)
                      createFire(_x, _y - (i * size), owner, FIRE_BRICK);
                   else createFire(_x, _y - (i * size), owner, FIRE_NORTH);
                }
                else createFire(_x, _y - (i * size), owner, FIRE_VERTICAL);
            }
            for (int i = 1; i <= southBlocks; i++) {
                if (i == southBlocks) {
                    if (southStop == BRICK)
                       createFire(_x, _y + (i * size), owner, FIRE_BRICK);
                    else createFire(_x, _y + (i * size), owner, FIRE_SOUTH);
                }
                else createFire(_x, _y + (i * size), owner, FIRE_VERTICAL);
            }
            for (int i = 1; i <= eastBlocks; i++) {
                if (i == eastBlocks) {
                    if (eastStop == BRICK)
                       createFire(_x + (i * size), _y, owner, FIRE_BRICK);
                    else createFire(_x + (i * size), _y, owner, FIRE_EAST);
                }
                else createFire(_x + (i * size), _y, owner, FIRE_HORIZONTAL);
            }
            for (int i = 1; i <= westBlocks; i++) {
                if (i == westBlocks) {
                    if (westStop == BRICK)
                       createFire(_x - (i * size), _y, owner, FIRE_BRICK);
                    else createFire(_x - (i * size), _y, owner, FIRE_WEST);
                }
                /** Normales Feuer, wenn keine Kettenreaktion */
                else createFire(_x - (i * size), _y, owner, FIRE_HORIZONTAL);
            }
        }
     }

    /**
     * Methode zum Malen
     *
     */
     public synchronized void paint(Graphics graphics) {
        Graphics g = graphics;
        /** wenn java ist runtime Java 2 */
        if (Main.J2) { paint2D(graphics); }
        /** wenn java runtime ist nicht Java 2 */
        else {
            /** Wenn Game Over */
            if (gameOver) {
                /** Bildschirm schwarz */
                g.setColor(Color.black);
                g.fillRect(0, 0, 17 << BomberMain.shiftCount,
                17 << BomberMain.shiftCount);
            }
            /** wenn das Spiel noch nicht zuende ist */
            else {
                /** Hintergrundfarbe */
                g.setColor(backgroundColor);
                g.fillRect(0, 0, 17 << BomberMain.shiftCount,
                17 << BomberMain.shiftCount);
                /** draw the map */
                for (int r = 0; r < 17; r++) for (int c = 0; c < 17; c++) {
                    /** Wenn das Feld nicht frei ist */
                    if (grid[r][c] > NOTHING &&
                    grid[r][c] != BOMB && grid[r][c] != FIRE_BRICK &&
                    mapImages[level][grid[r][c]] != null) {
                        g.drawImage(mapImages[level][grid[r][c]],
                        r << BomberMain.shiftCount, c << BomberMain.shiftCount,
                        BomberMain.size, BomberMain.size, null);
                    }
                    /** Wenn das Feld leer ist */
                    else {
                        if (mapImages[level][2] != null) {
                           /** draw the floor */
                           g.drawImage(mapImages[level][2],
                           r << BomberMain.shiftCount, c <<
                           BomberMain.shiftCount, BomberMain.size,
                           BomberMain.size, null);
                        }
                    }
                }
            }
        }
        if (!gameOver) {
            /** generiert die Bomben */
            Bonus bb = null;
            int i = 0, k = bonuses.size();
            while (i < k) {
                bb = (Bonus)bonuses.elementAt(i);
                if (bonusGrid[bb.r][bb.c] != null)
                   bonusGrid[bb.r][bb.c].paint(g);
                i += 1;
                k = bonuses.size();
            }
            /** generiert die Bomben */
            Bomb b = null;
            i = 0; k = bombs.size();
            while (i < k)
            {
                b = (Bomb)bombs.elementAt(i);
                if (bombGrid[b.r][b.c] != null)
                   bombGrid[b.r][b.c].paint(g);
                i += 1;
                k = bombs.size();
            }
        }
     }

    /**
     * Methode für Java 2's Graphics2D
     * 
     */
    public void paint2D(Graphics graphics) {
        Graphics2D g2 = (Graphics2D)graphics;
        
        g2.setRenderingHints((RenderingHints)hints);
        /** Wenn Game Over */
        if (gameOver) {
            /** Bildschirm schwarz */
            g2.setColor(Color.black);
            g2.fillRect(0, 0, 17 << BomberMain.shiftCount,
            17 << BomberMain.shiftCount);
        }
        /** if das Spiel ist noch nicht vorbei */
        else {
            /** Hintergrundfarbe */
            g2.setColor(backgroundColor);
            g2.fillRect(0, 0, 17 << BomberMain.shiftCount,
            17 << BomberMain.shiftCount);
            /** draw the map */
            for (int r = 0; r < 17; r++) for (int c = 0; c < 17; c++) {
                /** wenn das Feld frei ist */
                if (grid[r][c] > NOTHING &&
                grid[r][c] != BOMB && grid[r][c] != FIRE_BRICK &&
                mapImages[level][grid[r][c]] != null) {
                    g2.drawImage(mapImages[level][grid[r][c]],
                    r << BomberMain.shiftCount, c << BomberMain.shiftCount,
                    BomberMain.size, BomberMain.size, null);
                }
                /** wenn das Feld leer ist */
                else {
                    if (mapImages[level][2] != null) {
                       /** generiert Hintergrund */
                       g2.drawImage(mapImages[level][2],
                       r << BomberMain.shiftCount, c <<
                       BomberMain.shiftCount, BomberMain.size,
                       BomberMain.size, null);
                    }
                }
            }
        }
    }
}