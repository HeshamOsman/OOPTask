package gameticket_package;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;



public class GameTicket {
	
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
abstract class App{
	
	private List<Game> games;
	private boolean exit;
	protected Scanner input;
	
	public App(List<Game> games,Scanner input) {
		this.games = games;
		this.input=input;
	}
	
	public abstract App activateScreen()throws ParseException;
	
	public boolean isExitSelected() {
		return exit;
	}
	protected void setExit(boolean exit) {
		this.exit = exit;
	}
	protected List<Game> getGames(){
		return games;
	}
}
class OrganizerApp extends App{
	
	public OrganizerApp(List<Game> games,Scanner input) {
		super(games,input);
	}
	
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

class FanApp extends App{
	public FanApp(List<Game> games,Scanner input) {
		super(games,input);
	}
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

class Game{
	private int code;
	private String location;
	private Date date;
	private List<Seat> seats;
	private List<Ticket> tickets;
	private List<String> bets;
	private String teamOneName;
	private String teamTwoName;
	
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
	
	public List<Ticket> getTickets(){
		return tickets;
	}
	
	public List<String> getBets(){
		return bets;
	}
	
	public int getCode(){
		return code;
	}
	
	public String getTeamsNames() {
		return teamOneName+" vs "+teamTwoName;
	}
	
	public String getTeamOneName() {
		return teamOneName;
	}
	
	public String getTeamTwoName() {
		return teamTwoName;
	}
	
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
	
	public boolean cancelTicketReservation(int ticketNumber) {
		Ticket selectedTicket = getTicket(ticketNumber);
		
		long diff = TimeUnit.DAYS.convert(date.getTime()-new Date().getTime(), TimeUnit.MILLISECONDS);
		if(diff<=3)
			throw new TicketCanNotCanelledException();
		
		selectedTicket.getSeat().setReserved(false);
		tickets.remove(selectedTicket);
		
		return true;
	}
	
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
	
	@Override
	public String toString() {
		return "Code: "+code+", "+teamOneName+" vs "+teamTwoName+", Location: "+location+", Date: "+date;
	}
	
	
}


class GameHelper{
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
	public static void addBet(List<Game> games,int gameCode,int teamOneResult,int teamTwoResult) {
		Game sg = getGameByCode(games, gameCode);
		
		sg.getBets().add(teamOneResult+"/"+teamTwoResult);
	}
}

class GameNotFoundException extends RuntimeException{

	public GameNotFoundException() {
		super("No game exist with that code");
	}

}

abstract class SeatException extends RuntimeException{
	public SeatException(String message) {
		super(message);
	}
}

abstract class TicketException extends RuntimeException{
	public TicketException(String message) {
		super(message);
	}
}

class NoAvailiableSeatsException extends SeatException{
	public NoAvailiableSeatsException() {
		super("No Seats Availiable");
	}

}

class SeatNotAvailiableException extends SeatException{
	public SeatNotAvailiableException() {
		super("Seat not availiable");
	}

}

class TicketNotExistException extends TicketException{
	public TicketNotExistException() {
		super("Ticket not found");
	}

}

class TicketCanNotCanelledException extends TicketException{
	public TicketCanNotCanelledException() {
		super("You can only cancel ticket until three days before the game date");
	}

}

abstract class Seat{
	private int number;
	private int price;
	private int category;
	private boolean reserved;
	
	public Seat(int number, int price, int category, boolean reserved) {
		this.number = number;
		this.price = price;
		this.category = category;
		this.reserved = reserved;
	}
	public int getNumber() {
		return number;
	}
	public int getPrice() {
		return price;
	}
	public int getCategory() {
		return category;
	}
	public boolean isReserved() {
		return reserved;
	}
	
	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}
	
	@Override
	public String toString() {
		return "Seat number: "+number+", Seat category: "+category+", price: "+price;
	}
	
	
	
}
class Category1Seat extends Seat{

	public Category1Seat(int number) {
		super(number, 50, 1, false);
	}
	
}

class Category2Seat extends Seat{
	public Category2Seat(int number) {
		super(number, 20, 2, false);
	}
}

class Category3Seat extends Seat{
	public Category3Seat(int number) {
		super(number, 10, 3, false);
	}
	
}

class Ticket{
	private int ticketNumber;
	private Seat seat;
	public Ticket(Seat seat) {
		this.ticketNumber = new Random().nextInt(1000)+1000;
		this.seat = seat;
	}
	public int getTicketNumber() {
		return ticketNumber;
	}
	public Seat getSeat() {
		return seat;
	}
    
	public void setSeat(Seat s) {
		this.seat=s;
	}


	@Override
	public String toString() {
		return "Ticket number: "+ticketNumber+", Price: "+seat.getPrice()+", Seat number: "+seat.getNumber()+", Seat category: "+seat.getCategory();
	}
	
}
