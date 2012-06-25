
public class BomberRandInt {

    private int low = 0;
    private int high = 0;


    private static final int BUFFER_SIZE = 101;
    private static double[] buffer = new double[BUFFER_SIZE];

    static {
        for (int i = 0; i < BUFFER_SIZE; i++)
            buffer[i] = java.lang.Math.random();
    }

    public BomberRandInt(int low, int high) {
        this.low = low;
        this.high = high;
    }

    public int draw() {
        int result = low + (int)((high - low + 1) * nextRandom());
        return result;
    }

    private static double nextRandom() {

        int position = (int)(java.lang.Math.random() * BUFFER_SIZE);
        if (position == BUFFER_SIZE)
            position = BUFFER_SIZE - 1;
        double result = buffer[position];
        buffer[position] = java.lang.Math.random();
        return result;
    }
}