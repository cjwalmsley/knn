import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class knn_class {
    ArrayList<ArrayList> train_data = new ArrayList<ArrayList>();
    ArrayList<ArrayList> test_data = new ArrayList<ArrayList>();
    ArrayList<Integer> train_label = new ArrayList<Integer>();
    ArrayList<Integer> test_label = new ArrayList<Integer>();

    public void load_all_Data() {
        this.train_data = this.load_float_Data("test_data_class.txt");
        this.test_data = this.load_float_Data("train_data_class.txt");
        this.train_label = this.load_int_Data("test_label_class.txt").get(0);
        this.test_label = this.load_int_Data("train_label_class.txt").get(0);
    }

    public ArrayList<ArrayList> load_float_Data(String filename) {
        // load the data into an array

        ArrayList<ArrayList> data = new ArrayList<>();

        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line_data = myReader.nextLine();
                ArrayList<Float> line_array = new ArrayList<Float>();
                Scanner stringScanner = new Scanner(line_data);
                while (stringScanner.hasNextFloat()) {
                    line_array.add(stringScanner.nextFloat());
                }
                data.add(line_array);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return data;
    }
    public ArrayList<ArrayList> load_int_Data(String filename) {
        // load the data into an array

        ArrayList<ArrayList> data = new ArrayList<>();

        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line_data = myReader.nextLine();
                ArrayList<Integer> line_array = new ArrayList<Integer>();
                Scanner stringScanner = new Scanner(line_data);
                while (stringScanner.hasNextInt()) {
                    line_array.add(stringScanner.nextInt());
                }
                data.add(line_array);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return data;
    }
    public ArrayList<ArrayList> calculate_knn_array_for(ArrayList training_array, ArrayList testing_array) {

        ArrayList<ArrayList> result = new ArrayList<ArrayList>();

        for (int i=0; i < testing_array.size(); i++) {
            ArrayList<Float> testing_row = (ArrayList<Float>) testing_array.get(i);
            ArrayList training_index_and_knn_index = new ArrayList<ArrayList>();
            training_index_and_knn_index.add(i);
            training_index_and_knn_index.add(this.row_index_of_knn_for(testing_row, training_array));
            result.add(training_index_and_knn_index);
        };
        return result;
    }
    public Integer row_index_of_knn_for (ArrayList<Float> testing_row, ArrayList<ArrayList> training_array) {

        Integer result = 0;
        Float closest_knn = (float) 1000.0f;
        for (int i=0; i < training_array.size(); i++)
        {
            ArrayList<Float> training_row = (ArrayList<Float>) training_array.get(i);
            Float knn = this.compute_distance_between(training_row, testing_row);
            if(knn < closest_knn) {
                result = i;
                closest_knn = knn;
            }
        }
        return result;
    }
    public Float compute_distance_between(ArrayList<Float> training_row, ArrayList<Float> testing_row) {

        Iterator<Float> training_values = training_row.iterator();
        Iterator<Float> testing_values = testing_row.iterator();

        Float total = 0.0f;
        while (training_values.hasNext() && testing_values.hasNext()) {
            total = (float) (total + Math.pow((training_values.next() - testing_values.next()), 2));
        }
        return (float) Math.sqrt(total);
    }

    public ArrayList<Integer> get_predictions (ArrayList<ArrayList> training_results, ArrayList<Integer> training_labels) {

        ArrayList<Integer> result = new ArrayList<>();
        for (int i=0; i < training_results.size(); i++)
        {
            ArrayList<Integer> result_row = (ArrayList<Integer>) training_results.get(i);
            Integer predicted_value = training_labels.get(result_row.get(1));
            result.add(predicted_value);
            }
        return result;
    }

    public Float percentage_of_matching_labels(ArrayList<Integer> predictions, ArrayList<Integer> test_labels) {
        Integer matching_predictions = 0;
        for (int i=0; i < predictions.size(); i++)
        {
            if(predictions.get(i).equals(test_labels.get(i))){
                matching_predictions ++;
            }
        }
        System.out.println(matching_predictions);
        System.out.println(predictions.size());
        System.out.println((float) matching_predictions / (float) predictions.size());
        return (float) (((float)matching_predictions / (float) predictions.size()) * 100.0f);
    }
}
