package cp6g18.CFRecommenderSystem.Controller;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

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

        // CHECKING FOR INVALID DENOMINATOR // denominator can have value of 0, which causes NaN error // TODO look as to why this can happen and what it means
        if(denominator == 0){
            return similarity;
        }

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

        /**
         * Things that could go wrong:
         * - GENERAL:
         *  - Item cold start - This item has not been seen before, so there are no similar itms.
         *      - Return sensible prediction for the item (i.e., user average rating).
         *  - User cold start - This user has not rated any items.
         *      - Return sensible prediction for the item (i.e., item average rating).
         * - Both user and item have not been seen before
         *      - Return sensble prediction for the item (e.g., average rating of all items/all users)
         * - WHEN LOOPING THROUGH SIMILAR ITEMS:
         *  - The similar item has a negative similarity.
         *  - The user hasnt rated the similar item.
         */

        /////////////////
        // PREPERATION //
        /////////////////

        // variable to store prediction
        Float prediction = 0f;

        // getting simiilarity to other ites
        HashMap<Integer, Float> similarItems = this.getModel().getSimilaritiesForObject(itemID);

        // CHECKING FOR ITEM COLD START //
        if(similarItems == null){
            System.out.println("Cold start for ITEM " + itemID);

            return 0f; // TODO return sensible prediction - e.g., user's average rating
        }

        // variables to store sums
        float numerator = 0f;
        float denominator = 0f;

        /////////////////
        // CALCULATION //
        /////////////////

        // iterating through items
        for(Entry<Integer, Float> similarItem : similarItems.entrySet()){
            int similarItemID = similarItem.getKey();
            Float similarityOfSimilarItem = similarItem.getValue();

            // CHECKING FOR DISSIMILARITY //
            if(similarityOfSimilarItem <= 0){
                // dont want to consider items that are not similar
                // within the prediction
                continue;
            }

            // NUMERATOR //

            // getting user rating of this item
            Float userRatingOfSimilarItem = this.getTrainingDataset().getUserRatingOfItem(userID, similarItemID);

            // CHECKING USER HAS RATED SIMILAR ITEM //
            if(userRatingOfSimilarItem == null){
                // cant include this item if the user has not rated it // TODO use the item's average rating instead
                continue;
            }

            // addding to numerator
            numerator += ((float) similarityOfSimilarItem * userRatingOfSimilarItem);

            // DENOMINATOR //

            // adding to denominator
            denominator += similarityOfSimilarItem;
        }

        // CHECKING FOR USER COLD START // (if denominator and numerator are both zero, they were nevr incremented cus the user had no ratings, thus it is user cold start)
        if(denominator == 0 && numerator == 0){
            return 0f; // TODO return sensble prediction - i.e., average rating of item.
        }

        // getting final results
        prediction = numerator / denominator;

        //////////////
        // RETURING //
        //////////////

        // returning prediction
        return prediction;
    }
}