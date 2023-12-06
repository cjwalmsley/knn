import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class kNN2 {

    Boolean use_min_max_normalisation = false;
    Integer k = 3;
    Integer minkowski_distance = 1;
    ArrayList<ArrayList> train_data = new ArrayList<ArrayList>();
    ArrayList<ArrayList> test_data = new ArrayList<ArrayList>();
    ArrayList<Integer> train_label = new ArrayList<Integer>();
    ArrayList<Integer> test_label = new ArrayList<Integer>();

    public void load_all_Data() {
        this.train_data = this.load_double_Data("test_data.txt");
        this.test_data = this.load_double_Data("train_data.txt");
        this.train_label = this.load_int_Data("test_label.txt").get(0);
        this.test_label = this.load_int_Data("train_label.txt").get(0);
    }

    private ArrayList<ArrayList> load_double_Data(String filename) {
        // load the data into an array

        ArrayList<ArrayList> data = new ArrayList<>();

        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line_data = myReader.nextLine();
                ArrayList<Double> line_array = new ArrayList<Double>();
                Scanner stringScanner = new Scanner(line_data);
                while (stringScanner.hasNextDouble()) {
                    line_array.add(stringScanner.nextDouble());
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

    public void normalise_all_data() {
        if(this.use_min_max_normalisation){
            this.min_max_mornalise(this.train_data, this.test_data);
        }
    }
    public void min_max_mornalise(ArrayList<ArrayList> trainData, ArrayList<ArrayList> testData) {
        ArrayList<Double> flattened_array = new ArrayList<Double>();
        ArrayList<ArrayList> joined_array = new ArrayList<ArrayList>();
        joined_array.addAll(trainData);
        joined_array.addAll(testData);

        //Collections.sort(joined_array);
        Double max_value = Double.MAX_VALUE;
        Double min_value = Double.MIN_VALUE;
    }
    public ArrayList<ArrayList> calculate_knn_array_for(ArrayList training_array, ArrayList testing_array) {

        ArrayList<ArrayList> result = new ArrayList<ArrayList>();


        for (int i=0; i < testing_array.size(); i++) {
            ArrayList<Double> testing_row = (ArrayList<Double>) testing_array.get(i);
            ArrayList<Object> training_index_and_predicted_class = new ArrayList<Object>();
            Integer nearest_neighbour_class = this.nearest_neighbour_class_for(this.row_indices_of_nearest_neighbours_for(testing_row, training_array));
            // are the below 2 the wrong way round?
            training_index_and_predicted_class.add(i);
            // need to return index?  or change sending method
            training_index_and_predicted_class.add(nearest_neighbour_class);
            result.add(training_index_and_predicted_class);
        };
        return result;
    }
    public Integer nearest_neighbour_class_for(ArrayList<Integer> row_indices_of_nearest_neighbours) {
// write code to pick out the nearest neighbour for the given value of k

        Integer one_count = 0;
        Integer zero_count = 0;
        Iterator<Integer> nearest_neighbour_indices = row_indices_of_nearest_neighbours.iterator();
        Integer nearest_neighbour_class = 2;

        while (nearest_neighbour_indices.hasNext()) {
            Integer classification = this.train_label.get(nearest_neighbour_indices.next());
            if (classification == 1) {
                one_count++;
            } else {
                zero_count++;
            }
        }
            if (one_count >= zero_count) {
                nearest_neighbour_class = 1;
            } else {
                nearest_neighbour_class = 0;
            }
        return nearest_neighbour_class;
        }

        public ArrayList<Integer> row_indices_of_nearest_neighbours_for (ArrayList<Double> testing_row, ArrayList<ArrayList> training_array) {
        ArrayList<ArrayList> neighbours_and_distances = new ArrayList<ArrayList>();
        ArrayList<Integer> indices = new ArrayList<Integer>();
        for (int i=0; i < training_array.size(); i++)
        {
            ArrayList<Double> training_row = (ArrayList<Double>) training_array.get(i);
            Double nearest_neighbour_distance = this.compute_distance_between(training_row, testing_row);
            this.update_nearest_neighbours_and_distances_with(nearest_neighbour_distance, i, neighbours_and_distances);
            }
        // strip out distances and just return indices
        for (int i=0; i < neighbours_and_distances.size(); i++){
            indices.add((Integer) neighbours_and_distances.get(i).getFirst());
        }
        return indices;
    }
    public void update_nearest_neighbours_and_distances_with(Double nearest_neighbour_distance, Integer index, ArrayList<ArrayList>neighbours_and_distances) {
        ArrayList<Object> new_neighbour = new ArrayList<Object>();
        new_neighbour.add(index);
        new_neighbour.add(nearest_neighbour_distance);
        if (neighbours_and_distances.size() < this.k) {
            neighbours_and_distances.add(new_neighbour);
        } else{
            ArrayList<Object> furthest_neighbour = new ArrayList<Object>();
            for (int i=0; i < neighbours_and_distances.size(); i++){
                if(furthest_neighbour.isEmpty() || (double) furthest_neighbour.getLast() < (double) neighbours_and_distances.get(i).getLast()){
                    furthest_neighbour = neighbours_and_distances.get(i);
                }
            }
            if((double) furthest_neighbour.getLast() > (double) new_neighbour.getLast()){
                neighbours_and_distances.set(neighbours_and_distances.indexOf(furthest_neighbour), new_neighbour);
            }
        }
    }
    private Double compute_distance_between(ArrayList<Double> trainingRow, ArrayList<Double> testingRow) {
        return this.compute_minkowski_distance_between(trainingRow, testingRow, (double) this.minkowski_distance);
    }

    private Double compute_minkowski_distance_between(ArrayList<Double> training_row, ArrayList<Double> testing_row, Double power) {
        Iterator<Double> training_values = training_row.iterator();
        Iterator<Double> testing_values = testing_row.iterator();

        Double total = 0.0;
        while (training_values.hasNext() && testing_values.hasNext()) {
            Double addition = 0.0;
            addition = (Double) Math.pow(Math.abs((training_values.next() - testing_values.next())), power);
            total = (total + addition);
        }
        Double result = (Double) Math.pow(total, 1/power);
        return result;
    }

    public ArrayList<Integer> get_predictions (ArrayList<ArrayList>training_results) {

        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int i=0; i < training_results.size(); i++)
        {
            Integer predicted_value = (Integer) training_results.get(i).getLast();
            result.add(predicted_value);
        }
        return result;
    }

    public Double percentage_of_matching_labels(ArrayList<Integer> predictions, ArrayList<Integer> test_labels) {
        Integer matching_predictions = 0;
        for (int i=0; i < predictions.size(); i++)
        {
            if(predictions.get(i).equals(test_labels.get(i))){
                matching_predictions ++;
            }
        }
        System.out.println(matching_predictions);
        System.out.println(predictions.size());
        System.out.println((double) matching_predictions / (double) predictions.size());
        return (double) (((double)matching_predictions / (double) predictions.size()) * 100.0f);
    }
    public void write_out_predictions(ArrayList<Integer> predictions, String filename) {
        //create the file if it does not exist already
        try {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            }
            else {
                System.out.println("File already exists.");
            }
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        //write out the file
        try {
            FileWriter myWriter = new FileWriter(filename);
            for (int i: predictions) {
                myWriter.write(String.valueOf(i));
                myWriter.write(" ");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        kNN2 instance = new kNN2();
        instance.load_all_Data();
        instance.normalise_all_data();
        //System.out.println(instance.train_data);
        //System.out.println(instance.test_data);
        //System.out.println(instance.test_label);
        //System.out.println(instance.train_label);

        // find knn for all test data
        ArrayList<ArrayList> training_results = instance.calculate_knn_array_for(instance.train_data, instance.test_data);
        System.out.println(training_results);
        ArrayList<Integer> predictions = instance.get_predictions(training_results);
        Double percentage_predicted = instance.percentage_of_matching_labels(predictions, instance.test_label);
        System.out.println(predictions);
        System.out.println(instance.test_label);
        System.out.println(percentage_predicted);
        instance.write_out_predictions(predictions, "output2.txt");

    }
}