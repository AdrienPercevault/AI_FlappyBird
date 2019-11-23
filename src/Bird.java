import java.awt.Image;
import java.awt.event.KeyEvent;
//import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.Random;

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
    
    public int score;
    public Boolean gameover;
    
    public int isFirst;

    // SETUP NEURAL NETWORK
    public final int genomes_per_generation = 3;
    public final int neurons_amount[] = {2, 2, 1};
    public int layers_amount = neurons_amount.length;
    public NeuralNetwork nn;
    public boolean autoplay = true;
    public final double inputs[] = new double[2];
    public double outputs[] = new double[1];
    protected double synapses_val[][][][];
    public double min_weight = -1;
    public double max_weight = 1;
    
    public Bird(int x, int y) {
    	synapses_val = initSynapsesRandomly();
    	this.nn = new NeuralNetwork(neurons_amount, genomes_per_generation, layers_amount, synapses_val, 0.5, min_weight, max_weight);
		this.x = x; // x = 100;
    	this.y = y; // y = 150;
        yvel = 0;
        width = 45;
        height = 32;
        gravity = 0.5;
        jumpDelay = 0;
        rotation = 0.0;
        dead = false;
        isFirst = 0;
        
        fitness = 0;
        		
        gameover = false;
        score = 0;

        totalDist = 0;
        distX = 0;
        distY = 0;
        
        indexpipes = 0;
        
        rand = 0;
        
        keyboard = Keyboard.getInstance();
    }
    
    private double[][][][] initSynapsesRandomly() {
    	synapses_val = new double[genomes_per_generation][][][];
    	int neurons_amount[] = {3, 3, 1};
    	for(int k = 0; k < genomes_per_generation; k++) {
    		synapses_val[k] = new double[layers_amount - 1][][];
                for(int i = 0; i < layers_amount - 1; i++) {
                	synapses_val[k][i] = new double[neurons_amount[i]][];
                    for(int j = 0; j < neurons_amount[i]; j++) {
                        if(i + 1 != layers_amount - 1) {
                        	synapses_val[k][i][j] = new double[neurons_amount[i + 1] - 1];
                        }
                        else {
                        	synapses_val[k][i][j] = new double[neurons_amount[i + 1]];
                        }	
                    }
                }
    	}
    	
        for(int l = 0; l < genomes_per_generation; l++) {
            for(int i = 0; i < layers_amount - 1; i++) {
                for(int j = 0; j < neurons_amount[i]; j++) {
                    int m;                    
                    if(i + 1 != layers_amount - 1) {
                        m = neurons_amount[i + 1] - 1;
                    }
                    else {
                        m = neurons_amount[i + 1];
                    }
                    for(int k = 0; k < m; k++) {
                    	synapses_val[l][i][j][k] = randDouble(min_weight, max_weight);
                    }
                }
            }
        }
		return synapses_val;
     }
    
    private double randDouble(double min, double max) {
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }

	public void update() {
        yvel += gravity;

        if (jumpDelay > 0)
            jumpDelay--;

        //System.out.println(distX);
        //System.out.println(distY);
        
        inputs[0] = distX;
        inputs[1] = distY;
        outputs = nn.getOutputs(inputs);
        
        double jumper = outputs[0];
        // System.out.println(jumper);
        
//        if (!dead && keyboard.isDown(KeyEvent.VK_SPACE) && jumpDelay <= 0) {
        if (!dead && jumper > 0.5 && jumpDelay <= 0) {
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
            image = Util.loadImage("lib/bird" + getIsFirst() + ".png");
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
	
	public double[][][][] getInitSynapsesRandomly() {
		return initSynapsesRandomly();
	}
	
    public int getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(int isFirst) {
		this.isFirst = isFirst;
	}
	
    public NeuralNetwork getNn() {
		return nn;
	}

	public void setNn(NeuralNetwork nn) {
		this.nn = nn;
	}
	
    public double[][][][] getSynapses_val() {
		return synapses_val;
	}

	public void setSynapses_val(double[][][][] synapses_val) {
		this.synapses_val = synapses_val;
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
