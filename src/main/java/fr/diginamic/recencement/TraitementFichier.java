package fr.diginamic.recencement;
import fr.diginamic.recencement.Dto.DepartementApiDto;
import fr.diginamic.recencement.Dto.RegionApiDto;
import fr.diginamic.recencement.Entites.Departement;
import fr.diginamic.recencement.Entites.Region;
import fr.diginamic.recencement.interfaces.RegionRepository;
import org.springframework.transaction.annotation.Transactional;
import fr.diginamic.recencement.interfaces.DepartementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class TraitementFichier implements CommandLineRunner {

    @Autowired
    DepartementRepository departementRepository;

    @Autowired
    RegionRepository regionRepository;

    public static void main(String[] args) {
        new SpringApplicationBuilder(TraitementFichier.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        RestTemplate restTemplate = new RestTemplate();

        // Departement

        DepartementApiDto[] depsResponse = restTemplate.getForObject(
                "https://geo.api.gouv.fr/departements",
                DepartementApiDto[].class);

        Set<Departement> departsToUpdate = new HashSet<>();
        for (DepartementApiDto dto : depsResponse) {
            departementRepository.findByCode(dto.getCode()).ifPresent(dept -> {
                dept.setNom(dto.getNom());
                departsToUpdate.add(dept);
            });
        }
        departementRepository.saveAll(departsToUpdate);
        System.out.println("✅ " + departsToUpdate.size() + " départements mis à jour !");



        // 3.1 Créer régions
        RegionApiDto[] regionsResponse = restTemplate.getForObject(
                "https://geo.api.gouv.fr/regions",
                RegionApiDto[].class);

        for (RegionApiDto dto : regionsResponse) {
            Region region = regionRepository.findById(dto.getCode()).orElse(new Region());
            region.setCode(dto.getCode());
            region.setNom(dto.getNom());
            regionRepository.save(region);
        }
        System.out.println("✅ " + regionsResponse.length + " régions créées !");

        // 3.2 Lier Départements → Régions
        System.out.println("🔗 Liaison Départements-Régions...");

        for (RegionApiDto regDto : regionsResponse) {
            DepartementApiDto[] depsByRegion = restTemplate.getForObject(
                    "https://geo.api.gouv.fr/regions/" + regDto.getCode() + "/departements",
                    DepartementApiDto[].class);

            Region region = regionRepository.findById(regDto.getCode()).get();
            for (DepartementApiDto depDto : depsByRegion) {
                departementRepository.findByCode(depDto.getCode()).ifPresent(dept -> {
                    dept.setRegion(region);
                    departementRepository.save(dept);
                                    });
            }
        }


    }
}