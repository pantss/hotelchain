package hotelchain;

/**
 * Provides a text-based user interface towards a Hotel Chain's guest registration system.
 * Extends TextInterface for basic text-based user interface functionality.
 * @author Joost Janssen
 */
public class GuestRegistrationTextInterface extends TextInterface
{	
	/**
	 * Constructs an instance of a textual guest registration interface towards a given HotelChain. 
	 * @param _chain HotelChain for which this object provides a textual guest registration interface. 
	 * @param _init Indicates whether this interface should be displayed.
	 */
	public GuestRegistrationTextInterface(HotelChain _chain, boolean _init)
	{
		super(_chain);
		if(_init)
			init();
	}
	
	/**
	 * Initializes this interface.
	 */
	private void init()
	{
		while(!exitRequested)
			showFirstLevelOptionMenu();		
	}
	
	/**
	 * Displays the first screen of this interface.
	 */
	private void showFirstLevelOptionMenu()
	{
		//Last option in array must be exit option
		String[] options = {"General information about guests." ,	
											"Find a Guest.",					
											"Register a New Guest.",									
											"Remove a Guest's registration.",				
											"Go Back"};							
		
		printHeader("GUEST REGISTRATION MANAGEMENT");
		
		printOptions(options);				
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
		System.out.println(" | ID |	# of guests: " +  chain.getGuestRegistration().getNumberOfRegisteredGuests());
	
		for(int i=0; i<chain.getGuestRegistration().getGuestIDcounter(); i++)
		{
			Guest g = chain.getGuestRegistration().getGuest(i);
			if(g!=null)
			{
				printSingleLine();
				System.out.println(" | " + g.getID() + ": " + g.getName()
						+ "\n |    " + g.getAddress() + ", " + g.getCity() + ", " + g.getCountry());			
			}				
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
		printOptions(options);		
		int choice = getUserChoice(0,options.length);
		
		int guestID = -1;
		boolean cancel = false;		
	
		switch(choice)
		{
			case 1: 	
				System.out.println("> Enter name: ");
				String nameEntered = getUserInput();
				guestID = chain.getGuestRegistration().findGuestID(nameEntered);
				break;
			case 2:
				System.out.println("> Enter guest ID: ");
				guestID = getUserChoice(0, chain.getGuestRegistration().getIDcounter());
				break;
			default: 
				cancel = true;
		}
		if(!cancel)
		{	
			Guest guest = chain.getGuestRegistration().getGuest(guestID);
			
			if(guest != null)
				printGuestInfo(guest, true);
			else
				System.out.println("! Guest not found.");		
		}
	}
	
	/**
	 * Displays the Add New Guest screen of this interface. 
	 * @param showHeader Indicates whether the header of this screen should be printed.
	 * 
	 * TODO Cancel while entering info
	 */
	private void showAddNewGuest(boolean showHeader)
	{
		if(showHeader)
			printHeader("REGISTER A NEW GUEST");
		else
			System.out.println("! Information discarded. Please re-enter information.");
		
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
		
		printGuestInfo(new Guest(name, address, city, country, -1), false);
				
		boolean correct = promptInputConfirmation(true);
			
		if(correct)
		{
			int guestID = chain.getGuestRegistration().registerNewGuest(name, address, city, country);
			
			System.out.println("	*****************"
					+ "\n	* New guest:\n	* \n	* "
					+ guestID + ": " + name 
					+ "\n	*****************"); 
		}
		else
			showAddNewGuest(false);		
	}	
	
	/**
	 * Displays the Remove Guest screen of this interface.
	 */
	private void showRemoveGuest()
	{
		//TODO choose between choose all or find guest
		displayGuestsInformation();
		
		System.out.println("> Please enter ID of guest to be removed: ");
		int guestID = getUserChoice(0, chain.getGuestRegistration().getIDcounter());
		Guest guest = chain.getGuestRegistration().getGuest(guestID);
		
		if(guest != null)
		{
			printGuestInfo(guest, true);
			System.out.println("> Are you sure you want to remove this guest?");
			boolean correct = promptInputConfirmation(false);
			
			if(correct)
			{
				if(chain.getGuestRegistration().removeGuest(guest))
					System.out.println("! Guest succesfully removed.");
			}
			else		
				System.out.println("! No changes made.");						
		}
		else
			System.out.println("! Error: Guest could not be found.");
		showFirstLevelOptionMenu();
	}
	
	/**
	 * Prints the registered information of a given Guest.
	 * @param guest Guest to print information of.
	 * @param printID Indicates whether the Guests's guestID should be printed.
	 */
	protected void printGuestInfo(Guest guest, boolean printID)
	{
		System.out.println("\n	 Result:\n	*****************************");
		System.out.println("	* Name: " + guest.getName()
						+ "\n	* Address: " + guest.getAddress()
						+ "\n	* City: " + guest.getCity()
						+ "\n	* Country: " + guest.getCountry() );
		if(printID)
			System.out.println( "	* Guest ID: " + guest.getID());
		System.out.println("	*****************************");
	}
}