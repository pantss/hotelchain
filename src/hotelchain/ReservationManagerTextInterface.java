package hotelchain;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Provides a text-based user interface towards a HotelChain's reservation management system.
 * Extends GuestRegistrationTextInterface as this class extends a GuestRegistrationTextInterface with reservation management options.
 * @author Joost Janssen
 */
public class ReservationManagerTextInterface extends GuestRegistrationTextInterface
{
	private boolean exitRequested;
	private Calendar currentDate;
	private final ReservationManager reservationManager;
	
	/**
	 * Constructs an instance of a textual reservation management interface, extending the GuestRegistration textual interface.
	 * @param _reservationManager Reservation manager this class provides a text-based interface for.
	 * @param _guestRegistration Guest registration providing information to the reservation manager.
	 */
	public ReservationManagerTextInterface(ReservationManager _reservationManager, GuestRegistration _guestRegistration)
	{
	   super(_guestRegistration);
	   reservationManager = _reservationManager;
	   currentDate = reservationManager.getCurrentDate();
	}	
	
	/**
	 * Initializes this interface.
	 */
	public void init()
	{
		while(!exitRequested)
			showReservationManager();		
	}
	
	/**
	 * Displays the first screen of this interface.
	 */
	private void showReservationManager()
	{
		//Last option in array must be exit option
		String[] options = {"General information about \n	reservations.",
											"Reserve a Room",
											"Find a Reservation",											
											"Cancel a Reservation",
											"Clean Reservations",
											"Go Back"};
		
		printHeader("RESERVATION MANAGEMENT");
		
		printOptions(options, true);
		int choice = getUserChoice(0, options.length);
		
		switch(choice)
		{
		case 1: displayReservationsInformation(); break;
		case 2: showReserveRoom(); break;
		case 3: showFindReservation(); break;
		case 4: showCancelReservation(); break;
		case 5: showCleanReservations(); break;
		default: exitRequested = true;
		}
	}
	
	/**
	 * Displays the Clean Reservations (move to archive) screen.
	 */
	private void showCleanReservations()
	{
		ArrayList<Reservation> pastAndCancelledReservations = reservationManager.getPastAndCancelledReservations();
		for(int i=0; i<pastAndCancelledReservations.size();i++)
		{
			printReservation(pastAndCancelledReservations.get(i));
			System.out.println("> Are you sure you want to move this reservation to the archive? (Y/N): ");
			if(!promptInputConfirmation(false))
				pastAndCancelledReservations.remove(i);		
		}
		if(pastAndCancelledReservations.size() > 0)
		{
			if(reservationManager.moveReservationsToArchive(pastAndCancelledReservations))
				System.out.println("! Indicated reservation(s) successfully moved to archive.");
			else
				System.out.println("! There was an error moving reservations to archive.");
		}
		else
			System.out.println("! No reservations were removed.");
	}
	
	/**
	 * Displays the Reservations Information screen of this interface.
	 */
	private void displayReservationsInformation()
	{
		printHeader("All Reservations");
		System.out.println(addEastBorderTo(" | ID | # of reservations: " + reservationManager.getNumberOfReservations(), "|"));
		
		for(int i=0; i<reservationManager.getReservationIDcounter(); i++)
		{
			Reservation r = reservationManager.getReservation(i);
			if(r!=null)
			{
				printSingleLine();
				String out = " | " + r.getID() + ": " + guestRegistration.getGuest(r.getGuestID()).getName() + " (" + r.getGuestID() + ")";
				System.out.println(out = addEastBorderTo(out, "|"));
				String out2 = " |  at " + r.getHotelName();
				if(r.isBridalSuite())
					out2 = out2.concat(", the Bridal Suite");
				else
					out2 = out2.concat(", Room #" + r.getRoomNumber());
				System.out.println(addEastBorderTo(out2, "|"));
				String out3 = " |  " + reservationManager.calendarToString(r.getStartDate());
				System.out.println(addEastBorderTo(out3, "|"));
				String out4 = " |    - " + reservationManager.calendarToString(r.getEndDate());
				System.out.println(addEastBorderTo(out4, "|"));
				if(r.isCancelled())
					System.out.println(addEastBorderTo(" | ! This reservation was CANCELLED.", "|"));
			}
		}		
		printDoubleLine();
	}
	
	/**
	 * Displays the Reserve Room screen of this interface.
	 */
	private void showReserveRoom()
	{	
		if(guestRegistration.getNumberOfRegisteredGuests() < 1)
		{
			System.out.println("! Please register a guest first:");
			showAddNewGuest(false);			
		}
		//Can be substituted with showFindGuests (needs to be made protected then)
		displayGuestsInformation();
		
		System.out.println("> Please enter ID of guest to book a room for: ");
		int guestID = getUserChoice(0, guestRegistration.getIDcounter());
		Guest guest = guestRegistration.getGuest(guestID);
		
		Hotel chosenHotel = null;
		Calendar startDate, endDate = null;
		
		if(guest != null)
		{
			printSingleGuestInfo(guest, true);
			boolean correct = promptInputConfirmation(true);
			
			if(correct)
			{
				chosenHotel = showChooseHotel();
				if(chosenHotel != null)
				{						
					System.out.println("Today is: " +reservationManager.calendarToString(currentDate));		
					startDate = showGetDesiredDate("Arrival");
					endDate = showGetDesiredDate("Departure");
					
					boolean bridalDesired = showGetBridalDesired(chosenHotel, startDate, endDate);			
					
					Reservation newReservation = reservationManager.reserveRoom(guest, chosenHotel, startDate, endDate, bridalDesired);
					
					if(newReservation != null)
					{
						printReservation(newReservation);
						
						if(promptInputConfirmation(true))
							System.out.println("! Reservation booked succesfully!");
						else
						{
							reservationManager.cancelReservation(newReservation);
							System.out.println("! Reservation cancelled.");
						}							
					}
					else
						System.out.println("! There was an error booking this reservation.");					
				}					
			}
			else
				System.out.println("! No guest selected.");				
		}
		else
			System.out.println("! Error: Guest could not be found.");
	}
	
	/**
	 * Displays the Find Reservation screen of this interface.
	 */
	private void showFindReservation()
	{
		String[] options = { "Name",
							 "Reservation ID",
							 "Cancel"}; 
		
		printHeader("FIND A RESERVATION");
		System.out.println("> Find a reservation by");
		printOptions(options, false);
		int choice = -1;
		while(choice == -1)
			choice = getUserChoice(0,options.length);
		
		int reservationID = -1;
		boolean cancel = false;
		
		switch(choice)
		{
		case 1: 
			System.out.println("> Enter name: ");
			String nameEntered = getUserInput();
			if(!nameEntered.isEmpty())
			{
				int guestIDfound = showFindGuestID(nameEntered, true);
				reservationID = showFindReservationID(guestIDfound);
			}
			else
			{
				showFindReservation();
				cancel = true;				
			}
			break;
		case 2:
			System.out.println("> Enter reservation ID: ");
			reservationID = getUserChoice(0, reservationManager.getIDcounter());
			if(reservationID == -1)
			{
				showFindReservation();
				cancel = true;
			}
			break;
		default:
			cancel = true;
		}
		if(!cancel)
		{
			Reservation reservation = reservationManager.getReservation(reservationID);
			
			if(reservation != null)
				printReservation(reservation);
			else
				System.out.println("! Reservation not found.");
		}
	}
	
	/**
	 * Finds a reservation ID based on a given guest ID. If multiple matches, asks for reservation selection.
	 * @param guestID Guest ID to use as search criterium for reservation.
	 * @return Returns a reservation ID as indicated by user input. Returns -1 if no reservation was found, -99 if multiple reservations were found and displayed
	 */
	private int showFindReservationID(int guestID)
	{
		ArrayList<Reservation> resFound = reservationManager.findReservationID(guestID);
		int resID = -1;
		if(resFound.size() == 1)
			resID = resFound.get(0).getID();
		else if(resFound.size() > 1)
		{
			for(int i=0; i<resFound.size(); i++)
			{
				printReservation(resFound.get(i));
				if(promptInputConfirmation(true))
				{
					resID = resFound.get(i).getID();
					i = resFound.size();
				}
			}
		}
		return resID;
	}
	
	/**
	 * Displays the Cancel Reservation screen of this interface.
	 */
	private void showCancelReservation()
	{
		//Can be substituted with showFindReservation()
		displayReservationsInformation();
		
		System.out.println("> Please enter ID of reservation to be cancelled: ");
		int reservationID = getUserChoice(0, reservationManager.getIDcounter());
		Reservation reservation = reservationManager.getReservation(reservationID);
		
		if(reservation!=null)
		{
			printReservation(reservation);
			System.out.println("> Are you sure you want to cancel this reservation? (Y/N): ");
			boolean correct = promptInputConfirmation(false);
			
			if(correct)
			{
				reservationManager.cancelReservation(reservation);
				System.out.println("! Reservation cancelled.");
			}
			else
				System.out.println("! No changes were made.");
		}
		else
			System.out.println("! Error: Reservation could not be found.");
	}	
	/**
	 * Displays the Choose Hotel screen of this interface.
	 * @return Returns the Hotel indicated by user input.
	 */
	private Hotel showChooseHotel()
	{
		Hotel[] hotels = reservationManager.getHotels();
		String[] options = new String[hotels.length+1];
		
		for(int i=0; i<hotels.length; i++)
			options[i] = hotels[i].getName();
		options[options.length-1] = "Cancel";		
		printOptions(options, true);
		
		int choice = -1;
		while(choice == -1)
			choice = getUserChoice(0,options.length);
		if(choice == 9)
		{
			System.out.println("! Cancelled.");
			return null;
		}
		return hotels[choice-1];	
	}
	
	/**
	 * Displays the Indicate Desired Date screen of this interface.
	 * @param time The current date.
	 * @return Returns the desired date as indicated by user input.
	 * 
	 * TODO Fix date input formatting (slashes?)
	 */
	private Calendar showGetDesiredDate(String time)
	{
		System.out.println("> Please enter the desired date of " + time + " (\"mm/dd\" or \"mm/dd/yyyy\")");		
		Calendar desiredDate = null;
		while(desiredDate == null)
		{
			String input = getUserInput();
			desiredDate= convertInputToDate(input);
		}
		System.out.println("Date entered: " + reservationManager.calendarToString(desiredDate));
		
		return desiredDate;
	}
	
	/**
	 * Displays the Indicate Bridalsuite Desired screen of this interface.
	 * @param hotel Desired Hotel to be reserved. 
	 * @param startDate Desired start date of reservation.
	 * @param endDate Desired end date of reservation.
	 * @return Returns whether the bridal suite is desired as indicated by user input.
	 */
	private boolean showGetBridalDesired(Hotel hotel, Calendar startDate, Calendar endDate)
	{
		System.out.println("> Would you like to book the bridal suite (if available)?");
			 return promptInputConfirmation(false);		
	}
	
	/**
	 * Prints information of a given Reservation to the screen.
	 * @param res Reservation whose information is to be printed.
	 */
	private void printReservation(Reservation res)
	{
		System.out.println("\n	 Result:\n	******************************");
		System.out.println(addEastBorderTo("        * Reservation ID: " + res.getID(), "*"));
		System.out.println(addEastBorderTo("        * -------------------------- ", "*"));
		System.out.println(addEastBorderTo("        *  " + guestRegistration.getGuest(res.getGuestID()).getName(), "*"));
		System.out.println(addEastBorderTo("        * "+ res.getHotelName(), "*"));
		if(res.isBridalSuite())
			System.out.println(addEastBorderTo("        * Room: Bridal Suite", "*"));
		else
			System.out.println(addEastBorderTo( "        * Room #" + res.getRoomNumber(), "*"));
		System.out.println(addEastBorderTo( "        *  " + reservationManager.calendarToString(res.getStartDate()), "*"));
		System.out.println(addEastBorderTo( "        *   - " + reservationManager.calendarToString(res.getEndDate()), "*"));
		if(res.isCancelled())
			System.out.println(addEastBorderTo("        * ! CANCELLED", "*"));
		System.out.println("	******************************");
	}	
	
	/**
	 * Converts a given user input to a date in Calendar format.
	 * @param input User input.
	 * @return Returns a date in Calendar format. 
	 */
	private Calendar convertInputToDate(String input)
	{
		String[] split = input.split("/");
		
		int year = currentDate.get(Calendar.YEAR);
		
		if(split.length != 2) 
		{
			if(split.length > 3 || split.length < 2)
				return null;
			
			String yearString = split[2];
			
			if(yearString.length()!=4)
				return null;
			year = Integer.parseInt(yearString);			
		}
				
		String dayString = split[1];
		
		if(dayString.length()>2 || dayString.length() < 1)
			return null;
		int day = Integer.parseInt(dayString);
		
		int month = -1;
		
		try{
			month =  Integer.parseInt(split[0]);
		}
		catch (NumberFormatException e)	{
			return null;
		}
		
		if(month > 12 || month < 1)
			return null;
		
		Calendar newDate = Calendar.getInstance();
		newDate.set(year, month-1, day, 0, 0, 0);
	
		return newDate;
	}
}