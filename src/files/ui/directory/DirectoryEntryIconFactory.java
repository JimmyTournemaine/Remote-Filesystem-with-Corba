/**
 * Created on February 6th, 2017 for a project proposed by Mr Frank Singhoff as part of the teaching
 * unit system objects distributed at the University of Western Brittany.
 */
package files.ui.directory;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * A factory to get icons depending of the type of the file
 *
 * @author Jimmy Tournemaine
 */
class DirectoryEntryIconFactory {

    private static Icon fileIcon;
    private static Icon directoryIcon;
    private static Icon parentIcon;

    /**
     * Load images just once.
     */
    static {
        fileIcon = new ImageIcon("resources/file-icon.png");
        directoryIcon = new ImageIcon("resources/directory-icon.png");
        parentIcon = new ImageIcon("resources/parent-icon.png");
    }

    /**
     * Get an icon for a directory
     *
     * @return The directory's icon
     */
    public static Icon getDirectoryIcon() {
        return directoryIcon;
    }

    /**
     * Get an icon for a file
     *
     * @return The file's icon
     */
    public static Icon getFileIcon() {
        return fileIcon;
    }

    /**
     * Get an icon for the "parent folder"
     *
     * @return The parent's icon
     */
    public static Icon getParentIcon() {
        return parentIcon;
    }
}
