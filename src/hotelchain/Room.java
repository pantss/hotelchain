package hotelchain;
import java.io.Serializable;

/**
 * Represents of a room in a hotel with a certain room number 
 * Implements Serializable so instances can be stored using the FileHandler class.
 * @author Joost Janssen
 */
public class Room implements Serializable 
{
	private static final long serialVersionUID = -3251826277669516453L;
	private int roomNumber;
	
	/**
	 * Constructs an instance of a room.
	 * @param room Room number of this room.
	 */
	public Room(int room)
	{
		roomNumber = room;
	}	
	
	/**
	 * @return Returns this room's number.
	 */
	public int getRoomNumber()
	{
		return roomNumber;
	}
	
	/**
	 * @return Returns whether this room is a bridal suite.
	 */
	public boolean isBridalSuite()
	{
		return false;
	}
}