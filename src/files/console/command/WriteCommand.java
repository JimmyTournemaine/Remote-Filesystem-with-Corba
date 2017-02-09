/**
 * Created on February 1st, 2017 for a project proposed by Mr Frank Singhoff as part of the teaching
 * unit system objects distributed at the University of Western Brittany.
 */
package files.console.command;

import files.invalid_operation;
import files.regular_file;

/**
 * A command to write in a file.
 */
public class WriteCommand extends FileCommand {

    public WriteCommand(regular_file file) {
        super(file);
    }

    @Override
    public void run(String[] args) throws Exception {
        String line = System.console().readLine();

        try {
            file.write(line.length(), line);
        } catch (invalid_operation e) {
            throw new Exception("Writing is not allowed.");
        }
    }

}
