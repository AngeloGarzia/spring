package fr.diginamic.hello.controleurs;

import fr.diginamic.hello.exceptions.VilleApiException;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleControleur {


    private static int cptId = 1;
    private final List<Ville> villes = new ArrayList<>();

    @PostConstruct
    public void initVilles() {
        villes.add(new Ville("Paris", 200000, 1));
        villes.add(new Ville("Lyon", 500000, 2));
        villes.add(new Ville("Marseille", 800000, 3));
        villes.add(new Ville("Toulouse", 400000, 4));
        cptId = 5;
    }

    @GetMapping
    public List<Ville> getVilles() {
        return villes;
    }

    @PostMapping()

    public ResponseEntity<String> ajouterVille(@RequestBody Ville nouvelleVille) throws VilleApiException {
        for (Ville v : villes) {
            if (v.getNom().equals(nouvelleVille.getNom())) {
                throw new VilleApiException("La ville existe déjà");
            }
        }
        if (nouvelleVille.getPopulation()<=10) {
            throw new VilleApiException("La Popualation doit être superieur a 10");

        }
        if (nouvelleVille.getNom().length()<2) {
            throw new VilleApiException("La Ville doit contenir 2 carateres");

        }
        nouvelleVille.setId(cptId++);
        villes.add(nouvelleVille);
        return ResponseEntity.ok("Ville insérée avec succès");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVilleById(@PathVariable int id) throws VilleApiException {
        for (Ville v : villes) {
            if (v.getId() == id) {
                return ResponseEntity.ok(v);
            }
        }
        throw new VilleApiException("La ville n'a pas été trouvée");
    }
    @GetMapping("/nom/{prefixe}")
    public ResponseEntity<?> getVilleByNom(@PathVariable String prefixe) throws VilleApiException {
        List<Ville> resultat = villes.stream()
                .filter(v -> v.getNom() != null &&
                        v.getNom().toLowerCase().startsWith(prefixe.toLowerCase()))
                .toList();

        if (resultat.isEmpty()) {
            throw new VilleApiException(
                    "Aucune ville dont le nom commence par " + prefixe + " n'a pas été trouvée"
            );
        }
            return ResponseEntity.ok(resultat);
    }
    @GetMapping("/pop/{min}/{max}")
    public ResponseEntity<?> getVilleByPop(@PathVariable int min, @PathVariable int max) throws VilleApiException {
        List<Ville> resultat = villes.stream()
                .filter(v -> v.getPopulation() > min && v.getPopulation() < max)
                .toList();

        if (resultat.isEmpty()) {
            throw new VilleApiException(
                    "Aucune ville dont la population est comprise entre " + min + " et "+max
            );
        }
        return ResponseEntity.ok(resultat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifierVille(@PathVariable int id, @RequestBody Ville villeModifiee) throws VilleApiException {
        for (Ville v : villes) {
            if (v.getId() == id) {
                v.setNom(villeModifiee.getNom());
                v.setPopulation(villeModifiee.getPopulation());
                return ResponseEntity.ok(v);
            }
        }
        throw new VilleApiException("La ville n'a pas été trouvée");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerVille(@PathVariable int id) throws VilleApiException {
        for (Ville v : villes) {
            if (v.getId() == id) {
                villes.remove(v);
                return ResponseEntity.ok("Ville supprimée avec succès");
            }
        }
        throw new VilleApiException("La ville n'a pas été trouvée");
    }


}

