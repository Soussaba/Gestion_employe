package dao;

import database.DatabaseConnection;
import model.Departement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartementDAO {

    public boolean ajouter(Departement d) throws SQLException {
        String sql = "INSERT INTO departement (nom_departement) VALUES (?)";
        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
        ps.setString(1, d.getNomDepartement());
        return ps.executeUpdate() > 0;
    }

    public boolean modifier(Departement d) throws SQLException {
        String sql = "UPDATE departement SET nom_departement=? WHERE id=?";
        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
        ps.setString(1, d.getNomDepartement());
        ps.setInt(2, d.getId());
        return ps.executeUpdate() > 0;
    }

    public boolean supprimer(int id) throws SQLException {
        // Vérifier si le département a des employés
        String check = "SELECT COUNT(*) FROM employe WHERE id_departement=?";
        PreparedStatement ps1 = DatabaseConnection.getConnection().prepareStatement(check);
        ps1.setInt(1, id);
        ResultSet rs = ps1.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            throw new SQLException("Ce département contient des employés !");
        }
        String sql = "DELETE FROM departement WHERE id=?";
        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeUpdate() > 0;
    }

    public List<Departement> listerTous() throws SQLException {
        List<Departement> liste = new ArrayList<>();
        String sql = "SELECT * FROM departement";
        Statement st = DatabaseConnection.getConnection().createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            liste.add(new Departement(
                    rs.getInt("id"),
                    rs.getString("nom_departement")
            ));
        }
        return liste;
    }
}