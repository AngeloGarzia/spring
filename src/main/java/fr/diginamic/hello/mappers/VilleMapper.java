package fr.diginamic.hello.mappers;

import fr.diginamic.hello.controleurs.Ville;
import fr.diginamic.hello.dto.VilleDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
    public class VilleMapper {

        public VilleDto toDto(Ville ville) {
            if (ville == null) return null;
            VilleDto dto = new VilleDto();
            dto.setId(ville.getId());
            dto.setNom(ville.getNom());
            dto.setPopulation(ville.getPopulation());
            if (ville.getDepartement() != null) {
                dto.setIdDepartement(ville.getDepartement().getId());
                dto.setCodeDepartement(ville.getDepartement().getCode());
            }
            return dto;
        }

        public Ville toEntity(VilleDto dto) {
            if (dto == null) return null;
            Ville ville = new Ville();
            ville.setId(dto.getId());
            ville.setNom(dto.getNom());
            ville.setPopulation(dto.getPopulation());
            return ville;
        }

        public List<VilleDto> toDtos(List<Ville> villes) {
            return villes.stream().map(this::toDto).collect(Collectors.toList());
        }
    }
