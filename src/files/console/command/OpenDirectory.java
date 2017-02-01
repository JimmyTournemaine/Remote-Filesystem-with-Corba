package files.console.command;

import files.directory;
import files.directoryHolder;

public class OpenDirectory extends FolderCommand {

	private directory root;
	
	public OpenDirectory(directory root, directory current) {
		super(current);
		this.root = root;
	}

	@Override
	public void run(String[] args) throws Exception {
		/* cd root */
		if(args.length == 1){
			current = root;
			return;
		}
		/* cd directory */
		try {
			directoryHolder dirHolder = new directoryHolder();
			current.open_directory(dirHolder, args[1]);
			current = dirHolder.value;
		} catch (files.no_such_file e) {
			throw new Exception(String.format("%s %s : No such file or directory", args[0], args[1]));
		} catch (files.invalid_type_file e) {
			throw new Exception(String.format("%s %s : Invalid file type", args[0], args[1]));
		}
	}
	
	public directory getChild() {
		return current;
	}

}
