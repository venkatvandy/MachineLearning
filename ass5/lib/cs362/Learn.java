package cs362;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class
Learn {
	static public LinkedList<Option> options = new LinkedList<Option>();
	public static double online_learning_rate;
	public static int online_training_iterations;
	public static double lambda;
	public static String kernel;
	public static double polynomial_kernel_exponent;
	public static double gaussian_kernel_sigma;
	public static double gradient_ascent_learning_rate;
	public static int gradient_ascent_training_iterations;
	public static double cluster_lambda;
	public static int clustering_training_iterations;


	public static void main(String[] args) throws IOException {
		// Parse the command line.
		String[] manditory_args = { "mode"};
		createCommandLineOptions();
		CommandLineUtilities.initCommandLineParameters(args, Learn.options, manditory_args);
	
		String mode = CommandLineUtilities.getOptionValue("mode");
		String data = CommandLineUtilities.getOptionValue("data");
		String predictions_file = CommandLineUtilities.getOptionValue("predictions_file");
		String algorithm = CommandLineUtilities.getOptionValue("algorithm");
		String model_file = CommandLineUtilities.getOptionValue("model_file");
		String task = CommandLineUtilities.getOptionValue("task"); // classification vs. regression

		boolean classify = true;

		cluster_lambda = 0.0;
		if (CommandLineUtilities.hasArg("cluster_lambda"))
			cluster_lambda = CommandLineUtilities.getOptionValueAsFloat("cluster_lambda");

		clustering_training_iterations = 10;
		if (CommandLineUtilities.hasArg("clustering_training_iterations"))
			clustering_training_iterations = CommandLineUtilities.getOptionValueAsInt("clustering_training_iterations");

		online_learning_rate = 1.0;
		if (CommandLineUtilities.hasArg("online_learning_rate"))
			online_learning_rate = CommandLineUtilities.getOptionValueAsFloat("online_learning_rate");

		online_training_iterations = 1;
		if (CommandLineUtilities.hasArg("online_training_iterations"))
			online_training_iterations = CommandLineUtilities.getOptionValueAsInt("online_training_iterations");

		lambda = 1.0;
		if (CommandLineUtilities.hasArg("lambda"))
			lambda = CommandLineUtilities.getOptionValueAsFloat("lambda");

		kernel = "linear_kernel";
		if (CommandLineUtilities.hasArg("kernel"))
			kernel = CommandLineUtilities.getOptionValue("kernel");

		polynomial_kernel_exponent = 2;
		if (CommandLineUtilities.hasArg("polynomial_kernel_exponent"))
			polynomial_kernel_exponent = CommandLineUtilities.getOptionValueAsFloat("polynomial_kernel_exponent");

		gaussian_kernel_sigma = 1;
		if (CommandLineUtilities.hasArg("gaussian_kernel_sigma"))
			gaussian_kernel_sigma = CommandLineUtilities.getOptionValueAsFloat("gaussian_kernel_sigma");

		gradient_ascent_learning_rate = 0.01;
		if (CommandLineUtilities.hasArg("gradient_ascent_learning_rate"))
			gradient_ascent_learning_rate =
					CommandLineUtilities.getOptionValueAsFloat("gradient_ascent_learning_rate");

		gradient_ascent_training_iterations = 5;
		if (CommandLineUtilities.hasArg("gradient_ascent_training_iterations"))
			gradient_ascent_training_iterations =
					CommandLineUtilities.getOptionValueAsInt("gradient_ascent_training_iterations");

		if (task != null && task.equals("regression")) {
		    classify = false;
		}
		
		if (mode.equalsIgnoreCase("train")) {
			if (data == null || algorithm == null || model_file == null) {
				System.out.println("Train requires the following arguments: data, algorithm, model_file");
				System.exit(0);
			}
			// Load the training data.
			DataReader data_reader = new DataReader(data, classify);
			List<Instance> instances = data_reader.readData();
			data_reader.close();
			
			// Train the model.
			Predictor predictor = train(instances, algorithm);
			saveObject(predictor, model_file);		
			
		} else if (mode.equalsIgnoreCase("test")) {
			if (data == null || predictions_file == null || model_file == null) {
				System.out.println("Train requires the following arguments: data, predictions_file, model_file");
				System.exit(0);
			}
			
			// Load the test data.
			DataReader data_reader = new DataReader(data, classify);
			List<Instance> instances = data_reader.readData();
			data_reader.close();
			
			// Load the model.
			Predictor predictor = (Predictor)loadObject(model_file);
			evaluateAndSavePredictions(predictor, instances, predictions_file);
		} else {
			System.out.println("Requires mode argument.");
		}
	}
	

	private static Predictor train(List<Instance> instances, String algorithm) {
	    // TODO Train the model using "algorithm" on "data"
	    // TODO Evaluate the model
		if(algorithm.equalsIgnoreCase("lambda_means")){
			LambdaMeansPredictor lmp = new LambdaMeansPredictor(instances,cluster_lambda,clustering_training_iterations);
			lmp.train(instances);
			return lmp;
		}
		return null;
	}

	private static void evaluateAndSavePredictions(Predictor predictor,
			List<Instance> instances, String predictions_file) throws IOException {
		PredictionsWriter writer = new PredictionsWriter(predictions_file);
		// TODO Evaluate the model if labels are available.
		double accuracy = 0;
		int total = 0;
		
		for (Instance instance : instances) {
			Label label = predictor.predict(instance);
			//KernelLogisticRegression.count++;
			writer.writePrediction(label);
			if(instance.getLabel()!=null){
				total += 1;
				if(label.toString().equals(instance.getLabel().toString())){
					accuracy += 1;
				}
			}
		}
		double clrate = accuracy*100/total;
		//System.out.println("\n\n****Accuracy****: "+ clrate);

		writer.close();
		
	}

	public static void saveObject(Object object, String file_name) {
		try {
			ObjectOutputStream oos =
				new ObjectOutputStream(new BufferedOutputStream(
						new FileOutputStream(new File(file_name))));
			oos.writeObject(object);
			oos.close();
		}
		catch (IOException e) {
			System.err.println("Exception writing file " + file_name + ": " + e);
		}
	}

	/**
	 * Load a single object from a filename. 
	 * @param file_name
	 * @return
	 */
	public static Object loadObject(String file_name) {
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(file_name))));
			Object object = ois.readObject();
			ois.close();
			return object;
		} catch (IOException e) {
			System.err.println("Error loading: " + file_name);
		} catch (ClassNotFoundException e) {
			System.err.println("Error loading: " + file_name);
		}
		return null;
	}
	
	public static void registerOption(String option_name, String arg_name, boolean has_arg, String description) {
		OptionBuilder.withArgName(arg_name);
		OptionBuilder.hasArg(has_arg);
		OptionBuilder.withDescription(description);
		Option option = OptionBuilder.create(option_name);
		
		Learn.options.add(option);		
	}
	
	private static void createCommandLineOptions() {
		registerOption("data", "String", true, "The data to use.");
		registerOption("mode", "String", true, "Operating mode: train or test.");
		registerOption("predictions_file", "String", true, "The predictions file to create.");
		registerOption("algorithm", "String", true, "The name of the algorithm for training.");
		registerOption("model_file", "String", true, "The name of the model file to create/load.");
		registerOption("task", "String", true, "The name of the task (classification or regression).");
		registerOption("online_learning_rate", "double", true, "The LTU learning rate.");
		registerOption("online_training_iterations", "int", true, "The number of training iterations for LTU.");
		registerOption("lambda", "double", true, "The level of smoothing for Naive Bayes.");
		registerOption("kernel", "String", true, "The kernel for Kernel Logistic regression [linear_kernel, polynomial_kernel, gaussian_kernel].");
		registerOption("polynomial_kernel_exponent", "double", true, "The exponent of the polynomial kernel.");
		registerOption("gaussian_kernel_sigma", "double", true, "The sigma of the Gaussian kernel.");
		registerOption("gradient_ascent_learning_rate", "double", true, "The learning rate for logistic regression.");
		registerOption("gradient_ascent_training_iterations", "int", true, "The number of training iterations.");
		registerOption("cluster_lambda", "double", true, "The value of lambda in lambda-means.");
		registerOption("clustering_training_iterations", "int", true, "The number of lambda-means EM iterations.");

		// Other options will be added here.
	}
}
