package pos;

import java.sql.Date;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;
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

public class sell_record extends javax.swing.JFrame {

    String[] seta = new String[50];
    String[] setb = new String[50];
    String[] setc = new String[50];
    String date1 = null, date2 = null;
    String st = null, cat = null;
    connectDB cnnt = new connectDB();
    Connection conn;
    Statement stat;
    ResultSet res;
    ResultSet res2;
    Statement s;
    connectDB cdb;
    Integer total = 0;
    int flag = 0;
    String initial_table_query = "SELECT `product_id`, `product_name`, `product_vendor`, `buying_price`, sell_rec.`selling_price`, `selling_date`, `category_main`, `category_sub`, `warranty_period` FROM ((product join buy_rec using(product_id)) join sell_rec using(product_id)) join category using(cat_id)";

    public sell_record() {
        initComponents();
        setAlwaysOnTop(true);
        setLocation(230, 0);
        jTextField1.setEnabled(false);

        Table(initial_table_query);

        ButtonGroup bg = new ButtonGroup();
        bg.add(jRadioButton1);
        bg.add(jRadioButton2);
        //bg.add(jRadioButton3);
    }

    public void Table(String query) {

        try {
            cnnt.myConnect();

            stat = cnnt.getStatement();
            res = cnnt.getResultset();

            res = stat.executeQuery(query);

            DefaultTableModel dtm = new DefaultTableModel();

            dtm.setColumnIdentifiers(new String[]{"Product ID", "Product Name", "Company", "Buying price", "Selling price", "Date", "Category", "Sub-category", "Warranty"});

            int size = 0;
            while (res.next()) {
                size++;
            }

            dtm.setRowCount(size);

            ResultSet r1 = cnnt.getResultset();
            Statement s1 = cnnt.getStatement();

            r1 = s1.executeQuery(query);

            int i = 0;
            while (r1.next()) {

                dtm.setValueAt(r1.getString(1), i, 0);
                dtm.setValueAt(r1.getString(2), i, 1);
                dtm.setValueAt(r1.getString(3), i, 2);
                dtm.setValueAt(r1.getInt(4), i, 3);
                dtm.setValueAt(r1.getInt(5), i, 4);
                dtm.setValueAt(r1.getDate(6), i, 5);
                dtm.setValueAt(r1.getString(7), i, 6);
                dtm.setValueAt(r1.getString(8), i, 7);
                dtm.setValueAt(r1.getInt(9), i, 8);

                i++;

            }
            jTable1.setModel(dtm);

        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Search by"));
        jPanel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel3.setText("To");

        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Print");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel2.setText("From");

        jDateChooser2.setDateFormatString("yyyy-MM-dd");
        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2changeDate2(evt);
            }
        });

        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1changeDate1(evt);
            }
        });

        jRadioButton1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Date");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jRadioButton2.setText("Product ID");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton3.setText("Exit");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Refresh");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jRadioButton2)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(9, 9, 9))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(3, 3, 3))
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(4, 4, 4)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButton2)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Product Name", "Company", "Quantity", "Buying Price", "Selling Price", "Date", "Category", "Sub-Category", "Warranty"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Total =");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("0");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 204, 204));
        jLabel6.setText("No. of Sold Product = ");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(204, 204, 204));
        jLabel7.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 829, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(14, 14, 14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (jRadioButton1.isSelected()) {
            System.out.println("Date from " + date1 + " to " + date2);
            st = "SELECT `product_id`, `product_name`, `product_vendor`, `buying_price`, sell_rec.`selling_price`, "
                    + "`selling_date`, `category_main`, `category_sub`, `warranty_period` "
                    + "FROM ((product join buy_rec using(product_id)) join sell_rec using(product_id)) "
                    + "join category using(cat_id) where selling_date BETWEEN '" + date1 + "' AND '" + date2 + "' ;";
            Table(st);
        } else if (jRadioButton2.isSelected()) {
            st = "SELECT `product_id`, `product_name`, `product_vendor`, `buying_price`, sell_rec.`selling_price`, "
                    + "`selling_date`, `category_main`, `category_sub`, `warranty_period` "
                    + "FROM ((product join buy_rec using(product_id)) join sell_rec using(product_id)) "
                    + "join category using(cat_id) where product_id = '" + jTextField1.getText() + "'";
            Table(st);
        }

        String s_sell;
        Integer sell = 0;
        total = 0;
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            s_sell = jTable1.getValueAt(i, 4).toString();
            sell = Integer.parseInt(s_sell);
            total += sell;
            System.out.println(s_sell);
        }
        System.out.println("Total = " + total);
        jLabel4.setText(total.toString());
        //System.out.println("Total product sell = "+jTable1.getRowCount());
        jLabel7.setText(jTable1.getRowCount() + "");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        cdb = new connectDB();
        cdb.myConnect();

        conn = cdb.getConnection();
        stat = cdb.getStatement();
        res = cdb.getResultset();

        try {
            String location = "C:\\report_buy_add.jrxml";

            try {
                InputStream fs = new FileInputStream(new File(location));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(record_buy_add.class.getName()).log(Level.SEVERE, null, ex);
            }
            InputStream is = this.getClass().getResourceAsStream(location);

            JasperDesign jd = JRXmlLoader.load(location);
            String query = null;
            if (flag == 1) {
                query = "Select `product_id`, `product_name`, `product_quantity`, `product_vendor`,"
                        + " `warranty_period`, `buying_price`, `selling_price`, `buying_date`,`category_main`, "
                        + "`category_sub` from (product join buy_rec using(product_id)) join category using(cat_id) "
                        + "WHERE buying_date BETWEEN '" + date1 + "' AND '" + date2 + "' ;";
            } else if (flag == 2) {
                query = "Select `product_id`, `product_name`, `product_quantity`, `product_vendor`,"
                        + " `warranty_period`, `buying_price`, `selling_price`, `buying_date`,`category_main`, "
                        + "`category_sub` from (product join buy_rec using(product_id)) join category using(cat_id) "
                        + "where product_id = '" + jTextField1.getText() + "';";
            }

            JRDesignQuery qur = new JRDesignQuery();
            qur.setText(query);
            jd.setQuery(qur);
            JasperReport jasperReport = JasperCompileManager.compileReport(jd);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, conn);

            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jDateChooser2changeDate2(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2changeDate2
        if ("date".equals(evt.getPropertyName())) {
            date2 = ((JTextField) jDateChooser2.getDateEditor().getUiComponent()).getText();
            System.out.println("Date : " + date2);
        }
    }//GEN-LAST:event_jDateChooser2changeDate2

    private void jDateChooser1changeDate1(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1changeDate1
        if ("date".equals(evt.getPropertyName())) {
            date1 = ((JTextField) jDateChooser1.getDateEditor().getUiComponent()).getText();
            System.out.println("Date : " + date1);
        }
    }//GEN-LAST:event_jDateChooser1changeDate1

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        jTextField1.setEnabled(false);
        jDateChooser1.setEnabled(true);
        jDateChooser2.setEnabled(true);
        flag = 1;
        //jComboBox1.setEnabled(false);
        //jComboBox2.setEnabled(false);
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        jTextField1.setEnabled(true);
        jDateChooser1.setEnabled(false);
        jDateChooser2.setEnabled(false);
        flag = 2;
        //jComboBox1.setEnabled(false);
        //jComboBox2.setEnabled(false);
        jTextField1.requestFocus(true);
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        Table(initial_table_query);
        jLabel4.setText("0");
        jLabel7.setText("0");
        flag = 0;
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        st = "SELECT `product_id`, `product_name`, `product_vendor`, `buying_price`, sell_rec.`selling_price`, "
                + "`selling_date`, `category_main`, `category_sub`, `warranty_period` "
                + "FROM ((product join buy_rec using(product_id)) join sell_rec using(product_id)) "
                + "join category using(cat_id) where product_id = '" + jTextField1.getText() + "'";
        Table(st);

        String s_sell;
        Integer sell = 0;
        total = 0;
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            s_sell = jTable1.getValueAt(i, 4).toString();
            sell = Integer.parseInt(s_sell);
            total += sell;
            System.out.println(s_sell);
        }
        System.out.println("Total = " + total);
        jLabel4.setText(total.toString());
        //System.out.println("Total product sell = "+jTable1.getRowCount());
        jLabel7.setText(jTable1.getRowCount() + "");
    }//GEN-LAST:event_jTextField1ActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                /*if ("Windows".equals(info.getName())) {
                 javax.swing.UIManager.setLookAndFeel(info.getClassName());
                 break;
                 }*/
                UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Category.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Category.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Category.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Category.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new sell_record().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
