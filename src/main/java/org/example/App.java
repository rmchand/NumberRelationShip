package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Sample App to find the relationship between given numbers
 *  as csv data and aggregate into groups per csv data set
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //Parallel Processing using fixed thread pool of 2 - configurable
        ExecutorService executorService= Executors.newFixedThreadPool(2);

        //Input Data Set ot simulate multiple files
        List<String>  inputDataSet = new ArrayList<>();
        inputDataSet.add("1,2\n2,3\n3,4\n7,5\n4,6");
        inputDataSet.add("10,11\n11,13\n12,11\n11,14");
        inputDataSet.add("1,2\n3,4\n5,6\n7,8\n");
        inputDataSet.add("-1,3\n-3,4\n4,1.2\n5,6");
        inputDataSet.add("0.001,0.002\n0.002,0.004\n4,5");
        inputDataSet.add("");

        // Loop over the list of our inputData Set and submit to the Aggregator application
        for(String data: inputDataSet) {
            try {
                executorService.execute( new RelationShipAggregator(data));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ;
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(10_000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
