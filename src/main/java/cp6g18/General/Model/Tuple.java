package cp6g18.General.Model;

import java.io.Serializable;

/**
 * A Pair of items. 
 * 
 * The items do not have to be of the same type.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public class Tuple<T,S> implements Serializable{

    // MEMBER VARIABLES //
    public T first; // the first item in the pair.
    public S second; // the second item in the pair.

    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     * 
     * Constructs a Tuple that contains a pair of items.
     * 
     * The items do not have to be of the same type.
     * 
     * @param first The first item in the pair.
     * @param second The second item in the pair.
     */
    public Tuple(T first, S second){
        // initializing
        this.first = first;
        this.second = second;
    }

    /**
     * Default constructor.
     * 
     * Constructs an empty Typle.
     */
    public Tuple(){}

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * Converts the Tuple to a string.
     * 
     * @return A String representation of the tuple.
     */
    @Override
    public String toString(){
        return ("(" + first.toString() + ", " + second.toString() + ")");
    }
}