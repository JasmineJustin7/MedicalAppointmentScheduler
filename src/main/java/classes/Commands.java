package classes;

/**class to run and process commands*/
public interface Commands {
    /**takes in user input and processes commands
     * @param commandLine takes in input from user*/
    void processCommand(String commandLine);
    /**runs the application*/
    void run ();
    /**terminates the application*/
    void stop();
}