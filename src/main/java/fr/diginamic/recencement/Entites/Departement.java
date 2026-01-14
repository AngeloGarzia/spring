package fr.diginamic.recencement.Entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Entité JPA représentant un département français.
 * <p>
 * Mappée sur la table MySQL "departement" avec :
 * - id_dept (PK auto-incrémenté)
 * - code_dept ("34" pour Hérault)
 * - nom ("Hérault")
 * - Relation 1:N vers villes.
 * <p>
 * Utilisée par DAOs pour INSERT/SELECT/UPDATE en base.
 */
@Entity
@Table(name = "departement")
public class Departement {

    /**
     * ID auto
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dept")
    private Integer id;

    /**
     * Code INSEE
     */
    @NotNull(message = "Le code département est obligatoire")
    @Size(min = 2, max = 3, message = "Le code département doit contenir entre 2 et 3 caractères")
    @Column(name = "code_dept", nullable = false, length = 3)
    private String code;   // code postal du departement

    /**
     * Nom dept
     */
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    /**
     * Liste villes
     */
    @OneToMany(mappedBy = "departement")
    // Evite la boucle infinie ville/departement
    @JsonIgnore
    private List<Ville> villes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_code")  // FK vers Region.code
    private Region region;


    @Override
    public String toString() {
        return "Departement{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", nom='" + nom + '\'' +
                ", villes=" + villes +
                '}';
    }
    //guetter setter

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Ville> getVilles() {
        return villes;
    }

    public void setVilles(List<Ville> villes) {
        this.villes = villes;
    }
}


