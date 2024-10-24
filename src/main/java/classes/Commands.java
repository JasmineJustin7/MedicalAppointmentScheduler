package classes;

public interface Commands {
    void processCommand(String commandLine);
    void run ();
    void stop();
}