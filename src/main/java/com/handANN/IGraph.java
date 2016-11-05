package com.handANN;

/**
 * Created by miao on 2016/11/5.
 */
public interface IGraph {
    public void forward();

    public void backwardTheta();

    public void backwardWeight();

    public void train();
}
