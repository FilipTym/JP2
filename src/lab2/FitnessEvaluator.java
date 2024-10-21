package lab2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


// Klasa FitnessEvaluator zawiera metody oceny efektywności przypisania pracowników do projektów

public class FitnessEvaluator {

    // Metoda obliczająca całkowitą efektywność przypisania pracowników do projektów

    public static int evaluate(List<EmployeeAssignment> assignments) {
        int totalFitness = 0;
// assignments lista przypisań pracowników do projektów
        for (EmployeeAssignment assignment : assignments) {
            int projectFitness = calculateProjectFitnessWithSkills(assignment.getProject(), assignment.getEmployee());
            totalFitness += projectFitness;
        }
//końcowa efektywność przypisania
        return totalFitness;
    }


     //Metoda obliczająca efektywność przypisania pracownika do konkretnego projektu, uwzględniając umiejętności

     // zwracamy efektywność przypisania pracownika do projektu.

    public static int calculateProjectFitnessWithSkills(Project project, Employee employee) {
        int projectFitness = 0;

        Set<String> uniqueSkills = new HashSet<>(project.getSkills());

        for (String skill : uniqueSkills) {
            if (employee.getSkills().contains(skill)) {
                projectFitness++;
            }
        }

        // Ustawienie limitów dla QA i PM
        if (project.getSkills().contains("PM") || project.getSkills().contains("QA")) {
            int pmAndQaCount = countPmAndQaProjects(employee.getAssignments());
            if (pmAndQaCount >= 2) {
                projectFitness = 0;
            } else if (pmAndQaCount == 1) {
                projectFitness = Math.max(0, projectFitness - 1);
            }
        }

        return projectFitness;
    }


    //Metoda zliczająca ilość projektów, do których pracownik został przypisany jako PM lub QA
    private static int countPmAndQaProjects(List<EmployeeAssignment> assignments) {
        int count = 0;
        for (EmployeeAssignment assignment : assignments) {
            Project project = assignment.getProject();
            List<String> skills = project.getSkills();
            if (skills.contains("PM") || skills.contains("QA")) {
                count++;
            }
        }
        return count;
    }
}
