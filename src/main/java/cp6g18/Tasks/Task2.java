package cp6g18.Tasks;

import java.io.File;

import cp6g18.CFRecommenderSystem.Controller.IBRecommender;
import cp6g18.CFRecommenderSystem.Model.Database;
import cp6g18.CFRecommenderSystem.Model.IBTrainingDataset;
import cp6g18.CFRecommenderSystem.Model.TestingDataset;
import cp6g18.Tools.FileHandler;
import cp6g18.Tools.Logger;

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
            Logger.logTaskStart(2);

            ///////////////////////////
            // LOADING TRAINING DATA //
            ///////////////////////////

            // logging
            Logger.logSubTaskStart("LOADING TRAINING DATA");

            // connecting to task 1 database
            Database database = new Database(Task2.DATBASE_FILENAME);

            // gathering training data
            IBTrainingDataset trainingDataset = new IBTrainingDataset();
            database.loadRatingsDataset(trainingDataset, Task2.TRAINING_TABLE_NAME);

            // creating recommender system
            IBRecommender recommender = new IBRecommender();

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