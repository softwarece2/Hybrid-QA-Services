package in.co.bel.olms.transaction.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "cqai_inote_number", schema = "tdms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CqaiInoteNo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Integer id;

  @Column(unique = true)
  private String iNoteNo;

  private Integer counter;

  @CreationTimestamp
  private Date createdAt;

}