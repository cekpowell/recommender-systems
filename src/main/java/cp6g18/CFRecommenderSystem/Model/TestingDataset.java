package cp6g18.CFRecommenderSystem.Model;

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
public class TestingDataset extends Dataset{

    // member variables
    private ArrayList<Rating> data; // TODO

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     * 
     * @param datasetMappingType The type of mapping of data stored in this dataset.
     */
    public TestingDataset(){
        // initializing
        this.data = new ArrayList<Rating>();
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

        // updating maximum and minimum timestamps
        this.updateMaxAndMinTimestamps(timestamp);
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