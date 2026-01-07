package fr.diginamic.recencement.mappers;

import fr.diginamic.recencement.Entites.Departement;
import fr.diginamic.recencement.Dto.DepartementDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DepartementMapper {

    public DepartementDto toDto(Departement dept) {
        if (dept == null) return null;
        DepartementDto dto = new DepartementDto();
        dto.setId(dept.getId());
        dto.setCode(dept.getCode());
        dto.setNom(dept.getNom());
        return dto;
    }

    public Departement toEntity(DepartementDto dto) {
        if (dto == null) return null;
        Departement dept = new Departement();
        dept.setId(dto.getId());
        dept.setCode(dto.getCode());
        dept.setNom(dto.getNom());
        return dept;
    }

    public List<DepartementDto> toDtos(List<Departement> depts) {
        return depts.stream().map(this::toDto).collect(Collectors.toList());
    }
}