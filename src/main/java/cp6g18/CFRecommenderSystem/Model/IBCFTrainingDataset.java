package cp6g18.CFRecommenderSystem.Model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import cp6g18.General.Model.Dataset;
import cp6g18.General.Model.Tuple;

/**
 * An implementation of a TrainingDataset that stores data for the purposes of item-based
 * collaborative filtering recommendations.
 * 
 * The data is stored as a mapping of {item -> {user -> (rating, timestamp)}}.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public class IBCFTrainingDataset extends CFTrainingDataset{

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
        HashMap<Integer, Tuple<Float, Integer>> itemRatings = this.getData().get(itemID);

        // instantiating the hashamp if it was null
        if(itemRatings == null){
            itemRatings = new HashMap<Integer, Tuple<Float, Integer>>();
            this.getData().put(itemID, itemRatings);
        }

        // adding this rating to the user's ratings
        itemRatings.put(userID, new Tuple<Float, Integer>(rating, timestamp));

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
        // creating new set
        Set<Integer> users = new HashSet<Integer>();

        // iterating through data and adding each item to the set
        for(HashMap<Integer, Tuple<Float, Integer>> rating : this.getData().values()){
            for(int user : rating.keySet()){
                users.add(user);
            }
        }
        
        // returning set as list
        return users;
    }

    /**
     * Returns the set of all items stored in the dataset.
     * 
     * @return The set of all items (itemIDs) stored in the datset.
     */
    public Set<Integer> getItems(){
        // returning the items
        return this.getData().keySet();
    }

    /**
     * Returns the Set of all users who have rated a particular item.
     * 
     * @param itemID The ID of the item for which the users who have rated it are being gathered.
     * @return The set of all users (user IDs) who have rated the item.
     */
    public Set<Integer> getUsersWhoRatedItem(int itemID){
        // returning the users
        return this.getData().get(itemID).keySet();
    }

    /**
     * Returns the set of all users who have rated a pair of items.
     * 
     * @param item1ID The first item the users must have rated. 
     * @param item2ID The second item the users must have rated.
     * @return The set of users (user IDs) who have rated both items.
     */
    public Set<Integer> getUsersWhoRatedItems(int item1ID, int item2ID){
        // returning the users
        return (Dataset.getCommonElements(this.getUsersWhoRatedItem(item1ID), this.getUsersWhoRatedItem(item2ID)));
    }

    /**
     * Returns the set of items rated by a particular user.
     * 
     * @param userID The ID of the user who's ratings are being gathered.
     * @return The set of items (item IDs) rated by the user.
     */
    public Set<Integer> getItemsRatedByUser(int userID){
        // creating new set
        Set<Integer> items = new HashSet<Integer>();
            
        // finding which items the user has rated
        for(Entry<Integer, HashMap<Integer, Tuple<Float, Integer>>> rating : this.getData().entrySet()){
            // seeing if this rating is from the user
            if(rating.getValue().containsKey(userID)){
                // adding the user to the list if it is
                items.add(rating.getKey());
            }
        }

        // returning the set of items
        return items;
    }

    /**
     * Returns the set of items rated by a pair of users.
     * 
     * @param user1ID The first user who must have rated the items.
     * @param user2ID The second user who must have rated the items.
     * @return The set of items (item IDs) rated by both users.
     */
    public Set<Integer> getItemsRatedByUsers(int user1ID, int user2ID){
        return (Dataset.getCommonElements(this.getItemsRatedByUser(user1ID), this.getItemsRatedByUser(user2ID)));
    }

    /**
     * Returns the user's rating of an item.
     * 
     * @param userID The ID of the user who gave the rating.
     * @param itemID The ID of the item that was rated.
     * @return The rating (float) given to the item by the user.
     */
    public Float getUserRatingOfItem(int userID, int itemID){
        try{
            // returning the rating
            return (this.getData().get(itemID).get(userID).first);
        }
        // case where rating does not exist
        catch(NullPointerException e){
            return null;
        } 
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
            return (this.getData().get(itemID).get(userID).second);
        }        
        // rating does not exist
        catch(NullPointerException e){
            return null;
        }
    }
}