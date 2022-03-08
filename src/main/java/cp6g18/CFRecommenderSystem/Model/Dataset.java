package cp6g18.CFRecommenderSystem.Model;


import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
 */
public abstract class Dataset implements Serializable{

    //////////////////////////////
    // ADDING RATING TO DATASET //
    //////////////////////////////

    /**
     * // TODO
     * 
     * @param userID
     * @param itemID
     * @param itemRating
     * @param timestamp
     */
    public abstract void addRating(int userID, int itemID, float itemRating, int timestamp);

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * // TODO
     */
    public abstract void clear();

    /**
     * // TODO
     * 
     * @return 
     */
    public abstract String toString();

    /**
     * // TODO
     * 
     * @param <T>
     * @param list1
     * @param list2
     * @return
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