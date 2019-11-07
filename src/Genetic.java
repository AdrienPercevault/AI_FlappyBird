
public class Genetic {

	private static int selectionSize = 5;
	private static final double crossoverRate = 0.5;
	private static final double mutationRate = 0.1;
	
	public static Population evolvePopulation(Population pop) {
		Population newPop = new Population(pop.popSize(), false);
		
		newPop.addPop(pop.getFittest());
		
		// Crossover loop !
		for (int i=1; i < pop.popSize(); i++) {
			Bird b1 = selectBird(pop);
			Bird b2 = selectBird(pop);
			Bird newBird = crossover(b1, b2);
			newPop.addPop(newBird);
		}
		
		for (int i=1; i < newPop.popSize(); i++) {
			mutate(newPop.getBird(i));
		}
		
		return newPop;
	}
	
	private static Bird crossover (Bird b1, Bird b2) {
		int upper = 350;
		Bird newBird = new Bird(100, (int) (Math.random() * upper));
		
		for (int i=0; i < b1.geneSize(); i++) {
			if (Math.random() <= crossoverRate) {
				newBird.setGene(i, b1.getGene(i));
			}
			else {
				newBird.setGene(i, b2.getGene(i));
			}
		}
		
		return newBird;
	}
	
	private static void mutate(Bird b) {
		for(int i=0; i < b.geneSize(); i++) {
			if (Math.random() <= mutationRate) {
				byte gene = (byte) Math.round(Math.random());
				b.setGene(i, gene);
			}
		}
	}
	
	private static Bird selectBird(Population pop) {
		Population selection = new Population(selectionSize, false);
		for (int i=0; i < selectionSize; i++) {
			int randomId = (int) (Math.random() * pop.popSize());
			selection.addPop(pop.getBird(randomId));
		}
		Bird fittest = selection.getFittest();
		return fittest;
	}
}
