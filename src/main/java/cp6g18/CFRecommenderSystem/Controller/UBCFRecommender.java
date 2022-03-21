package cp6g18.CFRecommenderSystem.Controller;

import java.util.HashMap;

import cp6g18.CFRecommenderSystem.Model.UBCFTrainingDataset;

/**
 * // TODO
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public class UBCFRecommender extends CFRecommender<UBCFTrainingDataset>{

    //////////////////
    // INITIALISING //
    //////////////////

    /**
     * Class constructor.
     */
    public UBCFRecommender(){
        // initializing
        super();
    }

    /////////////////////////////
    // SIMILARITY CALCULATIONS //
    /////////////////////////////

    /**
     * // TODO
     * 
     * @param trainingDataset
     * @param item1ID
     * @param item2ID
     * @return 
     */
    public float getSimiarity(UBCFTrainingDataset trainingDataset, HashMap<Integer, Float> userAverageRatings, int item1ID, int item2ID){
        // TODO
        return 0f;
    }

    ////////////////////////////////////
    // PREDICTED RATINGS CALCULATIONS //
    ////////////////////////////////////

    /**
     * // TODO
     * 
     * @param model
     * @param userID
     * @param itemID
     * @param timestamp
     * @return
     */
    protected float makePrediction(int userID, int itemID, int timestamp){
        // TODO
        return 0f;
    }
}