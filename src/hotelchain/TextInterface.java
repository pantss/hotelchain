package hotelchain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Provides basic functionality of a text-based user interface towards a HotelChain. 
 * @author Joost Janssen
 */
public class TextInterface
{
	protected boolean exitRequested;
	private BufferedReader inputReader;
		
	/**
	 * Constructs a new instance of a text-based HotelChain user interface.
	 */
	public TextInterface()
	{	
		exitRequested = false;
		inputReader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	/**
	 * Presents a yes/no confirmation dialog.
	 * @param printText Indicates whether the dialog text is to be printed.
	 * @return Returns true or false as indicated by user input.
	 */
	protected boolean promptInputConfirmation(boolean printText)
	{
		if(printText)
			System.out.println("> Is the above information correct? (Y/N): ");
		boolean valid = false;
		boolean correct = false;
		
		while(!valid) 
		{
				String answer = getUserInput();
				if(!answer.isEmpty())
				{
					char c = answer.charAt(0);
					if(Character.toLowerCase(c) == 'y')
					{
						valid = true;
						correct = true;
					}
					else if( Character.toLowerCase(c) == 'n')
						valid = true;
					
					if(!valid)
						System.out.println("! Please give a valid answer.");
				}
		}		
		return correct;	
	}
	
	/**
	 * Presents a given list of valid user choice options. Last option in the list must be an exit option.
	 * @param options List of valid user choice options in String format.
	 */
	protected void printOptions(String[] options, boolean printText)
	{
		if(printText)
			System.out.println("\n> Please select an option.");
		printDoubleLine();
		
		for(int i=1; i<options.length; i++)
		{
			System.out.println(" " + i + ": " + options[i-1]);
		}
		System.out.println(" ------------");
		if(options.length<10)
			System.out.println(" " + 9 +": " + options[options.length-1]);
		else
			System.out.println("   " + options.length + ": " + options[options.length-1]);
		printDoubleLine();
		System.out.println("\n> Your choice: ");
	}
	
	/**
	 * Presents a user choice input dialog with a given valid choice range.
	 * @param firstOption Displayed value of first choice of valid choice range.
	 * @param lastOption Displayed value of last choice of valid choice range.
	 * @return Returns the choice as indicated by user input. Returns -1 if no number was entered.
	 */
	protected  int getUserChoice(int firstOption, int lastOption)
	{
		boolean validResponse = false;		
		String response = null;
		int choice = -1;
		
		while(!validResponse)
		{
			try	{
				response = inputReader.readLine();
			} 
			catch (IOException e)	{
				e.printStackTrace(System.out);
			}
			if(!response.isEmpty())
			try{
				 choice = Integer.parseInt(response);
			}
			catch(NumberFormatException e)	{
				
			}
	
			if(choice == 9 || (choice >= firstOption && choice < lastOption))
				validResponse = true;
			else
				System.out.println("! Please enter a valid choice."	
						+ "\n> Your choice: ");
		}			
		return choice;
	}

	/**
	 * Presents a user input dialog.
	 * @return Returns user input.
	 */
	protected String getUserInput()
	{
		String input = null;
		
		while(input == null)
		{
			try {
				input = inputReader.readLine();
			} catch (IOException e) {
				input = null;
				e.printStackTrace();
			}
		}		
		return input;
	}	
	
	/**
	 * Prints a given header String between two short double lines.
	 * @param header Text to be printed as header.
	 */
	protected void printHeader(String header)
	{
		System.out.println();
		printDoubleLine();
		print(header, "|");
		printDoubleLine();
	}
	
	/**
	 * Prints a given string surrounded by Strings "border". Extends to next line (within borders) if necessary.
	 * @param string String to print between borders.
	 * @param border Border String.
	 */
	protected void print(String string, String border)
	{
		if(!string.startsWith(" " + border))
			string = " ".concat(border + " " + string);
	//	string = string.concat(string);
		int borderLimit = 37;
		for(int i= string.length(); i<borderLimit; i++)
			string = string.concat(" ");
		if(string.length()>borderLimit)
		{
			int charCtr = 0;
			
			int i;
			for(i=0; charCtr < 2; i++)
			{
				if(string.charAt(i) != ' ')
					charCtr++;
			}
			int splitIndex = 0;
			for(int j= borderLimit-2; j>=0; j--)
				if(string.startsWith(" ", j))
				{
					splitIndex = j;
					j=-1;
				}
			print(string.substring(0, splitIndex), border);
			print(string.substring(0, i-1).concat(string.substring(splitIndex, string.length())), border);		
		}
		else
		{	
			string = string.concat(border);
			System.out.println(string);
		}
	}
		
	/**
	 * Prints a single horizontal line to the screen, bordered by " | "s.
	 */
	protected void printSingleLine()
	{
		System.out.println(" | --------------------------------- |");
	}
	
	/**
	 * Prints a double horizontal line to the screen.
	 */
	protected void printDoubleLine()
	{
		System.out.println("  ===================================");
	}

	/**
	 * Capitalizes every first letter of each word of a given String.
	 * @param string String to be capitalized.
	 * @return Returns a new String with each word's first letter Capitalized.
	 */
	protected String capitalize(String string)
	{	if(!string.isEmpty())
		{
			char[] chars = string.toCharArray();
			chars[0] = Character.toUpperCase(chars[0]);
			
			for(int i=1; i<chars.length;i++)
				if(Character.isSpaceChar(chars[i]))
					if(Character.isLetter(chars[i+1]))
						chars[i+1] = Character.toUpperCase(chars[i+1]);
		
			return new String(chars);
		}
		return "";
	}
}