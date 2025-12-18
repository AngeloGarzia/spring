package fr.diginamic.hello.controleurs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ville {
    private String nom;
    private int population;
    private int id;

    public Ville() {
    }

    public Ville(String nom, int population,int id) {
        this.nom = nom;
        this.population = population;
        this.id = id;

            }


    @Override
    public String toString() {
        return "Ville{" +
                "nom='" + nom + '\'' +
                ", population=" + population +
                ", id=" + id +
                '}';
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}
