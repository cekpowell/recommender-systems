package cp6g18.CFRecommenderSystem.Model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
 */
public class IBTrainingDataset extends TrainingDataset{

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
        HashMap<Integer, Tuple<Float, Integer>> itemRatings = this.getData().get(itemID);

        // instantiating the hashamp if it was null
        if(itemRatings == null){
            itemRatings = new HashMap<Integer, Tuple<Float, Integer>>();
            this.getData().put(itemID, itemRatings);
        }

        // adding this rating to the user's ratings
        itemRatings.put(userID, new Tuple<Float, Integer>(rating, timestamp));

        /////////////////////////
        // INCREMENTING TOTALS //
        /////////////////////////

        // incrementing total user rating
        Tuple<Integer, Float> totalUserRating = this.getTotalUserRatings().get(userID);
        if(totalUserRating == null){
            totalUserRating = new Tuple<Integer, Float>(0, 0f);
            this.getTotalUserRatings().put(userID, totalUserRating);
        }
        totalUserRating.first++;
        totalUserRating.second += rating;

        // incrementing item total rating
        Tuple<Integer, Float> totalItemRating = this.getTotalItemRatings().get(itemID);
        if(totalItemRating == null){
            totalItemRating = new Tuple<Integer, Float>(0, 0f);
            this.getTotalItemRatings().put(itemID, totalItemRating);
        }
        totalItemRating.first++;
        totalItemRating.second += rating;
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
     * // TODO
     * 
     * @return
     */
    public Set<Integer> getItems(){
        // returning the items
        return this.getData().keySet();
    }

    /**
     * // TODO
     * 
     * @param itemID
     * @return
     */
    public Set<Integer> getUsersWhoRatedItem(int itemID){
        // returning the users
        return this.getData().get(itemID).keySet();
    }

    /**
     * // TODO
     * 
     * @param item1ID
     * @param item2ID
     * @return
     */
    public Set<Integer> getUsersWhoRatedItems(int item1ID, int item2ID){
        // returning the users
        return (Dataset.getCommonElements(this.getUsersWhoRatedItem(item1ID), this.getUsersWhoRatedItem(item2ID)));
    }

    /**
     * // TODO
     * 
     * @param userID
     * @return
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
     * // TODO
     * 
     * @param user1ID
     * @param user2ID
     * @return
     */
    public Set<Integer> getItemsRatedByUsers(int user1ID, int user2ID){
        return (Dataset.getCommonElements(this.getItemsRatedByUser(user1ID), this.getItemsRatedByUser(user2ID)));
    }

    /**
     * // TODO
     * 
     * @param userID
     * @param itemID
     * @return
     */
    public Float getUserRatingOfItem(int userID, int itemID){
        // returning the rating
        return (this.getData().get(itemID).get(userID).first);
    }
}