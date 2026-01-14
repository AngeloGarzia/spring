package fr.diginamic.recencement.controleurs;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Chunk;
import fr.diginamic.recencement.Dto.VilleDto;
import fr.diginamic.recencement.services.VilleService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class VilleControleur implements IVilleControleur {

    private final VilleService villeService;

    public VilleControleur(VilleService villeService) {
        this.villeService = villeService;
    }

    /**
     * GET permettant de récupérer la liste de toutes les villes (paginée).
     * URL : GET /villes?page=0&size=20
     */
    @GetMapping
    @Override
    public ResponseEntity<?> getVilles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(villeService.extractVillesPage(page, size));
    }

    /**
     * POST : insère une nouvelle ville et retourne la ville créée.
     * URL : POST /villes
     */
    @PostMapping
    @Override
    public ResponseEntity<VilleDto> ajouterVille(@Valid @RequestBody VilleDto nouvelleVille,
                                                 BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        VilleDto saved = villeService.insertVille(nouvelleVille);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * GET : retourne une ville en fonction de son id.
     * URL : GET /villes/{id}
     */
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<VilleDto> getVilleById(@PathVariable int id) {
        return ResponseEntity.ok(villeService.extractVille(id));
    }

    /**
     * GET : villes commençant par {nom}
     */
    @GetMapping("/nom/{prefixe}")
    @Override
    public ResponseEntity<?> getVillesParNomPrefixe(@PathVariable String prefixe) {
        List<VilleDto> villes = villeService.extractVillesParNomPrefixe(prefixe);

        if (villes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Aucune ville commençant par '" + prefixe + "' n'existe");
        }
        return ResponseEntity.ok(villes);
    }
    /**
     * GET : retourne les villes avec population entre min et max.
     * URL : GET /villes/population/{min}/{max}
     */
    @GetMapping("/population/{min}/{max}")
    @Override
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
    @Override
    public ResponseEntity<VilleDto> modifierVille(
            @PathVariable int id,
            @Valid @RequestBody VilleDto villeModifiee,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        VilleDto updated = villeService.modifierVille(id, villeModifiee);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE : supprime une ville et retourne la liste après suppression.
     * URL : DELETE /villes/{id}
     */
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<List<VilleDto>> supprimerVille(
            @PathVariable int id) {
        return ResponseEntity.ok(villeService.supprimerVille(id));
    }
    /**
     * Trouve les villes de plus de N habitants et creer un csv
     * URL :GET /csv/{min}
     */
    @GetMapping("/csv/{min}")
    @Operation(summary = "Export CSV villes > population min")
    public ResponseEntity<byte[]> exportCsvVilles(@PathVariable int min) {
        List<VilleDto> villes = villeService.extractVillesParPopulationSup(min);

        String csv = "nom de la ville,nombre d'habitants,code département,nom département\n";
        for (VilleDto v : villes) {
            csv += String.format("%s,%d,%s,%s\n",
                    v.getNom(), v.getPopulation(),
                    v.getCodeDepartement());
        }

        byte[] bytes = csv.getBytes();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=villes_" + min + ".csv")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }

}