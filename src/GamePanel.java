import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable {

    private Game game;
	private int generationCount;

    public GamePanel(Population pop, int generationCount) {
    	this.generationCount = generationCount;
        game = new Game(pop);
        new Thread(this).start();
    }

    public void update() {
        game.update();
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        for (Render r : game.getRenders())
            if (r.transform != null)
                g2D.drawImage(r.image, r.transform, null);
            else
                g.drawImage(r.image, r.x, r.y, null);


        g2D.setColor(Color.BLACK);

        if (!game.started) {
            g2D.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g2D.drawString("Press SPACE to start", 150, 240);
        } else {
            g2D.setFont(new Font("TimesRoman", Font.PLAIN, 24));
            g2D.drawString("Score : " + Integer.toString(game.score), 10, 465);
            g2D.drawString("Gen : " + Integer.toString(generationCount), 160, 465);
            //g2D.drawString("Best score : " + Integer.toString(game.bestScore), 300, 465);  TODO : Add best score
        }

    }

    public void run() {
        try {
            while (true) {
                update();
                Thread.sleep(25);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean getBirdAreOut() {
    	return game.isAllDead();
    }
}
