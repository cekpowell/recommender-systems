package cp6g18.CFRecommenderSystem.Model;

import java.util.ArrayList;

/**
 * A dataset used for the purposes of storing the ratings for which a recommender must predict,
 * and the results of these predictions.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public class TestingDataset extends Dataset{

    // member variables
    private ArrayList<Rating> data; // The object used to store the dataset.

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
     * Adds the provided rating to the dataset.
     * 
     * @param userID The ID of the user who made the rating.
     * @param itemID The ID of the item the rating is for.
     * @param itemRating The rating given to the item.
     * @param timestamp The timestamp of the rating.
     */
    public void addRating(int userID, int itemID, float itemRating, int timestamp){
        // creating rating object
        Rating rating = new Rating(userID, itemID, itemRating, timestamp);

        // adding rating object to dataset
        this.data.add(rating);
    }

    ///////////////////////////////////
    // GETTING DATA FROM THE DATASET //
    ///////////////////////////////////

    /**
     * Getter method for the list of ratings stored in this dataset.
     * 
     * @return The list of ratings stored in this dataset.
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
     * Converts the dataset to a string.
     * 
     * @return A string representation of the dataset.
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