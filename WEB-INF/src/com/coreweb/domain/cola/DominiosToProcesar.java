package com.coreweb.domain.cola;

import java.util.Hashtable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Register;
import com.coreweb.util.Pair;

public class DominiosToProcesar {

	private static GrabarDominio gd;
	
	public static int nGrabar = 5000;
	
	static{
		DominiosToProcesar.ini();
	}

	private static BlockingQueue<Pair<String, Domain>> cola = new ArrayBlockingQueue<>(
			100 * 1000, true);

	 public static void put(String login, Domain dom)
			throws InterruptedException {
		cola.put(new Pair<String, Domain>(login, dom));
		
		synchronized(DominiosToProcesar.gd){
			DominiosToProcesar.gd.notify();
		}
		
	}

	synchronized public static Pair<String, Domain> take()
			throws InterruptedException {
		return cola.take();
	}

	public static int size() {
		return cola.size();
	}
	
	private static void ini(){
		DominiosToProcesar.gd = new GrabarDominio();
		DominiosToProcesar.gd.start();
	}
	
	 public static void stop(){
		DominiosToProcesar.gd.seguir = false;
		System.out.println("Pendientes: "+cola.size());
		synchronized(DominiosToProcesar.gd){
			DominiosToProcesar.gd.notify();
		}
	}

}

class GrabarDominio extends Thread {
	boolean seguir = true;

	public void run() {

		Register rr = Register.getInstance();
		
		while ((this.seguir == true)||(DominiosToProcesar.size() > 0)) {

			try {
				//synchronized (this) 
				{
					
					Session session = rr.SESSIONgetSession();
					Transaction tx = session.beginTransaction();
					
					// mientras haya elementos grabar
					int nn = 0;
					while ((DominiosToProcesar.size() > 0)&&(nn < DominiosToProcesar.nGrabar)) {
						nn++;
						Pair<String, Domain> par = DominiosToProcesar.take();
						rr.SESSIONsaveObjectDomain(par.getRight(), session, par.getLeft());
		//				System.out.println((nn++)+"------------------------------"+par.getRight().getId());
					}
					
					tx.commit();
					rr.SESSIONcloseSession(session);
					
					while((DominiosToProcesar.size() == 0)&&(this.seguir == true)){
						synchronized (this){
							this.wait();
						}
					}

				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

	}

	synchronized private void grabar() {

	}

}
