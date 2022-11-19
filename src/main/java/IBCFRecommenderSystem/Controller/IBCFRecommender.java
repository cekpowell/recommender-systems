package IBCFRecommenderSystem.Controller;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import General.Controller.Recommender;
import IBCFRecommenderSystem.Model.IBCFSimilarityMatrix;
import IBCFRecommenderSystem.Model.IBCFTrainingDataset;
import Tools.Logger;

/** 
 * An item-based collaborative filtering recommender.
 * 
 * Training:
 *      - An item-item similarity matrix is trained using a training dataset of ratings. 
 *      - The similarity between two items is calculated using the Adjusted Cosine 
 *      Similarity algorithm.
 *      - Some additional steps are also included to improve the performance of the algorithm 
 *      (documentation given at relevant code).
 * 
 * Predicting Ratings:
 *      - Predicted ratings are calculated according to the algorithm presented in the lectures, using the
 *      trained similarity matrix.
 *      - Some additional steps are also included to improve the performance of the algorithm.
 *      (documentation given at relevant code).
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public class IBCFRecommender extends Recommender<IBCFTrainingDataset>{

    // MEMBER VARIABLES //
    private IBCFTrainingDataset trainingDataset; // the training dataset used to train the recommender
    private IBCFSimilarityMatrix model; // the model learnt from the training dataset

    // RECOMMENDER PARAMETERS //
    private int significanceValue; // The minimum number of co-rated users that must exist between an item pair when determining its similarity.
    private float minSimilarity; // The minimum similarity that must exist between two items for the item to be considered when calculating predicted ratings.
    private float temporalWeightFactor; // The decay rate for the temporal weight applied to the similarity and predicted ratings.
    
    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor. Constructs a new IBCFRecommender using the provided parameters.
     * 
     * @param minRating The minimum rating that can be given to an item
     * @param maxRating The maximum rating that can be given to an item
     * @param significanceValue The minimum number of co-rated users that must exist between an item 
     * pair when determining its similarity.
     * @param minSimilarity The minimum similarity that must exist between two items for the item to 
     * be considered when calculating predicted ratings.
     * @param temporalWeightFactor The decay rate for the temporal weight applied to the similarity 
     * and predicted ratings.
     */
    public IBCFRecommender(float minRating, float maxRating, int significanceValue, float minSimilarity, float temporalWeightFactor){
        // initializing
        super(minRating, maxRating);
        this.trainingDataset = null;
        this.significanceValue = significanceValue;
        this.minSimilarity = minSimilarity;
        this.temporalWeightFactor = temporalWeightFactor;
    }

    /////////////////////
    // TRAINING SYSTEM //
    /////////////////////

    /**
     * Trains the recommender using the provided training dataset. 
     * 
     * This is done by iterating over all of the items in the training dataset, and for each, 
     * computing the similarity between it and all other items.
     * 
     * The similarity between two items is computed using the getSimilarity() method, and all 
     * similarities are stored in a SimilarityMatrix object (the model for the recommender).
     * 
     * @param trainingDataset The training dataset the recommender will be trained on.
     */
    public void train(IBCFTrainingDataset trainingDataset){
        // informing
        Logger.logProcessStart("Training recommender system");

        ///////////////
        // PREPARING //
        ///////////////

        // setting the training dataset
        this.trainingDataset = trainingDataset;

        // setting up the model
        this.model = new IBCFSimilarityMatrix();

        // gathering average user ratings
        /**
         * User average ratings are needed for the similarity calculator, but it is quicker
         * to gather them once and pass the same object in, rather than gather them each time
         * the similarity algorithm is called.
         */
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
                    float similarity = this.getSimiarity(item1, item2, userAverageRatings);

                    // adding similarity to the matrix
                    model.setSimilarity(item1, item2, similarity);
                }
            }
        }

        ///////////////
        // FINISHING //
        ///////////////

        // informing
        Logger.logProcessEnd("Recommender system successfully trained");
    }

    /**
     * Calculates the similarity between two items according to the Adjusted Cosine Similarity
     * algorithm presented in the lectures (with some additions), and based on the ratings present 
     * in the training dataset.
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
     * Full Algorithm:
     *  - // TODO
     * 
     * @param item1ID The first item the similarity is being calculated for.
     * @param item2ID The second item the similarity is being calculated for.
     * @param userAverageRatings A mapping of users to the average rating they provided.
     * @return The Adjusted Cosine Similarity between the two items.
     */
    public float getSimiarity(int item1ID, int item2ID, HashMap<Integer, Float> userAverageRatings){
        /////////////////
        // PREPERATION //
        /////////////////

        // variable to store similarity
        float similarity = 0f;

        // list of user's who have rated both items
        Set<Integer> commonUsers = this.getTrainingDataset().getUsersWhoRatedItems(item1ID, item2ID);

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
            int item1RatingTimestamp = this.getTrainingDataset().getTimestampOfRating(user, item1ID);
            int item2RatingTimestamp = this.getTrainingDataset().getTimestampOfRating(user, item2ID);

            // applying temporal weighting
            // weighting function: 
            //   - f(t_ui) = e ^^ (-a (| t_uj - t_ui |))
            //   - 0 < a < 1 - higher values of a = weight more sensitive to time, having a = 0 : no time consideration at all
            float timeDif = Math.abs(item1RatingTimestamp - item2RatingTimestamp);
            float temporalWeight = (float) Math.exp((-1 * this.temporalWeightFactor) * timeDif);

            // NUMERATOR //

            // numerator
            numerator += ((this.getTrainingDataset().getUserRatingOfItem(user, item1ID) - userAverageRating) * temporalWeight) * ((this.getTrainingDataset().getUserRatingOfItem(user, item2ID) - userAverageRating) * temporalWeight);

            // DENOMINATOR //

            // lhs
            denominatorLhs += ((this.getTrainingDataset().getUserRatingOfItem(user, item1ID) - userAverageRating) * temporalWeight) * ((this.getTrainingDataset().getUserRatingOfItem(user, item1ID) - userAverageRating) * temporalWeight);

            // rhs
            denominatorRhs += ((this.getTrainingDataset().getUserRatingOfItem(user, item2ID) - userAverageRating) * temporalWeight) * ((this.getTrainingDataset().getUserRatingOfItem(user, item2ID) - userAverageRating) * temporalWeight);
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
        
        /**
         * Significance weighting - Adjusting similarity based on number of common users - items with 
         * less common users should have lower similarities 
         * (https://www.diva-portal.org/smash/get/diva2:1352791/FULLTEXT01.pdf)
         */
        similarity *= (float) Math.min(commonUsers.size(), this.significanceValue) / this.significanceValue;

        //////////////////////
        // RETURNING RESULT //
        //////////////////////

        // returning the similarity
        return similarity;
    }

    ////////////////////////
    // MAKING PREDICTIONS //
    ////////////////////////

    /**
     * Calculates the predicted rating of a user-item rating pair according to the item-based 
     * algorithm presented in the lectures (with some additions).
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
     *    weightings that are close together.
     *      - Using method described here: https://www.diva-portal.org/smash/get/diva2:1352791/FULLTEXT01.pdf
     *  - Mean centering applied to the prediction formula to account for user/item biases.
     *      - Using method described here: https://www.diva-portal.org/smash/get/diva2:1352791/FULLTEXT01.pdf
     *  - Rounding prediction to nearest whole number (because ratings are only in whole numbers).
     *  - Ensuring all predictions fall wihtin the bounds of 1 and 5. // TODO
     * 
     * Edge cases:
     *  - Cold Starts:
     *      - Item Cold start - the item has not been seen before, so there are no similar items:
     *          - Use the user's average rating as the prediction.
     *      - User Cold Start - The user has not been seen before, so there are no ratings for them:
     *          - Use the items average prediction as the rating.
     *      - User and Item Cold Start:
     *          - Use the average rating of the dataset.
     *  - When looping through similar items:
     *      - Item has similarity below the threshold:
     *          - Do not consider this item in the predicted rating calculation.
     *      - The user has not rated a particular similar item:
     *          - Do not consider this item in the predicted rating calculation.
     *      - User has not rated any of the similar items:
     *          - Return the average of the average user rating and the average item rating.
     * 
     * Full Algorithm:
     *  - // TODO
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

        // gathering dataset averages
        float datasetAverageRating = this.getTrainingDataset().getDatasetAverageRating();
        Float averageUserRating = this.getTrainingDataset().getAverageUserRating(userID);
        Float averageItemRating = this.getTrainingDataset().getAverageItemRating(itemID);

        // USER AND ITEM COLD START //
        if(averageUserRating == null && averageItemRating == null){
            // return sensible value - average rating across all items
            return datasetAverageRating;
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
        Float prediction = this.getMinRating(); // base rating = minimum rating.

        // getting similarities to other items.
        HashMap<Integer, Float> similarItems = this.getModel().getSimilaritiesForObject(itemID);

        // variables to store sums
        float numerator = 0f;
        float denominator = 0f;

        /////////////////
        // CALCULATION //
        /////////////////

        // iterating through similar items
        for(Entry<Integer, Float> similarItem : similarItems.entrySet()){
            // gathering item ID and similatity value
            int similarItemID = similarItem.getKey();
            Float similarityOfSimilarItem = similarItem.getValue();
            float similarItemAverageRating = this.getTrainingDataset().getAverageItemRating(similarItemID);

            // CHECKING FOR DISSIMILARITY //
            if(similarityOfSimilarItem <= this.minSimilarity){
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

            // applying temporal weighting
            // weighting function: 
            //   - f(t_ui) = e ^^ (-a (| t_uj - t_ui |))
            //   - 0 < a < 1 - higher values of a = weight more sensitive to time, having a = 0 : no time consideration at all
            float timeDif = Math.abs(timestamp - timestampOfUserRatingOfSimilarItem);
            float temporalWeight = (float) Math.exp((-1 * this.temporalWeightFactor) * timeDif);

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

        // rouding rating to nearest 1 (because ratings are in increments of 1)
        prediction = (float) Math.round(prediction);
        
        //////////////
        // RETURING //
        //////////////

        // returning prediction
        return prediction;
    }

    /////////////////////////
    // GETTERS AND SETTERS //
    /////////////////////////

    /**
     * Getter method for the recommender's training dataset.
     * 
     * @return The training dataset associated with this recommender.
     */
    public IBCFTrainingDataset getTrainingDataset(){
        return this.trainingDataset;
    }

    /**
     * Getter method for the recommender's model.
     * 
     * @return The model associated with this recommender.
     */
    public IBCFSimilarityMatrix getModel(){
        return this.model;
    }

    /**
     * Setter method for the recommender's model.
     * 
     * @param model The new model to be associated with this recommender.
     */
    public void setModel(IBCFSimilarityMatrix model){
        this.model = model;
    }
}