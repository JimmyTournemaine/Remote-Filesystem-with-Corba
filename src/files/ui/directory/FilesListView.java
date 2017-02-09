/**
 * Created on February 6th, 2017 for a project proposed by Mr Frank Singhoff 
 * as part of the teaching unit system objects distributed 
 * at the University of Western Brittany.
 */
package files.ui.directory;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import files.directory_entry;
import files.file_type;

/**
 * A component that displays a file list
 * 
 * @see javax.swing.JList
 */
public class FilesListView extends JList implements ListCellRenderer {

	private static final long serialVersionUID = -377440651436868454L;

	public FilesListView() {
        this.setCellRenderer(this);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected, boolean expanded) {
		
		directory_entry file = (directory_entry) value;
		JLabel label = new JLabel();
		label.setOpaque(true);
		
		if(index == 0) { // The first if the parent link
			label.setIcon(DirectoryEntryIconFactory.getParentIcon());
		} else if(file.type.value() == file_type._directory_type) {
			label.setIcon(DirectoryEntryIconFactory.getDirectoryIcon());
		} else {
			label.setIcon(DirectoryEntryIconFactory.getFileIcon());
		}

        label.setText(file.name);
        label.setToolTipText(file.name);

        if (selected) {
            label.setForeground(Color.WHITE);
            label.setBackground(getSelectionBackground());
        } else {
            label.setForeground(Color.BLACK);
            label.setBackground(this.getBackground());
        }

        return label;
	}
}
