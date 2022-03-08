package cp6g18.CFRecommenderSystem.Controller;

import java.util.HashMap;

import cp6g18.CFRecommenderSystem.Model.Rating;
import cp6g18.CFRecommenderSystem.Model.SimilarityMatrix;
import cp6g18.CFRecommenderSystem.Model.TestingDataset;
import cp6g18.CFRecommenderSystem.Model.TrainingDataset;
import cp6g18.Tools.Logger;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
 */
public abstract class Recommender<T extends TrainingDataset>{

    // member variables
    private boolean isTrained;
    private SimilarityMatrix model;
    
    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     */
    public Recommender(){
        // initializing
        this.model = null;
        this.isTrained = false;
    }

    /////////////////////
    // TRAINING SYSTEM //
    /////////////////////

    /**
     * // TODO
     * 
     * @param trainingDataset
     */
    public void train(T trainingDataset){
        // informing
        Logger.logProcessStart("Training recommender system");

        ///////////////
        // PREPARING //
        ///////////////

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
     * // TODO
     * 
     * @param trainingDataset
     * @param item1ID
     * @param item2ID
     * @return 
     */
    protected abstract float getSimiarity(T trainingDataset, HashMap<Integer, Float> userAverageRatings, int item1ID, int item2ID);

    ////////////////////////
    // MAKING PREDICTIONS //
    ////////////////////////

    /**
     * // TODO
     * 
     * @param dataset
     * @return
     * @throws
     */
    public TestingDataset makePredictions(TestingDataset dataset) throws Exception{
        // checking system has been trained               
        if(this.isTrained){
            // creating new testing dataset for the result (so that original one is kept)
            TestingDataset predictions = new TestingDataset();

            // iterating through unknown datasets
            for(Rating rating : dataset.getRatings()){
                // gathering needed information
                int userID = rating.getUserID();
                int itemID = rating.getItemID();

                // gathering predicted rating for this entry
                float predictedRating = this.makePrediction(userID, itemID);

                // adding new rating object to predictions
                predictions.addRating(userID, itemID, predictedRating, rating.getTimestamp());
            }

            // returning predictions
            return predictions;
        }
        else{
            throw new Exception("Recommender not yet trained!");
        }
    }

    /**
     * // TODO
     * 
     * @param model
     * @param userID
     * @param itemID
     * @return
     */
    protected abstract float makePrediction(int userID, int itemID);

    /////////////////////////
    // GETTERS AND SETTERS //
    /////////////////////////

    /**
     * // TODO
     * 
     * @return
     */
    public SimilarityMatrix getModel(){
        return this.model;
    }

    public void setModel(SimilarityMatrix model){
        this.model = model;
    }
}