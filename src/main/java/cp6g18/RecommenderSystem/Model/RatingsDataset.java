package cp6g18.RecommenderSystem.Model;

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
public abstract class RatingsDataset{

    // CONSTANTS //
    public static final int UNRATED_ITEM_RATING = -1; // TODO

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
    public abstract Set<Integer> getUsers();

    /**
     * // TODO
     * 
     * @return
     */
    public abstract Set<Integer> getItems();

    /**
     * // TODO
     * 
     * @param itemID
     * @return
     */
    public abstract Set<Integer> getUsersWhoRatedItem(int itemID);

    /**
     * // TODO
     * 
     * @param item1ID
     * @param item2ID
     * @return
     */
    public abstract Set<Integer> getUsersWhoRatedItems(int item1ID, int item2ID);

    /**
     * // TODO
     * 
     * @param userID
     * @return
     */
    public abstract Set<Integer> getItemsRatedByUser(int userID);

    /**
     * // TODO
     * 
     * @param user1ID
     * @param user2ID
     * @return
     */
    public abstract Set<Integer> getItemsRatedByUsers(int user1ID, int user2ID);

    /**
     * // TODO
     * 
     * @param userID
     * @param itemID
     * @return
     */
    public abstract Float getUserRatingOfItem(int userID, int itemID);

    /**
     * Gathers the average rating of each user within the dataset.
     * 
     * @return A mapping of user IDs to their average rating.
     */
    public abstract HashMap<Integer, Float> getAverageUserRatings();

    /**
     * Gathers the average rating of each item within the dataset.
     * 
     * @return A mapping of item IDs to their average rating.
     */
    public abstract HashMap<Integer, Float> getAverageItemRatings();

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