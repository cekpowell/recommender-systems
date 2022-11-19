package Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import General.Model.TestingDataset;
import General.Model.TestingRating;

/**
 * Defines static methods to handle reading/writing data to files.
 * 
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 */
public class FileHandler {
    
    ///////////////////////////////
    // WRITING TO FILE AS STRING //
    ///////////////////////////////

    /**
     * Writes the object to the file.
     * 
     * @param object The object being written to the file.
     * @param file The file the object will be written to.
     * @throws Exception If object could not be written to the file.s
     */
    public static void writeObjectToFileAsString(Object object, File file) throws Exception{
        try{
            // logging
            Logger.logProcessStart("Writing object as string to file : '" + file.getName() + "''");

            // setting up output stream
            OutputStream out = new FileOutputStream(file);

            // gathering dataset content
            String content = object.toString();

            // writing content to file
            out.write(content.getBytes());

            // closing the file
            out.close();

            // logging
            Logger.logProcessEnd("Successfully written object as string to file : '" + file.getName()+ "'");
        }
        catch(Exception e){
            throw new Exception("Unable to write object as string to file '" + file.getName() + "'.\n" + 
                                "Cause : " + e.toString());
        }
    }

    /**
     * Writes the given testing dataset to the file.
     * 
     * @param testingDataset The testing dataset being written to the file.
     * @param file The file the testing dataset is being written to.
     */
    public static void writeTestingDatasetToFile(TestingDataset testingDataset, File file) throws Exception{
        try{
            // logging
            Logger.logProcessStart("Writing object as string to file : '" + file.getName() + "''");

            // setting up output stream
            PrintStream out = new PrintStream(file);

            // iterating over the file and writing each line to the file
            for(TestingRating testingRating : testingDataset.getRatings()){
                out.println(testingRating.toString());
                
            }

            // closing the file
            out.flush();
            out.close();

            // logging
            Logger.logProcessEnd("Successfully written object as string to file : '" + file.getName()+ "'");
        }
        catch(Exception e){
            throw new Exception("Unable to write object as string to file '" + file.getName() + "'.\n" + 
                                "Cause : " + e.toString());
        }
    }

    ////////////////////////////////////////
    // SERIALIZING / DESERIALIZING OBJECT //
    ////////////////////////////////////////

    /**
     * Serializes the given object to the given file.
     * 
     * @param object The object being serialized to the file.
     * @param file The file the object will be serialized to.
     * @throws Exception If the object could not be serialized.
     */
    public static void serializeObjectToFile(Object object, File file) throws Exception{
        try{
            // logging
            Logger.logProcessStart("Serializing object to file : '" + file.getName() + "''");

            // setting up output stream
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));

            // writing content to file
            out.writeObject(object);

            // closing the file
            out.close();

            // logging
            Logger.logProcessEnd("Successfully serialized object to file : '" + file.getName()+ "'");
        }
        catch(Exception e){
            throw new Exception("Unable to serialize object to file '" + file.getName() + "'.\n" + 
                                "Cause : " + e.toString());
        }
    }

    /**
     * Deserializes an object from the give file.
     * 
     * @param file The File the object is being deserialized from.
     * @throws Exception If the object could not be deserialized from the file.
     */
    public static Object deSerializeObjectFromFile(File file) throws Exception{
        try{
            // logging
            Logger.logProcessStart("Deserializing object from file : '" + file.getName() + "''");

            // setting up input stream
            ObjectInputStream in =new ObjectInputStream(new FileInputStream(file));  

            // reading content from file
            Object object = in.readObject();  

            // closing the file
            in.close();

            // logging
            Logger.logProcessEnd("Successfully deserialized object from file : '" + file.getName()+ "'");

            // returning 
            return object;
        }
        catch(Exception e){
            throw new Exception("Unable to deserialize object from file '" + file.getName() + "'.\n" + 
                                "Cause : " + e.toString());
        }
    }
}