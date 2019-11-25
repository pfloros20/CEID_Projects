import java.util.ArrayList;
public class Hotel {
	public int id,stars,numberofRooms;
	public String name;
	public ArrayList<Reservation> reservations=new ArrayList<Reservation>();
	public Hotel(){}
	public Hotel(int id,String name,int stars ,int numberofRooms)
	{
		this.id=id;
		this.name=name;
		this.stars=stars;
		this.numberofRooms=numberofRooms;
	}

}
