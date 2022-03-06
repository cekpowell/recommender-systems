package cp6g18.RecommenderSystem.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
 */
public abstract class RatingsDataset<L, // Typing of lists
                                     R, // Typing of raw item rating
                                     A> { // Typing of data average

    // CONSTANTS //
    public static final String DELIMITER = ","; // TODO
    public static final int USER_ID_INDEX = 0; // TODO
    public static final int ITEM_ID_INDEX = 1; // TODO
    public static final int ITEM_RATING_INDEX = 2; // TODO
    public static final int TIMESTAMP_INDEX = 3; // TODO
    public static final int UNRATED_ITEM_RATING = -1;

    public static final String USER_ID_NAME = "UserID"; // TODO
    public static final String ITEM_ID_NAME = "ItemID"; // TODO
    public static final String RATING_NAME = "Rating"; // TODO
    public static final String TIMESTAMP_NAME = "Timestamp"; // TODO

    //////////////////////////////
    // ADDING RATING TO DATASET //
    //////////////////////////////

    /**
     * // TODO
     * 
     * @param userID
     * @param itemID
     * @param itemRating
     * @param timestamp
     */
    public abstract void addRating(int userID, int itemID, float itemRating, int timestamp);

    ///////////////////////////////////
    // GETTING DATA FROM THE DATASET //
    ///////////////////////////////////

    /**
     * // TODO
     * 
     * @return
     */
    public abstract L getUsers();

    /**
     * // TODO
     * 
     * @return
     */
    public abstract L getItems();

    /**
     * // TODO
     * 
     * @param itemID
     * @return
     */
    public abstract L getUsersWhoRatedItem(int itemID);

    /**
     * // TODO
     * 
     * @param item1ID
     * @param item2ID
     * @return
     */
    public abstract L getUsersWhoRatedItems(int item1ID, int item2ID);

    /**
     * // TODO
     * 
     * @param userID
     * @return
     */
    public abstract L getItemsRatedByUser(int userID);

    /**
     * // TODO
     * 
     * @param user1ID
     * @param user2ID
     * @return
     */
    public abstract L getItemsRatedByUsers(int user1ID, int user2ID);

    /**
     * // TODO
     * 
     * @param userID
     * @param itemID
     * @return
     */
    public abstract R getUserRatingOfItem(int userID, int itemID);

    /**
     * Gathers the average rating of each user within the dataset.
     * 
     * @return A mapping of user IDs to their average rating.
     */
    public abstract A getAverageUserRatings();

    /**
     * Gathers the average rating of each item within the dataset.
     * 
     * @return A mapping of item IDs to their average rating.
     */
    public abstract A getAverageItemRatings();

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * // TODO
     * 
     * @return 
     */
    public abstract String toString();

    /**
     * // TODO
     * 
     * @param <T>
     * @param list1
     * @param list2
     * @return
     */
    public static <T> Set<T> getCommonElements(Collection<T> list1, Collection<T> list2){
        // creating new array list
        Set<T> commonElements = new HashSet<T>();

        // finding common elements
        for(T element : list1){
            if(list2.contains(element)){
                commonElements.add(element);
            }
        }

        // returning common elements
        return commonElements;
    }
}