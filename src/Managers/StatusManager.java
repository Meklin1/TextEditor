package Managers;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import Dictionary.Dictionary;
import Panel.TextPanelStatus;
/**
 * Class to handle text editor status managing.
 * @author Meinardas Klinkovas
 *
 */
public class StatusManager{
	private static StatusManager object;
	private static Stack<TextPanelStatus> redoStatusCollection = new Stack<TextPanelStatus>();
	private static Stack<TextPanelStatus> undoStatusCollection = new Stack<TextPanelStatus>();
	private static ArrayList<String>  addedWords = new  ArrayList<>();
	private static Dictionary words = new Dictionary();
	public static final String PROJECT_PATH = "C:\\Users\\User\\eclipse-workspace\\Project\\";
	
	/**
	 * Method used get stored text panel statuses in a redo collection.
	 * @return redoStatusCollection
	 */
	public static Stack<TextPanelStatus> getRedoStatusCollection() {
		return redoStatusCollection;
	}
	/**
	 * Method used get stored text panel statuses in a undo collection.
	 * @return undoStatusCollection.
	 */
	public static Stack<TextPanelStatus> getUndoStatusCollection() {
		return undoStatusCollection;
	}
	/**
	 * Method used to get user-added words to Dictionary.
	 * @return list of user-added words.
	 */
	public static ArrayList<String> getAddedWords() {
		return addedWords;
	}
	/**
	 * Method used to get default dictionary of words used in word suggesting.
	 * @return Dictionary.
	 */
	public static Dictionary getWords() {
		return words;
	}
	

	/**
	 * Method only single time constructs status manager, which fills dictionary with English language words.
	 * @param textArea
	 * @param status
	 * @param label
	 * @return StatusManager object.
	 */
	public static StatusManager getInstance (JTextArea textArea, TextPanelStatus status, JLabel label)
	{
		if(object == null) {
			object = new StatusManager(textArea, status, label);
		}
		return object;
	}
	

	private JLabel label;
	private TextPanelStatus status;
	private JTextArea textArea;
	
	private StatusManager(JTextArea textArea, TextPanelStatus status, JLabel label)
	{
		try {
			words.formDictionary(PROJECT_PATH + "languages\\EN\\input.txt");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.label = label;
		this.status = status;
		this.textArea = textArea;
	}
	/**
	 * Method used to all unknown words to dictionary.
	 */
	protected void addAllToDictionary ()
	{

		String text =  textArea.getText();
		StringBuffer temp = new StringBuffer();
		Boolean checkedStartFlag = false;
		Boolean checkedEndFlag = false;
		for(int i = 0; i < text.length(); ++i) {
			if(Character.isAlphabetic(text.charAt(i))) {
				temp.append(text.charAt(i));
				if(!checkedStartFlag) {
				}	
				checkedStartFlag = true;
				checkedEndFlag = false;
			}		  
			if(!Character.isAlphabetic(text.charAt(i))) {
				if(!checkedEndFlag) {
					String word = temp.toString().toLowerCase();
					words.add(word);
					addedWords.add(word);

				}

				checkedEndFlag = true;
				temp.delete(0, temp.length());
				checkedStartFlag = false;

			}
		}
	}
	/**
	 * Method used to get current text panel status.
	 * @return current text panel.
	 */
	public TextPanelStatus getStatus() {
		return status;
	}
	/**
	 * Method to highlight all incorrect words in text area.
	 */
	public void highlightIncorrectWords () {
		Highlighter highlighter = textArea.getHighlighter();
		HighlightPainter incorrectPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
		Boolean errorsFound = false;

		highlighter.removeAllHighlights();
		String text =  textArea.getText();
		StringBuffer temp = new StringBuffer();
		int startPosition = 0;
		int endPosition = 0;
		Boolean checkedStartFlag = false;
		Boolean checkedEndFlag = false;
		for(int i = 0; i < text.length(); ++i) {
			if(Character.isAlphabetic(text.charAt(i))) {
				temp.append(text.charAt(i));
				if(!checkedStartFlag) {
					startPosition = i;
				}	
				checkedStartFlag = true;
				checkedEndFlag = false;
			}		  
			if(!Character.isAlphabetic(text.charAt(i))) {
				if(!checkedEndFlag) {
					endPosition = i;				  
					String word = temp.toString().toLowerCase();
					if(!words.find(word)) {
						errorsFound = true;
						try {
							highlighter.addHighlight(startPosition, endPosition, incorrectPainter);
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
					}
				}

				checkedEndFlag = true;
				startPosition = 0;
				endPosition = 0;
				temp.delete(0, temp.length());
				checkedStartFlag = false;

			}
		}
		if(errorsFound) {
			label.setIcon(new ImageIcon(PROJECT_PATH + "\\images\\incorrect.png"));
		}
		else {
			label.setIcon(new ImageIcon(PROJECT_PATH + "\\images\\correct.png"));
		}
	}
	/**
	 * Method to load saved text panel status.
	 * @param file directory.
	 */
	public void load(String fileDirectory)
	{
		readFromStatusFile(fileDirectory);
		textArea.replaceRange(getStatus().getText(), 0, textArea.getText().length());
		status.changePanelInfo(textArea);
		StatusManager.getUndoStatusCollection().push((TextPanelStatus) status.clone());
	}

	private TextPanelStatus readFromStatusFile(String fileDirectory)
	{
		FileInputStream file = null;
		ObjectInputStream in = null;
		try {
			file = new FileInputStream(fileDirectory);
			in = new ObjectInputStream (file);
			status = (TextPanelStatus)in.readObject();
			System.out.println(status.getText());
			System.out.println(status.getAddedWords());
			file.close();
			in.close();

		} 

		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File Not Found");
			return null;
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There was a failure during reading operation.");
			return null;
		}

		catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Can not read from file.");
			return null;
		}
		for(String word : status.getAddedWords()) {
			words.add(word);
		}

		return status;

	}
	/**
	 * Method to change text panel status with with later saved text panel status.
	 */
	public void redo() {
		if(!redoStatusCollection.isEmpty()) {
			textArea.replaceRange(redoStatusCollection.peek().getText(), 0, textArea.getText().length());
			undoStatusCollection.push(redoStatusCollection.pop());
		}
	}
	/**
	 * Method to save current text panel status to file.
	 * @param file directory.
	 */
	public void save(String fileDirectory) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		status.changePanelInfo(textArea);
		writeToStatusFile(fileDirectory);
	}
	/**
	 * Method to change text panel status with with previously saved text panel status.
	 */
	public void undo() {

		if(undoStatusCollection.size() == 0) {
			textArea.replaceRange("",0,textArea.getText().length());
		}
		else {
			textArea.replaceRange(undoStatusCollection.peek().getText(), 0, textArea.getText().length());
			redoStatusCollection.push(undoStatusCollection.pop());
		}

	}

	private void writeToStatusFile(String fileDirectory)
	{

		FileOutputStream file = null;
		ObjectOutputStream out = null;
		try {
			file = new FileOutputStream(fileDirectory);
			out = new ObjectOutputStream(file);
			out.writeObject(status);
			out.flush();
			file.close();
			out.close();

		} 
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There was a failure during writing operation.");
		}		
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Can write to file.");
		}		

	}

}
