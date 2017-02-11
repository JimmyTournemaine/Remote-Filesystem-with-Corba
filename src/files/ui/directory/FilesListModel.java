/**
 * Created on February 6th, 2017 for a project proposed by Mr Frank Singhoff as part of the teaching
 * unit system objects distributed at the University of Western Brittany.
 */
package files.ui.directory;

import javax.swing.DefaultListModel;
import files.directory;
import files.directory_entry;
import files.directory_entryHolder;
import files.file_listHolder;
import files.file_type;

/**
 * The model of the list
 *
 * @author Jimmy Tournemaine
 */
public class FilesListModel extends DefaultListModel {

    private static final long serialVersionUID = 5037457163325723718L;
    private directory current;

    /**
     * Create a file list from the {@code current} directory.
     *
     * @param current The current directory.
     */
    public FilesListModel(directory current) {
        addParent();
        setCurrent(current);
    }

    /**
     * Set the current directory.
     *
     * @param current The new current directory.
     */
    public void setCurrent(directory current) {
        this.current = current;
        refresh();
    }

    /**
     * Get the current directory.
     *
     * @return The current directory.
     */
    public directory getCurrent() {
        return current;
    }

    /**
     * Refresh the file list
     */
    public void refresh() {
        directory_entryHolder eh = new directory_entryHolder(
                new directory_entry("", file_type.regular_file_type));
        file_listHolder lh = new file_listHolder();
        current.list_files(lh);
        clear();
        addParent();
        while (lh.value.next_one(eh)) {
            addElement(eh.value);
        }
    }

    /**
     * Add the "get parent" element in the list
     */
    public void addParent() {
        super.addElement(new directory_entry("..", file_type.directory_type));
    }

    /**
     * Inserts the specified element at the specified position in this list.
     *
     * @param index The index at which the specified element is to be inserted.
     * @param element The element to be inserted.
     */
    public void add(int index, directory_entry element) {
        super.add(index, element);
    }

    /**
     * Inserts the specified element at the end of the list.
     *
     * @param element The element to be inserted.
     */
    public void addElement(directory_entry element) {
        super.addElement(element);
    }

}
