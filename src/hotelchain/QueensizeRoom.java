package hotelchain;

/**
 * Represents a specific type of a Room containing a queensize bed, accommodating 2 occupants. 
 * @author Joost Janssen
 */
public class QueensizeRoom extends Room 
{
	private static final long serialVersionUID = 5767445757518415158L;

	/**
	 * Constructs a new Queensize room.
	 * @param _roomNumber Room number.
	 * @param _rate Nightly rate.
	 */
	public QueensizeRoom(int _roomNumber, int _rate)
	{
		super(_roomNumber, 2, _rate);
	}
	
	@Override
	public String toString()
	{
		return "Queensize";
	}
}