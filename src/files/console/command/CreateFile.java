/**
 * Created on February 1st, 2017 for a project proposed by Mr Frank Singhoff 
 * as part of the teaching unit system objects distributed 
 * at the University of Western Brittany.
 */
package files.console.command;

import files.directory;
import files.regular_fileHolder;

/**
 * A command to create a new empty file
 */
public class CreateFile extends FolderCommand {

	public CreateFile(directory current) {
		super(current);
	}

	@Override
	public void run(String[] args) throws Exception {
		if(args.length < 2)
			throw new Exception(String.format("Usage : %s filename", args[0]));
		
		current.create_regular_file(new regular_fileHolder(), args[1]);
	}

}
