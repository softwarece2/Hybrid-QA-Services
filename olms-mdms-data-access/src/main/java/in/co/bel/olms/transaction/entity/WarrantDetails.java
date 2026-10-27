package in.co.bel.olms.transaction.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.SerialNoStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Warrant Details entity class
 *
 * @author Shridhar R
 * @version 1.0
 * @since 20-01-2026
 */

@Entity
@Table(name = "warrant_details", schema = "tdms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WarrantDetails {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY  )
@Column(name = "id", unique = true, nullable = false)
private Integer id;

@CreationTimestamp
private Date createdDate;

@JsonIgnore
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "created_by")
private Employee createdBy;

@JsonIgnore
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "modified_by")
private Employee modifiedBy;

private Date modifiedDate;

@JsonIgnore
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "category_id")
private Category category;

@JsonIgnore
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "line_item_serial_no_id")
private LineItemSerialNo lineItemSerialNo; 

@JsonIgnore
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "ser_no_sts_id")
private SerialNoStatus serialNoStatus; 
}
