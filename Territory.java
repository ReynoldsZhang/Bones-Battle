/**
 * The Territory class represents a single square on the game board.
 * Each Territory is associated with a specific map, has a unique ID, 
 * and contains information about its owner, the number of dice assigned to it, 
 * and whether it is playable.
 *
 * This class provides constructors, getters, and setters to manage the properties 
 * of the Territory and includes methods to compute the row and column index based 
 * on the territory's unique ID.
 *
 * Author: Zehang Zhang
 */
public class Territory {
    private Map map; // Reference to the map that this territory belongs to
    private Player owner; // The player who owns this territory
    private int dice; // Number of dice currently assigned to this territory
    private int idNum; // Unique identifier for the territory
    private boolean playable; // Indicates whether the territory is playable or not

    /**
     * Constructor to initialize a Territory with a reference to the map.
     * Sets default values for owner, dice, idNum, and initializes the territory 
     * as playable.
     *
     * @param map The map this territory is associated with.
     */
    public Territory(Map map) {
        this.map = map;
        this.owner = null; // By default, no player owns this territory
        this.dice = -1; // Default value indicating no dice are assigned
        this.idNum = -1; // Default ID value
        this.playable = true; // Default to playable
    }

    /**
     * Constructor to initialize a Territory with specific attributes.
     *
     * @param map The map this territory is associated with.
     * @param owner The player who owns this territory.
     * @param dice The number of dice currently assigned to this territory.
     * @param idNum The unique identifier for the territory.
     */
    public Territory(Map map, Player owner, int dice, int idNum) {
        this.map = map;
        this.owner = owner;
        this.dice = dice;
        this.idNum = idNum;
        this.playable = true; // Default to playable
    }

    // Getters

    /**
     * Returns the number of dice currently assigned to this territory.
     *
     * @return The number of dice.
     */
    public int getDice() {
        return dice;
    }

    /**
     * Returns the unique identifier of the territory.
     *
     * @return The unique ID number.
     */
    public int getIdNum() {
        return idNum;
    }

    /**
     * Returns the map that this territory is part of.
     *
     * @return The Map object associated with this territory.
     */
    public Map getMap() {
        return map;
    }

    /**
     * Returns the player who owns this territory.
     *
     * @return The Player object that owns this territory.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Checks whether the territory is currently playable.
     *
     * @return True if the territory is playable, false otherwise.
     */
    public boolean isPlayable() {
        return playable;
    }

    // Setters

    /**
     * Sets the number of dice assigned to this territory.
     *
     * @param dice The new number of dice to be assigned.
     */
    public void setDice(int dice) {
        this.dice = dice;
    }

    /**
     * Sets the unique identifier of the territory.
     *
     * @param idNum The unique ID number to set.
     */
    public void setIdNum(int idNum) {
        this.idNum = idNum;
    }

    /**
     * Sets the player who owns this territory.
     *
     * @param owner The Player object to be set as the owner.
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * Sets the playable status of this territory.
     *
     * @param playable True if the territory should be playable, false otherwise.
     */
    public void setPlayable(boolean playable) {
        this.playable = playable;
    }

    // Other Methods

    /**
     * Computes the row index on the game board based on the territory's ID.
     *
     * @return The row index.
     */
    public int getRow() {
        return idNum / map.COLUMNS;
    }

    /**
     * Computes the column index on the game board based on the territory's ID.
     *
     * @return The column index.
     */
    public int getCol() {
        return idNum % map.COLUMNS;
    }
}
