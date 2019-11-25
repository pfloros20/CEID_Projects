/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;
import java.sql.Connection;
import java.io.IOException;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
/**
 *
 * @author panos
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    Connection conn=null;   
    public static Connection ConnectDb() { 
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn=DriverManager.getConnection("jdbc:mysql://localhost/ESHOP", "root", "bitch1please");
            return conn;
           }
        catch(Exception e){
        JOptionPane.showMessageDialog(null,e);
        return null;
        }
    }
    public static void main(String[] args) {
        // TODO code application logic here
        while(true){
            System.out.println("Menu:");
            System.out.println("0)Guest Mode.");
            System.out.println("1)Customer Mode.");
            System.out.println("2)Admin Mode.");
            System.out.println("3)Run All Modes.");
            System.out.println("4)Exit.");
            System.out.println("Press numbers 0-4 for your choice.");
            try{
                int choice=System.in.read()-48;//48 ascii code for 0
                switch(choice){
                    case 0:
                        GuestAPI ggui=new GuestAPI();
                        ggui.setVisible(true);
                        break;
                    case 1:
                        CustomerAPI cgui=new CustomerAPI();
                        cgui.setVisible(true);
                        break;
                    case 2:
                        AdministratorAPI agui=new AdministratorAPI();
                        agui.setVisible(true);
                        break;
                    case 3:
                        ggui=new GuestAPI();
                        ggui.setVisible(true);
                        cgui=new CustomerAPI();
                        cgui.setVisible(true);
                        agui=new AdministratorAPI();
                        agui.setVisible(true);
                        break;
                    case 4:
                        System.exit(0);
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
}
