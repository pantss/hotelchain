package hotelchain;
import java.io.Serializable;
import java.util.Calendar;


public class Reservation implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8903154660483910256L;
	private final Guest guest;
	private Hotel hotel;
	private Room room;
	private Calendar startDate;
	private Calendar endDate;
	private final int reservationID;
	private boolean cancelled;
	
	public Reservation(Guest _guest, Hotel resHotel, Room resRoom, Calendar resStartDate, Calendar resEndDate, int ID)
	{
		guest = _guest;
		hotel = resHotel;
		room = resRoom;
		startDate = resStartDate;
		endDate = resEndDate;
		reservationID = ID;
		cancelled = false;
	}

	public void cancel()
	{
		room.vacate(reservationID);
		cancelled = true;
	}
	public Guest getGuest()
	{
		return guest;
	}
	public Hotel getHotel()
	{
		return hotel;
	}
	public Room getRoom()
	{
		return room;
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
}
