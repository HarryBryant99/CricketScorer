import java.util.ArrayList;

public class Team {
    private String name;
    private ArrayList<Player> players = new ArrayList<>();

    public Team(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPlayer(String firstname, String surname){
        Player newPlayer = new Player(firstname, surname);
        players.add(newPlayer);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
