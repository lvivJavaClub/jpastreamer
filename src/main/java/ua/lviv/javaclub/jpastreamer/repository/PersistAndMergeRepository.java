package ua.lviv.javaclub.jpastreamer.repository;

import java.util.List;

public interface PersistAndMergeRepository<T> {
    <S extends T> S persist(S entity);
    <S extends T> List<S> persistAll(Iterable<S> entities);
    <S extends T> S merge(S entity);
    <S extends T> List<S> mergeAll(Iterable<S> entities);
}
