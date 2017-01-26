package files;

import java.util.ArrayList;
import java.io.File;

public class directoryImpl extends directoryPOA {

	File file = new File("root");
	private int number_of_file;
	private ArrayList<directory_entry> files;
	
	public directoryImpl() {
		file = new File("root");
		init();
	}
	
	public directoryImpl(File file) {
		this.file = file;
		init();
	}
	
	private void init() {
		files = new ArrayList<directory_entry>();
		for(File f : file.listFiles()){
			files.add(new directory_entry(
			    f.getName(), 
			    f.isDirectory() ? file_type.directory_type : file_type.regular_file_type
			));
		}
		number_of_file = files.size();
	}

	public int number_of_file() {
		return number_of_file;
	}

	public void open_regular_file(files.regular_fileHolder r, java.lang.String name, files.mode m)
			throws files.no_such_file, files.invalid_type_file, files.io {
			
		File f = new File(file, name);
		if(!f.exists()) throw new files.no_such_file();
		if(!f.isFile()) throw new files.invalid_type_file();
		
		try {
		    f.value = new regular_fileImpl(new File(file, name), m);
		} catch(FileNotFoundException|IOException e) {
		    throw new io(e.getMessage());
		}
	}

	public void open_directory(files.directoryHolder f, java.lang.String name)
			throws files.no_such_file, files.invalid_type_file {
			
	    File f = new File(file, name);
		if(!f.exists()) throw new files.no_such_file();
		if(!f.isFile()) throw new files.invalid_type_file();
		f.value = new directoryImpl(new File(file, name), m);
	}

	public void create_regular_file(files.regular_fileHolder r, java.lang.String name) throws files.already_exist {
		File f = new File(file, name);
		if(f.exists()) throw new files.already_exist();
        f.createNewFile();
		files.add(new directory_entry(name, file_type.regular_file_type));
		number_of_file++;
	}

	public void create_directory(files.directoryHolder f, java.lang.String name) throws files.already_exist {
		File f = new File(file, name);
		if(f.exists()) throw new files.already_exist();
	
		f.mkdir(); // Create the directory
		files.add(new directory_entry(name, file_type.directory_type)); // Add entry
		number_of_file++;
	}

	public void delete_file(java.lang.String name) throws files.no_such_file {
		for (directory_entry entry : files) {
			if (entry.name.equals(name)) {
				(new File(file, entry.name)).delete(); // Delete the file/directory
				files.remove(entry); // Remove the entry
				number_of_file--;
			}
		}
		throw new files.no_such_file(name);
	}

	/**
	 * Get an iterator to list files
	 * @param l a file_listHolder to get the list_file
	 * @return The number of files
	 */
	public int list_files(files.file_listHolder l) {
		l.value = new file_list(files);
		return files.size();
	}
}
