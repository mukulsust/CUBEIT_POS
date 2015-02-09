/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos;

import com.sun.rowset.CachedRowSetImpl;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Andromida
 */
public class Loan extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame
     */
    connectDB cnnt = new connectDB();
    String lp = "";
    String ccl = "", bl = "";
    Statement stat = null, stat2 = null, stat1 = null;
    ResultSet res = null, res2 = null, res1 = null;
    CachedRowSetImpl crs;
    String date1 = "", date2 = "";
    Date today = null, q_date = null, d_date = null;
    DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
    Calendar currentDate = Calendar.getInstance(); //Get the current date
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //format it as per your requirement
    String dateNow = formatter.format(currentDate.getTime());

    public Loan() {

        initComponents();
        setAlwaysOnTop(true);
        cnnt.myConnect();

        //Bank name combo
        String b_name = null;
        stat = cnnt.getStatement();
        res = cnnt.getResultset();
        try {

            //jComboBox1.removeAllItems();
            //jComboBox2.removeAllItems();
            res = stat.executeQuery("select distinct bank_name from bank_loan");
            crs = new CachedRowSetImpl();
            crs.populate(res);
            while (crs.next()) {
                b_name = crs.getString("bank_name");
                jComboBox1.addItem(b_name);
                jComboBox2.addItem(b_name);
            }
            AutoCompleteDecorator.decorate(jComboBox1);
            jComboBox1.revalidate();
            jComboBox3.revalidate();
            //res.close();
            //stat.close();
        } catch (SQLException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }

        //table bl call
        bl = "SELECT `loan_id`, `bank_name`, `loan_type`, `account_number`, `loan_amount`, `interest_rate`, `issue_date` FROM `bank_loan` WHERE 1";

        Table_bl(bl);
        //available credit limit label
        acll();

        //table ccl call
        ccl = "SELECT `cc_id`, `date`, `withdrawal`, `deposit`, `balance`, `interest` FROM `cc_loan` WHERE loan_id like (select loan_id from bank_loan where bank_name like '" + jComboBox2.getSelectedItem().toString() + "' and loan_type like 'CC_LOAN')";

        Table_ccl(ccl);

    }

    public void Table_ccl(String query) {

        try {
            cnnt.myConnect();

            stat = cnnt.getStatement();
            res = cnnt.getResultset();

            res = stat.executeQuery(query);

            DefaultTableModel dtm = new DefaultTableModel();

            dtm.setColumnIdentifiers(new String[]{"ID", "Date", "Withdrawal", "Deposit", "Balance", "Interest", "Total Interest"});

            int size = 0;
            while (res.next()) {
                size++;
            }

            dtm.setRowCount(size);

            ResultSet r1 = cnnt.getResultset();
            Statement s1 = cnnt.getStatement();

            r1 = s1.executeQuery(query);

//            String qu="select interest from cc_loan where cc_id like(select MAX(cc_id) AS cc_id from cc_loan) and loan_id like (select loan_id from bank_loan where bank_name like '"+jComboBox2.getSelectedItem().toString()+"' and loan_type like 'CC_LOAN' )";
            Double t_intr = 0.0;
//            res= stat.executeQuery(qu);
//            
//            while(res.next()){
//                intr=res.getInt("interest");
//            }

            int i = 0;

            while (r1.next()) {
                dtm.setValueAt(r1.getInt(1), i, 0);
                dtm.setValueAt(r1.getDate(2), i, 1);
                dtm.setValueAt(r1.getInt(3), i, 2);
                dtm.setValueAt(r1.getInt(4), i, 3);
                dtm.setValueAt(r1.getInt(5), i, 4);
                dtm.setValueAt(r1.getInt(5) * r1.getInt(6) / 100.0, i, 5);
                t_intr = t_intr + r1.getInt(5) * r1.getInt(6) / 100.0;
                dtm.setValueAt(t_intr, i, 6);
                i++;

            }
            jTable2.setModel(dtm);

        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    public void Table_bl(String query) {

        try {
            cnnt.myConnect();

            stat = cnnt.getStatement();
            res = cnnt.getResultset();

            res = stat.executeQuery(query);

            DefaultTableModel dtm = new DefaultTableModel();

            dtm.setColumnIdentifiers(new String[]{"Bank ID", "Bank Name", "Loan Type", "Account Number", "Loan Amount", "Interest Rate", "Issue Date"});

            int size = 0;
            while (res.next()) {
                size++;
            }

            dtm.setRowCount(size);

            ResultSet r1 = cnnt.getResultset();
            Statement s1 = cnnt.getStatement();

            r1 = s1.executeQuery(query);

//            String qu="select interest from cc_loan where cc_id like(select MAX(cc_id) AS cc_id from cc_loan) and loan_id like (select loan_id from bank_loan where bank_name like '"+jComboBox2.getSelectedItem().toString()+"' and loan_type like 'CC_LOAN' )";
            Double intr = 0.0, intr_2 = 0.0;
//            res= stat.executeQuery(qu);
//            
//            while(res.next()){
//                intr=res.getInt("interest");
//            }

            int i = 0;
            while (r1.next()) {

                dtm.setValueAt(r1.getInt(1), i, 0);
                dtm.setValueAt(r1.getString(2), i, 1);
                dtm.setValueAt(r1.getString(3), i, 2);
                dtm.setValueAt(r1.getString(4), i, 3);
                dtm.setValueAt(r1.getInt(5), i, 4);
                dtm.setValueAt(r1.getInt(6), i, 5);
                dtm.setValueAt(r1.getDate(7), i, 6);
                //intr = r1.getInt(6)*r1.getInt(5)/100.0;
                i++;

            }
            jTable1.setModel(dtm);

        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    public void acll() {
        Integer balance = 0, amount = 0;
        stat1 = cnnt.getStatement();
        res1 = cnnt.getResultset();

        String q = "SELECT balance FROM `cc_loan` WHERE cc_id like (select MAX(cc_id) AS cc_id from cc_loan) and loan_id like (select loan_id from bank_loan where bank_name like '" + jComboBox2.getSelectedItem().toString() + "' and loan_type like 'CC_LOAN')";

        try {
            res1 = stat1.executeQuery(q);

            while (res1.next()) {
                balance = res1.getInt("balance");
            }
            //res.close();
            //stat.close();
        } catch (SQLException ex) {
            Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("acll balance "+balance);
        stat2 = cnnt.getStatement();
        res2 = cnnt.getResultset();

        q = "select loan_amount from bank_loan where bank_name like '" + jComboBox2.getSelectedItem().toString() + "' and loan_type like 'CC_LOAN'";

        try {
            res2 = stat2.executeQuery(q);

            while (res2.next()) {
                amount = res2.getInt("loan_amount");
            }
            // res.close();
            // stat.close();
        } catch (SQLException ex) {
            Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("acll amount "+amount);
        if (balance == null) {
            balance = 0;
        }
        System.out.println("Credit limit : " + (amount - balance));
        jLabel8.setText(amount - balance + "");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jTextField3 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Bank"));

        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.setFocusCycleRoot(true);
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        jComboBox1.setEditable(true);
        jComboBox1.setAutoscrolls(true);
        jComboBox1.setFocusCycleRoot(true);
        jComboBox1.setInheritsPopupMenu(true);
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Bank Name");

        jLabel3.setText("Loan Amount");

        jLabel4.setText("Interest Rate(%)");

        jLabel5.setText("Date");

        jLabel6.setText("Account Number");

        jLabel7.setText("Loan Type");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CC_LOAN", "PLAIN_LOAN" }));
        jComboBox3.setAutoscrolls(true);
        jComboBox3.setFocusCycleRoot(true);
        jComboBox3.setInheritsPopupMenu(true);
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox3, 0, 144, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField1)
                    .addComponent(jTextField2)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                    .addComponent(jTextField3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(0, 22, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTable1MouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("ADD");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("UPDATE");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("DELETE");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pos/refresh.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton4)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("LOAN Profile", jPanel2);

        jComboBox2.setEditable(true);
        jComboBox2.setAutoscrolls(true);
        jComboBox2.setFocusCycleRoot(true);
        jComboBox2.setInheritsPopupMenu(true);
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jLabel1.setText("Select Bank Name ");

        jLabel8.setBackground(new java.awt.Color(255, 255, 153));
        jLabel8.setToolTipText("");
        jLabel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setText("Available Credit Limit");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("New"));

        jDateChooser2.setDateFormatString("yyyy-MM-dd");
        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        jLabel11.setText("Withdrawal");

        jLabel10.setText("Date");

        jLabel12.setText("Deposit");

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton5.setText("Add");
        jButton5.setAlignmentY(0.0F);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton6.setText("Modify");
        jButton6.setAlignmentY(0.0F);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12))
                                .addGap(26, 26, 26)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pos/refresh.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 274, Short.MAX_VALUE)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9))
                    .addComponent(jComboBox2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("CC_LOAN", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // ADD BUTTON
        String s = "INSERT INTO `bank_loan`(`bank_name`, `loan_type`, `account_number`, `loan_amount`, `interest_rate`, `issue_date`)"
                + " VALUES ('" + jComboBox1.getSelectedItem().toString() + "','" + jComboBox3.getSelectedItem().toString() + "',"
                + "'" + jTextField3.getText() + "','" + jTextField1.getText() + "','" + jTextField2.getText() + "', '" + date1 + "')";

        stat = cnnt.getStatement();

        try {
            stat.executeUpdate(s);
            Table_bl(bl);
            JOptionPane.showMessageDialog(rootPane, "Data inserted!");
        } catch (SQLException ex) {
            Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:

        String s = "delete from bank_loan where loan_id like '" + jTable1.getValueAt(jTable1.getSelectedRow(), 0) + "'";

        try {
            stat.executeUpdate(s);
            Table_bl(bl);
            JOptionPane.showMessageDialog(rootPane, "Entry deleted!");
        } catch (SQLException ex) {
            Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        Table_ccl(ccl);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        Table_bl(bl);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // UPDATE BUTTON
        String s = "UPDATE `bank_loan` SET `bank_name`='" + jComboBox1.getSelectedItem().toString() + "',"
                + "`loan_type`='" + jComboBox3.getSelectedItem().toString() + "',`account_number`='" + jTextField3.getText() + "',"
                + "`loan_amount`='" + jTextField1.getText() + "',`interest_rate`='" + jTextField2.getText() + "',"
                + "`issue_date`='" + date1 + "' WHERE loan_id like '" + jTable1.getValueAt(jTable1.getSelectedRow(), 0) + "'";

        stat = cnnt.getStatement();

        try {
            stat.executeUpdate(s);
            Table_bl(bl);
            JOptionPane.showMessageDialog(rootPane, "Entry updated!");
        } catch (SQLException ex) {
            Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        // TODO add your handling code here:
        if ("date".equals(evt.getPropertyName())) {
            date1 = ((JTextField) jDateChooser1.getDateEditor().getUiComponent()).getText();
            //System.out.println("Date : " + date1);
        }
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:

        jComboBox1.setSelectedItem(jTable1.getValueAt(jTable1.getSelectedRow(), 1));
        jTextField3.setText(jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString());
        jComboBox3.setSelectedItem(jTable1.getValueAt(jTable1.getSelectedRow(), 2));
        jTextField1.setText(jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString());
        jTextField2.setText(jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString());

        jDateChooser2.setDate((Date) jTable1.getValueAt(jTable1.getSelectedRow(), 6));
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseEntered

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // ADD button on cc_loan
        res = cnnt.getResultset();
        stat = cnnt.getStatement();

        //loan_id getter
        String li = "";
        Integer wd = 0, dp = 0, ir = 0;
        String h = "select `loan_id`, `interest_rate` from bank_loan where bank_name like '" + jComboBox2.getSelectedItem().toString() + "'";

        try {
            res = stat.executeQuery(h);

            while (res.next()) {
                li = res.getString("loan_id");
                ir = res.getInt("interest_rate");
            }

            stat.closeOnCompletion();
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
        }
        //withdrawal and deposit adder
        Integer balance = 0;
        String s = "INSERT INTO `cc_loan`(`loan_id`, `date`, `withdrawal`, `deposit`, `balance`, `interest`) VALUES "
                + "(" + li + ",'" + date2 + "'," + jTextField4.getText() + "," + jTextField5.getText() + "," + balance + "," + ir + ")";

        try {
            stat.executeUpdate(s);
            stat.closeOnCompletion();
        } catch (SQLException ex) {
            Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
        }
        //balance getter
        String k = "select SUM(withdrawal) AS withdrawal, SUM(deposit) AS deposit from cc_loan";

        try {
            res = stat.executeQuery(k);

            while (res.next()) {
                wd = res.getInt("withdrawal");
                dp = res.getInt("deposit");
            }

            stat.closeOnCompletion();
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
        }

        balance = wd - dp;

        //balance updater
        //cc_id getter
        int ci=0;
        String bg="select MAX(cc_id) AS cc_id from cc_loan";
        try {
            res=stat.executeQuery(bg);
            while(res.next()){
                ci=res.getInt("cc_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String j = "UPDATE `cc_loan` SET `balance`=" + balance + " WHERE cc_id like "+ci+"";

        try {
            stat.execute(j);
            stat.closeOnCompletion();
            Table_ccl(ccl);
            JOptionPane.showMessageDialog(rootPane, "Added!");

        } catch (SQLException ex) {
            Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        // TODO add your handling code here:
        if ("date".equals(evt.getPropertyName())) {
            date2 = ((JTextField) jDateChooser2.getDateEditor().getUiComponent()).getText();
            System.out.println("Date : " + date2);
        }
    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // Modify button on cc_loan
        res = cnnt.getResultset();
        stat = cnnt.getStatement();

        //loan_id getter
        String li = "";
        Integer wd = 0, dp = 0, ir = 0;
        String h = "select `loan_id`, `interest_rate` from bank_loan where bank_name like '" + jComboBox2.getSelectedItem().toString() + "'";

        try {
            res = stat.executeQuery(h);

            while (res.next()) {
                li = res.getString("loan_id");
                ir = res.getInt("interest_rate");
            }

            stat.closeOnCompletion();
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
        }
        //withdrawal and deposit adder
        Integer balance = 0;
        String ci = jTable2.getValueAt(jTable2.getSelectedRow(), 0).toString();
//        String s="INSERT INTO `cc_loan`(`loan_id`, `date`, `withdrawal`, `deposit`, `balance`, `interest`) VALUES "
//                + "("+li+",'"+date2+"','"+jTextField4.getText()+"','"+jTextField5.getText()+"',"+balance+","+ir+")";
        System.out.println(date2);
        String s = "UPDATE `cc_loan` SET `loan_id`=" + li + ",`date`='" + date2 + "',"
                + "`withdrawal`=" + jTextField4.getText() + ",`deposit`=" + jTextField5.getText() + ","
                + "`interest`=" + ir + " WHERE cc_id like " + ci + "";
        try {
            stat.execute(s);
            stat.closeOnCompletion();
        } catch (SQLException ex) {
            Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
        }
        //balance getter
        String k = "select SUM(withdrawal) AS withdrawal, SUM(deposit) AS deposit from cc_loan";

        try {
            res = stat.executeQuery(k);

            while (res.next()) {
                wd = res.getInt("withdrawal");
                dp = res.getInt("deposit");
            }

            stat.closeOnCompletion();
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
        }

        balance = wd - dp;

        //balance updater
        String j = "UPDATE `cc_loan` SET `balance`=" + balance + " WHERE cc_id like " + ci + "";

        try {
            stat.execute(j);
            stat.closeOnCompletion();
            Table_ccl(ccl);
            JOptionPane.showMessageDialog(rootPane, "Updated!");

        } catch (SQLException ex) {
            Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
        table_cc_updater();
        ccl = "SELECT `cc_id`, `date`, `withdrawal`, `deposit`, `balance`, `interest` FROM `cc_loan` WHERE loan_id like (select loan_id from bank_loan where bank_name like '" + jComboBox2.getSelectedItem().toString() + "' and loan_type like 'CC_LOAN')";

        Table_ccl(ccl);

    }//GEN-LAST:event_jComboBox2ItemStateChanged
    private int calculateNumberOfDaysBetween(Date startDate, Date endDate) {
        System.out.println("date1 " + startDate);
        System.out.println("date2 " + endDate);
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("End date should be grater or equals to start date");
        }

        long startDateTime = startDate.getTime();
        long endDateTime = endDate.getTime();
        long milPerDay = 1000 * 60 * 60 * 24;

        int numOfDays = (int) ((endDateTime - startDateTime) / milPerDay); // calculate vacation duration in days

        return (numOfDays); // add one day to include start date in interval
    }

    public void table_cc_updater() {
        //current date getter
        res = cnnt.getResultset();
        stat = cnnt.getStatement();
        try {
            today = (Date) f.parse(dateNow);

        } catch (Exception ex) {
            Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
        }
        jDateChooser2.setDate(today);
        System.out.println("Today " + date2);
        //last entried date getter
        String s = "select `date` from cc_loan where cc_id like (select MAX(cc_id) AS cc_id from cc_loan)";

        try {
            res = stat.executeQuery(s);

            while (res.next()) {
                q_date = res.getDate("date");
            }

            stat.closeOnCompletion();
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("q_date prev " + q_date);

        if (q_date != null) {
            Calendar cl = Calendar.getInstance();
            cl.setTime(q_date);
            cl.add(Calendar.DATE, 1);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            String fkl = sdf2.format(cl.getTime());

            //date parser
            try {
                q_date = (Date) f.parse(fkl);
                System.out.println(q_date);
            } catch (Exception ex) {
                Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("q_date aft " + q_date);
            jDateChooser2.setDate(q_date);

        }
        Boolean comp = false;
        
        comp = today.equals(q_date);
        System.out.println("Comp " + comp);
        if (q_date != null && comp == false) {
            if (today.after(q_date)) {
                //day difference gen 
                Integer day_diff = calculateNumberOfDaysBetween(q_date, today);
                //System.out.println("Day_diff " + day_diff);
                //Date dt = q_date;
                while (q_date.before(today)) {
                    //insertion

                    res = cnnt.getResultset();
                    stat = cnnt.getStatement();

                    //loan_id getter
                    String li = "";
                    Integer wd = 0, dp = 0, ir = 0;
                    String h = "select `loan_id`, `interest_rate` from bank_loan where bank_name like '" + jComboBox2.getSelectedItem().toString() + "'";

                    try {
                        res = stat.executeQuery(h);

                        while (res.next()) {
                            li = res.getString("loan_id");
                            ir = res.getInt("interest_rate");
                        }

                        stat.closeOnCompletion();
                        res.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //minus balance adder
                    Integer balance = 0;
                    String gh = "";
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    gh = sdf.format(q_date);
                    System.out.println("GH " + gh);
                    String sr = "INSERT INTO `cc_loan`(`loan_id`, `date`, `withdrawal`, `deposit`, `balance`, `interest`) VALUES "
                            + "(" + li + ",'" + gh + "',0,0," + balance + "," + ir + ")";

                    try {
                        stat.executeUpdate(sr);
                        stat.closeOnCompletion();
                    } catch (SQLException ex) {
                        Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //balance getter
                    String k = "select SUM(withdrawal) AS withdrawal, SUM(deposit) AS deposit from cc_loan";

                    try {
                        res = stat.executeQuery(k);

                        while (res.next()) {
                            wd = res.getInt("withdrawal");
                            dp = res.getInt("deposit");
                        }

                        stat.closeOnCompletion();
                        res.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    balance = wd - dp;

                    //balance updater
                    String fk = "select MAX(cc_id) AS cc_id from cc_loan";
                    Integer ok = 0;
                    try {
                        res = stat.executeQuery(fk);

                        while (res.next()) {
                            ok = res.getInt("cc_id");
                        }

                        stat.closeOnCompletion();
                        res.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    String j = "UPDATE `cc_loan` SET `balance`=" + balance + " WHERE cc_id like " + ok + "";

                    try {
                        stat.execute(j);
                        stat.closeOnCompletion();
                        ccl = "SELECT `cc_id`, `date`, `withdrawal`, `deposit`, `balance`, `interest` FROM `cc_loan` WHERE loan_id like (select loan_id from bank_loan where bank_name like '" + jComboBox2.getSelectedItem().toString() + "' and loan_type like 'CC_LOAN')";

                        Table_ccl(ccl);
                        //JOptionPane.showMessageDialog(rootPane, "Added!");

                    } catch (SQLException ex) {
                        Logger.getLogger(Loan.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //date updater
                    Calendar c = Calendar.getInstance();

                    c.setTime(q_date);
                    c.add(Calendar.DATE, 1);
                    System.out.println("get time " + c.getTime());
                    String fg = sdf.format(c.getTime());

                    //date parser
                    try {
                        q_date = (Date) f.parse(fg);
                        System.out.println(q_date);
                    } catch (Exception ex) {
                        Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    jDateChooser2.setDate(q_date);
                    //q_date = f.parse(date2);
                }

            }
        }

    }

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
            java.util.logging.Logger.getLogger(Loan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Loan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Loan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Loan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Loan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
