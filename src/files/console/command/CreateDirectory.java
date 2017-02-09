/**
 * Created on February 1st, 2017 for a project proposed by Mr Frank Singhoff 
 * as part of the teaching unit system objects distributed 
 * at the University of Western Brittany.
 */
package files.console.command;

import files.directory;
import files.directoryHolder;

/**
 * A command to create a new empty directory
 */
public class CreateDirectory extends FolderCommand {

	public CreateDirectory(directory current) {
		super(current);
	}

	@Override
	public void run(String[] args) throws Exception {
		if(args.length < 2) 
				throw new Exception(String.format("Usage : %s directory_name", args[0]));
		
		directoryHolder dirHolder = new directoryHolder();
		current.create_directory(dirHolder, args[1]);
		current = dirHolder.value;
	}

}
