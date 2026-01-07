package fr.diginamic.recencement.Entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Entité Ville avec relation ManyToOne vers Departement.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "ville")
public class Ville {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Le nom de la ville est obligatoire")
    @Size(min = 2, message = "Le nom de la ville doit contenir au moins 2 caractères")
    @Column(name = "nom_ville", nullable = false, length = 100)
    private String nom;

    @Min(value = 2, message = "Le nombre d'habitants doit être supérieur ou égal à 1")
    @Column(name = "population_ville", nullable = false)
    private int population;

    @ManyToOne
    @JoinColumn(name = "id_dept", nullable = false)
    private Departement departement;

    public Ville() {
    }

    // constructeur pratique sans departement si tu veux
    public Ville(Integer id, String nom, int population) {
        this.id = id;
        this.nom = nom;
        this.population = population;
    }

    @Override
    public String toString() {
        return "Ville{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", population=" + population +
                ", departement=" + departement +
                '}';
    }

    // getters / setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }
}