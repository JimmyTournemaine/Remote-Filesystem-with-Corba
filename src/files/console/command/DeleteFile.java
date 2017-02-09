/**
 * Created on February 1st, 2017 for a project proposed by Mr Frank Singhoff as part of the teaching
 * unit system objects distributed at the University of Western Brittany.
 */
package files.console.command;

import files.directory;

/**
 * A command to delete a file in the given directory
 */
public class DeleteFile extends FolderCommand {

    public DeleteFile(directory current) {
        super(current);
    }

    @Override
    public void run(String[] args) throws Exception {
        if (args.length < 2) throw new Exception(String.format("Usage : %s file", args[0]));

        try {
            current.delete_file(args[1]);
        } catch (files.no_such_file e) {
            throw new Exception(
                    String.format("%s %s : No such File or directory", args[0], args[1]));
        }
    }

}
