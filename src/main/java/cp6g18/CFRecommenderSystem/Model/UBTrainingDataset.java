package cp6g18.CFRecommenderSystem.Model;

import java.util.HashMap;
import java.util.Set;

/**
 * // TODO
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public class UBTrainingDataset extends TrainingDataset{

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
    }

    ///////////////////////////////////
    // GETTING DATA FROM THE DATASET //
    ///////////////////////////////////

    /**
     * Returns the set of all users stored in the dataset.
     * 
     * @return The set of all users (userIDs) stored in the datset.
     */
    public Set<Integer> getUsers(){
        // TODO
        return null;
    }

    /**
     * Returns the set of all items stored in the dataset.
     * 
     * @return The set of all items (itemIDs) stored in the datset.
     */
    public Set<Integer> getItems(){
        // TODO
        return null;
    }

    /**
     * Returns the Set of all users who have rated a particular item.
     * 
     * @param itemID The ID of the item for which the users who have rated it are being gathered.
     * @return The set of all users (user IDs) who have rated the item.
     */
    public Set<Integer> getUsersWhoRatedItem(int itemID){
        // TODO
        return null;
    }

    /**
     * Returns the set of items rated by a particular user.
     * 
     * @param userID The ID of the user who's ratings are being gathered.
     * @return The set of items (item IDs) rated by the user.
     */
    public Set<Integer> getUsersWhoRatedItems(int item1ID, int item2ID){
        // TODO
        return null;
    }

    /**
     * Returns the set of items rated by a particular user.
     * 
     * @param userID The ID of the user who's ratings are being gathered.
     * @return The set of items (item IDs) rated by the user.
     */
    public Set<Integer> getItemsRatedByUser(int userID){
        // TODO
        return null;
    }

    /**
     * Returns the set of items rated by a pair of users.
     * 
     * @param user1ID The first user who must have rated the items.
     * @param user2ID The second user who must have rated the items.
     * @return The set of items (item IDs) rated by both users.
     */
    public Set<Integer> getItemsRatedByUsers(int user1ID, int user2ID){
        // TODO
        return null;
    }

    /**
     * Returns the user's rating of an item.
     * 
     * @param userID The ID of the user who gave the rating.
     * @param itemID The ID of the item that was rated.
     * @return The rating (float) given to the item by the user.
     */
    public Float getUserRatingOfItem(int userID, int itemID){
        // TODO
        return null;
    }

    /**
     * Returns the timestamp of a particular rating.
     * 
     * @param userID The ID of the user who gave the rating.
     * @param itemID The ID of the item that was rated.
     * @return The timestamp of the rating (int).
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