package files.console.command;

import files.directory;
import files.directoryHolder;

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
