package dev.albertv.projects.api.mapper.util;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CollectionsMapper<E, D> {

    // to Entity

    List<E> toListEntity(Collection<D> dtos);

    Set<E> toSetEntity(Collection<D> dtos);

    // to DTO

    List<D> toListDTO(Collection<E> entities);

    Set<D> toSetDTO(Collection<E> entities);

}
