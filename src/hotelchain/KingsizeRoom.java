package hotelchain;

/**
 * Represents a specific type of a Room containing a kingsize bed, accommodating 2 occupants. 
 * @author Joost Janssen
 */
public class KingsizeRoom extends Room 
{
	private static final long serialVersionUID = -8504496275498754646L;

	/**
	 * Constructs a new Kingsize room.
	 * @param _roomNumber Room number.
	 * @param _rate Nightly rate.
	 */
	public KingsizeRoom(int _roomNumber, int _rate)
	{
		super(_roomNumber,2, _rate);
	}
	
	@Override
	public String toString()
	{
		return "Kingsize";
	}
}