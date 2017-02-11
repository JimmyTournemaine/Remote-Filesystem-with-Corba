/**
 * Created on February 1st, 2017 for a project proposed by Mr Frank Singhoff as part of the teaching
 * unit system objects distributed at the University of Western Brittany.
 */
package files.console.command;

import org.omg.CORBA.StringHolder;
import files.end_of_file;
import files.invalid_operation;
import files.regular_file;

/**
 * A command read some characters in a file.
 *
 * @author Jimmy Tournemaine
 */
public class ReadCommand extends FileCommand {

    public ReadCommand(regular_file file) {
        super(file);
    }

    @Override
    public void run(String[] args) throws Exception {
        if (args.length != 2) throw badParametersException();

        try {
            StringHolder strHolder = new StringHolder("");
            int size = Integer.parseInt(args[1]);
            file.read(size, strHolder);
            System.out.println(strHolder.value);
        } catch (NumberFormatException e) {
            throw badParametersException();
        } catch (invalid_operation e) {
            throw new Exception(args[0] + " " + args[1] + " : invalid operation");
        } catch (end_of_file eof) {
            throw new Exception(args[0] + " " + args[1] + " : end of file reached");
        }
    }

    public Exception badParametersException() {
        return new Exception("Usage : read n");
    }

}
