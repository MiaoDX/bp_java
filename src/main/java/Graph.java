import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multimap;
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
    private double target; // target output of the last node

    /**
     *
     * @param layerNum, each element present the number of point of given layer
     *                  [1,4,1] means first layer has 1 point,second layer has 4 points,...
     */
    Graph(List<Integer> layerNum){
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


        //init the values
        initGraph();
    }


    Graph(Multimap<Point, Point> pointPair, List<Point> pointsInOrder) {
        weightedGraph.clear();
        for(Map.Entry<Point, Point> m : pointPair.entries()){
            //System.out.println(m.getKey() + "" + m.getValue());
            weightedGraph.put(m.getKey(), m.getValue(),0.0);
        }

        this.pointsInOrder = pointsInOrder;
        this.pointsInReversedOrder.addAll(pointsInOrder);
        Collections.reverse(this.pointsInReversedOrder);
    }



    public void initGraph(){

        Random ranGen = new Random(0);//At first,make the random fixed

        for(Table.Cell<Point, Point, Double> cell: weightedGraph.cellSet() ){
            //weightedGraph.put(cell.getRowKey(), cell.getColumnKey(), ranGen.nextDouble());
            weightedGraph.put(cell.getRowKey(), cell.getColumnKey(), 0.5);
        }

        //System.out.println(this);
    }

    public void updateGraph(float input, float target){
        pointsInOrder.get(0).setOutput(input);
        this.input = input;
        this.target = target;
    }


    public void forward(){

        //for(Point nowPoint : weightedGraph.columnKeySet()){
        for(Point nowPoint : pointsInOrder.subList(1,pointsInOrder.size())){
            System.out.println(nowPoint);
            double nowPointInput = 0.0;
            for(Map.Entry<Point, Double> previous : weightedGraph.column(nowPoint).entrySet()){
                //System.out.println(previous.getKey() + ":" + previous.getValue());
                Point previousPoint = previous.getKey();
                double weight = previous.getValue();
                nowPointInput += previousPoint.getOutput()*weight;
            }

            double nowPointOutput = activationF(nowPointInput);
            nowPoint.setInput(nowPointInput);
            nowPoint.setOutput(nowPointOutput);
        }
    }


    /**
     * back the error(theta)
     */
    public void backwardTheta(double output){

        double theta = target - output;
        pointsInReversedOrder.get(0).setTheta(theta);

        for(Point nowPoint : pointsInReversedOrder.subList(1, pointsInReversedOrder.size())){
            System.out.println(nowPoint + ":" + nowPoint.getTheta());
            double nowPointTheta = 0;

            for(Map.Entry<Point, Double> after: weightedGraph.row(nowPoint).entrySet()){
                Point afterPoint = after.getKey();
                double weight = after.getValue();
                nowPointTheta += afterPoint.getTheta()*weight;
            }

            nowPoint.setTheta(nowPointTheta);
        }
    }

    /**
     * As the http://galaxy.agh.edu.pl/%7Evlsi/AI/backp_t_en/backprop.html,back or forward is the same
     */
    public void backwardWeight(){

        


    }







    public double activationF(double input){
        return sigmoid(input);
    }

    public double sigmoid(double input){
        //return 1.0/(1+Math.exp(-input));
        return input*2;
    }


    public Table<Point, Point, Double> getWeightedGraph() {
        return weightedGraph;
    }


    public void setPointsInOrder(List<Point> pointsInOrder) {
        this.pointsInOrder = pointsInOrder;
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
