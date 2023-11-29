import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.ArrayList;

public class kNN1 {
    ArrayList<Float> train_data = new ArrayList<Float>();
    /*
Load all the data (similar to the class work, after setting the required declarations)
• Implement the necessary codes to compute Euclidean distance measures of the
test data from the training data, to obtain the predicted labels (using k=1) and the
classification accuracy.
• Compile and run. As the test data labels are provided, you should be able to obtain
the classification accuracy. This will be the benchmark accuracy that you will need
to improve for Part B.
• The predicted labels of the test data should also be generated in the output1.txt
file in the manner prescribed below (write the codes for this). In the first line, there
will be a single line of 200 values in the file with either 0 or 1 representing the
predicted class of the test data with a single space between the predicted labels.
• You may wish to compile and run your kNN1.java to ensure that it runs correctly
on raptor as markers will re-compile and run this kNN1.java which will generate
the predicted labels in output1.txt. Using the predicted labels in output1.txt,
markers will obtain the classification accuracy to be used in the marking scheme.
• You need to place your solution file:
kNN1.java
in:/proj/comp8250/ga/xyz
 */
    public void loadData() {
        // load the data into an array
         try {
             File myObj = new File("train_data.txt");
             Scanner myReader = new Scanner(myObj);
                    while (myReader.hasNextFloat()) {
                        this.train_data.add(myReader.nextFloat());
                        System.out.println(this.train_data );
                    }
                    myReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }

    public void load_Data() {
    }
}
