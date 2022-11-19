package MFRecommenderSystem.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

/**
 * A matrix used within the Matrix Factorization algorithm.
 * 
 * Each matrix stores a mapping of object IDs (where objects are either items or users) to a list
 * of factors (i.e., vector of values).
 * 
 * Each list of factors is learnt during the Matrix Factorisation algorithm, and describes it's
 * corresponding object.
 */
public class MFMatrix{
    
    // MEMBER VARIABLES //
    private HashMap<Integer, ArrayList<Float>> matrix; // {object ID -> [factor]} (object is either user or item depending on matrix type)

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     * 
     * @param objects A list of object IDs to be stored in the matrix.
     * @param factors The number of factors (Dimensions) within the matrix for each object.
     * (i.e., the dimensionality of each object's vector).
     * @param mean The mean on the Gaussian spread of randomly generated initial numbers.
     * @param variance The variance on the Gaussian spread of randomly generated initial numbers.
     */
    public MFMatrix(Collection<Integer> objects, int factors, float mean, float variance){
        // initializing
        this.matrix = new HashMap<Integer, ArrayList<Float>>();

        // initializing the matrix
        this.init(objects, factors, mean, variance);
    }

    /**
     * Initializes the matrix.
     * 
     * @param objects A list of object IDs to be stored in the matrix.
     * @param factors The number of factors (Dimensions) within the matrix for each object.
     * @param mean The mean on the Gaussian spread of randomly generated initial numbers.
     * @param variance The variance on the Gaussian spread of randomly generated initial numbers.
     */
    private void init(Collection<Integer> objects, int factors, float mean, float variance){
        // iterating through list of objects
        for(int object : objects){
            // creating vector for this object (arraylist)
            ArrayList<Float> vector = new ArrayList<Float>();

            // creating random variable
            Random random = new Random();

            // initializing the vector with random values for each factor
            for(int i = 0; i < factors; i++){
                vector.add((float) (random.nextGaussian() * variance) + mean);
            }

            // adding the vector to the matrix (mapped to by object)
            this.matrix.put(object, vector);
        }
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

    /////////////////////////////////
    // SETTING DATA INTO THE MODEL //
    /////////////////////////////////

    /**
     * Sets an object vector into the matrix at the location of the object ID.
     * 
     * @param objectID The ID of the object the vector is being set for.
     * @param objectVector The vector being set into the matrix with the associated object.
     */
    public void setObjectVector(int objectID, ArrayList<Float> objectVector){
        // setting the object vector into the matrix
        this.matrix.put(objectID, objectVector);
    }

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * Converts the MFMatrix to a string.
     * 
     * @return String representation of the MFMatrix.
     */
    public String toString(){
        // returning string representation of the matrix
        return this.matrix.toString();
    }
}
