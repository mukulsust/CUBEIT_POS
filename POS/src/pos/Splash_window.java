package pos;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.*;

public class Splash_window implements Runnable {

    JProgressBar bar;
    JWindow window;
    public static Process p;

    public Splash_window() {
        showSplash();
        Thread t = new Thread(this);
        t.start();
        
        try {
            p = Runtime.getRuntime().exec("\"E:\\xampp\\xampp-control.exe\"");
            //p.waitFor();
        } catch (Exception ex) {
            ex.printStackTrace();
            /*JOptionPane.showMessageDialog(this, "please start xampp from\n"
                    + "c:/xampp/xampp-control\n"
                    + "or c:/xampp/xampp-control.exe\n", "xampp not found",
                        JOptionPane.ERROR_MESSAGE);*/
        }
    }

    private void showSplash() {
        window = new JWindow();
        window.setBounds(500, 250, 800, 450);
        window.setLocation(300, 150);
        JPanel content = (JPanel) window.getContentPane();

        JLabel splashimage = new JLabel(new ImageIcon("splash.JPG"));
        content.add(splashimage, BorderLayout.CENTER);
        bar = new JProgressBar(0, 2000);
        int i = 0;
        bar.setValue(i);
        bar.setBorderPainted(true);
        content.add(bar, BorderLayout.SOUTH);
        content.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 10));

        window.setVisible(true);
        try {
            while (i <= 3000) {
                bar.setValue(++i);
                Thread.sleep(1);
            }
        } catch (Exception ex) {
            System.out.println("SplashScreen Thread sleep exception");
        }
        window.setVisible(false);
        window.dispose();
    }

    public synchronized void initClasses() {
        connectDB cdb = new connectDB();
    }
    int i = 0;

    @Override
    public void run() {
        window.setVisible(true);
        try {
            initClasses();
            bar.setValue(++i);
            Thread.sleep(1);
        } catch (Exception ex) {
            System.out.println("SplashScreen Thread sleep exception");
        }
        window.setVisible(false);
        window.dispose();
    }
    public static void main(String[] args) {
        Splash_window sw = new Splash_window();
        sw.showSplash();
        
//        login l = new login();
//        l.show();
    }
}