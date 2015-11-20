package gycattendance;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class GYCAttendance {

    public static DateFormat formatter = new SimpleDateFormat("MM-dd-yy", Locale.US);
    public static Set<User> db = new TreeSet<User>();
    public static View viewer = null;

    public static void main(String[] args) throws IOException {

        if (new File(CSVUpdate.localize()).exists()) {
            CSVUpdate.readIn();
        }
        invokeGUI();
    }

    private static void userTest() {
        User a = new User();
        a.setGrade("3");
        a.setSchool("avon Grove");
        a.setfName("Jay");
        a.setlName("Bhagat");
        User b = new User();
        b.setGrade("3");
        b.setSchool("avoan Grove");
        b.setfName("Jay");
        b.setlName("Bhagat");
        db.add(a);
        db.add(b);
        db.add(a);
    }

    private static void invokeGUI() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                viewer = new View();
                viewer.init();
            }
        });
    }

}
