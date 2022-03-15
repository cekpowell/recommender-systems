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
    private HashMap<Integer, Tuple<Float, Integer>> totalUserRatings; // {userID -> (total rating given by user, numberOfRatings given by user)}
    private HashMap<Integer, Tuple<Float, Integer>> totalItemRatings; // {itemID -> (total rating given to item, number of ratings given to item)}

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
        this.totalUserRatings = new HashMap<Integer, Tuple<Float, Integer>>();
        this.totalItemRatings = new HashMap<Integer, Tuple<Float, Integer>>();
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
        Tuple<Float, Integer> totalUserRating = this.getTotalUserRatings().get(userID);
        if(totalUserRating == null){
            totalUserRating = new Tuple<Float, Integer>(0f, 0);
            this.getTotalUserRatings().put(userID, totalUserRating);
        }
        totalUserRating.first += rating;
        totalUserRating.second++;

        // incrementing item total rating
        Tuple<Float, Integer> totalItemRating = this.getTotalItemRatings().get(itemID);
        if(totalItemRating == null){
            totalItemRating = new Tuple<Float, Integer>(0f, 0);
            this.getTotalItemRatings().put(itemID, totalItemRating);
        }
        totalItemRating.first += rating;
        totalItemRating.second++;
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
        for(Entry<Integer, Tuple<Float, Integer>> totalUserRating : this.totalUserRatings.entrySet()){
            averageUserRatings.put(totalUserRating.getKey(), (totalUserRating.getValue().first / totalUserRating.getValue().second));
        }

        // returning the hashamp
        return averageUserRatings;
    }

    /**
     * // TODO
     * 
     * @param userID
     * @return
     */
    public Float getAverageUserRating(int userID){
        try{
            // calculating the average rating for the user
            Float averageUserRating = this.totalUserRatings.get(userID).first / this.totalUserRatings.get(userID).second;

            // returning the average rating for the user
            return averageUserRating;
        }
        catch(NullPointerException e){
            return null;
        }
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
        for(Entry<Integer, Tuple<Float, Integer>> itemUserRating : this.totalItemRatings.entrySet()){
            averageItemRatings.put(itemUserRating.getKey(), (itemUserRating.getValue().first / itemUserRating.getValue().second));
        }

        // returning the hashamp
        return averageItemRatings;
    }

    /**
     * // TODO
     * 
     * @param itemID
     * @return
     */
    public Float getAverageItemRating(int itemID){
        try{
            // calculating the average rating for the item
            Float averageItemRating = this.totalItemRatings.get(itemID).first / this.totalItemRatings.get(itemID).second;

            // returning the average rating for the item
            return averageItemRating;
        }
        catch(NullPointerException e){
            return null;
        }
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
    public HashMap<Integer, Tuple<Float, Integer>> getTotalUserRatings(){
        return this.totalUserRatings;
    }

    /**
     * // TODO
     * 
     * @return
     */
    public HashMap<Integer, Tuple<Float, Integer>> getTotalItemRatings(){
        return this.totalItemRatings;
    }
}