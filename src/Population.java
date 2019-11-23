import java.util.ArrayList;

public class Population {
	
	private Bird bird;
	
	public int numbirds;
	public ArrayList<Bird> birdslist;
	public int rand;	
	public boolean dead;
	
	public Population(int numbirds, boolean init) {
		this.dead = false;
		this.numbirds = numbirds;
		
		birdslist = new ArrayList<Bird>();
		if (init) {
	        for (int i=0; i<numbirds; i++) {
	        	bird = new Bird(100, (int) (Math.random() * 350));
	        	rand = (int) (Math.random() * 2);
	        	bird.setRand(rand);
	        	addPop(bird);
	        }
	    }
	}
	
	public Bird getBird(int index) {
		return birdslist.get(index);
	}
	
	public Bird getFittest() {
		Bird fittest = birdslist.get(0);
		for (Bird b : birdslist) {
			if (fittest.getFitness() <= b.getFitness()) {
				fittest = b;
			}
		}
		
		Bird fittest2 = new Bird(100, (int) (Math.random() * 350));
		//fittest2.setGene(0, fittest.getGene(0));
		fittest2.setNn(fittest.getNn());
		fittest2.setIsFirst(1);
		return fittest2;
	}
	
	public void setEverybodyDead(boolean bool) {
		this.dead = bool;
	}
	
	public boolean getEverybodyDead() {
		return this.dead;
	}
	
	public int popSize() {
		return birdslist.size();
	}
	
	public void addPop(Bird bird) {
		birdslist.add(bird);
	}

	public ArrayList<Bird> getBirdslist() {
		return birdslist;
	}

	public void setBirdslist(ArrayList<Bird> birdslist) {
		this.birdslist = birdslist;
	}
}
