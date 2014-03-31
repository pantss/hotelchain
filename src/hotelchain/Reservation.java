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
	private final int reservationID;
	private final int guestID;
	private String hotelName;
	private int roomNumber;
	private boolean bridalSuite;
	private boolean cancelled;
	
	/**
	 * Constructs a new Reservation with given guest ID number, hotel name, room number, start and en dates and reservation ID.
	 * @param _guestID ID number of Guest this reservation was booked by.
	 * @param _hotelName Name of hotel booked by this reservation.  
	 * @param _roomNumber Room number of Room booked by this reservation.
	 * @param resStartDate Start date of this reservation.
	 * @param resEndDate End date of this reservation.
	 * @param resID ID number of this reservation.
	 */
	public Reservation(int _guestID, String _hotelName, int _roomNumber, Calendar resStartDate, Calendar resEndDate, boolean _bridalSuite, int resID)
	{
		guestID = _guestID;
		hotelName = _hotelName;
		roomNumber = _roomNumber;
		startDate = resStartDate;
		endDate = resEndDate;
		bridalSuite = _bridalSuite;
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