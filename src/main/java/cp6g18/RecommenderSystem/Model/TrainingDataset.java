package cp6g18.RecommenderSystem.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
 */
public class TrainingDataset{
    // constants
    private static final String DATASET_DELIMITER = ",";
    private static final int DATESET_USER_ID_INDEX = 0;
    private static final int DATESET_ITEM_ID_INDEX = 1;
    private static final int DATESET_RATING_INDEX = 2;
    private static final int DATESET_TIMESTAMP_INDEX = 3;

    // member variables
    private DatasetMappingType datasetMappingType; // The type of mapping of data stored in this dataset.
    private HashMap<Integer, HashMap<Integer, Tuple<Float, Integer>>> data; // {user ID -> {item ID -> (rating,timestamp)}} OR {item ID -> {user ID -> (rating,timestamp)}}.

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     * 
     * @param datasetMappingType The type of mapping of data stored in this dataset.
     */
    public TrainingDataset(DatasetMappingType datasetMappingType){
        // initializing
        this.datasetMappingType = datasetMappingType;
        this.data = new HashMap<Integer, HashMap<Integer, Tuple<Float, Integer>>>();
    }

    /**
     * // TODO
     * 
     * @param datasetMappingType
     * @param trainingDatasetFile
     * @return
     */
    public static TrainingDataset loadFromCSVFile(DatasetMappingType datasetMappingType, File trainingDatasetFile){


        return null;
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
    public static TrainingDataset readFromCSVFile(String filename, DatasetMappingType datasetMappingType) throws Exception{
        try{
            // informing
            System.out.println("\n");
            System.out.print("Loading training dataset from file : '" + filename + "' ...");
            System.out.println("\n");

            // creating object to store dataset
            TrainingDataset trainingDataset = new TrainingDataset(datasetMappingType);

            // creating file object
            File file = new File(TrainingDataset.class.getClassLoader().getResource(filename).getFile());

            // setting up file reader 
            BufferedReader reader = new BufferedReader(new FileReader(file));

            // count to track number of ratings in dataset
            int count = 0;

            // iterating through the file and adding each row to the dataset
            while(reader.ready()){
                // getting next line
                String line = reader.readLine();

                // gathering entry data from line
                int userID = Integer.parseInt(line.split(TrainingDataset.DATASET_DELIMITER)[TrainingDataset.DATESET_USER_ID_INDEX]);
                int itemID = Integer.parseInt(line.split(TrainingDataset.DATASET_DELIMITER)[TrainingDataset.DATESET_ITEM_ID_INDEX]);
                float rating = Float.parseFloat(line.split(TrainingDataset.DATASET_DELIMITER)[TrainingDataset.DATESET_RATING_INDEX]);
                int timestamp = Integer.parseInt(line.split(TrainingDataset.DATASET_DELIMITER)[TrainingDataset.DATESET_TIMESTAMP_INDEX]);

                // adding row to dataset
                trainingDataset.addRating(userID, itemID, rating, timestamp);

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

    /////////////////////
    // WRITING DATASET //
    /////////////////////

    /**
     * Writes the dataset to the given file.
     * 
     * Algorithm:
     *  - Sets up a FileOutputStream to write the Dataset to the provided file.
     *  - Gathers the Dataset 'content' by converting it to a String.
     *  - Writes the Dataset content to the File using the FileOutputStream.
     * 
     * @param filename The name of the file the dataset will be written to.
     * @throws Exception If the dataset could not be written to the given file.
     */
    public void writeToFile(String filename) throws Exception{
        try{
            // informing
            System.out.println("\n");
            System.out.print("Writing training dataset to file : '" + filename + "' ...");
            System.out.println("\n");

            // setting up output stream
            OutputStream out = new FileOutputStream(filename);

            // gathering dataset content
            String content = this.toString();

            // writing content to file
            out.write(content.getBytes());

            // closing the file
            out.close();

            // informing
            System.out.println("\n");
            System.out.println("Successfully written training dataset to file : '" + filename+ "' !");
            System.out.println("\n");
        }
        catch(Exception e){
            throw new Exception("Unable to write dataset to file '" + filename + "'.\n" + 
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
     * @param rating
     * @param timestamp
     */
    public void addRating(int userID, int itemID, float itemRating, int timestamp){
        // HANDLING USERS TO ITEMS MAPPING CASE //

        if(this.datasetMappingType == DatasetMappingType.USERS_TO_ITEMS){
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
    }

    ///////////////////////////////////
    // GETTING DATA FROM THE DATASET //
    ///////////////////////////////////

    // TODO

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