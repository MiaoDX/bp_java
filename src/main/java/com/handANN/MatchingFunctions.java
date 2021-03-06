package com.handANN; /**
 * Created by miao on 2016/10/25.
 */

import java.util.function.Function;

/**
 * Some function for our tools to simulate.
 */
public class MatchingFunctions {
    public static Function<Double, Double> sinF = input -> Math.sin(input);

    public static Function<Double, Double> ThreeSinAddTwoCos = input -> 3 * Math.sin(input) + 2 * Math.cos(input);

    public static Function<Double, Double> OnePlusThreeSinAddTwoCos = input -> 1.0 + ThreeSinAddTwoCos.apply(input);

    public static Function<Double, Double> OnePlusSinPiX = input -> 1.0 + Math.sin(Math.PI * input);

    public static Function<Double, Double> FiveSinADDFourCosX = input -> 5.0*Math.sin(input) + 4.0*Math.cos(input);

    public static Function<Double, Double> OnePlusTwoSinPlusThreeExp = input -> 1.0 + 2.0*Math.sin(input) + 3.0*Math.exp(input);

    public static Function<Double, Double> SinAddCos = x -> Math.sin(x) + Math.cos(x);

}
