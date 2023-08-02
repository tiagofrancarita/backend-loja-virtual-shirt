package br.com.franca.ShirtVirtual.service;

import br.com.franca.ShirtVirtual.model.AccessTokenJunoAPI;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

public class AccessTokenJunoService{

    @PersistenceContext
    private EntityManager entityManager;

    public AccessTokenJunoAPI buscaTokenAtivoJuno() {

        try {

            AccessTokenJunoAPI accessTokenJunoAPI =
                    (AccessTokenJunoAPI) entityManager
                            .createQuery("select a from AccessTokenJunoAPI a ")
                            .setMaxResults(1).getSingleResult();

            return accessTokenJunoAPI;
        }catch (NoResultException e) {
            return null;
        }


    }

}
