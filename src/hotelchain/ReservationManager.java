package hotelchain;

import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * Manages the Reservations made at a HotelChain. It contains information about hotels, reservations and the date.
 * Extends the FileHandler class in order to be able to store guest registration information to a file. 
 * The variable filename may be adapted to reflect a desired file name.
 * @author Joost Janssen
 * TODO Future features: pricing, check in/out, read archive if desired.
 */
public class ReservationManager extends FileHandler
{
	private Hotel[] hotels;
	private ArrayList<Reservation> reservations;
	private int reservationIDcounter;
	
	private Calendar currentDate = Calendar.getInstance();	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy");
	
	private final static String filename = "hotelchain.reservations";
	private final static String archive_filename = "hotelchain.reservationsArchive";
	private FileHandler archiveFile;
	
	/**
	 * Constructs a new instance of a reservation manager managing reservations at the given Hotels.
	 * @param _hotels Array of Hotels where this reservation manager is used.
	 */
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
		
		archiveFile = new FileHandler(archive_filename);
	}
	
	/**
	 * Converts a given date in Calendar format to a String.
	 * @param date Date in Calendar format to be converted.
	 * @return Returns a String representation of the given date.
	 */
	public String calendarToString(Calendar date)
	{
		return dateFormat.format(date.getTime());
	}
	
	/**
	 * Passes information of already existing, upcoming reservations to the respective Hotels.
	 */
	private void passUpcomingReservationsToHotels()
	{
		for(int i=0; i<reservations.size(); i++)
			if(!reservations.get(i).isCancelled() && currentDate.before(reservations.get(i).getEndDate()))
				for(int j=0; j<hotels.length; j++)
					if(hotels[j].getName().equals(reservations.get(i).getHotelName()))
						hotels[j].addReservation(reservations.get(i));					
	}
	
	/**
	 * Reserve a room for a guest at a hotel at a certain time. Returns the resulting Reservation.
	 * @param resGuest Guest reserving a room.
	 * @param resHotel Reserved Hotel. 
	 * @param resStartDate Start date of reservation.
	 * @param resEndDate End date of reservation.
	 * @param bridalSuite Indicates whether a bridal suite is desired.
	 * @return Returns the resulting reservation made. Returns null if desired reservation could not be made.
	 */
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

	/**
	 * @return Returns an ArrayList of Reservations that where either cancelled or whose end date is before the current date.
	 */
	public ArrayList<Reservation> getPastAndCancelledReservations()
	{
		ArrayList<Reservation> pastReservations = new ArrayList<Reservation>();
		for(int i=0; i<reservations.size(); i++)
			if(reservations.get(i).isCancelled() || reservations.get(i).getEndDate().before(currentDate))
				pastReservations.add(reservations.get(i));
		
		return pastReservations;
	}
	
	/**
	 * Moves a given ArrayList of Reservations the archive by removing its Reservations from the list of current reservations. Writes archive to file.
	 * @param oldReservations ArrayList of Reservations to be moved to archive.
	 * @return Returns whether the given ArrayList was successfully moved to archive.
	 */
	@SuppressWarnings("unchecked")
	public boolean moveReservationsToArchive(ArrayList<Reservation> oldReservations)
	{
		for(int i=0; i<oldReservations.size();i++)
			reservations.remove(oldReservations.get(i));
		if(!writeFile(reservations, reservationIDcounter))
			return false;
		
		ArrayList<Reservation> archive = (ArrayList<Reservation>)archiveFile.readFile();
		if(archive != null)
			archive.addAll(oldReservations);
		else
			archive = oldReservations;
		
		if(archiveFile.writeFile(archive, archive.size()))
			return true;
		
		return false;		
	}
	/**
	 * Cancels a given Reservation.
	 * @param reservation Reservation to be cancelled.
	 */
	public void cancelReservation(Reservation reservation)
	{
		for(int i=0;i<reservations.size();i++)
			if(reservation.getID() == reservations.get(i).getID())
			{
				reservations.get(i).cancel();
				for(int j=0; j<hotels.length; j++)
					if(hotels[j].getName().equals(reservations.get(i).getHotelName()))
						hotels[j].cancelReservation(reservations.get(i));
				
				if(!writeFile(reservations, reservationIDcounter))
					System.out.println("ERROR: could not remove reservation from file");
			}
	}
	
	/**
	 * Returns the Reservation with the given reservation ID.
	 * @param resID Reservation ID.
	 * @return Returns the Reservation with given reservation ID. Returns null if no reservation was found.
	 */
	public Reservation getReservation(int resID)
	{
		for(int i=0; i<reservations.size();i++)
			if(reservations.get(i).getID() == resID)
				return reservations.get(i);
		
		return null;
	}
	
	/**
	 * Finds the ID number of the reservation made by the Guest with the given guest ID.
	 * @param guestID Guest ID number.
	 * @return Returns the ID number of the reservation made by the Guest with given guestID. Returns -1 if no reservation was found.
	 */
	public ArrayList<Reservation> findReservationID(int guestID)
	{
		ArrayList<Reservation> hits = new ArrayList<Reservation>();
		if(guestID>-1)
			for(int i=0; i<reservations.size(); i++)
				if(reservations.get(i).getGuestID() == guestID)
					hits.add(reservations.get(i));
		
		return hits;
	}

	
	/**
	 * @return Returns the current date.
	 */
	public Calendar getCurrentDate()
	{
		return currentDate;
	}
	
	/**
	 * @return Returns the current state of the reservation ID counter.
	 */
	public int getReservationIDcounter()
	{
		return reservationIDcounter;
	}
	
	/**
	 * @return Returns the current total number of Reservations.
	 */
	public int getNumberOfReservations()
	{
		return reservations.size();
	}
		
	/**
	 * @return Returns an array of the hotels managed by this Reservationmanager.
	 */
	public Hotel[] getHotels()
	{
		return hotels;
	}
}