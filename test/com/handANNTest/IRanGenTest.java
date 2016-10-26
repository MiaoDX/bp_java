package com.handANNTest;
import com.handANN.RanGenFaux;
import org.testng.annotations.Test;

/**
 * Created by miao on 2016/10/4.
 */
public class IRanGenTest {
    @Test
    public void testRanGenFaux(){
        RanGenFaux ranGenFaux = new RanGenFaux();
        for (int i = 0; i < 20; i ++){
            System.out.println(ranGenFaux.nextDouble());
        }
    }
}
