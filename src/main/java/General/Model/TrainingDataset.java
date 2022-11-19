package General.Model;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Dataset used to train a recommender system.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public abstract class TrainingDataset extends Dataset{

    // MEMBER VARIABLES //
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
        this.totalDatasetRating = new Tuple<Float, Integer>(0f, 0);
        this.totalUserRatings = new HashMap<Integer, Tuple<Float, Integer>>();
        this.totalItemRatings = new HashMap<Integer, Tuple<Float, Integer>>();
    }
    
    //////////////////////////////
    // ADDING RATING TO DATASET //
    //////////////////////////////

    /**
     * Adds the provided rating to the dataset.
     * 
     * @param userID The ID of the user who made the rating.
     * @param itemID The ID of the item the rating is for.
     * @param itemRating The rating given to the item.
     * @param timestamp The timestamp of the rating.
     */
    public abstract void addRating(int userID, int itemID, float rating, int timestamp);

    ////////////////////////////
    // UPDATING TOTAL RATINGS //
    ////////////////////////////

    /**
     * Updates the hashmaps that store a record of the total ratings for the dataset based on a
     * new rating.
     * 
     * @param userID The ID of the user who made the rating.
     * @param itemID The ID of the item being rated.
     * @param rating The rating given to the item by the user.
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

        // incrementing dataset total ratings
        this.totalDatasetRating.first += rating;
        this.totalDatasetRating.second++;
    }

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * Clears the dataset by removing all of its data (used to free up memory when
     * using large datasets).
     */
    public abstract void clear();

    /**
     * Converts the dataset to a String.
     * 
     * @return A String representation of the dataset.
     */
    public abstract String toString();

    /////////////////////////
    // GETTERS AND SETTERS //
    /////////////////////////

        /**
     * 
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
     * Returns a mapping of user ID's to average rating given by the user.
     * 
     * @return A mapping of user ID's to average rating given by the user.
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
     * Returns the average rating given by a user.
     * 
     * @param userID The ID of the user for which the average rating is being gathered.
     * @return The average rating given by the user.
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
     * Returns a mapping of item IDs to their average rating.
     * 
     * @return A mapping of item IDs to their average rating.
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
     * Returns the average rating given to an item.
     * 
     * @param itemID The ID of the item for which the average rating is being gathered.
     * @return The average rating given to the item.
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

    /**
     * Getter method for the object that stores the information on the total rating stored in
     * the dataset.
     * 
     * @return The Tuple that stores the total rating given within the dataset.
     */
    public Tuple<Float, Integer> getTotalDatasetRating(){
        return this.totalDatasetRating;
    }

    /**
     * Getter method for the map that stores the total ratings given by each user.
     * 
     * @return The map that stores the total ratings given by each user.
     */
    public HashMap<Integer, Tuple<Float, Integer>> getTotalUserRatings(){
        return this.totalUserRatings;
    }

    /**
     * Getter method for the map that stores the total ratings given to each item.
     * 
     * @return The map that stores the total ratings given to each item.
     */
    public HashMap<Integer, Tuple<Float, Integer>> getTotalItemRatings(){
        return this.totalItemRatings;
    }
}
