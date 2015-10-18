package my.com.saiboon.fitnesscompanion.Classes;

/**
 * Created by saiboon on 10/6/2015.
 */
public class RealTimeFitness {

    private String RealTimeFitnessID;
    private DateTime CaptureDateTime = new DateTime();
    private int StepNumber;
    public RealTimeFitness(){}
    public RealTimeFitness(String RealTimeFitnessID, String CaptureDateTime, int StepNumber)
    {
        this.RealTimeFitnessID = RealTimeFitnessID;
        this.CaptureDateTime.setDateTime(CaptureDateTime);
        this.StepNumber = StepNumber;
    }

    public String getRealTimeFitnessID() {
        return RealTimeFitnessID;
    }

    public DateTime getCaptureDateTime() {
        return CaptureDateTime;
    }

    public int getStepNumber() {
        return StepNumber;
    }

    public void setRealTimeFitnessID(String RealTimeFitnessID) {
        RealTimeFitnessID = RealTimeFitnessID;
    }

    public void setCaptureDateTime(String captureDateTime) {
        this.CaptureDateTime.setDateTime(captureDateTime);
    }

    public void setStepNumber(int stepNumber) {
        StepNumber = stepNumber;
    }
}
