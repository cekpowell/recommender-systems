package cp6g18.RecommenderSystem.Controller;

import java.io.File;
import java.io.FileOutputStream;
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
public class FileWriter {
    
    /////////////////////
    // WRITING TO FILE //
    /////////////////////

    /**
     * Writes the object to the file.
     * 
     * @param object The object being written to the file.
     * @param file The file the object will be written to.
     * @throws Exception If object could not be written to the file.s
     */
    public static void writeObjectToFile(Object object, File file) throws Exception{
        try{
            // logging
            Logger.logProcessStart("Writing object to file : '" + file.getName() + "'' ...");

            // setting up output stream
            OutputStream out = new FileOutputStream(file);

            // gathering dataset content
            String content = object.toString();

            // writing content to file
            out.write(content.getBytes());

            // closing the file
            out.close();

            // logging
            Logger.logProcessEnd("Successfully written object to file : '" + file.getName()+ "' !");
        }
        catch(Exception e){
            throw new Exception("Unable to write object to file '" + file.getName() + "'.\n" + 
                                "Cause : " + e.toString());
        }
    }
}
