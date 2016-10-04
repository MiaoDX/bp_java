import java.util.ArrayList;
import java.util.List;

/**
 * Created by miao on 2016/10/4.
 */
public class FunctionFaux {

    private List<Double> targets = new ArrayList<Double>();

    private IRanGen ranGen;

    private int num;

    FunctionFaux(IRanGen ranGen, int num){
        this.num = num;
        this.ranGen = ranGen;
    }

    double genF(double input){
        return Math.sin(input);
    }

    public List<Double> getNextInputAndTarget(){

        List<Double> doubles = new ArrayList<Double>();

        int index = (int)(ranGen.nextDouble()*num);

        double input = index*(Math.PI/num);

        double target = genF(input);

        doubles.add(input);
        doubles.add(target);

        return doubles;
    }


}
