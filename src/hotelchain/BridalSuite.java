package hotelchain;

/**
 * Represents a Bridal Suite, a special type of hotel Room containing a kingsize bed, accommodating 2 occupants.
 * @author Joost Janssen
 */
public class BridalSuite extends Room
{
	private static final long serialVersionUID = -1344213575347808024L;

	/**
	 * Constructs a new bridal suite with room number room
	 * @param room Room number of this bridal suite.
	 * @param rate Nightly rate
	 */
	public BridalSuite(int room,int rate)
	{
		super(room, 2, rate);
	}
	
	@Override
	public String toString()
	{
		return "Bridal";
	}
}