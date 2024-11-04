import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The Map class represents the game board, consisting of territories organized in a grid.
 * Each territory is managed using a graph structure to establish connections between neighbors.
 * The class manages the distribution of territories among players and assigns dice to each territory.
 *
 * Author: Zehang Zhang
 */
public class Map {
    // Constants
    public final int ROWS;          // Number of rows in the game board
    public final int COLUMNS;       // Number of columns in the game board
    public final int VICTIMS;       // Number of unplayable territories (victims)
    public final int NUMTERRITORIES; // Total number of territories (ROWS * COLUMNS)
    public final int OCCUPIED;      // Total number of playable territories
    public final int MAXDICE;       // Maximum dice per territory

    // Instance variables
    private Territory[][] territories; // 2D array representing the game board
    private Graph graph;               // Graph representing neighbor relationships between territories
    private ArrayList<Player> players; // List of players in the game

    /**
     * Constructor to initialize the game board with specified parameters.
     * Creates territories, constructs the graph, and assigns territories to players.
     *
     * @param players List of players participating in the game.
     * @param rows Number of rows on the game board.
     * @param columns Number of columns on the game board.
     * @param victims Number of unplayable territories.
     * @param maxDice Maximum number of dice allowed per territory.
     */
    public Map(ArrayList<Player> players, int rows, int columns, int victims, int maxDice) {
        this.ROWS = rows;
        this.COLUMNS = columns;
        this.VICTIMS = victims;
        this.NUMTERRITORIES = rows * columns;
        this.OCCUPIED = NUMTERRITORIES - VICTIMS;
        this.MAXDICE = maxDice;
        this.players = players;
        this.territories = new Territory[ROWS][COLUMNS];

        // Initialize and set up the game board
        createTerritories();
        this.graph = constructGraph(ROWS, COLUMNS, VICTIMS);
        partitionTerritories();
        distributeDice();
    }

    /**
     * Creates territories and assigns unique IDs to each.
     */
    private void createTerritories() {
        int idCounter = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                territories[i][j] = new Territory(this);
                territories[i][j].setIdNum(idCounter++);
            }
        }
    }

    /**
     * Constructs the graph by linking playable neighboring territories.
     * Randomly assigns some territories as 'victims' (unplayable).
     *
     * @param rows Number of rows on the game board.
     * @param cols Number of columns on the game board.
     * @param victims Number of unplayable territories.
     * @return The graph representing the game board.
     */
    public Graph constructGraph(int rows, int cols, int victims) {
        Graph graph = new Graph(NUMTERRITORIES);

        // Randomly set some territories as unplayable
        Random rand = new Random();
        for (int i = 0; i < victims; i++) {
            int victimRow = rand.nextInt(ROWS);
            int victimCol = rand.nextInt(COLUMNS);
            territories[victimRow][victimCol].setPlayable(false);
        }

        // Add edges to the graph to connect neighboring territories
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                Territory territory = territories[i][j];
                if (territory.isPlayable()) {
                    int id = territory.getIdNum();
                    // Check neighboring positions (up, down, left, right)
                    if (i > 0 && territories[i - 1][j].isPlayable()) {
                        graph.addEdge(id, territories[i - 1][j].getIdNum());
                    }
                    if (i < ROWS - 1 && territories[i + 1][j].isPlayable()) {
                        graph.addEdge(id, territories[i + 1][j].getIdNum());
                    }
                    if (j > 0 && territories[i][j - 1].isPlayable()) {
                        graph.addEdge(id, territories[i][j - 1].getIdNum());
                    }
                    if (j < COLUMNS - 1 && territories[i][j + 1].isPlayable()) {
                        graph.addEdge(id, territories[i][j + 1].getIdNum());
                    }
                }
            }
        }

        return graph;
    }

    /**
     * Returns the 2D array representing the game board.
     *
     * @return A 2D array of Territory objects.
     */
    public Territory[][] getMap() {
        return territories;
    }

    /**
     * Retrieves the graph representing the game board's neighbor relationships.
     *
     * @return The Graph object.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Retrieves a specific territory by its row and column.
     *
     * @param row The row index of the territory.
     * @param column The column index of the territory.
     * @return The Territory object at the specified position.
     */
    public Territory getTerritory(int row, int column) {
        return territories[row][column];
    }

    /**
     * Retrieves the unique ID of a specific territory based on its row and column.
     *
     * @param row The row index of the territory.
     * @param column The column index of the territory.
     * @return The unique ID of the territory.
     */
    public int getTerritoryId(int row, int column) {
        return territories[row][column].getIdNum();
    }

    /**
     * Counts the number of territories owned by a specific player.
     *
     * @param player The player whose territories are being counted.
     * @return The number of territories owned by the player.
     */
    public int countTerritories(Player player) {
        return getPropertyOf(player).size();
    }

    /**
     * Counts the total number of dice owned by a specific player.
     *
     * @param player The player whose dice are being counted.
     * @return The total number of dice.
     */
    public int countDice(Player player) {
        int totalDice = 0;
        for (Territory territory : getPropertyOf(player)) {
            totalDice += territory.getDice();
        }
        return totalDice;
    }

    /**
     * Retrieves all the territories owned by a specific player.
     *
     * @param player The player whose territories are being retrieved.
     * @return A list of the player's territories.
     */
    public ArrayList<Territory> getPropertyOf(Player player) {
        ArrayList<Territory> playerTerritories = new ArrayList<>();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (territories[i][j].getOwner() == player) {
                    playerTerritories.add(territories[i][j]);
                }
            }
        }
        return playerTerritories;
    }

    /**
     * Retrieves the neighboring territories of a specific territory.
     *
     * @param cell The territory whose neighbors are being retrieved.
     * @return A list of neighboring territories.
     */
    public ArrayList<Territory> getNeighbors(Territory cell) {
        ArrayList<Territory> neighbors = new ArrayList<>();
        List<Integer> adjacentIds = graph.getAdjacent(cell.getIdNum());

        for (Integer id : adjacentIds) {
            int row = id / COLUMNS;
            int col = id % COLUMNS;
            neighbors.add(territories[row][col]);
        }
        return neighbors;
    }

    /**
     * Retrieves the enemy neighboring territories of a specific territory.
     *
     * @param cell The territory whose enemy neighbors are being retrieved.
     * @return A list of enemy neighboring territories.
     */
    public ArrayList<Territory> getEnemyNeighbors(Territory cell) {
        ArrayList<Territory> enemyNeighbors = new ArrayList<>();
        ArrayList<Territory> neighbors = getNeighbors(cell);

        for (Territory neighbor : neighbors) {
            if (neighbor.getOwner() != cell.getOwner()) {
                enemyNeighbors.add(neighbor);
            }
        }
        return enemyNeighbors;
    }

    /**
     * Randomly assigns playable territories to players.
     */
    private void partitionTerritories() {
        ArrayList<Territory> playableTerritories = new ArrayList<>();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (territories[i][j].isPlayable()) {
                    playableTerritories.add(territories[i][j]);
                }
            }
        }

        Collections.shuffle(playableTerritories);

        int playerIndex = 0;
        for (Territory territory : playableTerritories) {
            Player player = players.get(playerIndex % players.size());
            territory.setOwner(player);
            playerIndex++;
        }
    }

    /**
     * Distributes dice to each player's territories, ensuring no territory exceeds the max limit.
     */
    private void distributeDice() {
        for (Player player : players) {
            ArrayList<Territory> ownedTerritories = getPropertyOf(player);
            int totalDice = ownedTerritories.size() * 3;

            for (Territory territory : ownedTerritories) {
                territory.setDice(1);
                totalDice--;
            }

            Random rand = new Random();
            while (totalDice > 0) {
                Territory territory = ownedTerritories.get(rand.nextInt(ownedTerritories.size()));
                if (territory.getDice() < MAXDICE) {
                    territory.setDice(territory.getDice() + 1);
                    totalDice--;
                }
            }
        }
    }

    /**
     * Returns the size of the largest connected cluster of territories owned by a specific player.
     *
     * @param player The player whose territories are being checked.
     * @return The size of the largest connected cluster.
     */
    public int countConnected(Player player) {
        return graph.largestConnectedCluster(player, this);
    }
}
