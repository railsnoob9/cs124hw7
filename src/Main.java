import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Main {

	
	public static void main(String[] args)
	{
		ArrayList<String> dictlines = Main.readFile("data/chindict", 130);
		for (int i=0;i<dictlines.size();i++)
		{
			String tempstring = dictlines.get(i);
			dictlines.remove(i);
			dictlines.add(i, tempstring + " ");
		}
		ArrayList<String> spandoc = Main.readFile("data/chinparagraph", 10);
		
		ArrayList<String> englishsents = Main.doInitialTranslation(dictlines, spandoc);
		
		Main.writefile(englishsents, "data/englishsentencedoc");
		ArrayList<ArrayList<TaggedWord>> taggedsentences = Main.tag("data/englishsentencedoc");
		
		System.out.println();
		
		Main.handletaggedsentences(taggedsentences);
	}
	
	public static void handletaggedsentences(ArrayList<ArrayList<TaggedWord>> taggedsentences)
	{

		
		for (ArrayList<TaggedWord> sentence : taggedsentences)
		{
			for (int i=0;i<sentence.size();i++)
			{


				
				
			}

			
		}

		
		//if any rules need to be run after the first set, go here:
		
		for (ArrayList<TaggedWord> sentence : taggedsentences)
		{
			for (int i=0;i<sentence.size();i++)
			{
		

				
				

			}
			
			System.out.println(Sentence.listToString(sentence, false));
		}
		
	}
	
	public static ArrayList<ArrayList<TaggedWord>> tag(String sentencedoc)
	{
	    MaxentTagger tagger;
		try {
			
				
			tagger = new MaxentTagger("data/wsj-0-18-bidirectional-distsim.tagger");
	
		    List<List<HasWord>> sentences = 
		    	MaxentTagger.tokenizeText(new BufferedReader(new FileReader(sentencedoc)));
		   
		    ArrayList<ArrayList<TaggedWord>> taggedsentences = new ArrayList<ArrayList<TaggedWord>>(sentences.size());
		    
		    for (List<HasWord> sentence : sentences) {
		      ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentence);
		      taggedsentences.add(tSentence);
		      System.out.println(Sentence.listToString(tSentence, false));
		    }
		    

		    return taggedsentences;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static void writefile(List<String> aLines, String aFileName) 
	{
		    try 
		    {
		    	BufferedWriter writer = new BufferedWriter(new FileWriter(aFileName));

		      for(String line : aLines){
		        writer.write(line + ".");
		        writer.newLine();
		      }
		      
		    	writer.close();
		    }
		    catch (IOException e)
		    {
		    	
		    }
	}
	
	
	public static ArrayList<String> doInitialTranslation(ArrayList<String> dictlines, ArrayList<String> doc)
	{
		ArrayList<String> englishsentences = new ArrayList<String>(doc.size());
		for (String docline : doc)
		{
			String[] words = docline.split(" ");
			
			String translateddoc = "";
			
			for (String word : words)
			{
				word = word.toLowerCase();
				word = word.replace(".", "");
				
				boolean iscomma=false;
				if (word.contains(","))
				{
					word = word.replace(",", "");
					iscomma=true;
				}
				
				boolean isquotebeginning=false;
				boolean isquoteend=false;
				if (word.contains("\""))
				{
					if (word.charAt(0)==('\"'))
					{
						isquotebeginning=true;
						word.replace("\"", "");
					}
					if (word.charAt(word.length()-1)==('\"'))
					{
						isquoteend=true;
						word.replace("\"", "");
					}					
				}
				
				word = word.replace("(", "");
				word = word.replace(")", "");
				ArrayList<String> candidatedictlines = new ArrayList<String>(10);
				for (int i=0;i<dictlines.size();i++)
				{
					if (dictlines.get(i).contains(" " + word + " "))
					{
						String[] dicthit = dictlines.get(i).split(" ");
						
						String englishword = dicthit[0].replace("|", " ");
						
						if (iscomma==true)
							englishword+=",";
						if (isquotebeginning=false)
							englishword= "\"" + englishword;
						if (isquoteend==true)
							englishword+="\"";
							
						candidatedictlines.add(englishword);
					}
					
					
				}
				if (candidatedictlines.size()!=1)
				{
					System.err.println("crap");
				}
				translateddoc+=candidatedictlines.get(0) + " ";
				
			}
			englishsentences.add(translateddoc);
			System.out.println(translateddoc);
		}
		return englishsentences;
	}
	

	public static ArrayList<String> readFile(String filename,int size)
	{
    	ArrayList<String> fileContents = new ArrayList<String>(size);

    	
    	  try{
    		  // Open the file that is the first 
    		  // command line parameter
    		  FileInputStream fstream = new FileInputStream(filename);
    		  // Get the object of DataInputStream
    		  DataInputStream in = new DataInputStream(fstream);
    		  BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
    		  String strLine;
    		  //Read File Line By Line
    		  while ((strLine = br.readLine()) != null)   {
    		  // Print the content on the console
    		  //System.out.println (strLine);
    			  
    			  //String newStr= strLine.replaceAll("-", ""); //pre-emptively remove gaps
    			  fileContents.add(strLine);
    		  }
    		  //Close the input stream
    		  in.close();
    		    }catch (Exception e){//Catch exception if any
    		  System.err.println("Error: " + e.getMessage());
    		  }

    	fileContents.trimToSize();
    	return fileContents;

	}
	
}
