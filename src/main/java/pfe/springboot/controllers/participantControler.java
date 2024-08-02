package pfe.springboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pfe.springboot.entities.participant;
import pfe.springboot.services.userServicesinter;
import pfe.springboot.services.participant.participantserviceinter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/participant")
public class participantControler {
    @Autowired
    userServicesinter userServicesinter;

    @Autowired
    participantserviceinter participantServicesinter;


    @PostMapping("/add")
    public ResponseEntity<Object> addUser(@RequestBody participant user) {
        try {
            Optional<participant> existingUser = userServicesinter.findByEmail(user.getEmail());

            if (existingUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Un participant avec cet e-mail existe déjà.");
            }

            userServicesinter.addUser(user);
            return ResponseEntity.ok(new SuccessResponse("Participant ajouté avec succès."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de l'ajout du participant."));
        }
    }

    // Response classes
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class SuccessResponse {
        private String message;

        public SuccessResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody participant user) {
        Optional<participant> existingUser = userServicesinter.findByEmail(user.getEmail());

        if (existingUser.isPresent() && existingUser.get().getPassword().equals(user.getPassword())) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Connexion réussie.");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Email ou mot de passe incorrect.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping(value = "/listeParticipant")
    public List<participant> getAllUser() {
        List<participant> liste = userServicesinter.getAllUser();
        return liste;
    }
    /*
     * @DeleteMapping(value = "/deleteuser")
     * public void deleteuser(){
     * userServicesinter.deleteuser(user );
     * }
     */


    @DeleteMapping(value = "/del/{id_participant}")
    public void deleteparticipant(@PathVariable Long id_participant) {
        participantServicesinter.deleteparticipant(id_participant);
    }

    @PutMapping(value = "/modifierparticipant/{id_participant}")
    public participant updateprof(@PathVariable Long id_participant, @RequestBody participant participantmodifier) {
        return participantServicesinter.updatePart(id_participant, participantmodifier);
    }


    // @PutMapping("/activateParticipant/{id}")
    // public ResponseEntity<participant> activateParticipant(@PathVariable Long id) {
    //     participant updatedParticipant = participantServicesinter.activateParticipant(id);
    //     return ResponseEntity.ok(updatedParticipant);
    // }


    // @PutMapping("/deactivateParticipant/{id}")
    // public ResponseEntity<participant> deactivateParticipant(@PathVariable Long id) {
    //     participant updatedParticipant = participantServicesinter.deactivateParticipant(id);
    //     return ResponseEntity.ok(updatedParticipant);
    // }
}