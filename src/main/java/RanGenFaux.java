import java.util.ArrayList;
import java.util.List;

/**
 * Created by miao on 2016/10/4.
 */
public class RanGenFaux implements IRanGen {

    List<Double> list = new ArrayList<Double>();
    int nowIndex = -1;
    int maxnum = 100;

    RanGenFaux(){

        for(int i = 0; i < maxnum; i ++){
            list.add(((double)i)/maxnum);
        }


//        list.add(0.1);
//        list.add(0.2);
//        list.add(0.3);
//        list.add(0.4);
//        list.add(0.5);
//        list.add(0.6);
    }

    public double nextDouble() {
        nowIndex ++;
        return list.get(nowIndex);
    }
}
