package hotelchain;

public class HotelChainTextInterface 
{
	private HotelChain chain;
	
	public HotelChainTextInterface(HotelChain _chain)
	{
		chain = _chain;
		
		showInterface();
	}
	
	private void showInterface()
	{
		showWelcomeText();
		
		new FirstLevelMenuTextInterface(chain);
	}
	
	private void showWelcomeText()
	{
		System.out.println("Welcome to " + chain.getName() + ". ");
	}
	
}
