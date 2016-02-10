package my.com.taruc.fitnesscompanion.Classes;

/**
 * Created by Hexa-Jackson Foo on 10/25/2015.
 */
public class Ranking {
    private String RankingID, UserID, Type, name;
    private Integer points;
    private String fitnessRecordID;
    private DateTime CreatedAt, UpdatedAt;

    public Ranking(){
    }

    public Ranking(String rankingID, String userID, String name, String type, Integer points, String fitnessRecordID, DateTime createdAt, DateTime updatedAt) {
        RankingID = rankingID;
        UserID = userID;
        this.name = name;
        Type = type;
        this.points = points;
        CreatedAt = createdAt;
        this.fitnessRecordID = fitnessRecordID;
        UpdatedAt = updatedAt;
    }

    public String getRankingID() {
        return RankingID;
    }

    public void setRankingID(String rankingID) {
        RankingID = rankingID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getFitnessRecordID() {
        return fitnessRecordID;
    }

    public void setFitnessRecordID(String fitnessRecordID) {
        this.fitnessRecordID = fitnessRecordID;
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
