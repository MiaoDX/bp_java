package com.handANNTest;

import com.handANN.FunctionFaux;
import com.handANN.IRanGen;
import com.handANN.MatchingFunctions;
import com.handANN.RanGen;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by miao on 2016/10/4.
 */
public class FunctionFauxTest {
    @Test
    public void FunctionFaux(){
        //IRanGen ranGen = new RanGenFaux();
        IRanGen ranGen = new RanGen(0);
        int num = 100;

        Function<Double, Double> f = MatchingFunctions.sinF;

        FunctionFaux functionFaux = new FunctionFaux(ranGen,f,-Math.PI,Math.PI,num);

//        FunctionFaux functionFaux = new FunctionFaux(ranGen,f,-1.0,1.0,num);

        for(int i = 0;i < num; i ++){
            //System.out.println(functionFaux.getRandomNextInputAndTarget());
        }

        List<Double> domainValues = functionFaux.getDomainValues();
        List<Double> functionValues = functionFaux.getFunctionValues();

        for(int i = 0;i < num;i ++){
            System.out.println(domainValues.get(i) + ":" + functionValues.get(i));
            assertThat(f.apply(domainValues.get(i))).isEqualTo(functionValues.get(i));
        }

    }


    @Test
    public void testRandom(){
        double a = -10,b = 10;

        Random r = new Random();

        Function<Integer, Double> rangeRandom = input -> r.nextDouble()*(b-a)+a;

        for(int i = 0;i < 1000000;i ++){
//            System.out.println(rangeRandom.apply(i));
            assertThat(rangeRandom.apply(i)).isBetween(a,b);

        }

    }

}
