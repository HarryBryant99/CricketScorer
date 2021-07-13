import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner in = new Scanner(System.in);

    private static ArrayList<Team> teams = new ArrayList<>();
    private static int overs;

    private static String toss;
    static Team batting;
    static Team bowling;

    public static void main(String[] args) throws IOException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException {
        String confirm = "";
        overs = 0;
        while (!confirm.equalsIgnoreCase("yes")) {

            System.out.println("Enter home team:");
            String team = in.nextLine();
            Team home = new Team(team);
            teams.add(home);

            System.out.println("Enter away team:");
            team = in.nextLine();
            Team away = new Team(team);
            teams.add(away);

            System.out.println("Number of overs per side");
            while (overs < 1) {
                overs = in.nextInt();
                if (overs < 1) {
                    System.out.println("Enter a number greater than 0");
                }
            }

            in.nextLine();

            System.out.println(home.getName() + " v " + away.getName() + ", " + overs + " over game");
            System.out.println("Yes to continue");
            confirm = in.nextLine();
        }

        clear();

        System.out.println("Pick " + teams.get(0).getName() + " side:");
        pickTeam(teams.get(0));

        clear();

        System.out.println("Pick " + teams.get(1).getName() + " side:");
        pickTeam(teams.get(1));

        clear();

        printTeams();

        toss();

        boolean isHomeBatting = false;

        if (batting.getName().equals(teams.get(0).getName())){
            isHomeBatting = true;
        }

        Innings firstInnings = new Innings(batting, bowling, true, isHomeBatting, 0,0,0, overs*6);

        System.out.println(firstInnings.getScore());
    }

    private static void clear() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        final String os = System.getProperty("os.name");
        if (os.contains("Windows"))
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        else
            Runtime.getRuntime().exec("clear");

        System.out.println("\n");
    }

    private static void pickTeam(Team team) {
        for (int i = 0; i < 11; i++) {
            System.out.println("First name: ");
            String firstname = in.nextLine();
            System.out.println("Surname: ");
            String surname = in.nextLine();
            team.addPlayer(firstname, surname);

            System.out.println("Player Created: " + team.getPlayers().get(i).getName() + "\n");
        }

        String correct = "no";
        while (correct.equalsIgnoreCase("no")){
            System.out.println("Lineup Correct? - enter 'No' to make changes");
            correct = in.nextLine();

            if (correct.equalsIgnoreCase("no")){
                System.out.println("Enter player number to change:");
                for (int i = 0; i < 11; i++) {
                    System.out.println(i+1 + ": " + team.getPlayers().get(i).getName());
                }
                int player = in.nextInt();
                player--;

                in.nextLine();

                System.out.println("First name: ");
                String firstname = in.nextLine();
                System.out.println("Surname: ");
                String surname = in.nextLine();

                team.getPlayers().get(player).setFirstname(firstname);
                team.getPlayers().get(player).setSurname(surname);

                System.out.println("Player Created: " + team.getPlayers().get(player).getName() + "\n");
            }
        }
    }

    private static void printTeams(){
        String leftAlignFormat = "| %-25s | %-25s |%n";

        System.out.format("+---------------------------+---------------------------+%n");
        System.out.format(leftAlignFormat, teams.get(0).getName(), teams.get(1).getName());
        System.out.format("+---------------------------+---------------------------+%n");
        for (int i = 0; i < 11; i++) {
            System.out.format(leftAlignFormat, teams.get(0).getPlayers().get(i).getShortName(), teams.get(1).getPlayers().get(i).getShortName());
        }
        System.out.format("+---------------------------+---------------------------+%n");
    }

    private static void toss() {
        System.out.println("Toss won by:");
        String winner = "";
        while (!winner.equalsIgnoreCase(teams.get(0).getName()) && !winner.equalsIgnoreCase(teams.get(1).getName())){
            winner = in.nextLine();
        }
        System.out.println("And chose to: (bat or bowl)");
        String choice = "";
        while (!choice.equalsIgnoreCase("bat") && !choice.equalsIgnoreCase("bowl")){
            choice = in.nextLine();
        }

        if (winner.equalsIgnoreCase(teams.get(0).getName()) && choice.equals("bat") ||
                winner.equalsIgnoreCase(teams.get(1).getName()) && choice.equals("bowl")){
            batting = teams.get(0);
            bowling = teams.get(1);
        } else if(winner.equalsIgnoreCase(teams.get(0).getName()) && choice.equals("bowl") ||
                winner.equalsIgnoreCase(teams.get(1).getName()) && choice.equals("bat")){
            batting = teams.get(1);
            bowling = teams.get(0);
        }


        toss = winner + " won the toss and chose to " + choice;
        System.out.println(toss);
    }
}
