package org.example;


import java.io.BufferedReader;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class RelationShipAggregator extends Thread {

    //Input Data to operate
    String inputData;

    //Obtain the input data via constructor
    public RelationShipAggregator(String dataSet) throws Exception {
        if(dataSet==null || dataSet.length()==0){
            throw new Exception("Thread : "+Thread.currentThread().getName() + " Input data has to be csv with two columns");
        }
        this.inputData =dataSet;
    }

    @Override
    public void run() {
     //Read file stuff - mimicked
     //String inputData= "1,2\n2,3\n3,4\n7,5\n4,6";
     //File data is stored in map to represent relation eg 1 -- > (2,3) , 2-->(3)
     Map<String, List<String>> inputDataMap=new HashMap<>();
     BufferedReader bufferedReader=new BufferedReader(new StringReader(inputData));
     String row ;
     //Some optimization - to avoid regex in loop for large data set -
     // better to compile the regex if generic at start and reuse it.
     Pattern pattern=Pattern.compile(",");
     try {
         while ((row = bufferedReader.readLine()) != null) {
             String[] data=pattern.split(row);
             //Store both key and value linked to key and value eg if 1 ,2 then 1 is linked to 2 and vice versa
             aggregateToMap(inputDataMap,data[0],data[1]);
             aggregateToMap(inputDataMap,data[1],data[0]);

         }
         System.out.println("Thread : "+Thread.currentThread().getName() +" Input Map"+inputDataMap);
         
         //copy the data to concurrent hash map to represet cache while we iterate the actual inputDataMap
         Map<String,List<String>> cache=new ConcurrentHashMap<>(inputDataMap);
         for(Map.Entry<String,List<String>> entry : inputDataMap.entrySet()){
             List finalDataGroup=printAllLinks(cache,entry.getKey(),entry.getValue(),new ArrayList<String>());
             if(!finalDataGroup.isEmpty()){
                 System.out.println( "Thread : "+Thread.currentThread().getName() +" Final Group Details"+ finalDataGroup);
             }
         }
     }catch (Exception e){
         e.printStackTrace();
     }
    }

    /**
      Method to insert into map key being string and value is list<String>
     Input passby ref map object with the key and value to be inserted
     */
    private void aggregateToMap(Map<String,List<String>> dataSet,String a,String b)  {
        if(dataSet.get(a)!=null){
            dataSet.get(a).add(b);
        } else{
            List<String> data1=new ArrayList<>();
            data1.add(b);
            dataSet.put(a,data1);
        }

    }

    /**
     * This method has recurssion enabled to find deep nested links using the cache object provided
     *
     *
     * @param cache
     * @param key
     * @param dataLinks
     * @param interimDataGroup
     * @return  List<String> grouped data to caller
     */
    private List<String> printAllLinks(Map<String,List<String>> cache,String key,List<String> dataLinks,List<String> interimDataGroup){
        List<String> groupData=interimDataGroup;
        if(cache.get(key)!=null){
            //Check if data is not preset in map and add to list
            if(!groupData.contains(key)){
                //System.out.println("Adding entry in group "+key);
                groupData.add(key);
            }
            //Iterate all the deep links for the key provided
            for(String linkedData :dataLinks){
                if(!groupData.contains(linkedData) && cache.get(linkedData)!=null){
                    //System.out.println("Looking for deep links"+dataLinks);
                    //Doing a recurssive search for the value of key to find deep nested links
                    printAllLinks(cache,linkedData,cache.get(linkedData),groupData) ;
                } else{
                    //System.out.println("Data already in group"+linkedData);
                }
                //Remove the entry from cache to avoid cyclic loops
                cache.remove(linkedData);
            }
        }
        return groupData;

    }
}
