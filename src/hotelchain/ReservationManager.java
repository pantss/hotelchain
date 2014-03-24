package hotelchain;

import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;

//TODO add logs for reservations as well as ReservationArchive
public class ReservationManager extends FileHandler
{
	private ArrayList<Reservation> reservations;

	private int reservationIDcounter;
	private Calendar currentDate = Calendar.getInstance();	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy");
	
	private final static String filename = "reservations";
	
	@SuppressWarnings("unchecked")
	public ReservationManager()
	{
		super(filename);
		
		if(createNewFile())
			reservations = new ArrayList<Reservation>();
		else
			reservations = (ArrayList<Reservation>)readFile();
		
		reservationIDcounter = getIDcounter();
	}
	
	public Reservation reserveRoom(Guest resGuest, Hotel resHotel, Calendar resStartDate, Calendar resEndDate, boolean bridalSuite)
	{
		Reservation reservation = null;
	
		if(bridalSuite)
		{
			//TODO  TST
			System.out.println("Bridalsuite=y ");
			reservation = resHotel.reserveBridalSuite(resGuest, resStartDate, resEndDate, reservationIDcounter);

			//TODO  TST
			System.out.println("ReservationBridal: " + reservation); 
		}
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
	
	public int findReservationID(String name)
	{
		if(name!=null)
			for(int i=0; i<reservations.size(); i++)
				if(reservations.get(i).getGuest().getName().equalsIgnoreCase(name))
					return reservations.get(i).getID();
		
		return -1;
	}
	
	public void cancelReservation(Reservation reservation)
	{
		for(int i=0;i<reservations.size();i++)
			if(reservation.equals(reservations.get(i)))
			{
				reservations.get(i).cancel();
				
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
	/*
	 * Printing this instead of passing reservations so as to keep instances of Reservation private.
	 * 
	 * TODO build printAll, printComing, printAllNon-Cancelled, either here or in interface
	 */
/*	public void printAllReservations()
	{	
		for(int i=0; i<reservations.size();i++)
		{
			Reservation r = reservations.get(i);
//TODO TST
// System.out.println("Reservation: " + r.toString());

			System.out.println(r.getID() + ": " + r.getGuest().getName()
					+ "\n              from " + calendarToString(r.getStartDate()) + " untill " + calendarToString(r.getEndDate())
					+ "\nat hotel " + r.getHotel().getName() + ", room no. " + r.getRoom().getRoomNumber());
			if(r.isCancelled())
				System.out.println("!!! Reservation was CANCELLED.");
		}
		if(reservations.size() == 0)
			System.out.println("There are currently no reservations.");
	}
*/	//TODO printOccupationStatistics()
}