package cp6g18.CFRecommenderSystem.Controller;

import java.util.HashMap;
import java.util.Set;

import cp6g18.CFRecommenderSystem.Model.IBTrainingDataset;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
 */
public class IBRecommender extends Recommender<IBTrainingDataset>{

    //////////////////
    // INITIALISING //
    //////////////////

    /**
     * Class constructor.
     */
    public IBRecommender(){
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
    public float getSimiarity(IBTrainingDataset trainingDataset, HashMap<Integer, Float> userAverageRatings, int item1ID, int item2ID){
        /**
         * - Find set of users that rated both item 1 and item 2
         * - Calculate numerator of equation
         * - Calculator demoninator of equation
         * - return result
         */

        /////////////////
        // PREPERATION //
        /////////////////

        // variable to store similarity
        float similarity = 0f;

        // list of user's who have rated both items
        Set<Integer> commonUsers = trainingDataset.getUsersWhoRatedItems(item1ID, item2ID);

        // if no common users, similarity is 0, so can return similarity at this point
        if(commonUsers.size() == 0){
            return similarity;
        }

        /////////////////
        // CALCULATION //
        /////////////////

        // variables to store calculation segments
        float numerator = 0f;
        float denominator = 0f;
        float denominatorLhs = 0f;
        float denominatorRhs = 0f;

        // iterating over all users and summing the segments
        for(int user : commonUsers){
            // getting user's average rating
            float userAverageRating = userAverageRatings.get(user);

            // NUMERATOR //

            // numerator
            numerator += (trainingDataset.getUserRatingOfItem(user, item1ID) - userAverageRating) * (trainingDataset.getUserRatingOfItem(user, item2ID) - userAverageRating);

            // DENOMINATOR //

            // lhs
            denominatorLhs += (trainingDataset.getUserRatingOfItem(user, item1ID) - userAverageRating) * (trainingDataset.getUserRatingOfItem(user, item1ID) - userAverageRating);

            // rhs
            denominatorRhs += (trainingDataset.getUserRatingOfItem(user, item2ID) - userAverageRating) * (trainingDataset.getUserRatingOfItem(user, item2ID) - userAverageRating);
        }

        // finalising calculations
        denominatorLhs = (float) Math.sqrt((double) denominatorLhs);
        denominatorRhs = (float) Math.sqrt((double) denominatorRhs);
        denominator = denominatorLhs * denominatorRhs;

        // COMBINING NUMERATOR AND DEMONINATOR //

        similarity = numerator / denominator;

        //////////////////////
        // RETURNING RESULT //
        //////////////////////

        // returning the similarity
        return similarity;
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
     * @return
     */
    protected float makePrediction(int userID, int itemID){
        // TODO
        return 0f;
    }
}