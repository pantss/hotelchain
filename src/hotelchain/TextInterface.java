package hotelchain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * TODO: East border of columns
 */
public class TextInterface
{
	protected HotelChain chain;
	protected boolean exitRequested;
	private BufferedReader inputReader;
		
	public TextInterface(HotelChain _chain)
	{
		chain = _chain;
		exitRequested = false;
		inputReader = new BufferedReader(new InputStreamReader(System.in));
	}
	
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
					{
						System.out.println("! Please give a valid answer.");
						System.out.println("> Is the above information correct? (Y/N): ");
					}

				}
		}
		
		return correct;	
	}
	
	/**
	 * Gets user's choice of options displayed.
	 * 
	 * @param int firstOption: displayed value of first option in choice menu
	 * @param int lastOption: displayed value of last option in choice menu
	 * @return int: displayed value of option chosen
	 * 
	 *  firstOption <= lastOption
	 *  TODO Cancel input
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
		//		e.printStackTrace();
			}
			
			try{
				 choice = Integer.parseInt(response);
			}
			catch(NumberFormatException e)	{
				choice = -1;
			}
	
			if(choice == 9 || (choice >= firstOption && choice <= lastOption))
				validResponse = true;
			else
				System.out.println("! Please enter a valid choice."	
						+ "\n> Your choice: ");
		}	
		
		return choice;
	}

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

	protected void printOptions(String[] options)
	{
		
		System.out.println("\n> Please select an option.");
		printDoubleLine();
		
		for(int i=1; i<options.length; i++)
		{
			System.out.println(" " + i + ": " + options[i-1]);
		}
		System.out.println(" -------------");
		if(options.length<10)
			System.out.println(" " + 9 +": " + options[options.length-1]);
		else
			System.out.println("   " + options.length + ": " + options[options.length-1]);
		printDoubleLine();
		System.out.println("\n> Your choice: ");
	}
	
	/**
	 * Prints the header String between two short double lines.
	 * @param header Text to be printed as header.
	 */
	protected void printHeader(String header)
	{
		System.out.println();
		printDoubleLine();
		System.out.println(" | " + header);
		printDoubleLine();
		//System.out.println("\n  ====" + header + "=====");
	}
	/**
	 * Capitalizes every first letter of each word of a String and returns this as a new String
	 * @param string: String to be capitalized
	 * @return a new String with each word's first letter Capitalized
	 */
	protected String capitalize(String string)
	{
		char[] chars = string.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		
		for(int i=1; i<chars.length;i++)
			if(Character.isSpaceChar(chars[i]))
				if(Character.isLetter(chars[i+1]))
					chars[i+1] = Character.toUpperCase(chars[i+1]);
		
		return new String(chars);
	}
	
	/**
	 * Prints a single line on the console, preceded by " | ".
	 */
	protected void printSingleLine()
	{
		System.out.println(" | ----------------------------------");
	}
	/**
	 * Prints a double line on the console.
	 */
	protected void printDoubleLine()
	{
		System.out.println("  ===================================");
	}
}