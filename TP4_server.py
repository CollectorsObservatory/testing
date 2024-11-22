"""
GLO-2000 Travail pratique 4 - Serveur
Noms et numéros étudiants:
- Alice Dupuis (12345678)
- Bob Tremblay (87654321)
- Charlie Martin (11223344)
"""

import hashlib
import json
import os
import select
import socket
import sys

import glosocket
import gloutils


class Server:
    """Serveur mail @glo2000.ca."""

    def __init__(self) -> None:
        """
        Prépare le socket du serveur _server_socket
        et le met en mode écoute.

        Prépare les attributs suivants:
        - _client_socs une liste des sockets clients.
        - _logged_users un dictionnaire associant chaque
          socket client à un nom d'utilisateur.

        S'assure que les dossiers de données du serveur existent.
        """
        self._server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        try:
            self._server_socket.bind(("", gloutils.APP_PORT))
            self._server_socket.listen()
            print(f"Le serveur est en écoute sur le port {gloutils.APP_PORT}")
        except socket.error as e:
            print(f"Erreur lors de la configuration du serveur : {e}")
            sys.exit(1)

        self._client_socs = []
        self._logged_users = {}

        # Vérification des dossiers de données
        os.makedirs(gloutils.SERVER_DATA_DIR, exist_ok=True)
        os.makedirs(os.path.join(gloutils.SERVER_DATA_DIR, gloutils.SERVER_LOST_DIR), exist_ok=True)

    def cleanup(self) -> None:
        """Ferme toutes les connexions résiduelles."""
        print("Nettoyage des connexions...")
        for client_soc in self._client_socs:
            client_soc.close()
        self._server_socket.close()

    def _accept_client(self) -> None:
        """Accepte un nouveau client."""
        client_soc, _ = self._server_socket.accept()
        self._client_socs.append(client_soc)
        print("Nouveau client connecté.")

    def _remove_client(self, client_soc: socket.socket) -> None:
        """Retire le client des structures de données et ferme sa connexion."""
        if client_soc in self._logged_users:
            print(f"Déconnexion : {self._logged_users[client_soc]}")
            del self._logged_users[client_soc]
        if client_soc in self._client_socs:
            self._client_socs.remove(client_soc)
        client_soc.close()
        print("Client déconnecté.")

    def _create_account(self, client_soc: socket.socket, payload: gloutils.AuthPayload) -> gloutils.GloMessage:
        """Crée un compte à partir des données fournies."""
        username = payload["username"]
        password = payload["password"]

        if not (3 <= len(username) <= 20 and username.isalnum()):
            return gloutils.GloMessage(header=gloutils.Headers.ERROR, payload={"error_message": "Nom d'utilisateur invalide."})

        if len(password) < 10 or not any(c.isdigit() for c in password) or not any(c.isupper() for c in password):
            return gloutils.GloMessage(header=gloutils.Headers.ERROR, payload={"error_message": "Mot de passe invalide."})

        user_dir = os.path.join(gloutils.SERVER_DATA_DIR, username)
        if os.path.exists(user_dir):
            return gloutils.GloMessage(header=gloutils.Headers.ERROR, payload={"error_message": "Utilisateur déjà existant."})

        os.makedirs(user_dir)
        hashed_password = hashlib.sha512(password.encode()).hexdigest()
        with open(os.path.join(user_dir, gloutils.PASSWORD_FILENAME), "w") as f:
            f.write(hashed_password)

        self._logged_users[client_soc] = username
        print(f"Compte créé pour : {username}")
        return gloutils.GloMessage(header=gloutils.Headers.OK)

    def _login(self, client_soc: socket.socket, payload: gloutils.AuthPayload) -> gloutils.GloMessage:
        """Connecte un utilisateur existant."""
        username = payload["username"]
        password = payload["password"]

        user_dir = os.path.join(gloutils.SERVER_DATA_DIR, username)
        if not os.path.exists(user_dir):
            return gloutils.GloMessage(header=gloutils.Headers.ERROR, payload={"error_message": "Utilisateur inexistant."})

        with open(os.path.join(user_dir, gloutils.PASSWORD_FILENAME), "r") as f:
            stored_password = f.read().strip()

        if hashlib.sha512(password.encode()).hexdigest() != stored_password:
            return gloutils.GloMessage(header=gloutils.Headers.ERROR, payload={"error_message": "Mot de passe incorrect."})

        self._logged_users[client_soc] = username
        print(f"Connexion réussie pour : {username}")
        return gloutils.GloMessage(header=gloutils.Headers.OK)

    def _logout(self, client_soc: socket.socket) -> None:
        """Déconnecte un utilisateur."""
        username = self._logged_users.pop(client_soc, None)
        if username:
            print(f"Utilisateur déconnecté : {username}")
        else:
            print("Aucun utilisateur associé à ce socket.")

    def _send_email(self, payload: gloutils.EmailContentPayload) -> gloutils.GloMessage:
        """Envoi un email interne."""
        sender = payload["sender"]
        destination = payload["destination"]

        if not destination.endswith("@glo2000.ca"):
            return gloutils.GloMessage(header=gloutils.Headers.ERROR, payload={"error_message": "Destinataire externe non supporté."})

        destination_username = destination.split("@")[0]
        user_dir = os.path.join(gloutils.SERVER_DATA_DIR, destination_username)

        if not os.path.exists(user_dir):
            lost_dir = os.path.join(gloutils.SERVER_DATA_DIR, gloutils.SERVER_LOST_DIR)
            os.makedirs(lost_dir, exist_ok=True)
            lost_email_file = os.path.join(lost_dir, f"{gloutils.get_current_utc_time().replace(':', '_')}.json")
            with open(lost_email_file, "w") as f:
                json.dump(payload, f)
            return gloutils.GloMessage(header=gloutils.Headers.ERROR, payload={"error_message": "Destinataire inconnu. Courriel placé dans LOST."})

        email_file = os.path.join(user_dir, f"{gloutils.get_current_utc_time().replace(':', '_')}.json")
        with open(email_file, "w") as f:
            json.dump(payload, f)

        print(f"Courriel envoyé : {sender} -> {destination}")
        return gloutils.GloMessage(header=gloutils.Headers.OK)

    def _get_email_list(self, client_soc: socket.socket) -> gloutils.GloMessage:
        """Récupère la liste des emails d'un utilisateur."""
        username = self._logged_users.get(client_soc, None)
        if not username:
            return gloutils.GloMessage(header=gloutils.Headers.ERROR, payload={"error_message": "Utilisateur non connecté."})

        user_dir = os.path.join(gloutils.SERVER_DATA_DIR, username)
        if not os.path.exists(user_dir):
            return gloutils.GloMessage(header=gloutils.Headers.ERROR, payload={"error_message": "Aucun courriel trouvé."})

        email_list = sorted(os.listdir(user_dir), reverse=True)
        emails = []
        for email_file in email_list:
            with open(os.path.join(user_dir, email_file), "r") as f:
                email = json.load(f)
                emails.append(email)

        return gloutils.GloMessage(header=gloutils.Headers.OK, payload={"emails": emails})

    def run(self):
        """Point d'entrée du serveur."""
        while True:
            readable, _, _ = select.select([self._server_socket] + self._client_socs, [], [])
            for soc in readable:
                if soc == self._server_socket:
                    self._accept_client()
                else:
                    try:
                        message = json.loads(glosocket.recv_mesg(soc))
                        print(f"Message reçu : {message}")
                        header = gloutils.Headers(message["header"])
                        if header == gloutils.Headers.AUTH_REGISTER:
                            response = self._create_account(soc, message["payload"])
                        elif header == gloutils.Headers.AUTH_LOGIN:
                            response = self._login(soc, message["payload"])
                        elif header == gloutils.Headers.AUTH_LOGOUT:
                            self._logout(soc)
                            response = gloutils.GloMessage(header=gloutils.Headers.OK)
                        elif header == gloutils.Headers.EMAIL_SENDING:
                            response = self._send_email(message["payload"])
                        elif header == gloutils.Headers.INBOX_READING_REQUEST:
                            response = self._get_email_list(soc)
                        else:
                            response = gloutils.GloMessage(header=gloutils.Headers.ERROR, payload={"error_message": "Action non supportée."})
                        glosocket.snd_mesg(soc, json.dumps(response))
                    except glosocket.GLOSocketError:
                        self._remove_client(soc)


def _main() -> int:
    server = Server()
    try:
        server.run()
    except KeyboardInterrupt:
        server.cleanup()
    return 0


if __name__ == '__main__':
    sys.exit(_main()) 