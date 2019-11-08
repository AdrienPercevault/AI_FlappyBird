import java.awt.Image;
import java.awt.event.KeyEvent;
//import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

public class Bird {

    public int x;
    public int y;
    public int width;
    public int height;

    public int totalDist;
    public int distX;
    public int distY;
    
    public boolean dead;
    
    public double yvel;
    public double gravity;

    public int rand;
    public int indexpipes;
    
    private int jumpDelay;
    private double rotation;

    private Image image;
    private int fitness;
    private Keyboard keyboard;
    
    private static int defaultGeneLenght;
    private byte[] genes;
    
    public int score;
    public Boolean gameover;

    public Bird(int x, int y) {
    	this.x = x; // x = 100;
    	this.y = y; // y = 150;
        yvel = 0;
        width = 45;
        height = 32;
        gravity = 0.5;
        jumpDelay = 0;
        rotation = 0.0;
        dead = false;
        
        fitness = 0;
        defaultGeneLenght = 64;
        genes = new byte[defaultGeneLenght];
        		
        gameover = false;
        score = 0;

        totalDist = 0;
        distX = 0;
        distY = 0;
        
        indexpipes = 0;
        
        rand = 0;
        
        keyboard = Keyboard.getInstance();
    }

	public void update(double isJump) {
        yvel += gravity;

        if (jumpDelay > 0)
            jumpDelay--;

        if (!dead && keyboard.isDown(KeyEvent.VK_SPACE) && jumpDelay <= 0) {
//        if (!dead && isJump > 0.5 && jumpDelay <= 0) {
        	yvel = -10;
            jumpDelay = 10;
        } 

        y += (int)yvel;
    }

	public Render getRender() {
        Render r = new Render();
        r.x = x;
        r.y = y;

        if (image == null) {
            image = Util.loadImage("lib/bird" + rand + ".png");
        }
        r.image = image;

        rotation = (90 * (yvel + 20) / 20) - 90;
        rotation = rotation * Math.PI / 180;

        if (rotation > Math.PI / 2)
            rotation = Math.PI / 2;

        r.transform = new AffineTransform();
        r.transform.translate(x + width / 2, y + height / 2);
        r.transform.rotate(rotation);
        r.transform.translate(-width / 2, -height / 2);

        return r;
    }
	
	public byte getGene(int index) {
		return genes[index];
	}

	public void setGene(int index, byte value) {
		genes[index] = value;
		fitness = 0;
	}

	public int geneSize() {
		return genes.length;
	}
	
	public int getFitness() {
		this.fitness = totalDist;
		return fitness;
	}
	
	
	
    public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Boolean getGameover() {
		return gameover;
	}

	public void setGameover(Boolean gameover) {
		this.gameover = gameover;
	}

	public int getTotalDist() {
		return totalDist;
	}

	public void setTotalDist(int totalDist) {
		this.totalDist = totalDist;
	}
	
	public int getDistX() {
		return distX;
	}

	public void setDistX(int distX) {
		this.distX = distX;
	}

	public int getDistY() {
		return distY;
	}

	public void setDistY(int distY) {
		this.distY = distY;
	}
	

	public int getRand() {
		return rand;
	}

	public void setRand(int rand) {
		this.rand = rand;
	}
	
	public int getIndexpipes() {
		return indexpipes;
	}

	public void setIndexpipes(int indexpipes) {
		this.indexpipes = indexpipes;
	}
}
