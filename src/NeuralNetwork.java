import java.util.Random;
import static java.lang.Math.exp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class NeuralNetwork {

    protected int layers_amount;
    private double fits[]; // [genome_index]
    protected int neurons_amount[]; // [layer_index]. Must be >= 2 (inputs and outputs). This is also used to calculate the synapses amount
    private double inputs[];
    protected double neurons[][]; // [layer_index][neuron_index]
    protected double synapses[][][][]; // [genome_index][layer_index][neuron_index][synapses_index]
    protected double synapses_temp[][][][];
    protected int genomes_per_generation;
    protected int current_genome = 0;
    protected int current_generation = 0;
    private double random_mutation_probability;
    private double min_weight, max_weight;
        
    public NeuralNetwork(int neurons_amount[], int genomes_per_generation, int layers_amount, double[][][][] synapses_temp, double random_mutation_probability, double min_weight, double max_weight) {
    	
    // Copy constructor parameters
	this.neurons_amount = neurons_amount;
	this.genomes_per_generation = genomes_per_generation;
	this.random_mutation_probability = random_mutation_probability;
	this.min_weight = min_weight;
	this.max_weight = max_weight;
	this.synapses_temp = synapses_temp;
    this.layers_amount = layers_amount;
        //layers_amount = neurons_amount.length;
        
        // Create fits array
	fits = new double[genomes_per_generation];
	
	// Generate neurons
	neurons = new double[layers_amount][];
	for(int i = 0; i < layers_amount; i++) {
            if(i != layers_amount - 1) {
                neurons_amount[i]++; // The last neuron is the bias.
            }
            neurons[i] = new double[neurons_amount[i]];
	}
        
        // Set biases to 1
	for(int i = 0; i < layers_amount - 1; i++) {
            neurons[i][neurons_amount[i] - 1] = 1;
	}
        
 
	synapses = synapses_temp;
    }
    
    public double[] getOutputs(double inputs[]) {
    	this.inputs = inputs;
    	setNeuronsValues();
    	return neurons[layers_amount - 1];
    }
    
    private void setNeuronsValues() {
	for(int i = 0; i < neurons_amount[0] - 1; i++) {
            neurons[0][i] = inputs[i];
	}
	
	// Init the other neurons to 0
	for(int i = 1; i < layers_amount; i++) {
            int m;
            if(i + 1 != layers_amount) {
                m = neurons_amount[i] - 1;
            }
            else {
                m = neurons_amount[i];
            }
            for(int j = 0; j < m; j++) {
                neurons[i][j] = 0;
            }
	}
	
	// Multiply neurons and synapses and sum the results (calculate the next neurons values)
	for(int i = 1; i < layers_amount; i++) {
            int m;
            if(i != layers_amount - 1) {
                m = neurons_amount[i] - 1;
            }
            else {
                m = neurons_amount[i];
            }
            for(int j = 0; j < m; j++) {
                for(int k = 0; k < neurons_amount[i - 1]; k++) {
                    neurons[i][j] += neurons[i - 1][k] * synapses[current_genome][i - 1][k][j];
                }

                // Activation function
                neurons[i][j] = sigmoid(neurons[i][j]);
            }		
		}
    }
    
    private double sigmoid(double x) {
        return 1 / (1 + exp(-x));
    }

}	

