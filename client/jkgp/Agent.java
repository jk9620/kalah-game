package kgp;

import java.io.IOException;
import java.math.BigInteger;

// though that would mean that every server would have to develop it's own library

// Implement the constructor and search() for your agent according to the comments, and you're done
// Not knowing the board size upon creation of the agent is on purpose
// Note that servers might punish agents whose constructor needs too much time
// Prints the client-server communication to the error stream
// Btw. this is a single threaded implementation, the agent and the protocol manager are calling each other
// So don't worry about scheduling issues if your tournament restricts the agent to one CPU core
public abstract class Agent {

    // can be null
    private String name, authors, description;
    private BigInteger N, e, d;

    // MANDATORY: Read the documentation of submitMove() and shouldStop()
    // Find the best move for the given state here
    // It's always south's turn to move
    public abstract void search(KalahState ks) throws IOException;

    // the communication instance the agent uses to communicate with the server
    private final ProtocolManager com;

    // Creates an agent
    // If host and port are non-null, the agent will connect to that server, otherwise connects to localhost:2671
    // If name/authors/description is non-null, the server is told the value of name/authors/description
    //
    // RSA: public key (N,e), private key (N,d)
    // If all three are non-null, the agent will authenticate itself upon connecting.
    // Authentication might be necessary for leaderboards etc.,
    public Agent(String host, int port, String name, String authors, String description, BigInteger N, BigInteger e, BigInteger d) {

        com = new ProtocolManager(host, port, this);

        this.name = name;
        this.authors = authors;
        this.description = description;

        this.N = N;
        this.e = e;
        this.d = d;
    }

    // Returns the name of the agent or null if not specified
    public String getName()
    {
        return name;
    }

    // Returns the authors of the agent or null if not specified
    public String getAuthors()
    {
        return authors;
    }

    // Returns the description of the agent or null if not specified
    public String getDescription()
    {
        return description;
    }

    // Returns [N,e,d] of the agent or null if not specified
    public BigInteger[] getRSA()
    {
        if (N == null ||
                e == null ||
                d == null)
        {
            return null;
        }
        else
        {
            return new BigInteger[] {N, e, d};
        }
    }

    // connects to the server, plays the tournament/game/whatever, closes the connection
    // passes on IOExceptions
    // if there is a mistake in the implementation and the client crashes, it will notify the server and end
    // the connection before crashing the program
    public final void run() throws IOException {
        com.run();
    }

    // Tell the server the currently "best" move (according to your agent)
    // Call at least one time or the server might punish you!
    // A move is a number in [0 ..., board_size-1] (in the direction of sowing)
    protected final void submitMove(int move) throws IOException {
        com.sendMove(move + 1);
    }

    // Call this function a few times per second in search()
    // Calling this function regularly is necessary to handle the protocol communication!
    // Returns true after the server told the client to stop searching.
    // End search as fast as possible if it returns true! A good server implementation
    // subtracts overtime from the next move to prevent cheating!
    // Note: You can return from search() anytime, for example if you don't need more time
    protected final boolean shouldStop() throws IOException {
        return com.shouldStop();
    }

    // Tells the server to comment on the current position, call it during search() for example
    // Pass your string, can include linebreaks and newlines
    protected final void sendComment(String comment) throws IOException {
        com.sendComment(comment);
    }

    // Get time mode if available, otherwise returns null
    protected final ProtocolManager.TimeMode getTimeMode(){
        return com.getTimeMode();
    }

    // Get number of seconds on agent's clock if available, otherwise returns null
    protected final Integer getTimeClock(){
        return com.getTimeClock();
    }

    // Get number of seconds on opponent's clock if available, otherwise returns null
    protected final Integer getTimeOppClock(){
        return com.getTimeOppClock();
    }

    // Get name of server if available, otherwise returns null
    protected final String getServerName(){
        return com.getServerName();
    }
}