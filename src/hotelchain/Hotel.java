package hotelchain;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Representation of a hotel. Contains hotel information such as its name and rooms.
 * Implements Serializable so instances can be stored using the FileHandler class.
 * @author Joost Janssen
 */
public class Hotel implements Serializable 
{
	private static final long serialVersionUID = 3061856451553104034L;
	private final int numberOfRooms;
	private final String name;	
	private final String[] roomTypes;
	private final ArrayList<Room> hotelRooms;
	private ArrayList<Reservation> upcomingReservations;

	/**
	 * Constructs an instance of a Hotel. 
	 * @param _name Name of this hotel.
	 * @param _roomTypes Types of rooms in this hotel.
	 */
	public Hotel(String _name, int rooms, String[] _roomTypes)
	{
		name = _name;
		numberOfRooms = rooms;
		roomTypes = _roomTypes;
		
		upcomingReservations = new ArrayList<Reservation>();		
		hotelRooms = new ArrayList<Room>();	
	}
	
	/**
	 * Adds a given room to the list of rooms in this Hotel.
	 * @param room Room to add.
	 */
	protected void addRoom(Room room)
	{
		hotelRooms.add(room);
	}
	
	/**
	 * Reserves a room in this hotel. 
	 * @param guest Guest to book reservation.
	 * @param startDate Arrival date of guest.
	 * @param endDate Departure date of guest.
	 * @param roomType Type of room.
	 * @param reservationID Reservation ID number.
	 * @return Returns the resulting Reservation. Returns null if no free room could be found.
	 */
	protected Reservation reserveRoom(Guest guest, Calendar startDate, Calendar endDate, String roomType, int reservationID)
	{
		int	roomNr = getFreeRoom(startDate, endDate, roomType);
		if(roomNr!= -1 )
		{
			Reservation reservation = new Reservation(guest.getID(), this.getName(), roomNr, roomType, startDate, endDate, hotelRooms.get(roomNr-1).getRate(), reservationID);			
			addReservation(reservation);
			
			return reservation;
		}
		return null;
	}	
	
	/**
	 * Adds a reservation to the list of upcoming reservations of this Hotel. 
	 * @param reservation Reservation to be added.
	 */
	protected void addReservation(Reservation reservation)
	{
		if(reservation != null)
			upcomingReservations.add(reservation);
	}
	
	/**
	 * Cancels a given Reservation at this hotel.
	 * @param reservation Reservation to be cancelled.
	 */
	protected void cancelReservation(Reservation reservation)
	{
		upcomingReservations.remove(reservation);			
	}
	
	/**
	 * Finds an unreserved room of a specific type in this hotel during a given time frame. 
	 * @param startDate Start date of time frame.
	 * @param endDate End date of time frame.
	 * @param roomType Type of room.
	 * @return Returns an available Room during the given time frame, or -1 when none is available.
	 */
	private int getFreeRoom(Calendar startDate, Calendar endDate, String roomType)
	{
		for(int i=1; i<=hotelRooms.size(); i++)
			if(hotelRooms.get(i-1).toString().equals(roomType)) 
				if(isRoomAvailableBetween(i, startDate, endDate))
					return hotelRooms.get(i-1).getRoomNumber();
		
		return -1;
	}
	
	/**
	 * Checks whether the Room with a given roomnumber is available at a given time interval.
	 * @param roomNr Room number.
	 * @param startDate Start date of interval.
	 * @param endDate End date of interval.
	 * @return Returns whether the Room with given room number is available at the given interval.
	 */
	private boolean isRoomAvailableBetween(int roomNr, Calendar startDate, Calendar endDate)
	{
		boolean available = true;
		
		for(int i=0;i<upcomingReservations.size();i++)
			if(upcomingReservations.get(i).getRoomNumber() == roomNr) 
				if(upcomingReservations.get(i).getStartDate().getTime().before(endDate.getTime()))
					if(startDate.getTime().before(upcomingReservations.get(i).getEndDate().getTime()))
						if(!upcomingReservations.get(i).isCancelled())
							available = false;						
		return available;
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
	
	/**
	 * @return Returns the types of rooms in this hotel.
	 */
	public String[] getRoomTypes()
	{
		return roomTypes;
	}	
	
	public int getRateOfRoom(String roomType)
	{
		for(int i=1; i<=hotelRooms.size(); i++)
			if(hotelRooms.get(i-1).toString().equals(roomType))
				return hotelRooms.get(i-1).getRate();
		
		return -1;
	}
}