package fr.diginamic.recencement.services;

import fr.diginamic.recencement.Dto.DepartementDto;
import fr.diginamic.recencement.Dto.VilleDto;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IDepartementService {
    @Transactional
    DepartementDto creer(DepartementDto dto);

    @Transactional
    List<DepartementDto> lister();

    @Transactional
    DepartementDto getById(Integer id);

    @Transactional
    DepartementDto getByCode(String code);

    @Transactional
    List<DepartementDto> supprimer(Integer id);

    @Transactional
    DepartementDto modifier(Integer id, DepartementDto dto);

    @Transactional
    List<VilleDto> topVilles(Integer idDept, int n);

    @Transactional
    List<VilleDto> villesParPopulation(Integer idDept, int min, int max);

    List<VilleDto> villesParDepartementParPopulation(String code, int min, int max);
}
