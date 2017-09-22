package cs362;

import java.util.List;

public class LinearKernelLogisticRegression extends KernelLogisticRegression {

    public LinearKernelLogisticRegression(List<Instance> instances, String kernel, double polynomial_kernel_exponent, double gaussian_kernel_sigma, double gradient_ascent_learning_rate, int gradient_ascent_training_iterations) {
        super(instances, kernel, polynomial_kernel_exponent, gaussian_kernel_sigma, gradient_ascent_learning_rate, gradient_ascent_training_iterations);
    }

    public void calculate_gram_matrix(){

    }
}
