package com.sliit.ayushada_server.modules.Prescription_and_Verification_Management.Exeption;

<<<<<<< Updated upstream
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPrescriptionException extends RuntimeException {
    public InvalidPrescriptionException(String message) {
        super(message);
    }
}
=======
public class InvalidPrescriptionException {
}
>>>>>>> Stashed changes
