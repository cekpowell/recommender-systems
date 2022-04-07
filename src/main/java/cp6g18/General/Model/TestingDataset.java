package cp6g18.General.Model;

import java.util.ArrayList;

/**
 * A dataset that stores a set of ratings a Recommender is to make predictions for.
 * 
 * The ratings are stored as an ArrayList of Rating objects. As a precition must be given to each
 * rating in the dataset, all of the ratings must be iterated over, and thus there is no advantage
 * to storing the ratings in a structure such as a HashMap. Additionally, the ordering of the 
 * ratings must be presevered (as evaluation is done by comparing entry to entry, not rating to
 * rating), which is the case with an ArrayList.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public class TestingDataset extends Dataset{

    // MEMBER VARIABLES //
    private ArrayList<TestingRating> data; // The object used to store the dataset.

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
        this.data = new ArrayList<TestingRating>();
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
        TestingRating rating = new TestingRating(userID, itemID, itemRating, timestamp);

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
    public ArrayList<TestingRating> getRatings(){
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
        for(TestingRating rating : this.data){
            string += rating.toString() + "\n";
        }

        // returning completed string
        return string;
    }
}