package Model;

/**
 * Created by piranavan on 2017-01-18.
 */

public class Weather {
    public Location place;
    public String iconData;
    public CurrentCondition currentCondition = new CurrentCondition();
    public Temperature temperature = new Temperature();
    public Wind wind = new Wind();
    public Snow snow = new Snow();
    public Clouds clouds = new Clouds();

}

