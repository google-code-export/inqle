// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inqle.domain.Participant;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Participant_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager Participant.entityManager;
    
    public static final EntityManager Participant.entityManager() {
        EntityManager em = new Participant().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Participant.countParticipants() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Participant o", Long.class).getSingleResult();
    }
    
    public static List<Participant> Participant.findAllParticipants() {
        return entityManager().createQuery("SELECT o FROM Participant o", Participant.class).getResultList();
    }
    
    public static Participant Participant.findParticipant(Long id) {
        if (id == null) return null;
        return entityManager().find(Participant.class, id);
    }
    
    public static List<Participant> Participant.findParticipantEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Participant o", Participant.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void Participant.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Participant.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Participant attached = Participant.findParticipant(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Participant.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Participant.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Participant Participant.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Participant merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
