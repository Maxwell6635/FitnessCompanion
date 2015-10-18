package my.com.saiboon.fitnesscompanion.Reminder;

/**
 * Created by saiboon on 12/7/2015.
 */
public class listTitle {
    public String title;
    public String choice;
    public listTitle(){};
    public listTitle(String Title, String choice){
        this.title=Title;
        this.choice=choice;
    }

    public String getTitle() {
        return title;
    }

    public String getChoice() {
        return choice;
    }

    public void setTitle(String title) {
        title = title;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }
}
