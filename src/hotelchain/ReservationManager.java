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
		Room reservedRoom = null;
	
		if(bridalSuite)
		{	
			if(resHotel.isBridalSuiteAvailable(resStartDate, resEndDate))
				reservedRoom = resHotel.reserveBridalSuite(resGuest, resStartDate, resEndDate, reservationIDcounter);		
		}
		else
		{
			reservedRoom = resHotel.reserveRoom(resGuest, resStartDate, resEndDate, reservationIDcounter);
		}
		if(reservedRoom != null)
		{ 
			Reservation reservation = new Reservation(resGuest, resHotel, reservedRoom, resStartDate, resEndDate, reservationIDcounter++);
			reservedRoom.addReservation(reservation);
			reservations.add(reservation);
			
			if(writeFile(reservations, reservationIDcounter))
				return reservation;
		}
		return null;
	}	
	
	public Reservation findReservation(int resID)
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
	
	//TODO decide whether to keep cancelled reservations in array "reservations" or check for Reservation.cancelled
	public void cancelReservation(Reservation reservation)
	{
		for(int i=0;i<reservations.size();i++)
			if(reservation.equals(reservations.get(i)))
			{
				reservations.get(i).cancel();
	reservations.remove(i);
				
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
	
	/*
	 * Printing this instead of passing reservations so as to keep instances of Reservation private.
	 * 
	 * TODO build printAll, printComing, printAllNon-Cancelled, either here or in interface
	 */
	public void printAllReservations()
	{	
		for(int i=0; i<reservations.size();i++)
		{
			Reservation r = reservations.get(i);
//TODO TST
System.out.println("Reservation: " + r.toString());
//System.out.println(""
//System.out.println(""
//System.out.println("
			System.out.println(r.getID() + ": " + r.getGuest().getName()
					+ "\n              from " + calendarToString(r.getStartDate()) + " untill " + calendarToString(r.getEndDate())
					+ "\nat hotel " + r.getHotel().getName() + ", room no. " + r.getRoom().getRoomNumber());
		}
	}
	//TODO printOccupationStatistics()
}
