package cp6g18.CFRecommenderSystem.Model;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import cp6g18.General.Model.TrainingDataset;
import cp6g18.General.Model.Tuple;

/**
 * A dataset used for the purposes of training a Recommender. The data is stored as a nested hashmap
 * for quick access, and additional hashmaps are maintained for convenience.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public abstract class CFTrainingDataset extends TrainingDataset{

    // MEMBER VARIABLES //
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
    public CFTrainingDataset(){
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
     * Adds the provided rating to the dataset.
     * 
     * @param userID The ID of the user who made the rating.
     * @param itemID The ID of the item the rating is for.
     * @param itemRating The rating given to the item.
     * @param timestamp The timestamp of the rating.
     */
    public abstract void addRating(int userID, int itemID, float rating, int timestamp);

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
    }

    ///////////////////////////////////
    // GETTING DATA FROM THE DATASET //
    ///////////////////////////////////

    /**
     * Returns the set of all users stored in the dataset.
     * 
     * @return The set of all users (userIDs) stored in the datset.
     */
    public abstract Set<Integer> getUsers();

    /**
     * Returns the set of all items stored in the dataset.
     * 
     * @return The set of all items (itemIDs) stored in the datset.
     */
    public abstract Set<Integer> getItems();

    /**
     * Returns the Set of all users who have rated a particular item.
     * 
     * @param itemID The ID of the item for which the users who have rated it are being gathered.
     * @return The set of all users (user IDs) who have rated the item.
     */
    public abstract Set<Integer> getUsersWhoRatedItem(int itemID);

    /**
     * Returns the set of all users who have rated a pair of items.
     * 
     * @param item1ID The first item the users must have rated. 
     * @param item2ID The second item the users must have rated.
     * @return The set of users (user IDs) who have rated both items.
     */
    public abstract Set<Integer> getUsersWhoRatedItems(int item1ID, int item2ID);

    /**
     * Returns the set of items rated by a particular user.
     * 
     * @param userID The ID of the user who's ratings are being gathered.
     * @return The set of items (item IDs) rated by the user.
     */
    public abstract Set<Integer> getItemsRatedByUser(int userID);

    /**
     * Returns the set of items rated by a pair of users.
     * 
     * @param user1ID The first user who must have rated the items.
     * @param user2ID The second user who must have rated the items.
     * @return The set of items (item IDs) rated by both users.
     */
    public abstract Set<Integer> getItemsRatedByUsers(int user1ID, int user2ID);

    /**
     * Returns the user's rating of an item.
     * 
     * @param userID The ID of the user who gave the rating.
     * @param itemID The ID of the item that was rated.
     * @return The rating (float) given to the item by the user.
     */
    public abstract Float getUserRatingOfItem(int userID, int itemID);

    /**
     * Returns the timestamp of a particular rating.
     * 
     * @param userID The ID of the user who gave the rating.
     * @param itemID The ID of the item that was rated.
     * @return The timestamp of the rating (int).
     */
    public abstract Integer getTimestampOfRating(int userID, int itemID);

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
     * Converts the dataset to a String.
     * 
     * @return A String representation of the dataset.
     */
    public String toString(){
        return this.data.toString();
    }

    /////////////////////////
    // GETTERS AND SETTERS //
    /////////////////////////

    /**
     * Getter method for the object that holds the dataset's data.
     * 
     * @return The hashmap that stores the dataset's data.
     */
    public HashMap<Integer, HashMap<Integer, Tuple<Float, Integer>>> getData(){
        return this.data;
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