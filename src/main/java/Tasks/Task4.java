package Tasks;

import java.io.File;

import General.Model.Database;
import General.Model.TestingDataset;
import MFRecommenderSystem.Controller.MFRecommender;
import MFRecommenderSystem.Model.MFTrainingDataset;
import Tools.FileHandler;
import Tools.Logger;

/**
 * Main Class for Task 4 - Trains an matrix factorisation recommender using a training dataset, 
 * and makes a set of predictions for a testing dataset.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public class Task4 {
    
    // CONSTANTS //
    private static final String DATBASE_FILENAME = "data" + File.separator + "Task4" + File.separator + "database.db"; // the name of the database file storing the data
    private static final String TRAINING_TABLE_NAME = "training"; // the name of the table containing the training datas
    private static final String TESTING_TABLE_NAME = "testing"; // the name of the table containing the testing datasName of file containing testing ratings
    private static final String RESULTS_FILE = "out" + File.separator + "Task4/results.csv"; // The name of the file the task 1 results will be written to

    // SYSTEM PARAMETERS //
    private static final float MIN_RATING = 1f; // the minimum rating that can be given to an item 
    private static final float MAX_RATING = 5f; // the maximum rating that can be given to an item
    private static final int FACTORS = 20; // the number of latent factors in the matricies.
    private static final int MIN_ITERATIONS = 20; // the minimum number of iterations the algorithm will perform.
    private static final float LEARNING_RATE = 0.01f; // the learning rate of the algorithm
    private static final int LEARNING_RATE_ADJUSTMENT_FREQUENCY = 5; // the learning rate will be adjusted every x iterations.
    private static final float LEARNING_RATE_ADJUSTMENT_FACTOR = 0.1f; // the percentage reduction in the learning rate that will occur every adjustmment.
    private static final float REGULARIZATION_RATE = 0.02f; // the regularization rate of the algorithm
    private static final int REGULARIZATION_RATE_ADJUSTMENT_FREQUENCY = 5; // the regularization rate will be adjusted every x iterations.
    private static final float REGULARIZATION_RATE_ADJUSTMENT_FACTOR = 0.1f; // the percentage reduction in the regularization rate that will occur every adjustmment.
    private static final float MEAN = 0.0f; // the mean of the gaussian distribution used to generate the initial numbers for the matricies
    private static final float VARIANCE = 0.01f; // the variance of the gaussian distribution used to generate the initial numbers for the matricies.

    //////////////////
    // RUNNING TASK //
    //////////////////

    /**
     * Runner funtion for Task 4.
     * 
     * Creates a new MFRecommender, trains the recommender using the training dataset, makes
     * predictions using the testing dataset, writes the results to a file.
     */
    public static void run(){
        try{
            // logging
            Logger.logTaskStart(4);

            ///////////////////////////
            // LOADING TRAINING DATA //
            ///////////////////////////

            // logging
            Logger.logSubTaskStart("LOADING TRAINING DATA");

            // connecting to task 1 database
            Database database = new Database(Task4.DATBASE_FILENAME);

            // gathering training data
            MFTrainingDataset trainingDataset = new MFTrainingDataset();
            database.loadRatingsDataset(trainingDataset, Task4.TRAINING_TABLE_NAME);

            // creating recommender system
            MFRecommender recommender = new MFRecommender(Task4.MIN_RATING,
                                                          Task4.MAX_RATING,
                                                          Task4.FACTORS, 
                                                          Task4.MIN_ITERATIONS,
                                                          Task4.LEARNING_RATE, 
                                                          Task4.LEARNING_RATE_ADJUSTMENT_FREQUENCY,
                                                          Task4.LEARNING_RATE_ADJUSTMENT_FACTOR,
                                                          Task4.REGULARIZATION_RATE, 
                                                          Task4.REGULARIZATION_RATE_ADJUSTMENT_FREQUENCY,
                                                          Task4.REGULARIZATION_RATE_ADJUSTMENT_FACTOR,
                                                          Task4.MEAN, 
                                                          Task4.VARIANCE);

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
            database.loadRatingsDataset(testingDataset, Task4.TESTING_TABLE_NAME);

            // logging
            Logger.logSubTaskEnd("LOADING TESTING DATA");

            ////////////////////////
            // MAKING PREDICTIONS //
            ////////////////////////

            // logging
            Logger.logSubTaskStart("PREDICTING");

            TestingDataset predictions = recommender.makePredictions(testingDataset);
            
            // writing to file
            FileHandler.writeTestingDatasetToFile(predictions, new File(Task4.RESULTS_FILE));

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
            Logger.logTaskEnd(4);
        }
        catch(Exception e){
            System.out.println("\nUnable to run Task 4!\n" + 
                               "Cause : " + e.toString() + "\n");
            e.printStackTrace();
        }
    }
}