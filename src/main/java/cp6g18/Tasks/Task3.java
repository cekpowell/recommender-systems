package cp6g18.Tasks;

import java.io.File;

import cp6g18.General.Model.Database;
import cp6g18.General.Model.TestingDataset;
import cp6g18.MFRecommenderSystem.Controller.MFRecommender;
import cp6g18.MFRecommenderSystem.Model.MFTrainingDataset;
import cp6g18.Tools.FileHandler;
import cp6g18.Tools.Logger;

/**
 * Main Class for Task 2 - Trains an matrix factorisation recommender using a training dataset, 
 * and makes a set of predictions for a testing dataset.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public class Task3 {
    
    // CONSTANTS //
    private static final String DATBASE_FILENAME = "data" + File.separator + "Task3" + File.separator + "database.db"; // the name of the database file storing the data
    private static final String TRAINING_TABLE_NAME = "training"; // the name of the table containing the training datas
    private static final String TESTING_TABLE_NAME = "testing"; // the name of the table containing the testing datasName of file containing testing ratings
    private static final String RESULTS_FILE = "out" + File.separator + "Task3/results.csv"; // The name of the file the task 1 results will be written to

    // SYSTEM PARAMETERS //
    private static final int FACTORS = 40; // the number of latent factors in the matricies.
    private static final int MIN_ITERATIONS = 10; // the minimum number of iterations the algorithm will perform.
    private static final int MAX_ITERATIONS = 100; // the maximum number of iterations the algorithm will perform.
    private static final float MIN_CHANGE_IN_MAE = 0.0001f; // the change MAE between two iterations that will cause the recommender to stop training if it is reached.
    private static final float LEARNING_RATE = 0.1f; // the learning rate of the algorithm
    private static final float REGULARIZATION_RATE = 0.0f; // the regularization rate of the algorithm
    private static final float MEAN = 0.0f; // the mean of the gaussian distribution used to generate the initial numbers for the matricies
    private static final float VARIANCE = 1.0f; // the variance of the gaussian distribution used to generate the initial numbers for the matricies.

    //////////////////
    // RUNNING TASK //
    //////////////////

    /**
     * Runner funtion for Task 2.
     * 
     * Creates a new MFRecommender, trains the recommender using the training dataset, makes
     * predictions using the testing dataset, writes the results to a file.
     */
    public static void run(){
        try{
            // logging
            Logger.logTaskStart(3);

            ///////////////////////////
            // LOADING TRAINING DATA //
            ///////////////////////////

            // logging
            Logger.logSubTaskStart("LOADING TRAINING DATA");

            // connecting to task 1 database
            Database database = new Database(Task3.DATBASE_FILENAME);

            // gathering training data
            MFTrainingDataset trainingDataset = new MFTrainingDataset();
            database.loadRatingsDataset(trainingDataset, Task3.TRAINING_TABLE_NAME);

            // creating recommender system
            MFRecommender recommender = new MFRecommender(Task3.FACTORS, 
                                                          Task3.MIN_ITERATIONS, 
                                                          Task3.MAX_ITERATIONS, 
                                                          Task3.MIN_CHANGE_IN_MAE, 
                                                          Task3.LEARNING_RATE, 
                                                          Task3.REGULARIZATION_RATE, 
                                                          Task3.MEAN, 
                                                          Task3.VARIANCE);

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
            database.loadRatingsDataset(testingDataset, Task3.TESTING_TABLE_NAME);

            // logging
            Logger.logSubTaskEnd("LOADING TESTING DATA");

            ////////////////////////
            // MAKING PREDICTIONS //
            ////////////////////////

            // logging
            Logger.logSubTaskStart("PREDICTING");

            TestingDataset predictions = recommender.makePredictions(testingDataset);
            
            // writing to file
            FileHandler.writeObjectToFileAsString(predictions, new File(Task3.RESULTS_FILE));

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
            System.out.println("\nUnable to run Task 3!\n" + 
                               "Cause : " + e.toString() + "\n");
            e.printStackTrace();
        }
    }
}