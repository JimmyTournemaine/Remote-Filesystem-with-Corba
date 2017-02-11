/**
 * Created on February 1st, 2017 for a project proposed by Mr Frank Singhoff as part of the teaching
 * unit system objects distributed at the University of Western Brittany.
 */
package files.console.command;

import files.directory;

/**
 * A directory command abstraction.
 *
 * @author Jimmy Tournemaine
 */
public abstract class FolderCommand implements Command {

    protected directory current;

    /**
     * Construct the command for the given directory
     *
     * @param current The directory on which apply the command
     */
    public FolderCommand(directory current) {
        this.current = current;
    }

    /**
     * Get the current directory
     *
     * @return The current directory
     */
    public directory getDirectory() {
        return current;
    }

}
