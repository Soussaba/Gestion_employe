package model;

public class Employe {
    private int id;
    private String nom;
    private String prenom;
    private String poste;
    private double salaire;
    private int idDepartement;
    private String nomDepartement;

    public Employe() {}

    public Employe(int id, String nom, String prenom,
                   String poste, double salaire, int idDepartement) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.poste = poste;
        this.salaire = salaire;
        this.idDepartement = idDepartement;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getPoste() { return poste; }
    public void setPoste(String poste) { this.poste = poste; }

    public double getSalaire() { return salaire; }
    public void setSalaire(double salaire) { this.salaire = salaire; }

    public int getIdDepartement() { return idDepartement; }
    public void setIdDepartement(int idDepartement) {
        this.idDepartement = idDepartement;
    }

    public String getNomDepartement() { return nomDepartement; }
    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
    }
}