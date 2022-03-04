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
public class Database {
    // constants
    public static final float UNRATED_ITEM_RATING = -1.0f;

    // member variables
    private String databaseFilename; // TODO
    private String trainingDatasetTableName; // TODO
    private String testingDatasetTableName; // TODO
    private SQLiteConnection connection; // TODO
    private TrainingDataset trainingDataset; // TODO
    private TrainingDataset testingDataset; // TODO

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
    public Database(String databaseFilename, String trainingDatasetTableName, String testingDatasetTableName, DatasetMappingType datasetMappingType) throws Exception{
        // configuring SQLite4Java - necesarry because of this: https://github.com/aws-samples/aws-dynamodb-examples/issues/22
        System.setProperty("sqlite4java.library.path", "src/main/resources/libs/");

        // initializing
        this.databaseFilename = databaseFilename;
        this.trainingDatasetTableName = trainingDatasetTableName;
        this.testingDatasetTableName = testingDatasetTableName; 
        this.connection = new SQLiteConnection(new File(TrainingDataset.class.getClassLoader().getResource(this.databaseFilename).getFile()));
        this.trainingDataset = new TrainingDataset(datasetMappingType);
        this.testingDataset = new TrainingDataset(datasetMappingType);

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
        System.out.println("\n");
        System.out.print("Connecting to database : '" + this.databaseFilename + "' ...");
        System.out.println("\n");

        // opening the connection to the database
        this.connection.open(allowCreateTable);

        // CONNNECTION SUCCESSFULL //

        // informing
        System.out.println("\n");
        System.out.println("Successfully connected to database : '" + this.databaseFilename + "' !");
        System.out.println("\n");
    }

    /**
	 * Closes the database by disposing of the connection.
	 */
	public void close() {
        // CLEARING DATASETS //

        this.clearDatasets();

		// DISCONNECTING FROM DATABASE //

        // informing
        System.out.println("\n");
        System.out.print("Disconnecting from database : '" + this.databaseFilename + "' ...");
        System.out.println("\n");

        // opening the connection to the database
        this.connection.dispose();

        // DISCONNNECTION SUCCESSFULL //

        // informing
        System.out.println("\n");
        System.out.println("Successfully disconnected from database : '" + this.databaseFilename + "' !");
        System.out.println("\n");
	}


    ////////////////////////////////////
    // LOADING DATASETS FROM DATABASE //
    ////////////////////////////////////

    /**
     * Loads the training dataset from the table into the Database's Dataset object.
     * 
     * @throws SQLiteException If the call to the database fails.
     */
    public void loadTrainingDataset() throws SQLiteException{

        /////////////////
        // PREPERATION //
        /////////////////

        // informing
        System.out.println("\n");
        System.out.print("Loading training ratings from table : '" + this.trainingDatasetTableName + "' ...");
        System.out.println("\n");

        // tracking number of loaded ratings
        int count = 0;

        // SQLiteStatement object to hold the database information
        SQLiteStatement statement = this.connection.prepare("SELECT * FROM " + this.trainingDatasetTableName);

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
            this.trainingDataset.addRating(userID, itemID, itemRating, timestamp);

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
        System.out.println("\n");
        System.out.println("Successfully loaded training ratings from table : '" + this.trainingDatasetTableName + "' !");
        System.out.println("Number of Ratings : " + count);
        System.out.println("Number of entries in training dataset : " + this.trainingDataset.getNumberOfEntries());
        System.out.println("\n");
    }

    /**
     * Loads the testing dataset from the table into the Database's Dataset object.
     * 
     * @throws SQLiteException If the call to the database fails.
     */
    public void loadTestingDataset() throws Exception{

        /////////////////
        // PREPERATION //
        /////////////////

        // informing
        System.out.println("\n");
        System.out.print("Loading testing ratings from table : '" + this.testingDatasetTableName + "' ...");
        System.out.println("\n");

        // tracking number of loaded ratings
        int count = 0;

        // SQLiteStatement object to hold the database information
        SQLiteStatement statement = this.connection.prepare("SELECT * FROM " + this.testingDatasetTableName);

        /////////////
        // LOADING //
        /////////////

        // evaluating the SQL statement and iterating through the loaded rows
        while(statement.step()){
            // extracting information for this rating
            Integer userID = statement.columnInt(0);
            Integer itemID = statement.columnInt(1);
            Integer timestamp = statement.columnInt(2);

            // adding the rating to the dataset
            this.testingDataset.addRating(userID, itemID, Database.UNRATED_ITEM_RATING, timestamp);

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
        System.out.println("\n");
        System.out.println("Successfully loaded testing ratings from table : '" + this.testingDatasetTableName + "' !");
        System.out.println("Number of Ratings : " + count);
        System.out.println("Number of entries in testing dataset : " + this.testingDataset.getNumberOfEntries());
        System.out.println("\n");
    }

    /////////////////////////////////////////
    // CREATING NEW TRAINING AND TEST SETS //
    /////////////////////////////////////////

    // TODO

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * Clears the datasets populated with data from this database. Used
     * to free up memory for very large datasets.
     */
    public void clearDatasets(){
        this.trainingDataset.clear();
        this.testingDataset.clear();
    }

    /////////////////////////
    // GETTERS AND SETTERS //
    /////////////////////////

    /**
     * Getter method for the training dataset.
     * 
     * @return The training dataset associates with this database.
     */
    public TrainingDataset getTrainingDataset(){
        return this.trainingDataset;
    }

    /**
     * Getter method for the testing dataset.
     * 
     * @return The testing dataset associated with this database.
     */
    public TrainingDataset getTestingDataset(){
        return this.testingDataset;
    }
}