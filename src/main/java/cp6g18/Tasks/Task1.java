package cp6g18.Tasks;

import java.io.File;

import cp6g18.CFRecommenderSystem.Controller.Evaluator;
import cp6g18.CFRecommenderSystem.Model.Database;
import cp6g18.CFRecommenderSystem.Model.Evaluation;
import cp6g18.CFRecommenderSystem.Model.TestingDataset;
import cp6g18.Tools.FileHandler;
import cp6g18.Tools.Logger;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * Main Class for Task 1 - finds the MSE, RMSE and MAE for a set of predictions
 * and truth ratings.
 */
public class Task1 {
    
    // constants
    private static final String DATBASE_FILENAME = "data" + File.separator + "Task1" + File.separator + "database.db"; // the name of the database file storing the data
    private static final String PREDICTIONS_TABLE_NAME = "predictions"; // the name of the table containing the predictions
    private static final String TRUTHS_TABLE_NAME = "truths"; // the name of the table containing the truths

    private static final String PREDICTIONS_FILE = "data" + File.separator + "Task1" + File.separator + "predictions.csv"; // Name of file containing rating predictions
    private static final String TRUTHS_FILE = "data" + File.separator + "Task1" + File.separator + "truths.csv"; // Name of file containing rating truths

    private static final String RESULTS_FILE = "out" + File.separator  + "Task1/results.csv"; // The name of the file the task 1 results will be written to

    //////////////////
    // RUNNING TASK //
    //////////////////

    /**
     * Runner funtion for Task 1.
     * 
     * Algorithm:
     * - Loads the datasets from the resoures.
     * - Gathers the evaluation metrics.
     * - Outputs results to terminal.
     * - Logs results to file.
     */
    public static void run(){
        try{
            // logging
            Logger.logTaskStart(1);

            /////////////////
            // PREPERATION //
            /////////////////

            // logging
            Logger.logSubTaskStart("PREPARING");

            // connecting to task 1 database
            Database database = new Database(Task1.DATBASE_FILENAME);

            // gathering predictions
            TestingDataset predictions = new TestingDataset();
            database.loadRatingsDataset(predictions, Task1.PREDICTIONS_TABLE_NAME);

            // gathering truths
            TestingDataset truths = new TestingDataset();
            database.loadRatingsDataset(truths, Task1.TRUTHS_TABLE_NAME);

            // logging
            Logger.logSubTaskEnd("PREPARING");

            ////////////////
            // EVALUATING //
            ////////////////

            // logging
            Logger.logSubTaskStart("EVALUATING");

            Evaluation evaluation = Evaluator.evaluatePredictions(predictions.getRatings(), truths.getRatings());

            // logging
            Logger.logSubTaskEnd("EVALUATING");

            ////////////////////////
            // RECORDING RESULTS //
            ////////////////////////

            // logging
            Logger.logSubTaskStart("RECORDING");
            
            // printing to screen
            System.out.println("\nTask 1 Result : " + evaluation.toString());
            
            // writing to file
            FileHandler.writeObjectToFileAsString(evaluation, new File(Task1.RESULTS_FILE));

            // logging
            Logger.logSubTaskEnd("RECORDING");

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
            Logger.logTaskEnd(1);
        }
        catch(Exception e){
            System.out.println("\nUnable to run Task 1!\n" + 
                               "Cause : " + e.toString() + "\n");
            e.printStackTrace();
        }
    }
}