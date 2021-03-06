package hotelchain;

import java.util.ArrayList;

/**
 * Manages the administration of guests of a hotel chain.
 * Extends the FileHandler class in order to be able to store guest registration information to a file. 
 * @author Joost Janssen
 */
public class GuestRegistration extends FileHandler
{
	private  ArrayList<Guest> guests;
	private int guestIDcounter;	
	private final static  String filename = "hotelchain.registeredGuests";

	/**
	 * Constructs an instance of the guest registration management system.
	 * Reads existing guests from file "filename". If file does not exists, creates a new, empty file.
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
	 * @param name Guest's name.
	 * @param address Guest's address.
	 * @param city Guest's city.
	 * @param country Guest's country.
	 * @return Returns the newly registered guest's ID number.
	 */
	protected int registerNewGuest(String name, String address, String city, String country)
	{		
		Guest newGuest = new Guest(name, address, city, country, guestIDcounter++);
		guests.add(newGuest);
		
		if(writeFile(guests, guestIDcounter))
			return newGuest.getID();
		
		return -1;
	}	

	/**
	 * Removes a given guest's information from the hotel chain's systems.
	 * @param guest Guest to be removed.
	 * @return Returns whether the guest was successfully removed.
	 */
	protected boolean removeGuest(Guest guest)
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
	 * Finds a guest's ID number based on (part of) their name.
	 * @param name Name given to match guest ID number to.
	 * @return Returns guest ID belong to the given name or null if no match was found.
	 */
	protected ArrayList<Guest> findGuestID(String name)
	{
		ArrayList<Guest> hits = new ArrayList<Guest>();
		if(name!=null)
			for(int i=0;i<guests.size();i++)
				if(guests.get(i).getName().toLowerCase().contains(name.toLowerCase()))
					hits.add(guests.get(i));
				
		return hits;		
	}	
	
	/**
	 * Finds a guest based on their guest ID number.
	 * Note: assumes there can be no duplicate IDs
	 * @param guestID Guest ID number 
	 * @return Returns the guest whose ID number was given or null if no match was found. 	
	 */
	protected Guest getGuest(int guestID)
	{
		if(guestID>=0)
			for(int i=0; i<guests.size();i++)
				if(guests.get(i).getID() == guestID)
					return guests.get(i);
			
		return null;
	}	
		
	/**
	 * @return Returns the number of guests currently registered at this hotel chain.
	 */
	protected int getNumberOfRegisteredGuests()
	{
		return guests.size();
	}	
	
	/**
	 * @return Returns the current guestIDcounter.
	 */
	protected int getGuestIDcounter()
	{
		return guestIDcounter;
	}
}