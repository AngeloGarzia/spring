package fr.diginamic.recencement.services;

import fr.diginamic.recencement.Dto.VilleDto;
import fr.diginamic.recencement.Entites.Departement;
import fr.diginamic.recencement.exceptions.VilleApiException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IVilleService {

    /**
     * Liste toutes les villes (non paginée)
     */
    List<VilleDto> extractVilles();

    /**
     * Liste paginée des villes
     */
    Page<VilleDto> extractVillesPage(int page, int size);

    /**
     * Ville par ID
     */
    VilleDto extractVille(int idVille);

    /**
     * Ville par nom exact (premier résultat)
     */
    VilleDto extractVille(String nom);

    /**
     * Villes par préfixe nom (insensible casse)
     */
    List<VilleDto> extractVillesParNomPrefixe(String prefixe);

    /**
     * Villes par population [min-max] globale
     */
    List<VilleDto> extractVillesParPopulation(int min, int max);

    /**
     * Villes par population [min-max] + département
     */
    List<VilleDto> extractVillesParPopulationEtDepartement(int idDept, int min, int max);

    /**
     * Top N villes d'un département
     */
    List<VilleDto> extractTopNVillesParDepartement(int idDept, int n);

    /**
     * Création ville (avec validation + anti-duplicata)
     */
    VilleDto insertVille(VilleDto villeDto);

    /**
     * Modification ville
     */
    VilleDto modifierVille(int idVille, VilleDto villeModifiee);

    /**
     * Suppression ville + liste restante
     */
    List<VilleDto> supprimerVille(int idVille);


}
