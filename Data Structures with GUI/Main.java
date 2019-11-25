import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Collections;
import java.util.Scanner;
import DataStructures.AVLTree;
import DataStructures.DigitalTrie;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class Main {
	static AVLTree<Hotel> avl=new AVLTree<Hotel>();
	static DigitalTrie<Reservation> dt=new DigitalTrie<Reservation>("qwertyuiopasdfghjkl'zxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM");
	static ArrayList<Hotel> list=new ArrayList<Hotel>();
	public static void main(String args[])

	{
				Menu gui=new Menu();

	}

	public static void LoadHotelsnReservations(String source) throws FileNotFoundException,IOException
	{

		BufferedReader bfile=new BufferedReader(new FileReader(source));
		Scanner scanner = new Scanner(bfile.readLine());
		Scanner file=scanner.useDelimiter("\\s*;\\s*");			//Use ";" as delimiter when inputing a file.
		int numberofHotels;										//Get number of hotels.
		numberofHotels=file.nextInt();
		for(int i=0;i<=numberofHotels-1;i++)
		{
			file=new Scanner(bfile.readLine());					//Read Line and add Hotel Object to the list with the appropriate data from file.
		    file.useDelimiter("\\s*;\\s*");
			list.add(new Hotel(file.nextInt(),file.next(),file.nextInt(),file.nextInt()));

			int j=0;
			while(file.hasNext())
			{													//Read and add reservations to the Hotel Object accordingly.
				list.get(i).reservations.add(new Reservation());
				list.get(i).reservations.get(j).name=file.next();
				list.get(i).reservations.get(j).setCheckinDate(file.next());
				list.get(i).reservations.get(j).stayDurationDays=file.nextInt();
				list.get(i).reservations.get(j).hotel=list.get(i);
				dt.insert(list.get(i).reservations.get(j),list.get(i).reservations.get(j).name);
				j+=1;
			}
			avl.insert(list.get(i),list.get(i).id);							//Insert the Hotel Object in the AVL Tree.
		}
		scanner.close();
		bfile.close();
	}

	public static void SaveHotelsnReservations(String source) throws IOException
	{
		BufferedWriter file=new BufferedWriter(new FileWriter(source));
		file.write(list.size()+";");
		for(int i=0;i<=list.size()-1;i++)
		{//Write the data in the ArrayList to file accordingly getting the same type of file and file structure as input.
			//After each element the delimiter is written.
			file.write("\n");
			file.write(list.get(i).id+";");
			file.write(list.get(i).name+";");
			file.write(list.get(i).stars+";");
			file.write(list.get(i).numberofRooms+";");

			for(int j=0;j<=list.get(i).reservations.size()-1;j++)
			{//Write Reservations to file.
				file.write(list.get(i).reservations.get(j).name+";");
				file.write(list.get(i).reservations.get(j).format.format(list.get(i).reservations.get(j).checkinDate)+";");
				file.write(list.get(i).reservations.get(j).stayDurationDays+";");

			}
		}
			file.close();
	}

	public static void AddaHotel()
	{
		list.add(new Hotel());						//Create a Hotel Object get input user data in the Object.
		String in=JOptionPane.showInputDialog(null,"Type Hotel ID:");
		list.get(list.size()-1).id=Integer.parseInt(in);
		in=JOptionPane.showInputDialog(null,"Type Hotel Name:");
		list.get(list.size()-1).name=in;
		in=JOptionPane.showInputDialog(null,"Type Hotel Stars:");
		list.get(list.size()-1).stars=Integer.parseInt(in);
		in=JOptionPane.showInputDialog(null,"Type Hotel Number of Rooms:");
		list.get(list.size()-1).numberofRooms=Integer.parseInt(in);
		for(int i=0;true;i++)
			{in=JOptionPane.showInputDialog(null,"Type 0 to Stop Typing Reservation Data or any other number to continue: ");
			if(Integer.parseInt(in)==0)
				break;
			list.get(list.size()-1).reservations.add(new Reservation());
			in=JOptionPane.showInputDialog(null,"Type Reservation Name: ");
			list.get(list.size()-1).reservations.get(i).name=in;
			in=JOptionPane.showInputDialog(null,"Type Reservation Checkin Date:");
			list.get(list.size()-1).reservations.get(i).setCheckinDate(in);
			in=JOptionPane.showInputDialog(null,"Type Stay Duration:");
			list.get(list.size()-1).reservations.get(i).stayDurationDays=Integer.parseInt(in);
			list.get(list.size()-1).reservations.get(i).hotel=list.get(list.size()-1);
			dt.insert(list.get(list.size()-1).reservations.get(i),list.get(list.size()-1).reservations.get(i).name);
		}
		avl.insert(list.get(list.size()-1),list.get(list.size()-1).id);	//Insert Object in AVL Tree.
	}

	public static Hotel SearchById(int key)
	{//Search items in list one by one until key is found or list has been completely searched.
		for(int i=0;i<=list.size()-1;i++)
			if(list.get(i).id==key)
				{

						return list.get(i);//Returns Object with correct id or null.
				}

		return null;

	}

	public static void SearchBySurname(String key,int mode)
	{//Search items in list one by one until key is found or list has been completely searched.
		String message="Not Found!";

		JFrame frame=new JFrame("Linear Name Search.");
		JPanel panel=new JPanel();
		ArrayList<String> messages=new ArrayList<String>();

		for(int i=0;i<=list.size()-1;i++)
			for(int j=0;j<=list.get(i).reservations.size()-1;j++)
				if(list.get(i).reservations.get(j).name.equals(key))
					{

							message="In Hotel : "+list.get(i).name
								+"  "+"Reservation Date: "+list.get(i).reservations.get(j).format.format(list.get(i).reservations.get(j).checkinDate)
								+"  "+"Reservation Duration: "+list.get(i).reservations.get(j).stayDurationDays;
							messages.add(message);

					}
					if(mode==1)
						if(!messages.isEmpty()){
							String[] messagearray=new String[messages.size()];
							for(int i=0;i<messages.size();i++)
								messagearray[i]=messages.get(i);
								JList<String> messagelist=new JList<String>(messagearray);
								panel.add(messagelist);
								frame.add(panel);
								frame.pack();
								frame.setVisible(true);
							}else
								JOptionPane.showMessageDialog(null,"Not Found!");
	}

	public static void showMenu(int option)
	{
		if(option==0)
		{
			System.out.println("\nBasic Menu: ");
			System.out.println("1.Load Hotels and Reservations from file. ");
			System.out.println("2.Save Hotels and Reservations to file. ");
			System.out.println("3.Add a Hotel. ");
			System.out.println("4.Search and Display a Hotel by id. ");
			System.out.println("5.Display Reservations by surname search. ");
			System.out.println("6.Test Searches. ");
			System.out.println("7.Exit. ");
			System.out.print("\nPress a Number to Choose Function: ...");
		}else if(option==4)
		{
			System.out.println("\n4.Search and Display a Hotel by id. ");
			System.out.println("1.Linear Search. ");
			System.out.println("2.Binary Search. ");
			System.out.println("3.Interpolation Search. ");
			System.out.println("4.AVL Tree Search. ");
			System.out.println("5.Back. ");
			System.out.print("\nPress a Number to Choose Function: ...");
		}else if(option==5){
			System.out.println("\n5.Search and Display a Reservation by Name. ");
			System.out.println("1.Linear Search. ");
			System.out.println("2.Digital Trie Search. ");
			System.out.println("3.Back. ");
			System.out.print("\nPress a Number to Choose Function: ...");
		}

	}

	public static void HeapSort()
	{
		int l=(list.size()-1)/2+1;		//l is the index in which the Heap properties are correct.
		int r=list.size()-1;
		int k,j;
		Hotel s;
		while(r>=1)
		{//Constructing the Heap
			if(l>0)
			{
				l-=1;
				j=l;
			}else
			{//Swapping to reestablish the Heap properties.
				Collections.swap(list,0,r);
				r-=1;
				j=0;
			}
			s=list.get(j);
			label:
			while(2*j<=r)
			{
				k=2*j;
				if(k<r&&list.get(k).id<list.get(k+1).id)
					k+=1;
				if(s.id<list.get(k).id)
				{
					list.set(j,list.get(k));
					j=k;
				}else
					break label;
			}

			list.set(j,s);
		}
	}

	public static Hotel BinarySearch(int key,int left,int right)
	{
		int middle=(right+left)/2; //middle is the next index thats going to be searched.

		if(key==list.get(middle).id)//If it is found print message and return Hotel.
			return list.get(middle);
		else if(right<=left)//If right<=left the item is not on the list
			return null;
		else if(key>list.get(middle).id)
		{
			middle+=1;
			return BinarySearch(key,middle,right);
		}
		else if(key<list.get(middle).id)
		{
			middle-=1;
			return BinarySearch(key,left,middle);
		}
		return null;

	}

	public static Hotel InterpolationSearch(int key,int left,int right)
	{
		while(list.get(right).id != list.get(left).id  && key >= list.get(left).id  && key <= list.get(right).id){
			int middle=(key-list.get(left).id)*(right-left)/(list.get(right).id-list.get(left).id)+left;
			//Almost the same with Binary but next index is determined by a different formula.
			if(key>list.get(middle).id)
				left=middle+1;
			else if(key<list.get(middle).id)
				right=middle-1;
			else return list.get(middle);
		}
		if (key == list.get(left).id)
	        return  list.get(left);
	    else
	        return null;
	}

	public static void Tester(){

		ArrayList<String> names=new ArrayList<>();
		names.addAll(Arrays.asList("Ashley","Burgess","Underwood","Elliott","Burnett","Gould","Banana","trapezi","miyagi"
				,"Floros","Wynn","win","Austin","Justin","police","Gonzales","speedy","Valenzuela","Magician","Kline",
				"Mine","Battle","Fighto","Carr","Nash","Cash","cash","mass"
				,"fast","fiddo"));
		double avglnrnm=0;	//Averages
		double avgdt=0;
		double avglnrid=0;
		double avgbin=0;
		double avginter=0;
		double avgavl=0;

		String message="This might take a few minutes...\n";
		for(int l=1;l<=10;l++){
			message=message+"Test #"+l;
			long startTime=System.currentTimeMillis();
			for(int k=0;k<=l*10;k++){
				int i;
				for(i=0;i<30;i++){
					SearchBySurname(names.get(i),0);

				}
			i=0;
			}
			long endTime=System.currentTimeMillis();
			message=message+"\n\tLinear Search by Name: Time for "+l*300+" searches= "+(endTime-startTime)+" ms";
			avglnrnm+=endTime-startTime;

			startTime=System.currentTimeMillis();
			for(int k=0;k<=l*10;k++){
				int i;
				for(i=0;i<30;i++){
					dt.search(names.get(i));

			}
			i=0;
			}
			endTime=System.currentTimeMillis();
			message=message+"\n\tTrie Search by Name: Time for "+l*300+" searches= "+(endTime-startTime)+" ms";
			avgdt+=endTime-startTime;

			Random random=new Random();
			startTime=System.currentTimeMillis();
			for(int i=0;i<l*1000;i++){
				SearchById(random.nextInt(1310 - 1 + 1));
			}

			endTime=System.currentTimeMillis();
			message=message+"\n\tLinear Search by ID: Time for "+l*1000+" searches= "+(endTime-startTime)+" ms";
			avglnrid+=endTime-startTime;

			startTime=System.currentTimeMillis();
			for(int i=0;i<l*1000;i++){
				BinarySearch(random.nextInt(1310 - 1 + 1),0,1099);

			}

			endTime=System.currentTimeMillis();
			message=message+"\n\tBinary Search by ID: Time for "+l*1000+" searches= "+(endTime-startTime)+" ms";
			avgbin+=endTime-startTime;

			startTime=System.currentTimeMillis();
			for(int i=0;i<l*1000;i++)
				InterpolationSearch(random.nextInt(1310 - 1 + 1),0,1099);


			endTime=System.currentTimeMillis();
			message=message+"\n\tInterpolation Search by ID: Time for "+l*1000+" searches= "+(endTime-startTime)+" ms";
			avginter+=endTime-startTime;

			startTime=System.currentTimeMillis();
			for(int i=0;i<l*1000;i++){
				avl.access(random.nextInt(1310 - 1 + 1));

			}

			endTime=System.currentTimeMillis();
			message=message+"\n\tAVL Tree Search by ID: Time for "+l*1000+" searches= "+(endTime-startTime)+" ms";
			avgavl+=endTime-startTime;
			JOptionPane.showMessageDialog(null,message);
			message="";
		}
		String average="\nAverage Linear Search by Name "+avglnrnm/10+" ms\n"
									+"Average Trie Search by Name "+avgdt/10+" ms\n"
									+"Average Linear Search by ID "+avglnrid/10+" ms\n"
									+"Average Binary Search by ID "+avgbin/10+" ms\n"
									+"Average Interpolation Search by ID "+avginter/10+" ms\n"
									+"Average AVL Tree Search by ID "+avgavl/10+" ms";
		JOptionPane.showMessageDialog(null,average);

	}
}
