package cp6g18.RecommenderSystem.Controller;

/**
 * // TODO
 */
public class Logger {
    
    // CONSTANTS //

    private static final int TIME_PRECISION = 4;

    // STATICS //

    private static long taskStartTime = 0;
    private static long taskEndTime = 0;
    private static long subTaskStartTime = 0;
    private static long subTaskEndTime = 0;
    private static long processStartTime = 0;
    private static long processEndTime = 0;

    //////////////////
    // LOGGING TASK //
    //////////////////

    /**
     * // TODO
     * 
     * @param taskNumber
     */
    public static void logTaskStart(int taskNumber){
        // recording time
        Logger.taskStartTime = System.currentTimeMillis();

        // logging
        System.out.println("\n=====================================");
        System.out.println(  "========== START OF TASK " + taskNumber + " ==========");
        System.out.println(  "=====================================");
    }

    /**
     * // TODO
     * 
     * @param taskNumber
     */
    public static void logTaskEnd(int taskNumber){
        // recording time
        Logger.taskEndTime = System.currentTimeMillis();

        // logging
        System.out.println("\n=====================================");
        System.out.println(  "========= END OF TASK " + taskNumber + " " + Logger.getTaskCompletionTime() + " =========");
        System.out.println(  "=====================================\n");
    }

    ///////////////////////
    // LOGGING SUB-TASKS //
    ///////////////////////

    /**
     * // TODO
     * 
     * @param message
     */
    public static void logSubTaskStart(String subTask){
        // recording time
        Logger.subTaskStartTime = System.currentTimeMillis();

        // logging
        System.out.println("\n============= " + subTask + " ==============");
    }

    /**
     * // TODO
     * 
     * @param message
     */
    public static void logSubTaskEnd(String subTask){
        // recording time
        Logger.subTaskEndTime = System.currentTimeMillis();

        // logging
        System.out.println("\n========== " + subTask + " COMPLETED " + Logger.getSubTaskCompletionTime() + " ===========");
    }

    ///////////////////////
    // LOGGING PROCESSES //
    ///////////////////////

    /**
     * // TODO
     * 
     * @param message
     */
    public static void logProcessStart(String message){
        // recording time
        Logger.processStartTime = System.currentTimeMillis();

        // logging
        System.out.println("\n" + message);
    }

    /**
     * // TODO
     * 
     * @param message
     */
    public static void logProcessEnd(String message){
        // recording time
        Logger.processEndTime = System.currentTimeMillis();

        // logging
        System.out.println(message + " " + Logger.getProcessCompletionTime());
    }

    ////////////////////////////////
    // GETTING COMPLETION TIMINGS //
    ////////////////////////////////

    /**
     * // TODO
     * 
     * @return
     */
    private static String getTaskCompletionTime(){
        return ("(" + ("" + ((float ) ((Logger.taskEndTime - taskStartTime) / 1000f))).substring(0, Logger.TIME_PRECISION) + "s)");
    }

    /**
     * // TODO
     * 
     * @return
     */
    private static String getSubTaskCompletionTime(){
        return ("(" + ("" + ((float ) ((Logger.subTaskEndTime - subTaskStartTime) / 1000f))).substring(0, Logger.TIME_PRECISION) + "s)");
    }

    /**
     * // TODO
     * 
     * @return
     */
    private static String getProcessCompletionTime(){
        return ("(" + ("" + ((float ) ((Logger.processEndTime - processStartTime) / 1000f))).substring(0, Logger.TIME_PRECISION) + "s)");
    }
}
