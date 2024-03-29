

import Tasks.Task1;
import Tasks.Task2;
import Tasks.Task3;


/**
 * Main class for application - used to run the individual tasks.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public class App 
{
    /**
     * Main method - runs the individual tasks.
     * 
     * @param args The system arguments.
     */
    public static void main( String[] args ){        
        ////////////
        // TASK 1 //
        ////////////

        Task1.run();

        ////////////
        // TASK 2 //
        ////////////

        Task2.run();

        ////////////
        // TASK 3 //
        ////////////

        Task3.run();
    }
}