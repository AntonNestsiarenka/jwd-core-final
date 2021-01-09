package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;

/**
 * Should be a builder for {@link com.epam.jwd.core_final.domain.CrewMember} fields
 */
public class CrewMemberCriteria extends Criteria<CrewMember> {

    private final Role whereMemberRole;
    private final Rank whereMemberRank;
    private final Boolean whichReadyForNextMission;

    private CrewMemberCriteria(Long whereId, String whereName, Role whereMemberRole, Rank whereMemberRank,
                               Boolean whichReadyForNextMission) {
        super(whereId, whereName);
        this.whereMemberRole = whereMemberRole;
        this.whereMemberRank = whereMemberRank;
        this.whichReadyForNextMission = whichReadyForNextMission;
    }

    public Role getWhereMemberRole() {
        return whereMemberRole;
    }

    public Rank getWhereMemberRank() {
        return whereMemberRank;
    }

    public Boolean getWhichReadyForNextMission() {
        return whichReadyForNextMission;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long whereId;
        private String whereName;
        private Role whereMemberRole;
        private Rank whereMemberRank;
        private Boolean whichReadyForNextMission;

        public Builder whereId(Long id) {
            whereId = id;
            return this;
        }

        public Builder whereName(String name) {
            whereName = name;
            return this;
        }

        public Builder whereMemberRole(Role role) {
            whereMemberRole = role;
            return this;
        }

        public Builder whereMemberRank(Rank rank) {
            whereMemberRank = rank;
            return this;
        }

        public Builder whichReadyForNextMission(Boolean isReadyForNextMission) {
            whichReadyForNextMission = isReadyForNextMission;
            return this;
        }

        public CrewMemberCriteria build() {
            return new CrewMemberCriteria(whereId, whereName, whereMemberRole, whereMemberRank,
                    whichReadyForNextMission);
        }
    }
}
