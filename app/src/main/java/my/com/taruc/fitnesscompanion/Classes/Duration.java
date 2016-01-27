package my.com.taruc.fitnesscompanion.Classes;

/**
 * Created by saiboon on 28/1/2016.
 */
public class Duration {
    private int years = 0;
    private int months = 0;
    private int days = 0;
    private int hours = 0;
    private int minutes = 0 ;
    private double seconds = 0 ;

    public Duration() {
    }

    public Duration(int years, int months, int days, int hours, int minutes, double seconds) {
        this.years = years;
        this.months = months;
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public double getSeconds() {
        return seconds;
    }

    public void setSeconds(double seconds) {
        this.seconds = seconds;
    }

    public void addYears(int inYear){
        years += inYear;
    }

    public void addMonths(int inMonth){
        int tempMonth = inMonth + months;
        if(tempMonth>12){
            addYears(tempMonth / 12);
            tempMonth = tempMonth % 12;
        }else if(tempMonth<0){
            addYears((tempMonth / 12)-1);
            tempMonth = 12 + tempMonth;
        }
        setMonths(tempMonth);
    }

    public void addDays(int inDate){
        int tempDate = getDays() + inDate;
        int NumberOfDAYInMonth = 30;
        if(tempDate>NumberOfDAYInMonth){
            addMonths(tempDate / NumberOfDAYInMonth);
            tempDate %= NumberOfDAYInMonth;
        }else if(tempDate<0){
            addMonths((tempDate / NumberOfDAYInMonth) - 1);
            tempDate = NumberOfDAYInMonth + tempDate;
        }
        setDays(tempDate);
    }

    public void addHours(int inHour){
        int tempHour = inHour + hours;
        if(tempHour>23){
            addDays(tempHour / 24);
            tempHour %= 24;
        }else if(tempHour<0){
            addDays((tempHour / 24) - 1);
            tempHour = 24 + tempHour;
        }
        setHours(tempHour);
    }

    public void addMinutes(int inMinutes){
        int tempMinutes = inMinutes + getMinutes();
        if(tempMinutes>59){
            addHours(tempMinutes / 60);
            tempMinutes %= 60;
        }else if(tempMinutes < 0){
            addHours((tempMinutes / 60) - 1);
            tempMinutes = 60 + tempMinutes;
        }
        setMinutes(tempMinutes);
    }

    public void addSeconds(double inSecond){
        double tempSecond = inSecond + getSeconds();
        if(tempSecond>59){
            addMinutes((int) tempSecond / 60);
            tempSecond %= 60;
        }else if(tempSecond < 0){
            addMinutes(((int) tempSecond / 60) - 1);
            tempSecond = 60 + tempSecond;
        }
        setSeconds(tempSecond);
    }

    public int getTotalSeconds(){
        int totalSeconds = (int)seconds + (minutes * 60) + (hours * 3600) + (days * 3600 * 24) + (months * 3600 * 24 * 30) + (years * 3600 * 24 * 30 * 12);
        return totalSeconds;
    }

    public String getDuration(){
        String myDurationString ="";
        if(years>0){
            myDurationString += years + " years ";
        }
        if(months>0){
            myDurationString += months + " months ";
        }
        if(days>0){
            myDurationString += days + " days ";
        }
        if(hours>0){
            myDurationString += hours + " hours ";
        }
        if(minutes>0){
            myDurationString += minutes + " minutes ";
        }
        if(seconds>0){
            myDurationString += seconds + " seconds ";
        }
        return myDurationString;
    }
}
