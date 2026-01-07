package fr.diginamic.recencement.controleurs;

import fr.diginamic.recencement.Dto.DepartementDto;
import fr.diginamic.recencement.Dto.VilleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface IDepartementControleur {

    // POST /departements → Crée un département
    @Operation(summary = "Crée un nouveau département")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Département créé",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepartementDto.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    ResponseEntity<DepartementDto> creerDepartement(
            @Valid @RequestBody DepartementDto dto,         // DTO validé depuis body JSON
            BindingResult result);



    // HTTP DELETE avec paramètre id
    @Operation(summary = "Supprime un département")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste départements après suppression",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DepartementDto.class))))
    })
    ResponseEntity<List<DepartementDto>> supprimerDepartement(
            @PathVariable Integer id);

    // PUT /departements/{id} → Met à jour un département
// HTTP GET racine
    @Operation(summary = "Modifie un département")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Département modifié"),
            @ApiResponse(responseCode = "404", description = "Département introuvable")
    })
    ResponseEntity<DepartementDto> modifierDepartement(
            @PathVariable Integer id,                       // ID depuis URL
            @Valid @RequestBody DepartementDto dto,         // DTO validé depuis body
            BindingResult result);



    //  GET /departements
    @Operation(summary = "Liste tous les départements")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste départements",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DepartementDto.class))))
    })
    ResponseEntity<List<DepartementDto>> getDepartements();


    // GET /departements/{id}/top-villes/{n} → Top N villes d'un département
    // GET avec 2 paramètres

    @Operation(summary = "Top N villes d'un département")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Top N villes",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = VilleDto.class))))
    })
    ResponseEntity<List<VilleDto>> topVilles(
            @PathVariable Integer id,                       // ID département
            @PathVariable int n);

    //GET /departements/{id}/population/{min}/{max} → Villes par population min/max et par id departement
    @Operation(summary = "Villes par population + département ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Villes filtrées",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = VilleDto.class))))
    })
    ResponseEntity<List<VilleDto>> villesParPopulation(
            @PathVariable Integer id,                       // ID département
            @PathVariable int min,                          // Population min
            @PathVariable int max);

    //GET /departements/code}/population/{min}/{max} → Villes par population min/max et par code departement
    @Operation(summary = "Villes par population + code département")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Villes filtrées",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = VilleDto.class))))
    })
    ResponseEntity<List<VilleDto>> villesParDepartementParPopulation(
            @PathVariable String code,                       // Code département
            @PathVariable int min,                          // Population min
            @PathVariable int max);
}
