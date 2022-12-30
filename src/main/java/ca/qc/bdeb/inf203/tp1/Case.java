package ca.qc.bdeb.inf203.tp1;

public class Case {
    private int valeur=0;
    private boolean drapeau;
    private boolean ouvert=false;
    private boolean gameOver=false;
    private int x;
    private int y;

    /**
     * Constructeur de la case: il faut avant tout que la case aille des coordonnées pour être identifié.
     * @param x coordonnée x
     * @param y coordonnée y
     */
    protected Case (int x, int y){
        this.x=x;
        this.y=y;
    }
    /**
     * méthode appelée seulement pour mettre une bombe, il ne va même pas demander la valeur à mettre.
     */
    protected void setValeur (){
        valeur=666;
    }

    /**
     * retourne la valeur de la Case. Il n'est pas utilisé pour l'affichage, juste pour vérifier le genre de la case (chiffre
     * ou bombe)
     * @return retourne la valeur
     */
    protected int getValeur (){
        return valeur;
    }

    /**
     * méthode qui vérifie si c'est une case bombe, retourne vrai si oui.
     * @return retourne true ou false selon la case.
     */
    protected boolean toucheBombe (){
        if (valeur==666){
            gameOver=true;
            return gameOver;
        }else {
            return false;
        }
    }

    /**
     *
     * @return il retourne si cette case est une mine ou non en booléen
     */
    protected boolean getBombe (){
        if (valeur==666){
            return true;
        }else {
            return false;
        }
    }

    /**
     * Il change la valeur de la case directement,appelé seulement lors du bonus
     * @param i la valeur qui va remplacer
     */
    protected void changeValeur (int i){
        valeur=i;
    }

    /**
     * Cache la bombe, le gameOver redevient faux
     */
    protected void cacherBombe (){
        gameOver=false;
    }

    /**
     *
     * @return retourne le coordonnéee x de la case
     */
    protected int getX (){return x;}

    /**
     * retourne le coordonnéee y de la case
     * @return
     */
    protected int getY (){return y;}

    /**
     * méthode qui retourne la case à afficher: des chiffres normalement, un 'x' si c'est une bombe, un 'd' si le booléen
     * drapeau est vrai et un 'B' si c'est la case avec true pour gameOver (signifie que c'est la case qui a tué le joueur)
     * Il retourne ' ' si la case n'est pas ouverte, aussi.
     * @return retourne un charactère qui serait affiché en tant que mine.
     */
    protected char getAffichage (){
        if (ouvert){
            if (valeur==666){
                if (gameOver&&!drapeau){
                    return 'B';
                }
                else {
                    return 'x';
                }
            }else {
                char a = Character.forDigit(valeur, 10);
                return a;
            }
        }
        else if (drapeau){
            return 'd';
        }
        else {
            return ' ';
        }
    }

    /**
     * méthode appelée pour les 8 cases à côté d'une bombe, la valeur augmente de 1 SI ce n'est pas une case de bombe aussi.
     */
    protected void bombePresent (){
        if (valeur!=666){
            valeur++;}
    }

    /**
     * Changer le booléen ouvert en vrai simplement, pour dire au programme que cette case est ouverte.
     */
    protected void setOuvert (){
        ouvert=true;
    }

    /**
     * Ferme la case
     */
    protected void setFerme (){ouvert=false;}

    /**
     * Méthode qui change le status de drapeau: s'il est déjà drappé, alors il enlève le drapeau (boolean devient false) et
     * vice-versa.
     */
    protected void setDrapeau (){
        if (!drapeau) {
            drapeau = true;
        }
        else {
            drapeau=false;
        }
    }

    /**
     * Enlève le drapeau de la case
     */
    protected void fermeDrapeau (){
        drapeau=false;
    }

    /**
     * méthode qui retourne si la case a un drapeau ou non, utilisé pour l'affichage et pour déterminer le compte de bombe
     * à afficher et le vrai compte.
     * @return retourne true ou false selon le booléen drapeau.
     */
    protected boolean getDrapeau (){
        return drapeau;
    }

    /**
     * Retourne si la case est ouverte ou non. Attention, si la case a un drapeau alors elle est considérée comme ouverte.
     * @return retourne true ou false selon le booléen ouvert.
     */
    protected boolean getOuvert (){
            return ouvert;
    }

}
