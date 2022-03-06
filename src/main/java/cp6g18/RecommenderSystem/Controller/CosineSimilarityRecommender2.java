package cp6g18.RecommenderSystem.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import com.almworks.sqlite4java.SQLiteStatement;

import cp6g18.RecommenderSystem.Model.ArrayListRatingsDataset;
import cp6g18.RecommenderSystem.Model.DatabaseTableRatingsDataset;
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
public class CosineSimilarityRecommender2 extends Recommender{

    //////////////////
    // INITIALISING //
    //////////////////

    /**
     * Class constructor.
     */
    public CosineSimilarityRecommender2(){
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
            //SimilarityMatrix model = CosineSimilarityRecommender2.getItemBasedAdjustedCosineSimilarityMatrix(trainingDataset);

            // setting model into system
            //this.setModel(model);
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
    public static SimilarityMatrix getItemBasedAdjustedCosineSimilarityMatrix(DatabaseTableRatingsDataset trainingDataset) throws Exception{
        ///////////////
        // PREPARING //
        ///////////////

        // creating new similarity matrix instance
        SimilarityMatrix similarityMatrix = new SimilarityMatrix();

        /////////////////
        // CALCULATING //
        /////////////////

        // finding similarity between every item
        SQLiteStatement items1 = trainingDataset.getItems();
        while(items1.step()){
            SQLiteStatement items2 = trainingDataset.getItems();
            while(items2.step()){
                int item1 = items1.columnInt(0);
                int item2 = items2.columnInt(0);

                // calculating similarity
                float similarity = CosineSimilarityRecommender2.getItemBasedAdjustedCosineSimilarity(trainingDataset, item1, item2);

                System.out.println("Similarity between item " + item1 + " and " + item2 + " : " + similarity);

                // adding similarity to the matrix
                similarityMatrix.setSimilarity(item1, item2, similarity);
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
    private static float getItemBasedAdjustedCosineSimilarity(DatabaseTableRatingsDataset trainingDataset, int item1ID, int item2ID) throws Exception{
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
        SQLiteStatement commonUsers = trainingDataset.getUsersWhoRatedItems(item1ID, item2ID);

        /////////////////
        // CALCULATION //
        /////////////////

        // NUMERATOR //

        // variable to store numerator
        float numerator = 0f;

        // iterating over common users
        while(commonUsers.step()){
            // getting userID
            int userID = commonUsers.columnInt(0);

            // getting user's average rating
            float userAverageRating = trainingDataset.getAverageUserRating(userID);

            // user rating of item 1
            float userRatingOfItem1 = trainingDataset.getUserRatingOfItem(userID, item1ID);

            // user rating of item 2
            float userRatingOfItem2 = trainingDataset.getUserRatingOfItem(userID, item2ID);

            // combining
            numerator += (userRatingOfItem1 - userAverageRating) * (userRatingOfItem2 - userAverageRating);
        }

        // DENOMINATOR //

        // variable to store denominator
        float denominator = 0f;

        // lhs

        // list of user's who have rated both items
        commonUsers = trainingDataset.getUsersWhoRatedItems(item1ID, item2ID);

        float lhs = 0f;
        while(commonUsers.step()){
            // getting user ID
            int userID = commonUsers.columnInt(0);

            // average user rating
            float userAverageRating = trainingDataset.getAverageUserRating(userID);

            // user rating of item 1
            float userRatingOfItem1 = trainingDataset.getUserRatingOfItem(userID, item1ID);

            // calculation
            float calculation = (userRatingOfItem1 - userAverageRating) * (userRatingOfItem1 - userAverageRating);

            // adding calculation to sum
            lhs += calculation;
        }
        lhs = (float) Math.sqrt((double) lhs);

        // rhs

        // list of user's who have rated both items
        commonUsers = trainingDataset.getUsersWhoRatedItems(item1ID, item2ID);

        float rhs = 0f;
        while(commonUsers.step()){
            // getting user ID
            int userID = commonUsers.columnInt(0);

            // average user rating
            float userAverageRating = trainingDataset.getAverageUserRating(userID);

            // user rating of item 2
            float userRatingOfItem2 = trainingDataset.getUserRatingOfItem(userID, item2ID);

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