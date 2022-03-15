package cp6g18.CFRecommenderSystem.Model;

import java.util.HashMap;
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
public class UBTrainingDataset extends TrainingDataset{

    ////////////////////////////////
    // ADDING DATA TO THE DATASET //
    ////////////////////////////////

    /**
     * // TODO
     * 
     * @param userID
     * @param itemID
     * @param itemRating
     * @param timestamp
     */
    public void addRating(int userID, int itemID, float rating, int timestamp){
        ///////////////////
        // ADDING RATING //
        ///////////////////

        // loading the hashmap for this item
        HashMap<Integer, Tuple<Float, Integer>> userRatings = this.getData().get(userID);

        // instantiating the hashamp if it was null
        if(userRatings == null){
            userRatings = new HashMap<Integer, Tuple<Float, Integer>>();
            this.getData().put(userID, userRatings);
        }

        // adding this rating to the user's ratings
        userRatings.put(itemID, new Tuple<Float, Integer>(rating, timestamp));

        // updating total user ratings
        this.updateTotalRatings(userID, itemID, rating);

        // updating maximum and minimum timestamps
        this.updateMaxAndMinTimestamps(timestamp);
    }

    ///////////////////////////////////
    // GETTING DATA FROM THE DATASET //
    ///////////////////////////////////

    /**
     * // TODO
     * 
     * @return
     */
    public Set<Integer> getUsers(){
        // TODO
        return null;
    }

    /**
     * // TODO
     * 
     * @return
     */
    public Set<Integer> getItems(){
        // TODO
        return null;
    }

    /**
     * // TODO
     * 
     * @param itemID
     * @return
     */
    public Set<Integer> getUsersWhoRatedItem(int itemID){
        // TODO
        return null;
    }

    /**
     * // TODO
     * 
     * @param item1ID
     * @param item2ID
     * @return
     */
    public Set<Integer> getUsersWhoRatedItems(int item1ID, int item2ID){
        // TODO
        return null;
    }

    /**
     * // TODO
     * 
     * @param userID
     * @return
     */
    public Set<Integer> getItemsRatedByUser(int userID){
        // TODO
        return null;
    }

    /**
     * // TODO
     * 
     * @param user1ID
     * @param user2ID
     * @return
     */
    public Set<Integer> getItemsRatedByUsers(int user1ID, int user2ID){
        // TODO
        return null;
    }

    /**
     * // TODO
     * 
     * @param userID
     * @param itemID
     * @return
     */
    public Float getUserRatingOfItem(int userID, int itemID){
        // TODO
        return null;
    }

    /**
     * // TODO
     * 
     * @param userID
     * @param itemID
     * @return 
     */
    public Integer getTimestampOfRating(int userID, int itemID){
        try{
            // returning the rating's timestamp
            return (this.getData().get(userID).get(itemID).second);
        }        
        // rating does not exist
        catch(NullPointerException e){
            return null;
        }
    }
}