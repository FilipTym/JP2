package lab2;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private String name;
    private List<String> skills;
    private List<EmployeeAssignment> assignments;


    public Employee(String name) {
        this.name = name;
        this.skills = new ArrayList<>();
        this.assignments = new ArrayList<>();
    }

    public String getName() {
        return name;
    }


    public List<String> getSkills() {
        return skills;
    }


     // dodawanie umiejętności do zestawu umiejętności pracownika
    public void addSkill(String skill) {
        skills.add(skill);
    }

     // Pobiera listę przypisanych pracownikowi zadań
     // return Lista przypisanych zadań pracownika

    public List<EmployeeAssignment> getAssignments() {
        return assignments;
    }

     // Dodaje zadanie do listy

    public void addAssignment(EmployeeAssignment assignment) {
        assignments.add(assignment);
    }


     // Sprawdza, czy pracownik może pracować nad projektem z określonymi umiejętnościami


     // metoda zwraca True, jeśli pracownik może pracować nad projektem; false w przeciwnym razie

    public boolean canWorkOnProjectWithSkills(Project project) {
        int qaOrPmProjectsCount = 0;

        for (EmployeeAssignment assignment : assignments) {
            Project assignedProject = assignment.getProject();
            if (assignedProject.getSkills().contains("QA") || assignedProject.getSkills().contains("PM")) {
                qaOrPmProjectsCount++;
            }
        }

        if (project.getSkills().contains("QA") || project.getSkills().contains("PM")) {
            return qaOrPmProjectsCount < 2;
        } else {
            return assignments.isEmpty() || !isEmployeeAssignedToProject(this, project);
        }
    }

     // Sprawdza, czy pracownik jest już przypisany do określonego projektu

     // zwraca True, jeśli pracownik jest przypisany do projektu; false w przeciwnym razie

    private boolean isEmployeeAssignedToProject(Employee employee, Project project) {
        for (EmployeeAssignment assignment : employee.getAssignments()) {
            if (assignment.getProject().equals(project)) {
                return true;
            }
        }
        return false;
    }
}
