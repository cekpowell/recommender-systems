package cp6g18.General.Model;


/**
 * Holds the result of the evaluation of a recommender system.
 * 
 * Stores the MSE, RMSE, MAE of the evaluation.
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
}