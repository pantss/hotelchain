package hotelchain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * TODO: Exit options
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
			System.out.println("Is the above information correct? (Y/N): ");
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
				System.out.println("Please enter a valid choice."	
						+ "\nYour choice: ");
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
//		System.out.println("\nPlease select an option."
//				+ "\n================================");
		
		for(int i=0; i<options.length-1; i++)
		{
			System.out.println(i+1 + ": " + options[i]);
		}
		if(options.length<10)
			System.out.println(9 +": " + options[options.length-1]);
		else
			System.out.println(options.length + ": " + options[options.length-1]);
		
		System.out.println("================================"
				+ "\nYour choice: ");
	}
	
	protected void printHeader(String header)
	{
	//	System.out.println( "\n================================\n" 
	//			+ header
	//			+ "\n================================");
		
		System.out.println("=====" + header + "=====");
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
}