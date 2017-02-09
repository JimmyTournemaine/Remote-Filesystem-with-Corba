/**
 * Created on February 6th, 2017 for a project proposed by Mr Frank Singhoff 
 * as part of the teaching unit system objects distributed 
 * at the University of Western Brittany.
 */
package files.ui.directory.controller;

import files.directory_entry;
import files.file_type;
import files.ui.directory.FilesListModel;
import files.ui.directory.FilesListView;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * The controller for the file-list on mouse events
 */
public class FilesListController extends AbstractController implements MouseListener {

	public FilesListController(FilesListModel model, FilesListView list) {
		super(model, list);
		this.view.setModel(model);
		this.view.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

		directory_entry entry = (directory_entry) view.getSelectedValue();
		if (entry == null)
			return;

		/* Double click */
		if (arg0.getClickCount() == 2) {
			if (entry.type.value() == file_type._directory_type) {
				openDirectory();
			} else {
				openFile();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
