package recoding.hackathon.com.flugproject;

import java.util.ArrayList;

/**
 * Created by KSH on 2017-06-17.
 */

public class User {
    private ArrayList<String> interests;

    private String airport = "";
    private String number = "";
    private String date = "";

    private String make = "Samsung"; // The production-company of the mobile
    private String model = "S7"; // The model -> It can be a hint to give informatio about the user.
    // If the user is using expensive device or not. If the device is used by elder people or not...
    private String id="";

    public User(){
        interests = new ArrayList<>();
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void addIntetest(String interest){
        interests.add(interest);
    }
}
