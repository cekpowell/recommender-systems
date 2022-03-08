package cp6g18.Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * @module  COMP3208: Social Computing Techniques
 * @project Coursework
 * @author  Charles Powell
 * 
 * -- DESCRIPTION -- 
 * 
 * // TODO
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

    ////////////////////////////////////////
    // SERIALIZING / DESERIALIZING OBJECT //
    ////////////////////////////////////////

    /**
     * // TODO
     * 
     * @param object
     * @param file
     * @throws Exception
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
     * // TODO
     * 
     * @param object
     * @param file
     * @throws Exception
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
