// package cp6g18.Tasks;

// import java.io.File;

// import cp6g18.RecommenderSystem.Model.Dataset.Dataset;

// /**
//  * @module  COMP3208: Social Computing Techniques
//  * @project Coursework
//  * @author  Charles Powell
//  * 
//  * -- DESCRIPTION -- 
//  * 
//  * Main Class for Task 2 - // TODO
//  */
// public class Task2 {
    
//     // constants
//     private static final String RESULTS_FILE = "task2_results.csv"; // The name of the file the task 1 results will be written to

//     private static final String TRAINING_FILE = "Task2" + File.separator + "train.csv"; // Name of file containing training data
//     private static final String TESTING_FILE = "Task2" + File.separator + "test.csv"; // Name of file containing testing data

//     //////////////////
//     // RUNNING TASK //
//     //////////////////

//     /**
//      * Runner funtion for Task 2.
//      * 
//      * Algorithm:
//      * // TODO
//      */
//     public static void run(){
//         try{
//             // formatting
//             System.out.println("\n===== TASK 2 =====\n");

//             // PREPERATION //

//             // loading datasets
//             Dataset trainingDataset = Dataset.readFromFile(Task2.TRAINING_FILE);
//             Dataset testingDataset = Dataset.readFromFile(Task2.TESTING_FILE);

//             // CALCULATING RESULTS //

//             // TODO

//             // OUTPUTTING RESULTS //
            
//             // printing to screen
//             System.out.println("Task 2 Output:");
//             //System.out.println(resultsDataset.toString());
            
//             // writing to file
//             System.out.println("Writing Task 2 Results to file : '" + Task2.RESULTS_FILE + "'");
//             //esultsDataset.writeToFile(new File(Task2.RESULTS_FILE));
//             System.out.println("Task 2 results successfully written to file : '" + Task2.RESULTS_FILE + "'");

//             // formatting
//             System.out.println();
//         }
//         catch(Exception e){
//             System.out.println("Unable to run Task 2!" + "\t\n" + "Cause : " + e.toString());
//         }
//     }
// }