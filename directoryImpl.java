package files;

import java.util.ArrayList;
import java.io.File;

import org.omg.PortableServer.*;

public class directoryImpl extends directoryPOA {

	File file = new File("root");
	private int number_of_file;
	private ArrayList<directory_entry> files;
	private POA poa_;
	
	public directoryImpl(POA poa) {
	    poa_ = poa;
		this.file = new File("root");
		init();
	}
	
	public directoryImpl(POA poa, File file) {
	    poa_ = poa;
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
		    r.value = (regular_file) this.impl_to_narrowed(new regular_fileImpl(new File(file, name), m));
		} catch(Exception e) {
		    throw new io(e.getMessage());
		}
	}

	public void open_directory(files.directoryHolder r, java.lang.String name)
			throws files.no_such_file, files.invalid_type_file {
			
	    File f = new File(file, name);
		if(!f.exists()) throw new files.no_such_file();
		if(!f.isFile()) throw new files.invalid_type_file();
		try {
		    r.value = (directory) impl_to_narrowed(new directoryImpl(poa_, new File(file, name)));
		} catch(Exception e) {
            System.out.println("An error occured to open directory "+name);
        }
	}

	public void create_regular_file(files.regular_fileHolder r, java.lang.String name) throws files.already_exist, files.io {
		File f = new File(file, name);
		if(f.exists()) 
		    throw new files.already_exist(); 
		try {
		    f.createNewFile();
		    files.add(new directory_entry(name, file_type.regular_file_type));
		    number_of_file++;
		    r.value = (regular_file) impl_to_narrowed(new regular_fileImpl(new File(file, name), mode.write_trunc));
		    
		    regular_fileImpl fileImpl = new regular_fileImpl(new File(file, name), mode.write_trunc);
		    org.omg.CORBA.Object o = poa_.servant_to_reference(fileImpl);
		    r.value = regular_fileHelper.narrow(o);
		} catch(Exception e) {
		    System.out.println("An error occured during the creation of file "+name);
		}
	}

	public void create_directory(files.directoryHolder r, java.lang.String name) throws files.already_exist {
		File f = new File(file, name);
		if(f.exists()) throw new files.already_exist(name+" already exists.");
		f.mkdir(); // Create the directory
		
		directory_entry de = new directory_entry(name, file_type.directory_type);
		files.add(de); // Add entry
		number_of_file++;
		
		try {
		    directoryImpl directoryimpl = new directoryImpl(poa_, new File(file, name));
		    org.omg.CORBA.Object o = poa_.servant_to_reference(directoryimpl);
		    r.value = directoryHelper.narrow(o);
		} catch(Exception e) {
            System.out.println("An error occured during the creation of directory "+name);
        }
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
	
	public Object impl_to_narrowed(Servant impl) 
	        throws org.omg.PortableServer.POAPackage.ServantNotActive, org.omg.PortableServer.POAPackage.WrongPolicy {
        org.omg.CORBA.Object o = poa_.servant_to_reference(impl);
        if(impl instanceof directoryImpl) {
            return directoryHelper.narrow(o);
        }
        return regular_fileHelper.narrow(o);
	}

	/**
	 * Get an iterator to list files
	 * @param l a file_listHolder to get the list_file
	 * @return The number of files
	 */
	public int list_files(files.file_listHolder l) {
		l.value = (file_list) new file_listImpl(files);
		return files.size();
	}
}
