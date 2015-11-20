package gycattendance;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.DatatypeConverter;

public class User implements Comparable {

    private List<Date> checkIns = new ArrayList<Date>();
    private String fName, lName, grade, school, id = "";
    private Date firstLogin = null;
    private String hashPW = "";
    private String uName = "";

    public User() {
        firstLogin = new Date(System.currentTimeMillis());
    }

    public void checkIn() {
        checkIns.add(new Date(System.currentTimeMillis()));
    }

    public void setPW(String pw) {
        hashPW = digest(pw);
    }

    public boolean checkPW(String pw) {
        if (hashPW.equals(digest(pw))) {
            return true;
        }
        return false;
    }

    public String getPWHash() {
        return hashPW;
    }

    public void setUsername(String name) {
        uName = name;
    }

    public String getUsername() {
        return uName;
    }

    public boolean isUser(String user) {
        return user.equals(uName);
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> csvOut() {
        List<String> fields = new ArrayList<String>();
        fields.add(grade);
        fields.add(fName);
        fields.add(lName);
        fields.add(id);
        fields.add(school);
        fields.add(GYCAttendance.formatter.format(firstLogin));
        fields.add(uName);
        fields.add(hashPW);
        for (Date d : checkIns) {
            fields.add(GYCAttendance.formatter.format(d));
        }
        return fields;
    }

    public User csvIn(List<Object> fields) {
        grade = fields.get(2).toString();
        fName = fields.get(0).toString();
        lName = fields.get(1).toString();
        id = fields.get(4).toString();
        school = fields.get(3).toString();
        try {
            firstLogin = GYCAttendance.formatter.parse(fields.get(5).toString());
        } catch (ParseException ex) {
            System.out.println("Date parse failed: " + fields.get(5).toString());
        }
        try {
            uName = fields.get(6).toString();
            hashPW = fields.get(7).toString();
        } catch (Exception e) {
        }
        if (fields.size() > 7) {
            for (int i = 8; i < fields.size(); i++) {
                try {
                    checkIns.add(GYCAttendance.formatter.parse(fields.get(i).toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        User u = (User) o;
        if ((u.fName.equals(fName)) && (u.lName.equals(lName)) && (u.grade.equals(grade)) && (u.school.equals(school))) {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Object o) {
        StackTraceElement[] cause = Thread.currentThread().getStackTrace();
        User u = (User) o;
        int res = lName.compareTo(u.lName);
        if (res == 0) {
            if (this.equals(u)) {
                return 0;
            } else {
                if (fName.compareTo(u.fName) == 0) {
                    if (grade.compareTo(u.grade) == 0) {
                        return 0;
                    } else {
                        return grade.compareTo(u.grade);
                    }
                } else {
                    return fName.compareTo(u.fName);
                }
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return "\nFirst Name:  " + fName + "\nLast Name: " + lName + "\nGrade: " + grade + "\nSchool: " + school;
    }

    public String digest(String input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
        }

        try {
            md.update(input.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
        }
        String str = "";
        try {
            str = DatatypeConverter.printBase64Binary(md.digest());
        } catch (Exception ex) {
        }
        return str;
    }

}
