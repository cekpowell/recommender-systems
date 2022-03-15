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
    private Tuple<Float, Integer> totalDatasetRating; // (total rating in dataset, number of ratings in dataset)
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
        this.totalDatasetRating = new Tuple<Float, Integer>();
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

    /**
     * // TODO
     * 
     * @param userID
     * @param itemID
     * @param rating
     */
    public void updateTotalRatings(int userID, int itemID, float rating){
        // incrementing total user rating
        Tuple<Integer, Float> totalUserRating = this.getTotalUserRatings().get(userID);
        if(totalUserRating == null){
            totalUserRating = new Tuple<Integer, Float>(0, 0f);
            this.getTotalUserRatings().put(userID, totalUserRating);
        }
        totalUserRating.first++;
        totalUserRating.second += rating;

        // incrementing item total rating
        Tuple<Integer, Float> totalItemRating = this.getTotalItemRatings().get(itemID);
        if(totalItemRating == null){
            totalItemRating = new Tuple<Integer, Float>(0, 0f);
            this.getTotalItemRatings().put(itemID, totalItemRating);
        }
        totalItemRating.first++;
        totalItemRating.second += rating;
    }

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
     * @param userID
     * @param itemID
     * @return
     */
    public abstract Integer getTimestampOfRating(int userID, int itemID);

    /**
     * // TODO
     * 
     * @return
     */
    public Float getDatasetAverageRating(){
        // calculating the average rating for the dataset.
        float datasetAverageRating = this.totalDatasetRating.first / this.totalDatasetRating.second;

        // returning the average rating for the dataset.
        return datasetAverageRating;
    }

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
     * @return
     */
    public Tuple<Float, Integer> getTotalDatasetRating(){
        return this.totalDatasetRating;
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