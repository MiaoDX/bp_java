import org.testng.annotations.Test;

/**
 * Created by miao on 2016/10/4.
 */
public class FunctionFauxTest {
    @Test
    public void FunctionFaux(){
        //IRanGen ranGen = new RanGenFaux();
        IRanGen ranGen = new RanGen(0);
        int num = 100;
        FunctionFaux functionFaux = new FunctionFaux(ranGen,num);

        for(int i = 0;i < num; i ++){
            System.out.println(functionFaux.getNextInputAndTarget());
        }


    }
}
