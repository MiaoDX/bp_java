package com.handANN;

/**
 * Created by miao on 2016/11/5.
 */
public abstract class IGraph {
    abstract public void forward();

    abstract public void backwardTheta();

    abstract public void backwardWeight();

    final public void train() {
        this.forward();
        this.backwardTheta();
        this.backwardWeight();
    }
}
