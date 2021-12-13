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
import tech.bgdigital.online.payment.exceptions.NotDeleteEntityException;
import tech.bgdigital.online.payment.models.entity.AccountStatement;
import tech.bgdigital.online.payment.models.enumeration.State;
import tech.bgdigital.online.payment.models.repository.AccountStatementRepository;
import tech.bgdigital.online.payment.services.http.response.HttpResponseApiInterface;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/account-statement")
@Api(tags = "Admin account statement",description = ".")
public class AccountStatementController {
    final AccountStatementRepository accountStatementRepository;
    final
    HttpResponseApiInterface httpResponseApi;
    public AccountStatementController(AccountStatementRepository accountStatementRepository, HttpResponseApiInterface httpResponseApi) {
        this.accountStatementRepository = accountStatementRepository;
        this.httpResponseApi = httpResponseApi;
    }

    @GetMapping("/paginate")
    @ApiOperation(value = "Liste des relevés de compte paginés")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<AccountStatement> listType = new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);
            Page<AccountStatement> pageTuts;
            pageTuts = accountStatementRepository.findAllByStateIsNot(State.DELETED, paging);
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
    @ApiOperation(value = "Liste des relevés de compte plat")
    public Map<String, Object> index() {
        try {
            return httpResponseApi.response(accountStatementRepository.findAllByStateNot(State.DELETED), HttpStatus.CREATED.value(), false, "Données disponibles");
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());

        }
    }

    @PostMapping("")
    @ApiOperation(value = "Ajouter relevés de compte")
    public Map<String, Object> store(@Valid @RequestBody AccountStatement accountStatement) {

        try {

            if (accountStatement.getAmount().compareTo(new BigDecimal("0")) < 0) {
                return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "un ou plusieurs champs incorrects.");
            }

            accountStatementRepository.save(accountStatement);
            String msg = "Données enregistrée avec succés";
            return httpResponseApi.response(accountStatement, HttpStatus.CREATED.value(), false, msg);
        } catch (Exception e) {
            e.getStackTrace();
            return httpResponseApi.response(accountStatement, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }

    }

    @GetMapping("{id}")
    @ApiOperation(value = "Voir details relevé de compte paginés")
    public Map<String, Object> show(@PathVariable() Integer id) {
        try {

            AccountStatement accountStatement = accountStatementRepository.findByIdAndStateNot(id, State.DELETED);
            if (accountStatement != null) {
                return httpResponseApi.response(accountStatement, HttpStatus.CREATED.value(), false, "Donnée disponible.");
            } else {
                return httpResponseApi.response(null, HttpStatus.NOT_FOUND.value(), true, "Cette région n'existe pas.");
            }
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }
    }

    @Transactional
    @PutMapping("")
    @ApiOperation(value = "Modifier  relevé de compte paginés")
    public Map<String, Object> updateRegion(@Valid @RequestBody AccountStatement accountStatement) {

        try {
            if (accountStatement.getAmount().compareTo(new BigDecimal("0")) > 0) {
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), true, "Paramétre envoyé invalide");
            } else {
                AccountStatement accountStatement1 = accountStatementRepository.findByIdAndStateNot(accountStatement.getId(), State.DELETED);
                if (accountStatement1 == null) {
                    return httpResponseApi.response(null, HttpStatus.CONFLICT.value(), true, "Cette région n'existe pas.");
                } else {
                    //todo set all value you want to update from accountStatement
                    accountStatement.setState(accountStatement.getState());
                    accountStatementRepository.save(accountStatement1);
                    return httpResponseApi.response(accountStatement, HttpStatus.CREATED.value(), false, "Données mises à jour avec succés");
                }
            }

        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), true, e.getMessage());
        }
    }


    /**
     * Desactiver etat a 0
     */
    @GetMapping("/state/{id}")
    @Transactional
    @ApiOperation(value = "Changer etat des relevés de compte paginés")
    public Map<String, Object> activeDesactiveEtat(@PathVariable Integer id) {
        String message = "";
        if (id == null) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "1 ou plusieurs paramètres manquants");
        }
        try {

            AccountStatement accountStatementExist = accountStatementRepository.findByIdAndStateNot(id, State.DELETED);
            if ((accountStatementExist) == null) {
                return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "Cet Objet n'existe pas.");
            }

            if (accountStatementExist.getState().equals(State.ACTIVED)) {
                accountStatementExist.setState(State.ACTIVED);
                message = "etat activé avec succéss";

            } else {
                accountStatementExist.setState(State.DISABLED);
                message = "etat desactivé avec succéss";
            }

            accountStatementRepository.save(accountStatementExist);
            return httpResponseApi.response(accountStatementExist, HttpStatus.CREATED.value(), false, message);
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }
    }

    /**
     * suppression
     * modification etat a 2
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "Supprimer les relevés de compte paginés")
    @Transactional
    public Map<String, Object> delete(@PathVariable Integer id) {
        try {
            AccountStatement accountStatement_ = accountStatementRepository.findByIdAndStateNot(id, State.DELETED);
            if (accountStatement_ == null) {
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), true, "Cette région n'existe pas");
            } else {
                AccountStatement accountStatement = new AccountStatement();
                accountStatement.setId(id);
                accountStatementRepository.delete(accountStatement_);
                accountStatement.setState(State.DELETED);
                accountStatementRepository.save(accountStatement);
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), false, "Données supprimé avec success");
            }
        } catch(Exception e) {
            throw new NotDeleteEntityException(null,HttpStatus.BAD_REQUEST.value(),true,"Cette entité est lié à d'autre(s), veuillez les supprimer d'abord.");
        }
    }

}
