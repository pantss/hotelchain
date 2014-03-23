package hotelchain;

public class FirstLevelMenuTextInterface extends TextInterface
{	
	public FirstLevelMenuTextInterface(HotelChain _chain)
	{
		super(_chain);
		while(!exitRequested)
			showFirstLevelOptionMenu();
	}
	
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
				new GuestRegistrationTextInterface(chain, true);			break;
			case 3:
				new ReservationManagerTextInterface(chain);	break;
			default: exitRequested = true; System.out.println("Exiting interface."); 
		}	
	}
	
	private void showAbout()
	{
		Hotel[] hotels = chain.getHotels();
		
		System.out.println("================================"
			+ "\nAbout " + chain.getName() + "."
			+ "\n================================");
		System.out.println("Hotel" + "          " + "No. of Rooms"
			+ "\n---------------------------");
		
		//TODO: Calculate number of spaces needed for correct column alignment
		for(int i=0; i<hotels.length;i++)
			System.out.println(hotels[i].getName() + "          " + hotels[i].getNumberOfRooms());				
	}	
}