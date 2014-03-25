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
	private ArrayList<Reservation> upcomingReservations;
	

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
		upcomingReservations = new ArrayList<Reservation>();
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
	public Reservation reserveRoom(Guest guest, Calendar startDate, Calendar endDate, int reservationID)
	{
		Room freeRoom = getFreeRoom(startDate, endDate);
		if(freeRoom!=null)
		{
			Reservation reservation = new Reservation(guest.getID(), this.getName(), freeRoom.getRoomNumber(), startDate, endDate, reservationID);
			addReservation(reservation);
			return reservation;
		}
		return null;
	}
	
	/**
	 * Adds a reservation to the upcoming reservations administration of this Hotel.
	 * 
	 * @param reservation Reservation to be added to administration of this Hotel.
	 */
	public void addReservation(Reservation reservation)
	{
		if(reservation != null)
		{
			upcomingReservations.add(reservation);
			System.out.println("Hi! " + this.getName());
			for(int i=0; i<upcomingReservations.size(); i++)
				System.out.println("res: " + upcomingReservations.get(i));
		}
	}
	
	/**
	 * Reserves the bridal suite of this hotel. If bridal suite is unavailable, reserve a normal room at this hotel.
	 * 
	 * @param guest Guest to book reservation.
	 * @param startDate Arrival date of guest.
	 * @param endDate Departure date of guest.
	 * @param reservationID Reservation ID number.
	 * @return Returns an instance of the reserved bridal suite. If bridal suite is unavailable, reserve a normal room at this hotel.
	 */
	public Reservation reserveBridalSuite(Guest guest, Calendar startDate, Calendar endDate, int reservationID)
	{
		Reservation reservation = null;
		
		if(isRoomAvailableBetween(bridalSuiteNumber, startDate, endDate))
			reservation = new Reservation(guest.getID(), this.getName(), bridalSuiteNumber, startDate, endDate, reservationID);
		else
			 reservation =  reserveRoom(guest, startDate, endDate, reservationID);	
		
		addReservation(reservation);
		return reservation;		
	}
	
	/**
	 * Cancels a given Reservation at this hotel.
	 * @param reservation Reservation to be cancelled.
	 */
	public void cancelReservation(Reservation reservation)
	{
		upcomingReservations.remove(reservation);			
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
		for(int i=1; i<=hotelRooms.length; i++)
			if(isRoomAvailableBetween(i, startDate, endDate))
				return hotelRooms[i-1];
		
		return null;
	}
	
	/**
	 * Checks whether the Room at this hotel with a given roomnumber is available at a given interval.
	 * 
	 * @param roomNr Room number
	 * @param startDate Start date of interval
	 * @param endDate End date of interval
	 * @return Returns whether the Room with given room number is available at the given interval.
	 */
	private boolean isRoomAvailableBetween(int roomNr, Calendar startDate, Calendar endDate)
	{
		boolean available = true;
		
		for(int i=0;i<upcomingReservations.size();i++)
		{	
			
			if(upcomingReservations.get(i).getRoomNumber() == roomNr)
			{
				if(upcomingReservations.get(i).getStartDate().before(endDate))
				{	
					if(startDate.before(upcomingReservations.get(i).getEndDate()))
						if(!upcomingReservations.get(i).isCancelled())
							available = false;
				}
				else if(upcomingReservations.get(i).getStartDate().before(endDate))
					if(!upcomingReservations.get(i).isCancelled())
						available = false;
			}				
		}
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
}