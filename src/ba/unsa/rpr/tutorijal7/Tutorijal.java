package ba.unsa.rpr.tutorijal7;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Tutorijal {

    public static ArrayList<Grad> ucitajGradove()throws FileNotFoundException {
        ArrayList<Grad> gradovi = new ArrayList<Grad>();
        Scanner ulaz;
        try {
            ulaz = new Scanner(new FileReader("mjerenja.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("Datoteka mjerenja.txt ne postoji ili se ne može otvoriti");
            throw e;
        }
        ulaz.useDelimiter("\n");
        while (ulaz.hasNext()) {
            String podaci = ulaz.next();
            String[] nizPodataka = podaci.split(",");
            String grad = nizPodataka[0];
            Double[] temperature = new Double[nizPodataka.length-1];
            for (int i = 1; i < nizPodataka.length; i++) {
                temperature[i - 1] = Double.parseDouble(nizPodataka[i]);
            }
            gradovi.add(new Grad(grad, temperature));
        }
        ulaz.close();
        return gradovi;
    }

    public static void zapisiXml(UN drzave) throws FileNotFoundException {
        XMLEncoder izlaz = new XMLEncoder(new FileOutputStream("un.xml"));
        izlaz.writeObject(drzave);
        izlaz.close();
    }
    public static UN ucitajXml (ArrayList<Grad> gradovi) {
        UN povratne_drzave = new UN();
        ArrayList<Drzava> povratna = new ArrayList<Drzava>();
        Document xmldoc = null;
        try {
            DocumentBuilder docReader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            xmldoc = docReader.parse(new File("drzave.xml"));
            Element korijen = xmldoc.getDocumentElement();
            NodeList djeca = korijen.getChildNodes();
            for (int i = 0; i < djeca.getLength(); i++) {
                Node drzave = djeca.item(i);
                if (drzave instanceof Element && drzave.getNodeName().equals("#text")) continue;
                if (drzave instanceof Element && ((Element) drzave).getTagName().equals("drzava")) {
                    int brojStanovnika = Integer.parseInt(((Element) drzave).getAttribute("stanovnika"));
                    String nazivDrzave = ((Element) drzave).getElementsByTagName("naziv").item(0).getTextContent();
                    Double povrsinaDrzave = Double.parseDouble(((Element) drzave).getElementsByTagName("povrsina").item(0).getTextContent());
                    String jedinicaPovrsine = ((Element) ((Element) drzave).getElementsByTagName("povrsina").item(0)).getAttribute("jedinica");
                    NodeList glavni = ((Element) drzave).getElementsByTagName("glavnigrad");
                    String nazivGlavnog = ((Element) glavni.item(0)).getElementsByTagName("naziv").item(0).getTextContent();
                    int brojStanovnikaGlavnog = Integer.parseInt(((Element) glavni.item(0)).getAttribute("stanovnika"));
                    double[] temperatureGlavnog = new double[1000];
                    Grad pronadjiGrad = null;
                    for (Grad pomocna : gradovi) {
                        if (pomocna.getNaziv().equals(nazivGlavnog)) {
                            pronadjiGrad = pomocna;
                            temperatureGlavnog = pomocna.getTemperature();
                            pomocna.setBrojStanovnika(brojStanovnikaGlavnog);
                            break;
                        }
                    }
                    if (pronadjiGrad != null )
                        povratna.add(new Drzava(nazivDrzave,brojStanovnika,povrsinaDrzave,jedinicaPovrsine,pronadjiGrad));
                    else
                        povratna.add(new Drzava(nazivDrzave,brojStanovnika,povrsinaDrzave,jedinicaPovrsine,new Grad(nazivGlavnog,brojStanovnikaGlavnog,null)));
                }
            }
        }
        catch (Exception e) {
            throw new IllegalArgumentException("drzave.xml nije validan XML dokument");
        }
        povratne_drzave.setDrzave(povratna);
        return povratne_drzave;
    }

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<Grad> rez=new ArrayList<Grad>();

        try {
            rez = ucitajGradove();
            for (var grad : rez) {
                System.out.println(grad);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        UN novi=new UN();
        novi=ucitajXml(rez);
        zapisiXml(novi);
        Grad g1=rez.get(0);
        Grad g2=rez.get(1);
        Drzava drzava=new Drzava();
        drzava.setGlavniGrad(g1);
        drzava.setNaziv("Bih");
        drzava.setBrojStanovnika(45525);
        drzava.setJedinicaZaPovrsinu("km");
        drzava.setPovrsina(25525.0);

        Drzava drzava1=new Drzava();
        drzava1.setGlavniGrad(g2);
        drzava1.setNaziv("BH");
        drzava1.setBrojStanovnika(455);
        drzava1.setJedinicaZaPovrsinu("km");
        drzava1.setPovrsina(225.0);

        UN drz=new UN();
        drz.dodajDrzavu(drzava);
        drz.dodajDrzavu(drzava1);
        zapisiXml(drz);


    }
}
