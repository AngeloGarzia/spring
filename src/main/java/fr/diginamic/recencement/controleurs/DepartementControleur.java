package fr.diginamic.recencement.controleurs;

import fr.diginamic.recencement.Dto.DepartementDto;
import fr.diginamic.recencement.Dto.VilleDto;
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
 * GET    /departements/{id}/population/{min}/{max}
 */
@RestController
@RequestMapping("/departements")// Base URL commune
public class DepartementControleur implements IDepartementControleur {

    // Service métier injecté
    private final DepartementService service;

    // Mapper pour conversions DTO
    private final DepartementMapper mapper;

    // Constructeur pour injection de dépendances
    public DepartementControleur(DepartementService service, DepartementMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    // POST /departements → Crée un département
    @PostMapping
    @Override
    public ResponseEntity<DepartementDto> creerDepartement(
            @Valid @RequestBody DepartementDto dto,         // DTO validé depuis body JSON
            BindingResult result) {                         // Erreurs de validation
        if (result.hasErrors()) {                           // Vérifie erreurs validation
            return ResponseEntity.badRequest().build();     // 400 si DTO invalide
        }
        return ResponseEntity.status(HttpStatus.CREATED)    // 201 Created
                .body(service.creer(dto));                  // + département créé
    }
    // DELETE /departements/{id} → Supprime un département
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<List<DepartementDto>> supprimerDepartement(
            @PathVariable Integer id) {                     // ID extrait de l'URL
        return ResponseEntity.ok(service.supprimer(id));    // 200 + liste mise à jour
    }

    // PUT /departements/{id} → Met à jour un département
    @PutMapping("/{id}")
    @Override
    public ResponseEntity<DepartementDto> modifierDepartement(
            @PathVariable Integer id,                       // ID depuis URL
            @Valid @RequestBody DepartementDto dto,         // DTO validé depuis body
            BindingResult result) {                         // Erreurs validation
        if (result.hasErrors())
            return ResponseEntity.badRequest()
                   .build();                                // 400 erreur
            return ResponseEntity
                   .ok(service.modifier(id, dto));          // 200 + département modifié
    }

    // GET /departements → Liste tous les départements
    @GetMapping
    @Override
    public ResponseEntity<List<DepartementDto>> getDepartements() {
        return ResponseEntity.ok(service.lister());
    }

    // GET /departements/{id}/top-villes/{n} → Top N villes d'un département
    @GetMapping("/{id}/top-villes/{n}")
    @Override
    public ResponseEntity<List<VilleDto>> topVilles(
            @PathVariable Integer id,                       // ID département
            @PathVariable int n) {                          // Nombre de villes max dans n
        List<VilleDto> villes = service.topVilles(id, n);   // Appel service
        if (villes.isEmpty()) {                             // Vérifie résultat vide
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(villes);
    }

    //GET /departements/{id}/population/{min}/{max} → Villes par population min/max et par id departement
    @GetMapping("/{id}/population/{min}/{max}")
    @Override
    public ResponseEntity<List<VilleDto>> villesParPopulation(
            @PathVariable Integer id,                       // ID département
            @PathVariable int min,                          // Population min
            @PathVariable int max) {                        // Population max
        List<VilleDto> villes = service.villesParPopulation(id, min, max);
        if (villes.isEmpty()) {
            return ResponseEntity.notFound().build();       // Vérifie résultat vide
        }
        return ResponseEntity.ok(villes);
    }
    //GET /departements/code}/population/{min}/{max} → Villes par population min/max et par code departement
    @GetMapping("/code/{code}/population/{min}/{max}")
    @Override
    public ResponseEntity<List<VilleDto>> villesParDepartementParPopulation(
            @PathVariable String code,                       // Code département
            @PathVariable int min,                          // Population min
            @PathVariable int max) {                        // Population max
        List<VilleDto> villes = service.villesParDepartementParPopulation(code, min, max);
        if (villes.isEmpty()) {
            return ResponseEntity.notFound().build();       // Vérifie résultat vide
        }
        return ResponseEntity.ok(villes);
    }

}