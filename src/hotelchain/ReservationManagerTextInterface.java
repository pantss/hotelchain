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
	private ArrayList<Integer> removedGuestIDs;
	
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
	   removedGuestIDs = new ArrayList<Integer>();
	}	
	
	/**
	 * Initializes this interface.
	 */
	public void init()
	{
		exitRequested = false;
	//	reservationManager.checkForRemovedGuests();
		checkForRemovedGuests(); //TODO choose
		while(!exitRequested)
			showReservationManager();		
	}
	
	/**
	 * Displays the first screen of this interface.
	 */
	private void showReservationManager()
	{
		//Last option in array must be exit option
		String[] options = {"Show all reservations.",
							"Reserve a room",
							"Find a reservation",											
							"Cancel a reservation",
							"Clean reservations",
							"Go back"};
		
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
	 * Displays the Reservations Information screen of this interface.
	 */
	private void displayReservationsInformation()
	{
		printHeader("All Reservations");
		if(reservationManager.getNumberOfReservations() > 0)
			print("ID| # of reservations: " + reservationManager.getNumberOfReservations());
		else
			print("  | # of reservations: " + reservationManager.getNumberOfReservations());
		
		for(int i=0; i<reservationManager.getReservationIDcounter(); i++)
		{
			Reservation r = reservationManager.getReservation(i);						
			if(r!=null)
			{
				printSingleLine();
				printReservation(r);
				if(r.isCancelled())
					print("! This reservation was CANCELLED.");
			}
		}		
		printDoubleLine();
	}
	
	/**
	 * Displays the Reserve Room screen of this interface.
	 */
	private void showReserveRoom()
	{	
		boolean guestRegistered = false;
		if(guestRegistration.getNumberOfRegisteredGuests() < 1)
		{
			System.out.println("! Please register a guest first:");
			showAddNewGuest(false);		
			if(guestRegistration.getNumberOfRegisteredGuests() > 0)
				guestRegistered = true;
		}
		else
			guestRegistered = true;
		if(guestRegistered)
		{	
			//Can be substituted with showFindGuests (needs to be "protected" in that case)
			displayGuestsInformation();
			
			System.out.println("> Please enter ID of guest to book a room for: ");
			int guestID = getUserChoice(0, guestRegistration.getIDcounter());
			Guest guest = guestRegistration.getGuest(guestID);
			
			Hotel chosenHotel = null;
			Calendar startDate, endDate = null;
			
			if(guest != null)
			{
				printSingleLine();
				printGuest(guest, true);
				printSingleLine();
				chosenHotel = showChooseHotel();
				if(chosenHotel != null)
				{				
					String roomType = showGetRoomType(chosenHotel);
					if(roomType!=null)
					{
						System.out.println("Today is: " +reservationManager.calendarToString(currentDate));		
					
						startDate = showGetDesiredDate("Arrival");
						endDate = showGetDesiredDate("Departure");
						Reservation newReservation = reservationManager.reserveRoom(guest, chosenHotel, roomType, startDate, endDate);					
						if(newReservation != null)
						{
							printHeader("New Reservation");
							printReservation(newReservation);
							printDoubleLine();
							if(promptInputConfirmation(true))
								System.out.println("! Reservation booked succesfully!");
							else
							{
								reservationManager.cancelReservation(newReservation);
								System.out.println("! Reservation cancelled.");
							}							
						}
						else //TODO Not error if type unavailable, dont book until confirmed (implement & handle multiple reserveRoom return options ) or test if standard Room is auto-booked
							System.out.println("! There was an error booking this reservation.");			
					}
				//	else
						
				}								
			}
			else
				System.out.println("! Error: Guest could not be found.");
		}	
	}

	/**
	 * Displays the Cancel Reservation screen of this interface.
	 * TODO Present search or show all option.
	 */
	private void showCancelReservation()
	{
		if(reservationManager.getNumberOfReservations()>0)
		{
			//Can be substituted with showFindReservation()		
			displayReservationsInformation();
			
			System.out.println("> Please enter ID of reservation to be cancelled: ");
			int reservationID = getUserChoice(0, reservationManager.getIDcounter());
			Reservation reservation = reservationManager.getReservation(reservationID);
			
			if(reservation!=null)
			{
				printHeader("Result");
				printReservation(reservation);
				printDoubleLine();
			//	printReservationInfo(reservation);
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
		else
			System.out.println("! There are currently no reservations registered.");
	}	
	
	/**
	 * Displays the Find Reservation screen of this interface.
	 */
	private void showFindReservation()
	{
		if(reservationManager.getNumberOfReservations() > 0)
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
				{
					printHeader("Result:");
					printReservation(reservation);
					printDoubleLine();
				}
				else
					System.out.println("! Reservation not found.");
			}
		}
		else
			System.out.println("! There are currently no reservations registered.");	
	}
	
	/**
	 * Displays the Clean Reservations (move to archive) screen.
	 */
	private void showCleanReservations()
	{
		ArrayList<Reservation> pastAndCancelledReservations = reservationManager.getPastAndCancelledReservations();
	//	printHeader("Results");
		for(int i=0; i<pastAndCancelledReservations.size();i++)
		{
			if(pastAndCancelledReservations.get(i).isCancelled())
				printHeader("Cancelled Reservation");
			else
				printHeader("Past Reservation");
			
			printReservation(pastAndCancelledReservations.get(i));
			printDoubleLine();
			System.out.println("> Are you sure you want to move this reservation to the archive? (Y/N): ");
			if(!promptInputConfirmation(false))
				pastAndCancelledReservations.remove(i);	
			
		}
	//	printDoubleLine();
	//	System.out.println("! " + pastAndCancelledReservations.size() + " reservations found.");
			
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
	 * Finds a reservation ID based on a given guest ID. If multiple matches, asks for reservation selection.
	 * @param guestID Guest ID to use as search criterium for reservation.
	 * @return Returns a reservation ID as indicated by user input. Returns -1 if no reservation was found, -99 if multiple reservations were found and displayed.
	 */
	private int showFindReservationID(int guestID)
	{
		ArrayList<Reservation> resFound = reservationManager.findReservationID(guestID);
		int resID = -1;
		if(resFound.size() == 1)
			resID = resFound.get(0).getID();
		else if(resFound.size() > 1)
		{
			printHeader("Results");
			for(int i=0; i<resFound.size(); i++)
			{
				if(i!=0)
					printSingleLine();
				printReservation(resFound.get(i));
			//	printReservationInfo(resFound.get(i));
			//	if(promptInputConfirmation(true))
			//	{
			//		resID = resFound.get(i).getID();
			//		i = resFound.size();
			//	}
			}
			printDoubleLine();
			System.out.println("! " + resFound.size() + " matching reservations found.");
			System.out.println("> Enter reservation ID: ");
			while(resID==-1)
				resID = getUserChoice(0, reservationManager.getReservationIDcounter());
		}
		return resID;
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
		if(choice == 9 && options.length <=9 || choice >= options.length)
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
	 * TODO comment block
	 */
	private String showGetRoomType(Hotel hotel)
	{
		System.out.println("\n> What type of room would you like to book?");
		String[] options = new String[hotel.getRoomTypes().length+1];
		for(int i=0; i<hotel.getRoomTypes().length; i++)
		{
			options[i] = hotel.getRoomTypes()[i].concat("\n	Rate: " + hotel.getRateOfRoom(hotel.getRoomTypes()[i]) + " / night.");
		}
		options[options.length-1] = "Cancel";
		printOptions(options, false);
		
		int choice = getUserChoice(1, options.length);
		if((choice == 9 && options.length <= 9) || choice >= options.length)
			return null;
		
		return hotel.getRoomTypes()[choice-1];
	}
	
	/**
	 * Prints information of a given Reservation to the screen.
	 * @param res Reservation whose information is to be printed.
	 */
/*	private void printReservationInfo(Reservation res)
	{
		System.out.println("\n  Result:\n  ***********************************");
		print("Reservation ID: " + res.getID(), "*");
		print("-------------------------- ", "*");
		print(guestRegistration.getGuest(res.getGuestID()).getName(), "*");
		print("Room #" + res.getRoomNumber() + " (" + res.getRoomType() + ")", "*");
		print(reservationManager.calendarToString(res.getStartDate()), "*");
		print("  - " + reservationManager.calendarToString(res.getEndDate()), "*");
		print("Nightly rate: " + res.getNightlyRate(), "*");
		print("Total cost: " + res.getTotalRate(), "*");
		if(res.isCancelled())
			print("! CANCELLED", "*");
		System.out.println("  ***********************************");
	}	
*/	
	/**
	 * Prints Reservation r to the screen as part of a table.
	 * @param r Reservation to be printed.
	 */
	private void printReservation(Reservation r)
	{
		if(r.getID() <10)
			print(" " + r.getID() + "|" + guestRegistration.getGuest(r.getGuestID()).getName() + " (" + r.getGuestID() + ")");
		else
			print(r.getID() + "|" + guestRegistration.getGuest(r.getGuestID()).getName() + " (" + r.getGuestID() + ")");
		print("------------");
		String out2 = " " + r.getHotelName();
		out2 = out2.concat(", Room #" + r.getRoomNumber() + " (" + r.getRoomType() + ")");
		print(out2);
		print(" " + reservationManager.calendarToString(r.getStartDate()));
		print("   - " + reservationManager.calendarToString(r.getEndDate()));
		print(" Nightly rate: " + r.getNightlyRate());
		print(" Total cost: " + r.getTotalRate());		
	}
	
	/**
	 * Checks whether there are currently no reservations booked by guests who were removed from the system. 
	 * Moves any reservations that are booked by a removed guest to the relative archive.
	 * TODO Not in manager?
	 */
	private void checkForRemovedGuests()
	{
		int ctr = 0;
		if(removedGuestIDs.size()>0)
			ctr = removedGuestIDs.get(removedGuestIDs.size()-1);
		for(int i=ctr; i<guestRegistration.getGuestIDcounter(); i++)
			if (guestRegistration.getGuest(i) == null)
				removedGuestIDs.add(i);	
		for(int i=0; i<removedGuestIDs.size(); i++)
		{	
			ArrayList<Reservation> res = reservationManager.findReservationID(removedGuestIDs.get(i));
			reservationManager.moveReservationsToArchive(res);	
		}	
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
		if(split.length==2)
			if(newDate.before(currentDate))
				newDate.set(year+1, month-1, day, 0, 0, 0);
	
		return newDate;
	}
}