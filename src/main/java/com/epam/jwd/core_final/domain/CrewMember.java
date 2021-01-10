package com.epam.jwd.core_final.domain;

import java.util.Objects;

/**
 * Expected fields:
 * <p>
 * role {@link Role} - member role
 * rank {@link Rank} - member rank
 * isReadyForNextMissions {@link Boolean} - true by default. Set to false, after first failed mission
 */
public class CrewMember extends AbstractBaseEntity {
    // todo
    private static Long idUnique = 0L;

    private final Role memberRole;
    private Rank memberRank;
    private Boolean isReadyForNextMissions;

    public CrewMember(String name, Role memberRole, Rank memberRank) {
        super(idUnique++, name);
        this.memberRole = memberRole;
        this.memberRank = memberRank;
        this.isReadyForNextMissions = true;
    }

    public Role getMemberRole() {
        return memberRole;
    }

    public Rank getMemberRank() {
        return memberRank;
    }

    public void setMemberRank(Rank memberRank) {
        this.memberRank = memberRank;
    }

    public Boolean getReadyForNextMissions() {
        return isReadyForNextMissions;
    }

    public void setReadyForNextMissions(Boolean readyForNextMissions) {
        isReadyForNextMissions = readyForNextMissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrewMember that = (CrewMember) o;
        return getId().equals(that.getId()) &&
                getName().equals(that.getName()) &&
                memberRole == that.memberRole &&
                memberRank == that.memberRank &&
                Objects.equals(isReadyForNextMissions, that.isReadyForNextMissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), memberRole, memberRank, isReadyForNextMissions);
    }

    @Override
    public String toString() {
        return "CrewMember{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", memberRole=" + memberRole +
                ", memberRank=" + memberRank +
                ", isReadyForNextMissions=" + isReadyForNextMissions +
                '}';
    }
}
