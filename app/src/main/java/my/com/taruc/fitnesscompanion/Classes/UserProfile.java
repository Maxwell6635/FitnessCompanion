package my.com.taruc.fitnesscompanion.Classes;

import java.sql.Blob;

import my.com.taruc.fitnesscompanion.Classes.DateTime;

/**
 * Created by saiboon on 12/12/2015.
 */
public class UserProfile {
    private String UserID, Email, Password, Name;
    private DateTime DOB;
    private String Gender;
    private double Initial_Weight, Height;
    private int Reward_Point;
    private DateTime Created_At;
    private String Image;

    public UserProfile() {}

    //Login Page
    public UserProfile(String email, String password) {
        this.Email = email;
        this.Password = password;
    }

    public UserProfile(String userID, String email, String password, String name, DateTime DOB, String gender, double initial_Weight, double height, int reward_Point, DateTime created_At, String image) {
        UserID = userID;
        Email = email;
        Password = password;
        Name = name;
        this.DOB = DOB;
        Gender = gender;
        Initial_Weight = initial_Weight;
        Height = height;
        Reward_Point = reward_Point;
        Created_At = created_At;
        Image = image;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public DateTime getDOB() {
        return DOB;
    }

    public void setDOB(DateTime DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public double getInitial_Weight() {
        return Initial_Weight;
    }

    public void setInitial_Weight(double initial_Weight) {
        Initial_Weight = initial_Weight;
    }

    public double getHeight() {
        return Height;
    }

    public void setHeight(double height) {
        Height = height;
    }

    public int getReward_Point() {
        return Reward_Point;
    }

    public void setReward_Point(int reward_Point) {
        Reward_Point = reward_Point;
    }

    public DateTime getCreated_At() {
        return Created_At;
    }

    public void setCreated_At(DateTime created_At) {
        Created_At = created_At;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int calAge(){
        if (DOB.getDateTime() != ""){
            int ThisYear = new DateTime().getCurrentDateTime().getDate().getYear();
            int DOBYear = DOB.getDate().getYear();
            return ThisYear - DOBYear;
        }else{
            return 0;
        }
    }
}
