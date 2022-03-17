package cp6g18.CFRecommenderSystem.Controller;

import java.util.HashMap;

import cp6g18.CFRecommenderSystem.Model.Rating;
import cp6g18.CFRecommenderSystem.Model.SimilarityMatrix;
import cp6g18.CFRecommenderSystem.Model.TestingDataset;
import cp6g18.CFRecommenderSystem.Model.TrainingDataset;
import cp6g18.Tools.Logger;

/** 
 * An abstract class that represents a type of recommender.
 * 
 * All recommenders are types based on the type of the underyling training dataset.
 * 
 * Defines methods for the training of the recommender, and its use to make predictions.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public abstract class Recommender<T extends TrainingDataset>{

    // member variables
    private T trainingDataset; // the training dataset used to train the recommender
    private SimilarityMatrix model; // the model learnt from the training dataset
    private boolean isTrained; // stores whether or not the recommender has been trained yet
    
    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     */
    public Recommender(){
        // initializing
        this.trainingDataset = null;
        this.model = null;
        this.isTrained = false;
    }

    /////////////////////
    // TRAINING SYSTEM //
    /////////////////////

    /**
     * Trains the recommender using the provided training dataset.
     * 
     * @param trainingDataset The training dataset the recommender will be trained on.
     */
    public void train(T trainingDataset){
        // informing
        Logger.logProcessStart("Training recommender system");

        ///////////////
        // PREPARING //
        ///////////////

        // setting the training dataset
        this.trainingDataset = trainingDataset;

        // creating new similarity matrix instance
        SimilarityMatrix model = new SimilarityMatrix();

        // gathering average user ratings
        HashMap<Integer, Float> userAverageRatings = trainingDataset.getAverageUserRatings();

        /////////////////
        // CALCULATING //
        /////////////////

        // finding similarity between every item
        for(int item1 : trainingDataset.getItems()){
            for(int item2 : trainingDataset.getItems()){
                // calculating if not yet calculated
                if(!model.hasSimilarity(item1, item2)){
                    // calculating similarity
                    float similarity = this.getSimiarity(trainingDataset, userAverageRatings, item1, item2);

                    // adding similarity to the matrix
                    model.setSimilarity(item1, item2, similarity);
                }
            }
        }

        ///////////////
        // FINISHING //
        ///////////////

        // setting model into system
        this.setModel(model);

        // setting the status of the training variable
        this.isTrained = true;

        // informing
        Logger.logProcessEnd("Recommender system successfully trained");
    }

    /**
     * Gathers the similarity between two objects (i.e., items or users).
     * 
     * @param trainingDataset The training dataset being used to determine the similarity.
     * @param object1D The ID of the first object the similarity is being calculated for.
     * @param object2ID The ID of the second object the similarity is being calclated for.
     * @return 
     */
    protected abstract float getSimiarity(T trainingDataset, HashMap<Integer, Float> userAverageRatings, int object1ID, int object2ID);

    ////////////////////////
    // MAKING PREDICTIONS //
    ////////////////////////

    /**
     * Makes predictions for the provided testing dataset using the trained system.
     * 
     * @param dataset The testing dataset the predictions are being made for.
     * @return A TestingDataset object that contains the predictions.
     * @throws Exception If the recommender has not been trained yet.
     */
    public TestingDataset makePredictions(TestingDataset dataset) throws Exception{
        // logging
        Logger.logProcessStart("Making predictions");

        // checking system has been trained               
        if(this.isTrained){
            // creating new testing dataset for the result (so that original one is kept)
            TestingDataset predictions = new TestingDataset();

            // iterating through unknown datasets
            for(Rating rating : dataset.getRatings()){
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
        else{
            throw new Exception("Recommender not yet trained!");
        }
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

    /**
     * Getter method for the recommender's model.
     * 
     * @return The model associated with this recommender.
     */
    public SimilarityMatrix getModel(){
        return this.model;
    }

    /**
     * Setter method for the recommender's model.
     * 
     * @param model The new model to be associated with this recommender.
     */
    public void setModel(SimilarityMatrix model){
        this.model = model;
    }
}