import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class inputOutputHandler {
    public static Chart readFile(File file, Scanner fileScanner, int chartID) {
        int i = 0;
        double[] data = new double[60];
        while(fileScanner.hasNextDouble() && i < 60) {
            data[i] = fileScanner.nextDouble();
            i++;
        }
        return new Chart(chartID, data);
    }

    public static void printCharts(ArrayList<Chart> charts) {
        int chartNum = 1;
        for (Chart chartObject : charts) {
            System.out.print("Chart " + chartNum+ ": ");
            for (int i = 0; i < chartObject.chart.length; i++) {
                System.out.print(chartObject.chart[i] + ", ");
            }
            System.out.print("\n");
            chartNum++;
        }
    }

    public static void printClusters(ArrayList<ArrayList<Double>> clusters) {
        int currentCluster = 1;
        for (ArrayList<Double> cluster : clusters) {
            System.out.println("\nCluster #" + currentCluster + ":");
            for (double data : cluster) {
                System.out.print(data + ", ");
            }
            currentCluster++;
        }
    }

    public static void output(ArrayList<ArrayList<Double>> clusters) {
        //Extract the clusters and send each to a txt file
        int currentCluster = 1;
        int colCount = 1; //Split the rows after 60 columns
        for (ArrayList<Double> cluster : clusters) {
            try {
                FileWriter writer = new FileWriter("cluster" + currentCluster + ".txt");
                for (double dataPoint : cluster) {
                    writer.write(String.valueOf(dataPoint) + " ");
                    if (colCount >= 60) {
                        writer.write("\n");
                        colCount = 0;
                    }
                    colCount++;
                }
                currentCluster++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
