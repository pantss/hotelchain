package hotelchain;

import java.util.Calendar;

public class ReservationManagerTextInterface extends GuestRegistrationTextInterface
{
	private boolean exitRequested;
	private Calendar currentDate;
	
	public ReservationManagerTextInterface(HotelChain _chain)
	{
	   super(_chain, false);
	   currentDate = chain.getReservationManager().getCurrentDate();
		while(!exitRequested)
			showReservationManager();
	}	
	
	private void showReservationManager()
	{
		//Last option in array must be exit option
		String[] options = {"General information about \n	reservations.",
											"Reserve a Room",
											"Find a Reservation",											
											"Cancel a Reservation",
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
		default: exitRequested = true;
		}
	}
	
	private void displayReservationsInformation()
	{
		printHeader("All Reservations");
		System.out.println(" | ID | # of reservations: " + chain.getReservationManager().getNumberOfReservations());
		
		for(int i=0; i<chain.getReservationManager().getReservationIDcounter(); i++)
		{
			Reservation r = chain.getReservationManager().getReservation(i);
			if(r!=null)
			{
				printSingleLine();
				System.out.println(" | " + r.getID() + ": " + r.getGuest().getName() + "(" + r.getGuest().getID() + ")"
						+ "\n | 	at " + r.getHotel().getName() +  ", Room #" + r.getRoom().getRoomNumber()
						+ "\n | 	 " + chain.getReservationManager().calendarToString(r.getStartDate()) 
							+ "\n |  	 - " + chain.getReservationManager().calendarToString(r.getEndDate()));
				if(r.isCancelled())
					System.out.println(" | ! This reservation was CANCELLED.");
			}
		}		
		printDoubleLine();
	}
	
	private void showReserveRoom()
	{		
		//TODO choose between choose all or find guest
		displayGuestsInformation();
		
		System.out.println("> Please enter ID of guest to book a room for: ");
		int guestID = getUserChoice(0, chain.getGuestRegistration().getIDcounter());
		Guest guest = chain.getGuestRegistration().getGuest(guestID);
		
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
						
						System.out.println("Today is: " +chain.getReservationManager().calendarToString(currentDate));		
						startDate = showGetDesiredDate("arrival");
						endDate = showGetDesiredDate("departure");
						boolean bridalDesired = showGetBridalDesired(chosenHotel, startDate, endDate);			
						
						Reservation newReservation = chain.getReservationManager().reserveRoom(guest, chosenHotel, startDate, endDate, bridalDesired);
					
						if(newReservation != null)
						{
							printReservation(newReservation);
							
							if(promptInputConfirmation(true))
								System.out.println("! Reservation booked succesfully!");
							else
							{
									chain.getReservationManager().cancelReservation(newReservation);
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
	
	private Hotel showChooseHotel()
	{
		Hotel[] hotels = chain.getHotels();
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
	
	private Calendar showGetDesiredDate(String time)
	{
	//	currentDate = chain.getReservationManager().getCurrentDate();		
		System.out.println("> Please enter the desired date of " + time + " (\"Month dd yyyy\" or \"Month dd\")");		
		String input = getUserInput();
		Calendar desiredDate= convertInputToDate(input);
		
		return desiredDate;
	}
	
	private boolean showGetBridalDesired(Hotel hotel, Calendar startDate, Calendar endDate)
	{
		// TODO FIX DOUBLE booking
			System.out.println("> Would you like to book the bridal suite?");
			 return promptInputConfirmation(false);
		
	}
	
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
	
	private void printReservation(Reservation res)
	{
		System.out.println("	 Result:");
		System.out.println("	**********************");
		System.out.println("	* Reservation ID: " + res.getID()
										+ "\n	* Guest: " + res.getGuest().getName()
										+ "\n	* Hotel: " + res.getHotel().getName());
		if(res.getRoom().getClass().equals(BridalSuite.class))
			System.out.println("	* Room: Bridal Suite");
		else
			System.out.println("	* Room #" + res.getRoom().getRoomNumber());
		System.out.println("	*  " + chain.getReservationManager().calendarToString(res.getStartDate())
										+ "\n	*   - " + chain.getReservationManager().calendarToString(res.getEndDate()));
		System.out.println("	**********************");
	}
	
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
			reservationID = chain.getReservationManager().findReservationID(nameEntered);
			break;
		case 2:
			System.out.println("> Enter reservation ID: ");
			reservationID = getUserChoice(0, chain.getReservationManager().getIDcounter());
			break;
		default:
			cancel = true;
		}
		if(!cancel)
		{
			Reservation reservation = chain.getReservationManager().getReservation(reservationID);
			
			if(reservation != null)
				printReservation(reservation);
			else
				System.out.println("! Reservation not found.");
		}
	}
	
	private void showCancelReservation()
	{
		//TODO choose between choose all or find reservation
		displayReservationsInformation();
		
		System.out.println("> Please enter ID of reservation to be cancelled: ");
		int reservationID = getUserChoice(0, chain.getReservationManager().getIDcounter());
		Reservation reservation = chain.getReservationManager().getReservation(reservationID);
		
		if(reservation!=null)
		{
			printReservation(reservation);
			System.out.println("> Are you sure you want to cancel this reservation?");
			boolean correct = promptInputConfirmation(false);
			
			if(correct)
			{
				chain.getReservationManager().cancelReservation(reservation);
				System.out.println("! Reservation cancelled.");
			}
			else
				System.out.println("! No changes were made.");
		}
		else
			System.out.println("! Error: Reservation could not be found.");
	showReservationManager();
	}
}
