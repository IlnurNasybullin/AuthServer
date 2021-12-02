package org.freestudy.authserver.model;

import java.io.Serializable;

public interface IEntity<ID> extends Serializable {

    ID getId();
    void setId(ID id);

}
