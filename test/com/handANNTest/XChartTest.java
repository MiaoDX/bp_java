package com.handANNTest;

import com.handANN.*;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by miao on 2016/10/26.
 */
public class XChartTest {
    public static void main(String[] args) throws IOException {
        double[] xData = new double[]{0.0, 1.0, 2.0};
        double[] yData1 = new double[]{2.0, 1.0, 0.0};
        double[] yData2 = new double[]{1.0, 2.0, 3.0};

        Double[] xDataD = new Double[]{0.0, 1.0, 2.0};
        Double[] yDataD = new Double[]{2.0, 1.0, 0.0};

        List<Double> x = Arrays.asList(xDataD);
        List<Double> y = Arrays.asList(yDataD);

        double[][] yData = {yData1, yData2};

        String[] seriesNames = {"y1(x)", "y2(x)"};

        //getChart(String chartTitle, String xTitle, String yTitle, String[] seriesNames, double[] xData, double[][] yData) {


        // Create Chart
        XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y1(x)", x, y);

        // Show it
        new SwingWrapper(chart).displayChart();

        // Save it
        BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapEncoder.BitmapFormat.PNG);

        // or save it in high-res
        BitmapEncoder.saveBitmapWithDPI(chart, "./target/Sample_Chart_300_DPI", BitmapEncoder.BitmapFormat.PNG, 300);
    }


    @Test
    public void sinAndSinAddCos() throws IOException, InterruptedException {
        IRanGen ranGen = new RanGen(0);
        int num = 100;
        double domainStart = -Math.PI*2;
        double domainEnd = Math.PI*2;

        Function<Double, Double> sinF = MatchingFunctions.sinF;
        FunctionFaux functionFaux = new FunctionFaux(ranGen, sinF, domainStart, domainEnd, num);

        List<Double> domainValues = functionFaux.getDomainValues();
        List<Double> siFYs = functionFaux.getFunctionValues();

        Function<Double, Double> SinAddCos = MatchingFunctions.SinAddCos;
        Function<Double, Double> FiveSinADDFourCosX = MatchingFunctions.FiveSinADDFourCosX;

        Function<Double, Double> sinHalfAddcosOneThird = x -> 0.5*Math.sin(x/2);

        FunctionFaux functionFaux2 = new FunctionFaux(ranGen, sinHalfAddcosOneThird, domainStart, domainEnd, num);


        List<Double> sinAddCosYs = functionFaux2.getFunctionValues();

        for (int i = 0; i < num; i++) {
            //System.out.println(domainValues.get(i) + ":" + siFYs.get(i));
            assertThat(sinF.apply(domainValues.get(i))).isEqualTo(siFYs.get(i));
            assertThat(sinHalfAddcosOneThird.apply(domainValues.get(i))).isEqualTo(sinAddCosYs.get(i));
        }

        AreaLineChartWithXChart.show(domainValues, siFYs, sinAddCosYs, "function_complex" ,"sin(x)", "0.5*sin(x/2)");

    }
}
