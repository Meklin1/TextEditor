package Application;

import java.io.FileNotFoundException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import Panel.TextEditor;

public class Application {
	public static void main(String[] args) {
		
	    try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }
	    catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
	        e.printStackTrace();
	    } 
	    SwingUtilities.invokeLater(new Runnable() {

	        @Override
	        public void run() {
	            try {
					new TextEditor();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    });
	}
}
