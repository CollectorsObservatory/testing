/**
 * \file Reseau.cpp
 * \brief Définition de l'interface pour un reseau informatique
 * \author TE
*/
#include <algorithm>
#include "Reseau.h"
#include "ContratException.h"

using namespace std;

/**
 * \brief Constructeur par défaut
 * \post Un réseau vide est instancié.
 */
Reseau::Reseau() { };

/**
 * \brief Constructeur à partir d'un fichier en entrée.
 * \param[in, out] p_fichierEntree fichierEntree un flux sur un fichier d'entrée
 * \pre Il y a assez de mémoire pour charger tous les routeur de la liste.
 * \pre Le fichier p_fichierEntree est ouvert correctement.
 * \post Le fichier p_fichierEntree n'est pas fermé par la fonction.
 */
Reseau::Reseau(std::ifstream& p_fichierEntree) {
    string nom;
    string adresseIP;
    string localisation;
    int numero = 1;
    vector<string> tabNomRouteurs;
    bool sentinelle = false;

    while (!p_fichierEntree.eof() && !sentinelle) {
        getline(p_fichierEntree, nom);
        if (nom == "$") {
            sentinelle = true;
        } else {
            getline(p_fichierEntree, adresseIP);
            getline(p_fichierEntree, localisation);
            Routeur lRouteur(nom, adresseIP, localisation);
            m_graphe.ajouterSommet(numero, lRouteur);
            ++numero;
            tabNomRouteurs.push_back(nom);
        }
    }

    int tempsTransmission;
    string nomD;
    int indiceSource;
    int indiceDestination;
    char buff[255];
    vector<string>::iterator location;
    while (!p_fichierEntree.eof()) {
        p_fichierEntree.getline(buff, 100);
        nom = buff;
        location = find(tabNomRouteurs.begin(), tabNomRouteurs.end(), nom);
        indiceSource = location - tabNomRouteurs.begin();
        p_fichierEntree.getline(buff, 100);
        nomD = buff;
        location = find(tabNomRouteurs.begin(), tabNomRouteurs.end(), nomD);
        indiceDestination = location - tabNomRouteurs.begin();
        p_fichierEntree >> tempsTransmission;
        p_fichierEntree.ignore();
        m_graphe.ajouterArc(indiceSource + 1, indiceDestination + 1, tempsTransmission);
    }

    cout << "******** Graphe lu ********" << std::endl;
    cout << m_graphe;
    cout << "***************************" << std::endl;
}

/**
 * \brief Affiche une liste de routeurs du réseau à l'écran.
 */
void Reseau::afficherRouteurs(std::vector<Routeur>& p_vRouteurs) {
    if (p_vRouteurs.empty()) {
        cout << "La liste est vide\n";
        return;
    }

    for (unsigned int i = p_vRouteurs.size(); i > 0; i--) {
        cout << p_vRouteurs[i - 1] << endl;
    }
}

/**
 * \brief Affiche la liste de tous les routeurs du réseau.
 */
void Reseau::afficherRouteurs() {
    if (m_graphe.estVide()) {
        cerr << "[DEBUG] Le graphe est vide, impossible d'afficher les routeurs." << endl;
    }
    PRECONDITION(!m_graphe.estVide());
    vector<Routeur> lListeRouteur = m_graphe.listerEtiquetteSommets();
    afficherRouteurs(lListeRouteur);
}

/**
 * \brief Renvoie tous les routeurs enregistrés dans le réseau.
 */
void Reseau::getRouteurs(vector<Routeur>& p_vRouteurs) const {
    p_vRouteurs = m_graphe.listerEtiquetteSommets();
}

/**
 * \brief Vérifie si tous les routeurs du réseau sont accessibles.
 */
bool Reseau::routeursAccessibles() {
    if (m_graphe.estVide()) {
        cerr << "[DEBUG] Le graphe est vide, il n'est pas fortement connexe." << endl;
    }
    PRECONDITION(!m_graphe.estVide());
    return m_graphe.estFortementConnexe();
}

/**
 * \brief Renvoie une liste des routeurs critiques du réseau.
 */
vector<Routeur> Reseau::routeursCritiques() {
    vector<Routeur> pointsCritiques;
    m_graphe.getPointsArticulation(pointsCritiques);
    return pointsCritiques;
}

/**
 * \brief Détermine le chemin minimal entre deux routeurs.
 */
vector<Routeur> Reseau::determinerMinParcours(const Routeur& p_routeurOrigine,
                                              const Routeur& p_routeurDestination, int& nbSec) {
    if (!m_graphe.sommetExiste(m_graphe.getNumeroSommet(p_routeurOrigine))) {
        cerr << "[DEBUG] Le routeur d'origine (" << p_routeurOrigine.reqNom()
             << ") n'existe pas dans le graphe." << endl;
    }
    if (!m_graphe.sommetExiste(m_graphe.getNumeroSommet(p_routeurDestination))) {
        cerr << "[DEBUG] Le routeur de destination (" << p_routeurDestination.reqNom()
             << ") n'existe pas dans le graphe." << endl;
    }
    PRECONDITION(m_graphe.sommetExiste(m_graphe.getNumeroSommet(p_routeurOrigine)));
    PRECONDITION(m_graphe.sommetExiste(m_graphe.getNumeroSommet(p_routeurDestination)));

    int numeroOrigine = m_graphe.getNumeroSommet(p_routeurOrigine);
    int numeroDestination = m_graphe.getNumeroSommet(p_routeurDestination);

    vector<Routeur> cheminRouteurs;
    nbSec = m_graphe.dijkstra(p_routeurOrigine, p_routeurDestination, cheminRouteurs);

    return cheminRouteurs;
}
