package cp6g18.RecommenderSystem.Controller;

import java.util.HashMap;
import java.util.Set;

import cp6g18.RecommenderSystem.Model.ArrayListRatingsDataset;
import cp6g18.RecommenderSystem.Model.HashMapRatingsDataset;
import cp6g18.RecommenderSystem.Model.HashMapRatingsDatasetMappingType;
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
        // USERS TO ITEMS MAPPING TYPE //
        
        if(trainingDataset.getMappingType() == HashMapRatingsDatasetMappingType.USERS_TO_ITEMS){
            // TODO handle user-based filtering
        }

        // ITEMS TO USERS MAPPING TYPE //
        
        else{
            // gathering the similarity matrix
            SimilarityMatrix model = CosineSimilarityRecommender.getItemBasedAdjustedCosineSimilarityMatrix(trainingDataset);

            // setting model into system
            this.setModel(model);
        }
    }

    ////////////////////////
    // MAKING PREDICTIONS //
    ////////////////////////

    protected ArrayListRatingsDataset makePredictionsAux(ArrayListRatingsDataset dataset){
        // TODO

        return null;
    }

    ////////////////////////////
    // SIMILARITY CALCULATORS //
    ////////////////////////////

    // USER-BASED COLLABORATIVE FILTERING  //

    // TODO

    // ITEM-BASED COLLABORATIVE FILTERING  //

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
}