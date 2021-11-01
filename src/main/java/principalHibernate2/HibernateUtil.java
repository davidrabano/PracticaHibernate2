package principalHibernate2;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
	
	private static Logger logger = LogManager.getLogger(HibernateUtil.class);
	// La SessionFactory se establece una sola vez por aplicacion!
	private static final SessionFactory sessionFactory = buildSessionFactory();
	
	private static SessionFactory buildSessionFactory() {
		String methodName = HibernateUtil.class.getSimpleName() + ".buildSessionFactory()";
		// configure settings from hibernate.cfg.xml
		final StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                                                          		.configure("hibernate.cfg.xml").build();

		try {
			Metadata metadata = new MetadataSources(standardRegistry).getMetadataBuilder().build();
			SessionFactory sessionFactory= metadata.getSessionFactoryBuilder().build();
			logger.info(String.format("%1$s: SessionFactory created.", methodName));
			return sessionFactory;
		} catch (Exception ex) {
			logger.error(String.format("%1$s: Initial SessionFactory creation failed.", methodName), ex); 
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public static Session retrieveSession() {
		// necesita property hibernate.current_session_context_class
		//Session s = getSessionFactory().getCurrentSession();
		Session	s = getSessionFactory().openSession();
		return s;
	}
}
