/**
 * Created on January 28th, 2017 for a project proposed by Mr Frank Singhoff as part of the teaching
 * unit system objects distributed at the University of Western Brittany.
 */
package files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

/**
 * An implementation of the {@link files.directory } interface defined by
 * <a href="../../files.idl">files.idl</a>
 *
 * @author Pierre Siguret
 * @author Jimmy Tournemaine
 */
public class directoryImpl extends directoryPOA {

    File file = new File("root");
    private int number_of_file;
    private POA poa_;

    /**
     * Construct the root directory
     *
     * @param poa the object adapter to activate objects
     */
    public directoryImpl(POA poa) {
        poa_ = poa;
        file = getRoot();
        init();
    }

    /**
     * Construct the directory for the given folder
     *
     * @param poa the object adapter to activate objects
     * @param file the representation of the directory pathname
     * @throws IOException
     */
    public directoryImpl(POA poa, File file) throws IOException {
        poa_ = poa;
        this.file = file.getCanonicalFile();
        init();
    }

    /**
     * Initialize the number of files and directories
     */
    private void init() {
        number_of_file = file.list().length;
    }

    /**
     * Return the number of files and directories
     *
     * @return The number of files and directories
     */
    @Override
    public int number_of_file() {
        return number_of_file;
    }

    /**
     * Open a regular file
     *
     * @param r An holder to store the file opened.
     * @param name The filename of the file to open.
     * @param m The opening mode, should be a {@link files.mode} constant.
     * @throws no_such_files No such file named by {@code name} exists in the directory.
     * @throws invalid_file_type Throws if {@code name} is the name of a directory.
     * @throws access_denied Denied access for files above the {@code root} folder.
     * @throws io Throws if an IOException occurred during the file opening.
     */
    @Override
    public void open_regular_file(files.regular_fileHolder r, java.lang.String name, files.mode m)
            throws files.no_such_file, files.invalid_type_file, access_denied, io {

        File f = new File(file, name);
        if (!accessGranted(f)) throw new files.access_denied();
        if (!f.exists()) throw new files.no_such_file();
        if (!f.isFile()) throw new files.invalid_type_file();

        try {
            r.value = (regular_file) impl_to_narrowed(
                    new regular_fileImpl(new File(file, name), m));
        } catch (Exception e) {
            throw new files.io();
        }
    }

    /**
     * Open a directory
     *
     * @param r An holder to store the directory opened.
     * @param name The filename of the directory to open.
     * @throws no_such_files No such file named by {@code name} exists in the directory.
     * @throws invalid_file_type Throws if {@code name} is the name of a regular file.
     * @throws access_denied Denied access for directories above the {@code root} folder.
     * @throws io Throws if an IOException occurred during the file opening.
     */
    @Override
    public void open_directory(files.directoryHolder r, java.lang.String name)
            throws files.no_such_file, files.invalid_type_file, files.access_denied, io {

        File f = new File(file, name);
        if (!accessGranted(f)) throw new files.access_denied();
        if (!f.exists()) throw new files.no_such_file();
        if (!f.isDirectory()) throw new files.invalid_type_file();
        try {
            r.value = (directory) impl_to_narrowed(new directoryImpl(poa_, new File(file, name)));
        } catch (Exception e) {
            throw new files.io();
        }
    }

    /**
     * Create a regular file and open it in read/write mode.
     *
     * @param r An holder to store the file created.
     * @param name The filename of the file to create.
     * @throws already_exist The name is already used.
     * @throws access_denied Denied access for files above the {@code root} folder.
     * @throws io Throws if an IOException occurred during the file opening.
     */
    @Override
    public void create_regular_file(files.regular_fileHolder r, java.lang.String name)
            throws files.already_exist, files.io, access_denied {
        File f = new File(file, name);
        if (!accessGranted(f)) throw new files.access_denied();
        if (f.exists()) throw new files.already_exist();
        try {
            f.createNewFile();
            number_of_file++;
            r.value = (regular_file) impl_to_narrowed(
                    new regular_fileImpl(new File(file, name), mode.read_write));
        } catch (Exception e) {
            throw new files.io();
        }
    }

    /**
     * Create a directory.
     *
     * @param r An holder to store the directory created.
     * @param name The filename of the directory to create.
     * @throws already_exist The name is already used.
     * @throws access_denied Denied access for directory above the {@code root} folder.
     * @throws io Throws if an IOException occurred during the directory opening.
     */
    @Override
    public void create_directory(files.directoryHolder r, java.lang.String name)
            throws files.already_exist, files.io, access_denied {
        File f = new File(file, name);
        if (!accessGranted(f)) throw new files.access_denied();
        if (f.exists()) throw new files.already_exist(name + " already exists.");
        if (f.mkdir() == false) throw new files.io();

        number_of_file++;

        try {
            directoryImpl directoryimpl = new directoryImpl(poa_, new File(file, name));
            org.omg.CORBA.Object o = poa_.servant_to_reference(directoryimpl);
            r.value = directoryHelper.narrow(o);
        } catch (Exception e) {
            throw new files.io();
        }
    }

    /**
     * Delete the {@code name} file or directory. Deleting a directory involved to remove all the
     * file hierarchy in this directory.
     *
     * @param name The name of the file or directory to delete.
     * @throws no_such_files No such file named by {@code name} exists in the directory.
     * @throws access_denied Denied access for files above the {@code root} folder.
     */
    @Override
    public void delete_file(java.lang.String name) throws files.no_such_file, access_denied {
        File f = new File(file, name);
        if (!accessGranted(f)) throw new files.access_denied();
        if (!f.exists()) throw new files.no_such_file(name);
        if (f.isFile()) {
            f.delete();
        }
        if (f.isDirectory()) {
            this.delete_file(f);
        }
        number_of_file--;
    }

    /**
     * Remove a file hierarchy
     *
     * @param f The rooted file.
     */
    private void delete_file(File f) {
        for (File d : f.listFiles()) {
            if (d.isDirectory()) {
                this.delete_file(d);
            } else {
                d.delete();
            }
        }
        f.delete();
    }

    /**
     * Obtain the typed CORBA object reference for {@link files.directory} or
     * {@link files.regular_file}.
     *
     * @param impl The servant object.
     * @return The narrowed object.
     * @throws ServantNotActive
     * @throws WrongPolicy
     */
    private Object impl_to_narrowed(Servant impl) throws ServantNotActive, WrongPolicy {
        org.omg.CORBA.Object o = poa_.servant_to_reference(impl);
        if (impl instanceof directoryImpl) return directoryHelper.narrow(o);
        return regular_fileHelper.narrow(o);
    }

    /**
     * Get an iterator to list files
     *
     * @param l a file_listHolder to get the list_file
     * @return The number of files
     */
    @Override
    public int list_files(files.file_listHolder l) {

        /* Get files */
        ArrayList<directory_entry> files = new ArrayList<directory_entry>();
        for (File f : file.listFiles()) {
            files.add(new directory_entry(f.getName(),
                    f.isDirectory() ? file_type.directory_type : file_type.regular_file_type));
        }

        /* Return the iterator on the list of directory_entry */
        try {
            file_listImpl fl = new file_listImpl(files);
            Object o = poa_.servant_to_reference(fl);
            l.value = file_listHelper.narrow(o);
        } catch (Exception e) {
            System.out.println("An error occured to get the file list in " + file.getName());
        }
        return files.size();
    }

    /**
     * Get the name of the directory
     *
     * @return The name of the directory
     */
    @Override
    public String name() {
        return file.getName();
    }

    /**
     * Get the root folder
     *
     * @return A File representation of the root directory.
     */
    private File getRoot() {
        return new File("root");
    }

    /**
     * Check access of a file by its path even if the file does not exist.
     *
     * @param f The file to check.
     * @return If the access is granted or not.
     */
    private boolean accessGranted(File f) {
        try {
            if (!f.getCanonicalPath().contains(getRoot().getAbsolutePath())) return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
