package files.ui.directory;

import javax.swing.Icon;
import javax.swing.ImageIcon;

class DirectoryEntryIconFactory {
	
	private static Icon fileIcon;
	private static Icon directoryIcon;
	private static Icon parentIcon;
	
	static {
		fileIcon = new ImageIcon("resources/file-icon.png");
		directoryIcon = new ImageIcon("resources/directory-icon.png");
		parentIcon = new ImageIcon("resources/parent-icon.png");
	}
	
	public static Icon getDirectoryIcon() {
		return directoryIcon;
	}

	public static Icon getFileIcon() {
		return fileIcon;
	}

	public static Icon getParentIcon() {
		return parentIcon;
	}
}
