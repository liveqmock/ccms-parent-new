package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yunat.ccms.core.support.json.JsonDateDeserialize;
import com.yunat.ccms.core.support.json.JsonDateSerializer;

@Entity
@Table(name="tb_tc_affairs")
public class AffairDomain{

	/**主键 **/
    @Id
    @GeneratedValue
    private Long pkid;

    @Column(name = "dp_id")
    private String dpId;

    @Column(name = "tid")
    private String tid;

    @Column(name = "oid")
    private String oid;

    @Column(name = "customerno")
    private String customerno;

	@Column(name = "title")
    private String title;

    @Column(name = "important")
    private Integer important;

    @Column(name = "status")
    private Integer status;

    @Column(name = "founder")
    private String founder;

    @Column(name = "current_handler")
    private String currentHandler;

    @JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserialize.class)
    @Column(name = "expiration_time")
    private Date expirationTime;

    @Column(name = "note")
    private String note;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "source")
    private String source;

    @Column(name = "source_type_id")
    private Long sourceTypeId;

    @Column(name = "source_type")
    private String sourceType;

    @JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserialize.class)
    @Column(name = "created")
    private Date created;

    @JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserialize.class)
    @Column(name = "updated")
    private Date updated;

	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

    public String getDpId() {
        return dpId;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getCustomerno() {
		return customerno;
	}

	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getImportant() {
		return important;
	}

	public void setImportant(Integer important) {
		this.important = important;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFounder() {
		return founder;
	}

	public void setFounder(String founder) {
		this.founder = founder;
	}

	public String getCurrentHandler() {
		return currentHandler;
	}

	public void setCurrentHandler(String currentHandler) {
		this.currentHandler = currentHandler;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getSourceTypeId() {
		return sourceTypeId;
	}

	public void setSourceTypeId(Long sourceTypeId) {
		this.sourceTypeId = sourceTypeId;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

}
