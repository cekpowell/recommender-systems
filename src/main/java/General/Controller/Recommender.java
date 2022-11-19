package General.Controller;

import General.Model.TestingDataset;
import General.Model.TestingRating;
import General.Model.TrainingDataset;
import Tools.Logger;

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
    // CONSTANTS //
    private float minRating; // The minimum rating that can be given to an item
    private float maxRating; // The maximum rating that can be given to an item
    
    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     * 
     * @param minRating The minimum rating that can be given to an item
     * @param maxRating The maximum rating that can be given to an item
     */
    public Recommender(float minRating, float maxRating){
        // initializing
        this.trainingDataset = null;
        this.minRating = minRating;
        this.maxRating = maxRating;
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

    ////////////////////////////////
    // CALCULATION HELPER METHODS //
    ////////////////////////////////

    /**
     * Converts the given value to a whole number.
     * 
     * @param number The value being converted to a whole number.
     * @return The value rounded to the nearest whole number.
     */
    public static float convertToWholeNumber(float value){
        return (float) Math.round(value);
    }

    /**
     * Returns a rating within the ratings bound permitted by this recommender.
     * 
     * This rating is within the minRating and maxRating properties of the recommender.
     * 
     * @param rating The rating being forced into its bounds.
     * @return The rating within the bounds of the ratings permitted by this recommender.
     */
    public float forceRatingWithinBounds(float rating){
        // rating too small - returning minimum allowed rating
        if(rating < this.minRating){
            return this.minRating;
        }
        // rating too big - returning maximum allowed rating
        else if(rating > this.maxRating){
            return this.maxRating;
        }
        // rating okay - returning rating.
        else{
            return rating;
        }
    }

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

    /**
     * Getter method for the minimum rating that can be given to an item by this recommender.
     * 
     * @return The minimum rating that can be given to an item by this recommender.
     */
    public float getMinRating(){
        return this.minRating;
    }

    /**
     * Getter method for the maximum rating that can be given to an item by this recommender.
     * 
     * @return The maximum rating that can be given to an item by this recommender.
     */
    public float getMaxRating(){
        return this.maxRating;
    }
}