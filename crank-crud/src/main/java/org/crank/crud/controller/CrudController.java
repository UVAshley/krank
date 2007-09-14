package org.crank.crud.controller;

import java.io.Serializable;
import java.util.List;
import org.crank.annotations.design.ExpectsInjection;

/**
 * Controls CRUD operations from an application.
 * @author Rick
 *
 * @param <T> Entity type
 * @param <PK> Entity primary key
 * @see CrudOperations
 * @see Toggleable
 */
public class CrudController<T, PK extends Serializable> extends CrudControllerBase<T, PK>  {
    public CrudController () {
        
    }
    
    /**
     * Update the entity in the data store. 
     * Notify listeners that the entity was updated.
     * @see CrudOperations#update()
     * @return outcome
     */
    @SuppressWarnings("unchecked")
    public CrudOutcome doUpdate() {
        dao.update((T)entity);
        state = CrudState.UNKNOWN; 
        fireToggle();
        return CrudOutcome.LISTING;
    }
    
    /**
     * Creates a new instance of the entity class and sets the entity to this new instance.
     * @see CrudOperations#loadCreate()
     */
    @SuppressWarnings("unchecked")
    public CrudOutcome doLoadCreate() {
        init();
        createEntity();
        this.state = CrudState.ADD;
        return CrudOutcome.FORM;
    }

    /**
     * Create a new instance in the database.
     * Notify listeners that the entity was created.
     * @see CrudOperations#create()
     * @retrun outcome
     */
    @SuppressWarnings("unchecked")
    public CrudOutcome doCreate() {
        dao.create((T)entity);
        this.state = CrudState.UNKNOWN;
        fireToggle();
        return CrudOutcome.LISTING;
    }

    /**
     * Delete the entity from the data store.
     * Notify listeners that the model changed.
     * @see CrudOperations#delete()
     * @retrun outcome
     */
    @SuppressWarnings("unchecked")    
    public CrudOutcome doDelete() {
        doDelete((T)getCurrentEntity());        
        fireToggle();
        return CrudOutcome.LISTING;
    }

    @SuppressWarnings("unchecked")
    private void doDelete(Object entity) {
        dao.delete( (PK) propertyUtil.getPropertyValue( idPropertyName, entity ));
    }
    
    public CrudOutcome deleteSelected() {
        List listToDelete = this.getSelectedEntities();
        /* You could change this to delete a list of ids. */
        for (Object entity : listToDelete) {
            doDelete( entity );
        }
        fireToggle();        
        return CrudOutcome.LISTING;        
    }

    /**
     * Read the entity from the data store. If the readPopulated flag is set, read 
     * the entity fully populated. You need to read the entity fully populated for 
     * master detail forms and such.
     * @see CrudOperations#read()
     * @retrun outcome
     */
    @SuppressWarnings("unchecked")
    public CrudOutcome doRead() {
        init();
        PK id = null;
        
        String sId = this.retrieveId();
        if (sId==null) {
        	entity = (T)getCurrentEntity();
        	id = (PK) propertyUtil.getPropertyValue( idPropertyName, entity );
        } else {
        	id = (PK) Long.valueOf(sId);
        }
        state = CrudState.EDIT; 
        if (readPopulated) {
            entity = dao.readPopulated( id );
        }
        return CrudOutcome.FORM;
    }

    private Object getCurrentEntity() {
        return entityLocator.getEntity();
    }

    private List getSelectedEntities() {
        return entityLocator.getSelectedEntities();
    }

    @ExpectsInjection
    public void setEntityLocator( EntityLocator entityLocator ) {
        this.entityLocator = entityLocator;
    }

    public CrudOutcome doCancel() {
        state = CrudState.UNKNOWN;
        cancelChildren();
        return CrudOutcome.LISTING;
    }
    
    
}