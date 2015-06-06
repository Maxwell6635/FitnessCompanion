package my.com.saiboon.fitnesscompanion;

/**
 * Created by JACKSON on 5/25/2015.
 */
public class UserProfile {
    String email, name, DOB, gender, password, DOJ;
    int age, reward;
    Double weight ,height;

    public UserProfile(){
    }

    public UserProfile(String email, String name, String DOB, int age, String gender, Double height, Double weight, String password, String DOJ, int reward) {
        this.email = email;
        this.name = name;
        this.DOB = DOB;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.password = password;
        this.DOJ = DOJ;
        this.reward = reward;
    }

    public UserProfile(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
