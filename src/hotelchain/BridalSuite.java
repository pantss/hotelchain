package hotelchain;

/**
 * Represents a Bridal Suite, a special type of hotel room.
 * @author Joost Janssen
 */
public class BridalSuite extends Room
{
	private static final long serialVersionUID = -1344213575347808024L;

	/**
	 * Constructs a new bridal suite with room number room
	 * @param room Room number of this bridal suite.
	 */
	public BridalSuite(int room)
	{
		super(room);
	}
	
	/* 
	 * @see hotelchain.Room#isBridalSuite()
	 */
	@Override
	public boolean isBridalSuite()
	{
		return true;
	}
}