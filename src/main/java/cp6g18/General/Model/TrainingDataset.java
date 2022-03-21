package cp6g18.General.Model;

/**
 * Dataset used to train a recommender system.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public abstract class TrainingDataset extends Dataset{
    
    //////////////////////////////
    // ADDING RATING TO DATASET //
    //////////////////////////////

    /**
     * Adds the provided rating to the dataset.
     * 
     * @param userID The ID of the user who made the rating.
     * @param itemID The ID of the item the rating is for.
     * @param itemRating The rating given to the item.
     * @param timestamp The timestamp of the rating.
     */
    public abstract void addRating(int userID, int itemID, float rating, int timestamp);

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * Clears the dataset by removing all of its data (used to free up memory when
     * using large datasets).
     */
    public abstract void clear();

    /**
     * Converts the dataset to a String.
     * 
     * @return A String representation of the dataset.
     */
    public abstract String toString();
}
