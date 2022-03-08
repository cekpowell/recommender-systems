package cp6g18.CFRecommenderSystem.Model;

import java.io.File;
import java.util.Random;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteStatement;

import cp6g18.Tools.Logger;

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
public class Database{ 

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
     * @throws SQLiteException // TODO
     */
    public Database(String databaseFilename) throws SQLiteException{
        // configuring SQLite4Java - necesarry because of this: https://github.com/aws-samples/aws-dynamodb-examples/issues/22
        System.out.println();
        System.setProperty("sqlite4java.library.path", "src/main/resources/libs/");

        // initializing
        this.databaseFilename = databaseFilename;
        this.connection = new SQLiteConnection(new File(Database.class.getClassLoader().getResource(this.databaseFilename).getFile()));

        // connecting to database
        this.open(false);
    }

    /////////////////////////////////////////
    // OPENING/CLOSING DATABASE CONNECTION //
    /////////////////////////////////////////

    /**
     * Opens the database by opening the connection to the underlying 
     * database file.
     * 
     * @param allowCreateDatabase Specifies if the opening can create the database if it does not
     * already exist.
     * @throws SQLiteException If the connection to the database could not
     * be opened.
     */
    private void open(boolean allowCreateDatabase) throws SQLiteException{
        // CONNECTING TO DATABASE //

        // logging
        Logger.logProcessStart("Connecting to database : '" + this.databaseFilename + "'\n");

        // opening the connection to the database
        this.connection.open(allowCreateDatabase);

        // CONNNECTION SUCCESSFULL //

        // logging
        Logger.logProcessEnd("\nSuccessfully connected to database : '" + this.databaseFilename + "'");
    }

    /**
	 * Closes the database by disposing of the connection.
	 */
	public void close() {
		// DISCONNECTING FROM DATABASE //

        // logging
        Logger.logProcessStart("Disconnecting from database : '" + this.databaseFilename + "'\n");

        // opening the connection to the database
        this.connection.dispose();

        // DISCONNNECTION SUCCESSFULL //

        // logging
        Logger.logProcessEnd("\nSuccessfully disconnected from database : '" + this.databaseFilename + "'");
	}


    ///////////////////////////////////////////
    // LOADING DATASET OBJECTS FROM DATABASE //
    ///////////////////////////////////////////

    /**
     * // TODO
     * 
     * @param dataset
     * @param tableName
     */
    public void loadRatingsDataset(Dataset dataset, String tableName) throws SQLiteException{
        /////////////////
        // PREPERATION //
        /////////////////

        // logging
        Logger.logProcessStart("Loading ratings from table : '" + tableName + "'");

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
            Integer userID = statement.columnInt(RatingsDatabaseSchema.USER_ID.getColIndex());
            Integer itemID = statement.columnInt(RatingsDatabaseSchema.ITEM_ID.getColIndex());
            Float itemRating = (float) statement.columnDouble(RatingsDatabaseSchema.RATING.getColIndex());
            Integer timestamp = statement.columnInt(RatingsDatabaseSchema.TIMESTAMP.getColIndex());

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
        Logger.logProcessEnd("Successfully loaded ratings from table : '" + tableName + "' (" + count + " ratings)");
    }

    /////////////////////////
    // CREATING NEW TABLES //
    /////////////////////////

    /**
	 * Creates new table with the given name if one does not yet exist - clears
     * the data from the table if it does exist.
	 * 
	 * @param tableName The name of the table to be added to the databaxse
     * @throws SQLiteException Thrown due to error with sqlite4java library.
	 */
	private void createTable(String tableName) throws SQLiteException{
        // logging
        Logger.logProcessStart("Creating table '" + tableName + "' (if it does not yet exist, clearing it if it does) in database '" + this.databaseFilename + "'");

        // create the table if it does not exist
        this.connection.exec("CREATE TABLE IF NOT EXISTS " + tableName + " " + RatingsDatabaseSchema.getSchemaString());

        // delete entries from table in case it does exist
        this.connection.exec("DELETE FROM " + tableName);

        // logging
        Logger.logProcessEnd("Successfully created table '" + tableName + "' in database '" + this.databaseFilename + "'");
	}

    /**
     * // TODO
     * 
     * @param originalTrainingTableName
     * @param newTrainingTableName
     * @param newTestingTableName
     * @param ratio
     * @throws SQLiteException
     */
	public void createNewTrainingAndTestingTables(String originalTrainingTableName, String newTrainingTableName, String newTestingTableName, int ratio) throws SQLiteException{

        // logging
        Logger.logProcessStart("Creating new training and testing tables in database '" + this.databaseFilename + "' from table '" + originalTrainingTableName + "'");

        // creating new tables with the given table names
        this.createTable(newTrainingTableName);
        this.createTable(newTestingTableName);

        ///////////////
        // PREPARING //
        ///////////////

        // statement to gather data from existing table
        SQLiteStatement originalTrainingTableStatement = this.connection.prepare("SELECT * FROM " + originalTrainingTableName);

        // statements to add data to new tables
        SQLiteStatement newTrainingTableStatement = this.connection.prepare("INSERT INTO " + newTrainingTableName + "  VALUES (?,?,?,?)");
        SQLiteStatement newTestingTableStatement = this.connection.prepare("INSERT INTO " + newTestingTableName + " VALUES (?,?,?,?)");

        // count to keep track of number of ratings that have been iterated
        int count = 0;

        // incrementing count by random amount (to add randomness to generated sets)
        count += new Random().nextInt(ratio);

        ///////////////////////////////
        // ADDING DATA TO NEW TABLES //
        ///////////////////////////////

        // starting begin clause
        this.connection.exec("BEGIN");

        // iterating through entries in original table
        while(originalTrainingTableStatement.step()){
            // select whether to put it in the test or training set
            if (count % ratio == 0) {
                // insert in test set
                newTestingTableStatement.bind(RatingsDatabaseSchema.USER_ID.getColID(), originalTrainingTableStatement.columnInt(RatingsDatabaseSchema.USER_ID.getColIndex()));
                newTestingTableStatement.bind(RatingsDatabaseSchema.ITEM_ID.getColID(), originalTrainingTableStatement.columnInt(RatingsDatabaseSchema.ITEM_ID.getColIndex()));
                newTestingTableStatement.bind(RatingsDatabaseSchema.RATING.getColID(), Rating.UNRATED_ITEM_RATING);
                newTestingTableStatement.bind(RatingsDatabaseSchema.TIMESTAMP.getColID(), originalTrainingTableStatement.columnInt(RatingsDatabaseSchema.TIMESTAMP.getColIndex()));
                newTestingTableStatement.stepThrough();
                newTestingTableStatement.reset();
            } 
            else {
                // insert in training set
                newTrainingTableStatement.bind(RatingsDatabaseSchema.USER_ID.getColID(), originalTrainingTableStatement.columnInt(RatingsDatabaseSchema.USER_ID.getColIndex()));
                newTrainingTableStatement.bind(RatingsDatabaseSchema.ITEM_ID.getColID(), originalTrainingTableStatement.columnInt(RatingsDatabaseSchema.ITEM_ID.getColIndex()));
                newTrainingTableStatement.bind(RatingsDatabaseSchema.RATING.getColID(), originalTrainingTableStatement.columnInt(RatingsDatabaseSchema.RATING.getColIndex()));
                newTrainingTableStatement.bind(RatingsDatabaseSchema.TIMESTAMP.getColID(), originalTrainingTableStatement.columnInt(RatingsDatabaseSchema.TIMESTAMP.getColIndex()));
                newTrainingTableStatement.stepThrough();
                newTrainingTableStatement.reset();
            }
            
            // incrementing count
            count++;
        }

        // commiting changes
        this.connection.exec("COMMIT");

        ///////////////
        // FINISHING //
        ///////////////

        // closing sql statements
        originalTrainingTableStatement.dispose();
        newTrainingTableStatement.dispose();
        newTestingTableStatement.dispose();

        // logging
        Logger.logProcessEnd("Successfully created new training and testing tables in database '" + this.databaseFilename + "'");
    }
}

////////////////////
// HELPER CLASSES //
////////////////////

/**
 * // TODO
 */
enum RatingsDatabaseSchema{
    // TYPES // (Each type is a database column)
    USER_ID(
        "UserID",  // name of column
        "INTEGER", // datatype of column
        1,         // id of column
        0          // index of column
    ),
    ITEM_ID(
        "ItemID",  // name of column
        "INTEGER", // datatype of column
        2,         // id of column
        1          // index of column
    ),
    RATING(
        "Rating", // name of column
        "REAL",   // datatype of column
        3,        // id of column
        2         // index of column
    ),
    TIMESTAMP(
        "Timestamp", // name of column
        "INTEGER",   // datatype of column
        4,           // id of column
        3            // index of column
    );

    // MEMBER VARIABLES // (information about the columns)
    private String colName; // TODO
    private String colDataType; // TODO
    private int colID; // TODO
    private int colIndex; // TODO

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     * 
     * @param colName Name of the column within the database.
     * @param colDataType The datatype of the column within the database.
     * @param colID The ID of the column within the database.
     * @param colIndex Index for the column within the database.
     */
    private RatingsDatabaseSchema(String colName, String colDataType, int colID, int colIndex){
        // initializing
        this.colName = colName;
        this.colDataType = colDataType;
        this.colID = colID;
        this.colIndex = colIndex;
    }

    ////////////////////////////
    // GETTING SCHEME MESSAGE //
    ////////////////////////////

    /**
     * // TODO
     * 
     * @return
     */
    public static String getSchemaString(){
        return ("( " + 
                    RatingsDatabaseSchema.USER_ID.getColName() + " " + RatingsDatabaseSchema.USER_ID.getColDataType() + ", " +
                    RatingsDatabaseSchema.ITEM_ID.getColName() + " " + RatingsDatabaseSchema.ITEM_ID.getColDataType() + ", " +
                    RatingsDatabaseSchema.RATING.getColName() + " " + RatingsDatabaseSchema.RATING.getColDataType() + ", " +
                    RatingsDatabaseSchema.TIMESTAMP.getColName() + " " + RatingsDatabaseSchema.TIMESTAMP.getColDataType() + 
                ")");
    }

    /////////////////////////
    // GETTERS AND SETTERS //
    /////////////////////////

    /**
     * // TODO
     * 
     * @return
     */
    public String getColName(){
        return this.colName;
    }

    /**
     * // TODO
     * 
     * @return
     */
    public String getColDataType(){
        return this.colDataType;
    }

    /**
     * // TODO
     * 
     * @return
     */
    public int getColID(){
        return this.colID;
    }

    /**
     * // TODO
     * 
     * @return
     */
    public int getColIndex(){
        return this.colIndex;
    }
}