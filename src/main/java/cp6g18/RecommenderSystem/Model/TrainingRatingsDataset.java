package cp6g18.RecommenderSystem.Model;

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
public class TrainingRatingsDataset extends RatingsDataset{

    // member variables
    private TrainingRatingsDatasetMappingType mappingType; // The type of mapping to store in the hashmap
    private HashMap<Integer, HashMap<Integer, Tuple<Float, Integer>>> data; // {user ID -> {item ID -> (rating,timestamp)}} OR {item ID -> {user ID -> (rating,timestamp)}}.
    private HashMap<Integer, Tuple<Integer, Float>> totalUserRatings; // {userID -> (numberOfRatings given by user, total rating given by user)}
    private HashMap<Integer, Tuple<Integer, Float>> totalItemRatings; // {itemID -> (number of ratings given to item, total rating given to item)}

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     * 
     * @param recommenderType The type of recommender system being trained by this dataset.
     */
    public TrainingRatingsDataset(TrainingRatingsDatasetMappingType mappingType){
        // initializing
        this.mappingType = mappingType;
        this.data = new HashMap<Integer, HashMap<Integer, Tuple<Float, Integer>>>();
        this.totalUserRatings = new HashMap<Integer, Tuple<Integer, Float>>();
        this.totalItemRatings = new HashMap<Integer, Tuple<Integer, Float>>();
    }

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
    public void addRating(int userID, int itemID, float itemRating, int timestamp){
        // HANDLING USERS TO ITEMS MAPPING CASE //

        if(this.mappingType == TrainingRatingsDatasetMappingType.USERS_TO_ITEMS){
            // loading the hashmap for this ite,
            HashMap<Integer, Tuple<Float, Integer>> userRatings = this.data.get(userID);

            // instantiating the hashamp if it was null
            if(userRatings == null){
                userRatings = new HashMap<Integer, Tuple<Float,Integer>>();
                this.data.put(userID, userRatings);
            }

            // adding rating to the user's ratings
            userRatings.put(itemID, new Tuple<Float, Integer>(itemRating, timestamp));
        }

        // HANDLING ITEMS TO USERS MAPPING CASE //

        else{
            // loading the hashmap for this item
            HashMap<Integer, Tuple<Float, Integer>> itemRatings = this.data.get(itemID);

            // instantiating the hashamp if it was null
            if(itemRatings == null){
                itemRatings = new HashMap<Integer, Tuple<Float, Integer>>();
                this.data.put(itemID, itemRatings);
            }

            // adding this rating to the user's ratings
            itemRatings.put(userID, new Tuple<Float, Integer>(itemRating, timestamp));
        }

        // INCREMENTING TOTALS //

        // incrementing total user rating
        Tuple<Integer, Float> totalUserRating = this.totalUserRatings.get(userID);
        if(totalUserRating == null){
            totalUserRating = new Tuple<Integer, Float>(0, 0f);
            this.totalUserRatings.put(userID, totalUserRating);
        }
        totalUserRating.first++;
        totalUserRating.second += itemRating;

        // incrementing item total rating
        Tuple<Integer, Float> totalItemRating = this.totalItemRatings.get(itemID);
        if(totalItemRating == null){
            totalItemRating = new Tuple<Integer, Float>(0, 0f);
            this.totalItemRatings.put(itemID, totalItemRating);
        }
        totalItemRating.first++;
        totalItemRating.second += itemRating;
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
        // HANDLING USERS TO ITEMS MAPPING CASE //

        if(this.mappingType == TrainingRatingsDatasetMappingType.USERS_TO_ITEMS){
            return this.data.keySet();
        }

        // HANDLING ITEMS TO USERS MAPPING CASE //

        else{
            // creating new set
            Set<Integer> users = new HashSet<Integer>();

            // iterating through data and adding each item to the set
            for(HashMap<Integer, Tuple<Float, Integer>> rating : this.data.values()){
                for(int user : rating.keySet()){
                    users.add(user);
                }
            }
            
            // returning set as list
            return users;
        }
    }

    /**
     * // TODO
     * 
     * @return
     */
    public Set<Integer> getItems(){
        // HANDLING USERS TO ITEMS MAPPING CASE //

        if(this.mappingType == TrainingRatingsDatasetMappingType.USERS_TO_ITEMS){
            // creating new set
            Set<Integer> items = new HashSet<Integer>();

            // iterating through data and adding each item to the set
            for(HashMap<Integer, Tuple<Float, Integer>> rating : this.data.values()){
                for(int item : rating.keySet()){
                    items.add(item);
                }
            }
            
            // returning set as list
            return items;
        }

        // HANDLING ITEMS TO USERS MAPPING CASE //

        else{
            return this.data.keySet();
        }
    }

    /**
     * // TODO
     * 
     * @param itemID
     * @return
     */
    public Set<Integer> getUsersWhoRatedItem(int itemID){
        // HANDLING USERS TO ITEMS MAPPING CASE //

        if(this.mappingType == TrainingRatingsDatasetMappingType.USERS_TO_ITEMS){
            // creating new set
            Set<Integer> users = new HashSet<Integer>();
            
            // finding which users rated the item
            for(Entry<Integer, HashMap<Integer, Tuple<Float, Integer>>> rating : this.data.entrySet()){
                // seeing if this rating is for the item
                if(rating.getValue().containsKey(itemID)){
                    // adding the user to the list if it is
                    users.add(rating.getKey());
                }
            }

            // returning the list of users
            return users;
        }

        // HANDLING ITEMS TO USERS MAPPING CASE //

        else{
            return this.data.get(itemID).keySet();
        }
    }

    /**
     * // TODO
     * 
     * @param item1ID
     * @param item2ID
     * @return
     */
    public Set<Integer> getUsersWhoRatedItems(int item1ID, int item2ID){
        return (RatingsDataset.getCommonElements(this.getUsersWhoRatedItem(item1ID), this.getUsersWhoRatedItem(item2ID)));
    }

    /**
     * // TODO
     * 
     * @param userID
     * @return
     */
    public Set<Integer> getItemsRatedByUser(int userID){
        // HANDLING USERS TO ITEMS MAPPING CASE //

        if(this.mappingType == TrainingRatingsDatasetMappingType.USERS_TO_ITEMS){
            return this.data.get(userID).keySet();
        }

        // HANDLING ITEMS TO USERS MAPPING CASE //

        else{
            // creating new set
            Set<Integer> items = new HashSet<Integer>();
            
            // finding which items the user has rated
            for(Entry<Integer, HashMap<Integer, Tuple<Float, Integer>>> rating : this.data.entrySet()){
                // seeing if this rating is from the user
                if(rating.getValue().containsKey(userID)){
                    // adding the user to the list if it is
                    items.add(rating.getKey());
                }
            }

            // returning the list of users
            return items;
        }
    }

    /**
     * // TODO
     * 
     * @param user1ID
     * @param user2ID
     * @return
     */
    public Set<Integer> getItemsRatedByUsers(int user1ID, int user2ID){
        return (RatingsDataset.getCommonElements(this.getItemsRatedByUser(user1ID), this.getItemsRatedByUser(user2ID)));
    }

    /**
     * // TODO
     * 
     * @param userID
     * @param itemID
     * @return
     */
    public Float getUserRatingOfItem(int userID, int itemID){
        // HANDLING USERS TO ITEMS MAPPING CASE //

        if(this.mappingType == TrainingRatingsDatasetMappingType.USERS_TO_ITEMS){
            return (this.data.get(userID).get(itemID).first);
        }

        // HANDLING ITEMS TO USERS MAPPING CASE //

        else{
            return (this.data.get(itemID).get(userID).first);
        }
    }

    /**
     * // TODO
     * 
     * @return
     */
    public HashMap<Integer, Float> getAverageUserRatings(){
        // creating new hashmap
        HashMap<Integer, Float> averageUserRatings = new HashMap<Integer, Float>();

        // adding average ratings to the hashmap
        for(Entry<Integer, Tuple<Integer, Float>> totalUserRating : this.totalUserRatings.entrySet()){
            averageUserRatings.put(totalUserRating.getKey(), (totalUserRating.getValue().second / totalUserRating.getValue().first));
        }

        // returning the hashamp
        return averageUserRatings;
    }

    /**
     * // TODO
     * 
     * @return
     */
    public HashMap<Integer, Float> getAverageItemRatings(){
        // creating new hashmap
        HashMap<Integer, Float> averageItemRatings = new HashMap<Integer, Float>();

        // adding average ratings to the hashmap
        for(Entry<Integer, Tuple<Integer, Float>> itemUserRating : this.totalItemRatings.entrySet()){
            averageItemRatings.put(itemUserRating.getKey(), (itemUserRating.getValue().second / itemUserRating.getValue().first));
        }

        // returning the hashamp
        return averageItemRatings;
    }

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * Clears the dataset.
     */
    public void clear(){
        this.data.clear();
    }

    /**
     * // TODO
     * 
     * @return
     */
    public String toString(){
        return this.data.toString();
    }

    /////////////////////////
    // GETTERS AND SETTERS //
    /////////////////////////

    /**
     * Returns the mapping type for the dataset.
     * 
     * @return The mapping type for the dataset.
     */
    public TrainingRatingsDatasetMappingType getMappingType(){
        return this.mappingType;
    }
}