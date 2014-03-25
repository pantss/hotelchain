package hotelchain;
import java.io.Serializable;
import java.util.Calendar;


public class Reservation implements Serializable
{
	private static final long serialVersionUID = -8903154660483910256L;
	private Calendar startDate;
	private Calendar endDate;
	private final int reservationID;
	private boolean cancelled;
	

	private final int guestID;
	private String hotelName;
	private int roomNumber;
	
	public Reservation(int _guestID, String _hotelName, int _roomNumber, Calendar resStartDate, Calendar resEndDate, int resID)
	{
		guestID = _guestID;
		hotelName = _hotelName;
		roomNumber = _roomNumber;
		startDate = resStartDate;
		endDate = resEndDate;
		reservationID = resID;
		cancelled = false;
	}

	public void cancel()
	{
		cancelled = true;
	}
	
	public String getHotelName()
	{
		return hotelName;
	}
	
	public int getRoomNumber()
	{
		return roomNumber;
	}
	
	public int getGuestID()
	{
		return guestID;
	}
	
	public Calendar getStartDate()
	{
		return startDate;
	}
	public Calendar getEndDate()
	{
		return endDate;
	}
	public int getID()
	{
		return reservationID;
	}
	
	public boolean isCancelled()
	{
		return cancelled;
	}
}