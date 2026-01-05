package fr.diginamic.hello.services;


import fr.diginamic.hello.controleurs.Departement;
import fr.diginamic.hello.controleurs.Ville;
import fr.diginamic.hello.dto.DepartementDto;
import fr.diginamic.hello.dto.VilleDto;
import fr.diginamic.hello.mappers.DepartementMapper;
import fr.diginamic.hello.mappers.VilleMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Service métier pour Départements .
 */
@Service
@Transactional
public class DepartementService {

    private final DepartementDao dao;
    private final VilleDao villeDao;
    private final DepartementMapper mapper;
    private final VilleMapper villeMapper;

    public DepartementService(DepartementDao dao, VilleDao villeDao,
                              DepartementMapper mapper, VilleMapper villeMapper) {
        this.dao = dao;
        this.villeDao = villeDao;
        this.mapper = mapper;
        this.villeMapper = villeMapper;
    }

    @Transactional
    public DepartementDto creer(DepartementDto dto) {
        Departement dept = mapper.toEntity(dto);
        Departement saved = dao.save(dept);
        return mapper.toDto(saved);
    }

    @Transactional
    public List<DepartementDto> lister() {
        return mapper.toDtos(dao.findAll());
    }

    @Transactional
    public DepartementDto getById(Integer id) {
        Departement dept = dao.findById(id);
        if (dept == null) {
            throw new RuntimeException("Département non trouvé");
        }
        return mapper.toDto(dept);
    }

    @Transactional
    public DepartementDto getByCode(String code) {
        Departement dept = dao.findByCode(code);
        if (dept == null) {
            throw new RuntimeException("Département non trouvé");
        }
        return mapper.toDto(dept);
    }

    @Transactional
    public List<DepartementDto> supprimer(Integer id) {
        Departement dept = dao.findById(id);
        if (dept == null) {
            throw new RuntimeException("Département non trouvé");
        }
        dao.deleteById(id);
        return lister();
    }

    @Transactional
    public DepartementDto modifier(Integer id, DepartementDto dto) {
        Departement existant = dao.findById(id);
        if (existant == null) {
            throw new RuntimeException("Département non trouvé");
        }
        existant.setCode(dto.getCode());
        existant.setNom(dto.getNom());
        Departement saved = dao.save(existant);
        return mapper.toDto(saved);
    }

    //  : N plus grandes villes
    @Transactional
    public List<VilleDto> topVilles(Integer idDept, int n) {
        List<Ville> villes = villeDao.findTopNVillesByDepartement(idDept, n);
        return villeMapper.toDtos(villes);
    }

    @Transactional
    public List<VilleDto> villesParPopulation(Integer idDept, int min, int max) {
        List<Ville> villes =
                villeDao.findByPopulationBetweenAndDepartement(min, max, idDept);
        return villeMapper.toDtos(villes);
    }
}