/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory.beans;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "client_type")
@NamedQueries({
    @NamedQuery(name = "ClientType.findAll", query = "SELECT c FROM ClientType c"),
    @NamedQuery(name = "ClientType.findByClientTypeId", query = "SELECT c FROM ClientType c WHERE c.clientTypeId = :clientTypeId"),
    @NamedQuery(name = "ClientType.findByClientTitle", query = "SELECT c FROM ClientType c WHERE c.clientTitle = :clientTitle")})
public class ClientType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "clientTypeId")
    private String clientTypeId;
    @Column(name = "clientTitle")
    private String clientTitle;
    @OneToMany(mappedBy = "clientType")
    private List<Client> clientList;

    public ClientType() {
    }

    public ClientType(String clientTypeId) {
        this.clientTypeId = clientTypeId;
    }

    public String getClientTypeId() {
        return clientTypeId;
    }

    public void setClientTypeId(String clientTypeId) {
        this.clientTypeId = clientTypeId;
    }

    public String getClientTitle() {
        return clientTitle;
    }

    public void setClientTitle(String clientTitle) {
        this.clientTitle = clientTitle;
    }

    public List<Client> getClientList() {
        return clientList;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientTypeId != null ? clientTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientType)) {
            return false;
        }
        ClientType other = (ClientType) object;
        if ((this.clientTypeId == null && other.clientTypeId != null) || (this.clientTypeId != null && !this.clientTypeId.equals(other.clientTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "inventory.beans.ClientType[clientTypeId=" + clientTypeId + "]";
    }

}
