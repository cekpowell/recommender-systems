package cp6g18.RecommenderSystem.Model;

import java.io.File;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteStatement;

import cp6g18.RecommenderSystem.Controller.Logger;

import com.almworks.sqlite4java.SQLiteException;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
 */
public class RatingsDatabase{ 

    // member variables
    private String databaseFilename; // TODO
    private SQLiteConnection connection; // TODO

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class Constructor. Opens the database with the provided filename. An error is
     * thrown if no database with this filename exists.
     * 
     * @param databaseFilename The name of the database file. 
     * @param trainingDatasetTableName The name of the training table within the database.
     * @param testingDatasetTableName The name of the testing table within the database.
     * @param datasetMappingType The type of mapping of data to be stored in the database's corresponding datasets.
     */
    public RatingsDatabase(String databaseFilename) throws Exception{
        // configuring SQLite4Java - necesarry because of this: https://github.com/aws-samples/aws-dynamodb-examples/issues/22
        System.out.println();
        System.setProperty("sqlite4java.library.path", "src/main/resources/libs/");

        // initializing
        this.databaseFilename = databaseFilename;
        this.connection = new SQLiteConnection(new File(HashMapRatingsDataset.class.getClassLoader().getResource(this.databaseFilename).getFile()));

        // connecting to database
        this.open(false); /** False so that database only loaded, not created. */
    }

    /////////////////////////////////////////
    // OPENING/CLOSING DATABASE CONNECTION //
    /////////////////////////////////////////

    /**
     * Opens the database by opening the connection to the underlying 
     * database file.
     * 
     * @param allowCreateTable Specifies if the opening can create the database if it does not
     * already exist.
     * @throws SQLiteException If the connection to the database could not
     * be opened.
     */
    private void open(boolean allowCreateTable) throws SQLiteException{
        // CONNECTING TO DATABASE //

        // logging
        Logger.logProcessStart("Connecting to database : '" + this.databaseFilename + "' ...\n\n");

        // opening the connection to the database
        this.connection.open(allowCreateTable);

        // CONNNECTION SUCCESSFULL //

        // logging
        Logger.logProcessEnd("\nSuccessfully connected to database : '" + this.databaseFilename + "' !");
    }

    /**
	 * Closes the database by disposing of the connection.
	 */
	public void close() {
		// DISCONNECTING FROM DATABASE //

        // logging
        Logger.logProcessStart("Disconnecting from database : '" + this.databaseFilename + "' ...\n\n");

        // opening the connection to the database
        this.connection.dispose();

        // DISCONNNECTION SUCCESSFULL //

        // logging
        Logger.logProcessEnd("\nSuccessfully disconnected from database : '" + this.databaseFilename + "' !");
	}


    ///////////////////////////////////////////
    // LOADING DATASET OBJECTS FROM DATABASE //
    ///////////////////////////////////////////

    /**
     * Loads thes dataset from the table into a RatingsDataset object.
     * 
     * @param tableName // TODO
     * @return // TODO
     * @throws SQLiteException If the call to the database fails.
     */
    public ArrayListRatingsDataset loadArrayListRatingsDataset(String tableName) throws SQLiteException{

        /////////////////
        // PREPERATION //
        /////////////////

        // logging
        Logger.logProcessStart("Loading ratings from table : '" + tableName + "' as ArrayListRatingsDataset ...");

        // creating new dataset object
        ArrayListRatingsDataset dataset = new ArrayListRatingsDataset();

        // tracking number of loaded ratings
        int count = 0;

        // SQLiteStatement object to hold the database information
        SQLiteStatement statement = this.connection.prepare("SELECT * FROM " + tableName);

        /////////////
        // LOADING //
        /////////////

        // evaluating the SQL statement and iterating through the loaded rows
        while(statement.step()){
            // extracting information for this rating
            Integer userID = statement.columnInt(0);
            Integer itemID = statement.columnInt(1);
            Float itemRating = (float) statement.columnDouble(2);
            Integer timestamp = statement.columnInt(3);

            // adding the rating to the dataset
            dataset.addRating(userID, itemID, itemRating, timestamp);

            // incrementing count
            count++;
        }

        //////////////
        // CLEANING //
        //////////////

        // disposing statements
        statement.dispose();

        ///////////////
        // INFORMING //
        ///////////////

        // logging
        Logger.logProcessEnd("Successfully loaded ratings from table : '" + tableName + "' as ArrayListRatingsDataset (" + count + " ratings) !");

        ///////////////
        // RETURNING //
        ///////////////

        // returning loaded dataset object
        return dataset;
    }

    /**
     * Loads the dataset from the table into a HashMapRatingsDataset object.
     * 
     * @param tableName // TODO
     * @param trainingDatasetMappingType // TODO
     * @returns HashMapRatings dataset object containing the dataset.
     * @throws SQLiteException If the call to the database fails.
     */
    public HashMapRatingsDataset loadHashMapRatingsDataset(String tableName, RecommenderType trainingDatasetMappingType) throws SQLiteException{

        /////////////////
        // PREPERATION //
        /////////////////

        // logging
        Logger.logProcessStart("Loading ratings from table : '" + tableName + "' as HashMapRatingsDataset ...");

        // creating new training dataset object
        HashMapRatingsDataset trainingDataset = new HashMapRatingsDataset(trainingDatasetMappingType);

        // tracking number of loaded ratings
        int count = 0;

        // SQLiteStatement object to hold the database information
        SQLiteStatement statement = this.connection.prepare("SELECT * FROM " + tableName);

        /////////////
        // LOADING //
        /////////////

        // evaluating the SQL statement and iterating through the loaded rows
        while(statement.step()){
            // extracting information for this rating
            Integer userID = statement.columnInt(0);
            Integer itemID = statement.columnInt(1);
            Float itemRating = (float) statement.columnDouble(2);
            Integer timestamp = statement.columnInt(3);

            // adding the rating to the dataset
            trainingDataset.addRating(userID, itemID, itemRating, timestamp);

            // incrementing count
            count++;
        }

        //////////////
        // CLEANING //
        //////////////

        // disposing statements
        statement.dispose();

        ///////////////
        // INFORMING //
        ///////////////

        // logging
        Logger.logProcessEnd("Successfully loaded ratings from table : '" + tableName + "' as HashMapRatingsDataset (" + count + " ratings) !");

        ///////////////
        // RETURNING //
        ///////////////

        // returning training dataset
        return trainingDataset;
    }

    /////////////////////////
    // CREATING NEW TABLES //
    /////////////////////////

    // TODO
}