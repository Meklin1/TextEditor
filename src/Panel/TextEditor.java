package Panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import Listeners.JComboBoxListener;
import Listeners.JTextAreaKeyListener;
import Listeners.JTextAreaMouseAdapter;
import Managers.ButtonManager;
import Managers.StatusManager;


/**
 * TextEditor class is used to initialize text editor application.
 * @author Meinardas Klinkovas
 *
 */
public class TextEditor {
	
	/**
	 * Initializes text editor application
	 * @throws FileNotFoundException
	 */
	public TextEditor() throws FileNotFoundException
	{
	
		File folder = new File(StatusManager.PROJECT_PATH + "languages");
		//System.out.println(folder.getAbsolutePath());
		String[] fileNames = null;
		File[] files = null;
		if (folder.isDirectory())
		{
			fileNames = folder.list();
			files = folder.listFiles();
		}
		JComboBox<String> languagesComboBox = null;
		try {
			languagesComboBox = new JComboBox<String>(fileNames);
		}catch(NullPointerException e) {
			JOptionPane.showMessageDialog(null, "No languages found.");
		}
		
		languagesComboBox.addActionListener (new JComboBoxListener (files, languagesComboBox)); 
		languagesComboBox.setBounds(20, 500, 50, 40);
		languagesComboBox.setFont(new Font("Arial", Font.BOLD, 15));
		TextPanelStatus panelStatus = new TextPanelStatus();;
		final JFrame frame = new JFrame();
		final JTextArea textArea = new JTextArea();;
		StatusManager statusManager;
		JScrollPane panel;
		JLabel label = new JLabel();
		label.setBounds(920, 500, 40, 40);
		frame.add(label);
		textArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		textArea.setFont(textArea.getFont().deriveFont(16f));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(true);
		//SuggectionPanelController panelController = new SuggectionPanelController(textArea, panelStatus);
		statusManager = StatusManager.getInstance(textArea, panelStatus, label);	
		textArea.addKeyListener(new JTextAreaKeyListener(textArea, statusManager));
		textArea.addMouseListener(new JTextAreaMouseAdapter(textArea, statusManager));
		new ButtonManager(frame, textArea, statusManager);
		panel = new JScrollPane(textArea);
		panel.setBounds(20, 10, 945, 465);
		frame.setLayout(null);
		frame.setBounds(0, 0, 1000, 600);
		frame.add(panel);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Word Assist");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Image logo = Toolkit.getDefaultToolkit().getImage(StatusManager.PROJECT_PATH + "images\\logo.png");    
		frame.setIconImage(logo);
		frame.add(languagesComboBox);
		//words.formDictionary("./src/languages/EN/input.txt");
	}
	



}