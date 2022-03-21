package cp6g18.MFRecommenderSystem.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import cp6g18.General.Model.TestingRating;
import cp6g18.General.Model.TrainingDataset;

/**
 * A training dataset for an MFRecommender. 
 * 
 * Data is stored as an ArrayList and methods are provided to return the data in a random order.
 * Additional collections are used to keep track of the users and items in the system.
 */
public class MFTrainingDataset extends TrainingDataset{

    // MEMBER VARIABLES //
    private ArrayList<TestingRating> data; // The object used to store the dataset.
    private HashSet<Integer> users; // The set of all users within the dataset
    private HashSet<Integer> items; // The set of all items within the dataset

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     * 
     * @param datasetMappingType The type of mapping of data stored in this dataset.
     */
    public MFTrainingDataset(){
        // initializing
        this.data = new ArrayList<TestingRating>();
        this.users = new HashSet<Integer>();
        this.items = new HashSet<Integer>();
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

        // adding user and item ids to set of all users and items
        this.users.add(userID);
        this.items.add(itemID);
    }

    ///////////////////////////////////
    // GETTING DATA FROM THE DATASET //
    ///////////////////////////////////

    /**
     * Getter method for the list of ratings stored in this dataset.
     * 
     * @return The list of ratings stored in this dataset.
     */
    public ArrayList<TestingRating> getShuffledRatings(){
        // shuffiling the dataset
        Collections.shuffle(this.data);

        // returning shuffled dataset
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