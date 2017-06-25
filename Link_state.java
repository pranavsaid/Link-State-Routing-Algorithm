/*
 * CS-542-Computer Netwroks - Fall 16 Project Submission  
 * Project Succesfully Written and Executed by Pranav sai Deenumsetti
 * CWID - A20370660
 * To Implement Link State Routing USing Dijkstra’s Shortest Path Algorithms
 */
package link_state;

import java.io.*; // For File IO 
import java.util.*; //To Invoke Java Utilities
import java.util.stream.*; // For inpustream and IntStream Used to add Aray Elements

/**
 *
 * @author dpranavsai
 */
public class Link_state { //Main Class Begins. Only one class is inclided in the project.

    //Initializing some commong variable. However the lenghts of variables can be changble by re initilising 
    static int[][] matrix = new int[99][99];
    static int matrix_size = 0;
    static String[] path = new String[99];
    static String[] interfaces = new String[99];
    static int[] totalCost = new int[99];
    static int bestRouter = 0;
    static int choice = 0;
    static int source = 0, destination = 0;
    static boolean validInput = false;
    static String input = "";
    //boolean values to validate option Selection in Oder Wise.  
    static boolean option1 = false, option2 = false, option3 = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) { // Main Functions

        String exitChoice = "n"; // Intially set to no and is set to Y by user while intended to Exit in COmmand 6
        Scanner sca = new Scanner(System.in);

        while (exitChoice.equalsIgnoreCase("n")) {// This switch case runs unti user wishes to exit.

            String commands = "\n(1) Create a Network Topology\n" //Options intialising
                    + "(2) Build a Connection Table\n"
                    + "(3) Shortest Path to Destination Router\n"
                    + "(4) Modify a Topology\n"
                    + "(5) Best Router for Broadcast\n"
                    + "(6) Exit";
            System.out.println("\n--------------------------------------------------------");
            System.out.println("\nChoose a command from below to proceed:" + commands);
            System.out.println("\nEnter your Command: ");
            choice = sca.nextInt(); // reading choice from user 
            switch (choice) {
                case 1:       //If User Select 1st commnd to create Network Topology
                    do {
                        validInput = false;
                        System.out.println("Enter File Name with Extension(Please place file in the project director)");
                        input = sca.next();
                        if (input.contains(".txt")) { // Checking if the File Contains Extension or Not
                            validInput = true;
                        } else {
                            System.out.println("Please Include Extension");
                        }
                    } while (!validInput); // Repeats until Valid FIle name with Extension in Entered
                    displayMatrix(input);// Calling dispaly matrix function which read an input file and stores the data in to a 2-dimensionall Array
                    option1 = true;
                    break;//End of Case 1
                case 2:       //If User Select 2nd commnd to create connection Table
                    if (option1) {//Chechks if a valid file is read and matrix is stored before procedding
                        do {
                            // To Check if the Source Name is in Valid Range or Not
                            validInput = false;
                            System.out.println("Please Select the Source Router In Between 1 - " + matrix_size);
                            source = sca.nextInt();
                            if (source != 0 && source <= matrix_size) {
                                source = source - 1;
                                validInput = true;
                            } else {
                                System.out.println("Not a Valid Selection");
                            }
                        } while (!validInput);
                        connection(source);// Invokes Connection Function which intakes source table a create a Connection table for that source based on the given Topology
                        option2 = true;
                    } else {
                        System.out.println("Please Input a valid File with Matrix");
                    }

                    break; // End of Choise - 2
                case 3:    //If User Select 3rd commnd to create Shortes PAth from Source to the Destination
                    if (option2) {//Checks if User selected Source before Selecting Destination
                        do {
                            validInput = false;
                            System.out.println("Please Select the Destination Router In Between 1 - " + matrix_size);
                            destination = sca.nextInt();
                            if (destination != 0 && destination <= matrix_size) {
                                destination = destination - 1;
                                validInput = true;
                            } else {
                                System.out.println("Not a Valid Selection");
                            }
                        } while (!validInput);
                        dikj_sp(matrix, matrix_size, source);
                        System.out.println("Shortest Path from Router " + (source + 1) + " to Router " + (destination + 1) + " is ");
                        System.out.format("%3s%15s", "Path", "Cost\n");
                        //path is static Global Variable Updated Inside dikj_sp Function
                        System.out.format("%3s%9d", path[destination], totalCost[destination]);
                        option3 = true;
                    } else {
                        System.out.println("Please Select a source before proceeding");
                    }
                    break; // End of Command 3
                case 4:
                    //If User Select 4th commnd to Update Network Topology by removing a node
                    if (option1) {
                        System.out.println("Update Topology");
                        updateTopology();
                    } else {
                        System.out.println("Please Input a Valid Matrix befor updating");
                    }
                    break; // End of 4th Command
                case 5:
                    //If User Select 5th commnd to view best router in Network Topology
                    if (option1) {
                        //This would calculate the sum of costs from Ith router to all other routers
                        int allRoutersTotal[] = new int[matrix_size];
                        //This would calculate the same thing as above variable but here
                        //the MAx values oh un reached routers are substituted with Zero
                        //for the purpose of total sum calculations
                        int sumOftotalCosts[] = new int[matrix_size];
                        System.out.println("Best Router");
                        for (int i = 0; i < matrix_size; i++) {
                            //For each iteration 1 router is considered as source
                            dikj_sp(matrix, matrix_size, i);//Invoking Dijkstra algo
                            //totalcost is a array which contains all the min distance
                            //from source router to all other routers.
                            allRoutersTotal[i] = IntStream.of(totalCost).sum();
                            //above variable has sum of total costs. Including the unreached variable values
                            //below loop will substitute unreached routers min distance to Zero
                            // and will calculate the SUM again
                            for (int j = 0; j < matrix_size; j++) {
                                if (totalCost[j] == Short.MAX_VALUE) {
                                    totalCost[j] = 0;
                                }
                            }
                            sumOftotalCosts[i] = IntStream.of(totalCost).sum();
                            //New sum is stored here
                        }
                        
                        //below loop will find the minimum value from array obtained above.
                        int min_value = Integer.MAX_VALUE;
                        for (int i = 0; i < allRoutersTotal.length; i++) {
                            if (allRoutersTotal[i] < min_value && allRoutersTotal[i] > 0) {
                                min_value = allRoutersTotal[i];//Min value is set
                                bestRouter = i + 1;//Index containing Min value is noted
                            }
                        }
                        //Update Min Value to the value without unreached routers.
                        min_value = sumOftotalCosts[bestRouter - 1];
                        System.out.println("The Best Router is : " + bestRouter); // Printing the best router Result
                        System.out.println("Sum of the costs from this router to all other routers is :");
                        System.out.println(min_value);// Print the minum Cost obtained.
                        if (option2) {//if Source is intitally selected then update the source value to the selection.
                            dikj_sp(matrix, matrix_size, source);
                        }
                    } else {
                        System.out.println("Please Input a valid matrix before proceeding");
                    }
                    break;// End of Command 5

                case 6:      //If User Select 6th commnd to close Application
                    System.out.println("Do you Really want to exit? (Y/N)");
                    exitChoice = sca.next(); // Read Confirmations. Y is for YEs and N is for No
                    if (exitChoice.equalsIgnoreCase("y")) {
                        System.out.println("Good Bye!!Hope you liked the Application!");
                    }
                    break; // End of Command 6 
                default:
                    // If the User Selection is Not Valid
                    System.out.println("Invalid Choice");
                    break;
            }
        }

    }

    
    //This Function is Invoked In 2nd Command to build Connection Table. This takes a source as input and prints Connection Table.
    static void connection(int s) {
        System.out.println("Connection Table for Router " + (s + 1) + " is ");
        System.out.println("\n--------------------------------------------------------");
        // This formats the output to table lookalike
        System.out.format("%3s%19s", "Name of the router", "Interface Used");
        System.out.println("\n--------------------------------------------------------");
        //Invoking dikjstra Alogirithm based on the current selected source.
        int[][] dikj_data = dikj_sp(matrix, matrix_size, s);
        for (int i = 0; i < matrix_size; ++i) {
            System.out.println("");
            if (dikj_data[i][1] > 0) {
                System.out.format("%3d%25d", i + 1, dikj_data[i][1]);
            } else if (dikj_data[i][1] == 0) {//check if the router is source
                System.out.format("%3d%25s", i + 1, "---"); //cant reach from source to source
            } else {//Any router which cant be reached from the source or which is kept Down
                System.out.format("%3d%50s", i + 1, "Router " + (i + 1) + " Removed or can't be reached from " + (s + 1));
            }

            // System.out.println("----- " + dikj_data[i][0]+ "------"+dikj_data[i][1]);
        }
    }

    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    //display Matrix Function is invoked in Command 1 to read file and store data in 2D array
    static int[][] displayMatrix(String fileName) {
        int rows = 0, columns = 0;
        try {

            File fin = new File(fileName);

            Scanner fileScanner = new Scanner(fin);
            while (fileScanner.hasNextLine()) {
                rows++;
                //columns = fileScanner.nextLine().replaceAll("[^0-9]+", "").length();
            }
            columns= rows;
            matrix = new int[rows][columns];
            matrix_size = rows;
            fileScanner.close();
            fileScanner = new Scanner(fin);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (fileScanner.hasNextInt()) {
                        matrix[i][j] = fileScanner.nextInt();
                    }
                }
            }
            System.out.println("The file has been Read. Please Review the Matrix");
            for (int i = 0; i < rows; ++i) {
                for (int j = 0; j < columns; ++j) {
                    System.out.format("%8d", matrix[i][j]);
                }
                System.out.println("");
            }
        } catch (Exception e) {
            System.out.println("The File you Specified is Not present is this directory!! Please check Extension and File Name and try Again");
            option1 = false;
        }
        return matrix;
    }
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    //updateTopology Function is invoked in Command 4 to remove a routr and then update topology matrix.
    static void updateTopology() {
        int removeNode;
        Scanner sca = new Scanner(System.in);
        do {
            validInput = false;
            System.out.println("\n--------------------------------------------------------");
            System.out.println("\nEnter Node number betweeb 1 and " + matrix_size + " that you wanted to remove: ");
            removeNode = sca.nextInt();
            if (removeNode != 0 && removeNode <= matrix_size) {
                removeNode = removeNode - 1;
                validInput = true;
            } else {
                System.out.println("Not a Valid Selection");
            }
        } while (!validInput);
        for (int i = 0; i < matrix_size; ++i) {
            for (int j = 0; j < matrix_size; ++j) {
                if (i == removeNode || j == removeNode) {
                    matrix[i][j] = -1;
                }
                if (i == removeNode && j == removeNode) {
                    matrix[i][j] = 0;
                }
            }
        }
        for (int i = 0; i < matrix_size; ++i) {
            for (int j = 0; j < matrix_size; ++j) {
                if (matrix[i][j] == Short.MAX_VALUE) {
                    matrix[i][j] = -1;
                }
               }
        }
        System.out.println("The Node is SET down and Thus all the connections are set to -1.\n There are no connections from removed router to any other router \n The Topology is updated as below");
        for (int i = 0; i < matrix_size; ++i) {
            for (int j = 0; j < matrix_size; ++j) {
                System.out.format("%8d", matrix[i][j]);
            }
            System.out.println("");
        }
        if (option2) {
            System.out.println("Updated Connection Table for previously so");
            connection(source);
        }

    }
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    //dikj_sp Function is the one where dikjstra algorithm is implemented.
    //This Functions Reads matrix, Source amd size of matrix. Then it returns 2D array.
    // The returning array contains as many rows as matrix and 2 coulmns.
    // First coulmn is Total cost from source router to Ith router. 
    //Second is the Interface choosen from source to Ith router
    static int[][] dikj_sp(int[][] m, int size, int source) {

        int[][] data = new int[size][2];
        int[][] c = new int[size][size];
        int dist[] = new int[size];
        int previous_node[] = new int[size];
        boolean isNodeVisited[] = new boolean[size];
        int min_value = 0;
        int next = 0;

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (m[i][j] == -1) {
                    m[i][j] = Short.MAX_VALUE;
                }
                c[i][j] = m[i][j];
            }
        }
        for (int i = 0; i < size; i++) {
            isNodeVisited[i] = false;
            dist[i] = c[source][i];
            previous_node[i] = source;
            path[i] = "";
            totalCost[i] = 0;
        }
        dist[source] = 0;
        isNodeVisited[source] = true;
        for (int itteration = 1; itteration < size; itteration++) {

            min_value = Short.MAX_VALUE;
            for (int i = 0; i < size; i++) {
                if (dist[i] < min_value && !(isNodeVisited[i])) {
                    min_value = dist[i];
                    next = i;
                }
            }

            isNodeVisited[next] = true;
            for (int i = 0; i < size; i++) {
                if (!(isNodeVisited[i])) {
                    if (min_value + c[next][i] < dist[i]) {
                        dist[i] = min_value + c[next][i];
                        previous_node[i] = next;
                    }
                }
            }
        }
        for (int i = 0; i < size; i++) {

            //Path is a global Static variable which is updated here with path from 
            //Source to I th router as destination.
            //I is the total no of routers
            path[i] = "" + (i + 1);
            if (dist[i] != Short.MAX_VALUE) {
                totalCost[i] = dist[i];
                data[i][0] = dist[i];
                if (i != source) {
                    data[i][1] = i + 1;
                    int j = i;
                    j = i;
                    do {
                        j = previous_node[j];
                        path[i] = path[i] + "<-" + (j + 1);
                        if (j != source) {
                            data[i][1] = j + 1;
                        }
                    } while (j != source);

                }
            } else {
                totalCost[i] = Short.MAX_VALUE;
                data[i][0] = 0;
                data[i][1] = -1;
                path[i] = "Removed or Can't be reached from source";
            }

        }
        
        return data;
        //As stated above this function would return the 2 dimensional array with
        //Total cost and Interface choosen from source to all routers
    }

}
