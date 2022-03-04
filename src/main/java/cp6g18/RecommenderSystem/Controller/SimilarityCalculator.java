// package cp6g18.RecommenderSystem.Controller;

// import java.util.ArrayList;
// import java.util.HashMap;

// import cp6g18.RecommenderSystem.Model.SimilarityMatrix;
// import cp6g18.RecommenderSystem.Model.Dataset.Dataset;

// /**
//  * @module  COMP3208: Social Computing Techniques
//  * @project Coursework
//  * @author  Charles Powell
//  * 
//  * -- DESCRIPTION -- 
//  * 
//  * // TODO
//  */
// public class SimilarityCalculator {
    
//     /////////////////////////////////////////
//     // ITEM-BASED COLLABORATIVE FILTERING  //
//     /////////////////////////////////////////

//     // SIMILARITY MATRIX //

//     /**
//      * // TODO
//      * 
//      * @param dataset
//      * @param userIDColIndex
//      * @param itemIDColIndex
//      * @param itemRatingColIndex
//      * @return
//      */
//     public static SimilarityMatrix getItemBasedAdjustedCosineSimilarityMatrix(Dataset dataset, int userIDColIndex, int itemIDColIndex, int itemRatingColIndex){
//         // instantiating new similarity matrix
//         SimilarityMatrix similarityMatrix = new SimilarityMatrix();

//         // gathering average user ratings
//         HashMap<String, Float> userAverageRatings = DatasetQuerier.getUserAverageRatings(dataset, userIDColIndex, itemIDColIndex, itemRatingColIndex);

//         // iterating through set of items
//         ArrayList<String> items = DatasetQuerier.getItems(dataset, itemIDColIndex);
//         for(String item1 : items){
//             // finding similarity between this item and all other items
//             for(String item2 : items){
//                 // only calculating similarity if its not already in the matrix, and if the items are not the same
//                 if(similarityMatrix.needsSimilarity(item1, item2)){
//                     // calculating the similarity between the items
//                     float similarity = SimilarityCalculator.getItemBasedAdjustedCosineSimilarity(dataset, userAverageRatings, userIDColIndex, itemIDColIndex, itemRatingColIndex, item1, item2);

//                     // adding the similarity to the matrix
//                     similarityMatrix.setSimilarity(item1, item2, similarity);
//                 }
//             }
//         }

//         // returning similarity matrix
//         return similarityMatrix;
//     }


//     // COSINE SIMILARITY //

//     /**
//      * // TODO
//      * 
//      * @param item1ID
//      * @param item2ID
//      * @return
//      */
//     public static float getItemBasedAdjustedCosineSimilarity(Dataset dataset, HashMap<String, Float> userAverageRatings, int userIDColIndex, int itemIDColIndex, int itemRatingColIndex, String item1ID, String item2ID){
//         /**
//          * - Find set of users that rated both item 1 and item 2
//          * - Calculate numerator of equation
//          * - Calculator demoninator of equation
//          * - return result
//          */

//         /////////////////
//         // PREPERATION //
//         /////////////////

//         // variable to store similarity
//         float similarity = 0f;

//         // list of user's who have rated both items
//         ArrayList<String> users = DatasetQuerier.getUsersWhoRatedItems(dataset, userIDColIndex, itemIDColIndex, item1ID, item2ID);

//         if(users.size() == 0){
//             return similarity;
//         }

//         /////////////////
//         // CALCULATION //
//         /////////////////

//         // NUMERATOR //

//         float numerator = 0f;
//         for(String user : users){
//             // average user rating
//             float userAverageRating = userAverageRatings.get(user);

//             // user rating of item 1
//             float userRatingOfItem1 = DatasetQuerier.getUserRatingOfItem(dataset, userIDColIndex, itemIDColIndex, itemRatingColIndex, user, item1ID);

//             // user rating of item 2
//             float userRatingOfItem2 = DatasetQuerier.getUserRatingOfItem(dataset, userIDColIndex, itemIDColIndex, itemRatingColIndex, user, item2ID);

//             // combining
//             numerator += (userRatingOfItem1 - userAverageRating) * (userRatingOfItem2 - userAverageRating);
//         }

//         // DENOMINATOR //

//         float denominator = 0f;

//         // lhs

//         float lhs = 0f;
//         for(String user : users){
//             // average user rating
//             float userAverageRating = userAverageRatings.get(user);

//             // user rating of item 1
//             float userRatingOfItem1 = DatasetQuerier.getUserRatingOfItem(dataset, userIDColIndex, itemIDColIndex, itemRatingColIndex, user, item1ID);

//             // calculation
//             float calculation = (userRatingOfItem1 - userAverageRating) * (userRatingOfItem1 - userAverageRating);

//             // adding calculation to sum
//             lhs += calculation;
//         }
//         lhs = (float) Math.sqrt((double) lhs);

//         // rhs

//         float rhs = 0f;
//         for(String user : users){
//             // average user rating
//             float userAverageRating = userAverageRatings.get(user);

//             // user rating of item 2
//             float userRatingOfItem2 = DatasetQuerier.getUserRatingOfItem(dataset, userIDColIndex, itemIDColIndex, itemRatingColIndex, user, item2ID);

//             // calculation
//             float calculation = (userRatingOfItem2 - userAverageRating) * (userRatingOfItem2 - userAverageRating);

//             // adding calculation to sum
//             rhs += calculation;
//         }
//         rhs = (float) Math.sqrt((double) rhs);

//         denominator = lhs * rhs;

//         // COMBINING NUMERATOR AND DENOMINATOR //

//         similarity = numerator / denominator;

//         // returning similarity
//         return similarity;  
//     }
// }