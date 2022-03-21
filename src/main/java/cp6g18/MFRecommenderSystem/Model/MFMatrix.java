package cp6g18.MFRecommenderSystem.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * A matrix used within the Matrix Factorization algorithm.
 * 
 * Stores either a user-factor mappinigs, or item-factor mappings.
 */
public class MFMatrix{
    
    // MEMBER VARIABLES //
    private HashMap<Integer, ArrayList<Float>> matrix; // {object ID -> {factor ID -> value}}

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     * 
     * @param objects A list of object IDs to be stored in the matrix.
     * @param factors The number of factors (Dimensions) within the matrix for each object.
     */
    public MFMatrix(Collection<Integer> objects, int factors){
        // initializing
        this.matrix = new HashMap<Integer, ArrayList<Float>>();

        // initializing the matrix
        this.init(objects, factors);
    }

    /**
     * Initializes the matrix.
     * 
     * @param objects A list of object IDs to be stored in the matrix.
     * @param factors The number of factors (Dimensions) within the matrix for each object.
     */
    private void init(Collection<Integer> objects, int factors){
        // TODO
    }

    /////////////////////////////////
    // GETTING DATA FROM THE MODEL //
    /////////////////////////////////

    /**
     * Gathers the vector for a specific object using the object's ID.
     * 
     * @param objectID The ID of the object who's vector is being gathered.
     * @return The vector associated with the given object ID, null if there is no associated 
     * vector.
     */
    public ArrayList<Float> getObjectVector(int objectID){
        // returning the object's vector
        return this.matrix.get(objectID);
    }
}
