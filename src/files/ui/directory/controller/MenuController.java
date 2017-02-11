/**
 * Created on February 6th, 2017 for a project proposed by Mr Frank Singhoff as part of the teaching
 * unit system objects distributed at the University of Western Brittany.
 */
package files.ui.directory.controller;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import files.directory_entry;
import files.file_type;
import files.ui.directory.ClientUI;
import files.ui.directory.FilesListModel;
import files.ui.directory.FilesListView;

/**
 * The controller for the popup menus.
 *
 * @author Jimmy Tournemaine
 */
public class MenuController extends AbstractController implements MouseListener {

    private JPopupMenu defaultMenu;
    private JPopupMenu fileMenu;
    private JPopupMenu directoryMenu;

    public MenuController(FilesListModel model, FilesListView list) {
        super(model, list);
        view.setModel(model);
        view.addMouseListener(this);

        initDefaultMenu();
        initFileMenu();
        initDirectoryMenu();
    }

    /**
     * Add the default menu to any menu.
     *
     * @param menu The menu on which to add the item.
     */
    private void addDefaultMenu(JPopupMenu menu) {
        JMenuItem newDir = new JMenuItem("New directory");
        newDir.setName(ClientUI.NEW_DIRECTORY);
        newDir.addActionListener(this);
        menu.add(newDir);

        JMenuItem newFile = new JMenuItem("New file");
        newFile.setName(ClientUI.NEW_FILE);
        newFile.addActionListener(this);
        menu.add(newFile);
    }

    /**
     * Add a delete item to a menu.
     *
     * @param menu The menu on which to add the item.
     */
    private void addDeleteMenu(JPopupMenu menu) {
        JMenuItem newDir = new JMenuItem("Delete");
        newDir.setName(ClientUI.DELETE);
        newDir.addActionListener(this);
        menu.add(newDir);
    }

    /**
     * Initialize the default menu (no selection)
     */
    private void initDefaultMenu() {
        defaultMenu = new JPopupMenu();
        addDefaultMenu(defaultMenu);
    }

    /**
     * Initialize the file menu
     */
    private void initFileMenu() {
        fileMenu = new JPopupMenu();
        addDefaultMenu(fileMenu);

        fileMenu.addSeparator();

        JMenuItem open = new JMenuItem("Open");
        open.setName(ClientUI.OPEN_FILE);
        open.addActionListener(this);
        fileMenu.add(open);

        fileMenu.addSeparator();

        addDeleteMenu(fileMenu);
    }

    /**
     * Initialize the directory menu
     */
    private void initDirectoryMenu() {
        directoryMenu = new JPopupMenu();
        addDefaultMenu(directoryMenu);
        directoryMenu.addSeparator();

        JMenuItem open = new JMenuItem("Go into");
        open.setName(ClientUI.OPEN_DIRECTORY);
        open.addActionListener(this);
        directoryMenu.add(open);

        directoryMenu.addSeparator();

        addDeleteMenu(directoryMenu);
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        showPopupMenu(arg0);
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        showPopupMenu(arg0);
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    /**
     * Show the menu corresponding to the selection
     *
     * @param arg0 The mouse event.
     */
    private void showPopupMenu(MouseEvent arg0) {
        if (arg0.isPopupTrigger()) {
            directory_entry entry = (directory_entry) view.getSelectedValue();
            Point position = arg0.getLocationOnScreen();
            position.translate(-5, -5);
            if (entry == null) {
                defaultMenu.show(view, arg0.getX(), arg0.getY());
            } else if (entry.type.value() == file_type._directory_type) {
                directoryMenu.show(view, arg0.getX(), arg0.getY());
            } else {
                fileMenu.show(view, arg0.getX(), arg0.getY());
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
