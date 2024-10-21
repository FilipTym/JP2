package lab2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Printer {


     //Metoda wyświetlająca przydziały pracowników do projektów wraz z ich umiejętnościami i dopasowaniem do projektu

     // assignments = Przydziały pracowników do projektów
     // projects = Lista projektów

    public static void printEmployeeAssignmentsToProjects(List<EmployeeAssignment> assignments, List<Project> projects) {
        System.out.println("---------------------------------------------");
        System.out.println("Employee assignments to projects:");

        // Mapa przechowująca przydziały pracowników do poszczególnych projektów
        Map<Project, List<EmployeeAssignment>> projectAssignments = new HashMap<>();

        // Grupowanie przydziałów według projektów
        for (EmployeeAssignment assignment : assignments) {
            Project project = assignment.getProject();
            projectAssignments.computeIfAbsent(project, k -> new ArrayList<>()).add(assignment);
        }

        // Wyświetlanie przydziałów dla każdego projektu
        for (Map.Entry<Project, List<EmployeeAssignment>> entry : projectAssignments.entrySet()) {
            Project project = entry.getKey();
            List<EmployeeAssignment> employeeAssignments = entry.getValue();

            if (employeeAssignments != null && !employeeAssignments.isEmpty()) {
                System.out.println(project.getName() + ": Skills - " + project.getSkills());

                // Wyświetlanie szczegółów przydziałów dla danego projektu
                for (EmployeeAssignment employeeAssignment : employeeAssignments) {
                    Employee employee = employeeAssignment.getEmployee();
                    List<String> responsibilities = new ArrayList<>(employeeAssignment.getEmployee().getSkills());
                    responsibilities.retainAll(project.getSkills());  // Zatrzymaj tylko umiejętności związane z projektem

                    System.out.println("  Employee: " + employee.getName() +
                            " | Responsibilities: " + responsibilities +
                            " | Fitness: " + FitnessEvaluator.calculateProjectFitnessWithSkills(project, employee));
                }
            }
        }
    }


     // Metoda wyświetlająca informacje o projektach wraz z przypisanymi do nich umiejętnościami.

    public static void printProjects(List<Project> projects) {
        System.out.println("Projects:");
        for (Project project : projects) {
            System.out.println(project.getName() + " - Skills: " + project.getSkills());
        }
    }

     // Metoda wyświetlająca informacje o pracownikach wraz z ich umiejętnościami

    public static void printEmployees(List<Employee> employees) {
        System.out.println("Employees:");
        for (Employee employee : employees) {
            System.out.println(employee.getName() + " - Skills: " + employee.getSkills());
        }
    }
}