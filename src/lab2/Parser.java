package lab2;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Parser {
    // metoda parsująca dane wejściowe "PROJECTS" na obiekty Projektów
    public static List<Project> parseProjects(BufferedReader reader) throws IOException {
        List<Project> projects = new ArrayList<>();
        boolean inProjectsSection = false;

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("PROJECTS")) {
                inProjectsSection = true;
                continue;
            } else if (line.startsWith("STAFF")) {
                break;
            }

            if (inProjectsSection) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String projectName = parts[0].trim();
                    String[] skills = parts[1].split(" ");
                    Project project = new Project(projectName);
                    for (String skill : skills) {
                        if (!skill.trim().isEmpty()) {
                            project.addSkill(skill.trim());
                        }
                    }
                    projects.add(project);
                }
            }
        }

        return projects;
    }

    //metoda parsująca dane wejściowe "STAFF" na obiekty Pracowników
    public static List<Employee> parseEmployees(BufferedReader reader) throws IOException {
        List<Employee> employees = new ArrayList<>();
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("STAFF")) {
                continue;
            }

            String[] parts = line.split(":");
            if (parts.length == 2) {
                String employeeName = parts[0].trim();
                String[] skills = parts[1].trim().split(" ");
                Employee employee = new Employee(employeeName);
                // dodawanie umiejętności pojedynczemu pracownikowi
                for (String skill : skills) {
                    employee.addSkill(skill);
                }
                // dodawanie pracownika do listy pracowników
                employees.add(employee);
            }
        }

        return employees;
    }
}
