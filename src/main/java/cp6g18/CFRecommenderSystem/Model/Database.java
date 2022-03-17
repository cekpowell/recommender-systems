package cp6g18.CFRecommenderSystem.Model;

import java.io.File;
import java.util.Random;

import com.almworks.sqlite4java.SQLite;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteStatement;
import com.almworks.sqlite4java.SQLiteException;

import cp6g18.Tools.Logger;

/**
 * An interface to an SQLite Database through the sqlite4java package.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public class Database{ 

    // member variables
    private String databaseFilename; // The name of the file of the associated database.
    private SQLiteConnection connection; // The Connection to the database.

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class Constructor. Opens the database with the provided filename. An error is
     * thrown if no database with this filename exists.
     * 
     * @param databaseFilename The name of the database file. 
     * @throws SQLiteException If it is not possible to connect to the database.
     */
    public Database(String databaseFilename) throws SQLiteException{
        // configuring SQLite4Java - necesarry because of this: https://github.com/aws-samples/aws-dynamodb-examples/issues/22
        System.out.println();
        SQLite.setLibraryPath("src/main/resources/libs/");

        // initializing
        this.databaseFilename = databaseFilename;
        this.connection = new SQLiteConnection(new File(this.databaseFilename));

        // connecting to database
        this.open(true);
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
     * Loads a dataset from the provided table name into the provided Dataset object.
     * 
     * @param dataset The Dataset object the dataset will be loaded into.
     * @param tableName The name of the database table the dataset will be loaded from.
     * @throws SQLiteException If an error occurs with the sqlite4java package.
     */
    public void loadRatingsDataset(Dataset dataset, String tableName) throws SQLiteException{
        /////////////////
        // PREPERATION //
        /////////////////

        // logging
        Logger.logProcessStart("Loading ratings from table : '" + tableName + "'");

        // clearing the dataset
        dataset.clear();

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
	public void createTable(String tableName) throws SQLiteException{
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
     * Drops the provided table from the database if it exists.
     * 
     * @param tableName The name of the table being dropped from the database.
     * @throws SQLiteException Thrown due to an error with sqlite4java library.
     */
    public void dropTable(String tableName)throws SQLiteException{
        // logging
        Logger.logProcessStart("Dropping table '" + tableName + "' from database '" + this.databaseFilename + "'");

        // create the table if it does not exist
        this.connection.exec("DROP TABLE IF EXISTS " + tableName);

        // logging
        Logger.logProcessEnd("Successfully dropped table '" + tableName + "' from database '" + this.databaseFilename + "'");
    }

    /**
     * Creates set of new training and testing datasets (along with the ground truths) within the
     * database using the provided information.
     * 
     * @param originalTrainingTableName The name of the table that the new training and testing
     * datasets will be constructed from.
     * @param newTrainingTableName The name of the table that the new training dataset will be
     * stored within.
     * @param newTestingTableName The name of the table that the new testing dataset will be stored
     * in.
     * @param ratio The split between the new training and testing datasets. 1 in every ratio amount
     * of data items within the original training dataset will be placed in the new testing dataset,
     * and the remainder will be placed in the new training dataset.
     * @throws SQLiteException If there is an error with the sqlite4java package.
     */
	public void createNewTrainingAndTestingTables(String originalTrainingTableName, String newTrainingTableName, String newTestingTableName, String newTestingTableTruthsName, int ratio) throws SQLiteException{
        // logging
        Logger.logProcessStart("Creating new training and testing tables in database '" + this.databaseFilename + "' from table '" + originalTrainingTableName + "'");

        // creating new tables with the given table names
        this.createTable(newTrainingTableName);
        this.createTable(newTestingTableName);
        this.createTable(newTestingTableTruthsName);

        ///////////////
        // PREPARING //
        ///////////////

        // statement to gather data from existing table
        SQLiteStatement originalTrainingTableStatement = this.connection.prepare("SELECT * FROM " + originalTrainingTableName);

        // statements to add data to new tables
        SQLiteStatement newTrainingTableStatement = this.connection.prepare("INSERT INTO " + newTrainingTableName + "  VALUES (?,?,?,?)");
        SQLiteStatement newTestingTableStatement = this.connection.prepare("INSERT INTO " + newTestingTableName + " VALUES (?,?,?,?)");
        SQLiteStatement newTestingTableTruthsStatement = this.connection.prepare("INSERT INTO " + newTestingTableTruthsName + " VALUES (?,?,?,?)");

        // count to keep track of number of ratings that have been iterated
        int count = 0;

        // incrementing count by random amount (to add randomness to generated sets)
        count += new Random().nextInt(ratio);

        ///////////////////////////////
        // ADDING DATA TO NEW TABLES //
        ///////////////////////////////

        // starting begin clause (queing all these operations to be performed in one go)
        this.connection.exec("BEGIN");

        // iterating through entries in original table
        while(originalTrainingTableStatement.step()){
            // select whether to put it in the test or training set
            if (count % ratio == 0) {
                // insert in test set (unknowns)
                newTestingTableStatement.bind(RatingsDatabaseSchema.USER_ID.getColID(), originalTrainingTableStatement.columnInt(RatingsDatabaseSchema.USER_ID.getColIndex()));
                newTestingTableStatement.bind(RatingsDatabaseSchema.ITEM_ID.getColID(), originalTrainingTableStatement.columnInt(RatingsDatabaseSchema.ITEM_ID.getColIndex()));
                newTestingTableStatement.bind(RatingsDatabaseSchema.RATING.getColID(), Rating.UNRATED_ITEM_RATING);
                newTestingTableStatement.bind(RatingsDatabaseSchema.TIMESTAMP.getColID(), originalTrainingTableStatement.columnInt(RatingsDatabaseSchema.TIMESTAMP.getColIndex()));
                newTestingTableStatement.stepThrough();
                newTestingTableStatement.reset();

                // insert into test set (truths)
                newTestingTableTruthsStatement.bind(RatingsDatabaseSchema.USER_ID.getColID(), originalTrainingTableStatement.columnInt(RatingsDatabaseSchema.USER_ID.getColIndex()));
                newTestingTableTruthsStatement.bind(RatingsDatabaseSchema.ITEM_ID.getColID(), originalTrainingTableStatement.columnInt(RatingsDatabaseSchema.ITEM_ID.getColIndex()));
                newTestingTableTruthsStatement.bind(RatingsDatabaseSchema.RATING.getColID(), originalTrainingTableStatement.columnInt(RatingsDatabaseSchema.RATING.getColIndex()));
                newTestingTableTruthsStatement.bind(RatingsDatabaseSchema.TIMESTAMP.getColID(), originalTrainingTableStatement.columnInt(RatingsDatabaseSchema.TIMESTAMP.getColIndex()));
                newTestingTableTruthsStatement.stepThrough();
                newTestingTableTruthsStatement.reset();
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
            
            // incrementing count by 1 or 2 (to make training and testing sets more random)
            int randomIncrement = new Random().nextInt(2) + 1;
            count += randomIncrement;
        }

        // commiting changes (commiting all changes made since the last begin statement).
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
 * Represents the schema of the database. 
 * 
 * Each type within the enum represents a column within the database, and stores the information
 * about this column.
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
    private String colName; // The name of the column
    private String colDataType; // the datatype of the column
    private int colID; // The column id of the column
    private int colIndex; // The index of the column within a list (i.e., ID - 1).

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
     * Helper method to construct the database schema string from theinformation stored
     * in the enum.
     * 
     * @return A string that represents the schema of the database.
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
     * Getter method for the name of the column.
     * 
     * @return The name of the column
     */
    public String getColName(){
        return this.colName;
    }

    /**
     * Getter method for the data type of the column.
     * 
     * @return The datatype of a particular column.
     */
    public String getColDataType(){
        return this.colDataType;
    }

    /**
     * Getter method for the ID of a column.
     * 
     * @return The ID of a column.
     */
    public int getColID(){
        return this.colID;
    }

    /**
     * Getter method for the index of a column.
     * 
     * @return The index of a column.
     */
    public int getColIndex(){
        return this.colIndex;
    }
}