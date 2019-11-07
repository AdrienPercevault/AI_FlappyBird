import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Game {

    public static final int PIPE_DELAY = 100;

    private Boolean paused;

    private int pauseDelay;
    private int restartDelay;
    private int pipeDelay;

    private int numbirds;
    private Bird bird;
    private ArrayList<Bird> birdslist;
    private ArrayList<Pipe> pipes;
    private ArrayList<Pipe> pipesL;
    private int indexpipes;
    private Keyboard keyboard;

    public Boolean started;
    public int score;
    
    public int distX;
    public int distY;
    public int totalDist;
    
    public Game() {
        keyboard = Keyboard.getInstance();
        restart();
    }

    public void restart() {
        paused = false;
        started = false;
        pauseDelay = 0;
        restartDelay = 0;
        pipeDelay = 0;
        score = 0;

        pipes = new ArrayList<Pipe>();
        pipesL = new ArrayList<Pipe>();
        indexpipes = 0;
        distX = 0;
        distY = 0;
        totalDist = 0;
        
        numbirds = 100;
        Population pop = new Population(numbirds, true);
        birdslist = pop.getBirdslist();
               
//      Problème à modifier !
//      While incohérent ! Continuer jusqu'a la mort de l'individu
        int generationCount = 0;
        while (pop.getFittest().getFitness() < 10000) {
        	generationCount++;
        	System.out.println("Génération : " + generationCount);
        	pop = Genetic.evolvePopulation(pop);
        	update();
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }        

    }

    public void update() {
        watchForStart();

        if (!started)
            return;

        watchForPause();
//        watchForReset();

        if (paused)
            return;

        System.out.println("***** ***** ***** ***** *****");
        System.out.println("\tDistance X " + distX);
        System.out.println("\tDistance Y " + distY);
        System.out.println("\tDistance T " + totalDist);

        for (Bird b : birdslist) {
        	totalDist = b.getTotalDist();
        	score = b.getScore();
        	double isJump = Math.random();
        	b.update(isJump);
        }
               
        int num = 0;
        for (Bird b : birdslist) {
        	if (b.getGameover() == true)
        		num++;
        	if (num == birdslist.size()) {
        		watchForReset();
//        		return;
        	}
        }
       
        movePipes();
        checkForCollisions();
    }

    public ArrayList<Render> getRenders() {
        ArrayList<Render> renders = new ArrayList<Render>();
        renders.add(new Render(0, 0, "lib/background.png"));
        for (Pipe pipe : pipes)
            renders.add(pipe.getRender());
        
        renders.add(new Render(0, 0, "lib/foreground.png"));
        for (Bird b : birdslist)
        	renders.add(b.getRender());
        
        return renders;
    }

    private void watchForStart() {
//        if (!started && keyboard.isDown(KeyEvent.VK_SPACE)) {
            started = true;
//        }
    }

    private void watchForPause() {
        if (pauseDelay > 0)
            pauseDelay--;

        if (keyboard.isDown(KeyEvent.VK_P) && pauseDelay <= 0) {
            paused = !paused;
            pauseDelay = 10;
        }
    }

    private void watchForReset() {
        if (restartDelay > 0)
            restartDelay--;

//        if (keyboard.isDown(KeyEvent.VK_R) && restartDelay <= 0) {
        if (restartDelay <= 0) {
            restart();
            restartDelay = 10;
            return;
        }
    }

    private void movePipes() {
        pipeDelay--;

        if (pipeDelay < 0) {
            pipeDelay = PIPE_DELAY;
            Pipe northPipe = null;
            Pipe southPipe = null;

            // Look for pipes off the screen
            for (Pipe pipe : pipes) {
                if (pipe.x - pipe.width < 0) {
                    if (northPipe == null) {
                        northPipe = pipe;
                    } else if (southPipe == null) {
                        southPipe = pipe;
                        break;
                    }
                }
            }

            if (northPipe == null) {
                Pipe pipe = new Pipe("north");
                pipes.add(pipe);
                northPipe = pipe;
            } else {
                northPipe.reset();
            }

            if (southPipe == null) {
                Pipe pipe = new Pipe("south");
                pipes.add(pipe);
                southPipe = pipe;
            } else {
                southPipe.reset();
            }
            pipesL.add(northPipe);
            
            northPipe.y = southPipe.y + southPipe.height + 175;
        }

        for (Pipe pipe : pipes) {
            pipe.update();
        }
    }

    private void checkForCollisions() {
    	Pipe actPipe = pipesL.get(indexpipes);
    	int pipeX = actPipe.x;
    	int pipeY = actPipe.y - 88;
    	
        for (Pipe pipe : pipes) {
            for (Bird b : birdslist) {
            	distX = pipeX - b.x;
            	totalDist = score * 400 + 400 - distX;
            	b.setTotalDist(totalDist);
            	distY = pipeY - b.y;
            	if (pipe.collides(b.x, b.y, b.width, b.height)) {
            		b.setGameover(true);
            		b.dead = true;
            	} else if (pipe.x == b.x && pipe.orientation.equalsIgnoreCase("south")) {
            		indexpipes ++;
            		b.setScore(bird.getScore() + 1);
            	}
            }
        }

        // Ground + Bird collision
        for (Bird b : birdslist) {
            if (b.y + b.height > App.HEIGHT - 80) {
                b.setGameover(true);
                b.y = App.HEIGHT - 80 - b.height;
            }
        }
    }
}
