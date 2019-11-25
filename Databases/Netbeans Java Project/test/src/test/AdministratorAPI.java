/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.DefaultListModel;
/**
 *
 * @author panos
 */
public class AdministratorAPI extends javax.swing.JFrame {

    /**
     * Creates new form AdministratorAPI
     */
    Connection conn=null;
    Statement stmt=null;
    PreparedStatement pst=null;
    ResultSet rs=null;
    int id=-1;
    String email;
    enum CurrentList{orders,customers,queries};
    CurrentList currentList;
    String toBeDeleted=null;
    public AdministratorAPI() {
        initDialog();
        initComponents();
        this.setLocationRelativeTo(null);
        ProductList.setModel(new DefaultListModel());
        
    }

    private void initDialog(){
        
        WrongInputLabel = new javax.swing.JLabel();
        LoginDialog = new javax.swing.JDialog();
        LoginLabel = new javax.swing.JLabel();
        IDLabel = new javax.swing.JLabel();
        EmailLabel = new javax.swing.JLabel();
        IDText = new javax.swing.JTextField();
        EmailText = new javax.swing.JTextField();
        OKButton = new javax.swing.JButton();
        CloseButton = new javax.swing.JButton();

        LoginDialog.setTitle("Log In");
        LoginDialog.setMinimumSize(new java.awt.Dimension(298, 150));
        LoginDialog.setModal(true);
        LoginDialog.setUndecorated(true);
        LoginDialog.setResizable(false);

        LoginLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        LoginLabel.setText("Log In");

        IDLabel.setText("ID:");

        EmailLabel.setText("Email:");

        OKButton.setText("OK");
        OKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKButtonActionPerformed(evt);
            }
        });

        CloseButton.setText("Close");
        CloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseButtonActionPerformed(evt);
            }
        });

        WrongInputLabel.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        WrongInputLabel.setForeground(new java.awt.Color(255, 51, 51));
        WrongInputLabel.setText("Wrong Log In Info");
        WrongInputLabel.setVisible(false);

        javax.swing.GroupLayout LoginDialogLayout = new javax.swing.GroupLayout(LoginDialog.getContentPane());
        LoginDialog.getContentPane().setLayout(LoginDialogLayout);
        LoginDialogLayout.setHorizontalGroup(
            LoginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginDialogLayout.createSequentialGroup()
                .addGroup(LoginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LoginDialogLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(LoginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(LoginDialogLayout.createSequentialGroup()
                                .addComponent(OKButton, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CloseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(LoginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(WrongInputLabel)
                                .addGroup(LoginDialogLayout.createSequentialGroup()
                                    .addGroup(LoginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(IDLabel)
                                        .addComponent(EmailLabel))
                                    .addGap(6, 6, 6)
                                    .addGroup(LoginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(EmailText, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(IDText, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(LoginDialogLayout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addComponent(LoginLabel)))
                .addContainerGap(79, Short.MAX_VALUE))
        );
        LoginDialogLayout.setVerticalGroup(
            LoginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginDialogLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(LoginLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(LoginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IDLabel)
                    .addComponent(IDText, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(LoginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EmailLabel)
                    .addComponent(EmailText, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(WrongInputLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(LoginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(OKButton, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CloseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(19, 19, 19))
        );
        LoginDialog.setLocationRelativeTo(null);
        LoginDialog.setVisible(true);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        CategoriesPanel = new javax.swing.JPanel();
        jSeparator = new javax.swing.JSeparator();
        ActionsLabel = new javax.swing.JLabel();
        ShowOrdersButton = new javax.swing.JButton();
        ShowCustomersButton = new javax.swing.JButton();
        DeleteCustomerButton = new javax.swing.JButton();
        ShowQueriesButton = new javax.swing.JButton();
        TitlePanel = new javax.swing.JPanel();
        TitleLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ProductList = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Eshop");
        setBackground(new java.awt.Color(255, 255, 204));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CategoriesPanel.setBackground(new java.awt.Color(51, 153, 255));
        CategoriesPanel.setAlignmentX(0.0F);
        CategoriesPanel.setAlignmentY(0.0F);

        ActionsLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        ActionsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ActionsLabel.setText("Actions");
        ActionsLabel.setAlignmentY(0.0F);
        ActionsLabel.setMinimumSize(new java.awt.Dimension(109, 22));

        ShowOrdersButton.setBackground(new java.awt.Color(51, 153, 255));
        ShowOrdersButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        ShowOrdersButton.setText("Show Orders");
        ShowOrdersButton.setAlignmentY(0.0F);
        ShowOrdersButton.setBorder(null);
        ShowOrdersButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ShowOrdersButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        ShowOrdersButton.setFocusable(false);
        ShowOrdersButton.setRequestFocusEnabled(false);
        ShowOrdersButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ShowOrdersButtonMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ShowOrdersButtonMouseEntered(evt);
            }
        });
        ShowOrdersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowOrdersButtonActionPerformed(evt);
            }
        });

        ShowCustomersButton.setBackground(new java.awt.Color(51, 153, 255));
        ShowCustomersButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        ShowCustomersButton.setText("Show Customers");
        ShowCustomersButton.setAlignmentY(0.0F);
        ShowCustomersButton.setBorder(null);
        ShowCustomersButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ShowCustomersButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        ShowCustomersButton.setFocusable(false);
        ShowCustomersButton.setRequestFocusEnabled(false);
        ShowCustomersButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ShowCustomersButtonMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ShowCustomersButtonMouseEntered(evt);
            }
        });
        ShowCustomersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowCustomersButtonActionPerformed(evt);
            }
        });

        DeleteCustomerButton.setBackground(new java.awt.Color(51, 153, 255));
        DeleteCustomerButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        DeleteCustomerButton.setText("Delete Customer");
        DeleteCustomerButton.setAlignmentY(0.0F);
        DeleteCustomerButton.setBorder(null);
        DeleteCustomerButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        DeleteCustomerButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        DeleteCustomerButton.setFocusable(false);
        DeleteCustomerButton.setRequestFocusEnabled(false);
        DeleteCustomerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                DeleteCustomerButtonMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                DeleteCustomerButtonMouseEntered(evt);
            }
        });
        DeleteCustomerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteCustomerButtonActionPerformed(evt);
            }
        });

        ShowQueriesButton.setBackground(new java.awt.Color(51, 153, 255));
        ShowQueriesButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        ShowQueriesButton.setText("Show Queries");
        ShowQueriesButton.setAlignmentY(0.0F);
        ShowQueriesButton.setBorder(null);
        ShowQueriesButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ShowQueriesButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        ShowQueriesButton.setFocusable(false);
        ShowQueriesButton.setRequestFocusEnabled(false);
        ShowQueriesButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ShowQueriesButtonMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ShowQueriesButtonMouseEntered(evt);
            }
        });
        ShowQueriesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowQueriesButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CategoriesPanelLayout = new javax.swing.GroupLayout(CategoriesPanel);
        CategoriesPanel.setLayout(CategoriesPanelLayout);
        CategoriesPanelLayout.setHorizontalGroup(
            CategoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CategoriesPanelLayout.createSequentialGroup()
                .addGroup(CategoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CategoriesPanelLayout.createSequentialGroup()
                        .addComponent(jSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(ActionsLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ShowOrdersButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ShowCustomersButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(DeleteCustomerButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ShowQueriesButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        CategoriesPanelLayout.setVerticalGroup(
            CategoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CategoriesPanelLayout.createSequentialGroup()
                .addComponent(ActionsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ShowOrdersButton)
                .addGap(18, 18, 18)
                .addComponent(ShowCustomersButton)
                .addGap(18, 18, 18)
                .addComponent(DeleteCustomerButton)
                .addGap(18, 18, 18)
                .addComponent(ShowQueriesButton)
                .addContainerGap(614, Short.MAX_VALUE))
        );

        getContentPane().add(CategoriesPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 280, 830));

        TitlePanel.setBackground(new java.awt.Color(255, 255, 153));
        TitlePanel.setAlignmentX(0.0F);
        TitlePanel.setAlignmentY(0.0F);

        TitleLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        TitleLabel.setText("Eshop Administrator API");

        javax.swing.GroupLayout TitlePanelLayout = new javax.swing.GroupLayout(TitlePanel);
        TitlePanel.setLayout(TitlePanelLayout);
        TitlePanelLayout.setHorizontalGroup(
            TitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TitlePanelLayout.createSequentialGroup()
                .addContainerGap(172, Short.MAX_VALUE)
                .addComponent(TitleLabel)
                .addGap(139, 139, 139))
        );
        TitlePanelLayout.setVerticalGroup(
            TitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(TitlePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(243, 0, 560, 50));

        ProductList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        ProductList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ProductListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(ProductList);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 60, 500, 500));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ShowOrdersButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ShowOrdersButtonMouseExited
        // TODO add your handling code here:
        ShowOrdersButton.setBackground(new java.awt.Color(51, 153, 255));
    }//GEN-LAST:event_ShowOrdersButtonMouseExited

    private void ShowOrdersButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ShowOrdersButtonMouseEntered
        // TODO add your handling code here:
        ShowOrdersButton.setBackground(new java.awt.Color(204, 255, 255));
    }//GEN-LAST:event_ShowOrdersButtonMouseEntered

    private void ShowOrdersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowOrdersButtonActionPerformed
        // TODO add your handling code here:
        try {
            conn=Test.ConnectDb();
            String query="SELECT customer_id,used_together,cpu_model,mobo_model,gpu_model,psu_model,ram_model,case_model,ssd_model,hdd_model,exthd_model FROM `ORDER`";
            DefaultListModel myTempList = new DefaultListModel();
            stmt=conn.createStatement();
            rs=stmt.executeQuery(query);
            while(rs.next()){
                String myTempString = "Customer id: "+rs.getString("customer_id")+
                                        " Used Together: "+rs.getString("used_together")+
                                        " CPU: "+rs.getString("cpu_model")+
                                        " Motherboard: "+rs.getString("mobo_model")+
                                        " PSU: "+rs.getString("psu_model")+
                                        " RAM: "+rs.getString("ram_model")+
                                        " CASE: "+rs.getString("case_model")+
                                        " SSD: "+rs.getString("ssd_model")+
                                        " HDD: "+rs.getString("hdd_model")+
                                        " extHD: "+rs.getString("exthd_model");
                myTempList.addElement(myTempString);
            }
            ProductList.setModel(myTempList);
            currentList=CurrentList.orders;
        } catch (SQLException se) {
        //Handle errors for JDBC
        se.printStackTrace();
        }catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }//GEN-LAST:event_ShowOrdersButtonActionPerformed

    private void OKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKButtonActionPerformed
        // TODO add your handling code here:
        try {
            String idstring=IDText.getText();
            email=EmailText.getText();
            id=Integer.parseInt(idstring);
            conn=Test.ConnectDb();
            String query="SELECT id,email FROM `ADMIN` WHERE id='"+id+"'&&email='"+email+"'";
            DefaultListModel myTempList = new DefaultListModel();
            stmt=conn.createStatement();
            rs=stmt.executeQuery(query);
            if(rs.next())
                LoginDialog.dispose();
            else
                WrongInputLabel.setVisible(true);
        } catch (SQLException se) {
        //Handle errors for JDBC
        se.printStackTrace();
        }catch (NumberFormatException e) {
            //Handle errors for Class.forName
            WrongInputLabel.setVisible(true);
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_OKButtonActionPerformed

    private void CloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseButtonActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_CloseButtonActionPerformed

    private void ProductListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ProductListMouseClicked
        // TODO add your handling code here:
        switch(currentList){
            case customers:
                String parts[]=ProductList.getSelectedValue().split(" ");
                toBeDeleted=parts[2];
                break;
        }
    }//GEN-LAST:event_ProductListMouseClicked

    private void ShowCustomersButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ShowCustomersButtonMouseExited
        // TODO add your handling code here:
        ShowCustomersButton.setBackground(new java.awt.Color(51, 153, 255));
    }//GEN-LAST:event_ShowCustomersButtonMouseExited

    private void ShowCustomersButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ShowCustomersButtonMouseEntered
        // TODO add your handling code here:
        ShowCustomersButton.setBackground(new java.awt.Color(204, 255, 255));
    }//GEN-LAST:event_ShowCustomersButtonMouseEntered

    private void ShowCustomersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowCustomersButtonActionPerformed
        // TODO add your handling code here:
        try {
            conn=Test.ConnectDb();
            String query="SELECT id,name,surname,email,phone,registration_date FROM `CUSTOMER`";
            DefaultListModel myTempList = new DefaultListModel();
            stmt=conn.createStatement();
            rs=stmt.executeQuery(query);
            while(rs.next()){
                String myTempString = "Customer id: "+rs.getString("id")+
                                        " Name: "+rs.getString("name")+
                                        " Surname: "+rs.getString("surname")+
                                        " Email: "+rs.getString("email")+
                                        " Phone: "+rs.getString("phone")+
                                        " Registration Date: "+rs.getString("registration_date");
                myTempList.addElement(myTempString);
            }
            ProductList.setModel(myTempList);
            currentList=CurrentList.customers;
        } catch (SQLException se) {
        //Handle errors for JDBC
        se.printStackTrace();
        }catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }//GEN-LAST:event_ShowCustomersButtonActionPerformed

    private void DeleteCustomerButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DeleteCustomerButtonMouseExited
        // TODO add your handling code here:
        DeleteCustomerButton.setBackground(new java.awt.Color(51, 153, 255));
    }//GEN-LAST:event_DeleteCustomerButtonMouseExited

    private void DeleteCustomerButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DeleteCustomerButtonMouseEntered
        // TODO add your handling code here:
        DeleteCustomerButton.setBackground(new java.awt.Color(204, 255, 255));
    }//GEN-LAST:event_DeleteCustomerButtonMouseEntered

    private void DeleteCustomerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteCustomerButtonActionPerformed
        // TODO add your handling code here:
        try {
            conn=Test.ConnectDb();
            String query="DELETE FROM `CUSTOMER` WHERE id="+toBeDeleted;
            stmt=conn.createStatement();
            stmt.executeUpdate(query);
            toBeDeleted=null;
            query="SELECT id,name,surname,email,phone,registration_date FROM `CUSTOMER`";
            DefaultListModel myTempList = new DefaultListModel();
            stmt=conn.createStatement();
            rs=stmt.executeQuery(query);
            while(rs.next()){
                String myTempString = "Customer id: "+rs.getString("id")+
                                        " Name: "+rs.getString("name")+
                                        " Surname: "+rs.getString("surname")+
                                        " Email: "+rs.getString("email")+
                                        " Phone: "+rs.getString("phone")+
                                        " Registration Date: "+rs.getString("registration_date");
                myTempList.addElement(myTempString);
            }
            ProductList.setModel(myTempList);
        } catch (SQLException se) {
        //Handle errors for JDBC
        se.printStackTrace();
        }catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }//GEN-LAST:event_DeleteCustomerButtonActionPerformed

    private void ShowQueriesButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ShowQueriesButtonMouseExited
        // TODO add your handling code here:
        ShowQueriesButton.setBackground(new java.awt.Color(51, 153, 255));
    }//GEN-LAST:event_ShowQueriesButtonMouseExited

    private void ShowQueriesButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ShowQueriesButtonMouseEntered
        // TODO add your handling code here:
        ShowQueriesButton.setBackground(new java.awt.Color(204, 255, 255));
    }//GEN-LAST:event_ShowQueriesButtonMouseEntered

    private void ShowQueriesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowQueriesButtonActionPerformed
        // TODO add your handling code here:
        try {
            conn=Test.ConnectDb();
            DefaultListModel myTempList = new DefaultListModel();
            String Queries[]={  "SELECT mobo_model,COUNT(cpu_socket)\n" +
                                "FROM `MOTHERBOARD` LEFT JOIN `CPU`\n" +
                                "ON mobo_socket=cpu_socket\n" +
                                "GROUP BY mobo_model ORDER BY COUNT(cpu_socket)",
                       
                                "SELECT position,year,AVG(amount) AS AVG\n" +
                                "FROM `ADMIN`INNER JOIN `GOT_PAID`\n" +
                                "ON id=admin_id\n" +
                                "INNER JOIN `SALARY`\n" +
                                "ON salary_id=`SALARY`.id\n" +
                                "WHERE month IN(1,2,3)\n" +
                                "GROUP BY year,position",
            
                                "SELECT `SSD`.ssd_model AS MODEL,ssd_capacity AS CAPACITY,ssd_connection AS CONN_TYPE,ssd_price AS PRICE,COUNT(`ORDER`.ssd_model) AS TIMES_ORDERED\n" +
                                "FROM `SSD` LEFT JOIN `ORDER` ON `ORDER`.ssd_model=`SSD`.ssd_model\n" +
                                "WHERE order_date>=DATE_SUB(NOW(),INTERVAL 2 MONTH)\n" +
                                "GROUP BY(`SSD`.ssd_model)\n" +
                                "UNION ALL\n" +
                                "SELECT `HDD`.hdd_model AS MODEL,hdd_capacity AS CAPACITY,hdd_connection AS CONN_TYPE,hdd_price AS PRICE,COUNT(`ORDER`.hdd_model) AS TIMES_ORDERED\n" +
                                "FROM `HDD` LEFT JOIN `ORDER` ON `ORDER`.hdd_model=`HDD`.hdd_model\n" +
                                "WHERE order_date>=DATE_SUB(NOW(),INTERVAL 2 MONTH)\n" +
                                "GROUP BY(`HDD`.hdd_model)\n" +
                                "UNION ALL\n" +
                                "SELECT `extHD`.exthd_model AS MODEL,exthd_capacity AS CAPACITY,exthd_connection AS CONN_TYPE,exthd_price AS PRICE,COUNT(`ORDER`.exthd_model) AS TIMES_ORDERED\n" +
                                "FROM `extHD` LEFT JOIN `ORDER` ON `ORDER`.exthd_model=`extHD`.exthd_model\n" +
                                "WHERE order_date>=DATE_SUB(NOW(),INTERVAL 2 MONTH)\n" +
                                "GROUP BY(`extHD`.exthd_model)",

                                "SELECT PSU_WITH_WATTAGE FROM\n" +
                                "(SELECT CONCAT(`ORDER`.psu_model,' with ',`PSU`.psu_power) AS PSU_WITH_WATTAGE,COUNT(`ORDER`.psu_model) AS COUNT\n" +
                                "FROM `ORDER` INNER JOIN `PSU` ON `ORDER`.psu_model=`PSU`.psu_model\n" +
                                "GROUP BY(`ORDER`.psu_model)\n" +
                                "HAVING `ORDER`.psu_model NOT LIKE '%Plus%'\n" +
                                "ORDER BY(`ORDER`.psu_model) DESC LIMIT 3)AS T",
            
                                "SELECT name,surname FROM\n" +
                                "`CUSTOMER` INNER JOIN `ORDER` ON `CUSTOMER`.id=customer_id\n" +
                                "GROUP BY `CUSTOMER`.id\n" +
                                "HAVING COUNT(`CUSTOMER`.id) = (SELECT MAX(count) FROM\n" +
                                "(SELECT COUNT(`CUSTOMER`.id) AS count\n" +
                                "FROM `CUSTOMER` INNER JOIN `ORDER` ON `CUSTOMER`.id=customer_id\n" +
                                "GROUP BY `CUSTOMER`.id)AS T)",
            
                                "SELECT Senior,GROUP_CONCAT(Junior SEPARATOR ', ') AS Junior FROM\n" +
                                "(SELECT CONCAT(`SEN`.name,' ',`SEN`.surname) AS Senior,CONCAT(`JUN`.name,' ',`JUN`.surname) AS Junior\n" +
                                "FROM `ADMIN` AS SEN INNER JOIN `ADMIN` AS JUN ON `SEN`.id=`JUN`.supervised_by)\n" +
                                "AS T GROUP BY(Senior)"};
            for(int i=0;i<Queries.length;i++){
                switch(i){
                    case 0:
                        myTempList.addElement("* Number of CPUs compatible with Motherboard *");
                        break;
                    case 1:
                        myTempList.addElement("* Average salary for each first quarter of each year for junior and senior admins *");
                        break;
                    case 2:
                        myTempList.addElement("* Hard Drives,size,connection type,price and times ordered *");
                        break;
                    case 3:
                        myTempList.addElement("* Top 3 Ordered PSUs with Wattage *");
                        break;
                    case 4:
                        myTempList.addElement("* Customer(s) with Most Orders *");
                        break;
                    case 5:
                        myTempList.addElement("* Seniors and the Juniors they supervise *");
                        break;
                }
                stmt=conn.createStatement();
                rs=stmt.executeQuery(Queries[i]);
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                    while(rs.next()){
                        String myTempString=null;
                        if(columnsNumber==5)
                            myTempString = rs.getString(1)+"  |  "+rs.getString(2)+"  |  "+rs.getString(3)+"  |  "+rs.getString(4)+"  |  "+rs.getString(5);
                        else if(columnsNumber==3)
                            myTempString = rs.getString(1)+"  |  "+rs.getString(2)+"  |  "+rs.getString(3);
                        else if(columnsNumber==2)
                            myTempString = rs.getString(1)+"  |  "+rs.getString(2);
                        else if(columnsNumber==1)
                            myTempString = rs.getString(1);
                        myTempList.addElement(myTempString);
                    }
                    
            myTempList.addElement(" ");
            }
            ProductList.setModel(myTempList);
            currentList=CurrentList.customers;
        } catch (SQLException se) {
        //Handle errors for JDBC
        se.printStackTrace();
        }catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }//GEN-LAST:event_ShowQueriesButtonActionPerformed

    /**
     * @param args the command line arguments
     */

    private javax.swing.JLabel WrongInputLabel;
    private javax.swing.JLabel IDLabel;
    private javax.swing.JTextField IDText;
    private javax.swing.JDialog LoginDialog;
    private javax.swing.JLabel LoginLabel;
    private javax.swing.JLabel EmailLabel;
    private javax.swing.JTextField EmailText;
    private javax.swing.JButton OKButton;
    private javax.swing.JButton CloseButton;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ActionsLabel;
    private javax.swing.JPanel CategoriesPanel;
    private javax.swing.JButton DeleteCustomerButton;
    private javax.swing.JList<String> ProductList;
    private javax.swing.JButton ShowCustomersButton;
    private javax.swing.JButton ShowOrdersButton;
    private javax.swing.JButton ShowQueriesButton;
    private javax.swing.JLabel TitleLabel;
    private javax.swing.JPanel TitlePanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator;
    // End of variables declaration//GEN-END:variables
}
