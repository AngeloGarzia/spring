package fr.diginamic.recencement;

import fr.diginamic.recencement.Dto.VilleDto;
import fr.diginamic.recencement.services.VilleService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;


@SpringBootTest(classes = AppRecencement.class)
@ActiveProfiles("test")
public class VilleServiceTest {

    @Autowired
    VilleService villeService;

    @Test
    void  testextractVilles() {

       // return mapper.toDtos(villeRepository.findAll());
    }

}
