package fr.diginamic.recencement.services;


import fr.diginamic.recencement.Entites.Departement;
import fr.diginamic.recencement.Entites.Ville;
import fr.diginamic.recencement.Dto.DepartementDto;
import fr.diginamic.recencement.Dto.VilleDto;
import fr.diginamic.recencement.interfaces.DepartementRepository;
import fr.diginamic.recencement.interfaces.VilleRepository;
import fr.diginamic.recencement.mappers.DepartementMapper;
import fr.diginamic.recencement.mappers.VilleMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier gérant les opérations sur les départements et leurs villes associées. [file:16]
 * <p>
 * Ce service s'appuie sur les repositories JPA {@link DepartementRepository} et {@link VilleRepository}
 * ainsi que sur les mappers {@link DepartementMapper} et {@link VilleMapper} pour exposer des DTO. [file:6][file:9][file:12][file:15]
 */
@Service
@Transactional
public class DepartementService implements IDepartementService {

    private final DepartementRepository departementRepository;
    private final VilleRepository villeRepository;
    private final DepartementMapper mapper;
    private final VilleMapper villeMapper;

    /**
     * Constructeur injectant les dépendances nécessaires au service. [file:16]
     *
     * @param departementRepository repository JPA pour les entités {@link Departement}
     * @param villeRepository       repository JPA pour les entités {@link Ville}
     * @param mapper                mapper pour convertir {@link Departement} ↔ {@link DepartementDto}
     * @param villeMapper           mapper pour convertir {@link Ville} ↔ {@link VilleDto}
     */
    public DepartementService(DepartementRepository departementRepository,
                              VilleRepository villeRepository,
                              DepartementMapper mapper,
                              VilleMapper villeMapper) {
        this.departementRepository = departementRepository;
        this.villeRepository = villeRepository;
        this.mapper = mapper;
        this.villeMapper = villeMapper;
    }

    /**
     * Crée un nouveau département à partir d'un DTO et retourne le département créé. [file:16]
     *
     * @param dto données du département à créer
     * @return département créé sous forme de {@link DepartementDto}
     */
    @Transactional
    @Override
    public DepartementDto creer(DepartementDto dto) {
        Departement dept = mapper.toEntity(dto);
        Departement saved = departementRepository.save(dept);
        return mapper.toDto(saved);
    }

    /**
     * Retourne la liste de tous les départements présents en base. [file:16]
     *
     * @return liste de {@link DepartementDto}
     */
    @Transactional
    @Override
    public List<DepartementDto> lister() {
        return mapper.toDtos(departementRepository.findAll());
    }

    /**
     * Recherche un département par son identifiant technique. [file:16]
     *
     * @param id identifiant du département
     * @return département correspondant sous forme de {@link DepartementDto}
     * @throws RuntimeException si aucun département n'est trouvé
     */
    @Transactional
    @Override
    public DepartementDto getById(Integer id) {
        Departement dept = departementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Département non trouvé"));
        return mapper.toDto(dept);
    }

    /**
     * Recherche un département par son code (ex : "34" pour l'Hérault). [file:8][file:16]
     *
     * @param code code du département
     * @return département correspondant sous forme de {@link DepartementDto}
     * @throws RuntimeException si aucun département n'est trouvé
     */
    @Transactional
    @Override
    public DepartementDto getByCode(String code) {
        Departement dept = departementRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Département non trouvé"));
        return mapper.toDto(dept);
    }
    @Transactional
    public List<VilleDto> getVillesByDepartement(String code) {
        // Version 1 : Via VilleRepository direct
        return villeMapper.toDtos(villeRepository.findByDepartementCodeOrderByPopulationDesc(code));

        // Version 2 : Via tes méthodes existantes (si tu préfères)
        // Integer id = departementRepository.findByCode(code).get().getId();
        // return villeService.extractTopNVillesParDepartement(id, 100);
    }
    /**
     * Supprime un département par son identifiant puis retourne la liste mise à jour des départements. [file:16]
     *
     * @param id identifiant du département à supprimer
     * @return liste des départements restante sous forme de {@link DepartementDto}
     * @throws RuntimeException si le département n'existe pas
     */
    @Transactional
    @Override
    public List<DepartementDto> supprimer(Integer id) {
        if (!departementRepository.existsById(id)) {
            throw new RuntimeException("Département non trouvé");
        }
        departementRepository.deleteById(id);
        return lister();
    }

    /**
     * Modifie un département existant à partir de son identifiant et des données fournies. [file:16]
     *
     * @param id  identifiant du département à modifier
     * @param dto nouvelles valeurs (code, nom)
     * @return département modifié sous forme de {@link DepartementDto}
     * @throws RuntimeException si le département n'existe pas
     */
    @Transactional
    @Override
    public DepartementDto modifier(Integer id, DepartementDto dto) {
        Departement existant = departementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Département non trouvé"));

        existant.setCode(dto.getCode());
        existant.setNom(dto.getNom());

        Departement saved = departementRepository.save(existant);
        return mapper.toDto(saved);
    }

    /**
     * Retourne les N villes les plus peuplées d'un département donné. [file:9][file:16]
     *
     * @param idDept identifiant du département
     * @param n      nombre de villes à retourner (doit être &gt; 0)
     * @return liste des N villes les plus peuplées sous forme de {@link VilleDto}
     */
    @Transactional
    @Override
    public List<VilleDto> topVilles(Integer idDept, int n) {
        List<Ville> villes = villeRepository.findTopNByDepartement(
                idDept,
                org.springframework.data.domain.PageRequest.of(0, n)
        );
        return villeMapper.toDtos(villes);
    }

    /**
     * Retourne les villes d'un département dont la population est comprise entre min et max,
     * triées par population décroissante. [file:9][file:16]
     *
     * @param idDept identifiant du département
     *
     * @param min    population minimale
     * @param max    population maximale
     * @return liste de {@link VilleDto} correspondant aux critères
     */
    @Transactional
    @Override
    public List<VilleDto> villesParPopulation(Integer idDept, int min, int max) {
        List<Ville> villes =
                villeRepository.findByPopulationBetweenAndDepartementIdOrderByPopulationDesc(min, max, idDept);
        return villeMapper.toDtos(villes);
    }

    @Override
    public List<VilleDto> villesParDepartementParPopulation(String code, int min, int max) {
        List<Ville> villes =
                villeRepository.findByPopulationBetweenAndDepartementCodeOrderByPopulationDesc(min, max, code);
        return villeMapper.toDtos(villes);
    }
}