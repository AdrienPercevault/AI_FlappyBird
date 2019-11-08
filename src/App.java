import javax.swing.*;

public class App {

    public static int WIDTH = 500;
    public static int HEIGHT = 500;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);

        Keyboard keyboard = Keyboard.getInstance();
        frame.addKeyListener(keyboard);

        int numbirds = 3;
        Population pop = new Population(numbirds, true);
        GamePanel panel = new GamePanel(pop);

        
        int generationCount = 1;
        while (pop.getFittest().getFitness() < 10000) {
        	if (generationCount == 1) {
            	System.out.println("Génération : " + generationCount);

            	pop = Genetic.evolvePopulation(pop); 
            	frame.add(panel);
            	generationCount++;
        	}

        	if (pop.getEverybodyDead()) {
        		System.out.println("test");
            	System.out.println("Génération : " + generationCount);
                
            	pop = Genetic.evolvePopulation(pop); 
            	panel = new GamePanel(pop);
            	frame.add(panel);
            	pop.setEverybodyDead(false);
            	generationCount++;
        	}
        	
//        	try {
//				Thread.sleep(100000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}

        }
    }
}
