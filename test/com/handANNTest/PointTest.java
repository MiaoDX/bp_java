package com.handANNTest;
/**
 * Created by miao on 2016/10/4.
 */

import com.handANN.Point;
import org.testng.annotations.Test;

public class PointTest {
    @Test
    public void testPoint(){
        Point p = new Point(1,2);
        System.out.println(p);
        //assertThat(p.toString()).isEqualTo("{layer:1,number:2}");
    }
}
