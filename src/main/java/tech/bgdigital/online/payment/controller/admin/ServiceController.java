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

import tech.bgdigital.online.payment.models.entity.Partner;
import tech.bgdigital.online.payment.models.entity.Profil;
import tech.bgdigital.online.payment.models.entity.Service;
import tech.bgdigital.online.payment.models.enumeration.State;
import tech.bgdigital.online.payment.models.repository.ServiceRepository;
import tech.bgdigital.online.payment.services.http.response.HttpResponseApiInterface;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/service")
@Api(tags = "Admin service",description = ".")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ServiceController {
    final ServiceRepository serviceRepository;
    final
    HttpResponseApiInterface httpResponseApi;

    public ServiceController(ServiceRepository serviceRepository, HttpResponseApiInterface httpResponseApi) {
        this.serviceRepository = serviceRepository;
        this.httpResponseApi = httpResponseApi;
    }


    @GetMapping("/paginate")
    @ApiOperation(value = "Liste des services paginés")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<Service> listType = new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);
            Page<Service> pageTuts;
            pageTuts = serviceRepository.findAllByStateIsNot(State.DELETED, paging);
            listType = pageTuts.getContent();
            Map<String, Object> response = new HashMap<>();
            response = httpResponseApi.paginate(listType, pageTuts);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            return new ResponseEntity<>(httpResponseApi.response(null, 500, true,"Ok"), HttpStatus.OK);
        }
    }


    @PostMapping("")
    @ApiOperation(value = "Ajouter service")
    public Map<String, Object> store(@Valid @RequestBody Service service) {

        try {

         /*   if (accountStatement.getAmount().compareTo(new BigDecimal("0")) < 0) {
                return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "un ou plusieurs champs incorrects.");
            }*/
            service.setState("ACTIVED");
            serviceRepository.save(service);
            String msg = "Données enregistrée avec succés";
            return httpResponseApi.response(service, HttpStatus.CREATED.value(), false, msg);
        } catch (Exception e) {
            e.getStackTrace();
            return httpResponseApi.response(service, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }

    }

    @Transactional
    @PutMapping("")
    @ApiOperation(value = "Modifier  services paginés")
    public Map<String, Object> updateRegion(@Valid @RequestBody Service service) {

        try {
           /* if (accountStatement.getAmount().compareTo(new BigDecimal("0")) > 0) {
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), true, "Paramétre envoyé invalide");
            } else {*/
            Service service1 = serviceRepository.findByIdAndStateNot(service.getId(), State.DELETED);
            if (service1 == null) {
                return httpResponseApi.response(null, HttpStatus.CONFLICT.value(), true, "Ce service n'existe pas.");
            } else {
                //todo set all value you want to update from accountStatement
                service.setState(service.getState());
                serviceRepository.save(service1);
                return httpResponseApi.response(service, HttpStatus.CREATED.value(), false, "Données mises à jour avec succés");
            }
            //}

        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), true, e.getMessage());
        }
    }

    @GetMapping("")
    @ApiOperation(value = "Liste des services")
    public Map<String, Object> index() {
        try {
            return httpResponseApi.response(serviceRepository.findAllByStateNot(State.DELETED), HttpStatus.CREATED.value(), false, "Données disponibles");
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());

        }
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Voir details services paginés")
    public Map<String, Object> show(@PathVariable() Integer id) {
        try {

            Service service = serviceRepository.findByIdAndStateNot(id, State.DELETED);
            if (service != null) {
                return httpResponseApi.response(service, HttpStatus.CREATED.value(), false, "Donnée disponible.");
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
    @ApiOperation(value = "Changer etat des services paginés")
    public Map<String, Object> activeDesactiveEtat(@PathVariable Integer id) {
        String message = "";
        if (id == null) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "1 ou plusieurs paramètres manquants");
        }
        try {

            Service serviceExist = serviceRepository.findByIdAndStateNot(id, State.DELETED);
            if ((serviceExist) == null) {
                return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, "Cet Objet n'existe pas.");
            }

            if (serviceExist.getState().equals(State.ACTIVED)) {
                serviceExist.setState(State.DISABLED);
                message = "etat activé avec succéss";

            } else {
                serviceExist.setState(State.ACTIVED);
                message = "etat desactivé avec succéss";
            }

            serviceRepository.save(serviceExist);
            return httpResponseApi.response(serviceExist, HttpStatus.CREATED.value(), false, message);
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }
    }

    /**
     * suppression
     * modification state a DELETED
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "Supprimer services paginés")
    @Transactional
    public Map<String, Object> delete(@PathVariable Integer id) {
        try {

            Service service = serviceRepository.findByIdAndStateNot(id, State.DELETED);
            if (service == null) {
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), true, "Ce service n'existe pas");
            } else {
                serviceRepository.delete(service);
                return httpResponseApi.response(null, HttpStatus.NO_CONTENT.value(), false, "Données supprimées avec success");
            }
        } catch(Exception e) {
            throw new NotDeleteEntityException(null,HttpStatus.BAD_REQUEST.value(),true,"Cette entité est lié à d'autre(s), veuillez les supprimer d'abord.");
        }
    }

    @GetMapping("/search")
    @ApiOperation(value = "Voir details service recherché")
    public Map<String, Object> showRecherche(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name) {
        try {

            List<Service> service =  serviceRepository.findProfilsByNameOrCode(name, code);
            if (service != null) {
                return httpResponseApi.response(service, HttpStatus.CREATED.value(), false, "Donnée disponible.");
            } else {
                return httpResponseApi.response(null, HttpStatus.NOT_FOUND.value(), true, "Cet service n'existe pas.");
            }
        } catch (Exception e) {
            return httpResponseApi.response(null, HttpStatus.BAD_REQUEST.value(), true, e.getMessage());
        }
    }
}
