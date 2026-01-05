package fr.diginamic.recencement.interfaces;

import fr.diginamic.recencement.controleurs.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface VilleRepository extends JpaRepository<Ville, Integer> {
    List<Ville> findByNomStartingWith(String nom);
    List<Ville> findByPopulationBetween(int min, int max);
    //  Top N villes (avec Pageable)
    @Query("SELECT v FROM Ville v WHERE v.departement.id = :idDept ORDER BY v.population DESC")
    List<Ville> findTopNByDepartement(@Param("idDept") Integer idDept, Pageable pageable);

    // : Population + dept (nommage Spring)
    List<Ville> findByPopulationBetweenAndDepartementId(int min, int max, Integer idDept);
}