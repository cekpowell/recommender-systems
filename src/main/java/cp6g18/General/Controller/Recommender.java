package cp6g18.General.Controller;

import cp6g18.General.Model.TestingDataset;
import cp6g18.General.Model.TestingRating;
import cp6g18.General.Model.TrainingDataset;
import cp6g18.Tools.Logger;

/** 
 * A Recommender System.
 * 
 * All recommenders are types based on the type of the underyling training dataset.
 * 
 * The recommender defines methods for training the system using a training dataset, and predicting
 * ratings using a testing dataset.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public abstract class Recommender<T extends TrainingDataset>{

    // MEMBER VARIABLES //
    private T trainingDataset; // the training dataset used to train the recommender
    
    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     */
    public Recommender(){
        // initializing
        this.trainingDataset = null;
    }

    /////////////////////
    // TRAINING SYSTEM //
    /////////////////////

    /**
     * Trains the recommender using the provided training dataset.
     * 
     * @param trainingDataset The training dataset the recommender will be trained on.
     */
    public abstract void train(T trainingDataset);

    ////////////////////////
    // MAKING PREDICTIONS //
    ////////////////////////

    /**
     * Makes predictions for the provided testing dataset using the trained system.
     * 
     * @param dataset The testing dataset the predictions are being made for.
     * @return A TestingDataset object that contains the predictions.
     */
    public TestingDataset makePredictions(TestingDataset dataset){
        // logging
        Logger.logProcessStart("Making predictions");

        // creating new testing dataset for the result (so that original one is kept)
        TestingDataset predictions = new TestingDataset();

        // iterating through unknown datasets
        for(TestingRating rating : dataset.getRatings()){
            // gathering needed information
            int userID = rating.getUserID();
            int itemID = rating.getItemID();
            int timestamp = rating.getTimestamp();

            // gathering predicted rating for this entry
            float predictedRating = this.makePrediction(userID, itemID, timestamp);

            // adding new rating object to predictions
            predictions.addRating(userID, itemID, predictedRating, rating.getTimestamp());
        }

        // logging
        Logger.logProcessEnd("Predictions made successfully");

        // returning predictions
        return predictions;
    }

    /**
     * Calculates a predicted rating for the provided user-item pair.
     * 
     * @param userID The ID of the user in the rating.
     * @param itemID The ID of the item in the rating.
     * @return The predicted rating for the user-item pair.
     */
    protected abstract float makePrediction(int userID, int itemID, int timestamp);

    /////////////////////////
    // GETTERS AND SETTERS //
    /////////////////////////

    /**
     * Getter method for the recommender's training dataset.
     * 
     * @return The training dataset associated with this recommender.
     */
    public T getTrainingDataset(){
        return this.trainingDataset;
    }
}