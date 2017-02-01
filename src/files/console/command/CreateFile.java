package files.console.command;

import files.directory;
import files.regular_fileHolder;

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
