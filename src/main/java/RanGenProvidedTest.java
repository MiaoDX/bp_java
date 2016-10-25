import org.testng.annotations.Test;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by miao on 2016/10/24.
 */
public class RanGenProvidedTest {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private void test(String... args) {
        System.out.println(args.getClass());
        for (String arg : args) {
            System.out.println(arg);
        }
    }

    private void test2(Double... args) {
        System.out.println(args.getClass());
        for (Double arg : args) {
            System.out.println(arg);
        }
    }

//    @Test
    public void testNoStaticArgs(){
        test();
        test("aaa");
        test("aaa", "bbb");
        test("aaa", "bbb", "ccc");
        test2(0.1);
        test2(0.1,0.2);
    }


    @Test
    public void testRanGenProvided(){
        logger.info("Now testRanGenProvided");
        IRanGen ranGen = new RanGenProvided(0.1,0.2,0.3);
        while (ranGen.hasNext()){
            System.out.println(ranGen.nextDouble());
        }
    }


}
