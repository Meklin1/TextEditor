# TextEditor

This text editor is an assistive text typing tool that helps you to type text comfortably and without errors.

The program is launched via the created .jar file or by going to the project's src folder via the command line and typing: "javac application/application.java && java application/application.java" via the command line.

The program is able to correct and suggest written words, it is possible to go back steps and go forward again. Also, the user can choose the writing language and save/load the progress made to the selected file.

Main classes: Application, TextEditor, Dictionary, StatusManager, SuggestionPanel, TextPanelStatus, JComboBoxListener, JListMouseAdapter, JTextAreaKeyListener, JTextAreaMouseAdapter.

In the future, more languages can be added to the program, the functionality of the words offered to the user can be improved based on the frequency of use of certain words. It is also possible to increase the number of different buttons by increasing the functionality.

The StatusManager class uses the Singleton creation design pattern so that no other object of the mentioned class is used, which could harm the correct operation of the program.


## Demonstration ##
![Screenshot 2023-10-06 124340](https://github.com/Meklin1/TextEditor/assets/93739199/97e56645-1031-4423-ae32-00b26b79d051)
![Screenshot 2023-10-06 124454](https://github.com/Meklin1/TextEditor/assets/93739199/a2cacf90-40a3-4367-a83f-1f54f0bdacea)
![Screenshot 2023-10-06 124952](https://github.com/Meklin1/TextEditor/assets/93739199/461e77f2-9ff7-4a64-ba12-0542696e7a12)
![Screenshot 2023-10-06 125056](https://github.com/Meklin1/TextEditor/assets/93739199/001b4ebb-8530-443c-808c-d5c9a62519c9)
![Screenshot 2023-10-06 125310](https://github.com/Meklin1/TextEditor/assets/93739199/28ce6bb8-7357-4d7f-9cf1-f36f849022b9)
![Screenshot 2023-10-06 125624](https://github.com/Meklin1/TextEditor/assets/93739199/0ab79101-5bff-44ad-8f7b-e16bb8984276)
