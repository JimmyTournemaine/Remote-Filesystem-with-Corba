/**
 * Created on February 6th, 2017 for a project proposed by Mr Frank Singhoff as part of the teaching
 * unit system objects distributed at the University of Western Brittany.
 */
package files.ui.regular;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import files.access_denied;
import files.directory;
import files.invalid_type_file;
import files.io;
import files.no_such_file;

/**
 * A dialog to edit a file contents.
 */
public class RegularFileDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 88307160972817987L;
    private final JPanel contentPanel = new JPanel();
    private RegularFileManager om;
    private JTextPane textPane;
    private JButton okButton;

    /**
     * Create the dialog.
     *
     * @throws io If any sort of I/O problem occurred.
     * @throws invalid_type_file If the name does not corresponding to a file.
     * @throws access_denied The file is above the root of the file system.
     * @throws no_such_file Any filename of this name exists.
     */
    public RegularFileDialog(directory current, String name)
            throws no_such_file, access_denied, invalid_type_file, io {
        super(null, ModalityType.APPLICATION_MODAL);
        setTitle(name);
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setLayout(new FlowLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        textPane = new JTextPane();
        textPane.setEditable(true);
        contentPanel.add(textPane);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        okButton = new JButton("OK");
        okButton.addActionListener(this);
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPane.add(cancelButton);

        om = new RegularFileManager(current, name);
        textPane.setText(om.readAll());

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (((JButton) arg0.getSource()) == okButton) {
            try {
                om.writeAll(textPane.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dispose();
    }
}
