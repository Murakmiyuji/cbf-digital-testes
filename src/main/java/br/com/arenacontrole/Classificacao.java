package br.com.arenacontrole;

public class Classificacao {
    private final String nome;
    private final int PG, J, V, E, D, GP, GC, SG, CA, CV;

    public Classificacao(Time t) {
        this.nome = t.getNome();
        this.PG = t.getPontos();
        this.J  = t.getJogos();
        this.V  = t.getVitorias();
        this.E  = t.getEmpates();
        this.D  = t.getDerrotas();
        this.GP = t.getGolsPro();
        this.GC = t.getGolsContra();
        this.SG = t.getSaldoGols();
        this.CA = t.getCartoesAmarelos();
        this.CV = t.getCartoesVermelhos();
    }

    public String getNome() { return nome; }
    public int getPG() { return PG; }
    public int getJ() { return J; }
    public int getV() { return V; }
    public int getE() { return E; }
    public int getD() { return D; }
    public int getGP() { return GP; }
    public int getGC() { return GC; }
    public int getSG() { return SG; }
    public int getCA() { return CA; }
    public int getCV() { return CV; }
}
