public class PlayerMovementThread extends Thread {
    private final Player1 player; // Currently unused; kept for future use

    public PlayerMovementThread(Player1 player) {
        this.player = player;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
