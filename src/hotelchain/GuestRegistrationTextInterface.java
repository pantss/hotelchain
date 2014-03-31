package hotelchain;

import java.util.ArrayList;

/**
 * Provides a text-based user interface towards a Hotel Chain's guest registration system.
 * Extends TextInterface for basic text-based user interface functionality.
 * @author Joost Janssen
 */
public class GuestRegistrationTextInterface extends TextInterface
{	
	protected final GuestRegistration guestRegistration;

	/**
	 * Constructs an instance of a textual guest registration interface, extending the basic TextInterface.
	 * @param _guestRegistration The Guest Registration system this class provides an interface for.
	 * @param _init Indicates whether this interface should be displayed.
	 */
	public GuestRegistrationTextInterface(GuestRegistration _guestRegistration)
	{
		super();
		guestRegistration = _guestRegistration;
	}
	
	/**
	 * Initializes this interface.
	 */
	public void init()
	{
		while(!exitRequested)
			showGuestRegistration();		
	}
	
	/**
	 * Displays the first screen of this interface.
	 */
	private void showGuestRegistration()
	{
		//Last option in array must be exit option
		String[] options = {"General information about guests." ,	
											"Find a Guest.",					
											"Register a New Guest.",									
											"Remove a Guest's registration.",				
											"Go Back"};							
		
		printHeader("GUEST REGISTRATION MANAGEMENT");
		
		printOptions(options, true);				
		int choice = getUserChoice(0,options.length);
		
		switch(choice)
		{
			case 1: displayGuestsInformation();  break;
			case 2: showFindGuests(); break;
			case 3:  showAddNewGuest(true); break;
			case 4: showRemoveGuest(); break;
			default: exitRequested = true;
		}		
	}
	
	/**
	 * Displays the Guests Information screen of this interface.
	 */
	protected void displayGuestsInformation()
	{
		printHeader("Currently Registered Guests");
		System.out.println(addEastBorderTo(" | ID |	# of guests: " +  guestRegistration.getNumberOfRegisteredGuests(), "|"));
	
		for(int i=0; i<guestRegistration.getGuestIDcounter(); i++)
		{
			Guest g = guestRegistration.getGuest(i);
			if(g!=null)
			{
				printSingleLine();
				printGuest(g);
	/*			String out = " | " + g.getID() + ": " + g.getName();
				System.out.println(addEastBorderTo(out, 37, "|"));
				String out2 = " |    " + g.getAddress() + ", " + g.getCity() + ", " + g.getCountry();
				System.out.println(addEastBorderTo(out2, 37, "|"));
	*/		}				
		}
		printDoubleLine();
	}
	
	/**
	 * Displays the Find Guests screen of this interface.
	 */
	private void showFindGuests()
	{
		String[] options = { "Name", 			
						  	"Guest ID",
							"Cancel"};	
		
		printHeader("FIND A GUEST BY");			
		printOptions(options, false);		
		int choice = -1;
		while(choice == -1)
			choice = getUserChoice(0,options.length);
		
		int guestID = -1;
		boolean cancel = false;		
	
		switch(choice)
		{
			case 1: 	
				System.out.println("> Enter name: ");
				String nameEntered = getUserInput();
				if(!nameEntered.isEmpty())
					guestID = showFindGuestID(nameEntered, false);
				else
				{
					showFindGuests();
					cancel = true;
				}
				if(guestID == -99)
					cancel = true;
				break;
			case 2:
				System.out.println("> Enter guest ID: ");
				guestID = getUserChoice(0, guestRegistration.getIDcounter());
				if(guestID == -1)
				{
					showFindGuests();
					cancel = true;
				}
				break;
			default: 
				cancel = true;
		}
		if(!cancel)
		{	
			Guest guest = guestRegistration.getGuest(guestID);
			
			if(guest != null)
				printSingleGuestInfo(guest, true);
			else
				System.out.println("! Guest not found.");		
		}
	}
	
	/**
	 * Finds a guest ID based on a given name. If multiple matches, asks for guest selection.
	 * @param nameEntered Name of guest to be found
	 * @return Returns a guest ID as indicated by user input. Returns -1 if no guest was found, -99 if multiple guests were found and displayed
	 */
	protected int showFindGuestID(String nameEntered, boolean forceChoice)
	{
		ArrayList<Guest> guestsFound = guestRegistration.findGuestID(nameEntered);
		int guestID = -1;
		
		if(guestsFound.size() == 1)
			guestID = guestsFound.get(0).getID();
		
		else if(guestsFound.size() > 1)
		{
			printHeader("Results");
			
			for(int i=0; i<guestsFound.size(); i++)
			{
				if(i != 0)
					printSingleLine();
				
				printGuest(guestsFound.get(i));
			}
			printDoubleLine();
			if(!forceChoice)
				return -99;
			else
			{
				System.out.println("> Enter Guest ID: ");
				while(guestID==-1)
					guestID = this.getUserChoice(0, guestRegistration.getGuestIDcounter());
			}
		}
		return guestID;
	}
	
	/**
	 * Displays the Add New Guest screen of this interface.
	 * @param showHeader Indicates whether the header of this screen should be printed.
	 */
	protected void showAddNewGuest(boolean showHeader)
	{
		if(showHeader)
			printHeader("REGISTER A NEW GUEST");			
		
		System.out.println("> Please enter guest's name:");
		String nameEntered = getUserInput();		
		String name = capitalize(nameEntered);		
	
		System.out.println("> Please enter guest's street address:");
		String addressEntered = getUserInput();
		String address = capitalize(addressEntered);
		
		System.out.println("> Please enter guest's city:");
		String cityEntered = getUserInput();
		String city = capitalize(cityEntered);
		
		System.out.println("> Please enter guest's country:");
		String countryEntered = getUserInput();
		String country = capitalize(countryEntered);
		
		printSingleGuestInfo(new Guest(name, address, city, country, -1), false);
				
		boolean correct = promptInputConfirmation(true);
			
		if(correct)
		{
			int guestID = guestRegistration.registerNewGuest(name, address, city, country);
			System.out.println("! New guest "+ name + " succesfully added with guest ID number: "+ guestID + "."); 
		}
		else
		{
			System.out.println("! Information discarded. Please re-enter infsormation.");
			showAddNewGuest(false);		
		}
	}	
	
	/**
	 * Displays the Remove Guest screen of this interface.
	 */
	private void showRemoveGuest()
	{
		//Can be substituted with showFindGuests() 
		displayGuestsInformation();
		
		System.out.println("> Please enter ID of guest to be removed: ");
		int guestID = getUserChoice(0, guestRegistration.getIDcounter());
		Guest guest = guestRegistration.getGuest(guestID);
		
		if(guest != null)
		{
			printSingleGuestInfo(guest, true);
			System.out.println("> Are you sure you want to remove this guest? (Y/N): ");
			boolean correct = promptInputConfirmation(false);
			
			if(correct)
			{
				if(guestRegistration.removeGuest(guest))
					System.out.println("! Guest succesfully removed.");
			}
			else		
				System.out.println("! No changes made.");						
		}
		else
			System.out.println("! Error: Guest could not be found.");
	}
	
	/**
	 * Prints the registered information of a given Guest.
	 * @param guest Guest to print information of.
	 * @param printID Indicates whether the Guests's guestID should be printed.
	 */
	protected void printSingleGuestInfo(Guest guest, boolean printID)
	{
		System.out.println("\n	 Result:\n	******************************");
		System.out.println(addEastBorderTo("        * Name: " + guest.getName(), "*"));
		System.out.println(addEastBorderTo("        * Address: " + guest.getAddress(), "*"));
		System.out.println(addEastBorderTo("        * City: " + guest.getCity(), "*"));
		System.out.println(addEastBorderTo("        * Country: " + guest.getCountry(), "*"));
		if(printID)
			System.out.println(addEastBorderTo( "        * Guest ID: " + guest.getID(), "*"));
		System.out.println("	******************************");
	}
	
	/**
	 * Prints Guest g's personal information to the screen.
	 * @param g Guest
	 */
	private void printGuest(Guest g)
	{
		String out = " | " + g.getID() + ": " + g.getName();
		System.out.println(addEastBorderTo(out,  "|"));
		String out2 = " |    " + g.getAddress();
		System.out.println(addEastBorderTo(out2, "|"));
		String out3 = " |    " + g.getCity() + ", " + g.getCountry();
		System.out.println(addEastBorderTo(out3, "|"));

	}
}