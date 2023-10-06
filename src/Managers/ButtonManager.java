package Managers;


import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Class ButtonManager creates buttons and their action listeners used for text editor application
 * @author Meinardas Klinkovas
 *
 */
public class ButtonManager {
	
	/**
	 * Constructs and initializes buttons and their action listeners needed for text editor application.
	 * @param frame reference is needed to add buttons.
	 * @param textArea is used to save current typed text to file.
	 * @param statusManager is used to change application status.
	 */
	public ButtonManager(JFrame frame, JTextArea textArea, StatusManager statusManager)
	{
		JButton saveButton = new JButton("Save");
		JButton loadSaved = new JButton("Load saved progress");
		JButton undoButton = new JButton(new ImageIcon(StatusManager.PROJECT_PATH + "images\\undo.png"));
		JButton redoButton = new JButton(new ImageIcon(StatusManager.PROJECT_PATH + "images\\redo.png"));
		JButton addAll = new JButton("Add all to dictionary");

		saveButton.setBounds(130, 500, 100, 40);
		saveButton.setFont(new Font("Arial", Font.BOLD, 15));
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(textArea.getText().length() == 0) {
           		  return;
           	  	}
				 
				 SwingUtilities.invokeLater(new Runnable() {
                      @Override
                     public void run() {
                    	  JFileChooser fileChooser = new JFileChooser();                   
                          FileNameExtensionFilter binFilter = new FileNameExtensionFilter("bin files (*.bin)", "bin");
                          fileChooser.addChoosableFileFilter(binFilter);
                          fileChooser.setFileFilter(binFilter);
                          int option = fileChooser.showOpenDialog(frame);
                          if(option == JFileChooser.APPROVE_OPTION){
                             File file = fileChooser.getSelectedFile();
                     		//for(String word : addedWords) {
                 			
                             statusManager.save(file.getAbsolutePath());
                          }
                     }
                 });
			 }

			
		});
		
		undoButton.setBounds(510, 500, 60, 40);
		undoButton.setFont(new Font("Arial", Font.BOLD, 15));
		undoButton.addActionListener(new ActionListener() {
			@Override
			 public void actionPerformed(ActionEvent e) {
				 	
					 SwingUtilities.invokeLater(new Runnable() {
	                    @Override
	                    public void run() {
	                    	statusManager.undo();
	                    }
	                }); 
			 }
		});
		
		redoButton.setBounds(580, 500, 60, 40);
		redoButton.setFont(new Font("Arial", Font.BOLD, 15));
		redoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                    	statusManager.redo();
                    	
                    }
                });
				 
			 }
		});
		loadSaved.setBounds(240, 500, 200, 40);
		loadSaved.setFont(new Font("Arial", Font.BOLD, 15));
		loadSaved.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					 SwingUtilities.invokeLater(new Runnable() {
	                    @Override
	                    public void run() {
	                    	JFileChooser fileChooser = new JFileChooser();
	                        FileNameExtensionFilter binFilter = new FileNameExtensionFilter("bin files (*.bin)", "bin");
	                        fileChooser.addChoosableFileFilter(binFilter);
	                        fileChooser.setFileFilter(binFilter);
	                        int option = fileChooser.showOpenDialog(frame);
	                        if(option == JFileChooser.APPROVE_OPTION){
	                             File file = fileChooser.getSelectedFile();
	                             statusManager.load(file.getAbsolutePath());
	                        }	
	                    }
	                });
					 
				 }

				
			});
		addAll.setBounds(710, 500, 200, 40);
		addAll.setFont(new Font("Arial", Font.BOLD, 15));
		addAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(EventQueue.isDispatchThread());
					 SwingUtilities.invokeLater(new Runnable() {
	                    @Override
	                    public void run() {
	                    	statusManager.addAllToDictionary();
	                    	statusManager.highlightIncorrectWords();
	                    }
	                });
					 
				 }

				
			});
		frame.add(saveButton);
		frame.add(undoButton);
		frame.add(redoButton);
		frame.add(loadSaved);
		frame.add(addAll);
	}
}
