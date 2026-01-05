package fr.diginamic.recencement.interfaces;

import fr.diginamic.recencement.controleurs.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
    public interface DepartementRepository extends JpaRepository<Departement, Integer> {
        Optional<Departement> findByCode(String code);
    }

