
public class PlayerMovementThread extends Thread {

    private Player1 player; // Reference to the Player object

    public PlayerMovementThread(Player1 player) {
        this.player = player;
    }

    @Override
    public void run() {
        while (!isInterrupted()) { // Run until interrupted
            //player.updatePosition(); // Update player's position
            try {
                Thread.sleep(10); // Sleep for 10 milliseconds (adjust as needed)
            } catch (InterruptedException e) {
                break; // Exit loop if interrupted
            }
        }
    }
}
