package gycattendance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSVUpdate {

    public static void updateFile() {
        Writer writer = null;

        safeBackup();

        try {
            writer = new BufferedWriter(new FileWriter(new File(localize())));
        } catch (IOException ex) {
        }

        ArrayList<String> headers = new ArrayList<String>();
        headers.add("Grade");
        headers.add("First Name");
        headers.add("Last Name");
        headers.add("ID");
        headers.add("School");
        headers.add("First Logged In");
        headers.add("User Name");

        int count = 0;
        try {
            CSVReader.writeLine(writer, headers);
            for (User u : GYCAttendance.db) {
                CSVReader.writeLine(writer, u.csvOut());
            }

        } catch (Exception ex) {
        }
        try {
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(CSVUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void readIn() {
        List<Object> lines = null;
        Reader r = null;
        try {
            r = new BufferedReader(new FileReader(localize()));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        int count = 0;
        try {
            while ((lines = CSVReader.parseLine(r)) != null) {
                if(count==0){
                    count++;
                    continue;
                }
                GYCAttendance.db.add(new User().csvIn(lines));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String localize() {
        boolean isMac = System.getProperty("os.name").contains("Mac OS X");
        if (isMac) {
            return System.getProperty("user.home") + File.separator + "Documents" + File.separator + "GarageSignIn.csv";
        } else {
            return "E:\\garagetest.csv";
        }
    }

//    public static String localizeAuth() {
//        boolean isMac = System.getProperty("os.name").contains("Mac OS X");
//        if (isMac) {
//            return System.getProperty("user.home") + File.separator + "Documents" + File.separator + "auth.csv";
//        } else {
//            return "E:\\auth.csv";
//        }
//    }
//    public static void exportAuth() {
//        Writer writer = null;
//        try {
//            writer = new BufferedWriter(new FileWriter(new File(localizeAuth())));
//        } catch (IOException ex) {
//        }
//
//        try {
//            for (User u : GYCAttendance.db) {
//                List<String> tempList = new ArrayList<String>();
//                tempList.add(u.getUsername());
//                tempList.add(u.getPWHash());
//                CSVReader.writeLine(writer, tempList);
//            }
//
//        } catch (Exception ex) {
//        }
//        try {
//            writer.flush();
//            writer.close();
//        } catch (IOException ex) {
//            Logger.getLogger(CSVUpdate.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    private static void safeBackup() {
        if (new File(localize() + ".backup").exists()) {
            try {
                Files.copy(new File(localize() + ".backup").toPath(), new File(localize() + ".backup1").toPath());
            } catch (IOException ex) {
            }
        }
        try {
            Files.delete(new File(localize() + ".backup").toPath());
            Files.copy(new File(localize()).toPath(), new File(localize() + ".backup").toPath());
        } catch (IOException ex) {
        }
    }

}
