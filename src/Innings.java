import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Innings {
    Scanner in = new Scanner(System.in);

    private int score;
    private int wickets;
    private int wides;
    private int noBalls;
    private int byes;
    private int legByes;
    private int penalties;

    private int balls;

    private boolean firstInnings;
    private int firstRuns;
    private int firstWickets;
    private int firstBalls;

    private int totalBalls;

    private Team batting;
    private Team bowling;

    private boolean isHomeBatting;

    private int striker = -1;
    private int nonStriker = -1;
    private int bowler = -1;
    private int previousBowler = -1;

    private int partnershipRuns;
    private int partnershipBalls;
    private int partnershipFours;
    private int partnershipSixes;

    private int dots;

    private String lastWicket = "";
    private int fow = -1;

    private ArrayList<String> previousBalls = new ArrayList<>();

    private ArrayList<Integer> batsman = new ArrayList<>();
    private ArrayList<Integer> bowlers = new ArrayList<>();

    private boolean onStrike;

    public Innings(Team batting, Team bowling, boolean firstInnings, boolean isHomeBatting, int firstRuns, int firstWickets, int firstBalls, int totalBalls)
            throws IOException, InterruptedException {
        setScore(0);
        setWickets(0);
        setWides(0);
        setNoBalls(0);
        setByes(0);
        setLegByes(0);
        setPenalties(0);
        setBalls(0);
        setDots(0);

        this.firstRuns = firstRuns;
        this.firstWickets = firstWickets;
        this.firstBalls = firstBalls;
        this.firstInnings = firstInnings;
        this.totalBalls = totalBalls;

        this.batting = batting;
        this.bowling = bowling;

        this.isHomeBatting = isHomeBatting;

        System.out.println("\nInnings of " + batting.getName());

        String confirm = "";
        while (!confirm.equalsIgnoreCase("yes")) {
            if (batsman.size() > 0){
                batsman.clear();
                striker = -1;
                nonStriker = -1;
                bowler = -1;
            }

            while (0 > striker || striker > 12) {
                System.out.println("Enter player number who is facing the first ball:");
                for (int i = 0; i < batting.getPlayers().size(); i++) {
                    System.out.println(i + 1 + ": " + batting.getPlayers().get(i).getName());
                }
                striker = in.nextInt();
            }
            striker--;

            batsman.add(striker);

            boolean alreadyBatted = false;

            while (0 > nonStriker || nonStriker > 12 || nonStriker == striker+1) {
                System.out.println("Enter player number who is the non striker:");
                for (int i = 0; i < batting.getPlayers().size(); i++) {
                    for (int j = 0; j < batsman.size(); j++) {
                        if (i == batsman.get(j)) {
                            alreadyBatted = true;
                        }
                    }
                    if (!alreadyBatted) {
                        System.out.println(i + 1 + ": " + batting.getPlayers().get(i).getName());
                    }
                    alreadyBatted = false;
                }
                nonStriker = in.nextInt();
            }
            nonStriker--;

            batsman.add(nonStriker);

            in.nextLine();

            System.out.println(batting.getName() + " to open with " + batting.getPlayers().get(striker).getShortName() +
                    " and " + batting.getPlayers().get(nonStriker).getShortName() + "?");

            System.out.println("Yes to continue");
            confirm = in.nextLine();
        }

        confirm = "";
        while (!confirm.equalsIgnoreCase("yes")) {
            while (0 > bowler || bowler > 12) {
                System.out.println("Enter player number who is bowling:");
                for (int i = 0; i < batting.getPlayers().size(); i++) {
                    System.out.println(i + 1 + ": " + bowling.getPlayers().get(i).getName());
                }

                bowler = in.nextInt();
            }
            bowler--;

            bowlers.add(bowler);

            in.nextLine();

            System.out.println(bowling.getPlayers().get(bowler).getShortName() + " bowling for " + bowling.getName() + "?");

            System.out.println("Yes to continue");
            confirm = in.nextLine();
        }

        onStrike = true;

        while ((getWickets() < 10) &&
                ((getBalls() < getTotalBalls())) &&
                ((getFirstRuns() >= getScore()) || firstInnings)){
            nextBall();
        }
    }

    private void nextBall() throws IOException, InterruptedException {
        System.out.println("\n");
        String confirm = "";
        if (isOver()){
            if (getDots() == 6){
                bowling.getPlayers().get(bowler).setMaidens(bowling.getPlayers().get(bowler).getMaidens()+1);
            }
            setDots(0);

            if (getPreviousBowler() !=-1){
                do {
                    System.out.println(bowling.getPlayers().get(previousBowler).getName() + " to continue? (Yes/No)");
                    confirm = in.nextLine();
                } while (!confirm.equalsIgnoreCase("yes") && !confirm.equalsIgnoreCase("no"));

                if (confirm.equalsIgnoreCase("yes")){
                    int temp = getPreviousBowler();
                    setPreviousBowler(bowler);
                    bowler = temp;
                } else {
                    newBowler();
                }
            } else {
                newBowler();
            }
        }

        clear();
        printScore();

        String input = "";
        while (input.equalsIgnoreCase("")){
            System.out.println("Enter next ball (h or 'help' for more information)");
            input = in.nextLine();
        }

        if (previousBalls.size()>18){
            previousBalls.remove(0);
        }

        if (isNumeric(input) && Integer.parseInt(input) > -1){
            if (Integer.parseInt(input) == 0){
                setDots(getDots()+1);
            }
            updateScore(input);
            updateBalls();
            bowling.getPlayers().get(bowler).setRunsConceded(bowling.getPlayers().get(bowler).getRunsConceded()+Integer.parseInt(input));
            bowling.getPlayers().get(bowler).setBallsBowled(bowling.getPlayers().get(bowler).getBallsBowled()+1);

            updatePartnership(input);

            if (onStrike) {
                updateBatsmen(striker, input);

                previousBalls.add(bowling.getPlayers().get(bowler).getInitials() + " to " +
                        batting.getPlayers().get(striker).getInitials() + ": " + input);
            } else {
                updateBatsmen(nonStriker, input);

                previousBalls.add(bowling.getPlayers().get(bowler).getInitials() + " to " +
                        batting.getPlayers().get(nonStriker).getInitials() + ": " + input);
            }
        } else if (input.equalsIgnoreCase("scorecard")){
            clear();
            printScorecard();
        } else if (input.equalsIgnoreCase("b") ||
                input.equalsIgnoreCase("lb") ||
                input.equalsIgnoreCase("+") ||
                input.equalsIgnoreCase("nb")){
            extas(input);
        } else if (input.equalsIgnoreCase("Switch bat")){
            switchBat("1");
        } else if (input.equalsIgnoreCase("h") || input.equalsIgnoreCase("help")) {
            help();
        }
    }

    private void printScore(){
        String scoreFormat = "| %76s   %8s   %8s |%n";
        String hideLine = " %25s |%n";

        System.out.format("+----------------------------------------------------------------------------------------------------+%n");

        String overs;
        if (getBalls()!=0) {
            if (getBalls() % 6 == 0){
                overs = getBalls()/6 + ".0";

                if (onStrike) {
                    onStrike = false;
                } else {
                    onStrike = true;
                }

            } else {
                overs = ((getBalls() - ((getBalls() % 6)))/6 + "." + (getBalls() % 6));
            }
        } else {
            overs = "0.0";
        }

        if (isHomeBatting) {
            System.out.format(scoreFormat,
                    batting.getName(),
                    getScore() + "-" + getWickets(), "(" + overs +")");

            System.out.format(scoreFormat,
                    bowling.getName(),"", "");
        } else {
            System.out.format(scoreFormat,
                    bowling.getName(),"", "");

            System.out.format(scoreFormat,
                    batting.getName(),
                    getScore() + "-" + getWickets(), "(" + overs +")");
        }

        String leftAlignFormat = "| %-25s | %6s | %6s | %6s | %6s | %6s | %25s |%n";

        System.out.format("+------------------------------------------------------------------------+---------------------------+%n");

        String runRateFormat = "| %34s   %33s | %25s |%n";

        if (firstInnings){
            System.out.format(runRateFormat, "Predicted Score: " + getPrediction(),
                    "Current RR: " + getRunRate(),
                    printPrevious(1));
        } else {

        }

        System.out.format("+---------------------------+--------+--------+--------+--------+--------+");
        System.out.format(hideLine, printPrevious(2));

        System.out.format(leftAlignFormat,"","R","B","4s","6s","SR",printPrevious(3));
        System.out.format("+---------------------------+--------+--------+--------+--------+--------+");
        System.out.format(hideLine, printPrevious(4));

        if (onStrike) {
            System.out.format(leftAlignFormat,
                    batting.getPlayers().get(striker).getShortName() + "*",
                    batting.getPlayers().get(striker).getRuns(),
                    batting.getPlayers().get(striker).getBalls(),
                    batting.getPlayers().get(striker).getFours(),
                    batting.getPlayers().get(striker).getSixes(),
                    batting.getPlayers().get(striker).getStrikeRate(),
                    printPrevious(5));

            System.out.format(leftAlignFormat,
                    batting.getPlayers().get(nonStriker).getShortName(),
                    batting.getPlayers().get(nonStriker).getRuns(),
                    batting.getPlayers().get(nonStriker).getBalls(),
                    batting.getPlayers().get(nonStriker).getFours(),
                    batting.getPlayers().get(nonStriker).getSixes(),
                    batting.getPlayers().get(nonStriker).getStrikeRate(),
                    printPrevious(6));
        } else {
            System.out.format(leftAlignFormat,
                    batting.getPlayers().get(striker).getShortName(),
                    batting.getPlayers().get(striker).getRuns(),
                    batting.getPlayers().get(striker).getBalls(),
                    batting.getPlayers().get(striker).getFours(),
                    batting.getPlayers().get(striker).getSixes(),
                    batting.getPlayers().get(striker).getStrikeRate(),
                    printPrevious(5));

            System.out.format(leftAlignFormat,
                    batting.getPlayers().get(nonStriker).getShortName() + "*",
                    batting.getPlayers().get(nonStriker).getRuns(),
                    batting.getPlayers().get(nonStriker).getBalls(),
                    batting.getPlayers().get(nonStriker).getFours(),
                    batting.getPlayers().get(nonStriker).getSixes(),
                    batting.getPlayers().get(nonStriker).getStrikeRate(),
                    printPrevious(6));
        }

        System.out.format("+---------------------------+--------+--------+--------+--------+--------+");

        System.out.format(hideLine, printPrevious(7));

        System.out.format(leftAlignFormat,
                "Partnership",
                getPartnershipRuns(),
                getPartnershipBalls(),
                getPartnershipFours(),
                getPartnershipSixes(),
                getPartnershipStrikeRate(),
                printPrevious(8));
        System.out.format("+---------------------------+--------+--------+--------+--------+--------+");
        System.out.format(hideLine, printPrevious(9));
        System.out.format(leftAlignFormat,"","O","M","R","W","Econ",printPrevious(10));
        System.out.format("+---------------------------+--------+--------+--------+--------+--------+");
        System.out.format(hideLine, printPrevious(11));
        System.out.format(leftAlignFormat,
                bowling.getPlayers().get(bowler).getShortName(),
                bowling.getPlayers().get(bowler).getOvers(),
                bowling.getPlayers().get(bowler).getMaidens(),
                bowling.getPlayers().get(bowler).getRunsConceded(),
                bowling.getPlayers().get(bowler).getWickets(),
                bowling.getPlayers().get(bowler).getEconomy(),
                printPrevious(12));
        System.out.format("+---------------------------+--------+--------+--------+--------+--------+");
        System.out.format(hideLine, printPrevious(13));
        System.out.format(leftAlignFormat,"","Extras","B","LB","W","NB",printPrevious(14));
        System.out.format("+---------------------------+--------+--------+--------+--------+--------+");
        System.out.format(hideLine, printPrevious(15));
        System.out.format(leftAlignFormat,
                "",
                getExtras(),
                getByes(),
                getLegByes(),
                getWides(),
                getNoBalls(),
                printPrevious(16));
        System.out.format("+---------------------------+--------+--------+--------+--------+--------+");
        System.out.format(hideLine, printPrevious(17));

        if (lastWicket.equalsIgnoreCase("")){
            System.out.format(leftAlignFormat,
                    "", "", "", "", "", "", printPrevious(18));
        } else {
            System.out.format(leftAlignFormat,
                    lastWicket, "FOW", getFow(), "", "", "", 19);
        }
        System.out.format("+---------------------------+--------+--------+--------+--------+--------+---------------------------+");
        System.out.println("\n");
    }

    private void printScorecard(){
        String scoreFormat = "| %76s   %8s   %8s |%n";
        System.out.format("+----------------------------------------------------------------------------------------------------+%n");

        String overs;
        if (getBalls()!=0) {
            if (getBalls() % 6 == 0){
                overs = getBalls()/6 + ".0";

                if (onStrike) {
                    onStrike = false;
                } else {
                    onStrike = true;
                }

            } else {
                overs = ((getBalls() - ((getBalls() % 6)))/6 + "." + (getBalls() % 6));
            }
        } else {
            overs = "0.0";
        }

        if (isHomeBatting) {
            System.out.format(scoreFormat,
                    batting.getName(),
                    getScore() + "-" + getWickets(), "(" + overs +")");
            System.out.format(scoreFormat,
                    bowling.getName(),"", "");
        } else {
            System.out.format(scoreFormat,
                    bowling.getName(),"", "");

            System.out.format(scoreFormat,
                    batting.getName(),
                    getScore() + "-" + getWickets(), "(" + overs +")");
        }

        String leftAlignFormat = "| %-25s | %25s | %6s | %6s | %6s | %6s | %6s |%n";

        System.out.format("+---------------------------+---------------------------+--------+--------+--------+--------+--------+%n");
        System.out.format(leftAlignFormat,"","","R","B","4s","6s","SR");
        System.out.format("+---------------------------+---------------------------+--------+--------+--------+--------+--------+%n");

        for (int i = 0; i < batsman.size(); i++) {
            System.out.format(leftAlignFormat,
                    batting.getPlayers().get(batsman.get(i)).getShortName(),
                    batting.getPlayers().get(batsman.get(i)).getHowOut(),
                    batting.getPlayers().get(batsman.get(i)).getRuns(),
                    batting.getPlayers().get(batsman.get(i)).getBalls(),
                    batting.getPlayers().get(batsman.get(i)).getFours(),
                    batting.getPlayers().get(batsman.get(i)).getSixes(),
                    batting.getPlayers().get(batsman.get(i)).getStrikeRate());
        }

        boolean alreadyBatted = false;
        for (int i = 0; i < batting.getPlayers().size(); i++) {
            for (int j = 0; j < batsman.size(); j++) {
                if (i == batsman.get(j)) {
                    alreadyBatted = true;
                }
            }
            if (!alreadyBatted) {
                System.out.format(leftAlignFormat,
                        batting.getPlayers().get(i).getShortName(),
                        "",
                        "",
                        "",
                        "",
                        "",
                        "");
            }
            alreadyBatted = false;
        }

        System.out.format("+---------------------------+---------------------------+--------+--------+--------+--------+--------+%n");
                System.out.format(leftAlignFormat,"","","O","M","R","W","Econ");
        System.out.format("+---------------------------+---------------------------+--------+--------+--------+--------+--------+%n");

        for (int i = 0; i < bowlers.size(); i++) {
            System.out.format(leftAlignFormat,"",
                    bowling.getPlayers().get(bowlers.get(i)).getShortName(),
                    bowling.getPlayers().get(bowlers.get(i)).getOvers(),
                    bowling.getPlayers().get(bowlers.get(i)).getMaidens(),
                    bowling.getPlayers().get(bowlers.get(i)).getRunsConceded(),
                    bowling.getPlayers().get(bowlers.get(i)).getWickets(),
                    bowling.getPlayers().get(bowlers.get(i)).getEconomy());
        }
        System.out.format("+---------------------------+---------------------------+--------+--------+--------+--------+--------+%n");
        System.out.format(leftAlignFormat,"","","Extras","B","LB","W","NB");
        System.out.format("+---------------------------+---------------------------+--------+--------+--------+--------+--------+%n");
        System.out.format(leftAlignFormat,
                "", "",
                getExtras(),
                getByes(),
                getLegByes(),
                getWides(),
                getNoBalls());
        System.out.format("+---------------------------+---------------------------+--------+--------+--------+--------+--------+%n");


        String confirm = "";
        while (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Yes to continue");
            confirm = in.nextLine();
        }
    }

    private void updateBatsmen(int batsman, String input){
        batting.getPlayers().get(batsman).setRuns(batting.getPlayers().get(batsman).getRuns() + Integer.parseInt(input));
        batting.getPlayers().get(batsman).setBalls(batting.getPlayers().get(batsman).getBalls() + 1);

        if (input.equalsIgnoreCase("4")){
            batting.getPlayers().get(batsman).setFours(batting.getPlayers().get(batsman).getFours() + 1);
        } else if (input.equalsIgnoreCase("6")){
            batting.getPlayers().get(batsman).setSixes(batting.getPlayers().get(batsman).getSixes() + 1);
        }

        if (Integer.parseInt(input) % 2 == 1) {
            if (onStrike) {
                onStrike = false;
            } else {
                onStrike = true;
            }
        }
    }

    private void updateBatsmenExtra(int batsman, String input, String noBallOrWide, String isOffBat){
        if (isOffBat.equalsIgnoreCase("yes")) {
            batting.getPlayers().get(batsman).setRuns(batting.getPlayers().get(batsman).getRuns() + Integer.parseInt(input) - 1);
            if (input.equalsIgnoreCase("5")){
                batting.getPlayers().get(batsman).setFours(batting.getPlayers().get(batsman).getFours() + 1);
            } else if (input.equalsIgnoreCase("7")){
                batting.getPlayers().get(batsman).setSixes(batting.getPlayers().get(batsman).getSixes() + 1);
            }
        }

        if (noBallOrWide.equalsIgnoreCase("nb")) {
            batting.getPlayers().get(batsman).setBalls(batting.getPlayers().get(batsman).getBalls() + 1);
        }

        if (Integer.parseInt(input) % 2 == 0) {
            if (onStrike) {
                onStrike = false;
            } else {
                onStrike = true;
            }
        }
    }

    public int getWickets() {
        return wickets;
    }

    public void setWickets(int wickets) {
        this.wickets = wickets;
    }

    public int getWides() {
        return wides;
    }

    public void setWides(int wides) {
        this.wides = wides;
    }

    public int getNoBalls() {
        return noBalls;
    }

    public void setNoBalls(int noBalls) {
        this.noBalls = noBalls;
    }

    public int getByes() {
        return byes;
    }

    public void setByes(int byes) {
        this.byes = byes;
    }

    public int getLegByes() {
        return legByes;
    }

    public void setLegByes(int legByes) {
        this.legByes = legByes;
    }

    public int getPenalties() {
        return penalties;
    }

    public void setPenalties(int penalties) {
        this.penalties = penalties;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getBalls() {
        return balls;
    }

    public void setBalls(int balls) {
        this.balls = balls;
    }

    public boolean isFirstInnings() {
        return firstInnings;
    }

    public void setFirstInnings(boolean firstInnings) {
        this.firstInnings = firstInnings;
    }

    public int getFirstRuns() {
        return firstRuns;
    }

    public void setFirstRuns(int firstRuns) {
        this.firstRuns = firstRuns;
    }

    public int getFirstWickets() {
        return firstWickets;
    }

    public void setFirstWickets(int firstWickets) {
        this.firstWickets = firstWickets;
    }

    public int getFirstBalls() {
        return firstBalls;
    }

    public void setFirstBalls(int firstBalls) {
        this.firstBalls = firstBalls;
    }

    public int getPartnershipRuns() {
        return partnershipRuns;
    }

    public void setPartnershipRuns(int partnershipRuns) {
        this.partnershipRuns = partnershipRuns;
    }

    public int getPartnershipBalls() {
        return partnershipBalls;
    }

    public void setPartnershipBalls(int partnershipBalls) {
        this.partnershipBalls = partnershipBalls;
    }

    public int getPartnershipFours() {
        return partnershipFours;
    }

    public void setPartnershipFours(int partnershipFours) {
        this.partnershipFours = partnershipFours;
    }

    public int getPartnershipSixes() {
        return partnershipSixes;
    }

    public void setPartnershipSixes(int partnershipSixes) {
        this.partnershipSixes = partnershipSixes;
    }

    public String getLastWicket() {
        return lastWicket;
    }

    public void setLastWicket(String lastWicket) {
        this.lastWicket = lastWicket;
    }

    public int getFow() {
        return fow;
    }

    public void setFow(int fow) {
        this.fow = fow;
    }

    public String getPartnershipStrikeRate(){
        if (getBalls()!=0) {
            DecimalFormat df = new DecimalFormat("#.00");
            //return df.format((getRuns() / getBalls()) * 100);
            double runs = getPartnershipRuns();
            double balls = getPartnershipBalls();
            return df.format((runs / balls) * 100);
        } else {
            return "0.00";
        }
    }

    private void updatePartnership(String input) {
        setPartnershipRuns(getPartnershipRuns() + Integer.parseInt(input));
        setPartnershipBalls(getPartnershipBalls() + 1);

        if (input.equalsIgnoreCase("4")) {
            setPartnershipFours(getPartnershipFours() + 1);
        } else if (input.equalsIgnoreCase("6")) {
            setPartnershipSixes(getPartnershipSixes() + 1);
        }
    }

    private void clear() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        final String os = System.getProperty("os.name");
        if (os.contains("Windows"))
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        else
            Runtime.getRuntime().exec("clear");

        System.out.println("\n");
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public int getExtras(){
        return (getByes() + getLegByes() + getWides() + getNoBalls());
    }

    private String printPrevious(int num){
        if (previousBalls.size() < num) {
            return "";
        } else {
            return previousBalls.get(previousBalls.size()-num);
        }
    }

    public int getPreviousBowler() {
        return previousBowler;
    }

    public void setPreviousBowler(int previousBowler) {
        this.previousBowler = previousBowler;
    }

    private boolean isOver(){
        if (getBalls()!=0 && getBalls() % 6 == 0){
            return true;
        } else {
            return false;
        }
    }

    public int getTotalBalls() {
        return totalBalls;
    }

    public void setTotalBalls(int totalBalls) {
        this.totalBalls = totalBalls;
    }

    public int getDots() {
        return dots;
    }

    public void setDots(int dots) {
        this.dots = dots;
    }

    private String getRunRate(){
        if (getBalls()!=0) {
            DecimalFormat df = new DecimalFormat("#.00");
            //return df.format((getRuns() / getBalls()) * 100);
            double runs = getScore();
            double balls = getBalls();
            return df.format((runs / balls) * 6);
        } else {
            return "0.00";
        }
    }

    private String getRequired(){
        if (getBalls()!=0) {
            DecimalFormat df = new DecimalFormat("#.00");
            //return df.format((getRuns() / getBalls()) * 100);
            double runs = getScore();
            double balls = getBalls();
            return df.format((runs / balls) * 6);
        } else {
            return "0.00";
        }
    }

    private String getPrediction(){
        if (getBalls()!=0) {
            DecimalFormat df = new DecimalFormat("#");
            //return df.format((getRuns() / getBalls()) * 100);
            double runs = getScore();
            double balls = getBalls();
            return df.format((runs / balls) * getTotalBalls());
        } else {
            return "";
        }
    }

    private void newBowler() {
        setPreviousBowler(bowler);
        bowler = -1;
        while (0 > bowler || bowler > 12 || bowler == getPreviousBowler()+1) {
            System.out.println("Enter player number who is bowling:");
            for (int i = 0; i < bowling.getPlayers().size(); i++) {
                if (i != getPreviousBowler()) {
                    System.out.println(i + 1 + ": " + bowling.getPlayers().get(i).getName());
                }
            }
            bowler = in.nextInt();
        }
        bowler--;

        boolean alreadyBowled = false;
        for (int j = 0; j < bowlers.size(); j++) {
            if (bowler == bowlers.get(j)) {
                alreadyBowled = true;
            }
        }
        if (!alreadyBowled) {
            bowlers.add(bowler);
        }
        in.nextLine();
    }

    private void updateScore(String input){
        setScore(getScore()+Integer.parseInt(input));
    }

    private void updateBalls(){
        setBalls(getBalls()+1);
    }

    private void switchBat(String input){
        if (Integer.parseInt(input) % 2 == 1) {
            if (onStrike) {
                onStrike = false;
            } else {
                onStrike = true;
            }
        }
    }

    private void extas(String input){
        String amount = "-1";
        input = input.toLowerCase(Locale.ROOT);

        String offBat = "";
        while (Integer.parseInt(amount) < 0){
            switch(input) {
                case "b" :
                    System.out.println("Enter number of byes");
                    amount = in.nextLine();
                    break; // optional
                case "lb" :
                    System.out.println("Enter number of leg byes");
                    amount = in.nextLine();
                    break; // optional
                case "w" :
                    System.out.println("Enter number of wides");
                    amount = in.nextLine();
                    break; // optional
                case "nb" :
                    System.out.println("Enter number of no balls");
                    amount = in.nextLine();
                    if (Integer.parseInt(amount) > 1){
                        while (!offBat.equalsIgnoreCase("yes") && !offBat.equalsIgnoreCase("no")){
                            System.out.println("Runs off bat or byes? (yes/no)");
                            offBat = in.nextLine();
                        }
                    }
                    break; // optional
                // You can have any number of case statements.
                default : // Optional
                    // Statements
            }
        }

        updateScore(amount);

        switch(input) {
            case "b" :
                setByes(getByes()+Integer.parseInt(amount));
                bowling.getPlayers().get(bowler).setBallsBowled(bowling.getPlayers().get(bowler).getBallsBowled()+1);
                updatePartnership(amount);
                break; // optional
            case "lb" :
                setLegByes(getLegByes()+Integer.parseInt(amount));
                bowling.getPlayers().get(bowler).setBallsBowled(bowling.getPlayers().get(bowler).getBallsBowled()+1);
                updatePartnership(amount);
                break; // optional
            case "w" :
                setWides(getWides()+Integer.parseInt(amount));
                bowling.getPlayers().get(bowler).setRunsConceded(bowling.getPlayers().get(bowler).getRunsConceded()+Integer.parseInt(amount));
                setPartnershipRuns(getPartnershipRuns() + Integer.parseInt(amount));
                break; // optional
            case "nb" :
                setNoBalls(getNoBalls()+1);
                setPartnershipRuns(getPartnershipRuns() + Integer.parseInt(amount));
                if (offBat.equalsIgnoreCase("yes")){
                    bowling.getPlayers().get(bowler).setRunsConceded(bowling.getPlayers().get(bowler).getRunsConceded()+Integer.parseInt(amount));
                } else {
                    bowling.getPlayers().get(bowler).setRunsConceded(bowling.getPlayers().get(bowler).getRunsConceded()+Integer.parseInt("1"));
                    setByes(getByes()+Integer.parseInt(amount)-1);
                }
                break; // optional
            // You can have any number of case statements.
            default : // Optional
                // Statements
        }

        if (onStrike) {
            if (input.equalsIgnoreCase("nb") || input.equalsIgnoreCase("+")){
                updateBatsmenExtra(striker, amount, input, offBat);
            } else {
                updateBatsmen(striker, "0");
            }

            previousBalls.add(bowling.getPlayers().get(bowler).getInitials() + " to " +
                    batting.getPlayers().get(striker).getInitials() + ": " + amount + input);
        } else {
            if (input.equalsIgnoreCase("nb") || input.equalsIgnoreCase("+")){
                updateBatsmenExtra(nonStriker, amount, input, offBat);
            } else {
                updateBatsmen(nonStriker, "0");
            }

            previousBalls.add(bowling.getPlayers().get(bowler).getInitials() + " to " +
                    batting.getPlayers().get(nonStriker).getInitials() + ": " + amount + input);
        }

        switch(input) {
            case "b" :
                switchBat(amount);
                break; // optional
            case "lb" :
                switchBat(amount);
                break; // optional
            // You can have any number of case statements.
            default : // Optional
                // Statements
        }
    }

    private void help(){
        System.out.println("Enter one of the following options: \n" +
                "#: Enter the number of runs scored\n" +
                "W: Wicket\n"+
                "B: Bye\n"+
                "LB: Leg Bye\n"+
                "+: Wide\n"+
                "NB: No Ball\n"+
                "Switch Bat: Change the batsman who is on strike");
    }
}
