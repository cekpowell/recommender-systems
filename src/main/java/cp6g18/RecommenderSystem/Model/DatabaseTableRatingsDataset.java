package cp6g18.RecommenderSystem.Model;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteStatement;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
 */
public class DatabaseTableRatingsDataset extends RatingsDataset<SQLiteStatement,  // Typing of lists
                                                                Float,            // Typing of Raw item rating
                                                                SQLiteStatement>{ // Typing of data average

    // member variables
    private SQLiteConnection databaseConnection; // TODO
    private String tableName; // TODO

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
    public DatabaseTableRatingsDataset(SQLiteConnection databaseConnection, String tableName){
        // initializing
        this.databaseConnection = databaseConnection;
        this.tableName = tableName;
    }

    ////////////////////////////
    // ADDING DATA TO DATASET //
    ////////////////////////////

    /**
     * // TODO
     * 
     * @param userID
     * @param itemID
     * @param itemRating
     * @param timestamp
     */
    public void addRating(int userID, int itemID, float itemRating, int timestamp){
        // TODO
    }

    ///////////////////////////////////
    // GETTING DATA FROM THE DATASET //
    ///////////////////////////////////

    /**
     * // TODO
     * 
     * @return
     */
    public SQLiteStatement getUsers(){
        try{
            // preparing statement
            SQLiteStatement statement = this.databaseConnection.prepare("SELECT" + " " +  
                                                                            RatingsDataset.USER_ID_NAME + " " +
                                                                        "FROM" + " " +
                                                                            this.tableName + " " +
                                                                        "GROUP BY" + " " + 
                                                                            RatingsDataset.USER_ID_NAME);

            // returning prepared statement
            return statement;
        }
        catch(Exception e){
            System.out.println(e.toString());
            System.exit(0);
        }

        return null;
    }

    /**
     * // TODO
     * 
     * @return
     */
    public SQLiteStatement getItems(){
        try{
            // preparing statement
            SQLiteStatement statement = this.databaseConnection.prepare("SELECT" + " " +  
                                                                            RatingsDataset.ITEM_ID_NAME + " " +
                                                                        "FROM" + " " +
                                                                            this.tableName + " " +
                                                                        "GROUP BY" + " " + 
                                                                            RatingsDataset.ITEM_ID_NAME);

            // returning prepared statement
            return statement;
        }
        catch(Exception e){
            System.out.println(e.toString());
            System.exit(0);
        }

        return null;
    }

    /**
     * // TODO
     * 
     * @return
     */
    public SQLiteStatement getRatings(){
        try{
            // preparing statement
            SQLiteStatement statement = this.databaseConnection.prepare("SELECT" + " " + 
                                                                            RatingsDataset.RATING_NAME + " " +
                                                                        "FROM" + " " + 
                                                                            this.tableName);

            // returning prepared statement
            return statement;
        }
        catch(Exception e){
            System.out.println(e.toString());
            System.exit(0);
        }

        return null;
    }

    /**
     * // TODO
     * 
     * @param itemID
     * @return
     */
    public SQLiteStatement getUsersWhoRatedItem(int itemID){
        try{
            // preparing statement
            SQLiteStatement statement = this.databaseConnection.prepare("SELECT" + " " + 
                                                                        RatingsDataset.USER_ID_NAME + 
                                                                        " FROM" + " " + 
                                                                            this.tableName + " " +
                                                                        "WHERE" + " " + 
                                                                            RatingsDataset.ITEM_ID_NAME + "=" + itemID);

            // returning prepared statement
            return statement;
        }
        catch(Exception e){
            System.out.println(e.toString());
            System.exit(0);
        }

        return null;
    }

    /**
     * // TODO
     * 
     * @param item1ID
     * @param item2ID
     * @return
     */
    public SQLiteStatement getUsersWhoRatedItems(int item1ID, int item2ID){
        try{
            // preparing statement
            SQLiteStatement statement = this.databaseConnection.prepare("SELECT" + " " + 
                                                                            "A." + RatingsDataset.USER_ID_NAME + " " + 
                                                                        "FROM" + " " + 
                                                                            this.tableName + " " + "A" + " " +
                                                                        "INNER JOIN" + " " +
                                                                            this.tableName + " " + "B" + " " +
                                                                        "ON" + " " + 
                                                                            "A." + RatingsDataset.USER_ID_NAME + 
                                                                            "=" + 
                                                                            "B." + RatingsDataset.USER_ID_NAME + " " +
                                                                        "WHERE" + " " + 
                                                                            "A." + RatingsDataset.ITEM_ID_NAME + "=" + item1ID + " " +
                                                                            "AND" + " " +
                                                                            "B." + RatingsDataset.ITEM_ID_NAME + "=" + item2ID);

            // returning prepared statement
            return statement;
            
        }
        catch(Exception e){
            System.out.println(e.toString());
            System.exit(0);
        }

        return null;
    }

    /**
     * // TODO
     * 
     * @param userID
     * @return
     */
    public SQLiteStatement getItemsRatedByUser(int userID){
        try{
            // preparing statement
            SQLiteStatement statement = this.databaseConnection.prepare("SELECT" + " " +
                                                                            RatingsDataset.ITEM_ID_NAME + " " +
                                                                        "FROM" + " " + 
                                                                            this.tableName + " " +
                                                                        "WHERE" + " " + 
                                                                            RatingsDataset.ITEM_ID_NAME + "=" + userID);

            // returning prepared statement
            return statement;
        }
        catch(Exception e){
            System.out.println(e.toString());
            System.exit(0);
        }

        return null;
    }

    /**
     * // TODO
     * 
     * @param user1ID
     * @param user2ID
     * @return
     */
    public SQLiteStatement getItemsRatedByUsers(int user1ID, int user2ID){
        try{
            // preparing statement
            SQLiteStatement statement = this.databaseConnection.prepare("SELECT" + " " + 
                                                                            "A." + RatingsDataset.ITEM_ID_NAME + " " + 
                                                                        "FROM" + " " + 
                                                                            this.tableName + " " + "A" + " " +
                                                                        "INNER JOIN" + " " +
                                                                            this.tableName + " " + "B" + " " +
                                                                        "ON" + " " + 
                                                                            "A." + RatingsDataset.ITEM_ID_NAME + 
                                                                            "=" + 
                                                                            "B." + RatingsDataset.ITEM_ID_NAME + " " +
                                                                        "WHERE" + " " + 
                                                                            "A." + RatingsDataset.USER_ID_NAME + "=" + user1ID + " " +
                                                                            "AND" + " " +
                                                                            "B." + RatingsDataset.USER_ID_NAME + "=" + user2ID);


            // returning prepared statement
            return statement;
        }
        catch(Exception e){
            System.out.println(e.toString());
            System.exit(0);
        }

        return null;
    }

    /**
     * // TODO
     * 
     * @param userID
     * @param itemID
     * @return
     */
    public Float getUserRatingOfItem(int userID, int itemID){
        try{
            // preparing statement
            SQLiteStatement statement = this.databaseConnection.prepare("SELECT * FROM" + " " + 
                                                                            this.tableName + " " +
                                                                        "WHERE" + " " + 
                                                                            RatingsDataset.USER_ID_NAME + "=" + userID + " " +
                                                                        "AND" + " " + 
                                                                            RatingsDataset.ITEM_ID_NAME + "=" + itemID);

            // gathering data from the statement
            float rating = 0f;
            while(statement.step()){
                rating = (float) statement.columnDouble(RatingsDataset.ITEM_RATING_INDEX);
            }
            return rating;
        }
        catch(Exception e){
            System.out.println(e.toString());
            System.exit(0);
        }

        return null;
    }

    /**
     * Gathers the average rating of each user within the dataset.
     * 
     * @return A mapping of user IDs to their average rating.
     */
    public SQLiteStatement getAverageUserRatings(){
        try{
            // preparing statement
            SQLiteStatement statement = this.databaseConnection.prepare("SELECT" + " " +
                                                                            RatingsDataset.USER_ID_NAME + "," + " " + "avg(" + RatingsDataset.RATING_NAME + ")" + " " +
                                                                        "FROM" + " " +
                                                                            this.tableName + " " +
                                                                        "GROUP BY" + " " +
                                                                            RatingsDataset.USER_ID_NAME);

            // returning prepared statement
            return statement;
        }
        catch(Exception e){
            System.out.println(e.toString());
            System.exit(0);
        }

        return null;
    }

    /**
     * // TODO
     * 
     * @param userID
     * @return
     */
    public Float getAverageUserRating(int userID){
        try{
            // preparing statement
            SQLiteStatement statement = this.databaseConnection.prepare("SELECT" + " " +
                                                                            "avg(" + RatingsDataset.RATING_NAME + ")" + " " +
                                                                        "FROM" + " " +
                                                                            this.tableName + " " +
                                                                        "WHERE" + " " +
                                                                            RatingsDataset.USER_ID_NAME + "=" + userID + " " +
                                                                        "GROUP BY" + " " +
                                                                            RatingsDataset.USER_ID_NAME);

            // gathering average rating from statement
            float averageRating = 0f;
            while(statement.step()){
                averageRating = (float) statement.columnDouble(0);
            }

            // returning average rating
            return averageRating;
        }
        catch(Exception e){
            System.out.println(e.toString());
            System.exit(0);
        }

        return null;
    }

    /**
     * Gathers the average rating of each item within the dataset.
     * 
     * @return A mapping of item IDs to their average rating.
     */
    public SQLiteStatement getAverageItemRatings(){
        try{
            // preparing statement
            SQLiteStatement statement = this.databaseConnection.prepare("SELECT" + " " +
                                                                            RatingsDataset.ITEM_ID_NAME + "," + " " + "avg(" + RatingsDataset.RATING_NAME + ")" + " " +
                                                                        "FROM" + " " +
                                                                            this.tableName + " " +
                                                                        "GROUP BY" + " " +
                                                                            RatingsDataset.ITEM_ID_NAME);

            // returning prepared statement
            return statement;
        }
        catch(Exception e){
            System.out.println(e.toString());
            System.exit(0);
        }

        return null;
    }

    /**
     * // TODO
     * 
     * @param userID
     * @return
     */
    public Float getAverageItemRating(int itemID){
        try{
            // preparing statement
            SQLiteStatement statement = this.databaseConnection.prepare("SELECT" + " " +
                                                                            "avg(" + RatingsDataset.RATING_NAME + ")" + " " +
                                                                        "FROM" + " " +
                                                                            this.tableName + " " +
                                                                        "WHERE" + " " +
                                                                            RatingsDataset.ITEM_ID_NAME + "=" + itemID + " " +
                                                                        "GROUP BY" + " " +
                                                                            RatingsDataset.ITEM_ID_NAME);

            // gathering average rating from statement
            float averageRating = 0f;
            while(statement.step()){
                averageRating = (float) statement.columnDouble(0);
            }

            // returning average rating
            return averageRating;
        }
        catch(Exception e){
            System.out.println(e.toString());
            System.exit(0);
        }

        return null;
    }

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * // TODO
     * 
     * @return
     */
    public String toString(){
        return this.tableName;
    }
}
