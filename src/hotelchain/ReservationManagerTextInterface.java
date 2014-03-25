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
	 * @param _reservationManager
	 * @param _guestRegistration
	 */
	public ReservationManagerTextInterface(ReservationManager _reservationManager, GuestRegistration _guestRegistration)//HotelChain _chain)
	{
	   super(_guestRegistration, false);
	   reservationManager = _reservationManager;
	   currentDate = reservationManager.getCurrentDate();
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
		
		printOptions(options);
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
		System.out.println(addEastBorderTo(" | ID | # of reservations: " + reservationManager.getNumberOfReservations(), 37, "|"));
		
		for(int i=0; i<reservationManager.getReservationIDcounter(); i++)
		{
			Reservation r = reservationManager.getReservation(i);
			if(r!=null)
			{
				printSingleLine();
				String out = " | " + r.getID() + ": " + guestRegistration.getGuest(r.getGuestID()).getName() + "(" + r.getGuestID() + ")";
				System.out.println(out = addEastBorderTo(out, 37, "|"));
				String out2 = " | 	at " + r.getHotelName() +  ", Room #" + r.getRoomNumber();
				System.out.println(addEastBorderTo(out2, 33, "|"));
				String out3 = " | 	 " + reservationManager.calendarToString(r.getStartDate());
				System.out.println(addEastBorderTo(out3, 33, "|"));
				String out4 = " |  	 - " + reservationManager.calendarToString(r.getEndDate());
				System.out.println(addEastBorderTo(out4, 34, "|"));
				if(r.isCancelled())
					System.out.println(addEastBorderTo(" | ! This reservation was CANCELLED.", 37, "|"));
			}
		}		
		printDoubleLine();
	}
	
	/**
	 * Displays the Reserve Room screen of this interface.
	 */
	private void showReserveRoom()
	{		
		//Can be substituted with showFindGuests (needs to be made protected then)
		displayGuestsInformation();
		
		System.out.println("> Please enter ID of guest to book a room for: ");
		int guestID = getUserChoice(0, guestRegistration.getIDcounter());
		Guest guest = guestRegistration.getGuest(guestID);
		
		Hotel chosenHotel = null;
		Calendar startDate, endDate = null;
		
		if(guest != null)
		{
			printGuestInfo(guest, true);
			boolean correct = promptInputConfirmation(true);
			
			if(correct)
			{
				chosenHotel = showChooseHotel();
				if(chosenHotel != null)
				{						
					System.out.println("Today is: " +reservationManager.calendarToString(currentDate));		
					startDate = showGetDesiredDate("arrival");
					endDate = showGetDesiredDate("departure");
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
		
		showReservationManager();		
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
		printOptions(options);
		int choice = getUserChoice(0,options.length);
		
		int reservationID = -1;
		boolean cancel = false;
		
		switch(choice)
		{
		case 1: 
			System.out.println("> Enter name: ");
			String nameEntered = getUserInput();
			int guestIDfound = showFindGuestID(nameEntered);
			reservationID = showFindReservationID(guestIDfound);
			break;
		case 2:
			System.out.println("> Enter reservation ID: ");
			reservationID = getUserChoice(0, reservationManager.getIDcounter());
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
	 * @return Returns a reservation ID as indicated by user input.
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
	showReservationManager();
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
		printOptions(options);
		
		int choice = getUserChoice(0,options.length);
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
	 */
	private Calendar showGetDesiredDate(String time)
	{
		System.out.println("> Please enter the desired date of " + time + " (\"Month dd yyyy\" or \"Month dd\")");		
		String input = getUserInput();
		Calendar desiredDate= convertInputToDate(input);
		
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
		System.out.println("	 Result:");
		System.out.println("	***********************");
		System.out.println(addEastBorderTo("	* Reservation ID: " + res.getID(), 23, "*"));
		System.out.println(addEastBorderTo( "	* Guest: " + guestRegistration.getGuest(res.getGuestID()).getName(), 23, "*"));
		System.out.println(addEastBorderTo( "	* Hotel: " + res.getHotelName(), 23, "*"));
		System.out.println(addEastBorderTo( "	* Room #" + res.getRoomNumber()	, 23, "*"));
		System.out.println(addEastBorderTo( "	*  " + reservationManager.calendarToString(res.getStartDate()), 23, "*"));
		System.out.println(addEastBorderTo( "	*   - " + reservationManager.calendarToString(res.getEndDate()), 23, "*"));
		if(res.isCancelled())
			System.out.println(addEastBorderTo( "	* ! CANCELLED", 23, "*"));
		System.out.println("	***********************");
	}	
	
	/**
	 * Converts a given user input to a date in Calendar format.
	 * @param input User input.
	 * @return Returns a date in Calendar format. 
	 */
	private Calendar convertInputToDate(String input)
	{
		String[] split = input.split(" ");
		
		int year = currentDate.get(Calendar.YEAR);
		
		if(split.length == 2)
				System.out.println("! Assuming year is " + year);
		else 
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
		
		String monthString = split[0];
		int month = getMonth(monthString);
		Calendar newDate = Calendar.getInstance();
		newDate.set(year, month, day, 0, 0, 0);
	
		return newDate;
	}
	
	/**
	 * Returns a Calendar representation of a given month.
	 * @param monthString String representation of a month.
	 * @return Returns a Calendar representation of a given month.
	 */
	private int getMonth(String monthString)
	{	
		if(monthString.equalsIgnoreCase("january"))
		     return Calendar.JANUARY;	
		else if(monthString.equalsIgnoreCase("february"))
			return Calendar.FEBRUARY;
		else if(monthString.equalsIgnoreCase("march"))
				return Calendar.MARCH;
		else if(monthString.equalsIgnoreCase("april"))
			return Calendar.APRIL;
		else if(monthString.equalsIgnoreCase("may"))
			return  Calendar.MAY;
		else if(monthString.equalsIgnoreCase("june"))
			return Calendar.JUNE;
		else if(monthString.equalsIgnoreCase("july"))
				return Calendar.JULY;
		else if(monthString.equalsIgnoreCase("august"))
				return Calendar.AUGUST;
		else if(monthString.equalsIgnoreCase("september"))
			return Calendar.SEPTEMBER;
		else if(monthString.equalsIgnoreCase("october"))
			return Calendar.OCTOBER;
		else if(monthString.equalsIgnoreCase("november"))
			return Calendar.NOVEMBER;
		else if(monthString.equalsIgnoreCase("december"))
			return Calendar.DECEMBER;
		else
			{
				System.out.println("> Please enter a correct month: ");
				getMonth(getUserInput());
			}	
		return -1;		
	}
}