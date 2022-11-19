package MFRecommenderSystem.Model;

import java.util.Collection;

/**
 * The model for a MFRecommender.
 * 
 * The model is comprised of two MFMatrix objects:
 *  - One which stores the user vectors (i.e., the user matrix).
 *  - One which stores the item vectors (i.e., the item matrix).
 */
public class MFModel {
    
    // MEMBER VARIABLES //
    private MFMatrix userMFMatrix; // matrix that stores user vectors
    private MFMatrix itemMFMatrix; // matrix that stores item vectors

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     * 
     * @param users A list user IDs to be stored in model's user matrix.
     * @param items A list of item IDs to be stored in the model's item matrix.
     * @param factors The number of factors (dimensions) to be used in the model's matricies.
     * @param mean The mean on the Gaussian spread of randomly generated initial numbers.
     * @param variance The variance on the Gaussian spread of randomly generated initial numbers.
     */
    public MFModel(Collection<Integer> users, Collection<Integer> items, int factors, float mean, float variance){
        // initializing
        this.userMFMatrix = new MFMatrix(users, factors, mean, variance);
        this.itemMFMatrix = new MFMatrix(items, factors, mean, variance);
    }

    ///////////////////////////////////
    // GETTING MATRIX FROM THE MODEL //
    ///////////////////////////////////

    /**
     * Gathers the user matrix associated with this model.
     * 
     * @return The user matrix assocaited with this model.
     */
    public MFMatrix getUserMFMatrix(){
        return this.userMFMatrix;
    }

    /**
     * Gathers the item matrix.
     * 
     * @return The item matrix associated with this model.
     */
    public MFMatrix getItemMFMatrix(){
        return this.itemMFMatrix;
    }
}