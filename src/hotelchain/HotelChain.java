package hotelchain;
/**
 * @author Joost Janssen
 *  
 *		Main class of the HotelChain application. Contains chain and hotel information 
 *		as well as its guest and registration management systems.
 *
 *		 int NUMBER_OF_HOTELS, String[] hotelNames and  int[] roomsInHotel can be adapted
 *		to reflect chain's current situation.
 */
public class HotelChain 
{	
	private final String name;
	private Hotel[] hotels;
	private GuestRegistration guests;
	private ReservationManager reservations;
	
	private final int NUMBER_OF_HOTELS = 3;	
	private final String[] hotelNames = new String[] { "SimpleHotel", "MediocreHotel", "FancyHotel" };
	private final int[] roomsInHotel = new int[] { 60, 60, 80 };
	
	/**
	 * @param _name Name of hotel chain
	 * 
	 * Constructs an instance of the HotelChain class and creates instances
	 * of its guest registration and reservation manager systems.
	 */
	public HotelChain(String _name)
	{
		name = _name;
		hotels = new Hotel[NUMBER_OF_HOTELS];
		addHotels();
		
		guests = new GuestRegistration(); 
		reservations = new ReservationManager(hotels);
	}
	
	/**
	 * Populates the hotel chain with its hotels.
	 */
	private void addHotels()
	{
		for(int i=0; i< NUMBER_OF_HOTELS;i++)
			hotels[i] = new Hotel(hotelNames[i], roomsInHotel[i]);
	}
	
	/**
	 * @return Returns a string representation of the hotel chain's name.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @return Returns a Hotelarray containing this chain's hotels.
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
	 * @return REturns this chain's GuestRegistration system.
	 */
	public GuestRegistration getGuestRegistration()
	{
		return guests;
	}
}