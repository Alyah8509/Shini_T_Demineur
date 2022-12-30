package ca.qc.bdeb.inf203.tp1;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.Random;
import java.util.Scanner;

public class Demineur {
    private int longueur;
    private int largeur;
    private Case[][] mine;
    private int bombes;
    private int finalBombes;
    //valeur utilisé pour JavaFX, lorsqu'on recommence le jeu. Vu que la bombe change de valeur, finalBombes va garder le nombre
    //de bombe initial.
    private int drapeau;
    //la valeur de drapeau dans le jeu
    private boolean partieFini =false;
    private ImageView[][] grille;
    //tableau pour JavaFX
    private String mots;
    //le clic du souris transmis en String
    private int mortOuDecede=0;
    //la situation de partieFini: gagné ou perdu
    private int nouveauBombe;
    private  boolean premier=true;
    /**
     * Le constructeur de Demineur, demande au minimum la largeur et longueur du jeu ainsi que le nombre de bombe.
     * Le drapeau s'agit du nombre de bombe qui serait affiché au joueur, mais dire qu'il reste 6 bombes sur 10 car il y a 4
     * drapeau ne veut pas dire que c'est vrai si le drapeau n'est pas mis sur la bombe, donc drapeau et bombe ont des comptes
     * différents.
     * @param longueur
     * @param largeur
     * @param bombes
     */
    protected Demineur (int longueur,int largeur, int bombes){
        this.largeur=largeur;
        this.bombes = bombes;
        this.longueur=longueur;
        drapeau=bombes;
        finalBombes=bombes;
    }

    /**
     *La méthode qui est appelée par MainCmd, c'est ici qu'on lit la commande et opère.
     */
    protected void jouer (){
        Scanner sc=new Scanner(System.in);
        commencer();
        do {//en tant que la partie n'est pas finie, il va continuer de lire ce qui est inscrit
            String donnees=sc.nextLine();
            interagir(donnees);
        }while (!partieFini);
    }
    /**
     *La première classe qui doit être appelé lors d'un jeu: il génère la mine et l'affiche.
     */
    private void commencer (){
        generer();
        afficher();
    }


    /**
     * Méthode qui créer la mine, private car elle est propre à Demineur.
     * Le code est expliqué en détail dans la méthode, elle va juste créer un tableau de Case, les remplir avec des
     * cases vides au début, générer des chiffres x et y en random selon le nombre de bombe et changer la valeur
     * de la case correspondant au x y à '666' comme signe de bombe. Il va aussi changer les 8 cases autour de cette
     * bombe, en additionnant 1 (+1) aux cases (0 à 1, 1 à 2, 2 à 3 et etc.)
     */
    private void generer (){
        Random rnd=new Random();
        mine=new Case[longueur][largeur];
        //initialise le tableau de case avec la longueur et la largeur et dans une boucle. Les Cases sont toujours vide.
        for (int x=0; x<longueur; x++){
            for (int y=0; y<largeur; y++){
                mine[x][y] = new Case(x,y);
            }
        }
        for (int i=0; i<bombes; i++){
            //génère les coordonnées de la bombe autant de fois qu'il y a de bombe, avec mine.length comme limite.
            int x=rnd.nextInt(largeur);
            int y=rnd.nextInt(longueur);
            if (mine[x][y].getValeur()!=666){//si cette case n'est pas déjà une bombe, il devient une bombe
                mine[x][y].setValeur();
            }else {
                do {//sinon, il regénère en tant que la position créer est une bombe.
                    x=rnd.nextInt(largeur);
                    y=rnd.nextInt(longueur);
                }while (mine[x][y].getValeur()==666);
                mine[x][y].setValeur();//setValeur va changer la valeur de la case à 666 (qui est une bombe)
            }
        }
        for (int x=0; x<largeur; x++){
            for (int y=0; y<longueur; y++){
                if (mine[x][y].getValeur()==666){//si la case est une bombe
                    caseVoisins(x,y);//methode de raccourci
                }
            }
        }
    }
    private void caseVoisins (int x,int y){
        if (x!=0){//vérifie qu'il n'est pas sur la première ligne, pour éviter les exceptions
            if (y!=0){//et qu'il n'est pas sur la première colonne
                mine[x-1][y-1].bombePresent();
                //si les deux conditions sont remplis, la case en haut à gauche appèle bombePresent
            }
            //ici, c'est la case en haut directement
            mine[x-1][y].bombePresent();
            if (y!=longueur-1){
                //en haut à droite
                mine[x-1][y+1].bombePresent();}
        }
        //les 8 cases autours de la mine sont donc appelés (s'ils existent).
        if (y!=0){
            mine[x][y-1].bombePresent();
        }if (y!=longueur-1){
            mine[x][y+1].bombePresent();
        }
        if (x!=largeur-1){
            if (y!=0){
                mine[x+1][y-1].bombePresent();}
            mine[x+1][y].bombePresent();
            if (y!=longueur-1){
                mine[x+1][y+1].bombePresent();}
        }
    }

    /**
     * Méthode en bonus, il s'assure que la première case touchée n'explose pas
     * @param x coordonnée x de la case cliqué
     * @param y coordonnée y de la case cliqué
     */
    private void bonus (int x,int y){
        boolean pose=false;
        for (int i=0; i<largeur; i++){
            for (int t=0; t<longueur; t++){
                if (!mine[i][t].getBombe()){
                    //si la case n'est pas une bombe, il devient une bombe
                    mine[i][t].setValeur();
                    caseVoisins(i,t);
                    //il explique à ses cases voisins qu'il est une bombe
                    int compte=0;
                    //la même logique que caseVoisins et il compte le nombre de bombe voisin
                    if (x!=0){
                        if (y!=0){
                            if (mine[x-1][y-1].getBombe()) {
                                compte++;
                            }
                        }
                        if (mine[x-1][y].getBombe()){
                            compte++;
                        }
                        if (y!=longueur-1){
                            if (mine[x-1][y+1].getBombe()) {
                                compte++;
                            }
                        }
                    }
                    if (y!=0){
                        if (mine[x][y-1].getBombe()){
                            compte++;
                        }
                    }if (y!=longueur-1){
                        if (mine[x][y+1].getBombe()){
                            compte++;
                        }
                    }
                    if (x!=largeur-1){
                        if (y!=0){
                            if (mine[x+1][y-1].getBombe()){
                                compte++;
                            }
                        }
                       if ( mine[x+1][y].getBombe()){
                           compte++;
                       }
                        if (y!=longueur-1){
                            if (mine[x+1][y+1].getBombe()){
                                compte++;
                            }
                        }
                    }
                    //selon le nombre de mine voisins, il change la valeur de cette case cliquée
                    mine[x][y].changeValeur(compte);
                    pose=true;break;//la bombe est décalée, cette méthode peut finir
                }
            }
            if (pose){
                break;
            }
        }
    }
    /**
     * méthode privée qui va simplement afficher la mine et dépendamment du statut du jeu, le message qui vient avec.
     * (getAffichage est différent de getValeur, car la première retourne des d, x et B aussi alors que le deuxième retourne
     * seulement des chiffres).
     */
    private void afficher (){
        if (partieFini&& bombes!=0){//si la partie a fini et le nombre de bombe n'est pas à zéro (bombe explosé)
            for (int i=0; i<largeur; i++){
                for (int y=0; y<longueur; y++){
                    if (mine[i][y].getDrapeau()&&!mine[i][y].toucheBombe()){
                        //s'il y a un drapeau et la case n'est pas une bombe
                        mine[i][y].setDrapeau();//appele setDrapeau, qui met le drapeau à false (l'enlève)
                    }
                    mine[i][y].setOuvert();//met la case à ouvert
                    System.out.print(mine[i][y].getAffichage());//et il affiche la case
                }
                System.out.println();
            }
        }
        else {for (int i=0; i<largeur; i++){//sinon,
            for (int y=0; y<longueur; y++){//il affiche la case tout simplement.
                System.out.print(mine[i][y].getAffichage());
            }
            System.out.println();
        }}
        if (!partieFini){//si la parie est en cours, il affiche le nombre de bombe-drapeau
            System.out.println("Il reste "+ drapeau+ " bombes!");
        }if (partieFini){
            if (mortOuDecede==2){//si la partie est finie et la situation est =2 (perdu)
                System.out.println("Vous avez perdu! :(");
            }else {
                System.out.println("Vous avez gagné! :D");
            }
        }
    }

    /**
     * Méthode qui va lire la commande et changer les cases selon ça. Il pose des drapeaux et ouvre des cases (une seule
     * case ou plusieurs si c'est une case vide (0) correspondant au String demandé.
     * Cette méthode va aussi déterminer si la partie est finie (gagné/perdu).
     * @param donnees le String qui va être apperçu comme la commande.
     */
    private void interagir (String donnees){
        int x=0;int y=0;
        boolean drapeau=false;
        boolean mort=false;
        boolean chiffreIncorrect=false;
        String [] commande=donnees.split(" ");//sépare le string avec des espaces
        for (int i=0; i<commande.length; i++) {
            int a = 0;
            boolean chiffre = true;
            try {//essaie de convertir la commande en chiffre
                a = Integer.parseInt(commande[i]);
            } catch (NumberFormatException nfe) {//si c'est une lettre et non un chifre,
                chiffre = false;//il le note avec le booléen qui devient faux
            }
            if (chiffre) {//si la commande est un chiffre,
                x = a;//il note dans x, le int a
                if (drapeau){//si c'est une commande drapeau mais il y a moins de 2 chiffres, il y a erreur
                    if (commande.length!=3){
                    chiffreIncorrect=true;break;}
                }else if (commande.length!=2){//ou si c'est une commande normale mais avec des coordonnées manquantes
                    chiffreIncorrect=true;break;
                }
                try {
                    a = Integer.parseInt(commande[i + 1]);//remplace a par la suite du tableau String
                }catch (Exception e){
                    //si y est quelque chose qui ne compte pas comme un chiffre
                    chiffreIncorrect=true; break;
                }
                y = a;//et c'est y
                if (x > largeur || x < 0) {
                    chiffreIncorrect = true;
                    break;
                    //s'assurer que la commande n'induit pas arrayOutOfBound
                } else if (y > longueur || y < 0) {
                    chiffreIncorrect = true;
                    break;
                } else {
                    if (i != 0 && !drapeau) {
                        //si c'est la deuxième place du tableau et le drapeau est false, alors il y avait une lettre
                        //autre que d qui est écrit: ne pas réagir dans ce cas
                    }
                    if (drapeau) {
                        //si le drapeau est vrai, il ne réagit pas non plus.
                    } else {
                        if (!mine[x][y].getDrapeau()) {//si cette case n'a pas de drapeau
                            if (premier){//si c'est le premier clic
                                if (mine[x][y].getBombe()){//et que ça va exploser
                                bonus(x,y);//décale la bombe
                                }
                                premier=false;//et ce n'est plus le premier clic!
                            }
                            mine[x][y].setOuvert();//on ouvre cette case (avec x et y)
                            mort = mine[x][y].toucheBombe();//vérifie si cette case ouverte était fatale
                        } else {//si cette case a un drapeau et la commande est de l'ouvrir...
                            System.out.println("Le drapeau vous regarde d'un oeil menaçant...");
                        }
                    }
                    break;//arrête le for car on a le x et le y.
                }
            } else {//si c'est une lettre qui est d,
                if (!chiffreIncorrect){//si ce n'est pas un chiffre bizarre
                if (commande[i].equals("d") || commande[i].equals("D")) {
                    //alors c'est une commande drapeau
                    drapeau = true;
                }
            }
        }
        }
        if (!chiffreIncorrect) {
            if (drapeau) {
                if (mine[x][y].getOuvert() && !mine[x][y].getDrapeau()) {
                    System.out.println("Cette case est deja ouverte");
                } else {//si la case n'est pas ouverte
                    if (mine[x][y].getDrapeau()) {//si la case a déjà un drapeau
                        this.drapeau++;// le nombre de drapeau augmente (il diminue si on met un drapeau)
                        if (mine[x][y].toucheBombe()) {//et si cette case est une bombe
                            bombes++;
                        }//le compte de bombe augmente
                    } else if (!mine[x][y].getDrapeau()) {//si c'est une case sans drapeau
                        this.drapeau--;//il y a moins de drapeau à mettre
                        if (mine[x][y].toucheBombe()) {
                            bombes--;//si c'est une case bombe, le nombre de bombe restant diminue
                        }
                    }
                    mine[x][y].setDrapeau();//si la case avait un drapeau, il devient false. Sinon, il devient true.
                }
            }
            if (mort) {//si au final, on a ouvert une case bombe
                partieFini = true;
                mortOuDecede = 2;
            }if (mine[x][y].getValeur() == 0 && mine[x][y].getOuvert()) {
                    //si on a ouvert une case vide
                    if (!commande[0].equals("d") && !commande[0].equals("D")) {//et que ce n'est pas un drapeau
                        caseVide(x, y);//utilise la méthode pour ouvrir la chaine de cases autour
                        winOrLose();//et on vérifie si la partie est gagnée
                    }
                }else {
                winOrLose();
                //sinon, il faut juste vérifier la partie
            }
            afficher();
        }
    }
    /*
    En bref,si la commande était de 0 5 par exemple, le 0 serait convertit en int a en test. Sans erreur, le booléen chiffre
    serait vrai et et la valeur de a va devenir la coordonnée x, alors que le prochain chiffre du tableau (i+1) serait y.
    À ce point, x = 0 et y = 5 et du moment qu'il n'y a pas eu de cas spécial (lettre non-autorisé écrite), il va
    directement ouvrir cette case, et sortir du for. Ensuite, il vérifie si cette case retournait une bombe ou non.
    S'il retourne, la partie finit et le statut = 2 (perdu). Puis, si la case ouverte était vide (0), il utilise une
    méthode qui va ouvrir tous les cases autour et vérifie si on a gagné. Si aucun de ces if n'a été appelé, on
    vérifie juste si la partie est gagnée ou non.
     */

    /**
     * Méthode qui vérifie si la partie est gagnée
     */
    private void winOrLose (){
        int lesCases =largeur*longueur;
        //le nombre de case au total
        int compteur=0;//le compteur part à 0
        for (int i=0; i<largeur; i++){
            for (int t=0; t<longueur; t++){
                if (mine[i][t].getOuvert()){//si la case est ouverte
                    compteur++;
                }else if (mine[i][t].getDrapeau()){//ou si c'est une case avec drapeau
                    if (mine[i][t].getValeur()==666){//et que le drapeau cache une bombe
                        compteur++;
                    }
                }else if (mine[i][t].getValeur()==666){//ou, si c'est une case bombe
                    if (!mine[i][t].getOuvert()){//qui n'est pas ouverte
                        compteur++;
                    }
                }
            }
        }
        if (compteur==lesCases){//si au final tous les cases ont répondu aux if, on a gagné.
            partieFini=true;mortOuDecede=1;
        }
        /*
        Du moment que tous les cases sont ouvertes, la partie est gagné. Pour les bombes, soit que c'est un drapeau
        ou que c'est simplement fermé.
         */
    }

    /**
     * Méthode privée utilisé pour révéler les cases vides. Il va ouvrir les 8 cases à côté du case vide, et si ces 8
     * cases sont vides aussi il va reopérer avec cette fois une autre case vide comme noyau (voir la méthode)
     * Il est utilisé en boucle si besoin avec verify et raccourciOuverture.
     * @param x coordonnée x du case vide
     * @param y coordonnée y du case vide
     */
    private void caseVide (int x, int y){
        if (x!=0){//vérifie la ligne et colonne pour éviter les erreurs
            if (y!=0){
                raccourciOuverture(x-1,y-1);//examine la case en question
            }
            raccourciOuverture(x-1,y);
            if (y!=mine[0].length-1){
                raccourciOuverture(x-1,y+1);
            }
        }
        if (y!=0){
            raccourciOuverture(x,y-1);
        }if (y!=mine[0].length-1){
            raccourciOuverture(x,y+1);
        }
        if (x!=mine.length-1){
            if (y!=0){
                raccourciOuverture(x+1,y-1);}
            raccourciOuverture(x+1,y);
            if (y!=mine[0].length-1){
                raccourciOuverture(x+1,y+1);
            }
        }
    }

    /**
     * une méthode appelé dans raccourciOuverture, il détermine si la case est vide. Si oui, il relance caseVide avec
     * cette case.
     * @param a la Case qui est vérifié
     */
    private void verify (Case a){
        //si elle est vide, il recommence caseVide avec cette case. Ça s'enchaîne jusqu'à ce qu'il n'y a plus aucune case vide
        //autour.
        if (a.getValeur()==0){
            caseVide(a.getX(),a.getY());
        }
    }

    /**
     * comme son nom l'indique, c'est une méthode qui est un raccourci de deux commandes répétés 8 fois ou moins par case
     * Il ouvre la case si ce n'est pas encore fait, et vérifie si cette case est vide ou non.
     * Si la case est ouverte, il ne fait rien et caseVide continue.
     * @param x coordonnée x à ouvrir et vérifier
     * @param y coordonnée y à ouvrir et vérifier
     */
    private void raccourciOuverture (int x, int y){
        if (mine[x][y].getOuvert()){
            //ne fait rien si c'est ouvert
        }else if (mine[x][y].getDrapeau()){
            //et si c'est un drapeau
        }
        else {
            mine[x][y].setOuvert();
            //vérifie cette case
            verify(mine[x][y]);
        }
    }

    /**
     *l'équivalent de jouer en commande. Mis à par les boutons, il traduit les clics en lettre et chiffre, pour
     * s'opérer en commande et remet la grille de case en immage.
     * @param gridPane le gridPane qui contient les images de démineur
     * @param root la base de la fenêtre, un BorderPane
     * @param indice le texte affiché en bas, sur le nombre de bombe
     * @param abandon le bouton abandonner qui montre la réponse du démineur
     * @param recommencer bouton qui recommence le jeu tel quel
     * @param c1 premier choix d'un nouveau jeu, c'est un menuItem qui fait partie de np (choix de 1 bombe)
     * @param np bouton qui crée un nouveau jeu de démineur
     * @param c2 deuxième choix du bouton np, choix de 5 bombes
     * @param c3 menuItem de 15 bombes
     * @param c4 menuItem de 30 bombes
     * @param c5 choix de 200 bombes
     */
    protected void jouerEnFX (GridPane gridPane, BorderPane root, Text indice, Button abandon, Button recommencer, Button np,
                               MenuItem c1,MenuItem c2,MenuItem c3, MenuItem c4,MenuItem c5) {

        commencer();
        grille=new ImageView[longueur][largeur];
        for (int x = 0; x < longueur; x++) {
            for (int y = 0; y < largeur; y++) {
                int finalX = x;
                int finalY = y;
                //commencer par afficher le démineur complètement fermé. Les images du gridPane sont tous fermés, mais
                //le tableau de case contient ses valeurs véritables.
                affiche(x, y, gridPane, "ferme.png");
                abandon.setOnAction((e) ->{
                    //si le bouton d'abandonné est cliqué
                    montrer(gridPane);
                });
                recommencer.setOnAction((e) ->{
                    //si on recommence, tout est réinitialisé, l'indice est mis à jour
                    restart();
                    indice.setText("Encore "+drapeau+" mines!");
                    changer(gridPane);
                    //on refait le même programme, mais quelques lignes de code qui génère le démineur.
                    memeFX(gridPane,root,indice);
                });
                grille[x][y].setOnMouseClicked((MouseEvent e) -> {
                    //si une image est cliqué, la méthode correspondante est appelée
                    deroulement(finalY,finalX,gridPane,root,indice,e);
                });
                //au début, si on clique sur nouveau jeu, le nombre de bombe est la même
                nouveauBombe=finalBombes;
                c1.setOnAction(eventc1 -> {
                    //à partir d'ici, il remplace le nombre de bombe selon la boite de choix.
                    nouveauBombe=1;
                });
                c2.setOnAction(eventc2 -> {
                    nouveauBombe=5;
                });
                c3.setOnAction(eventc3 -> {
                    nouveauBombe=15;
                });
                c4.setOnAction(eventc4 -> {
                    nouveauBombe=30;
                });
                c5.setOnAction(eventc5 -> {
                    nouveauBombe=200;
                });
                np.setOnAction((event) -> {
                    //et si le bouton nouveau jeu est cliqué, il recrée un démineur, cette fois avec
                    //un nombre de bombe différent.
                    Demineur demineur1=new Demineur(longueur,largeur,nouveauBombe);
                    indice.setText("Encore "+demineur1.getDrapeau()+" mines!");
                    demineur1.jouerEnFX(gridPane,root,indice,abandon,recommencer,np,c1,c2,c3,c4,c5);
                });
            }
        }
    }

    /**
     *La méthode appelée pour recommencer le même jeu. C'est en générale les mêmes code, mais il ne génère plus
     * de nouveau démineur et il n'y a plus de réaction aux boutons.
     * @param gridPane les mêmes éléments expliqués en haut.
     * @param root
     * @param indice
     */
    private void memeFX (GridPane gridPane, BorderPane root, Text indice){
        afficher();
        indice.setText("Encore "+drapeau+" mines!");
        for (int x = 0; x < longueur; x++) {
            for (int y = 0; y < largeur; y++) {
                int finalX = x;
                int finalY = y;
                affiche(x, y, gridPane, "ferme.png");
                grille[x][y].setOnMouseClicked((MouseEvent e) -> {
                    deroulement(finalY,finalX,gridPane,root,indice,e);
                });

            }
        }
    }

    /**
     *la méthode qui s'opère après qu'une image est cliquée. Il lit si le clic est droite ou gauche, et met un drapeau
     * ou ouvre la case.
     * @param finalY coordonnée y de la case touchée
     * @param finalX coordonnée x
     * @param gridPane le tableau d'image, un gridPane
     * @param root la base de la fenêtre
     * @param indice le texte affichée en bas du démineur
     * @param e l'évènement e s'agit du clic de souris
     */
    private void deroulement (int finalY,int finalX,GridPane gridPane, BorderPane root, Text indice,MouseEvent e){
            if (e.getButton()==MouseButton.PRIMARY) {
                //si c'est un clic gauche
                if (!partieFini) {
                    //il transforme y et x en String, avec une espace entre les deux. C'est la même chose que de
                    //l'écrire manuellement en fait.
                    mots = String.valueOf(finalY) + " " + String.valueOf(finalX);
                    interagir(mots);
                    changer(gridPane);//convertir le tableau de case en image
                    //remet les images, le GridPane au milieu de la fenêtre.
                    root.setCenter(gridPane);
                    indice.setText("Encore " + drapeau + " mines!");
                    if (!partieFini) {
                        //si le jeu ne finit pas, il entre dans un loop ou la méthode loop s'utilise infiniment
                        //jusqu'à ce que le jeu finisse
                        loop(gridPane, root, indice);
                    }
                }
            }else if (e.getButton()==MouseButton.SECONDARY){
                //le clic droit est pour le drapeau
                if (!partieFini) {
                    //donc il fait la même chose qu'un clic gauche, mais avec un d au début.
                    mots = "d "+String.valueOf(finalY) + " " + String.valueOf(finalX);
                    interagir(mots);
                    changer(gridPane);
                    root.setCenter(gridPane);
                    indice.setText("Encore " + drapeau + " mines!");
                    if (!partieFini) {
                        loop(gridPane, root,indice);}
                }
            }if (partieFini){
                if (mortOuDecede==2){
                    //le jeu qui fini: si mortouDecede est 2, alors c'est une explosion. Sinon, c'est une victoire.
                    indice.setText(" Vous avez perdu :(");
                    //rip
                }else {
                    indice.setText("Vous avez gagné! :D");
                }

            }
    }

    /**
     *la méthode qui vient tout reinitialiser, y inclus les composants du javaCmd.
     */
    private void restart (){
        for (int i=0; i<mine.length; i++){
            for (int j=0; j<mine[0].length; j++){
                //il ferme les cases, enlève les drapeaux et si la case a explosé, l'explosion est annulé.
                mine[i][j].setFerme();
                mine[i][j].fermeDrapeau();
                mine[i][j].cacherBombe();
            }
        }
        //le nombre de bombe redevient finalBombes qui est une constante, le drapeau est réinitialisé ainsi que les
        //variables qui décident du game over.
        bombes=finalBombes;drapeau=bombes;
        partieFini=false;mortOuDecede=0;premier=true;
    }

    /**
     *Méthode qui convertit la case de cmd en image.
     * Attention! Il montre la réponse du jeu et non le jeu normal.
     * @param gridPane le tableau d'image qu'il va modifier
     */
    private void montrer (GridPane gridPane) {
        String url;
        for (int x = 0; x < mine.length; x++) {
            for (int y = 0; y < mine[0].length; y++) {
                switch (mine[x][y].getValeur()) {
                    //switch qui détermine l'url, selon sa valeur.
                    case 1: url = "1.png";break;
                    case 2: url = "2.png";break;
                    case 3: url = "3.png";break;
                    case 4: url = "4.png";break;
                    case 5: url = "5.png";break;
                    case 6: url = "6.png";break;
                    case 7: url = "7.png";break;
                    case 8: url = "8.png";break;
                    case 666: url = "bombe.png";break;
                    default: url = "0.png";
                }
                affiche(y, x, gridPane, url);
            }
        }
    }

    /**
     * Environ le même code que deroulement, mais il se rappelle.
     * @param gridPane les mêmes variables que tout ce qui est expliqué en haut.
     * @param root
     * @param indice
     */
    private void loop (GridPane gridPane,BorderPane root,Text indice){
        for (int x=0; x<longueur; x++){
            for (int y=0; y<largeur; y++){
                int finalX = x;
                int finalY = y;
                grille[x][y].setPickOnBounds(true);
                grille[x][y].setOnMouseClicked((MouseEvent e) -> {
                    if (e.getButton()==MouseButton.PRIMARY) {
                        if (!partieFini) {
                            mots = String.valueOf(finalY) + " " + String.valueOf(finalX);
                            interagir(mots);
                            changer(gridPane);
                            root.setCenter(gridPane);
                            indice.setText("Encore "+drapeau+" mines!");
                            if (!partieFini) {
                                //Ici! C'est ici qu'il s'appelle lui-même!
                                //pour rester dans le jeu et continuer à réagir aux clics, le loop continue tant que
                                //la parie n'est pas fini.
                                //Bienvenue à Re:zero.
                                loop(gridPane, root,indice);
                            }
                        }
                    }else if (e.getButton()==MouseButton.SECONDARY){
                            if (!partieFini) {
                                mots = "d "+String.valueOf(finalY) + " " + String.valueOf(finalX);
                                interagir(mots);
                                changer(gridPane);
                                root.setCenter(gridPane);
                                indice.setText("Encore "+drapeau+" mines!");
                            }
                        if (!partieFini){
                            loop(gridPane,root,indice);
                        }
                        }
                    if (partieFini){
                        if (mortOuDecede==2){
                            indice.setText("Vous avez perdu! :(");
                        }else {
                            indice.setText("Vous avez gagne! :)");
                        }

                    }
                });
            }
        }
    }

    /**
     * La méthode qui va remplir le tableau grille d'image, et l'ajouter au gridpane.
     * @param x x et y sont les coordonnées de la grille ou l'imageView va rentrer
     * @param y
     * @param gridPane le gridPane ou la grille s'ajoute.
     * @param url l'url pour prendre l'image.
     */
    private void affiche (int x,int y, GridPane gridPane,String url){
        Image image=new Image(url);
        grille[x][y]=new ImageView(image);
        grille[x][y].setPickOnBounds(true);
        gridPane.add(grille[x][y],x,y);
    }

    /**
     * Méthode qui vient changer l'état tel quel de la case en image: si la case est fermée, l'image est fermée aussi
     * @param gridPane
     */
    private void changer (GridPane gridPane) {
        String url;
        for (int x = 0; x < mine.length; x++) {
            for (int y = 0; y < mine[0].length; y++) {
                switch (mine[x][y].getAffichage()) {
                    case '0':
                        url = "0.png";break;
                    case '1': url = "1.png";break;
                    case '2': url = "2.png";break;
                    case '3': url = "3.png";break;
                    case '4': url = "4.png";break;
                    case '5': url = "5.png";break;
                    case '6': url = "6.png";break;
                    case '7': url = "7.png";break;
                    case '8': url = "8.png";break;
                    case 'B': url = "boom.png";break;
                    case 'x': url = "bombe.png";break;
                    case 'd': url = "drapeau.png";break;
                    default: url = "ferme.png";
                }
                if (partieFini) {
                    if (mine[x][y].getDrapeau() && mine[x][y].getAffichage() == 'x') {
                        //si l'affichage est x, mais il y avait un drapeau, alors l'image est un drapeau!
                        url = "drapeau.png";
                    }
                }
                affiche(y, x, gridPane, url);
            }}
    }

    /**
     *retourne le nombre de drapeau
     * @return drapeau
     */
    protected int getDrapeau(){return drapeau;}
}
