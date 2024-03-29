// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inqle.domain.Subscription;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Subscription_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager Subscription.entityManager;
    
    public static final EntityManager Subscription.entityManager() {
        EntityManager em = new Subscription().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Subscription.countSubscriptions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Subscription o", Long.class).getSingleResult();
    }
    
    public static List<Subscription> Subscription.findAllSubscriptions() {
        return entityManager().createQuery("SELECT o FROM Subscription o", Subscription.class).getResultList();
    }
    
    public static Subscription Subscription.findSubscription(Long id) {
        if (id == null) return null;
        return entityManager().find(Subscription.class, id);
    }
    
    public static List<Subscription> Subscription.findSubscriptionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Subscription o", Subscription.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void Subscription.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Subscription.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Subscription attached = Subscription.findSubscription(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Subscription.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Subscription.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Subscription Subscription.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Subscription merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
