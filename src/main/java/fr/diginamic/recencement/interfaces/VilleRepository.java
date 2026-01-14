package fr.diginamic.recencement.interfaces;

import fr.diginamic.recencement.Entites.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface VilleRepository extends JpaRepository<Ville, Integer> {

    // 1) Nom commençant par ...
    List<Ville> findByNomStartingWith(String nom);

    // 2) Population > min, tri desc
    List<Ville> findByPopulationGreaterThanOrderByPopulationDesc(int min);

    // 3) min < population < max, tri desc
    List<Ville> findByPopulationBetweenOrderByPopulationDesc(int min, int max);

    // 4) Villes d’un département avec population > min, tri desc
    List<Ville> findByPopulationGreaterThanAndDepartementIdOrderByPopulationDesc(int min, Integer idDept);

    // 5) Villes d’un département avec population entre min et max, tri desc par l'id du dept
    List<Ville> findByPopulationBetweenAndDepartementIdOrderByPopulationDesc(int min, int max, Integer idDept);

    // 6) Top N villes d’un département (N limité par Pageable), tri desc
    @Query("SELECT v FROM Ville v WHERE v.departement.id = :idDept ORDER BY v.population DESC")
    List<Ville> findTopNByDepartement(@Param("idDept") Integer idDept, Pageable pageable);

    // 7) Villes d’un département avec population entre min et max, tri desc par le code du dept
    List<Ville> findByPopulationBetweenAndDepartementCodeOrderByPopulationDesc(int min, int max, String code);
    // Recherche villes par préfixe nom INSENSIBLE À LA CASSE (TP10)
    List<Ville> findByNomStartingWithIgnoreCase(String prefixe);

    //  AJOUTE pour TP12 PDF
    List<Ville> findByDepartementCodeOrderByPopulationDesc(String code);


}
