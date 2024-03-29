package MFRecommenderSystem.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import General.Model.TestingRating;
import General.Model.TrainingDataset;

/**
 * A training dataset for an MFRecommender. 
 * 
 * The data is stored as an ArrayList (most effecient data structure for the Matrix Factorisation 
 * algorithm as each iteration requires to go over the entire training dataset in a random order, 
 * so there is no advantage of using a structure like a HashMap).
 * 
 * Additional collections are used to keep track of the IDs of all users and items in the system
 * for the convenience of the Matrix Factorisation algorithm.
 */
public class MFTrainingDataset extends TrainingDataset{

    // MEMBER VARIABLES //
    private ArrayList<TestingRating> data; // The object used to store the dataset.
    private HashSet<Integer> users; // The set of all users within the dataset
    private HashSet<Integer> items; // The set of all items within the dataset
    private HashMap<Integer, ArrayList<TestingRating>> userRatingsMap; // mapiing of user ID -> their ratings
    private HashMap<Integer, ArrayList<TestingRating>> itemRatingsMap; // mapping of item ID -> their ratings

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
        super();
        this.data = new ArrayList<TestingRating>();
        this.users = new HashSet<Integer>();
        this.items = new HashSet<Integer>();
        this.userRatingsMap = new HashMap<Integer, ArrayList<TestingRating>>();
        this.itemRatingsMap = new HashMap<Integer, ArrayList<TestingRating>>();
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

        // adding rating to mapping of user ratings
        ArrayList<TestingRating> userRatings = this.userRatingsMap.get(userID);
        if(userRatings == null){
            userRatings = new ArrayList<TestingRating>();
            this.userRatingsMap.put(userID, userRatings);
        }
        userRatings.add(rating);

        // adding rating to mapping of item ratings
        ArrayList<TestingRating> itemRatings = this.itemRatingsMap.get(itemID);
        if(itemRatings == null){
            itemRatings = new ArrayList<TestingRating>();
            this.itemRatingsMap.put(itemID, itemRatings);
        }
        itemRatings.add(rating);

        // updating the total ratings
        this.updateTotalRatings(userID, itemID, itemRating);
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

    /////////////////////////
    // GETTERS AND SETTERS //
    /////////////////////////

    /**
     * Getter methods for the set of users contained in this dataset.
     * 
     * @return The set of users contained in this dataset.
     */
    public HashSet<Integer> getUsers(){
        return this.users;
    }

    /**
     * Getter method for the set of items contained in this dataset.
     * 
     * @return The set of items contained in this dataset.
     */
    public HashSet<Integer> getItems(){
        return this.items;
    }
}