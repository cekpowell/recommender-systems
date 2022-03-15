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

    // member variables
    private int minTimestamp; // minimum timestamp value seen in the dataset
    private int maxTimestamp; // maximum timestamp value seen in the dataset

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor
     */
    public Dataset(){
        // initializing
        this.minTimestamp = Integer.MAX_VALUE;
        this.maxTimestamp = Integer.MIN_VALUE;
    }

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

    /**
     * // TODO
     * 
     * @param timestamp
     */
    public void updateMaxAndMinTimestamps(int timestamp){
        if(timestamp > this.maxTimestamp){
            this.maxTimestamp = timestamp;
        }

        if(timestamp < this.minTimestamp){
            this.minTimestamp = timestamp;
        }
    }

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

    /////////////////////////
    // GETTERS AND SETTERS //
    /////////////////////////

    /**
     * // TODO
     * 
     * @return
     */
    public int getMinTimestamp(){
        return this.minTimestamp;
    }

    /**
     * // TODO
     * 
     * @return
     */
    public int getMaxTimestamp(){
        return this.maxTimestamp;
    }
}