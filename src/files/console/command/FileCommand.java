/**
 * Created on February 1st, 2017 for a project proposed by Mr Frank Singhoff as part of the teaching
 * unit system objects distributed at the University of Western Brittany.
 */
package files.console.command;

import files.regular_file;

/**
 * Represents a command for a regular file.
 */
public abstract class FileCommand implements Command {

    protected regular_file file;

    /**
     * Create a command with a file on which apply the command.
     *
     * @param file The file on which apply the command.
     */
    public FileCommand(regular_file file) {
        this.file = file;
    }

}
