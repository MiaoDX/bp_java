package com.handANN;

/**
 * Created by miao on 2016/11/5.
 */
public interface IGraph {
    public void forward();

    public void backwardTheta();

    public void backwardWeight();

    default public void train(){
        this.forward();
        this.backwardTheta();
        this.backwardWeight();
    }
}
