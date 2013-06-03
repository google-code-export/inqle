package com.beyobe.db.util;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentityGenerator;

import com.beyobe.domain.HasUuid;

public class UseIdOrGenerate extends IdentityGenerator {

//    private static final Logger log = Logger.getLogger(UseIdOrGenerate.class
//            .getName());

//    @Override
//    public Serializable generate(SessionImplementor session, Object object)
//            throws HibernateException {
//        if (object == null)
//            throw new HibernateException(new NullPointerException());
//
//        for (Field field : object.getClass().getDeclaredFields()) {
//            if (field.isAnnotationPresent(Id.class)
//                    && field.isAnnotationPresent(GeneratedValue.class)) {
//                boolean isAccessible = field.isAccessible();
//                try {
//                    field.setAccessible(true);
//                    Object obj = field.get(object);
//                    field.setAccessible(isAccessible);
//                    if (obj != null) {
//                        if (Integer.class.isAssignableFrom(obj.getClass())) {
//                            if (((Integer) obj) > 0) {
//                                return (Serializable) obj;
//                            }
//                        }
//                    }
//                } catch (IllegalArgumentException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return super.generate(session, object);
//    }
    
    public Serializable generate(SessionImplementor arg0, Object object) throws HibernateException {
    	if (object instanceof HasUuid) {
    		String id = ((HasUuid)object).getId();
    		if (id != null) return id;
    	}
    	return UUID.randomUUID().toString();
    }
}