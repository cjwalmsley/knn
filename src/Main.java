import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        knn_class instance = new knn_class();
        instance.load_all_Data();
        System.out.println(instance.train_data);
        System.out.println(instance.test_data);
        System.out.println(instance.test_label);
        System.out.println(instance.train_label);

        // find knn for all test data
        ArrayList<ArrayList> training_results = instance.calculate_knn_array_for(instance.train_data, instance.test_data);
        ArrayList<Integer> predictions = instance.get_predictions(training_results, instance.train_label);
        Float percentage_predicted = instance.percentage_of_matching_labels(predictions, instance.test_label);
        System.out.println(predictions);
        System.out.println(instance.test_label);
        System.out.println(percentage_predicted);

    }
    }