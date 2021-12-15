package tech.bgdigital.online.payment.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.bgdigital.online.payment.models.entity.CallFund;
import tech.bgdigital.online.payment.models.entity.Transaction;
import tech.bgdigital.online.payment.models.enumeration.State;
import tech.bgdigital.online.payment.models.repository.TransactionRepository;
import tech.bgdigital.online.payment.services.http.response.HttpResponseApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/transactions")
@Api(tags = "Admin transaction",description = ".")
public class TransactionController {
    final TransactionRepository transactionRepository;
    final
    HttpResponseApiInterface httpResponseApi;

    public TransactionController(TransactionRepository transactionRepository, HttpResponseApiInterface httpResponseApi) {
        this.transactionRepository = transactionRepository;
        this.httpResponseApi = httpResponseApi;
    }

    @GetMapping("/paginate")
    @ApiOperation(value = "Liste des transactions paginées")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<Transaction> listType = new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);
            Page<Transaction> pageTuts;
            pageTuts = transactionRepository.findAllByStateIsNot(State.DELETED, paging);
            listType = pageTuts.getContent();
            Map<String, Object> response = new HashMap<>();
            response = httpResponseApi.paginate(listType, pageTuts);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            return new ResponseEntity<>(httpResponseApi.response(null, 500, true,"Ok"), HttpStatus.OK);
        }
    }

    @GetMapping("")
    @ApiOperation(value = "Liste des transactions")
    public Map<String, Object> index() {
        try {
            return httpResponseApi.response(transactionRepository.findAllByStateNot(State.DELETED), HttpStatus.CREATED.value(), false, "Données disponibles");
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());

        }
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Voir details transactions paginées")
    public Map<String, Object> show(@PathVariable() Integer id) {
        try {

            Transaction transaction = transactionRepository.findByIdAndStateNot(id, State.DELETED);
            if (transaction != null) {
                return httpResponseApi.response(transaction, HttpStatus.CREATED.value(), false, "Donnée disponible.");
            } else {
                return httpResponseApi.response(null, HttpStatus.NOT_FOUND.value(), true, "Cette région n'existe pas.");
            }
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }
    }


    /**
     * Desactiver etat a 0
     */
    @GetMapping("/state/{id}")
    @Transactional
    @ApiOperation(value = "Changer etat des transactions paginées")
    public Map<String, Object> activeDesactiveEtat(@PathVariable Integer id) {
        String message = "";
        if (id == null) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "1 ou plusieurs paramètres manquants");
        }
        try {

            Transaction transactionExist = transactionRepository.findByIdAndStateNot(id, State.DELETED);
            if ((transactionExist) == null) {
                return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "Cet Objet n'existe pas.");
            }

            if (transactionExist.getState().equals(State.ACTIVED)) {
                transactionExist.setState(State.ACTIVED);
                message = "etat activé avec succéss";

            } else {
                transactionExist.setState(State.DISABLED);
                message = "etat desactivé avec succéss";
            }

            transactionRepository.save(transactionExist);
            return httpResponseApi.response(transactionExist, HttpStatus.CREATED.value(), false, message);
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }
    }
}
