package General.Model;


/**
 * A evaluation metrics object.
 * 
 * Stores the MSE, RMSE, MAE of an evaluation of a recommender system.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public class Evaluation {
    
    // MEMBER VARIABLES //
    private float mse; // the Mean Square Error in the predictions
    private float rmse; // the Root Mean Square Error in the predictions
    private float mae; // the Mean Absolute Error in the predictions

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class  constructor.
     * 
     * @param mse The MSE value for the evaluation.
     * @param rmse The RMSE value for the evaluation.
     * @param mae The MAE value for the evaluation.
     */
    public Evaluation(float mse, float rmse, float mae){
        // initializing
        this.mse = mse;
        this.rmse = rmse;
        this.mae = mae;
    }

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * Converts the evaluation to a string.
     * 
     * @return The evaluation results as a string.
     */
    public String toString(){
        return this.mse + "," + this.rmse + "," + this.mae;
    }

    ////////////////////
    // GETTER METHODS //
    ////////////////////

    /**
     * Returns the MSE for this evaluation.
     * 
     * @return The MSE for this evaluation.
     */
    public float getMSE(){
        return this.mse;
    }

    /**
     * Returns the RMSE for this evaluation.
     *
     * @return The RMSE for this evaluation.
     */
    public float getRMSE(){
        return this.rmse;
    }

    /**
     * Returns the MAE for this evaluation.
     * 
     * @return The MAE for this evaluation.
     */
    public float getMAE(){
        return this.mae;
    }
}