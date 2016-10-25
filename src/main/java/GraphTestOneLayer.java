import com.google.common.collect.LinkedHashMultimap;
import org.assertj.core.data.Offset;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by miao on 2016/10/25.
 */
public class GraphTestOneLayer {

    /**
     * this network only has one layer: p00 -> p10
     * p00 is input and p10 is output
     * p00 with a halfActivationF
     * p10 with a sameActivationF
     * At first,weight is set to 0.5
     *
     * So is "f(x) = 1/2 x * weight"
     *
     * We use (1.0, 1.0) as the goal,it should finally make `weight` to 2
     *
     * We track the first run and the final answer.SEEMS OK.
     */
    @Test
    public void oneLayerWithHalfAndSameActivationFunction(){
        Point p00 = new Point(0,0);
        Point p10 = new Point(1,0);
        p00.setActivationF(ActivationFuntions.halfLambda);
        p00.setDifferentiationF(ActivationFuntions.halfDifferentiationLambda);

        p10.setActivationF(ActivationFuntions.sameLambda);
        p10.setDifferentiationF(ActivationFuntions.sameDifferentiationLambda);

        LinkedHashMultimap<Point, Point> linkedHashMultimap = LinkedHashMultimap.create();
        linkedHashMultimap.put(p00, p10);

        List<Point> pointsOrder = new ArrayList<Point>();
        pointsOrder.add(p00);
        pointsOrder.add(p10);

        IRanGen ranGen = new RanGenProvided(0.5);
        Graph graph = new Graph(linkedHashMultimap,pointsOrder,ranGen);



        //init
        graph.updateGraph(1.0,1.0);
        assertThat(p00.getInput()).isEqualTo(1.0);
        assertThat(p00.getOutput()).isEqualTo(0.5);
        assertThat(p10.getInput()).isEqualTo(0.0);
        assertThat(p10.getOutput()).isEqualTo(0.0);
        assertThat(graph.getWeightedGraph().get(p00,p10)).isEqualTo(0.5);




        //forward
        graph.forward();
        assertThat(graph.getError()).isEqualTo(3.0/4);


        graph.backwardTheta();
        assertThat(p10.getTheta()).isEqualTo(3.0/4);    //sameDifferentiation is 1
        assertThat(p00.getTheta()).isEqualTo(3.0/16);


        graph.backwardWeight();
        assertThat(graph.getWeightedGraph().get(p00,p10)).isEqualTo(83.0/160);





        int time = 1;

        do{
            time ++;
            graph.train();

            if(time % 1000 == 0){
                System.out.println(time + ":" + graph.getOutput() +";" + graph.getError());
            }

        }while (graph.getError() > 0.001);

        graph.beautifyWeightedGraphOutput();
        System.out.println("\n" + time + ":" + graph.getOutput() +";" + graph.getError());

    }

    @Test
    public void oneLayerWithSigmoidAndSameActivationFunction(){
        Point p00 = new Point(0,0);
        Point p10 = new Point(1,0);

        p10.setActivationF(ActivationFuntions.sameLambda);
        p10.setDifferentiationF(ActivationFuntions.sameDifferentiationLambda);

        LinkedHashMultimap<Point, Point> linkedHashMultimap = LinkedHashMultimap.create();
        linkedHashMultimap.put(p00, p10);

        List<Point> pointsOrder = new ArrayList<Point>();
        pointsOrder.add(p00);
        pointsOrder.add(p10);

        IRanGen ranGen = new RanGenProvided(0.5);
        Graph graph = new Graph(linkedHashMultimap,pointsOrder,ranGen);


        Function<Double,Double> sigmoid = ActivationFuntions.sigmoidLambda;
        Function<Double,Double> sigmoidDifferentiationL = ActivationFuntions.sigmoidDifferentiationLambda;

        //init
        graph.updateGraph(1.0,1.0);
        assertThat(p00.getInput()).isEqualTo(1.0);

        Double p00_output = sigmoid.apply(1.0);

        assertThat(p00.getOutput()).isEqualTo(p00_output);
        assertThat(p10.getInput()).isEqualTo(0.0);
        assertThat(p10.getOutput()).isEqualTo(0.0);
        assertThat(graph.getWeightedGraph().get(p00,p10)).isEqualTo(0.5);




        //forward
        graph.forward();

        Double p10_output = 1.0-p00_output/2;

        assertThat(graph.getError()).isEqualTo(p10_output);


        graph.backwardTheta();
        assertThat(p10.getTheta()).isEqualTo(p10_output);    //sameDifferentiation is 1
        assertThat(p00.getTheta()).isEqualTo(p10_output*(1.0/2)*sigmoidDifferentiationL.apply(1.0));


        graph.backwardWeight();
        assertThat(graph.getWeightedGraph().get(p00,p10)).isEqualTo(1.0/2 + 0.05*p00_output*p10_output);





        int time = 1;

        do{
            time ++;
            graph.train();

            if(time % 1000 == 0){
                System.out.println(time + ":" + graph.getOutput() +";" + graph.getError());
            }

        }while (graph.getError() > 0.001);

        graph.beautifyWeightedGraphOutput();
        System.out.println("\n" + time + ":" + graph.getOutput() +";" + graph.getError());


        assertThat(graph.getWeightedGraph().get(p00,p10)).isCloseTo(1.0/sigmoid.apply(1.0), Offset.offset(0.01));

    }




}
