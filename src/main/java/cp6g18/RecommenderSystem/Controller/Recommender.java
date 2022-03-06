package cp6g18.RecommenderSystem.Controller;

import cp6g18.RecommenderSystem.Model.ArrayListRatingsDataset;
import cp6g18.RecommenderSystem.Model.HashMapRatingsDataset;
import cp6g18.RecommenderSystem.Model.RecommenderType;
import cp6g18.RecommenderSystem.Model.SimilarityMatrix;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
 */
public abstract class Recommender{

    // member variables
    private RecommenderType recommenderType;
    private boolean isTrained;
    private SimilarityMatrix model;
    
    //////////////////
    // INITIALIZING //
    //////////////////

    /**
     * Class constructor.
     */
    public Recommender(){
        // initializing
        this.recommenderType = null;
        this.model = null;
        this.isTrained = false;

        // logging
        System.out.println("\nNew recommender system instantiated!");
    }

    /////////////////////
    // TRAINING SYSTEM //
    /////////////////////

    /**
     * // TODO
     * 
     * @param trainingDataset
     */
    public void train(HashMapRatingsDataset trainingDataset){
        // informing
        System.out.println("\nTraining recommender system...");

        // training the system using the dataset
        long startTime = System.currentTimeMillis();
        this.trainAux(trainingDataset);
        long endTime = System.currentTimeMillis();

        // setting the status of the training variable
        this.isTrained = true;

        // informing
        System.out.println("Recommender system successfully trained in " + ((float ) ((endTime - startTime) / 1000f)) + " seconds!");
    }

    /**
     * // TODO
     * 
     * @param trainingDataset
     */
    protected abstract void trainAux(HashMapRatingsDataset trainingDataset);

    ////////////////////////
    // MAKING PREDICTIONS //
    ////////////////////////

    /**
     * // TODO
     * 
     * @param dataset
     * @return
     * @throws
     */
    public ArrayListRatingsDataset makePredictions(ArrayListRatingsDataset dataset) throws Exception{
        // checking system has been trained               
        if(this.isTrained){
            return this.makePredictionsAux(dataset);
        }
        else{
            throw new Exception("Recommender not yet trained!");
        }
    }

    /**
     * // TODO
     * 
     * @param dataset
     * @return
     */
    protected abstract ArrayListRatingsDataset makePredictionsAux(ArrayListRatingsDataset dataset);

    /////////////////////////
    // GETTERS AND SETTERS //
    /////////////////////////

    /**
     * // TODO
     * 
     * @return
     */
    public SimilarityMatrix getModel(){
        return this.model;
    }

    public void setModel(SimilarityMatrix model){
        this.model = model;
    }
}