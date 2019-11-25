import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
public class Reservation {
	public String name;
	public Date checkinDate;
	public int stayDurationDays;
	public Hotel hotel;	//Hotel that the reservation is under.
	
	public Reservation(){}
	public Reservation(String name,String date,int stay)
	{
		this.name=name;
		setCheckinDate(date);
		this.stayDurationDays=stay;
	}
	SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
	public void setCheckinDate(String input) 	//Makes a Date Object using the format of a string.
	{
		try{
		checkinDate=format.parse(input);
		}catch(ParseException e){System.out.println("Wrong Date Format");}
	}

}
