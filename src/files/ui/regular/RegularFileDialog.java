package files.ui.regular;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import files.regular_file;

import javax.swing.JTextPane;

public class RegularFileDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 88307160972817987L;
	private final JPanel contentPanel = new JPanel();
	private RegularFileManager om;
	private JTextPane textPane;
	private JButton okButton;

	/**
	 * Create the dialog.
	 */
	public RegularFileDialog(regular_file file) {
		super(null, ModalityType.APPLICATION_MODAL);
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
		
		om = new RegularFileManager(file);
		textPane.setText(om.readAll());
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(((JButton) arg0.getSource()) == okButton) {
			om.writeAll(textPane.getText());
		}
		dispose();
	}
}
