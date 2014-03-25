package hotelchain;

import java.util.ArrayList;

/**
 * Manages the administration of guests of a hotel chain.
 * Extends the FileHandler class in order to be able to store guest registration information to a file. 
 * The variable filename may be adapted to reflect a desired file name.
 * @author Joost Janssen
 */
public class GuestRegistration extends FileHandler
{
	private  ArrayList<Guest> guests;
	private int guestIDcounter;
	
	private final static  String filename = "registeredGuests";

	/**
	 * Constructs an instance of the guest registration management system.
	 * Reads existing guests from file with file name. IF file does not exists, creates a new, empty file.
	 */
	@SuppressWarnings("unchecked")
	public GuestRegistration() 
	{
		super(filename);
		
		if(createNewFile())
			guests = new ArrayList<Guest>();
		else
			guests = (ArrayList<Guest>)readFile();

		guestIDcounter = getIDcounter();
	}
	
	/**
	 * Registers a new guest of the hotel chain given their personal information and returns its guest ID number.
	 * @param name Guest's name
	 * @param address Guest's address
	 * @param city Guest's city
	 * @param country Guest's country
	 * @return Returns the newly registered guest's ID number.
	 */
	public int registerNewGuest(String name, String address, String city, String country)
	{		
		Guest newGuest = new Guest(name, address, city, country, guestIDcounter++);
		guests.add(newGuest);
		
		if(writeFile(guests, guestIDcounter))
			return newGuest.getID();
		
		return -1;
	}
	
	/**
	 * Finds a guest's ID number based on their name.
	 * @param name Name given to match guest ID number to
	 * @return Returns guest ID belong to the given name or null of no match was found.
	 * 
	 * TODO  Finds guest ID based on NAME ONLY (returns first match only)
	 * TODO Find guest based on part of name
	 * TODO Present multiple results and choose
	 */
	public int findGuestID(String name)
	{
		if(name!=null)
			for(int i=0;i<guests.size();i++)
				if(guests.get(i).getName().equalsIgnoreCase(name))
					return guests.get(i).getID();					
				
		return -1;		
	}	
	
	/**
	 * Finds a guest based on their guest ID number.
	 * Note: assumes there can be no duplicate IDs
	 * @param guestID Guest ID number 
	 * @return Returns the guest whose ID number was given or null of no match was found. 	
	 */
	public Guest getGuest(int guestID)
	{
		if(guestID>=0)
			for(int i=0; i<guests.size();i++)
				if(guests.get(i).getID() == guestID)
					return guests.get(i);
			
		return null;
	}	

	/**
	 * Removes a given guest's information from the hotel chain's systems.
	 * @param guest Guest to be removed.
	 * @return Returns whether the guest was successfully removed.
	 */
	public boolean removeGuest(Guest guest)
	{
		if(guest!=null)
		{
			guests.remove(guest);
			
			if(writeFile(guests, guestIDcounter))
				return true;
		}				
		return false;		
	}
		
	/**
	 * @return Returns the number of guests currently registered with this hotel chain.
	 */
	public int getNumberOfRegisteredGuests()
	{
		return guests.size();
	}	
	
	/**
	 * @return Returns the current guestIDcounter.
	 */
	public int getGuestIDcounter()
	{
		return guestIDcounter;
	}
}