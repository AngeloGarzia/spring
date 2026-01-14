package fr.diginamic.recencement.controleurs;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.BaseColor;
import java.io.ByteArrayOutputStream;
import fr.diginamic.recencement.Dto.DepartementDto;
import fr.diginamic.recencement.Dto.VilleDto;
import fr.diginamic.recencement.mappers.DepartementMapper;
import fr.diginamic.recencement.services.DepartementService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    //GET
    @GetMapping("/{code}/pdf")
    @Operation(summary = "Export PDF département")
    public ResponseEntity<byte[]> exportPdfDepartement(@PathVariable String code) {
        DepartementDto dept = service.getByCode(code);
        List<VilleDto> villes = service.getVillesByDepartement(code);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            // Cadre fin autour du document
            PdfPTable borderTable = new PdfPTable(1);
            borderTable.setWidthPercentage(80);
            borderTable.getDefaultCell().setBorder(PdfPCell.BOX);
            borderTable.getDefaultCell().setBorderWidth(2);
            borderTable.getDefaultCell().setBackgroundColor(new BaseColor(245, 245, 245));  // Fond très clair
            borderTable.getDefaultCell().setPadding(15);

            PdfPCell contentCell = new PdfPCell();
            contentCell.setBorder(PdfPCell.NO_BORDER);
            contentCell.setBackgroundColor(new BaseColor(245, 245, 245));
            contentCell.setPadding(15);

            // Contenu dans la cellule
            Paragraph titre = new Paragraph(dept.getNom(), FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 22));
            contentCell.addElement(titre);
            contentCell.addElement(Chunk.NEWLINE);

            contentCell.addElement(new Paragraph("Code du Departement: " + dept.getCode()));
            //contentCell.addElement(new Paragraph("Nom: " + dept.getNom()));
            contentCell.addElement(Chunk.NEWLINE);

            contentCell.addElement(new Paragraph("Villes:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            for (VilleDto v : villes) {
                contentCell.addElement(new Paragraph("  • " + v.getNom() + " - " + v.getPopulation() + " habitants."));
            }

            borderTable.addCell(contentCell);
            document.add(borderTable);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Erreur PDF", e);
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + code + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(baos.toByteArray());
    }
}