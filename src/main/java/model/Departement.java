package model;

public class Departement {
    private int id;
    private String nomDepartement;

    public Departement() {}

    public Departement(int id, String nomDepartement) {
        this.id = id;
        this.nomDepartement = nomDepartement;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomDepartement() { return nomDepartement; }
    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
    }

    @Override
    public String toString() {
        return nomDepartement;
    }
}