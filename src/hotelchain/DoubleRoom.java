package hotelchain;

/**
 * Represents a specific type of a Room containing two beds. 
 * @author Joost Janssen
 */
public class DoubleRoom extends Room 
{
	private static final long serialVersionUID = -5013125088845854614L;

	/**
	 * @param _roomNumber Room number.
	 * @param _rate Nightly rate.
	 */
	public DoubleRoom(int _roomNumber, int _rate)
	{
		super(_roomNumber,2, _rate);
	}
	
	@Override
	public String toString()
	{
		return "Double";
	}
}