package com.handANNTest;

import com.google.common.collect.Interner;
import org.testng.annotations.Test;
import com.google.common.graph.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;



/**
 * Created by miao on 2016/10/28.
 */
public class GuavaGraphTest {

    @Test
    public void simpleUse(){
        MutableValueGraph<Integer, Double> weightedGraph = ValueGraphBuilder.directed().build();
        weightedGraph.addNode(1);
        weightedGraph.putEdgeValue(2, 3, 1.5);  // also adds nodes 2 and 3 if not already present
        weightedGraph.putEdgeValue(3, 5, 1.5);  // edge values (like Map values) need not be unique

        assertThat(weightedGraph.nodes().contains(1)).isEqualTo(true);
    }


    


    @Test
    public void traverseDirectedGraph(){
        MutableValueGraph<Integer, Double> wG = ValueGraphBuilder.directed().build();
        wG.putEdgeValue(0, 1, 0.1);
        wG.putEdgeValue(0, 2, 0.2);
        wG.putEdgeValue(0, 3, 0.3);

        wG.putEdgeValue(1, 4, 0.4);
        wG.putEdgeValue(2, 4, 0.5);
        wG.putEdgeValue(3, 4, 0.6);

        List<Integer> layer0 = Arrays.asList(0);
        List<Integer> layer1 = Arrays.asList(1,2,3);
        List<Integer> layer2 = Arrays.asList(4);

        List<List<Integer>> allLayer = Arrays.asList(layer0, layer1, layer2);

        int first = 0, last = 4;
        // forward

        for(List<Integer> nowLayer : allLayer){
            System.out.println("\n\n\n now Layer: "+nowLayer);
            for(Integer nowNode : nowLayer){
                System.out.println("\n\t now Node: "+nowNode);
                Set<Integer> successors = wG.successors(nowNode);

                successors.stream().map(nowSuccessor ->  wG.edgeValue(nowNode ,nowSuccessor));//.forEach(v -> System.out.println("\n\t\t now successor: " + nowNode + ",weight:" + v));
                //System.out.println("\n\t\t now successor: " + v + ",weight:" + wG.edgeValue(nowNode ,v)
                //successors.forEach(v -> System.out.println("\n\t\t now successor: " + v + ",weight:" + wG.edgeValue(nowNode ,v)));



            }
        }





        System.out.println(wG);

    }

}
