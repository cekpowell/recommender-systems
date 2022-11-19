

import General.Controller.Evaluator;
import General.Model.Database;
import General.Model.Evaluation;
import IBCFRecommenderSystem.Controller.IBCFRecommender;
import IBCFRecommenderSystem.Model.IBCFTrainingDataset;
import MFRecommenderSystem.Controller.MFRecommender;
import MFRecommenderSystem.Model.MFTrainingDataset;

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