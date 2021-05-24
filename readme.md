Number RelationShip Aggregation App

This app accepts list as a data set and also can be extended to take any data source.

Input for the App : 

The input for app is two column csv of type string.

Output : 

Aggregates and prints the group with relation

Sample input data :    "1,2\n2,3\n3,4\n7,5\n4,6"
Sample output data : 

Thread : pool-1-thread-1 Final Group Details[1, 2, 3, 4, 6]
Thread : pool-1-thread-1 Final Group Details[5, 7]
      
Application logic overview :
  1) Reads the csv of two column dataset and prepares an interim map of relation using simple key ,value dataStructure
  2) Prepares a cached object to refer while iterating the original collection.
  3) Uses recursion to iterate the cached data against the input data provided.
  4) The app employs fixed thread pool executor config of 2 and can be extended to maximise performance.


  








