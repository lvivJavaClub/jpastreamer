package ua.lviv.javaclub.jpastreamer.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PersistAndMergeRepositoryImpl<T> implements PersistAndMergeRepository<T> {
    @PersistenceContext
    private EntityManager em;

    @Override
    public <S extends T> S persist(final S entity) {
        em.persist(entity);
        return entity;
    }

    @Transactional
    @Override
    public <S extends T> List<S> persistAll(final Iterable<S> entities) {
        final List<S> result = new ArrayList<>();
        entities.forEach(e -> result.add(persist(e)));
        return result;
    }

    @Override
    public <S extends T> S merge(S entity) {
        return em.merge(entity);
    }

    @Transactional
    @Override
    public <S extends T> List<S> mergeAll(final Iterable<S> entities) {
        final List<S> result = new ArrayList<>();
        entities.forEach(e -> result.add(merge(e)));
        return result;
    }
}
