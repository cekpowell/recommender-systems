package cp6g18.RecommenderSystem.Model;

import java.io.File;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteStatement;
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

        // informing
        System.out.print("\nConnecting to database : '" + this.databaseFilename + "' ...\n\n");

        // opening the connection to the database
        this.connection.open(allowCreateTable);

        // CONNNECTION SUCCESSFULL //

        // informing
        System.out.println("\nSuccessfully connected to database : '" + this.databaseFilename + "' !");
    }

    /**
	 * Closes the database by disposing of the connection.
	 */
	public void close() {
		// DISCONNECTING FROM DATABASE //

        // informing
        System.out.print("\nDisconnecting from database : '" + this.databaseFilename + "' ...\n\n");
        System.out.println();

        // opening the connection to the database
        this.connection.dispose();

        // DISCONNNECTION SUCCESSFULL //

        // informing
        System.out.println();
        System.out.println("\nSuccessfully disconnected from database : '" + this.databaseFilename + "' !s");
	}


    ///////////////////////////////////////////
    // LOADING DATASET OBJECTS FROM DATABASE //
    ///////////////////////////////////////////

    /**
     * // TODO
     * 
     * @param tableName
     * @return
     * @throws SQLiteException
     */
    public DatabaseTableRatingsDataset loaDatabaseTableRatingsDataset(String tableName) throws SQLiteException{
        return new DatabaseTableRatingsDataset(this.connection, tableName);
    }

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

        // informing
        System.out.println("\nLoading ratings from table : '" + tableName + "' as ArrayListRatingsDataset ...");

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

        // informing
        System.out.println("Successfully loaded ratings from table : '" + tableName + "' as ArrayListRatingsDataset !");
        System.out.println("\tNumber of Ratings : " + count);

        ///////////////
        // RETURNING //
        ///////////////

        // returning loaded dataset object
        return dataset;
    }

    /**
     * Loads the training dataset from the table into the Database's Dataset object.
     * 
     * @param trainingDatasetTableName // TODO
     * @param trainingDatasetMappingType // TODO
     * @returns TrainingDataset object containing the data from the training table.
     * @throws SQLiteException If the call to the database fails.
     */
    public HashMapRatingsDataset loadHashMapRatingsDataset(String trainingDatasetTableName, RecommenderType trainingDatasetMappingType) throws SQLiteException{

        /////////////////
        // PREPERATION //
        /////////////////

        // informing
        System.out.println("\nLoading training ratings from table : '" + trainingDatasetTableName + "' as HashMapRatingsDatset ...");

        // creating new training dataset object
        HashMapRatingsDataset trainingDataset = new HashMapRatingsDataset(trainingDatasetMappingType);

        // tracking number of loaded ratings
        int count = 0;

        // SQLiteStatement object to hold the database information
        SQLiteStatement statement = this.connection.prepare("SELECT * FROM " + trainingDatasetTableName);

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

        // informing
        System.out.println("Successfully loaded training ratings from table : '" + trainingDatasetTableName + "' as HashMapRatingsDatset !");
        System.out.println("\tNumber of Ratings : " + count);

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