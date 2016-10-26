package com.handANN;

import java.util.Random;

/**
 * Created by miao on 2016/10/4.
 */
public class RanGen implements IRanGen {

    private Random random = new Random();

    public RanGen(){}  //no seed,use the default value(Current time)

    public RanGen(int seed){
        random.setSeed(seed);
    }

    public int getMaxNum() {
        return Integer.MAX_VALUE;
    }

    public boolean hasNext() {
        return true;
    }

    public double nextDouble() {
        return random.nextDouble();
    }
}
