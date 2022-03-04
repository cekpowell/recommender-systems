package cp6g18.RecommenderSystem.Model;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
 */
public class Evaluation {
    // member variables (evaluation metrics)
    private float mse;
    private float rmse;
    private float mae;

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
        return "Evaluatioin Results" + "\n"
             + "\t MSE:" + this.mse + "\n"
             + "\t RMSE:" + this.rmse + "\n"
             + "\t MAE:" + this.mae + "\n";
    }

    /**
     * Converts the evaluation to a simple (i.e., shortened) string.
     * 
     * @return The evaluation results as a simple string.
     */
    public String toSimpleString(){
        return this.mse + "," + this.rmse + "," + this.mae;
    }
}