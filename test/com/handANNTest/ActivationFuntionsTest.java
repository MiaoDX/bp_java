package com.handANNTest;

import com.handANN.ActivationFuntions;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;



/**
 * Created by miao on 2016/10/25.
 */


/**
 * Test that we can pass function to classes and use them as a member
 */
class FunctionAsPara{
    Function<Integer, Integer> function;
    int a = 100;
    FunctionAsPara(Function<Integer, Integer> _function){
        this.function = _function;
    }

    public Integer f(){
        return function.apply(a);
    }
}



public class ActivationFuntionsTest {

    @Test
    public void testFunctionAsPara(){
        Function<Integer,Integer> function = a-> a + 1;
        FunctionAsPara functionAsPara = new FunctionAsPara(function);
        System.out.println(functionAsPara.f());
    }

    @Test
    public void testUseFunctionInActivationFuntions(){
        FunctionAsPara functionAsPara = new FunctionAsPara(ActivationFuntions.fortest);
        assertThat(functionAsPara.f()).isEqualTo(200);
    }


    //API which accepts an implementation of

    //Function interface

    void modifyTheValue(int valueToBeOperated, Function<Integer, Integer> function){

        int newValue = function.apply(valueToBeOperated);

        /*
         * Do some operations using the new value.
         */

        System.out.println(newValue);

    }

    @Test
    public void testFunction(){

        int incr = 20;  int myNumber = 10;

        modifyTheValue(myNumber, val-> val + incr);

        myNumber = 15;  modifyTheValue(myNumber, val-> val * 10);

        modifyTheValue(myNumber, val-> val - 100);

        modifyTheValue(myNumber, val-> "somestring".length() + val - 100);
    }


    @Test
    public void testMapFun(){
        Function<Double, Double> function = ActivationFuntions.halfLambda;

        List<Double> list = new ArrayList<>();
        list.add(1.0);list.add(2.0);list.add(3.0);

        List<Double> list1 = list.stream().map(d -> function.apply(d)).collect(Collectors.toList());

        for(Double l : list1){
            System.out.println(l);
        }


    }

}
