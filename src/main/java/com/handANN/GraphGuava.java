package com.handANN;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by miao on 2016/10/4.
 */
public class GraphGuava extends IGraph {


    private double learnRatio = 0.05;

    private List<List<Point>> allLayersNodes = new ArrayList<>();
    private MutableValueGraph<Point, Double> weightedGraphGuava = ValueGraphBuilder.directed().build();

    private List<Point> firstLayerNodes = new ArrayList<>();
    private List<Point> lastLayerNodes = new ArrayList<>();
    private List<Double> lastLayerNodesTargets = new ArrayList<>();
    private List<Double> lastLayerNodesErrors = new ArrayList<>();

    /**
     * @param layerNum, each element present the number of point of given layer
     *                  [1,4,1] means first layer has 1 point,second layer has 4 points,...
     */
    public GraphGuava(List<Integer> layerNum, IRanGen ranGen, double _learnRatio) {
        weightedGraphGuava = ValueGraphBuilder.directed().build();// new Graph

        for (int nowLayerNum = 0; nowLayerNum < layerNum.size(); nowLayerNum++) {
            List<Point> nowLayerNodes = new ArrayList<>();
            for (int nowNodeNum = 0; nowNodeNum < layerNum.get(nowLayerNum); nowNodeNum++) {
                Point nowPoint = new Point(nowLayerNum, nowNodeNum);
                nowLayerNodes.add(nowPoint);
            }
            allLayersNodes.add(nowLayerNodes);
        }


        for (int leftLayerNum = 0; leftLayerNum < layerNum.size() - 1; leftLayerNum++) {
            int rightLayerNum = leftLayerNum + 1;
            List<Point> leftLayerNodes = allLayersNodes.get(leftLayerNum);
            List<Point> rightLayerNodes = allLayersNodes.get(rightLayerNum);
            leftLayerNodes.forEach(leftNode -> rightLayerNodes.forEach(rightNode -> weightedGraphGuava.putEdgeValue(leftNode, rightNode, ranGen.nextDouble())));
        }


//        for (int nowLayerNum = 0; nowLayerNum < allLayersNodes.size(); nowLayerNum++) {
//            List<Point> nowLayerNodes = allLayersNodes.get(nowLayerNum);
//            nowLayerNodes.forEach(nowNode -> nowNode.setBias(ranGen.nextDouble()));
//        }

        // this can not make sure the order
        for (Point node : weightedGraphGuava.nodes()) {
            node.setBias(ranGen.nextDouble());
        }


        //set first and last layer nodes
        firstLayerNodes = allLayersNodes.get(0);
        lastLayerNodes = allLayersNodes.get(layerNum.size() - 1);

        learnRatio = _learnRatio;
    }


    public void updateGraph(List<Double> inputs, List<Double> targets) {

        if (inputs.size() != firstLayerNodes.size() || targets.size() != lastLayerNodes.size()) {
            throw new IllegalArgumentException("inputs or targets length should be same as defined");
        }

        // it can be a little meaningless,but I just want to use Stream once -.-
        IntStream.range(0, inputs.size()).forEach(i -> firstLayerNodes.get(i).setInput(inputs.get(i)));

        lastLayerNodesTargets.clear();
        lastLayerNodesTargets.addAll(targets);
    }


//    public void train() {
//        this.forward();
//        this.backwardTheta();
//        this.backwardWeight();
//    }


    public double weightGetter(Point a, Point b) {
        return weightedGraphGuava.edgeValue(a, b);
    }


    public void forwardCalcInput(Point nowNode) {
        Set<Point> preNodes = weightedGraphGuava.predecessors(nowNode);

        double nowNodeInput = 0.0;
        for (Point preNode : preNodes) {
            nowNodeInput += preNode.getOutput() * weightGetter(preNode, nowNode);
        }

        nowNode.setInput(nowNodeInput);
    }

    public void forwardCalcLayerInput(int layerNum) {
        if (layerNum == 0) { // since we have dealt with first layer,just return
            return;
        }

        List<Point> layerNodes = allLayersNodes.get(layerNum);
        for (Point node : layerNodes) {
            forwardCalcInput(node);
        }
    }

    public void forwardCalcLayerOutput(int layerNum) {
        List<Point> layerNodes = allLayersNodes.get(layerNum);
        for (Point node : layerNodes) {
            node.activateAndSetOutput();
        }
    }


    public void calcError() {
        lastLayerNodesErrors.clear();
//        lastLayerNodesTargets ,lastLayerNodesErrors
        for (int i = 0; i < lastLayerNodes.size(); i++) {
            // error = target - output
            lastLayerNodesErrors.add(lastLayerNodesTargets.get(i) - lastLayerNodes.get(i).getOutput());
        }
    }

    public void forward() {

        for (int nowLayerNum = 0; nowLayerNum < allLayersNodes.size(); nowLayerNum++) {
            forwardCalcLayerInput(nowLayerNum);
            forwardCalcLayerOutput(nowLayerNum);
        }
        calcError();
    }


    public void backwardCalcTheta(Point nowNode) {

        Set<Point> afterNodes = weightedGraphGuava.successors(nowNode);

        double afterNodesThetaSum = 0.0;

        for (Point afterNode : afterNodes) {
            afterNodesThetaSum += afterNode.getTheta() * weightGetter(nowNode, afterNode);
        }

        nowNode.setTheta(nowNode.derivativeAndReturnDifferentiation() * afterNodesThetaSum);
    }


    public void calcLastLayerTheta() {
        for (int i = 0; i < lastLayerNodes.size(); i++) {
            Point nowPoint = lastLayerNodes.get(i);
            nowPoint.setTheta(nowPoint.derivativeAndReturnDifferentiation() * lastLayerNodesErrors.get(i));
        }
    }

    public void backwardCalcLayerTheta(int layerNum) {

        if (layerNum == allLayersNodes.size() - 1) { // last layer must take care,since this don't have after nodes
            calcLastLayerTheta();
            return;
        }

        List<Point> layerNodes = allLayersNodes.get(layerNum);
        for (Point nowNode : layerNodes) {
            backwardCalcTheta(nowNode);
        }
    }


    /**
     * back the error(theta)
     */
    public void backwardTheta() {
        for (int nowLayerNum = allLayersNodes.size() - 1; nowLayerNum >= 0; nowLayerNum--) {
            backwardCalcLayerTheta(nowLayerNum);
        }
    }


    /**
     * As the http://galaxy.agh.edu.pl/%7Evlsi/AI/backp_t_en/backprop.html ,back or forward is the same
     */
    public void backwardWeight() {

        Set<EndpointPair<Point>> endpointPairs = weightedGraphGuava.edges();

        for (EndpointPair edge : endpointPairs) {
            updateWeight((Point) edge.source(), (Point) edge.target());
        }

        updateBias();
    }


    public void updateWeight(Point nowPoint, Point afterPoint) {
        double nowWeight = weightGetter(nowPoint, afterPoint);

        double newWeight = nowWeight + learnRatio * afterPoint.getTheta() *
                nowPoint.getOutput();

        weightedGraphGuava.putEdgeValue(nowPoint, afterPoint, newWeight);
    }

    public void updateBias() {
        for (Point p : weightedGraphGuava.nodes()) {
            double newBias = p.getBias() + learnRatio * p.getTheta();
            p.setBias(newBias);
        }
    }


    public List<List<Point>> getAllLayersNodes() {
        return allLayersNodes;
    }

    public MutableValueGraph<Point, Double> getWeightedGraphGuava() {
        return weightedGraphGuava;
    }

    public List<Double> getLastLayerNodesErrors() {
        return lastLayerNodesErrors;
    }

    public List<Point> getLastLayerNodes() {
        return lastLayerNodes;
    }


    // hand written helper function
    public List<Double> getLastLayerNodesOutput() {
        return lastLayerNodes.stream().map(node -> node.getOutput()).collect(Collectors.toList());
    }


    public void trainWithStream() {
        this.forwardWithStream();
        this.backwardThetaWithStream();
        this.backwardWeightWithStream();
    }


    public void forwardCalcInputWithStream(Point nowNode) {
        weightedGraphGuava.predecessors(nowNode).parallelStream().forEach(preNode -> preNode.activateAndSetOutput());
        double nowNodeInput = weightedGraphGuava.predecessors(nowNode).parallelStream().map(preNode -> preNode.getOutput() * weightGetter(preNode, nowNode)).mapToDouble(v -> v).sum();
        nowNode.setInput(nowNodeInput);
    }

    public void forwardCalcLayerInputWithStream(int layerNum) {
        if (layerNum == 0) { // since we have dealt with first layer,just return
            return;
        }

        List<Point> layerNodes = allLayersNodes.get(layerNum);

        layerNodes.parallelStream().forEach(node -> forwardCalcInputWithStream(node));
    }

    public void forwardCalcLayerOutputWithStream(int layerNum) {
        List<Point> layerNodes = allLayersNodes.get(layerNum);
        layerNodes.parallelStream().forEach(node -> node.activateAndSetOutput());
    }


    public void forwardWithStream() {

        for (int nowLayerNum = 0; nowLayerNum < allLayersNodes.size(); nowLayerNum++) {
            forwardCalcLayerInputWithStream(nowLayerNum);
            forwardCalcLayerOutputWithStream(nowLayerNum);
        }

        calcError();
    }


    public void backwardCalcLayerThetaWithStream(int layerNum) {

        if (layerNum == allLayersNodes.size() - 1) { // last layer must take care,since this don't have after nodes
            calcLastLayerTheta();
            return;
        }

        List<Point> layerNodes = allLayersNodes.get(layerNum);
        layerNodes.parallelStream().forEach(nowNode -> backwardCalcThetaWithStream(nowNode));
    }

    public void backwardCalcThetaWithStream(Point nowNode) {
        Set<Point> afterNodes = weightedGraphGuava.successors(nowNode);
        double nowNodeTheta = afterNodes.parallelStream().map(afterNode -> afterNode.getTheta() * weightGetter(nowNode, afterNode)).mapToDouble(v -> v).sum();
        nowNode.setTheta(nowNode.derivativeAndReturnDifferentiation() * nowNodeTheta);
    }

    public void backwardThetaWithStream() {
        for (int nowLayerNum = allLayersNodes.size() - 1; nowLayerNum >= 0; nowLayerNum--) {
            backwardCalcLayerThetaWithStream(nowLayerNum);
        }
    }

    public void backwardWeightWithStream() {
        weightedGraphGuava.edges().parallelStream().forEach(edge -> updateWeight((Point) edge.source(), (Point) edge.target()));
        updateBiasWithStream();
    }

    public void updateBiasWithStream() {
        weightedGraphGuava.nodes().parallelStream().forEach(p -> p.setBias(p.getBias() + learnRatio * p.getTheta()));
    }
}
