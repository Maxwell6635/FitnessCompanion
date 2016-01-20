package my.com.taruc.fitnesscompanion.Classes;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
        if(datetime != ""){
            stringToDateTime(datetime);
        }else {
            Log.i("DateTime Log", "Empty datetime string pass into DateTime.");
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

    public void setDateTime(String datetime){
        stringToDateTime(datetime);
    }

    public String getDateTimeString(){
        return date.getFullDateString() + " " + time.getFullTimeString();
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

    //special method for ichoice activity
    //example how to call this method: DateTime myDateTimeObject = new DateTime().iChoiceConversion("YOUR STRING");
    public DateTime iChoiceConversion(String inDateTime){
        DateTime ichoiceDateTime = new DateTime();
        try {
            String[] yearAndTheRest = inDateTime.split("年");
            date.setYear(Integer.parseInt(yearAndTheRest[0]));
            String[] monthAndTheRest = yearAndTheRest[1].split("月");
            date.setMonth(Integer.parseInt(monthAndTheRest[0]));
            String[] dateAndTheRest = monthAndTheRest[1].split("日:");
            date.setDateNumber(Integer.parseInt(dateAndTheRest[0]));
            String[] HourAndMinutes = dateAndTheRest[1].split(":");
            time.setHour(Integer.parseInt(HourAndMinutes[0]));
            time.setMinutes(Integer.parseInt(HourAndMinutes[1]));
            ichoiceDateTime = new DateTime(getDateTimeString());
        }catch (Exception ex){
            Log.i("DateTime Log", "IChoice datetime conversion Error. String passed in: " + inDateTime);
        }
        return ichoiceDateTime;
    }

    public void stringToDateTime(String datetime){
        String[] temp = datetime.split(" ");
        date = new Date(temp[0]);
        if(temp.length>1){
            time = new Time(temp[1]);
        }
    }

    public class Date{
        private int year;
        private int month;
        private int dateNumber;

        public Date(){}
        public Date(String input_date){
            String[] temp = input_date.split("-");
            System.out.print(temp[0]);
            try {
                setYear(Integer.parseInt(temp[0]));
                setMonth(Integer.parseInt(temp[1]));
                setDateNumber(Integer.parseInt(temp[2]));
            }catch(NumberFormatException numberEx) {
                System.out.print(numberEx);
            }
        }

        public int getYear() {
            return year;
        }

        public int getMonth() {
            return month;
        }

        public int getDateNumber() {
            return dateNumber;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public boolean setMonth(int inMonth) {
            if(inMonth>0 && inMonth<13) {
                month = inMonth;
                return true;
            }
            Log.i("DateTime Log", "Invalid month detected: " + inMonth);
            return false;
        }

        public boolean setDateNumber(int inDate) {
            if(inDate>0 && inDate<getNoOfDayInCurrentMonth()){
                this.dateNumber = inDate;
                return true;
            }
            Log.i("DateTime Log", "Invalid date Number detected: "+inDate);
            return false;
        }

        public void addYear(int inYear){
            year += inYear;
        }

        public void addMonth(int inMonth){
            int tempMonth = inMonth + month;
            if(tempMonth>12){
                addYear(tempMonth / 12);
                tempMonth = tempMonth % 12;
            }else if(tempMonth<=0){
                addYear((tempMonth / 12)-1);
                tempMonth = 12 + tempMonth;
            }
            setMonth(tempMonth);
        }

        public void addDateNumber(int inDate){
            int tempDate = getDateNumber() + inDate;
            int NumberOfDAYInMonth = getNoOfDayInCurrentMonth();
            if(tempDate>NumberOfDAYInMonth){
                addMonth(tempDate / NumberOfDAYInMonth);
                tempDate %= NumberOfDAYInMonth;
            }else if(tempDate<=0){
                addMonth((tempDate / NumberOfDAYInMonth) - 1);
                tempDate = getNoOfDayInCurrentMonth() + tempDate;
            }
            setDateNumber(tempDate);
        }

        public int getNoOfDayInCurrentMonth(){
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

        public String getFullDateString(){
            return String.format("%4d-%02d-%02d",year,month,dateNumber);
        }

        public String getTrimCurrentDateString(){
            return String.format("%02d%02d%4d",dateNumber,month,year);
        }
    }

    public class Time{
        private int hour = 0;
        private int minutes = 0;
        private double seconds = 0;

        public Time(){}
        public Time(String input_time){
            if (input_time.isEmpty()){
                Log.i("DateTime Log", "Empty time string pass into Time.");
            }else {
                String[] temp = input_time.split(":");
                setHour(Integer.parseInt(temp[0]));
                if (temp.length > 1) {
                    setMinutes(Integer.parseInt(temp[1]));
                    if (temp.length > 2) {
                        setSeconds(Double.parseDouble(temp[2]));
                    }
                }
            }
        }

        public int getHour() {
            return hour;
        }

        public int getMinutes() {
            return minutes;
        }

        public double getSeconds() {
            return seconds;
        }

        public boolean setHour(int inHour) {
            if(inHour>=0 && inHour<24) {
                this.hour = inHour;
                return true;
            }
            Log.i("DateTime Log", "Invalid hour detected: " + inHour);
            return false;
        }

        public void setHour24(int inHour){
            this.hour = inHour;
        }

        public boolean setMinutes(int inMinutes) {
            if(inMinutes>=0 && inMinutes < 60){
                this.minutes = inMinutes;
                return true;
            }
            Log.i("DateTime Log", "Invalid minutes detected: " + inMinutes);
            return false;
        }

        public boolean setSeconds(double inSeconds) {
            if(inSeconds>=0 && inSeconds<60) {
                this.seconds = inSeconds;
                return true;
            }
            Log.i("DateTime Log", "Invalid second detected: " + inSeconds);
            return false;
        }

        public void addHour(int inHour){
            int tempHour = inHour + getHour();
            if(tempHour>23){
                getDate().addDateNumber(tempHour / 24);
                tempHour %= 24;
            }else if(tempHour<0){
                getDate().addDateNumber((tempHour / 24)-1);
                tempHour = 24 + tempHour;
            }
            setHour(tempHour);
        }

        public void addMinutes(int inMinutes){
            int tempMinutes = inMinutes + getMinutes();
            if(tempMinutes>59){
                addHour(tempMinutes / 60);
                tempMinutes %= 60;
            }else if(tempMinutes < 0){
                addHour((tempMinutes / 60)-1);
                tempMinutes = 60 + tempMinutes;
            }
            setMinutes(tempMinutes);
        }

        public void addSecond(double inSecond){
            double tempSecond = inSecond + getSeconds();
            if(tempSecond>59){
                addMinutes((int)tempSecond / 60);
                tempSecond %= 60;
            }else if(tempSecond < 0){
                addMinutes(((int)tempSecond/60)-1);
                tempSecond = 60 + tempSecond;
            }
            setSeconds(tempSecond);
        }

        public String getFullTimeString(){
            return String.format("%02d:%02d:%02d",hour, minutes, Math.round(seconds));
        }

        /*public Time addDuration(int totalseconds){
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
        }*/
    }
}

