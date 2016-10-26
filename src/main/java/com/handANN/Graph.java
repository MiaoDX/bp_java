package com.handANN;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Table;

import java.util.*;

/**
 * Created by miao on 2016/10/4.
 */
public class Graph {


    private Table<Point, Point, Double> weightedGraph = HashBasedTable.create();
    private List<Point> pointsInOrder = new ArrayList<Point>();
    private List<Point> pointsInReversedOrder = new ArrayList<Point>();

    private double input; // the output of the first node
    private double output;
    private double target; // target output of the last node
    private double error; //target - output
    private double learnRatio = 0.05;

    /**
     *
     * @param layerNum, each element present the number of point of given layer
     *                  [1,4,1] means first layer has 1 point,second layer has 4 points,...
     */
    public Graph(List<Integer> layerNum){
        weightedGraph.clear();

        for(int leftLayer = 0; leftLayer < layerNum.size() - 1; leftLayer ++){
            for(int leftNum = 0; leftNum < layerNum.get(leftLayer); leftNum ++){
                Point leftPoint = new Point(leftLayer, leftNum);
                int rightLayer = leftLayer + 1;

                for(int rightNum = 0; rightNum < layerNum.get(rightLayer); rightNum ++){
                    Point rightPoint = new Point(rightLayer, rightNum);
                    weightedGraph.put(leftPoint, rightPoint, 0.0);
                }
            }
        }
    }


    /**
     *
     * @param pointPair the connections of points(p00-p10,p00-011,...)
     * @param pointsInOrder
     * @param ranGen
     */
    public Graph(LinkedHashMultimap<Point, Point> pointPair, List<Point> pointsInOrder, IRanGen ranGen) {
        weightedGraph.clear();
        for(Map.Entry<Point, Point> m : pointPair.entries()){
            //System.out.println(m.getKey() + "" + m.getValue());
            weightedGraph.put(m.getKey(), m.getValue(),ranGen.nextDouble());
        }

        for(Point m : pointsInOrder){
            m.setBias(ranGen.nextDouble());
        }

        this.pointsInOrder = pointsInOrder;
        this.pointsInReversedOrder.addAll(pointsInOrder);
        Collections.reverse(this.pointsInReversedOrder);
    }

    /**
     * OpenClose principle,to provide learnRatio para.
     */
    public Graph(LinkedHashMultimap<Point, Point> pointPair, List<Point> pointsInOrder, IRanGen ranGen, double learnRatio) {
        this(pointPair, pointsInOrder, ranGen); //no learnRation version
        this.learnRatio = learnRatio;
    }


    /**
     * Since when forward and backward we don't deal with the firstPoint,so when update the graph,we set the value properly.
     * @param input
     * @param target
     */
    public void updateGraph(double input, double target){
        Point firstPoint = pointsInOrder.get(0);
//        pointsInOrder.get(0).setInput(input);
//        pointsInOrder.get(0).setOutput(input);
        firstPoint.setInput(input);
        firstPoint.setOutput(firstPoint.getActivationF().apply(input));
        this.input = input;
        this.target = target;
    }


    public void train(){
        this.forward();
        this.backwardTheta();
        this.backwardWeight();
    }


    public void beautifyWeightedGraphOutput(){
        for(Point nowPoint: pointsInOrder){
            System.out.println("NowPoint: "+nowPoint);

            for(Map.Entry<Point,Double> after:weightedGraph.row(nowPoint).entrySet()){
                System.out.println("\t" + "afterPoint: " + after.getKey() +";weight: " + after.getValue());
            }
        }
    }



    public void forward(){

        //for(Point nowPoint : weightedGraph.columnKeySet()){
        for(Point nowPoint : pointsInOrder.subList(1,pointsInOrder.size())){
            //System.out.println(nowPoint);
            double nowPointInput = 0.0;
            for(Map.Entry<Point, Double> previous : weightedGraph.column(nowPoint).entrySet()){
                //System.out.println(previous.getKey() + ":" + previous.getValue());
                Point previousPoint = previous.getKey();
                double weight = weightedGraph.get(previousPoint, nowPoint);
                nowPointInput += previousPoint.getOutput()*weight;
            }

            nowPointInput += nowPoint.getBias();
//            double nowPointOutput = activationF(nowPointInput);
            double nowPointOutput = nowPoint.getActivationF().apply(nowPointInput);
            nowPoint.setInput(nowPointInput);
            nowPoint.setOutput(nowPointOutput);
        }

        output = pointsInReversedOrder.get(0).getOutput();
        error = target - output;
    }


    /**
     * back the error(theta)
     */
    public void backwardTheta(){

        double theta = error;
        Point lastPoint = pointsInReversedOrder.get(0);
//        lastPoint.setTheta(differentiationF(lastPoint.getInput())*theta);
        lastPoint.setTheta(lastPoint.getDifferentiationF().apply(lastPoint.getInput())*theta);

        for(Point nowPoint : pointsInReversedOrder.subList(1, pointsInReversedOrder.size())){
            //System.out.println(nowPoint + ":" + nowPoint.getTheta());
            double nowPointTheta = 0;

            for(Map.Entry<Point, Double> after: weightedGraph.row(nowPoint).entrySet()){
                Point afterPoint = after.getKey();
                double weight = weightedGraph.get(nowPoint, afterPoint);
                nowPointTheta += afterPoint.getTheta()*weight;
            }

//            nowPointTheta = nowPointTheta*differentiationF(nowPoint.getInput());
            nowPointTheta = nowPoint.getDifferentiationF().apply(nowPoint.getInput())*nowPointTheta;

            nowPoint.setTheta(nowPointTheta);
        }
    }

    /**
     * As the http://galaxy.agh.edu.pl/%7Evlsi/AI/backp_t_en/backprop.html ,back or forward is the same
     */
    public void backwardWeight(){
        for(Point nowPoint : pointsInReversedOrder.subList(1, pointsInReversedOrder.size())){
            //System.out.println(nowPoint + ":" + nowPoint.getTheta());

            for(Map.Entry<Point, Double> after: weightedGraph.row(nowPoint).entrySet()){
                Point afterPoint = after.getKey();
                updateWeight(nowPoint, afterPoint);
            }
        }

        updateBias();
    }



    public void updateWeight(Point nowPoint, Point afterPoint){
        double nowWeight = weightedGraph.get(nowPoint, afterPoint);

        double newWeight = nowWeight + learnRatio*afterPoint.getTheta()*
                nowPoint.getOutput() ;

        weightedGraph.put(nowPoint, afterPoint, newWeight);
    }

    public void updateBias(){
        for(Point p : pointsInOrder){
            double newBias = p.getBias() + learnRatio*p.getTheta();
            p.setBias(newBias);
        }
    }


    public double differentiationF(double input){
        double sig = ActivationFuntions.sigmoid(input);
        return sig*(1-sig);
    }




    public double activationF(double input){
        return ActivationFuntions.sigmoid(input);
    }




    public Table<Point, Point, Double> getWeightedGraph() {
        return weightedGraph;
    }


    public double getOutput() {
        return output;
    }

    public double getError() {
        return error;
    }


    @Override
    public String toString(){
        String rtn = "";
        for(Table.Cell<Point, Point, Double> cell: weightedGraph.cellSet() ){
            rtn += cell.getRowKey() + "," + cell.getColumnKey() + ":" + cell.getValue() + "\n";
        }
        return rtn;
    }

    public String print_out_points(){
        String rtn = "";
        for(Point p : pointsInOrder){
            rtn += p + "\n";
        }
        return rtn;
    }



}
