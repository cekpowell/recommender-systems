package cp6g18.Tasks;

import java.io.File;

import cp6g18.RecommenderSystem.Controller.CosineSimilarityRecommender;
import cp6g18.RecommenderSystem.Controller.FileWriter;
import cp6g18.RecommenderSystem.Model.RatingsDatabase;
import cp6g18.RecommenderSystem.Model.ArrayListRatingsDataset;
import cp6g18.RecommenderSystem.Model.HashMapRatingsDataset;
import cp6g18.RecommenderSystem.Model.RecommenderType;

// package cp6g18.Tasks;

// import java.io.File;

// import cp6g18.RecommenderSystem.Model.Dataset.Dataset;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * Main Class for Task 2 - \\ TODO
 */
public class Task2 {
    
    // constants
    private static final String DATBASE_FILENAME = "Task2" + File.separator + "database.db"; // the name of the database file storing the data
    private static final String TRAINING_TABLE_NAME = "training"; // the name of the table containing the training datas
    private static final String TESTING_TABLE_NAME = "testing"; // the name of the table containing the testing datas

    private static final String TRAINING_FILE = "Task2" + File.separator + "training.csv"; // Name of file containing training ratings
    private static final String TESTING_FILE = "Task2" + File.separator + "testing.csv"; // Name of file containing testing ratings

    private static final String RESULTS_FILE = "task2_results.csv"; // The name of the file the task 1 results will be written to

    //////////////////
    // RUNNING TASK //
    //////////////////

    /**
     * Runner funtion for Task 2.
     * 
     * // TODO
     */
    public static void run(){
        try{
            // logging
            System.out.println("\n=====================================");
            System.out.println(  "========== START OF TASK 2 ==========");
            System.out.println(  "=====================================");

            /////////////////
            // PREPERATION //
            /////////////////

            // logging
            System.out.println("\n============= PREPARING =============");

            // connecting to task 1 database
            RatingsDatabase database = new RatingsDatabase(Task2.DATBASE_FILENAME);

            // gathering training data
            HashMapRatingsDataset trainingDataset = database.loadHashMapRatingsDataset(Task2.TRAINING_TABLE_NAME, RecommenderType.ITEM_BASED);

            // gathering testing datas
            ArrayListRatingsDataset testingDataset = database.loadArrayListRatingsDataset(Task2.TESTING_TABLE_NAME);

            // creating recommender system
            CosineSimilarityRecommender recommender = new CosineSimilarityRecommender();

            //////////////
            // TRAINING //
            //////////////

            // logging
            System.out.println("\n============= TRAINING ==============");

            // training recommender
            recommender.train(trainingDataset);

            ////////////////////////
            // MAKING PREDICTIONS //
            ////////////////////////

            // logging
            System.out.println("\n============ PREDICTING =============");

            ArrayListRatingsDataset predictions = recommender.makePredictions(testingDataset);
            
            // writing to file
            FileWriter.writeObjectToFile(predictions, new File(Task2.RESULTS_FILE));

            // logging
            System.out.println("\n=====================================");
            System.out.println(  "=========== END OF TASK 2 ===========");
            System.out.println(  "=====================================\n");
        }
        catch(Exception e){
            System.out.println("Unable to run Task 2!\n" + 
                               "Cause : " + e.toString());
        }
    }
}