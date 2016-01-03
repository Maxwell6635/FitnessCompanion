package my.com.taruc.fitnesscompanion.Classes;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by saiboon on 19/9/2015.
 */
public class DateTime {

    private Date date = new Date("1994-01-01");
    private Time time = new Time("00:00");

    public DateTime(){}
    public DateTime(String datetime){
        if(datetime == ""){

        }else {
            stringToDateTime(datetime);
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = new Date(date);
    }

    public Time getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = new Time(time);
    }

    public String getDateTime(){
        return date.getFullDate() + " " + time.getFullTime();
    }

    public void setDateTime(String datetime){
        stringToDateTime(datetime);
    }

    public DateTime getCurrentDateTime(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String mydate = dateformat.format(calendar.getTime());
        String mytime = hour + ":" + min;
        return new DateTime(mydate + " " + mytime);
    }

    public String getTrimCurrentDateString(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("ddMMyyyy");
        String mydate = dateformat.format(calendar.getTime());
        return mydate;
    }

    void stringToDateTime(String datetime){
        String[] temp = datetime.split(" ");
        date = new Date(temp[0]);
        if(temp.length>1){
            time = new Time(temp[1]);
        }
    }

    public class Date{
        private int year;
        private int month;
        private int date;

        public Date(){}
        public Date(String input_date){
            String[] temp = input_date.split("-");
            System.out.print(temp[0]);
            try {
                this.year = Integer.parseInt(temp[0]);
                this.month = Integer.parseInt(temp[1]);
                this.date = Integer.parseInt(temp[2]);
            }catch(NumberFormatException numberEx) {
                System.out.print(numberEx);
            }
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int inMonth) {
            if(inMonth>12){
                int addToYear = inMonth / 12;
                setYear(getYear()+addToYear);
                inMonth = inMonth % 12;
            }else if(inMonth<=0){
                setYear(getYear()+(inMonth / 12)-1);
                inMonth = 12 + inMonth;
            }
            this.month = inMonth;
        }

        public int getDate() {
            return date;
        }

        public void setDate(int inDate) {
            inDate = updateDate(getMonthNoOfDate(),inDate);
            this.date = inDate;
        }

        public int updateDate(int NumberOfDAYInMonth, int inDate){
            if(inDate>NumberOfDAYInMonth){
                int addToMonth = inDate / NumberOfDAYInMonth;
                setMonth(getMonth()+addToMonth);
                inDate %= NumberOfDAYInMonth;
            }else if(inDate<=0){
                setMonth(getMonth()+(inDate/NumberOfDAYInMonth)-1);
                inDate = getMonthNoOfDate() + inDate;
            }
            return inDate;
        }

        public int getMonthNoOfDate(){
            if(month==2){
                if(isLeapYear()){
                    return 29;
                }else{
                    return 28;
                }
            }else if(month==4 || month==6 || month==9 || month==11){
                return 30;
            }else{
                return 31;
            }
        }

        public boolean isLeapYear(){
            if(this.year % 4 ==0){
                if(this.year % 100 ==0){
                    if(this.year %400 ==0){
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    return true;
                }
            }else{
                return false;
            }
        }

        public String getFullDate(){
            return String.format("%4d-%02d-%02d",year,month,date);
        }
    }

    public class Time{
        private int hour = 0;
        private int minutes = 0;
        private double seconds = 0;

        public Time(){}
        public Time(String input_time){
            if (input_time.isEmpty()){

            }else {
                String[] temp = input_time.split(":");
                this.hour = Integer.parseInt(temp[0]);
                if (temp.length > 1) {
                    this.minutes = Integer.parseInt(temp[1]);
                    if (temp.length > 2) {
                        this.seconds = Double.parseDouble(temp[2]);
                    }
                }
            }
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
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

        public String getFullTime(){
            return String.format("%02d:%02d:%02d",hour, minutes, Math.round(seconds));
        }

        public void addHour(int hour){
            this.hour += hour;
            if(this.hour>23){
                int extraDay = this.hour / 24;
                getDate().setDate(getDate().getDate()+extraDay);
                this.hour %= 24;
            }
        }

        public Time addDuration(int totalseconds){
            int addHour = 0;
            int addMin = 0;
            int addSec = 0;
            int newHour = hour;
            int newMin = minutes;
            double newSec = seconds;
            if (totalseconds>=60){
                addMin = totalseconds / 60;
                addSec = totalseconds % 60;
                if (addMin >= 60){
                    addHour = addMin / 60;
                    addMin = addMin % 60;
                }
            }else{
                addSec = totalseconds;
            }
            newSec += addSec;
            if (newSec >= 60){
                newMin += newSec/60;
                newSec = newSec%60;
            }
            newMin += addMin;
            if (newMin >= 60){
                newHour += newMin/60;
                newMin = newMin%60;
            }
            newHour += addHour;
            if(newHour>24){
                getDate().setDate(getDate().getDate()+(newHour/24));
            }
            return new Time(newHour + ":" + newMin + ":" + newSec);
        }

    }
}

