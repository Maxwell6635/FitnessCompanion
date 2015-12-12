package my.com.taruc.fitnesscompanion;


import my.com.taruc.fitnesscompanion.Classes.UserProfile;

/**
 * Created by JACKSON on 5/26/2015.
 */
interface GetUserCallback {

    /**
     * Invoked when background task is completed
     */
    public abstract void done(UserProfile returnedUser);
}
