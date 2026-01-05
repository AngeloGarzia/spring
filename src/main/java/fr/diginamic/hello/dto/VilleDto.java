package fr.diginamic.hello.dto;

public class VilleDto {
    private Integer id;
    private String nom;
    private int population;
    private Integer idDepartement;
    private String codeDepartement;

    public VilleDto() {}

    // getters/setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public int getPopulation() { return population; }
    public void setPopulation(int population) { this.population = population; }
    public Integer getIdDepartement() { return idDepartement; }
    public void setIdDepartement(Integer idDepartement) { this.idDepartement = idDepartement; }
    public String getCodeDepartement() { return codeDepartement; }
    public void setCodeDepartement(String codeDepartement) { this.codeDepartement = codeDepartement; }
}
