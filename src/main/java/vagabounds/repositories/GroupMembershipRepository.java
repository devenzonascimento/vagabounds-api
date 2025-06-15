package vagabounds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vagabounds.models.GroupMembership;
import vagabounds.models.GroupMembershipId;

public interface GroupMembershipRepository extends JpaRepository<GroupMembership, GroupMembershipId> {
}
