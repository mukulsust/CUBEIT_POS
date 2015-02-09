package pos;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class ledger_company extends javax.swing.JFrame {

    connectDB cnnt = new connectDB();
    Connection conn;
    Statement stat;
    ResultSet res;
    String name = null, date1 = null, date2 = null;
    int id = 0, money = 0;
    Integer purchase = 0, paid = 0, due = 0, due2 = 0;

    Calendar currentDate = Calendar.getInstance(); //Get the current date
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //format it as per your requirement
    String dateNow = formatter.format(currentDate.getTime());
    Date today = null;
    DateFormat f = new SimpleDateFormat("yyyy-MM-dd");

    public ledger_company() {

        cnnt.myConnect();
        Statement s1 = cnnt.getStatement();
        ResultSet r1 = cnnt.getResultset();

        Statement s2 = cnnt.getStatement();
        ResultSet r2 = cnnt.getResultset();

        initComponents();
        setAlwaysOnTop(true);
        setLocation(280, 70);
        ButtonGroup bg = new ButtonGroup();
        bg.add(jRadioButton1);
        bg.add(jRadioButton2);

        try {
            today = (Date) f.parse(dateNow);
        } catch (Exception ex) {
            Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
        }
        jDateChooser2.setDate(today);
        jComboBox1.requestFocus(true);

        try {
            jComboBox1.removeAllItems();
            r1 = s1.executeQuery("select distinct company_name from company");

            while (r1.next()) {
                name = r1.getString("company_name");
                jComboBox1.addItem(name);
            }
            AutoCompleteDecorator.decorate(jComboBox1);
            jComboBox1.revalidate();

        } catch (SQLException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            name = null;
            jComboBox2.removeAllItems();
            r2 = s2.executeQuery("select distinct bank_name from bank_profile");

            while (r2.next()) {
                name = r2.getString("bank_name");
                jComboBox2.addItem(name);
            }
            AutoCompleteDecorator.decorate(jComboBox2);
            jComboBox2.revalidate();

        } catch (SQLException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (jRadioButton2.isSelected()) {
            jDateChooser3.isEnabled();
            jTextField5.isEnabled();
            jComboBox2.isEnabled();
        }
    }

    public void Table(String query) {

        try {
            cnnt.myConnect();

            stat = cnnt.getStatement();
            res = cnnt.getResultset();

            res = stat.executeQuery(query);

            DefaultTableModel dtm = new DefaultTableModel();

            dtm.setColumnIdentifiers(new String[]{"Date", "Description", "Purchase", "Paid", "Due"});
            int company_id = 0;
            int size = 0;
            while (res.next()) {
                size++;
                company_id = res.getInt("company_id");
                purchase += res.getInt("purchase");
                paid += res.getInt("payment");
                due += res.getInt("due");
            }
            due = purchase - paid;
            System.out.println("pur = " + purchase);
            System.out.println("paid = " + paid);
            System.out.println("due = " + due);

            dtm.setRowCount(size);

            ResultSet r2 = cnnt.getResultset();
            Statement s2 = cnnt.getStatement();

            ResultSet r1 = cnnt.getResultset();
            Statement s1 = cnnt.getStatement();

            r1 = s1.executeQuery(query);

            int i = 0;

            while (r1.next()) {
                dtm.setValueAt(r1.getDate(3), i, 0);
                dtm.setValueAt(r1.getString(4), i, 1);//description
                dtm.setValueAt(r1.getInt(5), i, 2);//purchase
                dtm.setValueAt(r1.getInt(6), i, 3);//payment
                dtm.setValueAt(r1.getInt(7), i, 4);// due
                i++;

            }
            jTable1.setModel(dtm);

        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jTextField5 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jComboBox1.setEditable(true);
        jComboBox1.setAutoscrolls(true);
        jComboBox1.setFocusCycleRoot(true);
        jComboBox1.setInheritsPopupMenu(true);
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jTextField1.setEditable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel1.setText("Company Name");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel2.setText("Address");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel3.setText("Contact No.");

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton1.setText("Show");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Description", "Purchase", "Paid", "Due"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Company Ledger");

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane3.setViewportView(jTextArea2);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 153, 153));
        jLabel5.setText("Total");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 153, 153));
        jLabel6.setText("Total");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 153, 153));
        jLabel7.setText("Total");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setText("Purchase =");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setText("PAID =");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setText("DUE =");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Due Payment"));

        jLabel11.setText("Total Due =");

        jLabel13.setText("Rest Amount =");

        jLabel12.setText("PAID =");

        jTextField2.setEditable(false);

        jTextField4.setEditable(false);

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jButton2.setText("Want to Pay ?");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel14.setText("Paid By");

        jRadioButton1.setText("Cash");
        jRadioButton1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton1ItemStateChanged(evt);
            }
        });

        jRadioButton2.setText("Bank Cheque");
        jRadioButton2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton2ItemStateChanged(evt);
            }
        });
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        jRadioButton2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jRadioButton2KeyPressed(evt);
            }
        });

        jTextField5.setEnabled(false);

        jComboBox2.setEnabled(false);

        jDateChooser3.setDateFormatString("yyyy-MM-dd");
        jDateChooser3.setEnabled(false);
        jDateChooser3.setFocusable(false);
        jDateChooser3.setOpaque(false);
        jDateChooser3.setRequestFocusEnabled(false);
        jDateChooser3.setVerifyInputWhenFocusTarget(false);
        jDateChooser3.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser3PropertyChange(evt);
            }
        });

        jLabel15.setText("Cheque No.");

        jLabel16.setText("Bank");

        jLabel17.setText("Cheque Sanction Date");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                            .addComponent(jTextField3)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGap(26, 26, 26)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(31, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jRadioButton1)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButton2))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jDateChooser3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(34, 34, 34))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(107, 107, 107))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jDateChooser2.setDateFormatString("yyyy-MM-dd");
        jDateChooser2.setFocusable(false);
        jDateChooser2.setOpaque(false);
        jDateChooser2.setRequestFocusEnabled(false);
        jDateChooser2.setVerifyInputWhenFocusTarget(false);
        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(318, 318, 318)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(292, 292, 292)
                            .addComponent(jLabel4)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(185, 185, 185)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jComboBox1, 0, 251, Short.MAX_VALUE)
                                            .addComponent(jScrollPane3)))))
                            .addGap(18, 269, Short.MAX_VALUE)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 779, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 74, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        /*Statement s1 = cnnt.getStatement();
         ResultSet r1 = cnnt.getResultset();
        
         int id;
         try {
            
         r1 = s1.executeQuery("select * from customer where customer_name = '"+jComboBox1.getSelectedItem().toString()+"'");

         while (r1.next()) {
         c_name = r1.getString("customer_name");
         id = r1.getInt("customer_id");
                
         System.out.println(id);
         }

         } catch (SQLException ex) {
         //Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
         }*/
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        /*Statement s1 = cnnt.getStatement();
         ResultSet r1 = cnnt.getResultset();
        
         int id;
         try {
            
         r1 = s1.executeQuery("select * from customer where customer_name = '"+jComboBox1.getSelectedItem().toString()+"'");

         while (r1.next()) {
         c_name = r1.getString("customer_name");
         id = r1.getInt("customer_id");
                
         System.out.println(id);
         }

         } catch (SQLException ex) {
         //Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
         }*/
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void kapjhap() {
        Statement s1 = cnnt.getStatement();
        ResultSet r1 = cnnt.getResultset();

        String address = null, contact_no = null, c_name = jComboBox1.getSelectedItem().toString();
        jButton2.setEnabled(true);

        try {

            r1 = s1.executeQuery("select * from company where company_name = '" + c_name + "'");

            while (r1.next()) {
                contact_no = r1.getString("contact_no");
                id = r1.getInt("company_id");

                address = r1.getString("company_address");
                System.out.println(address);
                System.out.println(contact_no);
                System.out.println(id);

            }
            jTextArea2.setText(address);
            jTextField1.setText(contact_no);

        } catch (SQLException ex) {
            //Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }
        String query = "SELECT * FROM company_transaction WHERE company_id = " + id;
        Table(query);
        //due=due2;
        jLabel5.setText(purchase.toString());
        jLabel6.setText(paid.toString());
        jLabel7.setText(due.toString());
        jTextField2.setText(due.toString());
        jTextField4.setText(due.toString());

        purchase = 0;
        paid = 0;
        due2 = due;
        due = 0;
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        kapjhap();
        jRadioButton1.setSelected(true);
        jTextField3.requestFocus(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String s_money = jTextField3.getText();
        money = Integer.parseInt(s_money);
        Statement s1 = cnnt.getStatement();
        ResultSet r1 = cnnt.getResultset();

        int flag1 = 1;
        if (due2 > money) {
            flag1 = 1;
        } else if (due2 <= money) {
            flag1 = 0;
        }
        int reply = JOptionPane.showConfirmDialog(null, "Do you want to PAID " + s_money + " Taka from the DUE of " + jComboBox1.getSelectedItem().toString() + "?", "DELETE Product!!", JOptionPane.YES_NO_OPTION);

        if (reply == JOptionPane.YES_OPTION) {

            //problem ache
            if (s_money == "") {
                JOptionPane.showMessageDialog(rootPane, "If you want to pay, you have to entry some money to PAY for the Company.");
            } else {

                if (jRadioButton2.isSelected()) {
                    String sql = null;
                    sql = "INSERT INTO `company_transaction` (`company_id`, `purchase_date`, `description`, `payment`, `purchase`, `due`, `due_flag`) "
                            + "VALUES (" + id + ", '" + date2 + "', '" + jTextField5.getText() + "', 0,0,0," + flag1 + ")";
                    try {
                        stat.executeUpdate(sql);
                    } catch (SQLException ex) {
                        Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //Update total due into company table
                    due2 -= money;

                    jTextField3.setText("");
                    
                    int t_id = 0;
                    try {
                        sql = "SELECT `trans_id` FROM `company_transaction` WHERE `company_id` = "+id+"";
                        r1 = s1.executeQuery(sql);

                        while (r1.next()) {
                            t_id = r1.getInt("trans_id");
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(ledger_company.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    String bank_name = jComboBox2.getSelectedItem().toString();
                    int bank_id = 0;
                    
                    try {
                        sql = "SELECT `bank_id` FROM `bank_profile` WHERE `bank_name` = '"+bank_name+"'";
                        r1 = s1.executeQuery(sql);

                        while (r1.next()) {
                            bank_id = r1.getInt("bank_id");
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(ledger_company.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    sql = "INSERT INTO `company_check`(`trans_id`, `check_no`, `bank_id`, `amount`, `date`) VALUES("+t_id+", '"+jTextField5.getText()+"', "+bank_id+", "+money+", '"+date2+"')";
                    try {
                        s1.executeUpdate(sql);
                    } catch (SQLException ex) {
                        Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //it will work later
                    
                    /*sql = "INSERT INTO `bank_transaction`(`bank_id`, `in_out`, `trans_date`, `receiver_donor`, `check_no`, `amount`) VALUES("+bank_id+", 1, '"+ date2 +"', '"+jComboBox1.getSelectedItem().toString()+"', '"+jTextField5.getText()+"', "+money+")";
                    try {
                        s1.executeUpdate(sql);
                    } catch (SQLException ex) {
                        Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                    } */               
                    
                    kapjhap();

                } else if (jRadioButton1.isSelected()) {
                    String sql = "INSERT INTO `company_transaction` (`company_id`, `purchase_date`,  `purchase`, `payment`, `due`, `due_flag`) "
                            + "VALUES (" + id + ", '" + date1 + "', 0, " + money + ", 0," + flag1 + ")";
                    try {
                        stat.executeUpdate(sql);

                    } catch (SQLException ex) {
                        Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //Update total due into company table
                    due2 -= money;

                    jTextField3.setText("");
                    kapjhap();
                }
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        // TODO add your handling code here:
        if ("date".equals(evt.getPropertyName())) {
            date1 = ((JTextField) jDateChooser2.getDateEditor().getUiComponent()).getText();
            System.out.println("Date : " + date1);
        }
    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
        String s_money = jTextField3.getText();
        money = Integer.parseInt(s_money);

        System.out.println("due2 " + due2);
        Integer rest = due2 - money;
        System.out.println(rest);
        jTextField4.setText(rest.toString());
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jDateChooser3PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser3PropertyChange
        // TODO add your handling code here:
        if ("date".equals(evt.getPropertyName())) {
            date2 = ((JTextField) jDateChooser3.getDateEditor().getUiComponent()).getText();
            System.out.println("Date : " + date2);
        }
    }//GEN-LAST:event_jDateChooser3PropertyChange

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
        jDateChooser3.setEnabled(true);
        jTextField5.setEnabled(true);
        jComboBox2.setEnabled(true);
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jRadioButton2KeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_jRadioButton2KeyPressed

    private void jRadioButton2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton2ItemStateChanged
        // TODO add your handling code here:
        /*if (evt.getStateChange() == ItemEvent.SELECTED) {
         jDateChooser3.setEnabled(true);
         jTextField5.setEnabled(true);
         jComboBox2.setEnabled(true);
         }*/
    }//GEN-LAST:event_jRadioButton2ItemStateChanged

    private void jRadioButton1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton1ItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            jDateChooser3.setEnabled(false);
            jTextField5.setEnabled(false);
            jComboBox2.setEnabled(false);
        }
    }//GEN-LAST:event_jRadioButton1ItemStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ledger_customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ledger_customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ledger_customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ledger_customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ledger_customer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
