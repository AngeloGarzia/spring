package fr.diginamic.recencement.Dto;

public class RegionApiDto {
    private String code;
    private String nom;

    // Constructeurs vides + getters/setters (préférez explicites)
    public RegionApiDto() {}

    public RegionApiDto(String code, String nom) {
        this.code = code;
        this.nom = nom;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}
