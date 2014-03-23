package hotelchain;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author Joost Janssen
 *
 *	This class is a representation of a hotel. Contains hotel information such as its name and information about its rooms.
 *	Implements Serializable so instances can be stored using the FileHandler class.
 */
public class Hotel implements Serializable
{
	private static final long serialVersionUID = 3061856451553104034L;
	private final int numberOfRooms;
	private final String name;
	private final Room[] hotelRooms;
	private final int bridalSuiteNumber;	
	private ArrayList<Room> reservedRooms;
	
	/**
	 * Constructs an instance of a Hotel.
	 * 
	 * @param _name Name of this hotel
	 * @param rooms Number of rooms
	 */
	public Hotel(String _name, int rooms)
	{
		name = _name;
		numberOfRooms = rooms;
		hotelRooms = new Room[rooms];
		
		for(int i=0; i<hotelRooms.length-1; i++)
			hotelRooms[i] = new Room(i+1);
		
		hotelRooms[hotelRooms.length-1] = new BridalSuite(hotelRooms.length);
		bridalSuiteNumber = hotelRooms.length;		
		reservedRooms = new ArrayList<Room>();
	}
	
	/**
	 * Reserves a room in this hotel.
	 * 
	 * @param guest Guest to book reservation.
	 * @param startDate Arrival date of guest.
	 * @param endDate Departure date of guest.
	 * @param reservationID Reservation ID number.
	 * @return Returns an instance of the reserved room.
	 */
	public Room reserveRoom(Guest guest, Calendar startDate, Calendar endDate, int reservationID)
	{
		Room freeRoom = getFreeRoom(startDate, endDate);
		if(freeRoom!=null)
		{
			freeRoom.reserve(reservationID);
			reservedRooms.add(freeRoom);
			return freeRoom;
		}
		return null;
	}
	
	/**
	 * @return Room freeRoom
	 */
	/**
	 * Finds an unreserved room in this hotel during a given time frame. 
	 * 
	 * @param startDate Start date of time frame.
	 * @param endDate End date of time frame.
	 * @return Returns an instance of an available Room during the given time frame, or null when none is available.
	 */
	private Room getFreeRoom(Calendar startDate, Calendar endDate)
	{
		boolean freeRoomFound = false;
		int ctr = 0;
		
		while(!freeRoomFound)
		{
			if (hotelRooms[ctr].isAvailable(startDate, endDate))
				return hotelRooms[ctr];
			ctr++;
		}
		return null;
	}
	
	/**
	 * Reserves the bridal suite of this hotel.
	 * 
	 * @param guest Guest to book reservation.
	 * @param startDate Arrival date of guest.
	 * @param endDate Departure date of guest.
	 * @param reservationID Reservation ID number.
	 * @return Returns an instance of the reserved bridal suite.
	 */
	public Room reserveBridalSuite(Guest guest, Calendar startDate, Calendar endDate, int reservationID)
	{
		if(isBridalSuiteAvailable(startDate, endDate))
		{
			hotelRooms[bridalSuiteNumber-1].reserve(reservationID);
			reservedRooms.add(hotelRooms[bridalSuiteNumber-1]);
			return hotelRooms[bridalSuiteNumber-1];
		}
		return null;			
	}
	
	/**
	 * Checks whether the bridal suite of this hotel is available during a given time frame.
	 * 
	 * @param startDate Start date of time frame.
	 * @param endDate End date of time frame.
	 * @return Returns whether the bridal suite is available during the given time frame.
	 */
	public boolean isBridalSuiteAvailable(Calendar startDate, Calendar endDate)
	{
		return hotelRooms[bridalSuiteNumber-1].isAvailable(startDate, endDate);
	}
	
	/**
	 * @return Returns the name of this hotel.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @return Returns the number of rooms in this hotel.
	 */
	public int getNumberOfRooms()
	{
		return numberOfRooms;
	}
}