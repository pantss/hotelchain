package hotelchain;

import java.util.ArrayList;

/**
 * Main class of the HotelChain application. Contains chain and hotel information 
 * as well as its guest and registration management systems.
 * Extends the FileHandler class to store/export HotelChain configuration.
 * @author Joost Janssen
 */
public class HotelChain extends FileHandler
{	
	private final String name;
	private Hotel[] hotels;
	private GuestRegistration guests;
	private ReservationManager reservations;
	
	private final int NUMBER_OF_HOTELS = 3;	
	private final String[] hotelNames = new String[] { "SimpleHotel", "MediocreHotel", "FancyHotel" };
	private final String[] roomTypes = new String[]{"Single", "Double", "Queensize", "Kingsize", "Family", "Bridal"}; 
	private final int[] roomsInHotel = new int[] { 60, 60, 80 };
	private final int standardRate = 50; //Nightly rate for a single bed in the cheapest hotel, to be used as baseline.
	private final static String filename = "hotelchain.hotelsConfiguration";
	
	/**
	 * Constructs an instance of a HotelChain and creates instances of its guest registration and reservation manager systems.
	 * Reads hotel configuration from file, or else create new configuration as specified in method createHotels().
	 * @param _name Name of this HotelChain.
	 */
	@SuppressWarnings("unchecked")
	public HotelChain(String _name)
	{
		super(filename);
		name = _name;
		
		if(createNewFile())
		{
			hotels = new Hotel[NUMBER_OF_HOTELS];
			createHotels();
		}
		else
		{
			ArrayList<Hotel> hotelList = (ArrayList<Hotel>) readFile();
			hotels = new Hotel[hotelList.size()];
			for(int i=0; i< hotelList.size(); i++)
				hotels[i] = hotelList.get(i);
		}
		
		guests = new GuestRegistration(); 
		reservations = new ReservationManager(hotels);
	}
	
	/**
	 * Populates this hotel chain with its hotels and writes the hotels to file.
	 */
	private void createHotels()
	{
		for(int i=0; i<NUMBER_OF_HOTELS; i++)
		{
			int roomNrCtr = 1;			
			hotels[i] = new Hotel(hotelNames[i], roomsInHotel[i], roomTypes);
			
			int[] typeFrequency = { roomsInHotel[i] /10,  	 		// Ratio of single rooms.
									roomsInHotel[i] / 5,			// Ratio of double rooms.
									roomsInHotel[i] / 3,			// Ratio of queensize rooms
									roomsInHotel[i] / 5, 			// Ratio of  kingsize rooms
									roomsInHotel[i] * (1/6)-1,		// Leftovers minus bridal suite= amount of family suites.
									1 };							// Amount of Bridal suites
			
			for(int j=0; j < typeFrequency.length; j++)
				for(int k=0; k < typeFrequency[j]; k++)
				{
					switch(j) 
					{
						case 0: hotels[i].addRoom(new SingleRoom(roomNrCtr, standardRate + (i*standardRate)/2)); break;
						case 1: hotels[i].addRoom(new DoubleRoom(roomNrCtr,  standardRate + (1 + i)*standardRate/(2)));break;
						case 2: hotels[i].addRoom(new QueensizeRoom(roomNrCtr, standardRate + (2 + i)*standardRate/(2)));break;
						case 3: hotels[i].addRoom(new KingsizeRoom(roomNrCtr, standardRate + (3 + i)*standardRate/(2)));break;
						case 4: hotels[i].addRoom(new FamilySuite(roomNrCtr, standardRate + (3 + 5*i)*standardRate/(2)));break;
						case 5: hotels[i].addRoom(new BridalSuite(roomNrCtr, standardRate + (10 + 3*i*i)*standardRate/(2)));break;
						default: hotels[i].addRoom(new Room(roomNrCtr, 1, standardRate));
					}
					roomNrCtr++;						
				}
			while(roomNrCtr <= roomsInHotel[i])       // Fill family suites up with modulo values of ratios used in typeFrequency[] to complete hotel population. 
			{
				 hotels[i].addRoom(new FamilySuite(roomNrCtr, (3 + 5*i)*standardRate/(2)));
				 roomNrCtr++;
			}				
		}
		
		ArrayList<Hotel> hotelList = new ArrayList<Hotel>();
		for(int i=0; i < hotels.length; i++)
			hotelList.add(hotels[i]);
		
		writeFile(hotelList, -1);			
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
	protected Hotel[] getHotels()
	{
		return hotels;
	}
	
	/**
	 * @return Returns this chain's ReservationManager system.
	 */
	protected ReservationManager getReservationManager()
	{
		return reservations;
	}
	
	/**
	 * @return Returns this chain's GuestRegistration system.
	 */
	protected GuestRegistration getGuestRegistration()
	{
		return guests;
	}
}