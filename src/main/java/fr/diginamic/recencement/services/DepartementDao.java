package fr.diginamic.recencement.services;

import fr.diginamic.recencement.controleurs.Departement;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO pour opérations CRUD sur Departement via EntityManager (JPA native).
 */

@Repository
public class DepartementDao {
    /**
     * Injecte EntityManager par persistence unit (application.properties).
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Sauvegarde ou met à jour (persist si new, merge si existant).
     *
     * @param d Entité à persister.
     * @return Entité sauvée.
     */
    public Departement save(Departement d) {
        if (d.getId() == null) {
            em.persist(d);
            return d;
        }
        return em.merge(d);
    }

    /**
     * Trouve par ID.
     */
    public Departement findById(Integer id) {
        return em.find(Departement.class, id);
    }

    /**
     * Liste tous les departements.
     */
    public List<Departement> findAll() {
        String jpql = "SELECT d FROM Departement d";
        return em.createQuery(jpql, Departement.class).getResultList();
    }

    /**
     * trouve par code Postal.
     */
    public Departement findByCode(String code) {
        String jpql = "SELECT d FROM Departement d WHERE d.code = :code";
        TypedQuery<Departement> q = em.createQuery(jpql, Departement.class);
        q.setParameter("code", code);
        List<Departement> resultats = q.getResultList();
        return resultats.isEmpty() ? null : resultats.get(0);
    }

    /**
     * Supprime par ID.
     */
    public void deleteById(Integer id) {
        Departement d = em.find(Departement.class, id);
        if (d != null) {
            em.remove(d);
        }
    }
}