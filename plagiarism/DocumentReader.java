package plagiarism;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

/**
 * A class with some example code for reading a directory of documents for the Plagiarism Catcher
 * @author Joel Ross
 */
public class DocumentReader
{
	//static HashMap<Int, Int>[] chainMap;	// chainMap will hold a hash map for every file
	static ArrayList<ArrayList<Int>> mapArray = new ArrayList<>();
	static String[] fileName;				// an array for every file's name	 
	
	/**
	 * Utility method to check runtime used memory
	 * @return long used memory
	 */
	private static long usedMemory() {		
		long mem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024;
		return mem;
	}
	
	/**
	 * An example method for reading in a set of documents, parsing them into n-grams
	 * @param directoryPath the path to the directory (i.e., small_set)
	 * @param n the size of each n-gram (number of words to consider as a bunch)
	 */
	public static void readFiles(String directoryPath, int n)
	{
		Int zero = new Int(0);
		File dir = new File(directoryPath); //a file to represent the directory
		if(!dir.isDirectory())
			throw new IllegalArgumentException("Specify a directory, not a file!");

		String[] files = dir.list(); //get list of files in the directory
		//chainMap = new HashMap[files.length];	
		fileName = new String[files.length];
		
		for(int fi=0; fi<files.length; fi++) //go through the list
		{
			File f = new File(directoryPath+"/"+files[fi]); //make a file object representing that file in the directory
			//chainMap[fi] = new HashMap<>();	// capacity for map should be half the size of the file, set prime num, bucket size
			fileName[fi] = f.getName();
			
			ArrayList<Int> mapInts = new ArrayList<>();
			mapArray.add(mapInts);			// add the array list for each file to the arrayList containing all files
			
			try{		
				//Scanner doesn't work with these text files, so need to use a lower-level class BufferedReader.
				//It works similarly to the Scanner though
				BufferedReader reader = new BufferedReader(new FileReader(f)); 
				String text = ""; //the whole text of the document
				String line = reader.readLine(); //the line we're currently reading
				while(line != null)
				{
					text += line.toLowerCase()+" "; //append the (lowercase) version of the line to the text
					line = reader.readLine();
				}
				String[] words = text.split("\\W+"); //split into an array of words (breaking on any number of non-word letters)
				
				int numWords = words.length;		// total count of words in a document
				
				int phraseNum = numWords - (n - 1);		// number of phrases is equal to the amount of words - (phrase length - 1)
		
				StringBuilder phrase;				// StringBuilder object represents an n-word phrase. Should be
													// quicker, and use less memory than a String object to append words
				
				Int phraseVal;						// integer representation a phrase				
				for(int i=0; i < phraseNum; i++)	// for each phrase
				{
					String[] ngram = Arrays.copyOfRange(words, i, i+n); //load a chunk of n words into a local array	
					phrase = new StringBuilder();
					
					for (int j = 0; j < n; ++j) {	// for each word in a phrase
						phrase.append(ngram[j]);	// add it to phrase
					}					
					
					phraseVal = new Int(phrase.toString().hashCode());		// convert phrase to int
				
					//chainMap[fi].put(phraseVal, zero);							// add phrase to map as a key
					mapArray.get(fi).add(phraseVal);	// add each phrase to the corresponding array
				}
			
				reader.close();
			}
			catch(IOException ioe) //in case of any problems
			{
				System.out.println("Error reading file: "+files[fi]);
			}
		}
	}
	
	/*
	 * INSTRUCTIONS:
	 * The director must be placed into the workplace along-side the src folder.
	 * Director should be input as simply the file name (ex. small_set)
	 * Main uses the Scanner to fill the important information -
	 * namely the director, length of phrases, and match threshold.
	 */
	public static void main(String[] args)
	{
		
		MatchList outputList; // list holding all results
		String dir; // directory
		int n, mth; // n = phrase length, mth = minimum number of matches
		Scanner sc = new Scanner(System.in);
		System.out.print("Directory: ");
		dir = sc.nextLine();
		System.out.print("Phrase size: ");
		n = sc.nextInt();
		System.out.print("Min match: ");
		mth = sc.nextInt();
		sc.close();

		outputList = new MatchList();			
		long startTime = System.currentTimeMillis();
		readFiles(dir, n);
		
		//int numFiles = chainMap.length;			// the number of maps should represent the number of files	
		
		int numFiles = mapArray.size();				// number of files should equal number of secondary arrayLists
		int mapSize;
		for (int i = 0; i < numFiles; ++i) {		// for each file
			
			ArrayList<Int> mapEntry = mapArray.get(i);	// get the arrayList for the file to be checked
			mapSize = mapEntry.size();					// get the number of entries in the arrayList
			HashMap<Int, Int> chainMap = new HashMap<>(mapSize);	// create a hashMap for each file
			for (int j = 0; j < mapSize; ++j){		// for each entry in the file's arrayList
				Int value = mapEntry.get(j);		
				chainMap.put(value, value);			// use the entry as the map's key and value
			}
			for (int k = i + 1; k < numFiles; ++k) {	// for every other file to check against the initial file
				int counted = 0;
				ArrayList<Int> otherMap = mapArray.get(k);	// get the ArrayList of Ints for the file to check
				int otherMapSize = otherMap.size();			
				for (int z = 0; z < otherMapSize; ++z) {	// for each of the Ints in the file-to-check's ArrayList
					if (chainMap.containsKey(otherMap.get(z))){		// check if the initial file's map has an equivalent Int
						++counted;							// if it does, increase the number of similar phrases
					}
				}
				if (counted > mth) // if valid count number, add to list
					outputList.addEntry(counted, fileName[i], fileName[k]);
			}
		}
		/* THIS is the old code. Keeping here for reference.
		Set<Int>[] mapKeys = new HashSet[numFiles];		
		for (int i = 0; i < numFiles; ++i) {			// for each file
			mapKeys[i] = new HashSet<Int>(chainMap[i].keySet());	// create a keySet for each map
		}
		int count;
		for (int j = 0; j < numFiles; ++j) {			// for each file
			for (int k = j + 1; k < numFiles; ++k) {	// for every file starting after the file to be checked
				count = 0;
				//if (mapKeys[k].size() > mth) {
				for (Int compareKey : mapKeys[k]) {		// for each key in the key set
					if (chainMap[j].containsKey(compareKey)){	// check if the file being tested contains the key
						++count;								// if it does, increase the count
					}
				}
				if (count > mth) // if valid count number, add to list
					outputList.addEntry(count, fileName[j], fileName[k]);
				//}
			}
	
		}
		*/
		outputList.printList();// output the entire list, in order
		long endTime = System.currentTimeMillis();		
		double totalTime = (double) (endTime - startTime)/ 1000.0;			
		System.out.println("Runtime(s): " + totalTime);
	}
}
