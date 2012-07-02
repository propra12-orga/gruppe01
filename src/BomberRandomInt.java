
public class BomberRandomInt {
// zum zufälligem integer generieren
    private int low = 0; // kleinster int
    private int high = 0; // größter int

// stack größe
    private static final int BUFFER_SIZE = 101;
    private static double[] buffer = new double[BUFFER_SIZE]; // stack für zufällige doubles
    // stack mit zufälligen doubles füllen
    static {
        for (int i = 0; i < BUFFER_SIZE; i++)
            buffer[i] = java.lang.Math.random();
    }
    // konstruiere objekt um zufällige Int's in der gegeben reichweite zu generieren
    public BomberRandomInt(int low, int high) {
        this.low = low;
        this.high = high;
    }
    // nehme random double und mache daraus einen int
    public int draw() {
        int result = low + (int)((high - low + 1) * nextRandom());
        return result;
    }

    private static double nextRandom() {

        int position = (int)(java.lang.Math.random() * BUFFER_SIZE);
        if (position == BUFFER_SIZE) // wenn die position gleich der buffersize ist
            position = BUFFER_SIZE - 1; // position buffer size -1
        double result = buffer[position]; // result = buffer position
        buffer[position] = java.lang.Math.random();
        return result; // result returnen
    }
}