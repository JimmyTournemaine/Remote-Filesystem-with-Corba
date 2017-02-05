/*
 * Created on 25 jan. 2017 under the authority of Franck Singhoff 
 * as part of practical work at the University of Western Brittany
 */
package files;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

import org.omg.PortableServer.*;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

public class directoryImpl extends directoryPOA {

	File file = new File("root");
	private int number_of_file;
	private POA poa_;
	
	public directoryImpl(POA poa) {
	    poa_ = poa;
		file = getRoot();
		init();
	}
	
	public directoryImpl(POA poa, File file) throws IOException {
	    poa_ = poa;
		this.file = file.getCanonicalFile(); // Remove ".." from path
		init();
	}
	
	private void init() {
		number_of_file = this.file.list().length;
	}

	@Override
	public int number_of_file() {
		return number_of_file;
	}

	@Override
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

	@Override
	public void open_directory(files.directoryHolder r, java.lang.String name)
			throws files.no_such_file, files.invalid_type_file, files.access_denied {

		File f = new File(file, name);
	    if(isRootParent(f)) throw new files.access_denied();
		if(!f.exists()) throw new files.no_such_file();
		if(!f.isDirectory()) throw new files.invalid_type_file();
		try {
		    r.value = (directory) impl_to_narrowed(new directoryImpl(poa_, new File(file, name)));
		} catch(Exception e) {
            System.out.println("An error occured to open directory "+name);
        }
	}

	@Override
	public void create_regular_file(files.regular_fileHolder r, java.lang.String name) throws files.already_exist, files.io {
		File f = new File(file, name);
		if(f.exists()) throw new files.already_exist(); 
		try {
		    f.createNewFile();
		    number_of_file++;
		    r.value = (regular_file) impl_to_narrowed(new regular_fileImpl(new File(file, name), mode.read_write));
		} catch(Exception e) {
		    throw new files.io(e.getMessage());
		}
	}

	@Override
	public void create_directory(files.directoryHolder r, java.lang.String name) throws files.already_exist, files.io {
		File f = new File(file, name);
		if(f.exists()) throw new files.already_exist(name+" already exists.");
		if(f.mkdir() == false) throw new files.io();
		
		number_of_file++;
		
		try {
		    directoryImpl directoryimpl = new directoryImpl(poa_, new File(file, name));
		    org.omg.CORBA.Object o = poa_.servant_to_reference(directoryimpl);
		    r.value = directoryHelper.narrow(o);
		} catch(Exception e) {
            System.out.println("An error occured during the creation of directory "+name);
        }
	}

	@Override
	public void delete_file(java.lang.String name) throws files.no_such_file {
		File f = new File(file, name);
		if(!f.exists()) throw new files.no_such_file(name);
		if(f.isFile()) f.delete();
		if(f.isDirectory()) this.delete_file(f);
		number_of_file--;
	}
	
	private void delete_file(File f)
	{
		for(File d : f.listFiles()) {
			if(d.isDirectory())
				this.delete_file(d);
			else
				d.delete();
		}
		f.delete();
	}
	
	private Object impl_to_narrowed(Servant impl) throws ServantNotActive, WrongPolicy {
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
	@Override
	public int list_files(files.file_listHolder l) {
		
		/* Get files */
		ArrayList<directory_entry> files = new ArrayList<directory_entry>();
		for(File f : file.listFiles()){
			files.add(new directory_entry(
			    f.getName(), 
			    f.isDirectory() ? file_type.directory_type : file_type.regular_file_type
			));
		}
		
		/* Return the iterator on the list of directory_entry */
		try {
			file_listImpl fl = new file_listImpl(files);
			Object o = poa_.servant_to_reference(fl);
			l.value = file_listHelper.narrow(o);
		} catch(Exception e) {
			System.out.println("An error occured to get the file list in "+file.getName());
		}
		return files.size();
	}

	@Override
	public String name() {
		return file.getName();
	}
	
	private File getRoot() {
		return new File("root");
	}
	
	private boolean isRootParent(File f) {
		try {
			if(!f.getCanonicalPath().contains(getRoot().getName()))
				return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
