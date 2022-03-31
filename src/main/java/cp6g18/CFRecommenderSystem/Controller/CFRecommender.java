package cp6g18.CFRecommenderSystem.Controller;

import java.util.HashMap;

import cp6g18.General.Controller.Recommender;
import cp6g18.CFRecommenderSystem.Model.CFSimilarityMatrix;
import cp6g18.CFRecommenderSystem.Model.CFTrainingDataset;
import cp6g18.Tools.Logger;

/** 
 * A collaborative filtering recommender
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public abstract class CFRecommender<T extends CFTrainingDataset> extends Recommender<T>{

    // MEMBER VARIABLES //
    private T trainingDataset; // the training dataset used to train the recommender
    private CFSimilarityMatrix model; // the model learnt from the training dataset
    
    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     */
    public CFRecommender(){
        // initializing
        super();
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
    public void train(T trainingDataset){
        // informing
        Logger.logProcessStart("Training recommender system");

        ///////////////
        // PREPARING //
        ///////////////

        // setting the training dataset
        this.trainingDataset = trainingDataset;

        // creating new similarity matrix instance
        CFSimilarityMatrix model = new CFSimilarityMatrix();

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
    public CFSimilarityMatrix getModel(){
        return this.model;
    }

    /**
     * Setter method for the recommender's model.
     * 
     * @param model The new model to be associated with this recommender.
     */
    public void setModel(CFSimilarityMatrix model){
        this.model = model;
    }
}