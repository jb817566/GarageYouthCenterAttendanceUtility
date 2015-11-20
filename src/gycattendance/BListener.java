package gycattendance;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class BListener implements ActionListener {

    private JTextField userField = new JTextField();
    private JPasswordField pwField = new JPasswordField();
    private JPanel panel1 = null;
    private JButton linkedButton = null;
    private int type = 0;

    public BListener(int t) {
        userField.setMaximumSize(new Dimension(150, 30));
        pwField.setMaximumSize(new Dimension(150, 30));
        pwField.setPreferredSize(new Dimension(150, 30));
        userField.setPreferredSize(new Dimension(150, 30));
        type = t;
    }

    public BListener attach(JButton button) {
        linkedButton = button;
        return this;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (type == 1) {
            newUserAction();
        } else if (type == 0) {
            loginAction();
        } else if (type == 3) {
            CSVUpdate.updateFile();
        }
    }

    private void loginAction() {
        boolean foundUser = false;
        boolean dispWarning = false;
        List<JTextField> fields = GYCAttendance.viewer.fields.subList(GYCAttendance.viewer.fields.size() - 2, GYCAttendance.viewer.fields.size());
        String uName = fields.get(0).getText();
        String pw = new String(((JPasswordField) fields.get(1)).getPassword());
//        fields.get(0).setText("");
        fields.get(1).setText("");
        int idx = 0;
        Vector<Integer> found = new Vector<Integer>();
        for (User u : GYCAttendance.db) {
            if (u.isUser(uName) && u.checkPW(pw)) {
                found.add(idx);

            }
            idx++;
        }
//        System.out.println("Max count " + idx);
//        System.out.println("numFound " + found.size());
        if (found.size() == 0) {
            JLabel w = new JLabel("<html><center>Invalid Login<br/>Entrar inválido</center></html>");
            w.setFont(getFont(20));
            JOptionPane.showConfirmDialog(null, w, "Sorry", JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, getIcon("/resources/logo.png"));
            return;
        } else if (found.size() == 1) {
            String foundName = "";
            int count = 0;
            for (User u : GYCAttendance.db) {
                if (count == found.get(0)) {
                    u.checkIn();
                    foundName = u.getfName() + " " + u.getlName();
                }
                count++;
            }
            JLabel w = new JLabel("<html><center>Hey " + foundName +", You're Back!<br/>Hey, usted ha regresado!</center></html>");
            w.setFont(getFont(20));
            JOptionPane.showConfirmDialog(null, w, "Success!", JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, getIcon("/resources/logo.png"));
            CSVUpdate.updateFile();
            return;
        } else if (found.size() > 1) {
            ArrayList<User> records = new ArrayList<User>();
            int count = 0;
            for (User u : GYCAttendance.db) {
                for (Integer idxs : found) {
                    if (count == idxs) {
                        records.add(u);
                    }
                }
                count++;
            }
            JPanel userConf = new JPanel();
            userConf.setPreferredSize(new Dimension(450, 600));
            userConf.setLayout(new BoxLayout(userConf, BoxLayout.Y_AXIS));
            JTextArea textArea = new JTextArea("");
            textArea.setFont(getFont(20));
            JTextField userConfField = new JTextField();
            textArea.setPreferredSize(new Dimension(350, 600));
            textArea.setMaximumSize(new Dimension(350, 500));
            JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            textArea.append("Duplicate Credentials Detected.\nWhich Person Are You?\n(Enter a Number)\n\n");
            for (int i = 0; i < records.size(); i++) {
                textArea.append((i + 1) + ".  ");
                textArea.append("First Name: " + records.get(i).getfName() + "\n");
                textArea.append("    Last Name: " + records.get(i).getlName() + "\n");
                textArea.append("    Grade: " + records.get(i).getGrade() + "\n\n");
            }
            userConf.add(scroll);
            userConf.add(userConfField);
            boolean invalidEntry = true;
            String foundName = "";
            while (invalidEntry) {
                int op = JOptionPane.showConfirmDialog(null, userConf, "Which One Are You?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, getIcon("/resources/logo.png"));
                if (op == JOptionPane.OK_OPTION) {
                    int c = 0;
                    int sel = 0;
                    String f = userConfField.getText();
                    try {
                        sel = Integer.parseInt(f);
                        invalidEntry = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                        invalidEntry = true;
                        continue;
                    }

                    for (User u : GYCAttendance.db) {
                        if (sel - 1 == c) {
                            foundUser = true;
                            foundName = u.getfName() + " " + u.getlName();
                            u.checkIn();
                            break;
                        }
                        c++;
                    }
                }
            }
            //only if multiple entries found
            if (foundUser) {
                JLabel w = new JLabel("<html><center>Hey, " + foundName + " You're Back!<br/>Hey, usted ha regresado!</center></html>");
                w.setFont(getFont(20));
                JOptionPane.showConfirmDialog(null, w, "Success!", JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, getIcon("/resources/logo.png"));
            } else {
                JLabel w = new JLabel("<html><center>Incorrect Choice<br/>Elección inválido</center></html>");
                w.setFont(getFont(20));
                JOptionPane.showConfirmDialog(null, w, "Sorry", JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, getIcon("/resources/logo.png"));
            }
            CSVUpdate.updateFile();
        }
        CSVUpdate.updateFile();
    }

    private void newUserAction() {

        panel1 = new JPanel(new FlowLayout());
        panel1.add(new JLabel("Username"));
        panel1.add(userField);
        panel1.add(new JLabel("Password"));
        panel1.add(pwField);

        boolean dispWarning = false;
        boolean gradeOK = false;
        ArrayList<JTextField> fields = GYCAttendance.viewer.fields;
        if ((!fields.get(2).getText().matches("[0-9]+")) && (!(fields.get(2).getText().equals("")))) {
            JOptionPane.showConfirmDialog(null, "Enter a number for the Grade.\nEscribir un número para el Grado.", "ERROR!", JOptionPane.PLAIN_MESSAGE);
            gradeOK = false;
        } else {
            gradeOK = true;
        }
        for (int i = 0; i < fields.size() - 4; i++) {
//            System.out.println(fields.get(i).getText());
            if (fields.get(i).getText().equals("")) {
                dispWarning = true;
                break;
            }
        }
        if (dispWarning) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JLabel w = new JLabel("<html><center>Please fill out fields in BLUE.<br/>Por favor, rellene los casillas en AZUL.</center></html>");
                    w.setFont(getFont(20));
                    JOptionPane.showConfirmDialog(null, w, "ERROR!", JOptionPane.PLAIN_MESSAGE);
                }
            });
        } else {
            if (gradeOK) {
                boolean isDupe = false;

                User u = new User();
                u.setfName(fields.get(0).getText());
                u.setlName(fields.get(1).getText());
                u.setGrade(fields.get(2).getText());
                u.setSchool(fields.get(3).getText());
                u.setId(fields.get(4).getText());
                for(JTextField tf:fields){
                    tf.setText("");
                }
                for (Object temp : GYCAttendance.db.toArray()) {
                    if (temp.equals(u)) {
//                        System.out.println("Dupe");
                        isDupe = true;
                    }
                }
                if (!isDupe) {

                    int input = 1000;
                    while (input != JOptionPane.OK_OPTION) {
                        input = JOptionPane.showConfirmDialog(null, panel1, "Please set a login for fast Sign-In:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (input != JOptionPane.OK_OPTION) {
                            JLabel w = new JLabel("<html><center>You must enter sign-in information<br/>Debe introducir la información sesión!</center></html>");
                            w.setFont(getFont(20));
                            JOptionPane.showConfirmDialog(null, w, "Sorry!", JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE, getIcon("/resources/logo.png"));
                        }
                    }
                    String pw = "";
                    if (input == JOptionPane.OK_OPTION) {
                        for (char s : pwField.getPassword()) {
                            pw += s;
                        }
                    }
                    u.setPW(pw);
                    u.setUsername(userField.getText());
                    userField.setText("");
                    pwField.setText("");

                    GYCAttendance.db.add(u);
                    CSVUpdate.updateFile();
                    JLabel w = new JLabel("<html><center>Welcome to the Garage!<br/>Bienvenida a El Garaje!</center></html>");
                    w.setFont(getFont(20));
                    JOptionPane.showConfirmDialog(null, w, "Success!", JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, getIcon("/resources/logo.png"));
                } else {
                    JLabel w = new JLabel("<html><center>Your User Already Exists!<br/>Tu usuario ya existe!</center></html>");
                    w.setFont(getFont(20));
                    JOptionPane.showConfirmDialog(null, w, "Sorry!", JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE, getIcon("/resources/logo.png"));
                }

//                System.out.println("Size " + GYCAttendance.db.size());
            }
        }
    }

    private Font getFont(int size) {
        return new Font("Calibri", Font.BOLD, size);
    }

    private ImageIcon getIcon(String loc) {
        ImageIcon ic = new ImageIcon();
        try {
            ic.setImage(ImageIO.read(new File((this.getClass().getResource(loc)).toURI())));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ic;
    }

}