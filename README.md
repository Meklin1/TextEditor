# TextEditor

This text editor is an assistive text typing tool that helps you to type text comfortably and without errors.

The program is launched via the created .jar file or by going to the project's src folder via the command line and typing: "javac application/application.java && java application/application.java" via the command line.

The program is able to correct and suggest written words, it is possible to go back steps and go forward again. Also, the user can choose the writing language and save/load the progress made to the selected file.

Main classes: Application, TextEditor, Dictionary, StatusManager, SuggestionPanel, TextPanelStatus, JComboBoxListener, JListMouseAdapter, JTextAreaKeyListener, JTextAreaMouseAdapter.

In the future, more languages can be added to the program, the functionality of the words offered to the user can be improved based on the frequency of use of certain words. It is also possible to increase the number of different buttons by increasing the functionality.

The StatusManager class uses the Singleton creation design pattern so that no other object of the mentioned class is used, which could harm the correct operation of the program.
