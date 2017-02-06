package files.ui.directory.controller;

import files.directory_entry;
import files.file_type;
import files.ui.directory.FilesListModel;
import files.ui.directory.FilesListView;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class FilesListController extends AbstractController implements ActionListener, MouseListener {

	public FilesListController(FilesListModel model, FilesListView list) {
		super(model, list);
		this.view.setModel(model);
		this.view.addMouseListener(this);
	}

	/**
	 * Open file or directory on double click
	 */
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
