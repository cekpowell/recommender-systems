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
 * TODO
 */
public class IBRecommender extends Recommender<IBTrainingDataset>{

    // CONSTANTS //
    private static final int SIGNIFICANCE_VALUE = 50; // TODO
    private static final float MIN_SIMILARITY = 0.0f; // TODO
    private static final float TEMPORAL_WEIGHT_FACTOR = 0.001f; // TODO

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
     * TODO
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
         * 
         * - Edge Cases:
         *  - There are no users that have rated both items - similarity of zero.
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

            // getting timestamp of ratings
            int item1RatingTimestamp = trainingDataset.getTimestampOfRating(user, item1ID);
            int item2RatingTimestamp = trainingDataset.getTimestampOfRating(user, item2ID);

            // weighting function
            // f(t_ui) = e ^^ (-a (| t_uj - t_ui |))
            // 0 < a < 1 - higher values of a = weight more sensitive to time, having a = 0 : no time consideration at all
            float timeDif = Math.abs(item1RatingTimestamp - item2RatingTimestamp);
            float temporalWeight = (float) Math.exp((-1 * IBRecommender.TEMPORAL_WEIGHT_FACTOR) * timeDif);

            // NUMERATOR //

            // numerator
            numerator += ((trainingDataset.getUserRatingOfItem(user, item1ID) - userAverageRating) * temporalWeight) * ((trainingDataset.getUserRatingOfItem(user, item2ID) - userAverageRating) * temporalWeight);

            // DENOMINATOR //

            // lhs
            denominatorLhs += ((trainingDataset.getUserRatingOfItem(user, item1ID) - userAverageRating) * temporalWeight) * ((trainingDataset.getUserRatingOfItem(user, item1ID) - userAverageRating) * temporalWeight);

            // rhs
            denominatorRhs += ((trainingDataset.getUserRatingOfItem(user, item2ID) - userAverageRating) * temporalWeight) * ((trainingDataset.getUserRatingOfItem(user, item2ID) - userAverageRating) * temporalWeight);
        }

        // finalising calculations
        denominatorLhs = (float) Math.sqrt((double) denominatorLhs);
        denominatorRhs = (float) Math.sqrt((double) denominatorRhs);
        denominator = denominatorLhs * denominatorRhs;

        // CHECKING FOR INVALID DENOMINATOR // denominator can have value of 0, which causes NaN error
        if(denominator == 0){
            return similarity;
        }

        // COMBINING NUMERATOR AND DEMONINATOR //

        similarity = numerator / denominator;

        // DETERMINING SIGNIFICANCE //

        // adjusting similarity based on number of common users - items with less common users have lower similarities (https://www.diva-portal.org/smash/get/diva2:1352791/FULLTEXT01.pdf
        similarity *= (float) Math.min(commonUsers.size(), IBRecommender.SIGNIFICANCE_VALUE) / IBRecommender.SIGNIFICANCE_VALUE;

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
     * @param timestamp
     * @return
     */
    protected float makePrediction(int userID, int itemID, int timestamp){

        /**
         * Edge Cases:
         *  - General:
         *      - Item Cold start - the item has not been seen before, so there are no similar items.
         *      - User Cold Start - The user has not been seen before, so there are no ratings for them.
         *      - User and Item Cold Start
         *      - User has not rated any of the similar items.
         *  - When looping through similar items:
         *      - Item has negative similarity
         *      - The user has not rated the similar item.
         */

        //////////////////////////////
        // CHECKING FOR COLD STARTS //
        //////////////////////////////

        // gathering user and item averages
        Float averageUserRating = this.getTrainingDataset().getAverageUserRating(userID);
        Float averageItemRating = this.getTrainingDataset().getAverageItemRating(itemID);

        // USER AND ITEM COLD START //
        if(averageUserRating == null && averageItemRating == null){
            // return sensible value - average rating across all items
            return this.getTrainingDataset().getDatasetAverageRating();
        }
        // USER COLD START //
        else if(averageUserRating == null){
            // return sensible value - average rating of item
            return averageItemRating;
        }
        // ITEM COLD START //
        else if(averageItemRating == null){
            // return sensible value - average rating of user
            return averageUserRating;
        }

        /////////////////
        // PREPERATION //
        /////////////////

        // variable to store prediction
        Float prediction = 1f; // base recommendation = 1.0 (the lowest possible rating).

        // getting neighbourhood of similar items
        //ArrayList<Entry<Integer, Float>> similarItems = this.getModel().getKNearestSimilaritiesForObject(itemID, IBRecommender.K);
        //ArrayList<Entry<Integer, Float>> similarItems = this.getModel().getSortedSimilaritiesForObject(itemID);
        HashMap<Integer, Float> similarItems = this.getModel().getSimilaritiesForObject(itemID);

        // variables to store sums
        float numerator = 0f;
        float denominator = 0f;

        /////////////////
        // CALCULATION //
        /////////////////

        // iterating through items
        for(Entry<Integer, Float> similarItem : similarItems.entrySet()){

            // gathering item ID and similatity value
            int similarItemID = similarItem.getKey();
            Float similarityOfSimilarItem = similarItem.getValue();
            float similarItemAverageRating = this.getTrainingDataset().getAverageItemRating(similarItemID);

            // // CHECKING FOR DISSIMILARITY //
            if(similarityOfSimilarItem <= IBRecommender.MIN_SIMILARITY){
                // dont want to consider items that are not similar
                continue;
            }

            // getting user rating of this item
            Float userRatingOfSimilarItem = this.getTrainingDataset().getUserRatingOfItem(userID, similarItemID);

            // checking user has rated similar item
            if(userRatingOfSimilarItem == null){
                // using user's average rating instead
                continue;
            }

            // gathering timestamp of the rating of the similar item
            int timestampOfUserRatingOfSimilarItem = this.getTrainingDataset().getTimestampOfRating(userID, similarItemID);

            // weighting function
            // f(t_ui) = e ^^ (-a (| t_uj - t_ui |))
            // 0 < a < 1 - higher values of a = weight more sensitive to time, having a = 0 : no time consideration at all
            float timeDif = Math.abs(timestamp - timestampOfUserRatingOfSimilarItem);
            float temporalWeight = (float) Math.exp((-1 * IBRecommender.TEMPORAL_WEIGHT_FACTOR) * timeDif);

            // incrementing numerator
            numerator += ((float) similarityOfSimilarItem * (userRatingOfSimilarItem - similarItemAverageRating) * temporalWeight);

            // incrementing denominator
            denominator += similarityOfSimilarItem * temporalWeight;
        }

        // USER HAS NOT RATED ANY SIMILAR ITEMS //
        if(denominator == 0 && numerator == 0){
            // return sensble prediction - average of average rating of user and average rating of item
            return ((averageItemRating + averageUserRating) / 2); 
        }

        // getting final results
        prediction = averageItemRating + (numerator / denominator);
        
        //////////////
        // RETURING //
        //////////////

        // returning prediction
        return prediction;
    }
}