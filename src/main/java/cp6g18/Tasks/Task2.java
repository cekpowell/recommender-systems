package cp6g18.Tasks;

import java.io.File;

import cp6g18.General.Model.Database;
import cp6g18.General.Model.TestingDataset;
import cp6g18.IBCFRecommenderSystem.Controller.IBCFRecommender;
import cp6g18.IBCFRecommenderSystem.Model.IBCFTrainingDataset;
import cp6g18.Tools.FileHandler;
import cp6g18.Tools.Logger;

/**
 * Main Class for Task 2 - Trains an item-based recommender using a training dataset, and makes
 * a set of predictions for a testing dataset.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public class Task2 {
    
    // FILE INFORMATION //
    private static final String DATBASE_FILENAME = "data" + File.separator + "Task2" + File.separator + "database.db"; // the name of the database file storing the data
    private static final String TRAINING_TABLE_NAME = "training"; // the name of the table containing the training datas
    private static final String TESTING_TABLE_NAME = "testing"; // the name of the table containing the testing data
    private static final String RESULTS_FILE = "out" + File.separator + "Task2/results.csv"; // The name of the file the task 1 results will be written to

    // SYSTEM PARAMETERS //
    private static final int SIGNIFICANCE_VALUE = 50; // The minimum number of co-rated users that must exist between an item pair when determining its similarity.
    private static final float MIN_SIMILARITY = 0.0f; // The minimum similarity that can be used when calculating predicted ratings.
    private static final float TEMPORAL_WEIGHT_FACTOR = 0.001f; // The decay rate for the temporal weight applied to the similarity and predicted ratings.

    //////////////////
    // RUNNING TASK //
    //////////////////

    /**
     * Runner funtion for Task 2.
     * 
     * Creates a new IBCFRecommender, trains the recommender using the training dataset, makes
     * predictions using the testing dataset, writes the results to a file.
     */
    public static void run(){
        try{
            // logging
            Logger.logTaskStart(2);

            ///////////////////////////
            // LOADING TRAINING DATA //
            ///////////////////////////

            // logging
            Logger.logSubTaskStart("LOADING TRAINING DATA");

            // connecting to task 1 database
            Database database = new Database(Task2.DATBASE_FILENAME);

            // gathering training data
            IBCFTrainingDataset trainingDataset = new IBCFTrainingDataset();
            database.loadRatingsDataset(trainingDataset, Task2.TRAINING_TABLE_NAME);

            // creating recommender system
            IBCFRecommender recommender = new IBCFRecommender(Task2.SIGNIFICANCE_VALUE, Task2.MIN_SIMILARITY, Task2.TEMPORAL_WEIGHT_FACTOR);

            // logging
            Logger.logSubTaskEnd("LOADING TRAINING DATA");

            //////////////
            // TRAINING //
            //////////////

            // logging
            Logger.logSubTaskStart("TRAINING");

            // training recommender
            recommender.train(trainingDataset);

            // clearing training dataset (free up memory)
            //trainingDataset.clear();

            // logging
            Logger.logSubTaskEnd("TRAINING");

            //////////////////////////
            // LOADING TESTING DATA //
            //////////////////////////

            // logging
            Logger.logSubTaskStart("LOADING TESTING DATA");

            // gathering testing datas
            TestingDataset testingDataset = new TestingDataset();
            database.loadRatingsDataset(testingDataset, Task2.TESTING_TABLE_NAME);

            // logging
            Logger.logSubTaskEnd("LOADING TESTING DATA");

            ////////////////////////
            // MAKING PREDICTIONS //
            ////////////////////////

            // logging
            Logger.logSubTaskStart("PREDICTING");

            TestingDataset predictions = recommender.makePredictions(testingDataset);
            
            // writing to file
            FileHandler.writeObjectToFileAsString(predictions, new File(Task2.RESULTS_FILE));

            // logging
            Logger.logSubTaskEnd("PREDICTING");

            ////////////////
            // FINISHING //
            ///////////////

            // logging
            Logger.logSubTaskStart("FINISHING");

            // closing the database
            database.close();

            // logging
            Logger.logSubTaskEnd("FINISHING");

            // logging
            Logger.logTaskEnd(2);
        }
        catch(Exception e){
            System.out.println("\nUnable to run Task 2!\n" + 
                               "Cause : " + e.toString() + "\n");
            e.printStackTrace();
        }
    }
}