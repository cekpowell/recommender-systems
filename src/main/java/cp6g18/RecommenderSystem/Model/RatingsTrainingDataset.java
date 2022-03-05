package cp6g18.RecommenderSystem.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
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
public class RatingsTrainingDataset{
    // constants
    private static final String DATASET_DELIMITER = ","; // TODO
    private static final int DATESET_USER_ID_INDEX = 0; // TODO
    private static final int DATESET_ITEM_ID_INDEX = 1; // TODO
    private static final int DATESET_ITEM_RATING_INDEX = 2; // TODO
    private static final int DATESET_TIMESTAMP_INDEX = 3; // TODO

    // member variables
    private RatingsTrainingDatasetMappingType datasetMappingType; // The type of mapping of data stored in this dataset.
    private HashMap<Integer, HashMap<Integer, Tuple<Float, Integer>>> data; // {user ID -> {item ID -> (rating,timestamp)}} OR {item ID -> {user ID -> (rating,timestamp)}}.
    private int numOfEntries; // count for the number of entries in the dataset
    private HashMap<Integer, Tuple<Integer, Float>> userTotalRatings; // {userID -> (numberOfRatings given by user, total rating given by user)}
    private HashMap<Integer, Tuple<Integer, Float>> itemTotalRatings; // {itemID -> (number of ratings given to item, total rating given to item)}

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     * 
     * @param datasetMappingType The type of mapping of data stored in this dataset.
     */
    public RatingsTrainingDataset(RatingsTrainingDatasetMappingType datasetMappingType){
        // initializing
        this.datasetMappingType = datasetMappingType;
        this.data = new HashMap<Integer, HashMap<Integer, Tuple<Float, Integer>>>();
        this.userTotalRatings = new HashMap<Integer, Tuple<Integer, Float>>();
        this.itemTotalRatings = new HashMap<Integer, Tuple<Integer, Float>>();
    }

    /////////////////////
    // READING DATASET //
    /////////////////////

    /**
     * Reads a training dataset from a file.
     * 
     * Algorithm:
     *  - Creates new Dataset object.
     *  - Sets up a buffered reader object to read the given file.
     *  - Iterates through the file, and creates a new Row object for each line
     *    by splitting the line with the provided delimiter.
     *  - Adds the new Row object to the Dataset object.
     *  - Returns the new Dataset object.
     * 
     * @param filename The name of the file the dataset is being read from.
     * @param datasetMappingType // TODO
     * @return The loaded dataset as a Dataset object.
     * @throws Exception Thrown if the dataset could not be loaded from the file.
     */
    public static RatingsTrainingDataset readFromCSVFile(String filename, RatingsTrainingDatasetMappingType trainingDatasetMappingType) throws Exception{
        try{
            // informing
            System.out.println("\n");
            System.out.print("Loading training dataset from file : '" + filename + "' ...");
            System.out.println("\n");

            // creating object to store dataset
            RatingsTrainingDataset trainingDataset = new RatingsTrainingDataset(trainingDatasetMappingType);

            // creating file object
            File file = new File(RatingsTrainingDataset.class.getClassLoader().getResource(filename).getFile());

            // setting up file reader 
            BufferedReader reader = new BufferedReader(new FileReader(file));

            // count to track number of ratings in dataset
            int count = 0;

            // iterating through the file and adding each row to the dataset
            while(reader.ready()){
                // getting next line
                String line = reader.readLine();

                // gathering entry data from line
                int userID = Integer.parseInt(line.split(RatingsTrainingDataset.DATASET_DELIMITER)[RatingsTrainingDataset.DATESET_USER_ID_INDEX]);
                int itemID = Integer.parseInt(line.split(RatingsTrainingDataset.DATASET_DELIMITER)[RatingsTrainingDataset.DATESET_ITEM_ID_INDEX]);
                float itemRating = Float.parseFloat(line.split(RatingsTrainingDataset.DATASET_DELIMITER)[RatingsTrainingDataset.DATESET_ITEM_RATING_INDEX]);
                int timestamp = Integer.parseInt(line.split(RatingsTrainingDataset.DATASET_DELIMITER)[RatingsTrainingDataset.DATESET_TIMESTAMP_INDEX]);

                // adding row to dataset
                trainingDataset.addRating(userID, itemID, itemRating, timestamp);

                // incrementing count
                count++;
            }

            // closing the reader
            reader.close();

            // informing
            System.out.println("\n");
            System.out.println("Successfully loaded training dataset from file : '" + filename+ "' !");
            System.out.println("Number of Ratings : " + count);
            System.out.println("Number of entries in training dataset : " + trainingDataset.getNumberOfEntries());
            System.out.println("\n");

            // returning completed dataset
            return trainingDataset;
        }
        catch(Exception e){
            throw new Exception("Unable to read training dataset from file '" + filename + "'.\n" + 
            "Cause : \n\t" + e.toString());
        }
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

        if(this.datasetMappingType == RatingsTrainingDatasetMappingType.USERS_TO_ITEMS){
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

        // INCREMENTING RECORDS //

        // incrementing number of entries
        this.numOfEntries++;

        // adding rating to total ratings for user
        Tuple<Integer, Float> userTotal = this.userTotalRatings.get(userID);
        if(userTotal == null){
            userTotal = new Tuple<Integer, Float>(0, 0f);
            this.userTotalRatings.put(userID, userTotal);
        }
        userTotal.first++;
        userTotal.second += itemRating;

        // adding rating to total ratings for item
        Tuple<Integer, Float> itemTotal = this.itemTotalRatings.get(itemID);
        if(itemTotal == null){
            itemTotal = new Tuple<Integer, Float>(0, 0f);
            this.itemTotalRatings.put(itemID, itemTotal);
        }
        itemTotal.first++;
        itemTotal.second += itemRating;
    }

    ///////////////////////////////////
    // GETTING DATA FROM THE DATASET //
    ///////////////////////////////////

    /**
     * // TODO
     * 
     * @return
     */
    public ArrayList<Integer> getItems(){
        return (new ArrayList<Integer>(this.data.keySet()));
    }

    /**
     * // TODO
     * 
     * @param itemID
     * @return
     */
    public HashMap<Integer, Tuple<Float, Integer>> getUsersWhoRatedItem(int itemID){
        return this.data.get(itemID);
    }

    /**
     * // TODO
     * 
     * @param userID
     * @param itemID
     * @return
     */
    public float getUserRatingOfItem(int userID, int itemID){
        // HANDLING USERS TO ITEMS MAPPING CASE //

        if(this.datasetMappingType == RatingsTrainingDatasetMappingType.USERS_TO_ITEMS){
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
    public HashMap<Integer, Float> getUserAverageRatings(){
        // creating new hashmap
        HashMap<Integer, Float> userAverageRatings = new HashMap<Integer, Float>();

        // adding average ratings to the hashmap
        for(Entry<Integer, Tuple<Integer, Float>> userTotalRating : this.userTotalRatings.entrySet()){
            userAverageRatings.put(userTotalRating.getKey(), (userTotalRating.getValue().second / userTotalRating.getValue().first));
        }

        // returning the hashamp
        return userAverageRatings;
    }

    /**
     * // TODO
     * 
     * @return
     */
    public HashMap<Integer, Float> getItemAverageRatings(){
        // creating new hashmap
        HashMap<Integer, Float> itemAverageRatings = new HashMap<Integer, Float>();

        // adding average ratings to the hashmap
        for(Entry<Integer, Tuple<Integer, Float>> itemTotalRating : this.itemTotalRatings.entrySet()){
            itemAverageRatings.put(itemTotalRating.getKey(), (itemTotalRating.getValue().second / itemTotalRating.getValue().first));
        }

        // returning the hashamp
        return itemAverageRatings;
    }

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * Returns the number of entries within the dataset.
     * 
     * @return Number of entries within the dataset.
     */
    public int getNumberOfEntries(){
        return this.data.size();
    }

    /**
     * Clears the dataset.
     */
    public void clear(){
        this.data.clear();
    }
}