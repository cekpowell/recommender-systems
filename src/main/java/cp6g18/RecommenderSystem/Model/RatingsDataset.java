package cp6g18.RecommenderSystem.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;

import java.util.ArrayList;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
 */
public class RatingsDataset{
    // constants
    private static final String DATASET_DELIMITER = ","; // TODO
    private static final int DATESET_USER_ID_INDEX = 0; // TODO
    private static final int DATESET_ITEM_ID_INDEX = 1; // TODO
    private static final int DATESET_TIMESTAMP_INDEX = 2; // TODO

    // member variables
    private ArrayList<Rating> data; // TODO

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     * 
     * @param datasetMappingType The type of mapping of data stored in this dataset.
     */
    public RatingsDataset(){
        // initializing
        this.data = new ArrayList<Rating>();
    }

    /////////////////////
    // READING DATASET //
    /////////////////////

    /**
     * Reads a dataset from a file.
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
     * @return The loaded dataset as a Dataset object.
     * @throws Exception Thrown if the dataset could not be loaded from the file.
     */
    public static RatingsDataset readFromCSVFile(String filename) throws Exception{
        try{
            // informing
            System.out.println("\n");
            System.out.print("Loading dataset from file : '" + filename + "' ...");
            System.out.println("\n");

            // creating object to store dataset
            RatingsDataset dataset = new RatingsDataset();

            // creating file object
            File file = new File(RatingsDataset.class.getClassLoader().getResource(filename).getFile());

            // setting up file reader 
            BufferedReader reader = new BufferedReader(new FileReader(file));

            // count to record number of ratings
            int count = 0;

            // iterating through the file and adding each row to the dataset
            while(reader.ready()){
                // getting next line
                String line = reader.readLine();

                // gathering entry data from line
                int userID = Integer.parseInt(line.split(RatingsDataset.DATASET_DELIMITER)[RatingsDataset.DATESET_USER_ID_INDEX]);
                int itemID = Integer.parseInt(line.split(RatingsDataset.DATASET_DELIMITER)[RatingsDataset.DATESET_ITEM_ID_INDEX]);
                int timestamp = Integer.parseInt(line.split(RatingsDataset.DATASET_DELIMITER)[RatingsDataset.DATESET_TIMESTAMP_INDEX]);

                // adding row to dataset
                dataset.addRating(userID, itemID, timestamp);

                // incrementing count
                count++;
            }

            // closing the reader
            reader.close();

            // informing
            System.out.println("\n");
            System.out.println("Successfully loaded dataset from file : '" + filename+ "' !");
            System.out.println("Number of Ratings : " + count);
            System.out.println("\n");

            // returning completed dataset
            return dataset;
        }
        catch(Exception e){
            throw new Exception("Unable to read dataset from file '" + filename + "'.\n" + 
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
     * @param file The File object the dataset will be written to.
     * @throws Exception If the dataset could not be written to the given file.
     */
    public void writeToFile(File file) throws Exception{
        try{
            // informing
            System.out.println("\n");
            System.out.print("Writing dataset to file : '" + file.getName() + "' ...");
            System.out.println("\n");

            // setting up output stream
            OutputStream out = new FileOutputStream(file);

            // gathering dataset content
            String content = this.toString();

            // writing content to file
            out.write(content.getBytes());

            // closing the file
            out.close();

            // informing
            System.out.println("\n");
            System.out.println("Successfully written dataset to file : '" + file.getName()+ "' !");
            System.out.println("\n");
        }
        catch(Exception e){
            throw new Exception("Unable to write dataset to file '" + file.getName() + "'.\n" + 
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
        // creating rating object
        Rating rating = new Rating(userID, itemID, itemRating, timestamp);

        // adding rating object to dataset
        this.data.add(rating);
    }

    /**
     * // TODO
     * 
     * @param userID
     * @param itemID
     * @param timestamp
     */
    public void addRating(int userID, int itemID, int timestamp){
        // creating rating object
        Rating rating = new Rating(userID, itemID, timestamp);

        // adding rating object to dataset
        this.data.add(rating);
    }

    ///////////////////////////////////
    // GETTING DATA FROM THE DATASET //
    ///////////////////////////////////

    // TODO

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * // TODO
     * 
     * @return
     */
    public ArrayList<Float> getListOfItemRatings(){
        // creating empty array list
        ArrayList<Float> itemRatings = new ArrayList<Float>();

        // iterating through ratings and adding each item rating to the list
        for(Rating rating : this.data){
            itemRatings.add(rating.getItemRating());
        }

        // returning list of item ratings
        return itemRatings;
    }

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
        // creating empty string object
        String string = "";

        // adding each rating to the string
        for(Rating rating : this.data){
            string += rating.toString() + "\n";
        }

        // returning completed string
        return string;
    }
}