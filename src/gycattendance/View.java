package gycattendance;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class View {

    private JButton updateButton = new JButton();
    private JButton loginButton = new JButton();
    private JButton newUserButton = new JButton();
    public ArrayList<JTextField> fields = new ArrayList<JTextField>();
    private JFrame vFrame = new JFrame();
    private ArrayList<Object> elems = null;
    private JTabbedPane panel = new JTabbedPane();
    private JPanel login = new JPanel();
    private JPanel newUser = new JPanel();

    public View() {

    }

    public View(ArrayList<Object> els) {
        elems = els;
    }

    public void fill() {

    }

    public void init() {
        vFrame.setSize(396, 312);
        vFrame.setResizable(false);
        login.setLayout(new BoxLayout(login, BoxLayout.Y_AXIS));
        newUser.setLayout(new BoxLayout(newUser, BoxLayout.Y_AXIS));
        ArrayList<JPanel> fieldPanels = new ArrayList<JPanel>();
        ArrayList<JLabel> labels = new ArrayList<JLabel>();
        String[] lbls = new String[]{"First Name / Nombre Primero", "Last Name / Apellido", "Grade / Grado", "School / Escuela", "<html>School ID#<br/>Número de identificación<br/>de la escuela</html>", "Username", "Password"};
        for (int i = 0; i < lbls.length; i++) {
            fieldPanels.add(new JPanel());
            labels.add(new JLabel(lbls[i]));
        }
        for (int i = 0; i < lbls.length; i++) {
            JPanel temp = fieldPanels.get(i);
            if (i <= 2) {
                temp.setLayout(new GridLayout(1, 2));
                labels.get(i).setForeground(Color.BLUE);
            } else if (i > 4) {
                temp.setLayout(new BoxLayout(temp, BoxLayout.X_AXIS));
                labels.get(i).setForeground(Color.BLUE);
            } else {
                temp.setLayout(new GridLayout(1, 2));
                labels.get(i).setFont(new Font(null, Font.BOLD, 13));
            }

            temp.setSize(vFrame.getWidth(), 50);
            JTextField jtf = new JTextField();
            if (i == lbls.length - 1) {
                jtf = new JPasswordField();
            }
            jtf.setMaximumSize(new Dimension(vFrame.getSize().width / 2, 40));

            fields.add(jtf);
            temp.add(jtf);
            temp.add(labels.get(i));
        }

        for (int i = 0;
                i < lbls.length - 2; i++) {
            newUser.add(fieldPanels.get(i));
        }
        for (int i = lbls.length - 2;
                i < lbls.length;
                i++) {
            login.add(fieldPanels.get(i));
        }

        updateButton.addActionListener(new BListener(3));
        newUserButton.addActionListener(
                new BListener(1));
        loginButton.addActionListener(
                new BListener(0));

        loginButton.setMaximumSize(
                new Dimension(100, 40));
        loginButton.setFont(getFont(20));
        loginButton.setText(
                "Login");
        login.add(loginButton);

        newUserButton.setMaximumSize(
                new Dimension(150, 40));
        newUserButton.setFont(getFont(20));
        newUserButton.setText(
                "Add me!");
        newUser.add(newUserButton);
//        newUser.add(updateButton);

        panel.setLayout(
                new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.addTab(
                "Login", login);
        panel.addTab(
                "Are You New Here?", newUser);
        panel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        panel.setFont(getFont(20));
        panel.setSize(vFrame.getSize());

        vFrame.add(panel);

        vFrame.setVisible(
                true);
        vFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private Font getFont(int size) {
        return new Font("Cooper Black", Font.PLAIN, size);
    }

    private void checkSize() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for (int i = 0; i < 1; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(20000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(View.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                            System.out.println(vFrame.getSize());
                        }
                    }).start();

                }
            }
        });
    }

}
