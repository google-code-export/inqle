package org.inqle.rdf.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Annotation for any classes which are intended to be persisted to a datamodel of a consistent type
 * (e.g. system or data).
 * 
 * @author David Donohue
 * Nov 9, 2009
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TargetModelType {

	String value();

}
