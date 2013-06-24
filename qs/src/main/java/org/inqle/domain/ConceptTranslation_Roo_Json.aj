// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.inqle.domain.ConceptTranslation;

privileged aspect ConceptTranslation_Roo_Json {
    
    public String ConceptTranslation.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public static ConceptTranslation ConceptTranslation.fromJsonToConceptTranslation(String json) {
        return new JSONDeserializer<ConceptTranslation>().use(null, ConceptTranslation.class).deserialize(json);
    }
    
    public static String ConceptTranslation.toJsonArray(Collection<ConceptTranslation> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static Collection<ConceptTranslation> ConceptTranslation.fromJsonArrayToConceptTranslations(String json) {
        return new JSONDeserializer<List<ConceptTranslation>>().use(null, ArrayList.class).use("values", ConceptTranslation.class).deserialize(json);
    }
    
}