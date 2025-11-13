package br.com.arenacontrole;

public class Partida {
    private final String mandante;
    private final String visitante;
    private final int gm, gv, caM, cvM, caV, cvV;

    public Partida(String mandante, String visitante,
                   int gm, int gv, int caM, int cvM, int caV, int cvV) {
        if (mandante == null || visitante == null)
            throw new IllegalArgumentException("Times inválidos");
        if (mandante.equalsIgnoreCase(visitante))
            throw new IllegalArgumentException("Times não podem ser iguais");
        if (gm < 0 || gv < 0 || caM < 0 || cvM < 0 || caV < 0 || cvV < 0)
            throw new IllegalArgumentException("Valores não podem ser negativos");
        this.mandante = mandante;
        this.visitante = visitante;
        this.gm = gm; this.gv = gv;
        this.caM = caM; this.cvM = cvM; this.caV = caV; this.cvV = cvV;
    }

    // aplica (+1) ou desfaz (−1) o efeito da partida nos dois times
    public void aplicar(Time tM, Time tV, int sinal) {
        tM.ajustarJogos(sinal);
        tV.ajustarJogos(sinal);

        if (gm > gv) { tM.ajustarVitoria(sinal); tV.ajustarDerrota(sinal); }
        else if (gm < gv) { tV.ajustarVitoria(sinal); tM.ajustarDerrota(sinal); }
        else { tM.ajustarEmpate(sinal); tV.ajustarEmpate(sinal); }

        tM.ajustarGols(gm * sinal, gv * sinal);
        tV.ajustarGols(gv * sinal, gm * sinal);

        tM.ajustarCartoes(caM * sinal, cvM * sinal);
        tV.ajustarCartoes(caV * sinal, cvV * sinal);
    }

    public String getMandante() { return mandante; }
    public String getVisitante() { return visitante; }
    public int getGm() { return gm; }
    public int getGv() { return gv; }
    public int getCaM() { return caM; }
    public int getCvM() { return cvM; }
    public int getCaV() { return caV; }
    public int getCvV() { return cvV; }
}
