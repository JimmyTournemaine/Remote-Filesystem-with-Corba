package files;

import java.util.ArrayList;
import java.io.File;

public class directoryImpl extends directoryPOA {

	File file = new File("root");
	private int number_of_file;
	private ArrayList<directory_entry> files;
	
	public directoryImpl() {
		files = new ArrayList<directory_entry>();
		for(File f : file.listFiles()){
			files.add(new directory_entry(f.getName(), (f.isDirectory() ? file_type.directory_type : file_type.regular_file_type) ));
		}
		number_of_file = files.size();
	}
	
	public directoryImpl(File file) {
		this.file = file;
		this();
	}
	
	public directoryImpl(String name, directoryImpl parent) {
		file = new File(parent.file, name);
	}

	public int number_of_file() {
		return number_of_file;
	}

	public void open_regular_file(files.regular_fileHolder r, java.lang.String name, files.mode m)
			throws files.no_such_file, files.invalid_type_file {
		for (directory_entry entry : files) {
			if (entry.name.equals(name)) {
				if (entry.type != file_type.regular_file_type)
					throw new files.invalid_type_file();
				f.value = new regular_file(new File(file, entry.name), m);
				return;
			}
		}
		throw new files.no_such_file(name);
	}

	public void open_directory(files.directoryHolder f, java.lang.String name)
			throws files.no_such_file, files.invalid_type_file {
		for (directory_entry entry : files) {
			if (entry.name.equals(name)) {
				if (entry.type != file_type.directory_type)
					throw new files.invalid_type_file();
				f.value = new directoryImpl(name);
				return;
			}
		}
		throw new files.no_such_file(name);

	}

	public void create_regular_file(files.regular_fileHolder r, java.lang.String name) throws files.already_exist {
		for (directory_entry entry : files) {
			if (entry.name.equals(name))
				throw new files.already_exists();
		}
		(new File(file, name)).createNewFile();
		files.add(new directory_entry(name, file_type.regular_file_type));
	}

	public void create_directory(files.directoryHolder f, java.lang.String name) throws files.already_exist {
		for (directory_entry entry : files) {
			if (entry.name.equals(name))
				throw new files.already_exists();
		}
	
		(new File(file, name)).mkdir(); // Create the directory
		files.add(new directory_entry(name, file_type.directory_type)); // Add entry
	}

	public void delete_file(java.lang.String name) throws files.no_such_file {
		for (directory_entry entry : files) {
			if (entry.name.equals(name)) {
				(new File(file, entry.name)).delete(); // Delete the file/directory
				files.remove(entry); // Remove the entry
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
