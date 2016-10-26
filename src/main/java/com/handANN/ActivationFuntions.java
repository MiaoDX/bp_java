package com.handANN;

import java.util.function.Function;

/**
 * Created by miao on 2016/10/4.
 */
public class ActivationFuntions {

    /*   Some activation functions and their differentiation function   */
    public static double sigmoid(double input){
        return 1.0/(1+Math.exp(-input));
    }

    public static Function<Double,Double> sigmoidLambda = input -> sigmoid(input);

    public static Function<Double,Double> sigmoidDifferentiationLambda = input -> sigmoid(input)*(1-sigmoid(input));

    public static Function<Double,Double> halfLambda = input -> input/2;

    public static Function<Double,Double> halfDifferentiationLambda = input -> 0.5;

    public static Function<Double,Double> sameLambda = input -> input;

    public static Function<Double,Double> sameDifferentiationLambda = input -> 1.0;






    public static Function<Integer,Integer> fortest = input -> input*2;







    ActivationFuntions(){};
}
