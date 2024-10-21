package lab2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


 // Klasa ResourceAllocationProgram reprezentuje program do alokacji zasobów

public class ResourceAllocationProgram {
    private List<Project> projects;  // Lista projektów dostępnych do alokacji zasobów


     // Metoda główna programu 'run' uruchamiająca proces alokacji zasobów
     // zwraca Listę przypisań pracowników do projektów, reprezentująca optymalny wynik alokacji

    public List<EmployeeAssignment> run(String inputFilePath) {
        List<EmployeeAssignment> bestAssignments = null;  // Lista najlepszych przypisań pracowników do projektów
        int bestFitness = Integer.MIN_VALUE;  // Najlepsza wartość naszej funkcji dopasowania Fitness

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            List<Project> projects = Parser.parseProjects(reader);  // Parsowanie projektów z pliku
            List<Employee> employees = Parser.parseEmployees(reader);  // Parsowanie pracowników z pliku

            // Sortowanie projektów alfabetycznie (i tak wypisują sie losowo)
            projects.sort(Comparator.comparing(Project::getName));


            Printer.printProjects(projects);  // Wyświetlenie informacji o projektach
            Printer.printEmployees(employees);  // Wyświetlenie informacji o pracownikach

            // Iteracja główna - próba znalezienia optymalnego przypisania w ciągu 10000 iteracji
            for (int i = 0; i < 10000; i++) {
                List<EmployeeAssignment> currentAssignments = assignEmployeesToProjects(deepCopyProjects(projects), employees);

                int currentFitness = FitnessEvaluator.evaluate(currentAssignments);

                // Aktualizacja najlepszego przypisania, jeśli obecne przypisanie ma lepszą wartość funkcji dopasowania
                if (currentFitness > bestFitness) {
                    bestFitness = currentFitness;
                    bestAssignments = new ArrayList<>(currentAssignments);
                }
            }

            // Wyświetlenie ostatecznych wyników alokacji
            if (bestAssignments != null) {
                Printer.printEmployeeAssignmentsToProjects(bestAssignments, this.projects);
                System.out.println("Total Fitness: " + bestFitness);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bestAssignments;
    }
    // Głęboka kopia listy projektów

    private List<Project> deepCopyProjects(List<Project> projects) {
        return projects.stream()
                .map(Project::deepCopy)
                .collect(Collectors.toList());
    }


    //  Przypisujemy pracowników do projektów, starając się maksymalizować wartość funkcji dopasowania

     // projects to Lista projektów dostępnych do alokacji zasobów
     // employees to Lista pracowników dostępnych do przypisania
     //końcowa Lista przypisań pracowników do projektów

    private List<EmployeeAssignment> assignEmployeesToProjects(List<Project> projects, List<Employee> employees) {
        List<EmployeeAssignment> assignments = new ArrayList<>();

        // Iteracja po projektach
        for (Project project : projects) {
            List<Employee> eligibleEmployees = findEligibleEmployeesForProject(project, employees, assignments);
//tasujemy listę pracowników żeby zwiększyć losowość
            Collections.shuffle(eligibleEmployees);

            int numAssignments = getRequiredAssignments(project) - countAssignmentsForProject(assignments, project);

            // Iteracja po przypisaniach dla danego projektu
            for (int i = 0; i < Math.min(numAssignments, eligibleEmployees.size()); i++) {
                Employee bestEmployee = eligibleEmployees.get(i);

                // Sprawdzenie, czy pracownik już jest przypisany do umiejętności projektu
                if (isEmployeeAlreadyAssignedToSkills(bestEmployee, assignments, project.getSkills())) {
                    continue;
                }

                // Sprawdzenie, czy aktualne przypisanie ma wyższą wartość funkcji dopasowania
                EmployeeAssignment currentAssignment = getAssignmentForProject(assignments, project);
                if (currentAssignment != null && FitnessEvaluator.calculateProjectFitnessWithSkills(project, bestEmployee) > FitnessEvaluator.calculateProjectFitnessWithSkills(project, currentAssignment.getEmployee())) {
                    assignments.remove(currentAssignment);
                    eligibleEmployees.add(currentAssignment.getEmployee());
                }

                assignments.add(new EmployeeAssignment(bestEmployee, project));

                // Opcjonalne ograniczenie dla PM i QA - usuwa pracowników, którzy już są przypisani do PM lub QA
                if (project.getSkills().contains("PM") || project.getSkills().contains("QA")) {
                    eligibleEmployees.removeIf(employee ->
                            !employee.equals(bestEmployee) &&
                                    (employee.getSkills().contains("PM") || employee.getSkills().contains("QA")));
                }
            }
        }

        return assignments;
    }


      //Sprawdzamy, czy pracownik już jest przypisany do umiejętności projektu
     // zwraca true, jeśli pracownik jest już przypisany do umiejętności projektu, w przeciwnym razie false

    private boolean isEmployeeAlreadyAssignedToSkills(Employee employee, List<EmployeeAssignment> assignments, List<String> skills) {
        return assignments.stream()
                //filtrujemy żeby sprawdzić, które umiejętności dotyczą danego pracownika
                .filter(assignment -> assignment.getEmployee().equals(employee))
                //sprawdzamy czy którykolwiek się matchuje
                .anyMatch(assignment -> skills.stream().anyMatch(skill -> assignment.getProject().getSkills().contains(skill)));
        // jeżeli jest taki zwraca true
    }


     //Zwraca liczbę wymaganych przypisań dla danego projektu.
    private int getRequiredAssignments(Project project) {
        return (int) project.getSkills().stream()
                .filter(skill -> !skill.equals("PM") && !skill.equals("QA"))
                .count();
    }


     // Zlicza ilość przypisań pracowników do danego projektu

     // assignments to Lista przypisań pracowników do projektów.
     // project  to   Projekt do sprawdzenia.

    private int countAssignmentsForProject(List<EmployeeAssignment> assignments, Project project) {
        return (int) assignments.stream()
                .filter(assignment -> assignment.getProject().equals(project))
                .count();
    }


     // Znajduje pracowników, którzy są odpowiedni do przypisania do danego projektu.

     // project   to  Projekt, do którego pracownicy są przypisywani.
      //employees  to Lista dostępnych pracowników.
      //assignments to Lista obecnych przypisań pracowników do projektów.

    private List<Employee> findEligibleEmployeesForProject(Project project, List<Employee> employees, List<EmployeeAssignment> assignments) {
        employees.sort(Comparator.comparingInt(employee -> -FitnessEvaluator.calculateProjectFitnessWithSkills(project, employee)));

        return employees.stream()
                .filter(employee -> employee.canWorkOnProjectWithSkills(project) && !isEmployeeAlreadyAssigned(employee, assignments) && getAssignmentForProject(assignments, project) == null)
                .collect(Collectors.toList());
    }


     // Sprawdza, czy pracownik już jest przypisany do projektu.
     //zwraca true, jeśli pracownik już jest przypisany do projektu, w przeciwnym razie false.
    private boolean isEmployeeAlreadyAssigned(Employee employee, List<EmployeeAssignment> assignments) {
        return assignments.stream()
                .anyMatch(assignment -> assignment.getEmployee().equals(employee) && assignment.getProject().equals(assignment.getProject()));
    }


     // Znajduje przypisanie pracownika do danego projektu.
     // zwraca Przypisanie pracownika do projektu, jeśli istnieje; w przeciwnym razie null

    private EmployeeAssignment getAssignmentForProject(List<EmployeeAssignment> assignments, Project project) {
        return assignments.stream()
                .filter(assignment -> assignment.getProject().equals(project))
                .findFirst()
                .orElse(null);
    }
}
