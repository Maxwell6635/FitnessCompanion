package my.com.taruc.fitnesscompanion.Classes;

/**
 * Created by Hexa-Jackson Foo on 10/25/2015.
 */
public class Ranking {
    private String RankingID, UserID, Type;
    private Integer points;

    public Ranking(){
    }

    public Ranking(String rankingID, String userID, String type, Integer points) {
        RankingID = rankingID;
        UserID = userID;
        Type = type;
        this.points = points;
    }

    public String getRankingID() {
        return RankingID;
    }

    public String getUserID() {
        return UserID;
    }

    public String getType() {
        return Type;
    }

    public Integer getPoints() {
        return points;
    }

    public void setRankingID(String rankingID) {
        RankingID = rankingID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}