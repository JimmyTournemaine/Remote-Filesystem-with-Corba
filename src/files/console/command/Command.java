/**
 * Created on February 1st, 2017 for a project proposed by Mr Frank Singhoff as part of the teaching
 * unit system objects distributed at the University of Western Brittany.
 */
package files.console.command;

/**
 * Represents a command that the console client can run.
 *
 * @author Jimmy Tournemaine
 */
public interface Command {

    /**
     * Run the command
     *
     * @param args The arguments of the command if needed
     * @throws Exception
     */
    public void run(String[] args) throws Exception;
}
