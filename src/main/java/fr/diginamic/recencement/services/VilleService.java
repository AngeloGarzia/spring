package fr.diginamic.recencement.services;

import fr.diginamic.recencement.Entites.Departement;
import fr.diginamic.recencement.Entites.Ville;
import fr.diginamic.recencement.Dto.VilleDto;
import fr.diginamic.recencement.exceptions.VilleApiException;
import fr.diginamic.recencement.interfaces.DepartementRepository;
import fr.diginamic.recencement.interfaces.VilleRepository;
import fr.diginamic.recencement.mappers.VilleMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service métier gérant les opérations sur les villes.
 * <p>
 * Cette implémentation utilise une liste en mémoire pour stocker les villes.
 * Plus tard, elle pourra être remplacée par une implémentation basée sur JPA / base de données.
 */
@Service
@Slf4j
@Transactional
public class VilleService implements IVilleService {

    private final VilleRepository villeRepository;
    private final DepartementRepository departementRepository;
    private final VilleMapper mapper;

    public VilleService(VilleRepository villeRepository,
                        DepartementRepository departementRepository,
                        VilleMapper mapper) {
        this.villeRepository = villeRepository;
        this.departementRepository = departementRepository;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public List<VilleDto> extractVilles() {
        return mapper.toDtos(villeRepository.findAll());
    }

    @Transactional
    @Override
    public VilleDto extractVille(int idVille) {
        Ville ville = villeRepository.findById(idVille)
                .orElseThrow(() -> new VilleApiException("La ville n'a pas été trouvée"));
        return mapper.toDto(ville);
    }


    @Transactional
    @Override
    public List<VilleDto> extractVillesParNomPrefixe(String prefixe) {
        return mapper.toDtos(villeRepository.findByNomStartingWithIgnoreCase(prefixe));
    }

    @Transactional
    @Override
    public VilleDto extractVille(String nom) {
        List<Ville> resultats = villeRepository.findByNomStartingWith(nom);
        if (resultats.isEmpty()) {
            throw new VilleApiException("Aucune ville avec ce nom");
        }
        return mapper.toDto(resultats.get(0));
    }

    @Transactional
    @Override
    public VilleDto insertVille(VilleDto villeDto) {

        List<String> erreurs = new ArrayList<>();

        // population > 10
        if (villeDto.getPopulation() <= 10) {
            erreurs.add("La population doit être supérieure à 10");
        }

        // nom >= 2 caractères
        if (villeDto.getNom() == null || villeDto.getNom().length() < 2) {
            erreurs.add("Le nom de la ville doit contenir au moins 2 caractères");
        }
        if (villeDto.getNom().length()>20) {
            erreurs.add("Le nom de la ville contenir au maximum 20 caractères");
        }

        //  On n'insert pas deux fois la meme ville dans le meme departement
        List<Ville> existantesMemeNom = villeRepository.findByNomStartingWith(villeDto.getNom()); //Recheche le nom de la  ville venant du dto dans la base
        Departement deptCible = findOrCreateDepartement(villeDto); //trouve ou creer un departement venant du dto

        //recherche dans la liste de ville du meme departement(dto) si le nom de ville existe
        for (Ville v : existantesMemeNom) {
            if (v.getNom().equalsIgnoreCase(villeDto.getNom())
                    && v.getDepartement() != null
                    && v.getDepartement().getId().equals(deptCible.getId())) {
                erreurs.add("Une ville avec ce nom existe déjà dans ce département");
                break; //si au moins une ville existe au sort!
            }
        }

        if (!erreurs.isEmpty()) {
            throw new VilleApiException(String.join(" | ", erreurs));
        }

        //  Trouve/crée département
        Ville ville = mapper.toEntity(villeDto);
        ville.setDepartement(deptCible);

        //log
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username =authentication.getName();
        ville.setUserMaj(username);
        ville.setDateMaj(LocalDateTime.now());

        Ville savedVille = villeRepository.save(ville);
        log.info("Ville insérée en base - ID: {}, Nom: {}", savedVille.getId(), savedVille.getNom());
        return mapper.toDto(savedVille);
    }

    @Transactional
    @Override
    public VilleDto modifierVille(int idVille, VilleDto villeModifiee) {

        List<String> erreurs = new ArrayList<>();

        if (villeModifiee.getPopulation() <= 10) {
            erreurs.add("La population doit être supérieure à 10");
        }
        if (villeModifiee.getNom() == null || villeModifiee.getNom().length() < 2) {
            erreurs.add("Le nom de la ville doit contenir au moins 2 caractères");
        }

        if (!erreurs.isEmpty()) {
            throw new VilleApiException(String.join(" | ", erreurs));
        }

        Ville existante = villeRepository.findById(idVille)
                .orElseThrow(() -> new VilleApiException("La ville n'a pas été trouvée"));

        existante.setNom(villeModifiee.getNom());
        existante.setPopulation(villeModifiee.getPopulation());
        //log
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username =authentication.getName();
        existante.setUserMaj(username);
        existante.setDateMaj(LocalDateTime.now());
        log.info("Ville modifiée - ID: {}, Nouveau nom: {}, Population: {}",
                existante.getId(), existante.getNom(), existante.getPopulation());

        //  Gère département si changé
        if (villeModifiee.getCodeDepartement() != null || villeModifiee.getIdDepartement() != null) {
            existante.setDepartement(findOrCreateDepartement(villeModifiee));
        }

        Ville savedVille = villeRepository.save(existante);


        return mapper.toDto(savedVille);
    }

    @Transactional
    @Override
    public List<VilleDto> extractVillesParPopulation(int min, int max) {
        if (min > max) {
            throw new VilleApiException("La borne minimale doit être inférieure ou égale à la borne maximale");
        }
        return mapper.toDtos(villeRepository.findByPopulationBetweenOrderByPopulationDesc(min, max));
    }

    @Transactional
    @Override
    public List<VilleDto> supprimerVille(int idVille) {
        Ville existante = villeRepository.findById(idVille)
                .orElseThrow(() -> new VilleApiException("La ville n'a pas été trouvée"));

        villeRepository.deleteById(idVille);
        return mapper.toDtos(villeRepository.findAll());
    }
    @Transactional
    @Override
    public List<VilleDto> extractVillesParPopulationEtDepartement(int idDept, int min, int max) {
        if (min > max) {
            throw new VilleApiException("La borne minimale doit être inférieure ou égale à la borne maximale");
        }
        return mapper.toDtos(
                villeRepository.findByPopulationBetweenAndDepartementIdOrderByPopulationDesc(min, max, idDept)
        );
    }
    @Transactional
    @Override
    public List<VilleDto> extractTopNVillesParDepartement(int idDept, int n) {
        if (n <= 0) {
            throw new VilleApiException("Le nombre de villes doit être strictement positif");
        }
        return mapper.toDtos(
                villeRepository.findTopNByDepartement(idDept, PageRequest.of(0, n))
        );
    }
    @Transactional
    @Override
    public Page<VilleDto> extractVillesPage(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return villeRepository.findAll(pageable)
                .map(mapper::toDto);
    }
    /**
     * Trouve ou crée département (privé)
     */
    private Departement findOrCreateDepartement(VilleDto villeDto) {
        if (villeDto.getCodeDepartement() != null) {
            return departementRepository.findByCode(villeDto.getCodeDepartement())
                    .orElseGet(() -> {
                        Departement newDept = new Departement();
                        newDept.setCode(villeDto.getCodeDepartement());
                        newDept.setNom("Département " + villeDto.getCodeDepartement());
                        return departementRepository.save(newDept);
                    });
        }
        if (villeDto.getIdDepartement() != null) {
            return departementRepository.findById(villeDto.getIdDepartement())
                    .orElseThrow(() -> new VilleApiException("Département ID " + villeDto.getIdDepartement() + " inconnu"));
        }
        throw new VilleApiException("codeDepartement OU idDepartement requis");
    }

    public List<VilleDto> extractVillesParPopulationSup(int min) {
        return mapper.toDtos(villeRepository.findByPopulationGreaterThanOrderByPopulationDesc(min));
    }
}