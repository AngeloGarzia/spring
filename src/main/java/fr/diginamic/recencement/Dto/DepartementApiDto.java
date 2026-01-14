package fr.diginamic.recencement.Dto;

public class DepartementApiDto {
    private String code;
    private String nom;
    private String codeRegion;

    // Getters/Setters OBLIGATOIRES
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCodeRegion() { return codeRegion; }
    public void setCodeRegion(String codeRegion) { this.codeRegion = codeRegion; }
}