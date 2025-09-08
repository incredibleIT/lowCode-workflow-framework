package com.lowcode.workflow.common.utils;

import com.sun.scenario.effect.Merge;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MergeCollectionsBuilder {



    public static <T> MergeSourceBuilder<T> source(List<T> source) {
        return new MergeSourceBuilder<>(source);
    }

    public static <T, K> MergeSourceKeyBuilder<T, K> source(List<T> list, Function<T, K> function) {
        return new MergeSourceKeyBuilder<>(list, function);
    }


    /**
     * 合并两个集合
     */
    @AllArgsConstructor
    public static class Merger<T, R> {

        /**
         * 源集合
         */
        private final List<T> source;
        /**
         * 源集合映射函数
         */
        private final Function<T, ?> sourceKey;
        /**
         * 目标集合
         */
        private final List<R> target;
        /**
         * 目标集合映射函数
         */
        private final Function<R, ?> targetKey;

        /**
         * 源集合合并到目标集合
         * @param consumer 合并函数
         * @return 目标集合
         */
        public List<R> mergeStoT(BiConsumer<T, R> consumer) {
            if (source == null || source.isEmpty()) {
                return target;
            }

            target.stream()
                    .filter(Objects::nonNull)
                    .forEach(
                            t -> {
                                source.stream()
                                        .filter(Objects::nonNull)
                                        .filter(s -> Objects.equals(sourceKey.apply(s), targetKey.apply(t)))
                                        .forEach(
                                                s -> consumer.accept(s, t)
                                        );
                            }
                    );

            return target;
        }


        /**
         * 目标集合合并到源集合
         * @param consumer 合并函数
         * @return 源集合
         */
        public List<T> mergeTtoS(BiConsumer<T, R> consumer) {
            if (target != null) {
                mergeStoT(consumer);
            }
            return source;
        }
    }


    /**
     * 合并源构建器
     */
    @AllArgsConstructor
    public static class MergeSourceBuilder<T> {
        private final List<T> source;

        public <K> MergeSourceKeyBuilder<T, K> sourceKeyBuild(Function<T, K> function) {
            return new MergeSourceKeyBuilder<>(source, function);

        }
    }

    /**
     * 合并源映射构建器
     */
    @AllArgsConstructor
    public static class MergeSourceKeyBuilder<T, K> {
        private final List<T> source;
        private final Function<T, K> sourceKey;



        // 构建目标源, 直接传入target集合
        public <R> TargetSourceBuilder<T, K, R> target(List<R> target) {
            return new TargetSourceBuilder<>(source, sourceKey, target);
        }

        // 构建目标源,通过key列表获取target集合
        public <R> TargetSourceBuilder<T, K, R> target(Function<List<K>, List<R>> function) {
            List<K> keys = source.stream().map(sourceKey).distinct().collect(Collectors.toList());
            return new TargetSourceBuilder<>(source, sourceKey, function.apply(keys));
        }

        // 直接构建Merger, 有两种方式
        // 1. 根据Key构建target, 传入targetKey的映射
        public <R> Merger<T, R> target(Function<List<K>, List<R>> function, Function<R, ?> targetKey) {
            List<K> sourceKeys = source.stream().map(sourceKey).distinct().collect(Collectors.toList());
            return new Merger<>(source, sourceKey, function.apply(sourceKeys), targetKey);
        }

        // 2. 直接传入target集合, 传入targetKey的映射
        public <R> Merger<T, R> target(List<R> target , Function<R, ?> targetKey) {

            return new Merger<>(source, sourceKey, target, targetKey);

        }

    }


    /**
     * 合并目标构建器
     */
    @AllArgsConstructor
    public static class TargetSourceBuilder<T, K, R> {
        private final List<T> source;
        private final Function<T, K> sourceKey;
        private final List<R> target;

        public Merger<T, R> targetKey(Function<R, ?> targetKey) {
            return new Merger<>(source, sourceKey, target, targetKey);
        }
    }
}
