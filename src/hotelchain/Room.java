package hotelchain;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

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
	private ArrayList<Reservation> reservationList;
	
	/**
	 * Constructs an instance of a room in a hotel. 
	 * @param room Room number of this room.
	 */
	public Room(int room)
	{
		roomNumber = room;
		reservationList = new ArrayList<Reservation>();
	}
	
	/**
	 * Notifies the room of an occurrence of its reservation. 	 
	 * @param resID Reservation ID number.
	 */
	public void reserve( int resID)
	{
		reservationList.add(resID, null);
	}
	/**
	 * Passes a Reservation to this room.
	 * @param res Reservation this room has been booked in.
	 * 
	 * TODO is this clever?
	 */
	public void addReservation(Reservation res)
	{
		reservationList.set(res.getID(), res);
	}
	
	/**
	 * Vacates this room and designates it as available.
	 */
	public void vacate(int resID)
	{
			reservationList.remove(resID);
	}
	
	/**
	 * Checks whether this room is available at a given time frame.
	 * 
	 * @param startDate Start date of time frame.
	 * @param endDate End date of time frame.
	 * @return Returns whether this room is available at the given time frame.
	 */
	public boolean isAvailable(Calendar startDate, Calendar endDate)
	{
		for(int i=0; i < reservationList.size(); i++)
		{
			if(reservationList.get(i).getStartDate().before(endDate))
					return false;
		}
		return true;
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