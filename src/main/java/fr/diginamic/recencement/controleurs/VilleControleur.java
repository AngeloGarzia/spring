package fr.diginamic.recencement.controleurs;

import fr.diginamic.recencement.dto.VilleDto;
import fr.diginamic.recencement.mappers.VilleMapper;
import fr.diginamic.recencement.services.VilleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST exposant des endpoints pour gérer les villes.
 * Cette classe reçoit les requêtes HTTP, délègue le traitement au {@link VilleService}
 * et renvoie les réponses au client (JSON + codes HTTP).
 */
@RestController
@RequestMapping("/villes")
public class VilleControleur {

    private final VilleService villeService;
    private final VilleMapper mapper;

    public VilleControleur(VilleService villeService, VilleMapper mapper) {
        this.villeService = villeService;
        this.mapper=mapper;
    }

    /**
     * GET permettant de récupérer la liste de toutes les villes.
     * URL : GET /villes
     */
    @GetMapping
    public ResponseEntity<List<VilleDto>> getVilles() {
        // TP : List<VilleDto> extractVilles()
        return ResponseEntity.ok(villeService.extractVilles());
    }

    /**
     * POST : insère une nouvelle ville et retourne la ville créée.
     * URL : POST /villes
     */
    @PostMapping
    public ResponseEntity<VilleDto> ajouterVille(@Valid @RequestBody VilleDto nouvelleVille,
                                                 BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        // TP : VilleDto insertVille(VilleDto ville)
        VilleDto saved = villeService.insertVille(nouvelleVille);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * GET : retourne une ville en fonction de son id.
     * URL : GET /villes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<VilleDto> getVilleById(@PathVariable int id) {
        // TP : VilleDto extractVille(int idVille)
        return ResponseEntity.ok(villeService.extractVille(id));
    }

    /**
     * GET : retourne une ville en fonction de son nom.
     * URL : GET /villes/nom/{nom}
     */
    @GetMapping("/nom/{nom}")
    public ResponseEntity<VilleDto> getVilleByNom(@PathVariable String nom) {
        return ResponseEntity.ok(villeService.extractVille(nom));
    }

    /**
     * GET : retourne une ville en fonction d'une population entre min et max'.
     * URL : GET /villes/population
     */
    @GetMapping("/population/{min}/{max}")
    public ResponseEntity<?> getVillesParPopulation(
            @PathVariable int min,
            @PathVariable int max) {

        List<VilleDto> villes = villeService.extractVillesParPopulation(min, max);

        if (villes.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Aucune ville trouvée pour cette plage de population");
        }

        return ResponseEntity.ok(villes);
    }

    /**
     * PUT : modifie une ville et retourne la ville modifiée.
     * URL : PUT /villes/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<VilleDto> modifierVille(@PathVariable int id,
                                                  @Valid @RequestBody VilleDto villeModifiee,
                                                  BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        // TP : VilleDto modifierVille(int idVille, VilleDto villeModifiee)
        VilleDto updated = villeService.modifierVille(id, villeModifiee);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE : supprime une ville et retourne la liste après suppression.
     * URL : DELETE /villes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<List<VilleDto>> supprimerVille(@PathVariable int id) {
        // TP : List<VilleDto> supprimerVille(int idVille)
        return ResponseEntity.ok(villeService.supprimerVille(id));
    }
}