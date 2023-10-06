package Panel;

import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JTextArea;
import Managers.StatusManager;

/**
 * TextPanelStatus objects are used to store current status of application. This class is useful for undo and redo functions.
 * @author Meinadas Klinkovas
 *
 */
public class TextPanelStatus implements Cloneable, Serializable
{

	private static final long serialVersionUID = 5059043139380670563L;
	private ArrayList<String> addedWords;
	private String text;
	
	
	/**
	 * Constructs object with empty list of added words.
	 */
	public TextPanelStatus()
	{
		addedWords  = new ArrayList<String>();
	}
	/**
	 * Saves and changes current information with new information about typed text and added words to Dictionary 
	 * @param textArea is used to save current typed text
	 */
	public void changePanelInfo(JTextArea textArea)
	{
		this.text = textArea.getText();
		this.addedWords.clear();
		for(String word : StatusManager.getAddedWords()) {
			this.addedWords.add(word);
		}
	}
	
	/**
	 * Method is used to access added words variable
	 */
	public ArrayList<String> getAddedWords() {
		return addedWords;
	}

	/**
	 * Method is used to access text variable
	 */
	public String getText() {
		return text;
	}

	/**
	 * Overridden method is used to deep clone TextPanelStatus object.
	 */
	@Override
	public Object clone()
	{
		TextPanelStatus copy = null;
		try {
			copy = (TextPanelStatus) super.clone();
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		//copy.text = new String(this.text);
		copy.addedWords = new ArrayList<String>();
		for(String word : this.addedWords) {
			copy.addedWords.add(new String(word));
		}
		
		return copy;
	}
	
	
}
