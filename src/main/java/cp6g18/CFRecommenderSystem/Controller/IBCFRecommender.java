package cp6g18.CFRecommenderSystem.Controller;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import cp6g18.CFRecommenderSystem.Model.IBTrainingDataset;

/**
 * An implementation of an Item-Based Collaborative Filtering recommender.
 * 
 * Similarities and predicted ratings are determined according to an item-based model.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public class IBRecommender extends Recommender<IBTrainingDataset>{

    // CONSTANTS //
    private static final int SIGNIFICANCE_VALUE = 50; // The minimum number of co-rated users that must exist between an item pair when determining its similarity.
    private static final float MIN_SIMILARITY = 0.0f; // The minimum similarity that can be used when calculating predicted ratings.
    private static final float TEMPORAL_WEIGHT_FACTOR = 0.001f; // The decay rate for the temporal weight applied to the similarity and predicted ratings.

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
     * Determines the similarity between two items according to the Adjusted Cosine Similarity
     * algorithm presented in the lectures.
     * 
     * Formula:
     *  - Calculating similarity between item i1 and item i2.
     *  - r_ui = rating of user u for item i
     *  - r'_u = average rating of user u
     *  - U = set of all users who have rated both item i1 and item i2.
     *  - u = user within U.
     *  - Similarity (sim(i1,i2)) numerator   = Sum for all u [(r_ui1 - r'u) * (r_ui2 - r'_u)]
     *  - Similarity (sim(i1,i2)) denominator LHS = sqrt(sum for all u [(r_ui1 - r'u)^2])
     *  - Similarity (sim(i1,i2)) denominator RHS = sqrt(sum for all u [(r_ui2 - r'u)^2])
     *  - Similarity (sim(i1,i2)) = numerator / (denominator LHS * denominator RHS)
     * 
     * Base Algorithm:
     *  - Find the set of users that have rated both item 1 and item 2.
     *  - Iterate through this list and record sums for the numerator and LHS and RHS of denominator 
     *    of the ACS formula.
     *  - Combine numerator and denominator sums to produce the similarity.
     * 
     * Additions:
     *  - Significance weighting applied to penalise similarities that are based on a low number 
     *    of co-rating users (i.e., penalise similarities where the number of co-rated users is less
     *    than a significance weighting).
     *  - Temporal weighting applied to penalise for ratings that are far apart, and reward for
     *    weightings that are close together.
     *      - Using method described here: https://www.diva-portal.org/smash/get/diva2:1352791/FULLTEXT01.pdf
     * 
     * Edge cases:
     *  - There are no users that have rated both items - similarity of zero.
     * 
     * @param trainingDataset The training dataset used to determine the similarity.
     * @param item1ID The first item the similarity is being calculated for.
     * @param item2ID The second item the similarity is being calculated for.
     * @return The Adjusted Cosine Similarity between the two items.
     */
    public float getSimiarity(IBTrainingDataset trainingDataset, HashMap<Integer, Float> userAverageRatings, int item1ID, int item2ID){
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

        // SIGNIFICANCE WEIGHTING //

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
     * Calculates the predicted rating of a user-item pair according to the item-based algorithm
     * presented in the lectures.
     * 
     * Formula:
     *  - Predicting rating for:
     *      - User u
     *      - Item i
     *  - N = neighbourhood of items similar to item i
     *  - i' = item within neighbourhood N
     *  - r_ui = rating given by user u to item i
     *  - Predicted rating Numerator   = sum for all i' (sim(i, i') * r_ui') 
     *  - Predicted rating denominator = sum for all i' (sim(i, i'))
     *  - Predicted rating = numerator / denominator
     * 
     * Base Algorithm:
     *  - Check for user + item cold starts.
     *  - Gather the similarities between the item being rated and all other items.
     *  - Iterate through the similar items and record sums for the numerator and denominator of 
     *    the rating prediction formula.
     *      - Consider only similarities that are positive (i.e., the neighbourhood of similar
     *        items are all those items that are somewhat 'positively similar' to the item).
     *  - Combine the numerator and denominator together to give the predicted rating.
     * 
     * Aditions:
     *  - Temporal weighting applied to penalise for ratings that are far apart, and reward for
     *    weightings that are close together 
     *      - Using method described here: https://www.diva-portal.org/smash/get/diva2:1352791/FULLTEXT01.pdf
     *  - Mean centering applied to the prediction formula to account for user/item biases.
     *      - Using method described here: https://www.diva-portal.org/smash/get/diva2:1352791/FULLTEXT01.pdf
     * 
     * Edge cases:
     * - General:
     *      - Item Cold start - the item has not been seen before, so there are no similar items.
     *      - User Cold Start - The user has not been seen before, so there are no ratings for them.
     *      - User and Item Cold Start
     *  - When looping through similar items:
     *      - Item has negative similarity
     *      - The user has not rated a particular similar item.
     *      - User has not rated any of the similar items.
     * 
     * @param userID The ID of the user the in the rating.
     * @param itemID The ID of the item in this rating.
     * @param timestamp The timestamp of the rating.
     * @return The predicted rating.
     */
    protected float makePrediction(int userID, int itemID, int timestamp){
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

        // getting similarities to other items.
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
            numerator += ((float) similarityOfSimilarItem * (userRatingOfSimilarItem - similarItemAverageRating) * temporalWeight); // subtracing similarItemAverageRating for mean centering

            // incrementing denominator
            denominator += similarityOfSimilarItem * temporalWeight;
        }

        // USER HAS NOT RATED ANY SIMILAR ITEMS //
        if(denominator == 0){
            // return sensble prediction - average of average rating of user and average rating of item
            return ((averageItemRating + averageUserRating) / 2); 
        }

        // getting final prediction
        prediction = averageItemRating + (numerator / denominator); // adding averageItemRating to account for mean centering
        if(userID == 410 && itemID == 609){
            System.out.println(numerator);
            System.out.println(denominator);
        }
        
        //////////////
        // RETURING //
        //////////////

        // returning prediction
        return prediction;
    }
}