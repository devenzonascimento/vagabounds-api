package vagabounds.dtos.group;

import vagabounds.models.Group;

import java.util.List;

public record GroupDTO(
    Long id,
    String name,
    int membersCount
) {
    public static GroupDTO fromGroup(Group group) {
        if (group == null) {
            return null;
        }

        return new GroupDTO(
            group.getId(),
            group.getName(),
            group.getMemberships().size()
        );
    }

    public static List<GroupDTO> fromGroups(List<Group> groups) {
        return groups.stream().map(GroupDTO::fromGroup).toList();
    }
}
