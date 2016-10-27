package com.handANNTest;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
        BitmapEncoder.saveBitmapWithDPI(chart, "./Sample_Chart_300_DPI", BitmapEncoder.BitmapFormat.PNG, 300);
    }
}
