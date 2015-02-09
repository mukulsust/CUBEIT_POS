/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.ButtonGroup;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Andromida
 */
public class warranty_search extends javax.swing.JFrame {

    connectDB cnnt = new connectDB();
    Connection conn;
    Statement stat;
    ResultSet res;
    ResultSet res2;
    Statement s;
    connectDB cdb;
    
    String initial_table_query = "SELECT `product_id`,`product_name`,`warranty_status`, `warranty`.`warranty_period`, `selling_price`, `selling_date`,`customer_name`, `contact_no`, ` address`, `email`  FROM ((`warranty` join `customer` using(`warranty_id`)) join `sell_rec` using(`customer_id`)) join `product` using(`product_id`);";
    
    public warranty_search() {
        initComponents();
        setAlwaysOnTop(true);
        
        setLocation(100, 50);        
        cnnt.myConnect();
        jTextField1.requestFocus(true);
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(jRadioButton1);
        bg.add(jRadioButton2);
        bg.add(jRadioButton3);
        
        Table(initial_table_query);
    }
    
    public void Table(String query) {
        
        try {
            cnnt.myConnect();

            stat = cnnt.getStatement();
            res = cnnt.getResultset();

            res = stat.executeQuery(query);

            DefaultTableModel dtm = new DefaultTableModel();

            dtm.setColumnIdentifiers(new String[]{"Product ID", "Product Name", "Warranty Status","Warranty Period", "Selling Price", "Date", "Customer Name",  "Contact No", "Address", "email"});

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
                dtm.setValueAt(r1.getInt(3), i, 2);
                dtm.setValueAt(r1.getInt(4), i, 3);
                dtm.setValueAt(r1.getInt(5), i, 4);
                dtm.setValueAt(r1.getDate(6), i, 5);
                dtm.setValueAt(r1.getString(7), i, 6);
                dtm.setValueAt(r1.getString(8), i, 7);
                dtm.setValueAt(r1.getString(9), i, 8);
                dtm.setValueAt(r1.getString(10), i, 9);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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
        jScrollPane1.setViewportView(jTable1);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setText("Search Warranty");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jRadioButton1.setText("By Product ID");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setText("Product Name");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jRadioButton3.setText("Customer's Contact No.");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 835, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(285, 285, 285)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jRadioButton1)
                                .addGap(10, 10, 10)
                                .addComponent(jRadioButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButton3))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1)))))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton3))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        jTextField1.requestFocus(true);
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        jTextField1.requestFocus(true);
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        jTextField1.requestFocus(true);
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String sql = null, input = jTextField1.getText();
        
        if (jRadioButton1.isSelected()) {
            sql = "SELECT `product_id`,`product_name`,`warranty_status`, `warranty`.`warranty_period`, `selling_price`, `selling_date`,`customer_name`, `contact_no`, ` address`, `email`  FROM ((`warranty` join `customer` using(`warranty_id`)) join `sell_rec` using(`customer_id`)) join `product` using(`product_id`) WHERE product_id = '"+input+"';";
            Table(sql);
        } else if (jRadioButton2.isSelected()) {
            sql = "SELECT `product_id`,`product_name`,`warranty_status`, `warranty`.`warranty_period`, `selling_price`, `selling_date`,`customer_name`, `contact_no`, ` address`, `email`  FROM ((`warranty` join `customer` using(`warranty_id`)) join `sell_rec` using(`customer_id`)) join `product` using(`product_id`) WHERE product_name = '"+input+"';";
            Table(sql);
        } else if (jRadioButton3.isSelected()) {
            sql = "SELECT `product_id`,`product_name`,`warranty_status`, `warranty`.`warranty_period`, `selling_price`, `selling_date`,`customer_name`, `contact_no`, ` address`, `email`  FROM ((`warranty` join `customer` using(`warranty_id`)) join `sell_rec` using(`customer_id`)) join `product` using(`product_id`) WHERE contact_no = '"+input+"';";
            Table(sql);
        }
        jTextField1.requestFocus(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        String sql = null, input = jTextField1.getText();
        
        if (jRadioButton1.isSelected()) {
            sql = "SELECT `product_id`,`product_name`,`warranty_status`, `warranty`.`warranty_period`, `selling_price`, `selling_date`,`customer_name`, `contact_no`, ` address`, `email`  FROM ((`warranty` join `customer` using(`warranty_id`)) join `sell_rec` using(`customer_id`)) join `product` using(`product_id`) WHERE product_id = '"+input+"';";
            Table(sql);
        } else if (jRadioButton2.isSelected()) {
            sql = "SELECT `product_id`,`product_name`,`warranty_status`, `warranty`.`warranty_period`, `selling_price`, `selling_date`,`customer_name`, `contact_no`, ` address`, `email`  FROM ((`warranty` join `customer` using(`warranty_id`)) join `sell_rec` using(`customer_id`)) join `product` using(`product_id`) WHERE product_name = '"+input+"';";
            Table(sql);
        } else if (jRadioButton3.isSelected()) {
            sql = "SELECT `product_id`,`product_name`,`warranty_status`, `warranty`.`warranty_period`, `selling_price`, `selling_date`,`customer_name`, `contact_no`, ` address`, `email`  FROM ((`warranty` join `customer` using(`warranty_id`)) join `sell_rec` using(`customer_id`)) join `product` using(`product_id`) WHERE contact_no = '"+input+"';";
            Table(sql);
        }
        jTextField1.requestFocus(true);
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
                new warranty_search().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
