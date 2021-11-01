package principalHibernate2;

import java.net.URL;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import dao.DepartamentoDAO;
import dao.EmpleadoDAO;
import entity.Departamento;
import entity.Empleado;

public class App {
	
	// Método que pide un string por consola con la indicación de 'texto'
	public static String pedirString(String texto) {
		String salida;
			
		System.out.println(texto); // Ejemplo: texto="Introduzca si/no"
		Scanner entrada=new Scanner(System.in);
		salida=entrada.nextLine();
		logger.info(">>>> Se ha introducido por consola el string '" + salida + "'.");
		// entrada.close();		
		return salida;		
	}
	
	public static int mostrarMenu(String []opciones) {
		int opcionIntroducida=-1;
		
		// Muestra el menú a partir de un array de String y añade la opción "salir" al final
		logger.info(">>>> Se muestra el menú de opciones.");
		System.out.println("\nElija una opción del menú siguiente:");
		for (int i=0; i<opciones.length; i++) {
			System.out.println("Pulsa " + (i+1) + ": " + opciones[i]);
		}
		System.out.println("Pulsa " + (opciones.length+1) + ": Salir.");

		do {
			String entradaIntroducida=pedirString("Introduzca su opción:");
			try {
				opcionIntroducida=Integer.parseInt(entradaIntroducida); // Puede generar excepción si no puede convertir a integer
				if (opcionIntroducida<=0 || opcionIntroducida>opciones.length+1) {
					//System.out.print("La opción no es válida. ");
					logger.info(">>>> La opción " + opcionIntroducida + " introducida en el menú no es válida.");
				}
			} catch (Exception e) {
				// Captura NumberFormatException (introducir string en el parseInt)
				logger.error(">>>> " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": Imposible convertir el string '" + entradaIntroducida + "' en un int: " + e.getClass());
				// El logger captura: el nombre del método que genera la excepción: Aclaración del error: clase de la excepción
			}
				
		} while (opcionIntroducida<=0 || opcionIntroducida>opciones.length+1);
		// entradaIntroducida.close();
		return opcionIntroducida;
	}
	
	
	
	// Hay que crear una instancia de tipo Logger en cada clase que queramos hacer un seguimiento de log
	private static Logger logger = LogManager.getLogger(App.class);
	//private static Logger logger = LogManager.getLogger(NOMBRE_CLASE.class);
	
    public static void main( String[] args ){
    	
		/* Esto solo en el main y una vez por proyecto */
    	
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource("log4j.properties");
		PropertyConfigurator.configure(url);
		
		/**/
    	
    	final String []opciones = {			
    			"Insertar un Empleado en la BBDD.",
    			"Leer/Extraer un Empleado de la BBDD.",
    			"Actualizar la BBDD modificando un Empleado existente.",
    			"Borrar un Empleado de la BBDD.",
    			
    			"Insertar un Departamento en la BBDD.",
    			"Leer/Extraer un Departamento de la BBDD.",
    			"Actualizar la BBDD modificando un Departamento existente.",
    			"Borrar un Departamento de la BBDD."
    			};
        
        boolean exit = false; // flag de salida para el bucle do-while del switch

		do {
			int opcionMenu=mostrarMenu(opciones);
			switch (opcionMenu) {
			case 1: // Insertar Empleado
				logger.info(">>>> Se ha introducido la opción " + opcionMenu + " del menú: " + opciones[opcionMenu-1]);
				System.out.println("Se va a insertar un empleado:");
				String nombre1 = pedirString("Introduce el nombre: ");
				String apellido1 = pedirString("Introduce el primer apellido: ");
				String apellido2 = pedirString("Introduce el segundo apellido: ");

				// Hay que ver el último id de la tabla para pasar como parámetro el inmediato superior
				List<Empleado> empleadosTabla = EmpleadoDAO.getAllEmpleados();
				int ultimoIdUsado = empleadosTabla.get(empleadosTabla.size() - 1).getCodigo(); // Se coge el código del último (size-1) elemento de la lista

				// Empleado employee=new Empleado(nombre, apellido1, apellido2); // Para pruebas
				// Empleado employee=new Empleado(3, "Perez", "Lopez", 0, "", "", "", "Juan",
				// "", ""); // Para pruebas
				Empleado employee1 = new Empleado(ultimoIdUsado + 1, nombre1, apellido1, apellido2);

				EmpleadoDAO.insertEmpleado(employee1);

				break;

			case 2: // Leer/Extraer Empleado
				logger.info(">>>> Se ha introducido la opción " + opcionMenu + " del menú: " + opciones[opcionMenu-1]);
				System.out.println("Se va a leer un empleado:");
				String entradaId2=pedirString("Introduce el id: ");
				try {
					int id2 = Integer.parseInt(entradaId2); // Puede generar excepción si no puede convertir a integer
					
					if (EmpleadoDAO.existeEmpleado(id2)) {
						Empleado employee2 = EmpleadoDAO.getEmpleado(id2);
						//System.out.println(employee2.toString()); // Lo mostramos por consola
						logger.info(">>>> Leído " + employee2.toString());
					}
					else {
						//System.out.println("No existe ningún empleado en la BBDD con el id: " + id2 + ".");
						logger.info(">>>> No existe ningún empleado en la BBDD con el id: " + id2 + ".");
					}
				} catch (Exception e) {
					// Captura NumberFormatException (introducir string en el parseInt)
					logger.error(">>>> " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": Imposible convertir el string '" + entradaId2 + "' en un int: " + e.getClass());
					// El logger captura: el nombre del método que genera la excepción: Aclaración del error: clase de la excepción
				}

				break;

			case 3: // Actualizar Empleado
				logger.info(">>>> Se ha introducido la opción " + opcionMenu + " del menú: " + opciones[opcionMenu-1]);
				System.out.println("Se va a actualizar un empleado:");
				String entradaId3=pedirString("Introduce el id: ");				
				try {					
					int id3 = Integer.parseInt(entradaId3); // Puede generar excepción si no puede convertir a integer
					
					// Hay que comprobar que existe en la base de datos para poder actualizarlo, sino se muestra un mensaje y no se hace nada
					if (EmpleadoDAO.existeEmpleado(id3)) {
						System.out.println("Se va a actualizar el empleado: " + EmpleadoDAO.getEmpleado(id3).toString());
						String nombre3 = pedirString("Introduce el nombre: ");
						String ap1 = pedirString("Introduce el primer apellido: ");
						String ap2 = pedirString("Introduce el segundo apellido: ");
						Empleado employee3 = new Empleado(id3, nombre3, ap1, ap2);

						EmpleadoDAO.updateEmpleado(employee3);

					} else {
						//System.out.println("No existe ningún empleado en la BBDD con el id: " + id3 + ".");
						logger.info(">>>> No existe ningún empleado en la BBDD con el id: " + id3 + ".");
					}

				} catch (Exception e) {
					// Captura NumberFormatException (introducir string en el parseInt)
					logger.error(">>>> " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": Imposible convertir el string '" + entradaId3 + "' en un int: " + e.getClass());
					// El logger captura: el nombre del método que genera la excepción: Aclaración del error: clase de la excepción
				}

				break;

			case 4: // Borrar Empleado
				logger.info(">>>> Se ha introducido la opción " + opcionMenu + " del menú: " + opciones[opcionMenu-1]);
				System.out.println("Se va a eliminar un empleado:");					
				String entradaId4=pedirString("Introduce el id: ");
				try {					
					int id4 = Integer.parseInt(entradaId4); // Puede generar excepción si no puede convertir a integer
					
					// Hay que comprobar que existe en la base de datos para poder borrarlo, sino se muestra un mensaje y no se hace nada
					if (EmpleadoDAO.existeEmpleado(id4)) {
						System.out.println("Se va a elimirar el empleado: " + EmpleadoDAO.getEmpleado(id4).toString());
						EmpleadoDAO.deleteEmpleado(EmpleadoDAO.getEmpleado(id4));
					} 
					else {
						//System.out.println("No existe ningún empleado en la BBDD con el id: " + id4 + ".");
						logger.info(">>>> No existe ningún empleado en la BBDD con el id: " + id4 + ".");
					}

				} catch (Exception e) {
					// Captura NumberFormatException (introducir string en el parseInt)
					logger.error(">>>> " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": Imposible convertir el string '" + entradaId4 + "' en un int: " + e.getClass());
					// El logger captura: el nombre del método que genera la excepción: Aclaración del error: clase de la excepción
				}

				break;

			case 5:	// Insertar Departamento
				logger.info(">>>> Se ha introducido la opción " + opcionMenu + " del menú: " + opciones[opcionMenu-1]);
				System.out.println("Se va a insertar un departamento:");
				String nombre5 = pedirString("Introduce el nombre: ");
				String entradaCodResponsable5 = pedirString("Introduce el código de responsable: ");
				try {					
					int codResponsable5=Integer.parseInt(entradaCodResponsable5); // Puede generar excepción si no puede convertir a integer
					
					// Hay que ver el último id de la tabla para pasar como parámetro el inmediato superior
					List<Departamento> departamentosTabla = DepartamentoDAO.getAllDepartamentos();
					int ultimoCodigoUsado = departamentosTabla.get(departamentosTabla.size() - 1).getCodigo(); // Se coge el código del último (size-1) elemento de la lista
				
					Departamento deparment5=new Departamento(ultimoCodigoUsado+1, nombre5, codResponsable5);
					
					DepartamentoDAO.insertDepartamento(deparment5);
				} catch (Exception e) {
					// Captura NumberFormatException (introducir string en el parseInt)
					logger.error(">>>> " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": Imposible convertir el string '" + entradaCodResponsable5 + "' en un int: " + e.getClass());
					// El logger captura: el nombre del método que genera la excepción: Aclaración del error: clase de la excepción
				}
				
				break;
				
			case 6: // Leer/Extraer Departamento
				logger.info(">>>> Se ha introducido la opción " + opcionMenu + " del menú: " + opciones[opcionMenu-1]);
				System.out.println("Se va a leer un departamento:");
				String entradaId6=pedirString("Introduce el id: ");
				try {					
					int id6 = Integer.parseInt(entradaId6); // Puede generar excepción si no puede convertir a integer
					
					if (DepartamentoDAO.existeDepartamento(id6)) {
						Departamento department2 = DepartamentoDAO.getDepartamento(id6);
						//System.out.println(department2.toString()); // Lo mostramos por consola
						logger.info(">>>> Leído " + department2.toString());
					}
					else {
						//System.out.println("No existe ningún departamento en la BBDD con el id: " + id6 + ".");
						logger.info(">>>> No existe ningún departamento en la BBDD con el id: " + id6 + ".");
					}
				} catch (Exception e) {
					// Captura NumberFormatException (introducir string en el parseInt)
					logger.error(">>>> " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": Imposible convertir el string '" + entradaId6 + "' en un int: " + e.getClass());
					// El logger captura: el nombre del método que genera la excepción: Aclaración del error: clase de la excepción
				}

				break;

			case 7:  // Actualizar Departamento
				logger.info(">>>> Se ha introducido la opción " + opcionMenu + " del menú: " + opciones[opcionMenu-1]);
				System.out.println("Se va a actualizar un departamento:");
				String entradaId7=pedirString("Introduce el id: ");
				try {					
					int id7 = Integer.parseInt(entradaId7); // Puede generar excepción si no puede convertir a integer
					
					// Hay que comprobar que existe en la base de datos para poder actualizarlo, sino se muestra un mensaje y no se hace nada
					if (DepartamentoDAO.existeDepartamento(id7)) {
						System.out.println("Se va a actualizar el departamento: " + DepartamentoDAO.getDepartamento(id7).toString());
						String nombre7 = pedirString("Introduce el nombre: ");
						String entradaCodResponsable7 = pedirString("Introduce el código de responsable: ");
						try {
							int codResponsable7=Integer.parseInt(entradaCodResponsable7); // Puede generar excepción si no puede convertir a integer
						
							Departamento deparment7=new Departamento(id7, nombre7, codResponsable7);
						
							DepartamentoDAO.updateDepartamento(deparment7);
						} catch (Exception e) {
							// Captura NumberFormatException (introducir string en el parseInt)
							logger.error(">>>> " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": Imposible convertir el string '" + entradaCodResponsable7 + "' en un int: " + e.getClass());
							// El logger captura: el nombre del método que genera la excepción: Aclaración del error: clase de la excepción
						}
					} else {
						//System.out.println("No existe ningún departamento en la BBDD con el id: " + id7 + ".");
						logger.info(">>>> No existe ningún departamento en la BBDD con el id: " + id7 + ".");
					}

				} catch (Exception e) {
					// Captura NumberFormatException (introducir string en el parseInt)
					logger.error(">>>> " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": Imposible convertir el string '" + entradaId7 + "' en un int: " + e.getClass());
					// El logger captura: el nombre del método que genera la excepción: Aclaración del error: clase de la excepción
				}

				break;

			case 8: // Borrar Departamento
				logger.info(">>>> Se ha introducido la opción " + opcionMenu + " del menú: " + opciones[opcionMenu-1]);
				System.out.println("Se va a eliminar un departamento:");					
				String entradaId8=pedirString("Introduce el id: ");
				try {					
					int id8 = Integer.parseInt(entradaId8); // Puede generar excepción si no puede convertir a integer
					
					// Hay que comprobar que existe en la base de datos para poder borrarlo, sino se muestra un mensaje y no se hace nada
					if (DepartamentoDAO.existeDepartamento(id8)) {
						System.out.println("Se va a elimirar el departamento: " + DepartamentoDAO.getDepartamento(id8).toString());
						DepartamentoDAO.deleteDepartamento(DepartamentoDAO.getDepartamento(id8));
					} 
					else {
						//System.out.println("No existe ningún departamento en la BBDD con el id: " + id8 + ".");
						logger.info(">>>> No existe ningún departamento en la BBDD con el id: " + id8 + ".");
					}

				} catch (Exception e) {
					// Captura NumberFormatException (introducir string en el parseInt)
					logger.error(">>>> " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": Imposible convertir el string '" + entradaId8 + "' en un int: " + e.getClass());
					// El logger captura: el nombre del método que genera la excepción: Aclaración del error: clase de la excepción
				}

				break;
			case 9:
				logger.info(">>>> Se ha introducido la opción " + opcionMenu + " del menú: Salir.");
				System.out.println("Fin del programa.");
				exit = true;
				break;
				
			} // fin switch
		} while (exit == false);
		logger.info(">>>> Fin del programa.");
    } // fin main
}
        
    
//VERSION CON session COMO PARÁMETRO SIN LA PARTE DE DEPARTAMENTOS Y SIN LA PARTE DE LOGGER
//
//package ejercicioHibernate1.ejercicioHibernate1;
//
//import java.net.URL;
//import java.util.List;
//import java.util.Scanner;
//
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//
//import dao.EmpleadoDAO;
//import entity.Empleado;
//
//public class App {
//	
//	// Método que pide un string por consola con la indicación de 'texto'
//	public static String pedirString(String texto) {
//		String salida;
//			
//		System.out.println(texto); // Ejemplo: texto="Introduzca si/no"
//		Scanner entrada=new Scanner(System.in);
//		salida=entrada.nextLine();
//		// entrada.close();		
//		return salida;		
//	}
//	
//	public static int mostrarMenu(String []opciones) {
//		int opcionIntroducida;
//		// Muestra el menú a partir de un array de String y añade la opción "salir" al final
//		System.out.println("\nElija una opción del menú siguiente:");
//		for (int i=0; i<opciones.length; i++) {
//			System.out.println("Pulsa " + (i+1) + ": " + opciones[i]);
//		}
//		System.out.println("Pulsa " + (opciones.length+1) + ": Salir.");
//		
//		// Lee un int por consola con la opción elegida entre los posibles valores del menú
//		do {
//			System.out.println("Introduzca su opción:");
//			opcionIntroducida=new Scanner(System.in).nextInt();	
//			if (opcionIntroducida<=0 || opcionIntroducida>opciones.length+1) {
//				System.out.print("La opción no es válida. ");
//			}
//		} while (opcionIntroducida<=0 || opcionIntroducida>opciones.length+1);
//		// opcionIntroducida.close();
//		return opcionIntroducida;
//	}
//	
//	
//	
//	// Hay que crear una instancia de tipo Logger en cada clase que queramos hacer un seguimiento de log
//	private static Logger logger = LogManager.getLogger(App.class);
//	//private static Logger logger = LogManager.getLogger(NOMBRE_CLASE.class);
//	
//    public static void main( String[] args ){
//    	
//		/* Esto solo en el main y una vez por proyecto */
//    	
//		ClassLoader loader = Thread.currentThread().getContextClassLoader();
//		URL url = loader.getResource("log4j.properties");
//		PropertyConfigurator.configure(url);
//		
//		/**/
//    	
//    	final String []opciones={			
//    			"Insertar un Empleado en la BBDD.",
//    			"Leer/Extraer un Empleado de la BBDD.",
//    			"Actualizar la BBDD modificando un Empleado existente.",
//    			"Borrar un Empleado de la BBDD.",
//    			
//    			"Insertar un Departamento en la BBDD.",
//    			"Leer/Extraer un Departamento de la BBDD.",
//    			"Actualizar la BBDD modificando un Departamento existente.",
//    			"Borrar un Departamento de la BBDD."
//    			};
//        
//        boolean exit=false; // Para el bucle do-while del switch
//        
//        Session session = HibernateUtil.getSessionFactory().openSession(); // Copiada la clase HibernateUtil de su proyecto
//		Transaction tx = null;
//        
//        try {        	
//        	tx = session.beginTransaction();
//        	
//        	do {
//        		switch (mostrarMenu(opciones)) {
//        		case 1: // Insertar
//        			System.out.println("Se va a insertar un empleado:");
//        			String nombre=pedirString("Introduce el nombre: ");
//        			String apellido1=pedirString("Introduce el primer apellido: ");
//        			String apellido2=pedirString("Introduce el segundo apellido: ");
//        			
//        			// Hay que ver el último id de la tabla para pasar como parámetro el inmediato superior
//        			List<Empleado> empleadosTabla = EmpleadoDAO.getAllEmpleados(session);
//        			int ultimoIdUsado=empleadosTabla.get(empleadosTabla.size()-1).getCodigo(); // Se coge el código del último (size-1) elemento de la lista
//        			
//        			//Empleado employee=new Empleado(nombre, apellido1, apellido2); // Para pruebas
//        			//Empleado employee=new Empleado(3, "Perez", "Lopez", 0, "", "", "", "Juan", "", ""); // Para pruebas
//        			Empleado employee1=new Empleado(ultimoIdUsado+1, nombre, apellido1, apellido2);
//        			
//        			EmpleadoDAO.insertEmpleado(session, employee1);
//        			
//        			tx.commit();
//        			break;	
//        			
//        		case 2: // Leer/Extraer
//        			try {
//        				System.out.println("Se va a leer un empleado:");
//        				int id1=Integer.parseInt(pedirString("Introduce el id: "));
//        				Empleado employee2=EmpleadoDAO.getEmpleado(session, id1);
//        				
//        				System.out.println(employee2.toString()); // Lo mostramos por consola     				
//        				
//        			} catch (Exception e){
//        				// Captura NumberFormatException
//        			}
//        			
//        			//tx.commit(); Aquí no hace falta porque no se modifica la tabla.
//        			break;
//        			
//        		case 3: // Actualizar
//        			
//        			// Hay que comprobar que existe en la base de datos para poder actualizarlo, sino se muestra un mensaje y no se hace nada
//        			boolean existeEmpleado=false;
//        			Empleado empl=new Empleado();
//        			
//        			/*
//        			int id3=Integer.parseInt(pedirString("Introduce el id: "));
//        			String nombre1=pedirString("Introduce el nombre: ");
//        			String ap1=pedirString("Introduce el primer apellido: ");
//        			String ap2=pedirString("Introduce el segundo apellido: ");
//        			Empleado employee3=new Empleado(id3, nombre1, ap1, ap2);
//        			EmpleadoDAO.updateEmpleado(session, employee3);
//        			*/
//        			try {
//        				
//        				System.out.println("Se va a modificar un empleado:");
//        				int id3=Integer.parseInt(pedirString("Introduce el id: "));
//        				
//        				// Comprobamos que existe el empleado en la BBDD -> existeEmpleado=true; 
//        				List<Empleado> empleadosTabla3 = EmpleadoDAO.getAllEmpleados(session);
//        				for (Empleado empleado : empleadosTabla3) {
//        					if (empleado.getCodigo()==id3) {
//        						existeEmpleado=true;
//        						empl=empleado;
//        					}        					
//        				}
//        				
//        				if (existeEmpleado) { // existeEmpleado=true;
//        					//System.out.println("Se va a modificar el empleado: " + EmpleadoDAO.getEmpleado(session, id3).toString());
//        					System.out.println("Se va a modificar el empleado: " + empl.toString());
//        					String nombre1=pedirString("Introduce el nombre: ");
//                			String ap1=pedirString("Introduce el primer apellido: ");
//                			String ap2=pedirString("Introduce el segundo apellido: ");
//                			Empleado employee3=new Empleado(empl.getCodigo(), nombre1, ap1, ap2);
//                			
//                			EmpleadoDAO.updateEmpleado(session, employee3);
//                			
//        				}
//        				else {
//        					System.out.println("No existe ningún empleado en la BBDD con el id: " + id3);
//        				}
//        				
//        			} catch (Exception e){
//        				// Captura NumberFormatException
//        			}
//        			tx.commit();
//        			
//        			break;
//        			
//        		case 4:
//        			
//        			// Habría que comprobar que existe en la base de datos primero para poder borrarlo
//        			
//        			int id4=Integer.parseInt(pedirString("Introduce el id: "));
//        			EmpleadoDAO.deleteEmpleado(session, EmpleadoDAO.getEmpleado(session, id4));
//        			        			
//        			tx.commit();
//        			break;
//        			
//        		case 5:
//        			break;
//        		case 6:
//        			break;
//        		case 7: 
//        			break;
//        		case 8:
//        			break;
//        			
//        		case 9:
//        			System.out.println("Fin del programa.");
//        			exit=true;
//        		} // fin switch
//            } while (exit==false);
//        	
//        	//tx.commit();
//        	
//    	} catch (Exception e) {
//		  if (tx != null) {
//		    tx.rollback();
//		  }
//			logger.error(String.format("%1$s: error when inserting registries.", "insertEmpleado"), e);
//		}
//		finally {
//			if (session != null) {
//				session.close();
//			}
//		}
//        
//        
//        
//    } // fin main
//}
//      
    

