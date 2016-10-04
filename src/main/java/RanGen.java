import java.util.Random;

/**
 * Created by miao on 2016/10/4.
 */
public class RanGen implements IRanGen {

    private Random random = new Random();

    RanGen(int seed){
        random.setSeed(seed);
    }

    public double nextDouble() {
        return random.nextDouble();
    }
}
