package cp6g18.IBCFRecommenderSystem.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

/**
 * A structure that records the similarity between a set of objects (either items or users 
 * depending on the recommender system type).
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public class IBCFSimilarityMatrix {
    
    // MEMBER VARIABLES //
    private HashMap<Integer, HashMap<Integer,Float>> similarities; // {object ID -> {object ID -> similarity}

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     */
    public IBCFSimilarityMatrix(){
        this.similarities = new HashMap<Integer, HashMap<Integer,Float>>();
    }

    /////////////////////////////////////
    // ADDDING + REMOVING SIMILARITIES //
    /////////////////////////////////////

    /**
     * Sets/replaces a similarity within the similarity matrix.
     * 
     * @param object1ID The first object associated with the similarity.
     * @param object2ID The second object associated with the similarity.
     * @param similarity The similarity between the two objects.
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
     * Removes a similarity from the similarity matrix if it is contained within the matrix.
     * 
     * @param object1ID The ID of the first object associated with the similarity.
     * @param object1ID The ID of the second object associated with the similarity.
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
     * Getter method for the similarity between two objects.
     * 
     * @param object1ID The first object associated with the similarity.
     * @param object2ID The second object associated with the similarity.
     * @return The similarity between the two objects.
     */
    public float getSimilarity(int object1ID, int object2ID ){
        return this.similarities.get(object1ID).get(object2ID);
    }

    /**
     * Getter method for all similarities associated with a given object.
     * 
     * @param objectID The ID of the object who's similarities are being gathered.
     * @return The similarity between this object and all other objects as a mapping of other
     * objects to similarity value {object -> Similarity}.
     */
    public HashMap<Integer, Float> getSimilaritiesForObject(int objectID){
        return this.similarities.get(objectID);
    }

    /**
     * Helper method that returns an objects similarities as a sorted list, in order of most to 
     * least similar.
     * 
     * @param objectID The ID of the object who's similarities are being gathered.
     * @return The objects similarities to all other objects, sorted in most to least similar order.
     */
    public ArrayList<Entry<Integer, Float>> getSortedSimilaritiesForObject(int objectID) { 
        // getting all similarities
        HashMap<Integer, Float> similarities = this.similarities.get(objectID);

        // copying entry set into list
        ArrayList<Entry<Integer, Float>> listOfSimilarities = new ArrayList<Entry<Integer, Float>>(similarities.entrySet());

        // sorting list using custom comparator
        Collections.sort(listOfSimilarities, new Comparator<Entry<Integer,Float>>() {
            public int compare(Entry<Integer,Float> o1, Entry<Integer,Float> o2) {
                return ((o2)).getValue().compareTo((o1).getValue());
            }
        });

        // returning k most similar objects
        return listOfSimilarities;
    }

    /**
     * Helper method that returns the K most similar objects to the given object.
     * 
     * @param objectID The ID of the object who's similarities are being gathered.
     * @param k The number of similar objects to be gathered.
     * @return The k most similar objects.
     */
    public ArrayList<Entry<Integer, Float>> getKNearestSimilaritiesForObject(int objectID, int k) { 
        // getting all similarities
        HashMap<Integer, Float> similarities = this.similarities.get(objectID);

        // copying entry set into list
        LinkedList<Entry<Integer, Float>> listOfSimilarities = new LinkedList<Entry<Integer, Float>>(similarities.entrySet());

        // sorting list using custom comparator
        Collections.sort(listOfSimilarities, new Comparator<Entry<Integer,Float>>() {
            public int compare(Entry<Integer,Float> o1, Entry<Integer,Float> o2) {
                return ((o2)).getValue().compareTo((o1).getValue());
            }
        });
 
        // new list to storefor k most similar similarites
        ArrayList<Entry<Integer, Float>> kNearestSimilarities = new ArrayList<Entry<Integer, Float>>();

        // copying K entries into a list
        for(int i = 0; i < k; i++){
            kNearestSimilarities.add(listOfSimilarities.get(i));
        }

        // returning k most similar objects
        return kNearestSimilarities;
    }

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * Determines if the similarity matrix contains a similarity value for a given pair of objects.
     * 
     * @param object1ID The first object associated with the similarity.
     * @param object1ID The second object associated with the similarity.
     * @return True if the matrix contains a similarity value for the given pair of objects, false
     * if not.
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
     * Converts the similarity matrix to a string.
     * 
     * @return String representation of the similarity matrix.
     */
    @Override
    public String toString(){
        return this.similarities.toString();
    }
}