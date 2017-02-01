package files.console.command;

import files.regular_file;

public abstract class FileCommand implements Command {

	protected regular_file file;
	
	public FileCommand(regular_file file) {
		this.file = file;
	}

}
