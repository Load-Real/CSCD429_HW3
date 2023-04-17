import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/* Important Info:
-Cluster Definition: A collection of data objects
-Clustering Definition: Finding similarities between data according to the characteristics found in the data and grouping
similar data objects into a cluster
-Current Approach Parent Category:
Partitioning: Construct various partitions and then evaluate them by some criterion, e.g., minimizing the sum of square errors
-Current Approach Child Category:
K-Means: A heuristic method; a cluster is represented by the mean value (called a centroid)
    Using the squared-error criterion (distance from object to centroid is squared and then distances are summed) - SLIDE 10
    k < n, where n = # of objects
-The Steps:
1) Arbitrarily select k objects from D as the initial cluster mean (centroids)
2) Reassign each object upon the distance between the object and the cluster centroid
3) Update the cluster centroid using objects in the cluster
4) Repeat (2) and (3) until the criterion function converges (no redistribution of objects in any cluster)
 */

public class KMeans {
    static final int K = 6;
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("synthetic_control_data.txt"); //Contains 600 examples of control chart time series data
        Scanner fileScanner = new Scanner(file);
        ArrayList<Chart> charts = new ArrayList<Chart>();
        for (int i = 0; i < 600; i++) {
            charts.add(inputOutputHandler.readFile(file, fileScanner, i));
        }
        fileScanner.close();
        run(charts);
    }

    //I realized that if I wanna loop until convergence, it might be unsafe to do all that in the main
    public static void run(ArrayList<Chart> charts) {
        ArrayList<Double> centroids = initializeCentroids(charts);
        ArrayList<ArrayList<Double>> clusters = new ArrayList<>();
        while (true) {
            //Construct Clusters
            clusters = assignData(charts, centroids);

            //Update Centroids
            ArrayList<Double> updatedCentroids = updateCentroids(clusters);

            //Check for convergence
            if (centroids.equals(updatedCentroids)) {
                break;
            }
            centroids = updatedCentroids;
        }
        inputOutputHandler.output(clusters);
    }

    /*EXPL:
    1) This method randomly generates a number that will be an index in my charts array and then chooses the data points
    at these K indices to be more initial centroids.
     */
    public static ArrayList<Double> initializeCentroids(ArrayList<Chart> charts) {
        Random rand = new Random();
        ArrayList<Double> centroids = new ArrayList<Double>();
        for (int i = 0; i < K; i++) {
            int randIndex = rand.nextInt(charts.size());
            Chart chart = charts.get(randIndex);
            double centroid = chart.chart[rand.nextInt(chart.chart.length)];
            centroids.add(centroid);
        }
        return centroids;
    }

    /*EXPL:
    1) This method first creates the K clusters that will be returned
    2) Then this method computes distance (using my centroids generated above) in order to assign data points to certain clusters
     */
    public static ArrayList<ArrayList<Double>> assignData(ArrayList<Chart> charts, ArrayList<Double> centroids) {
        //Create clusters
        ArrayList<ArrayList<Double>> clusters = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            clusters.add(new ArrayList<>());
        }

        for (Chart chart : charts) {
            for (int dataIndex = 0; dataIndex < chart.chart.length; dataIndex++) {
                int nearestCentroidIndex = 0;
                double minDistance = Double.MAX_VALUE;
                for (int clusterIndex = 0; clusterIndex < K; clusterIndex++) {
                    double distance = Math.abs(chart.chart[dataIndex] - centroids.get(clusterIndex));
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestCentroidIndex = clusterIndex;
                    }
                }
                clusters.get(nearestCentroidIndex).add(chart.chart[dataIndex]);
            }
        }
        return clusters;
    }

    public static ArrayList<Double> updateCentroids (ArrayList<ArrayList<Double>> clusters) {
        ArrayList<Double> updatedCentroids = new ArrayList<Double>();
        for (ArrayList<Double> cluster : clusters) {
            double dataSum = 0.0;
            for (double dataPoint : cluster) {
                dataSum += dataPoint;
            }
            double clusterMean = dataSum / cluster.size();
            updatedCentroids.add(clusterMean);
        }
        return updatedCentroids;
    }
}
