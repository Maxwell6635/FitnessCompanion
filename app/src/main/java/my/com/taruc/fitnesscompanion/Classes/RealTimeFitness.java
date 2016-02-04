package my.com.taruc.fitnesscompanion.Classes;

/**
 * Created by saiboon on 10/6/2015.
 */
public class RealTimeFitness {

    private String RealTimeFitnessID, UserID;
    private DateTime CaptureDateTime = new DateTime();
    private int StepNumber;

    public RealTimeFitness(){}

    public RealTimeFitness(String realTimeFitnessID, String userID, DateTime captureDateTime, int stepNumber) {
        RealTimeFitnessID = realTimeFitnessID;
        UserID = userID;
        CaptureDateTime = captureDateTime;
        StepNumber = stepNumber;
    }

    public String getRealTimeFitnessID() {
        return RealTimeFitnessID;
    }

    public String getUserID() {
        return UserID;
    }

    public DateTime getCaptureDateTime() {
        return CaptureDateTime;
    }

    public int getStepNumber() {
        return StepNumber;
    }

    public void setRealTimeFitnessID(String realTimeFitnessID) {
        RealTimeFitnessID = realTimeFitnessID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setCaptureDateTime(DateTime captureDateTime) {
        CaptureDateTime = captureDateTime;
    }

    public void setStepNumber(int stepNumber) {
        StepNumber = stepNumber;
    }

    //Running - 6 mph - 10 minute miles - 303
    //url http://walking.about.com/od/measure/a/stepequivalents.htm
    final int BasicRunStepNumber = (10 * 6 * 303);

    public boolean isRunning(){
        return getStepNumber() > BasicRunStepNumber;
    }

    public boolean isWalking(){
        return (getStepNumber() <= BasicRunStepNumber && getStepNumber() > 0);
    }

    public boolean isSedentary(){
        return getStepNumber() <= 0;
    }
}
