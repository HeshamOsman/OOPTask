package gameticket_package;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


/**
 * 
 * @author hesham
 * 
 * @GameTicket is main class that holds how the sport reservation system works
 * 
 *
 */
public class GameTicket {
	/**
	 * Main method is the entry point for our application it allows user to choose between different screens
	 * the first is the @OrganizerApp screen and the second on is the @FanApp screen  
	 * it's important for the first time to choose the @OrganizerApp option to add games that fans can use after that. 
	 * 
	 * This method uses polymorphism with @App
	 * @param args
	 */
	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		
		List<Game> games = new ArrayList<>();
		
		boolean exit = false;
		App app = null;
		do {
			System.out.println("1- If you are an organizer press 1");
			System.out.println("2- If you are a fan press 2");
			System.out.println("3- To exit press any other number");
			switch(input.nextInt()){
				case 1:
					app = new OrganizerApp(games,input).activateScreen();
					exit = app.isExitSelected();
					break;
				case 2:
					app = new FanApp(games,input).activateScreen();
					exit = app.isExitSelected();
				    break;
				default:
					exit = true;
			}
			if(exit) System.out.println("GoodBye :-)");
		}while(!exit);
		
	}

}

/**
 * @App
 * An abstract class that holds the common data and method for @OrganizerApp and @FanApp
 * it contains all the games that added by an organizer and also the exit option.
 * 
 * this class represent inheritance
 * @author hesham
 *
 */
abstract class App{
	
	private List<Game> games;
	private boolean exit;
	protected Scanner input;
	/**
	 * Constructor 
	 * @param games
	 * @param input
	 */
	public App(List<Game> games,Scanner input) {
		this.games = games;
		this.input=input;
	}
	
	/**
	 * Abstract method to be implemented by subclasses @OrganizerApp and @FanApp to view which options the organizer or fan has.
	 * @return
	 * @throws ParseException
	 */
	public abstract App activateScreen()throws ParseException;
	
	/**
	 * get if the user want to exit from the app or not
	 * @return boolean 
	 */
	public boolean isExitSelected() {
		return exit;
	}
	
	/**
	 * set the exit value if the user want to exit from the app or not
	 * @param exit
	 */
	protected void setExit(boolean exit) {
		this.exit = exit;
	}
	/**
	 * get list of games that added to the app so far
	 * @return
	 */
	protected List<Game> getGames(){
		return games;
	}
}
/**
 * Child class of @App represents the options the organizer has.
 * 
 * this class uses method overloading
 * @author hesham
 *
 */
class OrganizerApp extends App{
	
	/**
	 * Constructor
	 * @param games
	 * @param input
	 */
	public OrganizerApp(List<Game> games,Scanner input) {
		super(games,input);
	}
	
	/**
	 * Shows all the options an organizer has.
	 * this method uses override to provide polymerphism
	 * 
	 */
	@Override
	public OrganizerApp activateScreen() {
		System.out.println("1- To create game press 1");
		System.out.println("2- To see all games press 2");
		System.out.println("3- To get info about a game press 3");
		System.out.println("4- To exit press any other number");
		switch(input.nextInt()) {
		case 1:
			System.out.println("first team name:");
			String teamOneName = input.next();
			System.out.println("second team name:");
			String teamTwoName = input.next();
			System.out.println("location");
			String location = input.next();
            System.out.println("date dd-mm-yyyy");
            String date = input.next();
            System.out.println("number of seats for category 1");
            int first = input.nextInt();
            System.out.println("number of seats for category 2");
            int second = input.nextInt();
            System.out.println("number of seats for category 3");
            int third = input.nextInt();
            try {
            	getGames().add(new Game(teamOneName, teamTwoName, location,new SimpleDateFormat("dd-MM-yyyy").parse(date)
                		, first, second, third));
            	System.out.println("Created");
            }catch (ParseException  e) {
            	System.out.println("Error creating game: not valid date");
			}
            
            
            System.out.println("To continue press 1 to exit press any other number");
            int e = input.nextInt();
            setExit(e==1?false:true);
			break;
		case 2:	
			for(Game g :getGames())
				System.out.println(g);
			System.out.println("To continue press 1 to exit press any other number");
            int e1 = input.nextInt();
            setExit(e1==1?false:true);
		    break;
		case 3:	
			System.out.println("Enter game code: ");
			Game g = null;
			try {
				 g = GameHelper.getGameByCode(getGames(), input.nextInt());
			} catch (GameNotFoundException ge) {
				System.out.println(ge.getMessage());
				return this;
			}
			System.out.println("1- to see number of sold tickets press 1");
			System.out.println("2- to see bets press 2");
			switch (input.nextInt()) {
			case 1:
				System.out.println(g.getTickets().size());
				break;
			case 2:
				for(String b:g.getBets()) 
				System.out.println(b);
				break;
			default:
				break;
			}
			System.out.println("To continue press 1 to exit press any other number");
            int e2 = input.nextInt();
            setExit(e2==1?false:true);
		    break;
		default:    
			setExit(true);
		}
		return this;
	}
}
/**
 * Child class of @App represents the options the fan has.
 * 
 * this class uses method overloading
 * @author hesham
 *
 */
class FanApp extends App{
	/**
	 * Constructor
	 * @param games
	 * @param input
	 */
	public FanApp(List<Game> games,Scanner input) {
		super(games,input);
	}
	/**
	 * Shows all the options an fan has.
	 * this method uses override to provide polymerphism
	 * 
	 */
	@Override
	public FanApp activateScreen() {
		
		if(getGames().isEmpty()) {
			System.out.println("Sorry there is no games right now");
			return this;
		}
			
		for(Game g :getGames())
			System.out.println(g);
		System.out.println("Enter game code: ");
		Game g = null;
		try {
			 g = GameHelper.getGameByCode(getGames(), input.nextInt());
		} catch (GameNotFoundException e) {
			System.out.println(e.getMessage());
			return this;
		}
		
		System.out.println("1- To see availiable seats press 1");
		System.out.println("2- To reserve a ticket press 2");
		System.out.println("3- To cancel reservation press 3");
		System.out.println("4- To make a bet press 4");
		System.out.println("5- To change seat press 5");
		System.out.println("6- To exit press any other number");
		switch(input.nextInt()) {
		case 1:
			try {
				for(Seat s:g.getAvailiableSeats())
					System.out.println(s);
			}catch(NoAvailiableSeatsException e) {
				System.out.println("Sorry, No available Seats right now");
			}
			
			System.out.println("To continue press 1 to exit press any other number");
            int e1 = input.nextInt();
            setExit(e1==1?false:true);
		    break;
		case 2:
			System.out.println("Enter seat category");
            int sc = input.nextInt();
			System.out.println("Enter seat number");
            int sn = input.nextInt();
            try {
            	Ticket t = g.reserveTicket(sn, sc);
                System.out.println("Done, your ticket info is: "+t);
            }catch (SeatNotAvailiableException e) {
            	System.out.println("Sorry, this seat is not available");
			}
            
			System.out.println("To continue press 1 to exit press any other number");
            int e2 = input.nextInt();
            setExit(e2==1?false:true);
		    break;
		case 3:
			System.out.println("Enter ticket number");
            int tn = input.nextInt();
            try {
            	g.cancelTicketReservation(tn);
                System.out.println("Cancelled");
            }catch (TicketException te) {
				System.out.println(te.getMessage());
			}
            
			System.out.println("To continue press 1 to exit press any other number");
            int e3 = input.nextInt();
            setExit(e3==1?false:true);
		    break;
		case 4:
			System.out.println("Enter expectation for "+g.getTeamOneName());
            int t1 = input.nextInt();
            System.out.println("Enter expectation for "+g.getTeamTwoName());
            int t2 = input.nextInt();
            GameHelper.addBet(getGames(), g.getCode(), t1, t2);
            System.out.println("Done.");
			System.out.println("To continue press 1 to exit press any other number");
            int e4 = input.nextInt();
            setExit(e4==1?false:true);
		    break;
		case 5:
			System.out.println("Enter ticket number");
            int tn1 = input.nextInt();
			System.out.println("Enter new seat category");
            int sc1 = input.nextInt();
			System.out.println("Enter new seat number");
            int sn1 = input.nextInt();
            try {
            	g.changeTicketReservation(tn1, sn1, sc1);
                System.out.println("Done, your new ticket info is: "+g.getTicket(tn1));
            }catch (TicketException te) {
				System.out.println(te.getMessage());
			}catch (SeatNotAvailiableException e) {
				System.out.println(e.getMessage());
			}
            
			System.out.println("To continue press 1 to exit press any other number");
            int e5 = input.nextInt();
            setExit(e5==1?false:true);
		    break;    
		default:
			setExit(true);
		}
		return this;
	}
}
/**
 * This class represent game information entered by an organizer
 * @author hesham
 *
 */
class Game{
	private int code;
	private String location;
	private Date date;
	private List<Seat> seats;
	private List<Ticket> tickets;
	private List<String> bets;
	private String teamOneName;
	private String teamTwoName;
	/**
	 * game constructor holds all the data to create a game and create game code randomly
	 * @param teamOneName
	 * @param teamTwoName
	 * @param location
	 * @param date
	 * @param category1SeatsNumber
	 * @param category2SeatsNumber
	 * @param category3SeatsNumber
	 */
	public Game(String teamOneName,String teamTwoName,String location,Date date,int category1SeatsNumber,
			int category2SeatsNumber,int category3SeatsNumber) {
		this.code = new Random().nextInt(100)+100;
		this.teamOneName = teamOneName;
		this.teamTwoName = teamTwoName;
		this.location = location;
		this.date = date;
		this.tickets = new ArrayList<>();
		this.bets = new ArrayList<>();
		this.seats = new ArrayList<>();
		for(int i = 0;i < category1SeatsNumber;i++)
			seats.add(new Category1Seat(i));
		for(int i = 0;i < category2SeatsNumber;i++)
			seats.add(new Category2Seat(i));
		for(int i = 0;i < category3SeatsNumber;i++)
			seats.add(new Category3Seat(i));
	}
	/**
	 * Method to get all available seats for a fan
	 * @return
	 */
	public List<Seat> getAvailiableSeats(){
		List<Seat> availiableSeats = new ArrayList<>();
		for(Seat s:seats) {
			if(!s.isReserved())
			   availiableSeats.add(s);
		}
		
		if(availiableSeats.isEmpty())
			throw new NoAvailiableSeatsException();
		else 
			return availiableSeats;
	}
	
	/**
	 * method to get all purchased tickets 
	 * @return
	 */
	public List<Ticket> getTickets(){
		return tickets;
	}
	/**
	 * method to get all bets tickets 
	 * @return
	 */
	public List<String> getBets(){
		return bets;
	}
	
	/**
	 * public method to get game code 
	 * @return
	 */
	public int getCode(){
		return code;
	}
	
	/**
	 * public method to get Teams Names
	 * @return
	 */
	public String getTeamsNames() {
		return teamOneName+" vs "+teamTwoName;
	}
	
	/**
	 * public method to get Team One Name 
	 * @return
	 */
	public String getTeamOneName() {
		return teamOneName;
	}
	
	/**
	 * public method to get Team two Name 
	 * @return
	 */
	public String getTeamTwoName() {
		return teamTwoName;
	}
	
	/**
	 * public method to reserve a ticket 
	 * throws an exception if the seat is not found or not available
	 * @throws @SeatNotAvailiableException
	 * @param seatNumber
	 * @param seatCategory
	 * @return
	 */
	public Ticket reserveTicket(int seatNumber,int seatCategory) {
		Seat selectedSeat = null;
		for(Seat s:seats) {
			if(s.getNumber()==seatNumber&&s.getCategory()==seatCategory&&!s.isReserved()) {
				selectedSeat = s;
				break;
			}
				
		}
		
		if(selectedSeat == null)
			throw new SeatNotAvailiableException();
		
		selectedSeat.setReserved(true);
		
		Ticket t = new Ticket(selectedSeat);
		tickets.add(t);
		return t;
	}
	
	/**
	 * public method to cancel a ticket
	 * @throws @TicketCanNotCanelledException if there is less that three days to the game date
	 * @throws @TicketNotExistException if the ticket number is not right
	 * @param ticketNumber
	 * @return
	 */
	public boolean cancelTicketReservation(int ticketNumber) {
		Ticket selectedTicket = getTicket(ticketNumber);
		
		long diff = TimeUnit.DAYS.convert(date.getTime()-new Date().getTime(), TimeUnit.MILLISECONDS);
		if(diff<=3)
			throw new TicketCanNotCanelledException();
		
		selectedTicket.getSeat().setReserved(false);
		tickets.remove(selectedTicket);
		
		return true;
	}
	
	/**
	 * public method to get ticket by its number
	 * @throws @TicketNotExistException if the ticket number is not right
	 * @param ticketNumber
	 * @return
	 */
	public Ticket getTicket(int ticketNumber) {
		Ticket selectedTicket = null;
		for(Ticket t :tickets) {
			if(t.getTicketNumber()==ticketNumber) {
				selectedTicket = t;
				break;
			}
				
		}
		
		if(selectedTicket == null)
			throw new TicketNotExistException();
		return selectedTicket;
	}
	/**
	 * change and upgrade seat
	 * @throws @TicketNotExistException if the ticket number is not right
	 * @throws @SeatNotAvailiableException if the new seat is not found or not available
	 * @param ticketNumber
	 * @param seatNumber
	 * @param seatCategory
	 * @return
	 */
	public boolean changeTicketReservation(int ticketNumber,int seatNumber,int seatCategory) {
		Ticket selectedTicket = null;
		for(Ticket t :tickets) {
			if(t.getTicketNumber()==ticketNumber) {
				selectedTicket = t;
				break;
			}
				
		}
		
		if(selectedTicket == null)
			throw new TicketNotExistException();
		
		Seat selectedSeat = null;
		for(Seat s:seats) {
			if(s.getNumber()==seatNumber&&s.getCategory()==seatCategory&&!s.isReserved()) {
				selectedSeat = s;
				break;
			}
				
		}
		
		if(selectedSeat == null)
			throw new SeatNotAvailiableException();
		
		selectedTicket.getSeat().setReserved(false);
		selectedSeat.setReserved(true);
		selectedTicket.setSeat(selectedSeat);
		
		return true;
	}
	/**
	 * override toString method to print an object
	 */
	@Override
	public String toString() {
		return "Code: "+code+", "+teamOneName+" vs "+teamTwoName+", Location: "+location+", Date: "+date;
	}
	
	
}

/**
 * Class that has some static methods to get games data
 * @author hesham
 *
 */
class GameHelper{
	/**
	 * static method to get game by code
	 * @param games
	 * @param gameCode
	 * @return
	 */
	public static Game getGameByCode(List<Game> games,int gameCode) {
		Game sg = null;
		for(Game game:games) {
			if(game.getCode()==gameCode) {
				sg = game;
				break;
			}
		}
		
		if(sg==null)
			throw new GameNotFoundException();
		return sg;
	}
	/**
	 * static method to get game bets
	 * @param games
	 * @param gameCode
	 * @param teamOneResult
	 * @param teamTwoResult
	 */
	public static void addBet(List<Game> games,int gameCode,int teamOneResult,int teamTwoResult) {
		Game sg = getGameByCode(games, gameCode);
		
		sg.getBets().add(teamOneResult+"/"+teamTwoResult);
	}
}
/**
 * Custom exception for not found game
 * @author hesham
 *
 */
class GameNotFoundException extends RuntimeException{

	public GameNotFoundException() {
		super("No game exist with that code");
	}

}
/**
 * abstract parent class for all seats exceptions
 * @author hesham
 *
 */
abstract class SeatException extends RuntimeException{
	public SeatException(String message) {
		super(message);
	}
}
/**
 * abstract parent class for all tickets exceptions
 * @author hesham
 *
 */
abstract class TicketException extends RuntimeException{
	public TicketException(String message) {
		super(message);
	}
}
/**
 * child class of @SeatException if all seats all reserved
 * @author hesham
 *
 */
class NoAvailiableSeatsException extends SeatException{
	public NoAvailiableSeatsException() {
		super("No Seats Availiable");
	}

}

/**
 * child class of @SeatException if the seat is not available
 * @author hesham
 *
 */
class SeatNotAvailiableException extends SeatException{
	public SeatNotAvailiableException() {
		super("Seat not availiable");
	}

}

/**
 *  child class of @TicketException if the ticket is not found
 * @author hesham
 *
 */
class TicketNotExistException extends TicketException{
	public TicketNotExistException() {
		super("Ticket not found");
	}

}
/**
 *  child class of @TicketException if you can't cancel your ticket
 * @author hesham
 *
 */
class TicketCanNotCanelledException extends TicketException{
	public TicketCanNotCanelledException() {
		super("You can only cancel ticket until three days before the game date");
	}

}
/**
 * abastract class for all seats categories
 * @author hesham
 *
 */
abstract class Seat{
	private int number;
	private int price;
	private int category;
	private boolean reserved;
	/**
	 * Constructor for creating a seat
	 * @param number
	 * @param price
	 * @param category
	 * @param reserved
	 */
	public Seat(int number, int price, int category, boolean reserved) {
		this.number = number;
		this.price = price;
		this.category = category;
		this.reserved = reserved;
	}
	/**
	 * get seat number
	 * @return
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * get seat price
	 * @return
	 */
	public int getPrice() {
		return price;
	}
	/**
	 * get category 
	 * @return
	 */
	public int getCategory() {
		return category;
	}
	/**
	 * check if the seat is reserved
	 * @return
	 */
	public boolean isReserved() {
		return reserved;
	}
	
	/**
	 * set seat status if reserved or not
	 * @param reserved
	 */
	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}
	
	/**
	 * print seat info using override
	 */
	@Override
	public String toString() {
		return "Seat number: "+number+", Seat category: "+category+", price: "+price;
	}
	
	
	
}
/**
 * Child class of @Seat for first category 
 * @author hesham
 *
 */
class Category1Seat extends Seat{
    /**
     * add price for category one seats and numbers of seats too
     * @param number
     */
	public Category1Seat(int number) {
		super(number, 50, 1, false);
	}
	
}
/**
 * Child class of @Seat for second category 
 * @author hesham
 *
 */
class Category2Seat extends Seat{
	/**
     * add price for category two seats and numbers of seats too
     * @param number
     */
	public Category2Seat(int number) {
		super(number, 20, 2, false);
	}
}
/**
 * Child class of @Seat for third category 
 * @author hesham
 *
 */
class Category3Seat extends Seat{
	/**
     * add price for category three seats and numbers of seats too
     * @param number
     */
	public Category3Seat(int number) {
		super(number, 10, 3, false);
	}
	
}
/**
 * class that holds ticket data
 * @author hesham
 *
 */
class Ticket{
	private int ticketNumber;
	private Seat seat;
	/**
	 * create ticket using a certain seat and create ticket number randomly
	 * @param seat
	 */
	public Ticket(Seat seat) {
		this.ticketNumber = new Random().nextInt(1000)+1000;
		this.seat = seat;
	}
	/**
	 * get ticket number
	 * @return
	 */
	public int getTicketNumber() {
		return ticketNumber;
	}
	/**
	 * get seat object
	 * @return
	 */
	public Seat getSeat() {
		return seat;
	}
    /** 
     * set seat
     * @param s
     */
	public void setSeat(Seat s) {
		this.seat=s;
	}

    /**
     * print ticket
     */
	@Override
	public String toString() {
		return "Ticket number: "+ticketNumber+", Price: "+seat.getPrice()+", Seat number: "+seat.getNumber()+", Seat category: "+seat.getCategory();
	}
	
}
