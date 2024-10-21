package lab2;


 // reprezentuje przypisanie pracownika do projektu w systemie RAP

public class EmployeeAssignment {
    private Employee employee;  // Pracownik do projektu
    private Project project;    // Projekt, do którego jest przypisany
    public EmployeeAssignment(Employee employee, Project project) {
        this.employee = employee;
        this.project = project;
    }
    public Employee getEmployee() {
        return employee;
    }
    public Project getProject() {
        return project;
    }


     // metoda toString, zwracająca opis przypisania pracownika do projektu.

    public String toString() {
        return employee.getName() + " assigned to project " + project.getName();
    }
}
