import java.util.ArrayList;
import java.util.List;

/**
 * Created by miao on 2016/10/4.
 */
public class RanGenFaux implements IRanGen {

    List<Double> list = new ArrayList<Double>();
    int nowIndex = -1;
    int maxNum = 100;

    RanGenFaux(){
        for(int i = 0; i < maxNum; i ++){
            list.add(((double)i)/maxNum);
        }
    }

    public int getMaxNum() {
        return maxNum;
    }

    public boolean hasNext() {
        return nowIndex < maxNum - 1;
    }

    public double nextDouble() {
        nowIndex ++;
        return list.get(nowIndex);
    }
}
