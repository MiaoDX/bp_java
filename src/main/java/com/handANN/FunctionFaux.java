package com.handANN;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by miao on 2016/10/4.
 */
public class FunctionFaux {

    private List<Double> targets = new ArrayList<Double>();

    private IRanGen ranGen;

    private double domainStart;

    private double domainEnd;

    private int num;

    private Function<Double, Double> matchingF;


    private List<Double> domainValues = new ArrayList<>();
    private List<Double> functionValues = null;

    private List<Double> widenDomainValues = new ArrayList<>();
    private List<Double> widenFunctionValues = null;

    public FunctionFaux(IRanGen ranGen, Function<Double, Double> matchingF, Double domainStart, Double domainEnd, int num) {


        double step = (domainEnd - domainStart) / (num - 1);  //a little attention,to split [0,1] to 5 parts,step should be 1.0/4 ^_^
        for (int i = 0; i < num; i++) {
            domainValues.add(domainStart + i * step);
        }

        functionValues = domainValues.stream().map(v -> matchingF.apply(v)).collect(Collectors.toList());


        this.ranGen = ranGen;
        this.matchingF = matchingF;
        this.domainStart = domainStart;
        this.domainEnd = domainEnd;
        this.num = num;
    }

    public void widenDomain(int widenSize){ //wide the domain and function value for better plot performance
        double oldGap = domainEnd - domainStart;
        double widenDomainStart = domainStart - oldGap*widenSize/2;
        double widenDomainEnd = domainEnd + oldGap*widenSize/2;

        int newNum = widenSize*num*2;   // multiply 2 is to generate more dense points

        double newStep = (widenDomainEnd - widenDomainStart) / (newNum - 1);

        for (int i = 0; i < newNum; i++) {
            widenDomainValues.add(widenDomainStart + i * newStep);
        }

        widenFunctionValues = widenDomainValues.stream().map(v -> matchingF.apply(v)).collect(Collectors.toList());
    }

    public List<Double> getRandomNextInputAndTarget() {
        List<Double> doubles = new ArrayList<Double>();
        double input = ranGen.nextDouble() * (domainEnd - domainStart) + domainStart;
        double target = matchingF.apply(input);
        doubles.add(input);
        doubles.add(target);
        return doubles;
    }

    public List<Double> getDomainValues() {
        return domainValues;
    }

    public List<Double> getFunctionValues() {
        return functionValues;
    }

    public List<Double> getWidenDomainValues() {
        return widenDomainValues;
    }

    public List<Double> getWidenFunctionValues() {
        return widenFunctionValues;
    }


}
