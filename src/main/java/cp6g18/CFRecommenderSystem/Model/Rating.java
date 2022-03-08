package cp6g18.CFRecommenderSystem.Model;

import java.io.Serializable;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
 */
public class Rating implements Serializable{

    // CONSTANTS //
    public static final int UNRATED_ITEM_RATING = -1; // TODO

    // member variables
    private int userID;
    private int itemID;
    private float rating;
    private int timestamp;

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     * 
     * // TODO
     * 
     * @param userID
     * @param itemID
     * @param rating
     * @param timestamp
     */
    public Rating(int userID, int itemID, float rating, int timestamp){
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
     * // TODO
     * 
     * @return
     */
    public boolean isUnrated(){
        return (this.rating == Rating.UNRATED_ITEM_RATING);
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
     * // TODO
     * 
     * @return
     */
    public int getUserID(){
        return this.userID;
    }

    /**
     * // TODO
     * 
     * @return
     */
    public int getItemID(){
        return this.itemID;
    }

    /**
     * // TODO
     * 
     * @return
     */
    public float getRating(){
        return this.rating;
    }

    /**
     * // TODO
     * 
     * @param rating
     */
    public void setRating(float rating){
        this.rating = rating;
    }

    /**
     * // TODO
     * 
     * @return
     */
    public int getTimestamp(){
        return this.timestamp;
    }
}