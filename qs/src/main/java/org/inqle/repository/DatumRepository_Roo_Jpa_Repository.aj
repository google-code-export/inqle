// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.repository;

import org.inqle.domain.Datum;
import org.inqle.repository.DatumRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect DatumRepository_Roo_Jpa_Repository {
    
    declare parents: DatumRepository extends JpaRepository<Datum, Long>;
    
    declare parents: DatumRepository extends JpaSpecificationExecutor<Datum>;
    
    declare @type: DatumRepository: @Repository;
    
}