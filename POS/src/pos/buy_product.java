package pos;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.*;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.JFileChooser;
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
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.BarcodeUtil;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

public class buy_product extends javax.swing.JFrame {

    connectDB cnnt = new connectDB();
    int catid;
    String cat = null;
    Connection conn;
    Statement statement;
    ResultSet res;
    ResultSet res2;
    Statement s;
    ResultSet r;
    connectDB cdb;
    String date, date1;

    PreparedStatement pst = null;
    PreparedStatement pst2 = null;

    String[] seta = new String[1000];
    String[] setb = new String[1000];
    String[] setc = new String[1000];
    String[] setd = new String[1000];

    String product_id = null;
    boolean flag = false;

    Calendar currentDate = Calendar.getInstance(); //Get the current date
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //format it as per your requirement
    String dateNow = formatter.format(currentDate.getTime());
    Date today = null;
    DateFormat f = new SimpleDateFormat("yyyy-MM-dd");

    public buy_product() {

        cnnt.myConnect();
        Statement s1 = cnnt.getStatement();
        Statement s2 = cnnt.getStatement();
        Statement s3 = cnnt.getStatement();
        Statement s4 = cnnt.getStatement();

        ResultSet r1 = cnnt.getResultset();
        ResultSet r2 = cnnt.getResultset();
        ResultSet r3 = cnnt.getResultset();
        ResultSet r4 = cnnt.getResultset();

        setLocation(60, 50);

        initComponents();
        setAlwaysOnTop(true);
        Table();
        jRadioButton1.setSelected(true);

        try {
            today = (Date) f.parse(dateNow);
        } catch (Exception ex) {
            Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
        }
        jDateChooser1.setDate(today);

        //for main-category combobox itemlist
        int i = 0;
        try {
            r1 = s1.executeQuery("select distinct category_main from category");

            while (r1.next()) {
                seta[i] = r1.getString("category_main");
                //System.out.println(seta[i]);
                i++;
            }
            //System.out.println("i = " + (i - 1));

        } catch (SQLException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println("new " + set[0]);
        //System.out.println(set[1]);
        for (int j = 0; j < i; j++) {
            jComboBox1.addItem(seta[j]);
        }
        AutoCompleteDecorator.decorate(jComboBox1);
        jComboBox1.setEditable(true);
        seta = null;

        //for sub-category combobox itemlist
        cat = jComboBox1.getSelectedItem().toString();
        i = 0;
        try {
            r2 = s2.executeQuery("select distinct category_sub from category where category_main like '" + cat + "'");

            while (r2.next()) {
                setb[i] = r2.getString("category_sub");
                //System.out.println(setb[i]);
                i++;
            }
            //System.out.println("i = " + (i - 1));
            cat = null;

        } catch (SQLException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }
        jComboBox2.removeAllItems();
        //System.out.println("new " + set[0]);
        //System.out.println(set[1]);
        for (int j = 0; j < i; j++) {
            jComboBox2.addItem(setb[j]);
        }
        AutoCompleteDecorator.decorate(jComboBox2);
        jComboBox2.setEditable(true);
        jComboBox2.revalidate();
        setb = null;
        //company
        i = 0;
        try {
            r3 = s3.executeQuery("SELECT distinct company_name FROM company");

            while (r3.next()) {
                setc[i] = r3.getString("company_name");
                jComboBox3.addItem(setc[i]);

                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        AutoCompleteDecorator.decorate(jComboBox3);
        jComboBox3.setEditable(true);
        jComboBox3.revalidate();
        setb = null;

        //product_name
        try {
            r4 = s4.executeQuery("select distinct product_name from product");

            while (r4.next()) {
                setd[i] = r4.getString("product_name");
                jComboBox4.addItem(setd[i]);
                i++;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }
        AutoCompleteDecorator.decorate(jComboBox4);
        jComboBox4.revalidate();

        ButtonGroup bg = new ButtonGroup();
        bg.add(jRadioButton1);
        bg.add(jRadioButton2);
        truncate();
        temp_Table();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Product Name", "Quantity", "Company", "Invoice No", "Warranty", "Buying Price", "Selling Price", "Date", "Category", "Sub-Category"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1DemoChange(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Buying Record"));

        jLabel4.setText("Buying Price");

        jLabel3.setText("Quantity");

        jLabel1.setText("Product ID");

        jButton1.setText("SAVE");
        jButton1.setFocusable(false);
        jButton1.setRequestFocusEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel6.setText("Date");

        jLabel2.setText("Product Name");

        jButton2.setText("EXIT");
        jButton2.setFocusable(false);
        jButton2.setRequestFocusEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel5.setText("Selling Price");

        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.setFocusable(false);
        jDateChooser1.setRequestFocusEnabled(false);
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                changeDate(evt);
            }
        });

        jLabel7.setText("Company");

        jLabel8.setText("Warranty Time (days)");

        jLabel9.setText("Category");

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

        jLabel10.setText("Sub- category");

        jComboBox2.setEditable(true);
        jComboBox2.setAutoscrolls(true);
        jComboBox2.setFocusCycleRoot(true);
        jComboBox2.setInheritsPopupMenu(true);
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jButton3.setText("+");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("PRINT");
        jButton4.setFocusable(false);
        jButton4.setRequestFocusEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("REFRESH");
        jButton5.setFocusable(false);
        jButton5.setRequestFocusEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("+");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jComboBox3.setEditable(true);
        jComboBox3.setAutoscrolls(true);
        jComboBox3.setFocusCycleRoot(true);
        jComboBox3.setInheritsPopupMenu(true);

        jComboBox4.setEditable(true);
        jComboBox4.setAutoscrolls(true);
        jComboBox4.setFocusCycleRoot(true);
        jComboBox4.setInheritsPopupMenu(true);

        jLabel12.setText("Invoice No.");

        jLabel11.setText("Payment Method");

        jRadioButton1.setText("Cash");

        jRadioButton2.setText("Due");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(58, 58, 58)
                        .addComponent(jRadioButton1)
                        .addGap(10, 10, 10)
                        .addComponent(jRadioButton2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel6))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton3)))
                                .addGap(20, 20, 20))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jComboBox4, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField5)
                                    .addComponent(jTextField4)
                                    .addComponent(jTextField3)
                                    .addComponent(jTextField1))
                                .addGap(52, 52, 52))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(25, 25, 25)
                        .addComponent(jTextField7)
                        .addGap(55, 55, 55))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel7))
                                .addGap(75, 75, 75)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton6))
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel2)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel3)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel4)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Product Name", "Quantity", "Company", "Invoice No", "Warranty", "Buying Price", "Selling Price", "Date", "Category", "Sub-Category"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jButton7.setText("test");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 3, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 841, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(21, 21, 21))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1DemoChange(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1DemoChange
        /*int row = jTable1.getSelectedRow();
         int col = jTable1.getSelectedColumn();

         for (int i = 0; i < 5; i++) {
         String s = jTable1.getValueAt(row, 0).toString();
         System.out.println(s);
         }*/

        //System.out.println(row+" "+col);
    }//GEN-LAST:event_jTable1DemoChange

    public void Table() {
        try {
            cnnt.myConnect();

            s = cnnt.getStatement();
            r = cnnt.getResultset();

            r = s.executeQuery("Select * from record");

            DefaultTableModel dtm = new DefaultTableModel();

            dtm.setColumnIdentifiers(new String[]{"Product ID", "Product Name", "Quantity", "Company", "Invoice No", "Warranty", "Buying Price", "Selling Price", "Date", "Category", "Sub-Category"});

            int size = 0;
            while (r.next()) {
                size++;
            }

            dtm.setRowCount(size);

            ResultSet r1 = cnnt.getResultset();
            Statement s1 = cnnt.getStatement();

            r1 = s1.executeQuery("Select `product_id`, `product_name`, `product_quantity`, `product_vendor`, `invoice_no`, "
                    + " `warranty_period`, `buying_price`, `selling_price`, `buying_date`,`category_main`, "
                    + "`category_sub` from record"); //(product join buy_rec using(product_id)) join category using(cat_id)

            int i = 0;
            while (r1.next()) {

                dtm.setValueAt(r1.getString(1), i, 0);
                dtm.setValueAt(r1.getString(2), i, 1);
                dtm.setValueAt(r1.getInt(3), i, 2);
                dtm.setValueAt(r1.getString(4), i, 3);
                dtm.setValueAt(r1.getString(5), i, 4);
                dtm.setValueAt(r1.getInt(6), i, 5);
                dtm.setValueAt(r1.getInt(7), i, 6);
                dtm.setValueAt(r1.getInt(8), i, 7);
                dtm.setValueAt(r1.getDate(9), i, 8);
                dtm.setValueAt(r1.getString(10), i, 9);
                dtm.setValueAt(r1.getString(11), i, 10);
                i++;
            }

            jTable1.setModel(dtm);

        } catch (Exception e) {
            //System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    private void truncate() {
        s = cnnt.getStatement();
        String ss = "TRUNCATE TABLE temp_record";
        try {
            s.execute(ss);
        } catch (SQLException ex) {
            Logger.getLogger(sell_product.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void temp_Table() {
        try {
            cnnt.myConnect();

            s = cnnt.getStatement();
            r = cnnt.getResultset();

            r = s.executeQuery("Select * from temp_record");

            DefaultTableModel dtm = new DefaultTableModel();

            dtm.setColumnIdentifiers(new String[]{"Product ID", "Product Name", "Quantity", "Company", "Invoice No", "Warranty", "Buying Price", "Selling Price", "Date", "Category", "Sub-Category"});

            int size = 0;
            while (r.next()) {
                size++;
            }

            dtm.setRowCount(size);

            ResultSet r1 = cnnt.getResultset();
            Statement s1 = cnnt.getStatement();

            r1 = s1.executeQuery("Select `product_id`, `product_name`, `product_quantity`, `product_vendor`, `invoice_no`, "
                    + " `warranty_period`, `buying_price`, `selling_price`, `buying_date`,`category_main`, "
                    + "`category_sub` from temp_record"); //(product join buy_rec using(product_id)) join category using(cat_id)

            int i = 0;
            while (r1.next()) {

                dtm.setValueAt(r1.getString(1), i, 0);
                dtm.setValueAt(r1.getString(2), i, 1);
                dtm.setValueAt(r1.getInt(3), i, 2);
                dtm.setValueAt(r1.getString(4), i, 3);
                dtm.setValueAt(r1.getString(5), i, 4);
                dtm.setValueAt(r1.getInt(6), i, 5);
                dtm.setValueAt(r1.getInt(7), i, 6);
                dtm.setValueAt(r1.getInt(8), i, 7);
                dtm.setValueAt(r1.getDate(9), i, 8);
                dtm.setValueAt(r1.getString(10), i, 9);
                dtm.setValueAt(r1.getString(11), i, 10);
                i++;
            }

            jTable2.setModel(dtm);

        } catch (Exception e) {
            //System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    public void barcodeGenerate(String barc, String pat) {
        try {
            String input = barc;

            BarcodeUtil util = BarcodeUtil.getInstance();
            BarcodeGenerator gen = util.createBarcodeGenerator(buildCfg("code39"));

            OutputStream fout = new FileOutputStream(pat + "/" + input + ".png");
            int resolution = 200;
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                    fout, "image/png", resolution, BufferedImage.TYPE_BYTE_BINARY, false, 0);

            gen.generateBarcode(canvas, input);
            canvas.finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static Configuration buildCfg(String type) {
        DefaultConfiguration cfg = new DefaultConfiguration("barcode");

        //Bar code type
        DefaultConfiguration child = new DefaultConfiguration(type);
        cfg.addChild(child);

        //Human readable text position
        DefaultConfiguration attr = new DefaultConfiguration("human-readable");
        DefaultConfiguration subAttr = new DefaultConfiguration("placement");
        subAttr.setValue("bottom");
        attr.addChild(subAttr);

        child.addChild(attr);
        return cfg;
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        int reply = JOptionPane.showConfirmDialog(null, "Do you Want to BUY this PRODUCT?", "ADD Product!!", JOptionPane.YES_NO_OPTION);

        if (reply == JOptionPane.YES_OPTION) {
            Boolean b = false;

            barcodeGenerate(jTextField1.getText(), "barcode");
            flag = true;
            cnnt.myConnect();
            s = cnnt.getStatement();
            r = cnnt.getResultset();

            String product_id = jTextField1.getText(),
                    product_name = jComboBox4.getSelectedItem().toString(),
                    invoice_no = jTextField2.getText();
            String product_company = jComboBox3.getSelectedItem().toString();
            String category = jComboBox1.getSelectedItem().toString(), sub_category = jComboBox2.getSelectedItem().toString();

            ResultSet chack_name_r;
            Statement chack_name_s;
            chack_name_r = cnnt.getResultset();
            chack_name_s = cnnt.getStatement();
            String sq1 = "Select product_id from product WHERE product_id= '" + product_id + "'";
            String tname;
            Boolean flag1 = true;
            try {
                chack_name_r = chack_name_s.executeQuery(sq1);
                while (chack_name_r.next()) {
                    tname = chack_name_r.getString("product_id");
                    if (tname.equals(product_id)) {
                        flag1 = false;
                        System.out.println("Duplicate");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
            }

            // doesn't work :/
            if (flag1 == false) {
                JOptionPane.showMessageDialog(null, "Sorry! Duplicate Product ID. Please Select a new Product ID.");
            } else if (flag1) {

                //System.out.println("Product name = " + product_name);
                //System.out.println("Company = " + product_company);
                String s_quantity = jTextField3.getText(),
                        s_warranty = jTextField7.getText();
                Integer quantity = Integer.parseInt(s_quantity);

                if (s_warranty.isEmpty()) {
                    s_warranty = "0";
                }
                Integer warranty = Integer.parseInt(s_warranty);

                String s_buyPrice = jTextField4.getText();
                String s_sellPrice = jTextField5.getText();

                Integer i_buyPrice = Integer.parseInt(s_buyPrice);
                Integer i_sellPrice = Integer.parseInt(s_sellPrice);
                //System.out.println(s_buyPrice + " " + s_sellPrice);

                int total_buy = quantity * i_buyPrice;

                try {
                    res2 = cnnt.getResultset();
                    String query = "select cat_id from category where category_main like '" + category + "' and category_sub like '" + sub_category + "'";

                    res2 = s.executeQuery(query);
                    int h = 0;
                    while (res2.next()) {
                        catid = res2.getInt("cat_id");
                        h++;
                    }
                    String s_prod = "INSERT INTO product (product_id, product_name, product_quantity, product_vendor, invoice_no, warranty_period, cat_id) "
                            + "VALUES('" + product_id + "' , '" + product_name
                            + "' , " + quantity + " , '" + product_company + "' , '" + invoice_no + "', " + warranty + ", " + catid + ");";

                    s.executeUpdate(s_prod);

                    String sql_query = "INSERT INTO buy_rec (product_id, buying_price, selling_price, buying_date) "
                            + "VALUES('" + product_id + "', " + i_buyPrice + ", " + i_sellPrice + ", '" + date1 + "')";

                    s.executeUpdate(sql_query);

                    String query_record = "INSERT INTO `record`(`product_id`, `product_name`, `product_quantity`,"
                            + " `product_vendor`, `invoice_no`, `warranty_period`, `buying_price`, `selling_price`, `buying_date`, `category_main`,"
                            + " `category_sub`) VALUES('" + product_id + "' , '" + product_name
                            + "' , " + quantity + " , '" + product_company + "' , '" + invoice_no + "', " + warranty + "," + i_buyPrice + ","
                            + " " + i_sellPrice + ", '" + date1 + "', '" + category + "', '" + sub_category + "')";

                    s.executeUpdate(query_record);
                    //System.out.println("Record entered " + product_id);

                    query_record = "INSERT INTO `temp_record`(`product_id`, `product_name`, `product_quantity`,"
                            + " `product_vendor`, `invoice_no`, `warranty_period`, `buying_price`, `selling_price`, `buying_date`, `category_main`,"
                            + " `category_sub`) VALUES('" + product_id + "' , '" + product_name
                            + "' , " + quantity + " , '" + product_company + "' , '" + invoice_no + "', " + warranty + "," + i_buyPrice + ","
                            + " " + i_sellPrice + ", '" + date1 + "', '" + category + "', '" + sub_category + "')";
                    s.executeUpdate(query_record);

                    query_record = "INSERT INTO `buy_history`(`product_id`, `product_name`, `quantity`, `company`, `invoice_no`, `warranty`, `buying_price`, `selling_price`, `date`, `category`, `sub_category`) "
                            + " VALUES('" + product_id + "' , '" + product_name + "', " + quantity + ", '" + product_company + "', '" + invoice_no + "', " + warranty + ", " + i_buyPrice + ", " + i_sellPrice + ", '" + date1 + "', '" + category + "', '" + sub_category + "')";
                    //s.executeUpdate(query_record);

                    //company_due
                    int due = 0;
                    String sql = null;
                    ResultSet r5 = cnnt.getResultset();
                    Statement s5 = cnnt.getStatement();
                    int company_id = 0;
                    int purchase = i_buyPrice * quantity;
                    if (jRadioButton2.isSelected()) {
                        sql = "Select company_id from company where company_name='" + product_company + "'";

                        r5 = s5.executeQuery(sql);
                        while (r5.next()) {
                            company_id = r5.getInt("company_id");
                        }

                        sql = "INSERT INTO `company_transaction`(`company_id`, `purchase_date`, `purchase`, `due`, `payment`, `due_flag`) VALUES(" + company_id + ",'" + date1 + "'," + purchase + "," + purchase + "," + 0 + "," + true + ")";
                        s5.executeUpdate(sql);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(buy_product.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } finally {
                    Table();
                    temp_Table();
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Duplicate product ID!!! Can't be saved!!!");
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        Category c = new Category();
        c.show();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void changeDate(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_changeDate
        // TODO add your handling code here:
        if ("date".equals(evt.getPropertyName())) {
            date1 = ((JTextField) jDateChooser1.getDateEditor().getUiComponent()).getText();
            //System.out.println("Date : " + date1);
        }
    }//GEN-LAST:event_changeDate

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        if (flag == false) {
            JOptionPane.showMessageDialog(null, "Please Add a Product first!!");
        } else if (flag == true) {

            product_id = jTextField1.getText().toString();
            //System.out.println("Product ID = " + product_id);
            String query = "Select `product_id`, `product_name`, `product_quantity`, `product_vendor`,"
                    + " `warranty_period`, `buying_price`, `selling_price`, `buying_date`,`category_main`, "
                    + "`category_sub` from (product join buy_rec using(product_id)) join category using(cat_id) "
                    + "WHERE product_id = '" + product_id + "';";

            cdb = new connectDB();
            cdb.myConnect();

            conn = cdb.getConnection();
            statement = cdb.getStatement();
            res = cdb.getResultset();

            try {
                String location = "C:\\report_first_print.jrxml";

                try {
                    InputStream fs = new FileInputStream(new File(location));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(record_buy_add.class.getName()).log(Level.SEVERE, null, ex);
                }
                InputStream is = this.getClass().getResourceAsStream(location);

                JasperDesign jd = JRXmlLoader.load(location);

                JRDesignQuery qur = new JRDesignQuery();
                qur.setText(query);
                jd.setQuery(qur);
                JasperReport jasperReport = JasperCompileManager.compileReport(jd);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, conn);

                JasperViewer.viewReport(jasperPrint, false);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        jTextField1.setText(null);
        jTextField2.setText(null);
        jComboBox4.getSelectedIndex();
        jTextField3.setText(null);
        jTextField4.setText(null);
        jTextField5.setText(null);
        jComboBox3.setSelectedIndex(-1);
        jTextField7.setText(null);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        company_entry ce = new company_entry();
        ce.show();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        //    Category c = new Category();
        //  c.show();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        Statement s = cnnt.getStatement();
        ResultSet r = cnnt.getResultset();
        cat = jComboBox1.getSelectedItem().toString();
        //System.out.println("Combo box :" + cat);
        int k = 0;
        try {
            r = s.executeQuery("select distinct category_sub from category where category_main like '" + cat + "'");

            while (r.next()) {
                setc[k] = r.getString("category_sub");
                //System.out.println("hi" +k);
                k++;
            }
            //System.out.println("i = " +(i-1));

        } catch (SQLException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println("new " + set[0]);
        //System.out.println(set[1]);
        jComboBox2.removeAllItems();
        for (int j = 0; j < k; j++) {
            jComboBox2.addItem(setc[j]);
        }

        AutoCompleteDecorator.decorate(jComboBox2);
        jComboBox2.setEditable(true);
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        cnnt.myConnect();
        s = cnnt.getStatement();
        r = cnnt.getResultset();

        String product_id = jTextField1.getText(),
                product_name = jComboBox4.getSelectedItem().toString(),
                invoice_no = jTextField2.getText();
        String product_company = jComboBox3.getSelectedItem().toString();
        String category = jComboBox1.getSelectedItem().toString(), sub_category = jComboBox2.getSelectedItem().toString();

        String s_quantity = jTextField3.getText(),
                s_warranty = jTextField7.getText();
        Integer quantity = Integer.parseInt(s_quantity);

        if (s_warranty.isEmpty()) {
            s_warranty = "0";
        }
        Integer warranty = Integer.parseInt(s_warranty);

        String s_buyPrice = jTextField4.getText();
        String s_sellPrice = jTextField5.getText();

        Integer i_buyPrice = Integer.parseInt(s_buyPrice);
        Integer i_sellPrice = Integer.parseInt(s_sellPrice);

        String query_record = "INSERT INTO `buy_history`(`product_id`, `product_name`, `quantity`, `company`, `invoice_no`, `warranty`, `buying_price`, `selling_price`, `date`, `category`, `sub_category`) "
                + " VALUES('" + product_id + "' , '" + product_name + "', " + quantity + ", '" + product_company + "', '" + invoice_no + "', " + warranty + ", " + i_buyPrice + ", " + i_sellPrice + ", '" + date1 + "', '" + category + "', '" + sub_category + "')";

        try {
            s.executeUpdate(query_record);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton7ActionPerformed

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
                new buy_product().setVisible(true);
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
    private javax.swing.JComboBox jComboBox4;
    private com.toedter.calendar.JDateChooser jDateChooser1;
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
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables
}
