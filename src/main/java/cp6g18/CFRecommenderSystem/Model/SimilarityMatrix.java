package cp6g18.CFRecommenderSystem.Model;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
 */
public class SimilarityMatrix {
    
    // member variables
    private HashMap<Integer, HashMap<Integer,Float>> similarities; // {object ID -> {object ID -> similarity}

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     */
    public SimilarityMatrix(){
        this.similarities = new HashMap<Integer, HashMap<Integer,Float>>();
    }

    /////////////////////////////////////
    // ADDDING + REMOVING SIMILARITIES //
    /////////////////////////////////////

    /**
     * // TODO
     * 
     * @param object1ID
     * @param object2ID
     * @param similarity
     */
    public void setSimilarity(int object1ID, int object2ID, float similarity){

        // SIMILARITY FOR OBJECT 1 TO OBJECT 2 //

        HashMap<Integer, Float> object1Similarities = this.similarities.get(object1ID);

        if(object1Similarities == null){
            object1Similarities = new HashMap<Integer, Float>();
            this.similarities.put(object1ID, object1Similarities);
        }

        object1Similarities.put(object2ID, similarity);

        // SIMILARITY FOR OBJECT 2 TO OBJECT 1 //

        HashMap<Integer, Float> object2Similarities = this.similarities.get(object2ID);

        if(object2Similarities == null){
            object2Similarities = new HashMap<Integer, Float>();
            this.similarities.put(object2ID, object2Similarities);
        }

        object2Similarities.put(object1ID, similarity);
    }

    /**
     * // TODO
     * 
     * @param object1ID
     * @param object1ID
     * @return
     */
    public void removeSimilarity(int object1ID, int object2ID){
        // removing object 1 -> object 2 mapping
        if(this.similarities.get(object1ID) != null){
            this.similarities.get(object1ID).remove(object2ID);
        }

        // removing object 2 -> object 1 mapping
        if(this.similarities.get(object2ID) != null){
            this.similarities.get(object2ID).remove(object1ID);
        }
    }

    ///////////////////////////
    // GETTING SIMMILARITIES //
    ///////////////////////////

    /**
     * // TODO
     * 
     * @param object1ID
     * @param object2ID
     * @return
     */
    public float getSimilarity(int object1ID, int object2ID ){
        return this.similarities.get(object1ID).get(object2ID);
    }

    /**
     * // TODO
     * 
     * @param objectID
     * @return
     */
    public HashMap<Integer, Float> getSimilaritiesForObject(int objectID){
        return this.similarities.get(objectID);
    }

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * // TODO
     * 
     * @param object1ID
     * @param object1ID
     * @return
     */
    public boolean hasSimilarity(int object1ID, int object2ID){
        try{
            if(this.similarities.get(object1ID).get(object2ID) != null 
            && this.similarities.get(object2ID).get(object1ID) != null){
                return true;
            }
            else{
                return false;
            }
        }
        // thrown if no entry exists, therefore similarity does not exist
        catch(NullPointerException e){
            return false;
        }
        
    }

    /**
     * // TODO
     * 
     * @return 
     */
    @Override
    public String toString(){
        return this.similarities.toString();
    }
}