package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.BaseEntity;

/**
 * Should be a builder for {@link BaseEntity} fields
 */
public abstract class Criteria<T extends BaseEntity> {

    private final Long whereId;
    private final String whereName;

    public Criteria(Long whereId, String whereName) {
        this.whereId = whereId;
        this.whereName = whereName;
    }

    public Long getWhereId() {
        return whereId;
    }

    public String getWhereName() {
        return whereName;
    }
}
