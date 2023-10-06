package Dictionary;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Class used to create object with data structure storing added words.
 * @author Meinardas Klinkovas
 *
 */
public class Dictionary {

	private char c;
	private LinkedList<Dictionary> children = new LinkedList<Dictionary> ();
	private boolean isEndOfWord;
	private boolean isFilled;

	/**
	 * Constructs and initializes Dictionary object.
	 */
	public Dictionary(){
		this.isEndOfWord = false;
		isFilled = false;

	}


	private Dictionary(char firstChar) {
		this.c = firstChar;
	}

	/**
	 * Method used to add word to dictionary.
	 * @param word
	 */
	public void add (String word)
	{

		if (word == null || word.isEmpty())
			return;

		if(find(word)) {
			return;
		}

		String wordLowerCase = word.toLowerCase();
		if(find(wordLowerCase)) {
			return;
		}

		Dictionary temp = this;
		Dictionary current = this;

		for (char symbol : wordLowerCase.toCharArray())
		{
			temp = current.findChild(symbol);
			if(temp == null) {
				current.children.add(new Dictionary(symbol));
				current = current.children.getLast();
			}
			else {
				current = temp;
			}
		}
		current.isEndOfWord = true;
	}

	private void bubbleSort(List<String> list, String inputWord)
	{
		int n = list.size();
		for (int i = 0; i < n-1; i++)
			for (int j = 0; j < n-i-1; j++)
				if (editDistance(inputWord,list.get(j)) > editDistance(inputWord,list.get(j + 1)))
				{
					String temp = list.get(j);
					list.set(j, list.get(j + 1));
					list.set(j + 1, temp);
				}
	}
	/**
	 * Methods used to clear dictionary.
	 */
	public void clear()
	{
		children.clear();
		isEndOfWord = false;
		isFilled = false;
		c = 0;

	}

	/**
	 * Method used to return String array with up to 3 suggested corrected words.
	 * @param inputWord
	 * @return String array with maximum size of 3.
	 */
	public String[] suggestCorected (String inputWord)
	{

		if (inputWord.length() < 3 || inputWord==null || this.find(inputWord.toLowerCase())) {
			return new String[0];
		}
		boolean firstLetterUpperCase = Character.isUpperCase(inputWord.charAt(0));
		inputWord = inputWord.toLowerCase();
		List<String> list = new ArrayList<>();
		StringBuffer curr = new StringBuffer(inputWord);
		LinkedList<Dictionary> nodeList = new LinkedList<Dictionary>();
		nodeList.add(this);

		Dictionary lastNode = this;
		for (char c : inputWord.toCharArray()) {
			lastNode = lastNode.findChild(c);  		
			if (lastNode != null) {
				nodeList.add(lastNode);
			}
			else{
				break;
			}
			curr.append(c);
		}
		System.out.println(inputWord);
		int MaxLenght = (inputWord.length() > 7) ? inputWord.length() / 2 : 3;
		//ciklas nuturi tikslo, nes zodis taisomas nuo prefixo, pvz (work)lalala, pataisytame zodyje visada bus prefixas, jei toks yra zodyne.
		while(curr.length() > 0 && (list.isEmpty() || MaxLenght > 1)) {
			StringBuffer temp =  curr;
			
			suggestCorrectionHelper(nodeList.getLast(), list, temp, MaxLenght, inputWord);
			nodeList.removeLast();
			curr.setLength(temp.length() - 1);
		}

		if(list.isEmpty()) {
			return new String[0];
		}

		if(list.size() == 4) {
			list.remove(3);
		}

		if(firstLetterUpperCase) {
			for(int i = 0; i < list.size(); ++i) {
				String modified = list.get(i).substring(0, 1).toUpperCase() + list.get(i).substring(1);
				list.set(i, modified);
			}

		}
		return list.toArray(new String[0]);
	}

	// Levenshtein distance 
	private int editDistance(String word1, String word2) {
		int n = word1.length();
		int m = word2.length();
		int dp[][] = new int[n+1][m+1];
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				if (i == 0)
					dp[i][j] = j;      
				else if (j == 0)
					dp[i][j] = i; 
				else if (word1.charAt(i-1) == word2.charAt(j-1))
					dp[i][j] = dp[i-1][j-1];                
				else if (i>1 && j>1  && word1.charAt(i-1) == word2.charAt(j-2) 
						&& word1.charAt(i-2) == word2.charAt(j-1))
					dp[i][j] = 1 + Math.min(Math.min(dp[i-2][j-2], dp[i-1][j]), Math.min(dp[i][j-1], dp[i-1][j-1]));
				else
					dp[i][j] = 1 + Math.min(dp[i][j-1], Math.min(dp[i-1][j], dp[i-1][j-1])); 
			}
		} 
		return dp[n][m];
	}
	/**
	 * Method used to find if word exists in dictionary.
	 * @param word
	 * @return true if word was found in dictionary, false otherwise.
	 */
	public boolean find(String word) {

		Dictionary current = this;
		for (char c : word.toCharArray())
		{

			current = current.findChild(c);
			if(current == null) {
				return false;
			}
		}
		return current.isEndOfWord;
	}     

	private Dictionary findChild(char symbol) {
		for(Dictionary child : children) {
			if(child.c == symbol) {
				return child;
			}
		}
		return null;
	} 
	/**
	 * Method used to form a dictionary from text file.
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public void  formDictionary (String fileName) throws FileNotFoundException
	{
		if(isFilled) {
			return;
		}
		File file = null;
		file = OpenInputFile(fileName);
		Scanner sc = null;
		try {
			sc = new Scanner(file, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (sc.hasNextLine()) {
			String temp = sc.nextLine();
			this.add(temp);
		}
		sc.close();
		isFilled = true;

	}

	private boolean isChildrenEmpty() {
		return children.isEmpty();
	}


	private File OpenInputFile(String fileName) {
		File file = new File(fileName);
		if(!file.exists()) {
			System.out.println("File does not exist\n");
		}
		return file;

	}

	/**
	 * Method used to return String array with up to 3 suggested words.
	 * @param inputWord
	 * @return String array with maximum size of 3.
	 */
	public String[] suggestSimilar(String prefix) {
		if (prefix.length()==0 || prefix == null) {
			return new String[0];
		}
		boolean firstLetterUpperCase = Character.isUpperCase(prefix.charAt(0));
		prefix = prefix.toLowerCase();
		List<String> list = new ArrayList<>();
		Dictionary lastNode = this;
		StringBuffer curr = new StringBuffer();
		for (char c : prefix.toCharArray()){
			lastNode = lastNode.findChild(c);
			if (lastNode == null)
				return new String[0];

			curr.append(c);
		}
		suggesSimilartHelper(lastNode, list, curr, Integer.MAX_VALUE);

		if(list.isEmpty()) {
			return new String[0];
		}

		if(list.size() == 4) {
			list.remove(3);
		}

		if(firstLetterUpperCase) {
			for(int i = 0; i < list.size(); ++i) {
				String modified = list.get(i).substring(0, 1).toUpperCase() + list.get(i).substring(1);
				list.set(i, modified);
			}

		}
		return list.toArray(new String[0]);
	}

	private void  suggestCorrectionHelper (Dictionary root, List<String> list, StringBuffer curr, int MaxLenght, String inputWord)
	{
		if(editDistance(inputWord, curr.toString()) > MaxLenght) {
			return;
		}
		if (root.isChildrenEmpty() && editDistance(inputWord, curr.toString()) > MaxLenght)
			return;

		if(MaxLenght == 1 && list.size() == 3) {
			return;
		}

		if (root.isEndOfWord) {

			if((editDistance(inputWord, curr.toString()) < MaxLenght) && list.size() < 4) {

				if(!list.contains(curr.toString())) {
					list.add(curr.toString());
				}		
			}
			else if(list.size() == 4 && !list.contains(curr.toString())) {
				list.set(3, curr.toString());
				MaxLenght = editDistance(inputWord, list.get(2));
			}
			bubbleSort(list, inputWord);
		}

		for (Dictionary child : root.children) {
			if(child != null) {
				suggestCorrectionHelper(child, list, curr.append(child.c), MaxLenght, inputWord);
				curr.setLength(curr.length() - 1);
			}  	
		}
	}

	private void suggesSimilartHelper(Dictionary root, List<String> list, StringBuffer curr, int MaxLenght) {

		if(curr.length() >= MaxLenght && list.size() > 3) {
			return;
		}

		if (root.isChildrenEmpty() && curr.length() >= MaxLenght) {
			return;
		}

		if (root.isEndOfWord) {
			if(list.size() < 4) {
				list.add(curr.toString());
			}
			else {
				list.set(3, curr.toString());
				list.sort(Comparator.comparingInt(String::length));
				MaxLenght = list.get(2).length();
			}                      
		}
		for (Dictionary child : root.children) {

			suggesSimilartHelper(child, list, curr.append(child.c), MaxLenght);
			curr.setLength(curr.length() - 1);          
		}
	}

}
