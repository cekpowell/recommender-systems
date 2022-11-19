package MFRecommenderSystem.Controller;

import java.util.ArrayList;

import General.Controller.Recommender;
import General.Model.TestingRating;
import MFRecommenderSystem.Model.MFModel;
import MFRecommenderSystem.Model.MFTrainingDataset;
import Tools.Logger;

/**
 * A Matrix Factorisation Recommender System.
 * 
 * All system variables are documented with a description of their purpose.
 * 
 * All methods are documented with a description of theirpurpose and functionality.
 * 
 * The key methods:
 *  - train() - used to train the recommender system.
 *  - makePrediction() - used to predict a rating for a given user-item pair.
 *  - These methods are complex, and make use of sub-routines. Their workflow
 *    is broken down into a number of steps, as detailed in their documentation.
 * 
 * Training:
 *  - Two matricies (one for item factors, and one for user factors) are defined.
 *  - The matricies are trained iteratively using a training dataset of ratings. 
 *  - Each iteration of the training process updates the matricies so that they 
 *  - provide predicted ratings closer to the values in the training dataset.
 *  - Some additional steps are included to improve the performance of the algorithm (documentation)
 *    given at point of relevant code).
 * 
 * Predicting Ratings:
 *  - A predicted rating for user u for item i is calculated by finding the dot product of the user
 *    vector for user u in the user matrix, and the item vector for the item i in the item matrix.
 *      - An explanation for this logic is given in the makePrediction() method.
 * - Some additional steps are included to improve the performance of the algorithm (documentation)
 *   given at point of relevant code).
 */
public class MFRecommender extends Recommender<MFTrainingDataset>{

    // MEMBER VARIABLES //
    private MFTrainingDataset trainingDataset; // the training dataset used to train the recommender
    private MFModel model; // the model learnt from the training dataset

    // SYSTEM PARAMETERS //
    private int factors; // the number of factors to be used in the user and item vectors
    private int minIterations; // the minimum number of iterations to be performed by the algorithm (termination factor).
    private float learningRate; // the learning rate of the algorithm - controls how much the vectors are adjusted each iteration
    private int learningRateAdjustmentFrequency; // the learning rate will be adjusted every x iterations.
    private float learningRateAdjustmentFactor; // the percentage reduction in the learning rate that will occur every adjustmment.
    private float regularizationRate; // the regularization rate of the algorithm - controls the extent of the regularization to avoid overfitting.
    private int regularizationRateAdjustmentFrequency; // the regularization rate will be adjusted every x iterations.
    private float regularizationRateAdjustmentFactor; // the percentage reduction in the regularization rate that will occur every adjustmment.
    private float mean; // The mean on the Gaussian spread of randomly generated initial numbers.
    private float variance; // The variance on the Gaussian spread of randomly generated initial numbers.
    
    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor. 
     * 
     * Constructs a new MFRecommender using the provided parameters.
     * 
     * @param minRating The minimum rating that can be given to an item
     * @param maxRating The maximum rating that can be given to an item
     * @param factors The number of factors to be used in the user and item vectors.
     * @param minIterations The minimum number of iterations to be performed by the algorithm (termination factor).
     * @param learningRate The learning rate of the algorithm - controls how much the vectors are adjusted each iteration.
     * @param learningRateAdjustmentFrequency the learning rate will be adjusted every x iterations.
     * @param learningRateAdjustmentFactor the percentage reduction in the learning rate that will occur every adjustmment.
     * @param regularizationRate The regularization rate of the algorithm - controls the extent of the regularization to avoid overfitting.
     * @param regularizationRateAdjustmentFrequency the regularization rate will be adjusted every x iterations.
     * @param regularizationRateAdjustmentFactor the percentage reduction in the regularization rate that will occur every adjustmment.
     * @param mean The mean on the Gaussian spread of randomly generated initial numbers.
     * @param variance The variance on the Gaussian spread of randomly generated initial numbers.
     */
    public MFRecommender(float minRating, 
                         float maxRating, 
                         int factors, 
                         int minIterations,
                         float learningRate, 
                         int learningRateAdjustmentFrequency, 
                         float learningRateAdjustmentFactor, 
                         float regularizationRate, 
                         int regularizationRateAdjustmentFrequency,
                         float regularizationRateAdjustmentFactor,
                         float mean, 
                         float variance){
        // initializing
        super(minRating, maxRating);
        this.trainingDataset = null;
        this.model = null;
        this.factors = factors;
        this.minIterations = minIterations;
        this.learningRate = learningRate;
        this.learningRateAdjustmentFrequency = learningRateAdjustmentFrequency;
        this.learningRateAdjustmentFactor = learningRateAdjustmentFactor;
        this.regularizationRate = regularizationRate;
        this.regularizationRateAdjustmentFrequency = regularizationRateAdjustmentFrequency;
        this.regularizationRateAdjustmentFactor = regularizationRateAdjustmentFactor;
        this.mean = mean;
        this.variance = variance;
    }

    /////////////////////
    // TRAINING SYSTEM //
    /////////////////////

    /**
     * Trains the recommender using the provided training dataset according to the Matrix 
     * Factorisation algorithm.
     * 
     * The recommender is trained by performing a number of training iterations.
     * 
     * Each training iteration is carried out using the trainOneIteration() method. Refer to this
     * method's documentaiton for the precise details of its workflow.
     * 
     * Iterations will continue to be performed until the 'shouldPerformAnotherTrainingIteration()'
     * method returns false. The 'shouldPerformAnotherTrainingIteration()' uses a number of 
     * termination factors to determine if another training iteration should be performed based
     * on the current state of the system. Refer to this method's documentation for the precise
     * details of it's workflow.
     * 
     * @param trainingDataset An MFTrainingDataset dataset that contains the ratings that will
     * be used to train the MFRecommender.
     */
    public void train(MFTrainingDataset trainingDataset){
        // logging
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

        // helper variables
        int iterationCount = 1; // counter for the number of iterations that have been performed

        // iteratively training the model
        while(this.shouldPerformAnotherTrainingIteration(iterationCount)){
            // performing a single training iteration
            float iterationMAE = this.trainOneIteration(iterationCount);

            // logging the results of the iteration
            Logger.logProcessMessage("Iteration : " + iterationCount + " completed.");
            Logger.logProcessMessage("\tMAE = " + iterationMAE);

            // updating the iteration count
            iterationCount++;
        }

        ///////////////
        // FINISHING //
        ///////////////

        // informing
        Logger.logProcessEnd("Recommender system successfully trained");
    }

    /**
     * Performs one iteration of the Matrix Factorisation training algorithm, as presented
     * in the lectures (with some additions), and using the ratings present in the training dataset.
     * 
     * This documentation describes the base algorithm + formula for training the system using
     * the Matrix Factorisation algorithm, the additions that have been made in this implementation,
     * and the full algorithm as a result of these additions. The full algorithm is broken down into
     * a number of steps, and the actual code is labelled with these steps.
     * 
     * One training iteration will perform one pass over the training dataset (in a random order), 
     * and for each training rating, will:
     *  - Make a predicted rating for the user-item pair using the current model.
     *  - Calculate the error between this predicted rating and the known rating.
     *  - Update the user and item vectors for the current user and item as to reduce
     *    this error (documentation of the update formula provided below).
     *  - Two parameters control the learning rate (how much each vector changes in each iteration)
     *    and the regularization rate (the extent of the regularization to avoid overfitting).
     * 
     * The method returns the Mean Absolute Error (MAE) of this iteration's
     * predictions.
     *  - This is the mean of the absolute error between the predicted and known ratings
     *    for all of the ratings in the training dataset, during this training iteration.
     *   
     * Base Algorithm:
     *  - Shuffle the training dataset into a random order.
     *      - This is done so that each iteration does not go over the ratings in the same
     *        order, and therefore, each iteration does not get 'trapped'/distorted by the natural
     *        patterns that may lie in the rating dataset because of its ordering.
     *      - i.e., the training is able to focus on the ratings themselves, instead of their order.
     *  - Iterate over the shuffled dataset, and for each rating:
     *      - Compute the current models prediction for the user-item pair in this rating.
     *      - Calculate the error between the prediction and the actual rating.
     *      - Update user and item matrix vectors for the relevant user and item according to the
     *        update formula (see below).
     * 
     * Base Update Formula:
     *  
     *  - Considering a rating in the training dataset, r_ui, where:
     *      - User = u
     *      - item = i
     *      - P = user matrix
     *      - p_u = user vector for user u
     *      - Q = item matrix
     *      - q_i = item vector for item i
     *      - 
     * 
     *  - Parameters:
     *      - Learning rate = y
     *          - Controls how much the vectors are adjusted with each iteration.
     *      - Regularization rate = h
     *          - Controls the extent of the regularization to avoid overfitting
     * 
     *  - Predicted rating for user u and item i = r'_ui
     *      - r'_ui = dot product of p_u and q_i = p_u * u_i
     *      - Why?
     *          - p_u describes how user u connects to each of the factors.
     *          - q_i descrives how item i connects to each of the factors.
     *          - User-item pairs that are 'similar' will have 'similar' user factors - e.g., the factor
     *            values will be high in the same places.
     *          - User-item pairs that have 'similar' (e.g., similarly high) factors will have higher 
     *            dot-products and thus higher predicted ratings.
     *          - p_u * q_i describes the INTERACTION between user u and item i, and thus predicts how
     *            user u rates item i.
     * 
     *  - Error in prediction:
     *      - e_ui = r_ui - r'_ui
     * 
     *  - Updated item vector:
     *      - q_i = q_i + y((e_ui * p_u) - (h * q_i))
     *      - Why?
     *          - q_i is the current user vector.
     *          - e_ui * p_u descrbes the error p_u has in the prediction of the user-item rating.
     *          - Adjusting by y(e_ui * p_u) makes q_i give a prediction closer to the true value.
     *          - Subtracting by (h - q_i) ensures that the model does not adjust too much to the 
     *            training dataset, and therefore that it does not overfit.
     * 
     *  - Updated user vector:
     *      - p_u = p_u + y((e_ui * q_i) - (h * p_u))
     *      - Why?
     *          - p_u is the current user vector.
     *          - e_ui * q_i descrbes the error p_u has in the prediction of the user-item rating.
     *          - Adjusting by y(e_ui * q_i) makes p_u give a prediction closer to the true value.
     *          - Subtracting by (h - p_u) ensures that the model does not adjust too much to the 
     *            training dataset, and therefore that it does not overfit.
     * 
     * Additions to the base algorithm:
     *  - The predicted rating is adjusted to take account for user and item biases that may lie 
     *    within the dataset.
     *      - Why?
     *          - Some users always rate higher than average, some users always rate lower than average.
     *          - Some items are always rated higher than average, some items are always rated higher
     *            than average.
     *          - By taking account for these biases when calculating the predicted rating for a user-item
     *            pair, a more accurate prediction can be obtained.
     *          - Dataset average rating = u
     *          - User bias b_u = u - user average rating.
     *          - Item bias b_i = u - item average rating.
     *          - Therefore:
     *              - Predicted rating = (user-item interaction) + u + b_u + b_i.
     *  - The learning rate is adjusted according to a step reduction function.
     *      - The 'learningRateAdjustmentFrequency' parameter determines how many iterations are performed
     *        before one reduction takes place.
     *          - e.g., a frequency of 10 will cause the learning rate to be reduced every 10 iterations.
     *      - The 'learningRateAdjustmentFactor' parameter determines size of the reduction step.
     *          - e.g., a factor of 0.1 will cause the learning rate to be reduced by 10% every reduction step.
     *      - Why?
     *          - Decreasing the learning rate over time allows for the model to make smaller and smaller 
     *            adjustments with each iteration, and thus more preciseley fit the model as it trains.
     *  - The regularization rate is adjusted according to a step reduction function.
     *      - The 'regularizationRateAdjustmentFrequency' parameter determines how many iterations are performed
     *        before one reduction takes place.
     *          - e.g., a frequency of 10 will cause the regularization rate to be reduced every 10 iterations.
     *      - The 'regularizationRateAdjustmentFactor' parameter determines size of the reduction step.
     *          - e.g., a factor of 0.1 will cause the regularization rate to be reduced by 10% every reduction step.
     *      - Why?
     *          - Decreasing the regularization rate over time allows for the model to make smaller and smaller 
     *            adjustments with each iteration, and thus more preciseley fit the model as it trains.
     * 
     * Edge Cases:
     *  - None.
     * 
     * Full Algorithm/Formula:
     * 
     *  - Definitions:
     *      - P = user matrix
     *      - p_u = user vector for user u
     *      - Q = Item Matrix
     *      - q_i = item vector for item i
     *      - n = number of iterations performed so far
     *      - y = learning rate
     *          - y_freq = learningRateUpdateFrequency
     *          - y_fac = learningRateUpdateFactor
     *      - h = regularization rate
     *          - h_freq = regularization Rate Update Frequency
     *          - h_fac = regularization Rate Update Factor
     *      - r'_ui = predicted rating for user u and item i
     *      - r_ui = actual rating for user u and item i
     * 
     *  - STEP 1: Shuffle training dataset
     * 
     *  - STEP 2: Update learning rate and regularization rate:
     *      - If n MODULUS y_freq = 0
     *          - Reduce y by the y_fac amount.
     *      - If n MODULUS h_freq = 0:
     *          - Reduce h by the h_fac amount.
     * 
     *  - STEP 3: For each rating in the shuffled training dataset:
     *      - User = u
     *      - item = i
     *      
     *      - STEP 3.1: Calculate user-item interaction
     *          - User-Item interaction for user u and item i = dot product of p_u and q_i = p_u * u_i
     * 
     *      - STEP 3.2: Calculate rating bias
     *          - a = dataset average rating
     *          - user bias = b_u = a - user average rating
     *          - item bias = b_i = a - item average rating
     *          - Rating bias = b_ui = a + b_u + b_i
     * 
     *      - STEP 3.3: Calculating predicted rating
     *          - predicted rating = r'_ui = (p_u * u_i) + b_ui
     * 
     *      - STEP 3.4: Calculate error in prediction
     *          - Error in prediction = e_ui = r_ui - r'_ui
     * 
     *      - STEP 3.5: Update user vector
     *          - Updated item vector:
     *          - q_i = q_i + y((eui * pu) - (h * qi))
     * 
     *      - STEP 3.6: Update item vector
     *          - Updated user vector:
     *          - p_u = p_u + y((eui * qi) - (h * pu))
     * 
     * @param iterationCount The number of training iterations that have been performed so far.
     * @return The MAE across this training iteration.
     */
    private float trainOneIteration(int iterationCount){

        /////////////////
        // PREPERATION //
        /////////////////

        // gathering dataset average
        float datasetAverageRating = this.getTrainingDataset().getDatasetAverageRating();

        // variable to keep track of absolute error across iteration (for MAE calculation)
        float sumOfAbsoluteError = 0f;

        ////////////////////////////////////////
        // STEP 1: SHUFFLING TRAINING DATASET //
        ////////////////////////////////////////

        // getting shuffled training dataset
        ArrayList<TestingRating> ratings = this.trainingDataset.getShuffledRatings();

        ////////////////////////////////////////////////////////
        // STEP 2: UPDATING LEARNING AND REGULARIZATION RATES //
        ////////////////////////////////////////////////////////

        // adjusting learning rate - adjustment made every learningRateAdjustmentFrequency iterations
        if((iterationCount % this.learningRateAdjustmentFrequency) == 0){
            // adjusting the learning rate according to the learnign rate factor
            this.learningRate -= (this.learningRate * this.learningRateAdjustmentFactor);
        }

        // adjusting regularization rate - adjustment made every regularizationRateAdjustmentFrequency iterations
        if((iterationCount % this.regularizationRateAdjustmentFrequency) == 0){
            // adjusting the regularization rate according to the adjustment factor
            this.regularizationRate -= (this.regularizationRate * this.regularizationRateAdjustmentFactor);
        }

        ////////////////////////////////////////////
        // STEP 3: UPDATING USER AND ITEM VECTORS //
        ////////////////////////////////////////////

        // iterating over ratings and updating model for each rating
        for(TestingRating rating : ratings){

            /////////////////
            // PREPERATION //
            /////////////////

            // gathering user vector p_u
            ArrayList<Float> userVector = this.model.getUserMFMatrix().getObjectVector(rating.getUserID());
            // gathering item vector q_i
            ArrayList<Float> itemVector = this.model.getItemMFMatrix().getObjectVector(rating.getItemID());

            // gathering user and item average ratings
            float userAverageRating = this.getTrainingDataset().getAverageUserRating(rating.getUserID());
            float itemAverageRating = this.getTrainingDataset().getAverageItemRating(rating.getItemID());

            /////////////////////////////////////////////////
            // STEP 3.1: CALCULATING USER-ITEM INTERACTION //
            /////////////////////////////////////////////////

            /// user-item interaction = p_u * q_i
            float userItemInteraction = MFRecommender.getDotProduct(userVector, itemVector);

            ///////////////////////////////////////
            // STEP 3.2: CALCULATING RATING BIAS //
            ///////////////////////////////////////

            // item bias b_i = dataset average - item average
            float itemBias = datasetAverageRating - itemAverageRating;
            // user bias b_u = dataset average - user average
            float userBias = datasetAverageRating - userAverageRating;
            // rating bias b_ui = datasetAverage + b_u + b_i
            float ratingBias = datasetAverageRating + itemBias + userBias;

            ////////////////////////////////////////////
            // STEP 3.3: CALCULATING PREDICTED RATING //
            ////////////////////////////////////////////

            // r_ui = userItemInteraction + rating bias
            float predictedRating = userItemInteraction + ratingBias;

            ///////////////////////////////////////////////
            // STEP 3.4: CALCULATING ERROR IN PREDICTION //
            ///////////////////////////////////////////////

            // e_ui = r_ui - r'_ui
            float ratingError = rating.getRating() - predictedRating;

            // adding absolute rating error to sum of absolute error (for this iterations MAE)
            sumOfAbsoluteError += Math.abs(ratingError);

            ////////////////////////////////////
            // STEP 3.5: UPDATING USER VECTOR //
            ////////////////////////////////////

            /*
             * Error in prediction = e_ui = r_ui - r'_ui
             * 
             * Learning rate = y
             *  - Controls how much the vectors are adjusted with each iteration.
             * Regularization rate = h
             *  - Controls the extent of the regularization to avoid overfitting
             * 
             * Updated user vector:
             *  - p_u = p_u + y((eui * qi) - (h * pu))
             */

            // DETERMINING UPDATE AMOUNT //

            // (eu * qui)
            ArrayList<Float> userError = MFRecommender.getScalarProduct(itemVector, ratingError);
            // (h * pu)
            ArrayList<Float> userRegularization = MFRecommender.getScalarProduct(userVector, this.regularizationRate);
            // (eu * qi) - (h * pu)
            ArrayList<Float> regularizedUserError = MFRecommender.getVectorSubtraction(userError, userRegularization);
            // y((eui * qi) - (h * pu))
            ArrayList<Float> userVectorUpdateAmount = MFRecommender.getScalarProduct(regularizedUserError, this.learningRate);

            // COMPUTING UPDATED VECTOR //

            // p_u + y((eui * qi) - (h * pu))
            ArrayList<Float> updatedUserVector = MFRecommender.getVectorAddition(userVector, userVectorUpdateAmount);
            
            // SETTING UPDATED VECTOR INTO MODEL //

            // updating the user vector in the model
            this.model.getUserMFMatrix().setObjectVector(rating.getUserID(), updatedUserVector);

            ////////////////////////////////////
            // STEP 3.6: UPDATING ITEM VECTOR //
            ////////////////////////////////////

            /*
             * Error in prediction = e_ui = r_ui - r'_ui
             * 
             * Learning rate = y
             *  - Controls how much the vectors are adjusted with each iteration.
             * Regularization rate = h
             *  - Controls the extent of the regularization to avoid overfitting
             * 
             * Updated item vector:
             *    q_i = q_i + y((eui * pu) - (h * qi))
             */

            // DETERMINING UPDATE AMOUNT //

            // (eui * pu)
            ArrayList<Float> itemError = MFRecommender.getScalarProduct(userVector, ratingError);
            // (h * qi)
            ArrayList<Float> itemRegularization = MFRecommender.getScalarProduct(itemVector, this.regularizationRate);
            // (eui * pu) - (h * qi)
            ArrayList<Float> regularizedItemError = MFRecommender.getVectorSubtraction(itemError, itemRegularization);
            // y((eui * pu) - (h * qi))
            ArrayList<Float> itemVectorUpdateAmount = MFRecommender.getScalarProduct(regularizedItemError, this.learningRate);

            // COMPUTING UPDATED VECTOR //

            // q_i + y((eui * pu) - (h * qi))
            ArrayList<Float> updatedItemVector = MFRecommender.getVectorAddition(itemVector, itemVectorUpdateAmount);

            // SETTING UPDATED VECTOR INTO MODEL //

            // updating the item vector in the model
            this.model.getItemMFMatrix().setObjectVector(rating.getItemID(), updatedItemVector);
        }

        ///////////////////////////////////////////////
        // step 4: RETURNING MAE ERROR FOR ITERATION //
        ///////////////////////////////////////////////

        // returning MAE for this iteration
        return (sumOfAbsoluteError / ratings.size());
    }

    /**
     * Determines if the recommender should perform another training iteration based on a set
     * of termination criteria.
     * 
     * The recommender system will perform another training iteration if:
     *  - The minimum number of training iterations has not yet been performed.
     * 
     * @param numIterations The number of training iterations performed already.
     * @return True if the recommender should perform another training iteration, false if not.
     */
    private boolean shouldPerformAnotherTrainingIteration(int numIterations){
        // MINIMUM NUMBER OF ITERATIONS NOT PERFORMED //
        if(numIterations < this.minIterations){
            // returning true
            return true;
        }
        // CONTINUE //
        else{
            return false;
        }
    }

    ////////////////////////
    // MAKING PREDICTIONS //
    ////////////////////////

    /**
     * Calculates a predicted rating for the provided user-item pair according to the Matrix 
     * Factorisation Algorithm.
     * 
     * This documentation describes the base algorithm + formula for making predictions using
     * the Matrix Factorisation algorithm, the additions that have been made in this implementation,
     * and the full algorithm as a result of these additions. The full algorithm is broken down into
     * a number of steps, and the actual code is labelled with these steps.
     * 
     * Base Algorithm:
     *  - Gather the user vector for user u.
     *  - Gather the item vector for item i.
     *  - Compute the dot product between the user vector and the item vector.
     *  - Return the result of the dot-product calculation as the predicted rating.
     * 
     * Base algorithm formula:
     *  - Predicting rating for:
     *      - User u
     *      - item i
     *  - p_u = User vector for user u (the user's vector within the user matrix)
     *  - q_i = Item vector for item i (the item's vector within the item matrix)
     *  - Prediction = Dot product of p_u and q_i = p_u * q_i
     *  - Why?
     *      - p_u describes how user u connects to each of the factors.
     *      - q_i descrives how item i connects to each of the factors.
     *      - User-item pairs that are 'similar' will have 'similar' user factors - e.g., the factor
     *        values will be high in the same places.
     *      - User-item pairs that have 'similar' (e.g., similarly high) factors will have higher 
     *        dot-products and thus higher predicted ratings.
     *      - p_u * q_i describes the INTERACTION between user u and item i, and thus predicts how
     *        user u rates item i.
     * 
     * Additions:
     *  - The predicted rating is adjusted to take account for user and item biases that may lie 
     *  within the dataset.
     *      - Why?
     *          - Some users always rate higher than average, some users always rate lower than average.
     *          - Some items are always rated higher than average, some items are always rated higher
     *            than average.
     *          - By taking account for these biases when calculating the predicted rating for a user-item
     *            pair, a more accurate prediction can be obtained.
     *          - Dataset average rating = u
     *          - User bias b_u = u - user average rating.
     *          - Item bias b_i = u - item average rating.
     *          - Therefore:
     *              - Predicted rating = (user-item interaction) + u + b_u + b_i.
     *  - Rating predictions are rounded to the nearest whole number.
     *      - Why?
     *          - The testing dataset, used to evaluate the performance of the recommender system, considers
     *            only ratings that are whole in value.
     *          - Rounding the ratings to the nearest whole value gives an increase in performance.
     *  - Ensuring all predictions are within the bounds of the possible ratings (e.g., less than
     *    6 and greater than 0).
     *      - Why?
     *          - Technically, it is possible according the algorithm that a predicted rating could be less
     *            than 1 or greater than 5.
     *          - The testing dataset, used to evaluate the performance of the recommender system, considers
     *            only ratings that are between 1 and 5.
     *          - Thus, by rounding any rating less than 1 to 1, or any rating greater than 5 to 5, we ensure
     *            that any unecesarry error is not incurred.
     * - Handling of cold starts:
     *      - If the item in the rating is not defined in the item matrix (i.e., the item was not in the training
     *        dataset), we have an item cold start.
     *          - This is handeled by using the average rating given by the user as the predicted rating.
     *          - Why?
     *              - As we have no information on the item, we are unable to determine how the user will interact
     *                with the item.
     *              - The most accurate rating we can therefore give to the item is the user's average rating.
     *      - If the user in the rating is not defined in the user matrix (i.e., the user was not in the training
     *        dataset), we have a user cold start.
     *          - This is handeled by using the average rating given to item as the predicted rating.
     *          - Why?
     *              - As we have no information on the user, we are unable to determine how the user will interact
     *                with the item.
     *              - The most accurate rating we can therefore give is the average rating given to the item.
     *      - If the user in the rating is nto defined in the user matrix, and the item in the rating is not defined
     *        in the user matrix (i.e., neither the user or the item were in the training dataset), then we have a
     *        user and item cold start.
     *          - This is handeled by using the average rating of the training dataset as the predicted rating.
     *          - Why?
     *              - We have no information on the user or the item.
     *              - The best possible rating that can be given is therefore the average rating of the training 
     *                dataset.
     * 
     * Full Algorithm Formula:
     * 
     *  - Definitions
     *      - P = user matrix
     *      - p_u = user vector for user u
     *      - Q = Item Matrix
     *      - q_i = item vector for item i
     *      - a = average rating for dataset
     *      - a_u = average rating for user u
     *      - a_i = average rating for item i
     *      - lb = lower bound of rating (e.g., 1 in a 1 to 5 rating scale)
     *      - ub = upper bound or ragting (e.g., 5 in a 1 to 5 rating scale)
     * 
     *  - STEP 1: Check for Cold-Starts
     *      - If user cold-start:
     *          - Return average rating of the item as the prediction
     *      - If item cold-start:
     *          - Return average rating of the user as the prediction.
     *      - If user and item cold-start:
     *          - Return average rating for the dataset as the prediction.
     * 
     *  - STEP 2: Calculate user-item interaction:
     *      - User-Item interaction for user u and item i:
     *          - = dot product of p_u and q_i = p_u * u_i
     * 
     *  - STEP 3: Calculate rating bias:
     *      - user bias = b_u = a - a_u
     *      - item bias = b_i = a - a_i
     *      - Rating bias = b_ui = a + b_u + b_i
     * 
     *  - STEP 4: Calculate predicted rating:
     *      - r'_ui = (p_u * u_i) + b_ui
     * 
     *  - STEP 5: Round the predicted rating to the nearest whole number.
     * 
     *  - STEP 6: Force the predicted rating to be within the lower and upper bounds of the rating 
     *  scheme.
     *      - lb <= r'ui <= ub
     * 
     *  - STEP 7: Return the predicted rating.
     *              
     * @param userID The ID of the user in the rating.
     * @param itemID The ID of the item in the rating.
     * @return The predicted rating for the user-item pair.
     */
    protected float makePrediction(int userID, int itemID, int timestamp){   

        /////////////////
        // PREPERATION //
        /////////////////

        // variable for predicted rating
        float predictedRating = this.getMinRating(); // base rating = minimum rating.

        // gathering factor vector for user (p_u)
        ArrayList<Float> userVector = this.model.getUserMFMatrix().getObjectVector(userID);

        // gathering factor vector for item (q_i)
        ArrayList<Float> itemVector = this.model.getItemMFMatrix().getObjectVector(itemID);

        // gathering dataset averages
        float datasetAverageRating = this.getTrainingDataset().getDatasetAverageRating();
        Float userAverageRating = this.getTrainingDataset().getAverageUserRating(userID);
        Float itemAverageRating = this.getTrainingDataset().getAverageItemRating(itemID);

        //////////////////////////////////////
        // STEP 1: CHECKING FOR COLD STARTS //
        //////////////////////////////////////

        // USER AND ITEM COLD START //
        if(userAverageRating == null && itemAverageRating == null){
            // return sensible value - average rating across all items
            return datasetAverageRating;
        }
        // USER COLD START //
        else if(userAverageRating == null){
            // return sensible value - average rating of item
            return itemAverageRating;
        }
        // ITEM COLD START //
        else if(itemAverageRating == null){
            // return sensible value - average rating of user
            return userAverageRating;
        }

        //////////////////////////////////////////////
        // STEP 2:CALCULATING USER ITEM INTERACTION //
        //////////////////////////////////////////////

        /// user-item interaction = p_u * q_i
        float userItemInteraction = MFRecommender.getDotProduct(userVector, itemVector);

        /////////////////////////////////////
        // STEP 3: CALCULATING RATING BIAS //
        /////////////////////////////////////

        // item bias b_i = dataset average - item average
        float itemBias = datasetAverageRating - itemAverageRating;
        // user bias b_u = dataset average - user average
        float userBias = datasetAverageRating - userAverageRating;
        // rating bias b_ui = datasetAverage + b_u + b_i
        float ratingBias = datasetAverageRating + itemBias + userBias;

        //////////////////////////////////////////
        // STEP 4: CALCULATING PREDICTED RATING //
        //////////////////////////////////////////

        // r_ui = userItemInteraction + rating bias
        predictedRating = userItemInteraction + ratingBias;

        ///////////////////////////////////////
        // STEP 5: ROUNDING PREDICTED RATING //
        ///////////////////////////////////////

        // rouding rating to nearest whole number (because ratings are all whole numbers)
        predictedRating = Recommender.convertToWholeNumber(predictedRating);

        //////////////////////////////////////////
        // STEP 6: FORCING RATING WITHIN BOUNDS //
        //////////////////////////////////////////

        // ensuring predictions are within bounds of rating (not less than min, not greater than max)
        predictedRating = this.forceRatingWithinBounds(predictedRating);

        //////////////////////////////////
        // STEP 7: RETURNING PREDICTION //
        //////////////////////////////////
        
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

    /////////////////////////////////
    // VECTOR MANIUPLATION METHODS //
    /////////////////////////////////

    /*
     * A number of methods to help with the vector manipulation required within the
     * matrix factorisation algorithm.
     */

    /**
     * Computes the dot product of two vectors.
     * 
     * The dot product of two vectors is the summation of the pairwise multiplication of all elements
     * in the two vectors.
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
     * Computes the scalar product of a vector with some scalar value.
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
     * Computes the pairwise addition of two vectors.
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
     * Computes the pairwise subtraction of two vectors.
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