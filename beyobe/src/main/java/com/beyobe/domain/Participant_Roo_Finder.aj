// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.beyobe.domain;

import com.beyobe.domain.Participant;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect Participant_Roo_Finder {
    
    public static TypedQuery<Participant> Participant.findParticipantsByUsernameEqualsAndPasswordEquals(String username, String password) {
        if (username == null || username.length() == 0) throw new IllegalArgumentException("The username argument is required");
        if (password == null || password.length() == 0) throw new IllegalArgumentException("The password argument is required");
        EntityManager em = Participant.entityManager();
        TypedQuery<Participant> q = em.createQuery("SELECT o FROM Participant AS o WHERE o.username = :username  AND o.password = :password", Participant.class);
        q.setParameter("username", username);
        q.setParameter("password", password);
        return q;
    }
    
}
