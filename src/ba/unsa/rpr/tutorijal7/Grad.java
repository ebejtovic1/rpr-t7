package ba.unsa.rpr.tutorijal7;

public class Grad {
    private String naziv;
    private int brojStanovnika;
    private double [] temperature;
    private int velicinaNiza;

    public Grad() {
        velicinaNiza = 0;
        temperature = new double[1000];
    }

    public Grad(String grad, Double[] temperature) {
        naziv = grad;
        velicinaNiza = temperature.length;
        this.temperature = new double[1000];
        for (int i = 0; i < velicinaNiza; i++)
            this.temperature[i] = temperature[i];
        brojStanovnika = 0;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(int brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }

    public double[] getTemperature() {
        return temperature;
    }

    public void setTemperature(double[] temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        String rezultat = naziv + " (" + brojStanovnika + "): ";
        for (int i = 0; i < velicinaNiza; i++) {
            if (i != velicinaNiza - 1)
                rezultat += temperature[i] + ", ";
            else
                rezultat += temperature[i];
        }
        return rezultat;
    }
}
