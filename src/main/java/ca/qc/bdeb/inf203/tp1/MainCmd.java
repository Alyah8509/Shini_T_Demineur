package ca.qc.bdeb.inf203.tp1;


public class MainCmd {
    public static void main(String[] args) {
        //crée un nouveau démineur, et le démarre.
        Demineur jeu=new Demineur(10,10,10);
        jeu.jouer();
    }
}
