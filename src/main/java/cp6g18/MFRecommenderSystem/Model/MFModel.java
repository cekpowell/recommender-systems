package cp6g18.MFRecommenderSystem.Model;

import java.util.Collection;

import com.github.sh0nk.matplotlib4j.Plot;

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