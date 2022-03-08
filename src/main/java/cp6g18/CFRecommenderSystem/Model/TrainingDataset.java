package cp6g18.CFRecommenderSystem.Model;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
 */
public abstract class TrainingDataset extends Dataset{

    // member variables
    private HashMap<Integer, HashMap<Integer, Tuple<Float, Integer>>> data; // {user ID -> {item ID -> (rating,timestamp)}} OR {item ID -> {user ID -> (rating,timestamp)}}.
    private HashMap<Integer, Tuple<Integer, Float>> totalUserRatings; // {userID -> (numberOfRatings given by user, total rating given by user)}
    private HashMap<Integer, Tuple<Integer, Float>> totalItemRatings; // {itemID -> (number of ratings given to item, total rating given to item)}

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     */
    public TrainingDataset(){
        // initializing
        this.data = new HashMap<Integer, HashMap<Integer, Tuple<Float, Integer>>>();
        this.totalUserRatings = new HashMap<Integer, Tuple<Integer, Float>>();
        this.totalItemRatings = new HashMap<Integer, Tuple<Integer, Float>>();
    }

    ////////////////////////////////
    // ADDING DATA TO THE DATASET //
    ////////////////////////////////

    /**
     * // TODO
     * 
     * @param userID
     * @param itemID
     * @param itemRating
     * @param timestamp
     */
    public abstract void addRating(int userID, int itemID, float rating, int timestamp);

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
     * // TODO
     * 
     * @return
     */
    public HashMap<Integer, Float> getAverageUserRatings(){
        // creating new hashmap
        HashMap<Integer, Float> averageUserRatings = new HashMap<Integer, Float>();

        // adding average ratings to the hashmap
        for(Entry<Integer, Tuple<Integer, Float>> totalUserRating : this.totalUserRatings.entrySet()){
            averageUserRatings.put(totalUserRating.getKey(), (totalUserRating.getValue().second / totalUserRating.getValue().first));
        }

        // returning the hashamp
        return averageUserRatings;
    }

    /**
     * // TODO
     * 
     * @return
     */
    public HashMap<Integer, Float> getAverageItemRatings(){
        // creating new hashmap
        HashMap<Integer, Float> averageItemRatings = new HashMap<Integer, Float>();

        // adding average ratings to the hashmap
        for(Entry<Integer, Tuple<Integer, Float>> itemUserRating : this.totalItemRatings.entrySet()){
            averageItemRatings.put(itemUserRating.getKey(), (itemUserRating.getValue().second / itemUserRating.getValue().first));
        }

        // returning the hashamp
        return averageItemRatings;
    }

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * Clears the dataset.
     */
    public void clear(){
        this.data.clear();
    }

    /**
     * // TODO
     * 
     * @return
     */
    public String toString(){
        return this.data.toString();
    }

    /////////////////////////
    // GETTERS AND SETTERS //
    /////////////////////////

    /**
     * // TODO
     * 
     * @return
     */
    public HashMap<Integer, HashMap<Integer, Tuple<Float, Integer>>> getData(){
        return this.data;
    }

    /**
     * // TODO
     * 
     * @return
     */
    public HashMap<Integer, Tuple<Integer, Float>> getTotalUserRatings(){
        return this.totalUserRatings;
    }

    /**
     * // TODO
     * 
     * @return
     */
    public HashMap<Integer, Tuple<Integer, Float>> getTotalItemRatings(){
        return this.totalItemRatings;
    }
}