package cp6g18.MFRecommenderSystem.Controller;

import java.util.ArrayList;

import cp6g18.General.Controller.Recommender;
import cp6g18.General.Model.TestingRating;
import cp6g18.MFRecommenderSystem.Model.MFModel;
import cp6g18.MFRecommenderSystem.Model.MFTrainingDataset;
import cp6g18.Tools.Logger;

/**
 * A Matrix Factorisation Recommender.
 */
public class MFRecommender extends Recommender<MFTrainingDataset>{

    // CONSTANTS //

    // MEMBER VARIABLES //
    private MFTrainingDataset trainingDataset; // the training dataset used to train the recommender
    private MFModel model; // the model learnt from the training dataset
    private int factors; // the number of factors to be used in the user and item vectors
    private int minIterations; // the minimum number of iterations to be performed by the algorithm (termination factor).
    private float minChangeInMae; // the change MAE between two iterations that will cause the recommender to stop training if it is reached (termination factor).
    private float learningRate; // the learning rate of the algorithm - controls how much the vectors are adjusted each iteration
    private float regularizationRate; // the regularization rate of the algorithm - controls the extent of the regularization to avoid overfitting.
    private float mean; // The mean on the Gaussian spread of randomly generated initial numbers.
    private float variance; // The variance on the Gaussian spread of randomly generated initial numbers.
    
    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor. 
     * 
     * @param factors The number of factors to be used in the user and item vectors.
     * @param minIterations The minimum number of iterations to be performed by the algorithm (termination factor).
     * @param minChangeInMae the change MAE between two iterations that will cause the recommender to stop training if it is reached (termination factor).
     * @param learningRate The learning rate of the algorithm - controls how much the vectors are adjusted each iteration.
     * @param regularizationRate The regularization rate of the algorithm - controls the extent of the regularization to avoid overfitting.
     * @param mean The mean on the Gaussian spread of randomly generated initial numbers.
     * @param variance The variance on the Gaussian spread of randomly generated initial numbers.
     */
    public MFRecommender(int factors, int minIterations, float minChangeInMae, float learningRate, float regularizationRate, float mean, float variance){
        // initializing
        super();
        this.trainingDataset = null;
        this.model = null;
        this.factors = factors;
        this.minIterations = minIterations;
        this.minChangeInMae = minChangeInMae;
        this.learningRate = learningRate;
        this.regularizationRate = regularizationRate;
        this.mean = mean;
        this.variance = variance;
    }

    /////////////////////
    // TRAINING SYSTEM //
    /////////////////////

    /**
     * Trains the recommender using the provided training dataset.
     * 
     * @param trainingDataset The training dataset the recommender will be trained on.
     */
    public void train(MFTrainingDataset trainingDataset){
        // informing
        Logger.logProcessStart("Training recommender system");

        ///////////////
        // PREPARING //
        ///////////////

        // setting the training dataset
        this.trainingDataset = trainingDataset;

        // setting up the model
        this.model = new MFModel(trainingDataset.getUsers(), trainingDataset.getItems(), this.factors, this.mean, this.variance);
         
        /////////////////
        // CALCULATING //
        /////////////////

        // variables to help with the process
        int iterationCount = 1; // the number of iterations that have been performed
        float previousIterationMae = Float.MAX_VALUE; // the MAE of the previous training iteration
        float changeInMae = Float.MAX_VALUE; // the most recent change in MAE

        // iteratively training the model
        while(this.continueToTrain(iterationCount, changeInMae)){
            // performing a single training iteration
            float iterationMAE = this.trainOneIteration();

            // logging
            System.out.println("Iteration : " + iterationCount + " completed.");
            System.out.println("\tMAE = " + iterationMAE);

            // updating the variables
            iterationCount++;
            changeInMae = previousIterationMae - iterationMAE;
            previousIterationMae = iterationMAE;
        }

        ///////////////
        // FINISHING //
        ///////////////

        // informing
        Logger.logProcessEnd("Recommender system successfully trained");
    }

    /**
     * Performs one iteration of the matrix factorisation training algorithm.
     * 
     * @return The MAE across this iteration
     */
    private float trainOneIteration(){
        // variable to keep track of MAE across iteration
        float sumOfAbsoluteError = 0f;

        // getting shuffled training dataset
        ArrayList<TestingRating> ratings = this.trainingDataset.getShuffledRatings();

        // iterating over ratings and updating model for each rating
        for(TestingRating rating : ratings){

            ///////////////////////
            // CALCULATING ERROR //
            ////////////////////////

            // getting predicted rating from model
            ArrayList<Float> userVector = this.model.getUserMFMatrix().getObjectVector(rating.getUserID());
            ArrayList<Float> itemVector = this.model.getItemMFMatrix().getObjectVector(rating.getItemID());
            float predictedRating = MFRecommender.getDotProduct(userVector, itemVector);

            // calculating error between predicted rating and actual rating
            float ratingError = rating.getRating() - predictedRating;

            // adding absolute rating error to sum of absolute error
            sumOfAbsoluteError += Math.abs(ratingError);

            //////////////////////////
            // UPDATING USER VECTOR //
            //////////////////////////

            /**
             * qi = qi + y * ((eui * pu) - (h * qi))
             */

            // determining update amount
            ArrayList<Float> userError = MFRecommender.getScalarProduct(itemVector, ratingError);
            ArrayList<Float> userRegularization = MFRecommender.getScalarProduct(userVector, this.regularizationRate);
            ArrayList<Float> regularizedUserError = MFRecommender.getVectorSubtraction(userError, userRegularization);
            ArrayList<Float> userVectorUpdateAmount = MFRecommender.getScalarProduct(regularizedUserError, this.learningRate);

            // computing updated user vector
            ArrayList<Float> updatedUserVector = MFRecommender.getVectorAddition(userVector, userVectorUpdateAmount);
            
            // updating the user vector in the model
            this.model.getUserMFMatrix().setObjectVector(rating.getUserID(), updatedUserVector);

            //////////////////////////
            // UPDATING ITEM VECTOR //
            //////////////////////////

            /**
             * pu = pu + y * ((eui * qi) - (h * pu))
             */

            // determining update amount
            ArrayList<Float> itemError = MFRecommender.getScalarProduct(userVector, ratingError);
            ArrayList<Float> itemRegularization = MFRecommender.getScalarProduct(itemVector, this.regularizationRate);
            ArrayList<Float> regularizedItemError = MFRecommender.getVectorSubtraction(itemError, itemRegularization);
            ArrayList<Float> itemVectorUpdateAmount = MFRecommender.getScalarProduct(regularizedItemError, this.learningRate);

            // computing updated item vector
            ArrayList<Float> updatedItemVector = MFRecommender.getVectorAddition(itemVector, itemVectorUpdateAmount);

            // updating the item vector in the model
            this.model.getItemMFMatrix().setObjectVector(rating.getItemID(), updatedItemVector);
        }

        // returning mae for this iteration
        return sumOfAbsoluteError / ratings.size();
    }

    /**
     * Determines if the recommender must still be trained - i.e., it determines the stop
     * criteria for the training of the recommender.
     * 
     * @param numIterations The number of training iterations performed already.
     * @param changeInMae The change in MAE on the most recent training iteration.
     * @return True if the recommender must still be trained, false otherwise.
     */
    private boolean continueToTrain(int numIterations, float changeInMae){
        return (numIterations <= this.minIterations) || (changeInMae >= this.minChangeInMae);
    }

    ////////////////////////
    // MAKING PREDICTIONS //
    ////////////////////////

    /**
     * Calculates a predicted rating for the provided user-item pair.
     * 
     * @param userID The ID of the user in the rating.
     * @param itemID The ID of the item in the rating.
     * @return The predicted rating for the user-item pair.
     */
    protected float makePrediction(int userID, int itemID, int timestamp){
        /**
         * Predicting rating for user u and item i.
         * 
         * user vector for user u = p_u
         * 
         * item vector for item i = q_i
         * 
         * Prediction r_ui = q_i (dot product) p_u
         */
        
        // variable for predicted rating
        float predictedRating = 0f;

        // gathering vector for user 
        ArrayList<Float> userVector = this.model.getUserMFMatrix().getObjectVector(userID);

        // gathering vector for item
        ArrayList<Float> itemVector = this.model.getItemMFMatrix().getObjectVector(itemID);

        // handling cold starts
        if(userVector == null || itemVector == null){
            return predictedRating;
        }

        // computing predicted rating as dot product of user and item vectors
        predictedRating = MFRecommender.getDotProduct(userVector, itemVector);
        
        // returning predicted rating
        return predictedRating;
    }

    /////////////////////////
    // GETTERS AND SETTERS //
    /////////////////////////

    /**
     * Getter method for the recommender's training dataset.
     * 
     * @return The training dataset associated with this recommender.
     */
    public MFTrainingDataset getTrainingDataset(){
        return this.trainingDataset;
    }

    /**
     * Getter method for the recommender's model.
     * 
     * @return The model associated with this recommender.
     */
    public MFModel getModel(){
        return this.model;
    }

    /**
     * Setter method for the recommender's model.
     * 
     * @param model The new model to be associated with this recommender.
     */
    public void setModel(MFModel model){
        this.model = model;
    }

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * Computes the dot product of two vectors.
     * 
     * @param vector1 The first vector.
     * @param vector2 The second vector.
     * @return The dot product of the first and second vectors.
     */
    public static float getDotProduct(ArrayList<Float> vector1, ArrayList<Float> vector2){
        // variable to keep track of sum
        float dotProduct = 0f;

        // iterating through the vectors
        for(int i = 0; i < vector1.size(); i++){
            // incrementing the sum
            dotProduct += (vector1.get(i)) * (vector2.get(i));
        }

        // returning the sum
        return dotProduct;
    }

    /**
     * Computes the scalar product of a vector.
     * 
     * @param vector The vector to be scaled.
     * @param scalar The scalar multiplier.
     * @return The scalar product of the vector - every element in the vector multiplied by the 
     * scalar value.
     */
    public static ArrayList<Float> getScalarProduct(ArrayList<Float> vector, float scalar){
        // arraylist to store result
        ArrayList<Float> scalarProduct = new ArrayList<Float>();

        // iterating through the vector
        for(Float number : vector){
            // adding the scalar of this element to the scalar product vector
            scalarProduct.add(number * scalar);
        }

        // returning the scalar product
        return scalarProduct;
    }

    /**
     * Computes the addition of two vectors - a pairwise addition of each element in the vectors.
     * 
     * @param vector1 The first vector.
     * @param vector2 The second vector.
     * @return The pairwise addition of the two vectors.
     */
    public static ArrayList<Float> getVectorAddition(ArrayList<Float> vector1, ArrayList<Float> vector2){
        // arraylist to store result
        ArrayList<Float> addition = new ArrayList<Float>();

        // iterating through the vectors
        for(int i = 0; i < vector1.size(); i++){
            // adding the two elements and putting result in the new list
            addition.add(vector1.get(i) + vector2.get(i));
        }

        // returning the scalar product
        return addition;
    }

    /**
     * Computes the subtraction of two vectors - a pairwise subtraction of each element in the vectors.
     * 
     * @param vector1 The first vector.
     * @param vector2 The second vector.
     * @return The pairwise subtraction of the two vectors (vector1 - vector2).
     */
    public static ArrayList<Float> getVectorSubtraction(ArrayList<Float> vector1, ArrayList<Float> vector2){
        // arraylist to store result
        ArrayList<Float> addition = new ArrayList<Float>();

        // iterating through the vectors
        for(int i = 0; i < vector1.size(); i++){
            // subtracting the two elements and putting result in the new list
            addition.add(vector1.get(i) - vector2.get(i));
        }

        // returning the scalar product
        return addition;
    }
}