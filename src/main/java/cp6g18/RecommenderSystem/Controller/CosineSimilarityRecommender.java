package cp6g18.RecommenderSystem.Controller;

import java.util.HashMap;
import java.util.Set;

import cp6g18.RecommenderSystem.Model.ArrayListRatingsDataset;
import cp6g18.RecommenderSystem.Model.HashMapRatingsDataset;
import cp6g18.RecommenderSystem.Model.RecommenderType;
import cp6g18.RecommenderSystem.Model.SimilarityMatrix;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
 */
public class CosineSimilarityRecommender extends Recommender{

    //////////////////
    // INITIALISING //
    //////////////////

    /**
     * Class constructor.
     */
    public CosineSimilarityRecommender(){
        // initializing
        super();
    }

    //////////////
    // TRAINING //
    //////////////

    /**
     * // TODO
     * 
     * @param trainingDataset
     */
    protected void trainAux(HashMapRatingsDataset trainingDataset){
        // ITEM BASED //
        if(trainingDataset.getRecommenderType() == RecommenderType.ITEM_BASED){
            // gathering the similarity matrix
            SimilarityMatrix model = CosineSimilarityRecommender.getItemBasedAdjustedCosineSimilarityMatrix(trainingDataset);

            // setting model into system
            this.setModel(model);
        }

        // TODO Handle other types of filtering
    }

    ////////////////////////
    // MAKING PREDICTIONS //
    ////////////////////////

    protected ArrayListRatingsDataset makePredictionsAux(ArrayListRatingsDataset dataset){
        // TODO

        return null;
    }

    /////////////////////////////////////////
    // ITEM-BASED COLLABORATIVE FILTERING  //
    /////////////////////////////////////////

    // SIMILARITY MATRIX //

    /**
     * // TODO
     * 
     * @param trainingDataset
     * @return
     */
    private static SimilarityMatrix getItemBasedAdjustedCosineSimilarityMatrix(HashMapRatingsDataset trainingDataset){
        ///////////////
        // PREPARING //
        ///////////////

        // creating new similarity matrix instance
        SimilarityMatrix similarityMatrix = new SimilarityMatrix();

        // gathering average user ratings
        HashMap<Integer, Float> userAverageRatings = trainingDataset.getAverageUserRatings();

        /////////////////
        // CALCULATING //
        /////////////////

        // finding similarity between every item
        for(int item1 : trainingDataset.getItems()){
            for(int item2 : trainingDataset.getItems()){
                // calculating if not yet calculated
                if(!similarityMatrix.hasSimilarity(item1, item2)){
                    // calculating similarity
                    float similarity = CosineSimilarityRecommender.getItemBasedAdjustedCosineSimilarity(trainingDataset, userAverageRatings, item1, item2);

                    // adding similarity to the matrix
                    similarityMatrix.setSimilarity(item1, item2, similarity);
                }
            }
        }

        ///////////////
        // RETURNING //
        ///////////////

        // returning final similarity matrix
        return similarityMatrix;
    }


    // COSINE SIMILARITY //

    /**
     * // TODO
     * 
     * @param trainingDataset
     * @param item1ID
     * @param item2ID
     * @return 
     */
    private static float getItemBasedAdjustedCosineSimilarity(HashMapRatingsDataset trainingDataset, HashMap<Integer, Float> userAverageRatings, int item1ID, int item2ID){
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

        // NUMERATOR //

        // variable to store numerator
        float numerator = 0f;

        // iterating over all users
        for(int user : commonUsers){
            // average user rating
            float userAverageRating = userAverageRatings.get(user);

            // user rating of item 1
            float userRatingOfItem1 = trainingDataset.getUserRatingOfItem(user, item1ID);

            // user rating of item 2
            float userRatingOfItem2 = trainingDataset.getUserRatingOfItem(user, item2ID);

            // combining
            numerator += (userRatingOfItem1 - userAverageRating) * (userRatingOfItem2 - userAverageRating);
        }

        // DENOMINATOR //

        // variable to store denominator
        float denominator = 0f;

        // lhs

        float lhs = 0f;
        for(int user : commonUsers){
            // average user rating
            float userAverageRating = userAverageRatings.get(user);

            // user rating of item 1
            float userRatingOfItem1 = trainingDataset.getUserRatingOfItem(user, item1ID);

            // calculation
            float calculation = (userRatingOfItem1 - userAverageRating) * (userRatingOfItem1 - userAverageRating);

            // adding calculation to sum
            lhs += calculation;
        }
        lhs = (float) Math.sqrt((double) lhs);

        // rhs

        float rhs = 0f;
        for(int user : commonUsers){
            // average user rating
            float userAverageRating = userAverageRatings.get(user);

            // user rating of item 2
            float userRatingOfItem2 = trainingDataset.getUserRatingOfItem(user, item2ID);

            // calculation
            float calculation = (userRatingOfItem2 - userAverageRating) * (userRatingOfItem2 - userAverageRating);

            // adding calculation to sum
            rhs += calculation;
        }
        rhs = (float) Math.sqrt((double) rhs);

        denominator = lhs * rhs;

        // COMBINING NUMERATOR AND DEMONINATOR //

        similarity = numerator / denominator;

        //////////////////////
        // RETURNING RESULT //
        //////////////////////

        // returning the similarity
        return similarity;
    }
}