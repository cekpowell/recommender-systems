package cp6g18.MFRecommenderSystem.Model;

import java.util.Collection;

/**
 * The model for a MFRecommender.
 * 
 * Contains two matricies - one for user vectors and one for item vectors.
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
     */
    public MFModel(Collection<Integer> users, Collection<Integer> items, int factors){
        // initializing
        this.userMFMatrix = new MFMatrix(users, factors);
        this.itemMFMatrix = new MFMatrix(items, factors);
    }

    ///////////////////////////////////
    // GETTING MATRIX FROM THE MODEL //
    ///////////////////////////////////

    /**
     * Gathers the user matrix associated with this model.
     * 
     * @return The user matrix assocaited with this model.
     */
    public MFMatrix getUserMFmatrix(){
        return this.userMFMatrix;
    }

    /**
     * Gathers the item matrix.
     * 
     * @return The item matrix associated with this model.
     */
    public MFMatrix getItemMfMatrix(){
        return this.itemMFMatrix;
    }
}