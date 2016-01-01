package my.com.taruc.fitnesscompanion.ServerAPI;


import my.com.taruc.fitnesscompanion.Classes.UserProfile;

/**
 * Created by JACKSON on 5/26/2015.
 */
public interface GetUserCallBack {

    /**
     * Invoked when background task is completed
     */
    public abstract void done(UserProfile returnedUser);
}
