package fr.diginamic.recencement.Securite;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {

    private final String nom;

    public Role(String nom) {
        this.nom = nom;
    }

    @Override
    public String getAuthority() {
        return nom;
    }
}