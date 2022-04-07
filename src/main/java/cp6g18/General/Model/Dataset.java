package cp6g18.General.Model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A Ratings Dataset.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public abstract class Dataset implements Serializable{

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

    /**
     * Helper method that returns the intersection of a pair of collections - i.e., the set of items
     * that are contained within both collections.
     * 
     * @param <T> The datatype of the collections.
     * @param list1 The first list being checked.
     * @param list2 The second list being checked.
     * @return A Set containing only those elements that are within both of the input lists.
     */
    public static <T> Set<T> getCommonElements(Collection<T> list1, Collection<T> list2){
        // creating new array list
        Set<T> commonElements = new HashSet<T>();

        // finding common elements
        for(T element : list1){
            if(list2.contains(element)){
                commonElements.add(element);
            }
        }

        // returning common elements
        return commonElements;
    }
}