package fr.diginamic.recencement.controleurs;

import fr.diginamic.recencement.dto.DepartementDto;
import fr.diginamic.recencement.dto.VilleDto;
import fr.diginamic.recencement.mappers.DepartementMapper;
import fr.diginamic.recencement.services.DepartementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST pour gérer les départements.
 * <p>
 * Endpoints :
 * POST   /departements          → créer
 * GET    /departements          → lister
 * DELETE /departements/{id}     → supprimer
 * GET    /departements/{id}/top-villes/{n}
 * GET    /departements/{id}/villes-population/{min}/{max}
 */
@RestController
@RequestMapping("/departements")
public class DepartementControleur {

    private final DepartementService service;
    private final DepartementMapper mapper;

    public DepartementControleur(DepartementService service, DepartementMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<DepartementDto> creerDepartement(@Valid @RequestBody DepartementDto dto,
                                                           BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.creer(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<DepartementDto>> supprimerDepartement(@PathVariable Integer id) {
        return ResponseEntity.ok(service.supprimer(id));
    }

    @GetMapping
    public ResponseEntity<List<DepartementDto>> getDepartements() {
        return ResponseEntity.ok(service.lister());
    }

    @GetMapping("/{id}/top-villes/{n}")
    public ResponseEntity<List<VilleDto>> topVilles(@PathVariable Integer id,
                                                    @PathVariable int n) {
        List<VilleDto> villes = service.topVilles(id, n);
        if (villes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(villes);
    }

    @GetMapping("/{id}/villes-population/{min}/{max}")
    public ResponseEntity<List<VilleDto>> villesParPopulation(@PathVariable Integer id,
                                                              @PathVariable int min,
                                                              @PathVariable int max) {
        List<VilleDto> villes = service.villesParPopulation(id, min, max);
        if (villes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(villes);
    }

}