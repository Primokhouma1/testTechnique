package tech.bgdigital.online.payment.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import tech.bgdigital.online.payment.models.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

//    public User register(User user) {
//        // Hachage du mot de passe
//        // Sauvegarde de l'utilisateur
//    }
//
//    public String login(String username, String password) {
//        // Vérification des informations d'identification
//        // Génération et retour du JWT
//    }
}
