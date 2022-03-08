package cp6g18.CFRecommenderSystem.Model;

import java.io.Serializable;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
 */
public class Tuple<T,S> implements Serializable{

    // member variables
    public T first; // TODO
    public S second; // TODO

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     * 
     * @param first
     * @param second
     */
    public Tuple(T first, S second){
        // initializing
        this.first = first;
        this.second = second;
    }

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * // TODO
     * 
     * @return
     */
    @Override
    public String toString(){
        return ("(" + first.toString() + ", " + second.toString() + ")");
    }
}