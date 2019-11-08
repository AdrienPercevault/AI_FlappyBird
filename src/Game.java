import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Game {

    public static final int PIPE_DELAY = 100;

    private Boolean paused;

    private int pauseDelay;
    private int restartDelay;
    private int pipeDelay;

    private ArrayList<Bird> birdslist;
    private ArrayList<Pipe> pipes;
    private ArrayList<Pipe> pipesL;
    private int indexpipes;
    private Keyboard keyboard;

    public Boolean started;
    public int score;
    
    public boolean alldead;
    
    public int distX;
    public int distY;
    public int totalDist;
    public Population pop;
        
    public Game(Population pop) {
        keyboard = Keyboard.getInstance();

        birdslist = pop.getBirdslist();
        this.pop = pop;
        restart();
    }

    public void restart() {
        paused = false;
        started = false;
        pauseDelay = 0;
        restartDelay = 0;
        pipeDelay = 0;
        score = 0;
        alldead = false;
                
        pipes = new ArrayList<Pipe>();
        pipesL = new ArrayList<Pipe>();
        indexpipes = 0;
        totalDist = 0;
        distX = 0;
        distY = 0;
    }

    public void update() {
        watchForStart();

        if (!started)
            return;

        watchForPause();
//        watchForReset();

        if (paused)
            return;

        for (Bird b : birdslist) {
        	
            System.out.println("***** ***** ***** ***** *****");
            System.out.println("\tBird : " + b);
            System.out.println("\tDistance X " + b.getDistX());
            System.out.println("\tDistance Y " + b.getDistY());
            System.out.println("\tDistance T " + b.getTotalDist());
            
        	totalDist = b.getTotalDist();
        	double isJump = Math.random();
        	b.update(isJump);
        }

        int num = 0;
        for (Bird b : birdslist) {
        	System.out.println(b.getScore());
        	if (b.getGameover() == true)
        		num++;
        	if (num == birdslist.size()) {
        		alldead = true;
        		birdslist = new ArrayList<Bird>();
        		return;
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

//    private void watchForReset() {
//        if (restartDelay > 0)
//            restartDelay--;
//
//        if (keyboard.isDown(KeyEvent.VK_R) && restartDelay <= 0) {
//        if (restartDelay <= 0) {
//            restartDelay = 10;
//            restart();
//            return;
//        }
//    }

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
            	score = b.getScore();
            	if (!b.getGameover()) {
	            	indexpipes = b.getIndexpipes();
	            	distX = pipeX - b.x;
	            	b.setDistX(distX);
	            	totalDist = b.getScore() * 400 + 400 - distX;
	            	b.setTotalDist(totalDist);
	            	distY = pipeY - b.y;
	            	b.setDistY(distY);
	            	
	            	if (pipe.collides(b.x, b.y, b.width, b.height)) {
	            		b.setGameover(true);
	            		b.dead = true;
	            	} else if (pipe.x == b.x && pipe.orientation.equalsIgnoreCase("south")) {
	            		indexpipes++;
	            		b.setIndexpipes(indexpipes);
	            		score++;
	            		b.setScore(b.getScore() + 1);
	            	}
            	}
            }
        }

        // Ground + Bird collision
        for (Bird b : birdslist) {
            if (b.y + b.height > App.HEIGHT - 80) {
                b.setGameover(true);
                b.dead = true;
                b.y = App.HEIGHT - 80 - b.height;
            }
        }
    }

	public boolean isAllDead() {
		return alldead;
	}

	public void setAllDead(boolean alldead) {
		this.alldead = alldead;
	}
}
