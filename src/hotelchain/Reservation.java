package hotelchain;
import java.io.Serializable;
import java.util.Calendar;


/**
 * Represents a reservation made at a hotel. It contains information about who made this reservation, when and where.
 * Implements Serializable so instances can be stored using the FileHandler class. 
 * @author Joost Janssen
 */
public class Reservation implements Serializable
{
	private static final long serialVersionUID = -8903154660483910256L;
	private Calendar startDate;
	private Calendar endDate;
	private final int reservationID, guestID;
//TODO repeat	private final int guestID;
	private String hotelName;
	private int roomNumber;

	private String roomType; 
	private boolean bridalSuite;
	private int totalRate;
	private boolean cancelled;
	
	/**
	 * Constructs a new Reservation with given guest ID number, hotel name, room number, start and end dates and reservation ID.
	 * @param _guestID ID number of Guest this reservation was booked by.
	 * @param _hotelName Name of hotel booked by this reservation.  
	 * @param _roomNumber Room number of Room booked by this reservation.
	 * @param resStartDate Start date of this reservation.
	 * @param resEndDate End date of this reservation.
	 * @param _bridalSuite Indicates whether this reservation is for a bridal suite.
	 * @param resID ID number of this reservation.
	 * TODO Comment, occupants
	 */
	public Reservation(int _guestID, String _hotelName, int _roomNumber, String _roomType, Calendar resStartDate, Calendar resEndDate, int _rate, boolean _bridalSuite, int resID)
	{
		guestID = _guestID;
	//	occupants = _occupants;
		hotelName = _hotelName;
		roomNumber = _roomNumber;
		roomType = _roomType;
		startDate = resStartDate;
		endDate = resEndDate;
		bridalSuite = _bridalSuite;
		totalRate = _rate;    //TODO Calculate total rate.
		reservationID = resID;		
		cancelled = false;
	}

	/**
	 * Cancels this reservation.
	 */
	public void cancel()
	{
		cancelled = true;
	}
	
	/**
	 * @return Returns the name of the reserved Hotel.
	 */
	public String getHotelName()
	{
		return hotelName;
	}
	
	/**
	 * @return Returns the room number of the reserved room.
	 */
	public int getRoomNumber()
	{
		return roomNumber;
	}
	
	/**
	 * @return Returns the ID number of the guest that made this reservation.
	 */
	public int getGuestID()
	{
		return guestID;
	}
	
	/**
	 * @return Returns the type of room reserved.
	 */
	public String getRoomType()
	{
		return roomType;
	}
	
	/**
	 * @return Returns the start date of this reservation.
	 */
	public Calendar getStartDate()
	{
		return startDate;
	}
	
	/**
	 * @return Returns the end date of this reservation.
	 */
	public Calendar getEndDate()
	{
		return endDate;
	}
	
	/**
	 * @return Returns whether this Reservation is for a bridal suite.
	 */
	public boolean isBridalSuite()
	{
		return bridalSuite;
	}
	
	/**
	 * @return Returns the total of this reservation.
	 */
	public int totalRate()
	{
		return totalRate;
	}
	
	/**
	 * @return Returns the ID number of this reservation.
	 */
	public int getID()
	{
		return reservationID;
	}
	
	/**
	 * @return Returns whether this reservation was cancelled.
	 */
	public boolean isCancelled()
	{
		return cancelled;
	}
}