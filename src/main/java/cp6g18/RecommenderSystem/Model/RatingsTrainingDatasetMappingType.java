 package cp6g18.RecommenderSystem.Model;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
 */
public enum RatingsTrainingDatasetMappingType {
    // Types
    USERS_TO_ITEMS, // {user ID -> {item ID -> (rating,timestamp)}}
    ITEMS_TO_USERS; // {item ID -> {user ID -> (rating,timestamp)}}
}