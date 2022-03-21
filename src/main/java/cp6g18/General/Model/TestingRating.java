package cp6g18.General.Model;

import java.io.Serializable;

/**
 * An instance of a rating within a TestingDataset - an object who's attributes are the information
 * surrounding the rating.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public class TestingRating implements Serializable{

    // CONSTANTS //
    public static final int UNRATED_ITEM_RATING = -1; // The value assigned to a rating if no prediction has been made yet.

    // MEMBER VARIABLES //
    private int userID; // The ID of the user who gave the rating.
    private int itemID; // The ID of the item being rated.
    private float rating; // The predicted rating given to the item (-1 if no prediction has been made yet).
    private int timestamp; // The timestamp of the rating.

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     * 
     * @param userID The ID of the user who gave the rating.
     * @param itemID The ID of the item being rated.
     * @param rating The predicted rating given to the item (-1 if no prediction has been made yet).
     * @param timestamp The timestamp of the rating.
     */
    public TestingRating(int userID, int itemID, float rating, int timestamp){
        // initializing
        this.userID = userID;
        this.itemID = itemID;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * Determines if a prediction has been made for this Rating.
     * 
     * @return True if a prediction has been made, false if not.
     */
    public boolean isUnrated(){
        return (this.rating == TestingRating.UNRATED_ITEM_RATING);
    }

    /**
     * Returns the rating entry as a String.
     * 
     * @return The rating as a String.
     */
    public String toString(){
        return ("" + this.userID + "," + this.itemID + "," + this.rating + "," + this.timestamp); 
    }

    /////////////////////////
    // GETTERS AND SETTERS //
    /////////////////////////

    /**
     * Getter method for the ID of the user who gave the rating.
     * 
     * @return The ID of the user who gave the rating.
     */
    public int getUserID(){
        return this.userID;
    }

    /**
     * Getter method for the ID of the item the rating is for.
     * 
     * @return The ID of the item the rating is for.
     */
    public int getItemID(){
        return this.itemID;
    }

    /**
     * Getter method for the predicted rating given to the item.
     * 
     * @return The predicted rating of the item, -1 if no prediction has been made yet.
     */
    public float getRating(){
        return this.rating;
    }

    /**
     * Setter method for the predicted rating.
     * 
     * @param rating The predicted rating for this rating.
     */
    public void setRating(float rating){
        this.rating = rating;
    }

    /**
     * Getter method for the timestamp of the rating.
     * 
     * @return The timestamp of the rating.
     */
    public int getTimestamp(){
        return this.timestamp;
    }
}