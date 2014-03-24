package hotelchain;
import java.io.Serializable;

/**
 * @author Joost Janssen
 * 
 * 	This class is a representation of a room in a hotel. It contains room information 
 * 	as well as details of its reservation.
 *		Implements Serializable so instances can be stored using the FileHandler class.
 */
public class Room implements Serializable 
{
	private static final long serialVersionUID = -3251826277669516453L;
	private int roomNumber;
	
	/**
	 * Constructs an instance of a room in a hotel. 
	 * @param room Room number of this room.
	 */
	public Room(int room)
	{
		roomNumber = room;
	}	
	
	/**
	 * @return Returns this room's room number.
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