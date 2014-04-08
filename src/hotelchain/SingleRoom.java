package hotelchain;

/**
 * Represents a specific type of a Room containing a single bed. 
 * @author Joost Janssen
 */
public class SingleRoom extends Room 
{
	private static final long serialVersionUID = 5790021076629102015L;

	/**
	 * @param _roomNumber Room number.
	 * @param _rate Nightly rate.
	 */
	public SingleRoom(int _roomNumber, int _rate)
	{
		super(_roomNumber,1, _rate);
	}
	
	@Override
	public String toString()
	{
		return "Single";
	}
}