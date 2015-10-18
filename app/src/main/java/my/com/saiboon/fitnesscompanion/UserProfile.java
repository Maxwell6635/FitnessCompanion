package my.com.saiboon.fitnesscompanion;

/**
 * Created by JACKSON on 5/25/2015.
 */
public class UserProfile {
    String email, name, DOB, gender, password, DOJ, id;
    int age, reward;
    Double weight, height;

    public UserProfile() {
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

    public UserProfile(String email, String password) {
        this.email = email;
        this.password = password;
    }


    //tsb add -- for add DA purpose
    public UserProfile(String id, String email, String name, String DOB, int age, String gender, Double height, Double weight, String password, String DOJ, int reward) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.DOB = DOB;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.password = password;
        this.DOJ = DOJ;
        this.weight = weight;
        this.reward = reward;
    }


    public String getDOJ() {
        return DOJ;
    }

    public void setDOJ(String DOJ) {
        this.DOJ = DOJ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
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

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
}


