package lab2;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        // Sprawdzenie, czy podano poprawną liczbę argumentów wejściowych
        if (args.length != 1) {
            System.err.println("Usage: java Application <data.txt>");
            System.exit(1); // Wyjście z programu z kodem błędu
        }

        // Pobranie ścieżki do pliku z danymi wejściowymi z argumentów linii poleceń
        String inputFilePath = args[0];
        ResourceAllocationProgram resourceAllocationProgram = new ResourceAllocationProgram();

        // Uruchomienie programu alokacji zasobów
        List<EmployeeAssignment> assignments = resourceAllocationProgram.run(inputFilePath);


    }
}
