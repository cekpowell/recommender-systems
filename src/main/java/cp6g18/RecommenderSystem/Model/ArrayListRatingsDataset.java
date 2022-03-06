package cp6g18.RecommenderSystem.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.HashMap;
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
public class ArrayListRatingsDataset extends RatingsDataset<Set<Integer>,             // Typing of lists
                                                            Float,                    // Typing of Raw item rating
                                                            HashMap<Integer, Float>>{ // Typing of data average

    // member variables
    private ArrayList<Rating> data; // TODO
    private HashMap<Integer, Tuple<Integer, Float>> totalUserRatings; // {userID -> (numberOfRatings given by user, total rating given by user)}
    private HashMap<Integer, Tuple<Integer, Float>> totalItemRatings; // {itemID -> (number of ratings given to item, total rating given to item)}

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     * 
     * @param datasetMappingType The type of mapping of data stored in this dataset.
     */
    public ArrayListRatingsDataset(){
        // initializing
        this.data = new ArrayList<Rating>();
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
    public void addRating(int userID, int itemID, float itemRating, int timestamp){
        // creating rating object
        Rating rating = new Rating(userID, itemID, itemRating, timestamp);

        // adding rating object to dataset
        this.data.add(rating);

        // INCREMENTING TOTALS //

        // incrementing total user rating
        Tuple<Integer, Float> totalUserRating = this.totalUserRatings.get(userID);
        if(totalUserRating == null){
            totalUserRating = new Tuple<Integer, Float>(0, 0f);
            this.totalUserRatings.put(userID, totalUserRating);
        }
        totalUserRating.first++;
        totalUserRating.second += itemRating;

        // incrementing item total rating
        Tuple<Integer, Float> totalItemRating = this.totalItemRatings.get(itemID);
        if(totalItemRating == null){
            totalItemRating = new Tuple<Integer, Float>(0, 0f);
            this.totalItemRatings.put(itemID, totalItemRating);
        }
        totalItemRating.first++;
        totalItemRating.second += itemRating;
    }

    ///////////////////////////////////
    // GETTING DATA FROM THE DATASET //
    ///////////////////////////////////

    /**
     * // TODO
     * 
     * @return
     */
    public Set<Integer> getUsers(){

        return null;
    }

    /**
     * // TODO
     * 
     * @return
     */
    public Set<Integer> getItems(){

        return null;
    }

    /**
     * // TODO
     * 
     * @return
     */
    public ArrayList<Float> getRatings(){
        // creating empty array list
        ArrayList<Float> itemRatings = new ArrayList<Float>();

        // iterating through ratings and adding each item rating to the list
        for(Rating rating : this.data){
            itemRatings.add(rating.getItemRating());
        }

        // returning list of item ratings
        return itemRatings;
    }

    /**
     * // TODO
     * 
     * @param itemID
     * @return
     */
    public Set<Integer> getUsersWhoRatedItem(int itemID){

        return null;
    }

    /**
     * // TODO
     * 
     * @param item1ID
     * @param item2ID
     * @return
     */
    public Set<Integer> getUsersWhoRatedItems(int item1ID, int item2ID){
        return null;
    }

    /**
     * // TODO
     * 
     * @param userID
     * @return
     */
    public Set<Integer> getItemsRatedByUser(int userID){
        return null;
    }

    /**
     * // TODO
     * 
     * @param user1ID
     * @param user2ID
     * @return
     */
    public Set<Integer> getItemsRatedByUsers(int user1ID, int user2ID){
        return null;
    }

    /**
     * // TODO
     * 
     * @param userID
     * @param itemID
     * @return
     */
    public Float getUserRatingOfItem(int userID, int itemID){
        return null;
    }

    /**
     * Gathers the average rating of each user within the dataset.
     * 
     * @return A mapping of user IDs to their average rating.
     */
    public HashMap<Integer, Float> getAverageUserRatings(){
        return null;
    }

    /**
     * Gathers the average rating of each item within the dataset.
     * 
     * @return A mapping of item IDs to their average rating.
     */
    public HashMap<Integer, Float> getAverageItemRatings(){
        return null;
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
        // creating empty string object
        String string = "";

        // adding each rating to the string
        for(Rating rating : this.data){
            string += rating.toString() + "\n";
        }

        // returning completed string
        return string;
    }
}