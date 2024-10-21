package lab2;

import java.util.ArrayList;
import java.util.List;


public class Project {
    private String name;
    // lista umiejętności wymaganych do projektu
    private List<String> skills;


    public Project(String name) {
        this.name = name;
        this.skills = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public List<String> getSkills() {
        return skills;
    }


     // Metoda dodająca umiejętność do listy umiejętności projektu

    public void addSkill(String skill) {
        skills.add(skill);
    }


     // Metoda ustawiająca listę umiejętności projektu na zadaną listę.

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
    //Metoda wykonująca głęboką kopię obiektu projektu tworzy nowy obiekt o takiej samej nazwie
     //i kopii listy umiejętności

    public Project deepCopy() {
        Project copy = new Project(this.name);
        copy.setSkills(new ArrayList<>(this.skills));
        return copy;
    }
}
