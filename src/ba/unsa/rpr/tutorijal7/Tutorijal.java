package ba.unsa.rpr.tutorijal7;

import java.beans.XMLEncoder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

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
