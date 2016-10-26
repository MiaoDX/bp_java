package com.handANN;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miao on 2016/10/24.
 */
public class RanGenProvided implements IRanGen {


    List<Double> list = new ArrayList<Double>();
    int nowIndex = -1;
    int maxNum = 0;


    public RanGenProvided(Double... args){
        int num = 0;
        for (Double arg : args) {
//            System.out.println(arg);
            list.add(arg);
            num += 1;
        }
        maxNum = num;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public boolean hasNext() {
        return nowIndex < maxNum - 1;
    }

    public double nextDouble() {
        nowIndex ++;
        return list.get(nowIndex);
    }
}
