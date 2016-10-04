

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by miao on 2016/10/4.
 */
public class PointPairTest {
    @Test
    public void testGenerator(){
        Point lp = new Point(1,2);
        Point rp = new Point(2,3);
        PointPair pointPair = new PointPair(lp, rp);

        Point lp1 = pointPair.getLeftPoint();
        System.out.println(lp1);
        assertThat(lp).isEqualTo(lp1);

        lp.setInput(100);

        System.out.println(lp);
        System.out.println(lp1);


        lp1.setInput(10000);

        System.out.println(lp);
        System.out.println(lp1);

    }
}
