/**
 * Created by miao on 2016/10/25.
 */

import java.util.function.Function;

/**
 * Some function for our tools to simulate.
 */
public class MatchingFunctions {
    public static Function<Double, Double> sinF = input -> Math.sin(input);

    public static Function<Double, Double> sin3AddCos2F = input -> 3*Math.sin(input)+2*Math.cos(input);

}
