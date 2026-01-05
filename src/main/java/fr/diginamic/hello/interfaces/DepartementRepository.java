package fr.diginamic.hello.interfaces;

import fr.diginamic.hello.controleurs.Departement;
import fr.diginamic.hello.controleurs.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
    public interface DepartementRepository extends JpaRepository<Departement, Integer> {
        Optional<Departement> findByCode(String code);
    }

