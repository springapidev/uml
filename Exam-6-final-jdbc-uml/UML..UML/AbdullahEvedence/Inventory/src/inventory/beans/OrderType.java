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
@Table(name = "order_type")
@NamedQueries({
    @NamedQuery(name = "OrderType.findAll", query = "SELECT o FROM OrderType o"),
    @NamedQuery(name = "OrderType.findByOrderTypeId", query = "SELECT o FROM OrderType o WHERE o.orderTypeId = :orderTypeId"),
    @NamedQuery(name = "OrderType.findByOrderTitle", query = "SELECT o FROM OrderType o WHERE o.orderTitle = :orderTitle")})
public class OrderType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "orderTypeId")
    private String orderTypeId;
    @Column(name = "orderTitle")
    private String orderTitle;
    @OneToMany(mappedBy = "orderType")
    private List<Oreder> orederList;

    public OrderType() {
    }

    public OrderType(String orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public String getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(String orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public List<Oreder> getOrederList() {
        return orederList;
    }

    public void setOrederList(List<Oreder> orederList) {
        this.orederList = orederList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderTypeId != null ? orderTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrderType)) {
            return false;
        }
        OrderType other = (OrderType) object;
        if ((this.orderTypeId == null && other.orderTypeId != null) || (this.orderTypeId != null && !this.orderTypeId.equals(other.orderTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "inventory.beans.OrderType[orderTypeId=" + orderTypeId + "]";
    }

}
