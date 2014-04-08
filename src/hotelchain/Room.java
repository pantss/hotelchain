package hotelchain;
import java.io.Serializable;

/**
 * Representation of a room in a hotel with a room number, number of beds, and nightly rate. 
 * Implements Serializable so instances can be stored using the FileHandler class.
 * @author Joost Janssen
 */
public class Room implements Serializable 
{
	private static final long serialVersionUID = -3251826277669516453L;
	private int roomNumber;
	private int beds;
	private int rate;
	
	/**
	 * Constructs an instance of a room.
	 * @param room Room number of this room.
	 * TODO Comment
	 */
	public Room(int _roomNumber, int _beds, int _rate)
	{
		roomNumber = _roomNumber;
		beds = _beds;
		rate = _rate;
	}	
	
	/**
	 * @return Returns this room's number.
	 */
	public int getRoomNumber()
	{
		return roomNumber;
	}
	
	/**
	 * @return Returns the number of beds in this room.
	 */
	public int getNumberOfBeds()
	{
		return beds;
	}
	
	/**
	 * @return Returns the rate of this room per night.
	 */
	public int getRate()
	{
		return rate;
	}
	
	@Override
	public String toString()
	{
		return "Room";
	}
}