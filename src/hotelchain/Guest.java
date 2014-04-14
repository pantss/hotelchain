package hotelchain;

import java.io.Serializable;

/**
 * Represents a guest of a hotel. It contains personal identification information of the guest.
 * Implements Serializable so instances can be stored using the FileHandler class.
 * @author Joost Janssen
 */
public class Guest  implements Serializable
{
	private static final long serialVersionUID = -2659809038612587271L;
	private final String name, address, city, country;
	private final int guestID;
	
	/**
	 * Constructs a new instance of a guest of this hotel based on a given name, address, city, country and guest ID number.
	 * @param _name Guest's name
	 * @param _address Guest's address
	 * @param _city Guest's city
	 * @param _country Guest's country
	 * @param id Guest's ID number
	 */
	public Guest(String _name, String _address, String _city, String _country, int id)
	{
		name = _name;
		address = _address;
		city = _city;
		country = _country;
		guestID = id;
	}

	/**
	 * @return Returns this guest's ID number.
	 */
	public int getID()
	{
		return guestID;
	}
	
	/**
	 * @return Returns this guest's name.
	 */
	protected String getName()
	{
		return name;
	}
	
	/**
	 * @return Returns this guest's city.
	 */
	protected String getCity()
	{
		return city;
	}	
	
	/**
	 * @return Returns this guest's address.
	 */
	protected String getAddress()
	{
		return address;
	}
	
	/**
	 * @return  Returns this guest's country.
	 */
	protected String getCountry()
	{
		return country;
	}
}