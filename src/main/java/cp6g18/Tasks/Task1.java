// package cp6g18.Tasks;

// import java.io.File;
// import java.util.ArrayList;

// import cp6g18.RecommenderSystem.Controller.DatasetQuerier;
// import cp6g18.RecommenderSystem.Controller.Evaluator;
// import cp6g18.RecommenderSystem.Model.Dataset.Column;
// import cp6g18.RecommenderSystem.Model.Dataset.Dataset;
// import cp6g18.RecommenderSystem.Model.Dataset.Row;

// /**
//  * @module  COMP3208: Social Computing Techniques
//  * @project Coursework
//  * @author  Charles Powell
//  * 
//  * -- DESCRIPTION -- 
//  * 
//  * Main Class for Task 1 - finds the MSE, RMSE and MAE for a set of predictions
//  * and truth ratings.
//  */
// public class Task1 {
    
//     // constants
//     private static final String RESULTS_FILE = "task1_results.csv"; // The name of the file the task 1 results will be written to
//     private static final String PREDICTIONS_FILE = "Task1" + File.separator + "predictions.csv"; // Name of file containing rating predictions
//     private static final int PREDICTIONS_RATING_INDEX = 2; // the column index of the predicted rating within the predictions dataset
//     private static final String TRUTHS_FILE = "Task1" + File.separator + "truths.csv"; // Name of file containing rating truths
//     private static final int TRUTHS_RATING_INDEX = 2; // The column index of the true rating within the truths dataset

//     //////////////////
//     // RUNNING TASK //
//     //////////////////

//     /**
//      * Runner funtion for Task 1.
//      * 
//      * Algorithm:
//      * - Loads the datasets from the resoures.
//      * - Gathers the evaluation metrics.
//      * - Outputs results to terminal.
//      * - Logs results to file.
//      */
//     public static void run(){
//         try{
//             // formatting
//             System.out.println("\n===== TASK 1 =====\n");

//             // PREPERATION //

//             // loading datasets
//             Dataset predictionsDataset = Dataset.readFromFile(Task1.PREDICTIONS_FILE);
//             Dataset truthsDataset = Dataset.readFromFile(Task1.TRUTHS_FILE);

//             // gathering needed columns
//             Column predictedRatings = DatasetQuerier.getColumn(predictionsDataset, Task1.PREDICTIONS_RATING_INDEX);
//             Column truthRatings = DatasetQuerier.getColumn(truthsDataset, Task1.TRUTHS_RATING_INDEX);

//             // CALCULATING RESULTS //

//             // getting evaluation metrics
//             float mse = Evaluator.getMSE(predictedRatings, truthRatings);
//             float rmse = Evaluator.getRMSE(predictedRatings, truthRatings);
//             float mae = Evaluator.getMAE(predictedRatings, truthRatings);

//             // adding results to list
//             ArrayList<String> resultsList = new ArrayList<String>();
//             resultsList.add(Float.toString(mse));
//             resultsList.add(Float.toString(rmse));
//             resultsList.add(Float.toString(mae));

//             // creating output row
//             Row resultsRow = new Row(resultsList);

//             // creating output dataset
//             Dataset resultsDataset = new Dataset();
//             resultsDataset.add(resultsRow);

//             // OUTPUTTING RESULTS //
            
//             // printing to screen
//             System.out.println("Task 1 Output:");
//             System.out.println(resultsDataset.toString());
            
//             // writing to file
//             System.out.println("Writing Task 1 Results to file : '" + Task1.RESULTS_FILE + "'");
//             resultsDataset.writeToFile(new File(Task1.RESULTS_FILE));
//             System.out.println("Task 1 results successfully written to file : '" + Task1.RESULTS_FILE + "'");

//             // formatting
//             System.out.println();
//         }
//         catch(Exception e){
//             System.out.println("Unable to run Task 1!" + "\t\n" + "Cause : " + e.toString());
//         }
//     }
// }