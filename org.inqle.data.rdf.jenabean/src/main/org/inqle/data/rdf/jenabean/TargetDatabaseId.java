package org.inqle.data.rdf.jenabean;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Annotation for any classes which are intended to be persisted in an SystemDatamodel.
 * The value should be the role ID of the datamodel, to which objects of this class should
 * be persisted.
 * 
 * @author David Donohue
 * Jul 3, 2008
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TargetDatabaseId {
	String value();
}