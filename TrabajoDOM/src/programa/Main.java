package programa;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import clases.Producto;

public class Main {
	
	public static Scanner sc = new Scanner(System.in);
	public static File f = new File("Supermercado.xml");
	
	public static void main (String[] args) {
		
		Document d = null;
		if(!f.exists()) {
			d = crearDocumento();	
			System.out.println("Se ha creado el documento XML Supermercado.xml");
		}
		else {
			d = parseDocument(d);
		}
		
		int opcion = 0;
		System.out.println("Se va a trabajar sobre el fichero de nombre: "+f.getName());
		System.out.println("==================================");
		System.out.println("Bienvenido al menu principal");
		do {
			mostrarMenu();
			System.out.println("-----------------");
			System.out.println("Elige una opcion");
			opcion=sc.nextInt();
			sc.nextLine();
			switch(opcion) 
			{
			case 1:
				String nombre;
				int id, precio, stock;
				System.out.println("¿Cuantos productos quieres añadir?");
				int num = sc.nextInt();
				ArrayList<Producto> productos = new ArrayList<Producto>(num);
				for (int i = 0; i < num; i++) {
					System.out.println("Introduce el ID:");
					id = sc.nextInt();
					System.out.println("Introduce el nombre: ");
					nombre = sc.next();
					System.out.println("Introduce el precio");
					precio = sc.nextInt();
					System.out.println("Introduce el stock");
					stock = sc.nextInt();
					productos.add(new Producto(id, nombre, precio,stock));
				}
				for (Producto p : productos) {
					escribir(p,d);
				}
				break;
			case 2:
				leer(d);
				break;
			case 3:
				System.out.println("Introduce el ID:");
				id = sc.nextInt();
				System.out.println("Introduce el nombre: ");
				nombre = sc.next();
				System.out.println("Introduce el precio");
				precio = sc.nextInt();
				System.out.println("Introduce el stock");
				stock = sc.nextInt();
				Producto p = new Producto(id, nombre, precio,stock);
				escribir(p,d);
				break;
			case 4:
				System.out.println("¿Que producto quieres eliminar?");
				String np6 = sc.next();
				borrar(d, np6);
				break;
			case 5:
				System.out.println("Saliendo");
				break;
			case 101:
				test(d);
				break;
			default:
				System.out.println("Opcion no valida");
				break;
			}
		}while(opcion!=5);
		
		System.out.println("\n");
		System.out.println("Hasta pronto");
		
	}
	
	private static void test(Document d) {
		escribir(new Producto(1, "Manzana", 3, 10), d);
		escribir(new Producto(2, "Pera", 4, 10), d);
		escribir(new Producto(3, "Platano", 5, 10), d);
		escribir(new Producto(4, "Naranja", 8, 10), d);
		escribir(new Producto(5, "Aceituna", 3, 10), d);
		escribir(new Producto(6, "Mandarina", 2, 10), d);
		escribir(new Producto(7, "Albaricoque", 1, 10), d);
	}

	private static void mostrarMenu() {
		System.out.println("1-Guardar productos");
		System.out.println("2-Mostrar productos");
		System.out.println("3-Añadir producto");
		System.out.println("4-Borrar producto");
		System.out.println("5-Salir");
	}
	
	public static Document crearDocumento() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		Document d = null;
		try {
			db = dbf.newDocumentBuilder();
			DOMImplementation domi = db.getDOMImplementation();
			d = domi.createDocument(null, null, null);
			d.setXmlVersion("1.0");
			d.setXmlStandalone(true);
			return d;
		} catch (ParserConfigurationException /*| SAXException | IOException*/ e) {
			System.err.println("Error al crear el Document");
			return null;
		}
	}
	
	private static void escribir(Producto p, Document d) {
		
		Element supermercado = null;
		Element productos = null;
		
		if(!d.hasChildNodes()) {
			supermercado = d.createElement("Supermercado");
			productos = d.createElement("Productos");
			supermercado.appendChild(productos);
			d.appendChild(supermercado);
		}
		else {
			supermercado = (Element) d.getFirstChild();
			productos = (Element) supermercado.getFirstChild();
		}
		
		Element producto = d.createElement("Producto");
		Element nombre = d.createElement("Nombre");
		Element precio = d.createElement("Precio");
		Element stock = d.createElement("Stock");
		Attr id = d.createAttribute("ID");
		id.setValue(String.valueOf(p.getId()));
		producto.setAttributeNode(id);
		
		Text nom = d.createTextNode(p.getNombre());
		nombre.appendChild(nom);
		Text pre = d.createTextNode(String.valueOf(p.getPrecio()));
		precio.appendChild(pre);
		Text sto = d.createTextNode(String.valueOf(p.getStock()));
		stock.appendChild(sto);
		
		producto.appendChild(nombre);
		producto.appendChild(precio);
		producto.appendChild(stock);
		productos.appendChild(producto);

		
		Source s = new DOMSource(d);
		StreamResult r = new StreamResult(f);
		Transformer t = null;
		try {
			t = TransformerFactory.newInstance().newTransformer();
			t.transform(s, (javax.xml.transform.Result) r);
		} catch (TransformerFactoryConfigurationError | TransformerException e) {
			e.printStackTrace();
		} 
	}
	
	public static Document parseDocument(Document d) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
			d = db.parse(f);
			return d;
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void leer(Document d) {
		d = parseDocument(d);
		NodeList productos = d.getElementsByTagName("Productos");
		System.out.println("Productos:");
		for (int i = 0; i < productos.getLength(); i++) {
			NodeList producto = d.getElementsByTagName("Producto");
			System.out.println("\t"+"Producto:");
			for (int j = 0; j < producto.getLength(); j++) {
				Element p = (Element) producto.item(j);
				
				String id = p.getAttribute("ID");
				
				NodeList n = p.getElementsByTagName("Nombre");
				Element nom =  (Element) n.item(0);
				String nombre = nom.getTextContent();
				
				NodeList pr = p.getElementsByTagName("Precio");
				Element pre = (Element) pr.item(0);
				String precio = pre.getTextContent();
				
				NodeList st = p.getElementsByTagName("Stock");
				Element sto = (Element) st.item(0);
				String stock = sto.getTextContent();
				
				System.out.println("\t"+"\t"+"---------------");
				System.out.printf("\t"+"\t"+"ID: " + id + "\n");
				System.out.printf("\t"+"\t"+"Nombre: " + nombre + "\n");
				System.out.printf("\t"+"\t"+"Precio: " + precio + "\n");
				System.out.printf("\t"+"\t"+"Stock: "+stock+ "\n");
				System.out.println("\t"+"\t"+"-----------------");
			}
		}
	}
	
	public static void borrar(Document d, String id) {
		d = parseDocument(d);
		NodeList productos = d.getElementsByTagName("Producto");
		for (int i = 0; i < productos.getLength(); i++) {
			Element producto = (Element) productos.item(i);
			if(producto.getAttribute("ID").equals(id)) {
				producto.getParentNode().removeChild(producto);
			}
		}
		
		Source s = new DOMSource(d);
		StreamResult r = new StreamResult(f);
		Transformer t = null;
		try {
			t = TransformerFactory.newInstance().newTransformer();
			t.transform(s, (javax.xml.transform.Result) r);
		} catch (TransformerFactoryConfigurationError | TransformerException e) {
			e.printStackTrace();
		} 
	}
	
}
