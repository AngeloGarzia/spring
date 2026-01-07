package fr.diginamic.recencement.controleurs;

import fr.diginamic.recencement.Dto.VilleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface IVilleControleur {
    //  GET /villes?page=0&size=20
    @Operation(summary = "Liste paginée des villes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page de villes",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)))  // Page<VilleDto>
    })
    ResponseEntity<?> getVilles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size);

    //  POST /villes
    @Operation(summary = "Crée une nouvelle ville")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ville créée"),
            @ApiResponse(responseCode = "400", description = "Validation échouée")
    })
    ResponseEntity<VilleDto> ajouterVille(@Valid @RequestBody VilleDto nouvelleVille,
                                          BindingResult result);

    // 3️⃣ GET /villes/{id}
    @Operation(summary = "Ville par ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ville trouvée"),
            @ApiResponse(responseCode = "404", description = "Ville introuvable")
    })
    ResponseEntity<VilleDto> getVilleById(@PathVariable int id);

    // 4️⃣ GET /villes/nom/{prefixe}
    @Operation(summary = "Villes par préfixe nom")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste villes",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = VilleDto.class)))),
            @ApiResponse(responseCode = "404", description = "Aucune ville")
    })

    //  {prefixe} pas {nom}
    ResponseEntity<?> getVillesParNomPrefixe(@PathVariable String prefixe);


    // 5️⃣ GET /villes/population/{min}/{max}
    @Operation(summary = "Villes par population")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Villes filtrées"),
            @ApiResponse(responseCode = "404", description = "Aucune ville")
    })
    ResponseEntity<?> getVillesParPopulation(
            @PathVariable int min,
            @PathVariable int max);

    // 6️⃣ PUT /villes/{id}
    @Operation(summary = "Modifie une ville")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ville modifiée"),
            @ApiResponse(responseCode = "400", description = "Validation"),
            @ApiResponse(responseCode = "404", description = "Ville introuvable")
    })
    ResponseEntity<VilleDto> modifierVille(
            @PathVariable int id,
            @Valid @RequestBody VilleDto villeModifiee,
            BindingResult result);

    // 7️⃣ DELETE /villes/{id}
    @Operation(summary = "Supprime une ville")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste après suppression")
    })
    ResponseEntity<List<VilleDto>> supprimerVille(
            @PathVariable int id);
}
