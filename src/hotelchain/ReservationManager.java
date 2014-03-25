package hotelchain;

import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;

//TODO add logs for reservations as well as ReservationArchive (add clean cancelled/past res. option)
public class ReservationManager extends FileHandler
{
	private ArrayList<Reservation> reservations;

	private int reservationIDcounter;
	private Calendar currentDate = Calendar.getInstance();	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy");
	private Hotel[] hotels;
	
	private final static String filename = "reservations";
	
	@SuppressWarnings("unchecked")
	public ReservationManager(Hotel[] _hotels)
	{
		super(filename);
		hotels = _hotels;
		
		if(createNewFile())
			reservations = new ArrayList<Reservation>();
		else
			reservations = (ArrayList<Reservation>)readFile();
		
		reservationIDcounter = getIDcounter();
		
		passUpcomingReservationsToHotels();
	}
	
	public Reservation reserveRoom(Guest resGuest, Hotel resHotel, Calendar resStartDate, Calendar resEndDate, boolean bridalSuite)
	{
		Reservation reservation = null;
	
		if(bridalSuite)
			reservation = resHotel.reserveBridalSuite(resGuest, resStartDate, resEndDate, reservationIDcounter);
		else
			reservation = resHotel.reserveRoom(resGuest, resStartDate, resEndDate, reservationIDcounter);
		
		if(reservation != null)
		{ 
			reservations.add(reservation);
			reservationIDcounter++;
			
			if(writeFile(reservations, reservationIDcounter))
				return reservation;
		}
		return null;
	}	
	
	public Reservation getReservation(int resID)
	{
		for(int i=0; i<reservations.size();i++)
			if(reservations.get(i).getID() == resID)
				return reservations.get(i);
		
		return null;
	}
	
	// TODO find more than 1 reservation if guest has more than 1.
	public int findReservationID(int guestID)
	{
		if(guestID>-1)
			for(int i=0; i<reservations.size(); i++)
				if(reservations.get(i).getGuestID() == guestID)
					return reservations.get(i).getID();
		
		return -1;
	}
	
	public void cancelReservation(Reservation reservation)
	{
		for(int i=0;i<reservations.size();i++)
			if(reservation.equals(reservations.get(i)))
			{
				reservations.get(i).cancel();
				for(int j=0; j<hotels.length; j++)
					if(hotels[j].getName().equals(reservations.get(i).getHotelName()))
						hotels[j].cancelReservation(reservations.get(i));
				
				if(!writeFile(reservations, reservationIDcounter))
					System.out.println("ERROR: could not remove reservation from file");
			}
	}
	
	public Calendar getCurrentDate()
	{
		return currentDate;
	}
	
	public String calendarToString(Calendar date)
	{
		return dateFormat.format(date.getTime());
	}
	
	public int getReservationIDcounter()
	{
		return reservationIDcounter;
	}
	
	public int getNumberOfReservations()
	{
		return reservations.size();
	}
	
	private void passUpcomingReservationsToHotels()
	{
		for(int i=0; i<reservations.size(); i++)
			if(!reservations.get(i).isCancelled() && currentDate.before(reservations.get(i).getEndDate()))
				for(int j=0; j<hotels.length; j++)
					if(hotels[j].getName().equals(reservations.get(i).getHotelName()))
						hotels[j].addReservation(reservations.get(i));					
	}
	//TODO printOccupationStatistics()
}