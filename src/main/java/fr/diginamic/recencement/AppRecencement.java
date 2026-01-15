package fr.diginamic.recencement;

import fr.diginamic.recencement.Securite.Role;
import fr.diginamic.recencement.Securite.Utilisateur;
import fr.diginamic.recencement.interfaces.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class AppRecencement {

	public static void main(String[] args) {
		SpringApplication.run(AppRecencement.class, args);
	}

	@Bean
	CommandLineRunner initData(UtilisateurRepository repo, PasswordEncoder encoder) {
		return args -> {
			if (repo.count() == 0) {

				Role adminRole = new Role();
				adminRole.setNom("ROLE_ADMIN");

				Role userRole = new Role();
				userRole.setNom("ROLE_USER");


				Utilisateur admin = new Utilisateur();
				admin.setUsername("admin");
				admin.setPassword(encoder.encode("admin123"));
				admin.setRoles(List.of(adminRole));
				adminRole.setUtilisateur(admin);

				Utilisateur user = new Utilisateur();
				user.setUsername("user");
				user.setPassword(encoder.encode("user123"));
				user.setRoles(List.of(userRole));
				userRole.setUtilisateur(user);

				repo.save(admin);
				repo.save(user);

			}
		};
	}
}
