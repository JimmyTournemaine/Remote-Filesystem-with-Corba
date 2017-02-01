package files.console.command;

import files.directory;

public abstract class FolderCommand implements Command {

	protected directory current;
	
	public FolderCommand(directory current) {
		this.current = current;
	}
	
	public directory getDirectory() {
		return current;
	}

}
