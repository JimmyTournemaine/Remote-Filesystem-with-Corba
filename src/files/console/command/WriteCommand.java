package files.console.command;

import files.invalid_operation;
import files.regular_file;

public class WriteCommand extends FileCommand {

	public WriteCommand(regular_file file) {
		super(file);
	}

	@Override
	public void run(String[] args) throws Exception {
		String line = System.console().readLine();
		
		try {
			file.write(line.length(), line);
		} catch(invalid_operation e) {
			throw new Exception("Writing is not allowed.");
		}
	}

}
