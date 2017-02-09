/**
 * Created on February 1st, 2017 for a project proposed by Mr Frank Singhoff 
 * as part of the teaching unit system objects distributed 
 * at the University of Western Brittany.
 */
package files.console.command;

import files.directory;
import files.directory_entry;
import files.directory_entryHolder;
import files.file_listHolder;
import files.file_type;

/**
 * A command to list files in the current directory
 */
public class ListFiles extends FolderCommand {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_BLUE = "\u001B[34m";
	
	public ListFiles(directory current) {
		super(current);
	}

	@Override
	public void run(String[] args) throws Exception {
		directory_entryHolder eh = new directory_entryHolder(new directory_entry("", file_type.regular_file_type));
		file_listHolder lh = new file_listHolder();
		
		current.list_files(lh);
		while (lh.value.next_one(eh)) {
			System.out.printf("%s%s%s\t", 
					(eh.value.type.value() == file_type._directory_type) ? ANSI_BLUE : ANSI_GREEN, 
					eh.value.name , 
					ANSI_RESET);
		}
		System.out.println();
	}
}
