

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

import General.Controller.Evaluator;
import General.Model.Database;
import General.Model.Evaluation;
import General.Model.TestingDataset;
import General.Model.Tuple;
import IBCFRecommenderSystem.Controller.IBCFRecommender;
import IBCFRecommenderSystem.Model.IBCFTrainingDataset;
import MFRecommenderSystem.Controller.MFRecommender;
import MFRecommenderSystem.Model.MFTrainingDataset;
import Tools.FileHandler;

/**
 * Performs testing on the system.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public class Test {

    // CONSTANTS //
    private static final float MIN_RATING = 1f; // the minimum rating that can be given to an item 
    private static final float MAX_RATING = 5f; // the maximum rating that can be given to an item
    
    /**
     * Main method - tests the application information.
     * 
     * @param args
     */
    public static void main(String[] args) throws Exception{
        Test.testTask4Recommender();
    }

    ////////////////////////////////////
    // TASK 3 RECOMMENDER GRID SEARCH //
    ////////////////////////////////////

    /**
     * Performs a grid search for the hyper-parameters for the MFRecommender.
     * 
     * @throws Exception ??
     */
    public static void gridSearchMFRecommender() throws Exception{

        ///////////////
        // PREPARING //
        ///////////////

        /**
         * To avoid the output from the recommender while it is being trained, we create two
         * output streams - the real one and the dummy one
         * 
         * We then do System.setOut() to switch between the real one and the dummy one so that
         * text is either outputted or not outputted.
         */

        // proper outputs stream
        PrintStream outputStream = System.out;

        // dummy output stream that doesnt output anything
        PrintStream noOutputStream = new PrintStream(new OutputStream(){
            public void write(int b) {
                // NO-OP
            }
        });

        /////////////////////////////////////////////
        // GATHERING TRAINING AND TESTING DATASETS //
        /////////////////////////////////////////////

        // database to get the data from
        Database database = new Database("data/Task3/database.db");

        // names for new training and testing tables
        String newTrainingTableName = "NEWtraining";
        String newTestingTableName = "NEWtesting";
        String newTestingTruthsTableName = "NEWtestingTRUTHS";

        // creating new trainning and testing sets
        database.createNewTrainingAndTestingTables("training", 
                                                   newTrainingTableName, 
                                                   newTestingTableName,
                                                   newTestingTruthsTableName,
                                                   10);

        // loadinng new training set
        MFTrainingDataset trainingDataset = new MFTrainingDataset();
        database.loadRatingsDataset(trainingDataset, newTrainingTableName);

        // loading new testing set
        TestingDataset testingDataset = new TestingDataset();
        database.loadRatingsDataset(testingDataset, newTestingTableName);

        // loading truths
        TestingDataset truths = new TestingDataset();
        database.loadRatingsDataset(truths, newTestingTruthsTableName);

        // dropping new training and testing tables from database
        database.dropTable(newTrainingTableName);
        database.dropTable(newTestingTableName);
        database.dropTable(newTestingTruthsTableName);

        //////////////////////////////////
        // GATHERING GRID OF PARAMETERS //
        //////////////////////////////////

        // DEFINING PARAMETER SCOPE //

        // learning rate adjustment frequency
        int minLearningRateAdjustmentFrequency = 2;
        int maxLearningRateAdjustmentFrequency = 50;
        int learningRateAdjustmentFrequencyStep = 2;
        // learning rate adjustment amount
        float minLearningRateAdjustmentFactor = 0.0f;
        float maxLearningRateAdjustmentFactor = 0.5f;
        float learningRateAdjustmentFactorStep = 0.05f;
        // regularization rate adjustment frequency
        int minRegularizationRateAdjustmentFrequency = 2;
        int maxRegularizationRateAdjustmentFrequency = 50;
        int regularizationRateAdjustmentFrequencyStep = 2;
        // regularization rate adjustment amount
        float minRegularizationRateAdjustmentFactor = 0.0f;
        float maxRegularizationRateAdjustmentFactor = 0.5f;
        float regularizationRateAdjustmentFactorStep = 0.05f;

        // CREATING GRID OF PARAMETERS //

        ArrayList<ArrayList<Float>> grid = generateHyperParameterGrid(minLearningRateAdjustmentFrequency, maxLearningRateAdjustmentFrequency, learningRateAdjustmentFrequencyStep,
                                                                      minLearningRateAdjustmentFactor, maxLearningRateAdjustmentFactor, learningRateAdjustmentFactorStep,
                                                                      minRegularizationRateAdjustmentFrequency, maxRegularizationRateAdjustmentFrequency, regularizationRateAdjustmentFrequencyStep,
                                                                      minRegularizationRateAdjustmentFactor,  maxRegularizationRateAdjustmentFactor, regularizationRateAdjustmentFactorStep);

        /////////////////////////////////////////////////////////
        // GRID SEARCHING OVER PARAMATER SPACE ON THE DATASETS //
        /////////////////////////////////////////////////////////
        
        // helper variables
        ArrayList<ArrayList<Float>> results = new ArrayList<ArrayList<Float>>();
        Tuple<ArrayList<Float>, Float> currentBest = new Tuple<ArrayList<Float>, Float>(new ArrayList<Float>(), Float.MAX_VALUE);
        ArrayList<Integer> indexesDone = new ArrayList<Integer>();

        // iterating over all rows of grid
        while(indexesDone.size() < grid.size()){

            // FORMATTING //
            System.out.println();

            // GETTING INDEX OF NEXT ROW (RANDOM) //

            // selecting a random row within the grid
            int index = new Random().nextInt(grid.size());

            // making sure this row has not been tested already
            while(indexesDone.contains(index)){
                index = new Random().nextInt(grid.size());
            }

            // GATHERING PARAMETERS AT THIS INDEX //

            // gathering the parameters at this index
            ArrayList<Float> paramaters = grid.get(index);
            int learningRateAdjustmentFrequency = paramaters.get(0).intValue();
            float learningRateAdjustmentFactor = paramaters.get(1);
            int regularizationRateAdjustmentFrequency = paramaters.get(2).intValue();
            float regularizationRateAdjustmentFactor = paramaters.get(3);

            // LOGGING //

            System.out.println("Test " + (indexesDone.size() + 1) + " out of " + grid.size() + " (" + ( (indexesDone.size() + 1) / grid.size() ) + "%)" ); 
            System.out.println("Testing recommender for parameters : "  + paramaters.toString());
            System.setOut(noOutputStream); // disabling outputting (so dont see output from training)

            // TRAINING RECOMMENDER WITH PARAMATERS //

            // creating recommender for this set of parameters
            MFRecommender mfRecommender = new MFRecommender(Test.MIN_RATING, 
                                                            Test.MAX_RATING, 
                                                            3, 
                                                            100, 
                                                            0.01f, 
                                                            learningRateAdjustmentFrequency, 
                                                            learningRateAdjustmentFactor, 
                                                            0.002f, 
                                                            regularizationRateAdjustmentFrequency, 
                                                            regularizationRateAdjustmentFactor, 
                                                            0.0f, 
                                                            0.01f);
            
            // training the recommender on the training dataset
            mfRecommender.train(trainingDataset);

            // RUNNING THE RECOMMENDER ON THE TESTING DATASET //

            TestingDataset predictions = mfRecommender.makePredictions(testingDataset);

            // EVALUATING PREDICTIONS //

            Evaluation evaluation = Evaluator.evaluatePredictions(predictions.getRatings(), truths.getRatings());

            // OUTPUTTING EVALUATION RESULT //

            System.setOut(outputStream); // enabling outputting (was disabled whilst training)
            System.out.println("Testing Complete.");
            System.out.println("Evaluation Result : " + evaluation.toString());

            // UPDATING CURRENT BEST MAE //

            if(evaluation.getMAE() < currentBest.second){
                currentBest.first = paramaters;
                currentBest.second = evaluation.getMAE();
            }

            // OUTPUTTING CURRENT BEST MAE //

            System.out.println("Current best : " + currentBest.toString());

            // UPDATING LIST OF COMPLETED INDEXES //

            indexesDone.add(index);

            // ADDING THE RESULT TO THE LIST OF RESULTS //
            
            // creating array list to hold this result
            ArrayList<Float> result = new ArrayList<Float>();
            
            // adding result to this list
            result.add((float) learningRateAdjustmentFrequency);
            result.add(learningRateAdjustmentFactor);
            result.add((float) regularizationRateAdjustmentFrequency);
            result.add(regularizationRateAdjustmentFactor);
            result.add(evaluation.getMAE());

            // adding result to list of results
            results.add(result);

            // writing results to file
            System.setOut(noOutputStream); // turning off output for this
            FileHandler.writeObjectToFileAsString(gridToString(results), new File("mfr_grid_search_results_lrfr_lrfa_rrfr_rrfa.txt"));
            System.setOut(outputStream); // turning output back on
        }
    }

    /**
     * Converts the given grid to a string.
     * 
     * @param grid The grid being converted into a string.
     * @return A string representation of the grid.
     */
    public static String gridToString(ArrayList<ArrayList<Float>> grid){
        // string
        String string = "";

        // populating string
        for(ArrayList<Float> row : grid){
            string += row.toString() + ",\n";
        }

        // returning string
        return string;
    }

    /**
     * Generates a grid of all possible hyper-parameters for the defined volume.
     */
    public static ArrayList<ArrayList<Float>> generateHyperParameterGrid (int minLearningRateAdjustmentFrequency, int maxLearningRateAdjustmentFrequency, int learningRateAdjustmentFrequencyStep,
                                                                          float minLearningRateAdjustmentFactor, float maxLearningRateAdjustmentFactor, float learningRateAdjustmentFactorStep,
                                                                          int minRegularizationRateAdjustmentFrequency, int maxRegularizationRateAdjustmentFrequency, int regularizationRateAdjustmentFrequencyStep,
                                                                          float minRegularizationRateAdjustmentFactor, float maxRegularizationRateAdjustmentFactor, float regularizationRateAdjustmentFactorStep){
        
        // initializing empty grid
        ArrayList<ArrayList<Float>> grid = new ArrayList<ArrayList<Float>>();

        // populating grid by iterating over the entire volume -  all possible combinations of hyper-params  
        
        // LEARNING RATE ADJUSTMENT FREQUENCY //
        for(float learningRateAdjustmentFrequency = minLearningRateAdjustmentFrequency; learningRateAdjustmentFrequency <= maxLearningRateAdjustmentFrequency; learningRateAdjustmentFrequency += learningRateAdjustmentFrequencyStep){
            // LEARNING RATE ADJUSMTNE FACTOR //
            for(float learningRateAdjustmentFactor = minLearningRateAdjustmentFactor; learningRateAdjustmentFactor <= maxLearningRateAdjustmentFactor; learningRateAdjustmentFactor+=learningRateAdjustmentFactorStep){
                // REGULARIZATION RATE ADJUSTMENT FREQUENCY //
                for(float regularizationRateAdjustmentFrequency = minRegularizationRateAdjustmentFrequency; regularizationRateAdjustmentFrequency <= maxRegularizationRateAdjustmentFrequency; regularizationRateAdjustmentFrequency+=regularizationRateAdjustmentFrequencyStep){
                    // REGULARIZATION RATE ADJUSTMENT FACTOR //
                    for(float regularizationRateAdjustmentFactor = minRegularizationRateAdjustmentFactor; regularizationRateAdjustmentFactor <= maxRegularizationRateAdjustmentFactor; regularizationRateAdjustmentFactor+=regularizationRateAdjustmentFactorStep){
                        // creating a new row for these entries
                        ArrayList<Float> currentRow = new ArrayList<Float>();
        
                        // adding these entries to the row
                        currentRow.add(learningRateAdjustmentFrequency);
                        currentRow.add(learningRateAdjustmentFactor);
                        currentRow.add(regularizationRateAdjustmentFrequency);
                        currentRow.add(regularizationRateAdjustmentFactor);
        
                        // adding this row to the grid
                        grid.add(currentRow);
                    }
                }
            }
        }

        // returning populated grid
        return grid;
    }

    ////////////////////
    // TESTING TASK 4 //
    ////////////////////

    public static void testTask4Recommender() throws Exception{
        Database database = new Database("data/Task4/database.db");

        Evaluation evaluation = Evaluator.evaluateRecommender(new MFRecommender(
                                                                                // min rating
                                                                                Test.MIN_RATING,
                                                                                // max rating
                                                                                Test.MAX_RATING,

                                                                                // factors
                                                                                20,

                                                                                // min iterations
                                                                                30,

                                                                                // learning rate
                                                                                0.008f,
                                                                                // learning rate adjustment frequency
                                                                                5,
                                                                                // learning rate adjustment factor
                                                                                0.1f,

                                                                                // regularization rate
                                                                                0.02f,
                                                                                // regularization rate adjustment frequency
                                                                                5,
                                                                                // regularization rate adjustment factor
                                                                                0.1f,

                                                                                // mean for initial matricies
                                                                                0f,
                                                                                // variance for initial matricies
                                                                                0.01f
                                                                                ), 
                                                              new MFTrainingDataset(), 
                                                              database, 
                                                              "training"
                                                              );

        System.out.println(evaluation);
    }

    ////////////////////
    // TESTING TASK 3 //
    ////////////////////

    public static void testTask3Recommender() throws Exception{
        Database database = new Database("data/Task3/database.db");

        Evaluation evaluation = Evaluator.evaluateRecommender(new MFRecommender(
                                                                                // min rating
                                                                                Test.MIN_RATING,
                                                                                // max rating
                                                                                Test.MAX_RATING,

                                                                                // factors
                                                                                3,

                                                                                // min iterations
                                                                                100,

                                                                                // learning rateno
                                                                                0.005f,
                                                                                // learning rate adjustment frequency
                                                                                6,
                                                                                // learning rate adjustment factor
                                                                                0.0f,

                                                                                // regularization rate
                                                                                0.05f,
                                                                                // regularization rate adjustment frequency
                                                                                18,
                                                                                // regularization rate adjustment factor
                                                                                0.0f,

                                                                                // mean for initial matricies
                                                                                0f,
                                                                                // variance for initial matricies
                                                                                0.01f
                                                                                ), 
                                                              new MFTrainingDataset(), 
                                                              database, 
                                                              "training"
                                                              );

        System.out.println(evaluation);
    }



    ////////////////////
    // TESTING TASK 2 //
    ////////////////////

    public static void testTask2Recommender() throws Exception{
        Database database = new Database("data/Task2/database.db");

        Evaluation evaluation = Evaluator.evaluateRecommender(new IBCFRecommender(
                                                                                  // min rating
                                                                                  Test.MIN_RATING,
                                                                                  // max rating
                                                                                  Test.MAX_RATING,
                                                                                  // significance value
                                                                                  50,
                                                                                  // minimum similarity
                                                                                  0.0f,
                                                                                  // temporal weight factor
                                                                                  0.001f
                                                                                  ), 
                                                              new IBCFTrainingDataset(), 
                                                              database, 
                                                              "training");

        System.out.println(evaluation);
    }
}