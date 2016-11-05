package com.handANN;

import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by miao on 2016/10/26.
 */
public class AreaLineChartWithXChart {

    public static void main(String[] args) throws InterruptedException, IOException {
        IRanGen ranGen = new RanGen(0);
        int num = 10;
        Function sinf = MatchingFunctions.sinF;
        Function same = ActivationFuntions.sameLambda;

        Function OnePlusSinPiX = MatchingFunctions.OnePlusSinPiX;

        FunctionFaux functionFaux = new FunctionFaux(ranGen, same, -2.0, 2.0, num);

        List<Double> x = functionFaux.getDomainValues();
        List<Double> y1 = functionFaux.getFunctionValues();


        functionFaux = new FunctionFaux(ranGen, sinf, -2.0, 2.0, num);
        List<Double> y2 = functionFaux.getFunctionValues();
        show(x, y1, y2);
    }


    public static void show(List<List<Double>> xAndYs, String picName, List<String> lineNames) throws InterruptedException, IOException {


        assertThat(xAndYs.size()).isEqualTo(lineNames.size() + 1);

// Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title(picName).xAxisTitle("x").yAxisTitle("f").build();


        for (int i = 0; i < lineNames.size(); i++) {
            chart.addSeries(lineNames.get(i), xAndYs.get(0), xAndYs.get(i + 1)).setMarker(SeriesMarkers.NONE);
            ;
        }

        new SwingWrapper<XYChart>(chart).displayChart();


        BitmapEncoder.saveBitmap(chart, "./" + picName, BitmapEncoder.BitmapFormat.PNG);
        Thread.sleep(5000);
    }


    public static void show(List<Double> x, List<Double> y1, String picName, String firstLineName) throws InterruptedException, IOException {
// Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title("Check").xAxisTitle("x").yAxisTitle("f").build();
        XYSeries seriesLiability = chart.addSeries(firstLineName, x, y1);
        seriesLiability.setMarker(SeriesMarkers.NONE);
        new SwingWrapper<XYChart>(chart).displayChart();
        BitmapEncoder.saveBitmap(chart, "./" + picName, BitmapEncoder.BitmapFormat.PNG);
        Thread.sleep(5000);
    }

    public static void show(List<Double> x, List<Double> y1, List<Double> y2, String picName, String firstLineName, String secondLineName) throws InterruptedException, IOException {
// Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title("Check").xAxisTitle("x").yAxisTitle("f").build();
        XYSeries seriesLiability = chart.addSeries(firstLineName, x, y1);
        seriesLiability.setMarker(SeriesMarkers.NONE);
        chart.addSeries(secondLineName, x, y2);
        new SwingWrapper<XYChart>(chart).displayChart();
        BitmapEncoder.saveBitmap(chart, "./" + picName, BitmapEncoder.BitmapFormat.PNG);
        Thread.sleep(5000);
    }

    public static void show(List<Double> x, List<Double> y1, List<Double> y2, String picName) throws InterruptedException, IOException {
        show(x, y1, y2, picName, "Should be", "We got");
    }

    public static void show(List<Double> x, List<Double> y1, List<Double> y2) throws InterruptedException, IOException {
        show(x, y1, y2, "Our_answer", "Should be", "We got");
    }
}
