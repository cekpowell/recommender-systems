package cp6g18.MFRecommenderSystem.Controller;

import cp6g18.General.Controller.Recommender;
import cp6g18.MFRecommenderSystem.Model.MFModel;
import cp6g18.MFRecommenderSystem.Model.MFTrainingDataset;
import cp6g18.Tools.Logger;

/**
 * A Matrix Factorisation Recommender.
 */
public abstract class MFRecommender<T extends MFTrainingDataset> extends Recommender<T>{

    // MEMBER VARIABLES //
    private T trainingDataset; // the training dataset used to train the recommender
    private MFModel model; // the model learnt from the training dataset
    
    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     */
    public MFRecommender(){
        // initializing
        this.trainingDataset = null;
        this.model = null;
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

        // setting up the model
        // TODO

        /////////////////
        // CALCULATING //
        /////////////////

        // TODO

        ///////////////
        // FINISHING //
        ///////////////

        // setting model into system
        this.setModel(model);

        // informing
        Logger.logProcessEnd("Recommender system successfully trained");
    }

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
    public MFModel getModel(){
        return this.model;
    }

    /**
     * Setter method for the recommender's model.
     * 
     * @param model The new model to be associated with this recommender.
     */
    public void setModel(MFModel model){
        this.model = model;
    }
}
