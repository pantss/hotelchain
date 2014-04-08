package hotelchain;
/**
 * Main class of the HotelChain application. Contains chain and hotel information 
 * as well as its guest and registration management systems.
 * int NUMBER_OF_HOTELS, String[] hotelNames and  int[] roomsInHotel can be adapted
 * to reflect chain's current situation.
 * @author Joost Janssen
 */
public class HotelChain 
{	
	private final String name;
	private Hotel[] hotels;
	private GuestRegistration guests;
	private ReservationManager reservations;
	
	private final int NUMBER_OF_HOTELS = 3;	
	private final String[] hotelNames = new String[] { "SimpleHotel", "MediocreHotel", "FancyHotel" };
	private final String[] typesOfRooms = new String[]{"Single", "Double", "Queensize", "Kingsize", "Family", "Bridal"}; 
	private final int[] roomsInHotel = new int[] { 60, 60, 80 };
	private final int standardRate = 50; //Nightly rate for a single bed in the cheapest hotel, to be used as baseline.	
	
	/**
	 * Constructs an instance of a HotelChain and creates instances of its guest registration and 
	 * reservation manager systems.
	 * @param _name Name of this HotelChain.
	 * TODO Comment
	 */
	public HotelChain(String _name)
	{
		name = _name;
		hotels = new Hotel[NUMBER_OF_HOTELS];
		//TODO Hotels to file?
		addHotels();
		
		guests = new GuestRegistration(); 
		reservations = new ReservationManager(hotels);
	}
	
	/**
	 * Populates this hotel chain with its hotels.
	 * TODO test	 */
	private void addHotels()
	{
		//TODO This 
		for(int i=0; i<NUMBER_OF_HOTELS; i++)
		{
			int roomNrCtr = 1;			
			hotels[i] = new Hotel(hotelNames[i], roomsInHotel[i], typesOfRooms);
			
			int[] typeFrequency = { roomsInHotel[i] /10,  	 		// Ratio of single rooms.
									roomsInHotel[i] / 5,			// Ratio of double rooms.
									roomsInHotel[i] / 3,			// Ratio of queensize rooms
									roomsInHotel[i] / 5, 			// Ratio of  kingsize rooms
									roomsInHotel[i] * (1/6)-1,		// Leftovers minus bridal suite= amount of family suites.
									1 };							// Amount of Bridal suites
			
			for(int j=0; j < typeFrequency.length; j++)
				for(int k=0; k < typeFrequency[j]; k++)
				{
					switch(j) //TODO rate by hotel
					{
						case 0: hotels[i].addRoom(new SingleRoom(roomNrCtr, standardRate)); break;
						case 1: hotels[i].addRoom(new DoubleRoom(roomNrCtr, standardRate));break;
						case 2: hotels[i].addRoom(new QueensizeRoom(roomNrCtr, standardRate));break;
						case 3: hotels[i].addRoom(new KingsizeRoom(roomNrCtr, standardRate));break;
						case 4: hotels[i].addRoom(new FamilySuite(roomNrCtr, standardRate));break;
						case 5: hotels[i].addRoom(new BridalSuite(roomNrCtr, standardRate));break;
						default: //TODO default
					}
					roomNrCtr++;	
				}
			while(roomNrCtr <= roomsInHotel[i])       // Fill family suites up with modulo values of ratios used in typeFrequency[] to complete hotel population. 
			{
				 hotels[i].addRoom(new FamilySuite(roomNrCtr, standardRate));
				 roomNrCtr++;
			}	
		}
			
	}
	
	/**
	 * @return Returns a string representation of this hotel chain's name.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @return Returns an array of this HotelChain's Hotels.
	 */
	public Hotel[] getHotels()
	{
		return hotels;
	}
	
	/**
	 * @return Returns this chain's ReservationManager system.
	 */
	public ReservationManager getReservationManager()
	{
		return reservations;
	}
	
	/**
	 * @return Returns this chain's GuestRegistration system.
	 */
	public GuestRegistration getGuestRegistration()
	{
		return guests;
	}
}