package my.com.taruc.fitnesscompanion.Classes;

import android.content.Context;

import my.com.taruc.fitnesscompanion.Database.UserProfileDA;

/**
 * Created by saiboon on 22/1/2016.
 */
public class FitnessFormula {

    Context context;

    public FitnessFormula(Context context) {
        this.context = context;
    }

    public double getDistance(int stepCount){
        //step = 0.45 * Height
        //url http://stackoverflow.com/questions/22292617/how-to-calculate-distance-while-walking-in-android
        UserProfileDA userProfileDA = new UserProfileDA(context);
        UserProfile userProfile = userProfileDA.getUserProfile2();
        return stepCount * (0.414 * userProfile.getHeight()) / 100; // return meter
    }

    public Duration calculationDuration(DateTime startDateTime, DateTime endDateTime){
        Duration myDuration = new Duration();
        double secondsDuration;
        int duration = 0;
        if(isValidStartEndDateTime(startDateTime, endDateTime)){
            //cal seconds
            secondsDuration = endDateTime.getTime().getSeconds() - startDateTime.getTime().getSeconds();
            if(secondsDuration<0){
                secondsDuration += 60;
                endDateTime.getTime().addMinutes(-1);
            }
            myDuration.addSeconds(secondsDuration);
            //cal minutes
            duration = endDateTime.getTime().getMinutes() - startDateTime.getTime().getMinutes();
            if(duration<0){
                duration += 60;
                endDateTime.getTime().addHour(-1);
            }
            myDuration.addMinutes(duration);
            //cal hour
            duration = endDateTime.getTime().getHour() - startDateTime.getTime().getHour();
            if(duration<0){
                duration += 24;
                endDateTime.getDate().addDateNumber(-1);
            }
            myDuration.addHours(duration);
            //cal days
            duration = endDateTime.getDate().getDateNumber() - startDateTime.getDate().getDateNumber();
            if(duration<0){
                duration += 30;
                endDateTime.getDate().addMonth(-1);
            }
            myDuration.addDays(duration);
            //cal months
            duration = endDateTime.getDate().getMonth() - startDateTime.getDate().getMonth();
            if(duration<0){
                duration += 30;
                endDateTime.getDate().addYear(-1);
            }
            myDuration.addMonths(duration);
            //cal Years
            duration = endDateTime.getDate().getYear() - startDateTime.getDate().getYear();
            myDuration.addYears(duration);
        }
        return myDuration;
    }

    public boolean isValidStartEndDateTime(DateTime startDateTime, DateTime endDateTime){
        //check year
        if(startDateTime.getDate().getYear() > endDateTime.getDate().getYear()){
            return false;
        }else if(startDateTime.getDate().getYear() == endDateTime.getDate().getYear()){
            //check month
            if(startDateTime.getDate().getMonth() > endDateTime.getDate().getMonth()){
                return false;
            }else if(startDateTime.getDate().getMonth() == endDateTime.getDate().getMonth()){
                //check date
                if(startDateTime.getDate().getDateNumber() > endDateTime.getDate().getDateNumber()){
                    return false;
                }else if(startDateTime.getDate().getDateNumber() == endDateTime.getDate().getDateNumber()){
                    //check hour
                    if(startDateTime.getTime().getHour() > endDateTime.getTime().getHour()){
                        return false;
                    }else if(startDateTime.getTime().getHour() == endDateTime.getTime().getHour()){
                        //check minutes
                        if(startDateTime.getTime().getMinutes() > endDateTime.getTime().getMinutes()){
                            return false;
                        }else if(startDateTime.getTime().getMinutes() == endDateTime.getTime().getMinutes()){
                            //check seconds
                            if(startDateTime.getTime().getSeconds() > endDateTime.getTime().getSeconds()){
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
