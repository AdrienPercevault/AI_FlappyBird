import java.util.ArrayList;

public class Population {
	
	private Bird bird;
	
	public int numbirds;
	public ArrayList<Bird> birdslist;
	
	public Population(int numbirds, boolean init) {
		this.numbirds = numbirds;
		
		birdslist = new ArrayList<Bird>();
		if (init) {
			int upper = 350;
	        for (int i=0; i<numbirds; i++) {
	        	bird = new Bird(100, (int) (Math.random() * upper));
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
		return fittest;
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
