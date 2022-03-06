package cp6g18.RecommenderSystem.Model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
 */
public class TestingRatingsDataset extends RatingsDataset{

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
    public TestingRatingsDataset(){
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
    public ArrayList<Rating> getRatings(){
        // returning ratings
        return this.data;
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