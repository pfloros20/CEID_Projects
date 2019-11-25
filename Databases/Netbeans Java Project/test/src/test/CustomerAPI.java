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
import java.sql.SQLException;
import javax.swing.DefaultListModel;
/**
 *
 * @author panos
 */
public class CustomerAPI extends javax.swing.JFrame {

    /**
     * Creates new form CustomerAPI
     */
    Connection conn=null;
    Statement stmt=null;
    PreparedStatement pst=null;
    ResultSet rs=null;
    int id=-1;
    String email;
    String cpu="NULL";
    String mobo="NULL";
    String gpu="NULL";
    String psu="NULL";
    String ram="NULL";
    String chassis="NULL";
    String ssd="NULL";
    String hdd="NULL";
    String exthd="NULL";
    String together=null;
    enum CurrentList{cpu,mobo,gpu,psu,ram,chassis,ssd,hdd,exthd,points};
    CurrentList currentList;
    
    public CustomerAPI() {
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

        Together = new javax.swing.JDialog();
        question = new javax.swing.JLabel();
        Yes = new javax.swing.JButton();
        No = new javax.swing.JButton();
        CategoriesPanel = new javax.swing.JPanel();
        CategoryLabel = new javax.swing.JLabel();
        jSeparator = new javax.swing.JSeparator();
        CPUButton = new javax.swing.JButton();
        MOBOButton = new javax.swing.JButton();
        GPUButton = new javax.swing.JButton();
        PSUButton = new javax.swing.JButton();
        RAMButton = new javax.swing.JButton();
        CASEButton = new javax.swing.JButton();
        SSDButton = new javax.swing.JButton();
        HDDButton = new javax.swing.JButton();
        EXTHDButton = new javax.swing.JButton();
        ActionsLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        ShowPointsButton = new javax.swing.JButton();
        Order = new javax.swing.JButton();
        TitlePanel = new javax.swing.JPanel();
        TitleLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ProductList = new javax.swing.JList<>();

        Together.setMinimumSize(new java.awt.Dimension(408, 88));
        Together.setUndecorated(true);
        Together.setResizable(false);

        question.setText("Are the components to be used together?");

        Yes.setText("Yes");
        Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                YesActionPerformed(evt);
            }
        });

        No.setText("No");
        No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout TogetherLayout = new javax.swing.GroupLayout(Together.getContentPane());
        Together.getContentPane().setLayout(TogetherLayout);
        TogetherLayout.setHorizontalGroup(
            TogetherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TogetherLayout.createSequentialGroup()
                .addGroup(TogetherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TogetherLayout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(Yes)
                        .addGap(76, 76, 76)
                        .addComponent(No))
                    .addGroup(TogetherLayout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(question)))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        TogetherLayout.setVerticalGroup(
            TogetherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TogetherLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(question, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(TogetherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Yes)
                    .addComponent(No))
                .addContainerGap())
        );

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

        CategoryLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        CategoryLabel.setText("Categories");

        CPUButton.setBackground(new java.awt.Color(51, 153, 255));
        CPUButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        CPUButton.setText("CPU");
        CPUButton.setAlignmentY(0.0F);
        CPUButton.setBorder(null);
        CPUButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        CPUButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        CPUButton.setFocusable(false);
        CPUButton.setRequestFocusEnabled(false);
        CPUButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CPUButtonMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CPUButtonMouseEntered(evt);
            }
        });
        CPUButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CPUButtonActionPerformed(evt);
            }
        });

        MOBOButton.setBackground(new java.awt.Color(51, 153, 255));
        MOBOButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        MOBOButton.setText("MotherBoard");
        MOBOButton.setAlignmentY(0.0F);
        MOBOButton.setBorder(null);
        MOBOButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        MOBOButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        MOBOButton.setFocusable(false);
        MOBOButton.setRequestFocusEnabled(false);
        MOBOButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                MOBOButtonMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                MOBOButtonMouseEntered(evt);
            }
        });
        MOBOButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MOBOButtonActionPerformed(evt);
            }
        });

        GPUButton.setBackground(new java.awt.Color(51, 153, 255));
        GPUButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        GPUButton.setText("GPU");
        GPUButton.setAlignmentY(0.0F);
        GPUButton.setBorder(null);
        GPUButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        GPUButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        GPUButton.setFocusable(false);
        GPUButton.setRequestFocusEnabled(false);
        GPUButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                GPUButtonMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                GPUButtonMouseEntered(evt);
            }
        });
        GPUButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GPUButtonActionPerformed(evt);
            }
        });

        PSUButton.setBackground(new java.awt.Color(51, 153, 255));
        PSUButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        PSUButton.setText("PSU");
        PSUButton.setAlignmentY(0.0F);
        PSUButton.setBorder(null);
        PSUButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        PSUButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        PSUButton.setFocusable(false);
        PSUButton.setRequestFocusEnabled(false);
        PSUButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PSUButtonMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PSUButtonMouseEntered(evt);
            }
        });
        PSUButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PSUButtonActionPerformed(evt);
            }
        });

        RAMButton.setBackground(new java.awt.Color(51, 153, 255));
        RAMButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        RAMButton.setText("RAM");
        RAMButton.setAlignmentY(0.0F);
        RAMButton.setBorder(null);
        RAMButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        RAMButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        RAMButton.setFocusable(false);
        RAMButton.setRequestFocusEnabled(false);
        RAMButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                RAMButtonMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                RAMButtonMouseEntered(evt);
            }
        });
        RAMButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RAMButtonActionPerformed(evt);
            }
        });

        CASEButton.setBackground(new java.awt.Color(51, 153, 255));
        CASEButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        CASEButton.setText("CASE");
        CASEButton.setAlignmentY(0.0F);
        CASEButton.setBorder(null);
        CASEButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        CASEButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        CASEButton.setFocusable(false);
        CASEButton.setRequestFocusEnabled(false);
        CASEButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CASEButtonMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CASEButtonMouseEntered(evt);
            }
        });
        CASEButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CASEButtonActionPerformed(evt);
            }
        });

        SSDButton.setBackground(new java.awt.Color(51, 153, 255));
        SSDButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        SSDButton.setText("SSD");
        SSDButton.setAlignmentY(0.0F);
        SSDButton.setBorder(null);
        SSDButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        SSDButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        SSDButton.setFocusable(false);
        SSDButton.setRequestFocusEnabled(false);
        SSDButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SSDButtonMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SSDButtonMouseEntered(evt);
            }
        });
        SSDButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SSDButtonActionPerformed(evt);
            }
        });

        HDDButton.setBackground(new java.awt.Color(51, 153, 255));
        HDDButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        HDDButton.setText("HDD");
        HDDButton.setAlignmentY(0.0F);
        HDDButton.setBorder(null);
        HDDButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        HDDButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        HDDButton.setFocusable(false);
        HDDButton.setRequestFocusEnabled(false);
        HDDButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                HDDButtonMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                HDDButtonMouseEntered(evt);
            }
        });
        HDDButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HDDButtonActionPerformed(evt);
            }
        });

        EXTHDButton.setBackground(new java.awt.Color(51, 153, 255));
        EXTHDButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        EXTHDButton.setText("extHD");
        EXTHDButton.setAlignmentY(0.0F);
        EXTHDButton.setBorder(null);
        EXTHDButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        EXTHDButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        EXTHDButton.setFocusable(false);
        EXTHDButton.setRequestFocusEnabled(false);
        EXTHDButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                EXTHDButtonMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                EXTHDButtonMouseEntered(evt);
            }
        });
        EXTHDButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EXTHDButtonActionPerformed(evt);
            }
        });

        ActionsLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        ActionsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ActionsLabel.setText("Actions");
        ActionsLabel.setAlignmentY(0.0F);
        ActionsLabel.setMinimumSize(new java.awt.Dimension(109, 22));

        ShowPointsButton.setBackground(new java.awt.Color(51, 153, 255));
        ShowPointsButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        ShowPointsButton.setText("Show Points");
        ShowPointsButton.setAlignmentY(0.0F);
        ShowPointsButton.setBorder(null);
        ShowPointsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ShowPointsButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        ShowPointsButton.setFocusable(false);
        ShowPointsButton.setRequestFocusEnabled(false);
        ShowPointsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ShowPointsButtonMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ShowPointsButtonMouseEntered(evt);
            }
        });
        ShowPointsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowPointsButtonActionPerformed(evt);
            }
        });

        Order.setBackground(new java.awt.Color(51, 153, 255));
        Order.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        Order.setText("Order");
        Order.setAlignmentY(0.0F);
        Order.setBorder(null);
        Order.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Order.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        Order.setFocusable(false);
        Order.setRequestFocusEnabled(false);
        Order.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                OrderMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                OrderMouseEntered(evt);
            }
        });
        Order.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OrderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CategoriesPanelLayout = new javax.swing.GroupLayout(CategoriesPanel);
        CategoriesPanel.setLayout(CategoriesPanelLayout);
        CategoriesPanelLayout.setHorizontalGroup(
            CategoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CategoriesPanelLayout.createSequentialGroup()
                .addGroup(CategoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(CategoriesPanelLayout.createSequentialGroup()
                        .addGroup(CategoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CategoriesPanelLayout.createSequentialGroup()
                                .addGap(81, 81, 81)
                                .addComponent(CategoryLabel))
                            .addComponent(jSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(ActionsLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CategoriesPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(CategoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CategoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(CPUButton, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                .addComponent(MOBOButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(GPUButton, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                .addComponent(PSUButton, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                .addComponent(SSDButton, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                .addComponent(HDDButton, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                .addComponent(EXTHDButton, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
                            .addComponent(RAMButton, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CASEButton, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(ShowPointsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Order, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        CategoriesPanelLayout.setVerticalGroup(
            CategoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CategoriesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CategoryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(CPUButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MOBOButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(GPUButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PSUButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RAMButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CASEButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SSDButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(HDDButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EXTHDButton)
                .addGap(27, 27, 27)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ActionsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ShowPointsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Order)
                .addContainerGap(355, Short.MAX_VALUE))
        );

        getContentPane().add(CategoriesPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 280, 830));

        TitlePanel.setBackground(new java.awt.Color(255, 255, 153));
        TitlePanel.setAlignmentX(0.0F);
        TitlePanel.setAlignmentY(0.0F);

        TitleLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        TitleLabel.setText("Eshop Customer API");

        javax.swing.GroupLayout TitlePanelLayout = new javax.swing.GroupLayout(TitlePanel);
        TitlePanel.setLayout(TitlePanelLayout);
        TitlePanelLayout.setHorizontalGroup(
            TitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TitlePanelLayout.createSequentialGroup()
                .addContainerGap(166, Short.MAX_VALUE)
                .addComponent(TitleLabel)
                .addGap(189, 189, 189))
        );
        TitlePanelLayout.setVerticalGroup(
            TitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TitleLabel)
                .addContainerGap(16, Short.MAX_VALUE))
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

    private void CPUButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CPUButtonActionPerformed
        // TODO add your handling code here:
        try {
            conn=Test.ConnectDb();
            String query="SELECT cpu_company,cpu_model,cpu_price FROM `CPU`";
            DefaultListModel myTempList = new DefaultListModel();
            stmt=conn.createStatement();
            rs=stmt.executeQuery(query);
            while(rs.next()){
                String myTempString = rs.getString("cpu_company")+"    "+rs.getString("cpu_model")+"    "+rs.getString("cpu_price");
                myTempList.addElement(myTempString);
            }
            ProductList.setModel(myTempList);
            currentList=CurrentList.cpu;
        } catch (SQLException se) {
        //Handle errors for JDBC
        se.printStackTrace();
    }catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }//GEN-LAST:event_CPUButtonActionPerformed

    private void CPUButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CPUButtonMouseExited
        // TODO add your handling code here:
        CPUButton.setBackground(new java.awt.Color(51, 153, 255));
    }//GEN-LAST:event_CPUButtonMouseExited

    private void CPUButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CPUButtonMouseEntered
        // TODO add your handling code here:
        CPUButton.setBackground(new java.awt.Color(204, 255, 255));
    }//GEN-LAST:event_CPUButtonMouseEntered

    private void MOBOButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MOBOButtonMouseExited
        // TODO add your handling code here:
        MOBOButton.setBackground(new java.awt.Color(51, 153, 255));
    }//GEN-LAST:event_MOBOButtonMouseExited

    private void MOBOButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MOBOButtonMouseEntered
        // TODO add your handling code here:
        MOBOButton.setBackground(new java.awt.Color(204, 255, 255));
    }//GEN-LAST:event_MOBOButtonMouseEntered

    private void MOBOButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MOBOButtonActionPerformed
        // TODO add your handling code here:
        try {
            conn=Test.ConnectDb();
            String query="SELECT mobo_company,mobo_model,mobo_price FROM `MOTHERBOARD`";
            DefaultListModel myTempList = new DefaultListModel();
            stmt=conn.createStatement();
            rs=stmt.executeQuery(query);
            while(rs.next()){
                String myTempString = rs.getString("mobo_company")+"    "+rs.getString("mobo_model")+"    "+rs.getString("mobo_price");
                myTempList.addElement(myTempString);
            }
            ProductList.setModel(myTempList);
            currentList=CurrentList.mobo;
        } catch (SQLException se) {
        //Handle errors for JDBC
        se.printStackTrace();
    }catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }//GEN-LAST:event_MOBOButtonActionPerformed

    private void GPUButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GPUButtonMouseExited
        // TODO add your handling code here:
        GPUButton.setBackground(new java.awt.Color(51, 153, 255));
    }//GEN-LAST:event_GPUButtonMouseExited

    private void GPUButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GPUButtonMouseEntered
        // TODO add your handling code here:
         GPUButton.setBackground(new java.awt.Color(204, 255, 255));
    }//GEN-LAST:event_GPUButtonMouseEntered

    private void GPUButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GPUButtonActionPerformed
        // TODO add your handling code here:
        try {
            conn=Test.ConnectDb();
            String query="SELECT gpu_supplier,gpu_company,gpu_model,gpu_price FROM `GPU`";
            DefaultListModel myTempList = new DefaultListModel();
            stmt=conn.createStatement();
            rs=stmt.executeQuery(query);
            while(rs.next()){
                String myTempString = rs.getString("gpu_supplier")+"    "+rs.getString("gpu_company")+"    "+rs.getString("gpu_model")+"    "+rs.getString("gpu_price");
                myTempList.addElement(myTempString);
            }
            ProductList.setModel(myTempList);
            currentList=CurrentList.gpu;
        } catch (SQLException se) {
        //Handle errors for JDBC
        se.printStackTrace();
    }catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }//GEN-LAST:event_GPUButtonActionPerformed

    private void PSUButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PSUButtonMouseExited
        // TODO add your handling code here:
        PSUButton.setBackground(new java.awt.Color(51, 153, 255));
    }//GEN-LAST:event_PSUButtonMouseExited

    private void PSUButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PSUButtonMouseEntered
        // TODO add your handling code here:
        PSUButton.setBackground(new java.awt.Color(204, 255, 255));
    }//GEN-LAST:event_PSUButtonMouseEntered

    private void PSUButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PSUButtonActionPerformed
        // TODO add your handling code here:
        try {
            conn=Test.ConnectDb();
            String query="SELECT psu_company,psu_model,psu_price FROM `PSU`";
            DefaultListModel myTempList = new DefaultListModel();
            stmt=conn.createStatement();
            rs=stmt.executeQuery(query);
            while(rs.next()){
                String myTempString = rs.getString("psu_company")+"    "+rs.getString("psu_model")+"    "+rs.getString("psu_price");
                myTempList.addElement(myTempString);
            }
            ProductList.setModel(myTempList);
            currentList=CurrentList.psu;
        } catch (SQLException se) {
        //Handle errors for JDBC
        se.printStackTrace();
    }catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }//GEN-LAST:event_PSUButtonActionPerformed

    private void RAMButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RAMButtonMouseExited
        // TODO add your handling code here:
        RAMButton.setBackground(new java.awt.Color(51, 153, 255));
    }//GEN-LAST:event_RAMButtonMouseExited

    private void RAMButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RAMButtonMouseEntered
        // TODO add your handling code here:
        RAMButton.setBackground(new java.awt.Color(204, 255, 255));
    }//GEN-LAST:event_RAMButtonMouseEntered

    private void RAMButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RAMButtonActionPerformed
        // TODO add your handling code here:
        try {
            conn=Test.ConnectDb();
            String query="SELECT ram_company,ram_model,ram_price FROM `RAM`";
            DefaultListModel myTempList = new DefaultListModel();
            stmt=conn.createStatement();
            rs=stmt.executeQuery(query);
            while(rs.next()){
                String myTempString = rs.getString("ram_company")+"    "+rs.getString("ram_model")+"    "+rs.getString("ram_price");
                myTempList.addElement(myTempString);
            }
            ProductList.setModel(myTempList);
            currentList=CurrentList.ram;
        } catch (SQLException se) {
        //Handle errors for JDBC
        se.printStackTrace();
    }catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }//GEN-LAST:event_RAMButtonActionPerformed

    private void CASEButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CASEButtonMouseExited
        // TODO add your handling code here:
        CASEButton.setBackground(new java.awt.Color(51, 153, 255));
    }//GEN-LAST:event_CASEButtonMouseExited

    private void CASEButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CASEButtonMouseEntered
        // TODO add your handling code here:
        CASEButton.setBackground(new java.awt.Color(204, 255, 255));
    }//GEN-LAST:event_CASEButtonMouseEntered

    private void CASEButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CASEButtonActionPerformed
        // TODO add your handling code here:
        try {
            conn=Test.ConnectDb();
            String query="SELECT case_company,case_model,case_price FROM `CASE`";
            DefaultListModel myTempList = new DefaultListModel();
            stmt=conn.createStatement();
            rs=stmt.executeQuery(query);
            while(rs.next()){
                String myTempString = rs.getString("case_company")+"    "+rs.getString("case_model")+"    "+rs.getString("case_price");
                myTempList.addElement(myTempString);
            }
            ProductList.setModel(myTempList);
            currentList=CurrentList.chassis;
        } catch (SQLException se) {
        //Handle errors for JDBC
        se.printStackTrace();
    }catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }//GEN-LAST:event_CASEButtonActionPerformed

    private void SSDButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SSDButtonMouseExited
        // TODO add your handling code here:
        SSDButton.setBackground(new java.awt.Color(51, 153, 255));
    }//GEN-LAST:event_SSDButtonMouseExited

    private void SSDButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SSDButtonMouseEntered
        // TODO add your handling code here:
        SSDButton.setBackground(new java.awt.Color(204, 255, 255));
    }//GEN-LAST:event_SSDButtonMouseEntered

    private void SSDButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SSDButtonActionPerformed
        // TODO add your handling code here:
        try {
            conn=Test.ConnectDb();
            String query="SELECT ssd_company,ssd_model,ssd_price FROM `SSD`";
            DefaultListModel myTempList = new DefaultListModel();
            stmt=conn.createStatement();
            rs=stmt.executeQuery(query);
            while(rs.next()){
                String myTempString = rs.getString("ssd_company")+"    "+rs.getString("ssd_model")+"    "+rs.getString("ssd_price");
                myTempList.addElement(myTempString);
            }
            ProductList.setModel(myTempList);
            currentList=CurrentList.ssd;
        } catch (SQLException se) {
        //Handle errors for JDBC
        se.printStackTrace();
    }catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }//GEN-LAST:event_SSDButtonActionPerformed

    private void HDDButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HDDButtonMouseExited
        // TODO add your handling code here:
        HDDButton.setBackground(new java.awt.Color(51, 153, 255));
    }//GEN-LAST:event_HDDButtonMouseExited

    private void HDDButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HDDButtonMouseEntered
        // TODO add your handling code here:
        HDDButton.setBackground(new java.awt.Color(204, 255, 255));
    }//GEN-LAST:event_HDDButtonMouseEntered

    private void HDDButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HDDButtonActionPerformed
        // TODO add your handling code here:
        try {
            conn=Test.ConnectDb();
            String query="SELECT hdd_company,hdd_model,hdd_price FROM `HDD`";
            DefaultListModel myTempList = new DefaultListModel();
            stmt=conn.createStatement();
            rs=stmt.executeQuery(query);
            while(rs.next()){
                String myTempString = rs.getString("hdd_company")+"    "+rs.getString("hdd_model")+"    "+rs.getString("hdd_price");
                myTempList.addElement(myTempString);
            }
            ProductList.setModel(myTempList);
            currentList=CurrentList.hdd;
        } catch (SQLException se) {
        //Handle errors for JDBC
        se.printStackTrace();
    }catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }//GEN-LAST:event_HDDButtonActionPerformed

    private void EXTHDButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EXTHDButtonMouseExited
        // TODO add your handling code here:
        EXTHDButton.setBackground(new java.awt.Color(51, 153, 255));
    }//GEN-LAST:event_EXTHDButtonMouseExited

    private void EXTHDButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EXTHDButtonMouseEntered
        // TODO add your handling code here:
        EXTHDButton.setBackground(new java.awt.Color(204, 255, 255));
    }//GEN-LAST:event_EXTHDButtonMouseEntered

    private void EXTHDButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EXTHDButtonActionPerformed
        // TODO add your handling code here:
        try {
            conn=Test.ConnectDb();
            String query="SELECT exthd_company,exthd_model,exthd_price FROM `extHD`";
            DefaultListModel myTempList = new DefaultListModel();
            stmt=conn.createStatement();
            rs=stmt.executeQuery(query);
            while(rs.next()){
                String myTempString = rs.getString("exthd_company")+"    "+rs.getString("exthd_model")+"    "+rs.getString("exthd_price");
                myTempList.addElement(myTempString);
            }
            ProductList.setModel(myTempList);
            currentList=CurrentList.exthd;
        } catch (SQLException se) {
        //Handle errors for JDBC
        se.printStackTrace();
    }catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }//GEN-LAST:event_EXTHDButtonActionPerformed

    private void ShowPointsButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ShowPointsButtonMouseExited
        // TODO add your handling code here:
        ShowPointsButton.setBackground(new java.awt.Color(51, 153, 255));
    }//GEN-LAST:event_ShowPointsButtonMouseExited

    private void ShowPointsButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ShowPointsButtonMouseEntered
        // TODO add your handling code here:
        ShowPointsButton.setBackground(new java.awt.Color(204, 255, 255));
    }//GEN-LAST:event_ShowPointsButtonMouseEntered

    private void ShowPointsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowPointsButtonActionPerformed
        // TODO add your handling code here:
        try {
            conn=Test.ConnectDb();
            String query="SELECT id,available_points FROM `CUSTOMER_CARD` WHERE customer_id='"+id+"'";
            DefaultListModel myTempList = new DefaultListModel();
            stmt=conn.createStatement();
            rs=stmt.executeQuery(query);
            while(rs.next()){
                String myTempString = "Card id: "+rs.getString("id")+" Available points: "+rs.getString("available_points");
                myTempList.addElement(myTempString);
            }
            ProductList.setModel(myTempList);
            currentList=CurrentList.points;
        } catch (SQLException se) {
        //Handle errors for JDBC
        se.printStackTrace();
        }catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }//GEN-LAST:event_ShowPointsButtonActionPerformed

    private void OKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKButtonActionPerformed
        // TODO add your handling code here:
        try {
            String idstring=IDText.getText();
            email=EmailText.getText();
            id=Integer.parseInt(idstring);
            conn=Test.ConnectDb();
            String query="SELECT id,email FROM `CUSTOMER` WHERE id='"+id+"'&&email='"+email+"'";
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
        String[] parts=ProductList.getSelectedValue().split("    ");
        //cpu,mobo,gpu,psu,ram,chassis,ssd,hdd,exthd,points
        switch(currentList){
            case cpu:
                cpu="'"+parts[1]+"'";
                break;
            case mobo:
                mobo="'"+parts[1]+"'";
                break;
            case gpu:
                gpu="'"+parts[2]+"'";
                break;
            case psu:
                psu="'"+parts[1]+"'";
                break;
            case ram:
                ram="'"+parts[1]+"'";
                break;
            case chassis:
                chassis="'"+parts[1]+"'";
                break;
            case ssd:
                ssd="'"+parts[1]+"'";
                break;
            case hdd:
                hdd="'"+parts[1]+"'";
                break;
            case exthd:
                exthd="'"+parts[1]+"'";
                break;
        }
    }//GEN-LAST:event_ProductListMouseClicked

    private void OrderMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OrderMouseExited
        // TODO add your handling code here:
        Order.setBackground(new java.awt.Color(51, 153, 255));
    }//GEN-LAST:event_OrderMouseExited

    private void OrderMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OrderMouseEntered
        // TODO add your handling code here:
        Order.setBackground(new java.awt.Color(204, 255, 255));
    }//GEN-LAST:event_OrderMouseEntered

    private void OrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OrderActionPerformed
        // TODO add your handling code here:
        try {
            Together.setLocationRelativeTo(null);
            Together.setModal(true);
            Together.setVisible(true);
            conn=Test.ConnectDb();
            String query="INSERT INTO `ORDER`(customer_id,used_together,cpu_model,mobo_model,gpu_model,psu_model,ram_model,case_model,ssd_model,hdd_model,exthd_model) VALUES ("
                    +id+","+together+","+cpu+","+mobo+","+gpu+","+psu+","+ram+","+chassis+","+ssd+","+hdd+","+exthd+")";
            System.out.println(query);
            stmt=conn.createStatement();
            stmt.executeUpdate(query);
            together=null;
            cpu="NULL";
            mobo="NULL";
            gpu="NULL";
            psu="NULL";
            ram="NULL";
            chassis="NULL";
            ssd="NULL";
            hdd="NULL";
            exthd="NULL";
        } catch (SQLException se) {
	    //Handle errors for JDBC
	    se.printStackTrace();
	}catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }//GEN-LAST:event_OrderActionPerformed

    private void YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_YesActionPerformed
        // TODO add your handling code here:
        together="TRUE";
        Together.setVisible(false);
    }//GEN-LAST:event_YesActionPerformed

    private void NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NoActionPerformed
        // TODO add your handling code here:
        together="FALSE";
        Together.setVisible(false);
    }//GEN-LAST:event_NoActionPerformed

    /**
     * @param args the command line arguments
     */

    private javax.swing.JLabel WrongInputLabel;
    private javax.swing.JDialog LoginDialog;
    private javax.swing.JLabel LoginLabel;
    private javax.swing.JLabel IDLabel;
    private javax.swing.JLabel EmailLabel;
    private javax.swing.JTextField IDText;
    private javax.swing.JTextField EmailText;
    private javax.swing.JButton OKButton;
    private javax.swing.JButton CloseButton;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ActionsLabel;
    private javax.swing.JButton CASEButton;
    private javax.swing.JButton CPUButton;
    private javax.swing.JPanel CategoriesPanel;
    private javax.swing.JLabel CategoryLabel;
    private javax.swing.JButton EXTHDButton;
    private javax.swing.JButton GPUButton;
    private javax.swing.JButton HDDButton;
    private javax.swing.JButton MOBOButton;
    private javax.swing.JButton No;
    private javax.swing.JButton Order;
    private javax.swing.JButton PSUButton;
    private javax.swing.JList<String> ProductList;
    private javax.swing.JButton RAMButton;
    private javax.swing.JButton SSDButton;
    private javax.swing.JButton ShowPointsButton;
    private javax.swing.JLabel TitleLabel;
    private javax.swing.JPanel TitlePanel;
    private javax.swing.JDialog Together;
    private javax.swing.JButton Yes;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel question;
    // End of variables declaration//GEN-END:variables
}
