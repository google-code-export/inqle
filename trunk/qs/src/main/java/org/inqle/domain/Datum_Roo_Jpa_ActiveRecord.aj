// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inqle.domain.Datum;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Datum_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager Datum.entityManager;
    
    public static final EntityManager Datum.entityManager() {
        EntityManager em = new Datum().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Datum.countData() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Datum o", Long.class).getSingleResult();
    }
    
    public static List<Datum> Datum.findAllData() {
        return entityManager().createQuery("SELECT o FROM Datum o", Datum.class).getResultList();
    }
    
    public static Datum Datum.findDatum(Long id) {
        if (id == null) return null;
        return entityManager().find(Datum.class, id);
    }
    
    public static List<Datum> Datum.findDatumEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Datum o", Datum.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void Datum.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Datum.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Datum attached = Datum.findDatum(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Datum.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Datum.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Datum Datum.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Datum merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
