package my.com.taruc.fitnesscompanion.Classes;

/**
 * Created by Hexa-Jackson Foo on 10/25/2015.
 */
public class Ranking {
    private String RankingID, UserID, Type;
    private Integer points;
    private DateTime CreatedAt, UpdatedAt;

    public Ranking(){
    }

    public Ranking(String rankingID, String userID, String type, Integer points, DateTime createdAt, DateTime updatedAt) {
        RankingID = rankingID;
        UserID = userID;
        Type = type;
        this.points = points;
        CreatedAt = createdAt;
        UpdatedAt = updatedAt;
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

    public DateTime getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        CreatedAt = createdAt;
    }

    public DateTime getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(DateTime updatedAt) {
        UpdatedAt = updatedAt;
    }
}
