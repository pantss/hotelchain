package hotelchain;

/**
 * Initializes the HotelChain application.
 * @author Joost Janssen
 */
public class Main {
	/**
	 * Initializes the HotelChain application and its interface.
	 * @param args Arguments to be passed to application.
	 */
	public static void main(String[] args)
	{		
		HotelChain chain = new HotelChain("Hotel's Hotels");
		
		new HotelChainTextInterface(chain);		
	//	new HotelChainGUI(chain);		
	}
}