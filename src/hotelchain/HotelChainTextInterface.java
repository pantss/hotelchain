package hotelchain;

/**
 * Represents the main menu of the HoteChain text-based user interface.
 * Extends TextInterface for basic text-based user interface functionality. 
 * @author Joost Janssen
 */
public class HotelChainTextInterface extends TextInterface
{
	
	/**
	 * Constructs an instance of a textual HotelChain user interface.
	 * @param _chain HotelChain for which this class presents a textual interface.
	 */
	public HotelChainTextInterface(HotelChain _chain)
	{
		super(_chain);	
		printHeader("Welcome to " + chain.getName() + ". ");
		
		while(!exitRequested)
			showFirstLevelOptionMenu();
	}	

	/**
	 * Displays the first screen of this interface.
	 */
	private void showFirstLevelOptionMenu()
	{	
		//last option must be exit option
		String[] options = { "About " + chain.getName() + ".", 	
							"Guest Registration Management.",	
							"Reservation Manager.",
							"Exit Menu"};						 
		printOptions(options);
			
		int choice = getUserChoice(0,options.length);
		
		switch(choice)
		{
			case 1: showAbout(); break;
			case 2: 
				new GuestRegistrationTextInterface(chain, true); break;
			case 3:
				new ReservationManagerTextInterface(chain);	break;
			default: exitRequested = true; System.out.println("! Exiting interface."); 
		}	
	}
	
	/**
	 * Displays the About screen of this interface.
	 */
	private void showAbout()
	{
		Hotel[] hotels = chain.getHotels();
		
		printHeader("About " + chain.getName());
		
		System.out.println(" | Hotel" + "          " + "No. of Rooms");
		printSingleLine();
		
		//TODO: Calculate number of spaces needed for correct column alignment
		for(int i=0; i<hotels.length;i++)
			System.out.println(" | " + hotels[i].getName() + "          " + hotels[i].getNumberOfRooms());	
		printDoubleLine();
	}	
}