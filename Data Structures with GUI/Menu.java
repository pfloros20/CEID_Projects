import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
@SuppressWarnings("serial")
public class Menu extends JFrame implements ActionListener  {
  JButton load;
  JButton save;
  JButton exit;
  JButton add;
  JButton test;
  JButton linId;
  JButton bin;
  JButton inter;
  JButton avl;
  JButton linName;
  JButton dt;

  String input="data.csv";
  static boolean loaded=false;

  public Menu(){
    super("Project Data Structures 2k17 LUL.");
    setSize(800,600);
    setVisible(true);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    ImageIcon loadIcon=new ImageIcon(getClass().getResource("Assets/Load.png"));
    loadIcon=resize(loadIcon,25,25);
    ImageIcon saveIcon=new ImageIcon(getClass().getResource("Assets/Save.png"));
    saveIcon=resize(saveIcon,25,25);
    ImageIcon addIcon=new ImageIcon(getClass().getResource("Assets/Add.png"));
    addIcon=resize(addIcon,25,25);
    ImageIcon searchIcon=new ImageIcon(getClass().getResource("Assets/Search.png"));
    searchIcon=resize(searchIcon,25,25);
    ImageIcon displayIcon=new ImageIcon(getClass().getResource("Assets/Display.png"));
    displayIcon=resize(displayIcon,25,25);
    ImageIcon testIcon=new ImageIcon(getClass().getResource("Assets/Test.png"));
    testIcon=resize(testIcon,25,25);
    ImageIcon exitIcon=new ImageIcon(getClass().getResource("Assets/Exit.png"));
    exitIcon=resize(exitIcon,25,25);
    ImageIcon hotel=new ImageIcon(getClass().getResource("Assets/Hotel.jpg"));

    load=new JButton("Load Hotels and Reservations.",loadIcon);
    save=new JButton("Save Hotels and Reservations.",saveIcon);
    add=new JButton("Add a Hotel.",addIcon);
    test=new JButton("Test Searches.",testIcon);
    exit=new JButton("Exit.",exitIcon);

    linId=new JButton("Linear Search.",searchIcon);
    bin=new JButton("Binary Search.",searchIcon);
    inter=new JButton("Interpolation Search.",searchIcon);
    avl=new JButton("AVL Tree Search.",searchIcon);
    linName=new JButton("Linear Search.",searchIcon);
    dt=new JButton("Digital Trie Search.",searchIcon);

    JMenuBar bar=new JMenuBar();
    JMenu main=new JMenu("Main Menu.");
    JMenu searchid=new JMenu("Search and Display a Hotel by id.");
    JMenu searchname=new JMenu("Display Reservations by surname search.");
    bar.add(main);
    bar.add(searchid);
    bar.add(searchname);

    JPanel mainpnl=new JPanel(new GridLayout(5,1));
    main.add(mainpnl);
    mainpnl.add(load);
    mainpnl.add(save);
    mainpnl.add(add);
    mainpnl.add(test);
    mainpnl.add(exit);

    JPanel idpnl=new JPanel(new GridLayout(4,1));
    searchid.add(idpnl);
    idpnl.add(linId);
    idpnl.add(bin);
    idpnl.add(inter);
    idpnl.add(avl);

    JPanel namepnl=new JPanel(new GridLayout(2,1));
    searchname.add(namepnl);
    namepnl.add(linName);
    namepnl.add(dt);


    JPanel panel=new JPanel();
    panel.setLayout(new BorderLayout());

    JLabel hotelLabel=new JLabel(hotel);

    panel.add(bar,BorderLayout.NORTH);
    panel.add(hotelLabel,BorderLayout.CENTER);

    load.addActionListener(this);
    save.addActionListener(this);
    exit.addActionListener(this);
    add.addActionListener(this);
    test.addActionListener(this);
    linName.addActionListener(this);
    dt.addActionListener(this);
    linId.addActionListener(this);
    bin.addActionListener(this);
    inter.addActionListener(this);
    avl.addActionListener(this);
    add(panel);
    setVisible(true);
    pack();


    }
    public static JFrame resultWindow(String title,String result){
      JFrame resultFrame=new JFrame(title);
      return resultFrame;
    }

    public void actionPerformed(ActionEvent action){
      try{
        if(action.getSource()==exit){
          if(loaded)
            Main.SaveHotelsnReservations(input);
            System.exit(1);
        }

        if(action.getSource()==load){
          String response=JOptionPane.showInputDialog(null,"Enter file name to be searched or nothing for default file name data.csv:");
          if(!response.equals(""))
            input=response;
          Main.LoadHotelsnReservations(input);
          Main.HeapSort();
          JOptionPane.showMessageDialog(null,"Loading Done.");
          loaded=true;
        }
        if(loaded==false)
          JOptionPane.showMessageDialog(null,"Must load a file first!");
        else if(action.getSource()==save){
          Main.SaveHotelsnReservations(input);
          JOptionPane.showMessageDialog(null,"Saving Done.");
        }else if(action.getSource()==add){
          Main.AddaHotel();
          Main.HeapSort();
          JOptionPane.showMessageDialog(null,"Hotel added.");
        }else if(action.getSource()==test){
          Main.Tester();
        }else if(action.getSource()==linName){
          String name=JOptionPane.showInputDialog(null,"Enter name to be searched:");
          Main.SearchBySurname(name,1);
        }else if(action.getSource()==dt){
          String name=JOptionPane.showInputDialog(null,"Enter name to be searched:");
          ArrayList<Reservation> list= Main.dt.search(name);
          if(list!=null){
            JFrame frame=new JFrame("Digital Trie Name Search.");
      		  JPanel panel=new JPanel();
            String[] message=new String[list.size()];
            for(int i=0;i<list.size();i++)
              message[i]="Reservation: "+list.get(i).name+"  Date: "+list.get(i).checkinDate+"  Staying for: "+list.get(i).stayDurationDays+" days "+" Hotel: "+list.get(i).hotel.name;

					  JList<String> messagelist=new JList<String>(message);
					  panel.add(messagelist);
					  frame.add(panel);
					  frame.pack();
					  frame.setVisible(true);
          }else
            JOptionPane.showMessageDialog(null,"Not Found!");
        }else if(action.getSource()==linId){
          String id=JOptionPane.showInputDialog(null,"Enter id to be searched:");
          Hotel result=Main.SearchById(Integer.parseInt(id));
          if(result==null)
            JOptionPane.showMessageDialog(null,"Not Found!");
          else
            JOptionPane.showMessageDialog(null," Hotel :"+result.name);
        }else if(action.getSource()==bin){
          String id=JOptionPane.showInputDialog(null,"Enter id to be searched:");
          Hotel result=Main.BinarySearch(Integer.parseInt(id),0,Main.list.size()-1);
          if(result==null)
            JOptionPane.showMessageDialog(null,"Not Found!");
          else
            JOptionPane.showMessageDialog(null," Hotel :"+result.name);
        }else if(action.getSource()==inter){
          String id=JOptionPane.showInputDialog(null,"Enter id to be searched:");
          Hotel result=Main.InterpolationSearch(Integer.parseInt(id),0,Main.list.size()-1);
          if(result==null)
            JOptionPane.showMessageDialog(null,"Not Found!");
          else
            JOptionPane.showMessageDialog(null," Hotel :"+result.name);
        }else if(action.getSource()==avl){
          String id=JOptionPane.showInputDialog(null,"Enter id to be searched:");
          Hotel result=Main.avl.access(Integer.parseInt(id));
          if(result==null)
            JOptionPane.showMessageDialog(null,"Not Found!");
          else
            JOptionPane.showMessageDialog(null," Hotel :"+result.name);
        }
      }catch(IOException e){
        JOptionPane.showMessageDialog(null,"File Not Found!");
      }

    }
    public static ImageIcon resize(ImageIcon icon, int width,int height){
      Image img = icon.getImage() ;
      Image newimg = img.getScaledInstance(width, height,  Image.SCALE_SMOOTH ) ;
      icon = new ImageIcon( newimg );
      return icon;
    }
  }
