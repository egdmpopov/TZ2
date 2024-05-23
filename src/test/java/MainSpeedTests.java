import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

public class MainSpeedTests {
    static int[] numNumbers = new int []{10, 100000, 200000, 300000, 400000, 500000, 600000, 700000, 800000, 900000, 1000000};
    int min = 1;
    int max = 10000;
    static String[] methods = new String[]{"_min", "_max", "_sum", "_mult"};

    @Test
    public void measureAllMethods() throws IOException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, WrongDataException {
        XYSeriesCollection collection = new XYSeriesCollection();
        for (String classMethod : methods) {
            XYSeries series = new XYSeries(classMethod);
            for (int numLenght : numNumbers) {
                Random rand = new Random();
                Main.numbers = new int[numLenght];
                max = numLenght;
                long referenceVal = switch (classMethod) {
                    case "_min" -> max;
                    case "_max" -> min;
                    case "_sum" -> 0;
                    case "_mult" -> 1;
                    default -> 0;
                };
                String numsFile = numLenght + "_numm.rtf";
                BufferedWriter writer = new BufferedWriter(new FileWriter(numsFile));
                for (int j = 0; j < numLenght; j++) {
                    int randomNum = rand.nextInt(min, max);
                    switch (classMethod) {
                        case "_min" -> {
                            if (referenceVal > randomNum) {
                                referenceVal = randomNum;
                            }
                        }
                        case "_max" -> {
                            if (referenceVal < randomNum) {
                                referenceVal = randomNum;
                            }
                        }
                        case "_sum" -> referenceVal += randomNum;
                        case "_mult" -> referenceVal *= randomNum;
                    }
                    writer.write(randomNum + " ");
                }
                writer.close();
                long start = System.currentTimeMillis();
                Main.loadFile(numsFile);
                Assertions.assertTrue(new File(numsFile).delete());
                Method method = Main.class.getMethod(classMethod);
                Assertions.assertEquals(referenceVal, method.invoke(Main.class));
                long duration = System.currentTimeMillis() - start;
                System.out.println(numLenght + " ints " + classMethod + "() calculation takes "
                        + duration + " milliseconds to run");
                series.add(numLenght, duration);
            }
            collection.addSeries(series);
        }
        JFreeChart chart = ChartFactory.createXYLineChart("Speed diagram",
                "number of integers in file", "milliseconds", collection);
        ChartUtils.saveChartAsPNG(new File("chart.png"), chart, 800, 600);
    }
}
