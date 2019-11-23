
public class Genetic {

	private static int selectionSize = 5;
	private static final double crossoverRate = 0.5;
	private static final double mutationRate = 0.1;
	
	public static Population evolvePopulation(Population pop) {
		Population newPop = new Population(pop.popSize(), false);
		
		newPop.addPop(pop.getFittest());
		
		// Crossover loop
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
		
		if (Math.random() <= crossoverRate) {
			newBird.setNn(b1.getNn());
		}
		else {
			newBird.setNn(b2.getNn());
		}
		
		return newBird;
	}
	
	private static void mutate(Bird b) {
		if (Math.random() <= mutationRate) {
			
			int neurons_amount[] = {2, 2, 1};
			int genomes_per_generation = 3;
			int layers_amount = neurons_amount.length;
			double synapses_val[][][][];
			double min_weight = -1;
		    double max_weight = 1;
		    
		    synapses_val = b.getInitSynapsesRandomly();
		    
			NeuralNetwork nn = new NeuralNetwork(neurons_amount, genomes_per_generation, layers_amount, synapses_val, 0.5, min_weight, max_weight);
			
			b.setNn(nn);
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
