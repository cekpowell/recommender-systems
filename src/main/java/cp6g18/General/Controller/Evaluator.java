package cp6g18.General.Controller;

import java.util.ArrayList;

import cp6g18.General.Model.Database;
import cp6g18.General.Model.Evaluation;
import cp6g18.General.Model.TestingDataset;
import cp6g18.General.Model.TestingRating;
import cp6g18.General.Model.TrainingDataset;
import cp6g18.Tools.Logger;

/**
 * Performs evaluation of a recommender system by comparing it's predicted ratings to a set
 * of ground truths, using standard evaluation metrics (MSE, RMSE and MAE).
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public class Evaluator {

    ////////////////
    // EVALUATION //
    ////////////////

    /**
     * Evaluates a recommender of the provided type using the provided database and training table.
     * 
     * Algorithm:
     *  - Create a new training and testing datasets using the training table within the database.
     *  - Train the recommender on the new training data.
     *  - Test the recommender on the new testing data.
     *  - Evalaute the performance of the recommender using the ground truths.
     * 
     * @param recommender
     * @param ratingsDatabase
     * @return
     */
    public static <T extends TrainingDataset> Evaluation evaluateRecommender(Recommender<T> recommender, T trainingDataset, Database database, String trainingTableName) throws Exception{

        ///////////////
        // PREPARING //
        ///////////////

        // names for new training and testing tables
        String newTrainingTableName = "NEWtraining";
        String newTestingTableName = "NEWtesting";
        String newTestingTruthsTableName = "NEWtestingTRUTHS";

        // creating new trainning and testing sets
        database.createNewTrainingAndTestingTables(trainingTableName, newTrainingTableName, newTestingTableName, newTestingTruthsTableName, 10);

        // loadinng new training set
        database.loadRatingsDataset(trainingDataset, newTrainingTableName);

        // loading new testing set
        TestingDataset testingDataset = new TestingDataset();
        database.loadRatingsDataset(testingDataset, newTestingTableName);

        // loading truths
        TestingDataset truths = new TestingDataset();
        database.loadRatingsDataset(truths, newTestingTruthsTableName);

        // dropping new training and testing tables from database
        database.dropTable(newTrainingTableName);
        database.dropTable(newTestingTableName);
        database.dropTable(newTestingTruthsTableName);

        ////////////////
        // PREDICTING //
        ////////////////

        // training recommender
        recommender.train(trainingDataset);

        // making predictions
        TestingDataset predictions = recommender.makePredictions(testingDataset);

        ////////////////
        // EVALUATING //
        ////////////////

        // gathering evaluation
        Evaluation evaluation = Evaluator.evaluatePredictions(predictions.getRatings(), truths.getRatings());

        ///////////////
        // RETURNING //
        ///////////////
        
        // returning evaluation
        return evaluation;
    }

    /**
     * Evaluates a set of predictions and true ratings according to standard
     * evaluation metrics.
     * 
     * @param predictions The predicted ratings.
     * @param truths The true ratings.
     * @return An Evaluation object containing the evaluation metrics.
     */
    public static Evaluation evaluatePredictions(ArrayList<TestingRating> predictions, ArrayList<TestingRating> truths){
        // logging
        Logger.logProcessStart("Evaluating predictions against truths");

        // gathering evaluation metrics
        float mse = Evaluator.getMSE(predictions, truths);
        float rmse = Evaluator.getRMSE(predictions, truths);
        float mae = Evaluator.getMAE(predictions, truths);

        // creating evaluation object
        Evaluation evaluation = new Evaluation(mse, rmse, mae);

        // logging
        Logger.logProcessEnd("Evaluation successfully completed");

        // returning evaluation
        return evaluation;
    }

    //////////////////////////////
    // MEAN SQUARED ERROR (MSE) //
    //////////////////////////////

    /**
     * Computes the MEAN SQUARED ERROR for a set of predicted ratings.
     * 
     * Algorithm:
     *  - error = Obersved - predicted
     *  - squared error = difference ^ 2
     *  - Sum of squared error = sum of squared error for all items
     *  - MSE = Sum of squared error / number of entries in dataset
     * 
     * @param predictions The predicted ratings.
     * @param truths The actual ratings.
     * @return The mean squared error for the predictions.
     */
    private static float getMSE(ArrayList<TestingRating> predictions, ArrayList<TestingRating> truths){
        // mae variable
        float mse = 0f;

        // summing squared errors
        float sumOfSquaredErrors = 0f;
        for(int i = 0; i < predictions.size(); i++){
            float truth = truths.get(i).getRating();
            float predicted = predictions.get(i).getRating();

            float error = truth - predicted;

            float squaredError = error * error;
        
            sumOfSquaredErrors += squaredError;
        }

        // finding average error
        mse = sumOfSquaredErrors / predictions.size();

        // returning result
        return mse;
    }

    ////////////////////////////////////
    // ROOT MEAN SQUARED ERROR (RMSE) //
    ////////////////////////////////////

    /**
     * Computes the ROOT MEAN SQUARED ERROR for a set of predicted ratings.
     * 
     * Algorithm:
     *  - error = Obersved - predicted
     *  - squared error = difference ^ 2
     *  - Sum of squared error = squared error for all items
     *  - MSE = Sum of squared error / number of entries in dataset
     *  - RMSE = Square root of MSE
     *
     * @param predictions The predicted ratings.
     * @param truths The actual ratings.
     * @return The root smean squared error for the predictions.
     */
    private static float getRMSE(ArrayList<TestingRating> predictions, ArrayList<TestingRating> truths){
        // rmse variable
        float rmse = 0;

        // gathering mse value
        float mse = Evaluator.getMSE(predictions, truths);

        // calculating rmse value
        rmse = (float) Math.sqrt(mse);

        // returning result
        return rmse;
    }

    ///////////////////////////////
    // MEAN ABSOLUTE ERROR (MAE) //
    ///////////////////////////////

    /**
     * Computes the MEAN ABSOLUTE ERROR for a set of predicted ratings.
     * 
     * Algorithm:
     *  - error = Obersved - predicted
     *  - abs error = absolute of error
     *  - Sum of absolute error = sum of absolute error for all entries.
     *  - MAE = sum of absolute error / number of entries in dataset
     * 
     * @param predictions The predicted ratings.
     * @param truths The actual ratings.
     * @return The mean absolute error for the predictions.
     */
    private static float getMAE(ArrayList<TestingRating> predictions, ArrayList<TestingRating> truths){
        // mae variable
        float mae = 0;

        // summing errors
        float sumOfErrors = 0;
        for(int i = 0; i < predictions.size(); i++){
            float truth = truths.get(i).getRating();
            float predicted = predictions.get(i).getRating();

            float error = truth - predicted;

            float absError = Math.abs(error);

            sumOfErrors += absError;
        }

        // finding average error
        mae = sumOfErrors / predictions.size();

        // returning result
        return mae;
    }
}