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

        int numbirds = 50;
        
        int generationCount = 1;
        Population pop = new Population(numbirds, true);
        GamePanel panel = new GamePanel(pop, generationCount);
        
        while (pop.getFittest().getFitness() < 10000) {
        	boolean boolDead = panel.getBirdAreOut();
        	
        	if (generationCount == 1) {
            	System.out.println("Génération : " + generationCount);

            	pop = Genetic.evolvePopulation(pop); 
            	frame.add(panel);
            	generationCount++;
        	}

        	if (boolDead) {
            	System.out.println("Génération : " + generationCount);
                
                pop = Genetic.evolvePopulation(pop);
                frame.remove(panel);
            	panel = new GamePanel(pop, generationCount);
            	frame.add(panel);
            	
            	frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(WIDTH, HEIGHT);
                frame.setLocationRelativeTo(null);   
            	
            	generationCount++;
        	}      	
        }
    }
}
