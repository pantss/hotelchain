package hotelchain;

/**
 * Represents a specific type of a Room containing one queensize and two single beds, accommodating 4 occupants. 
 * @author Joost Janssen
 */
public class FamilySuite extends Room 
{
	private static final long serialVersionUID = 8934183177613282629L;

	/**
	 * @param _roomNumber Room number.
	 * @param _rate Nightly rate.
	 */
	public FamilySuite(int _roomNumber, int _rate)
	{
		super(_roomNumber, 4, _rate);
	}
	
	@Override
	public String toString()
	{
		return "Family";
	}
}