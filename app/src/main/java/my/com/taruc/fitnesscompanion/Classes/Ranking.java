package my.com.taruc.fitnesscompanion.Classes;

/**
 * Created by Hexa-Jackson Foo on 10/25/2015.
 */
public class Ranking {
    String name ;
    Integer rank ,points;

    public Ranking(){
    }

    public Ranking(Integer rank,String name, Integer points){
        this.rank = rank;
        this.name = name;
        this.points = points;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
