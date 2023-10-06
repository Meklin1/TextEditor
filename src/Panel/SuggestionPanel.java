package Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import Listeners.JListMouseAdapter;
import Managers.StatusManager;
/**
 * SuggestionPanel is essential class for creating suggested word window in text editor  
 * @author Meinardas Klinkovas
 *
 */
public class SuggestionPanel {

	private static Boolean suggestionsFound = false;
	private Color colorOfList = Color.decode("#CAF3FF");  // Light-Yellow (default)
	private Color colorOfListText = Color.decode("#000000");  // Black (default)
	private  int insertionPosition;
	private JList<String> list;
	private String listFontName = Font.SANS_SERIF;
	private int listFontSize = 16;
	private int listFontStyle = Font.PLAIN;
	private TextPanelStatus panelStatus;
	final private JPopupMenu popupMenu;
	private StatusManager statusManager;
	private  String subWord;
	private JTextArea textArea;
	
	/**
	 * Constructs suggestion panel with suggested words.
	 * @param textarea is needed to ensure correct pop-up location.
	 * @param statusManager is used in action listeners to change text editor status.
	 * @param position indicates where new text should be inserted when suggested word is selected.
	 * @param subWord is needed to create suggested word list.
	 * @param location indicates where pop-up menu should show up
	 */
	public SuggestionPanel(JTextArea textarea, StatusManager statusManager, int position, String subWord, Point location) {
		this.statusManager = statusManager;
		this.panelStatus = statusManager.getStatus();
		suggestionsFound = false;
		this.textArea = textarea;
		this.subWord = subWord;
		if(location == null) {

			location = new Point();
			location.x = textarea.getCaretPosition();
			location.y = 0;

		}
		this.insertionPosition = position;
		this.subWord = subWord;    
		popupMenu = new JPopupMenu();      
		popupMenu.removeAll();
		popupMenu.setOpaque(false);
		popupMenu.setBorder(null);
		popupMenu.add(list = createSuggestionList(subWord), BorderLayout.CENTER);
		if(textarea.getWidth() < location.x + popupMenu.getPreferredSize().width) {
			location.x = textarea.getWidth()- popupMenu.getPreferredSize().width;
		}
		popupMenu.show(textarea, location.x, textarea.getBaseline(0, 0) + 4 + location.y);
	}
	
	/**
	 * Method is used to know if there any suggestions.
	 * @return true if suggestions were found, false otherwise.
	 */
	public static Boolean getSuggestionsFound() {
		return suggestionsFound;
	}

	private JList<String> createSuggestionList(final String subWord) {
		String[] data = StatusManager.getWords().suggestSimilar(subWord);
		suggestionsFound = (data.length > 0) ? true : false;

		if (!getSuggestionsFound()) {
			String[] correctList = StatusManager.getWords().suggestCorected(subWord);
			if(correctList.length == 0) {
				data = new String[1];      

			}
			else {
				data = new String[correctList.length + 2];
				data[1]= "Or did you mean:";
			}
			data[0] = "Unknown Word. Add To Dictionary ?";         
			int index = 0;
			for(int i = 2; i < data.length; ++i) {
				data[i] = correctList[index++];
			}
		}
		list = new JList<>(data);
		list.setFont(new Font(listFontName, listFontStyle, listFontSize));
		list.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBackground(colorOfList);
		list.setForeground(colorOfListText);
		list.addMouseListener(new JListMouseAdapter(this, getSuggestionsFound()));
		return list;
	}

	/**
	 * Method to get suggestion list in pop-up 
	 * @return list with suggested words.
	 */
	public JList<String> getList() {
		return list;
	}
	/**
	 * Method to get panel status.
	 * @return object storing panel status.
	 */
	public TextPanelStatus getPanelStatus() {
		return panelStatus;
	}
	/**
	 * Method to get pop-up menu.
	 * @return pop-up menu object.
	 */
	public JPopupMenu getPopupMenu() {
		return popupMenu;
	}
	/**
	 * Method to get StatusManager object.
	 * @return StatusManager object.
	 */
	public StatusManager getStatusManager() {
		return statusManager;
	}
	
	/**
	 * Method to get word or prefix of the word which is used in suggestion panel.
	 * @return subword string.
	 */
	public String getSubWord() {
		return subWord;
	}
	/**
	 * Method to get JTextArea object.
	 * @return JTextArea object.
	 */
	public JTextArea getTextArea() {
		return textArea;
	}
	/**
	 * Method to insert selected word to text area from suggestion panel.
	 * @return true if insertion was successful, false otherwise.
	 */
	public boolean insertSelection() {
		if (list.getSelectedValue() != null) {
			final String selectedSuggestion = list.getSelectedValue();
			getTextArea().replaceRange(selectedSuggestion, insertionPosition - getSubWord().length(), insertionPosition);
			popupMenu.setVisible(false);
			return false;
		}
		return true;
	}
	/**
	 * Method to select suggestion below current selection.
	 */
	public void moveDown() {
		int index = Math.min(list.getSelectedIndex() + 1, list.getModel().getSize() - 1);
		selectIndex(index);
	}
	/**
	 * Method to select suggestion above current selection.
	 */
	public void moveUp() {
		int index = Math.max(list.getSelectedIndex() - 1, 0);
		selectIndex(index);
	}

	private void selectIndex(int index) {
		final int position = getTextArea().getCaretPosition();
		list.setSelectedIndex(index);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				getTextArea().setCaretPosition(position);
			}
		});
	}



}
