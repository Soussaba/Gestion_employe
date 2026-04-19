package dao;

import database.DatabaseConnection;
import model.Employe;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeDAO {

    public boolean ajouter(Employe e) throws SQLException {
        String sql = "INSERT INTO employe (nom, prenom, poste, salaire, id_departement) VALUES (?,?,?,?,?)";
        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
        ps.setString(1, e.getNom());
        ps.setString(2, e.getPrenom());
        ps.setString(3, e.getPoste());
        ps.setDouble(4, e.getSalaire());
        ps.setInt(5, e.getIdDepartement());
        return ps.executeUpdate() > 0;
    }

    public boolean modifier(Employe e) throws SQLException {
        String sql = "UPDATE employe SET nom=?, prenom=?, poste=?, salaire=?, id_departement=? WHERE id=?";
        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
        ps.setString(1, e.getNom());
        ps.setString(2, e.getPrenom());
        ps.setString(3, e.getPoste());
        ps.setDouble(4, e.getSalaire());
        ps.setInt(5, e.getIdDepartement());
        ps.setInt(6, e.getId());
        return ps.executeUpdate() > 0;
    }

    public boolean supprimer(int id) throws SQLException {
        String sql = "DELETE FROM employe WHERE id=?";
        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeUpdate() > 0;
    }

    public List<Employe> listerTous() throws SQLException {
        List<Employe> liste = new ArrayList<>();
        String sql = "SELECT e.*, d.nom_departement FROM employe e " +
                "JOIN departement d ON e.id_departement = d.id";
        Statement st = DatabaseConnection.getConnection().createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            Employe e = new Employe(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("poste"),
                    rs.getDouble("salaire"),
                    rs.getInt("id_departement")
            );
            e.setNomDepartement(rs.getString("nom_departement"));
            liste.add(e);
        }
        return liste;
    }

    public List<Employe> rechercher(String motCle) throws SQLException {
        List<Employe> liste = new ArrayList<>();
        String sql = "SELECT e.*, d.nom_departement FROM employe e " +
                "JOIN departement d ON e.id_departement = d.id " +
                "WHERE e.nom LIKE ? OR e.prenom LIKE ? OR e.poste LIKE ?";
        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
        String p = "%" + motCle + "%";
        ps.setString(1, p);
        ps.setString(2, p);
        ps.setString(3, p);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Employe e = new Employe(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("poste"),
                    rs.getDouble("salaire"),
                    rs.getInt("id_departement")
            );
            e.setNomDepartement(rs.getString("nom_departement"));
            liste.add(e);
        }
        return liste;
    }

    public double masseSalariale() throws SQLException {
        String sql = "SELECT SUM(salaire) FROM employe";
        Statement st = DatabaseConnection.getConnection().createStatement();
        ResultSet rs = st.executeQuery(sql);
        if (rs.next()) return rs.getDouble(1);
        return 0;
    }

    public List<Employe> listerParDepartement(int idDepartement) throws SQLException {
        List<Employe> liste = new ArrayList<>();
        String sql = "SELECT e.*, d.nom_departement FROM employe e " +
                "JOIN departement d ON e.id_departement = d.id " +
                "WHERE e.id_departement = ?";
        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
        ps.setInt(1, idDepartement);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Employe e = new Employe(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("poste"),
                    rs.getDouble("salaire"),
                    rs.getInt("id_departement")
            );
            e.setNomDepartement(rs.getString("nom_departement"));
            liste.add(e);
        }
        return liste;
    }
}