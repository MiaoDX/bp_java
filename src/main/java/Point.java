import com.alibaba.fastjson.JSON;

import java.util.function.Function;

/**
 * Created by miao on 2016/10/4.
 */
public class Point {
    private int layer;
    private int number;
    private double theta;
    private double input;
    private double output;

    private Function<Double, Double> activationF = ActivationFuntions.sigmoidLambda;
    private Function<Double, Double> differentiationF = ActivationFuntions.sigmoidDifferentiationLambda;

    Point(int layer, int number){
        this.layer = layer;
        this.number = number;
    }




    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }




    public static void main(String[] args) {
        Point point = new Point(1,2);
        System.out.println(point);


    }

    public int getLayer() {
        return layer;
    }

    public int getNumber() {
        return number;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getInput() {
        return input;
    }

    public void setInput(double input) {
        this.input = input;
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public Function<Double, Double> getDifferentiationF() {
        return differentiationF;
    }

    public void setDifferentiationF(Function<Double, Double> differentiationF) {
        this.differentiationF = differentiationF;
    }

    public Function<Double, Double> getActivationF() {
        return activationF;
    }

    public void setActivationF(Function<Double, Double> activationF) {
        this.activationF = activationF;
    }
}
