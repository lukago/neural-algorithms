package iad.lab3;

import java.io.Serializable;

public class MultiLayerPerceptron implements Serializable {

	private static final long serialVersionUID = -4481210057997822231L;

	private double learningRate;
	private double momentum;
	private boolean useBias;
	private Layer[] layers;
	private ActivationFunction actFun;

	public MultiLayerPerceptron(int[] layersInfo, double learrningRate, 
			double momentum, boolean useBias, ActivationFunction actFun,
			double gaussDiv) {
		this.learningRate = learrningRate;
		this.momentum = momentum;
		this.useBias = useBias;
		this.actFun = actFun;
		Neuron.gaussDiv = gaussDiv;
		createLayers(layersInfo);
	}

	public double[] execute(double[] input) {
		// Put input to the first layer
		for (int i = 0; i < layers[0].neuronsNum; i++) {
			layers[0].neurons[i].value = input[i];
		}

		// transfer input from first layer to next layers
		for (int i = 1; i < layers.length; i++) {
			transferInput(i);
		}

		// get output form the last layer
		double output[] = new double[layers[layers.length - 1].neuronsNum];
		for (int i = 0; i < layers[layers.length - 1].neuronsNum; i++) {
			output[i] = layers[layers.length - 1].neurons[i].value;
		}

		return output;
	}

	public double[] backPropagate(double[] input, double[] exOutput) {
		double newOutput[] = execute(input);

		updateLastLayerDelta(newOutput, exOutput);

		for (int i = layers.length - 2; i >= 0; i--) {
			updateLayerDelta(i);
			updateLayerWeightsAndBias(i);
		}

		return newOutput;
	}

	private void updateLastLayerDelta(double[] newOutput, double exOutput[]) {
		double error, errAct;
		for (int i = 0; i < layers[layers.length - 1].neuronsNum; i++) {
			error = exOutput[i] - newOutput[i];
			errAct = error * actFun.evaluteDerivate(newOutput[i]);
			layers[layers.length - 1].neurons[i].delta = errAct;
		}
	}

	private void updateLayerDelta(int layer) {
		double error, delta, weight, errAct;
		for (int i = 0; i < layers[layer].neuronsNum; i++) {
			error = 0.0;
			for (int j = 0; j < layers[layer + 1].neuronsNum; j++) {
				delta = layers[layer + 1].neurons[j].delta;
				weight = layers[layer + 1].neurons[j].weights[i];
				error += delta * weight;
			}
			errAct = error * actFun.evaluteDerivate(layers[layer].neurons[i].value);
			layers[layer].neurons[i].delta = errAct;
		}
	}

	private void updateLayerWeightsAndBias(int layer) {
		double delta, value, weightsDiff, weightCurr, weightPrev, deltaValLr;

		for (int i = 0; i < layers[layer + 1].neuronsNum; i++) {
			for (int j = 0; j < layers[layer].neuronsNum; j++) {
				delta = layers[layer + 1].neurons[i].delta;
				value = layers[layer].neurons[j].value;
				deltaValLr = delta * value * learningRate;

				weightCurr = layers[layer + 1].neurons[i].weights[j];
				weightPrev = layers[layer + 1].neurons[i].prevWeights[j];
				weightsDiff = momentum * (weightCurr - weightPrev);

				layers[layer + 1].neurons[i].prevWeights[j] = weightCurr;
				layers[layer + 1].neurons[i].weights[j] += deltaValLr + weightsDiff;
			}
			delta = layers[layer + 1].neurons[i].delta;
			layers[layer + 1].neurons[i].bias += learningRate * delta;
		}
	}

	private void transferInput(int layer) {
		double newValue, weight, value;
		for (int i = 0; i < layers[layer].neuronsNum; i++) {
			newValue = 0.0;
			for (int j = 0; j < layers[layer - 1].neuronsNum; j++) {
				weight = layers[layer].neurons[i].weights[j];
				value = layers[layer - 1].neurons[j].value;
				newValue += weight * value;
			}
			if (useBias) {
				newValue += layers[layer].neurons[i].bias;
			}
			layers[layer].neurons[i].value = actFun.evalute(newValue);
		}
	}

	private void createLayers(int[] layersInfo) {
		layers = new Layer[layersInfo.length];
		for (int i = 0; i < layersInfo.length; i++) {
			if (i == 0) {
				this.layers[i] = new Layer(layersInfo[i], 0);
			} else {
				this.layers[i] = new Layer(layersInfo[i], layersInfo[i - 1]);
			}
		}
	}

	public Layer[] getLayers() {
		return layers;
	}
	

}