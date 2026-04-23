package controller;

import dao.DepartementDAO;
import dao.EmployeDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Departement;
import model.Employe;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EmployeController implements Initializable {

    private DepartementDAO deptDAO = new DepartementDAO();
    private EmployeDAO empDAO = new EmployeDAO();

    // ── Départements ──
    @FXML private TextField tfNomDept;
    @FXML private TableView<Departement> tableDepts;
    @FXML private TableColumn<Departement, Integer> colDeptId;
    @FXML private TableColumn<Departement, String> colDeptNom;

    private ObservableList<Departement> listeDepts = FXCollections.observableArrayList();
    private Departement deptSelectionne = null;

    // ── Employés ──
    @FXML private TextField tfNom, tfPrenom, tfPoste, tfSalaire, tfRecherche;
    @FXML private ComboBox<Departement> cbDepartement;
    @FXML private ComboBox<Departement> cbFiltre;
    @FXML private TableView<Employe> tableEmployes;
    @FXML private TableColumn<Employe, Integer> colEmpId;
    @FXML private TableColumn<Employe, String> colEmpNom, colEmpPrenom, colEmpPoste, colEmpDept;
    @FXML private TableColumn<Employe, Double> colEmpSalaire;
    @FXML private Label lblMasse;

    private ObservableList<Employe> listeEmps = FXCollections.observableArrayList();
    private Employe empSelectionne = null;
    private int empIdEnCours = -1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Colonnes départements
        colDeptId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDeptNom.setCellValueFactory(new PropertyValueFactory<>("nomDepartement"));
        tableDepts.setItems(listeDepts);

        // Colonnes employés
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmpNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colEmpPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmpPoste.setCellValueFactory(new PropertyValueFactory<>("poste"));
        colEmpSalaire.setCellValueFactory(new PropertyValueFactory<>("salaire"));
        colEmpDept.setCellValueFactory(new PropertyValueFactory<>("nomDepartement"));
        tableEmployes.setItems(listeEmps);

        chargerDepartements();
        chargerEmployes();
    }

    // ── Charger données ──
    private void chargerDepartements() {
        try {
            listeDepts.setAll(deptDAO.listerTous());
            cbDepartement.setItems(listeDepts);
            cbFiltre.setItems(listeDepts);
        } catch (SQLException e) {
            afficherErreur("Erreur", e.getMessage());
        }
    }

    private void chargerEmployes() {
        try {
            listeEmps.setAll(empDAO.listerTous());
            lblMasse.setText("Masse salariale totale : "
                    + empDAO.masseSalariale() + " FCFA");
        } catch (SQLException e) {
            afficherErreur("Erreur", e.getMessage());
        }
    }

    // ── Actions Départements ──
    @FXML
    private void ajouterDepartement() {
        String nom = tfNomDept.getText().trim();
        if (nom.isEmpty()) {
            afficherWarning("Champ vide", "Entrez un nom.");
            return;
        }
        try {
            deptDAO.ajouter(new Departement(0, nom));
            tfNomDept.clear();
            chargerDepartements();
        } catch (SQLException e) {
            afficherErreur("Erreur", e.getMessage());
        }
    }

    @FXML
    private void modifierDepartement() {
        if (deptSelectionne == null) {
            afficherWarning("Attention", "Sélectionnez un département.");
            return;
        }
        deptSelectionne.setNomDepartement(tfNomDept.getText().trim());
        try {
            deptDAO.modifier(deptSelectionne);
            tfNomDept.clear();
            deptSelectionne = null;
            chargerDepartements();
        } catch (SQLException e) {
            afficherErreur("Erreur", e.getMessage());
        }
    }

    @FXML
    private void supprimerDepartement() {
        if (deptSelectionne == null) {
            afficherWarning("Attention", "Sélectionnez un département.");
            return;
        }
        try {
            deptDAO.supprimer(deptSelectionne.getId());
            deptSelectionne = null;
            chargerDepartements();
        } catch (SQLException e) {
            afficherErreur("Erreur", e.getMessage());
        }
    }

    @FXML
    private void selectionnerDepartement() {
        deptSelectionne = tableDepts.getSelectionModel().getSelectedItem();
        if (deptSelectionne != null)
            tfNomDept.setText(deptSelectionne.getNomDepartement());
    }

    // ── Actions Employés ──
    @FXML
    private void ajouterEmploye() {
        Employe e = lireFormulaire();
        if (e == null) return;
        try {
            empDAO.ajouter(e);
            reinitialiserFormulaire();
            chargerEmployes();
        } catch (SQLException ex) {
            afficherErreur("Erreur", ex.getMessage());
        }
    }

    @FXML
    private void modifierEmploye() {
        if (empIdEnCours < 0) {
            afficherWarning("Attention", "Sélectionnez un employé.");
            return;
        }
        Employe e = lireFormulaire();
        if (e == null) return;
        e.setId(empIdEnCours);
        try {
            empDAO.modifier(e);
            reinitialiserFormulaire();
            chargerEmployes();
        } catch (SQLException ex) {
            afficherErreur("Erreur", ex.getMessage());
        }
    }

    @FXML
    private void supprimerEmploye() {
        if (empSelectionne == null) {
            afficherWarning("Attention", "Sélectionnez un employé.");
            return;
        }
        try {
            empDAO.supprimer(empSelectionne.getId());
            reinitialiserFormulaire();
            chargerEmployes();
        } catch (SQLException ex) {
            afficherErreur("Erreur", ex.getMessage());
        }
    }

    @FXML
    private void rechercherEmploye() {
        String mot = tfRecherche.getText().trim();
        if (mot.isEmpty()) { chargerEmployes(); return; }
        try {
            listeEmps.setAll(empDAO.rechercher(mot));
        } catch (SQLException e) {
            afficherErreur("Erreur", e.getMessage());
        }
    }

    @FXML
    private void filtrerParDepartement() {
        Departement dept = cbFiltre.getValue();
        if (dept == null) { chargerEmployes(); return; }
        try {
            listeEmps.setAll(empDAO.listerParDepartement(dept.getId()));
        } catch (SQLException e) {
            afficherErreur("Erreur", e.getMessage());
        }
    }

    @FXML
    private void afficherTous() {
        cbFiltre.setValue(null);
        chargerEmployes();
    }

    @FXML
    private void selectionnerEmploye() {
        empSelectionne = tableEmployes.getSelectionModel().getSelectedItem();
        if (empSelectionne != null) {
            empIdEnCours = empSelectionne.getId();
            tfNom.setText(empSelectionne.getNom());
            tfPrenom.setText(empSelectionne.getPrenom());
            tfPoste.setText(empSelectionne.getPoste());
            tfSalaire.setText(String.valueOf(empSelectionne.getSalaire()));
            cbDepartement.getItems().stream()
                    .filter(d -> d.getId() == empSelectionne.getIdDepartement())
                    .findFirst()
                    .ifPresent(cbDepartement::setValue);
        }
    }

    @FXML
    private void reinitialiserFormulaire() {
        tfNom.clear(); tfPrenom.clear();
        tfPoste.clear(); tfSalaire.clear();
        cbDepartement.setValue(null);
        empIdEnCours = -1;
        empSelectionne = null;
    }

    // ── Utilitaires ──
    private Employe lireFormulaire() {
        String nom = tfNom.getText().trim();
        String prenom = tfPrenom.getText().trim();
        String poste = tfPoste.getText().trim();
        String salStr = tfSalaire.getText().trim();
        Departement dept = cbDepartement.getValue();

        if (nom.isEmpty() || prenom.isEmpty() ||
                poste.isEmpty() || salStr.isEmpty()) {
            afficherWarning("Champs vides", "Remplissez tous les champs.");
            return null;
        }
        if (dept == null) {
            afficherWarning("Département", "Choisissez un département.");
            return null;
        }
        double salaire;
        try {
            salaire = Double.parseDouble(salStr);
        } catch (NumberFormatException e) {
            afficherWarning("Salaire invalide", "Entrez un nombre.");
            return null;
        }
        if (salaire <= 0) {
            afficherWarning("Salaire invalide", "Le salaire doit être positif.");
            return null;
        }
        return new Employe(0, nom, prenom, poste, salaire, dept.getId());
    }

    private void afficherErreur(String titre, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(titre);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void afficherWarning(String titre, String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(titre);
        a.setContentText(msg);
        a.showAndWait();
    }
}