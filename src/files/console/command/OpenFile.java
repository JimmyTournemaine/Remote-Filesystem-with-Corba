/**
 * Created on February 1st, 2017 for a project proposed by Mr Frank Singhoff as part of the teaching
 * unit system objects distributed at the University of Western Brittany.
 */
package files.console.command;

import files.directory;
import files.mode;
import files.regular_file;
import files.regular_fileHolder;

/**
 * A command to open a file.
 *
 * @author Jimmy Tournemaine
 */
public class OpenFile implements Command {

    private directory dir;
    private regular_file file;

    public OpenFile(directory folder) {
        dir = folder;
    }

    @Override
    public void run(String[] args) throws Exception {

        String message = String.format("Usage : %s filename mode\n", args[0]);
        message += "r \t read only\n";
        message += "rw \t read write (default)\n";
        message += "w \t write trunc\n";
        message += "w+ \t write append\n";

        if (args.length < 2) throw new Exception(message);

        mode m = null;
        if (args.length == 2) {
            m = mode.read_write;
        } else if (args[2].equals("r")) {
            m = mode.read_only;
        } else if (args[2].equals("rw")) {
            m = mode.read_write;
        } else if (args[2].equals("w")) {
            m = mode.write_trunc;
        } else if (args[2].equals("w+")) {
            m = mode.write_append;
        } else throw new Exception(message);

        try {
            regular_fileHolder fileHolder = new regular_fileHolder();
            dir.open_regular_file(fileHolder, args[1], m);
            file = fileHolder.value;
        } catch (files.no_such_file e) {
            throw new Exception(args[0] + " " + args[1] + " : no such file");
        } catch (files.invalid_type_file e) {
            throw new Exception(args[0] + " " + args[1] + " : invalid file type");
        }
    }

    public regular_file getFile() {
        return file;
    }

}
