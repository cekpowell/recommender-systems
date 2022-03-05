package cp6g18.RecommenderSystem.Model;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
 */
class Rating{
    // constants
    private static final int UNRATED_ITEM_RATING = -1;

    // member variables
    private int userID;
    private int itemID;
    private float itemRating;
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
     * @param itemRating
     * @param timestamp
     */
    public Rating(int userID, int itemID, float itemRating, int timestamp){
        // initializing
        this.userID = userID;
        this.itemID = itemID;
        this.itemRating = itemRating;
        this.timestamp = timestamp;
    }

    /**
     * Class constructor.
     * 
     * // TODO
     * 
     * @param userID
     * @param itemID
     * @param itemRating
     * @param timestamp
     */
    public Rating(int userID, int itemID, int timestamp){
        // initializing
        this.userID = userID;
        this.itemID = itemID;
        this.itemRating = Rating.UNRATED_ITEM_RATING;
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
        return (this.itemRating == Rating.UNRATED_ITEM_RATING);
    }

    /**
     * Returns the rating entry as a String.
     * 
     * @return The rating as a String.
     */
    public String toString(){
        return ("" + this.userID + "," + this.itemID + "," + this.itemRating + "," + this.timestamp); 
    }

    /////////////////////////
    // GETTERS AND SETTERS //
    /////////////////////////

    public int getUserID(){
        return this.userID;
    }

    public int getItemID(){
        return this.itemID;
    }

    public float getItemRating(){
        return this.itemRating;
    }

    public int getTimestamp(){
        return this.timestamp;
    }
}