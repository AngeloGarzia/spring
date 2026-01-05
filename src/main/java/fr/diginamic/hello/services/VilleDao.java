package fr.diginamic.hello.services;

import fr.diginamic.hello.controleurs.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public class VilleDao {

    @PersistenceContext
        private EntityManager em;

    public List<Ville> findAll() {
        String jpql = "SELECT v FROM Ville v";
        return em.createQuery(jpql, Ville.class).getResultList();
    }

    public Ville findById(Integer id) {
        return em.find(Ville.class, id);
    }

    public List<Ville> findByNomPrefix(String prefixe) {
        String jpql = "SELECT v FROM Ville v WHERE v.nom LIKE :p";
        TypedQuery<Ville> q = em.createQuery(jpql, Ville.class);
        q.setParameter("p", prefixe + "%");
        return q.getResultList();
    }

    public List<Ville> findByPopulationBetween(int min, int max) {
        String jpql = "SELECT v FROM Ville v WHERE v.population > :min AND v.population < :max";
        TypedQuery<Ville> q = em.createQuery(jpql, Ville.class);
        q.setParameter("min", min);
        q.setParameter("max", max);
        return q.getResultList();
    }

    public Ville save(Ville ville) {
        if (ville.getId()== null) {
            em.persist(ville);
            return ville;
        } else {
            return em.merge(ville);
        }
    }

    public void deleteById(Integer id) {
        Ville v = em.find(Ville.class, id);
        if (v != null) {
            em.remove(v);
        }
    }

    public List<Ville> findTopNVillesByDepartement(Integer idDept, int n) {
        String jpql = "SELECT v FROM Ville v WHERE v.departement.id = :idDept " +
                "ORDER BY v.population DESC";
        TypedQuery<Ville> q = em.createQuery(jpql, Ville.class);
        q.setParameter("idDept", idDept);
        q.setMaxResults(n);
        return q.getResultList();
    }
    public List<Ville> findByPopulationBetweenAndDepartement(int min, int max, Integer idDept) {
        String jpql = "SELECT v FROM Ville v WHERE v.population BETWEEN :min AND :max AND v.departement.id = :idDept";
        TypedQuery<Ville> q = em.createQuery(jpql, Ville.class);
        q.setParameter("min", min);
        q.setParameter("max", max);
        q.setParameter("idDept", idDept);
        return q.getResultList();
    }

}

