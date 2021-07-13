import java.text.DecimalFormat;

public class Player {
    private String firstname;
    private String surname;

    private int runs;
    private int balls;
    private int mins;
    private int fours;
    private int sixes;

    private int ballsBowled;
    private int runsConceded;
    private int wickets;
    private int maidens;

    private String howOut = "not out";

    public Player(String firstname, String surname) {
        this.firstname = firstname;
        this.surname = surname;
    }

    public String getName(){
        return (getFirstname() + " " + getSurname());
    }

    public String getShortName(){
        if (getSurname().length()>22){
            return (getFirstname().substring(0, 1) + " " + getSurname().substring(0,22));
        } else {
            return (getFirstname().substring(0, 1) + " " + getSurname());
        }
    }

    public String getInitials(){
        return getFirstname().substring(0,1) + getSurname().substring(0,1);
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getBalls() {
        return balls;
    }

    public void setBalls(int balls) {
        this.balls = balls;
    }

    public int getMins() {
        return mins;
    }

    public void setMins(int mins) {
        this.mins = mins;
    }

    public int getFours() {
        return fours;
    }

    public void setFours(int fours) {
        this.fours = fours;
    }

    public int getSixes() {
        return sixes;
    }

    public void setSixes(int sixes) {
        this.sixes = sixes;
    }

    public String getEconomy(){
        if (getRunsConceded()!=0) {
            DecimalFormat df = new DecimalFormat("#.00");
            double runs = getRunsConceded();
            double balls = getBallsBowled();
            return df.format((runs / balls)*6);
        } else {
            return "0.00";
        }
    }

    public String getOvers(){
        if (getBallsBowled()!=0) {
            if (getBallsBowled() % 6 == 0){
                return getBallsBowled()/6 + ".0";
            } else {
                return ((getBallsBowled() - ((getBallsBowled() % 6)))/6 + "." + (getBallsBowled() % 6));
            }
        } else {
            return "0.0";
        }
    }

    public int getBallsBowled() {
        return ballsBowled;
    }

    public void setBallsBowled(int ballsBowled) {
        this.ballsBowled = ballsBowled;
    }

    public int getRunsConceded() {
        return runsConceded;
    }

    public void setRunsConceded(int runsConceded) {
        this.runsConceded = runsConceded;
    }

    public int getWickets() {
        return wickets;
    }

    public void setWickets(int wickets) {
        this.wickets = wickets;
    }

    public int getMaidens() {
        return maidens;
    }

    public void setMaidens(int maidens) {
        this.maidens = maidens;
    }

    public String getStrikeRate(){
        if (getRuns()!=0) {
            DecimalFormat df = new DecimalFormat("#.00");
            //return df.format((getRuns() / getBalls()) * 100);
            double runs = getRuns();
            double balls = getBalls();
            return df.format((runs / balls) * 100);
        } else {
            return "0.00";
        }
    }

    public String getHowOut() {
        if (howOut.equalsIgnoreCase("")) {
            return "";
        } else {
            return howOut;
        }
    }

    public void setHowOut(String howOut) {
        this.howOut = howOut;
    }
}
