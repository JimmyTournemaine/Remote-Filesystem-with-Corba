/**
 * Created on February 1st, 2017 for a project proposed by Mr Frank Singhoff as part of the teaching
 * unit system objects distributed at the University of Western Brittany.
 */
package files.console.command;

import files.invalid_offset;
import files.invalid_operation;
import files.regular_file;

/**
 * A command to set the file-pointer offset of the given file.
 */
public class SeekCommand extends FileCommand {

    public SeekCommand(regular_file file) {
        super(file);
    }

    @Override
    public void run(String[] args) throws Exception {
        if (args.length != 2) throw badParametersException();

        try {
            int offset = Integer.parseInt(args[1]);
            file.seek(offset);
        } catch (NumberFormatException e) {
            throw badParametersException();
        } catch (invalid_offset e) {
            throw new Exception(args[0] + " " + args[1] + " : invalid offset");
        } catch (invalid_operation e) {
            throw new Exception(args[0] + " " + args[1] + " : seek is not allowed.");
        }
    }

    public Exception badParametersException() {
        return new Exception("Usage : seek n");
    }

}
