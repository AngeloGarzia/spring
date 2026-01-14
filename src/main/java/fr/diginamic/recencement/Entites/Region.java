package fr.diginamic.recencement.Entites;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "region")
public class Region {
    @Id
    private String code;  // code="11", "76"...

    private String nom;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Departement> departements = new HashSet<>();

    // Constructeurs
    public Region() {}

    public Region(String code, String nom) {
        this.code = code;
        this.nom = nom;
    }

    // Getters/Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Set<Departement> getDepartements() { return departements; }
    public void setDepartements(Set<Departement> departements) { this.departements = departements; }
}
