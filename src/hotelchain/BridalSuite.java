package hotelchain;

/**
 * @author pants
 *
 */
public class BridalSuite extends Room{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1344213575347808024L;

	/**
	 * @param room
	 */
	public BridalSuite(int room)
	{
		super(room);
	}
	
	@Override
	public boolean isBridalSuite()
	{
		return true;
	}

}
