/**
 * $Id: PEntityConditon.java,v 1.2 2013/11/16 04:57:48 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset.model.impl;

import java.util.ArrayList;
import java.util.List;


/**
 * @Title: PEntityConditon.java
 * @Description: 持久化实体的条件值
 * @Company: CSI
 * @Created on 2011-4-12 上午10:26:12
 * @author ganjp
 * @version $Revision: 1.2 $
 * @since 4.3
 */
public class PEntityConditon {
    
    private List srcRelationIds;
    private List tgtRelationIds;
    private String one2oneTgtRelation;
    
    public List getSrcRelationIds() {
        return srcRelationIds;
    }
    
    public void setSrcRelationIds(List srcRelationIds) {
        this.srcRelationIds = srcRelationIds;
    }
    
    public List getTgtRelationIds() {
        return tgtRelationIds;
    }
    
    public void setTgtRelationIds(List tgtRelationIds) {
        this.tgtRelationIds = tgtRelationIds;
    }
    
    public void addSrcRelationIds(List srcRelationIds) {
        if (this.srcRelationIds==null) {
            this.srcRelationIds = new ArrayList();
        }
        this.srcRelationIds.addAll(srcRelationIds);
    }

    public void addSrcRelationId(String srcRelationId) {
        if (this.srcRelationIds==null) {
            this.srcRelationIds = new ArrayList();
        }
        this.srcRelationIds.add(srcRelationId);
    }
    
    public void addTgtRelationIds(List tgtRelationIds) {
        if (this.tgtRelationIds==null) {
            this.tgtRelationIds = new ArrayList();
        }
        this.tgtRelationIds.addAll(tgtRelationIds);
    }

    public void addTgtRelationId(String tgtRelationId) {
        if (this.tgtRelationIds==null) {
            this.tgtRelationIds = new ArrayList();
        }
        this.tgtRelationIds.add(tgtRelationId);
    }
    
    public String getOne2oneTgtRelation() {
        return one2oneTgtRelation;
    }

    public void setOne2oneTgtRelation(String one2oneTgtRelation) {
        this.one2oneTgtRelation = one2oneTgtRelation;
    }
    
    public void clear() {
        this.tgtRelationIds = null;
        this.srcRelationIds = null;
        this.one2oneTgtRelation = null;
    }
    
}
