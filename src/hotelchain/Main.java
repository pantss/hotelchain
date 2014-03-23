package hotelchain;

/**
 * @author Joost Janssen
 *	 Initializes the HotelChain application
 */
public class Main {
	/**
	 *  Initializes the HotelChain application and its interface
	 * @param args: arguments to be passed to application
	 */
	public static void main(String[] args)
	{		
		HotelChain chain = new HotelChain("Hotel's Hotels");
		new HotelChainTextInterface(chain);
		
	//	new HotelChainGUI(chain);		
	}
}
