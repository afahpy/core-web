package com.coreweb.domain;

import org.hibernate.*;
import org.hibernate.cfg.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.coreweb.IDCore;
import com.coreweb.util.Misc;
//import org.hibernate.service.ServiceRegistryBuilder;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.logging.Level;

public class HibernateUtil {

	private static Configuration configuration;
	private static SessionFactory sessionFactory;
	// private Session session;

	private static void cargaPropiedades(Configuration conf) throws Exception {
		String file = "/data_source.properties";
		try {
			
				
			InputStream in = new IDCore().getClass().getResourceAsStream(file);

			Properties pro = new Properties();
			pro.load(in);
			
			for (Iterator ite = pro.keySet().iterator(); ite.hasNext();) {
				String key = (String) ite.next();
				String value = pro.getProperty(key);
				conf.setProperty(key, value);
			}
			
		} catch (Exception e) {
			System.out.println("\n\n\n Error al leer el archivo de Hibernate "+file+"\n"+e.getMessage()+"\n");
			e.printStackTrace();
		}
	}

	static {
		try {

			// Misc m = new Misc();

			// java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
			// configuration = new
			// Configuration().configure("classpath:/hibernate.cfg.xml");
			configuration = new Configuration().configure("hibernate.cfg.xml");
			cargaPropiedades(configuration);
			sessionFactory = createSessionFactory();
			
			/*
			  System.out.println("============================");
			  System.out.println("============================");
			  System.out.println("============================");
			  
			  Properties p = configuration.getProperties(); Set ks =
			  p.keySet(); for (Iterator iterator = ks.iterator();
			  iterator.hasNext();) { Object k = (String) iterator.next();
			  Object o = configuration.getProperty((String) k);
			  System.out.println("k: " + k + " - " + o); }
			  
			  System.out.println("============================");
			  System.out.println("============================");
			  System.out.println("============================");
			 */

			/*
			 * 
			 * 
			 * sessionFactory = configuration.configure().buildSessionFactory();
			 * session = sessionFactory.openSession();
			 */
			/*
			 * System.out.println("============================");
			 * System.out.println("============================");
			 * System.out.println("============================");
			 * 
			 * 
			 * Properties p = configuration.getProperties(); Set ks =
			 * p.keySet(); for (Iterator iterator = ks.iterator();
			 * iterator.hasNext();) { Object k = (String) iterator.next();
			 * Object o = configuration.getProperty((String)k);
			 * System.out.println("k: "+ k + " - " + o ); }
			 * 
			 * System.out.println("============================");
			 * System.out.println("============================");
			 * System.out.println("============================"); );
			 */

		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.out.println("Initial SessionFactory creation failed.\n" + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	private static SessionFactory createSessionFactory() {

		return createSessionFactory(configuration);
	}

	private static SessionFactory createSessionFactory(Configuration cfg) {
		// Create the SessionFactory from hibernate.cfg.xml
		configuration = cfg;
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
				.buildServiceRegistry();
		SessionFactory sf = configuration.buildSessionFactory(serviceRegistry);
		return sf;
	}

	private SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	synchronized public void forceRebuildSessionFactory(Configuration cfg) {
		try {
			sessionFactory = createSessionFactory(cfg);
			Session session = sessionFactory.openSession();
			System.out.println("---- ok forceRebuildSessionFactory ----");
		} catch (Exception ex) {
			System.out.println("---- error !!! forceRebuildSessionFactory ----");
			ex.printStackTrace();
		}
	}

	synchronized public void rebuildSessionFactory() {
		// si es null si o si hay que asignarle algo para sincronizar :(
		if (sessionFactory == null)
			sessionFactory = createSessionFactory();
		System.out.println("---- rebuildSessionFactory----");
		synchronized (sessionFactory) {
			try {
				sessionFactory = createSessionFactory();
				Session session = sessionFactory.openSession();
				System.out.println("---- ok rebuildSessionFactory----");
			} catch (Exception ex) {
				System.out.println("---- error !!! rebuildSessionFactory----");
				ex.printStackTrace();
			}
		}
	}

	public Session getSession() throws Exception {
		// checkSession();
		return sessionFactory.openSession();
	}

	private void checkSession() throws Exception {
		int v = 0;
		boolean ok = false;
		Session session = getSession();

		while (ok == false) {
			try {
				if (session.isOpen() == false) {
					// session = session.getSessionFactory().openSession();
					session = sessionFactory.openSession();
				}
				/*
				 * 
				 * Criteria cri = session.createCriteria(Ping.class.getName());
				 * Order r = (Order) Order.asc("echo"); cri.addOrder(r);
				 * 
				 * List lo = cri.list(); int size = lo.size();
				 */

				ok = true;

			} catch (Exception ex) {
				if (v >= 2) {
					ok = true;
					throw new Exception("No funciona la DB");
				}
				rebuildSessionFactory();
				v++;
			}
		}

	}

	public void clearDB() {
		try {
			Register rr = Register.getInstance();
			rr.dropAllTables();

			Ping p = new Ping();
			p.setEcho("clearDB-" + System.currentTimeMillis());
			// rr.saveObject(p);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void pruebaPreparedStatement() {
		try {
			Class.forName("org.postgresql.Driver");
			Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/yhaguydb", "postgres",
					"postgres");

			String sql = "SELECT * FROM usuariooperacion WHERE idformulario = ?";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, 4);

			System.out.println("====================");
			ResultSet rd = stm.getResultSet();
			ParameterMetaData pmd = stm.getParameterMetaData();
			System.out.println("ParameterMetaData:" + pmd);
			System.out.println("pmd.getParameterCount():" + pmd.getParameterCount());
			System.out.println("pmd.getParameterTypeName(0):" + pmd.getParameterTypeName(1));

			// System.out.println("rd.getObject(1):"+rd.getObject(0));
			System.out.println("====================");

			ResultSet rs1 = stm.executeQuery();
			System.out.println("List");
			while (rs1.next()) {
				String alias = rs1.getString(2);
				Object idformulario = rs1.getObject(7);
				System.out.println(alias + " " + idformulario);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void pruebaProperties(){
		try {
			
		Register rr = Register.getInstance();
		rr.getAllLogin();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// HibernateUtil.clearDB();

		// pruebaPreparedStatement();
		pruebaProperties();
	}

}
